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
import com.splendo.kaluga.bluetooth.device.DeviceWrapper
import com.splendo.kaluga.bluetooth.scanner.Scanner
import com.splendo.kaluga.test.base.mock.matcher.ParameterMatcher.Companion.eq
import com.splendo.kaluga.test.base.mock.verify
import com.splendo.kaluga.test.bluetooth.createDeviceWrapper
import com.splendo.kaluga.test.bluetooth.createMockDevice
import com.splendo.kaluga.test.bluetooth.device.MockAdvertisementData
import com.splendo.kaluga.test.bluetooth.device.MockDeviceConnectionManager
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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
        bluetooth.pairedDevices(pairedFilter, pairedDevicesTimer)
    }

    @Test
    fun testPairedDevices() = testWithFlowAndTestContext(Configuration.Bluetooth()) {
        mainAction {
            pairedDevicesTimer.tryEmit(Unit)
        }
        test {
            assertEquals(emptyList(), it)
        }
        val completableDeviceWrapper = CompletableDeferred<DeviceWrapper>()
        mainAction {
            val name = "Watch"
            val deviceWrapper = createDeviceWrapper(deviceName = name)
            pairedDevicesTimer.tryEmit(Unit)
            yield()
            scanner.retrievePairedDeviceDiscoveredEventsMock.verify(eq(pairedFilter))
            scanner.pairedDeviceDiscoveredEvents.emit(
                listOf(
                    Scanner.DeviceDiscovered(deviceWrapper.identifier, 0, MockAdvertisementData(name = name)) {
                        deviceWrapper to MockDeviceConnectionManager.Builder()
                    }
                )
            )
            pairedDevicesTimer.tryEmit(Unit)
            yield()
            scanner.retrievePairedDeviceDiscoveredEventsMock.verify(eq(pairedFilter), times = 2)
            completableDeviceWrapper.complete(deviceWrapper)
        }

        test { devices ->
            assertContentEquals(listOf(completableDeviceWrapper.await().identifier), devices.map { it.identifier })
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
            bluetooth.devices().first() // trigger scanning
            scanner.didStartScanningMock.verify(eq(emptySet()))
        }

        val completablePairedDeviceWrapper = CompletableDeferred<DeviceWrapper>()
        mainAction {
            val name = "Paired Device"
            val deviceWrapper = createDeviceWrapper(deviceName = name)
            scanner.retrievePairedDeviceDiscoveredEventsMock.verify(eq(pairedFilter))
            scanner.pairedDeviceDiscoveredEvents.emit(
                listOf(
                    Scanner.DeviceDiscovered(deviceWrapper.identifier, 0, MockAdvertisementData(name = name)) {
                        deviceWrapper to MockDeviceConnectionManager.Builder()
                    }
                )
            )
            completablePairedDeviceWrapper.complete(deviceWrapper)
        }

        test { devices ->
            assertContentEquals(listOf(completablePairedDeviceWrapper.await().identifier), devices.map { it.identifier })
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
            bluetooth.devices().first() // wait for scanned devices updated
        }

        val completableSecondPairedDeviceWrapper = CompletableDeferred<DeviceWrapper>()
        mainAction {
            val name = "One More Paired Device"
            val deviceWrapper = createDeviceWrapper(deviceName = name)
            scanner.retrievePairedDeviceDiscoveredEventsMock.verify(eq(pairedFilter), times = 2)
            scanner.pairedDeviceDiscoveredEvents.emit(
                listOf(completablePairedDeviceWrapper.await(), deviceWrapper).map {
                    Scanner.DeviceDiscovered(deviceWrapper.identifier, 0, MockAdvertisementData(name = name)) {
                        it to MockDeviceConnectionManager.Builder()
                    }
                }
            )
            completableSecondPairedDeviceWrapper.complete(deviceWrapper)
        }

        test { devices ->
            assertContentEquals(listOf(completablePairedDeviceWrapper, completableSecondPairedDeviceWrapper).map { it.await().identifier }, devices.map { it.identifier })
        }
    }
}
