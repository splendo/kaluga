package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.state.StateRepoAccesor
import java.util.concurrent.atomic.AtomicReference

actual class Descriptor(override val uuid: UUID, initialValue: ByteArray?, stateRepoAccesor: StateRepoAccesor<DeviceState>) : BaseDescriptor(initialValue, stateRepoAccesor) {

    val value = AtomicReference(initialValue)

    override fun createReadAction(): DeviceAction.Read.Descriptor {
        return DeviceAction.Read.Descriptor(this)
    }

    override fun createWriteAction(newValue: ByteArray?): DeviceAction.Write.Descriptor {
        value.set(newValue)
        return DeviceAction.Write.Descriptor(newValue, this)
    }

    override fun getUpdatedValue(): ByteArray? {
        return value.get()
    }
}