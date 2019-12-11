package com.splendo.kaluga.bluetooth

import android.bluetooth.*
import android.content.Context


actual class Device(private val device: BluetoothDevice, private val context: Context) : BaseDevice {

    override val uuid: List<UUID>
        get() = device.uuids.map { UUID(it) }
}
