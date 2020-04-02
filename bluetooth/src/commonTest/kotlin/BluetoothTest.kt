// /*
//  Copyright (c) 2020. Splendo Consulting B.V. The Netherlands
//
//     Licensed under the Apache License, Version 2.0 (the "License");
//     you may not use this file except in compliance with the License.
//     You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//     Unless required by applicable law or agreed to in writing, software
//     distributed under the License is distributed on an "AS IS" BASIS,
//     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//     See the License for the specific language governing permissions and
//     limitations under the License.
//
//  */
//
// package com.splendo.kaluga.bluetooth
//
// import com.splendo.kaluga.base.runBlocking
// import com.splendo.kaluga.bluetooth.device.*
// import com.splendo.kaluga.bluetooth.scanner.*
// import com.splendo.kaluga.permissions.Permission
// import com.splendo.kaluga.permissions.PermissionState
// import com.splendo.kaluga.permissions.Permissions
// import com.splendo.kaluga.state.StateRepo
// import com.splendo.kaluga.test.BaseTest
// import com.splendo.kaluga.test.FlowTest
// import com.splendo.kaluga.test.permissions.MockPermissionsBuilder
// import com.splendo.kaluga.utils.EmptyCompletableDeferred
// import com.splendo.kaluga.utils.complete
// import kotlinx.coroutines.CompletableDeferred
// import kotlinx.coroutines.MainScope
// import kotlinx.coroutines.async
// import kotlinx.coroutines.flow.filter
// import kotlinx.coroutines.flow.first
// import kotlinx.coroutines.launch
// import kotlin.test.*
//
// abstract class BluetoothTest : BaseTest() {
//
//     companion object {
//         val initialRssi = -100
//     }
//
//     private val mainScope = MainScope()
//
//     private val permissionsBuilder = MockPermissionsBuilder()
//     private val permissions = Permissions(permissionsBuilder)
//     private val permissionManager: com.splendo.kaluga.test.permissions.MockPermissionManager<Permission.Bluetooth>
//         get() {
//             return permissionsBuilder.bluetoothPMManager
//         }
//     lateinit var mockBaseScanner: MockBaseScanner
//     lateinit var bluetooth: Bluetooth
//
//     lateinit var bluetoothDisabledCalled: EmptyCompletableDeferred
//     lateinit var bluetoothMissingPermissionCalled: EmptyCompletableDeferred
//
//     abstract fun createFilter() : Set<UUID>
//     abstract fun createDeviceInfoImpl() : DeviceInfoImpl
//     abstract fun createService(stateRepo: StateRepo<DeviceState>) : Service
//
//     @BeforeTest
//     open fun setup() = runBlocking {
//         super.beforeTest()
//
//         permissions[Permission.Bluetooth]
//
//         val deferredBaseScanner = CompletableDeferred<MockBaseScanner>()
//         val scannerBuilder = object : BaseScanner.Builder {
//             override fun create(
//                 permissions: Permissions,
//                 connectionSettings: ConnectionSettings,
//                 autoRequestPermission: Boolean,
//                 autoEnableBluetooth: Boolean,
//                 scanningStateRepo: StateRepo<ScanningState>
//             ): BaseScanner {
//                 val scanner = MockBaseScanner(permissions, connectionSettings, autoRequestPermission, autoEnableBluetooth, scanningStateRepo)
//                 deferredBaseScanner.complete(scanner)
//                 return scanner
//             }
//         }
//
//         bluetoothDisabledCalled = EmptyCompletableDeferred()
//         bluetoothMissingPermissionCalled = EmptyCompletableDeferred()
//
//         bluetooth = Bluetooth(permissions, ConnectionSettings(), true, true, scannerBuilder)
//         mockBaseScanner = deferredBaseScanner.await()
//         mockBaseScanner.isEnabled = true
//         permissionManager.currentState = PermissionState.Allowed(permissionManager)
//     }
//
//     @Test
//     fun testScanDevice() = runBlocking {
//         FlowTest(bluetooth.devices()).testWithFlow {
//             test {
//                 assertEquals(emptyList(), it)
//                 assertTrue(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
//             }
//             action {
//                 bluetooth.startScanning()
//             }
//             test {
//                 assertEquals(emptyList(), it)
//                 assertEquals(emptySet(), mockBaseScanner.scanForDevicesCompleted.getCompleted())
//             }
//             val filter = createFilter()
//             action {
//                 mockBaseScanner.reset()
//                 bluetooth.startScanning(filter)
//             }
//             test {
//                 assertEquals(emptyList(), it)
//             }
//             test {
//                 assertEquals(emptyList(), it)
//                 assertTrue(mockBaseScanner.stopScanningCompleted.isCompleted)
//             }
//             test {
//                 assertEquals(emptyList(), it)
//                 assertEquals(filter, mockBaseScanner.scanForDevicesCompleted.getCompleted())
//             }
//             test {
//                 assertEquals(emptyList(), it)
//             }
//             val deviceInfo = createDeviceInfoImpl()
//             val device = createDevice(deviceInfo)
//             val scanCompleted = EmptyCompletableDeferred()
//             action {
//                 mainScope.launch {
//                     scanDevice(device, deviceInfo, scanCompleted)
//                 }
//             }
//             scanCompleted.await()
//             test {
//                 assertEquals(listOf(device), it)
//             }
//             action {
//                 mockBaseScanner.reset()
//                 bluetooth.stopScanning()
//             }
//             test {
//                 assertEquals(listOf(device), it)
//             }
//             test {
//                 assertTrue(mockBaseScanner.stopScanningCompleted.isCompleted)
//                 assertEquals(listOf(device), it)
//             }
//         }
//     }
//
//     @Test
//     fun testBluetoothDisabled() = runBlocking {
//         mockBaseScanner.isEnabled = false
//
//         FlowTest(bluetooth.devices()).testWithFlow {
//             test {
//                 assertEquals(emptyList(), it)
//                 assertTrue(bluetoothDisabledCalled.isCompleted)
//             }
//         }
//     }
//
//     @Test
//     fun testBluetoothMissingPermissions() = runBlocking {
//         mockBaseScanner.isEnabled = true
//         permissionManager.currentState = PermissionState.Denied.Requestable(permissionManager)
//
//         FlowTest(bluetooth.devices()).testWithFlow {
//             test {
//                 assertEquals(emptyList(), it)
//                 assertTrue(bluetoothMissingPermissionCalled.isCompleted)
//             }
//         }
//     }
//
//     @Test
//     fun testGetDevice() = runBlocking {
//         val deviceInfo = createDeviceInfoImpl()
//         val device = createDevice(deviceInfo)
//         FlowTest(bluetooth.devices()[device.identifier]).testWithFlow {
//             test {
//                 assertNull(it)
//             }
//             val scanCompleted = EmptyCompletableDeferred()
//             action {
//                 mainScope.launch {
//                     scanDevice(device, deviceInfo, scanCompleted)
//                 }
//                 bluetooth.startScanning()
//             }
//             scanCompleted.await()
//             val foundDevice = CompletableDeferred<Device>()
//             awaitDevice(this, foundDevice)
//             assertEquals(device, foundDevice.await())
//             action { bluetooth.stopScanning() }
//             test {
//                 assertEquals(device, it)
//             }
//         }
//     }
//
//     @Test
//     fun testConnectDevice() = runBlocking {
//         val deviceInfo = createDeviceInfoImpl()
//         val device = createDevice(deviceInfo)
//         mainScope.launch {
//             scanDevice(device, deviceInfo)
//         }
//         bluetooth.startScanning()
//         connectDevice(device)
//         disconnectDevice(device)
//     }
//
//     @Test
//     fun testRssi() = runBlocking {
//         val deviceInfo = createDeviceInfoImpl()
//         val device = createDevice(deviceInfo)
//         val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
//         val newRssi = -42
//         FlowTest(bluetooth.devices()[device.identifier].rssi()).testWithFlow {
//             action {
//                 mainScope.launch {
//                     scanDevice(device, deviceInfo)
//                 }
//                 bluetooth.startScanning()
//                 connectDevice(device)
//             }
//             test {
//                 assertEquals(initialRssi, it)
//             }
//             action {
//                 bluetooth.devices()[device.identifier].updateRssi()
//                 connectionManager.readRssiCompleted.await()
//                 device.flow().filter { it is DeviceState.Connected }.first()
//                 device.deviceConnectionManager.handleNewRssi(newRssi)
//             }
//             test(2) {
//                 assertEquals(newRssi, it)
//             }
//
//         }
//     }
//
//     @Test
//     fun testGetServices() = runBlocking {
//         val deviceInfo = createDeviceInfoImpl()
//         val device = createDevice(deviceInfo)
//         val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
//         val service = createService(connectionManager.stateRepo)
//         mainScope.launch {
//             scanDevice(device, deviceInfo)
//         }
//         bluetooth.startScanning()
//
//         FlowTest(bluetooth.devices()[device.identifier].services()).testWithFlow {
//             test {
//                 assertEquals(emptyList(), it)
//             }
//             action {
//                 connectDevice(device)
//             }
//             test(1) {
//                 assertEquals(emptyList(), it)
//                 assertTrue(connectionManager.discoverServicesCompleted.isCompleted)
//             }
//             test {
//                 assertEquals(emptyList(), it)
//             }
//             action {
//                 discoverService(service, device)
//             }
//             test {
//                 assertEquals(listOf(service), it)
//             }
//         }
//     }
//
//     @Test
//     fun testGetService() = runBlocking {
//         val deviceInfo = createDeviceInfoImpl()
//         val device = createDevice(deviceInfo)
//         val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
//         val service = createService(connectionManager.stateRepo)
//         mainScope.launch {
//             scanDevice(device, deviceInfo)
//         }
//         bluetooth.startScanning()
//
//         FlowTest(bluetooth.devices()[device.identifier].services()[service.uuid]).testWithFlow {
//             test {
//                 assertNull(it)
//             }
//
//             action {
//                 connectDevice(device)
//                 discoverService(service, device)
//             }
//             val foundService = CompletableDeferred<Service>()
//             awaitService(this, foundService)
//             assertEquals(service, foundService.await())
//         }
//     }
//
//     @Test
//     fun testGetCharacteristics() = runBlocking {
//         val deviceInfo = createDeviceInfoImpl()
//         val device = createDevice(deviceInfo)
//         val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
//         val service = createService(connectionManager.stateRepo)
//         mainScope.launch {
//             scanDevice(device, deviceInfo)
//         }
//         bluetooth.startScanning()
//
//         FlowTest(bluetooth.devices()[device.identifier].services()[service.uuid].characteristics()).testWithFlow {
//             test {
//                 assertEquals(emptyList(), it)
//             }
//             action {
//                 connectDevice(device)
//             }
//             test {
//                 assertEquals(emptyList(), it)
//                 assertTrue(connectionManager.discoverServicesCompleted.isCompleted)
//             }
//             action {
//                 discoverService(service, device)
//             }
//             test(2) {
//                 assertEquals(service.characteristics, it)
//             }
//
//         }
//     }
//
//     @Test
//     fun testGetCharacteristic() = runBlocking {
//         val deviceInfo = createDeviceInfoImpl()
//         val device = createDevice(deviceInfo)
//         val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
//         val service = createService(connectionManager.stateRepo)
//         val characteristic = service.characteristics.first()
//         mainScope.launch {
//             scanDevice(device, deviceInfo)
//         }
//         bluetooth.startScanning()
//
//         FlowTest(bluetooth.devices()[device.identifier].services()[service.uuid].characteristics()[characteristic.uuid]).testWithFlow {
//             test {
//                 assertNull(it)
//             }
//
//             action {
//                 connectDevice(device)
//                 discoverService(service, device)
//             }
//             val foundCharacteristic = CompletableDeferred<Characteristic>()
//             awaitCharacteristic(this, foundCharacteristic)
//             assertEquals(characteristic, foundCharacteristic.await())
//         }
//     }
//
//     @ExperimentalStdlibApi
//     @Test
//     fun testGetCharacteristicValue() = runBlocking {
//         val deviceInfo = createDeviceInfoImpl()
//         val device = createDevice(deviceInfo)
//         val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
//         val service = createService(connectionManager.stateRepo)
//         val characteristic = service.characteristics.first()
//         val newValue = "Test".encodeToByteArray()
//
//         FlowTest(bluetooth.devices()[device.identifier].services()[service.uuid].characteristics()[characteristic.uuid].value()).testWithFlow {
//             action {
//                 mainScope.launch {
//                     scanDevice(device, deviceInfo)
//                 }
//                 bluetooth.startScanning()
//                 connectDevice(device)
//                 discoverService(service, device)
//                 characteristic.set(newValue)
//             }
//             val foundByte = CompletableDeferred<ByteArray>()
//             awaitByte(this, foundByte)
//             assertEquals(newValue, foundByte.await())
//         }
//     }
//
//     @Test
//     fun testGetDescriptors() = runBlocking {
//         val deviceInfo = createDeviceInfoImpl()
//         val device = createDevice(deviceInfo)
//         val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
//         val service = createService(connectionManager.stateRepo)
//         val characteristic = service.characteristics.first()
//
//         FlowTest(bluetooth.devices()[device.identifier].services()[service.uuid].characteristics()[characteristic.uuid].descriptors()).testWithFlow {
//             action {
//                 mainScope.launch {
//                     scanDevice(device, deviceInfo)
//                 }
//                 bluetooth.startScanning()
//                 connectDevice(device)
//             }
//             test(1) {
//                 assertEquals(emptyList(), it)
//             }
//             test {
//                 assertEquals(emptyList(), it)
//                 assertTrue(connectionManager.discoverServicesCompleted.isCompleted)
//             }
//             test {
//                 assertEquals(emptyList(), it)
//             }
//             action {
//                 discoverService(service, device)
//             }
//             test {
//                 assertEquals(characteristic.descriptors, it)
//             }
//
//         }
//     }
//
//     @Test
//     fun testGetDescriptor() = runBlocking {
//         val deviceInfo = createDeviceInfoImpl()
//         val device = createDevice(deviceInfo)
//         val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
//         val service = createService(connectionManager.stateRepo)
//         val characteristic = service.characteristics.first()
//         val descriptor = characteristic.descriptors.first()
//
//         FlowTest(bluetooth.devices()[device.identifier].services()[service.uuid].characteristics()[characteristic.uuid].descriptors()[descriptor.uuid]).testWithFlow {
//             action {
//                 mainScope.launch {
//                     scanDevice(device, deviceInfo)
//                 }
//                 bluetooth.startScanning()
//                 connectDevice(device)
//                 discoverService(service, device)
//             }
//             val foundDescriptor = CompletableDeferred<Descriptor>()
//             awaitDescriptor(this, foundDescriptor)
//             assertEquals(descriptor, foundDescriptor.await())
//         }
//     }
//
//     @ExperimentalStdlibApi
//     @Test
//     fun testGetDescriptorValue() = runBlocking {
//         val deviceInfo = createDeviceInfoImpl()
//         val device = createDevice(deviceInfo)
//         val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
//         val service = createService(connectionManager.stateRepo)
//         val characteristic = service.characteristics.first()
//         val descriptor = characteristic.descriptors.first()
//         val newValue = "Test".encodeToByteArray()
//
//         FlowTest(bluetooth.devices()[device.identifier].services()[service.uuid].characteristics()[characteristic.uuid].descriptors()[descriptor.uuid].value()).testWithFlow {
//             action {
//                 mainScope.launch {
//                     scanDevice(device, deviceInfo)
//                 }
//                 bluetooth.startScanning()
//                 connectDevice(device)
//                 discoverService(service, device)
//                 descriptor.set(newValue)
//             }
//             val foundByte = CompletableDeferred<ByteArray>()
//             awaitByte(this, foundByte)
//             assertEquals(newValue, foundByte.await())
//         }
//     }
//
//     private suspend fun scanDevice(device: Device, deviceInfo: DeviceInfoImpl, scanCompleted: EmptyCompletableDeferred? = null) {
//         bluetooth.scanningStateRepo.flow().filter{it is ScanningState.Enabled.Scanning}.first()
//         bluetooth.scanningStateRepo.takeAndChangeState {state ->
//             when(state) {
//                 is ScanningState.Enabled.Scanning -> state.discoverDevice(deviceInfo.identifier, deviceInfo.advertisementData) { device }
//                 else -> state.remain
//             }
//         }
//         scanCompleted?.complete()
//     }
//
//     private fun awaitDevice(flowTest: FlowTest<Device?>, foundDevice: CompletableDeferred<Device>) {
//         val deviceNotFound = EmptyCompletableDeferred()
//         deviceNotFound.invokeOnCompletion {
//             awaitDevice(flowTest, foundDevice)
//         }
//         flowTest.test {
//             if (it != null) {
//                 foundDevice.complete(it)
//             } else {
//                 deviceNotFound.complete()
//             }
//         }
//     }
//
//     private suspend fun connectDevice(device: Device) {
//         val connectingJob = mainScope.async {
//             bluetooth.devices()[device.identifier].connect()
//         }
//         val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
//         connectionManager.connectCompleted.await()
//         device.deviceConnectionManager.handleConnect()
//         connectingJob.await()
//     }
//
//     private suspend fun disconnectDevice(device: Device) {
//         val disconnectingJob = mainScope.async {
//             bluetooth.devices()[device.identifier].disconnect()
//         }
//         val connectionManager = device.deviceConnectionManager as MockDeviceConnectionManager
//         connectionManager.disconnectCompleted.await()
//         device.deviceConnectionManager.handleDisconnect()
//         disconnectingJob.await()
//     }
//
//     private suspend fun discoverService(service: Service, device: Device) {
//         device.flow().filter { it is DeviceState.Connected.Discovering }.first()
//         device.deviceConnectionManager.handleScanCompleted(listOf(service))
//     }
//
//     private fun awaitService(flowTest: FlowTest<Service?>, foundService: CompletableDeferred<Service>) {
//         val serviceNotFound = EmptyCompletableDeferred()
//         serviceNotFound.invokeOnCompletion {
//             awaitService(flowTest, foundService)
//         }
//         flowTest.test {
//             if (it != null) {
//                 foundService.complete(it)
//             } else {
//                 serviceNotFound.complete()
//             }
//         }
//     }
//
//     private fun awaitCharacteristic(flowTest: FlowTest<Characteristic?>, foundCharacteristic: CompletableDeferred<Characteristic>) {
//         val characteristicNotFound = EmptyCompletableDeferred()
//         characteristicNotFound.invokeOnCompletion {
//             awaitCharacteristic(flowTest, foundCharacteristic)
//         }
//         flowTest.test {
//             if (it != null) {
//                 foundCharacteristic.complete(it)
//             } else {
//                 characteristicNotFound.complete()
//             }
//         }
//     }
//
//     private fun awaitDescriptor(flowTest: FlowTest<Descriptor?>, foundDescriptor: CompletableDeferred<Descriptor>) {
//         val descriptorNotFound = EmptyCompletableDeferred()
//         descriptorNotFound.invokeOnCompletion {
//             awaitDescriptor(flowTest, foundDescriptor)
//         }
//         flowTest.test {
//             if (it != null) {
//                 foundDescriptor.complete(it)
//             } else {
//                 descriptorNotFound.complete()
//             }
//         }
//     }
//
//     private fun awaitByte(flowTest: FlowTest<ByteArray?>, foundByte: CompletableDeferred<ByteArray>) {
//         val byteNotFound = EmptyCompletableDeferred()
//         byteNotFound.invokeOnCompletion {
//             awaitByte(flowTest, foundByte)
//         }
//         flowTest.test {
//             if (it != null) {
//                 foundByte.complete(it)
//             } else {
//                 byteNotFound.complete()
//             }
//         }
//     }
//
//     private fun createDevice(deviceInfo: DeviceInfoImpl): Device {
//         return Device(ConnectionSettings(), deviceInfo, object : BaseDeviceConnectionManager.Builder {
//
//             override fun create(connectionSettings: ConnectionSettings, deviceHolder: DeviceHolder, stateRepo: StateRepo<DeviceState>): BaseDeviceConnectionManager {
//                 return MockDeviceConnectionManager(connectionSettings, deviceHolder, stateRepo)
//             }
//
//         }, mainScope.coroutineContext)
//     }
//
// }
//
