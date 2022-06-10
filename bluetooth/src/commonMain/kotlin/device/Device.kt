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

import com.splendo.kaluga.base.utils.getCompletedOrNull
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.state.HotStateFlowRepo
import com.splendo.kaluga.state.StateRepo
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

typealias ConnectableDeviceStateFlowRepo = StateRepo<ConnectableDeviceState, MutableStateFlow<ConnectableDeviceState>>

interface Device {
    val identifier: Identifier
    val info: Flow<DeviceInfo>
    val state: Flow<DeviceState>

    suspend fun connect(): Boolean
    fun handleConnected()
    suspend fun disconnect()
    suspend fun handleDisconnected()
    fun rssiDidUpdate(rssi: Int)
    fun advertisementDataAndRssiDidUpdate(advertisementData: BaseAdvertisementData, rssi: Int)
}

class DeviceImpl(
    override val identifier: Identifier,
    initialDeviceInfo: DeviceInfoImpl,
    private val connectionSettings: ConnectionSettings,
    private val connectionManagerBuilder: (ConnectionSettings) -> DeviceConnectionManager,
    private val coroutineScope: CoroutineScope,
    private val createDeviceStateFlow: (DeviceConnectionManager, CoroutineContext) -> ConnectableDeviceStateFlowRepo = ::ConnectableDeviceStateImplRepo
) : Device, CoroutineScope by coroutineScope {

    companion object {
        const val TAG = "DeviceImp"
    }

    private val connectionManager = CompletableDeferred<DeviceConnectionManager>()
    private val sharedInfo = MutableStateFlow(initialDeviceInfo)
    private val deviceStateRepo = MutableStateFlow<ConnectableDeviceStateFlowRepo?>(null)
    override val info: Flow<DeviceInfo> = sharedInfo.asStateFlow()
    override val state: Flow<DeviceState> = combine(
        sharedInfo.map { it.advertisementData.isConnectable },
        deviceStateRepo
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
            sharedInfo.map { it.advertisementData.isConnectable }.first { it }
            createConnectionManagerIfNotCreated()
        }
        launch {
            sharedInfo.map { it.advertisementData.isConnectable }.distinctUntilChanged()
                .filterNot { it }.collect {
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
                    is DeviceConnectionManager.Event.Connected -> createDeviceStateRepoIfNotCreated()
                    is DeviceConnectionManager.Event.CancelledConnecting,
                    is DeviceConnectionManager.Event.Discovering,
                    is DeviceConnectionManager.Event.DiscoveredServices,
                    is DeviceConnectionManager.Event.AddAction,
                    is DeviceConnectionManager.Event.CompletedAction,
                    is DeviceConnectionManager.Event.Disconnecting,
                    is DeviceConnectionManager.Event.Disconnected,
                    is DeviceConnectionManager.Event.MtuUpdated -> deviceStateRepo.value
                }
                repo?.takeAndChangeState { state ->
                    event.stateTransition(state)
                }
            }
        }
    }

    override suspend fun connect(): Boolean = createDeviceStateRepoIfNotCreated()?.let { repo ->
        repo.transformLatest { deviceState ->
            when (deviceState) {
                is ConnectableDeviceState.Disconnected -> deviceState.startConnecting()
                is ConnectableDeviceState.Connected -> emit(true)
                is ConnectableDeviceState.Connecting, is ConnectableDeviceState.Reconnecting, is ConnectableDeviceState.Disconnecting -> {}
            }
        }.first()
    } ?: false

    override fun handleConnected() = createConnectionManagerIfNotCreated().handleConnect()

    override suspend fun disconnect() {
        deviceStateRepo.value?.transformLatest { deviceState ->
            when (deviceState) {
                is ConnectableDeviceState.Connected -> deviceState.startDisconnected()
                is ConnectableDeviceState.Connecting -> deviceState.handleCancel()
                is ConnectableDeviceState.Reconnecting -> deviceState.handleCancel()
                is ConnectableDeviceState.Disconnected -> emit(Unit)
                is ConnectableDeviceState.Disconnecting -> {} // just wait
            }
        }?.first()
    }

    override suspend fun handleDisconnected() = createConnectionManagerIfNotCreated().handleDisconnect()

    override fun rssiDidUpdate(rssi: Int) {
        sharedInfo.value = sharedInfo.value.copy(rssi = rssi)
    }

    override fun advertisementDataAndRssiDidUpdate(
        advertisementData: BaseAdvertisementData,
        rssi: Int
    ) {
        sharedInfo.value = sharedInfo.value.copy(rssi = rssi, advertisementData = advertisementData)
    }

    private fun createConnectionManagerIfNotCreated(): DeviceConnectionManager = if (connectionManager.isCompleted) {
        connectionManager.getCompleted()
    } else {
        connectionManagerBuilder(connectionSettings).also {
            connectionManager.complete(it)
        }
    }

    private fun createDeviceStateRepoIfNotCreated(): ConnectableDeviceStateFlowRepo? =
        deviceStateRepo.updateAndGet { repo ->
            repo ?: if (sharedInfo.value.advertisementData.isConnectable) createDeviceStateFlow(
                createConnectionManagerIfNotCreated(),
                coroutineScope.coroutineContext
            ) else null
        }

    private suspend fun DeviceConnectionManager.Event.stateTransition(state: ConnectableDeviceState): suspend () -> ConnectableDeviceState =
        when (this) {
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
        if (state is ConnectableDeviceState.Disconnected)
            state.connect
        else
            state.remain()

    private fun DeviceConnectionManager.Event.CancelledConnecting.stateTransition(state: ConnectableDeviceState) =
        when (state) {
            is ConnectableDeviceState.Connecting -> state.cancelConnection
            is ConnectableDeviceState.Reconnecting -> state.cancelConnection
            else -> state.remain()
        }

    private suspend fun DeviceConnectionManager.Event.Connected.stateTransition(state: ConnectableDeviceState) =
        when (state) {
            is ConnectableDeviceState.Connecting -> state.didConnect
            is ConnectableDeviceState.Reconnecting -> state.didConnect
            is ConnectableDeviceState.Connected -> state.remain()
            else -> {
                connectionManager.getCompletedOrNull()?.reset()
                state.remain()
            }
        }

    private fun DeviceConnectionManager.Event.Disconnecting.stateTransition(state: ConnectableDeviceState) =
        if (state is ConnectableDeviceState.Connected)
            state.disconnecting
        else
            state.remain()

    private suspend fun DeviceConnectionManager.Event.Disconnected.stateTransition(state: ConnectableDeviceState) =
        when (state) {
            is ConnectableDeviceState.Reconnecting -> {
                state.retry(connectionSettings.reconnectionSettings).also {
                    if (it == state.didDisconnect) {
                        onDisconnect()
                    }
                }
            }
            is ConnectableDeviceState.Connected -> when (connectionSettings.reconnectionSettings) {
                is ConnectionSettings.ReconnectionSettings.Always,
                is ConnectionSettings.ReconnectionSettings.Limited -> state.reconnect
                is ConnectionSettings.ReconnectionSettings.Never -> {
                    onDisconnect()
                    state.didDisconnect
                }
            }
            is ConnectableDeviceState.Disconnected -> state.remain()
            is ConnectableDeviceState.Connecting,
            is ConnectableDeviceState.Disconnecting -> {
                onDisconnect()
                state.didDisconnect
            }
        }

    private fun DeviceConnectionManager.Event.Discovering.stateTransition(state: ConnectableDeviceState) =
        if (state is ConnectableDeviceState.Connected.NoServices)
            state.discoverServices
        else
            state.remain()

    private fun DeviceConnectionManager.Event.DiscoveredServices.stateTransition(state: ConnectableDeviceState) =
        if (state is ConnectableDeviceState.Connected.Discovering)
            state.didDiscoverServices(services)
        else
            state.remain()

    private fun DeviceConnectionManager.Event.AddAction.stateTransition(state: ConnectableDeviceState) =
        when (state) {
            is ConnectableDeviceState.Connected.Idle -> {
                state.handleAction(action)
            }
            is ConnectableDeviceState.Connected.HandlingAction -> {
                state.addAction(action)
            }
            is ConnectableDeviceState.Connected.NoServices,
            is ConnectableDeviceState.Connected.Discovering,
            is ConnectableDeviceState.Connecting,
            is ConnectableDeviceState.Reconnecting,
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

    private fun DeviceConnectionManager.Event.MtuUpdated.stateTransition(state: ConnectableDeviceState) =
        if (state is ConnectableDeviceState.Connected) {
            state.didUpdateMtu(newMtu)
        } else {
            state.remain()
        }
}

abstract class BaseConnectableDeviceStateRepo(
    initialState: () -> ConnectableDeviceState,
    coroutineContext: CoroutineContext = Dispatchers.Main.immediate
) : HotStateFlowRepo<ConnectableDeviceState>(
    coroutineContext = coroutineContext,
    initialState = { initialState() }
)

class ConnectableDeviceStateImplRepo(
    connectionManager: DeviceConnectionManager,
    coroutineContext: CoroutineContext = Dispatchers.Main.immediate
) : BaseConnectableDeviceStateRepo(
    initialState = {
        when (connectionManager.getCurrentState()) {
            DeviceConnectionManager.State.CONNECTED -> ConnectableDeviceStateImpl.Connected.NoServices(
                null,
                connectionManager
            )
            DeviceConnectionManager.State.CONNECTING -> ConnectableDeviceStateImpl.Connecting(
                connectionManager
            )
            DeviceConnectionManager.State.DISCONNECTED -> ConnectableDeviceStateImpl.Disconnected(
                connectionManager
            )
            DeviceConnectionManager.State.DISCONNECTING -> ConnectableDeviceStateImpl.Disconnecting(
                connectionManager
            )
        }
    },
    coroutineContext = coroutineContext
)