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
import com.splendo.kaluga.base.state.HandleAfterOldStateIsRemoved
import com.splendo.kaluga.base.state.KalugaState
import kotlinx.coroutines.CompletableDeferred

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

    sealed class Write(val newValue: ByteArray) : DeviceAction() {
        class Characteristic(newValue: ByteArray, val characteristic: com.splendo.kaluga.bluetooth.Characteristic) : Write(newValue)
        class Descriptor(newValue: ByteArray, val descriptor: com.splendo.kaluga.bluetooth.Descriptor) : Write(newValue)
    }

    sealed class Notification(val characteristic: com.splendo.kaluga.bluetooth.Characteristic) : DeviceAction() {
        class Enable(characteristic: com.splendo.kaluga.bluetooth.Characteristic) : Notification(characteristic)
        class Disable(characteristic: com.splendo.kaluga.bluetooth.Characteristic) : Notification(characteristic)
    }
}

sealed interface DeviceState

interface NotConnectableDeviceState : DeviceState

sealed interface ConnectableDeviceState : DeviceState, KalugaState {
    sealed interface Connected : ConnectableDeviceState {
        interface NoServices : Connected {
            fun startDiscovering()
            val discoverServices: suspend () -> Discovering
        }

        interface Discovering : Connected {
            fun didDiscoverServices(services: List<Service>): suspend () -> Idle
        }

        sealed interface DiscoveredServices : Connected {
            val services: List<Service>
        }

        interface Idle : DiscoveredServices {
            fun handleAction(action: DeviceAction): suspend () -> HandlingAction
        }

        interface HandlingAction : DiscoveredServices {
            val action: DeviceAction
            val nextActions: List<DeviceAction>
            fun addAction(newAction: DeviceAction): suspend () -> HandlingAction
            val actionCompleted: suspend () -> DiscoveredServices
        }

        val mtu: Int?

        fun startDisconnected()

        val reconnect: suspend () -> Reconnecting
        fun didUpdateMtu(mtu: Int): suspend () -> Connected

        suspend fun readRssi()
        suspend fun requestMtu(mtu: Int): Boolean
        suspend fun pair()
    }

    interface Connecting : ConnectableDeviceState {
        val cancelConnection: suspend () -> Disconnecting
        val didConnect: suspend () -> Connected.NoServices
        fun handleCancel()
    }

    interface Reconnecting : ConnectableDeviceState {
        val attempt: Int
        val services: List<Service>?

        val raiseAttempt: suspend () -> Reconnecting
        val didConnect: suspend () -> Connected
        val cancelConnection: suspend () -> Disconnecting

        fun handleCancel()
        fun retry(reconnectionSettings: ConnectionSettings.ReconnectionSettings): suspend () -> ConnectableDeviceState = when (reconnectionSettings) {
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

    interface Disconnected : ConnectableDeviceState {
        val connect: suspend () -> Connecting

        fun startConnecting()
    }

    interface Disconnecting : ConnectableDeviceState

    val didDisconnect: suspend () -> Disconnected
    val disconnecting: suspend () -> Disconnecting

    suspend fun unpair()
}

object NotConnectableDeviceStateImpl : NotConnectableDeviceState

sealed class ConnectableDeviceStateImpl {

    protected abstract val deviceConnectionManager: DeviceConnectionManager

    sealed class Connected : ConnectableDeviceStateImpl() {

        data class NoServices constructor(
            override val mtu: Int?,
            override val deviceConnectionManager: DeviceConnectionManager
        ) : Connected(), ConnectableDeviceState.Connected.NoServices {

            override fun startDiscovering() {
                deviceConnectionManager.startDiscovering()
            }

            override val discoverServices = suspend {
                Discovering(mtu, deviceConnectionManager)
            }

            override fun didUpdateMtu(mtu: Int) = suspend { copy(mtu = mtu) }
        }

        data class Discovering constructor(
            override val mtu: Int?,
            override val deviceConnectionManager: DeviceConnectionManager
        ) : Connected(),
            ConnectableDeviceState.Connected.Discovering,
            HandleAfterOldStateIsRemoved<ConnectableDeviceState> {

            override fun didDiscoverServices(services: List<Service>): suspend () -> Idle {
                return { Idle(mtu, services, deviceConnectionManager) }
            }

            override fun didUpdateMtu(mtu: Int) = suspend { copy(mtu = mtu) }

            override suspend fun afterOldStateIsRemoved(oldState: ConnectableDeviceState) {
                deviceConnectionManager.discoverServices()
            }
        }

