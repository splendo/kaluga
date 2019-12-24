package com.splendo.kaluga.bluetooth.scanner

import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceInfoHolder
import com.splendo.kaluga.log.LogLevel
import com.splendo.kaluga.log.logger
import com.splendo.kaluga.permissions.Support
import com.splendo.kaluga.state.State
import com.splendo.kaluga.state.StateRepo
import com.splendo.kaluga.state.StateRepoAccesor

sealed class ScanningState(private val scanner: BaseScanner) : State<ScanningState>(scanner.stateRepoAccessor) {

    companion object {
        val tag = "BluetoothManager"
    }

    fun logError(error: Error) {
        error.message?.let { logger().log(LogLevel.ERROR, tag, it) }
    }

    sealed class Enabled constructor(val discoveredDevices: List<Device>, private val scanner: BaseScanner) : ScanningState(scanner) {

        suspend fun disable() {
            changeState(NoBluetoothState.Disabled(scanner))
        }

        suspend fun removePermissions() {
            changeState(NoBluetoothState.MissingPermissions(scanner))
        }

        override suspend fun initialState() {
            super.initialState()

            scanner.startMonitoringBluetooth()
        }

        override suspend fun finalState() {
            super.finalState()

            scanner.stopMonitoringBluetooth()
        }

        class Idle internal constructor(discoveredDevices: List<Device>,
                                        internal val oldFilter: Set<UUID>,
                                        private val scanner: BaseScanner
        )  : Enabled(discoveredDevices, scanner) {

            suspend fun startScanning(filter: Set<UUID>) {
                val devices = if (filter == oldFilter)
                    discoveredDevices
                else
                    emptyList()
                changeState(Scanning(devices, filter, scanner))
            }

        }

        class Scanning internal constructor(discoveredDevices: List<Device>,
                                            internal val filter: Set<UUID>,
                                            private val scanner: BaseScanner
        ) : Enabled(discoveredDevices, scanner) {

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

    sealed class NoBluetoothState constructor(private val scanner: BaseScanner) : ScanningState(scanner) {

        internal suspend fun checkAvailability() {
            val newState = when (scanner.permissions.getBluetoothManager().checkSupport()) {
                Support.POWER_ON -> Enabled.Idle(emptyList(), emptySet(), scanner)
                Support.NOT_SUPPORTED, Support.UNAUTHORIZED -> MissingPermissions(scanner)
                else -> Disabled(scanner)
            }
            changeState(newState)
        }

        class Disabled internal constructor(private val scanner: BaseScanner) : NoBluetoothState(scanner) {

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

        class MissingPermissions internal constructor(private val scanner: BaseScanner) : NoBluetoothState(scanner) {

            suspend fun givePermissions() {
                checkAvailability()
            }

            override suspend fun afterNewStateIsSet() {
                super.afterNewStateIsSet()

                when (scanner.stateRepoAccessor.currentState()) {
                    !is MissingPermissions -> scanner.startMonitoringBluetooth()
                }
            }

            override suspend fun afterOldStateIsRemoved(oldState: ScanningState) {
                super.afterOldStateIsRemoved(oldState)

                when (scanner.stateRepoAccessor.currentState()) {
                    !is MissingPermissions -> scanner.stopMonitoringBluetooth()
                }
            }

        }

    }

}

class ScanningStateRepo(builder: BaseScanner.Builder) : StateRepo<ScanningState>() {

    val manager = builder.create(StateRepoAccesor(this), this)

    override fun initialState(): ScanningState {
        return when (manager.permissions.getBluetoothManager().checkSupport()) {
            Support.POWER_ON -> {
                manager.startMonitoringBluetooth()
                ScanningState.Enabled.Idle(emptyList(), emptySet(), manager)
            }
            Support.UNAUTHORIZED, Support.NOT_SUPPORTED -> ScanningState.NoBluetoothState.MissingPermissions(manager)
            Support.POWER_OFF, Support.RESETTING -> {
                manager.startMonitoringBluetooth()
                ScanningState.NoBluetoothState.Disabled(manager)
            }
        }
    }

}