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

class BluetoothDescriptorsTest : BluetoothFlowTest<BluetoothFlowTest.Configuration.DeviceWithDescriptor, BluetoothFlowTest.DescriptorContext, List<Descriptor>>() {

    override val createTestContextWithConfiguration: suspend (configuration: Configuration.DeviceWithDescriptor, scope: CoroutineScope) -> DescriptorContext = { configuration, scope ->
        DescriptorContext(configuration, scope)
    }

    override val flowFromTestContext: suspend DescriptorContext.() -> Flow<List<Descriptor>> = {
        bluetooth.scannedDevices()[device.identifier].services()[serviceUuid].characteristics()[characteristicUuid].descriptors()
    }

    @Test
    fun testGetDescriptors() = testWithFlowAndTestContext(
        Configuration.DeviceWithDescriptor()
    ) {
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
            assertEquals(characteristic.descriptors, it)
        }
    }
}
