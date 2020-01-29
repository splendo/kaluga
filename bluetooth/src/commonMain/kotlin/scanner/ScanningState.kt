/*
 Copyright (c) 2020. Splendo Consulting B.V. The Netherlands

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 */

package com.splendo.kaluga.bluetooth.scanner

import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceInfoHolder
import com.splendo.kaluga.log.LogLevel
import com.splendo.kaluga.log.logger
import com.splendo.kaluga.permissions.Permit
import com.splendo.kaluga.permissions.Support
import com.splendo.kaluga.state.*
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

sealed class ScanningState(private val scanner: BaseScanner) : State<ScanningState>() {

    companion object {
        const val TAG = "BluetoothManager"
    }

    fun logError(error: Error) {
        error.message?.let { logger().log(LogLevel.ERROR, TAG, it) }
    }

    sealed class Enabled constructor(val discoveredDevices: List<Device>, private val scanner: BaseScanner) : ScanningState(scanner) {

        fun disable() : suspend () -> NoBluetoothState.Disabled {
            return {NoBluetoothState.Disabled(scanner)}
        }

        fun removePermissions() : suspend () -> NoBluetoothState.MissingPermissions {
            return {NoBluetoothState.MissingPermissions(scanner)}
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

            fun startScanning(filter: Set<UUID>) : () -> Scanning {
                return {
                    val devices = if (filter == oldFilter)
                        discoveredDevices
                    else
                        emptyList()
                    Scanning(devices, filter, scanner)
                }
            }

        }

        class Scanning internal constructor(discoveredDevices: List<Device>,
                                            internal val filter: Set<UUID>,
                                            private val scanner: BaseScanner
        ) : Enabled(discoveredDevices, scanner), HandleAfterOldStateIsRemoved<ScanningState>, HandleAfterCreating<ScanningState> {

            fun discoverDevices(vararg devices: Device) : suspend () -> Scanning {
                return {
                    val newDevices = listOf(*discoveredDevices.toTypedArray(), *devices)
                    Scanning(newDevices, filter, scanner)
                }
            }

            fun stopScanning() : suspend () -> Idle {
                return {Idle(discoveredDevices, filter, scanner)}
            }

            override suspend fun afterOldStateIsRemoved(oldState: ScanningState) {
                when (oldState) {
                    !is Scanning -> scanner.scanForDevices(filter)
                }
            }

            override suspend fun afterCreatingNewState(newState: ScanningState) {
                when (newState) {
                    !is Scanning -> {
                        scanner.stopScanning()
                    }
                }
            }

            override suspend fun finalState() {
                super.finalState()

                scanner.stopScanning()
            }
        }

    }

    sealed class NoBluetoothState constructor(private val scanner: BaseScanner) : ScanningState(scanner) {

        internal fun checkAvailability() : suspend () -> ScanningState {
            return {
                when (scanner.permissions.getBluetoothManager().checkSupport()) {
                    Support.POWER_ON -> {
                        when (scanner.permissions.getBluetoothManager().checkPermit()) {
                            Permit.ALLOWED -> Enabled.Idle(emptyList(), emptySet(), scanner)
                            else -> MissingPermissions(scanner)
                        }
                    }
                    Support.NOT_SUPPORTED, Support.UNAUTHORIZED -> MissingPermissions(scanner)
                    else -> Disabled(scanner)
                }
            }
        }

        class Disabled internal constructor(private val scanner: BaseScanner) : NoBluetoothState(scanner) {

            fun enable(): suspend () -> ScanningState {
                return checkAvailability()
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

        class MissingPermissions internal constructor(private val scanner: BaseScanner) : NoBluetoothState(scanner), HandleAfterNewStateIsSet<ScanningState>, HandleAfterOldStateIsRemoved<ScanningState> {

            fun givePermissions() : suspend () -> ScanningState {
                return checkAvailability()
            }

            override suspend fun afterNewStateIsSet(newState: ScanningState) {
                when (newState) {
                    !is MissingPermissions -> scanner.startMonitoringBluetooth()
                }
            }

            override suspend fun afterOldStateIsRemoved(oldState: ScanningState) {
                when (oldState) {
                    !is MissingPermissions -> scanner.stopMonitoringBluetooth()
                }
            }

        }

    }

}

class ScanningStateRepo(builder: BaseScanner.Builder, coroutineContext: CoroutineContext = Dispatchers.Main) : ColdStateRepo<ScanningState>(coroutineContext) {

    private val manager = builder.create(this)

    private var lastDevices: List<Device> = emptyList()
    private var lastFilter: Set<UUID> = emptySet()

    override fun initialValue(): ScanningState {
        val state = when (manager.permissions.getBluetoothManager().checkSupport()) {
            Support.POWER_ON -> {
                when (manager.permissions.getBluetoothManager().checkPermit()) {
                    Permit.ALLOWED -> {
                        manager.startMonitoringBluetooth()
                        ScanningState.Enabled.Idle(lastDevices, lastFilter, manager)
                    }
                    else -> ScanningState.NoBluetoothState.MissingPermissions(manager)
                }

            }
            Support.UNAUTHORIZED, Support.NOT_SUPPORTED -> ScanningState.NoBluetoothState.MissingPermissions(manager)
            Support.POWER_OFF, Support.RESETTING -> {
                manager.startMonitoringBluetooth()
                ScanningState.NoBluetoothState.Disabled(manager)
            }
        }
        lastDevices = emptyList()
        lastFilter = emptySet()
        return state
    }

    override fun deinitialize(state: ScanningState) {
        when (state) {
            is ScanningState.Enabled -> {
                lastDevices = state.discoveredDevices
                lastFilter = when (state) {
                    is ScanningState.Enabled.Idle -> state.oldFilter
                    is ScanningState.Enabled.Scanning -> state.filter
                }
            }
        }
    }
}