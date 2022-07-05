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
import com.splendo.kaluga.bluetooth.device.description
import com.splendo.kaluga.bluetooth.device.stringValue
import com.splendo.kaluga.bluetooth.uuidString
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.logging.info
import com.splendo.kaluga.permissions.base.PermissionState
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
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
    private val settings: Settings,
    private val coroutineScope: CoroutineScope
) : Scanner, CoroutineScope by coroutineScope {

    companion object {
        private const val LOG_TAG = "Bluetooth Scanner"
        const val DEFAULT_EVENT_BUFFER_SIZE = 256
    }

    data class Settings(
        val permissions: Permissions,
        val autoRequestPermission: Boolean = true,
        val autoEnableSensors: Boolean = true,
        val eventBufferSize: Int = DEFAULT_EVENT_BUFFER_SIZE,
        val logLevel: LogLevel = LogLevel.NONE
    )

    enum class LogLevel {
        NONE,
        INFO,
        VERBOSE
    }

    interface Builder {
        fun create(
            settings: Settings,
            coroutineScope: CoroutineScope
        ): BaseScanner
    }

    internal val permissions: Permissions = settings.permissions
    protected val autoRequestPermission: Boolean = settings.autoRequestPermission
    internal val autoEnableSensors: Boolean = settings.autoEnableSensors

    protected val sharedEvents = Channel<Scanner.Event>(UNLIMITED)
    override val events: Flow<Scanner.Event> = sharedEvents.receiveAsFlow()

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
        logDebug { "Start monitoring permissions" }
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
                    is PermissionState.Denied.Requestable -> {
                        logInfo { "Request permission" }
                        state.request()
                    }
                    else -> {}
                }
            }
        }
        val hasPermission = states.all { it is PermissionState.Allowed }
        logInfo { "Permission now ${if (hasPermission) "Granted" else "Denied"}" }
        emitSharedEvent(Scanner.Event.PermissionChanged(hasPermission))
    }

    override fun stopMonitoringPermissions() {
        monitoringPermissionsJob?.cancel()
        monitoringPermissionsJob = null
    }

    override suspend fun scanForDevices(filter: Set<UUID>) {
        if (filter.isEmpty()) {
            logInfo { "Start Scanning" }
        } else {
            logInfo { "Start scanning with filter [${filter.joinToString(", ") { it.uuidString }}]" }
        }
    }

    override suspend fun stopScanning() = logInfo { "Stop scanning" }

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
            val isEnabled = isHardwareEnabled()
            logDebug { "Request Enable Hardware: ${if (isEnabled) "Enabled" else "Disabled"}" }
            emitSharedEvent(if (isEnabled) Scanner.Event.BluetoothEnabled else Scanner.Event.BluetoothDisabled)
        } else if (
            flowOf(*actions.toTypedArray()).fold(true) { acc, action ->
                logDebug { "Request Enable Hardware awaiting action" }
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
    ) {
        logInfo { "Device ${identifier.stringValue} discovered with rssi: $rssi" }
        logDebug { "Device ${identifier.stringValue} discovered with advertisement data:\n ${advertisementData.description}" }
        emitSharedEvent(Scanner.Event.DeviceDiscovered(identifier, rssi, advertisementData, deviceCreator))
    }

    internal fun handleDeviceConnected(identifier: Identifier) {
        logDebug { "Device ${identifier.stringValue} connected" }
        emitSharedEvent(Scanner.Event.DeviceConnected(identifier))
    }

    internal fun handleDeviceDisconnected(identifier: Identifier) {
        logDebug { "Device ${identifier.stringValue} disconnected" }
        emitSharedEvent(Scanner.Event.DeviceDisconnected(identifier))
    }

    internal open suspend fun checkHardwareEnabledChanged() {
        val isEnabled = isHardwareEnabled()
        logInfo { "Bluetooth hardware now ${if (isEnabled) "enabled" else "disabled"}" }
        if (isEnabled)
            emitSharedEvent(Scanner.Event.BluetoothEnabled)
        else {
            emitSharedEvent(Scanner.Event.BluetoothDisabled)
            if (autoEnableSensors) {
                logInfo { "Bluetooth disabled. Attempt to automatically enable" }
                requestEnableHardware()
            }
        }
    }

    private fun emitSharedEvent(event: Scanner.Event) {
        // Channel has unlimited buffer so this will never fail due to capacity
        sharedEvents.trySend(event)
    }

    protected fun logInfo(message: () -> String) {
        if (settings.logLevel != LogLevel.NONE) {
            info(LOG_TAG, message)
        }
    }

    protected fun logDebug(message: () -> String) {
        if (settings.logLevel == LogLevel.VERBOSE) {
            debug(LOG_TAG, message)
        }
    }

    protected fun logError(message: () -> String) {
        if (settings.logLevel == LogLevel.VERBOSE) {
            com.splendo.kaluga.logging.error(LOG_TAG, message)
        }
    }
}

expect class DefaultScanner : BaseScanner
