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

import com.splendo.kaluga.bluetooth.device.BaseAdvertisementData
import com.splendo.kaluga.bluetooth.device.BaseDeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceInfoImpl
import com.splendo.kaluga.bluetooth.device.DeviceStateFlowRepo
import com.splendo.kaluga.bluetooth.device.DeviceWrapper
import com.splendo.kaluga.test.mock.bluetooth.device.MockAdvertisementData
import com.splendo.kaluga.bluetooth.randomUUID
import com.splendo.kaluga.bluetooth.scanner.ScanningState.Initialized.Enabled.Idle
import com.splendo.kaluga.bluetooth.scanner.ScanningState.Initialized.Enabled.Scanning
import com.splendo.kaluga.bluetooth.scanner.ScanningState.Initialized.NoBluetooth.Disabled
import com.splendo.kaluga.bluetooth.scanner.ScanningState.Initialized.NoBluetooth.MissingPermissions
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionState
import com.splendo.kaluga.permissions.PermissionStateRepo
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.test.FlowTestBlock
import com.splendo.kaluga.test.MockPermissionManager
import com.splendo.kaluga.test.SimpleFlowTest
import com.splendo.kaluga.test.mock.bluetooth.createDeviceWrapper
import com.splendo.kaluga.test.mock.bluetooth.device.MockDeviceConnectionManager
import com.splendo.kaluga.test.mock.permissions.MockPermissionsBuilder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ScanningStateRepoTest : SimpleFlowTest<ScanningState>() {

    private lateinit var permissionsBuilder: MockPermissionsBuilder
    private lateinit var permissions: Permissions
    private val permissionManager: MockPermissionManager<Permission.Bluetooth>
        get() {
            return permissionsBuilder.bluetoothPMManager
        }

    lateinit var mockBaseScanner: MockBaseScanner
    lateinit var scanningStateRepo: ScanningStateRepo

    var autoRequestPermission: Boolean = true
    var autoEnableBluetooth: Boolean = true
    var isEnabled: Boolean = true
    var permissionState: PermissionState<Permission.Bluetooth> = PermissionState.Allowed()
    var deviceFilter = setOf(
        randomUUID()
    )

    override val flow: suspend () -> Flow<ScanningState> = {
        setupPermissions()
        setupScanningState()
        scanningStateRepo
    }

    fun testWithScanningStateFlow(
        autoRequestPermission: Boolean = this.autoRequestPermission,
        autoEnableBluetooth:Boolean = this.autoEnableBluetooth,
        permissionState: PermissionState<Permission.Bluetooth> = this.permissionState,
        isEnabled:Boolean = this.isEnabled,
        block: FlowTestBlock<ScanningState, Flow<ScanningState>>
    ) {
        this.autoRequestPermission = autoRequestPermission
        this.autoEnableBluetooth = autoEnableBluetooth
        this.permissionState = permissionState
        this.isEnabled = isEnabled
        testWithFlow(block)
    }

    @Test
    fun testStartWithBluetoothEnabled() = testWithFlow {
        test {
            assertTrue(it is Idle)
            assertEquals(emptySet(), it.discovered.filter)
            assertEquals(emptyList(), it.discovered.devices)
            assertTrue(mockBaseScanner.startMonitoringPermissions.isCompleted)
            assertTrue(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
        }

        mockBaseScanner.stopMonitoringBluetoothCompleted.await()
        mockBaseScanner.stopMonitoringPermissions.await()
        permissionManager.hasStoppedMonitoring.await()
    }

    @Test
    fun testStartWithoutPermissions() = testWithScanningStateFlow(permissionState = PermissionState.Denied.Requestable()) {
        assertFalse(mockBaseScanner.startMonitoringPermissions.isCompleted)
        test {
            assertEquals(PermissionStateRepo.defaultMonitoringInterval, permissionManager.hasStartedMonitoring.getCompleted())
            assertTrue(it is MissingPermissions)
            assertFalse(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
            assertTrue(mockBaseScanner.startMonitoringPermissions.isCompleted)
        }
        action {
            permissionManager.hasRequestedPermission.await()
            permissionManager.setPermissionAllowed()
        }
        test {
            assertTrue(it is Idle)
            assertEquals(emptySet(), it.discovered.filter)
            assertEquals(emptyList(), it.discovered.devices)
            assertTrue(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
        }
        permissionManager.hasStoppedMonitoring.await()
        mockBaseScanner.stopMonitoringPermissions.await()
    }

    @Test
    fun testStartWithoutPermissionNoAutoRequest() = testWithScanningStateFlow(
        autoRequestPermission = false,
        permissionState = PermissionState.Denied.Requestable()
    ) {
        assertFalse(mockBaseScanner.startMonitoringPermissions.isCompleted)
        test {
            assertEquals(PermissionStateRepo.defaultMonitoringInterval, permissionManager.hasStartedMonitoring.getCompleted())
            assertTrue(it is MissingPermissions)
            assertFalse(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
            assertTrue(mockBaseScanner.startMonitoringPermissions.isCompleted)
        }
        permissionManager.hasStoppedMonitoring.await()
        mockBaseScanner.stopMonitoringPermissions.await()
        assertFalse(permissionManager.hasRequestedPermission.isCompleted)
    }

    @Test
    fun testStartWithBluetoothDisabled() = testWithScanningStateFlow(isEnabled = false) {
        test {
            assertTrue(it is Disabled)
            assertTrue(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
            assertTrue(mockBaseScanner.startMonitoringPermissions.isCompleted)
        }
        action {
            mockBaseScanner.requestEnableCompleted.await()
            mockBaseScanner.isEnabled = true
            mockBaseScanner.bluetoothEnabled()
        }
        test {
            assertTrue(it is Idle)
            assertEquals(emptySet(), it.discovered.filter)
            assertEquals(emptyList(), it.discovered.devices)
        }
        mockBaseScanner.stopMonitoringBluetoothCompleted.await()
        mockBaseScanner.stopMonitoringPermissions.await()
        permissionManager.hasStoppedMonitoring.await()
    }

    @Test
    fun testStartWithBluetoothDisabledNoAutoEnable() = testWithScanningStateFlow(autoEnableBluetooth = false, isEnabled = false) {
        test {
            assertTrue(it is Disabled)
            assertTrue(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
            assertTrue(mockBaseScanner.startMonitoringPermissions.isCompleted)
        }
        mockBaseScanner.stopMonitoringBluetoothCompleted.await()
        mockBaseScanner.stopMonitoringPermissions.await()
        permissionManager.hasStoppedMonitoring.await()

        assertFalse(mockBaseScanner.requestEnableCompleted.isCompleted)
    }

    @Test
    fun testScanning() = testWithFlow {
        val deviceWrapper = createDeviceWrapper()
        val advertisementData = MockAdvertisementData()
        val device = createDevice(deviceWrapper, advertisementData)
        test {
            assertTrue(it is Idle)
            assertEquals(emptySet(), it.discovered.filter)
            assertEquals(emptyList(), it.discovered.devices)
        }
        action {
            scanningStateRepo.takeAndChangeState { scanningState ->
                when (scanningState) {
                    is Idle -> scanningState.startScanning(deviceFilter)
                    else -> scanningState.remain()
                }
            }
        }
        test {
            assertTrue(it is Scanning)
            assertEquals(deviceFilter, it.discovered.filter)
            assertEquals(emptyList(), it.discovered.devices)
            assertEquals(deviceFilter, mockBaseScanner.scanForDevicesCompleted.getCompleted())
            assertFalse(mockBaseScanner.stopScanningCompleted.isCompleted)
        }
        action {
            scanningStateRepo.takeAndChangeState { scanningState ->
                when (scanningState) {
                    is Scanning -> scanningState.discoverDevice(deviceWrapper.identifier, 0, advertisementData) { device }
                    else -> scanningState.remain()
                }
            }
        }
        test {
            assertTrue(it is Scanning)
            assertEquals(deviceFilter, it.discovered.filter)
            assertEquals(listOf(device), it.discovered.devices)
            assertEquals(deviceFilter, mockBaseScanner.scanForDevicesCompleted.getCompleted())
            assertFalse(mockBaseScanner.stopScanningCompleted.isCompleted)
        }
        action {
            scanningStateRepo.takeAndChangeState { scanningState ->
                when (scanningState) {
                    is Scanning -> scanningState.stopScanning
                    else -> scanningState.remain()
                }
            }
        }
        test {
            assertTrue(it is Idle)
            assertEquals(deviceFilter, it.discovered.filter)
            assertTrue(mockBaseScanner.stopScanningCompleted.isCompleted)
            assertEquals(listOf(device), it.discovered.devices)
        }

        mockBaseScanner.stopMonitoringBluetoothCompleted.await()
        mockBaseScanner.stopMonitoringPermissions.await()
        permissionManager.hasStoppedMonitoring.await()

        mockBaseScanner.reset()

        test {
            assertTrue(it is Idle)
            assertEquals(deviceFilter, it.discovered.filter)
            assertEquals(listOf(device), it.discovered.devices)
        }

        mockBaseScanner.stopMonitoringBluetoothCompleted.await()
        mockBaseScanner.stopMonitoringPermissions.await()
        permissionManager.hasStoppedMonitoring.await()
    }

    @Test
    fun testRediscoverSameDevice() = testWithFlow {
        val deviceHolder = createDeviceWrapper()
        val initialRssi = -100
        val advertisementData = MockAdvertisementData(name = "Name")
        val device = createDevice(deviceHolder, advertisementData)
        test {
            assertTrue(it is Idle)
            assertEquals(emptySet(), it.discovered.filter)
            assertEquals(emptyList(), it.discovered.devices)
        }
        action {
            scanningStateRepo.takeAndChangeState { scanningState ->
                when (scanningState) {
                    is Idle -> scanningState.startScanning(deviceFilter)
                    else -> scanningState.remain()
                }
            }
        }
        test {
            assertTrue(it is Scanning)
            assertEquals(deviceFilter, it.discovered.filter)
            assertEquals(emptyList(), it.discovered.devices)
            assertEquals(deviceFilter, mockBaseScanner.scanForDevicesCompleted.getCompleted())
            assertFalse(mockBaseScanner.stopScanningCompleted.isCompleted)
        }
        action {
            scanningStateRepo.takeAndChangeState { scanningState ->
                when (scanningState) {
                    is Scanning -> scanningState.discoverDevice(deviceHolder.identifier, initialRssi, advertisementData) { device }
                    else -> scanningState.remain()
                }
            }
        }
        test {
            assertTrue(it is Scanning)
            assertEquals(advertisementData, it.discovered.devices[0].first().advertisementData)
            scanningStateRepo.takeAndChangeState { scanningState ->
                when (scanningState) {
                    is Scanning -> {
                        val newAdvertisementData = MockAdvertisementData(name = "New Name")
                        val newRssi = -42
                        val newState = scanningState.discoverDevice(deviceHolder.identifier, newRssi, newAdvertisementData) { device }
                        assertEquals(scanningState.remain(), newState)
                        assertEquals(listOf(device), scanningState.discovered.devices)
                        assertEquals(newAdvertisementData, scanningState.discovered.devices[0].first().advertisementData)
                        assertEquals(newRssi, scanningState.discovered.devices[0].first().rssi)
                        newState
                    }
                    else -> scanningState.remain()
                }
            }
        }

        mockBaseScanner.stopMonitoringBluetoothCompleted.await()
        mockBaseScanner.stopMonitoringPermissions.await()
        permissionManager.hasStoppedMonitoring.await()
    }

    @Test
    fun testPermissionsRevokedWhileIdle() = testWithFlow {
        test {
            assertTrue(it is Idle)
            assertTrue(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
        }
        action {
            permissionManager.setPermissionDenied()
        }
        test {
            assertTrue(it is MissingPermissions)
        }

        mockBaseScanner.stopMonitoringBluetoothCompleted.await()
        mockBaseScanner.stopMonitoringPermissions.await()
        permissionManager.hasStoppedMonitoring.await()
    }

    @Test
    fun testBluetoothDisabledWhileIdle() = testWithFlow {
        test {
            assertTrue(it is Idle)
            assertTrue(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
        }
        action {
            mockBaseScanner.isEnabled = false
            mockBaseScanner.bluetoothDisabled()
        }
        test {
            assertTrue(it is Disabled)
        }

        mockBaseScanner.stopMonitoringBluetoothCompleted.await()
        mockBaseScanner.stopMonitoringPermissions.await()
        permissionManager.hasStoppedMonitoring.await()
    }

    @Test
    fun testPermissionRevokedWhileScanning() = testWithFlow {
        test {
            assertTrue(it is Idle)
            assertTrue(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
        }
        action {
            scanningStateRepo.takeAndChangeState { scanningState ->
                when (scanningState) {
                    is Idle -> scanningState.startScanning(deviceFilter)
                    else -> scanningState.remain()
                }
            }
        }
        test {
            assertTrue(it is Scanning)
        }
        action {
            permissionManager.setPermissionDenied()
        }
        test {
            assertTrue(mockBaseScanner.stopScanningCompleted.isCompleted)
            assertTrue(it is MissingPermissions)
        }

        mockBaseScanner.stopMonitoringBluetoothCompleted.await()
        mockBaseScanner.stopMonitoringPermissions.await()
        permissionManager.hasStoppedMonitoring.await()
    }

    @Test
    fun testBluetoothDisabledWhileScanning() = testWithFlow {
        test {
            assertTrue(it is Idle)
            assertTrue(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
        }
        action {
            scanningStateRepo.takeAndChangeState { scanningState ->
                when (scanningState) {
                    is Idle -> scanningState.startScanning(deviceFilter)
                    else -> scanningState.remain()
                }
            }
        }
        test {
            assertTrue(it is Scanning)
        }
        action {
            mockBaseScanner.isEnabled = false
            mockBaseScanner.bluetoothDisabled()
        }
        test {
            assertTrue(mockBaseScanner.stopScanningCompleted.isCompleted)
            assertTrue(it is Disabled)
        }

        mockBaseScanner.stopMonitoringBluetoothCompleted.await()
        mockBaseScanner.stopMonitoringPermissions.await()
        permissionManager.hasStoppedMonitoring.await()
    }

    private fun setupPermissions() {
        permissionsBuilder = MockPermissionsBuilder()
        permissions = Permissions(permissionsBuilder)
        permissions[Permission.Bluetooth]
    }

    private fun setupScanningState() {
        scanningStateRepo = ScanningStateRepo(permissions, ConnectionSettings(), autoRequestPermission, autoEnableBluetooth, object : BaseScanner.Builder {

            override fun create(
                permissions: Permissions,
                connectionSettings: ConnectionSettings,
                autoRequestPermission: Boolean,
                autoEnableBluetooth: Boolean,
                scanningStateRepo: ScanningStateFlowRepo
            ): BaseScanner {
                mockBaseScanner = MockBaseScanner(permissions, connectionSettings, autoRequestPermission, autoEnableBluetooth, scanningStateRepo)
                return mockBaseScanner
            }
        })
        mockBaseScanner.isEnabled = isEnabled
        permissionManager.currentState = permissionState
    }

    private fun createDevice(deviceWrapper: DeviceWrapper, advertisementData: BaseAdvertisementData): Device {
        return Device(
            ConnectionSettings(),
            DeviceInfoImpl(deviceWrapper, 0, advertisementData),
            object : BaseDeviceConnectionManager.Builder {

                override fun create(
                    connectionSettings: ConnectionSettings,
                    deviceWrapper: DeviceWrapper,
                    stateRepo: DeviceStateFlowRepo,
                ): BaseDeviceConnectionManager {
                    return MockDeviceConnectionManager(
                        connectionSettings,
                        deviceWrapper,
                        stateRepo
                    )
                }
            },
        Dispatchers.Main.immediate
        )
    }

}
