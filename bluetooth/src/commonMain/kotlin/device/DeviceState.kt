package com.splendo.kaluga.bluetooth.device

import com.splendo.kaluga.state.State
import com.splendo.kaluga.state.StateRepo
import com.splendo.kaluga.state.StateRepoAccesor

sealed class DeviceAction {
    sealed class Read : DeviceAction() {
        sealed class Characteristic(characteristic: com.splendo.kaluga.bluetooth.Characteristic) : Read()
    }
    sealed class Write : DeviceAction() {
        sealed class Characteristic(characteristic: com.splendo.kaluga.bluetooth.Characteristic) : Write()
    }
}

sealed class DeviceState (internal val connectionManager: DeviceConnectionManager) : State<DeviceState>(connectionManager.repoAccessor), DeviceInfo by connectionManager.deviceInfoHolder {

    sealed class Connected(connectionManager: DeviceConnectionManager) : DeviceState(connectionManager) {

        class Idle internal constructor(device: DeviceInfoHolder, connectionManager: DeviceConnectionManager): Connected(connectionManager) {

            suspend fun discoverServices() {

            }

            suspend fun handleAction(action: DeviceAction) {

            }

        }
        class Discovering internal constructor(connectionManager: DeviceConnectionManager): Connected(connectionManager)
        class HandlingAction internal constructor(action: DeviceAction, nextActions: List<DeviceAction>, connectionManager: DeviceConnectionManager): Connected(connectionManager)

        suspend fun disconnect() {

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

        }

    }
    class Disconnected internal constructor(connectionManager: DeviceConnectionManager) : DeviceState(connectionManager) {

        suspend fun connect() {
            
        }

    }
    class Disconnecting internal constructor(connectionManager: DeviceConnectionManager) : DeviceState(connectionManager)

}

class Device internal constructor(private val deviceInfoHolder: DeviceInfoHolder, connectionBuilder: BaseDeviceConnectionManager.Builder) : StateRepo<DeviceState>(), DeviceInfo by deviceInfoHolder {

    private val deviceConnectionManager = connectionBuilder.create(deviceInfoHolder, StateRepoAccesor(this))

    override fun initialState(): DeviceState {
        return DeviceState.Disconnected(deviceConnectionManager)
    }

}