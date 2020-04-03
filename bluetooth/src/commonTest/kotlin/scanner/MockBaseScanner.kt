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

import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.state.StateRepo
import com.splendo.kaluga.utils.EmptyCompletableDeferred
import com.splendo.kaluga.utils.complete
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope

class MockBaseScanner(permissions: Permissions, connectionSettings: ConnectionSettings, autoRequestPermissions: Boolean, autoEnableBluetooth: Boolean, stateRepo: StateRepo<ScanningState>, coroutineScope: CoroutineScope) : BaseScanner(permissions,
    connectionSettings,
    autoRequestPermissions,
    autoEnableBluetooth,
    stateRepo,
    coroutineScope) {

    lateinit var scanForDevicesCompleted: CompletableDeferred<Set<UUID>>
    lateinit var stopScanningCompleted: EmptyCompletableDeferred
    lateinit var startMonitoringPermissions: EmptyCompletableDeferred
    lateinit var stopMonitoringPermissions: EmptyCompletableDeferred
    lateinit var requestEnableCompleted: EmptyCompletableDeferred
    lateinit var startMonitoringBluetoothCompleted: EmptyCompletableDeferred
    lateinit var stopMonitoringBluetoothCompleted: EmptyCompletableDeferred

    var isEnabled: Boolean = false

    init {
        reset()
    }

    fun reset() {
        scanForDevicesCompleted = CompletableDeferred()
        stopScanningCompleted = EmptyCompletableDeferred()
        stopMonitoringBluetoothCompleted = EmptyCompletableDeferred()
        startMonitoringBluetoothCompleted = EmptyCompletableDeferred()
        startMonitoringPermissions = EmptyCompletableDeferred()
        stopMonitoringPermissions = EmptyCompletableDeferred()
        requestEnableCompleted = EmptyCompletableDeferred()
    }

    override fun startMonitoringPermissions() {
        super.startMonitoringPermissions()
        startMonitoringPermissions.complete()
    }

    override fun stopMonitoringPermissions() {
        super.stopMonitoringPermissions()
        stopMonitoringPermissions.complete()
    }

    override suspend fun scanForDevices(filter: Set<UUID>) {
        scanForDevicesCompleted.complete(filter)
    }

    override suspend fun stopScanning() {
        stopScanningCompleted.complete()
    }

    override fun startMonitoringBluetooth() {
        startMonitoringBluetoothCompleted.complete()
    }

    override fun stopMonitoringBluetooth() {
        stopMonitoringBluetoothCompleted.complete()
    }

    override suspend fun isBluetoothEnabled(): Boolean {
        return isEnabled
    }

    override suspend fun requestBluetoothEnable() {
        requestEnableCompleted.complete()
    }
}

