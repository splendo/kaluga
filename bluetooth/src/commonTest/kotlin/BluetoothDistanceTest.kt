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

import com.splendo.kaluga.test.mock.bluetooth.device.MockAdvertisementData
import kotlinx.coroutines.flow.Flow
import kotlin.test.Test
import kotlin.test.assertEquals

class BluetoothDistanceTest: BluetoothFlowTest<Double>() {

    override val flow: suspend () -> Flow<Double> = {
        advertisementData = MockAdvertisementData(txPowerLevel = -50)
        rssi = -50
        setup(Setup.DEVICE)
        bluetooth.devices()[device.identifier].distance()
    }

    @Test
    fun testDistance() = testWithFlow {
        scanDevice(rssi = -50)
        bluetooth.startScanning()
        test {
            assertEquals(1.0, it)
        }
        action {
            scanDevice(rssi = -70)
        }
        test {
            assertEquals(5.5, it)
        }
        action {
            scanDevice(rssi = -50)
        }
        test {
            assertEquals(4.0, it)
        }
        action {
            scanDevice(rssi = -30)
        }
        test {
            assertEquals(3.025, it)
        }
        action {
            scanDevice(rssi = -30)
        }

        // this should not have led to an update since it repeats the RSSI value, so the following would fail:
        //
        // test {
        //     assertEquals(2.44, it)
        // }

        action {
            scanDevice(rssi = -70)
        }
        test {
            assertEquals(4.42, it)
        }

        resetFlow()
        permissionManager.hasStoppedMonitoring.await()
        mockBaseScanner().stopMonitoringPermissionsCompleted.get().await()
    }
}