package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceInfoHolder
import com.splendo.kaluga.bluetooth.scanner.BaseScanner
import com.splendo.kaluga.bluetooth.scanner.ScanningState
import com.splendo.kaluga.bluetooth.scanner.ScanningStateRepo
import com.splendo.kaluga.permissions.Permissions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transformLatest

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

    suspend fun scan(filter: Set<UUID> = emptySet()): Flow<List<Device>> {
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

}