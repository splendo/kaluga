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

package com.splendo.kaluga.bluetooth.scanner

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.BaseAdvertisementData
import com.splendo.kaluga.bluetooth.device.BaseDeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceHolder
import com.splendo.kaluga.bluetooth.device.DeviceInfoImpl
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.bluetooth.device.MockAdvertisementData
import com.splendo.kaluga.bluetooth.device.MockDeviceConnectionManager
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionState
import com.splendo.kaluga.permissions.PermissionStateRepo
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.state.StateRepo
import com.splendo.kaluga.test.FlowTest
import com.splendo.kaluga.test.FlowableTest
import com.splendo.kaluga.test.MockPermissionManager
import com.splendo.kaluga.test.permissions.MockPermissionsBuilder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first

abstract class ScanningStateRepoTest : FlowableTest<ScanningState>() {

    private lateinit var permissionsBuilder: MockPermissionsBuilder
    private lateinit var permissions: Permissions
    private val permissionManager: MockPermissionManager<Permission.Bluetooth>
        get() {
            return permissionsBuilder.bluetoothPMManager
        }

    lateinit var mockBaseScanner: MockBaseScanner
    lateinit var scanningStateRepo: ScanningStateRepo

    abstract fun createFilter(): Set<UUID>
    abstract fun createDeviceHolder(): DeviceHolder