        data class Idle constructor(
            override val mtu: Int?,
            override val services: List<Service>,
            override val deviceConnectionManager: DeviceConnectionManager
        ) : Connected(), ConnectableDeviceState.Connected.Idle {

            override fun handleAction(action: DeviceAction) = suspend { HandlingAction(action, emptyList(), mtu, services, deviceConnectionManager) }
            override fun didUpdateMtu(mtu: Int) = suspend { copy(mtu = mtu) }
        }

        data class HandlingAction constructor(
            override val action: DeviceAction,
            override val nextActions: List<DeviceAction>,
            override val mtu: Int?,
            override val services: List<Service>,
            override val deviceConnectionManager: DeviceConnectionManager
        ) : Connected(), ConnectableDeviceState.Connected.HandlingAction, HandleAfterOldStateIsRemoved<ConnectableDeviceState> {

            override fun addAction(newAction: DeviceAction) = suspend { HandlingAction(action, listOf(*nextActions.toTypedArray(), newAction), mtu, services, deviceConnectionManager) }
            override fun didUpdateMtu(mtu: Int) = suspend { copy(mtu = mtu) }

            override val actionCompleted: suspend () -> ConnectableDeviceState.Connected.DiscoveredServices = suspend {
                when (action) {
                    is DeviceAction.Read.Characteristic -> action.characteristic.updateValue()
                    is DeviceAction.Read.Descriptor -> action.descriptor.updateValue()
                    is DeviceAction.Write.Characteristic -> action.characteristic.updateValue()
                    is DeviceAction.Write.Descriptor -> action.descriptor.updateValue()
                    is DeviceAction.Notification.Enable -> action.characteristic.updateValue()
                    is DeviceAction.Notification.Disable -> { }
                }
                if (nextActions.isEmpty()) {
                    Idle(mtu, services, deviceConnectionManager)
                } else {
                    val nextAction = nextActions.first()
                    val remainingActions = nextActions.drop(1)
                    HandlingAction(nextAction, remainingActions, mtu, services, deviceConnectionManager)
                }
            }

            override suspend fun afterOldStateIsRemoved(oldState: ConnectableDeviceState) {
                if (oldState is HandlingAction && oldState.action == action) return
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

        suspend fun pair() = deviceConnectionManager.pair()
    }

    data class Connecting constructor(
        override val deviceConnectionManager: DeviceConnectionManager
    ) : ConnectableDeviceStateImpl(), ConnectableDeviceState.Connecting, HandleAfterOldStateIsRemoved<ConnectableDeviceState> {

        override val cancelConnection = disconnecting

        override fun handleCancel() = deviceConnectionManager.cancelConnecting()

        override val didConnect = suspend {
            Connected.NoServices(null, deviceConnectionManager)
        }

        override suspend fun afterOldStateIsRemoved(oldState: ConnectableDeviceState) {
            when (oldState) {
                is Disconnected -> deviceConnectionManager.connect()
                else -> {
                    // do nothing: TODO check all these are correct, e.g. Disconnecting
                }
            }
        }
    }

    data class Reconnecting constructor(
        override val attempt: Int,
        override val services: List<Service>?,
        override val deviceConnectionManager: DeviceConnectionManager
    ) : ConnectableDeviceStateImpl(), ConnectableDeviceState.Reconnecting, HandleAfterOldStateIsRemoved<ConnectableDeviceState> {

        override fun handleCancel() = deviceConnectionManager.cancelConnecting()

        override val cancelConnection = disconnecting

        override val raiseAttempt = suspend { Reconnecting(attempt + 1, services, deviceConnectionManager) }
        override val didConnect: suspend () -> ConnectableDeviceState.Connected = suspend {
            services?.let { Connected.Idle(null, services, deviceConnectionManager) } ?: Connected.NoServices(null, deviceConnectionManager)
        }

        override suspend fun afterOldStateIsRemoved(oldState: ConnectableDeviceState) {
            when (oldState) {
                is Connected, is Reconnecting -> deviceConnectionManager.connect()
                else -> {}
            }
        }
    }

    data class Disconnected constructor(
        override val deviceConnectionManager: DeviceConnectionManager
    ) : ConnectableDeviceStateImpl(), ConnectableDeviceState.Disconnected {

        override fun startConnecting() = deviceConnectionManager.startConnecting()

        override val connect = suspend { Connecting(deviceConnectionManager) }
    }

    data class Disconnecting constructor(
        override val deviceConnectionManager: DeviceConnectionManager
    ) : ConnectableDeviceStateImpl(), ConnectableDeviceState.Disconnecting, HandleAfterOldStateIsRemoved<ConnectableDeviceState> {

        override suspend fun afterOldStateIsRemoved(oldState: ConnectableDeviceState) {
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

    suspend fun unpair() = deviceConnectionManager.unpair()
}
