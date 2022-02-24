/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.base.utils.Date
import com.splendo.kaluga.bluetooth.device.BaseAdvertisementData
import com.splendo.kaluga.bluetooth.device.DeviceInfo
import com.splendo.kaluga.bluetooth.device.Identifier

class MockDeviceInfo(
    override val identifier: Identifier,
    override val name: String?,
    override val rssi: Int,
    override val advertisementData: BaseAdvertisementData,
    override val updatedAt: Date
) : DeviceInfo {
    override fun distance(environmentalFactor: Double): Double = 0.0

    class Builder {
        var advertisementData: BaseAdvertisementData? = null
        fun advertisementData(build: MockAdvertisementData.Builder.() -> Unit) {
            val builder = MockAdvertisementData.Builder()
            build(builder)
            advertisementData = builder.build()
        }

        var identifier: Identifier = randomIdentifier()
        var name: String? = null
        var rssi = -100
        var updatedAt = Date.now()

        fun build() = MockDeviceInfo(
            identifier = identifier,
            name = name,
            rssi = rssi,
            updatedAt = updatedAt,
            advertisementData = advertisementData ?: MockAdvertisementData.build { }
        )
    }

    companion object {

        fun build(build: Builder.() -> Unit): MockDeviceInfo {
            val builder = Builder()
            build(builder)
            return builder.build()
        }
    }
}

expect fun randomIdentifier(): Identifier
expect fun identifierFromString(string: String): Identifier?
