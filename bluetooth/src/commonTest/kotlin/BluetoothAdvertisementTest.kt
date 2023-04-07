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

package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.bluetooth.device.BaseAdvertisementData
import com.splendo.kaluga.test.bluetooth.device.MockAdvertisementData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlin.test.Test
import kotlin.test.assertEquals

class BluetoothAdvertisementTest : BluetoothFlowTest<BluetoothFlowTest.Configuration.DeviceWithoutService, BluetoothFlowTest.DeviceContext, BaseAdvertisementData>() {

    override val flowFromTestContext: suspend DeviceContext.() -> Flow<BaseAdvertisementData> = {
        bluetooth.scannedDevices()[device.identifier].advertisement()
    }

    override val createTestContextWithConfiguration: suspend (configuration: Configuration.DeviceWithoutService, scope: CoroutineScope) -> DeviceContext = { configuration, scope ->
        DeviceContext(configuration, scope)
    }

    @Test
    fun testAdvertisementData() = testWithFlowAndTestContext(
        Configuration.DeviceWithoutService()
    ) {

        mainAction {
            bluetooth.startScanning()
            scanDevice()
        }

        test {
            assertEquals(configuration.advertisementData, it)
        }

        val newAdvertisementData = MockAdvertisementData(name = "New Name")

        mainAction {
            scanDevice(advertisementData = newAdvertisementData)
        }
        test {
            assertEquals(newAdvertisementData, it)
        }
    }
}
