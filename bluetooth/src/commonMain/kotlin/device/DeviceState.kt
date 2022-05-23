/*
 Copyright (c) 2020. Splendo Consulting B.V. The Netherlands

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 */

package com.splendo.kaluga.bluetooth.device

import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.state.HandleAfterOldStateIsRemoved
import com.splendo.kaluga.state.HotStateFlowRepo
import com.splendo.kaluga.state.KalugaState
import com.splendo.kaluga.state.StateRepo
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

sealed class DeviceAction {

    /**
     * A Deferred that will be completed with
     * `true` if `DeviceAction` was succeeded, or
     * `false` if `DeviceAction` was failed
     * */
    val completedSuccessfully = CompletableDeferred<Boolean>()

    sealed class Read : DeviceAction() {
        class Characteristic(val characteristic: com.splendo.kaluga.bluetooth.Characteristic) : Read()
        class Descriptor(val descriptor: com.splendo.kaluga.bluetooth.Descriptor) : Read()
    }

    sealed class Write(val newValue: ByteArray?) : DeviceAction() {
        class Characteristic(newValue: ByteArray?, val characteristic: com.splendo.kaluga.bluetooth.Characteristic) : Write(newValue)
        class Descriptor(newValue: ByteArray?, val descriptor: com.splendo.kaluga.bluetooth.Descriptor) : Write(newValue)
    }

    sealed class Notification(val characteristic: com.splendo.kaluga.bluetooth.Characteristic) : DeviceAction() {
        class Enable(characteristic: com.splendo.kaluga.bluetooth.Characteristic) : Notification(characteristic)
        class Disable(characteristic: com.splendo.kaluga.bluetooth.Characteristic) : Notification(characteristic)
    }
}

typealias DeviceStateFlowRepo = StateRepo<DeviceState, MutableStateFlow<DeviceState>>

