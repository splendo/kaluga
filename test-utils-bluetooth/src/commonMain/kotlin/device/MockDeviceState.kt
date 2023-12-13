/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.test.bluetooth.device

import com.splendo.kaluga.base.state.HandleAfterOldStateIsRemoved
import com.splendo.kaluga.base.state.KalugaState
import com.splendo.kaluga.bluetooth.MTU
import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.device.ConnectableDeviceState
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.NotConnectableDeviceState

sealed class MockDeviceState : KalugaState {
    data object NotConnectable : MockDeviceState(), NotConnectableDeviceState
    sealed class Connectable : MockDeviceState() {

        abstract val mockConnectableDeviceManager: MockConnectableDeviceManager

        val didDisconnect: suspend () -> Disconnected = { Disconnected(mockConnectableDeviceManager) }

        val disconnecting: suspend () -> Disconnecting = { Disconnecting(mockConnectableDeviceManager) }

        suspend fun unpair() = mockConnectableDeviceManager.unpair()

        abstract val asDeviceState: ConnectableDeviceState
    }
    sealed class Connected : Connectable() {

        fun startDisconnected() = mockConnectableDeviceManager.startDisconnected()

        suspend fun readRssi() = mockConnectableDeviceManager.readRssi()

        suspend fun requestMtu(mtu: MTU): Boolean = mockConnectableDeviceManager.requestMtu(mtu)

        suspend fun pair() = mockConnectableDeviceManager.pair()

        data class NoServices(
            override val reconnectionSettings: ConnectionSettings.ReconnectionSettings,
            override val mtu: MTU?,
            override val mockConnectableDeviceManager: MockConnectableDeviceManager,
        ) : Connected(), ConnectableDeviceState.Connected.NoServices {

            override fun startDiscovering() = mockConnectableDeviceManager.startDiscovering()

            override val discoverServices: suspend () -> ConnectableDeviceState.Connected.Discovering = { Discovering(reconnectionSettings, mtu, mockConnectableDeviceManager) }
            override fun didUpdateMtu(mtu: MTU) = suspend {
                copy(mtu = mtu)
            }
            override fun updateReconnectionSettings(reconnectionSettings: ConnectionSettings.ReconnectionSettings) = suspend {
                copy(reconnectionSettings = reconnectionSettings)
            }

            override val reconnect: suspend () -> ConnectableDeviceState.Connecting = { Connecting(reconnectionSettings, mockConnectableDeviceManager) }

            override val asDeviceState: ConnectableDeviceState = this
        }

        data class Discovering(
            override val reconnectionSettings: ConnectionSettings.ReconnectionSettings,
            override val mtu: MTU?,
            override val mockConnectableDeviceManager: MockConnectableDeviceManager,
        ) : Connected(), ConnectableDeviceState.Connected.Discovering, HandleAfterOldStateIsRemoved<MockDeviceState> {
            override fun didDiscoverServices(services: List<Service>): suspend () -> ConnectableDeviceState.Connected.Idle = {
                Idle(reconnectionSettings, mtu, services, mockConnectableDeviceManager)
            }
            override fun didUpdateMtu(mtu: MTU) = suspend {
                copy(mtu = mtu)
            }
            override fun updateReconnectionSettings(reconnectionSettings: ConnectionSettings.ReconnectionSettings) = suspend {
                copy(reconnectionSettings = reconnectionSettings)
            }
            override val reconnect: suspend () -> ConnectableDeviceState.Connecting = { Connecting(reconnectionSettings, mockConnectableDeviceManager) }

            override val asDeviceState: ConnectableDeviceState = this

            override suspend fun afterOldStateIsRemoved(oldState: MockDeviceState) {
                mockConnectableDeviceManager.discoverServices()
            }
        }

