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

package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.base.singleThreadDispatcher
import com.splendo.kaluga.base.text.lowerCased
import com.splendo.kaluga.base.utils.KalugaLocale
import com.splendo.kaluga.base.utils.enUsPosix
import com.splendo.kaluga.bluetooth.device.BaseAdvertisementData
import com.splendo.kaluga.bluetooth.device.ConnectableDevice
import com.splendo.kaluga.bluetooth.device.ConnectableDeviceState
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.DeviceInfo
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.device.stringValue
import com.splendo.kaluga.bluetooth.scanner.BaseScanner
import com.splendo.kaluga.bluetooth.scanner.Filter
import com.splendo.kaluga.bluetooth.scanner.ScanningState
import com.splendo.kaluga.bluetooth.scanner.ScanningStateFlowRepo
import com.splendo.kaluga.bluetooth.scanner.ScanningStateRepo
import com.splendo.kaluga.bluetooth.serialization.BluetoothFormat
import com.splendo.kaluga.bluetooth.server.BluetoothServer
import com.splendo.kaluga.bluetooth.server.BluetoothServerDSL
import com.splendo.kaluga.bluetooth.server.ServerSettings
import com.splendo.kaluga.permissions.base.Permissions
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.serializer
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration.Companion.seconds

private val defaultBluetoothClientDispatcher by lazy {
    singleThreadDispatcher("BluetoothClient")
}

private val defaultBluetoothServerDispatcher by lazy {
    singleThreadDispatcher("BluetoothServer")
}

/**
 * The transmission power level in dBm
 */
typealias TxPower = Int

/**
 * The Received signal strength indication (RSSI)
 */
typealias RSSI = Int

/**
 * The Maximum Transmission Unit (MTU)
 */
typealias MTU = Int

/**
 * A service for managing Bluetooth [ConnectableDevice]
 */
interface BluetoothService {

    /**
     * Specifies the behaviour of cleaning up the list of [ConnectableDevice] discovered by a [BluetoothService]
     */
    enum class CleanMode {

        /**
         * Retains all [ConnectableDevice] previously scanned, regardless of [Filter]
         */
        RETAIN_ALL,

        /**
         * Removes all [ConnectableDevice] previously scanned, regardless of [Filter]
         */
        REMOVE_ALL,

        /**
         * Removes only the [ConnectableDevice] previously scanned with the [Filter] used for scanning
         */
        ONLY_PROVIDED_FILTER,
    }

    /**
     * Starts scanning for [ConnectableDevice].
     * To receive the devices, use [scannedDevices] or [allDevices]
     * @param filter if not empty, only [ConnectableDevice] that have at least one [Service] matching one of the [UUID] will be scanned.
     * @param cleanMode the [CleanMode] to apply to previously scanned [ConnectableDevice]. [CleanMode.ONLY_PROVIDED_FILTER] will apply to [filter]
     * @param connectionSettings the [ConnectionSettings] to apply to scanned [ConnectableDevice]. If `null` the default will be used
     * Note that if a [ConnectableDevice] was previously scanned (and not cleaned by [cleanMode]) the old [ConnectionSettings] will still apply.
     */
    fun startScanning(filter: Filter = emptySet(), cleanMode: CleanMode = CleanMode.REMOVE_ALL, connectionSettings: ConnectionSettings? = null)

    /**
     * Stops scanning for [ConnectableDevice]
     * @param cleanMode the [CleanMode] to apply to previously scanned [ConnectableDevice]. [CleanMode.ONLY_PROVIDED_FILTER] will apply to the [Filter] last passed to [startScanning]
     */
    fun stopScanning(cleanMode: CleanMode = CleanMode.REMOVE_ALL)

    /**
     * Gets a [Flow] of the list of [ConnectableDevice] that have been paired to the system
     * @param filter filters the list to only return the [ConnectableDevice] that at least one [Service] matching one of the provided [UUID]
     * @param removeForAllPairedFilters if `true` the list of paired devices for all filters will be emptied
     * @param connectionSettings the [ConnectionSettings] to apply to the paired devices found. If `null` the default will be used
     */
    fun pairedDevices(filter: Filter, removeForAllPairedFilters: Boolean = true, connectionSettings: ConnectionSettings? = null): Flow<List<ConnectableDevice>>

    /**
     * Gets a [Flow] containing a list of all [ConnectableDevice] scanned by the service.
     * Requires that [startScanning] has been called, otherwise no devices will be found
     */
    fun allDevices(): Flow<List<ConnectableDevice>>

