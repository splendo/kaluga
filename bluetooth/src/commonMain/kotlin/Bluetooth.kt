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
import com.splendo.kaluga.bluetooth.device.BaseAdvertisementData
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceInfoImpl
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.bluetooth.device.DeviceState.Connected
import com.splendo.kaluga.bluetooth.device.DeviceState.Connecting
import com.splendo.kaluga.bluetooth.device.DeviceState.Disconnected
import com.splendo.kaluga.bluetooth.device.DeviceState.Disconnecting
import com.splendo.kaluga.bluetooth.device.DeviceState.Reconnecting
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.scanner.BaseScanner
import com.splendo.kaluga.bluetooth.scanner.ScanningState
import com.splendo.kaluga.bluetooth.scanner.ScanningStateRepo
import com.splendo.kaluga.logging.info
import com.splendo.kaluga.permissions.Permissions
import kotlin.jvm.JvmName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

class Bluetooth internal constructor(
    permissions: Permissions,
    connectionSettings: ConnectionSettings,
    autoRequestPermission: Boolean,
    autoEnableBluetooth: Boolean,
    scannerBuilder: BaseScanner.Builder,
    coroutineScope: CoroutineScope
) : CoroutineScope by coroutineScope {

    interface Builder {
        fun create(
            connectionSettings: ConnectionSettings = ConnectionSettings(ConnectionSettings.ReconnectionSettings.Always),
            autoRequestPermission: Boolean = true,
            autoEnableBluetooth: Boolean = true,
            coroutineScope: CoroutineScope = MainScope()
        ): Bluetooth
    }

    companion object {
        private const val LOG_TAG = "Kaluga Bluetooth"
    }

    internal val scanningStateRepo = ScanningStateRepo(
        permissions,
        connectionSettings,
        autoRequestPermission,
        autoEnableBluetooth,
        scannerBuilder
    )

    val scanningState: Flow<ScanningState> = scanningStateRepo

    sealed class ScanMode {
        object Stopped : ScanMode()
        class Scan(val filter: Set<UUID>) : ScanMode()
    }

    private val _scanMode = MutableStateFlow<ScanMode>(ScanMode.Stopped)
    val scanMode = _scanMode.asStateFlow()

    fun devices(): Flow<List<Device>> = combine(scanningStateRepo, scanMode) { scanState, scanMode ->
        when (scanState) {
            is ScanningState.Initialized.Enabled.Idle -> when(scanMode) {
                is ScanMode.Scan -> {
                    scanningStateRepo.takeAndChangeState(
                        remainIfStateNot = ScanningState.Initialized.Enabled.Idle::class
                    ) { it.startScanning(scanMode.filter) }
                    if (scanState.discovered.filter == scanMode.filter) {
                        scanState.discovered.devices
                    } else {
                        emptyList()
                    }
                }
                is ScanMode.Stopped -> emptyList()
            }
            is ScanningState.Initialized.Enabled.Scanning -> when (scanMode) {
                is ScanMode.Scan -> {
                    // d("devices: ${scanState.discovered}")
                    if (scanState.discovered.filter == scanMode.filter) {
                        scanState.discovered.devices
                    } else {
                        scanningStateRepo.takeAndChangeState(
                            remainIfStateNot = ScanningState.Initialized.Enabled.Scanning::class
                        ) { it.stopScanning }
                        emptyList()
                    }
                }
                is ScanMode.Stopped -> {
                    scanningStateRepo.takeAndChangeState(
                        remainIfStateNot = ScanningState.Initialized.Enabled.Scanning::class
                    ) { it.stopScanning }
                    scanState.discovered.devices
                }
            }
            is ScanningState.Initialized.NoBluetooth -> {
                info(LOG_TAG, "No Bluetooth ($scanState) in mode ($scanMode)")
                emptyList()
            }
            is ScanningState.NotInitialized -> {
                scanningStateRepo.takeAndChangeState(
                    remainIfStateNot = ScanningState.NotInitialized::class
                ) { it.initialize(scanningStateRepo) }
                emptyList()
            }
        }
    }.distinctUntilChanged()

    fun startScanning(filter: Set<UUID> = emptySet()) {
        info(LOG_TAG, "Start Scanning for $filter")
        _scanMode.value = ScanMode.Scan(filter)
    }

    fun stopScanning() {
        info(LOG_TAG, "Stop Scanning")
        _scanMode.value = ScanMode.Stopped
    }

    suspend fun isScanning() = combine(scanningStateRepo, scanMode) { scanState, scanMode ->
        scanState is ScanningState.Initialized.Enabled.Scanning && scanMode is ScanMode.Scan
    }.stateIn(this)
}

expect class BluetoothBuilder : Bluetooth.Builder

operator fun Flow<List<Device>>.get(identifier: Identifier): Flow<Device?> {
    return this.map { devices ->
        devices.firstOrNull { it.identifier == identifier }
    }
}

fun Flow<Device?>.state(): Flow<DeviceState> {
    return this.flatMapLatest { device ->
        device ?: emptyFlow()
    }
}

fun Flow<Device?>.services(): Flow<List<Service>> {
    return state().transformLatest { deviceState ->
            emit(
                when (deviceState) {
                    is Connected -> {
                        when (deviceState) {
                            is Connected.NoServices -> {
                                deviceState.startDiscovering()
                                emptyList()
                            }
                            is Connected.Idle -> deviceState.services
                            is Connected.HandlingAction -> deviceState.services
                            else -> emptyList()
                        }
                    }
                    else -> emptyList()
                }
            )
        }.distinctUntilChanged()
}

suspend fun Flow<Device?>.connect() {
    state().transformLatest { deviceState ->
            when (deviceState) {
                is Disconnected -> deviceState.startConnecting()
                is Connected -> emit(Unit)
                is Connecting, is Reconnecting, is Disconnecting -> { }
            }
    }.first()
}

suspend fun Flow<Device?>.disconnect() {
    state().transformLatest { deviceState ->
            when (deviceState) {
                is Connected -> deviceState.startDisconnected()
                is Connecting -> deviceState.handleCancel()
                is Reconnecting -> deviceState.handleCancel()
                is Disconnected -> emit(Unit)
                is Disconnecting -> {} // just wait
            }
    }.first()
}

fun Flow<Device?>.info(): Flow<DeviceInfoImpl> {
    return state().transformLatest { deviceState ->
        emit(deviceState.deviceInfo)
    }
}

fun Flow<Device?>.advertisement(): Flow<BaseAdvertisementData> {
    return this.info().map { it.advertisementData }.distinctUntilChanged()
}

fun Flow<Device?>.rssi(): Flow<Int> {
    return this.info().map { it.rssi }.distinctUntilChanged()
}

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
            lastNResults.reduce { acc, d -> acc + d } / lastNResults.size.toDouble()
        else
            Double.NaN
    }
}

suspend fun Flow<Device?>.updateRssi() {
    state().transformLatest { deviceState ->
            when (deviceState) {
                is Connected -> {
                    deviceState.readRssi()
                    emit(Unit)
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
