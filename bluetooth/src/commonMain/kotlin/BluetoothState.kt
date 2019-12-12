package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.log.LogLevel
import com.splendo.kaluga.log.logger
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.Support
import com.splendo.kaluga.state.State
import com.splendo.kaluga.state.StateRepo
import com.splendo.kaluga.state.StateRepoAccesor
import kotlinx.coroutines.CoroutineScope

sealed class BluetoothState(private val manager: BaseBluetoothManager) : State<BluetoothState>(manager.stateRepoAccesor) {

    companion object {
        val tag = "BluetoothManager"
    }

    protected suspend fun changeState(toState: BluetoothState) {
        manager.stateRepoAccesor.s.changeState {
            if (it === this)
                toState
            else
                it
            }
    }

    fun logError(error: Error) {
        error.message?.let { logger().log(LogLevel.ERROR, tag, it) }
    }

    open class Enabled internal constructor(private val manager: BaseBluetoothManager) : BluetoothState(manager) {

        suspend fun disable() {
            changeState(Disabled(manager))
        }

        suspend fun removePermissions() {
            changeState(MissingPermissions(manager))
        }

    }

     open class NoBluetoothState internal constructor(private val manager: BaseBluetoothManager) : BluetoothState(manager) {

        internal suspend fun checkAvailability() {
            val newState = when(manager.permissions.getBluetoothManager().checkSupport()) {
                Support.POWER_ON -> Idle(emptyList(), emptySet(), manager)
                Support.NOT_SUPPORTED, Support.UNAUTHORIZED -> MissingPermissions(manager)
                else -> Disabled(manager)
            }
            changeState(newState)
        }

    }

    class Disabled internal constructor(private val manager: BaseBluetoothManager) : NoBluetoothState(manager) {

        suspend fun enable() {
            checkAvailability()
        }

    }

    class MissingPermissions internal constructor(private val manager: BaseBluetoothManager) : NoBluetoothState(manager) {

        suspend fun givePermissions() {
            checkAvailability()
        }

    }

    class Idle internal constructor(val discoveredDevices: List<Device>,
                                    private val oldFilter: Set<UUID>,
                                    private val manager: BaseBluetoothManager)  : Enabled(manager) {

        suspend fun startScanning(filter: Set<UUID>) {
            val devices = if (filter == oldFilter)
                discoveredDevices
            else
                emptyList()
            changeState(Scanning(devices, filter, manager))
        }

    }

    class Scanning internal constructor(val discoveredDevices: List<Device>,
                                        private val filter: Set<UUID>,
                                        private val manager: BaseBluetoothManager) : Enabled(manager) {

        suspend fun discoverDevices(vararg devices: Device) {
            val newDevices = listOf(*discoveredDevices.toTypedArray(), *devices)
            changeState(Scanning(newDevices, filter, manager))
        }

        suspend fun stopScanning() {
            changeState(Idle(discoveredDevices, filter, manager))
        }

        override suspend fun afterOldStateIsRemoved(oldState: BluetoothState) {
            super.afterOldStateIsRemoved(oldState)

            when (oldState) {
                !is Scanning -> manager.scanForDevices(filter)
            }
        }

        override suspend fun afterCreatingNewState(newState: BluetoothState) {
            super.afterCreatingNewState(newState)

            when (newState) {
                !is Scanning -> {
                    manager.stopScanning()
                }
            }
        }
    }
}

class Bluetooth( builder: BaseBluetoothManager.Builder) : StateRepo<BluetoothState>() {

    val manager = builder.create(StateRepoAccesor(this), this)

    init {
        manager.startMonitoringBluetooth()
    }

    override fun initialState(): BluetoothState {
        return when (manager.permissions.getBluetoothManager().checkSupport()) {
            Support.POWER_ON -> BluetoothState.Idle(emptyList(), emptySet(), manager)
            Support.UNAUTHORIZED, Support.NOT_SUPPORTED -> BluetoothState.MissingPermissions(manager)
            Support.POWER_OFF, Support.RESETTING -> BluetoothState.Disabled(manager)
        }
    }

    override suspend fun cancel() {
        manager.stopMonitoringBluetooth()
    }

}

abstract class BaseBluetoothManager(internal val permissions: Permissions, internal val stateRepoAccesor: StateRepoAccesor<BluetoothState>) {

    interface Builder {
        fun create(stateRepoAccessor: StateRepoAccesor<BluetoothState>, coroutineScope: CoroutineScope): BluetoothManager
    }

    abstract fun scanForDevices(filter: Set<UUID>)
    abstract fun stopScanning()
    abstract fun startMonitoringBluetooth()
    abstract fun stopMonitoringBluetooth()

}

expect class BluetoothManager : BaseBluetoothManager