    /**
     * Gets a [Flow] containing a list of all [ConnectableDevice] scanned by the service for a given [Filter].
     * Requires that [startScanning] has been called with [filter], otherwise no devices will be found
     * @param filter the [Filter] to get the devices for
     */
    fun scannedDevices(filter: Filter = emptySet()): Flow<List<ConnectableDevice>>

    /**
     * Gets a [Flow] containing a list of all [ConnectableDevice] scanned by the service for the last [Filter] passed to [startScanning].
     * Requires that [startScanning] has been called with [filter], otherwise no devices will be found
     */
    fun devices(): Flow<List<ConnectableDevice>>

    /**
     * Gets a [Flow] that indicates whether the service is actively scanning for [ConnectableDevice]
     */
    suspend fun isScanning(): Flow<Boolean>

    /**
     * A [Flow] that indicates whether Bluetooth is currently enabled. When `false`, Bluetooth might be unavailable or permissions may be missing.
     */
    val isEnabled: Flow<Boolean>
}

/**
 * A [BluetoothService] that uses a [ScanningStateFlowRepo] to manage the [ScanningState]
 * @param coroutineContext the [CoroutineContext] in which Bluetooth runs
 * @param scanningStateRepoBuilder method for creating a the [ScanningStateFlowRepo] to contain the [ScanningState] of the Bluetooth service
 */
