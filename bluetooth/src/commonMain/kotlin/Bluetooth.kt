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

import com.splendo.kaluga.base.flow.filterOnlyImportant
import com.splendo.kaluga.base.singleThreadDispatcher
import com.splendo.kaluga.bluetooth.device.BaseAdvertisementData
import com.splendo.kaluga.bluetooth.device.ConnectableDeviceState
import com.splendo.kaluga.bluetooth.device.ConnectableDeviceStateImplRepo
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceImpl
import com.splendo.kaluga.bluetooth.device.DeviceInfo
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.device.stringValue
import com.splendo.kaluga.bluetooth.scanner.BaseScanner
import com.splendo.kaluga.bluetooth.scanner.ScanningState
import com.splendo.kaluga.bluetooth.scanner.ScanningStateRepo
import com.splendo.kaluga.permissions.base.Permissions
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.isActive
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.JvmName
import kotlin.time.Duration.Companion.seconds

private val defaultBluetoothDispatcher by lazy {
    singleThreadDispatcher("Bluetooth")
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
 * A service for managing Bluetooth [Device]
 */
interface BluetoothService {

    /**
     * Starts scanning for [Device].
     * To receive the devices, use [devices]
     * @param filter if not empty, only [Device] that have at least one [Service] matching one of the [UUID] will be scanned.
     */
    fun startScanning(filter: Set<UUID> = emptySet())

    /**
     * Stops scanning for [Device]
     */
    fun stopScanning()

    /**
     * Gets a [Flow] of the list of [Device] that have been paired to the system
     * @param filter filters the list to only return the [Device] that at least one [Service] matching one of the provided [UUID]
     */
    fun pairedDevices(filter: Set<UUID>): Flow<List<Device>>

    /**
     * Gets a [Flow] containing a list of [Device] scanned by the service.
     * Requires that [startScanning] has been called, otherwise no devices will be found
     */
    fun devices(): Flow<List<Device>>

    /**
     * Gets a [Flow] that indicates whether the service is actively scanning for [Device]
     */
    suspend fun isScanning(): Flow<Boolean>

    /**
     * A [Flow] that indicates whether Bluetooth is currently enabled. When `false`, Bluetooth might be unavailable or permissions may be missing.
     */
    val isEnabled: Flow<Boolean>
}

/**
 * A [BluetoothService] that builds a [BaseScanner] for scanning.
 * @param scannerSettingsBuilder a method for getting the [BaseScanner.Settings] to be used while scanning from a [CoroutineContext]
 * @param connectionSettings the [ConnectionSettings] to apply to [Device] scanned
 * @param scannerBuilder the [BaseScanner.Builder] to use to create a [BaseScanner] responsible for scanning.
 * @param coroutineContext the [CoroutineContext] in which Bluetooth runs
 */
class Bluetooth internal constructor(
    scannerSettingsBuilder: suspend (CoroutineContext) -> BaseScanner.Settings,
    connectionSettings: ConnectionSettings,
    scannerBuilder: BaseScanner.Builder,
    coroutineContext: CoroutineContext,
) : BluetoothService, CoroutineScope by CoroutineScope(coroutineContext + CoroutineName("Bluetooth")) {

    internal val scanningStateRepo = ScanningStateRepo(
        scannerSettingsBuilder,
        scannerBuilder,
        { identifier, deviceInfo, deviceWrapper, connectionManagerBuilder ->
            DeviceImpl(
                identifier,
                deviceInfo,
                connectionSettings,
                { settings -> connectionManagerBuilder.create(deviceWrapper, settings, CoroutineScope(coroutineContext + CoroutineName("ConnectionManager ${identifier.stringValue}"))) },
                CoroutineScope(coroutineContext + CoroutineName("Device ${identifier.stringValue}"))
            ) { connectionManager, coroutineContext ->
                ConnectableDeviceStateImplRepo(
                    connectionManager,
                    coroutineContext
                )
            }
        },
        coroutineContext + CoroutineName("Scanning State Repo")
    )

    private sealed class ScanMode {
        object Stopped : ScanMode()
        class Scan(val filter: Set<UUID>) : ScanMode()
    }

    private val scanMode = MutableStateFlow<ScanMode>(ScanMode.Stopped)

    private companion object {
        val PAIRED_DEVICES_REFRESH_RATE = 15.seconds
    }

    private val timer get() = flow {
        while (isActive) {
            emit(Unit) // start 'timer' instantly
            delay(PAIRED_DEVICES_REFRESH_RATE)
        }
    }

    override fun pairedDevices(filter: Set<UUID>): Flow<List<Device>> =
        combine(scanningStateRepo, timer) { scanningState, _ -> scanningState }
            .transformLatest { state ->
                if (state is ScanningState.Enabled) {
                    // trigger retrieve paired devices list
                    state.retrievePairedDevices(filter)
                    emit(state.paired.devices)
                }
            }
            .distinctUntilChanged()

    override fun devices(): Flow<List<Device>> = combine(scanningStateRepo.filterOnlyImportant(), scanMode) { scanState, scanMode ->
        when (scanState) {
            is ScanningState.Enabled.Idle -> when (scanMode) {
                is ScanMode.Scan -> {
                    scanningStateRepo.takeAndChangeState(
                        remainIfStateNot = ScanningState.Enabled.Idle::class
                    ) { it.startScanning(scanMode.filter) }
                    if (scanState.discovered.filter == scanMode.filter) {
                        scanState.discovered.devices
                    } else {
                        emptyList()
                    }
                }
                is ScanMode.Stopped -> emptyList()
            }
            is ScanningState.Enabled.Scanning -> when (scanMode) {
                is ScanMode.Scan -> {
                    if (scanState.discovered.filter == scanMode.filter) {
                        scanState.discovered.devices
                    } else {
                        scanningStateRepo.takeAndChangeState(
                            remainIfStateNot = ScanningState.Enabled.Scanning::class
                        ) { it.stopScanning }
                        emptyList()
                    }
                }
                is ScanMode.Stopped -> {
                    scanningStateRepo.takeAndChangeState(
                        remainIfStateNot = ScanningState.Enabled.Scanning::class
                    ) { it.stopScanning }
                    scanState.discovered.devices
                }
            }
            is ScanningState.NoBluetooth, is ScanningState.NoHardware, is ScanningState.Inactive, is ScanningState.Initializing -> emptyList()
        }
    }.distinctUntilChanged()

    override fun startScanning(filter: Set<UUID>) {
        scanMode.value = ScanMode.Scan(filter)
    }

    override fun stopScanning() {
        scanMode.value = ScanMode.Stopped
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
     * @param connectionSettings the [ConnectionSettings] to apply to [Device] scanned by the [Bluetooth] service.
     * @param coroutineContext the [CoroutineContext] in which Bluetooth runs
     * @return the created [Bluetooth]
     */
    fun create(
        scannerSettingsBuilder: (Permissions) -> BaseScanner.Settings = { BaseScanner.Settings(it) },
        connectionSettings: ConnectionSettings = ConnectionSettings(),
        coroutineContext: CoroutineContext = defaultBluetoothDispatcher,
    ): Bluetooth
}

/**
 * A default implementation of [BaseBluetoothBuilder]
 */
expect class BluetoothBuilder : BaseBluetoothBuilder

/**
 * Gets a ([Flow] of) [Device] with a given [Identifier] from a [Flow] of a list of [Device].
 * @param identifier the [Identifier] of the [Device] to get.
 * @return the [Flow] of [Device] matching the [identifier]
 */
operator fun Flow<List<Device>>.get(identifier: Identifier): Flow<Device?> {
    return this.map { devices ->
        devices.firstOrNull { it.identifier == identifier }
    }
}

/**
 * Gets a ([Flow] of) [DeviceState] from a [Flow] or [Device]
 * @return the [Flow] of [DeviceState] associated with the [Device] in the given [Flow]
 */
fun Flow<Device?>.state(): Flow<DeviceState> {
    return this.flatMapLatest { device ->
        device?.state ?: emptyFlow()
    }
}

/**
 * Gets a ([Flow] of) the list of [Service] associated with the [Device] in a [Flow]
 * This will automatically discover services if the device is in a [ConnectableDeviceState.Connected.NoServices] state.
 * @return the [Flow] of the list of [Service] associated with the [Device] in the given [Flow]
 */
fun Flow<Device?>.services(): Flow<List<Service>> {
    return state().transformLatest { deviceState ->
        emit(
            when (deviceState) {
                is ConnectableDeviceState.Connected -> {
                    when (deviceState) {
                        is ConnectableDeviceState.Connected.NoServices -> {
                            deviceState.startDiscovering()
                            emptyList()
                        }
                        is ConnectableDeviceState.Connected.Idle -> deviceState.services
                        is ConnectableDeviceState.Connected.HandlingAction -> deviceState.services
                        else -> emptyList()
                    }
                }
                else -> emptyList()
            }
        )
    }.distinctUntilChanged()
}

/**
 * Attempts to connect to the [Device] from a [Flow] of [Device]
 * When this method completes, the devices should be in a [ConnectableDeviceState.Connected] state
 */
suspend fun Flow<Device?>.connect() {
    transformLatest { device ->
        device?.let {
            emit(it.connect())
        }
    }.first()
}

/**
 * Attempts to disconnect to the [Device] from a [Flow] of [Device]
 * When this method completes, the devices should be in a [ConnectableDeviceState.Disconnected] state
 */
suspend fun Flow<Device?>.disconnect() {
    transformLatest { device ->
        device?.let {
            it.disconnect()
            emit(Unit)
        }
    }.first()
}

/**
 * Gets the ([Flow] of) [DeviceInfo] from a [Flow] of [Device]
 * @return the [Flow] of [DeviceInfo] associated with the [Device] in the given [Flow]
 */
fun Flow<Device?>.info(): Flow<DeviceInfo> = flatMapLatest { device ->
    device?.info ?: emptyFlow()
}

/**
 * Gets the ([Flow] of) [BaseAdvertisementData] from a [Flow] of [Device]
 * @return the [Flow] of [BaseAdvertisementData] associated with the [Device] in the given [Flow]
 */
fun Flow<Device?>.advertisement(): Flow<BaseAdvertisementData> = info().map { it.advertisementData }.distinctUntilChanged()

/**
 * Gets the ([Flow] of) the [RSSI] value from a [Flow] of [Device]
 * @return the [Flow] of the RSSI value associated with the [Device] in the given [Flow]
 */
fun Flow<Device?>.rssi(): Flow<RSSI> = info().map { it.rssi }.distinctUntilChanged()

/**
 * Gets the ([Flow] of) the [MTU] from a [Flow] of [Device]
 * @return the [Flow] of [MTU] associated with the [Device] in the given [Flow]
 */
fun Flow<Device?>.mtu() = state().map { state ->
    if (state is ConnectableDeviceState.Connected) {
        state.mtu
    } else {
        null
    }
}.distinctUntilChanged()

/**
 * Gets the ([Flow] of) the distance in meters between the scanner and a [Flow] of [Device].
 * To get a more stable result, this method will average the distance over the last [averageOver] results.
 * @param environmentalFactor the constant to account for environmental interference. Should usually range between 2.0 and 4.0
 * @param averageOver averages the calculated distance over this amount of scan results. Always uses the last results.
 * @return the [Flow] of distance in meters between the scanner and the [Device] in the given [Flow]
 */
fun Flow<Device?>.distance(environmentalFactor: Double = 2.0, averageOver: Int = 5): Flow<Double> {
    val lastNResults = mutableListOf<Double>()
    return this.info().map { deviceInfo ->
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
 * Attempts to request an update to the RSSI of the [Device] from a [Flow] of [Device]
 * When this method completes, the devices should have had [ConnectableDeviceState.Connected.readRssi] called
 */
suspend fun Flow<Device?>.updateRssi() {
    state().transformLatest { deviceState ->
        when (deviceState) {
            is ConnectableDeviceState.Connected -> {
                deviceState.readRssi()
                emit(Unit)
            }
            else -> {}
        }
    }.first()
}

/**
 * Attempts to request a [MTU] size for the [Device] from a [Flow] of [Device]
 * When this method completes, the devices should have had [ConnectableDeviceState.Connected.requestMtu] called
 * @param mtu the [MTU] size to request
 * @return if `true` the new MTU value has been requested successfully
 */
suspend fun Flow<Device?>.requestMtu(mtu: MTU): Boolean {
    return state().transformLatest { deviceState ->
        when (deviceState) {
            is ConnectableDeviceState.Connected -> {
                emit(deviceState.requestMtu(mtu))
            }
            else -> {}
        }
    }.first()
}

/**
 * Gets a ([Flow] of) [Service] of a given [UUID] from a [Flow] of a list of [Service]
 * @param uuid the [UUID] of the [Service] to get
 * @return the [Flow] of the [Service] with [uuid] in the list of [Service] in the given [Flow]
 */
@JvmName("getService")
operator fun Flow<List<Service>>.get(uuid: UUID): Flow<Service?> {
    return this.map { services ->
        services.firstOrNull {
            it.uuid.uuidString == uuid.uuidString
        }
    }
}

/**
 * Gets a ([Flow] of) the list [Characteristic] associated with the [Service] in a [Flow]
 * @return the [Flow] of the list of [Characteristic] associated with the [Service] in the given [Flow]
 */
fun Flow<Service?>.characteristics(): Flow<List<Characteristic>> {
    return this.mapLatest { service -> service?.characteristics ?: emptyList() }.distinctUntilChanged()
}

/**
 * Gets a ([Flow] of) the list [Descriptor] associated with the [Characteristic] in a [Flow]
 * @return the [Flow] of the list of [Descriptor] associated with the [Characteristic] in the given [Flow]
 */
fun Flow<Characteristic?>.descriptors(): Flow<List<Descriptor>> {
    return this.mapLatest { characteristic -> characteristic?.descriptors ?: emptyList() }.distinctUntilChanged()
}

/**
 * Gets a ([Flow] of) [AttributeType] of a given [UUID] from a [Flow] of a list of [AttributeType]
 * @param AttributeType the type of [Attribute] to get
 * @param ReadAction the [DeviceAction.Read] associated with [AttributeType]
 * @param WriteAction the [DeviceAction.Write] associated with [AttributeType]
 * @param uuid the [UUID] of the [AttributeType] to get
 * @return the [Flow] of the [AttributeType] with [uuid] in the list of [AttributeType] in the given [Flow]
 */
@JvmName("getAttribute")
operator fun <AttributeType : Attribute<ReadAction, WriteAction>, ReadAction : DeviceAction.Read, WriteAction : DeviceAction.Write> Flow<List<AttributeType>>.get(uuid: UUID): Flow<AttributeType?> {
    return this.map { attribute ->
        attribute.firstOrNull {
            it.uuid.uuidString == uuid.uuidString
        }
    }
}

/**
 * Gets a ([Flow] of) the [ByteArray] value from a [Flow] of an [AttributeType]
 * @param AttributeType the type of [Attribute] to get the value from
 * @param ReadAction the [DeviceAction.Read] associated with [AttributeType]
 * @param WriteAction the [DeviceAction.Write] associated with [AttributeType]
 * @return the [Flow] of the [ByteArray] value of the [AttributeType] in the given [Flow]
 */
fun <AttributeType : Attribute<ReadAction, WriteAction>, ReadAction : DeviceAction.Read, WriteAction : DeviceAction.Write> Flow<AttributeType?>.value(): Flow<ByteArray?> {
    return this.flatMapLatest { attribute ->
        attribute ?: flowOf(null)
    } // TODO: we probably want to read duplicate values so this is for now disabled: .distinctUntilChanged()
}
