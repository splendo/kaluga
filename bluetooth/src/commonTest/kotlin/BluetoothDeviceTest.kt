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

import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.test.mock.matcher.ParameterMatcher.Companion.eq
import com.splendo.kaluga.test.mock.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.yield
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class BluetoothDeviceTest : BluetoothFlowTest<BluetoothFlowTest.Configuration.DeviceWithoutService, BluetoothFlowTest.DeviceContext, Device?>() {

    override val createTestContextWithConfiguration: suspend (configuration: Configuration.DeviceWithoutService, scope: CoroutineScope) -> DeviceContext = { configuration, scope ->
        DeviceContext(configuration, scope)
    }

    override val flowFromTestContext: suspend DeviceContext.() -> Flow<Device?> = { bluetooth.devices()[device.identifier] }

    @Test
    fun testGetDevice() = testWithFlowAndTestContext(
        Configuration.DeviceWithoutService()
    ) {

        test {
            assertNull(it)
        }
        mainAction {
            bluetooth.startScanning()
            yield()
            scanner.scanForDevicesMock.verify(eq(emptySet()))
            scanDevice()
        }

        test {
            assertEquals(device, it)
        }

        mainAction {
            bluetooth.stopScanning()
            yield()
            scanner.stopScanningMock.verify()
        }

        test {
            assertNull(it)
        }
    }
}
