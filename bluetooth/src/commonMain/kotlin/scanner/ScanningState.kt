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
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.state.ColdStateFlowRepo
import com.splendo.kaluga.state.HandleAfterCreating
import com.splendo.kaluga.state.HandleAfterNewStateIsSet
import com.splendo.kaluga.state.HandleAfterOldStateIsRemoved
import com.splendo.kaluga.state.HandleBeforeOldStateIsRemoved
import com.splendo.kaluga.state.KalugaState
import com.splendo.kaluga.state.StateRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.coroutines.CoroutineContext

typealias Filter = Set<UUID>

sealed class ScanningState : KalugaState {

    data class Discovered(
        val devices: List<Device>,
        internal val filter: Filter,
    ) {
        constructor(filter: Filter) : this(emptyList(), filter)

        fun copyAndAdd(device: Device): Discovered =
            Discovered(listOf(*devices.toTypedArray(), device), filter)

        fun discoveredForFilter(filter: Filter) =
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

    sealed class Inactive : ScanningState(), SpecialFlowValue.NotImportant

    class NotInitialized : Inactive() {

        fun startInitializing(
            scanner: BaseScanner
        ): suspend () -> ScanningState {
            return if (!scanner.isSupported) {
                { NoHardware }
            } else {
                { Initializing(nothingDiscovered, scanner) }
            }
        }
    }

    data class Deinitialized(val previouslyDiscovered: Discovered, val scanner: BaseScanner) :
        Inactive() {
        val reinitialize: suspend () -> ScanningState =
            { Initializing(previouslyDiscovered, scanner) }
    }

    sealed class Active :
        ScanningState(),
        HandleBeforeOldStateIsRemoved<ScanningState>,
        HandleAfterNewStateIsSet<ScanningState> {
        override suspend fun beforeOldStateIsRemoved(oldState: ScanningState) {
            when (oldState) {
                is Inactive -> {
                    scanner.startMonitoringPermissions()
                }
                is Active, NoHardware -> {}
            }
        }

        override suspend fun afterNewStateIsSet(newState: ScanningState) {
            when (newState) {
                is Inactive -> {
                    scanner.stopMonitoringPermissions()
                }
                is Active, NoHardware -> {}
            }
        }

        protected abstract val scanner: BaseScanner
        abstract val discovered: Discovered
        val deinitialize: suspend () -> ScanningState = { Deinitialized(discovered, scanner) }
    }

    interface Permitted : HandleBeforeOldStateIsRemoved<ScanningState>, HandleAfterNewStateIsSet<ScanningState> {
        val revokePermission: suspend () -> NoBluetooth.MissingPermissions
    }

    class PermittedHandler(val previouslyDiscovered: Discovered, val scanner: BaseScanner) : Permitted {

        override val revokePermission = suspend { NoBluetooth.MissingPermissions(scanner) }

        override suspend fun afterNewStateIsSet(newState: ScanningState) {
            when (newState) {
                is Inactive,
                is Initializing,
                is NoHardware,
                is NoBluetooth.MissingPermissions -> scanner.stopMonitoringSensors()
                else -> {}
            }
        }

        override suspend fun beforeOldStateIsRemoved(oldState: ScanningState) {
            when (oldState) {
                is Inactive,
                is Initializing,
                is NoHardware,
                is NoBluetooth.MissingPermissions -> scanner.startMonitoringSensors()
                else -> {}
            }
        }
    }

    data class Initializing(
        override val discovered: Discovered,
        override val scanner: BaseScanner
    ) : Active(), SpecialFlowValue.NotImportant {

        fun initialized(hasPermission: Boolean, enabled: Boolean): suspend () -> ScanningState =
            suspend {
                when {
                    !hasPermission -> NoBluetooth.MissingPermissions(scanner)
                    !enabled -> NoBluetooth.Disabled(scanner)
                    else -> Enabled.Idle(discovered, scanner)
                }
            }
    }

    sealed class Enabled(private val permittedHandler: PermittedHandler) : Active(), Permitted {

        val disable = suspend {
            NoBluetooth.Disabled(scanner)
        }

        fun pairedDevices(filter: Set<UUID>) = scanner.pairedDevices(filter)

        override val revokePermission: suspend () -> NoBluetooth.MissingPermissions = permittedHandler.revokePermission

        override suspend fun afterNewStateIsSet(newState: ScanningState) {
            super.afterNewStateIsSet(newState)
            permittedHandler.afterNewStateIsSet(newState)
        }

        override suspend fun beforeOldStateIsRemoved(oldState: ScanningState) {
            super.beforeOldStateIsRemoved(oldState)
            permittedHandler.beforeOldStateIsRemoved(oldState)
        }

        class Idle internal constructor(
            override val discovered: Discovered,
            override val scanner: BaseScanner
        ) : Enabled(PermittedHandler(discovered, scanner)) {

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
            override val discovered: Discovered,
            override val scanner: BaseScanner
        ) : Enabled(PermittedHandler(discovered, scanner)),
            HandleAfterOldStateIsRemoved<ScanningState>,
            HandleAfterCreating<ScanningState> {

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

    sealed class NoBluetooth : Active() {

        override val discovered: Discovered = nothingDiscovered

        class Disabled internal constructor(
            override val scanner: BaseScanner
        ) : NoBluetooth(), HandleAfterOldStateIsRemoved<ScanningState>, Permitted {

            private val permittedHandler = PermittedHandler(nothingDiscovered, scanner)

            val enable: suspend () -> Enabled = {
                Enabled.Idle(nothingDiscovered, scanner)
            }

            override val revokePermission: suspend () -> MissingPermissions = permittedHandler.revokePermission

            override suspend fun afterNewStateIsSet(newState: ScanningState) {
                super.afterNewStateIsSet(newState)
                permittedHandler.afterNewStateIsSet(newState)
            }

            override suspend fun beforeOldStateIsRemoved(oldState: ScanningState) {
                super.beforeOldStateIsRemoved(oldState)
                permittedHandler.beforeOldStateIsRemoved(oldState)
            }

            override suspend fun afterOldStateIsRemoved(oldState: ScanningState) {
                if (oldState !is Disabled && scanner.autoEnableSensors)
                    scanner.requestSensorsEnable()
            }
        }

        class MissingPermissions internal constructor(
            override val scanner: BaseScanner
        ) : NoBluetooth() {

            fun permit(enabled: Boolean): suspend () -> ScanningState = {
                if (enabled) Enabled.Idle(nothingDiscovered, scanner)
                else Disabled(scanner)
            }
        }
    }

    object NoHardware : ScanningState()
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
    initChangeStateWithRepo = { state, repo ->
        when (state) {
            is ScanningState.NotInitialized -> {
                val scanner = builder.create(
                    permissions,
                    connectionSettings,
                    autoRequestPermission,
                    autoEnableBluetooth,
                    scanningStateRepo = repo as ScanningStateRepo
                )
                state.startInitializing(scanner)
            }
            is ScanningState.Deinitialized -> {
                state.reinitialize
            }
            is ScanningState.Active, is ScanningState.NoHardware -> state.remain()
        }
    },
    deinitChangeStateWithRepo = { state, _ ->
        when (state) {
            is ScanningState.Active -> state.deinitialize
            is ScanningState.Inactive, is ScanningState.NoHardware -> state.remain()
        }
    },
    firstState = {
        ScanningState.NotInitialized()
    }
)