sealed class DeviceState(
    open val deviceInfo: DeviceInfoImpl,
    open val connectionManager: BaseDeviceConnectionManager
) : KalugaState, DeviceInfo by deviceInfo {

    sealed class Connected(
        deviceInfo: DeviceInfoImpl,
        connectionManager: BaseDeviceConnectionManager
    ) : DeviceState(deviceInfo, connectionManager) {

        data class NoServices constructor(
            override val deviceInfo: DeviceInfoImpl,
            override val connectionManager: BaseDeviceConnectionManager
        ) : Connected(deviceInfo, connectionManager) {

            fun startDiscovering() {
                connectionManager.startDiscovering()
            }

            val discoverServices = suspend {
                Discovering(deviceInfo, connectionManager)
            }
        }

        data class Discovering constructor(
            override val deviceInfo: DeviceInfoImpl,
            override val connectionManager: BaseDeviceConnectionManager
        ) : Connected(deviceInfo, connectionManager),
            HandleAfterOldStateIsRemoved<DeviceState> {

            internal fun didDiscoverServices(services: List<Service>): suspend () -> Idle {
                return { Idle(services, deviceInfo, connectionManager) }
            }

            override suspend fun afterOldStateIsRemoved(oldState: DeviceState) {
                connectionManager.discoverServices()
            }
        }

        data class Idle constructor(
            val services: List<Service>,
            override val deviceInfo: DeviceInfoImpl,
            override val connectionManager: BaseDeviceConnectionManager
        ) : Connected(deviceInfo, connectionManager) {

            fun handleAction(action: DeviceAction): suspend () -> HandlingAction {
                return { HandlingAction(action, emptyList(), services, deviceInfo, connectionManager) }
            }
        }

        data class HandlingAction constructor(
            internal val action: DeviceAction,
            internal val nextActions: List<DeviceAction>,
            val services: List<Service>,
            override val deviceInfo: DeviceInfoImpl,
            override val connectionManager: BaseDeviceConnectionManager
        ) : Connected(deviceInfo, connectionManager), HandleAfterOldStateIsRemoved<DeviceState> {

            fun addAction(newAction: DeviceAction): suspend () -> HandlingAction {
                return { HandlingAction(action, listOf(*nextActions.toTypedArray(), newAction), services, deviceInfo, connectionManager) }
            }

            internal val actionCompleted = suspend {
                when (action) {
                    is DeviceAction.Read.Characteristic -> action.characteristic.updateValue()
                    is DeviceAction.Read.Descriptor -> action.descriptor.updateValue()
                    is DeviceAction.Write.Characteristic -> action.characteristic.updateValue()
                    is DeviceAction.Write.Descriptor -> action.descriptor.updateValue()
                    is DeviceAction.Notification.Enable -> action.characteristic.updateValue()
                    is DeviceAction.Notification.Disable -> { }
                }
                if (nextActions.isEmpty()) {
                    Idle(services, deviceInfo, connectionManager)
                } else {
                    val nextAction = nextActions.first()
                    val remainingActions = nextActions.drop(1)
                    HandlingAction(nextAction, remainingActions, services, deviceInfo, connectionManager)
                }
            }

            override suspend fun afterOldStateIsRemoved(oldState: DeviceState) {
                if (oldState is HandlingAction && oldState.action == action)
                    return
                connectionManager.performAction(action)
            }
        }

        fun startDisconnected() = connectionManager.startDisconnecting()

        val reconnect = suspend {
            // All services, characteristics and descriptors become invalidated after it disconnects
            Reconnecting(0, null, deviceInfo, connectionManager)
        }

        suspend fun readRssi() {
            connectionManager.readRssi()
        }

        suspend fun requestMtu(mtu: Int): Boolean {
            return connectionManager.requestMtu(mtu)
        }

        override suspend fun finalState() {
            super.finalState()

            connectionManager.disconnect()
        }
    }
    data class Connecting constructor(
        override val deviceInfo: DeviceInfoImpl,
        override val connectionManager: BaseDeviceConnectionManager
    ) : DeviceState(deviceInfo, connectionManager), HandleAfterOldStateIsRemoved<DeviceState> {

        val cancelConnection = disconnecting

        fun handleCancel() = connectionManager.cancelConnecting()

        internal val didConnect = suspend {
            Connected.NoServices(deviceInfo, connectionManager)
        }

        override suspend fun afterOldStateIsRemoved(oldState: DeviceState) {
            when (oldState) {
                is Disconnected -> connectionManager.connect()
                is Connected,
                is Connecting,
                is Reconnecting,
                is Disconnecting -> {
                    // do nothing: TODO check all these are correct, e.g. Disconnecting
                }
            }
        }
    }

    data class Reconnecting constructor(
        val attempt: Int,
        val services: List<Service>?,
        override val deviceInfo: DeviceInfoImpl,
        override val connectionManager: BaseDeviceConnectionManager
    ) : DeviceState(deviceInfo, connectionManager), HandleAfterOldStateIsRemoved<DeviceState> {

        fun retry(reconnectionSettings: ConnectionSettings.ReconnectionSettings): suspend () -> DeviceState {
            return when (reconnectionSettings) {
                is ConnectionSettings.ReconnectionSettings.Always -> remain()
                is ConnectionSettings.ReconnectionSettings.Never -> didDisconnect
                is ConnectionSettings.ReconnectionSettings.Limited -> {
                    val nextAttempt = attempt + 1
                    if (nextAttempt < reconnectionSettings.attempts) {
                        suspend { copy(attempt = nextAttempt) }
                    } else {
                        didDisconnect
                    }
                }
            }
        }

        fun handleCancel() = connectionManager.cancelConnecting()

        val cancelConnection = disconnecting

        internal val didConnect = suspend {
            services?.let { Connected.Idle(services, deviceInfo, connectionManager) } ?: Connected.NoServices(deviceInfo, connectionManager)
        }

        override suspend fun afterOldStateIsRemoved(oldState: DeviceState) {
            when (oldState) {
                is Connected, is Reconnecting -> connectionManager.connect()
                else -> {}
            }
        }
    }

    data class Disconnected constructor(
        override val deviceInfo: DeviceInfoImpl,
        override val connectionManager: BaseDeviceConnectionManager
    ) : DeviceState(deviceInfo, connectionManager) {

        fun startConnecting() = connectionManager.startConnecting()

        fun connect(deviceState: DeviceState): suspend () -> DeviceState =
            if (deviceInfo.advertisementData.isConnectible)
                suspend {
                    Connecting(deviceInfo, connectionManager)
                }
            else
                deviceState.remain()
    }

    data class Disconnecting constructor(
        override val deviceInfo: DeviceInfoImpl,
        override val connectionManager: BaseDeviceConnectionManager
    ) : DeviceState(deviceInfo, connectionManager), HandleAfterOldStateIsRemoved<DeviceState> {

        override suspend fun afterOldStateIsRemoved(oldState: DeviceState) {
            when (oldState) {
                is Connecting, is Reconnecting, is Connected -> connectionManager.disconnect()
                else -> {}
            }
        }
    }

    fun rssiDidUpdate(rssi: Int): suspend () -> DeviceState {
        return updateDeviceInfo(deviceInfo.copy(rssi = rssi))
    }

    fun advertisementDataAndRssiDidUpdate(advertisementData: BaseAdvertisementData, rssi: Int): suspend () -> DeviceState {
        return updateDeviceInfo(deviceInfo.copy(rssi = rssi, advertisementData = advertisementData))
    }

    private fun updateDeviceInfo(newDeviceInfo: DeviceInfoImpl): suspend () -> DeviceState {
        return {
            when (this) {
                is Connected.NoServices -> copy(deviceInfo = newDeviceInfo)
                is Connected.Idle -> copy(deviceInfo = newDeviceInfo)
                is Connected.HandlingAction -> copy(deviceInfo = newDeviceInfo)
                is Connected.Discovering -> copy(deviceInfo = newDeviceInfo)
                is Connecting -> copy(deviceInfo = newDeviceInfo)
                is Reconnecting -> copy(deviceInfo = newDeviceInfo)
                is Disconnecting -> copy(deviceInfo = newDeviceInfo)
                is Disconnected -> copy(deviceInfo = newDeviceInfo)
            }
        }
    }

    internal val didDisconnect = suspend {
        Disconnected(deviceInfo, connectionManager)
    }

    internal val disconnecting = suspend {
        Disconnecting(deviceInfo, connectionManager)
    }
}

