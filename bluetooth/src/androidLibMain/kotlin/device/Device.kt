package com.splendo.kaluga.bluetooth.device

import android.bluetooth.BluetoothDevice
import android.content.Context

actual class Device(private val device: BluetoothDevice, override val advertisementData: AdvertisementData, private val context: Context) : BaseDevice {

    override val name: String?
        get() = device.name

    override val identifier: Identifier
        get() = device.address
}

actual typealias Identifier = String