class Bluetooth constructor(coroutineContext: CoroutineContext, scanningStateRepoBuilder: (CoroutineContext) -> ScanningStateFlowRepo) :
    BluetoothService,
    CoroutineScope by CoroutineScope(coroutineContext + CoroutineName("Bluetooth")) {

    internal constructor(
        scannerSettingsBuilder: suspend (CoroutineContext) -> BaseScanner.Settings,
        scannerBuilder: BaseScanner.Builder,
        coroutineContext: CoroutineContext,
    ) : this(
        coroutineContext,
        { context ->
            ScanningStateRepo(
                scannerSettingsBuilder,
                scannerBuilder,
                { identifier -> context + CoroutineName("Device ${identifier.stringValue}") },
                context + CoroutineName("Scanning State Repo"),
            )
        },
    )

    internal val scanningStateRepo = scanningStateRepoBuilder(coroutineContext + CoroutineName("Scanning State Repo"))

    private sealed class ScanMode {
        data class Stopped(val cleanMode: BluetoothService.CleanMode) : ScanMode()
        data class Scan(val filter: Filter, val cleanMode: BluetoothService.CleanMode, val connectionSettings: ConnectionSettings?) : ScanMode()
    }

    private val scanMode = MutableStateFlow<ScanMode>(ScanMode.Stopped(BluetoothService.CleanMode.REMOVE_ALL))

    private companion object {
        val PAIRED_DEVICES_REFRESH_RATE = 15.seconds
    }

    private val timer get() = flow {
        while (isActive) {
            emit(Unit) // start 'timer' instantly
            delay(PAIRED_DEVICES_REFRESH_RATE)
        }
    }

    override fun pairedDevices(filter: Filter, removeForAllPairedFilters: Boolean, connectionSettings: ConnectionSettings?): Flow<List<ConnectableDevice>> =
        pairedDevices(filter, removeForAllPairedFilters, connectionSettings, timer)

    internal fun pairedDevices(
        filter: Filter,
        removeForAllPairedFilters: Boolean = true,
        connectionSettings: ConnectionSettings? = null,
        timer: Flow<Unit>,
    ): Flow<List<ConnectableDevice>> {
        var shouldStartRetrievingPairing = true
        return combineTransform(
            timer.onEach { shouldStartRetrievingPairing = true },
            scanningStateRepo,
        ) { _, scanState ->
            when (scanState) {
                is ScanningState.Enabled -> {
                    if (shouldStartRetrievingPairing) {
                        scanState.retrievePairedDevices(filter, removeForAllPairedFilters, connectionSettings)
                        shouldStartRetrievingPairing = false
                    }
                    emit(scanState.devices.devicesForDiscoveryMode(ScanningState.DeviceDiscoveryMode.Paired(filter)))
                }

                is ScanningState.Initialized -> {
                    shouldStartRetrievingPairing = true
                    emit(emptyList())
                }

                else -> {
                    shouldStartRetrievingPairing = true
                }
            }
        }.distinctUntilChanged()
    }

    private fun devicesForScanMode(): Flow<ScanningState.Devices> = combine(scanningStateRepo, scanMode) { scanState, scanMode ->
        when (scanState) {
            is ScanningState.Enabled.Idle -> when (scanMode) {
                is ScanMode.Scan -> {
                    scanningStateRepo.takeAndChangeState(
                        remainIfStateNot = ScanningState.Enabled.Idle::class,
                    ) { it.startScanning(scanMode.filter, scanMode.cleanMode, scanMode.connectionSettings) }
                    scanState.devices
                }

                is ScanMode.Stopped -> scanState.devices
            }

            is ScanningState.Enabled.Scanning -> when (scanMode) {
                is ScanMode.Scan -> {
                    if (scanState.devices.currentScanFilter.filter == scanMode.filter) {
                        scanState.devices
                    } else {
                        scanningStateRepo.takeAndChangeState(
                            remainIfStateNot = ScanningState.Enabled.Scanning::class,
                        ) {
                            // Cleaning should happen when the new scan is started to ensure the proper clean mode is applied
                            it.stopScanning(BluetoothService.CleanMode.RETAIN_ALL)
                        }
                        scanState.devices
                    }
                }

                is ScanMode.Stopped -> {
                    scanningStateRepo.takeAndChangeState(
                        remainIfStateNot = ScanningState.Enabled.Scanning::class,
                    ) { it.stopScanning(scanMode.cleanMode) }
                    scanState.devices
                }
            }

            is ScanningState.Deinitialized -> scanState.previousDevices

            is ScanningState.NoBluetooth, is ScanningState.NoHardware, is ScanningState.Inactive, is ScanningState.Initializing -> scanState.nothingFound
        }
    }.distinctUntilChanged()

    override fun allDevices(): Flow<List<ConnectableDevice>> = devicesForScanMode().map { it.allDevices.values.toList() }.distinctUntilChanged()
    override fun scannedDevices(filter: Filter): Flow<List<ConnectableDevice>> = devicesForScanMode().map {
        it.devicesForDiscoveryMode(ScanningState.DeviceDiscoveryMode.Scanning(filter))
    }.distinctUntilChanged()

    override fun devices(): Flow<List<ConnectableDevice>> = devicesForScanMode().map {
        it.devicesForCurrentScanFilter()
    }

    override fun startScanning(filter: Filter, cleanMode: BluetoothService.CleanMode, connectionSettings: ConnectionSettings?) {
        scanMode.value = ScanMode.Scan(filter, cleanMode, connectionSettings)
    }

    override fun stopScanning(cleanMode: BluetoothService.CleanMode) {
        scanMode.value = ScanMode.Stopped(cleanMode)
    }

    override suspend fun isScanning() = scanMode.flatMapLatest { scanMode ->
        when (scanMode) {
            is ScanMode.Scan -> scanningStateRepo.map { scanState -> scanState is ScanningState.Enabled.Scanning }
            is ScanMode.Stopped -> flowOf(false)
        }
    }.distinctUntilChanged()

    override val isEnabled = scanningStateRepo
        .mapLatest { it is ScanningState.Enabled }
}

/**
 * Builder class for creating a [Bluetooth] object.
 */
interface BaseBluetoothBuilder {

    /**
     * Creates a [Bluetooth] object
     * @param scannerSettingsBuilder a method for getting the [BaseScanner.Settings] to be used while scanning from a [CoroutineContext]
     * @param coroutineContext the [CoroutineContext] in which Bluetooth runs
     * @return the created [Bluetooth]
     */
    fun createClient(
        scannerSettingsBuilder: (Permissions) -> BaseScanner.Settings = { BaseScanner.Settings(it) },
        coroutineContext: CoroutineContext = defaultBluetoothClientDispatcher,
    ): Bluetooth

    /**
     * Creates a [BluetoothServer]
     * @param settingsBuilder a method for getting the [ServerSettings] to be used while scanning from a [CoroutineContext]
     * @param coroutineContext the [CoroutineContext] in which Bluetooth runs
     * @param specs the [BluetoothServerDSL] to build the [BluetoothServer] from
     * @return the created [BluetoothServer]
     */
    suspend fun createServer(
        settingsBuilder: (Permissions) -> ServerSettings = { ServerSettings(permissions = it) },
        coroutineContext: CoroutineContext = defaultBluetoothServerDispatcher,
        specs: BluetoothServerDSL.() -> Unit,
    ): BluetoothServer

