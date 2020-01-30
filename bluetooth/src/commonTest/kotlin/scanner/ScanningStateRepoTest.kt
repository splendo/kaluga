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
import com.splendo.kaluga.bluetooth.device.*
import com.splendo.kaluga.bluetooth.device.MockDeviceConnectionManager
import com.splendo.kaluga.permissions.Permit
import com.splendo.kaluga.permissions.Support
import com.splendo.kaluga.state.StateRepo
import com.splendo.kaluga.test.FlowableTest
import com.splendo.kaluga.utils.EmptyCompletableDeferred
import com.splendo.kaluga.utils.complete
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlin.test.*

abstract class ScanningStateRepoTest  : FlowableTest<ScanningState>() {

    lateinit var permissionManager: MockPermissionManager

    lateinit var mockBaseScanner: MockBaseScanner
    lateinit var scanningStateRepo: ScanningStateRepo

    abstract fun createFilter() : Set<UUID>
    abstract fun createDeviceInfoHolder() : DeviceInfoHolder

    @BeforeTest
    override fun setUp() {
        super.setUp()

        permissionManager = MockPermissionManager()
        val permissions = MockBasePermissions(permissionManager)

        scanningStateRepo = ScanningStateRepo(object: BaseScanner.Builder {

            override val autoEnableBluetooth: Boolean
                get() = true

            override fun create(stateRepo: StateRepo<ScanningState>): BaseScanner {
                mockBaseScanner = MockBaseScanner(permissions, stateRepo)
                return mockBaseScanner
            }
        })
        flowable.complete(scanningStateRepo.flowable.value)
    }


