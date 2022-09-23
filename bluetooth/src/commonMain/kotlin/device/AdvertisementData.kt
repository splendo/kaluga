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

import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.uuidString

interface BaseAdvertisementData {
    val name: String?
    val manufacturerId: Int?
    val manufacturerData: ByteArray?
    val serviceUUIDs: List<UUID>
    val serviceData: Map<UUID, ByteArray?>
    val txPowerLevel: Int
    val isConnectable: Boolean
}

expect class AdvertisementData : BaseAdvertisementData

val BaseAdvertisementData.description: String get() = listOfNotNull(
    name?.let { "Name: $it" },
    manufacturerId?.let { "ManufacturerId: $it" },
    manufacturerData?.let { "ManufacturerData: $it" },
    if (serviceUUIDs.isEmpty()) null else { "ServiceUUIDS: ${serviceUUIDs.joinToString(", ") { it.uuidString }}" },
    if (serviceData.isEmpty()) null else { "ServiceData: ${serviceData.entries.joinToString(",") { (uuid, data) -> "[${uuid.uuidString} : $data]" } }" },
    "TxPowerLevel: $txPowerLevel",
    "IsConnectable: $isConnectable"
).joinToString("\n")

internal data class PairedAdvertisementData(
    override val name: String? = null,
    override val serviceUUIDs: List<UUID> = emptyList()
) : BaseAdvertisementData {
    override val manufacturerId: Int? = null
    override val manufacturerData: ByteArray? = null
    override val serviceData: Map<UUID, ByteArray?> = emptyMap()
    override val txPowerLevel = Int.MIN_VALUE
    override val isConnectable = true
}