    @Deprecated("User createClient instead", replaceWith = ReplaceWith("createClient(scannerSettingsBuilder, coroutineContext)"))
    fun create(
        scannerSettingsBuilder: (Permissions) -> BaseScanner.Settings = { BaseScanner.Settings(it) },
        coroutineContext: CoroutineContext = defaultBluetoothClientDispatcher,
    ) = createClient(scannerSettingsBuilder, coroutineContext)
}

/**
 * A default implementation of [BaseBluetoothBuilder]
 */
expect class BluetoothBuilder : BaseBluetoothBuilder {
    override fun createClient(scannerSettingsBuilder: (Permissions) -> BaseScanner.Settings, coroutineContext: CoroutineContext): Bluetooth
    override suspend fun createServer(settingsBuilder: (Permissions) -> ServerSettings, coroutineContext: CoroutineContext, specs: BluetoothServerDSL.() -> Unit): BluetoothServer
}

/**
 * Gets a ([Flow] of) [ConnectableDevice] with a given [Identifier] from a [Flow] of a list of [ConnectableDevice].
 * @param identifier the [Identifier] of the [ConnectableDevice] to get.
 * @return the [Flow] of [ConnectableDevice] matching the [identifier]
 */
operator fun Flow<List<ConnectableDevice>>.get(identifier: Identifier): Flow<ConnectableDevice?> = this.map { devices ->
    devices.firstOrNull { it.identifier.stringValue.lowerCased(KalugaLocale.enUsPosix) == identifier.stringValue.lowerCased(KalugaLocale.enUsPosix) }
}.distinctUntilChanged()

/**
 * Gets a ([Flow] of) [DeviceState] from a [Flow] or [ConnectableDevice]
 * @return the [Flow] of [DeviceState] associated with the [ConnectableDevice] in the given [Flow]
 */
fun Flow<ConnectableDevice?>.state(): Flow<DeviceState> = this.flatMapLatest { device ->
    device?.state ?: emptyFlow()
}

internal fun Flow<ConnectableDevice?>.startDiscovering(): Flow<ConnectableDeviceState.Connected.DiscoveredServices?> = flatMapLatest { device ->
    device?.filterDiscovering() ?: flowOf(null)
}

/**
 * Gets a ([Flow] of) [ConnectableDeviceState.Connected.DiscoveredServices] from a [ConnectableDevice]
 * This will automatically start discovering services if the device is in a [ConnectableDeviceState.Connected.NoServices] state.
 * @return the [Flow] of [ConnectableDeviceState.Connected.DiscoveredServices] associated with the [ConnectableDevice]. Only emits once services are discovered.
 */
fun ConnectableDevice.filterDiscovering() = state.transformLatest { deviceState ->
    emit(
        when (deviceState) {
            is ConnectableDeviceState.Connected -> {
                when (deviceState) {
                    is ConnectableDeviceState.Connected.NoServices -> {
                        deviceState.startDiscovering()
                        null
                    }

                    is ConnectableDeviceState.Connected.DiscoveredServices -> deviceState
                    is ConnectableDeviceState.Connected.HandlingAction -> deviceState
                    else -> null
                }
            }

            else -> null
        },
    )
}

/**
 * Gets a ([Flow] of) the list of [Service] associated with the [ConnectableDevice] in a [Flow]
 * This will automatically discover services if the device is in a [ConnectableDeviceState.Connected.NoServices] state.
 * @return the [Flow] of the list of [Service] associated with the [ConnectableDevice] in the given [Flow]
 */
fun Flow<ConnectableDevice?>.services(): Flow<List<RemoteService>> = flatMapLatest { device ->
    device?.services() ?: flowOf(emptyList())
}.distinctUntilChanged()

/**
 * Gets a ([Flow] of) the list of [Service] associated with the [ConnectableDevice]
 * This will automatically discover services if the device is in a [ConnectableDeviceState.Connected.NoServices] state.
 * @return the [Flow] of the list of [Service] associated with the [ConnectableDevice]. This may emit an empty list if discovery hasn't completed yet.
 */
fun ConnectableDevice.services() = filterDiscovering().map { discoveredState ->
    discoveredState?.services.orEmpty()
}