    @Test
    fun testStartWithBluetoothEnabled() = runBlocking {
        permissionManager.support = Support.POWER_ON
        permissionManager.permit = Permit.ALLOWED
        val stopMonitoring = EmptyCompletableDeferred()
        mockBaseScanner.stopMonitoringBluetoothCompleted.invokeOnCompletion {
            stopMonitoring.complete()
        }
        testWithFlow { flowTest ->
            flowTest.test {
                assertTrue(it is ScanningState.Enabled.Idle)
                assertTrue(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
            }
        }
        stopMonitoring.await()
    }

    @Test
    fun testStartWithoutPermissions() {
        permissionManager.support = Support.POWER_ON
        permissionManager.permit = Permit.DENIED
        testWithFlow { flowTest ->
            flowTest.test {
                assertTrue(it is ScanningState.NoBluetoothState.MissingPermissions)
                assertFalse(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
            }
        }
    }

    @Test
    fun testStartWithBluetoothDisabled() = runBlocking {
        permissionManager.support = Support.POWER_OFF
        val stopMonitoring = EmptyCompletableDeferred()
        mockBaseScanner.stopMonitoringBluetoothCompleted.invokeOnCompletion { flowTest ->
            stopMonitoring.complete()
        }
        testWithFlow {
            flowTest.test {
                assertTrue(it is ScanningState.NoBluetoothState.Disabled)
                assertTrue(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
            }
        }
        stopMonitoring.await()
    }

    @Test
    fun testScanning() {
        permissionManager.support = Support.POWER_ON
        permissionManager.permit = Permit.ALLOWED
        val filter = createFilter()
        val device = createDevice()
        testWithFlow {flowTest ->
            flowTest.test {
                assertTrue(it is ScanningState.Enabled.Idle)
                assertEquals(emptySet(), it.oldFilter)
                assertEquals(emptyList(), it.discoveredDevices)
            }
            flowTest.action {
                scanningStateRepo.takeAndChangeState {scanningState ->
                    when(scanningState) {
                        is ScanningState.Enabled.Idle -> scanningState.startScanning(filter)
                        else -> scanningState.remain
                    }
                }
            }
            flowTest.test {
                assertTrue(it is ScanningState.Enabled.Scanning)
                assertEquals(filter, it.filter)
                assertEquals(emptyList(), it.discoveredDevices)
                assertEquals(filter, mockBaseScanner.scanForDevicesCompleted.getCompleted())
                assertFalse(mockBaseScanner.stopScanningCompleted.isCompleted)
            }
            flowTest.action {
                scanningStateRepo.takeAndChangeState {scanningState ->
                    when(scanningState) {
                        is ScanningState.Enabled.Scanning -> scanningState.discoverDevices(device)
                        else -> scanningState.remain
                    }
                }
            }
            flowTest.test {
                assertTrue(it is ScanningState.Enabled.Scanning)
                assertEquals(filter, it.filter)
                assertEquals(listOf(device), it.discoveredDevices)
                assertEquals(filter, mockBaseScanner.scanForDevicesCompleted.getCompleted())
                assertFalse(mockBaseScanner.stopScanningCompleted.isCompleted)
            }
            flowTest.action {
                scanningStateRepo.takeAndChangeState {scanningState ->
                    when(scanningState) {
                        is ScanningState.Enabled.Scanning -> scanningState.stopScanning
                        else -> scanningState.remain
                    }
                }
            }
            flowTest.test {
                assertTrue(it is ScanningState.Enabled.Idle)
                assertEquals(filter, it.oldFilter)
                assertTrue(mockBaseScanner.stopScanningCompleted.isCompleted)
                assertEquals(listOf(device), it.discoveredDevices)
            }
        }
        testWithFlow {
            flowTest.test {
                assertTrue(it is ScanningState.Enabled.Idle)
                assertEquals(filter, it.oldFilter)
                assertEquals(listOf(device), it.discoveredDevices)
            }
        }
    }

    @Test
    fun testBluetoothDisabledWhileIdle() {
        permissionManager.support = Support.POWER_ON
        permissionManager.permit = Permit.ALLOWED
        testWithFlow { flowTest ->
            flowTest.test {
                assertTrue(it is ScanningState.Enabled.Idle)
                assertTrue(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
            }
            flowTest.action {
                permissionManager.support = Support.POWER_OFF
                scanningStateRepo.takeAndChangeState {scanningState ->
                    when(scanningState) {
                        is ScanningState.Enabled.Idle -> scanningState.disable
                        else -> scanningState.remain
                    }
                }
            }
            flowTest.test {
                assertTrue(it is ScanningState.NoBluetoothState.Disabled)
            }
        }
    }

    @Test
    fun testBluetoothDisabledWhileScanning() {
        permissionManager.support = Support.POWER_ON
        permissionManager.permit = Permit.ALLOWED
        testWithFlow { flowTest ->
            flowTest.test {
                assertTrue(it is ScanningState.Enabled.Idle)
                assertTrue(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
            }
            flowTest.action {
                scanningStateRepo.takeAndChangeState {scanningState ->
                    when(scanningState) {
                        is ScanningState.Enabled.Idle -> scanningState.startScanning(createFilter())
                        else -> scanningState.remain
                    }
                }
            }
            flowTest.test {
                assertTrue(it is ScanningState.Enabled.Scanning)
            }
            flowTest.action {
                permissionManager.support = Support.POWER_OFF
                scanningStateRepo.takeAndChangeState {scanningState ->
                    when(scanningState) {
                        is ScanningState.Enabled -> scanningState.disable
                        else -> scanningState.remain
                    }
                }
            }
            flowTest.test {
                assertTrue(mockBaseScanner.stopScanningCompleted.isCompleted)
                assertTrue(it is ScanningState.NoBluetoothState.Disabled)
            }
        }
    }

    @Test
    fun testBluetoothEnabled() {
        permissionManager.support = Support.POWER_OFF
        permissionManager.permit = Permit.ALLOWED
        testWithFlow { flowTest ->
            flowTest.test {
                assertTrue(it is ScanningState.NoBluetoothState.Disabled)
                assertTrue(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
            }
            flowTest.action {
                permissionManager.support = Support.POWER_ON
                scanningStateRepo.takeAndChangeState {scanningState ->
                    when(scanningState) {
                        is ScanningState.NoBluetoothState.Disabled -> scanningState.enable
                        else -> scanningState.remain
                    }
                }
            }

            flowTest.test {
                assertTrue(it is ScanningState.Enabled.Idle)
                assertEquals(emptySet(), it.oldFilter)
                assertEquals(emptyList(), it.discoveredDevices)
            }
        }
    }

    @Test
    fun testPermissionsGranted() {
        permissionManager.support = Support.POWER_ON
        permissionManager.permit = Permit.DENIED
        testWithFlow { flowTest ->
            flowTest.test {
                assertTrue(it is ScanningState.NoBluetoothState.MissingPermissions)
                assertFalse(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
            }
            flowTest.action {
                permissionManager.permit = Permit.ALLOWED
                scanningStateRepo.takeAndChangeState {scanningState ->
                    when(scanningState) {
                        is ScanningState.NoBluetoothState.MissingPermissions -> scanningState.givePermissions
                        else -> scanningState.remain
                    }
                }
            }

            flowTest.test {
                assertTrue(it is ScanningState.Enabled.Idle)
                assertEquals(emptySet(), it.oldFilter)
                assertEquals(emptyList(), it.discoveredDevices)
                assertTrue(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
            }
        }
    }

    @Test
    fun testPermissionsRevoked() {
        permissionManager.support = Support.POWER_ON
        permissionManager.permit = Permit.ALLOWED
        testWithFlow { flowTest ->
            flowTest.test {
                assertTrue(it is ScanningState.Enabled.Idle)
                assertTrue(mockBaseScanner.startMonitoringBluetoothCompleted.isCompleted)
            }
            flowTest.action {
                permissionManager.permit = Permit.DENIED
                scanningStateRepo.takeAndChangeState {scanningState ->
                    when(scanningState) {
                        is ScanningState.Enabled -> scanningState.removePermissions
                        else -> scanningState.remain
                    }
                }
            }

            flowTest.test {
                assertTrue(it is ScanningState.NoBluetoothState.MissingPermissions)
                assertTrue(mockBaseScanner.stopMonitoringBluetoothCompleted.isCompleted)
            }
        }
    }

    private fun createDevice(): Device {
        return Device(ConnectionSettings(), createDeviceInfoHolder(), DeviceTest.initialRssi, object : BaseDeviceConnectionManager.Builder {
            override fun create(connectionSettings: ConnectionSettings, deviceInfo: DeviceInfoHolder, stateRepo: StateRepo<DeviceState>): BaseDeviceConnectionManager {
                return MockDeviceConnectionManager(connectionSettings, deviceInfo, stateRepo)
            }
        })
    }


}
