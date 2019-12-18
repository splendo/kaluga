package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.base.toByteArray
import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.state.StateRepoAccesor
import platform.CoreBluetooth.CBDescriptor
import platform.Foundation.NSData

actual class Descriptor(private val descriptor: CBDescriptor, stateRepoAccessor: StateRepoAccesor<DeviceState>) : BaseDescriptor((descriptor.value as? NSData)?.toByteArray(), stateRepoAccessor) {

    override val uuid: UUID
        get() = UUID(descriptor.UUID)

    override fun createReadAction(): DeviceAction.Read.Descriptor {
        return DeviceAction.Read.Descriptor(this)
    }

    override fun createWriteAction(newValue: ByteArray?): DeviceAction.Write.Descriptor {
        return DeviceAction.Write.Descriptor(newValue, this)
    }

    override fun getUpdatedValue(): ByteArray? {
        return (descriptor.value as? NSData)?.toByteArray()
    }
}