/**
 * Gets a ([Flow] of) the list of [Service] associated with the [ConnectableDevice] in a [Flow]
 * This will automatically discover services if the device is in a [ConnectableDeviceState.Connected.NoServices] state.
 * This differs from [services] in that the resulting flow will only emit once services are discovered.
 * @return the [Flow] of the list of [Service] associated with the [ConnectableDevice] in a [Flow].
 */
fun Flow<ConnectableDevice?>.discoveredServices(): Flow<List<RemoteService>> = flatMapLatest { device ->
    device?.discoveredServices() ?: emptyFlow()
}

/**
 * Gets a ([Flow] of) the list of [Service] associated with the [ConnectableDevice]
 * This will automatically discover services if the device is in a [ConnectableDeviceState.Connected.NoServices] state.
 * This differs from [services] in that the resulting flow will only emit once services are discovered.
 * @return the [Flow] of the list of [Service] associated with the [ConnectableDevice].
 */
fun ConnectableDevice.discoveredServices() = filterDiscovering().mapNotNull { discoveredState ->
    discoveredState?.services
}.distinctUntilChanged()

/**
 * Attempts to connect to the [ConnectableDevice] from a [Flow] of [ConnectableDevice]
 * When this method completes, the devices should be in a [ConnectableDeviceState.Connected] state
 * @param reconnectionSettings the [ConnectionSettings.ReconnectionSettings] to use if the [ConnectableDevice] disconnects after connecting. If `null` the default will be used.
 * @return `true` if connection was successful
 */
suspend fun Flow<ConnectableDevice?>.connect(reconnectionSettings: ConnectionSettings.ReconnectionSettings? = null): Boolean = transformLatest { device ->
    device?.let {
        try {
            emit(it.connect(reconnectionSettings))
        } catch (e: CancellationException) {
            withContext(NonCancellable) {
                it.disconnect()
            }
            throw e
        }
    }
}.first()

/**
 * Attempts to disconnect to the [ConnectableDevice] from a [Flow] of [ConnectableDevice]
 * When this method completes, the devices should be in a [ConnectableDeviceState.Disconnected] state
 */
suspend fun Flow<ConnectableDevice?>.disconnect() {
    transformLatest { device ->
        device?.let {
            it.disconnect()
            emit(Unit)
        }
    }.first()
}

/**
 * Gets the ([Flow] of) [DeviceInfo] from a [Flow] of [ConnectableDevice]
 * @return the [Flow] of [DeviceInfo] associated with the [ConnectableDevice] in the given [Flow]
 */
fun Flow<ConnectableDevice?>.info(): Flow<DeviceInfo> = flatMapLatest { device ->
    device?.info ?: emptyFlow()
}

/**
 * Gets the ([Flow] of) [BaseAdvertisementData] from a [Flow] of [ConnectableDevice]
 * @return the [Flow] of [BaseAdvertisementData] associated with the [ConnectableDevice] in the given [Flow]
 */
fun Flow<ConnectableDevice?>.advertisement(): Flow<BaseAdvertisementData> = info().map { it.advertisementData }.distinctUntilChanged()

/**
 * Gets the ([Flow] of) the [RSSI] value from a [Flow] of [ConnectableDevice]
 * @return the [Flow] of the RSSI value associated with the [ConnectableDevice] in the given [Flow]
 */
fun Flow<ConnectableDevice?>.rssi(): Flow<RSSI> = info().map { it.rssi }.distinctUntilChanged()

/**
 * Gets the ([Flow] of) the [MTU] from a [Flow] of [ConnectableDevice]
 * @return the [Flow] of [MTU] associated with the [ConnectableDevice] in the given [Flow]
 */
fun Flow<ConnectableDevice?>.mtu() = flatMapLatest { device ->
    device?.mtu() ?: flowOf(null)
}.distinctUntilChanged()

/**
 * Gets the ([Flow] of) the [MTU] from a [ConnectableDevice]
 * @return the [Flow] of [MTU] associated with the [ConnectableDevice]
 */
fun ConnectableDevice.mtu() = state.map { state ->
    if (state is ConnectableDeviceState.Connected.MtuHolder) {
        state.mtu
    } else {
        null
    }
}.distinctUntilChanged()

/**
 * Gets the ([Flow] of) the distance in meters between the scanner and a [Flow] of [ConnectableDevice].
 * To get a more stable result, this method will average the distance over the last [averageOver] results.
 * @param environmentalFactor the constant to account for environmental interference. Should usually range between 2.0 and 4.0
 * @param averageOver averages the calculated distance over this amount of scan results. Always uses the last results.
 * @return the [Flow] of distance in meters between the scanner and the [ConnectableDevice] in the given [Flow]
 */
