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

import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.test.base.mock.matcher.ParameterMatcher.Companion.eq
import com.splendo.kaluga.test.base.mock.verify
import com.splendo.kaluga.test.bluetooth.createDeviceWrapper
import com.splendo.kaluga.test.bluetooth.device.MockAdvertisementData
import com.splendo.kaluga.test.bluetooth.device.MockDeviceConnectionManager
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.yield
import kotlin.test.Test
import kotlin.test.assertEquals

class BluetoothDevicesTest : BluetoothFlowTest<BluetoothFlowTest.Configuration.Bluetooth, BluetoothFlowTest.BluetoothContext, List<Device>>() {

    override val createTestContextWithConfiguration: suspend (configuration: Configuration.Bluetooth, scope: CoroutineScope) -> BluetoothContext = { configuration, scope ->
        BluetoothContext(configuration, scope)
    }

    override val flowFromTestContext: suspend BluetoothContext.() -> Flow<List<Device>> = {
        bluetooth.devices()
    }

    @Test
    fun testScanDevice() = testWithFlowAndTestContext(
        Configuration.Bluetooth()
    ) {
        test {
            assertEquals(emptyList(), it)
        }

        val filter = setOf(randomUUID())
        val deferredDevice = CompletableDeferred<Device>()
        mainAction {
            bluetooth.startScanning()
            yield()
            scanner.scanForDevicesMock.verify(eq(emptySet()))

            yield()
            bluetooth.startScanning(filter)
            val rssi = -100
            val advertisementData = MockAdvertisementData()
            val deviceWrapper = createDeviceWrapper()
            val device = createDevice(ConnectionSettings(), deviceWrapper, rssi, advertisementData) {
                MockDeviceConnectionManager(true, deviceWrapper, ConnectionSettings(), coroutineScope)
            }
            deferredDevice.complete(device)
            scanDevice(device, deviceWrapper, rssi, advertisementData)
        }
        test {
            scanner.stopScanningMock.verify()
            scanner.scanForDevicesMock.verify(eq(filter))
            assertEquals(listOf(deferredDevice.getCompleted()), it)
        }
        mainAction {
            bluetooth.stopScanning()
        }

        test {
            scanner.stopScanningMock.verify(times = 2)
            assertEquals(emptyList(), it)
        }
    }
}
