package com.splendo.kaluga.bluetooth.device

import com.splendo.kaluga.state.StateRepoAccesor

internal abstract class BaseDeviceConnectionManager(var reconnectionAttempts: Int = 0,
                                                    internal val deviceInfoHolder: DeviceInfoHolder,
                                                    internal val repoAccessor: StateRepoAccesor<DeviceState>) {

    interface Builder {
        fun create(reconnectionAttempts: Int, deviceInfo: DeviceInfoHolder, repoAccessor: StateRepoAccesor<DeviceState>): DeviceConnectionManager
    }

    abstract suspend fun connect()
    abstract suspend fun discoverServices()
    abstract suspend fun disconnect()
    abstract suspend fun readRssi()
    abstract suspend fun performAction(action: DeviceAction): Boolean
}

internal expect class DeviceConnectionManager : BaseDeviceConnectionManager