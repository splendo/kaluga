package com.splendo.kaluga.bluetooth.device

import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBPeripheral
import platform.Foundation.NSUUID

actual class DeviceInfoHolder(internal val peripheral: CBPeripheral,
                              internal val centralManager: CBCentralManager,
                              override val advertisementData: AdvertisementData) : DeviceInfo {

    override val name: String?
        get() =
            peripheral.name

    override val identifier: Identifier
        get() = peripheral.identifier
}

actual typealias Identifier = NSUUID