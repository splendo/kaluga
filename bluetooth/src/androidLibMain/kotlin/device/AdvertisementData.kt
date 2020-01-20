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