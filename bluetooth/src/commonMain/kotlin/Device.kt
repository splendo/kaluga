package com.splendo.kaluga.bluetooth


expect class Identifier

interface BaseAdvertisementData {
    val name: String?
    val manufacturerData: ByteArray?
    val serviceUUIDs: List<UUID>
    val serviceData: Map<UUID, ByteArray?>
    val txPowerLevel: Int
}

expect class AdvertisementData {

}

interface BaseDevice {
    val name: String?
    val identifier: Identifier
    val advertisementData: AdvertisementData
}

expect class Device : BaseDevice {

}