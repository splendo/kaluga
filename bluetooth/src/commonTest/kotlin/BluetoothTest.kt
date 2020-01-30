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

import com.splendo.kaluga.base.MultiplatformMainScope
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.bluetooth.device.*
import com.splendo.kaluga.bluetooth.device.MockDeviceConnectionManager
import com.splendo.kaluga.bluetooth.scanner.*
import com.splendo.kaluga.permissions.Permit
import com.splendo.kaluga.permissions.Support
import com.splendo.kaluga.state.StateRepo
import com.splendo.kaluga.test.BaseTest
import com.splendo.kaluga.test.FlowTest
import com.splendo.kaluga.utils.EmptyCompletableDeferred
import com.splendo.kaluga.utils.complete
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlin.test.*

abstract class BluetoothTest : BaseTest() {

    companion object {
        val initialRssi = -100
    }

    private val mainScope = MultiplatformMainScope()

    lateinit var permissionManager: MockPermissionManager
    lateinit var mockBaseScanner: MockBaseScanner
    lateinit var bluetooth: Bluetooth

    lateinit var bluetoothDisabledCalled: EmptyCompletableDeferred
    lateinit var bluetoothMissingPermissionCalled: EmptyCompletableDeferred

    abstract fun createFilter() : Set<UUID>
    abstract fun createDeviceInfoHolder() : DeviceInfoHolder
    abstract fun createService(stateRepo: StateRepo<DeviceState>) : Service

