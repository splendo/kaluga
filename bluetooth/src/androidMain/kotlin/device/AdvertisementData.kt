/*
 Copyright (c) 2020. Splendo Consulting B.V. The Netherlands

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 */

package com.splendo.kaluga.bluetooth.device

import com.splendo.kaluga.bluetooth.TxPower
import com.splendo.kaluga.bluetooth.UUID
import no.nordicsemi.android.support.v18.scanner.ScanResult
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Android implementation of [BaseAdvertisementData]
 * @param scanResult the [ScanResult] being advertised
 */
actual class AdvertisementData(private val scanResult: ScanResult?) : BaseAdvertisementData {

    private val scanRecord = scanResult?.scanRecord

    actual override val name: String?
        get() = scanRecord?.deviceName
    actual override val manufacturerId: Int?
        get() = scanRecord?.manufacturerSpecificData?.let { if (it.size() > 0) it.keyAt(0) else null }
    actual override val manufacturerData: ByteArray?
        get() = scanRecord?.manufacturerSpecificData?.let { manufacturerSpecificData ->
            manufacturerId?.let { key ->
                val keyBytes = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(key.toShort()).array()
                byteArrayOf(*keyBytes, *manufacturerSpecificData[key])
            }
        }
    actual override val serviceUUIDs: List<UUID>
        get() = scanRecord?.serviceUuids?.map { it.uuid } ?: emptyList()
    actual override val serviceData: Map<UUID, ByteArray?>
        get() = scanRecord?.serviceData?.mapKeys { it.key.uuid } ?: emptyMap()
    actual override val txPowerLevel: TxPower
        get() = scanRecord?.txPowerLevel ?: Int.MIN_VALUE
    actual override val isConnectable: Boolean
        get() = scanResult?.isConnectable == true
}
