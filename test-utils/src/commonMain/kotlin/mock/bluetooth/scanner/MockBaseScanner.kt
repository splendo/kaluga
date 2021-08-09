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

import co.touchlab.stately.concurrency.AtomicBoolean
import co.touchlab.stately.concurrency.AtomicReference
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.scanner.BaseScanner
import com.splendo.kaluga.bluetooth.scanner.ScanningState
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.state.StateRepo
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableStateFlow

class MockBaseScanner(
    permissions: Permissions,
    connectionSettings: ConnectionSettings,
    autoRequestPermissions: Boolean,
    autoEnableBluetooth: Boolean,
    stateRepo: StateRepo<ScanningState, MutableStateFlow<ScanningState>>
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
    val startMonitoringBluetoothCompleted = AtomicReference(EmptyCompletableDeferred())
    val stopMonitoringBluetoothCompleted = AtomicReference(EmptyCompletableDeferred())

    val _isEnabled = AtomicBoolean(false)
    var isEnabled: Boolean
        get() = _isEnabled.value
        set(value) { _isEnabled.value = value }

    init {
        reset()
    }

    fun reset() {
        scanForDevicesCompleted.set(CompletableDeferred())
        stopScanningCompleted.set(EmptyCompletableDeferred())
        stopMonitoringBluetoothCompleted.set(EmptyCompletableDeferred())
        startMonitoringBluetoothCompleted.set(EmptyCompletableDeferred())
        startMonitoringPermissionsCompleted.set(EmptyCompletableDeferred())
        stopMonitoringPermissionsCompleted.set(EmptyCompletableDeferred())
        requestEnableCompleted.set(EmptyCompletableDeferred())
    }

    override fun startMonitoringPermissions() {
        super.startMonitoringPermissions()
        startMonitoringPermissionsCompleted.get().complete()
    }

    override fun stopMonitoringPermissions() {
        super.stopMonitoringPermissions()
        stopMonitoringPermissionsCompleted.get().complete()
    }

    override suspend fun scanForDevices(filter: Set<UUID>) {
        scanForDevicesCompleted.get().complete(filter)
    }

    override suspend fun stopScanning() {
        stopScanningCompleted.get().complete()
    }

    override fun startMonitoringBluetooth() {
        startMonitoringBluetoothCompleted.get().complete()
    }

    override fun stopMonitoringBluetooth() {
        stopMonitoringBluetoothCompleted.get().complete()
    }

    override suspend fun isBluetoothEnabled(): Boolean {
        return isEnabled
    }

    override suspend fun requestBluetoothEnable() {
        requestEnableCompleted.get().complete()
    }
}
