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
// package com.splendo.kaluga.bluetooth.scanner
//
// import com.splendo.kaluga.base.runBlocking
// import com.splendo.kaluga.bluetooth.UUID
// import com.splendo.kaluga.bluetooth.device.*
// import com.splendo.kaluga.bluetooth.device.MockDeviceConnectionManager
// import com.splendo.kaluga.permissions.Permission
// import com.splendo.kaluga.permissions.PermissionState
// import com.splendo.kaluga.permissions.Permissions
// import com.splendo.kaluga.state.StateRepo
// import com.splendo.kaluga.test.FlowableTest
// import com.splendo.kaluga.test.permissions.MockPermissionsBuilder
// import com.splendo.kaluga.utils.EmptyCompletableDeferred
// import com.splendo.kaluga.utils.complete
// import kotlin.test.*
//
// abstract class ScanningStateRepoTest  : FlowableTest<ScanningState>() {
//
//     private val permissionsBuilder = MockPermissionsBuilder()
//     private val permissions = Permissions(permissionsBuilder)
//     private val permissionManager: com.splendo.kaluga.test.permissions.MockPermissionManager<Permission.Bluetooth>
//         get() {
//             return permissionsBuilder.bluetoothPMManager
//         }
//
//     lateinit var mockBaseScanner: MockBaseScanner
//     lateinit var scanningStateRepo: ScanningStateRepo
//
//     abstract fun createFilter() : Set<UUID>
//     abstract fun createDeviceInfo() : DeviceInfoImpl
//
//     @BeforeTest
//     override fun setUp() {
//         super.setUp()
//
//         scanningStateRepo = ScanningStateRepo(permissions, ConnectionSettings(), true, true, object: BaseScanner.Builder {
//
//             override fun create(
//                 permissions: Permissions,
//                 connectionSettings: ConnectionSettings,
//                 autoRequestPermission: Boolean,
//                 autoEnableBluetooth: Boolean,
//                 scanningStateRepo: StateRepo<ScanningState>
//             ): BaseScanner {
//                 mockBaseScanner = MockBaseScanner(permissions, connectionSettings, autoRequestPermission, autoEnableBluetooth, scanningStateRepo)
//                 return mockBaseScanner
//             }
//         })
//         flowable.complete(scanningStateRepo.flowable.value)
//         mockBaseScanner.isEnabled = true
//         permissionManager.currentState = PermissionState.Allowed(permissionManager)
//     }
//
//
//     @Test
//     fun testStartWithBluetoothEnabled() = runBlocking {
//         val stopMonitoring = EmptyCompletableDeferred()
//         mockBaseScanner.stopMonitoringBluetoothCompleted.invokeOnCompletion {
//             stopMonitoring.complete()
//         }
//         testWithFlow {
//             test {
//                 assertTrue(it is ScanningState.Enabled.Idle)
//                 assertTrue(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
//             }
//         }
//         stopMonitoring.await()
//     }
//
//     @Test
//     fun testStartWithoutPermissions() {
//         mockBaseScanner.isEnabled = true
//         permissionManager.currentState = PermissionState.Denied.Requestable(permissionManager)
//         testWithFlow {
//             test {
//                 assertTrue(it is ScanningState.NoBluetoothState.MissingPermissions)
//                 assertFalse(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
//             }
//         }
//     }
//
//     @Test
//     fun testStartWithBluetoothDisabled() = runBlocking {
//         mockBaseScanner.isEnabled = false
//         val stopMonitoring = EmptyCompletableDeferred()
//         mockBaseScanner.stopMonitoringBluetoothCompleted.invokeOnCompletion {
//             stopMonitoring.complete()
//         }
//         testWithFlow {
//             test {
//                 assertTrue(it is ScanningState.NoBluetoothState.Disabled)
//                 assertTrue(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
//             }
//         }
//         stopMonitoring.await()
//     }
//
//     @Test
//     fun testScanning() {
//         val filter = createFilter()
//         val deviceInfo = createDeviceInfo()
//         val device = createDevice(deviceInfo)
//         testWithFlow {
//             test {
//                 assertTrue(it is ScanningState.Enabled.Idle)
//                 assertEquals(emptySet(), it.oldFilter)
//                 assertEquals(emptyList(), it.discoveredDevices)
//             }
//             action {
//                 scanningStateRepo.takeAndChangeState {scanningState ->
//                     when(scanningState) {
//                         is ScanningState.Enabled.Idle -> scanningState.startScanning(filter)
//                         else -> scanningState.remain
//                     }
//                 }
//             }
//             test {
//                 assertTrue(it is ScanningState.Enabled.Scanning)
//                 assertEquals(filter, it.filter)
//                 assertEquals(emptyList(), it.discoveredDevices)
//                 assertEquals(filter, mockBaseScanner.scanForDevicesCompleted.getCompleted())
//                 assertFalse(mockBaseScanner.stopScanningCompleted.isCompleted)
//             }
//             action {
//                 scanningStateRepo.takeAndChangeState {scanningState ->
//                     when(scanningState) {
//                         is ScanningState.Enabled.Scanning -> scanningState.discoverDevice(deviceInfo.identifier, deviceInfo.advertisementData) {device}
//                         else -> scanningState.remain
//                     }
//                 }
//             }
//             test {
//                 assertTrue(it is ScanningState.Enabled.Scanning)
//                 assertEquals(filter, it.filter)
//                 assertEquals(listOf(device), it.discoveredDevices)
//                 assertEquals(filter, mockBaseScanner.scanForDevicesCompleted.getCompleted())
//                 assertFalse(mockBaseScanner.stopScanningCompleted.isCompleted)
//             }
//             action {
//                 scanningStateRepo.takeAndChangeState {scanningState ->
//                     when(scanningState) {
//                         is ScanningState.Enabled.Scanning -> scanningState.stopScanning
//                         else -> scanningState.remain
//                     }
//                 }
//             }
//             test {
//                 assertTrue(it is ScanningState.Enabled.Idle)
//                 assertEquals(filter, it.oldFilter)
//                 assertTrue(mockBaseScanner.stopScanningCompleted.isCompleted)
//                 assertEquals(listOf(device), it.discoveredDevices)
//             }
//         }
//         testWithFlow {
//             test {
//                 assertTrue(it is ScanningState.Enabled.Idle)
//                 assertEquals(filter, it.oldFilter)
//                 assertEquals(listOf(device), it.discoveredDevices)
//             }
//         }
//     }
//
//     @Test
//     fun testBluetoothDisabledWhileIdle() {
//         testWithFlow {
//             test {
//                 assertTrue(it is ScanningState.Enabled.Idle)
//                 assertTrue(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
//             }
//             action {
//                 mockBaseScanner.isEnabled = false
//                 scanningStateRepo.takeAndChangeState {scanningState ->
//                     when(scanningState) {
//                         is ScanningState.Enabled.Idle -> scanningState.disable
//                         else -> scanningState.remain
//                     }
//                 }
//             }
//             test {
//                 assertTrue(it is ScanningState.NoBluetoothState.Disabled)
//             }
//         }
//     }
//
//     @Test
//     fun testBluetoothDisabledWhileScanning() {
//         testWithFlow {
//             test {
//                 assertTrue(it is ScanningState.Enabled.Idle)
//                 assertTrue(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
//             }
//             action {
//                 scanningStateRepo.takeAndChangeState {scanningState ->
//                     when(scanningState) {
//                         is ScanningState.Enabled.Idle -> scanningState.startScanning(createFilter())
//                         else -> scanningState.remain
//                     }
//                 }
//             }
//             test {
//                 assertTrue(it is ScanningState.Enabled.Scanning)
//             }
//             action {
//                 mockBaseScanner.isEnabled = false
//                 scanningStateRepo.takeAndChangeState {scanningState ->
//                     when(scanningState) {
//                         is ScanningState.Enabled -> scanningState.disable
//                         else -> scanningState.remain
//                     }
//                 }
//             }
//             test {
//                 assertTrue(mockBaseScanner.stopScanningCompleted.isCompleted)
//                 assertTrue(it is ScanningState.NoBluetoothState.Disabled)
//             }
//         }
//     }
//
//     @Test
//     fun testBluetoothEnabled() {
//         mockBaseScanner.isEnabled = false
//         testWithFlow {
//             test {
//                 assertTrue(it is ScanningState.NoBluetoothState.Disabled)
//                 assertTrue(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
//             }
//             action {
//                 mockBaseScanner.isEnabled = true
//                 scanningStateRepo.takeAndChangeState {scanningState ->
//                     when(scanningState) {
//                         is ScanningState.NoBluetoothState.Disabled -> scanningState.enable
//                         else -> scanningState.remain
//                     }
//                 }
//             }
//
//             test {
//                 assertTrue(it is ScanningState.Enabled.Idle)
//                 assertEquals(emptySet(), it.oldFilter)
//                 assertEquals(emptyList(), it.discoveredDevices)
//             }
//         }
//     }
//
//     @Test
//     fun testPermissionsGranted() {
//         mockBaseScanner.isEnabled = true
//         permissionManager.currentState = PermissionState.Denied.Requestable(permissionManager)
//         testWithFlow {
//             test {
//                 assertTrue(it is ScanningState.NoBluetoothState.MissingPermissions)
//                 assertFalse(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
//             }
//             action {
//                 permissionManager.currentState = PermissionState.Allowed(permissionManager)
//                 scanningStateRepo.takeAndChangeState {scanningState ->
//                     when(scanningState) {
//                         is ScanningState.NoBluetoothState.MissingPermissions -> scanningState.permit(true)
//                         else -> scanningState.remain
//                     }
//                 }
//             }
//
//             test {
//                 assertTrue(it is ScanningState.Enabled.Idle)
//                 assertEquals(emptySet(), it.oldFilter)
//                 assertEquals(emptyList(), it.discoveredDevices)
//                 assertTrue(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
//             }
//         }
//     }
//
//     @Test
//     fun testPermissionsRevoked() {
//         testWithFlow {
//             test {
//                 assertTrue(it is ScanningState.Enabled.Idle)
//                 assertTrue(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
//             }
//             action {
//                 permissionManager.currentState = PermissionState.Denied.Requestable(permissionManager)
//                 scanningStateRepo.takeAndChangeState {scanningState ->
//                     when(scanningState) {
//                         is ScanningState.Enabled -> scanningState.revokePermission
//                         else -> scanningState.remain
//                     }
//                 }
//             }
//
//             test {
//                 assertTrue(it is ScanningState.NoBluetoothState.MissingPermissions)
//                 assertTrue(mockBaseScanner.stopMonitoringBluetoothCompleted.isCompleted)
//             }
//         }
//     }
//
//     private fun createDevice(deviceInfoImpl: DeviceInfoImpl): Device {
//         return Device(ConnectionSettings(), deviceInfoImpl, object : BaseDeviceConnectionManager.Builder {
//
//             override fun create(connectionSettings: ConnectionSettings, deviceHolder: DeviceHolder, stateRepo: StateRepo<DeviceState>): BaseDeviceConnectionManager {
//                 return MockDeviceConnectionManager(connectionSettings, deviceHolder, stateRepo)
//             }
//         })
//     }
//
//
// }
