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

import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.test.Test
import kotlin.test.assertEquals

class BluetoothRssiTest : BluetoothFlowTest<BluetoothFlowTest.Configuration.DeviceWithoutService, BluetoothFlowTest.DeviceContext, Int>() {

    override val createTestContextWithConfiguration: suspend (configuration: Configuration.DeviceWithoutService, scope: CoroutineScope) -> DeviceContext = { configuration, scope ->
        DeviceContext(configuration, scope)
    }

    override val flowFromTestContext: suspend DeviceContext.() -> Flow<Int> = { bluetooth.scannedDevices()[device.identifier].rssi() }

    @Test
    fun testRssi() = testWithFlowAndTestContext(
        Configuration.DeviceWithoutService()
    ) {
        val newRssi = -42
        mainAction {
            bluetooth.startScanning()
            scanDevice()
        }
        test {
            assertEquals(configuration.rssi, it)
        }
        mainAction {
            connectDevice()
            connectionManager.readRssiMock.on().doExecuteSuspended {
                coroutineScope.launch {
                    connectionManager.handleNewRssi(newRssi)
                }
            }
            bluetooth.scannedDevices()[device.identifier].updateRssi()
            connectionManager.readRssiMock.verify()
        }
        test {
            assertEquals(newRssi, it)
        }
    }
}
