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
import com.splendo.kaluga.state.StateRepoAccesor
import com.splendo.kaluga.test.FlowableTest
import com.splendo.kaluga.utils.EmptyCompletableDeferred
import com.splendo.kaluga.utils.complete
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlin.test.*

abstract class ScanningStateRepoTest  : FlowableTest<ScanningState>() {

    lateinit var permissionManager: MockPermissionManager

    lateinit var mockBaseScanner: MockBaseScanner

    abstract fun createFilter() : Set<UUID>
    abstract fun createDeviceInfoHolder() : DeviceInfoHolder

    @BeforeTest
    override fun setUp() {
        super.setUp()

        permissionManager = MockPermissionManager()
        val permissions = MockBasePermissions(permissionManager)

        val scanningStateRepo = ScanningStateRepo(object: BaseScanner.Builder {

            override val autoEnableBluetooth: Boolean
                get() = true

            override fun create(stateRepoAccessor: StateRepoAccesor<ScanningState>, coroutineScope: CoroutineScope): BaseScanner {
                mockBaseScanner = MockBaseScanner(permissions, stateRepoAccessor, coroutineScope)
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
        mockBaseScanner.stopMonitoringBluetooth.invokeOnCompletion {
            stopMonitoring.complete()
        }
        runBlockingWithFlow {
            test {
                assertTrue(it is ScanningState.Enabled.Idle)
                assertTrue(mockBaseScanner.startMonitoringBluetooth.isCompleted)
            }
        }
        stopMonitoring.await()
    }

    @Test
    fun testStartWithoutPermissions() {
        permissionManager.support = Support.POWER_ON
        permissionManager.permit = Permit.DENIED
        runBlockingWithFlow {
            test {
                assertTrue(it is ScanningState.NoBluetoothState.MissingPermissions)
                assertFalse(mockBaseScanner.startMonitoringBluetooth.isCompleted)
            }
        }
    }

    @Test
    fun testStartWithBluetoothDisabled() = runBlocking {
        permissionManager.support = Support.POWER_OFF
        val stopMonitoring = EmptyCompletableDeferred()
        mockBaseScanner.stopMonitoringBluetooth.invokeOnCompletion {
            stopMonitoring.complete()
        }
        runBlockingWithFlow {
            test {
                assertTrue(it is ScanningState.NoBluetoothState.Disabled)
                assertTrue(mockBaseScanner.startMonitoringBluetooth.isCompleted)
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
        runBlockingWithFlow {
            val idleState = CompletableDeferred<ScanningState.Enabled.Idle>()
            test {
                assertTrue(it is ScanningState.Enabled.Idle)
                assertEquals(emptySet(), it.oldFilter)
                assertEquals(emptyList(), it.discoveredDevices)
                idleState.complete(it)
            }
            action {
                idleState.getCompleted().startScanning(filter)
            }
            var scanningState = CompletableDeferred<ScanningState.Enabled.Scanning>()
            test {
                assertTrue(it is ScanningState.Enabled.Scanning)
                assertEquals(filter, it.filter)
                assertEquals(emptyList(), it.discoveredDevices)
                assertEquals(filter, mockBaseScanner.scanForDevicesCompleted.getCompleted())
                assertFalse(mockBaseScanner.stopScanningCompleted.isCompleted)
                scanningState.complete(it)
            }
            action {
                scanningState.getCompleted().discoverDevices(device)
            }
            scanningState = CompletableDeferred()
            test {
                assertTrue(it is ScanningState.Enabled.Scanning)
                assertEquals(filter, it.filter)
                assertEquals(listOf(device), it.discoveredDevices)
                assertEquals(filter, mockBaseScanner.scanForDevicesCompleted.getCompleted())
                assertFalse(mockBaseScanner.stopScanningCompleted.isCompleted)
                scanningState.complete(it)
            }
            action {
                scanningState.getCompleted().stopScanning()
            }
            test {
                assertTrue(it is ScanningState.Enabled.Idle)
                assertEquals(filter, it.oldFilter)
                assertTrue(mockBaseScanner.stopScanningCompleted.isCompleted)
                assertEquals(listOf(device), it.discoveredDevices)
            }
        }
        runBlockingWithFlow {
            test {
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
        runBlockingWithFlow {
            val idleState = CompletableDeferred<ScanningState.Enabled.Idle>()
            test {
                assertTrue(it is ScanningState.Enabled.Idle)
                assertTrue(mockBaseScanner.startMonitoringBluetooth.isCompleted)
                idleState.complete(it)
            }
            action {
                permissionManager.support = Support.POWER_OFF
                idleState.getCompleted().disable()
            }
            test {
                assertTrue(it is ScanningState.NoBluetoothState.Disabled)
            }
        }
    }

    @Test
    fun testBluetoothDisabledWhileScanning() {
        permissionManager.support = Support.POWER_ON
        permissionManager.permit = Permit.ALLOWED
        runBlockingWithFlow {
            val idleState = CompletableDeferred<ScanningState.Enabled.Idle>()
            test {
                assertTrue(it is ScanningState.Enabled.Idle)
                assertTrue(mockBaseScanner.startMonitoringBluetooth.isCompleted)
                idleState.complete(it)
            }
            action {
                idleState.getCompleted().startScanning(createFilter())
            }
            val scanningState = CompletableDeferred<ScanningState.Enabled.Scanning>()
            test {
                assertTrue(it is ScanningState.Enabled.Scanning)
                scanningState.complete(it)
            }
            action {
                permissionManager.support = Support.POWER_OFF
                scanningState.getCompleted().disable()
            }
            test {
                assertTrue(mockBaseScanner.stopScanningCompleted.isCompleted)
                assertTrue(it is ScanningState.NoBluetoothState.Disabled)
            }
        }
    }

    @Test
    fun testBluetoothEnabled() {
        permissionManager.support = Support.POWER_OFF
        permissionManager.permit = Permit.ALLOWED
        runBlockingWithFlow {
            val disabledState = CompletableDeferred<ScanningState.NoBluetoothState.Disabled>()
            test {
                assertTrue(it is ScanningState.NoBluetoothState.Disabled)
                assertTrue(mockBaseScanner.startMonitoringBluetooth.isCompleted)
                disabledState.complete(it)
            }
            action {
                permissionManager.support = Support.POWER_ON
                disabledState.getCompleted().enable()
            }

            test {
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
        runBlockingWithFlow {
            val missingPermissionsState = CompletableDeferred<ScanningState.NoBluetoothState.MissingPermissions>()
            test {
                assertTrue(it is ScanningState.NoBluetoothState.MissingPermissions)
                assertFalse(mockBaseScanner.startMonitoringBluetooth.isCompleted)
                missingPermissionsState.complete(it)
            }
            action {
                permissionManager.permit = Permit.ALLOWED
                missingPermissionsState.getCompleted().givePermissions()
            }

            test {
                assertTrue(it is ScanningState.Enabled.Idle)
                assertEquals(emptySet(), it.oldFilter)
                assertEquals(emptyList(), it.discoveredDevices)
                assertTrue(mockBaseScanner.startMonitoringBluetooth.isCompleted)
            }
        }
    }

    @Test
    fun testPermissionsRevoked() {
        permissionManager.support = Support.POWER_ON
        permissionManager.permit = Permit.ALLOWED
        runBlockingWithFlow {
            val idleState = CompletableDeferred<ScanningState.Enabled.Idle>()
            test {
                assertTrue(it is ScanningState.Enabled.Idle)
                assertTrue(mockBaseScanner.startMonitoringBluetooth.isCompleted)
                idleState.complete(it)
            }
            action {
                permissionManager.permit = Permit.DENIED
                idleState.getCompleted().removePermissions()
            }

            test {
                assertTrue(it is ScanningState.NoBluetoothState.MissingPermissions)
                assertTrue(mockBaseScanner.stopMonitoringBluetooth.isCompleted)
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
