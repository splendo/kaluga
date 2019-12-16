package com.splendo.kaluga.bluetooth.device

import com.splendo.kaluga.base.toByteArray
import com.splendo.kaluga.base.typedList
import com.splendo.kaluga.base.typedMap
import com.splendo.kaluga.bluetooth.UUID
import platform.CoreBluetooth.*
import platform.Foundation.NSData

actual class AdvertisementData(private val advertisementData: Map<String, Any>) : BaseAdvertisementData {

    override val name: String?
        get() = advertisementData[CBAdvertisementDataLocalNameKey] as? String
    override val manufacturerData: ByteArray?
        get() = (advertisementData[CBAdvertisementDataManufacturerDataKey] as? NSData)?.toByteArray()
    override val serviceUUIDs: List<UUID>
        get() = {
            (advertisementData[CBAdvertisementDataServiceUUIDsKey] as? List<*>)?.
                typedList<CBUUID>()?.
                map { UUID(it) } ?: emptyList()
        }()
    override val serviceData: Map<UUID, ByteArray?>
        get() = {
            (advertisementData[CBAdvertisementDataManufacturerDataKey] as? Map<*, *>)?.
                typedMap<CBUUID, NSData>()?.
                mapNotNull { Pair(UUID(it.key), it.value.toByteArray()) }?.
                toMap() ?: emptyMap()
        }()
    override val txPowerLevel: Int
        get() = (advertisementData[CBAdvertisementDataTxPowerLevelKey] as? Int) ?: 0
}