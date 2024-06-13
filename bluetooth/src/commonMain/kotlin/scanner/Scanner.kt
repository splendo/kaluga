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
import com.splendo.kaluga.base.singleThreadDispatcher
import com.splendo.kaluga.base.utils.BufferedAsListChannel
import com.splendo.kaluga.bluetooth.BluetoothMonitor
import com.splendo.kaluga.bluetooth.RSSI
import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.BaseAdvertisementData
import com.splendo.kaluga.bluetooth.device.ConnectableDeviceStateImplRepo
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.DeviceImpl
import com.splendo.kaluga.bluetooth.device.DeviceInfoImpl
import com.splendo.kaluga.bluetooth.device.DeviceWrapper
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.device.description
import com.splendo.kaluga.bluetooth.device.stringValue
import com.splendo.kaluga.bluetooth.scanner.BaseScanner.Settings
import com.splendo.kaluga.bluetooth.uuidString
import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.RestrictedLogLevel
import com.splendo.kaluga.logging.RestrictedLogger
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.logging.info
import com.splendo.kaluga.permissions.base.PermissionState
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermission
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

typealias EnableSensorAction = suspend () -> Boolean

internal val scanningDispatcher: CoroutineDispatcher by lazy {
    singleThreadDispatcher("Scanning for Devices")
}

/**
 * Scans for Bluetooth [com.splendo.kaluga.bluetooth.device.Device]
 */
interface Scanner {

    /**
     * An indication a [com.splendo.kaluga.bluetooth.device.Device] was discovered
     * @property identifier the [Identifier] of the device discovered
     * @property rssi the [RSSI] value of the device discovered
     * @property advertisementData the [BaseAdvertisementData] of the device discovered
     * @property deviceCreator method for creating a device if it had not yet been discovered.
     */
    data class DeviceDiscovered(
        val identifier: Identifier,
        val rssi: Int,
        val advertisementData: BaseAdvertisementData,
        val deviceCreator: (CoroutineContext) -> Device,
    )

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
        data object BluetoothEnabled : Event()

        /**
         * An [Event] indicating the Bluetooth service has become disabled
         */
        data object BluetoothDisabled : Event()

        /**
         * An [Event] indicating the Scanner failed to start scanning
         */
        data object FailedScanning : Event()

        /**
         * An [Event] indicating a list of [DeviceDiscovered] events are paired to the system
         * @property filter the set of [UUID] applied to filter for the paired devices
         * @property devices the list of [DeviceDiscovered] paired to the system
         */
        data class PairedDevicesRetrieved(
            val devices: List<DeviceDiscovered>,
            val filter: Filter,
            val removeForAllPairedFilters: Boolean,
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
    }

    /**
     * Events detected by the a Bluetooth Connection observer
     */
    sealed class ConnectionEvent {
        /**
         * A [ConnectionEvent] indicating a [com.splendo.kaluga.bluetooth.device.Device] has changed to a connected state.
         * @property identifier the [Identifier] of the device connected
         */
        data class DeviceConnected(val identifier: Identifier) : ConnectionEvent()

        /**
         * A [ConnectionEvent] indicating a [com.splendo.kaluga.bluetooth.device.Device] has changed to a disconnected state.
         * @property identifier the [Identifier] of the device disconnected
         */
        data class DeviceDisconnected(val identifier: Identifier) : ConnectionEvent()
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
     * The [Flow] of all the [DeviceDiscovered] detected by the scanner.
     * These are grouped in a list until collected to account for high volumes of events
     */
    val discoveryEvents: Flow<List<DeviceDiscovered>>

    /**
     * The [Flow] of all the [ConnectionEvent] detected by the scanner
     */
    val connectionEvents: Flow<ConnectionEvent>

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
    suspend fun scanForDevices(filter: Filter, connectionSettings: ConnectionSettings?)

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
     * @param removeForAllPairedFilters if `true` the list of paired devices for all filters will be emptied
     * @param connectionSettings the [ConnectionSettings] to apply to the paired devices found. If `null` the default will be used
     */
    suspend fun retrievePairedDevices(withServices: Filter, removeForAllPairedFilters: Boolean, connectionSettings: ConnectionSettings?)

