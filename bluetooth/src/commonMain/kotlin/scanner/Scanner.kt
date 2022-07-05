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
import com.splendo.kaluga.bluetooth.BluetoothMonitor
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.AdvertisementData
import com.splendo.kaluga.bluetooth.device.BaseDeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.DeviceWrapper
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.permissions.base.PermissionState
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

typealias EnableSensorAction = suspend () -> Boolean

interface Scanner {

    sealed class Event {
        data class PermissionChanged(val hasPermission: Boolean) : Event()
        object BluetoothEnabled : Event()
        object BluetoothDisabled : Event()
        object FailedScanning : Event()
        data class DeviceDiscovered(
            val identifier: Identifier,
            val rssi: Int,
            val advertisementData: AdvertisementData,
            val deviceCreator: () -> (Pair<DeviceWrapper, BaseDeviceConnectionManager.Builder>)
        ) : Event()
        data class DeviceConnected(val identifier: Identifier) : Event()
        data class DeviceDisconnected(val identifier: Identifier) : Event()
    }

    val isSupported: Boolean
    val events: Flow<Event>
    fun startMonitoringPermissions()
    fun stopMonitoringPermissions()
    suspend fun scanForDevices(filter: Set<UUID>)
    suspend fun stopScanning()
    fun startMonitoringHardwareEnabled()
    fun stopMonitoringHardwareEnabled()
    suspend fun isHardwareEnabled(): Boolean
    suspend fun requestEnableHardware()
    fun generateEnableSensorsActions(): List<EnableSensorAction>
    fun pairedDevices(withServices: Set<UUID>): List<Identifier>
}

abstract class BaseScanner constructor(
    settings: Settings,
    private val coroutineScope: CoroutineScope
) : Scanner, CoroutineScope by coroutineScope {

    companion object {
        const val DEFAULT_EVENT_BUFFER_SIZE = 256
    }

    data class Settings(
        val permissions: Permissions,
        val autoRequestPermission: Boolean = true,
        val autoEnableSensors: Boolean = true,
        val eventBufferSize: Int = DEFAULT_EVENT_BUFFER_SIZE
    )

    interface Builder {
        fun create(
            settings: Settings,
            coroutineScope: CoroutineScope
        ): BaseScanner
    }

    internal val permissions: Permissions = settings.permissions
    protected val autoRequestPermission: Boolean = settings.autoRequestPermission
    internal val autoEnableSensors: Boolean = settings.autoEnableSensors

    protected val sharedEvents = Channel<Scanner.Event>(Channel.UNLIMITED)
    override val events: Flow<Scanner.Event> = sharedEvents.consumeAsFlow()

    protected val bluetoothPermissionRepo get() = permissions[BluetoothPermission]
    protected abstract val bluetoothEnabledMonitor: BluetoothMonitor?

    protected open val permissionsFlow: Flow<List<PermissionState<*>>> get() = bluetoothPermissionRepo.filterOnlyImportant().map { listOf(it) }
    protected open val enabledFlow: Flow<List<Boolean>> get() = (bluetoothEnabledMonitor?.isEnabled ?: flowOf(false)).map { listOf(it) }

    private val _monitoringPermissionsJob = AtomicReference<Job?>(null)
    private var monitoringPermissionsJob: Job?
        get() = _monitoringPermissionsJob.get()
        set(value) { _monitoringPermissionsJob.set(value) }

    private val _monitoringBluetoothEnabledJob = AtomicReference<Job?>(null)
    private var monitoringBluetoothEnabledJob: Job?
        get() = _monitoringBluetoothEnabledJob.get()
        set(value) { _monitoringBluetoothEnabledJob.set(value) }

    override fun startMonitoringPermissions() {
        if (monitoringPermissionsJob != null) return
        monitoringPermissionsJob = launch(coroutineContext) {
            permissionsFlow.collect { state ->
                handlePermissionState(state)
            }
        }
    }

    private suspend fun handlePermissionState(states: List<PermissionState<*>>) {
        if (autoRequestPermission) {
            states.forEach { state ->
                when (state) {
                    is PermissionState.Denied.Requestable -> state.request()
                    else -> {}
                }
            }
        }
        val hasPermission = states.all { it is PermissionState.Allowed }
        sharedEvents.trySend(Scanner.Event.PermissionChanged(hasPermission))
    }

    override fun stopMonitoringPermissions() {
        monitoringPermissionsJob?.cancel()
        monitoringPermissionsJob = null
    }

    override fun startMonitoringHardwareEnabled() {
        val bluetoothEnabledMonitor = bluetoothEnabledMonitor ?: return
        bluetoothEnabledMonitor.startMonitoring()
        if (monitoringBluetoothEnabledJob != null) return
        monitoringBluetoothEnabledJob = launch {
            enabledFlow.collect {
                checkHardwareEnabledChanged()
            }
        }
    }

    override fun stopMonitoringHardwareEnabled() {
        val bluetoothEnabledMonitor = bluetoothEnabledMonitor ?: return
        bluetoothEnabledMonitor.stopMonitoring()
        monitoringBluetoothEnabledJob?.cancel()
        monitoringBluetoothEnabledJob = null
    }

    override suspend fun isHardwareEnabled(): Boolean = bluetoothEnabledMonitor?.isServiceEnabled ?: false
    override suspend fun requestEnableHardware() {
        val actions = generateEnableSensorsActions()
        if (actions.isEmpty()) {
            sharedEvents.trySend(if (isHardwareEnabled()) Scanner.Event.BluetoothEnabled else Scanner.Event.BluetoothDisabled)
        } else if (
            flowOf(*actions.toTypedArray()).fold(true) { acc, action ->
                acc && action()
            }
        ) {
            requestEnableHardware()
        }
    }

    internal fun handleDeviceDiscovered(
        identifier: Identifier,
        rssi: Int,
        advertisementData: AdvertisementData,
        deviceCreator: () -> Pair<DeviceWrapper, BaseDeviceConnectionManager.Builder>
    ) = sharedEvents.trySend(Scanner.Event.DeviceDiscovered(identifier, rssi, advertisementData, deviceCreator))
    abstract override fun pairedDevices(withServices: Set<UUID>): List<Identifier>

    internal fun handleDeviceConnected(identifier: Identifier) = sharedEvents.trySend(Scanner.Event.DeviceConnected(identifier))
    internal fun handleDeviceDisconnected(identifier: Identifier) = sharedEvents.trySend(Scanner.Event.DeviceDisconnected(identifier))

    internal open suspend fun checkHardwareEnabledChanged() {
        if (isHardwareEnabled())
            sharedEvents.trySend(Scanner.Event.BluetoothEnabled)
        else {
            sharedEvents.trySend(Scanner.Event.BluetoothDisabled)
            if (autoEnableSensors) {
                requestEnableHardware()
            }
        }
    }
}

expect class DefaultScanner : BaseScanner
