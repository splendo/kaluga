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
import com.splendo.kaluga.test.base.yieldMultiple
import com.splendo.kaluga.bluetooth.BluetoothFlowTest
import com.splendo.kaluga.bluetooth.device.BaseAdvertisementData
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.scanner.ScanningState.Enabled.Idle
import com.splendo.kaluga.bluetooth.scanner.ScanningState.Enabled.Scanning
import com.splendo.kaluga.bluetooth.scanner.ScanningState.NoBluetooth.Disabled
import com.splendo.kaluga.bluetooth.scanner.ScanningState.NoBluetooth.MissingPermissions
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermission
import com.splendo.kaluga.test.base.mock.matcher.ParameterMatcher.Companion.eq
import com.splendo.kaluga.test.base.mock.verification.VerificationRule.Companion.never
import com.splendo.kaluga.test.base.mock.verify
import com.splendo.kaluga.test.bluetooth.device.MockAdvertisementData
import com.splendo.kaluga.test.permissions.MockPermissionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.fail

class ScanningStateRepoTest : BluetoothFlowTest<BluetoothFlowTest.Configuration.DeviceWithoutService, BluetoothFlowTest.DeviceContext, ScanningState>() {

    override val createTestContextWithConfiguration: suspend (configuration: Configuration.DeviceWithoutService, scope: CoroutineScope) -> DeviceContext = { configuration, coroutineScope -> DeviceContext(configuration, coroutineScope) }
    override val flowFromTestContext: suspend DeviceContext.() -> Flow<ScanningState> = { bluetooth.scanningStateRepo.filterOnlyImportant() }

    override val filter: (Flow<ScanningState>) -> Flow<ScanningState> = {
        it.distinctUntilChanged(areEquivalent = { old, new -> (old is Idle && new is Idle) || old == new }).filterOnlyImportant()
    }

    @Test
    fun testStartWithBluetoothEnabled() = testWithFlowAndTestContext(Configuration.DeviceWithoutService()) {
        test {
            scanner.startMonitoringPermissionsMock.verify()
            scanner.startMonitoringHardwareEnabledMock.verify()
            assertIs<Idle>(it)
            assertEquals(emptySet(), it.discovered.filter)
            assertEquals(emptyList(), it.discovered.devices)
        }
    }

    @Test
    fun testStartWithoutPermissions() = testWithFlowAndTestContext(Configuration.DeviceWithoutService(initialPermissionState = MockPermissionState.ActiveState.REQUESTABLE)) {
        test {
            permissionStateRepo.currentMockState.requestMock.verify()
            scanner.startMonitoringPermissionsMock.verify()
            scanner.startMonitoringHardwareEnabledMock.verify(rule = never())
            assertIs<MissingPermissions>(it)
        }
        mainAction {
            permissionStateRepo.takeAndChangeState { state ->
                (state as MockPermissionState<BluetoothPermission>).allow
            }
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
            initialPermissionState = MockPermissionState.ActiveState.LOCKED
        )
    ) {
        test {
            permissionStateRepo.currentMockState.requestMock.verify(rule = never())
            scanner.startMonitoringPermissionsMock.verify()
            scanner.startMonitoringHardwareEnabledMock.verify(rule = never())
            assertIs<MissingPermissions>(it)
        }
    }

    @Test
    fun testStartWithBluetoothDisabled() = testWithFlowAndTestContext(Configuration.DeviceWithoutService(isEnabled = false)) {
        test {
            assertIs<Disabled>(it)
        }
        mainAction {
            scanner.generateEnableSensorsActionsMock.verify()
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
            assertIs<Disabled>(it)
        }
        mainAction {
            scanner.generateEnableSensorsActionsMock.verify(rule = never())
        }
    }

    private suspend fun Scanning.discoverDevice(
        identifier: Identifier,
        rssi: Int,
        advertisementData: BaseAdvertisementData,
        deviceCreator: () -> Device
    ) = discoverDevices(
        listOf(
            Scanning.DiscoveredDevice(
                identifier,
                rssi,
                advertisementData,
                deviceCreator
            )
        )
    )

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
            scanner.didStartScanningMock.verify(eq(deviceFilter))
            scanner.didStopScanningMock.verify(rule = never())
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
            scanner.didStopScanningMock.verify(rule = never())
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
            scanner.didStopScanningMock.verify()
            assertEquals(listOf(device), it.discovered.devices)
        }

        action {
            resetFlow()
            yieldMultiple()
        }

        mainAction {
            yieldMultiple()
            scanner.stopMonitoringPermissionsMock.verify()
            scanner.stopMonitoringHardwareEnabledMock.verify()

            // here to debug this test potentially being unstable
            println("peek current state: ${bluetooth.scanningStateRepo.stateFlow.value}")
            delay(100)
            println("peek current state after delay: ${bluetooth.scanningStateRepo.stateFlow.value}")
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
            scanner.didStartScanningMock.verify(eq(deviceFilter))
            scanner.didStopScanningMock.verify(rule = never())
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
            assertEquals(configuration.advertisementData, it.discovered.devices[0].info.first().advertisementData)
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
                            scanningState.discovered.devices[0].info.first().advertisementData
                        )
                        assertEquals(newRssi, scanningState.discovered.devices[0].info.first().rssi)
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
            permissionStateRepo.takeAndChangeState { state ->
                (state as MockPermissionState<BluetoothPermission>).lock
            }
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
            permissionStateRepo.takeAndChangeState { state ->
                (state as MockPermissionState<BluetoothPermission>).lock
            }
        }
        test {
            scanner.didStopScanningMock.verify()
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
            scanner.didStopScanningMock.verify()
            assertIs<Disabled>(it)
        }
    }
}
