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

import com.splendo.kaluga.base.flow.filterOnlyImportant
import com.splendo.kaluga.bluetooth.BluetoothFlowTest.DeviceContext
import com.splendo.kaluga.bluetooth.device.randomIdentifier
import com.splendo.kaluga.bluetooth.scanner.ScanningState
import com.splendo.kaluga.test.base.mock.on
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class BluetoothPairedDevicesTest : BluetoothFlowTest<BluetoothFlowTest.Configuration.DeviceWithoutService, DeviceContext, ScanningState>() {

    @Test
    fun testPairedDevices() = testWithFlowAndTestContext(Configuration.DeviceWithoutService()) {

        mainAction {
            assertTrue(bluetooth.pairedDevices().isEmpty())
        }

        val list = listOf(randomIdentifier())

        mainAction {
            scanner.pairedDevicesMock.on().doReturn(list)
        }

        test {
            assertIs<ScanningState.Enabled>(it)
            assertContentEquals(list, it.pairedDevices(emptySet()))
        }
    }

    override val flowFromTestContext: suspend DeviceContext.() -> Flow<ScanningState> =
        { bluetooth.scanningStateRepo.stateFlow.filterOnlyImportant() }

    override val createTestContextWithConfiguration: suspend (configuration: Configuration.DeviceWithoutService, scope: CoroutineScope) -> DeviceContext = { configuration, scope ->
        DeviceContext(configuration, scope)
    }
}
