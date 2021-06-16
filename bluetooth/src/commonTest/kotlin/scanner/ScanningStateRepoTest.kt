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

import com.splendo.kaluga.base.flow.filterOnlyImportant
import com.splendo.kaluga.bluetooth.BluetoothFlowTest
import com.splendo.kaluga.bluetooth.BluetoothFlowTest.Setup.DEVICE
import com.splendo.kaluga.test.mock.bluetooth.device.MockAdvertisementData
import com.splendo.kaluga.bluetooth.randomUUID
import com.splendo.kaluga.bluetooth.scanner.ScanningState.Initialized.Enabled.Idle
import com.splendo.kaluga.bluetooth.scanner.ScanningState.Initialized.Enabled.Scanning
import com.splendo.kaluga.bluetooth.scanner.ScanningState.Initialized.NoBluetooth.Disabled
import com.splendo.kaluga.bluetooth.scanner.ScanningState.Initialized.NoBluetooth.MissingPermissions
import com.splendo.kaluga.bluetooth.scanner.ScanningState.NotInitialized
import com.splendo.kaluga.permissions.PermissionState
import com.splendo.kaluga.permissions.PermissionStateRepo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlin.test.fail

class ScanningStateRepoTest : BluetoothFlowTest<ScanningState>() {

    private val deviceFilter = setOf(randomUUID())

    override val filter: (Flow<ScanningState>) -> Flow<ScanningState> = {
        it.distinctUntilChanged(areEquivalent = { old, new -> (old is Idle && new is Idle) || old == new })
    }

    override val flow: suspend () -> Flow<ScanningState> = {
        setup(DEVICE)
        bluetooth.scanningStateRepo.takeAndChangeState(remainIfStateNot = NotInitialized::class) {
            it.initialize(bluetooth.scanningStateRepo)
        }

        bluetooth.scanningStateRepo.filterOnlyImportant()
    }


    @Test
    fun testStartWithBluetoothEnabled() = testWithFlow {
        test {
            assertTrue(it is Idle)
            assertEquals(emptySet(), it.discovered.filter)
            assertEquals(emptyList(), it.discovered.devices)
        }
    }

    @Test
    fun testStartWithoutPermissions() = testWithBluetoothFlow(permissionState = PermissionState.Denied.Requestable()) {
        val hasStartedMonitoring = permissionManager.hasStartedMonitoring
        test {
            assertEquals(PermissionStateRepo.defaultMonitoringInterval, hasStartedMonitoring.getCompleted())
            assertTrue(it is MissingPermissions)
        }
        mockBaseScanner().startMonitoringBluetoothCompleted.get().await()
        assertTrue(mockBaseScanner().startMonitoringPermissionsCompleted.get().isCompleted)

        action {
            permissionManager.hasRequestedPermission.await()
            permissionManager.setPermissionAllowed()
        }
        val startMonitoringBluetoothCompleted = mockBaseScanner().startMonitoringBluetoothCompleted.get()
        test {
            assertTrue(it is Idle)
            assertEquals(emptySet(), it.discovered.filter)
            assertEquals(emptyList(), it.discovered.devices)
            assertTrue(startMonitoringBluetoothCompleted.isCompleted)
        }
    }

    @Test
    fun testStartWithoutPermissionNoAutoRequest() = testWithBluetoothFlow(
        autoRequestPermission = false,
        permissionState = PermissionState.Denied.Requestable()
    ) {
        val hasStartedMonitoring = permissionManager.hasStartedMonitoring
        test {
            assertEquals(PermissionStateRepo.defaultMonitoringInterval, hasStartedMonitoring.getCompleted())
            assertTrue(it is MissingPermissions)
        }
        assertTrue(mockBaseScanner().startMonitoringBluetoothCompleted.get().isCompleted)
        assertTrue(mockBaseScanner().startMonitoringPermissionsCompleted.get().isCompleted)

        assertFalse(permissionManager.hasRequestedPermission.isCompleted)
    }

    @Test
    fun testStartWithBluetoothDisabled() = testWithBluetoothFlow(isEnabled = false) {

        test {
            assertTrue(it is Disabled)
        }

        // TODO:
        // val startMonitoringBluetoothCompleted = mockBaseScanner().startMonitoringBluetoothCompleted.get().await()

        action {
            mockBaseScanner().requestEnableCompleted.get().await()
            mockBaseScanner().isEnabled = true
            mockBaseScanner().bluetoothEnabled()
        }

        test {
            assertTrue(it is Idle)
            assertEquals(emptySet(), it.discovered.filter)
            assertEquals(emptyList(), it.discovered.devices)
        }
    }

    @Test
    fun testStartWithBluetoothDisabledNoAutoEnable() = testWithBluetoothFlow(autoEnableBluetooth = false, isEnabled = false) {
        test {
            assertTrue(it is Disabled)

        }

        mockBaseScanner().startMonitoringPermissionsCompleted.get().await()
        assertFalse(mockBaseScanner().startMonitoringBluetoothCompleted.get().isCompleted)
        assertFalse(mockBaseScanner().requestEnableCompleted.get().isCompleted)
    }

