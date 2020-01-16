package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.bluetooth.device.*
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

    suspend fun devices(filter: Set<UUID> = emptySet()): Flow<List<Device>> {
        var hasStartedScanning = false
        var hasStoppedScanning = false
        return scanningStateRepo.flow().transformLatest { scanState ->
            if (hasStoppedScanning)
                return@transformLatest
            when(scanState) {

                is ScanningState.Enabled -> {
                    when (scanState) {
                        is ScanningState.Enabled.Idle -> {
                            if (!hasStartedScanning) {
                                scanState.startScanning(filter)
                                hasStartedScanning = true
                            } else if (scanState.oldFilter == filter) {
                                emit(scanState.discoveredDevices)
                                return@transformLatest
                            }
                        }
                        is ScanningState.Enabled.Scanning -> {
                            if (scanState.filter != filter) {
                                scanState.stopScanning()
                                hasStoppedScanning = true
                            } else {
                                emit(scanState.discoveredDevices)
                                return@transformLatest
                            }
                        }
                    }
                }
                is ScanningState.NoBluetoothState -> {
                    when (scanState) {
                        is ScanningState.NoBluetoothState.MissingPermissions ->
                            requestPermission()
                        is ScanningState.NoBluetoothState.Disabled ->
                            notifyBluetoothDisabled()
                    }
                }
            }

            emit(emptyList())
        }
    }

    suspend fun stopScanning() {
        scanningStateRepo.flow().transformLatest { scanState ->
            when(scanState) {
                is ScanningState.Enabled.Scanning -> scanState.stopScanning()
                is ScanningState.Enabled.Idle -> emit(true)
            }
        }.first()
    }

}

fun Flow<List<Device>>.get(identifier: Identifier) : Flow<Device?> {
    return this.map { devices ->
        devices.firstOrNull { it.identifier == identifier }
    }
}

suspend fun Flow<Device?>.services(): Flow<List<Service>> {
    return this.mapDeviceState {deviceState ->
        when (deviceState) {
            is DeviceState.Connected -> {
                when (deviceState) {
                    is DeviceState.Connected.Idle -> {
                        if (deviceState.services.isEmpty())
                            deviceState.discoverServices()
                    }
                }
                emit(deviceState.services)
                return@mapDeviceState
            }
        }
        emit(emptyList())
    }
}

suspend fun Flow<Device?>.connect() {
    this.mapDeviceState<Boolean> {deviceState ->
        when (deviceState) {
            is DeviceState.Disconnected -> deviceState.connect()
            is DeviceState.Connected -> emit(true)
        }
    }.first()
}

suspend fun Flow<Device?>.disconnect() {
    this.mapDeviceState<Boolean> {deviceState ->
            when (deviceState) {
                is DeviceState.Connected -> deviceState.disconnect()
                is DeviceState.Disconnected -> emit(true)
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