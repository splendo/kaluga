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

import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.base.utils.firstInstance
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.scanner.Scanner
import com.splendo.kaluga.bluetooth.scanner.ScanningState
import com.splendo.kaluga.test.base.mock.matcher.ParameterMatcher.Companion.eq
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.verify
import com.splendo.kaluga.test.base.yieldMultiple
import com.splendo.kaluga.test.bluetooth.createDeviceWrapper
import com.splendo.kaluga.test.bluetooth.createMockDevice
import com.splendo.kaluga.test.bluetooth.device.MockAdvertisementData
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class BluetoothPairedDevicesTest : BluetoothFlowTest<BluetoothFlowTest.Configuration.Bluetooth, BluetoothFlowTest.BluetoothContext, List<Device>>() {

    companion object {
        private val pairedFilter = setOf(uuidFromShort("130D"))
    }
    override val createTestContextWithConfiguration: suspend (configuration: Configuration.Bluetooth, scope: CoroutineScope) -> BluetoothContext = { configuration, scope ->
        BluetoothContext(configuration, scope)
    }

    override val flowFromTestContext: suspend BluetoothContext.() -> Flow<List<Device>> = {
        bluetooth.pairedDevices(pairedFilter, timer = pairedDevicesTimer)
    }

    @Test
    fun testPairedDevices() = testWithFlowAndTestContext(Configuration.Bluetooth()) {
        mainAction {
            pairedDevicesTimer.tryEmit(Unit)
        }
        test {
            assertEquals(emptyList(), it)
        }
        val completableDevice = CompletableDeferred<Device>()
        mainAction {
            yieldMultiple(10)
            scanner.retrievePairedDeviceDiscoveredEventsMock.verify(eq(pairedFilter))
            val name = "Watch"
            val deviceWrapper = createDeviceWrapper(deviceName = name)
            val device = createMockDevice(deviceWrapper, coroutineScope) { deviceName = name }
            val hasFoundDevices = EmptyCompletableDeferred()
            scanner.retrievePairedDeviceDiscoveredEventsMock.resetCalls()
            scanner.retrievePairedDeviceDiscoveredEventsMock.on(eq(pairedFilter)).doExecuteSuspended {
                hasFoundDevices.await()
                completableDevice.complete(device)
                listOf(
                    Scanner.DeviceDiscovered(deviceWrapper.identifier, 0, MockAdvertisementData(name = name)) {
                        device
                    },
                )
            }
            pairedDevicesTimer.emit(Unit)
            yieldMultiple(10)
            scanner.retrievePairedDeviceDiscoveredEventsMock.verify(eq(pairedFilter))
            pairedDevicesTimer.emit(Unit)
            yieldMultiple(10)
            scanner.retrievePairedDeviceDiscoveredEventsMock.verify(eq(pairedFilter))
            hasFoundDevices.complete()
        }

        test { devices ->
            assertContentEquals(listOf(completableDevice.await().identifier), devices.map { it.identifier })
        }
    }

    @Test
    fun testPairedDevicesWhileScanning() = testWithFlowAndTestContext(Configuration.Bluetooth()) {
        mainAction {
            pairedDevicesTimer.tryEmit(Unit)
        }
        test {
            assertEquals(emptyList(), it)
        }

        val scannedDevice = CompletableDeferred<Device>()
        mainAction {
            bluetooth.startScanning()
            yield()
            val name = "Discovered Device"
            val deviceWrapper = createDeviceWrapper(deviceName = name)
            val device = createMockDevice(deviceWrapper, coroutineScope) {
                deviceName = name
            }
            scannedDevice.complete(device)
            scanDevice(device, deviceWrapper, rssi = 0, advertisementData = MockAdvertisementData())
            val job = launch {
                bluetooth.scannedDevices().collect() // trigger scanning
            }
            bluetooth.scanningStateRepo.firstInstance<ScanningState.Enabled.Scanning>()
            yieldMultiple(10)
            scanner.didStartScanningMock.verify(eq(emptySet()))
            scanner.retrievePairedDeviceDiscoveredEventsMock.verify(eq(pairedFilter))
            job.cancel()
        }

        val completablePairedDevice = CompletableDeferred<Device>()
        mainAction {
            scanner.retrievePairedDeviceDiscoveredEventsMock.resetCalls()
            val name = "Paired Device"
            val deviceWrapper = createDeviceWrapper(deviceName = name)
            val device = createMockDevice(deviceWrapper, coroutineScope) {
                deviceName = name
            }
            scanner.retrievePairedDeviceDiscoveredEventsMock.on(eq(pairedFilter)).doExecuteSuspended {
                completablePairedDevice.complete(device)
                listOf(
                    Scanner.DeviceDiscovered(deviceWrapper.identifier, 0, MockAdvertisementData(name = name)) { device },
                )
            }
            pairedDevicesTimer.emit(Unit)
            yieldMultiple(10)
            scanner.retrievePairedDeviceDiscoveredEventsMock.verify(eq(pairedFilter))
        }

        test { devices ->
            assertContentEquals(listOf(completablePairedDevice.await()), devices)
        }

        val scannedList = CompletableDeferred<List<Device>>()
        mainAction {
            val name = "Yet Another Discovered Device"
            val deviceWrapper = createDeviceWrapper(deviceName = name)
            val device = createMockDevice(deviceWrapper, coroutineScope) {
                deviceName = name
            }
            scannedList.complete(listOf(scannedDevice.getCompleted(), device))
            scanDevice(device, deviceWrapper, rssi = 0, advertisementData = MockAdvertisementData())
            bluetooth.scannedDevices().first() // wait for scanned devices updated
        }

        val completableSecondPairedDevice = CompletableDeferred<Device>()
        mainAction {
            val name = "One More Paired Device"
            val deviceWrapper = createDeviceWrapper(deviceName = name)
            val device = createMockDevice(deviceWrapper, coroutineScope) {
                deviceName = name
            }
            scanner.retrievePairedDeviceDiscoveredEventsMock.on(eq(pairedFilter)).doExecuteSuspended {
                completableSecondPairedDevice.complete(device)
                listOf(completablePairedDevice.await(), device).map { deviceToAdd ->
                    Scanner.DeviceDiscovered(deviceToAdd.identifier, 0, MockAdvertisementData()) { deviceToAdd }
                }
            }
            pairedDevicesTimer.tryEmit(Unit)
            yieldMultiple(20)
            scanner.retrievePairedDeviceDiscoveredEventsMock.verify(eq(pairedFilter), times = 2)
        }

        test { devices ->
            assertContentEquals(listOf(completablePairedDevice, completableSecondPairedDevice).map { it.await() }, devices)
        }
    }
}
