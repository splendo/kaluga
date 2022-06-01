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
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

sealed interface DeviceState : KalugaState {
    sealed interface Connected : DeviceState {
        interface NoServices : Connected {
            fun startDiscovering()
            val discoverServices: suspend () -> Discovering
        }

        interface Discovering : Connected {
            fun didDiscoverServices(services: List<Service>): suspend () -> Idle
        }

        interface DiscoveredServices : Connected {
            val services: List<Service>
        }

        interface Idle : DiscoveredServices {
            fun handleAction(action: DeviceAction): suspend () -> HandlingAction
        }

        interface HandlingAction : DiscoveredServices {
            fun addAction(newAction: DeviceAction): suspend () -> HandlingAction
            val actionCompleted: suspend () -> DiscoveredServices
        }
    }

    interface Connecting : DeviceState {
        val cancelConnection: suspend () -> Disconnecting
        val didConnect: suspend () -> Connected.NoServices
        fun handleCancel()
    }

    interface Reconnecting : DeviceState {
        val attempt: Int
        val services: List<Service>?

        val raiseAttempt: suspend () -> Reconnecting
        val didConnect: suspend () -> Connected
        val cancelConnection: suspend () -> Disconnecting

        fun retry(reconnectionSettings: ConnectionSettings.ReconnectionSettings): suspend () -> DeviceState = when (reconnectionSettings) {
            is ConnectionSettings.ReconnectionSettings.Always -> raiseAttempt
            is ConnectionSettings.ReconnectionSettings.Never -> didDisconnect
            is ConnectionSettings.ReconnectionSettings.Limited -> {
                if (attempt + 1 < reconnectionSettings.attempts) {
                    raiseAttempt
                } else {
                    didDisconnect
                }
            }
        }
    }

    interface Disconnected : DeviceState {
        val connect: suspend () -> Connecting

        fun startConnecting()
    }

    interface Disconnecting : DeviceState

    val didDisconnect: suspend () -> Disconnected
    val disconnecting: suspend () -> Disconnecting
}

sealed class DeviceStateImpl {

    protected abstract val deviceConnectionManager: BaseDeviceConnectionManager

    sealed class Connected : DeviceStateImpl() {

        data class NoServices constructor(
            override val deviceConnectionManager: BaseDeviceConnectionManager
        ) : Connected(), DeviceState.Connected.NoServices {

            override fun startDiscovering() {
                deviceConnectionManager.startDiscovering()
            }

            override val discoverServices = suspend {
                Discovering(deviceConnectionManager)
            }
        }

        data class Discovering constructor(
            override val deviceConnectionManager: BaseDeviceConnectionManager
        ) : Connected(),
            DeviceState.Connected.Discovering,
            HandleAfterOldStateIsRemoved<DeviceState> {

            override fun didDiscoverServices(services: List<Service>): suspend () -> Idle {
                return { Idle(services, deviceConnectionManager) }
            }

            override suspend fun afterOldStateIsRemoved(oldState: DeviceState) {
                deviceConnectionManager.discoverServices()
            }
        }

        data class Idle constructor(
            override val services: List<Service>,
            override val deviceConnectionManager: BaseDeviceConnectionManager
        ) : Connected(), DeviceState.Connected.Idle {

            override fun handleAction(action: DeviceAction) = suspend { HandlingAction(action, emptyList(), services, deviceConnectionManager) }
        }

        data class HandlingAction constructor(
            internal val action: DeviceAction,
            internal val nextActions: List<DeviceAction>,
            override val services: List<Service>,
            override val deviceConnectionManager: BaseDeviceConnectionManager
        ) : Connected(), DeviceState.Connected.HandlingAction, HandleAfterOldStateIsRemoved<DeviceState> {

            override fun addAction(newAction: DeviceAction) = suspend { HandlingAction(action, listOf(*nextActions.toTypedArray(), newAction), services, deviceConnectionManager) }

            override val actionCompleted: suspend () -> DeviceState.Connected.DiscoveredServices = suspend {
                when (action) {
                    is DeviceAction.Read.Characteristic -> action.characteristic.updateValue()
                    is DeviceAction.Read.Descriptor -> action.descriptor.updateValue()
                    is DeviceAction.Write.Characteristic -> action.characteristic.updateValue()
                    is DeviceAction.Write.Descriptor -> action.descriptor.updateValue()
                    is DeviceAction.Notification.Enable -> action.characteristic.updateValue()
                    is DeviceAction.Notification.Disable -> { }
                }
                if (nextActions.isEmpty()) {
                    Idle(services, deviceConnectionManager)
                } else {
                    val nextAction = nextActions.first()
                    val remainingActions = nextActions.drop(1)
                    HandlingAction(nextAction, remainingActions, services, deviceConnectionManager)
                }
            }

            override suspend fun afterOldStateIsRemoved(oldState: DeviceState) {
                if (oldState is HandlingAction && oldState.action == action)
                    return
                deviceConnectionManager.performAction(action)
            }
        }

