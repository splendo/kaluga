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

import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.state.HotStateFlowRepo
import com.splendo.kaluga.state.StateRepo
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

typealias ConnectibleDeviceStateFlowRepo = StateRepo<ConnectibleDeviceState, MutableStateFlow<ConnectibleDeviceState>>

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
    private val connectionManager: BaseDeviceConnectionManager,
    private val coroutineScope: CoroutineScope,
    private val createDeviceStateFlow: (BaseDeviceConnectionManager, CoroutineContext) -> ConnectibleDeviceStateFlowRepo = ::ConnectibleDeviceStateImplRepo
) : Device, CoroutineScope by coroutineScope {

    companion object {
        const val TAG = "DeviceImp"
    }

    private val sharedInfo = MutableStateFlow(initialDeviceInfo)
    private val deviceStateRepo = MutableStateFlow<ConnectibleDeviceStateFlowRepo?>(null)
    override val info: Flow<DeviceInfo> = sharedInfo.asStateFlow()
    override val state: Flow<DeviceState> = combine(sharedInfo.map { it.advertisementData.isConnectable }, deviceStateRepo) { isConnectable, repo ->
        debug("TEST $isConnectable ${repo == null}")
        when {
            !isConnectable -> null
            repo != null -> repo
            else -> createDeviceStateRepoIfNotExisting()
        }
    }.distinctUntilChanged().flatMapLatest {
        it ?: flowOf(NotConnectableDeviceStateImpl)
    }

    init {
        launch {
            sharedInfo.map { it.advertisementData.isConnectable }.distinctUntilChanged()
                .filterNot { it }.collect {
                connectionManager.disconnect()
                deviceStateRepo.value = null
            }
        }
        launch {
            connectionManager.rssi.collect(::rssiDidUpdate)
        }
        launch {
            connectionManager.events.collect { event ->
                val repo = when (event) {
                    is BaseDeviceConnectionManager.Event.Connecting,
                    is BaseDeviceConnectionManager.Event.Connected -> createDeviceStateRepoIfNotExisting()
                    is BaseDeviceConnectionManager.Event.CancelledConnecting,
                    is BaseDeviceConnectionManager.Event.Discovering,
                    is BaseDeviceConnectionManager.Event.DiscoveredServices,
                    is BaseDeviceConnectionManager.Event.AddAction,
                    is BaseDeviceConnectionManager.Event.CompletedAction,
                    is BaseDeviceConnectionManager.Event.Disconnecting,
                    is BaseDeviceConnectionManager.Event.Disconnected,
                    is BaseDeviceConnectionManager.Event.MtuUpdated -> deviceStateRepo.value
                }
                repo?.takeAndChangeState { state ->
                    event.stateTransition(state)
                }
            }
        }
    }

    override suspend fun connect(): Boolean = createDeviceStateRepoIfNotExisting()?.let { repo ->
        repo.transformLatest { deviceState ->
            when (deviceState) {
                is ConnectibleDeviceState.Disconnected -> deviceState.startConnecting()
                is ConnectibleDeviceState.Connected -> emit(true)
                is ConnectibleDeviceState.Connecting, is ConnectibleDeviceState.Reconnecting, is ConnectibleDeviceState.Disconnecting -> {}
            }
        }.first()
    } ?: false

    override fun handleConnected() = connectionManager.handleConnect()

    override suspend fun disconnect() {
        deviceStateRepo.value?.transformLatest { deviceState ->
            when (deviceState) {
                is ConnectibleDeviceState.Connected -> deviceState.startDisconnected()
                is ConnectibleDeviceState.Connecting -> deviceState.handleCancel()
                is ConnectibleDeviceState.Reconnecting -> deviceState.handleCancel()
                is ConnectibleDeviceState.Disconnected -> emit(Unit)
                is ConnectibleDeviceState.Disconnecting -> {} // just wait
            }
        }?.first()
    }

    override suspend fun handleDisconnected() = connectionManager.handleDisconnect()

    override fun rssiDidUpdate(rssi: Int) {
        sharedInfo.value = sharedInfo.value.copy(rssi = rssi)
    }

    override fun advertisementDataAndRssiDidUpdate(
        advertisementData: BaseAdvertisementData,
        rssi: Int
    ) {
        sharedInfo.value = sharedInfo.value.copy(rssi = rssi, advertisementData = advertisementData)
    }

    private fun createDeviceStateRepoIfNotExisting(): ConnectibleDeviceStateFlowRepo? =
        deviceStateRepo.updateAndGet { repo ->
            repo ?: if (sharedInfo.value.advertisementData.isConnectable) createDeviceStateFlow(
                connectionManager,
                coroutineScope.coroutineContext
            ) else null
        }

    private suspend fun BaseDeviceConnectionManager.Event.stateTransition(state: ConnectibleDeviceState): suspend () -> ConnectibleDeviceState =
        when (this) {
            is BaseDeviceConnectionManager.Event.Connecting -> stateTransition(state)
            is BaseDeviceConnectionManager.Event.CancelledConnecting -> stateTransition(state)
            is BaseDeviceConnectionManager.Event.Connected -> stateTransition(state)
            is BaseDeviceConnectionManager.Event.Disconnecting -> stateTransition(state)
            is BaseDeviceConnectionManager.Event.Disconnected -> stateTransition(state)
            is BaseDeviceConnectionManager.Event.Discovering -> stateTransition(state)
            is BaseDeviceConnectionManager.Event.DiscoveredServices -> stateTransition(state)
            is BaseDeviceConnectionManager.Event.AddAction -> stateTransition(state)
            is BaseDeviceConnectionManager.Event.CompletedAction -> stateTransition(state)
            is BaseDeviceConnectionManager.Event.MtuUpdated -> stateTransition(state)
        }

    private fun BaseDeviceConnectionManager.Event.Connecting.stateTransition(state: ConnectibleDeviceState) =
        if (state is ConnectibleDeviceState.Disconnected)
            state.connect
        else
            state.remain()

    private fun BaseDeviceConnectionManager.Event.CancelledConnecting.stateTransition(state: ConnectibleDeviceState) =
        when (state) {
            is ConnectibleDeviceState.Connecting -> state.cancelConnection
            is ConnectibleDeviceState.Reconnecting -> state.cancelConnection
            else -> state.remain()
        }

    private suspend fun BaseDeviceConnectionManager.Event.Connected.stateTransition(state: ConnectibleDeviceState) =
        when (state) {
            is ConnectibleDeviceState.Connecting -> state.didConnect
            is ConnectibleDeviceState.Reconnecting -> state.didConnect
            is ConnectibleDeviceState.Connected -> state.remain()
            else -> {
                connectionManager.reset()
                state.remain()
            }
        }

    private fun BaseDeviceConnectionManager.Event.Disconnecting.stateTransition(state: ConnectibleDeviceState) =
        if (state is ConnectibleDeviceState.Connected)
            state.disconnecting
        else
            state.remain()

    private suspend fun BaseDeviceConnectionManager.Event.Disconnected.stateTransition(state: ConnectibleDeviceState) =
        when (state) {
            is ConnectibleDeviceState.Reconnecting -> {
                state.retry(connectionSettings.reconnectionSettings).also {
                    if (it == state.didDisconnect) {
                        onDisconnect()
                    }
                }
            }
            is ConnectibleDeviceState.Connected -> when (connectionSettings.reconnectionSettings) {
                is ConnectionSettings.ReconnectionSettings.Always,
                is ConnectionSettings.ReconnectionSettings.Limited -> state.reconnect
                is ConnectionSettings.ReconnectionSettings.Never -> {
                    onDisconnect()
                    state.didDisconnect
                }
            }
            is ConnectibleDeviceState.Disconnected -> state.remain()
            is ConnectibleDeviceState.Connecting,
            is ConnectibleDeviceState.Disconnecting -> {
                onDisconnect()
                state.didDisconnect
            }
        }

    private fun BaseDeviceConnectionManager.Event.Discovering.stateTransition(state: ConnectibleDeviceState) =
        if (state is ConnectibleDeviceState.Connected.NoServices)
            state.discoverServices
        else
            state.remain()

    private fun BaseDeviceConnectionManager.Event.DiscoveredServices.stateTransition(state: ConnectibleDeviceState) =
        if (state is ConnectibleDeviceState.Connected.Discovering)
            state.didDiscoverServices(services)
        else
            state.remain()

    private fun BaseDeviceConnectionManager.Event.AddAction.stateTransition(state: ConnectibleDeviceState) =
        when (state) {
            is ConnectibleDeviceState.Connected.Idle -> {
                state.handleAction(action)
            }
            is ConnectibleDeviceState.Connected.HandlingAction -> {
                state.addAction(action)
            }
            is ConnectibleDeviceState.Connected.NoServices,
            is ConnectibleDeviceState.Connected.Discovering,
            is ConnectibleDeviceState.Connecting,
            is ConnectibleDeviceState.Reconnecting,
            is ConnectibleDeviceState.Disconnected,
            is ConnectibleDeviceState.Disconnecting,
            -> {
                state.remain() // TODO consider an optional buffer
            }
        }

    private fun BaseDeviceConnectionManager.Event.CompletedAction.stateTransition(state: ConnectibleDeviceState) =
        if (state is ConnectibleDeviceState.Connected.HandlingAction && state.action === action) {
            state.action.completedSuccessfully.complete(succeeded)
            debug(TAG) { "Action $action has been succeeded: $succeeded" }
            state.actionCompleted
        } else {
            state.remain()
        }

    private fun BaseDeviceConnectionManager.Event.MtuUpdated.stateTransition(state: ConnectibleDeviceState) = if (state is ConnectibleDeviceState.Connected) {
        state.didUpdateMtu(newMtu)
    } else {
        state.remain()
    }
}

abstract class BaseConnectibleDeviceStateRepo(
    initialState: () -> ConnectibleDeviceState.Disconnected,
    coroutineContext: CoroutineContext = Dispatchers.Main.immediate
) : HotStateFlowRepo<ConnectibleDeviceState>(
    coroutineContext = coroutineContext,
    initialState = { initialState() }
)

class ConnectibleDeviceStateImplRepo(
    connectionManager: BaseDeviceConnectionManager,
    coroutineContext: CoroutineContext = Dispatchers.Main.immediate
) : BaseConnectibleDeviceStateRepo(
    initialState = { ConnectibleDeviceStateImpl.Disconnected(connectionManager) },
    coroutineContext = coroutineContext
)
