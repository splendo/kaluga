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

import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.test.mock.matcher.AnyOrNullCaptor
import com.splendo.kaluga.test.mock.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.yield
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BluetoothDescriptorValueTest : BluetoothFlowTest<BluetoothFlowTest.Configuration.DeviceWithDescriptor, BluetoothFlowTest.DescriptorContext, ByteArray?>() {

    override val createTestContextWithConfiguration: suspend (configuration: Configuration.DeviceWithDescriptor, scope: CoroutineScope) -> DescriptorContext = { configuration, scope ->
        DescriptorContext(configuration, scope)
    }

    override val flowFromTestContext: suspend DescriptorContext.() -> Flow<ByteArray?> = {
        bluetooth.devices()[device.identifier].services()[serviceUuid].characteristics()[characteristicUuid].descriptors()[descriptorUuid].value()
    }

    @Test
    fun testGetDescriptorValue() = testWithFlowAndTestContext(
        Configuration.DeviceWithDescriptor()
    ) {
        val newValue = "Test".encodeToByteArray()
        mainAction {
            bluetooth.startScanning()
            scanDevice()
        }
        test {
            assertNull(it)
        }
        mainAction {
            connectDevice()
            discoverService()
            yield()
            descriptor.writeValue(newValue)
            val captor = AnyOrNullCaptor<DeviceAction>()
            connectionManager.performActionMock.verify(captor)
            assertIs<DeviceAction.Write.Descriptor>(captor.lastCaptured)
            connectionManager.handleCurrentAction()
        }
        test {
            assertTrue(newValue contentEquals it)
        }
    }
}
