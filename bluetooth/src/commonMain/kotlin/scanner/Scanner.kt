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

import com.splendo.kaluga.base.flow.filterOnlyImportant
import com.splendo.kaluga.bluetooth.BluetoothMonitor
import com.splendo.kaluga.bluetooth.RSSI
import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.AdvertisementData
import com.splendo.kaluga.bluetooth.device.BaseAdvertisementData
import com.splendo.kaluga.bluetooth.device.BaseDeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.Device
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

/**
 * Creates a [DeviceWrapper] and [BaseDeviceConnectionManager.Builder] for a discovered device
 */
typealias DeviceCreator = () -> Pair<DeviceWrapper, BaseDeviceConnectionManager.Builder>

/**
 * Scans for Bluetooth [com.splendo.kaluga.bluetooth.device.Device]
 */
interface Scanner {

    /**
     * Events detected by a [Scanner]
     */
    sealed class Event {

        /**
         * An [Event] indicating permissions have changed
         * @property hasPermission if `true` the permissions required for Bluetooth have been granted
         */
        data class PermissionChanged(val hasPermission: Boolean) : Event()

        /**
         * An [Event] indicating the Bluetooth service has become enabled
         */
        object BluetoothEnabled : Event()

        /**
         * An [Event] indicating the Bluetooth service has become disabled
         */
        object BluetoothDisabled : Event()

        /**
         * An [Event] indicating the Scanner failed to start scanning
         */
        object FailedScanning : Event()

        /**
         * An [Event] indicating a [com.splendo.kaluga.bluetooth.device.Device] was discovered
         * @property identifier the [Identifier] of the device discovered
         * @property rssi the [RSSI] value of the device discovered
         * @property advertisementData the [BaseAdvertisementData] of the device discovered
         * @property deviceCreator method for creating a device if it had not yet been discovered.
         */
        data class DeviceDiscovered(
            val identifier: Identifier,
            val rssi: RSSI,
            val advertisementData: BaseAdvertisementData,
            val deviceCreator: DeviceCreator
        ) : Event()

        /**
         * An [Event] indicating a list of [DeviceDiscovered] events are paired to the system
         * @property filter the set of [UUID] applied to filter for the paired devices
         * @property devices the list of [DeviceDiscovered] paired to the system
         */
        data class PairedDevicesRetrieved(
            val filter: Filter,
            val devices: List<DeviceDiscovered>
        ) : Event() {

            /**
             * The list of [Identifier] discovered
             */
            val identifiers = devices.map(DeviceDiscovered::identifier)

            /**
             * The list of [DeviceCreator] to create devices that have not yet been discovered
             */
            val deviceCreators = devices.map(DeviceDiscovered::deviceCreator)
        }

        /**
         * An [Event] indicating a [com.splendo.kaluga.bluetooth.device.Device] has changed to a connected state.
         * @property identifier the [Identifier] of the device connected
         */
        data class DeviceConnected(val identifier: Identifier) : Event()

        /**
         * An [Event] indicating a [com.splendo.kaluga.bluetooth.device.Device] has changed to a disconnected state.
         * @property identifier the [Identifier] of the device disconnected
         */
        data class DeviceDisconnected(val identifier: Identifier) : Event()
    }

    /**
     * Indicates whether the system supports Bluetooth scanning
     */
    val isSupported: Boolean

    /**
     * The [Flow] of all the [Event] detected by the scanner
     */
    val events: Flow<Event>

    /**
     * Starts scanning for changes to permissions related to Bluetooth.
     * This will result in [Event.PermissionChanged] on the [events] flow.
     */
    suspend fun startMonitoringPermissions()

    /**
     * Stops scanning for changes to permissions related to Bluetooth.
     */
    suspend fun stopMonitoringPermissions()

    /**
     * Starts scanning for devices.
     * This will result in [Event.DeviceDiscovered] or [Event.FailedScanning]
     * @param filter if not empty, only [Device] that have at least one [Service] matching one of the [UUID] will be scanned.
     */
    suspend fun scanForDevices(filter: Set<UUID>)

    /**
     * Stops scanning for devices
     */
    suspend fun stopScanning()

    /**
     * Starts scanning for changes to the Bluetooth service being enabled
     * This will result in [Event.BluetoothEnabled] or [Event.BluetoothDisabled] on the [events] flow
     */
    suspend fun startMonitoringHardwareEnabled()

    /**
     * Stops scanning for changes to the Bluetooth service being enabled
     */
    suspend fun stopMonitoringHardwareEnabled()

