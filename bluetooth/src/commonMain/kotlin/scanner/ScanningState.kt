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
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.base.state.HandleAfterCreating
import com.splendo.kaluga.base.state.HandleAfterNewStateIsSet
import com.splendo.kaluga.base.state.HandleAfterOldStateIsRemoved
import com.splendo.kaluga.base.state.HandleBeforeOldStateIsRemoved
import com.splendo.kaluga.base.state.KalugaState

typealias Filter = Set<UUID>

sealed interface ScanningState : KalugaState {

    // Discovered or Paired devices
    interface Devices {
        val devices: List<Device>
        val filter: Filter

        fun copyAndAdd(device: Device): Devices
        fun foundForFilter(filter: Filter): Devices

        fun identifiers() = devices.map(Device::identifier).toSet()
    }

    sealed interface Inactive : ScanningState, SpecialFlowValue.NotImportant
    interface NotInitialized : Inactive

    interface Deinitialized : Inactive {
        val previouslyDiscovered: Devices
        val previouslyPaired: Devices
        val reinitialize: suspend () -> Initializing
    }

    sealed interface Active : ScanningState {

        val discovered: Devices
        val paired: Devices
        val deinitialize: suspend () -> Deinitialized
    }

    interface Initializing : Active, SpecialFlowValue.NotImportant {
        fun initialized(hasPermission: Boolean, enabled: Boolean): suspend () -> Initialized
    }
    sealed interface Initialized : Active
    sealed interface Permitted : Initialized {
        val revokePermission: suspend () -> NoBluetooth.MissingPermissions
    }

    sealed interface Enabled : Permitted {

        suspend fun retrievePairedDevices(filter: Filter)

        fun pairedDevices(
            filter: Filter,
            identifiers: Set<Identifier>,
            deviceCreators: List<() -> Device>
        ): suspend () -> Enabled

        val disable: suspend () -> NoBluetooth.Disabled

        interface Idle : Enabled {

            fun startScanning(filter: Filter = discovered.filter): suspend () -> Scanning

            fun refresh(filter: Filter = discovered.filter): suspend () -> Idle
        }

        interface Scanning : Enabled {

            suspend fun discoverDevice(
                identifier: Identifier,
                rssi: Int,
                advertisementData: BaseAdvertisementData,
                deviceCreator: () -> Device
            ): suspend () -> Scanning

            val stopScanning: suspend () -> Idle
        }
    }

    sealed interface NoBluetooth : Initialized {

        interface Disabled : NoBluetooth, Permitted {
            val enable: suspend () -> Enabled
        }

        interface MissingPermissions : NoBluetooth {
            fun permit(enabled: Boolean): suspend () -> ScanningState
        }
    }

    interface NoHardware : ScanningState
}

sealed class ScanningStateImpl {

    companion object {
        val nothingFound = Devices(filter = emptySet())
    }

    data class Devices(
        override val devices: List<Device>,
        override val filter: Filter,
    ) : ScanningState.Devices {
        constructor(filter: Filter) : this(emptyList(), filter)

        override fun copyAndAdd(device: Device): Devices =
            Devices(listOf(*devices.toTypedArray(), device), filter)

        override fun foundForFilter(filter: Filter) =
            if (this.filter == filter)
                this
            else
                Devices(filter)
    }

    object NotInitialized : ScanningStateImpl(), ScanningState.NotInitialized {

        fun startInitializing(
            scanner: Scanner
        ): suspend () -> ScanningState {
            return if (!scanner.isSupported) {
                { NoHardware }
            } else {
                { Initializing(nothingFound, nothingFound, scanner) }
            }
        }
    }

    data class Deinitialized(
        override val previouslyDiscovered: ScanningState.Devices,
        override val previouslyPaired: ScanningState.Devices,
        val scanner: Scanner
    ) :
        ScanningStateImpl(), ScanningState.Deinitialized {
        override val reinitialize = suspend { Initializing(previouslyDiscovered, previouslyPaired, scanner) }
    }

    sealed class Active :
        ScanningStateImpl(),
        HandleBeforeOldStateIsRemoved<ScanningState>,
        HandleAfterNewStateIsSet<ScanningState> {

        override suspend fun beforeOldStateIsRemoved(oldState: ScanningState) {
            when (oldState) {
                is ScanningState.Inactive -> {
                    scanner.startMonitoringPermissions()
                }
                is ScanningState.Active, is ScanningState.NoHardware -> {}
            }
        }

        override suspend fun afterNewStateIsSet(newState: ScanningState) {
            when (newState) {
                is ScanningState.Inactive -> {
                    scanner.stopMonitoringPermissions()
                }
                is ScanningState.Active, is ScanningState.NoHardware -> {}
            }
        }
        abstract val scanner: Scanner
        abstract val discovered: ScanningState.Devices
        abstract val paired: ScanningState.Devices
        val deinitialize: suspend () -> Deinitialized = { Deinitialized(discovered, paired, scanner) }
    }

    class PermittedHandler(val scanner: Scanner) {

        val revokePermission = suspend { NoBluetooth.MissingPermissions(scanner) }

        suspend fun afterNewStateIsSet(newState: ScanningState) {
            when (newState) {
                is ScanningState.Inactive,
                is ScanningState.Initializing,
                is ScanningState.NoHardware,
                is ScanningState.NoBluetooth.MissingPermissions -> scanner.stopMonitoringHardwareEnabled()
                is ScanningState.Active -> {}
            }
        }

