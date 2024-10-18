/*
 * Copyright 2022 Splendo Consulting B.V. The Netherlands
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.splendo.kaluga.bluetooth.device

import com.splendo.kaluga.base.state.HotStateFlowRepo
import com.splendo.kaluga.base.state.StateRepo
import com.splendo.kaluga.base.utils.getCompletedOrNull
import com.splendo.kaluga.bluetooth.RSSI
import com.splendo.kaluga.logging.debug
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * A Mutable [StateRepo] of [ConnectableDeviceState]
 */
typealias ConnectableDeviceStateFlowRepo = StateRepo<ConnectableDeviceState, MutableStateFlow<ConnectableDeviceState>>

/**
 * A Bluetooth device that can be connected to
 */
interface Device {

    /**
     * The [Identifier] of the device
     */
    val identifier: Identifier

    /**
     * A [Flow] of the latest [DeviceInfo] of the device
     */
    val info: Flow<DeviceInfo>

    /**
     * A [Flow] of the latest [DeviceState] of the device
     */
    val state: Flow<DeviceState>

    /**
     * Attempts to connect to the device
     * @param reconnectionSettings the [ConnectionSettings.ReconnectionSettings] to use when reconnecting if the device disconnects unexpectedly
     * @return `true` if connection was successful.
     */
    suspend fun connect(reconnectionSettings: ConnectionSettings.ReconnectionSettings? = null): Boolean {
        var hasStartedConnecting = false
        return state.transform { deviceState ->
            when (deviceState) {
                is ConnectableDeviceState.Disconnected -> if (!hasStartedConnecting) {
                    deviceState.startConnecting(reconnectionSettings)
                    hasStartedConnecting = true
                } else {
                    emit(false)
                }
                is ConnectableDeviceState.Connected -> emit(true)
                is ConnectableDeviceState.Connecting, is ConnectableDeviceState.Disconnecting -> {}
                is NotConnectableDeviceState -> emit(false)
            }
        }.first()
    }

    /**
     * Notifies the device that is has connected
     */
    fun handleConnected()

    /**
     * Attempts to disconnect from the device
     */
    suspend fun disconnect() {
        state.transformLatest { deviceState ->
            when (deviceState) {
                is ConnectableDeviceState.Connected -> deviceState.startDisconnected()
                is ConnectableDeviceState.Connecting -> deviceState.handleCancel()
                is ConnectableDeviceState.Disconnected -> emit(Unit)
                is ConnectableDeviceState.Disconnecting -> {} // just wait
                is NotConnectableDeviceState -> emit(Unit)
            }
        }.first()
    }

    /**
     * Notifies the device that is has disconnected
     */
    fun handleDisconnected()

    /**
     * Notifies the device that the [RSSI] has updated
     * @param rssi the new [RSSI] value
     */
    fun rssiDidUpdate(rssi: RSSI)

    /**
     * Notifies the device that the [BaseAdvertisementData] has updated
     * @param advertisementData the new [BaseAdvertisementData]
     */
    fun advertisementDataDidUpdate(advertisementData: BaseAdvertisementData)
}

/**
 * Implementation of [Device]
 * @param identifier The [Identifier] of the device
 * @param initialDeviceInfo the initial [DeviceInfoImpl] known about the device
 * @param connectionSettings the [ConnectionSettings] to apply to the [DeviceConnectionManager] associated with this device
 * @param connectionManagerBuilder creates a [DeviceConnectionManager] to manage connecting this device
 * @param coroutineScope the [CoroutineScope] this device is running on]
 * @param createDeviceStateFlow creates a [ConnectableDeviceStateFlowRepo] to manage the device connection state
 */
