package com.splendo.kaluga.bluetooth

import android.bluetooth.BluetoothGattDescriptor
import android.os.ParcelUuid
import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.state.StateRepoAccesor

actual class Descriptor(val descriptor: BluetoothGattDescriptor, stateRepoAccessor: StateRepoAccesor<DeviceState>) : BaseDescriptor(descriptor.value, stateRepoAccessor) {

    override val uuid: UUID
        get() = UUID(ParcelUuid(descriptor.uuid))

    override fun createReadAction(): DeviceAction.Read.Descriptor {
        return DeviceAction.Read.Descriptor(this)
    }

    override fun createWriteAction(newValue: ByteArray?): DeviceAction.Write.Descriptor {
        return DeviceAction.Write.Descriptor(newValue, this)
    }

    override fun getUpdatedValue(): ByteArray? {
        return descriptor.value
    }
}