fun Flow<ConnectableDevice?>.distance(environmentalFactor: Double = 2.0, averageOver: Int = 5): Flow<Double> = flatMapLatest { device ->
    device?.distance(environmentalFactor, averageOver) ?: flowOf(Double.NaN)
}

/**
 * Gets the ([Flow] of) the distance in meters between the scanner and a [ConnectableDevice].
 * To get a more stable result, this method will average the distance over the last [averageOver] results.
 * @param environmentalFactor the constant to account for environmental interference. Should usually range between 2.0 and 4.0
 * @param averageOver averages the calculated distance over this amount of scan results. Always uses the last results.
 * @return the [Flow] of distance in meters between the scanner and the [ConnectableDevice]
 */
fun ConnectableDevice.distance(environmentalFactor: Double = 2.0, averageOver: Int = 5): Flow<Double> {
    val lastNResults = mutableListOf<Double>()
    return info.map { deviceInfo ->
        while (lastNResults.size >= averageOver) {
            lastNResults.removeAt(0)
        }
        val distance = deviceInfo.distance(environmentalFactor)
        if (!distance.isNaN()) {
            lastNResults.add(distance)
        }

        if (lastNResults.isNotEmpty()) lastNResults.average() else Double.NaN
    }
}

/**
 * Attempts to request an update to the RSSI of the [ConnectableDevice] from a [Flow] of [ConnectableDevice]
 * When this method completes, the devices should have had [ConnectableDeviceState.Connected.readRssi] called
 */
suspend fun Flow<ConnectableDevice?>.updateRssi() = transformLatest { device ->
    device?.let {
        emit(it.updateRssi())
    }
}.first()

/**
 * Attempts to request an update to the RSSI of the [ConnectableDevice] from a [ConnectableDevice]
 * When this method completes, the devices should have had [ConnectableDeviceState.Connected.readRssi] called
 */
suspend fun ConnectableDevice.updateRssi() = state.transformLatest { deviceState ->
    when (deviceState) {
        is ConnectableDeviceState.Connected -> {
            deviceState.readRssi()
            emit(Unit)
        }
        else -> {}
    }
}.first()

/**
 * Attempts to request a [MTU] size for the [ConnectableDevice] from a [Flow] of [ConnectableDevice]
 * @param mtu the [MTU] size to request
 */
suspend fun Flow<ConnectableDevice?>.requestMtu(mtu: MTU) = transformLatest { device ->
    device?.let {
        emit(it.requestMtu(mtu).response.await())
    }
}.first()

/**
 * Attempts to request a [MTU] size for the [ConnectableDevice] from a [ConnectableDevice]
 * @param mtu the [MTU] size to request
 */
suspend fun ConnectableDevice.requestMtu(mtu: MTU) = state
    .filterIsInstance<ConnectableDeviceState.Connected.MtuHolder>()
    .first().requestMtu(mtu)

/**
 * Gets a ([Flow] of) the list [RemoteCharacteristic] associated with the [RemoteService] in a [Flow]
 * @return the [Flow] of the list of [RemoteCharacteristic] associated with the [RemoteService] in the given [Flow]
 */
fun Flow<RemoteService?>.characteristics(): Flow<List<RemoteCharacteristic>> = mapLatest { service -> service?.characteristics ?: emptyList() }.distinctUntilChanged()

/**
 * Gets a ([Flow] of) the list [RemoteService] that are included with the [RemoteService] in a [Flow]
 * @return the [Flow] of the list of [RemoteService] included in with the [RemoteService] in the given [Flow]
 */
fun Flow<RemoteService?>.includedServices(): Flow<List<RemoteService>> = mapLatest { service -> service?.includedServices ?: emptyList() }.distinctUntilChanged()

/**
 * Gets a ([Flow] of) the list [RemoteDescriptor] associated with the [RemoteCharacteristic] in a [Flow]
 * @return the [Flow] of the list of [RemoteDescriptor] associated with the [RemoteCharacteristic] in the given [Flow]
 */
fun Flow<RemoteCharacteristic?>.descriptors(): Flow<List<RemoteDescriptor>> = mapLatest { characteristic -> characteristic?.descriptors ?: emptyList() }.distinctUntilChanged()

