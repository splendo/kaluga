package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.log.LogLevel
import com.splendo.kaluga.log.logger
import com.splendo.kaluga.state.State
import com.splendo.kaluga.state.StateRepo

sealed class Bluetooth(stateRepo: StateRepo<Bluetooth>) : State<Bluetooth>(stateRepo) {
    data class MissingPermissions internal constructor(internal val shouldScan: Boolean, internal val filter: Set<UUID>, private val stateRepo: StateRepo<Bluetooth>) : Bluetooth(stateRepo)
    data class DisabledBluetooth internal constructor(internal val shouldScan: Boolean, internal val filter: Set<UUID>, private val stateRepo: StateRepo<Bluetooth>) : Bluetooth(stateRepo)
    data class Idle internal constructor(val discoveredDevices: List<Device>, internal val filter: Set<UUID>, private val stateRepo: StateRepo<Bluetooth>) : Bluetooth(stateRepo)
    data class Scanning internal constructor(val discoveredDevices: List<Device>, internal val filter: Set<UUID>, private val stateRepo: StateRepo<Bluetooth>) : Bluetooth(stateRepo)
}

abstract class BaseBluetoothManager : StateRepo<Bluetooth>() {

    companion object {
        val tag = "BluetoothManager"
    }

    override fun initialState(): Bluetooth {
        return Bluetooth.Idle(emptyList(), emptySet(), this)
    }

    internal suspend fun scan(filter: Set<UUID> = emptySet()) {
        handleStartScanning(filter)
        changeState { state ->
            when (state) {
                is Bluetooth.MissingPermissions -> state.copy(true, filter)
                is Bluetooth.DisabledBluetooth -> state.copy(true, filter)
                is Bluetooth.Idle -> {
                    val devices = if (filter == state.filter)
                        state.discoveredDevices
                    else
                        emptyList()
                    Bluetooth.Scanning(devices, filter, this)
                }
                is Bluetooth.Scanning -> {
                    if (state.filter == filter)
                        state.copy()
                    else {
                        handleStopScanning()
                        val devices = if (filter.isEmpty()) state.discoveredDevices else emptyList()
                        Bluetooth.Scanning(devices, filter, this)
                    }

                }
            }
        }
    }

    suspend fun stopScanning() {
        handleStopScanning()
        changeState { state ->
            when (state) {
                is Bluetooth.MissingPermissions -> state.copy(false)
                is Bluetooth.DisabledBluetooth -> state.copy(false)
                is Bluetooth.Idle -> state.copy()
                is Bluetooth.Scanning -> Bluetooth.Idle(state.discoveredDevices, state.filter, this)
            }
        }
    }

    suspend fun discoveredDevice(device: Device) {
        changeState { state ->
            when (state) {
                is Bluetooth.Scanning -> {
                    val newDevicesList = listOf(*state.discoveredDevices.toTypedArray(), device)
                    state.copy(newDevicesList)
                }
                is Bluetooth.MissingPermissions -> state.copy()
                is Bluetooth.DisabledBluetooth -> state.copy()
                is Bluetooth.Idle -> state.copy()
            }
        }
    }

    internal fun scanFailedWithError(error: Error, stopScanning: Boolean) {
        logger().log(LogLevel.ERROR, tag, "Scanning Failed. Reason: ${error.message}")

        if (stopScanning)
            runBlocking { stopScanning() }
    }

    abstract fun handleStartScanning(filter: Set<UUID>)
    abstract fun handleStopScanning()

}

expect class BluetoothManager : BaseBluetoothManager

