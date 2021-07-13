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
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.test.mock.bluetooth.createDeviceWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.test.Test
import kotlin.test.assertEquals

class BluetoothDevicesTest: BluetoothFlowTest<List<Device>>() {

    init {
        ensureNeverFrozen()
    }

    override val flow: suspend () -> Flow<List<Device>> = {
        setup(Setup.BLUETOOTH)
        bluetooth.devices()
    }

    @Test
    fun testScanDevice() = testWithFlow {
        ensureNeverFrozen()
        val noDevices = emptyList<Device>()

        test {
            assertEquals(noDevices, it)
        }

        val mockBaseScanner = mockBaseScanner()

        action {
            bluetooth.startScanning()
        }

        assertEquals(emptySet(), mockBaseScanner.scanForDevicesCompleted.get().await())

        val filter = setOf(randomUUID())
        action {
            mockBaseScanner.reset()
            bluetooth.startScanning(filter)
        }
        mockBaseScanner.stopScanningCompleted.get().await()
        assertEquals(filter, mockBaseScanner.scanForDevicesCompleted.get().await())

        mockBaseScanner.reset()

        val deviceWrapper = createDeviceWrapper()
        val device = createDevice(deviceWrapper)
        val scanCompleted = EmptyCompletableDeferred()
        action {
            scanDevice(device, deviceWrapper, scanCompleted = scanCompleted)
        }
        test {
            scanCompleted.await()
            assertEquals(listOf(device), it)
        }
        action {
            mockBaseScanner.reset()
            bluetooth.stopScanning()
        }

        mockBaseScanner.stopScanningCompleted.get().await()

        test {
            assertEquals(emptyList(), it)
        }
    }
}
