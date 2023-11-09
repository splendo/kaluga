/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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
import com.splendo.kaluga.bluetooth.BluetoothService
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.randomUUID
import com.splendo.kaluga.test.bluetooth.device.MockDevice
import kotlin.coroutines.CoroutineContext
import kotlin.test.Test
import kotlin.test.assertEquals

class DevicesTest {

    @Test
    fun testUpdateScanDevices() = runBlocking {
        val devices = List(4) { createMockDevice(coroutineContext) }
        val emptyFilter = emptySet<UUID>()
        val filter = setOf(randomUUID())

        DefaultDevices(emptyFilter)
            .addScannedAndValidate(devices[0]) {
                validateAllDevices(devices[0])
                validateDevicesForScanningFilter(emptyFilter, devices[0])
                validateDevicesForScanningFilter(filter)
                validateDevicesForCurrentScanFilter(devices[0])
            }
            .addScannedAndValidate(devices[1]) {
                validateAllDevices(devices[0], devices[1])
                validateDevicesForScanningFilter(emptyFilter, devices[0], devices[1])
                validateDevicesForScanningFilter(filter)
                validateDevicesForCurrentScanFilter(devices[0], devices[1])
            }
            .setFilterValidate(filter, BluetoothService.CleanMode.ONLY_PROVIDED_FILTER) {
                validateAllDevices(devices[0], devices[1])
                validateDevicesForScanningFilter(emptyFilter, devices[0], devices[1])
                validateDevicesForScanningFilter(filter)
                validateDevicesForCurrentScanFilter()
            }
            .addScannedAndValidate(devices[1]) {
                validateAllDevices(devices[0], devices[1])
                validateDevicesForScanningFilter(emptyFilter, devices[0], devices[1])
                validateDevicesForScanningFilter(filter, devices[1])
                validateDevicesForCurrentScanFilter(devices[1])
            }
            .addScannedAndValidate(devices[2]) {
                validateAllDevices(devices[0], devices[1], devices[2])
                validateDevicesForScanningFilter(emptyFilter, devices[0], devices[1])
                validateDevicesForScanningFilter(filter, devices[1], devices[2])
                validateDevicesForCurrentScanFilter(devices[1], devices[2])
            }
            .setFilterValidate(emptyFilter, BluetoothService.CleanMode.ONLY_PROVIDED_FILTER) {
                validateAllDevices(devices[1], devices[2])
                validateDevicesForScanningFilter(emptyFilter)
                validateDevicesForScanningFilter(filter, devices[1], devices[2])
                validateDevicesForCurrentScanFilter()
            }
            .addScannedAndValidate(devices[0]) {
                validateAllDevices(devices[0], devices[1], devices[2])
                validateDevicesForScanningFilter(emptyFilter, devices[0])
                validateDevicesForScanningFilter(filter, devices[1], devices[2])
                validateDevicesForCurrentScanFilter(devices[0])
            }
            .setFilterValidate(filter, BluetoothService.CleanMode.RETAIN_ALL) {
                validateAllDevices(devices[0], devices[1], devices[2])
                validateDevicesForScanningFilter(emptyFilter, devices[0])
                validateDevicesForScanningFilter(filter, devices[1], devices[2])
                validateDevicesForCurrentScanFilter(devices[1], devices[2])
            }
            .addPairedAndValidate(filter, false, devices[3]) {
                validateAllDevices(devices[0], devices[1], devices[2], devices[3])
                validateDevicesForScanningFilter(emptyFilter, devices[0])
                validateDevicesForScanningFilter(filter, devices[1], devices[2])
                validateDevicesForCurrentScanFilter(devices[1], devices[2])
            }
            .setFilterValidate(emptyFilter, BluetoothService.CleanMode.REMOVE_ALL) {
                validateAllDevices()
                validateDevicesForScanningFilter(emptyFilter)
                validateDevicesForScanningFilter(filter)
                validateDevicesForCurrentScanFilter()
            }
        Unit
    }

