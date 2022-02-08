/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.test.mock.bluetooth.device

import co.touchlab.stately.concurrency.AtomicReference
import com.splendo.kaluga.base.utils.Date
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.BaseAdvertisementData
import com.splendo.kaluga.bluetooth.device.Identifier

data class MockAdvertisementData(
    override val name: String? = null,
    override val manufacturerId: Int? = null,
    override val manufacturerData: ByteArray? = null,
    override val serviceUUIDs: List<UUID> = emptyList(),
    override val serviceData: Map<UUID, ByteArray?> = emptyMap(),
    override val txPowerLevel: Int = Int.MIN_VALUE
) : BaseAdvertisementData {

    class Builder {
        var name: String? = null
        var manufacturerId: Int? = null
        var manufacturerData: ByteArray? = null
        var serviceUUIDs: List<UUID> = emptyList()
        var serviceData: Map<UUID, ByteArray?> = emptyMap()
        var txPowerLevel: Int = Int.MIN_VALUE
        var isConnectible: Boolean = true
        fun build() : MockAdvertisementData {
            val advertisementData = MockAdvertisementData(
                name = name,
                manufacturerId = manufacturerId,
                manufacturerData = manufacturerData,
                serviceUUIDs = serviceUUIDs,
                serviceData = serviceData,
                txPowerLevel = txPowerLevel
            )
            advertisementData.isConnectible = isConnectible
            return advertisementData
        }
    }

    companion object {
        fun build(build: Builder.() -> Unit) : MockAdvertisementData {
            val builder = Builder()
            build(builder)
            return builder.build()
        }
    }

    private val _isConnectible = AtomicReference(true)
    override var isConnectible: Boolean
        get() = _isConnectible.get()
        set(value) { _isConnectible.set(value) }
}
