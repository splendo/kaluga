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
import com.splendo.kaluga.bluetooth.device.*
import com.splendo.kaluga.bluetooth.device.DeviceConnectionManager
import com.splendo.kaluga.permissions.BasePermissions
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.state.StateRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseScanner internal constructor(internal val permissions: BasePermissions,
                                                internal val stateRepo: StateRepo<ScanningState>)
    : CoroutineScope by stateRepo {

    interface Builder {
        val autoEnableBluetooth: Boolean
        fun create(scanningStateRepo: StateRepo<ScanningState>): BaseScanner
    }

    internal abstract fun scanForDevices(filter: Set<UUID>)
    internal abstract fun stopScanning()
    internal abstract fun startMonitoringBluetooth()
    internal abstract fun stopMonitoringBluetooth()

    internal fun bluetoothEnabled() {
        launch {
            stateRepo.takeAndChangeState { state ->
                when (state) {
                    is ScanningState.NoBluetoothState.Disabled -> state.enable
                    else -> state.remain
                }
            }
        }
    }

    internal fun bluetoothDisabled() {
        launch {
            stateRepo.takeAndChangeState { state ->
                when (state) {
                    is ScanningState.Enabled -> state.disable
                    else -> state.remain
                }
            }
        }
    }

    internal fun handleDevicesDiscovered(devices: List<Device> ) {
        launch {
            stateRepo.takeAndChangeState { state ->
                when (state) {
                    is ScanningState.Enabled.Scanning -> {
                        state.discoverDevices(*devices.toTypedArray())
                    }
                    else -> {
                        state.logError(Error("Discovered Device while not scanning"))
                        state.remain
                    }
                }
            }
        }
    }

}

expect class Scanner : BaseScanner