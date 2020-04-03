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
import com.splendo.kaluga.bluetooth.device.AdvertisementData
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.logging.info
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionState
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.state.StateRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

abstract class BaseScanner internal constructor(internal val permissions: Permissions,
                                                private val connectionSettings: ConnectionSettings,
                                                private val autoRequestPermission: Boolean,
                                                internal val autoEnableBluetooth: Boolean,
                                                internal val stateRepo: StateRepo<ScanningState>,
                                                coroutineScope: CoroutineScope)
    : CoroutineScope by coroutineScope {

    interface Builder {
        fun create(permissions: Permissions,
                   connectionSettings: ConnectionSettings,
                   autoRequestPermission: Boolean,
                   autoEnableBluetooth: Boolean,
                   scanningStateRepo: StateRepo<ScanningState>,
                   coroutineScope: CoroutineScope): BaseScanner
    }

    private val bluetoothPermissionRepo get() = permissions[Permission.Bluetooth]
    private var monitoringPermissionsJob: Job? = null

    internal open fun startMonitoringPermissions() {
        info("Scanner", "Start Monitoring")
        if (monitoringPermissionsJob != null) return
        info("Scanner", "Start Monitoring Will Launch")
        monitoringPermissionsJob = launch {
            info("Scanner", "Start Monitoring Did Launch")
            bluetoothPermissionRepo.collect { state ->
                info("Scanner", "Start Monitoring Did Collect")
                when (state) {
                    is PermissionState.Denied.Requestable -> if (autoRequestPermission) state.request()
                    else -> {}
                }
                val hasPermission = state is PermissionState.Allowed
                stateRepo.takeAndChangeState { scanState ->
                    when (scanState) {
                        is ScanningState.NoBluetoothState.Disabled, is ScanningState.Enabled -> if (hasPermission) scanState.remain else (scanState as ScanningState.Permitted).revokePermission
                        is ScanningState.NoBluetoothState.MissingPermissions -> if (hasPermission) scanState.permit(isBluetoothEnabled()) else scanState.remain
                    }
                }
            }
        }
    }

    internal open fun stopMonitoringPermissions() {
        monitoringPermissionsJob?.cancel()
        monitoringPermissionsJob = null
    }

    internal suspend fun isPermitted(): Boolean {
        return bluetoothPermissionRepo.first() is PermissionState.Allowed
    }

    internal abstract suspend fun scanForDevices(filter: Set<UUID>)
    internal abstract suspend fun stopScanning()
    internal abstract fun startMonitoringBluetooth()
    internal abstract fun stopMonitoringBluetooth()
    internal abstract suspend fun isBluetoothEnabled(): Boolean
    internal abstract suspend fun requestBluetoothEnable()

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

    internal fun handleDeviceDiscovered(identifier: Identifier, advertisementData: AdvertisementData, deviceCreator: () -> Device) {
        launch {
            stateRepo.takeAndChangeState { state ->
                when (state) {
                    is ScanningState.Enabled.Scanning -> {
                        state.discoverDevice(identifier, advertisementData, deviceCreator)
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