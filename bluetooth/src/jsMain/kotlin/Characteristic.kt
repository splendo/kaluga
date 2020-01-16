package com.splendo.kaluga.bluetooth

import co.touchlab.stately.concurrency.AtomicReference
import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.state.StateRepoAccesor

actual class Characteristic(override val uuid: UUID, override val descriptors: List<Descriptor>, initialValue: ByteArray?, stateRepoAccesor: StateRepoAccesor<DeviceState>) : BaseCharacteristic(initialValue, stateRepoAccesor) {

    var value = AtomicReference(initialValue)

    override fun createNotificationAction(enabled: Boolean): DeviceAction.Notification {
        return DeviceAction.Notification(this, enabled)
    }

    override fun createReadAction(): DeviceAction.Read.Characteristic {
        return DeviceAction.Read.Characteristic(this)
    }

    override fun createWriteAction(newValue: ByteArray?): DeviceAction.Write.Characteristic {
        value.set(newValue)
        return DeviceAction.Write.Characteristic(newValue, this)
    }

    override fun getUpdatedValue(): ByteArray? {
        return value.get()
    }
}