class DeviceImpl(
    override val identifier: Identifier,
    initialDeviceInfo: DeviceInfoImpl,
    private val connectionSettings: ConnectionSettings,
    private val connectionManagerBuilder: (ConnectionSettings) -> DeviceConnectionManager,
    private val coroutineScope: CoroutineScope,
    private val createDeviceStateFlow: (DeviceConnectionManager, CoroutineContext) -> ConnectableDeviceStateFlowRepo = { connectionManager, context ->
        ConnectableDeviceStateImplRepo(connectionSettings.reconnectionSettings, connectionManager, context)
    },
) : Device,
    CoroutineScope by coroutineScope {

    companion object {
        const val TAG = "DeviceImp"
    }

    private val connectionManager = CompletableDeferred<DeviceConnectionManager>()
    private val sharedInfo = MutableStateFlow(initialDeviceInfo)
    private val deviceStateRepo = MutableStateFlow<ConnectableDeviceStateFlowRepo?>(null)
    private val isConnectable = sharedInfo
        .map { it.advertisementData.isConnectable }
        .runningFold(
            initial = initialDeviceInfo.advertisementData.isConnectable,
            // Once device is connectable we keep that state
            operation = Boolean::or,
        )
    override val info: Flow<DeviceInfo> = sharedInfo.asStateFlow()
    override val state: Flow<DeviceState> = combine(
        isConnectable,
        deviceStateRepo,
    ) { isConnectable, repo ->
        when {
            !isConnectable -> null
            repo != null -> repo
            else -> createDeviceStateRepoIfNotCreated()
        }
    }.distinctUntilChanged().flatMapLatest {
        it ?: flowOf(NotConnectableDeviceStateImpl)
    }

    init {
        launch {
            isConnectable.first { it }
            createConnectionManagerIfNotCreated()
        }
        launch {
            isConnectable
                .distinctUntilChanged()
                .filterNot { it }
                .collect {
                    connectionManager.getCompletedOrNull()?.disconnect()
                    deviceStateRepo.value = null
                }
        }
        launch {
            connectionManager.await().rssi.collect(::rssiDidUpdate)
        }
        launch {
            connectionManager.await().events.collect { event ->
                val repo = when (event) {
                    is DeviceConnectionManager.Event.Connecting,
                    is DeviceConnectionManager.Event.Connected,
                    -> createDeviceStateRepoIfNotCreated()
                    is DeviceConnectionManager.Event.CancelledConnecting,
                    is DeviceConnectionManager.Event.Discovering,
                    is DeviceConnectionManager.Event.DiscoveredServices,
                    is DeviceConnectionManager.Event.AddAction,
                    is DeviceConnectionManager.Event.CompletedAction,
                    is DeviceConnectionManager.Event.Disconnecting,
                    is DeviceConnectionManager.Event.Disconnected,
                    is DeviceConnectionManager.Event.MtuUpdated,
                    -> deviceStateRepo.value
                }
                repo?.takeAndChangeState { state ->
                    event.stateTransition(state)
                }
            }
        }
    }

    override fun handleConnected() = createConnectionManagerIfNotCreated().handleConnect()

    override fun handleDisconnected() = createConnectionManagerIfNotCreated().handleDisconnect()

    override fun rssiDidUpdate(rssi: RSSI) {
        sharedInfo.value = sharedInfo.value.copy(rssi = rssi)
    }

    override fun advertisementDataDidUpdate(advertisementData: BaseAdvertisementData) {
        sharedInfo.value = sharedInfo.value.copy(advertisementData = advertisementData)
    }

    private fun createConnectionManagerIfNotCreated(): DeviceConnectionManager = if (connectionManager.isCompleted) {
        connectionManager.getCompleted()
    } else {
        connectionManagerBuilder(connectionSettings).also {
            connectionManager.complete(it)
        }
    }

    private fun createDeviceStateRepoIfNotCreated(): ConnectableDeviceStateFlowRepo? = deviceStateRepo.updateAndGet { repo ->
        repo ?: if (sharedInfo.value.advertisementData.isConnectable) {
            createDeviceStateFlow(
                createConnectionManagerIfNotCreated(),
                coroutineScope.coroutineContext,
            )
        } else {
            null
        }
    }

    private suspend fun DeviceConnectionManager.Event.stateTransition(state: ConnectableDeviceState): suspend () -> ConnectableDeviceState = when (this) {
        is DeviceConnectionManager.Event.Connecting -> stateTransition(state)
        is DeviceConnectionManager.Event.CancelledConnecting -> stateTransition(state)
        is DeviceConnectionManager.Event.Connected -> stateTransition(state)
        is DeviceConnectionManager.Event.Disconnecting -> stateTransition(state)
        is DeviceConnectionManager.Event.Disconnected -> stateTransition(state)
        is DeviceConnectionManager.Event.Discovering -> stateTransition(state)
        is DeviceConnectionManager.Event.DiscoveredServices -> stateTransition(state)
        is DeviceConnectionManager.Event.AddAction -> stateTransition(state)
        is DeviceConnectionManager.Event.CompletedAction -> stateTransition(state)
        is DeviceConnectionManager.Event.MtuUpdated -> stateTransition(state)
    }

    private fun DeviceConnectionManager.Event.Connecting.stateTransition(state: ConnectableDeviceState) =
        if (state is ConnectableDeviceState.Disconnected) state.connect(reconnectionSettings) else state.remain()

    private fun DeviceConnectionManager.Event.CancelledConnecting.stateTransition(state: ConnectableDeviceState) = when (state) {
        is ConnectableDeviceState.Connecting -> state.cancelConnection
        else -> state.remain()
    }

    private suspend fun DeviceConnectionManager.Event.Connected.stateTransition(state: ConnectableDeviceState) = when (state) {
        is ConnectableDeviceState.Connecting -> state.didConnect
        is ConnectableDeviceState.Connected -> state.remain()
        else -> {
            connectionManager.getCompletedOrNull()?.reset()
            state.remain()
        }
    }

    private fun DeviceConnectionManager.Event.Disconnecting.stateTransition(state: ConnectableDeviceState) =
        if (state is ConnectableDeviceState.Connected) state.disconnecting else state.remain()

    private suspend fun DeviceConnectionManager.Event.Disconnected.stateTransition(state: ConnectableDeviceState) = when (state) {
        is ConnectableDeviceState.Connected -> when (state.reconnectionSettings) {
            is ConnectionSettings.ReconnectionSettings.Always -> state.reconnect
            is ConnectionSettings.ReconnectionSettings.Never -> {
                onDisconnect()
                state.didDisconnect
            }
        }
        is ConnectableDeviceState.Disconnected -> state.remain()
        is ConnectableDeviceState.Connecting,
        is ConnectableDeviceState.Disconnecting,
        -> {
            onDisconnect()
            state.didDisconnect
        }
    }

    private fun DeviceConnectionManager.Event.Discovering.stateTransition(state: ConnectableDeviceState) =
        if (state is ConnectableDeviceState.Connected.NoServices) state.discoverServices else state.remain()

    private fun DeviceConnectionManager.Event.DiscoveredServices.stateTransition(state: ConnectableDeviceState) =
        if (state is ConnectableDeviceState.Connected.Discovering) state.didDiscoverServices(services) else state.remain()

    private fun DeviceConnectionManager.Event.AddAction.stateTransition(state: ConnectableDeviceState) = when (state) {
        is ConnectableDeviceState.Connected.Idle -> {
            state.handleAction(action)
        }
        is ConnectableDeviceState.Connected.HandlingAction -> {
            state.addAction(action)
        }
        is ConnectableDeviceState.Connected.NoServices,
        is ConnectableDeviceState.Connected.Discovering,
        is ConnectableDeviceState.Connecting,
        is ConnectableDeviceState.Disconnected,
        is ConnectableDeviceState.Disconnecting,
        -> {
            state.remain() // TODO consider an optional buffer
        }
    }

    private fun DeviceConnectionManager.Event.CompletedAction.stateTransition(state: ConnectableDeviceState) =
        if (state is ConnectableDeviceState.Connected.HandlingAction && state.action === action) {
            state.action.completedSuccessfully.complete(succeeded)
            debug(TAG) { "Action $action has been succeeded: $succeeded" }
            state.actionCompleted
        } else {
            state.remain()
        }

    private fun DeviceConnectionManager.Event.MtuUpdated.stateTransition(state: ConnectableDeviceState) = if (state is ConnectableDeviceState.Connected) {
        state.didUpdateMtu(newMtu)
    } else {
        state.remain()
    }
}

