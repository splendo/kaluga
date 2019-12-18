package com.splendo.kaluga.bluetooth.device

import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.state.State
import com.splendo.kaluga.state.StateRepo
import com.splendo.kaluga.state.StateRepoAccesor

sealed class DeviceAction {
    sealed class Read : DeviceAction() {
        class Characteristic(val characteristic: com.splendo.kaluga.bluetooth.Characteristic) : Read()
        class Descriptor(val characteristic: com.splendo.kaluga.bluetooth.Descriptor) : Read()
    }
    sealed class Write(val newValue: ByteArray?) : DeviceAction() {
        class Characteristic(newValue: ByteArray?, val characteristic: com.splendo.kaluga.bluetooth.Characteristic) : Write(newValue)
        class Descriptor(newValue: ByteArray?, val characteristic: com.splendo.kaluga.bluetooth.Descriptor) : Write(newValue)
    }

    data class Notification(val characteristic: com.splendo.kaluga.bluetooth.Characteristic, val enable: Boolean) : DeviceAction()

}

sealed class DeviceState (internal val connectionManager: DeviceConnectionManager) : State<DeviceState>(connectionManager.repoAccessor), DeviceInfo by connectionManager.deviceInfoHolder {

    sealed class Connected(connectionManager: DeviceConnectionManager) : DeviceState(connectionManager) {

        class Idle internal constructor(val services: List<Service>, connectionManager: DeviceConnectionManager): Connected(connectionManager) {

            suspend fun discoverServices() {
                changeState(Discovering(connectionManager))
            }

            suspend fun handleAction(action: DeviceAction) {
                changeState(HandlingAction(action, emptyList(), services, connectionManager))
            }

        }

        class Discovering internal constructor(connectionManager: DeviceConnectionManager): Connected(connectionManager) {

            internal suspend fun didDiscoverServices(services: List<Service>) {
                changeState(Idle(services, connectionManager))
            }

            override suspend fun afterNewStateIsSet() {
                super.afterNewStateIsSet()

                connectionManager.discoverServices()
            }

        }

        class HandlingAction internal constructor(internal val action: DeviceAction, internal val nextActions: List<DeviceAction>, private val services: List<Service>, connectionManager: DeviceConnectionManager): Connected(connectionManager) {

            suspend fun addAction(newAction: DeviceAction) {
                changeState(HandlingAction(action, listOf(*nextActions.toTypedArray(), newAction), services, connectionManager))
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
                    changeState(Idle(services, connectionManager))
                } else {
                    val nextAction = nextActions.first()
                    val remainingActions = nextActions.drop(1)
                    changeState(HandlingAction(nextAction, remainingActions, services, connectionManager))
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

                when (action) {
                    is DeviceAction.Read.Characteristic -> connectionManager.readCharacteristic(action.characteristic)
                    is DeviceAction.Write.Characteristic -> connectionManager.writeCharacteristic(action.characteristic)
                }

            }

        }

        suspend fun disconnect() {
            changeState(Disconnecting(connectionManager))
        }

        suspend fun readRssi() {
            connectionManager.readRssi()
        }

        override suspend fun finalState() {
            super.finalState()

            connectionManager.disconnect()
        }
    }
    class Connecting internal constructor(connectionManager: DeviceConnectionManager) : DeviceState(connectionManager) {

        suspend fun cancelConnection() {
            changeState(Disconnecting(connectionManager))
        }

        internal suspend fun didConnect() {
            changeState(Connected.Idle(emptyList(), connectionManager))
        }

        override suspend fun afterOldStateIsRemoved(oldState: DeviceState) {
            super.afterOldStateIsRemoved(oldState)

            when(oldState) {
                is Disconnected -> connectionManager.connect()
            }
        }
    }
    class Disconnected internal constructor(connectionManager: DeviceConnectionManager) : DeviceState(connectionManager) {

        suspend fun connect() {
            changeState(Connecting(connectionManager))
        }

    }
    class Disconnecting internal constructor(connectionManager: DeviceConnectionManager) : DeviceState(connectionManager) {

        internal suspend fun didDisconnect() {
            changeState(Disconnected(connectionManager))
        }

        override suspend fun afterOldStateIsRemoved(oldState: DeviceState) {
            super.afterOldStateIsRemoved(oldState)

            when (oldState) {
                is Connecting, is Connected -> connectionManager.disconnect()
            }
        }
    }

}

class Device internal constructor(private val deviceInfoHolder: DeviceInfoHolder, connectionBuilder: BaseDeviceConnectionManager.Builder) : StateRepo<DeviceState>(), DeviceInfo by deviceInfoHolder {

    private val deviceConnectionManager = connectionBuilder.create(deviceInfoHolder, StateRepoAccesor(this))

    override fun initialState(): DeviceState {
        return DeviceState.Disconnected(deviceConnectionManager)
    }

}