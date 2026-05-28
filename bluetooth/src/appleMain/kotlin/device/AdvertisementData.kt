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

import com.splendo.kaluga.base.utils.toByteArray
import com.splendo.kaluga.base.utils.typedList
import com.splendo.kaluga.base.utils.typedMap
import com.splendo.kaluga.bluetooth.TxPower
import com.splendo.kaluga.bluetooth.UUID
import platform.CoreBluetooth.CBAdvertisementDataIsConnectable
import platform.CoreBluetooth.CBAdvertisementDataLocalNameKey
import platform.CoreBluetooth.CBAdvertisementDataManufacturerDataKey
import platform.CoreBluetooth.CBAdvertisementDataServiceDataKey
import platform.CoreBluetooth.CBAdvertisementDataServiceUUIDsKey
import platform.CoreBluetooth.CBAdvertisementDataTxPowerLevelKey
import platform.CoreBluetooth.CBUUID
import platform.Foundation.NSData
import platform.Foundation.NSNumber

/**
 * iOS implementation of [BaseAdvertisementData]
 * @param advertisementData a map containing the data being advertised
 */
actual class AdvertisementData(private val advertisementData: Map<String, Any>) : BaseAdvertisementData {

    actual override val name: String?
        get() = advertisementData[CBAdvertisementDataLocalNameKey] as? String
    actual override val manufacturerId: Int?
        get() = manufacturerData?.let { manufacturerDataArray ->
            if (manufacturerDataArray.size >= 2) {
                (manufacturerDataArray[0].toUInt() + (manufacturerDataArray[1].toUInt() shl 8)).toInt()
            } else {
                null
            }
        }
    actual override val manufacturerData: ByteArray? get() = (advertisementData[CBAdvertisementDataManufacturerDataKey] as? NSData)?.toByteArray()
    actual override val serviceUUIDs: List<UUID> get() = (advertisementData[CBAdvertisementDataServiceUUIDsKey] as? List<*>)?.typedList() ?: emptyList()
    actual override val serviceData: Map<UUID, ByteArray?> get() = (advertisementData[CBAdvertisementDataServiceDataKey] as? Map<*, *>)
        ?.typedMap<CBUUID, NSData>()
        ?.mapNotNull { Pair(it.key, it.value.toByteArray()) }
        ?.toMap() ?: emptyMap()
    actual override val txPowerLevel: TxPower get() = (advertisementData[CBAdvertisementDataTxPowerLevelKey] as? NSNumber)?.intValue ?: Int.MIN_VALUE

    actual override val isConnectable: Boolean get() = ((advertisementData[CBAdvertisementDataIsConnectable] as? NSNumber)?.boolValue ?: false)
}
