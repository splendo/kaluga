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
import com.splendo.kaluga.bluetooth.scanner.ScanningState.Enabled.Idle
import com.splendo.kaluga.bluetooth.scanner.ScanningState.Enabled.Scanning
import com.splendo.kaluga.bluetooth.scanner.ScanningState.NoBluetooth.Disabled
import com.splendo.kaluga.bluetooth.scanner.ScanningState.NoBluetooth.MissingPermissions
import com.splendo.kaluga.bluetooth.scanner.ScanningState.NotInitialized
import com.splendo.kaluga.permissions.PermissionStateRepo
import com.splendo.kaluga.test.bluetooth.device.MockAdvertisementData
import com.splendo.kaluga.test.mock.matcher.ParameterMatcher.Companion.eq
import com.splendo.kaluga.test.mock.verification.VerificationRule.Companion.never
import com.splendo.kaluga.test.mock.verify
import com.splendo.kaluga.test.permissions.MockPermissionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.fail

class ScanningStateRepoTest : BluetoothFlowTest<BluetoothFlowTest.Configuration.DeviceWithoutService, BluetoothFlowTest.DeviceContext, ScanningState>() {

    override val createTestContextWithConfiguration: suspend (configuration: Configuration.DeviceWithoutService, scope: CoroutineScope) -> DeviceContext = { configuration, coroutineScope -> DeviceContext(configuration, coroutineScope) }
    override val flowFromTestContext: suspend DeviceContext.() -> Flow<ScanningState> = { bluetooth.scanningStateRepo.filterOnlyImportant() }

    override val filter: (Flow<ScanningState>) -> Flow<ScanningState> = {
        it.distinctUntilChanged(areEquivalent = { old, new -> (old is Idle && new is Idle) || old == new }).filterNot { state -> state is NotInitialized }
    }

    @Test
    fun testStartWithBluetoothEnabled() = testWithFlowAndTestContext(Configuration.DeviceWithoutService()) {
        test {
            permissionManager.startMonitoringMock.verify(eq(PermissionStateRepo.defaultMonitoringInterval))
            scanner.startMonitoringPermissionsMock.verify()
            scanner.startMonitoringSensorsMock.verify()
            assertIs<Idle>(it)
            assertEquals(emptySet(), it.discovered.filter)
            assertEquals(emptyList(), it.discovered.devices)
        }
    }

    @Test
    fun testStartWithoutPermissions() = testWithFlowAndTestContext(Configuration.DeviceWithoutService(initialPermissionState = MockPermissionManager.MockPermissionState.DENIED)) {
        test {
            permissionManager.startMonitoringMock.verify(eq(PermissionStateRepo.defaultMonitoringInterval))
            permissionManager.requestPermissionMock.verify()
            scanner.startMonitoringPermissionsMock.verify()
            scanner.startMonitoringSensorsMock.verify(rule = never())
            assertIs<MissingPermissions>(it)
        }
        mainAction {
            permissionManager.setPermissionAllowed()
        }
        test {
            assertIs<Idle>(it)
            assertEquals(emptySet(), it.discovered.filter)
            assertEquals(emptyList(), it.discovered.devices)
        }
    }

    @Test
    fun testStartWithoutPermissionNoAutoRequest() = testWithFlowAndTestContext(
        Configuration.DeviceWithoutService(
            autoRequestPermission = false,
            initialPermissionState = MockPermissionManager.MockPermissionState.DENIED
        )
    ) {
        test {
            permissionManager.startMonitoringMock.verify(eq(PermissionStateRepo.defaultMonitoringInterval))
            permissionManager.requestPermissionMock.verify(rule = never())
            scanner.startMonitoringPermissionsMock.verify()
            scanner.startMonitoringSensorsMock.verify(rule = never())
            assertIs<MissingPermissions>(it)
        }
    }

    @Test
    fun testStartWithBluetoothDisabled() = testWithFlowAndTestContext(Configuration.DeviceWithoutService(isEnabled = false)) {
        test {
            scanner.generateEnableSensorsActionsMock.verify()
            assertIs<Disabled>(it)
        }
        mainAction {
            scanner.isEnabled.value = true
        }
        test {
            assertIs<Idle>(it)
            assertEquals(emptySet(), it.discovered.filter)
            assertEquals(emptyList(), it.discovered.devices)
        }
    }

    @Test
    fun testStartWithBluetoothDisabledNoAutoEnable() = testWithFlowAndTestContext(Configuration.DeviceWithoutService(autoEnableBluetooth = false, isEnabled = false)) {
        test {
            scanner.generateEnableSensorsActionsMock.verify(rule = never())
            assertIs<Disabled>(it)
        }
    }