        sealed class DiscoveredServices : Connected()
        data class Idle(
            override val reconnectionSettings: ConnectionSettings.ReconnectionSettings,
            override val mtu: MTU?,
            override val services: List<Service>,
            override val mockConnectableDeviceManager: MockConnectableDeviceManager,
        ) : DiscoveredServices(), ConnectableDeviceState.Connected.Idle {
            override fun handleAction(action: DeviceAction): suspend () -> ConnectableDeviceState.Connected.HandlingAction = {
                HandlingAction(reconnectionSettings, mtu, services, action, emptyList(), mockConnectableDeviceManager)
            }
            override fun didUpdateMtu(mtu: MTU) = suspend {
                copy(mtu = mtu)
            }
            override fun updateReconnectionSettings(reconnectionSettings: ConnectionSettings.ReconnectionSettings) = suspend {
                copy(reconnectionSettings = reconnectionSettings)
            }

            override val reconnect: suspend () -> ConnectableDeviceState.Connecting = { Connecting(reconnectionSettings, mockConnectableDeviceManager) }

            override val asDeviceState: ConnectableDeviceState = this
        }
        data class HandlingAction(
            override val reconnectionSettings: ConnectionSettings.ReconnectionSettings,
            override val mtu: MTU?,
            override val services: List<Service>,
            override val action: DeviceAction,
            override val nextActions: List<DeviceAction>,
            override val mockConnectableDeviceManager: MockConnectableDeviceManager,
        ) : DiscoveredServices(), ConnectableDeviceState.Connected.HandlingAction {

            override fun addAction(newAction: DeviceAction): suspend () -> ConnectableDeviceState.Connected.HandlingAction = {
                HandlingAction(reconnectionSettings, mtu, services, action, nextActions + newAction, mockConnectableDeviceManager)
            }

            override val actionCompleted: suspend () -> ConnectableDeviceState.Connected.DiscoveredServices = {
                nextActions.firstOrNull()?.let {
                    HandlingAction(reconnectionSettings, mtu, services, it, nextActions.drop(1), mockConnectableDeviceManager)
                } ?: Idle(reconnectionSettings, mtu, services, mockConnectableDeviceManager)
            }

            override fun didUpdateMtu(mtu: MTU) = suspend {
                copy(mtu = mtu)
            }
            override fun updateReconnectionSettings(reconnectionSettings: ConnectionSettings.ReconnectionSettings) = suspend {
                copy(reconnectionSettings = reconnectionSettings)
            }

            override val reconnect: suspend () -> ConnectableDeviceState.Connecting = { Connecting(reconnectionSettings, mockConnectableDeviceManager) }

            override val asDeviceState: ConnectableDeviceState = this
        }
    }

    data class Connecting(
        private val reconnectionSettings: ConnectionSettings.ReconnectionSettings,
        override val mockConnectableDeviceManager: MockConnectableDeviceManager,
    ) : Connectable(), ConnectableDeviceState.Connecting, HandleAfterOldStateIsRemoved<MockDeviceState> {

        override val cancelConnection: suspend () -> Disconnecting = { Disconnecting(mockConnectableDeviceManager) }

        override val didConnect: suspend () -> ConnectableDeviceState.Connected.NoServices = { Connected.NoServices(reconnectionSettings, null, mockConnectableDeviceManager) }

        override fun handleCancel() = mockConnectableDeviceManager.handleCancelConnecting()

        override val asDeviceState: ConnectableDeviceState = this

        override suspend fun afterOldStateIsRemoved(oldState: MockDeviceState) {
            when (oldState) {
                is Disconnected -> mockConnectableDeviceManager.connect()
                else -> Unit
            }
        }
    }

    data class Disconnected(override val mockConnectableDeviceManager: MockConnectableDeviceManager) : Connectable(), ConnectableDeviceState.Disconnected {

        override fun connect(reconnectionSettings: ConnectionSettings.ReconnectionSettings): suspend () -> ConnectableDeviceState.Connecting = {
            Connecting(reconnectionSettings, mockConnectableDeviceManager)
        }

        override fun startConnecting(reconnectionSettings: ConnectionSettings.ReconnectionSettings?) = mockConnectableDeviceManager.startConnecting(reconnectionSettings)

        override val asDeviceState: ConnectableDeviceState = this
    }

    data class Disconnecting(
        override val mockConnectableDeviceManager: MockConnectableDeviceManager,
    ) : Connectable(), ConnectableDeviceState.Disconnecting, HandleAfterOldStateIsRemoved<MockDeviceState> {
        override val asDeviceState: ConnectableDeviceState = this

        override suspend fun afterOldStateIsRemoved(oldState: MockDeviceState) {
            when (oldState) {
                is Connecting, is Connected -> mockConnectableDeviceManager.disconnect()
                else -> Unit
            }
        }
    }
}
