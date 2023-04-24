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

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlin.test.Test
import kotlin.test.assertEquals

class BluetoothCharacteristicsTest : BluetoothFlowTest<BluetoothFlowTest.Configuration.DeviceWithService, BluetoothFlowTest.ServiceContext, List<Characteristic>>() {

    override val createTestContextWithConfiguration: suspend (configuration: Configuration.DeviceWithService, scope: CoroutineScope) -> ServiceContext = { configuration, scope ->
        ServiceContext(configuration, scope)
    }
    override val flowFromTestContext: suspend ServiceContext.() -> Flow<List<Characteristic>> = {
        bluetooth.scannedDevices()[device.identifier].services()[serviceUuid].characteristics()
    }

    @Test
    fun testGetCharacteristics() = testWithFlowAndTestContext(Configuration.DeviceWithService()) {
        mainAction {
            bluetooth.startScanning()
            scanDevice()
        }
        test {
            assertEquals(emptyList(), it)
        }
        mainAction {
            connectDevice()
            discoverService()
        }
        test {
            assertEquals(service.characteristics, it)
        }
    }
}
