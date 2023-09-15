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
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.scanner.ScanningState
import com.splendo.kaluga.test.base.mock.matcher.ParameterMatcher.Companion.eq
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.verify
import com.splendo.kaluga.test.bluetooth.createDeviceWrapper
import com.splendo.kaluga.test.bluetooth.device.MockAdvertisementData
import com.splendo.kaluga.test.bluetooth.device.MockDeviceConnectionManager
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.yield
import kotlin.test.Test
import kotlin.test.assertEquals

class BluetoothDevicesTest : BluetoothFlowTest<BluetoothFlowTest.Configuration.Bluetooth, BluetoothFlowTest.BluetoothContext, List<Device>>() {

    override val createTestContextWithConfiguration: suspend (configuration: Configuration.Bluetooth, scope: CoroutineScope) -> BluetoothContext = { configuration, scope ->
        BluetoothContext(configuration, scope)
    }

    override val flowFromTestContext: suspend BluetoothContext.() -> Flow<List<Device>> = {
        bluetooth.allDevices()
    }

    @Test
    fun testScanDevice() = testWithFlowAndTestContext(
        Configuration.Bluetooth(),
    ) {
        test {
            assertEquals(emptyList(), it)
            assertEquals(emptyList(), bluetooth.scannedDevices().first())
        }

        val filter = setOf(randomUUID())
        val deferredDevice = CompletableDeferred<Device>()

        mainAction {
            val didStartScanningCalled = EmptyCompletableDeferred()
            scanner.didStartScanningMock.on().doExecuteSuspended {
                didStartScanningCalled.complete()
            }
            bluetooth.startScanning()
            bluetooth.scanningStateRepo.firstInstance<ScanningState.Enabled.Scanning>()
            didStartScanningCalled.await()
            scanner.didStartScanningMock.verify(eq(emptySet()))
            bluetooth.startScanning(filter)
            bluetooth.scanningStateRepo.firstInstance<ScanningState.Enabled.Idle>()
            scanner.didStopScanningMock.verify()

            createAndScanDevice(deferredDevice)
        }
        test {
            scanner.didStartScanningMock.verify(eq(filter))
            assertEquals(listOf(deferredDevice.getCompleted()), it)
            assertEquals(emptyList(), bluetooth.scannedDevices().first())
            assertEquals(listOf(deferredDevice.getCompleted()), bluetooth.scannedDevices(filter).first())
        }

        mainAction {
            bluetooth.stopScanning()
        }

        test {
            scanner.didStopScanningMock.verify(times = 2)
            assertEquals(emptyList(), it)
            assertEquals(emptyList(), bluetooth.scannedDevices().first())
            assertEquals(emptyList(), bluetooth.scannedDevices(filter).first())
        }
    }

    @Test
    fun testScanAndCleanDevice() = testWithFlowAndTestContext(
        Configuration.Bluetooth(),
    ) {
        test {
            assertEquals(emptyList(), it)
        }

        val filter = setOf(randomUUID())
        val deferredDevice1 = CompletableDeferred<Device>()
        mainAction {
            bluetooth.startScanning()

            createAndScanDevice(deferredDevice1)
        }
        test {
            scanner.didStartScanningMock.verify(eq(emptySet()))
            assertEquals(listOf(deferredDevice1.getCompleted()), it)
            assertEquals(listOf(deferredDevice1.getCompleted()), bluetooth.scannedDevices().first())
        }

        val deferredDevice2 = CompletableDeferred<Device>()
        mainAction {
            bluetooth.stopScanning(cleanMode = BluetoothService.CleanMode.RETAIN_ALL)
            bluetooth.scanningStateRepo.firstInstance<ScanningState.Enabled.Idle>()
            scanner.didStopScanningMock.verify()
            bluetooth.startScanning(filter, BluetoothService.CleanMode.RETAIN_ALL)

            createAndScanDevice(deferredDevice2)
        }

        test {
            scanner.didStartScanningMock.verify(eq(filter))
            assertEquals(listOf(deferredDevice1.await(), deferredDevice2.await()), it)
            assertEquals(listOf(deferredDevice1.getCompleted()), bluetooth.scannedDevices().first())
            assertEquals(listOf(deferredDevice2.getCompleted()), bluetooth.scannedDevices(filter).first())
        }

        mainAction {
            bluetooth.stopScanning(cleanMode = BluetoothService.CleanMode.REMOVE_ALL)
        }

        test {
            scanner.didStopScanningMock.verify(times = 2)
            assertEquals(emptyList(), it)
            assertEquals(emptyList(), bluetooth.scannedDevices().first())
            assertEquals(emptyList(), bluetooth.scannedDevices(filter).first())
        }
    }

