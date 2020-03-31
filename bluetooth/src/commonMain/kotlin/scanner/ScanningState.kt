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

import com.splendo.kaluga.base.MainQueueDispatcher
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.AdvertisementData
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.state.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlin.coroutines.CoroutineContext

sealed class ScanningState(private val scanner: BaseScanner) : State<ScanningState>() {

    companion object {
        const val TAG = "BluetoothManager"
    }

    fun logError(error: Error) {
        error.message?.let { com.splendo.kaluga.logging.error(TAG, it) }
    }

    @InternalCoroutinesApi
    override suspend fun initialState() {
        scanner.startMonitoringPermissions()
    }

    override suspend fun finalState() {
        scanner.stopMonitoringPermissions()
    }

    interface Permitted : HandleBeforeOldStateIsRemoved<ScanningState>, HandleAfterNewStateIsSet<ScanningState> {
        val revokePermission: suspend () -> NoBluetoothState.MissingPermissions
        suspend fun initialState()
        suspend fun finalState()
    }

    class PermittedHandler(private val scanner: BaseScanner) : Permitted {

        override val revokePermission: suspend () -> NoBluetoothState.MissingPermissions = {
            NoBluetoothState.MissingPermissions(scanner)
        }

        override suspend fun afterNewStateIsSet(newState: ScanningState) {
            when (newState) {
                is NoBluetoothState.MissingPermissions -> scanner.stopMonitoringBluetooth()
                else -> {}
            }
        }

        override suspend fun beforeOldStateIsRemoved(oldState: ScanningState) {
            when (oldState) {
                is NoBluetoothState.MissingPermissions -> scanner.startMonitoringBluetooth()
                else -> {}
            }
        }

        override suspend fun initialState() {
            scanner.startMonitoringBluetooth()
        }

        override suspend fun finalState() {
            scanner.stopMonitoringBluetooth()
        }
    }


    sealed class Enabled(val discoveredDevices: List<Device>, private val scanner: BaseScanner) : ScanningState(scanner), Permitted {

        private val permittedHandler = PermittedHandler(scanner)


        override val revokePermission: suspend () -> NoBluetoothState.MissingPermissions = permittedHandler.revokePermission

        val disable = suspend {
            NoBluetoothState.Disabled(scanner)
        }

        @InternalCoroutinesApi
        override suspend fun initialState() {
            super.initialState()

            permittedHandler.initialState()
        }

        override suspend fun finalState() {
            super.finalState()

            permittedHandler.finalState()
        }

        override suspend fun afterNewStateIsSet(newState: ScanningState) {
            permittedHandler.afterNewStateIsSet(newState)
        }

        override suspend fun beforeOldStateIsRemoved(oldState: ScanningState) {
            permittedHandler.beforeOldStateIsRemoved(oldState)
        }

        class Idle internal constructor(discoveredDevices: List<Device>,
                                        internal val oldFilter: Set<UUID>,
                                        private val scanner: BaseScanner
        )  : Enabled(discoveredDevices, scanner) {

            fun startScanning(filter: Set<UUID>) : suspend () -> Scanning {
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

            suspend fun discoverDevice(identifier: Identifier, advertisementData: AdvertisementData, deviceCreator: () -> Device) : suspend () -> ScanningState {
                return discoveredDevices.firstOrNull { it.identifier == identifier }?.let { knownDevice ->
                    if (!knownDevice.flowable.isInitialized()) {
                        knownDevice.flowable.value
                    }
                    knownDevice.takeAndChangeState { state ->
                        state.advertisementDataDidUpdate(advertisementData)
                    }
                    remain
                } ?: run {
                    suspend {
                        val newDevices = listOf(*discoveredDevices.toTypedArray(), deviceCreator())
                        Scanning(newDevices, filter, scanner)
                    }
                }
            }

            val stopScanning = suspend {
                Idle(discoveredDevices, filter, scanner)
            }

            override suspend fun afterOldStateIsRemoved(oldState: ScanningState) {
                super.afterNewStateIsSet(oldState)
                when (oldState) {
                    !is Scanning -> scanner.scanForDevices(filter)
                    else -> {}
                }
            }

            override suspend fun afterCreatingNewState(newState: ScanningState) {
                when (newState) {
                    !is Scanning -> {
                        scanner.stopScanning()
                    }
                    else -> {}
                }
            }

            @InternalCoroutinesApi
            override suspend fun initialState() {
                super.initialState()

                scanner.scanForDevices(filter)
            }

            override suspend fun finalState() {
                super.finalState()

                scanner.stopScanning()
            }
        }

    }

    sealed class NoBluetoothState constructor(scanner: BaseScanner) : ScanningState(scanner) {

        class Disabled internal constructor(private val scanner: BaseScanner) : NoBluetoothState(scanner), Permitted {

            val enable : suspend () -> Enabled = {
                Enabled.Idle(emptyList(), emptySet(), scanner)
            }

            private val permittedHandler = PermittedHandler(scanner)

            override val revokePermission: suspend () -> MissingPermissions = permittedHandler.revokePermission

            @InternalCoroutinesApi
            override suspend fun initialState() {
                super.initialState()

                permittedHandler.initialState()
                if(scanner.autoEnableBluetooth)
                    scanner.requestBluetoothEnable()
            }

            override suspend fun finalState() {
                super.finalState()

                permittedHandler.finalState()
            }

            override suspend fun afterNewStateIsSet(newState: ScanningState) {
                permittedHandler.afterNewStateIsSet(newState)
            }

            override suspend fun beforeOldStateIsRemoved(oldState: ScanningState) {
                permittedHandler.beforeOldStateIsRemoved(oldState)
                when (oldState) {
                    !is Disabled -> if(scanner.autoEnableBluetooth) scanner.requestBluetoothEnable()
                    else -> {}
                }
            }

        }

        class MissingPermissions internal constructor(private val scanner: BaseScanner) : NoBluetoothState(scanner) {

            fun permit(enabled: Boolean) : suspend () -> ScanningState = {
                if (enabled) Enabled.Idle(emptyList(), emptySet(), scanner) else Disabled(scanner)
            }

        }

    }

}

class ScanningStateRepo(permissions: Permissions,
                        connectionSettings: ConnectionSettings,
                        autoRequestPermission: Boolean,
                        autoEnableBluetooth: Boolean,
                        builder: BaseScanner.Builder) : ColdStateRepo<ScanningState>() {

    private val scanner = builder.create(permissions, connectionSettings, autoRequestPermission, autoEnableBluetooth,this)

    private var lastDevices: List<Device> = emptyList()
    private var lastFilter: Set<UUID> = emptySet()

    override suspend fun initialValue(): ScanningState {
        val state: ScanningState = if (!scanner.isPermitted()) {
            ScanningState.NoBluetoothState.MissingPermissions(scanner)
        } else if (!scanner.isBluetoothEnabled()) {
            ScanningState.NoBluetoothState.Disabled(scanner)
        } else {
            ScanningState.Enabled.Idle(lastDevices, lastFilter, scanner)
        }
        lastDevices = emptyList()
        lastFilter = emptySet()
        return state
    }

    override suspend fun deinitialize(state: ScanningState) {
        when (state) {
            is ScanningState.Enabled -> {
                lastDevices = state.discoveredDevices
                lastFilter = when (state) {
                    is ScanningState.Enabled.Idle -> state.oldFilter
                    is ScanningState.Enabled.Scanning -> state.filter
                }
            }
            else ->{}
        }
    }
}