/**
 * Gets a ([Flow] of) the [ByteArray] value from a [Flow] of an [RemoteCharacteristic]
 * This method will automatically subscribe/unsubscribe to the [RemoteCharacteristic] when the [Flow] is collected
 * @return the [Flow] of the [ByteArray] value of the [RemoteCharacteristic] in the given [Flow]
 */
fun Flow<RemoteCharacteristic?>.value(): Flow<ByteArray> = distinctUntilChanged().flatMapLatest { characteristic ->
    characteristic?.value() ?: emptyFlow()
}

/**
 * Gets a ([Flow] of) [T] value from a [Flow] of an [RemoteCharacteristic]
 * This method will automatically subscribe/unsubscribe to the [RemoteCharacteristic] when the [Flow] is collected
 * @param T the type of the data to receive
 * @param deserializationStrategy the [DeserializationStrategy] to use to deserialize the [ByteArray] to [T]
 * @param bluetoothFormat the [BluetoothFormat] to use to deserialize the [ByteArray] to [T]
 * @return the [Flow] of the [T] value of the [RemoteCharacteristic] in the given [Flow]
 */
fun <T> Flow<RemoteCharacteristic?>.value(deserializationStrategy: DeserializationStrategy<T>, bluetoothFormat: BluetoothFormat = BluetoothFormat): Flow<T> =
    distinctUntilChanged().flatMapLatest { characteristic ->
        characteristic?.value(deserializationStrategy, bluetoothFormat) ?: emptyFlow()
    }

/**
 * Gets a ([Flow] of) [T] value from a [Flow] of an [RemoteCharacteristic]
 * This method will automatically subscribe/unsubscribe to the [RemoteCharacteristic] when the [Flow] is collected
 * @param T the type of the data to receive
 * @param bluetoothFormat the [BluetoothFormat] to use to deserialize the [ByteArray] to [T]
 * @return the [Flow] of the [T] value of the [RemoteCharacteristic] in the given [Flow]
 */
inline fun <reified T> Flow<RemoteCharacteristic?>.value(bluetoothFormat: BluetoothFormat = BluetoothFormat): Flow<T> =
    value(bluetoothFormat.serializersModule.serializer(), bluetoothFormat)

/**
 * Gets a ([Flow] of) the [ByteArray] value from a [RemoteCharacteristic]
 * This method will automatically subscribe/unsubscribe to the [RemoteCharacteristic] when the [Flow] is collected
 * @return the [Flow] of the [ByteArray] value of the [RemoteCharacteristic]
 */
fun RemoteCharacteristic.value(): Flow<ByteArray> = flow {
    val valueChannel = Channel<ByteArray>(Channel.UNLIMITED)
    val subscriptionResponse = subscribe {
        valueChannel.trySend(it)
    }
    subscriptionResponse.subscription?.let { subscription ->
        try {
            emitAll(valueChannel)
        } finally {
            subscription.startUnsubscribe()
        }
    }
}

/**
 * Gets a ([Flow] of) [T] value from a [RemoteCharacteristic]
 * This method will automatically subscribe/unsubscribe to the [RemoteCharacteristic] when the [Flow] is collected
 * @param T the type of the data to receive
 * @param deserializationStrategy the [DeserializationStrategy] to use to deserialize the [ByteArray] to [T]
 * @param bluetoothFormat the [BluetoothFormat] to use to deserialize the [ByteArray] to [T]
 * @return the [Flow] of the [T] value of the [RemoteCharacteristic]
 */
fun <T> RemoteCharacteristic.value(deserializationStrategy: DeserializationStrategy<T>, bluetoothFormat: BluetoothFormat = BluetoothFormat): Flow<T> = value().map { value ->
    bluetoothFormat.decodeFromByteArray(deserializationStrategy, value)
}

/**
 * Gets a ([Flow] of) [T] value from a [RemoteCharacteristic]
 * This method will automatically subscribe/unsubscribe to the [RemoteCharacteristic] when the [Flow] is collected
 * @param T the type of the data to receive
 * @param bluetoothFormat the [BluetoothFormat] to use to deserialize the [ByteArray] to [T]
 * @return the [Flow] of the [T] value of the [RemoteCharacteristic]
 */
inline fun <reified T> RemoteCharacteristic.value(bluetoothFormat: BluetoothFormat = BluetoothFormat) = value(bluetoothFormat.serializer<T>(), bluetoothFormat)