class Device constructor(
    private val connectionSettings: ConnectionSettings,
    private val connectionManager: BaseDeviceConnectionManager,
    private val initialDeviceInfo: DeviceInfoImpl,
    coroutineContext: CoroutineContext
) : HotStateFlowRepo<DeviceState>(
    coroutineContext = coroutineContext,
    initialState = {
        DeviceState.Disconnected(initialDeviceInfo, connectionManager)
    }
) {

    private companion object {
        const val TAG = "Device"
    }

    constructor(
        connectionSettings: ConnectionSettings,
        initialDeviceInfo: DeviceInfoImpl,
        connectionManagerBuilder: BaseDeviceConnectionManager.Builder,
        coroutineScope: CoroutineScope
    ) : this(
        connectionSettings,
        connectionManagerBuilder.create(initialDeviceInfo.deviceWrapper, coroutineScope = coroutineScope),
        initialDeviceInfo,
        coroutineScope.coroutineContext
    )

    val identifier: Identifier
        get() = initialDeviceInfo.identifier

    init {
        monitorRssi()
        monitorConnecting()
        monitorCancelConnection()
        monitorConnected()
        monitorDisconnecting()
        monitorDisconnected()
        monitorDiscovering()
        monitorDiscovered()
        monitorNewActions()
        monitorActionCompleted()
    }

    private fun monitorRssi() {
        launch {
            connectionManager.rssiUpdate.collect { rssi ->
                takeAndChangeState {
                    it.rssiDidUpdate(rssi)
                }
            }
        }
    }

    private fun monitorConnecting() {
        launch {
            connectionManager.startedConnecting.collect {
                takeAndChangeState(
                    remainIfStateNot = DeviceState.Disconnected::class
                ) { deviceState ->
                    deviceState.connect(deviceState)
                }
            }
        }
    }
    private fun monitorCancelConnection() {
        launch {
            connectionManager.cancelledConnecting.collect {
                takeAndChangeState { deviceState ->
                    when (deviceState) {
                        is DeviceState.Connecting -> deviceState.cancelConnection
                        is DeviceState.Reconnecting -> deviceState.cancelConnection
                        else -> deviceState.remain()
                    }
                }
            }
        }
    }

    private fun monitorConnected() {
        launch {
            connectionManager.didConnect.collect {
                takeAndChangeState { state ->
                    when (state) {
                        is DeviceState.Connecting -> state.didConnect
                        is DeviceState.Reconnecting -> state.didConnect
                        is DeviceState.Connected -> state.remain()
                        else -> {
                            connectionManager.reset()
                            state.remain()
                        }
                    }
                }
            }
        }
    }

    private fun monitorDisconnecting() {
        launch {
            connectionManager.startedDisconnecting.collect {
                takeAndChangeState(remainIfStateNot = DeviceState.Connected::class) { deviceState ->
                    deviceState.disconnecting
                }
            }
        }
    }

    private fun monitorDisconnected() {
        launch {
            connectionManager.didDisconnect.collect { clean ->
                takeAndChangeState { state ->
                    when (state) {
                        is DeviceState.Reconnecting -> {
                            state.retry(connectionSettings.reconnectionSettings).also {
                                if (it == state.didDisconnect) {
                                    clean()
                                }
                            }
                        }
                        is DeviceState.Connected -> when (connectionSettings.reconnectionSettings) {
                            is ConnectionSettings.ReconnectionSettings.Always,
                            is ConnectionSettings.ReconnectionSettings.Limited -> state.reconnect
                            is ConnectionSettings.ReconnectionSettings.Never -> {
                                clean()
                                state.didDisconnect
                            }
                        }
                        is DeviceState.Disconnected -> state.remain()
                        is DeviceState.Connecting,
                        is DeviceState.Disconnecting -> {
                            clean()
                            state.didDisconnect
                        }
                    }
                }
            }
        }
    }

    private fun monitorDiscovering() {
        launch {
            connectionManager.startedDiscovering.collect {
                takeAndChangeState { deviceState ->
                    if (deviceState is DeviceState.Connected.NoServices)
                        deviceState.discoverServices
                    else
                        deviceState.remain()
                }
            }
        }
    }

    private fun monitorDiscovered() {
        launch {
            connectionManager.discoverCompleted.collect { services ->
                takeAndChangeState { state ->
                    when (state) {
                        is DeviceState.Connected.Discovering -> state.didDiscoverServices(services)
                        else -> state.remain()
                    }
                }
            }
        }
    }

    private fun monitorNewActions() {
        launch {
            connectionManager.newAction.collect { action ->
                takeAndChangeState { state ->
                    when (state) {
                        is DeviceState.Connected.Idle -> {
                            state.handleAction(action)
                        }
                        is DeviceState.Connected.HandlingAction -> {
                            state.addAction(action)
                        }
                        is DeviceState.Connected.NoServices,
                        is DeviceState.Connected.Discovering,
                        is DeviceState.Connecting,
                        is DeviceState.Reconnecting,
                        is DeviceState.Disconnected,
                        is DeviceState.Disconnecting,
                        -> {
                            state.remain() // TODO consider an optional buffer
                        }
                    }
                }
            }
        }
    }

    private fun monitorActionCompleted() {
        launch {
            connectionManager.actionCompleted.collect { (action, succeeded) ->
                takeAndChangeState { state ->
                    if (state is DeviceState.Connected.HandlingAction && state.action == action) {
                        state.action.completedSuccessfully.complete(succeeded)
                        debug(TAG) { "Action $action has been succeeded: $succeeded" }
                        state.actionCompleted
                    } else {
                        state.remain()
                    }
                }
            }
        }
    }
}
