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

import co.touchlab.stately.ensureNeverFrozen
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.randomIdentifier
import com.splendo.kaluga.bluetooth.scanner.ScanningState
import com.splendo.kaluga.test.mock.bluetooth.createMockDevice
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class BluetoothPairedDevicesTest : BluetoothFlowTest<ScanningState>() {

    init {
        ensureNeverFrozen()
    }

    override val flow = suspend {
        setup(Setup.BLUETOOTH)
        bluetooth.scanningStateRepo.stateFlow
    }

    @Test
    fun testPairedDevices() = testWithFlow {
        ensureNeverFrozen()

        test {
            assertIs<ScanningState.NotInitialized>(it)
        }

        action {
            // Trigger initialization
            assertTrue(bluetooth.pairedDevices().isEmpty())
        }

        val device = createMockDevice(coroutineContext) {
            deviceName = "foo"
        }
        val list = listOf(device)
        mockBaseScanner().pairedDevices.value = list

        test {
            assertIs<ScanningState.Initialized.Enabled>(it)
            assertContentEquals(list, it.pairedDevices(emptySet()))
        }
    }
}