/**
 * A [HotStateFlowRepo] to manage the [ConnectableDeviceState] of a [Device]
 * @param initialState gets the initial [ConnectableDeviceState] when observation starts
 * @param coroutineContext the [CoroutineContext] of this repo
 */
abstract class BaseConnectableDeviceStateRepo(initialState: () -> ConnectableDeviceState, coroutineContext: CoroutineContext) :
    HotStateFlowRepo<ConnectableDeviceState>(
        coroutineContext = coroutineContext,
        initialState = { initialState() },
    )

/**
 * A [BaseConnectableDeviceStateRepo] managed by a [DeviceConnectionManager]
 * @param defaultReconnectionSettings the default [ConnectionSettings.ReconnectionSettings] to use when reconnecting after the device disconnects unexpectedly
 * @param connectionManager the [DeviceConnectionManager] to manage the [ConnectableDeviceState]
 * @param coroutineContext the [CoroutineContext] of this repo
 */
class ConnectableDeviceStateImplRepo(
    defaultReconnectionSettings: ConnectionSettings.ReconnectionSettings,
    connectionManager: DeviceConnectionManager,
    coroutineContext: CoroutineContext,
) : BaseConnectableDeviceStateRepo(
    initialState = {
        when (connectionManager.getCurrentState()) {
            DeviceConnectionManager.State.CONNECTED -> ConnectableDeviceStateImpl.Connected.NoServices(
                defaultReconnectionSettings,
                null,
                connectionManager,
            )
            DeviceConnectionManager.State.CONNECTING -> ConnectableDeviceStateImpl.Connecting(
                defaultReconnectionSettings,
                connectionManager,
            )
            DeviceConnectionManager.State.DISCONNECTED -> ConnectableDeviceStateImpl.Disconnected(
                connectionManager,
            )
            DeviceConnectionManager.State.DISCONNECTING -> ConnectableDeviceStateImpl.Disconnecting(
                connectionManager,
            )
        }
    },
    coroutineContext = coroutineContext,
)
