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

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.test.Test
import kotlin.test.assertEquals

class BluetoothCharacteristicsTest: BluetoothFlowTest<List<Characteristic>>() {

    override val flow: suspend () -> Flow<List<Characteristic>> = {
        setup(Setup.SERVICE)

        bluetooth.devices()[device.identifier].services()[service.uuid].characteristics()
    }

    @Test
    fun testGetCharacteristics() = testWithFlow {
        launch {
            scanDevice(device, deviceWrapper)
        }
        bluetooth.startScanning()

        test {
            assertEquals(emptyList(), it)
        }
        action {
            connectDevice(device, connectionManager, this)
            connectionManager.discoverServicesCompleted.await()
            discoverService(service, device)
        }
        test {
            assertEquals(service.characteristics, it)
        }

        permissionManager.hasStoppedMonitoring.await()
        mockBaseScanner().stopMonitoringPermissions.await()
        mockBaseScanner().stopMonitoringBluetoothCompleted.await()
    }
}