    @Test
    fun testScanDeviceAndCleanProvidedFilter() = testWithFlowAndTestContext(
        Configuration.Bluetooth(),
    ) {
        test {
            assertEquals(emptyList(), it)
        }

        val filter = setOf(randomUUID())
        val deferredDevice1 = CompletableDeferred<Device>()
        mainAction {
            bluetooth.startScanning()

            createAndScanDevice(deferredDevice1)
        }
        test {
            scanner.didStartScanningMock.verify(eq(emptySet()))
            assertEquals(listOf(deferredDevice1.getCompleted()), it)
            assertEquals(listOf(deferredDevice1.getCompleted()), bluetooth.scannedDevices().first())
        }

        val deferredDevice2 = CompletableDeferred<Device>()
        mainAction {
            bluetooth.stopScanning(cleanMode = BluetoothService.CleanMode.RETAIN_ALL)
            bluetooth.scanningStateRepo.firstInstance<ScanningState.Enabled.Idle>()
            scanner.didStopScanningMock.verify()
            bluetooth.startScanning(filter, BluetoothService.CleanMode.RETAIN_ALL)

            scanDevice(deferredDevice1.await(), createDeviceWrapper(identifier = deferredDevice1.await().identifier), -100, MockAdvertisementData())
            yield()
            createAndScanDevice(deferredDevice2)
        }

        test {
            scanner.didStartScanningMock.verify(eq(filter))
            assertEquals(listOf(deferredDevice1.await(), deferredDevice2.await()), it)
            assertEquals(listOf(deferredDevice1.getCompleted()), bluetooth.scannedDevices().first())
            assertEquals(listOf(deferredDevice1.getCompleted(), deferredDevice2.getCompleted()), bluetooth.scannedDevices(filter).first())
        }

        mainAction {
            bluetooth.stopScanning(cleanMode = BluetoothService.CleanMode.ONLY_PROVIDED_FILTER)
        }

        test {
            scanner.didStopScanningMock.verify(times = 2)
            assertEquals(listOf(deferredDevice1.await()), it)
            assertEquals(listOf(deferredDevice1.getCompleted()), bluetooth.scannedDevices().first())
            assertEquals(emptyList(), bluetooth.scannedDevices(filter).first())
        }

        mainAction {
            bluetooth.startScanning(cleanMode = BluetoothService.CleanMode.ONLY_PROVIDED_FILTER)
        }

        test {
            scanner.didStartScanningMock.verify(eq(emptySet()), times = 2)
            assertEquals(emptyList(), it)
            assertEquals(emptyList(), bluetooth.scannedDevices().first())
            assertEquals(emptyList(), bluetooth.scannedDevices(filter).first())
        }

        mainAction {
            scanDevice(deferredDevice2.await(), createDeviceWrapper(identifier = deferredDevice2.await().identifier), -100, MockAdvertisementData())
        }

        test {
            assertEquals(listOf(deferredDevice2.await()), it)
            assertEquals(listOf(deferredDevice2.getCompleted()), bluetooth.scannedDevices().first())
            assertEquals(emptyList(), bluetooth.scannedDevices(filter).first())
        }
    }

    private fun BluetoothContext.createAndScanDevice(deferred: CompletableDeferred<Device>) {
        val rssi = -100
        val advertisementData = MockAdvertisementData()
        val deviceWrapper = createDeviceWrapper()
        val device = createDevice(ConnectionSettings(), deviceWrapper, rssi, advertisementData) {
            MockDeviceConnectionManager(true, deviceWrapper, ConnectionSettings(), coroutineScope)
        }
        scanDevice(device, deviceWrapper, rssi, advertisementData)
        deferred.complete(device)
    }
}
