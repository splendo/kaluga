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

import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.scanner.BaseScanner
import com.splendo.kaluga.bluetooth.scanner.ScanningState
import com.splendo.kaluga.bluetooth.scanner.ScanningStateRepo
import com.splendo.kaluga.permissions.Permissions
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlin.jvm.JvmName

class Bluetooth internal constructor(private val builder: Builder) {

    class Builder(internal val scannerBuilder: BaseScanner.Builder, private val permissions: Permissions) {

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

    val scanningStateRepo = ScanningStateRepo(builder.scannerBuilder)
    val requestPermission = builder.requestPermission
    val notifyBluetoothDisabled = builder.notifyBluetoothDisabled

    suspend fun startScanning(filter: Set<UUID> = emptySet()) {
        scanningStateRepo.flow().transformLatest{ scanState ->
            when(scanState) {
                is ScanningState.Enabled -> {
                    when (scanState) {
                        is ScanningState.Enabled.Idle -> scanState.startScanning(filter)
                        is ScanningState.Enabled.Scanning -> {
                            if (filter == scanState.filter)
                                emit(Unit)
                            else
                                scanState.stopScanning()
                        }
                    }
                }
                is ScanningState.NoBluetoothState -> handleNoBluetooth(scanState)
            }
        }.first()
    }

    suspend fun devices(): Flow<List<Device>> {
        return scanningStateRepo.flow().transformLatest { scanState ->
            when(scanState) {
                is ScanningState.Enabled -> {
                    emit(scanState.discoveredDevices)
                }
                is ScanningState.NoBluetoothState -> handleNoBluetooth(scanState)
            }

            emit(emptyList())
        }
    }

    suspend fun stopScanning() {
        scanningStateRepo.flow().transformLatest { scanState ->
            when(scanState) {
                is ScanningState.Enabled.Scanning -> scanState.stopScanning()
                is ScanningState.Enabled.Idle -> emit(Unit)
            }
        }.first()
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

fun Flow<List<Device>>.get(identifier: Identifier) : Flow<Device?> {
    return this.map { devices ->
        devices.firstOrNull { it.identifier == identifier }
    }
}

suspend fun Flow<Device?>.services(): Flow<List<Service>> {
    return this.mapDeviceState {deviceState ->
        emit(when (deviceState) {
            is DeviceState.Connected -> {
                when (deviceState) {
                    is DeviceState.Connected.Idle -> {
                        if (deviceState.services.isEmpty())
                            deviceState.discoverServices()
                    }
                }
                deviceState.services
            }
            else -> emptyList()
        })
    }
}

suspend fun Flow<Device?>.connect() {
    this.mapDeviceState<Unit> {deviceState ->
        when (deviceState) {
            is DeviceState.Disconnected -> deviceState.connect()
            is DeviceState.Connected -> emit(Unit)
        }
    }.first()
}

suspend fun Flow<Device?>.disconnect() {
    this.mapDeviceState<Unit> {deviceState ->
            when (deviceState) {
                is DeviceState.Connected -> deviceState.disconnect()
                is DeviceState.Disconnected -> emit(Unit)
            }
    }.first()
}

suspend fun Flow<Device?>.rssi(interval: Long? = 1000) : Flow<Int> {
    return this.mapDeviceState {deviceState ->
        emit(deviceState.lastKnownRssi)
        interval?.let {
            delay(it)
        }
        when (deviceState) {
            is DeviceState.Connected -> deviceState.readRssi()
        }
    }
}

fun <T> Flow<Device?>.mapDeviceState(transform: suspend FlowCollector<T>.(value: DeviceState) -> Unit) : Flow<T> {
    return this.flatMapLatest { device ->
        device?.flow()?.transformLatest(transform) ?: emptyFlow()
    }
}

@JvmName("getService")
fun Flow<List<Service>>.get(uuid: UUID) : Flow<Service?> {
    return this.mapLatest { services ->
        services.firstOrNull { it.uuid.uuidString == uuid.uuidString }
    }
}

fun Flow<Service?>.characteristics() : Flow<List<Characteristic>> {
    return this.mapLatest { service -> service?.characteristics ?: emptyList() }
}

fun Flow<Characteristic?>.descriptors() : Flow<List<Descriptor>> {
    return this.mapLatest{ characteristic -> characteristic?.descriptors ?: emptyList() }
}

@JvmName("getAttribute")
fun <T : Attribute<R, W>, R : DeviceAction.Read, W : DeviceAction.Write> Flow<List<T>>.get(uuid: UUID) : Flow<T?> {
    return this.mapLatest{attribute ->
        attribute.firstOrNull {it.uuid.uuidString == uuid.uuidString}
    }
}

fun <T : Attribute<R, W>, R : DeviceAction.Read, W : DeviceAction.Write> Flow<T?>.value() : Flow<ByteArray?> {
    return this.flatMapLatest{attribute -> attribute?.flow() ?: emptyFlow()}
}