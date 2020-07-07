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
import com.splendo.kaluga.bluetooth.device.BaseAdvertisementData
import com.splendo.kaluga.bluetooth.device.BaseDeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceHolder
import com.splendo.kaluga.bluetooth.device.DeviceInfoImpl
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.bluetooth.device.MockAdvertisementData
import com.splendo.kaluga.bluetooth.device.MockDeviceConnectionManager
import com.splendo.kaluga.bluetooth.scanner.BaseScanner
import com.splendo.kaluga.bluetooth.scanner.MockBaseScanner
import com.splendo.kaluga.bluetooth.scanner.ScanningState
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionState
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.state.StateRepo
import com.splendo.kaluga.test.BaseTest
import com.splendo.kaluga.test.FlowTest
import com.splendo.kaluga.test.MockPermissionManager
import com.splendo.kaluga.test.permissions.MockPermissionsBuilder
import com.splendo.kaluga.utils.EmptyCompletableDeferred
import com.splendo.kaluga.utils.complete
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

abstract class BluetoothTest : BaseTest() {

    companion object {
        val initialRssi = -100
        val initialAdvertisementData = MockAdvertisementData()
    }

    private lateinit var permissionsBuilder: MockPermissionsBuilder
    private lateinit var permissions: Permissions
    private val permissionManager: MockPermissionManager<Permission.Bluetooth>
        get() {
            return permissionsBuilder.bluetoothPMManager
        }
    private lateinit var mockBaseScanner: MockBaseScanner
    private lateinit var bluetooth: Bluetooth

    abstract fun createFilter(): Set<UUID>
    abstract fun createDeviceHolder(): DeviceHolder
    abstract fun createService(stateRepo: StateRepo<DeviceState>): Service

    @Test
    fun testIsScanning() = runBlocking {
        setupPermissions(this)
        setupBluetooth(coroutineScope = this)
        val devicesJob = launch {
            bluetooth.devices().collect { }
        }
        val flowTest = FlowTest(bluetooth.isScanning(), this)
        flowTest.testWithFlow {
            test {
                assertFalse(it)
            }
            action {
                bluetooth.startScanning()
            }
            test {
                assertTrue(it)
            }
            action {
                bluetooth.stopScanning()
            }
            test {
                assertFalse(it)
            }
        }
        devicesJob.cancel()
    }

