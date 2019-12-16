package com.splendo.kaluga.bluetooth.device

import com.splendo.kaluga.bluetooth.UUID
import no.nordicsemi.android.support.v18.scanner.ScanRecord

actual class AdvertisementData(private val scanRecord: ScanRecord?) : BaseAdvertisementData {

    override val name: String?
        get() = scanRecord?.deviceName
    override val manufacturerData: ByteArray?
        get() = scanRecord?.manufacturerSpecificData?.let {
            if (it.size() > 0) it.get(0) else null
        }
    override val serviceUUIDs: List<UUID>
        get() = scanRecord?.serviceUuids?.map { UUID(it) } ?: emptyList()
    override val serviceData: Map<UUID, ByteArray?>
        get() = scanRecord?.serviceData?.mapKeys { UUID(it.key) } ?: emptyMap()
    override val txPowerLevel: Int
        get() = scanRecord?.txPowerLevel ?: 0
}