    /**
     * Checks whether the Bluetooth service  is currently enabled
     */
    suspend fun isHardwareEnabled(): Boolean

    /**
     * Attempts to request the user to enable Bluetooth
     */
    suspend fun requestEnableHardware()

    /**
     * Retrieves the list of paired [Device]
     * This will result in [Event.PairedDevicesRetrieved] on the [events] flow
     * @param withServices filters the list to only return the [Device] that at least one [Service] matching one of the provided [UUID]
     */
    suspend fun retrievePairedDevices(withServices: Set<UUID>)
}

/**
 * An abstract implementation for [Scanner]
 * @param settings the [Settings] to configure this scanner
 * @param coroutineScope the [CoroutineScope] this scanner runs on
 */
abstract class BaseScanner constructor(
    settings: Settings,
    private val coroutineScope: CoroutineScope
) : Scanner, CoroutineScope by coroutineScope {

    companion object {
        private const val LOG_TAG = "Bluetooth Scanner"
    }

    /**
     * Settings to configure a [BaseScanner]
     * @property permissions the [Permissions] to manage the bluetooth related permissions
     * @property autoRequestPermission if `true` the scanner should automatically request permissions if not granted
     * @property autoEnableSensors if `true` the scanner should automatically enable the Bluetooth service if disabled
     * @property discoverBondedDevices If `true` scanned results will include devices bonded to the system.
     * @property logger the [Logger] to log to
     */
    data class Settings(
        val permissions: Permissions,
        val autoRequestPermission: Boolean = true,
        val autoEnableSensors: Boolean = true,
        val discoverBondedDevices: Boolean = true,
        val logger: Logger = RestrictedLogger(RestrictedLogLevel.None)
    )

    /**
     * Builder for creating a [BaseScanner]
     */
    interface Builder {

        /**
         * Creates a [BaseScanner]
         * @param settings the [BaseScanner.Settings] to configure the scanner with
         * @param coroutineScope the [CoroutineScope] the scanner will run in
         * @return the [BaseScanner] created
         */
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

    private val permissionsLock = Mutex()
    private var monitoringPermissionsJob: Job? = null
    private val enabledJob = Mutex()
    private var monitoringBluetoothEnabledJob: Job? = null

    override suspend fun startMonitoringPermissions() = permissionsLock.withLock {
        logger.debug(LOG_TAG) { "Start monitoring permissions" }
        if (monitoringPermissionsJob != null) return
        monitoringPermissionsJob = coroutineScope.launch(coroutineContext) {
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

    override suspend fun stopMonitoringPermissions() = permissionsLock.withLock {
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

    override suspend fun startMonitoringHardwareEnabled() = enabledJob.withLock {
        val bluetoothEnabledMonitor = bluetoothEnabledMonitor ?: return
        bluetoothEnabledMonitor.startMonitoring()
        if (monitoringBluetoothEnabledJob != null) return
        monitoringBluetoothEnabledJob = coroutineScope.launch {
            enabledFlow.collect {
                checkHardwareEnabledChanged()
            }
        }
    }

    override suspend fun stopMonitoringHardwareEnabled() = enabledJob.withLock {
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

    protected abstract fun generateEnableSensorsActions(): List<EnableSensorAction>

    internal fun handleDeviceDiscovered(
        identifier: Identifier,
        rssi: RSSI,
        advertisementData: AdvertisementData,
        deviceCreator: DeviceCreator
    ) {
        logger.info(LOG_TAG) { "Device ${identifier.stringValue} discovered with rssi: $rssi" }
        logger.debug(LOG_TAG) { "Device ${identifier.stringValue} discovered with advertisement data:\n ${advertisementData.description}" }
        emitEvent(Scanner.Event.DeviceDiscovered(identifier, rssi, advertisementData, deviceCreator))
    }

    internal fun handlePairedDevices(filter: Filter, devices: List<Scanner.Event.DeviceDiscovered>) {
        logger.info(LOG_TAG) {
            val identifiers = devices.map(Scanner.Event.DeviceDiscovered::identifier)
            "Paired Devices retrieved: $identifiers for filter: $filter"
        }
        emitEvent(Scanner.Event.PairedDevicesRetrieved(filter, devices))
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
        if (isEnabled) {
            emitEvent(Scanner.Event.BluetoothEnabled)
        } else {
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

/**
 * A default implementation of [BaseScanner]
 */
expect class DefaultScanner : BaseScanner
