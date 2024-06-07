/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.test.bluetooth

import com.splendo.kaluga.bluetooth.RSSI
import com.splendo.kaluga.bluetooth.TxPower
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.test.bluetooth.device.MockAdvertisementData
import com.splendo.kaluga.test.bluetooth.device.MockDevice
import com.splendo.kaluga.test.bluetooth.device.MockDeviceInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration.Companion.milliseconds

class MockDeviceBuilder(
    private val context: CoroutineContext,
) {

    /** MockDevice's identifier */
    var identifier: Identifier = randomIdentifier()

    /** MockDevice's name */
    var name: String? = null

    /** RSSI value */
    var rssi: RSSI = Int.MIN_VALUE

    /** Is connectable flag in advertisement data */
    var isConnectable = true

    /** Manufacturer id in advertisement data */
    var manufacturerId: Int? = null

    /** Manufacturer data in advertisement data */
    var manufacturerData: ByteArray? = null

    /** Tx power level in advertisement data */
    var txPower: TxPower = Int.MIN_VALUE

    /** Setup mocks */
    var setupMocks = true

    private val serviceUUIDs = ArrayList<UUID>()

    /** Delay before connection and disconnection from the MockDevice */
    var connectionDelay = 500.milliseconds

    /** Add services advertised by the MockDevice */
    fun services(builder: ServiceUUIDsList.() -> Unit) = builder(serviceUUIDs)

    fun build(): MockDevice = MockDevice(
        identifier = identifier,
        info = MutableStateFlow(
            MockDeviceInfo(
                identifier = identifier,
                name = name,
                advertisementData = MockAdvertisementData(
                    name = name,
                    manufacturerId = manufacturerId,
                    manufacturerData = manufacturerData,
                    serviceUUIDs = serviceUUIDs.toList(),
                    txPowerLevel = txPower,
                    isConnectable = isConnectable,
                ),
                rssi = rssi,
            ),
        ),
        coroutineContext = context,
        connectionDelay = connectionDelay,
        setupMocks = setupMocks,
    )
}

/** Build MockDevice, see [MockDeviceBuilder] */
fun buildMockDevice(context: CoroutineContext, builder: MockDeviceBuilder.() -> Unit) = MockDeviceBuilder(context).apply(builder).build()
