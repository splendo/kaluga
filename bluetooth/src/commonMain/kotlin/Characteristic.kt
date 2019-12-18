package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.state.StateRepoAccesor

abstract class BaseCharacteristic(initialValue: ByteArray? = null, stateRepoAccessor: StateRepoAccesor<DeviceState>) : Attribute<DeviceAction.Read.Characteristic, DeviceAction.Write.Characteristic>(initialValue, stateRepoAccessor) {

    abstract val descriptors: List<Descriptor>
    var isNotifying: Boolean = false

    suspend fun enableNotification() {
        if (!isNotifying)
            addAction(createNotificationAction(true))
        isNotifying = true
    }

    suspend fun disableNotification() {
        if (isNotifying)
            addAction(createNotificationAction(false))
        isNotifying = false
    }

    internal abstract fun createNotificationAction(enabled: Boolean): DeviceAction.Notification

}

expect class Characteristic : BaseCharacteristic