        suspend fun beforeOldStateIsRemoved(oldState: ScanningState) {
            when (oldState) {
                is ScanningState.Inactive,
                is ScanningState.Initializing,
                is ScanningState.NoHardware,
                is ScanningState.NoBluetooth.MissingPermissions -> scanner.startMonitoringHardwareEnabled()
                is ScanningState.Active -> {}
            }
        }
    }

    data class Initializing(
        override val discovered: ScanningState.Devices,
        override val paired: ScanningState.Devices,
        override val scanner: Scanner
    ) : Active(), ScanningState.Initializing {

        override fun initialized(hasPermission: Boolean, enabled: Boolean): suspend () -> ScanningState.Initialized =
            suspend {
                when {
                    !hasPermission -> NoBluetooth.MissingPermissions(scanner)
                    !enabled -> NoBluetooth.Disabled(scanner)
                    else -> Enabled.Idle(discovered, paired, scanner)
                }
            }
    }

    sealed class Enabled : Active() {

        protected abstract val permittedHandler: PermittedHandler

        val disable = suspend {
            NoBluetooth.Disabled(scanner)
        }

        val revokePermission: suspend () -> NoBluetooth.MissingPermissions get() = permittedHandler.revokePermission

        override suspend fun afterNewStateIsSet(newState: ScanningState) {
            super.afterNewStateIsSet(newState)
            permittedHandler.afterNewStateIsSet(newState)
        }

        override suspend fun beforeOldStateIsRemoved(oldState: ScanningState) {
            super.beforeOldStateIsRemoved(oldState)
            permittedHandler.beforeOldStateIsRemoved(oldState)
        }

        class Idle internal constructor(
            override val discovered: ScanningState.Devices,
            override val paired: ScanningState.Devices,
            override val scanner: Scanner
        ) : Enabled(), ScanningState.Enabled.Idle {

            override val permittedHandler: PermittedHandler = PermittedHandler(scanner)

            override suspend fun retrievePairedDevices(filter: Filter) = scanner.retrievePairedDevices(filter)

            override fun pairedDevices(
                filter: Filter,
                identifiers: Set<Identifier>,
                deviceCreators: List<() -> Device>
            ): suspend () -> ScanningState.Enabled = if (paired.identifiers() == identifiers) {
                remain()
            } else {
                suspend {
                    Idle(
                        discovered,
                        Devices(deviceCreators.map { it.invoke() }, filter),
                        scanner
                    )
                }
            }

            override fun startScanning(filter: Set<UUID>): suspend () -> Scanning = {
                Scanning(
                    discovered.foundForFilter(filter),
                    paired,
                    scanner
                )
            }

            override fun refresh(filter: Set<UUID>): suspend () -> Idle = {
                Idle(
                    discovered.foundForFilter(filter),
                    paired,
                    scanner
                )
            }
        }

        class Scanning internal constructor(
            override val discovered: ScanningState.Devices,
            override val paired: ScanningState.Devices,
            override val scanner: Scanner
        ) : Enabled(),
            HandleAfterOldStateIsRemoved<ScanningState>,
            HandleAfterCreating<ScanningState>,
            ScanningState.Enabled.Scanning {

            override val permittedHandler: PermittedHandler = PermittedHandler(scanner)

            override suspend fun retrievePairedDevices(filter: Filter) = scanner.retrievePairedDevices(filter)

            override fun pairedDevices(
                filter: Filter,
                identifiers: Set<Identifier>,
                deviceCreators: List<() -> Device>
            ): suspend () -> ScanningState.Enabled = if (paired.identifiers() == identifiers) {
                remain()
            } else {
                suspend {
                    Scanning(
                        discovered,
                        Devices(deviceCreators.map { it.invoke() }, filter),
                        scanner
                    )
                }
            }

            override suspend fun discoverDevice(
                identifier: Identifier,
                rssi: Int,
                advertisementData: BaseAdvertisementData,
                deviceCreator: () -> Device
            ): suspend () -> ScanningState.Enabled.Scanning {

                return discovered.devices.find { it.identifier == identifier }
                    ?.let { knownDevice ->
                        knownDevice.rssiDidUpdate(rssi)
                        knownDevice.advertisementDataDidUpdate(advertisementData)
                        remain()
                    } ?: suspend { Scanning(discovered.copyAndAdd(deviceCreator()), paired, scanner) }
            }

            override val stopScanning = suspend { Idle(discovered, paired, scanner) }

            override suspend fun afterOldStateIsRemoved(oldState: ScanningState) {
                if (oldState !is Scanning) {
                    scanner.scanForDevices(discovered.filter)
                }
            }

            override suspend fun afterCreatingNewState(newState: ScanningState) {
                if (newState !is Scanning) {
                    scanner.stopScanning()
                }
            }
        }
    }

    sealed class NoBluetooth : Active() {

        override val discovered: Devices = nothingFound
        override val paired: Devices = nothingFound

        class Disabled internal constructor(
            override val scanner: Scanner
        ) : NoBluetooth(), ScanningState.NoBluetooth.Disabled {

            private val permittedHandler = PermittedHandler(scanner)

            override val enable: suspend () -> ScanningState.Enabled = {
                Enabled.Idle(nothingFound, nothingFound, scanner)
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
        }

        class MissingPermissions internal constructor(
            override val scanner: Scanner
        ) : NoBluetooth(), ScanningState.NoBluetooth.MissingPermissions {

            override fun permit(enabled: Boolean): suspend () -> ScanningState = {
                if (enabled) Enabled.Idle(nothingFound, nothingFound, scanner)
                else Disabled(scanner)
            }
        }
    }

    object NoHardware : ScanningStateImpl(), ScanningState.NoHardware
}
