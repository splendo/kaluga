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

import com.splendo.kaluga.base.flow.HotFlowable
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.scanner.BaseScanner
import com.splendo.kaluga.bluetooth.scanner.ScanningState
import com.splendo.kaluga.bluetooth.scanner.ScanningStateRepo
import com.splendo.kaluga.permissions.BasePermissions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.JvmName

class Bluetooth internal constructor(private val builder: Builder, coroutineContext: CoroutineContext = Dispatchers.Main) {

    class Builder(internal val scannerBuilder: BaseScanner.Builder, private val permissions: BasePermissions) {

        var requestPermission = suspend {permissions.getBluetoothManager().requestPermissions()}
        var notifyBluetoothDisabled = suspend {}

        fun setOnRequestPermission(onRequestPermissions:suspend  () -> Unit) : Builder {
            requestPermission = onRequestPermissions
            return this
        }

        fun setOnNotifyBluetoothDisabled(onBluetoothDisabled: suspend () -> Unit) : Builder {
            notifyBluetoothDisabled = onBluetoothDisabled
            return this
        }

        fun build(): Bluetooth {
            return Bluetooth(this)
        }
    }

    internal val scanningStateRepo = ScanningStateRepo(builder.scannerBuilder, coroutineContext)
    private val requestPermission = builder.requestPermission
    private val notifyBluetoothDisabled = builder.notifyBluetoothDisabled

    private val scanFilter = HotFlowable<Set<UUID>?>(null)

    @ExperimentalCoroutinesApi
    suspend fun devices(): Flow<List<Device>> {
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
                    handleNoBluetooth(scanState)
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

    private suspend fun handleNoBluetooth(noBluetoothState: ScanningState.NoBluetoothState) {
        when (noBluetoothState) {
            is ScanningState.NoBluetoothState.MissingPermissions ->
                requestPermission()
            is ScanningState.NoBluetoothState.Disabled ->
                notifyBluetoothDisabled()
        }
    }

}

operator fun Flow<List<Device>>.get(identifier: Identifier) : Flow<Device?> {
    return this.map { devices ->
        devices.firstOrNull { it.identifier == identifier }
    }
}

suspend fun Flow<Device?>.services(): Flow<List<Service>> {
    return this.mapDeviceState { deviceState ->
            emit(
                when (deviceState) {
                    is DeviceState.Connected -> {
                        when (deviceState) {
                            is DeviceState.Connected.Idle -> {
                                if (deviceState.services.isEmpty())
                                    deviceState.startDiscovering()
                            }
                        }
                        deviceState.services
                    }
                    else -> emptyList()
                }
            )
        }
}

suspend fun Flow<Device?>.connect() {
    this.mapDeviceState<Unit> { deviceState ->
            when (deviceState) {
                is DeviceState.Disconnected -> deviceState.startConnecting()
                is DeviceState.Connected -> emit(Unit)
            }
    }.first()
}

suspend fun Flow<Device?>.disconnect() {
    this.mapDeviceState<Unit> { deviceState ->
            when (deviceState) {
                is DeviceState.Connected -> deviceState.handleDisconnect()
                is DeviceState.Connecting -> deviceState.handleConnect()
                is DeviceState.Reconnecting -> deviceState.handleCancel()
                is DeviceState.Disconnected -> emit(Unit)
            }
    }.first()
}

suspend fun Flow<Device?>.rssi() : Flow<Int> {
    return this.mapDeviceState { deviceState ->
            emit(deviceState.lastKnownRssi)
        }
}

suspend fun Flow<Device?>.updateRssi() {
    this.mapDeviceState<Unit> { deviceState ->
            when (deviceState) {
                is DeviceState.Connected -> {
                    deviceState.readRssi()
                    emit(Unit)
                }
            }
    }.first()
}

fun <T> Flow<Device?>.mapDeviceState(transform: suspend FlowCollector<T>.(value: DeviceState) -> Unit) : Flow<T> {
    return this.flatMapLatest { device ->
        device?.flow()?.transformLatest(transform) ?: emptyFlow()
    }
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
    return this.mapLatest { service -> service?.characteristics ?: emptyList() }
}

fun Flow<Characteristic?>.descriptors() : Flow<List<Descriptor>> {
    return this.mapLatest{ characteristic -> characteristic?.descriptors ?: emptyList() }
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
    }
}