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

import com.splendo.kaluga.base.MainQueueDispatcher
import com.splendo.kaluga.base.flow.HotFlowable
import com.splendo.kaluga.bluetooth.device.*
import com.splendo.kaluga.bluetooth.scanner.BaseScanner
import com.splendo.kaluga.bluetooth.scanner.ScanningState
import com.splendo.kaluga.bluetooth.scanner.ScanningStateRepo
import com.splendo.kaluga.permissions.Permissions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.JvmName

class Bluetooth internal constructor(permissions: Permissions,
                                     connectionSettings: ConnectionSettings,
                                     autoRequestPermission: Boolean,
                                     autoEnableBluetooth: Boolean,
                                     scannerBuilder: BaseScanner.Builder) {

    interface Builder {
        fun create(connectionSettings: ConnectionSettings = ConnectionSettings(ConnectionSettings.ReconnectionSettings.Always),
                   autoRequestPermission: Boolean = true,
                   autoEnableBluetooth: Boolean = true): Bluetooth
    }

    internal val scanningStateRepo = ScanningStateRepo(permissions, connectionSettings, autoRequestPermission, autoEnableBluetooth, scannerBuilder)

    private val scanFilter = HotFlowable<Set<UUID>?>(null)

    @ExperimentalCoroutinesApi
    fun devices(): Flow<List<Device>> {
        return scanningStateRepo.flow().combine(scanFilter.flow()) { scanState, filter ->
            when (scanState) {
                is ScanningState.Enabled.Idle -> {
                    filter?.let {f ->
                        scanningStateRepo.takeAndChangeState { state ->
                            when(state) {
                                is ScanningState.Enabled.Idle -> state.startScanning(f)
                                else -> state.remain
                            }
                        }
                        if (scanState.oldFilter == f) scanState.discoveredDevices else emptyList()
                    } ?: scanState.discoveredDevices
                }
                is ScanningState.Enabled.Scanning -> {
                    filter?.let {f ->
                        if (scanState.filter == f) scanState.discoveredDevices else {
                            scanningStateRepo.takeAndChangeState { state ->
                                when(state) {
                                    is ScanningState.Enabled.Scanning -> state.stopScanning
                                    else -> state.remain
                                }
                            }
                            emptyList()
                        }
                    } ?: run {
                        scanningStateRepo.takeAndChangeState { state ->
                            when(state) {
                                is ScanningState.Enabled.Scanning -> state.stopScanning
                                else -> state.remain
                            }
                        }
                        scanState.discoveredDevices
                    }
                }
                is ScanningState.NoBluetoothState -> {
                    emptyList()
                }
            }
        }
    }

    suspend fun startScanning(filter: Set<UUID> = emptySet()) {
        scanFilter.set(filter)
    }

    suspend fun stopScanning() {
        scanFilter.set(null)
    }

    suspend fun isScanning(): Boolean {
        return scanFilter.flow().first() != null && scanningStateRepo.flow().first() is ScanningState.Enabled.Scanning
    }

}

expect class BluetoothBuilder : Bluetooth.Builder

operator fun Flow<List<Device>>.get(identifier: Identifier) : Flow<Device?> {
    return this.map { devices ->
        devices.firstOrNull { it.identifier == identifier }
    }
}

fun Flow<Device?>.state() : Flow<DeviceState> {
    return this.flatMapLatest { device ->
        device?.flow() ?: emptyFlow()
    }
}

fun Flow<Device?>.services(): Flow<List<Service>> {
    return state().transformLatest { deviceState ->
            emit(
                when (deviceState) {
                    is DeviceState.Connected -> {
                        when (deviceState) {
                            is DeviceState.Connected.NoServices -> {
                                deviceState.startDiscovering()
                                emptyList()
                            }
                            is DeviceState.Connected.Idle -> deviceState.services
                            is DeviceState.Connected.HandlingAction -> deviceState.services
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
                is DeviceState.Disconnected -> deviceState.startConnecting()
                is DeviceState.Connected -> emit(Unit)
                else -> {}
            }
    }.first()
}

suspend fun Flow<Device?>.disconnect() {
    state().transformLatest { deviceState ->
            when (deviceState) {
                is DeviceState.Connected -> deviceState.startDisconnected()
                is DeviceState.Connecting -> deviceState.handleCancel()
                is DeviceState.Reconnecting -> deviceState.handleCancel()
                is DeviceState.Disconnected -> emit(Unit)
                is DeviceState.Disconnecting -> {}
            }
    }.first()
}

fun Flow<Device?>.info() : Flow<DeviceInfoImpl> {
    return state().transformLatest { deviceState ->
        emit(deviceState.deviceInfo)
    }
}

fun Flow<Device?>.advertisement() : Flow<AdvertisementData> {
    return this.info().map { it.advertisementData }.distinctUntilChanged()
}

fun Flow<Device?>.rssi() : Flow<Int> {
    return this.info().map { it.rssi }.distinctUntilChanged()
}

fun Flow<Device?>.distance(environmentalFactor: Double = 2.0, averageOver: Int = 5) : Flow<Double> {
    val lastNResults = mutableListOf<Double>()
    return this.info().map { deviceInfo ->
        while (lastNResults.size >= averageOver) {
            lastNResults.removeAt(0)
        }
        val distance = deviceInfo.distance(environmentalFactor)
        if (!distance.isNaN())
            lastNResults.add(distance)
        if (lastNResults.isNotEmpty())
            (lastNResults.reduce { acc, d -> acc + d }) / lastNResults.size.toDouble()
        else
            Double.NaN
    }
}


suspend fun Flow<Device?>.updateRssi() {
    state().transformLatest { deviceState ->
            when (deviceState) {
                is DeviceState.Connected -> {
                    deviceState.readRssi()
                    emit(Unit)
                }
                else -> {}
            }
    }.first()
}


@JvmName("getService")
operator fun Flow<List<Service>>.get(uuid: UUID) : Flow<Service?> {
    return this.map { services ->
        services.firstOrNull {
            it.uuid.uuidString == uuid.uuidString
        }
    }
}

fun Flow<Service?>.characteristics() : Flow<List<Characteristic>> {
    return this.mapLatest { service -> service?.characteristics ?: emptyList() }.distinctUntilChanged()
}

fun Flow<Characteristic?>.descriptors() : Flow<List<Descriptor>> {
    return this.mapLatest{ characteristic -> characteristic?.descriptors ?: emptyList() }.distinctUntilChanged()
}

@JvmName("getAttribute")
operator fun <T : Attribute<R, W>, R : DeviceAction.Read, W : DeviceAction.Write> Flow<List<T>>.get(uuid: UUID) : Flow<T?> {
    return this.map { attribute ->
        attribute.firstOrNull {
            it.uuid.uuidString == uuid.uuidString
        }
    }
}

fun <T : Attribute<R, W>, R : DeviceAction.Read, W : DeviceAction.Write> Flow<T?>.value() : Flow<ByteArray?> {
    return this.flatMapLatest{attribute ->
        attribute?.flow() ?: emptyFlow()
    }.distinctUntilChanged()
}