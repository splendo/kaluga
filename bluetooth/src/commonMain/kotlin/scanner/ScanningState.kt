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
import com.splendo.kaluga.base.state.HandleAfterCreating
import com.splendo.kaluga.base.state.HandleAfterNewStateIsSet
import com.splendo.kaluga.base.state.HandleAfterOldStateIsRemoved
import com.splendo.kaluga.base.state.HandleBeforeOldStateIsRemoved
import com.splendo.kaluga.base.state.KalugaState
import com.splendo.kaluga.bluetooth.RSSI
import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.BaseAdvertisementData
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.Identifier

/**
 * A set of [UUID] to apply to a scan result
 * If not empty, only [Device] that are advertising at least one [Service] matching one of the [UUID] will be scanned.
 */
typealias Filter = Set<UUID>

/**
 * The [KalugaState] of scanning for Bluetooth devices
 */
sealed interface ScanningState : KalugaState {

    /**
     * The [Device] found during scanning
     */
    interface Devices {
        /**
         * The list of [Device] found
         */
        val devices: List<Device>

        /**
         * The [Filter] applied during scanning
         */
        val filter: Filter

        /**
         * Creates a new [Devices] instance that adds [device] to [devices]
         * @param device the [Device] to add
         * @return the new instance of [Devices] containing [device]
         */
        fun copyAndAdd(device: Device): Devices

        /**
         * Creates a [Devices] for a given [Filter]
         * If [filter] is the same as [Devices.filter], this instance will be returned
         * Otherwise a new [Devices] will be created.
         * @param filter the [Filter] to apply
         * @return the new [Devices]
         */
        fun foundForFilter(filter: Filter): Devices

        /**
         * The list of [Identifier] found
         */
        fun identifiers() = devices.map(Device::identifier).toSet()
    }

    /**
     * A [ScanningState] indicating observation is not active
     */
    sealed interface Inactive : ScanningState, SpecialFlowValue.NotImportant

    /**
     * An [Inactive] State indicating observation has not started yet
     */
    interface NotInitialized : Inactive

    /**
     * A [ScanningState] indicating observation has stopped after being started
     */
    interface Deinitialized : Inactive {
        /**
         * The [Devices] discovered before deinitalizing
         */
        val previouslyDiscovered: Devices

        /**
         * The [Devices] paired before deinitializing
         */
        val previouslyPaired: Devices

        /**
         * Transitions into an [Initializing] State
         */
        val reinitialize: suspend () -> Initializing
    }

    /**
     * A [ScanningState] indicating observation has started
     */
    sealed interface Active : ScanningState {

        /**
         * The [Devices] discovered
         */
        val discovered: Devices

        /**
         * The [Devices] paired
         */
        val paired: Devices

        /**
         * Transitions into a [Deinitialized] State
         */
        val deinitialize: suspend () -> Deinitialized
    }

    /**
     * An [Active] State indicating the state is transitioning from [Inactive] to [Initialized]
     */
    interface Initializing : Active, SpecialFlowValue.NotImportant {
        /**
         * Transitions into an [Initialized] State
         * @param hasPermission if `true` all permissions related to Bluetooth have been granted
         * @param enabled if `true` the Bluetooth service is enabled
         * @return method for transitioning into an [Initialized] State
         */
        fun initialized(hasPermission: Boolean, enabled: Boolean): suspend () -> Initialized
    }

    /**
     * An [Active] State indicating observation has started and initialization has completed
     */
    sealed interface Initialized : Active

    /**
     * An [Initialized] State indicating all permissions related to Bluetooth have been granted
     */
    sealed interface Permitted : Initialized {

        /**
         * Transitions into a [NoBluetooth.MissingPermissions] State
         */
        val revokePermission: suspend () -> NoBluetooth.MissingPermissions
    }

    /**
     * A [Permitted] State indicating the Bluetooth service is enabled
     */
    sealed interface Enabled : Permitted {

