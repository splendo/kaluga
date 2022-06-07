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
import com.splendo.kaluga.bluetooth.device.ConnectibleDeviceState
import com.splendo.kaluga.bluetooth.device.ConnectibleDeviceStateImplRepo
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.JvmName

interface BluetoothService {
    fun startScanning(filter: Set<UUID> = emptySet())
    fun stopScanning()
    fun devices(): Flow<List<Device>>
    suspend fun isScanning(): StateFlow<Boolean>
    val isEnabled: Flow<Boolean>
}

class Bluetooth internal constructor(
    scannerSettingsBuilder: (CoroutineContext) -> BaseScanner.Settings,
    connectionSettings: ConnectionSettings,
    scannerBuilder: BaseScanner.Builder,
    coroutineContext: CoroutineContext,
    contextCreator: CoroutineContext.(String) -> CoroutineContext = { this + singleThreadDispatcher(it) },
) : BluetoothService, CoroutineScope by CoroutineScope(coroutineContext + CoroutineName("Bluetooth")) {

    interface Builder {
        fun create(
            scannerSettingsBuilder: (Permissions) -> BaseScanner.Settings,
            connectionSettings: ConnectionSettings,
            coroutineContext: CoroutineContext = singleThreadDispatcher("Bluetooth"),
            contextCreator: CoroutineContext.(String) -> CoroutineContext = { this + singleThreadDispatcher(it) },
        ): Bluetooth
    }

    companion object {
        private const val LOG_TAG = "Kaluga Bluetooth"
    }

    internal val scanningStateRepo = ScanningStateRepo(
        scannerSettingsBuilder,
        scannerBuilder,
        { identifier, deviceInfo, deviceWrapper, connectionManagerBuilder ->
            DeviceImpl(
                identifier,
                deviceInfo,
                connectionSettings,
                { connectionManagerBuilder.create(deviceWrapper, connectionSettings.eventBufferSize, CoroutineScope(coroutineContext.contextCreator("ConnectionManager ${identifier.stringValue}"))) },
                CoroutineScope(coroutineContext.contextCreator("Device ${identifier.stringValue}"))
            ) { connectionManager, coroutineContext ->
                ConnectibleDeviceStateImplRepo(
                    connectionManager,
                    coroutineContext
                )
            }
        }
    )

    sealed class ScanMode {
        object Stopped : ScanMode()
        class Scan(val filter: Set<UUID>) : ScanMode()
    }

    private val scanMode = MutableStateFlow<ScanMode>(ScanMode.Stopped)

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

    override suspend fun isScanning() = combine(scanningStateRepo, scanMode) { scanState, scanMode ->
        scanState is ScanningState.Enabled.Scanning && scanMode is ScanMode.Scan
    }.stateIn(this)

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
                is ConnectibleDeviceState.Connected -> {
                    when (deviceState) {
                        is ConnectibleDeviceState.Connected.NoServices -> {
                            deviceState.startDiscovering()
                            emptyList()
                        }
                        is ConnectibleDeviceState.Connected.Idle -> deviceState.services
                        is ConnectibleDeviceState.Connected.HandlingAction -> deviceState.services
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
    if (state is ConnectibleDeviceState.Connected) {
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
            is ConnectibleDeviceState.Connected -> {
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
            is ConnectibleDeviceState.Connected -> {
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
