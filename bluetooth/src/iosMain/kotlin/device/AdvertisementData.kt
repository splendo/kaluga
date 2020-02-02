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
                typedList<CBUUID>() ?: emptyList()
        }()
    override val serviceData: Map<UUID, ByteArray?>
        get() = {
            (advertisementData[CBAdvertisementDataManufacturerDataKey] as? Map<*, *>)?.
                typedMap<CBUUID, NSData>()?.
                mapNotNull { Pair(it.key, it.value.toByteArray()) }?.
                toMap() ?: emptyMap()
        }()
    override val txPowerLevel: Int
        get() = (advertisementData[CBAdvertisementDataTxPowerLevelKey] as? Int) ?: 0
}