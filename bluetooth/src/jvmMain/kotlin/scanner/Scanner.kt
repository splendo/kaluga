package com.splendo.kaluga.bluetooth.scanner

import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.state.StateRepoAccesor
import kotlinx.coroutines.CoroutineScope

actual class Scanner(permissions: Permissions, stateRepoAccessor: StateRepoAccesor<ScanningState>, coroutineScope: CoroutineScope) : BaseScanner(permissions, stateRepoAccessor, coroutineScope) {

    class Builder(
        private val permissions: Permissions,
        override val autoEnableBluetooth: Boolean) : BaseScanner.Builder {
        override fun create(stateRepoAccessor: StateRepoAccesor<ScanningState>, coroutineScope: CoroutineScope): Scanner {
            return Scanner(permissions, stateRepoAccessor, coroutineScope)
        }
    }

    override fun scanForDevices(filter: Set<UUID>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stopScanning() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun startMonitoringBluetooth() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stopMonitoringBluetooth() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}