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

import co.touchlab.stately.collections.sharedMutableListOf
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
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.isActive
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.JvmName
import kotlin.native.concurrent.SharedImmutable
import kotlin.time.Duration.Companion.seconds

@SharedImmutable // NOTE: replace with a limited parallelism dispatcher view when available
private val defaultBluetoothDispatcher by lazy {
    singleThreadDispatcher("Bluetooth")
}

interface BluetoothService {
    fun startScanning(filter: Set<UUID> = emptySet())
    fun stopScanning()
    fun pairedDevices(filter: Set<UUID>): Flow<List<Device>>
    fun devices(): Flow<List<Device>>
    suspend fun isScanning(): Flow<Boolean>
    val isEnabled: Flow<Boolean>
}

class Bluetooth internal constructor(
    scannerSettingsBuilder: (CoroutineContext) -> BaseScanner.Settings,
    connectionSettings: ConnectionSettings,
    scannerBuilder: BaseScanner.Builder,
    coroutineContext: CoroutineContext,
) : BluetoothService, CoroutineScope by CoroutineScope(coroutineContext + CoroutineName("Bluetooth")) {

    interface Builder {
        fun create(
            scannerSettingsBuilder: (Permissions) -> BaseScanner.Settings = { BaseScanner.Settings(it) },
            connectionSettings: ConnectionSettings = ConnectionSettings(),
            coroutineContext: CoroutineContext = defaultBluetoothDispatcher,
        ): Bluetooth
    }

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

    sealed class ScanMode {
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
    override fun pairedDevices(filter: Set<UUID>): Flow<List<Device>> = pairedDevices(filter, timer)
    internal fun pairedDevices(filter: Set<UUID>, timer: Flow<Unit>): Flow<List<Device>> =
        combine(scanningStateRepo, timer) { scanningState, _ -> scanningState }
            .transform { state ->
                com.splendo.kaluga.logging.debug("State $state")
                if (state is ScanningState.Enabled) {
                    // trigger retrieve paired devices list
                    com.splendo.kaluga.logging.debug("Retrieve paired devices")
                    state.retrievePairedDevices(filter)
                    emit(state.paired.devices)
                } else {
                    emit(emptyList())
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

expect class BluetoothBuilder : Bluetooth.Builder

operator fun Flow<List<Device>>.get(identifier: Identifier): Flow<Device?> {
    return this.map { devices ->
        devices.firstOrNull { it.identifier == identifier }
    }
}

fun Flow<Device?>.state(): Flow<DeviceState> {
    return this.flatMapLatest { device ->
        device?.state ?: emptyFlow()
    }
}

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

suspend fun Flow<Device?>.connect() {
    transformLatest { device ->
        device?.let {
            emit(it.connect())
        }
    }.first()
}

suspend fun Flow<Device?>.disconnect() {
    transformLatest { device ->
        device?.let {
            it.disconnect()
            emit(Unit)
        }
    }.first()
}

fun Flow<Device?>.info(): Flow<DeviceInfo> = flatMapLatest { device ->
    device?.info ?: emptyFlow()
}

fun Flow<Device?>.advertisement(): Flow<BaseAdvertisementData> = info().map { it.advertisementData }.distinctUntilChanged()

fun Flow<Device?>.rssi(): Flow<Int> = info().map { it.rssi }.distinctUntilChanged()

fun Flow<Device?>.mtu() = state().map { state ->
    if (state is ConnectableDeviceState.Connected) {
        state.mtu
    } else {
        null
    }
}.distinctUntilChanged()

fun Flow<Device?>.distance(environmentalFactor: Double = 2.0, averageOver: Int = 5): Flow<Double> {
    val lastNResults = sharedMutableListOf<Double>()
    return this.info().map { deviceInfo ->
        while (lastNResults.size >= averageOver) {
            lastNResults.removeAt(0)
        }
        val distance = deviceInfo.distance(environmentalFactor)
        if (!distance.isNaN())
            lastNResults.add(distance)
        if (lastNResults.isNotEmpty())
            lastNResults.average()
        else
            Double.NaN
    }
}

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

suspend fun Flow<Device?>.requestMtu(mtu: Int): Boolean {
    return state().transformLatest { deviceState ->
        when (deviceState) {
            is ConnectableDeviceState.Connected -> {
                emit(deviceState.requestMtu(mtu))
            }
            else -> {}
        }
    }.first()
}

@JvmName("getService")
operator fun Flow<List<Service>>.get(uuid: UUID): Flow<Service?> {
    return this.map { services ->
        services.firstOrNull {
            it.uuid.uuidString == uuid.uuidString
        }
    }
}

fun Flow<Service?>.characteristics(): Flow<List<Characteristic>> {
    return this.mapLatest { service -> service?.characteristics ?: emptyList() }.distinctUntilChanged()
}

fun Flow<Characteristic?>.descriptors(): Flow<List<Descriptor>> {
    return this.mapLatest { characteristic -> characteristic?.descriptors ?: emptyList() }.distinctUntilChanged()
}

@JvmName("getAttribute")
operator fun <T : Attribute<R, W>, R : DeviceAction.Read, W : DeviceAction.Write> Flow<List<T>>.get(uuid: UUID): Flow<T?> {
    return this.map { attribute ->
        attribute.firstOrNull {
            it.uuid.uuidString == uuid.uuidString
        }
    }
}

fun <T : Attribute<R, W>, R : DeviceAction.Read, W : DeviceAction.Write> Flow<T?>.value(): Flow<ByteArray?> {
    return this.flatMapLatest { attribute ->
        attribute ?: flowOf(null)
    } // TODO: we probably want to read duplicate values so this is for now disabled: .distinctUntilChanged()
}
