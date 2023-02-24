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
import co.touchlab.stately.concurrency.value
import com.splendo.kaluga.base.flow.filterOnlyImportant
import com.splendo.kaluga.bluetooth.BluetoothMonitor
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.AdvertisementData
import com.splendo.kaluga.bluetooth.device.BaseAdvertisementData
import com.splendo.kaluga.bluetooth.device.BaseDeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.DeviceWrapper
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.device.description
import com.splendo.kaluga.bluetooth.device.stringValue
import com.splendo.kaluga.bluetooth.uuidString
import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.RestrictedLogLevel
import com.splendo.kaluga.logging.RestrictedLogger
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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

typealias EnableSensorAction = suspend () -> Boolean
typealias DeviceCreator = () -> Pair<DeviceWrapper, BaseDeviceConnectionManager.Builder>

interface Scanner {

    sealed class Event {
        data class PermissionChanged(val hasPermission: Boolean) : Event()
        object BluetoothEnabled : Event()
        object BluetoothDisabled : Event()
        object FailedScanning : Event()
        data class DeviceDiscovered(
            val identifier: Identifier,
            val rssi: Int,
            val advertisementData: BaseAdvertisementData,
            val deviceCreator: DeviceCreator
        ) : Event()
        data class PairedDevicesRetrieved(
            val filter: Filter,
            val devices: List<DeviceDiscovered>
        ) : Event() {
            val identifiers = devices.map(DeviceDiscovered::identifier)
            val deviceCreators = devices.map(DeviceDiscovered::deviceCreator)
        }
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
    suspend fun retrievePairedDevices(withServices: Set<UUID>)
}

abstract class BaseScanner constructor(
    settings: Settings,
    private val coroutineScope: CoroutineScope
) : Scanner, CoroutineScope by coroutineScope {

    companion object {
        private const val LOG_TAG = "Bluetooth Scanner"
    }

    data class Settings(
        val permissions: Permissions,
        val autoRequestPermission: Boolean = true,
        val autoEnableSensors: Boolean = true,
        val logger: Logger = RestrictedLogger(RestrictedLogLevel.None),
        /** If set, will include bonded devices in discovered result list on Android */
        val discoverBondedDevices: Boolean = true,
    )

    interface Builder {
        fun create(
            settings: Settings,
            coroutineScope: CoroutineScope
        ): BaseScanner
    }

    private val logger = settings.logger

    internal val permissions: Permissions = settings.permissions
    private val autoRequestPermission: Boolean = settings.autoRequestPermission
    private val autoEnableSensors: Boolean = settings.autoEnableSensors

    protected val eventChannel = Channel<Scanner.Event>(UNLIMITED)
    override val events: Flow<Scanner.Event> = eventChannel.receiveAsFlow()

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

    private val isRetrievingPairedDevicesMutex = Mutex()
    private val isRetrievingPairedDevicesFilter = AtomicReference<Set<UUID>?>(null)
    private val retrievingPairedDevicesJob = AtomicReference<Job?>(null)

    override fun startMonitoringPermissions() {
        logger.debug(LOG_TAG) { "Start monitoring permissions" }
        if (monitoringPermissionsJob != null) return
        monitoringPermissionsJob = launch(coroutineContext) {
            permissionsFlow.collect { state ->
                handlePermissionState(state)
            }
        }
    }

    private fun handlePermissionState(states: List<PermissionState<*>>) {
        if (autoRequestPermission) {
            states.forEach { state ->
                when (state) {
                    is PermissionState.Denied.Requestable -> {
                        logger.info(LOG_TAG) { "Request permission" }
                        state.request()
                    }
                    else -> {}
                }
            }
        }
        val hasPermission = states.all { it is PermissionState.Allowed }
        logger.info(LOG_TAG) { "Permission now ${if (hasPermission) "Granted" else "Denied"}" }
        emitEvent(Scanner.Event.PermissionChanged(hasPermission))
    }

    override fun stopMonitoringPermissions() {
        monitoringPermissionsJob?.cancel()
        monitoringPermissionsJob = null
    }

    final override suspend fun scanForDevices(filter: Set<UUID>) {
        if (filter.isEmpty()) {
            logger.info(LOG_TAG) { "Start Scanning" }
        } else {
            logger.info(LOG_TAG) { "Start scanning with filter [${filter.joinToString(", ") { it.uuidString }}]" }
        }
        didStartScanning(filter)
    }

    protected abstract suspend fun didStartScanning(filter: Set<UUID>)

    final override suspend fun stopScanning() {
        logger.info(LOG_TAG) { "Stop scanning" }
        didStopScanning()
    }

    protected abstract suspend fun didStopScanning()

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
            logger.debug(LOG_TAG) { "Request Enable Hardware: ${if (isEnabled) "Enabled" else "Disabled"}" }
            emitEvent(if (isEnabled) Scanner.Event.BluetoothEnabled else Scanner.Event.BluetoothDisabled)
        } else if (
            flowOf(*actions.toTypedArray()).fold(true) { acc, action ->
                logger.debug(LOG_TAG) { "Request Enable Hardware awaiting action" }
                acc && action()
            }
        ) {
            requestEnableHardware()
        }
    }

    override suspend fun retrievePairedDevices(withServices: Set<UUID>) {
        if (!checkIfNewPairingDiscoveryShouldBeStarted(withServices)) return

        retrievingPairedDevicesJob.compareAndSet(
            null,
            this@BaseScanner.launch {
                // We have to call even with empty list to clean up cached devices
                val devices = retrievePairedDeviceDiscoveredEvents(withServices)
                handlePairedDevices(withServices, devices)
            }
        )
    }

    private suspend fun checkIfNewPairingDiscoveryShouldBeStarted(withServices: Set<UUID>): Boolean = isRetrievingPairedDevicesMutex.withLock {
        when (isRetrievingPairedDevicesFilter.value) {
            withServices -> false
            null -> true
            else -> {
                retrievingPairedDevicesJob.value?.cancel()
                retrievingPairedDevicesJob.value = null
                true
            }
        }.also {
            isRetrievingPairedDevicesFilter.value = withServices
        }
    }

    protected abstract suspend fun retrievePairedDeviceDiscoveredEvents(withServices: Set<UUID>): List<Scanner.Event.DeviceDiscovered>

    internal fun handleDeviceDiscovered(
        identifier: Identifier,
        rssi: Int,
        advertisementData: AdvertisementData,
        deviceCreator: DeviceCreator
    ) {
        logger.info(LOG_TAG) { "Device ${identifier.stringValue} discovered with rssi: $rssi" }
        logger.debug(LOG_TAG) { "Device ${identifier.stringValue} discovered with advertisement data:\n ${advertisementData.description}" }
        emitEvent(Scanner.Event.DeviceDiscovered(identifier, rssi, advertisementData, deviceCreator))
    }

    internal suspend fun handlePairedDevices(filter: Filter, devices: List<Scanner.Event.DeviceDiscovered>) {
        // Only update if actually scanning for this Filter
        isRetrievingPairedDevicesMutex.withLock {
            if (isRetrievingPairedDevicesFilter.compareAndSet(filter, null)) {
                logger.info(LOG_TAG) {
                    val identifiers = devices.map(Scanner.Event.DeviceDiscovered::identifier)
                    "Paired Devices retrieved: $identifiers for filter: $filter"
                }
                emitEvent(Scanner.Event.PairedDevicesRetrieved(filter, devices))
                retrievingPairedDevicesJob.value = null
            }
        }
    }

    internal fun handleDeviceConnected(identifier: Identifier) {
        logger.debug(LOG_TAG) { "Device ${identifier.stringValue} connected" }
        emitEvent(Scanner.Event.DeviceConnected(identifier))
    }

    internal fun handleDeviceDisconnected(identifier: Identifier) {
        logger.debug(LOG_TAG) { "Device ${identifier.stringValue} disconnected" }
        emitEvent(Scanner.Event.DeviceDisconnected(identifier))
    }

    internal open suspend fun checkHardwareEnabledChanged() {
        val isEnabled = isHardwareEnabled()
        logger.info(LOG_TAG) { "Bluetooth hardware now ${if (isEnabled) "enabled" else "disabled"}" }
        if (isEnabled)
            emitEvent(Scanner.Event.BluetoothEnabled)
        else {
            emitEvent(Scanner.Event.BluetoothDisabled)
            if (autoEnableSensors) {
                logger.info(LOG_TAG) { "Bluetooth disabled. Attempt to automatically enable" }
                requestEnableHardware()
            }
        }
    }

    private fun emitEvent(event: Scanner.Event) {
        // Channel has unlimited buffer so this will never fail due to capacity
        eventChannel.trySend(event)
    }
}

expect class DefaultScanner : BaseScanner
