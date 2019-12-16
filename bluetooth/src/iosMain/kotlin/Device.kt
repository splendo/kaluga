package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.base.toByteArray
import com.splendo.kaluga.base.typedList
import com.splendo.kaluga.base.typedMap
import platform.CoreBluetooth.*
import platform.Foundation.NSData
import platform.Foundation.NSUUID

actual class Device(private val peripheral: CBPeripheral, private val centralManager: CBCentralManager, override val advertisementData: AdvertisementData) : BaseDevice {

    override val name: String?
        get() =
            peripheral.name

    override val identifier: Identifier
        get() = peripheral.identifier
}

actual typealias Identifier = NSUUID

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