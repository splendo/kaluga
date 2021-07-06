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

import com.splendo.kaluga.base.flow.SpecialFlowValue
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.BaseAdvertisementData
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.scanner.ScanningState.Initialized.Enabled.Scanning
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.state.ColdStateFlowRepo
import com.splendo.kaluga.state.HandleAfterCreating
import com.splendo.kaluga.state.HandleAfterOldStateIsRemoved
import com.splendo.kaluga.state.HandleBeforeOldStateIsRemoved
import com.splendo.kaluga.state.State
import com.splendo.kaluga.state.StateRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.coroutines.CoroutineContext

typealias Filter = Set<UUID>

sealed class ScanningState : State() {

    data class Discovered (
        val devices: List<Device>,
        internal val filter: Filter,
    ) {
        constructor(filter: Filter) : this(emptyList(), filter)

        fun copyAndAdd(device: Device): Discovered =
            Discovered(listOf(*devices.toTypedArray(), device), filter)

        fun discoveredForFilter(filter:Filter) =
            if (this.filter == filter)
                this
            else
                Discovered(filter)
    }

    companion object {
        val nothingDiscovered = Discovered(emptySet())
        const val TAG = "BluetoothManager"
    }

    fun logError(error: Error) {
        error.message?.let { com.splendo.kaluga.logging.error(TAG, it) }
    }

    sealed class Initialized(private val scanner: BaseScanner):ScanningState(),HandleBeforeOldStateIsRemoved<ScanningState> {

        override suspend fun beforeOldStateIsRemoved(oldState: ScanningState) {
            if (oldState is NotInitialized) {
                // Start monitoring Permissions and Bluetooth
                // Stop will be called from deinitChangeState
                scanner.startMonitoringPermissions()
                scanner.startMonitoringBluetooth()
            }
        }

        val revokePermission = suspend { NoBluetooth.MissingPermissions(scanner)}

        // TODO: verify better all below code is not needed any longer

//        interface Permitted : HandleBeforeOldStateIsRemoved<ScanningState>,
//            HandleAfterNewStateIsSet<ScanningState> {
//            suspend fun initialState()
//            suspend fun finalState()
//        }

//        class PermittedHandler(private val scanner: BaseScanner) : Permitted {

//            override val revokePermission: suspend () -> NoBluetooth.MissingPermissions = {
//                NoBluetooth.MissingPermissions(scanner)
//            }

//            override suspend fun afterNewStateIsSet(newState: ScanningState) {
//                when (newState) {
//                    // TODO: only Missing Permissions? Can't this be on that state?
//                    is NoBluetooth.MissingPermissions -> scanner.stopMonitoringBluetooth()
//                    else -> {
//                    }
//                }
//            }
//
//            override suspend fun beforeOldStateIsRemoved(oldState: ScanningState) {
//                when (oldState) {
//                    is NoBluetooth.MissingPermissions -> scanner.startMonitoringBluetooth()
//                    else -> {
//                    }
//                }
//            }

//            override suspend fun initialState() {
//                scanner.startMonitoringBluetooth()
//            }
//
//            override suspend fun finalState() {
//                scanner.stopMonitoringBluetooth()
//            }
//        }

        sealed class Enabled(
            val discovered: Discovered,
            protected val scanner: BaseScanner
        ) : Initialized(scanner) {

//            override val revokePermission: suspend () -> NoBluetooth.MissingPermissions =
//                permittedHandler.revokePermission

            val disable = suspend {
                NoBluetooth.Disabled(scanner)
            }

//            override suspend fun initialState() {
//                super.initialState()
//
//                permittedHandler.initialState()
//            }

//            override suspend fun finalState() {
//                super.finalState()
//
//                permittedHandler.finalState()
//            }

//            override suspend fun afterNewStateIsSet(newState: ScanningState) {
//                permittedHandler.afterNewStateIsSet(newState)
//            }
//
//            override suspend fun beforeOldStateIsRemoved(oldState: ScanningState) {
//                permittedHandler.beforeOldStateIsRemoved(oldState)
//            }

            class Idle internal constructor(
                previouslyDiscovered:Discovered,
                scanner: BaseScanner
            ) : Enabled(previouslyDiscovered, scanner) {

                fun startScanning(filter: Set<UUID> = discovered.filter): suspend () -> Scanning = {
                    Scanning(
                        discovered.discoveredForFilter(filter),
                        scanner
                    )
                }

                fun refresh(filter: Set<UUID> = discovered.filter): suspend () -> Idle = {
                    Idle(
                        discovered.discoveredForFilter(filter),
                        scanner
                    )
                }
            }

            class Scanning internal constructor(
                discovered: Discovered,
                scanner: BaseScanner
            ) : Enabled(discovered, scanner),
                HandleAfterOldStateIsRemoved<ScanningState>, HandleAfterCreating<ScanningState> {

                suspend fun discoverDevice(
                    identifier: Identifier,
                    rssi: Int,
                    advertisementData: BaseAdvertisementData,
                    deviceCreator: () -> Device
                ): suspend () -> ScanningState {

                    return discovered.devices.find { it.identifier == identifier }
                        ?.let { knownDevice ->
                            knownDevice.takeAndChangeState { state ->
                                state.advertisementDataAndRssiDidUpdate(advertisementData, rssi)
                            }
                            // TODO our contents have technically changed yet we remain()
                            // not storing Devices as repos, but rather storing their _state_
                            // would be better.
                            remain()
                        } ?: suspend { Scanning(discovered.copyAndAdd(deviceCreator()), scanner) }
                }

                val stopScanning = suspend { Idle(discovered, scanner) }

                override suspend fun afterOldStateIsRemoved(oldState: ScanningState) {
                    if (oldState !is Scanning)
                        scanner.scanForDevices(discovered.filter)
                }

                override suspend fun afterCreatingNewState(newState: ScanningState) {
                    if (newState !is Scanning)
                        scanner.stopScanning()
                }
            }
        }