    @Test
    fun testStartWithBluetoothEnabled() = runBlocking {
        setupPermissions(this)
        setupScanningState(coroutineScope = this)
        testWithFlow {
            test {
                assertTrue(it is ScanningState.Enabled.Idle)
                assertEquals(emptySet(), it.oldFilter)
                assertEquals(emptyList(), it.discoveredDevices)
                assertTrue(mockBaseScanner.startMonitoringPermissions.isCompleted)
                assertTrue(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
            }
        }
        mockBaseScanner.stopMonitoringBluetoothCompleted.await()
        mockBaseScanner.stopMonitoringPermissions.await()
        permissionManager.hasStoppedMonitoring.await()
    }

    @Test
    fun testStartWithoutPermissions() = runBlocking {
        setupPermissions(this)
        setupScanningState(permissionState = PermissionState.Denied.Requestable(permissionManager), coroutineScope = this)
        testWithFlow {
            assertFalse(mockBaseScanner.startMonitoringPermissions.isCompleted)
            test {
                assertEquals(PermissionStateRepo.defaultMonitoringInterval, permissionManager.hasStartedMonitoring.getCompleted())
                assertTrue(it is ScanningState.NoBluetoothState.MissingPermissions)
                assertFalse(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
                assertTrue(mockBaseScanner.startMonitoringPermissions.isCompleted)
            }
            action {
                permissionManager.hasRequestedPermission.await()
                permissionManager.setPermissionAllowed()
            }
            test {
                assertTrue(it is ScanningState.Enabled.Idle)
                assertEquals(emptySet(), it.oldFilter)
                assertEquals(emptyList(), it.discoveredDevices)
                assertTrue(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
            }
        }
        permissionManager.hasStoppedMonitoring.await()
        mockBaseScanner.stopMonitoringPermissions.await()
    }

    @Test
    fun testStartWithoutPermissionNoAutoRequest() = runBlocking {
        setupPermissions(this)
        setupScanningState(autoRequestPermission = false, permissionState = PermissionState.Denied.Requestable(permissionManager), coroutineScope = this)
        testWithFlow {
            assertFalse(mockBaseScanner.startMonitoringPermissions.isCompleted)
            test {
                assertEquals(PermissionStateRepo.defaultMonitoringInterval, permissionManager.hasStartedMonitoring.getCompleted())
                assertTrue(it is ScanningState.NoBluetoothState.MissingPermissions)
                assertFalse(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
                assertTrue(mockBaseScanner.startMonitoringPermissions.isCompleted)
            }
        }
        permissionManager.hasStoppedMonitoring.await()
        mockBaseScanner.stopMonitoringPermissions.await()
        assertFalse(permissionManager.hasRequestedPermission.isCompleted)
    }

    @Test
    fun testStartWithBluetoothDisabled() = runBlocking {
        setupPermissions(this)
        setupScanningState(isEnabled = false, coroutineScope = this)
        testWithFlow {
            test {
                assertTrue(it is ScanningState.NoBluetoothState.Disabled)
                assertTrue(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
                assertTrue(mockBaseScanner.startMonitoringPermissions.isCompleted)
            }
            action {
                mockBaseScanner.requestEnableCompleted.await()
                mockBaseScanner.isEnabled = true
                mockBaseScanner.bluetoothEnabled()
            }
            test {
                assertTrue(it is ScanningState.Enabled.Idle)
                assertEquals(emptySet(), it.oldFilter)
                assertEquals(emptyList(), it.discoveredDevices)
            }
        }
        mockBaseScanner.stopMonitoringBluetoothCompleted.await()
        mockBaseScanner.stopMonitoringPermissions.await()
        permissionManager.hasStoppedMonitoring.await()
    }

    @Test
    fun testStartWithBluetoothDisabledNoAutoEnable() = runBlocking {
        setupPermissions(this)
        setupScanningState(autoEnableBluetooth = false, isEnabled = false, coroutineScope = this)
        testWithFlow {
            test {
                assertTrue(it is ScanningState.NoBluetoothState.Disabled)
                assertTrue(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
                assertTrue(mockBaseScanner.startMonitoringPermissions.isCompleted)
            }
        }
        mockBaseScanner.stopMonitoringBluetoothCompleted.await()
        mockBaseScanner.stopMonitoringPermissions.await()
        permissionManager.hasStoppedMonitoring.await()

        assertFalse(mockBaseScanner.requestEnableCompleted.isCompleted)
    }

    @Test
    fun testScanning() = runBlocking {
        setupPermissions(this)
        setupScanningState(coroutineScope = this)
        val filter = createFilter()
        val deviceHolder = createDeviceHolder()
        val advertisementData = MockAdvertisementData()
        val device = createDevice(deviceHolder, advertisementData, this)
        FlowTest(scanningStateRepo.flowable).testWithFlow {
            test {
                assertTrue(it is ScanningState.Enabled.Idle)
                assertEquals(emptySet(), it.oldFilter)
                assertEquals(emptyList(), it.discoveredDevices)
            }
            action {
                scanningStateRepo.takeAndChangeState { scanningState ->
                    when (scanningState) {
                        is ScanningState.Enabled.Idle -> scanningState.startScanning(filter)
                        else -> scanningState.remain
                    }
                }
            }
            test {
                assertTrue(it is ScanningState.Enabled.Scanning)
                assertEquals(filter, it.filter)
                assertEquals(emptyList(), it.discoveredDevices)
                assertEquals(filter, mockBaseScanner.scanForDevicesCompleted.getCompleted())
                assertFalse(mockBaseScanner.stopScanningCompleted.isCompleted)
            }
            action {
                scanningStateRepo.takeAndChangeState { scanningState ->
                    when (scanningState) {
                        is ScanningState.Enabled.Scanning -> scanningState.discoverDevice(deviceHolder.identifier, 0, advertisementData) { device }
                        else -> scanningState.remain
                    }
                }
            }
            test {
                assertTrue(it is ScanningState.Enabled.Scanning)
                assertEquals(filter, it.filter)
                assertEquals(listOf(device), it.discoveredDevices)
                assertEquals(filter, mockBaseScanner.scanForDevicesCompleted.getCompleted())
                assertFalse(mockBaseScanner.stopScanningCompleted.isCompleted)
            }
            action {
                scanningStateRepo.takeAndChangeState { scanningState ->
                    when (scanningState) {
                        is ScanningState.Enabled.Scanning -> scanningState.stopScanning
                        else -> scanningState.remain
                    }
                }
            }
            test {
                assertTrue(it is ScanningState.Enabled.Idle)
                assertEquals(filter, it.oldFilter)
                assertTrue(mockBaseScanner.stopScanningCompleted.isCompleted)
                assertEquals(listOf(device), it.discoveredDevices)
            }
        }

        mockBaseScanner.stopMonitoringBluetoothCompleted.await()
        mockBaseScanner.stopMonitoringPermissions.await()
        permissionManager.hasStoppedMonitoring.await()

        mockBaseScanner.reset()

        FlowTest(scanningStateRepo.flowable).testWithFlow {
            test {
                assertTrue(it is ScanningState.Enabled.Idle)
                assertEquals(filter, it.oldFilter)
                assertEquals(listOf(device), it.discoveredDevices)
            }
        }

        mockBaseScanner.stopMonitoringBluetoothCompleted.await()
        mockBaseScanner.stopMonitoringPermissions.await()
        permissionManager.hasStoppedMonitoring.await()
    }

    @Test
    fun testRediscoverSameDevice() = runBlocking {
        setupPermissions(this)
        setupScanningState(coroutineScope = this)
        val filter = createFilter()
        val deviceHolder = createDeviceHolder()
        val initialRssi = -100
        val advertisementData = MockAdvertisementData(name = "Name")
        val device = createDevice(deviceHolder, advertisementData, this)
        testWithFlow {
            test {
                assertTrue(it is ScanningState.Enabled.Idle)
                assertEquals(emptySet(), it.oldFilter)
                assertEquals(emptyList(), it.discoveredDevices)
            }
            action {
                scanningStateRepo.takeAndChangeState { scanningState ->
                    when (scanningState) {
                        is ScanningState.Enabled.Idle -> scanningState.startScanning(filter)
                        else -> scanningState.remain
                    }
                }
            }
            test {
                assertTrue(it is ScanningState.Enabled.Scanning)
                assertEquals(filter, it.filter)
                assertEquals(emptyList(), it.discoveredDevices)
                assertEquals(filter, mockBaseScanner.scanForDevicesCompleted.getCompleted())
                assertFalse(mockBaseScanner.stopScanningCompleted.isCompleted)
            }
            action {
                scanningStateRepo.takeAndChangeState { scanningState ->
                    when (scanningState) {
                        is ScanningState.Enabled.Scanning -> scanningState.discoverDevice(deviceHolder.identifier, initialRssi, advertisementData) { device }
                        else -> scanningState.remain
                    }
                }
            }
            test {
                assertTrue(it is ScanningState.Enabled.Scanning)
                assertEquals(advertisementData, it.discoveredDevices[0].flow().first().advertisementData)
                scanningStateRepo.takeAndChangeState { scanningState ->
                    when (scanningState) {
                        is ScanningState.Enabled.Scanning -> {
                            val newAdvertisementData = MockAdvertisementData(name = "New Name")
                            val newRssi = -42
                            val newState = scanningState.discoverDevice(deviceHolder.identifier, newRssi, newAdvertisementData) { device }
                            assertEquals(scanningState.remain, newState)
                            assertEquals(listOf(device), scanningState.discoveredDevices)
                            assertEquals(newAdvertisementData, scanningState.discoveredDevices[0].flow().first().advertisementData)
                            assertEquals(newRssi, scanningState.discoveredDevices[0].flow().first().rssi)
                            newState
                        }
                        else -> scanningState.remain
                    }
                }
            }
        }

        mockBaseScanner.stopMonitoringBluetoothCompleted.await()
        mockBaseScanner.stopMonitoringPermissions.await()
        permissionManager.hasStoppedMonitoring.await()
    }

    @Test
    fun testPermissionsRevokedWhileIdle() = runBlocking {
        setupPermissions(this)
        setupScanningState(coroutineScope = this)
        testWithFlow {
            test {
                assertTrue(it is ScanningState.Enabled.Idle)
                assertTrue(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
            }
            action {
                permissionManager.setPermissionDenied()
            }
            test {
                assertTrue(it is ScanningState.NoBluetoothState.MissingPermissions)
            }
        }

        mockBaseScanner.stopMonitoringBluetoothCompleted.await()
        mockBaseScanner.stopMonitoringPermissions.await()
        permissionManager.hasStoppedMonitoring.await()
    }

    @Test
    fun testBluetoothDisabledWhileIdle() = runBlocking {
        setupPermissions(this)
        setupScanningState(coroutineScope = this)
        testWithFlow {
            test {
                assertTrue(it is ScanningState.Enabled.Idle)
                assertTrue(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
            }
            action {
                mockBaseScanner.isEnabled = false
                mockBaseScanner.bluetoothDisabled()
            }
            test {
                assertTrue(it is ScanningState.NoBluetoothState.Disabled)
            }
        }

        mockBaseScanner.stopMonitoringBluetoothCompleted.await()
        mockBaseScanner.stopMonitoringPermissions.await()
        permissionManager.hasStoppedMonitoring.await()
    }

    @Test
    fun testPermissionRevokedWhileScanning() = runBlocking {
        setupPermissions(this)
        setupScanningState(coroutineScope = this)
        testWithFlow {
            test {
                assertTrue(it is ScanningState.Enabled.Idle)
                assertTrue(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
            }
            action {
                scanningStateRepo.takeAndChangeState { scanningState ->
                    when (scanningState) {
                        is ScanningState.Enabled.Idle -> scanningState.startScanning(createFilter())
                        else -> scanningState.remain
                    }
                }
            }
            test {
                assertTrue(it is ScanningState.Enabled.Scanning)
            }
            action {
                permissionManager.setPermissionDenied()
            }
            test {
                assertTrue(mockBaseScanner.stopScanningCompleted.isCompleted)
                assertTrue(it is ScanningState.NoBluetoothState.MissingPermissions)
            }
        }

        mockBaseScanner.stopMonitoringBluetoothCompleted.await()
        mockBaseScanner.stopMonitoringPermissions.await()
        permissionManager.hasStoppedMonitoring.await()
    }

    @Test
    fun testBluetoothDisabledWhileScanning() = runBlocking {
        setupPermissions(this)
        setupScanningState(coroutineScope = this)
        testWithFlow {
            test {
                assertTrue(it is ScanningState.Enabled.Idle)
                assertTrue(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
            }
            action {
                scanningStateRepo.takeAndChangeState { scanningState ->
                    when (scanningState) {
                        is ScanningState.Enabled.Idle -> scanningState.startScanning(createFilter())
                        else -> scanningState.remain
                    }
                }
            }
            test {
                assertTrue(it is ScanningState.Enabled.Scanning)
            }
            action {
                mockBaseScanner.isEnabled = false
                mockBaseScanner.bluetoothDisabled()
            }
            test {
                assertTrue(mockBaseScanner.stopScanningCompleted.isCompleted)
                assertTrue(it is ScanningState.NoBluetoothState.Disabled)
            }
        }

        mockBaseScanner.stopMonitoringBluetoothCompleted.await()
        mockBaseScanner.stopMonitoringPermissions.await()
        permissionManager.hasStoppedMonitoring.await()
    }

    private fun setupPermissions(coroutineScope: CoroutineScope) {
        permissionsBuilder = MockPermissionsBuilder()
        permissions = Permissions(permissionsBuilder, coroutineScope.coroutineContext)
        permissions[Permission.Bluetooth]
    }

    private fun setupScanningState(autoRequestPermission: Boolean = true, autoEnableBluetooth: Boolean = true, isEnabled: Boolean = true, permissionState: PermissionState<Permission.Bluetooth> = PermissionState.Allowed(permissionManager), coroutineScope: CoroutineScope) {
        scanningStateRepo = ScanningStateRepo(permissions, ConnectionSettings(), autoRequestPermission, autoEnableBluetooth, object : BaseScanner.Builder {

            override fun create(
                permissions: Permissions,
                connectionSettings: ConnectionSettings,
                autoRequestPermission: Boolean,
                autoEnableBluetooth: Boolean,
                scanningStateRepo: StateRepo<ScanningState>,
                coroutineScope: CoroutineScope
            ): BaseScanner {
                mockBaseScanner = MockBaseScanner(permissions, connectionSettings, autoRequestPermission, autoEnableBluetooth, scanningStateRepo, coroutineScope)
                return mockBaseScanner
            }
        }, coroutineScope)
        flowable.complete(scanningStateRepo.flowable)
        mockBaseScanner.isEnabled = isEnabled
        permissionManager.currentState = permissionState
    }

    private fun createDevice(deviceHolder: DeviceHolder, advertisementData: BaseAdvertisementData, coroutineScope: CoroutineScope): Device {
        return Device(ConnectionSettings(), DeviceInfoImpl(deviceHolder, 0, advertisementData), object : BaseDeviceConnectionManager.Builder {

            override fun create(connectionSettings: ConnectionSettings, deviceHolder: DeviceHolder, stateRepo: StateRepo<DeviceState>, coroutineScope: CoroutineScope): BaseDeviceConnectionManager {
                return MockDeviceConnectionManager(connectionSettings, deviceHolder, stateRepo, coroutineScope)
            }
        }, coroutineScope)
    }
}
