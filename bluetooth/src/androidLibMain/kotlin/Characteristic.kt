package com.splendo.kaluga.bluetooth

import android.bluetooth.BluetoothGattCharacteristic
import android.os.ParcelUuid
import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.state.StateRepoAccesor

actual class Characteristic(val characteristic: BluetoothGattCharacteristic, stateRepoAccesor: StateRepoAccesor<DeviceState>) :
    BaseCharacteristic(characteristic.value, stateRepoAccesor) {

    override val uuid: UUID
        get() = UUID(ParcelUuid(characteristic.uuid))

    override val descriptors: List<Descriptor>
        get() = characteristic.descriptors.map { Descriptor(it, stateRepoAccessor) }

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
        return characteristic.value
    }

}