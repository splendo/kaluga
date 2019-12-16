package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.log.LogLevel
import com.splendo.kaluga.log.logger
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.Support
import com.splendo.kaluga.state.State
import com.splendo.kaluga.state.StateRepo
import com.splendo.kaluga.state.StateRepoAccesor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

sealed class ScanningState(private val scanner: BaseBluetoothScanner) : State<ScanningState>(scanner.stateRepoAccesor) {

    companion object {
        val tag = "BluetoothManager"
    }

    protected suspend fun changeState(toState: ScanningState) {
        scanner.stateRepoAccesor.s.changeState {
            if (it === this)
                toState
            else
                it
            }
    }

    fun logError(error: Error) {
        error.message?.let { logger().log(LogLevel.ERROR, tag, it) }
    }

    open class Enabled internal constructor(val discoveredDevices: List<Device>, private val scanner: BaseBluetoothScanner) : ScanningState(scanner) {

        suspend fun disable() {
            changeState(Disabled(scanner))
        }

        suspend fun removePermissions() {
            changeState(MissingPermissions(scanner))
        }

        override suspend fun initialState() {
            super.initialState()

            scanner.startMonitoringBluetooth()
        }

        override suspend fun finalState() {
            super.finalState()

            scanner.stopMonitoringBluetooth()
        }
    }

     open class NoBluetoothState internal constructor(private val scanner: BaseBluetoothScanner) : ScanningState(scanner) {

        internal suspend fun checkAvailability() {
            val newState = when(scanner.permissions.getBluetoothManager().checkSupport()) {
                Support.POWER_ON -> Idle(emptyList(), emptySet(), scanner)
                Support.NOT_SUPPORTED, Support.UNAUTHORIZED -> MissingPermissions(scanner)
                else -> Disabled(scanner)
            }
            changeState(newState)
        }

    }

    class Disabled internal constructor(private val scanner: BaseBluetoothScanner) : NoBluetoothState(scanner) {

        suspend fun enable() {
            checkAvailability()
        }

        override suspend fun initialState() {
            super.initialState()

            scanner.startMonitoringBluetooth()
        }

        override suspend fun finalState() {
            super.finalState()

            scanner.stopMonitoringBluetooth()
        }
    }

    class MissingPermissions internal constructor(private val scanner: BaseBluetoothScanner) : NoBluetoothState(scanner) {

        suspend fun givePermissions() {
            checkAvailability()
        }

        override suspend fun afterNewStateIsSet() {
            super.afterNewStateIsSet()

            when (scanner.stateRepoAccesor.currentState()) {
                !is MissingPermissions -> scanner.startMonitoringBluetooth()
            }
        }

        override suspend fun afterOldStateIsRemoved(oldState: ScanningState) {
            super.afterOldStateIsRemoved(oldState)

            when (scanner.stateRepoAccesor.currentState()) {
                !is MissingPermissions -> scanner.stopMonitoringBluetooth()
            }
        }

    }

    class Idle internal constructor(discoveredDevices: List<Device>,
                                    private val oldFilter: Set<UUID>,
                                    private val scanner: BaseBluetoothScanner)  : Enabled(discoveredDevices, scanner) {

        suspend fun startScanning(filter: Set<UUID>) {
            val devices = if (filter == oldFilter)
                discoveredDevices
            else
                emptyList()
            changeState(Scanning(devices, filter, scanner))
        }

    }

    class Scanning internal constructor(discoveredDevices: List<Device>,
                                        private val filter: Set<UUID>,
                                        private val scanner: BaseBluetoothScanner) : Enabled(discoveredDevices, scanner) {

        suspend fun discoverDevices(vararg devices: Device) {
            val newDevices = listOf(*discoveredDevices.toTypedArray(), *devices)
            changeState(Scanning(newDevices, filter, scanner))
        }

        suspend fun stopScanning() {
            changeState(Idle(discoveredDevices, filter, scanner))
        }

        override suspend fun afterOldStateIsRemoved(oldState: ScanningState) {
            super.afterOldStateIsRemoved(oldState)

            when (oldState) {
                !is Scanning -> scanner.scanForDevices(filter)
            }
        }

        override suspend fun afterCreatingNewState(newState: ScanningState) {
            super.afterCreatingNewState(newState)

            when (newState) {
                !is Scanning -> {
                    scanner.stopScanning()
                }
            }
        }
    }
}

class Scanner(builder: BaseBluetoothScanner.Builder) : StateRepo<ScanningState>() {

    val manager = builder.create(StateRepoAccesor(this), this)

    override fun initialState(): ScanningState {
        return when (manager.permissions.getBluetoothManager().checkSupport()) {
            Support.POWER_ON -> {
                manager.startMonitoringBluetooth()
                ScanningState.Idle(emptyList(), emptySet(), manager)
            }
            Support.UNAUTHORIZED, Support.NOT_SUPPORTED -> ScanningState.MissingPermissions(manager)
            Support.POWER_OFF, Support.RESETTING -> {
                manager.startMonitoringBluetooth()
                ScanningState.Disabled(manager)
            }
        }
    }

}

abstract class BaseBluetoothScanner(internal val permissions: Permissions, internal val stateRepoAccesor: StateRepoAccesor<ScanningState>, coroutineScope: CoroutineScope) : CoroutineScope by coroutineScope {

    interface Builder {
        fun create(stateRepoAccessor: StateRepoAccesor<ScanningState>, coroutineScope: CoroutineScope): BluetoothScanner
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

expect class BluetoothScanner : BaseBluetoothScanner