    @Test
    fun testScanning() = testWithFlowAndTestContext(Configuration.DeviceWithoutService()) {
        test {
            assertIs<Idle>(it)
            assertEquals(emptySet(), it.discovered.filter)
            assertEquals(emptyList(), it.discovered.devices)
        }
        mainAction {
            scanningStateRepo.takeAndChangeState { scanningState ->
                when (scanningState) {
                    is Idle -> scanningState.startScanning(deviceFilter)
                    else -> scanningState.remain()
                }
            }
        }
        val advertisementData = MockAdvertisementData()
        test {
            assertIs<Scanning>(it)
            assertEquals(deviceFilter, it.discovered.filter)
            assertEquals(emptyList(), it.discovered.devices)
            scanner.scanForDevicesMock.verify(eq(deviceFilter))
            scanner.stopScanningMock.verify(rule = never())
        }
        mainAction {
            val device = device
            scanningStateRepo.takeAndChangeState { scanningState ->
                when (scanningState) {
                    is Scanning -> scanningState.discoverDevice(deviceWrapper.identifier, 0, advertisementData) { device }
                    else -> scanningState.remain()
                }
            }
        }
        test {
            assertIs<Scanning>(it)
            assertEquals(deviceFilter, it.discovered.filter)
            assertEquals(listOf(device), it.discovered.devices)
            scanner.stopScanningMock.verify(rule = never())
        }
        mainAction {
            scanningStateRepo.takeAndChangeState { scanningState ->
                when (scanningState) {
                    is Scanning -> scanningState.stopScanning
                    else -> scanningState.remain()
                }
            }
        }
        test {
            assertIs<Idle>(it)
            assertEquals(deviceFilter, it.discovered.filter)
            scanner.stopScanningMock.verify()
            assertEquals(listOf(device), it.discovered.devices)
        }

        action {
            resetFlow()
        }

        mainAction {
            scanner.stopMonitoringPermissionsMock.verify()
            scanner.stopMonitoringSensorsMock.verify()
        }

        test {
            assertIs<Idle>(it)
            assertEquals(deviceFilter, it.discovered.filter)
            assertEquals(listOf(device), it.discovered.devices)
        }
    }

    @Test
    fun testRediscoverSameDevice() = testWithFlowAndTestContext(Configuration.DeviceWithoutService()) {
        test {
            assertIs<Idle>(it)
            assertEquals(emptySet(), it.discovered.filter)
            assertEquals(emptyList(), it.discovered.devices)
        }
        mainAction {
            scanningStateRepo.takeAndChangeState { scanningState ->
                when (scanningState) {
                    is Idle -> scanningState.startScanning(deviceFilter)
                    else -> scanningState.remain()
                }
            }
        }

        test {
            assertIs<Scanning>(it)
            assertEquals(deviceFilter, it.discovered.filter)
            assertEquals(emptyList(), it.discovered.devices)
            scanner.scanForDevicesMock.verify(eq(deviceFilter))
            scanner.stopScanningMock.verify(rule = never())
        }
        mainAction {
            scanningStateRepo.takeAndChangeState { scanningState ->
                when (scanningState) {
                    is Scanning -> scanningState.discoverDevice(deviceWrapper.identifier, configuration.rssi, configuration.advertisementData) { device }
                    else -> scanningState.remain()
                }
            }
        }

        test {
            assertIs<Scanning>(it)
            assertEquals(configuration.advertisementData, it.discovered.devices[0].first().advertisementData)
        }

        mainAction {

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
    fun testPermissionsRevokedWhileIdle() = testWithFlowAndTestContext(Configuration.DeviceWithoutService()) {
        test {
            assertIs<Idle>(it)
        }
        mainAction {
            permissionManager.setPermissionDenied()
        }
        test {
            assertIs<MissingPermissions>(it)
        }
    }

    @Test
    fun testBluetoothDisabledWhileIdle() = testWithFlowAndTestContext(Configuration.DeviceWithoutService()) {
        test {
            assertIs<Idle>(it)
        }
        mainAction {
            scanner.isEnabled.value = false
        }

        test {
            assertIs<Disabled>(it)
        }
    }

    @Test
    fun testPermissionRevokedWhileScanning() = testWithFlowAndTestContext(Configuration.DeviceWithoutService()) {
        test {
            assertIs<Idle>(it)
        }
        mainAction {
            scanningStateRepo.takeAndChangeState { scanningState ->
                when (scanningState) {
                    is Idle -> scanningState.startScanning(deviceFilter)
                    else -> scanningState.remain()
                }
            }
        }
        test {
            assertIs<Scanning>(it)
        }
        mainAction {
            permissionManager.setPermissionDenied()
        }
        test {
            scanner.stopScanningMock.verify()
            assertIs<MissingPermissions>(it)
        }
    }

    @Test
    fun testBluetoothDisabledWhileScanning() = testWithFlowAndTestContext(Configuration.DeviceWithoutService()) {
        test {
            assertIs<Idle>(it)
        }
        mainAction {
            scanningStateRepo.takeAndChangeState { scanningState ->
                when (scanningState) {
                    is Idle -> scanningState.startScanning(deviceFilter)
                    else -> fail("unexpected state $scanningState")
                }
            }
        }
        test {
            assertIs<Scanning>(it)
        }
        mainAction {
            scanner.isEnabled.value = false
        }
        test {
            scanner.stopScanningMock.verify()
            assertIs<Disabled>(it)
        }
    }
}
