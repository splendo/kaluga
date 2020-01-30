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
import com.splendo.kaluga.permissions.BasePermissions
import com.splendo.kaluga.state.StateRepo
import com.splendo.kaluga.utils.EmptyCompletableDeferred
import com.splendo.kaluga.utils.complete
import kotlinx.coroutines.CompletableDeferred

class MockBaseScanner(permissions: BasePermissions, stateRepo: StateRepo<ScanningState>) : BaseScanner(permissions, stateRepo) {

    lateinit var scanForDevicesCompleted: CompletableDeferred<Set<UUID>>
    lateinit var stopScanningCompleted: EmptyCompletableDeferred
    lateinit var startMonitoringBluetoothCompleted: EmptyCompletableDeferred
    lateinit var stopMonitoringBluetoothCompleted: EmptyCompletableDeferred

    init {
        reset()
    }

    fun reset() {
        scanForDevicesCompleted = CompletableDeferred()
        stopScanningCompleted = EmptyCompletableDeferred()
        stopMonitoringBluetoothCompleted = EmptyCompletableDeferred()
        startMonitoringBluetoothCompleted = EmptyCompletableDeferred()
    }

    override fun scanForDevices(filter: Set<UUID>) {
        scanForDevicesCompleted.complete(filter)
    }

    override fun stopScanning() {
        stopScanningCompleted.complete()
    }

    override fun startMonitoringBluetooth() {
        startMonitoringBluetoothCompleted.complete()
    }

    override fun stopMonitoringBluetooth() {
        stopMonitoringBluetoothCompleted.complete()
    }
}

