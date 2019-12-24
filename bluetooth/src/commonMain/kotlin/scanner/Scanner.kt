package com.splendo.kaluga.bluetooth.scanner

import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.BaseDeviceConnectionManager
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.state.StateRepoAccesor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseScanner internal constructor(internal val permissions: Permissions,
                                                internal val stateRepoAccessor: StateRepoAccesor<ScanningState>,
                                                coroutineScope: CoroutineScope)
    : CoroutineScope by coroutineScope {

    interface Builder {
        val autoEnableBluetooth: Boolean
        fun create(stateRepoAccessor: StateRepoAccesor<ScanningState>, coroutineScope: CoroutineScope): Scanner
    }

    internal abstract fun scanForDevices(filter: Set<UUID>)
    internal abstract fun stopScanning()
    internal abstract fun startMonitoringBluetooth()
    internal abstract fun stopMonitoringBluetooth()

    internal fun bluetoothEnabled() {
        launch {
            when (val state = stateRepoAccessor.currentState()) {
                is ScanningState.NoBluetoothState.Disabled -> state.enable()
            }
        }
    }

    internal fun bluetoothDisabled() {
        launch {
            when (val state = stateRepoAccessor.currentState()) {
                is ScanningState.Enabled -> state.disable()
            }
        }
    }

}

expect class Scanner : BaseScanner