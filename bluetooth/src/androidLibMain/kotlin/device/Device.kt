package com.splendo.kaluga.bluetooth.device

import android.bluetooth.*
import android.content.Context
import com.splendo.kaluga.bluetooth.UUID
import no.nordicsemi.android.support.v18.scanner.ScanRecord


actual class Device(private val device: BluetoothDevice, override val advertisementData: AdvertisementData, private val context: Context) : BaseDevice {

    override val name: String?
        get() = device.name

    override val identifier: Identifier
        get() = device.address
}

actual typealias Identifier = String