        fun startDisconnected() = deviceConnectionManager.startDisconnecting()

        val reconnect = suspend {
            // All services, characteristics and descriptors become invalidated after it disconnects
            Reconnecting(0, null, deviceConnectionManager)
        }

        suspend fun readRssi() {
            deviceConnectionManager.readRssi()
        }

        suspend fun requestMtu(mtu: Int): Boolean {
            return deviceConnectionManager.requestMtu(mtu)
        }
    }
    data class Connecting constructor(
        override val deviceConnectionManager: BaseDeviceConnectionManager
    ) : DeviceStateImpl(), DeviceState.Connecting, HandleAfterOldStateIsRemoved<DeviceState> {

        override val cancelConnection = disconnecting

        override fun handleCancel() = deviceConnectionManager.cancelConnecting()

        override val didConnect = suspend {
            Connected.NoServices(deviceConnectionManager)
        }

        override suspend fun afterOldStateIsRemoved(oldState: DeviceState) {
            when (oldState) {
                is Disconnected -> deviceConnectionManager.connect()
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
        override val attempt: Int,
        override val services: List<Service>?,
        override val deviceConnectionManager: BaseDeviceConnectionManager
    ) : DeviceStateImpl(), DeviceState.Reconnecting, HandleAfterOldStateIsRemoved<DeviceState> {

        fun handleCancel() = deviceConnectionManager.cancelConnecting()

        override val cancelConnection = disconnecting

        override val raiseAttempt = suspend { Reconnecting(attempt+1, services, deviceConnectionManager) }
        override val didConnect: suspend () -> DeviceState.Connected = suspend {
            services?.let { Connected.Idle(services, deviceConnectionManager) } ?: Connected.NoServices(deviceConnectionManager)
        }

        override suspend fun afterOldStateIsRemoved(oldState: DeviceState) {
            when (oldState) {
                is Connected, is Reconnecting -> deviceConnectionManager.connect()
                else -> {}
            }
        }
    }

    data class Disconnected constructor(
        override val deviceConnectionManager: BaseDeviceConnectionManager
    ) : DeviceStateImpl(), DeviceState.Disconnected {

        override fun startConnecting() = deviceConnectionManager.startConnecting()

        override val connect = suspend { Connecting(deviceConnectionManager) }
    }

    data class Disconnecting constructor(
        override val deviceConnectionManager: BaseDeviceConnectionManager
    ) : DeviceStateImpl(), DeviceState.Disconnecting, HandleAfterOldStateIsRemoved<DeviceState> {

        override suspend fun afterOldStateIsRemoved(oldState: DeviceState) {
            when (oldState) {
                is Connecting, is Reconnecting, is Connected -> deviceConnectionManager.disconnect()
                else -> {}
            }
        }
    }

    val didDisconnect = suspend {
        Disconnected(deviceConnectionManager)
    }

    val disconnecting = suspend {
        Disconnecting(deviceConnectionManager)
    }
}



// class Device constructor(
//     private val connectionSettings: ConnectionSettings,
//     private val connectionManager: BaseDeviceConnectionManager,
//     private val initialDeviceInfo: DeviceInfoImpl,
//     coroutineContext: CoroutineContext
// ) : HotStateFlowRepo<DeviceState>(
//     coroutineContext = coroutineContext,
//     initialState = {
//         DeviceState.Disconnected(initialDeviceInfo, connectionManager)
//     }
// ) {
//
//     private companion object {
//         const val TAG = "Device"
//     }
//
//     constructor(
//         connectionSettings: ConnectionSettings,
//         initialDeviceInfo: DeviceInfoImpl,
//         connectionManagerBuilder: BaseDeviceConnectionManager.Builder,
//         coroutineContext: CoroutineContext
//     ) : this(
//         connectionSettings,
//         connectionManagerBuilder.create(initialDeviceInfo.deviceWrapper, coroutineScope = CoroutineScope(coroutineContext + CoroutineName("ConnectionManager"))),
//         initialDeviceInfo,
//         coroutineContext
//     )
//
//     val identifier: Identifier
//         get() = initialDeviceInfo.identifier
//
//     init {
//         launch {
//             connectionManager.events.collect { event ->
//                 event.handle()
//             }
//         }
//     }
//
//     private suspend fun BaseDeviceConnectionManager.Event.handle() = takeAndChangeState {
//         stateTransition(it)
//     }
//
//     private suspend fun BaseDeviceConnectionManager.Event.stateTransition(state: DeviceState): suspend () -> DeviceState = when (this) {
//         is BaseDeviceConnectionManager.Event.RssiUpdate -> stateTransition(state)
//         is BaseDeviceConnectionManager.Event.Connecting -> stateTransition(state)
//         is BaseDeviceConnectionManager.Event.CancelledConnecting -> stateTransition(state)
//         is BaseDeviceConnectionManager.Event.Connected -> stateTransition(state)
//         is BaseDeviceConnectionManager.Event.Disconnecting -> stateTransition(state)
//         is BaseDeviceConnectionManager.Event.Disconnected -> stateTransition(state)
//         is BaseDeviceConnectionManager.Event.Discovering -> stateTransition(state)
//         is BaseDeviceConnectionManager.Event.DiscoveredServices -> stateTransition(state)
//         is BaseDeviceConnectionManager.Event.AddAction -> stateTransition(state)
//         is BaseDeviceConnectionManager.Event.CompletedAction -> stateTransition(state)
//     }
//
//     private fun BaseDeviceConnectionManager.Event.RssiUpdate.stateTransition(state: DeviceState) = state.rssiDidUpdate(rrsi)
//
//     private fun BaseDeviceConnectionManager.Event.Connecting.stateTransition(state: DeviceState) = if (state is DeviceState.Disconnected)
//         state.connect
//     else
//         state.remain()
//
//     private fun BaseDeviceConnectionManager.Event.CancelledConnecting.stateTransition(state: DeviceState) = when (state) {
//         is DeviceState.Connecting -> state.cancelConnection
//         is DeviceState.Reconnecting -> state.cancelConnection
//         else -> state.remain()
//     }
//
//     private suspend fun BaseDeviceConnectionManager.Event.Connected.stateTransition(state: DeviceState) = when (state) {
//         is DeviceState.Connecting -> state.didConnect
//         is DeviceState.Reconnecting -> state.didConnect
//         is DeviceState.Connected -> state.remain()
//         else -> {
//             connectionManager.reset()
//             state.remain()
//         }
//     }
//
//     private fun BaseDeviceConnectionManager.Event.Disconnecting.stateTransition(state: DeviceState) = if (state is DeviceState.Connected)
//         state.disconnecting
//     else
//         state.remain()
//
//     private suspend fun BaseDeviceConnectionManager.Event.Disconnected.stateTransition(state: DeviceState) = when (state) {
//         is DeviceState.Reconnecting -> {
//             state.retry(connectionSettings.reconnectionSettings).also {
//                 if (it == state.didDisconnect) {
//                     onDisconnect()
//                 }
//             }
//         }
//         is DeviceState.Connected -> when (connectionSettings.reconnectionSettings) {
//             is ConnectionSettings.ReconnectionSettings.Always,
//             is ConnectionSettings.ReconnectionSettings.Limited -> state.reconnect
//             is ConnectionSettings.ReconnectionSettings.Never -> {
//                 onDisconnect()
//                 state.didDisconnect
//             }
//         }
//         is DeviceState.Disconnected -> state.remain()
//         is DeviceState.Connecting,
//         is DeviceState.Disconnecting -> {
//             onDisconnect()
//             state.didDisconnect
//         }
//     }
//
//     private fun BaseDeviceConnectionManager.Event.Discovering.stateTransition(state: DeviceState) = if (state is DeviceState.Connected.NoServices)
//         state.discoverServices
//     else
//         state.remain()
//
//     private fun BaseDeviceConnectionManager.Event.DiscoveredServices.stateTransition(state: DeviceState) = if (state is DeviceState.Connected.Discovering)
//         state.didDiscoverServices(services)
//     else
//         state.remain()
//
//     private fun BaseDeviceConnectionManager.Event.AddAction.stateTransition(state: DeviceState) = when (state) {
//         is DeviceState.Connected.Idle -> {
//             state.handleAction(action)
//         }
//         is DeviceState.Connected.HandlingAction -> {
//             state.addAction(action)
//         }
//         is DeviceState.Connected.NoServices,
//         is DeviceState.Connected.Discovering,
//         is DeviceState.Connecting,
//         is DeviceState.Reconnecting,
//         is DeviceState.Disconnected,
//         is DeviceState.Disconnecting,
//         -> {
//             state.remain() // TODO consider an optional buffer
//         }
//     }
//
//     private fun BaseDeviceConnectionManager.Event.CompletedAction.stateTransition(state: DeviceState) = if (state is DeviceState.Connected.HandlingAction && state.action === action) {
//         state.action.completedSuccessfully.complete(succeeded)
//         debug(TAG) { "Action $action has been succeeded: $succeeded" }
//         state.actionCompleted
//     } else {
//         state.remain()
//     }
// }