        /**
         * Starts to retrieve the list of paired [Device]
         * @param filter the [Filter] to apply to the paired devices
         */
        suspend fun retrievePairedDevices(filter: Filter)

        /**
         * Transitions into an [Enabled] state where a set of devices is paired
         * @param filter the [Filter] applied to finding the paired devices
         * @param identifiers the set of [Identifier] of all devices paired
         * @param deviceCreators A list of methods for creating [Device] if [filter] has changed
         * @return a method for transitioning into [Enabled] with the new list of paired [Device]
         */
        fun pairedDevices(
            filter: Filter,
            identifiers: Set<Identifier>,
            deviceCreators: List<() -> Device>
        ): suspend () -> Enabled

        /**
         * Transitions into a [NoBluetooth.Disabled] State
         */
        val disable: suspend () -> NoBluetooth.Disabled

        /**
         * An [Enabled] State indicating no active scanning is taking place
         */
        interface Idle : Enabled {

            /**
             * Transitions into a [Scanning] State for a given filter
             * @param filter the [Filter] to apply for scanning
             * @return the method for transitioning into a [Scanning] state
             */
            fun startScanning(filter: Filter = discovered.filter): suspend () -> Scanning

            /**
             * Transitions into an [Idle] State with a new [Filter]
             * @param filter the new [Filter] to apply
             * @return the method for transitioning into a new [Idle] state
             */
            fun refresh(filter: Filter = discovered.filter): suspend () -> Idle
        }

        /**
         * An [Enabled] State indicating scanning is taking place
         */
        interface Scanning : Enabled {

            /**
             * Transitions into a [Scanning] state where a [Device] is added or updated
             * @param identifier the [Identifier] of the [Device] discovered
             * @param rssi the [RSSI] value of the [Device] discovered
             * @param advertisementData the [BaseAdvertisementData] of the [Device] discovered
             * @param deviceCreator Method for creating a [Device] if it had not been scanned previously.
             * @return method for transitioning into a [Scanning] state where the [Device] is discovered
             */
            suspend fun discoverDevice(
                identifier: Identifier,
                rssi: RSSI,
                advertisementData: BaseAdvertisementData,
                deviceCreator: () -> Device
            ): suspend () -> Scanning

            /**
             * Transitions into an [Idle] State
             */
            val stopScanning: suspend () -> Idle
        }
    }

    /**
     * An [Initialized] State indicating Bluetooth is not available
     */
    sealed interface NoBluetooth : Initialized {

        /**
         * A [NoBluetooth] State indicating Bluetooth is not available because the service has been disabled.
         */
        interface Disabled : NoBluetooth, Permitted {

            /**
             * Transitions into an [Enabled] State
             */
            val enable: suspend () -> Enabled
        }

        /**
         * A [NoBluetooth] State indicating Bluetooth is not available due to missing permissions
         */
        interface MissingPermissions : NoBluetooth {

            /**
             * Transitions into a [ScanningState] after Bluetooth permissions have been granted
             * @param enabled if `true` the Bluetooth service is enabled
             * @return a method for transitioning into the new [ScanningState]
             */
            fun permit(enabled: Boolean): suspend () -> ScanningState
        }
    }

    /**
     * A [ScanningState] indicating the system does not support Bluetooth
     */
    interface NoHardware : ScanningState
}

internal sealed class ScanningStateImpl {

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

        internal val revokePermission = suspend { NoBluetooth.MissingPermissions(scanner) }

        internal suspend fun afterNewStateIsSet(newState: ScanningState) {
            when (newState) {
                is ScanningState.Inactive,
                is ScanningState.Initializing,
                is ScanningState.NoHardware,
                is ScanningState.NoBluetooth.MissingPermissions -> scanner.stopMonitoringHardwareEnabled()
                is ScanningState.Active -> {}
            }
        }

        internal suspend fun beforeOldStateIsRemoved(oldState: ScanningState) {
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
                rssi: RSSI,
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