    @Test
    fun testUpdatePairedDevices() = runBlocking {
        val devices = List(4) { createMockDevice(coroutineContext) }
        val filter1 = setOf(randomUUID())
        val filter2 = setOf(randomUUID())

        DefaultDevices(emptySet())
            .addPairedAndValidate(filter1, false, devices[0], devices[1]) {
                validateAllDevices(devices[0], devices[1])
                validateDevicesForPairingFilter(filter1, devices[0], devices[1])
                validateDevicesForPairingFilter(filter2)
                validateDevicesForCurrentScanFilter()
            }
            .addPairedAndValidate(filter1, false, devices[1], devices[2]) {
                validateAllDevices(devices[1], devices[2])
                validateDevicesForPairingFilter(filter1, devices[1], devices[2])
                validateDevicesForPairingFilter(filter2)
                validateDevicesForCurrentScanFilter()
            }
            .addPairedAndValidate(filter2, false, devices[0], devices[3]) {
                validateAllDevices(devices[0], devices[1], devices[2], devices[3])
                validateDevicesForPairingFilter(filter1, devices[1], devices[2])
                validateDevicesForPairingFilter(filter2, devices[0], devices[3])
                validateDevicesForCurrentScanFilter()
            }
            .addScannedAndValidate(devices[0]) {
                validateAllDevices(devices[0], devices[1], devices[2], devices[3])
                validateDevicesForPairingFilter(filter1, devices[1], devices[2])
                validateDevicesForPairingFilter(filter2, devices[0], devices[3])
                validateDevicesForCurrentScanFilter(devices[0])
            }
            .addPairedAndValidate(filter1, true, devices[0], devices[1]) {
                validateAllDevices(devices[0], devices[1])
                validateDevicesForPairingFilter(filter1, devices[0], devices[1])
                validateDevicesForPairingFilter(filter2)
                validateDevicesForCurrentScanFilter(devices[0])
            }
            .setFilterValidate(filter1, cleanMode = BluetoothService.CleanMode.REMOVE_ALL) {
                validateAllDevices()
                validateDevicesForPairingFilter(filter1)
                validateDevicesForPairingFilter(filter2)
                validateDevicesForCurrentScanFilter()
            }
        Unit
    }

    private fun createMockDevice(coroutineContext: CoroutineContext) = MockDevice(coroutineContext = coroutineContext)

    private fun ScanningState.Devices.addScannedAndValidate(device: Device, validation: ScanningState.Devices.() -> Unit) = copyAndAddScanned(device.identifier) { device }
        .apply(validation)
    private fun ScanningState.Devices.addPairedAndValidate(filter: Filter, removeAllPairedFilters: Boolean, vararg device: Device, validation: ScanningState.Devices.() -> Unit) =
        copyAndSetPaired(device.associate { it.identifier to { it } }, filter, removeAllPairedFilters).apply(validation)

    private fun ScanningState.Devices.setFilterValidate(filter: Filter, cleanMode: BluetoothService.CleanMode, validation: ScanningState.Devices.() -> Unit) =
        updateScanFilter(filter, cleanMode).apply(validation)

    private fun ScanningState.Devices.validateAllDevices(vararg device: Device) = assertEquals(device.associateBy { it.identifier }, allDevices)

    private fun ScanningState.Devices.validateDevicesForScanningFilter(filter: Filter, vararg device: Device) =
        validateDevicesForDiscoveryMode(ScanningState.DeviceDiscoveryMode.Scanning(filter), *device)
    private fun ScanningState.Devices.validateDevicesForPairingFilter(filter: Filter, vararg device: Device) =
        validateDevicesForDiscoveryMode(ScanningState.DeviceDiscoveryMode.Paired(filter), *device)
    private fun ScanningState.Devices.validateDevicesForDiscoveryMode(discoveryMode: ScanningState.DeviceDiscoveryMode, vararg device: Device) {
        assertEquals(device.map { it.identifier }.toSet(), identifiersFoundForDeviceDiscoveryMode[discoveryMode] ?: emptySet())
        assertEquals(device.toList(), devicesForDiscoveryMode(discoveryMode))
    }
    private fun ScanningState.Devices.validateDevicesForCurrentScanFilter(vararg device: Device) {
        assertEquals(device.map { it.identifier }.toSet(), identifiersForCurrentScanFilter)
        assertEquals(device.toList(), this.devicesForCurrentScanFilter())
    }
}
