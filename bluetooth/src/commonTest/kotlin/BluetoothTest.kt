/*
 Copyright (c) 2020. Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.bluetooth.device.*
import com.splendo.kaluga.bluetooth.device.MockDeviceConnectionManager
import com.splendo.kaluga.bluetooth.scanner.*
import com.splendo.kaluga.permissions.Permit
import com.splendo.kaluga.permissions.Support
import com.splendo.kaluga.state.StateRepoAccesor
import com.splendo.kaluga.test.BaseTest
import com.splendo.kaluga.test.FlowTest
import com.splendo.kaluga.utils.EmptyCompletableDeferred
import com.splendo.kaluga.utils.complete
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.test.*

abstract class BluetoothTest : BaseTest() {

    lateinit var permissionManager: MockPermissionManager
    lateinit var mockBaseScanner: MockBaseScanner
    lateinit var bluetooth: Bluetooth

    lateinit var bluetoothDisabledCalled: EmptyCompletableDeferred
    lateinit var bluetoothMissingPermissionCalled: EmptyCompletableDeferred

    abstract fun createFilter() : Set<UUID>
    abstract fun createDeviceInfoHolder() : DeviceInfoHolder

    @BeforeTest
    fun setup() {
        super.beforeTest()

        permissionManager = MockPermissionManager()
        val permissions = MockBasePermissions(permissionManager)

        val scannerBuilder = object : BaseScanner.Builder {
            override val autoEnableBluetooth: Boolean
                get() = true

            override fun create(stateRepoAccessor: StateRepoAccesor<ScanningState>, coroutineScope: CoroutineScope): BaseScanner {
                mockBaseScanner = MockBaseScanner(permissions, stateRepoAccessor, coroutineScope)
                return mockBaseScanner
            }
        }

        bluetoothDisabledCalled = EmptyCompletableDeferred()
        bluetoothMissingPermissionCalled = EmptyCompletableDeferred()

        val builder = Bluetooth.Builder(scannerBuilder, permissions)
        builder.setOnNotifyBluetoothDisabled {
            bluetoothDisabledCalled.complete()
        }
        builder.setOnRequestPermission {
            bluetoothMissingPermissionCalled.complete()
        }
        bluetooth = Bluetooth(builder)
    }

    @Test
    fun testScanDevice() = runBlocking {
        permissionManager.support = Support.POWER_ON
        permissionManager.permit = Permit.ALLOWED

        FlowTest(bluetooth.devices()).runBlockingWithFlow { flowTest ->
            flowTest.test {
                assertEquals(emptyList(), it)
                assertTrue(mockBaseScanner.startMonitoringBluetooth.isCompleted)
            }
            flowTest.action {
                bluetooth.startScanning()
            }
            flowTest.test {
                assertEquals(emptyList(), it)
                assertEquals(emptySet(), mockBaseScanner.scanForDevicesCompleted.getCompleted())
            }
            val filter = createFilter()
            flowTest.action {
                mockBaseScanner.reset()
                bluetooth.startScanning(filter)
            }
            flowTest.test {
                assertEquals(emptyList(), it)
            }
            flowTest.test {
                assertEquals(emptyList(), it)
                assertTrue(mockBaseScanner.stopScanningCompleted.isCompleted)
            }
            flowTest.test {
                assertEquals(emptyList(), it)
                assertEquals(filter, mockBaseScanner.scanForDevicesCompleted.getCompleted())
            }
            flowTest.test {
                assertEquals(emptyList(), it)
            }
            val device = createDevice()
            val scanCompleted = EmptyCompletableDeferred()
            flowTest.action {
                launch {
                    scanDevice(device, scanCompleted)
                }
            }
            scanCompleted.await()
            flowTest.test {
                assertEquals(listOf(device), it)
            }
            flowTest.action {
                mockBaseScanner.reset()
                bluetooth.stopScanning()
            }
            flowTest.test {
                assertEquals(listOf(device), it)
            }
            flowTest.test {
                assertTrue(mockBaseScanner.stopScanningCompleted.isCompleted)
                assertEquals(listOf(device), it)
            }
        }
    }

    @Test
    fun testBluetoothDisabled() = runBlocking {
        permissionManager.support = Support.POWER_OFF
        permissionManager.permit = Permit.ALLOWED

        FlowTest(bluetooth.devices()).runBlockingWithFlow { flowTest ->
            flowTest.test {
                assertEquals(emptyList(), it)
                assertTrue(bluetoothDisabledCalled.isCompleted)
            }
        }
    }

    @Test
    fun testBluetoothMissingPermissions() = runBlocking {
        permissionManager.support = Support.POWER_ON
        permissionManager.permit = Permit.DENIED

        FlowTest(bluetooth.devices()).runBlockingWithFlow { flowTest ->
            flowTest.test {
                assertEquals(emptyList(), it)
                assertTrue(bluetoothMissingPermissionCalled.isCompleted)
            }
        }
    }

    @Test
    fun testGetDevice() = runBlocking {
        permissionManager.support = Support.POWER_ON
        permissionManager.permit = Permit.ALLOWED

        val device = createDevice()
        FlowTest(bluetooth.devices()[device.identifier]).runBlockingWithFlow { flowTest ->
            flowTest.test {
                assertNull(it)
            }
            val scanCompleted = EmptyCompletableDeferred()
            flowTest.action {
                launch {
                    scanDevice(device, scanCompleted)
                }
                bluetooth.startScanning()
            }
            scanCompleted.await()
            val foundDevice = CompletableDeferred<Device>()
            awaitDevice(flowTest, foundDevice)
            assertEquals(device, foundDevice.await())
            flowTest.action { bluetooth.stopScanning() }
            flowTest.test {
                assertEquals(device, it)
            }
        }
    }

    private suspend fun scanDevice(device: Device, scanCompleted: EmptyCompletableDeferred) {
        val scanningState = bluetooth.scanningStateRepo.flow().filter { it is ScanningState.Enabled.Scanning }.first() as ScanningState.Enabled.Scanning
        scanningState.discoverDevices(device)
        scanCompleted.complete()
    }

    private fun awaitDevice(flowTest: FlowTest<Device?>, foundDevice: CompletableDeferred<Device>) {
        val deviceNotFound = EmptyCompletableDeferred()
        deviceNotFound.invokeOnCompletion {
            awaitDevice(flowTest, foundDevice)
        }
        flowTest.test {
            if (it != null) {
                foundDevice.complete(it)
            } else {
                deviceNotFound.complete()
            }
        }
    }

    private fun createDevice(): Device {
        return Device(DeviceTest.reconnectionAttempts, createDeviceInfoHolder(), DeviceTest.initialRssi, object : BaseDeviceConnectionManager.Builder {
            override fun create(reconnectionAttempts: Int, deviceInfo: DeviceInfoHolder, repoAccessor: StateRepoAccesor<DeviceState>): BaseDeviceConnectionManager {
                return MockDeviceConnectionManager(reconnectionAttempts, deviceInfo, repoAccessor)
            }
        })
    }

}

