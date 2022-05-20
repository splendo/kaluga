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

import com.splendo.kaluga.test.base.mock.matcher.ParameterMatcher.Companion.eq
import com.splendo.kaluga.test.base.mock.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlin.test.Test
import kotlin.test.assertEquals

class BluetoothRequestMtuTest : BluetoothFlowTest<BluetoothFlowTest.Configuration.DeviceWithoutService, BluetoothFlowTest.DeviceContext, Int>() {

    override val createTestContextWithConfiguration: suspend (configuration: Configuration.DeviceWithoutService, scope: CoroutineScope) -> DeviceContext = { configuration, scope ->
        DeviceContext(configuration, scope)
    }

    override val flowFromTestContext: suspend DeviceContext.() -> Flow<Int> = { bluetooth.devices()[device.identifier].mtu() }

    @Test
    fun testRequestMtu() = testWithFlowAndTestContext(
        Configuration.DeviceWithoutService()
    ) {

        val newMtu = 512

        mainAction {
            bluetooth.startScanning()
            scanDevice()
        }
        test {
            assertEquals(-1, it)
        }
        mainAction {
            connectDevice()
            bluetooth.devices()[device.identifier].requestMtu(newMtu)
            connectionManager.requestMtuMock.verify(eq(newMtu))
        }

        test {
            assertEquals(newMtu, it)
        }
    }
}
