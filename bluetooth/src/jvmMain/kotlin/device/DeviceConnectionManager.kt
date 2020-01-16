package com.splendo.kaluga.bluetooth.device

import com.splendo.kaluga.state.StateRepoAccesor

internal actual class DeviceConnectionManager(reconnectionAttempts: Int, deviceInfoHolder: DeviceInfoHolder, repoAccessor: StateRepoAccesor<DeviceState>) : BaseDeviceConnectionManager(reconnectionAttempts, deviceInfoHolder, repoAccessor) {

    class Builder() : BaseDeviceConnectionManager.Builder {

        override fun create(reconnectionAttempts: Int, deviceInfo: DeviceInfoHolder, repoAccessor: StateRepoAccesor<DeviceState>): DeviceConnectionManager {
            return DeviceConnectionManager(reconnectionAttempts, deviceInfo, repoAccessor)
        }
    }

    override suspend fun connect() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun discoverServices() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun disconnect() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun readRssi() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun performAction(action: DeviceAction): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}