    @Test
    fun testScanDevice() = runBlocking {
        setupPermissions(this)
        setupBluetooth(coroutineScope = this)
        val flowTest = FlowTest(bluetooth.devices(), this)
        flowTest.testWithFlow {
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
            val deviceHolder = createDeviceHolder()
            val device = createDevice(deviceHolder, coroutineScope = this@runBlocking)
            val scanCompleted = EmptyCompletableDeferred()
            action {
                this@runBlocking.launch {
                    scanDevice(device, deviceHolder, scanCompleted = scanCompleted)
                }
            }
            test {
                scanCompleted.await()
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

        permissionManager.hasStoppedMonitoring.await()
        mockBaseScanner.stopMonitoringPermissions.await()
        mockBaseScanner.stopMonitoringBluetoothCompleted.await()
    }

    @Test
    fun testGetDevice() = runBlocking {
        setupPermissions(this)
        setupBluetooth(coroutineScope = this)
        val deviceHolder = createDeviceHolder()
        val device = createDevice(deviceHolder, coroutineScope = this)
        launch {
            scanDevice(device, deviceHolder)
        }
        bluetooth.startScanning()
        val flowTest = FlowTest(bluetooth.devices()[device.identifier], this)
        flowTest.testWithFlow {
            val foundDevice = CompletableDeferred<Device>()
            awaitDevice(this, foundDevice)
            assertEquals(device, foundDevice.await())
            action { bluetooth.stopScanning() }
            test {
                assertEquals(device, it)
            }
        }

        permissionManager.hasStoppedMonitoring.await()
        mockBaseScanner.stopMonitoringPermissions.await()
        mockBaseScanner.stopMonitoringBluetoothCompleted.await()
    }

    @Test
    fun testConnectDevice() = runBlocking {
        setupPermissions(this)
        setupBluetooth(coroutineScope = this)
        val deviceHolder = createDeviceHolder()
        val advertisementData = MockAdvertisementData()
        val device = createDevice(deviceHolder, coroutineScope = this)
        launch {
            scanDevice(device, deviceHolder)
        }
        bluetooth.startScanning()
        val deviceConnectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
        connectDevice(device, deviceConnectionManager, this)
        disconnectDevice(device, device.deviceConnectionManager, this)

        permissionManager.hasStoppedMonitoring.await()
        mockBaseScanner.stopMonitoringPermissions.await()
        mockBaseScanner.stopMonitoringBluetoothCompleted.await()
    }

    @Test
    fun testRssi() = runBlocking {
        setupPermissions(this)
        setupBluetooth(coroutineScope = this)
        val deviceHolder = createDeviceHolder()
        val device = createDevice(deviceHolder, coroutineScope = this)
        val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
        val newRssi = -42
        launch {
            scanDevice(device, deviceHolder)
        }
        bluetooth.startScanning()
        val flowTest = FlowTest(bluetooth.devices()[device.identifier].rssi(), this)
        flowTest.testWithFlow {
            test {
                assertEquals(initialRssi, it)
            }
            action {
                connectDevice(device, connectionManager, this@runBlocking)
                bluetooth.devices()[device.identifier].updateRssi()
                connectionManager.readRssiCompleted.await()
                device.flow().filter { it is DeviceState.Connected }.first()
                device.deviceConnectionManager.handleNewRssi(newRssi)
            }
            test {
                assertEquals(newRssi, it)
            }
        }

        permissionManager.hasStoppedMonitoring.await()
        mockBaseScanner.stopMonitoringPermissions.await()
        mockBaseScanner.stopMonitoringBluetoothCompleted.await()
    }

    @Test
    fun testAdvertisementData() = runBlocking {
        setupPermissions(this)
        setupBluetooth(coroutineScope = this)
        val deviceHolder = createDeviceHolder()
        val advertisementData = MockAdvertisementData(name = "Name")
        val device = createDevice(deviceHolder, initialRssi, advertisementData, this)
        val newAdvertisementData = MockAdvertisementData(name = "New Name")
        launch {
            scanDevice(device, deviceHolder, initialRssi, advertisementData)
        }
        bluetooth.startScanning()
        val flowTest = FlowTest(bluetooth.devices()[device.identifier].advertisement(), this)
        flowTest.testWithFlow {
            test {
                assertEquals(advertisementData, it)
            }
            action {
                scanDevice(device, deviceHolder, initialRssi, newAdvertisementData)
            }
            test {
                assertEquals(newAdvertisementData, it)
            }
        }

        permissionManager.hasStoppedMonitoring.await()
        mockBaseScanner.stopMonitoringPermissions.await()
        mockBaseScanner.stopMonitoringBluetoothCompleted.await()
    }

    @Test
    fun testDistance() = runBlocking {
        setupPermissions(this)
        setupBluetooth(coroutineScope = this)
        val deviceHolder = createDeviceHolder()
        val advertisementData = MockAdvertisementData(txPowerLevel = -50)
        val device = createDevice(deviceHolder, -50, advertisementData, this)
        launch {
            scanDevice(device, deviceHolder, -50, advertisementData)
        }
        bluetooth.startScanning()
        val flowTest = FlowTest(bluetooth.devices()[device.identifier].distance(), this)
        flowTest.testWithFlow {
            test {
                assertEquals(1.0, it)
            }
            action {
                scanDevice(device, deviceHolder, -70, advertisementData)
            }
            test {
                assertEquals(5.5, it)
            }
            action {
                scanDevice(device, deviceHolder, -50, advertisementData)
            }
            test {
                assertEquals(4.0, it)
            }
            action {
                scanDevice(device, deviceHolder, -30, advertisementData)
            }
            test {
                assertEquals(3.025, it)
            }
            action {
                scanDevice(device, deviceHolder, -30, advertisementData)
            }
            test {
                assertEquals(2.44, it)
            }
            action {
                scanDevice(device, deviceHolder, -70, advertisementData)
            }
            test {
                assertEquals(4.24, it)
            }
        }

        permissionManager.hasStoppedMonitoring.await()
        mockBaseScanner.stopMonitoringPermissions.await()
        mockBaseScanner.stopMonitoringBluetoothCompleted.await()
    }

    @Test
    fun testGetServices() = runBlocking {
        setupPermissions(this)
        setupBluetooth(coroutineScope = this)
        val deviceHolder = createDeviceHolder()
        val device = createDevice(deviceHolder, coroutineScope = this)
        val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
        val service = createService(connectionManager.stateRepo)
        launch {
            scanDevice(device, deviceHolder, initialRssi)
        }
        bluetooth.startScanning()

        val flowTest = FlowTest(bluetooth.devices()[device.identifier].services(), this)
        flowTest.testWithFlow {
            test {
                assertEquals(emptyList(), it)
            }
            action {
                connectDevice(device, connectionManager, this@runBlocking)
                connectionManager.discoverServicesCompleted.await()
                discoverService(service, device)
            }
            test {
                assertEquals(listOf(service), it)
            }
        }

        permissionManager.hasStoppedMonitoring.await()
        mockBaseScanner.stopMonitoringPermissions.await()
        mockBaseScanner.stopMonitoringBluetoothCompleted.await()
    }

    @Test
    fun testGetService() = runBlocking {
        setupPermissions(this)
        setupBluetooth(coroutineScope = this)
        val deviceHolder = createDeviceHolder()
        val device = createDevice(deviceHolder, coroutineScope = this)
        val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
        val service = createService(connectionManager.stateRepo)
        launch {
            scanDevice(device, deviceHolder)
        }
        bluetooth.startScanning()

        val flowTest = FlowTest(bluetooth.devices()[device.identifier].services()[service.uuid], this)
        flowTest.testWithFlow {
            test {
                assertNull(it)
            }

            action {
                connectDevice(device, connectionManager, this@runBlocking)
                discoverService(service, device)
            }
            val foundService = CompletableDeferred<Service>()
            awaitService(this, foundService)
            assertEquals(service, foundService.await())
        }

        permissionManager.hasStoppedMonitoring.await()
        mockBaseScanner.stopMonitoringPermissions.await()
        mockBaseScanner.stopMonitoringBluetoothCompleted.await()
    }

    @Test
    fun testGetCharacteristics() = runBlocking {
        setupPermissions(this)
        setupBluetooth(coroutineScope = this)
        val deviceHolder = createDeviceHolder()
        val device = createDevice(deviceHolder, coroutineScope = this)
        val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
        val service = createService(connectionManager.stateRepo)
        launch {
            scanDevice(device, deviceHolder)
        }
        bluetooth.startScanning()

        val flowTest = FlowTest(bluetooth.devices()[device.identifier].services()[service.uuid].characteristics(), this)
        flowTest.testWithFlow {
            test {
                assertEquals(emptyList(), it)
            }
            action {
                connectDevice(device, connectionManager, this@runBlocking)
                connectionManager.discoverServicesCompleted.await()
                discoverService(service, device)
            }
            test {
                assertEquals(service.characteristics, it)
            }
        }

        permissionManager.hasStoppedMonitoring.await()
        mockBaseScanner.stopMonitoringPermissions.await()
        mockBaseScanner.stopMonitoringBluetoothCompleted.await()
    }

    @Test
    fun testGetCharacteristic() = runBlocking {
        setupPermissions(this)
        setupBluetooth(coroutineScope = this)
        val deviceHolder = createDeviceHolder()
        val device = createDevice(deviceHolder, coroutineScope = this)
        val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
        val service = createService(connectionManager.stateRepo)
        val characteristic = service.characteristics.first()
        launch {
            scanDevice(device, deviceHolder)
        }
        bluetooth.startScanning()

        val flowTest = FlowTest(bluetooth.devices()[device.identifier].services()[service.uuid].characteristics()[characteristic.uuid], this)
        flowTest.testWithFlow {
            test {
                assertNull(it)
            }

            action {
                connectDevice(device, connectionManager, this@runBlocking)
                discoverService(service, device)
            }
            val foundCharacteristic = CompletableDeferred<Characteristic>()
            awaitCharacteristic(this, foundCharacteristic)
            assertEquals(characteristic, foundCharacteristic.await())
        }

        permissionManager.hasStoppedMonitoring.await()
        mockBaseScanner.stopMonitoringPermissions.await()
        mockBaseScanner.stopMonitoringBluetoothCompleted.await()
    }

    @ExperimentalStdlibApi
    @Test
    fun testGetCharacteristicValue() = runBlocking {
        setupPermissions(this)
        setupBluetooth(coroutineScope = this)
        val deviceHolder = createDeviceHolder()
        val device = createDevice(deviceHolder, coroutineScope = this)
        val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
        val service = createService(connectionManager.stateRepo)
        val characteristic = service.characteristics.first()
        val newValue = "Test".encodeToByteArray()

        val flowTest = FlowTest(bluetooth.devices()[device.identifier].services()[service.uuid].characteristics()[characteristic.uuid].value(), this)
        flowTest.testWithFlow {
            action {
                val isScanning = EmptyCompletableDeferred()
                this@runBlocking.launch {
                    scanDevice(device, deviceHolder)
                    isScanning.complete()
                }
                val startScanning = EmptyCompletableDeferred()
                this@runBlocking.launch {
                    bluetooth.startScanning()
                    startScanning.complete()
                }
                isScanning.await()
                startScanning.await()
                connectDevice(device, connectionManager, this@runBlocking)
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
        setupPermissions(this)
        setupBluetooth(coroutineScope = this)
        val deviceHolder = createDeviceHolder()
        val device = createDevice(deviceHolder, coroutineScope = this)
        val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
        val service = createService(connectionManager.stateRepo)
        val characteristic = service.characteristics.first()

        launch {
            scanDevice(device, deviceHolder)
        }
        bluetooth.startScanning()

        val flowTest = FlowTest(bluetooth.devices()[device.identifier].services()[service.uuid].characteristics()[characteristic.uuid].descriptors(), this)
        flowTest.testWithFlow {
            test {
                assertEquals(emptyList(), it)
            }
            action {
                connectDevice(device, connectionManager, this@runBlocking)
                connectionManager.discoverServicesCompleted.await()
                discoverService(service, device)
            }
            test {
                assertEquals(characteristic.descriptors, it)
            }
        }
    }

    @Test
    fun testGetDescriptor() = runBlocking {
        setupPermissions(this)
        setupBluetooth(coroutineScope = this)
        val deviceHolder = createDeviceHolder()
        val device = createDevice(deviceHolder, coroutineScope = this)
        val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
        val service = createService(connectionManager.stateRepo)
        val characteristic = service.characteristics.first()
        val descriptor = characteristic.descriptors.first()

        launch {
            scanDevice(device, deviceHolder)
        }
        bluetooth.startScanning()

        val flowTest = FlowTest(bluetooth.devices()[device.identifier].services()[service.uuid].characteristics()[characteristic.uuid].descriptors()[descriptor.uuid], this)
        flowTest.testWithFlow {
            test {
                assertNull(it)
            }
            action {
                connectDevice(device, connectionManager, this@runBlocking)
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
        setupPermissions(this)
        setupBluetooth(coroutineScope = this)
        val deviceHolder = createDeviceHolder()
        val device = createDevice(deviceHolder, coroutineScope = this)
        val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
        val service = createService(connectionManager.stateRepo)
        val characteristic = service.characteristics.first()
        val descriptor = characteristic.descriptors.first()
        val newValue = "Test".encodeToByteArray()

        val flowTest = FlowTest(bluetooth.devices()[device.identifier].services()[service.uuid].characteristics()[characteristic.uuid].descriptors()[descriptor.uuid].value(), this)
        flowTest.testWithFlow {
            action {
                val isScanning = EmptyCompletableDeferred()
                this@runBlocking.launch {
                    scanDevice(device, deviceHolder)
                    isScanning.complete()
                }
                val startScanning = EmptyCompletableDeferred()
                this@runBlocking.launch {
                    bluetooth.startScanning()
                    startScanning.complete()
                }
                isScanning.await()
                startScanning.await()
                connectDevice(device, connectionManager, this@runBlocking)
                discoverService(service, device)
                descriptor.set(newValue)
            }
            val foundByte = CompletableDeferred<ByteArray>()
            awaitByte(this, foundByte)
            assertEquals(newValue, foundByte.await())
        }
    }

    private fun setupPermissions(coroutineScope: CoroutineScope) {
        permissionsBuilder = MockPermissionsBuilder()
        permissions = Permissions(permissionsBuilder, coroutineScope.coroutineContext)
        permissions[Permission.Bluetooth]
    }

    private suspend fun setupBluetooth(autoRequestPermission: Boolean = true, autoEnableBluetooth: Boolean = true, isEnabled: Boolean = true, permissionState: PermissionState<Permission.Bluetooth> = PermissionState.Allowed(permissionManager), coroutineScope: CoroutineScope) {
        val deferredBaseScanner = CompletableDeferred<MockBaseScanner>()
        val scannerBuilder = object : BaseScanner.Builder {
            override fun create(
                permissions: Permissions,
                connectionSettings: ConnectionSettings,
                autoRequestPermission: Boolean,
                autoEnableBluetooth: Boolean,
                scanningStateRepo: StateRepo<ScanningState>,
                coroutineScope: CoroutineScope
            ): BaseScanner {
                val scanner = MockBaseScanner(permissions, connectionSettings, autoRequestPermission, autoEnableBluetooth, scanningStateRepo, coroutineScope)
                deferredBaseScanner.complete(scanner)
                return scanner
            }
        }

        bluetooth = Bluetooth(permissions, ConnectionSettings(), autoRequestPermission, autoEnableBluetooth, scannerBuilder, coroutineScope)
        mockBaseScanner = deferredBaseScanner.await()
        mockBaseScanner.isEnabled = isEnabled
        permissionManager.currentState = permissionState
    }

    private suspend fun scanDevice(device: Device, deviceHolder: DeviceHolder, rssi: Int = initialRssi, advertisementData: BaseAdvertisementData = initialAdvertisementData, scanCompleted: EmptyCompletableDeferred? = null) {
        bluetooth.scanningStateRepo.flow().filter {
            it is ScanningState.Enabled.Scanning
        }.first()
        bluetooth.scanningStateRepo.takeAndChangeState { state ->
            when (state) {
                is ScanningState.Enabled.Scanning -> {
                    state.discoverDevice(deviceHolder.identifier, rssi, advertisementData) { device }
                }
                else -> {
                    state.remain
                }
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

    private suspend fun connectDevice(device: Device, connectionManager: MockDeviceConnectionManager, coroutineScope: CoroutineScope) {
        connectionManager.reset()
        val connectingJob = coroutineScope.async {
            bluetooth.devices()[device.identifier].connect()
        }
        connectionManager.connectCompleted.await()
        device.deviceConnectionManager.handleConnect()
        connectingJob.await()
    }

    private suspend fun disconnectDevice(device: Device, connectionManager: MockDeviceConnectionManager, coroutineScope: CoroutineScope) {
        connectionManager.reset()
        val disconnectingJob = coroutineScope.async {
            bluetooth.devices()[device.identifier].disconnect()
        }
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

    private fun createDevice(deviceHolder: DeviceHolder, rssi: Int = initialRssi, advertisementData: BaseAdvertisementData = initialAdvertisementData, coroutineScope: CoroutineScope): Device {
        return Device(ConnectionSettings(), DeviceInfoImpl(deviceHolder, rssi, advertisementData), object : BaseDeviceConnectionManager.Builder {

            override fun create(connectionSettings: ConnectionSettings, deviceHolder: DeviceHolder, stateRepo: StateRepo<DeviceState>, coroutineScope: CoroutineScope): BaseDeviceConnectionManager {
                return MockDeviceConnectionManager(connectionSettings, deviceHolder, stateRepo, coroutineScope)
            }
        }, coroutineScope)
    }
}
