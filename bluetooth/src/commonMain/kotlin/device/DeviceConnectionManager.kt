package com.splendo.kaluga.bluetooth.device

import com.splendo.kaluga.bluetooth.Characteristic
import com.splendo.kaluga.state.StateRepoAccesor

internal abstract class BaseDeviceConnectionManager(internal val deviceInfoHolder: DeviceInfoHolder,
                                                    internal val repoAccessor: StateRepoAccesor<DeviceState>) {

    interface Builder {
        fun create(deviceInfo: DeviceInfoHolder, repoAccessor: StateRepoAccesor<DeviceState>): DeviceConnectionManager
    }

    abstract suspend fun connect()
    abstract suspend fun discoverServices()
    abstract suspend fun disconnect()
    abstract suspend fun readRssi()
    abstract suspend fun readCharacteristic(characteristic: Characteristic)
    abstract suspend fun readDescriptor()
    abstract suspend fun setCharacteristicNotification(characteristic: Characteristic, enabled: Boolean)
    abstract suspend fun writeCharacteristic(characteristic: Characteristic)
    abstract suspend fun writeDescriptor()
}

internal expect class DeviceConnectionManager : BaseDeviceConnectionManager