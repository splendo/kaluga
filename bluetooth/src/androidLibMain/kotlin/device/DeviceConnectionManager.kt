package com.splendo.kaluga.bluetooth.device

import com.splendo.kaluga.bluetooth.Characteristic
import com.splendo.kaluga.state.StateRepoAccesor

internal actual class DeviceConnectionManager(deviceInfoHolder: DeviceInfoHolder, repoAccessor: StateRepoAccesor<DeviceState>) : BaseDeviceConnectionManager(deviceInfoHolder, repoAccessor) {

    class Builder : BaseDeviceConnectionManager.Builder {
        override fun create(deviceInfo: DeviceInfoHolder, repoAccessor: StateRepoAccesor<DeviceState>): DeviceConnectionManager {
            return DeviceConnectionManager(deviceInfo, repoAccessor)
        }
    }

    val device = deviceInfoHolder.device

    override suspend fun connect() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun discoverServices() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun disconnect() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun readCharacteristic(characteristic: Characteristic) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun readDescriptor() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun setCharacteristicNotification(characteristic: Characteristic, enabled: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun writeCharacteristic(characteristic: Characteristic) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun writeDescriptor() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}