package com.splendo.kaluga.bluetooth

import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBService

actual class Device(private val peripheral: CBPeripheral) : BaseDevice {

    override val uuid: List<UUID>
        get() =
            peripheral.services?.let { services ->
                services.map {
                    val service = it as CBService
                    UUID(service.UUID)
                }
            } ?: emptyList()
}

