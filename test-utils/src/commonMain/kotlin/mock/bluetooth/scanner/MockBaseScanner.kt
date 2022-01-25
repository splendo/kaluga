/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.test.mock.bluetooth.scanner

import co.touchlab.stately.concurrency.AtomicReference
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.bluetooth.BluetoothMonitor
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.device.stringValue
import com.splendo.kaluga.bluetooth.scanner.BaseScanner
import com.splendo.kaluga.bluetooth.scanner.EnableSensorAction
import com.splendo.kaluga.bluetooth.scanner.ScanningState
import com.splendo.kaluga.bluetooth.uuidString
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.state.StateRepo
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class MockBaseScanner(
    permissions: Permissions,
    connectionSettings: ConnectionSettings,
    autoRequestPermissions: Boolean,
    autoEnableBluetooth: Boolean,
    stateRepo: StateRepo<ScanningState, MutableStateFlow<ScanningState>>,
    override val isSupported: Boolean = true
) : BaseScanner(
    permissions,
    connectionSettings,
    autoRequestPermissions,
    autoEnableBluetooth,
    stateRepo
) {

    val scanForDevicesCompleted = AtomicReference(CompletableDeferred<Set<UUID>?>())
    val stopScanningCompleted = AtomicReference(EmptyCompletableDeferred())
    val startMonitoringPermissionsCompleted = AtomicReference(EmptyCompletableDeferred())
    val stopMonitoringPermissionsCompleted = AtomicReference(EmptyCompletableDeferred())
    val requestEnableCompleted = AtomicReference(EmptyCompletableDeferred())
    val startMonitoringSensorsCompleted = AtomicReference(EmptyCompletableDeferred())
    val stopMonitoringSensorsCompleted = AtomicReference(EmptyCompletableDeferred())
    val pairedDevicesCompleted = AtomicReference(CompletableDeferred<Set<UUID>?>())

    val pairedDevices = MutableStateFlow<List<Identifier>>(emptyList())
    val isEnabled = MutableStateFlow(false)

    init {
        reset()
    }

    override val bluetoothEnabledMonitor: BluetoothMonitor = object : BluetoothMonitor {
        override val isServiceEnabled: Boolean
            get() = this@MockBaseScanner.isEnabled.value
        override val isEnabled: Flow<Boolean> = this@MockBaseScanner.isEnabled

        override fun startMonitoring() {}

        override fun stopMonitoring() {}
    }

    fun reset() {
        scanForDevicesCompleted.set(CompletableDeferred())
        stopScanningCompleted.set(EmptyCompletableDeferred())
        stopMonitoringSensorsCompleted.set(EmptyCompletableDeferred())
        startMonitoringSensorsCompleted.set(EmptyCompletableDeferred())
        startMonitoringPermissionsCompleted.set(EmptyCompletableDeferred())
        stopMonitoringPermissionsCompleted.set(EmptyCompletableDeferred())
        requestEnableCompleted.set(EmptyCompletableDeferred())
        pairedDevicesCompleted.set(CompletableDeferred())
    }

    override fun startMonitoringPermissions() {
        super.startMonitoringPermissions()
        startMonitoringPermissionsCompleted.get().complete()
    }

    override fun stopMonitoringPermissions() {
        super.stopMonitoringPermissions()
        stopMonitoringPermissionsCompleted.get().complete()
    }

    override fun startMonitoringSensors() {
        super.startMonitoringSensors()
        startMonitoringSensorsCompleted.get().complete()
    }

    override fun stopMonitoringSensors() {
        super.stopMonitoringSensors()
        stopMonitoringSensorsCompleted.get().complete()
    }

    override suspend fun scanForDevices(filter: Set<UUID>) {
        scanForDevicesCompleted.get().complete(filter)
    }

    override suspend fun stopScanning() {
        stopScanningCompleted.get().complete()
    }

    override fun generateEnableSensorsActions(): List<EnableSensorAction> {
        return if (requestEnableCompleted.get().isCompleted) {
            emptyList()
        } else {
            listOf(
                {
                    requestEnableCompleted.get().complete()
                    true
                }
            )
        }
    }

    override fun pairedDevices(withServices: Set<UUID>): List<Identifier> {
        pairedDevicesCompleted.get().complete(withServices)
        return if (isEnabled.value) pairedDevices.value.filter {
            withServices.map(UUID::uuidString).contains(it.stringValue)
        } else emptyList()
    }
}
