package com.splendo.kaluga.bluetooth.device

import android.bluetooth.BluetoothDevice
import android.content.Context

actual class DeviceInfoHolder(internal val device: BluetoothDevice, override val advertisementData: AdvertisementData, private val context: Context) : DeviceInfo {

    override val name: String?
        get() = device.name

    override val identifier: Identifier
        get() = device.address
}

actual typealias Identifier = String