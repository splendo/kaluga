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
import com.splendo.kaluga.base.DefaultServiceMonitor
import com.splendo.kaluga.base.flow.filterOnlyImportant
import com.splendo.kaluga.bluetooth.BluetoothMonitor
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.AdvertisementData
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.scanner.ScanningState.Initialized
import com.splendo.kaluga.bluetooth.scanner.ScanningState.Initialized.Enabled
import com.splendo.kaluga.bluetooth.scanner.ScanningState.Initialized.NoBluetooth.Disabled
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionState
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermission
import com.splendo.kaluga.state.StateRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.launch

typealias EnableSensorAction = suspend () -> Boolean

abstract class BaseScanner constructor(
    internal val permissions: Permissions,
    private val connectionSettings: ConnectionSettings,
    protected val autoRequestPermission: Boolean,
    internal val autoEnableSensors: Boolean,
    internal val stateRepo: StateRepo<ScanningState, MutableStateFlow<ScanningState>>,
) : CoroutineScope by stateRepo {

    interface Builder {
        fun create(
            permissions: Permissions,
            connectionSettings: ConnectionSettings,
            autoRequestPermission: Boolean,
            autoEnableSensors: Boolean,
            scanningStateRepo: ScanningStateFlowRepo
        ): BaseScanner
    }

    abstract val isSupported: Boolean
    private val bluetoothPermissionRepo get() = permissions[BluetoothPermission]
    protected abstract val bluetoothEnabledMonitor: DefaultServiceMonitor?

    private val _monitoringPermissionsJob = AtomicReference<Job?>(null)
    private var monitoringPermissionsJob: Job?
        get() = _monitoringPermissionsJob.get()
        set(value) { _monitoringPermissionsJob.set(value) }

    private val _monitoringBluetoothEnabledJob = AtomicReference<Job?>(null)
    private var monitoringBluetoothEnabledJob: Job?
        get() = _monitoringBluetoothEnabledJob.get()
        set(value) { _monitoringBluetoothEnabledJob.set(value) }

    open fun startMonitoringPermissions() {
        if (monitoringPermissionsJob != null) return
        monitoringPermissionsJob = launch(stateRepo.coroutineContext) {
            bluetoothPermissionRepo.collect { state ->
                handlePermissionState(state, BluetoothPermission)
            }
        }
    }

    protected suspend fun <P : Permission> handlePermissionState(state: PermissionState<P>, permission: P) {
        when (state) {
            is PermissionState.Denied.Requestable -> if (autoRequestPermission) state.request(permissions.getManager(permission))
            else -> {}
        }
        stateRepo.takeAndChangeState { scanState ->
            when (scanState) {
                is Disabled, is Enabled -> {
                    if (isPermitted())
                        scanState.remain()
                    else
                        (scanState as? Initialized)?.revokePermission ?: scanState.remain()
                }
                is Initialized.NoBluetooth.MissingPermissions -> if (isPermitted()) scanState.permit(areSensorsEnabled()) else scanState.remain()
                else -> { scanState.remain() }
            }
        }
    }

    open fun stopMonitoringPermissions() {
        monitoringPermissionsJob?.cancel()
        monitoringPermissionsJob = null
    }

    internal open suspend fun isPermitted(): Boolean {
        return bluetoothPermissionRepo.filterOnlyImportant().first() is PermissionState.Allowed
    }

    abstract suspend fun scanForDevices(filter: Set<UUID>)
    abstract suspend fun stopScanning()
    open fun startMonitoringSensors() {
        val bluetoothEnabledMonitor = bluetoothEnabledMonitor ?: return
        bluetoothEnabledMonitor.startMonitoring()
        if (monitoringBluetoothEnabledJob != null) return
        monitoringBluetoothEnabledJob = launch {
            bluetoothEnabledMonitor.stateFlow.collect {
                checkSensorsEnabledChanged()
            }
        }
    }
    open fun stopMonitoringSensors() {
        val bluetoothEnabledMonitor = bluetoothEnabledMonitor ?: return
        bluetoothEnabledMonitor.stopMonitoring()
        monitoringBluetoothEnabledJob?.cancel()
        monitoringBluetoothEnabledJob = null
    }
    open suspend fun areSensorsEnabled(): Boolean = bluetoothEnabledMonitor?.stateFlow is Enabled
    suspend fun requestSensorsEnable() {
        val actions = generateEnableSensorsActions()
        if (actions.isEmpty()) {
            checkSensorsEnabledChanged()
        } else if (
            flowOf(*actions.toTypedArray()).fold(true) { acc, action ->
                acc && action()
            }
        ) {
            requestSensorsEnable()
        }
    }
    abstract fun generateEnableSensorsActions(): List<EnableSensorAction>

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

    internal open suspend fun checkSensorsEnabledChanged() {
        when {
            areSensorsEnabled() -> bluetoothEnabled()
            else -> bluetoothDisabled()
        }
    }
}

expect class Scanner : BaseScanner
