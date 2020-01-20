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
import com.splendo.kaluga.bluetooth.device.BaseDeviceConnectionManager
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.state.StateRepoAccesor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseScanner internal constructor(internal val permissions: Permissions,
                                                internal val stateRepoAccessor: StateRepoAccesor<ScanningState>,
                                                coroutineScope: CoroutineScope)
    : CoroutineScope by coroutineScope {

    interface Builder {
        val autoEnableBluetooth: Boolean
        fun create(stateRepoAccessor: StateRepoAccesor<ScanningState>, coroutineScope: CoroutineScope): Scanner
    }

    internal abstract fun scanForDevices(filter: Set<UUID>)
    internal abstract fun stopScanning()
    internal abstract fun startMonitoringBluetooth()
    internal abstract fun stopMonitoringBluetooth()

    internal fun bluetoothEnabled() {
        launch {
            when (val state = stateRepoAccessor.currentState()) {
                is ScanningState.NoBluetoothState.Disabled -> state.enable()
            }
        }
    }

    internal fun bluetoothDisabled() {
        launch {
            when (val state = stateRepoAccessor.currentState()) {
                is ScanningState.Enabled -> state.disable()
            }
        }
    }

}

expect class Scanner : BaseScanner