    @Test
    fun testScanning() = testWithFlow {
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
        val deviceFilter = deviceFilter
        val scanForDevicesCompleted = mockBaseScanner().scanForDevicesCompleted.get()
        val stopScanningCompleted = mockBaseScanner().stopScanningCompleted.get()
        test {
            assertTrue(it is Scanning)
            assertEquals(deviceFilter, it.discovered.filter)
            assertEquals(emptyList(), it.discovered.devices)
            assertEquals(deviceFilter, scanForDevicesCompleted.getCompleted())
            assertFalse(stopScanningCompleted.isCompleted)
        }
        action {
            scanningStateRepo.takeAndChangeState { scanningState ->
                when (scanningState) {
                    is Scanning -> scanningState.discoverDevice(deviceWrapper.identifier, 0, advertisementData) { device }
                    else -> scanningState.remain()
                }
            }
        }
        val device = device
        val scanForDevicesCompletedSecond = mockBaseScanner().scanForDevicesCompleted.get()
        val stopScanningCompletedSecond = mockBaseScanner().stopScanningCompleted.get()
        test {
            assertTrue(it is Scanning)
            assertEquals(deviceFilter, it.discovered.filter)
            assertEquals(listOf(device), it.discovered.devices)
            assertEquals(deviceFilter, scanForDevicesCompletedSecond.getCompleted())
            assertFalse(stopScanningCompletedSecond.isCompleted)
        }
        action {
            scanningStateRepo.takeAndChangeState { scanningState ->
                when (scanningState) {
                    is Scanning -> scanningState.stopScanning
                    else -> scanningState.remain()
                }
            }
        }
        val stopScanningCompletedThird = mockBaseScanner().stopScanningCompleted.get()
        test {
            assertTrue(it is Idle)
            assertEquals(deviceFilter, it.discovered.filter)
            assertTrue(stopScanningCompletedThird.isCompleted)
            assertEquals(listOf(device), it.discovered.devices)
        }

        resetFlow()
        mockBaseScanner().reset()

        test {
            assertTrue(it is Idle)
            assertEquals(deviceFilter, it.discovered.filter)
            assertEquals(listOf(device), it.discovered.devices)
        }
    }

    @Test
    fun testRediscoverSameDevice() = testWithFlow {
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

        val scanForDevicesCompleted = mockBaseScanner().scanForDevicesCompleted.get()
        val stopScanningCompleted = mockBaseScanner().stopScanningCompleted.get()
        val deviceFilter = deviceFilter
        test {
            assertTrue(it is Scanning)
            assertEquals(deviceFilter, it.discovered.filter)
            assertEquals(emptyList(), it.discovered.devices)
            assertEquals(deviceFilter, scanForDevicesCompleted.getCompleted())
            assertFalse(stopScanningCompleted.isCompleted)
        }
        action {
            scanningStateRepo.takeAndChangeState { scanningState ->
                when (scanningState) {
                    is Scanning -> scanningState.discoverDevice(deviceWrapper.identifier, rssi, advertisementData) { device }
                    else -> scanningState.remain()
                }
            }
        }

        val advertisementData = advertisementData
        test {
            assertTrue(it is Scanning)
            assertEquals(advertisementData, it.discovered.devices[0].first().advertisementData)
        }

        action {

            val deviceWrapper = deviceWrapper
            val device = device
            val scanningStateRepo = scanningStateRepo

            scanningStateRepo.takeAndChangeState { scanningState ->
                when (scanningState) {
                    is Scanning -> {
                        val newAdvertisementData = MockAdvertisementData(name = "New Name")
                        val newRssi = -42
                        val newState = scanningState.discoverDevice(
                            deviceWrapper.identifier,
                            newRssi,
                            newAdvertisementData
                        ) { device }
                        assertEquals(scanningState.remain(), newState)
                        assertEquals(listOf(device), scanningState.discovered.devices)
                        assertEquals(
                            newAdvertisementData,
                            scanningState.discovered.devices[0].first().advertisementData
                        )
                        assertEquals(newRssi, scanningState.discovered.devices[0].first().rssi)
                        newState
                    }
                    else -> fail("unexpected state")
                }
            }
        }
    }

    @Test
    fun testPermissionsRevokedWhileIdle() = testWithFlow {
        test {
            assertTrue(it is Idle)
        }
        action {
            permissionManager.setPermissionDenied()
        }
        test {
            println("->> $it")
            assertTrue(it is MissingPermissions)
        }

        mockBaseScanner().startMonitoringBluetoothCompleted.get().await()

    }

    @Test
    fun testBluetoothDisabledWhileIdle() = testWithFlow {
        test {
            assertTrue(it is Idle)
        }
        action {
            mockBaseScanner().isEnabled = false
            mockBaseScanner().bluetoothDisabled()
        }

        test {
            assertTrue(it is Disabled)
        }
    }

    @Test
    fun testPermissionRevokedWhileScanning() = testWithFlow {
        test {
            assertTrue(it is Idle)
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
        val stopScanningCompleted = mockBaseScanner().stopScanningCompleted.get()
        test {
            assertTrue(stopScanningCompleted.isCompleted)
            assertTrue(it is MissingPermissions)
        }
    }

    @Test
    fun testBluetoothDisabledWhileScanning() = testWithBluetoothFlow {
        test {
            assertTrue(it is Idle)
        }
        action {
            scanningStateRepo.takeAndChangeState { scanningState ->
                when (scanningState) {
                    is Idle -> scanningState.startScanning(deviceFilter)
                    else -> fail("unexpected state $scanningState")
                }
            }
        }
        test {
            assertTrue(it is Scanning)
        }
        action {
            mockBaseScanner().isEnabled = false
            mockBaseScanner().bluetoothDisabled()
        }
        val stopScanningCompleted = mockBaseScanner().stopScanningCompleted.get()
        test {
            assertTrue(stopScanningCompleted.isCompleted)
            assertTrue(it is Disabled)
        }
    }
}