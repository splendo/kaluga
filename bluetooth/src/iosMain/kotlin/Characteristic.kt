package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.base.toByteArray
import com.splendo.kaluga.base.typedList
import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.state.StateRepoAccesor
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBDescriptor

actual class Characteristic(val characteristic: CBCharacteristic, val stateRepoAccesor: StateRepoAccesor<DeviceState>) : BaseCharacteristic(characteristic.value?.toByteArray(), stateRepoAccesor) {

    override val uuid: UUID
        get() = UUID(characteristic.UUID)

    override val descriptors: List<Descriptor>
        get() = characteristic.descriptors?.typedList<CBDescriptor>()?.map { Descriptor(it, stateRepoAccessor) } ?: emptyList()

    override fun createReadAction(): DeviceAction.Read.Characteristic {
        return DeviceAction.Read.Characteristic(this)
    }

    override fun createWriteAction(newValue: ByteArray?): DeviceAction.Write.Characteristic {
        return DeviceAction.Write.Characteristic(newValue, this)
    }

    override fun createNotificationAction(enabled: Boolean): DeviceAction.Notification {
        return DeviceAction.Notification(this, enabled)
    }

    override fun getUpdatedValue(): ByteArray? {
        return characteristic.value?.toByteArray()
    }
}