    fun cancelRetrievingPairedDevices()
}

/**
 * An abstract implementation for [Scanner]
 * @param settings the [Settings] to configure this scanner
 * @param coroutineScope the [CoroutineScope] this scanner runs on
 * @param scanningDispatcher the [CoroutineDispatcher] to which scanning should be dispatched. It is recommended to make this a dispatcher that can handle high frequency of events
 */
abstract class BaseScanner constructor(
    settings: Settings,
    private val coroutineScope: CoroutineScope,
    private val scanningDispatcher: CoroutineDispatcher = com.splendo.kaluga.bluetooth.scanner.scanningDispatcher,
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
     * @param defaultConnectionSettings The [ConnectionSettings] to apply for scanned devices if no settings are provided in [Scanner.scanForDevices]
     * @property logger the [Logger] to log to
     */
    data class Settings(
        val permissions: Permissions,
        val autoRequestPermission: Boolean = true,
        val autoEnableSensors: Boolean = true,
        val discoverBondedDevices: Boolean = true,
        val defaultConnectionSettings: ConnectionSettings = ConnectionSettings(),
        val logger: Logger = RestrictedLogger(RestrictedLogLevel.None),
    )

    /**
     * Builder for creating a [BaseScanner]
     */
    interface Builder {

        /**
         * Creates a [BaseScanner]
         * @param settings the [BaseScanner.Settings] to configure the scanner with
         * @param coroutineScope the [CoroutineScope] the scanner will run in
         * @param scanningDispatcher the [CoroutineDispatcher] to which scanning should be dispatched. It is recommended to make this a dispatcher that can handle high frequency of events
         * @return the [BaseScanner] created
         */
        fun create(
            settings: Settings,
            coroutineScope: CoroutineScope,
            scanningDispatcher: CoroutineDispatcher = com.splendo.kaluga.bluetooth.scanner.scanningDispatcher,
        ): BaseScanner
    }

    private val logger = settings.logger

    internal val permissions: Permissions = settings.permissions
    private val autoRequestPermission: Boolean = settings.autoRequestPermission
    private val autoEnableSensors: Boolean = settings.autoEnableSensors
    private val defaultConnectionSettings = settings.defaultConnectionSettings

    protected val eventChannel = Channel<Scanner.Event>(UNLIMITED)
    override val events: Flow<Scanner.Event> = eventChannel.receiveAsFlow()
    private val connectionEventChannel = Channel<Scanner.ConnectionEvent>(UNLIMITED)
    override val connectionEvents: Flow<Scanner.ConnectionEvent> = connectionEventChannel.receiveAsFlow()
    private val isScanningDevicesDiscovered = MutableStateFlow<BufferedAsListChannel<Scanner.DeviceDiscovered>?>(null)
    override val discoveryEvents: Flow<List<Scanner.DeviceDiscovered>> = isScanningDevicesDiscovered.flatMapLatest { it?.receiveAsFlow() ?: emptyFlow() }

    protected val bluetoothPermissionRepo get() = permissions[BluetoothPermission]
    protected abstract val bluetoothEnabledMonitor: BluetoothMonitor?

    protected open val permissionsFlow: Flow<List<PermissionState<*>>> get() = bluetoothPermissionRepo.filterOnlyImportant().map { listOf(it) }
    protected open val enabledFlow: Flow<List<Boolean>> get() = (bluetoothEnabledMonitor?.isEnabled ?: flowOf(false)).map { listOf(it) }

    private val permissionsLock = Mutex()
    private var monitoringPermissionsJob: Job? = null
    private val enabledJob = Mutex()
    private var monitoringBluetoothEnabledJob: Job? = null

    private val isScanningDevicesMutex = Mutex()
    private var isScanningDevicesFilter: Set<UUID>? = null

    private val isRetrievingPairedDevicesMutex = Mutex()
    private var isRetrievingPairedDevicesFilter: Set<UUID>? = null
    private var retrievingPairedDevicesJob: Job? = null

    private var currentConnectionSettings: ConnectionSettings? = null

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

    final override suspend fun scanForDevices(filter: Filter, connectionSettings: ConnectionSettings?) {
        if (filter.isEmpty()) {
            logger.info(LOG_TAG) { "Start Scanning" }
        } else {
            logger.info(LOG_TAG) { "Start scanning with filter [${filter.joinToString(", ") { it.uuidString }}]" }
        }
        isScanningDevicesMutex.withLock {
            isScanningDevicesDiscovered.value = BufferedAsListChannel(coroutineContext)
            isScanningDevicesFilter = filter
            currentConnectionSettings = connectionSettings
            withContext(coroutineContext + scanningDispatcher) {
                didStartScanning(filter)
            }
        }
    }

    protected abstract suspend fun didStartScanning(filter: Filter)

    final override suspend fun stopScanning() {
        logger.info(LOG_TAG) { "Stop scanning" }
        isScanningDevicesMutex.withLock {
            withContext(coroutineContext + scanningDispatcher) {
                didStopScanning()
            }
            isScanningDevicesDiscovered.value = null
            isScanningDevicesFilter = null
            currentConnectionSettings = null
        }
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

    override suspend fun retrievePairedDevices(withServices: Filter, removeForAllPairedFilters: Boolean, connectionSettings: ConnectionSettings?) =
        isRetrievingPairedDevicesMutex.withLock {
            if (!checkIfNewPairingDiscoveryShouldBeStarted(withServices)) return
            retrievingPairedDevicesJob = this@BaseScanner.launch {
                // We have to call even with empty list to clean up cached devices
                val devices = retrievePairedDeviceDiscoveredEvents(withServices, connectionSettings)
                handlePairedDevices(devices, withServices, removeForAllPairedFilters)
            }
        }

    override fun cancelRetrievingPairedDevices() {
        retrievingPairedDevicesJob?.cancel()
        retrievingPairedDevicesJob = null
        isRetrievingPairedDevicesFilter = null
    }

    private fun checkIfNewPairingDiscoveryShouldBeStarted(withServices: Filter): Boolean = when (isRetrievingPairedDevicesFilter) {
        withServices -> false
        null -> true
        else -> {
            retrievingPairedDevicesJob?.cancel()
            retrievingPairedDevicesJob = null
            true
        }
    }.also {
        isRetrievingPairedDevicesFilter = withServices
    }

    protected abstract suspend fun retrievePairedDeviceDiscoveredEvents(withServices: Filter, connectionSettings: ConnectionSettings?): List<Scanner.DeviceDiscovered>

    internal fun handleDeviceDiscovered(
        deviceWrapper: DeviceWrapper,
        rssi: RSSI,
        advertisementData: BaseAdvertisementData,
        connectionManagerBuilder: DeviceConnectionManager.Builder,
    ) = handleDeviceDiscovered(deviceWrapper, rssi, advertisementData) { coroutineContext ->
        getDeviceBuilder(
            deviceWrapper,
            rssi,
            advertisementData,
            connectionManagerBuilder,
            defaultConnectionSettings,
        )(coroutineContext)
    }

    protected open fun handleDeviceDiscovered(deviceWrapper: DeviceWrapper, rssi: RSSI, advertisementData: BaseAdvertisementData, deviceCreator: (CoroutineContext) -> Device) {
        logger.info(LOG_TAG) { "Device ${deviceWrapper.identifier.stringValue} discovered with rssi: $rssi" }
        logger.debug(LOG_TAG) { "Device ${deviceWrapper.identifier.stringValue} discovered with advertisement data:\n ${advertisementData.description}" }

        isScanningDevicesDiscovered.value?.trySend(
            Scanner.DeviceDiscovered(deviceWrapper.identifier, rssi, advertisementData, deviceCreator),
        )
    }

    private suspend fun handlePairedDevices(devices: List<Scanner.DeviceDiscovered>, filter: Filter, removeForAllPairedFilters: Boolean) {
        // Only update if actually scanning for this Filter
        isRetrievingPairedDevicesMutex.withLock {
            if (isRetrievingPairedDevicesFilter == filter) {
                logger.info(LOG_TAG) {
                    val identifiers = devices.map(Scanner.DeviceDiscovered::identifier)
                    "Paired Devices retrieved: $identifiers for filter: $filter"
                }
                emitEvent(Scanner.Event.PairedDevicesRetrieved(devices, filter, removeForAllPairedFilters))
                isRetrievingPairedDevicesFilter = null
                retrievingPairedDevicesJob = null
            }
        }
    }

    protected fun getDeviceBuilder(
        deviceWrapper: DeviceWrapper,
        rssi: RSSI,
        advertisementData: BaseAdvertisementData,
        connectionManagerBuilder: DeviceConnectionManager.Builder,
        connectionSettings: ConnectionSettings?,
    ): (CoroutineContext) -> Device = { coroutineContext ->
        DeviceImpl(
            deviceWrapper.identifier,
            DeviceInfoImpl(deviceWrapper, rssi, advertisementData),
            connectionSettings ?: defaultConnectionSettings,
            { settings ->
                connectionManagerBuilder.create(
                    deviceWrapper,
                    settings,
                    CoroutineScope(coroutineContext + CoroutineName("ConnectionManager ${deviceWrapper.identifier.stringValue}")),
                )
            },
            CoroutineScope(coroutineContext + CoroutineName("Device ${deviceWrapper.identifier.stringValue}")),
        ) { connectionManager, context ->
            ConnectableDeviceStateImplRepo(
                (connectionSettings ?: defaultConnectionSettings).reconnectionSettings,
                connectionManager,
                context,
            )
        }
    }

    internal fun handleDeviceConnected(identifier: Identifier) {
        logger.debug(LOG_TAG) { "Device ${identifier.stringValue} connected" }
        connectionEventChannel.trySend(Scanner.ConnectionEvent.DeviceConnected(identifier))
    }

    internal fun handleDeviceDisconnected(identifier: Identifier) {
        logger.debug(LOG_TAG) { "Device ${identifier.stringValue} disconnected" }
        connectionEventChannel.trySend(Scanner.ConnectionEvent.DeviceDisconnected(identifier))
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
expect class DefaultScanner : BaseScanner {
    override val isSupported: Boolean
    override val bluetoothEnabledMonitor: BluetoothMonitor?

    override suspend fun didStartScanning(filter: Filter)
    override suspend fun didStopScanning()
    override fun generateEnableSensorsActions(): List<EnableSensorAction>
    override suspend fun retrievePairedDeviceDiscoveredEvents(withServices: Filter, connectionSettings: ConnectionSettings?): List<Scanner.DeviceDiscovered>
}
