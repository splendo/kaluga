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
import com.splendo.kaluga.state.HotStateRepo
import com.splendo.kaluga.state.State
import com.splendo.kaluga.state.StateRepo
import com.splendo.kaluga.state.StateRepoAccesor

sealed class DeviceAction {
    sealed class Read : DeviceAction() {
        class Characteristic(val characteristic: com.splendo.kaluga.bluetooth.Characteristic) : Read()
        class Descriptor(val descriptor: com.splendo.kaluga.bluetooth.Descriptor) : Read()
    }
    sealed class Write(val newValue: ByteArray?) : DeviceAction() {
        class Characteristic(newValue: ByteArray?, val characteristic: com.splendo.kaluga.bluetooth.Characteristic) : Write(newValue)
        class Descriptor(newValue: ByteArray?, val descriptor: com.splendo.kaluga.bluetooth.Descriptor) : Write(newValue)
    }

    data class Notification(val characteristic: com.splendo.kaluga.bluetooth.Characteristic, val enable: Boolean) : DeviceAction()

}

sealed class DeviceState (open val lastKnownRssi: Int, internal open val connectionManager: DeviceConnectionManager) : State<DeviceState>(connectionManager.repoAccessor), DeviceInfo by connectionManager.deviceInfoHolder {

    sealed class Connected(open val services: List<Service>, lastKnownRssi: Int, connectionManager: DeviceConnectionManager) : DeviceState(lastKnownRssi, connectionManager) {

        data class Idle internal constructor(override val services: List<Service>, override val lastKnownRssi: Int, override val connectionManager: DeviceConnectionManager): Connected(services, lastKnownRssi, connectionManager) {

            suspend fun discoverServices() {
                changeState(Discovering(lastKnownRssi, connectionManager))
            }

            suspend fun handleAction(action: DeviceAction) {
                changeState(HandlingAction(action, emptyList(), services, lastKnownRssi, connectionManager))
            }

        }

        data class Discovering internal constructor(override val lastKnownRssi: Int, override val connectionManager: DeviceConnectionManager): Connected(emptyList(), lastKnownRssi, connectionManager) {

            internal suspend fun didDiscoverServices(services: List<Service>) {
                changeState(Idle(services, lastKnownRssi, connectionManager))
            }

            override suspend fun afterNewStateIsSet() {
                super.afterNewStateIsSet()

                connectionManager.discoverServices()
            }

        }

        data class HandlingAction internal constructor(internal val action: DeviceAction, internal val nextActions: List<DeviceAction>, override val services: List<Service>, override val lastKnownRssi: Int, override val connectionManager: DeviceConnectionManager): Connected(services, lastKnownRssi, connectionManager) {

            suspend fun addAction(newAction: DeviceAction) {
                changeState(HandlingAction(action, listOf(*nextActions.toTypedArray(), newAction), services, lastKnownRssi, connectionManager))
            }

            internal suspend fun actionCompleted() {
                when (action) {
                    is DeviceAction.Read.Characteristic -> action.characteristic.updateValue()
                    is DeviceAction.Write.Characteristic -> action.characteristic.updateValue()
                    is DeviceAction.Notification -> {
                        if (action.enable)
                            action.characteristic.updateValue()
                    }
                }

                if (nextActions.isEmpty()) {
                    changeState(Idle(services, lastKnownRssi, connectionManager))
                } else {
                    val nextAction = nextActions.first()
                    val remainingActions = nextActions.drop(1)
                    changeState(HandlingAction(nextAction, remainingActions, services, lastKnownRssi, connectionManager))
                }
            }

            override suspend fun afterOldStateIsRemoved(oldState: DeviceState) {
                super.afterOldStateIsRemoved(oldState)

                when (oldState) {
                    is HandlingAction -> {
                        if (oldState.action == action)
                            return
                    }
                }

                connectionManager.performAction(action)
            }

        }

        suspend fun disconnect() {
            changeState(Disconnecting(lastKnownRssi, connectionManager))
        }

        suspend fun reconnect() {
            changeState(Reconnecting(0, services, lastKnownRssi, connectionManager))
        }

        suspend fun readRssi() {
            connectionManager.readRssi()
        }

        override suspend fun finalState() {
            super.finalState()

            connectionManager.disconnect()
        }
    }
    data class Connecting internal constructor(override val lastKnownRssi: Int, override val connectionManager: DeviceConnectionManager) : DeviceState(lastKnownRssi, connectionManager) {

        suspend fun cancelConnection() {
            changeState(Disconnecting(lastKnownRssi, connectionManager))
        }

        internal suspend fun didConnect() {
            changeState(Connected.Idle(emptyList(), lastKnownRssi, connectionManager))
        }

        override suspend fun afterOldStateIsRemoved(oldState: DeviceState) {
            super.afterOldStateIsRemoved(oldState)

            when(oldState) {
                is Disconnected -> connectionManager.connect()
            }
        }
    }

    data class Reconnecting internal constructor(val attempt: Int, val services: List<Service>, override val lastKnownRssi: Int, override val connectionManager: DeviceConnectionManager) : DeviceState(lastKnownRssi, connectionManager) {

        suspend fun retry() {
            changeState(copy(attempt = attempt))
        }

        internal suspend fun didConnect() {
            changeState(Connected.Idle(services, lastKnownRssi, connectionManager))
        }

        override suspend fun afterOldStateIsRemoved(oldState: DeviceState) {
            super.afterOldStateIsRemoved(oldState)

            when (oldState) {
                is Connected, is Reconnecting -> connectionManager.connect()
            }
        }

    }

    data class Disconnected internal constructor(override val lastKnownRssi: Int, override val connectionManager: DeviceConnectionManager) : DeviceState(lastKnownRssi, connectionManager) {

        suspend fun connect() {
            changeState(Connecting(lastKnownRssi, connectionManager))
        }

    }
    data class Disconnecting internal constructor(override val lastKnownRssi: Int, override val connectionManager: DeviceConnectionManager) : DeviceState(lastKnownRssi, connectionManager) {

        override suspend fun afterOldStateIsRemoved(oldState: DeviceState) {
            super.afterOldStateIsRemoved(oldState)

            when (oldState) {
                is Connecting, is Connected -> connectionManager.disconnect()
            }
        }
    }

    suspend fun rssiDidUpdate(rssi: Int) {
        val newState = when (this) {
            is Connected.Idle -> copy(lastKnownRssi = rssi)
            is Connected.HandlingAction -> copy(lastKnownRssi = rssi)
            is Connected.Discovering -> copy(lastKnownRssi = rssi)
            is Connecting -> copy(lastKnownRssi = rssi)
            is Reconnecting -> copy(lastKnownRssi = rssi)
            is Disconnecting -> copy(lastKnownRssi = rssi)
            is Disconnected -> copy(lastKnownRssi = rssi)
        }
        changeState(newState)
    }

    internal suspend fun didDisconnect() {
        changeState(Disconnected(lastKnownRssi, connectionManager))
    }

}

class Device internal constructor(private val reconnectionAttempts: Int = 0, private val deviceInfoHolder: DeviceInfoHolder, private val initialRssi: Int, connectionBuilder: BaseDeviceConnectionManager.Builder) : HotStateRepo<DeviceState>(), DeviceInfo by deviceInfoHolder {

    internal val deviceConnectionManager = connectionBuilder.create(reconnectionAttempts, deviceInfoHolder, StateRepoAccesor(this))

    override fun initialValue(): DeviceState {
        return DeviceState.Disconnected(initialRssi, deviceConnectionManager)
    }

}