    @BeforeTest
    open fun setup() {
        super.beforeTest()

        permissionManager = MockPermissionManager()
        permissionManager.support = Support.POWER_ON
        permissionManager.permit = Permit.ALLOWED
        val permissions = MockBasePermissions(permissionManager)

        val scannerBuilder = object : BaseScanner.Builder {
            override val autoEnableBluetooth: Boolean
                get() = true

            override fun create(stateRepo: StateRepo<ScanningState>): BaseScanner {
                mockBaseScanner = MockBaseScanner(permissions, stateRepo)
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
        bluetooth = Bluetooth(builder, mainScope.coroutineContext)
    }

    @Test
    fun testScanDevice() = runBlocking {
        FlowTest(bluetooth.devices()).testWithFlow {
            test {
                assertEquals(emptyList(), it)
                assertTrue(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
            }
            action {
                bluetooth.startScanning()
            }
            test {
                assertEquals(emptyList(), it)
                assertEquals(emptySet(), mockBaseScanner.scanForDevicesCompleted.getCompleted())
            }
            val filter = createFilter()
            action {
                mockBaseScanner.reset()
                bluetooth.startScanning(filter)
            }
            test {
                assertEquals(emptyList(), it)
            }
            test {
                assertEquals(emptyList(), it)
                assertTrue(mockBaseScanner.stopScanningCompleted.isCompleted)
            }
            test {
                assertEquals(emptyList(), it)
                assertEquals(filter, mockBaseScanner.scanForDevicesCompleted.getCompleted())
            }
            test {
                assertEquals(emptyList(), it)
            }
            val device = createDevice()
            val scanCompleted = EmptyCompletableDeferred()
            action {
                mainScope.launch {
                    scanDevice(device, scanCompleted)
                }
            }
            scanCompleted.await()
            test {
                assertEquals(listOf(device), it)
            }
            action {
                mockBaseScanner.reset()
                bluetooth.stopScanning()
            }
            test {
                assertEquals(listOf(device), it)
            }
            test {
                assertTrue(mockBaseScanner.stopScanningCompleted.isCompleted)
                assertEquals(listOf(device), it)
            }
        }
    }

    @Test
    fun testBluetoothDisabled() = runBlocking {
        permissionManager.support = Support.POWER_OFF
        permissionManager.permit = Permit.ALLOWED

        FlowTest(bluetooth.devices()).testWithFlow {
            test {
                assertEquals(emptyList(), it)
                assertTrue(bluetoothDisabledCalled.isCompleted)
            }
        }
    }

    @Test
    fun testBluetoothMissingPermissions() = runBlocking {
        permissionManager.support = Support.POWER_ON
        permissionManager.permit = Permit.DENIED

        FlowTest(bluetooth.devices()).testWithFlow {
            test {
                assertEquals(emptyList(), it)
                assertTrue(bluetoothMissingPermissionCalled.isCompleted)
            }
        }
    }

    @Test
    fun testGetDevice() = runBlocking {
        val device = createDevice()
        FlowTest(bluetooth.devices()[device.identifier]).testWithFlow {
            test {
                assertNull(it)
            }
            val scanCompleted = EmptyCompletableDeferred()
            action {
                mainScope.launch {
                    scanDevice(device, scanCompleted)
                }
                bluetooth.startScanning()
            }
            scanCompleted.await()
            val foundDevice = CompletableDeferred<Device>()
            awaitDevice(this, foundDevice)
            assertEquals(device, foundDevice.await())
            action { bluetooth.stopScanning() }
            test {
                assertEquals(device, it)
            }
        }
    }

    @Test
    fun testConnectDevice() = runBlocking {
        val device = createDevice()
        mainScope.launch {
            scanDevice(device)
        }
        bluetooth.startScanning()
        connectDevice(device)
        disconnectDevice(device)
    }

    @Test
    fun testRssi() = runBlocking {
        val device = createDevice()
        val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
        val newRssi = -42
        FlowTest(bluetooth.devices()[device.identifier].rssi()).testWithFlow {
            action {
                mainScope.launch {
                    scanDevice(device)
                }
                bluetooth.startScanning()
                connectDevice(device)
            }
            test {
                assertEquals(initialRssi, it)
            }
            action {
                bluetooth.devices()[device.identifier].updateRssi()
                connectionManager.readRssiCompleted.await()
                device.flow().filter { it is DeviceState.Connected }.first()
                device.deviceConnectionManager.handleNewRssi(newRssi)
            }
            test(2) {
                assertEquals(newRssi, it)
            }

        }
    }

    @Test
    fun testGetServices() = runBlocking {
        val device = createDevice()
        val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
        val service = createService(connectionManager.stateRepo)
        mainScope.launch {
            scanDevice(device)
        }
        bluetooth.startScanning()

        FlowTest(bluetooth.devices()[device.identifier].services()).testWithFlow {
            test {
                assertEquals(emptyList(), it)
            }
            action {
                connectDevice(device)
            }
            test(1) {
                assertEquals(emptyList(), it)
                assertTrue(connectionManager.discoverServicesCompleted.isCompleted)
            }
            test {
                assertEquals(emptyList(), it)
            }
            action {
                discoverService(service, device)
            }
            test {
                assertEquals(listOf(service), it)
            }
        }
    }

    @Test
    fun testGetService() = runBlocking {
        val device = createDevice()
        val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
        val service = createService(connectionManager.stateRepo)
        mainScope.launch {
            scanDevice(device)
        }
        bluetooth.startScanning()

        FlowTest(bluetooth.devices()[device.identifier].services()[service.uuid]).testWithFlow {
            test {
                assertNull(it)
            }

            action {
                connectDevice(device)
                discoverService(service, device)
            }
            val foundService = CompletableDeferred<Service>()
            awaitService(this, foundService)
            assertEquals(service, foundService.await())
        }
    }

    @Test
    fun testGetCharacteristics() = runBlocking {
        val device = createDevice()
        val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
        val service = createService(connectionManager.stateRepo)
        mainScope.launch {
            scanDevice(device)
        }
        bluetooth.startScanning()

        FlowTest(bluetooth.devices()[device.identifier].services()[service.uuid].characteristics()).testWithFlow {
            test {
                assertEquals(emptyList(), it)
            }
            action {
                connectDevice(device)
            }
            test {
                assertEquals(emptyList(), it)
                assertTrue(connectionManager.discoverServicesCompleted.isCompleted)
            }
            action {
                discoverService(service, device)
            }
            test(2) {
                assertEquals(service.characteristics, it)
            }

        }
    }

    @Test
    fun testGetCharacteristic() = runBlocking {
        val device = createDevice()
        val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
        val service = createService(connectionManager.stateRepo)
        val characteristic = service.characteristics.first()
        mainScope.launch {
            scanDevice(device)
        }
        bluetooth.startScanning()

        FlowTest(bluetooth.devices()[device.identifier].services()[service.uuid].characteristics()[characteristic.uuid]).testWithFlow {
            test {
                assertNull(it)
            }

            action {
                connectDevice(device)
                discoverService(service, device)
            }
            val foundCharacteristic = CompletableDeferred<Characteristic>()
            awaitCharacteristic(this, foundCharacteristic)
            assertEquals(characteristic, foundCharacteristic.await())
        }
    }

    @ExperimentalStdlibApi
    @Test
    fun testGetCharacteristicValue() = runBlocking {
        val device = createDevice()
        val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
        val service = createService(connectionManager.stateRepo)
        val characteristic = service.characteristics.first()
        val newValue = "Test".encodeToByteArray()

        FlowTest(bluetooth.devices()[device.identifier].services()[service.uuid].characteristics()[characteristic.uuid].value()).testWithFlow {
            action {
                mainScope.launch {
                    scanDevice(device)
                }
                bluetooth.startScanning()
                connectDevice(device)
                discoverService(service, device)
                characteristic.set(newValue)
            }
            val foundByte = CompletableDeferred<ByteArray>()
            awaitByte(this, foundByte)
            assertEquals(newValue, foundByte.await())
        }
    }

    @Test
    fun testGetDescriptors() = runBlocking {
        val device = createDevice()
        val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
        val service = createService(connectionManager.stateRepo)
        val characteristic = service.characteristics.first()

        FlowTest(bluetooth.devices()[device.identifier].services()[service.uuid].characteristics()[characteristic.uuid].descriptors()).testWithFlow {
            action {
                mainScope.launch {
                    scanDevice(device)
                }
                bluetooth.startScanning()
                connectDevice(device)
            }
            test(1) {
                assertEquals(emptyList(), it)
            }
            test {
                assertEquals(emptyList(), it)
                assertTrue(connectionManager.discoverServicesCompleted.isCompleted)
            }
            test {
                assertEquals(emptyList(), it)
            }
            action {
                discoverService(service, device)
            }
            test {
                assertEquals(characteristic.descriptors, it)
            }

        }
    }

    @Test
    fun testGetDescriptor() = runBlocking {
        val device = createDevice()
        val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
        val service = createService(connectionManager.stateRepo)
        val characteristic = service.characteristics.first()
        val descriptor = characteristic.descriptors.first()

        FlowTest(bluetooth.devices()[device.identifier].services()[service.uuid].characteristics()[characteristic.uuid].descriptors()[descriptor.uuid]).testWithFlow {
            action {
                mainScope.launch {
                    scanDevice(device)
                }
                bluetooth.startScanning()
                connectDevice(device)
                discoverService(service, device)
            }
            val foundDescriptor = CompletableDeferred<Descriptor>()
            awaitDescriptor(this, foundDescriptor)
            assertEquals(descriptor, foundDescriptor.await())
        }
    }

    @ExperimentalStdlibApi
    @Test
    fun testGetDescriptorValue() = runBlocking {
        val device = createDevice()
        val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
        val service = createService(connectionManager.stateRepo)
        val characteristic = service.characteristics.first()
        val descriptor = characteristic.descriptors.first()
        val newValue = "Test".encodeToByteArray()

        FlowTest(bluetooth.devices()[device.identifier].services()[service.uuid].characteristics()[characteristic.uuid].descriptors()[descriptor.uuid].value()).testWithFlow {
            action {
                mainScope.launch {
                    scanDevice(device)
                }
                bluetooth.startScanning()
                connectDevice(device)
                discoverService(service, device)
                descriptor.set(newValue)
            }
            val foundByte = CompletableDeferred<ByteArray>()
            awaitByte(this, foundByte)
            assertEquals(newValue, foundByte.await())
        }
    }

    private suspend fun scanDevice(device: Device, scanCompleted: EmptyCompletableDeferred? = null) {
        bluetooth.scanningStateRepo.flow().filter{it is ScanningState.Enabled.Scanning}.first()
        bluetooth.scanningStateRepo.takeAndChangeState {state ->
            when(state) {
                is ScanningState.Enabled.Scanning -> state.discoverDevices(device)
                else -> state.remain
            }
        }
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

    private suspend fun connectDevice(device: Device) {
        val connectingJob = mainScope.async {
            bluetooth.devices()[device.identifier].connect()
        }
        val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
        connectionManager.connectCompleted.await()
        device.deviceConnectionManager.handleConnect()
        connectingJob.await()
    }

    private suspend fun disconnectDevice(device: Device) {
        val disconnectingJob = mainScope.async {
            bluetooth.devices()[device.identifier].disconnect()
        }
        val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
        connectionManager.disconnectCompleted.await()
        device.deviceConnectionManager.handleDisconnect()
        disconnectingJob.await()
    }

    private suspend fun discoverService(service: Service, device: Device) {
        device.flow().filter { it is DeviceState.Connected.Discovering }.first()
        device.deviceConnectionManager.handleScanCompleted(listOf(service))
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

    private fun awaitCharacteristic(flowTest: FlowTest<Characteristic?>, foundCharacteristic: CompletableDeferred<Characteristic>) {
        val characteristicNotFound = EmptyCompletableDeferred()
        characteristicNotFound.invokeOnCompletion {
            awaitCharacteristic(flowTest, foundCharacteristic)
        }
        flowTest.test {
            if (it != null) {
                foundCharacteristic.complete(it)
            } else {
                characteristicNotFound.complete()
            }
        }
    }

    private fun awaitDescriptor(flowTest: FlowTest<Descriptor?>, foundDescriptor: CompletableDeferred<Descriptor>) {
        val descriptorNotFound = EmptyCompletableDeferred()
        descriptorNotFound.invokeOnCompletion {
            awaitDescriptor(flowTest, foundDescriptor)
        }
        flowTest.test {
            if (it != null) {
                foundDescriptor.complete(it)
            } else {
                descriptorNotFound.complete()
            }
        }
    }

    private fun awaitByte(flowTest: FlowTest<ByteArray?>, foundByte: CompletableDeferred<ByteArray>) {
        val byteNotFound = EmptyCompletableDeferred()
        byteNotFound.invokeOnCompletion {
            awaitByte(flowTest, foundByte)
        }
        flowTest.test {
            if (it != null) {
                foundByte.complete(it)
            } else {
                byteNotFound.complete()
            }
        }
    }

    private fun createDevice(): Device {
        return Device(ConnectionSettings(), createDeviceInfoHolder(), initialRssi, object : BaseDeviceConnectionManager.Builder {
            override fun create(connectionSettings: ConnectionSettings, deviceInfo: DeviceInfoHolder, stateRepo: StateRepo<DeviceState>): BaseDeviceConnectionManager {
                return MockDeviceConnectionManager(connectionSettings, deviceInfo, stateRepo)
            }
        }, mainScope.coroutineContext)
    }

}

