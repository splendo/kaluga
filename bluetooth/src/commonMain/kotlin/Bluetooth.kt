package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.scanner.BaseScanner
import com.splendo.kaluga.bluetooth.scanner.ScanningState
import com.splendo.kaluga.bluetooth.scanner.ScanningStateRepo
import com.splendo.kaluga.permissions.Permissions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transformLatest

class Bluetooth(private val scanningStateRepo: ScanningStateRepo, private val requestPermission: suspend () -> Unit) {

    class Builder(private val scannerBuilder: BaseScanner.Builder, private val permissions: Permissions) {

        var requestPermission = suspend {permissions.getBluetoothManager().requestPermissions()}

        fun setOnRequestPermission(onRequestPermissions:suspend  () -> Unit) : Builder {
            requestPermission = onRequestPermissions
            return this
        }

        fun build(): Bluetooth {
            return Bluetooth(ScanningStateRepo(scannerBuilder), requestPermission)
        }
    }

    suspend fun scan(filter: Set<UUID> = emptySet()): Flow<List<Device>> {
        var hasStartedScanning = false
        return scanningStateRepo.flow().transformLatest { scanState ->
              when(scanState) {

                is ScanningState.Enabled -> {
                    when (scanState) {
                        is ScanningState.Enabled.Idle -> scanState.startScanning(filter)
                        is ScanningState.Enabled.Scanning -> {
                            if (scanState.filter != filter)
                                scanState.stopScanning()
                            
                        }
                    }
                    emit(scanState.discoveredDevices)
                }
                is ScanningState.NoBluetoothState -> {
                    when (scanState) {
                        is ScanningState.NoBluetoothState.MissingPermissions ->
                            requestPermission()
                    }
                }
            }
        }
    }

    Bluetooth.scan().collect()
   Bluetooth.scan(listOf(UUID)).collect()

    ////

    Bluetooth.stopScanning()

}