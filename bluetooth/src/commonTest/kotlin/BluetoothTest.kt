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
import kotlinx.coroutines.async
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
    abstract fun createService(stateRepoAccesor: StateRepoAccesor<DeviceState>) : Service

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

    @Test
    fun testConnectDevice() = runBlocking {
        permissionManager.support = Support.POWER_ON
        permissionManager.permit = Permit.ALLOWED

        val device = createDevice()
        launch {
            scanDevice(device)
        }
        bluetooth.startScanning()
        connectDevice(device)
        disconnectDevice(device)
    }

    @Test
    fun testGetServices() = runBlocking {
        permissionManager.support = Support.POWER_ON
        permissionManager.permit = Permit.ALLOWED

        val device = createDevice()
        val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
        val service = createService(connectionManager.repoAccessor)
        launch {
            scanDevice(device)
        }
        bluetooth.startScanning()

        FlowTest(bluetooth.devices()[device.identifier].services()).runBlockingWithFlow { flowTest ->
            flowTest.test {
                assertEquals(emptyList(), it)
            }
            flowTest.action {
                connectDevice(device)
            }
            flowTest.test {
                assertEquals(emptyList(), it)
                assertTrue(connectionManager.discoverServicesCompleted.isCompleted)
            }
            flowTest.test {
                assertEquals(emptyList(), it)
            }
            flowTest.action {
                val discoveringState = device.flow().filter { it is DeviceState.Connected.Discovering }.first() as DeviceState.Connected.Discovering
                discoveringState.didDiscoverServices(listOf(service))
            }
            flowTest.test {
                assertEquals(listOf(service), it)
            }
        }
    }

    @Test
    fun testGetService() = runBlocking {
        permissionManager.support = Support.POWER_ON
        permissionManager.permit = Permit.ALLOWED

        val device = createDevice()
        val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
        val service = createService(connectionManager.repoAccessor)
        launch {
            scanDevice(device)
        }
        bluetooth.startScanning()

        FlowTest(bluetooth.devices()[device.identifier].services()[service.uuid]).runBlockingWithFlow { flowTest ->
            flowTest.test {
                assertNull(it)
            }

            val discoveryCompleted = EmptyCompletableDeferred()
            flowTest.action {
                connectDevice(device)
                val discoveringState = CompletableDeferred<DeviceState.Connected.Discovering>()
                val job = launch {
                    device.flow().collect { state ->
                        print("Whatever")
                        when (state) {
                            is DeviceState.Connected.Discovering -> discoveringState.complete(state)
                        }
                    }
                }
                discoveringState.await().didDiscoverServices(listOf(service))
                job.cancel()
                discoveryCompleted.complete()
            }
            discoveryCompleted.await()
            val foundService = CompletableDeferred<Service>()
            awaitService(flowTest, foundService)
            assertEquals(service, foundService.await())
        }
    }

    private suspend fun scanDevice(device: Device, scanCompleted: EmptyCompletableDeferred? = null) {
        val scanningState = bluetooth.scanningStateRepo.flow().filter { it is ScanningState.Enabled.Scanning }.first() as ScanningState.Enabled.Scanning
        scanningState.discoverDevices(device)
        scanCompleted?.complete()
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

    private fun connectDevice(device: Device) = runBlocking {
        val connectingJob = async {
            bluetooth.devices()[device.identifier].connect()
        }
        val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
        connectionManager.connectCompleted.await()
        val connectingState = device.flow().filter { it is DeviceState.Connecting }.first() as DeviceState.Connecting
        connectingState.didConnect()
        connectingJob.await()
    }

    private fun disconnectDevice(device: Device) = runBlocking {
        val disconnectingJob = async {
            bluetooth.devices()[device.identifier].disconnect()
        }
        val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
        connectionManager.disconnectCompleted.await()
        val disconnectingState = device.flow().filter { it is DeviceState.Disconnecting }.first() as DeviceState.Disconnecting
        disconnectingState.didDisconnect()
        disconnectingJob.await()
    }

    private fun awaitService(flowTest: FlowTest<Service?>, foundService: CompletableDeferred<Service>) {
        val serviceNotFound = EmptyCompletableDeferred()
        serviceNotFound.invokeOnCompletion {
            awaitService(flowTest, foundService)
        }
        flowTest.test {
            if (it != null) {
                foundService.complete(it)
            } else {
                serviceNotFound.complete()
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

