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

import co.touchlab.stately.concurrency.AtomicReference
import com.splendo.kaluga.base.flow.filterOnlyImportant
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.AdvertisementData
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.scanner.ScanningState.Initialized
import com.splendo.kaluga.bluetooth.scanner.ScanningState.Initialized.Enabled
import com.splendo.kaluga.bluetooth.scanner.ScanningState.Initialized.NoBluetooth.Disabled
import com.splendo.kaluga.permissions.PermissionState
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermission
import com.splendo.kaluga.state.StateRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

abstract class BaseScanner constructor(
    internal val permissions: Permissions,
    private val connectionSettings: ConnectionSettings,
    private val autoRequestPermission: Boolean,
    internal val autoEnableBluetooth: Boolean,
    internal val stateRepo: StateRepo<ScanningState, MutableStateFlow<ScanningState>>,
) : CoroutineScope by stateRepo {

    interface Builder {
        fun create(
            permissions: Permissions,
            connectionSettings: ConnectionSettings,
            autoRequestPermission: Boolean,
            autoEnableBluetooth: Boolean,
            scanningStateRepo: ScanningStateFlowRepo
        ): BaseScanner
    }

    private val bluetoothPermissionRepo get() = permissions[BluetoothPermission]

    private val _monitoringPermissionsJob = AtomicReference<Job?>(null)
    private var monitoringPermissionsJob: Job?
        get() = _monitoringPermissionsJob.get()
        set(value) { _monitoringPermissionsJob.set(value) }

    open fun startMonitoringPermissions() {
        if (monitoringPermissionsJob != null) return
        monitoringPermissionsJob = launch(stateRepo.coroutineContext) {
            bluetoothPermissionRepo.collect { state ->
                when (state) {
                    is PermissionState.Denied.Requestable -> if (autoRequestPermission) state.request(permissions.getManager(BluetoothPermission))
                    else -> {}
                }
                val hasPermission = state is PermissionState.Allowed
                stateRepo.takeAndChangeState { scanState ->
                    when (scanState) {
                        is Disabled, is Enabled -> {
                            if (hasPermission)
                                scanState.remain()
                            else
                                (scanState as? Initialized)?.revokePermission ?: scanState.remain()
                        }
                        is Initialized.NoBluetooth.MissingPermissions -> if (hasPermission) scanState.permit(isBluetoothEnabled()) else scanState.remain()
                        else -> { scanState.remain() }
                    }
                }
            }
        }
    }

    open fun stopMonitoringPermissions() {
        monitoringPermissionsJob?.cancel()
        monitoringPermissionsJob = null
    }

    internal suspend fun isPermitted(): Boolean {
        return bluetoothPermissionRepo.filterOnlyImportant().first() is PermissionState.Allowed
    }

    abstract suspend fun scanForDevices(filter: Set<UUID>)
    abstract suspend fun stopScanning()
    abstract fun startMonitoringBluetooth()
    abstract fun stopMonitoringBluetooth()
    abstract suspend fun isBluetoothEnabled(): Boolean
    abstract suspend fun requestBluetoothEnable()

    fun bluetoothEnabled() = stateRepo.launchTakeAndChangeState(remainIfStateNot = Disabled::class) {
        it.enable
    }

    fun bluetoothDisabled() = stateRepo.launchTakeAndChangeState(remainIfStateNot = Enabled::class) {
        it.disable
    }

    internal fun handleDeviceDiscovered(identifier: Identifier, rssi: Int, advertisementData: AdvertisementData, deviceCreator: () -> Device) =
        stateRepo.launchTakeAndChangeState { state ->
            when (state) {
                is Enabled.Scanning -> {
                    state.discoverDevice(identifier, rssi, advertisementData, deviceCreator)
                }
                else -> {
                    state.logError(Error("Discovered Device while not scanning"))
                    state.remain()
                }
            }
        }
}

expect class Scanner : BaseScanner
