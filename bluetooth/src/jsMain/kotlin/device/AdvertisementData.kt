package com.splendo.kaluga.bluetooth.device

import com.splendo.kaluga.bluetooth.UUID

actual class AdvertisementData : BaseAdvertisementData {

    override val name: String?
        get() = null
    override val manufacturerData: ByteArray?
        get() = null
    override val serviceUUIDs: List<UUID>
        get() = emptyList()
    override val serviceData: Map<UUID, ByteArray?>
        get() = emptyMap()
    override val txPowerLevel: Int
        get() = Int.MIN_VALUE
}