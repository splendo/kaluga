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

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.bluetooth.device.BaseAdvertisementData
import com.splendo.kaluga.test.mock.bluetooth.device.MockAdvertisementData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.test.Test
import kotlin.test.assertEquals

class BluetoothAdvertisementTest: BluetoothFlowTest<BaseAdvertisementData>() {

    override val flow: suspend () -> Flow<BaseAdvertisementData> = {
        setup(Setup.DEVICE)
        bluetooth.devices()[device.identifier].advertisement()
    }

    @Test
    fun testAdvertisementData() = runBlocking {
        launch {
            scanDevice(device, deviceWrapper, initialRssi, advertisementData)
        }
        bluetooth.startScanning()

        test {
            assertEquals(advertisementData, it)
        }

        val newAdvertisementData = MockAdvertisementData(name = "New Name")

        action {
            scanDevice(device, deviceWrapper, initialRssi, newAdvertisementData)
        }
        test {
            assertEquals(newAdvertisementData, it)
        }

        permissionManager.hasStoppedMonitoring.await()
        mockBaseScanner().stopMonitoringPermissions.await()
        mockBaseScanner().stopMonitoringBluetoothCompleted.await()
    }
}
