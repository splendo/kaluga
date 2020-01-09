package com.splendo.kaluga.bluetooth.device

import com.splendo.kaluga.bluetooth.UUID

interface BaseAdvertisementData {
    val name: String?
    val manufacturerData: ByteArray?
    val serviceUUIDs: List<UUID>
    val serviceData: Map<UUID, ByteArray?>
    val txPowerLevel: Int
}

expect class AdvertisementData : BaseAdvertisementData