        sealed class NoBluetooth constructor(scanner: BaseScanner) : Initialized(scanner) {

            class Disabled internal constructor(
                private val scanner: BaseScanner
            ) : NoBluetooth(scanner), HandleAfterOldStateIsRemoved<ScanningState> {

                val enable: suspend () -> Enabled = {
                    Enabled.Idle(nothingDiscovered, scanner)
                }

                override suspend fun afterOldStateIsRemoved(oldState: ScanningState) {
                   if (oldState !is Disabled && scanner.autoEnableBluetooth)
                       scanner.requestBluetoothEnable()
                }
            }

            class MissingPermissions internal constructor(
                private val scanner: BaseScanner
            ) : NoBluetooth(scanner) {

                fun permit(enabled: Boolean): suspend () -> ScanningState = {
                    if (enabled) Enabled.Idle(nothingDiscovered, scanner)
                    else Disabled(scanner)
                }
            }
        }

        internal fun stopMonitoring() {
            scanner.stopMonitoringPermissions()
            scanner.stopMonitoringBluetooth()
        }
    }

    class NotInitialized(
        val permissions: Permissions,
        val connectionSettings: ConnectionSettings,
        val autoRequestPermission: Boolean,
        val autoEnableBluetooth: Boolean,
        val builder: BaseScanner.Builder
    ) : ScanningState(), SpecialFlowValue.NotImportant {

        suspend fun initialize(
            repo: StateRepo<ScanningState, MutableStateFlow<ScanningState>>
        ): suspend () -> Initialized {
            val scanner = builder.create(
                permissions,
                connectionSettings,
                autoRequestPermission,
                autoEnableBluetooth,
                scanningStateRepo = repo
            )
            return if (!scanner.isPermitted()) {
                { Initialized.NoBluetooth.MissingPermissions(scanner) }
            } else if (!scanner.isBluetoothEnabled()) {
                { Initialized.NoBluetooth.Disabled(scanner) }
            } else {
                { Initialized.Enabled.Idle(nothingDiscovered, scanner) }
            }
        }
    }
}

typealias ScanningStateFlowRepo = StateRepo<ScanningState, MutableStateFlow<ScanningState>>

class ScanningStateRepo(
    permissions: Permissions,
    connectionSettings: ConnectionSettings,
    autoRequestPermission: Boolean,
    autoEnableBluetooth: Boolean,
    builder: BaseScanner.Builder,
    coroutineContext: CoroutineContext = Dispatchers.Main.immediate
) : ColdStateFlowRepo<ScanningState>(
    coroutineContext = coroutineContext,
    initChangeState = { state ->
        when(state) {
            is ScanningState.Initialized.Enabled.Idle ->
                state.refresh() // check if we now need to start scanning again
            is Scanning,
            is ScanningState.Initialized.NoBluetooth.Disabled,
            is ScanningState.Initialized.NoBluetooth.MissingPermissions,
            is ScanningState.NotInitialized ->
                state.remain()
        }
    },
    deinitChangeState = { state ->
        if (state is ScanningState.Initialized)
            state.stopMonitoring()
        if (state is Scanning)
            state.stopScanning
        else
            state.remain()
    },
    firstState = {
        ScanningState.NotInitialized(
            permissions,
            connectionSettings,
            autoRequestPermission,
            autoEnableBluetooth,
            builder
        )
    }
)
