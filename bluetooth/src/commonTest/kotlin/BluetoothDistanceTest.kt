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

package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.test.bluetooth.device.MockAdvertisementData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlin.test.Test
import kotlin.test.assertEquals

class BluetoothDistanceTest : BluetoothFlowTest<BluetoothFlowTest.Configuration.DeviceWithoutService, BluetoothFlowTest.DeviceContext, Double>() {

    override val createTestContextWithConfiguration: suspend (configuration: Configuration.DeviceWithoutService, scope: CoroutineScope) -> DeviceContext = { configuration, scope ->
        DeviceContext(configuration, scope)
    }

    override val flowFromTestContext: suspend DeviceContext.() -> Flow<Double> = { bluetooth.devices()[device.identifier].distance() }

    @Test
    fun testDistance() = testWithFlowAndTestContext(
        Configuration.DeviceWithoutService(
            rssi = -50,
            advertisementData = MockAdvertisementData(txPowerLevel = -50)
        )
    ) {
        mainAction {
            bluetooth.startScanning()
            scanDevice(rssi = -50)
        }
        test {
            assertEquals(1.0, it)
        }
        mainAction {
            scanDevice(rssi = -70)
        }
        test {
            assertEquals(5.5, it)
        }
        mainAction {
            scanDevice(rssi = -50)
        }
        test {
            assertEquals(4.0, it)
        }
        mainAction {
            scanDevice(rssi = -30)
        }
        test {
            assertEquals(3.025, it)
        }
        mainAction {
            scanDevice(rssi = -30)
        }

        // this should not have led to an update since it repeats the RSSI value, so the following would fail:
        //
        // test {
        //     assertEquals(2.44, it)
        // }

        mainAction {
            scanDevice(rssi = -70)
        }
        test {
            assertEquals(4.42, it)
        }
    }
}
