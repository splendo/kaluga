package com.splendo.kaluga.bluetooth.scanner

import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.state.StateRepoAccesor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseScanner(internal val permissions: Permissions, internal val stateRepoAccesor: StateRepoAccesor<ScanningState>, coroutineScope: CoroutineScope) : CoroutineScope by coroutineScope {

    interface Builder {
        fun create(stateRepoAccessor: StateRepoAccesor<ScanningState>, coroutineScope: CoroutineScope): Scanner
    }

    internal abstract fun scanForDevices(filter: Set<UUID>)
    internal abstract fun stopScanning()
    internal abstract fun startMonitoringBluetooth()
    internal abstract fun stopMonitoringBluetooth()

    internal fun bluetoothEnabled() {
        launch {
            when (val state = stateRepoAccesor.currentState()) {
                is ScanningState.Disabled -> state.enable()
            }
        }
    }

    internal fun bluetoothDisabled() {
        launch {
            when (val state = stateRepoAccesor.currentState()) {
                is ScanningState.Enabled -> state.disable()
            }
        }
    }

}

expect class Scanner : BaseScanner