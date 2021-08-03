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
import kotlin.test.Test
import kotlin.test.assertEquals

class BluetoothServicesTest: BluetoothFlowTest<List<Service>>() {

    override val flow: () -> Flow<List<Service>> = {
        // setupPermissions(this)
        // setupBluetooth(coroutineScope = this)
        // deviceWrapper = createDeviceWrapper()
        // device = createDevice(deviceWrapper, coroutineScope = this)
        setup(Setup.SERVICE)
        bluetooth.devices()[device.identifier].services()
    }

    @Test
    fun testGetServices() = testWithFlow {
        scanDevice()
        bluetooth.startScanning()

        test {
            assertEquals(emptyList(), it)
        }
        action {
            connectDevice(device)
            connectionManager.discoverServicesCompleted.get().await()
            discoverService(service, device)
        }
        val services = listOf(service)
        test {
            assertEquals(services, it)
        }
    }
}
