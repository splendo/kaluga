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
import com.splendo.kaluga.bluetooth.BluetoothService
import com.splendo.kaluga.bluetooth.RSSI
import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.BaseAdvertisementData
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.scanner.ScanningState.Enabled.Scanning

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
     * A mode in which [Device] can be discovered
     */
    sealed class DeviceDiscoveryMode {

        /**
         * The [Filter] used for discovering [Device]
         */
        abstract val filter: Filter

        /**
         * A [DeviceDiscoveryMode] that discovers paired [Device]
         * @param filter the [Filter] used for discovering paired [Device]
         */
        data class Paired(override val filter: Filter) : DeviceDiscoveryMode()

        /**
         * A [DeviceDiscoveryMode] that discovers [Device] by scanning for them
         * @param filter the [Filter] used for scanning for [Device]
         */
        data class Scanning(override val filter: Filter) : DeviceDiscoveryMode()
    }

    /**
     * The [Device] found during scanning
     */
    interface Devices {
        /**
         * The map of [Device] found for given [Identifier]
         */
        val allDevices: Map<Identifier, Device>

        /**
         * A map of all [Identifier] found for scanning per [DeviceDiscoveryMode]
         */
        val identifiersFoundForDeviceDiscoveryMode: Map<DeviceDiscoveryMode, Set<Identifier>>

        /**
         * The [DeviceDiscoveryMode.Scanning] applied during scanning
         */
        val currentScanFilter: DeviceDiscoveryMode.Scanning

        /**
         * The set of [Identifier] scanned by [currentScanFilter]
         */
        val identifiersForCurrentScanFilter: Set<Identifier> get() = identifiersFoundForDeviceDiscoveryMode[currentScanFilter] ?: emptySet()

        /**
         * Creates a new [Devices] instance that adds an [Identifier] to the [currentScanFilter] and creates the corresponding [Device] if not yet discovered
         * @param identifier the [Identifier] of the [Device] to add
         * @param createDevice method for creating a [Device] for the [identifier]
         * @return the new instance of [Devices] where [allDevices] contains a [Device] for [identifier] and [identifiersForCurrentScanFilter] contains [identifier]
         */
        fun copyAndAddScanned(identifier: Identifier, createDevice: () -> Device): Devices

        /**
         * Creates a new [Devices] instance that sets the paired [Device] for a given [Filter]
         * @param devices map of all the [Identifier] paired and a method for creating the corresponding [Device]
         * @param filter a [Filter] applied for acquiring the scanned devices
         * @param removeForAllPairedFilters if `true` the list of paired devices for all filters will be emptied
         * @return the new instance of [Devices] where [allDevices] contains a [Device] for all [Identifier] in [devices] and [identifiersFoundForDeviceDiscoveryMode] will contains all [Identifier] in [devices] for a [DeviceDiscoveryMode.Paired] with [filter]
         */
        fun copyAndSetPaired(devices: Map<Identifier, () -> Device>, filter: Filter, removeForAllPairedFilters: Boolean): Devices

        /**
         * Creates a new [Devices] instance that sets the [currentScanFilter] for a [DeviceDiscoveryMode.Scanning] with a given [Filter]
         * @param filter the [Filter] to set as the [DeviceDiscoveryMode.Scanning.filter] for the new [currentScanFilter]
         * @param cleanMode the [BluetoothService.CleanMode] to apply for cleaning the previously found [Device]
         * @return the new instance of [Devices] where [currentScanFilter] if filtered with [filter] and cleanup has occurred according to [cleanMode]
         */
        fun updateScanFilter(filter: Filter, cleanMode: BluetoothService.CleanMode): Devices

        /**
         * The list of [Device] found for a given [DeviceDiscoveryMode]
         * @param filter the [DeviceDiscoveryMode] to get devices for
         * @return the list of [Device] found for the [filter]
         */
        fun devicesForDiscoveryMode(filter: DeviceDiscoveryMode) = identifiersFoundForDeviceDiscoveryMode[filter]?.let { identifiers ->
            allDevices.entries.mapNotNull { if (identifiers.contains(it.key)) it.value else null }
        } ?: emptyList()

        /**
         * The list of [Device] found for the [currentScanFilter]
         * @return the list of [Device] found for [currentScanFilter]
         */
        fun devicesForCurrentScanFilter() = devicesForDiscoveryMode(currentScanFilter)
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
         * The [Devices] found before deinitalizing
         */
        val previousDevices: Devices

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
        val devices: Devices

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
         * @param removeForAllPairedFilters if `true` the list of paired devices for all filters will be emptied
         * @param connectionSettings the [ConnectionSettings] to apply to [Device] found as paired devices. Note this will only be used if the devices was not previously discovered without cleaning up.
         */
        suspend fun retrievePairedDevices(filter: Filter, removeForAllPairedFilters: Boolean, connectionSettings: ConnectionSettings?)

        /**
         * Transitions into an [Enabled] state where a set of devices is paired
         * @param devices the map of [Identifier] and the method for creating a [Device] of all devices paired
         * @param filter the [Filter] applied to finding the paired devices
         * @param removeForAllPairedFilters if `true` the list of paired devices for all filters will be emptied
         * @return a method for transitioning into [Enabled] with the new list of paired [Device]
         */
        fun pairedDevices(
            devices: Map<Identifier, () -> Device>,
            filter: Filter,
            removeForAllPairedFilters: Boolean,
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
             * @param cleanMode the [BluetoothService.CleanMode] to apply to previously scanned [Device].
             * @param connectionSettings the [ConnectionSettings] to apply to any [Device] scanned that was not previously discovered. If `null` the default will be used
             * @return the method for transitioning into a [Scanning] state
             */
            fun startScanning(
                filter: Filter = devices.currentScanFilter.filter,
                cleanMode: BluetoothService.CleanMode = BluetoothService.CleanMode.REMOVE_ALL,
                connectionSettings: ConnectionSettings? = null,
            ): suspend () -> Scanning

            /**
             * Transitions into an [Idle] State with a new [Filter]
             * @param filter the new [Filter] to apply
             * @param cleanMode the [BluetoothService.CleanMode] to apply to previously scanned [Device].
             * @return the method for transitioning into a new [Idle] state
             */
            fun refresh(filter: Filter = devices.currentScanFilter.filter, cleanMode: BluetoothService.CleanMode = BluetoothService.CleanMode.REMOVE_ALL): suspend () -> Idle
        }

        /**
         * An [Enabled] State indicating scanning is taking place
         */
        interface Scanning : Enabled {

            /**
             * A class to add or update a [Device] using [discoverDevices]
             * @param identifier the [Identifier] of the [Device] discovered
             * @param rssi the [RSSI] value of the [Device] discovered
             * @param advertisementData the [BaseAdvertisementData] of the [Device] discovered
             * @param deviceCreator Method for creating a [Device] if it had not been scanned previously.
             */
            data class DiscoveredDevice(
                val identifier: Identifier,
                val rssi: RSSI,
                val advertisementData: BaseAdvertisementData,
                val deviceCreator: () -> Device,
            )

            /**
             * Transitions into a [Scanning] state where a list of [Device] is added or updated
             * @param devices the list of [DiscoveredDevice] to be scanned
             * @return method for transitioning into a [Scanning] state where the list of [Device] is discovered
             */
            suspend fun discoverDevices(
                devices: List<DiscoveredDevice>,
            ): suspend () -> Scanning

            /**
             * Transitions into an [Idle] State
             * @param cleanMode the [BluetoothService.CleanMode] to apply to previously scanned [Device].
             */
            fun stopScanning(cleanMode: BluetoothService.CleanMode = BluetoothService.CleanMode.REMOVE_ALL): suspend () -> Idle
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

    val nothingFound: Devices
}

/**
 * Transitions into a [Scanning] state where a [Device] is added or updated
 * @param identifier the [Identifier] of the [Device] discovered
 * @param rssi the [RSSI] value of the [Device] discovered
 * @param advertisementData the [BaseAdvertisementData] of the [Device] discovered
 * @param deviceCreator Method for creating a [Device] if it had not been scanned previously.
 * @return method for transitioning into a [Scanning] state where the [Device] is discovered
 */
suspend fun Scanning.discoverDevice(
    identifier: Identifier,
    rssi: RSSI,
    advertisementData: BaseAdvertisementData,
    deviceCreator: () -> Device,
) = discoverDevices(listOf(Scanning.DiscoveredDevice(identifier, rssi, advertisementData, deviceCreator)))

/**
 * Default implementation of [ScanningState.Devices]
 */
data class DefaultDevices(
    override val allDevices: Map<Identifier, Device>,
    override val identifiersFoundForDeviceDiscoveryMode: Map<ScanningState.DeviceDiscoveryMode, Set<Identifier>>,
    override val currentScanFilter: ScanningState.DeviceDiscoveryMode.Scanning,
) : ScanningState.Devices {

    /**
     * Constructor
     * @param filter the [Filter] to use for the [currentScanFilter]
     */
    constructor(filter: Filter) : this(emptyMap(), emptyMap(), ScanningState.DeviceDiscoveryMode.Scanning(filter))

    override fun copyAndAddScanned(
        identifier: Identifier,
        createDevice: () -> Device,
    ): ScanningState.Devices = copyAndAdd(identifier, currentScanFilter, createDevice)

    override fun copyAndSetPaired(
        devices: Map<Identifier, () -> Device>,
        filter: Filter,
        removeForAllPairedFilters: Boolean,
    ): ScanningState.Devices {
        val filtersToRemove = if (removeForAllPairedFilters) {
            identifiersFoundForDeviceDiscoveryMode.keys.filterIsInstance<ScanningState.DeviceDiscoveryMode.Paired>()
        } else {
            listOf(ScanningState.DeviceDiscoveryMode.Paired(filter))
        }

        val newPairedDevices = devices.map { (identifier, createDevice) -> identifier to allDevices.getOrElse(identifier, createDevice) }
        val deviceWithFiltersRemoved = filtersToRemove.fold(this) { acc, paired ->
            acc.cleanFilter(paired)
        }
        val newIdentifiersForDiscoveryMode = deviceWithFiltersRemoved.identifiersFoundForDeviceDiscoveryMode +
            listOf(ScanningState.DeviceDiscoveryMode.Paired(filter) to devices.keys)

        val newDevices = deviceWithFiltersRemoved.allDevices + newPairedDevices

        return DefaultDevices(newDevices, newIdentifiersForDiscoveryMode, currentScanFilter)
    }

    private fun copyAndAdd(
        identifier: Identifier,
        filter: ScanningState.DeviceDiscoveryMode,
        createDevice: () -> Device,
    ): DefaultDevices {
        val device = allDevices.getOrElse(identifier, createDevice)
        val newDevices = allDevices + listOf(identifier to device)
        val identifiersForCurrentFilter = identifiersFoundForDeviceDiscoveryMode.getOrElse(filter) { emptySet() } + identifier
        val newIdentifiersForFilter = identifiersFoundForDeviceDiscoveryMode + listOf(currentScanFilter to identifiersForCurrentFilter)
        return DefaultDevices(newDevices, newIdentifiersForFilter, currentScanFilter)
    }

    override fun updateScanFilter(
        filter: Filter,
        cleanMode: BluetoothService.CleanMode,
    ): ScanningState.Devices = when (cleanMode) {
        BluetoothService.CleanMode.RETAIN_ALL -> DefaultDevices(allDevices, identifiersFoundForDeviceDiscoveryMode, ScanningState.DeviceDiscoveryMode.Scanning(filter))
        BluetoothService.CleanMode.ONLY_PROVIDED_FILTER -> cleanFilter(ScanningState.DeviceDiscoveryMode.Scanning(filter))
        BluetoothService.CleanMode.REMOVE_ALL -> DefaultDevices(emptyMap(), emptyMap(), ScanningState.DeviceDiscoveryMode.Scanning(filter))
    }

    private fun cleanFilter(filter: ScanningState.DeviceDiscoveryMode): DefaultDevices {
        val potentialIdentifiersToRemove = identifiersFoundForDeviceDiscoveryMode[filter] ?: emptySet()
        val newIdentifiersFoundForFilter = identifiersFoundForDeviceDiscoveryMode - filter
        val identifiersToRemove = newIdentifiersFoundForFilter.entries.fold(potentialIdentifiersToRemove) { acc, entry ->
            acc - entry.value
        }

        val newDevices = allDevices - identifiersToRemove
        return DefaultDevices(newDevices, newIdentifiersFoundForFilter, filter as? ScanningState.DeviceDiscoveryMode.Scanning ?: currentScanFilter)
    }
}

internal sealed class ScanningStateImpl {

    val nothingFound = DefaultDevices(filter = emptySet())

    object NotInitialized : ScanningStateImpl(), ScanningState.NotInitialized {

        fun startInitializing(
            scanner: Scanner,
        ): suspend () -> ScanningState {
            return if (!scanner.isSupported) {
                { NoHardware }
            } else {
                { Initializing(nothingFound, scanner) }
            }
        }
    }

    data class Deinitialized(
        override val previousDevices: ScanningState.Devices,
        val scanner: Scanner,
    ) :
        ScanningStateImpl(), ScanningState.Deinitialized {
            override val reinitialize = suspend { Initializing(previousDevices, scanner) }
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
        abstract val devices: ScanningState.Devices

        open val deinitialize: suspend () -> Deinitialized = { Deinitialized(devices, scanner) }
    }

    class PermittedHandler(val devices: ScanningState.Devices, val scanner: Scanner) {

        internal val revokePermission = suspend { NoBluetooth.MissingPermissions(devices, scanner) }

        internal suspend fun afterNewStateIsSet(newState: ScanningState) {
            when (newState) {
                is ScanningState.Inactive,
                is ScanningState.Initializing,
                is ScanningState.NoHardware,
                is ScanningState.NoBluetooth.MissingPermissions,
                -> scanner.stopMonitoringHardwareEnabled()
                is ScanningState.Active -> {}
            }
        }

        internal suspend fun beforeOldStateIsRemoved(oldState: ScanningState) {
            when (oldState) {
                is ScanningState.Inactive,
                is ScanningState.Initializing,
                is ScanningState.NoHardware,
                is ScanningState.NoBluetooth.MissingPermissions,
                -> scanner.startMonitoringHardwareEnabled()
                is ScanningState.Active -> {}
            }
        }
    }

    data class Initializing(
        override val devices: ScanningState.Devices,
        override val scanner: Scanner,
    ) : Active(), ScanningState.Initializing {

        override fun initialized(hasPermission: Boolean, enabled: Boolean): suspend () -> ScanningState.Initialized =
            suspend {
                when {
                    !hasPermission -> NoBluetooth.MissingPermissions(devices, scanner)
                    !enabled -> NoBluetooth.Disabled(devices, scanner)
                    else -> Enabled.Idle(devices, scanner)
                }
            }
    }

    sealed class Enabled : Active() {

        protected abstract val permittedHandler: PermittedHandler

        val disable = suspend {
            NoBluetooth.Disabled(devices, scanner)
        }

        val revokePermission: suspend () -> NoBluetooth.MissingPermissions get() = permittedHandler.revokePermission

        override suspend fun afterNewStateIsSet(newState: ScanningState) {
            super.afterNewStateIsSet(newState)
            if (newState !is ScanningState.Enabled) {
                scanner.cancelRetrievingPairedDevices()
            }
            permittedHandler.afterNewStateIsSet(newState)
        }

        override suspend fun beforeOldStateIsRemoved(oldState: ScanningState) {
            super.beforeOldStateIsRemoved(oldState)
            permittedHandler.beforeOldStateIsRemoved(oldState)
        }

        protected fun devicesForPairedDevices(
            devices: Map<Identifier, () -> Device>,
            filter: Filter,
            removeForAllPairedFilters: Boolean,
        ) = this.devices.copyAndSetPaired(
            devices,
            filter,
            removeForAllPairedFilters,
        )

        suspend fun retrievePairedDevices(
            filter: Filter,
            removeForAllPairedFilters: Boolean,
            connectionSettings: ConnectionSettings?,
        ) = scanner.retrievePairedDevices(filter, removeForAllPairedFilters, connectionSettings)

        class Idle internal constructor(
            override val devices: ScanningState.Devices,
            override val scanner: Scanner,
        ) : Enabled(), ScanningState.Enabled.Idle {

            override val permittedHandler: PermittedHandler = PermittedHandler(devices, scanner)

            override fun pairedDevices(
                devices: Map<Identifier, () -> Device>,
                filter: Filter,
                removeForAllPairedFilters: Boolean,
            ): suspend () -> ScanningState.Enabled = suspend {
                val newDevices = devicesForPairedDevices(devices, filter, removeForAllPairedFilters)
                Idle(
                    newDevices,
                    scanner,
                )
            }

            override fun startScanning(
                filter: Filter,
                cleanMode: BluetoothService.CleanMode,
                connectionSettings: ConnectionSettings?,
            ): suspend () -> ScanningState.Enabled.Scanning = {
                Scanning(
                    devices.updateScanFilter(filter, cleanMode),
                    scanner,
                    connectionSettings,
                )
            }

            override fun refresh(
                filter: Filter,
                cleanMode: BluetoothService.CleanMode,
            ): suspend () -> ScanningState.Enabled.Idle = {
                Idle(
                    devices.updateScanFilter(filter, cleanMode),
                    scanner,
                )
            }
        }

        class Scanning internal constructor(
            override val devices: ScanningState.Devices,
            override val scanner: Scanner,
            private val connectionSettings: ConnectionSettings?,
        ) : Enabled(),
            HandleAfterOldStateIsRemoved<ScanningState>,
            HandleAfterCreating<ScanningState>,
            ScanningState.Enabled.Scanning {

                override val permittedHandler: PermittedHandler = PermittedHandler(devices, scanner)

                override fun pairedDevices(
                    devices: Map<Identifier, () -> Device>,
                    filter: Filter,
                    removeForAllPairedFilters: Boolean,
                ): suspend () -> ScanningState.Enabled = suspend {
                    Scanning(
                        devicesForPairedDevices(devices, filter, removeForAllPairedFilters),
                        scanner,
                        connectionSettings,
                    )
                }

                override suspend fun discoverDevices(
                    devices: List<ScanningState.Enabled.Scanning.DiscoveredDevice>,
                ): suspend () -> ScanningState.Enabled.Scanning {
                    devices.mapNotNull { device ->
                        this.devices.allDevices[device.identifier]?.let { knownDevice ->
                            knownDevice.rssiDidUpdate(device.rssi)
                            knownDevice.advertisementDataDidUpdate(device.advertisementData)
                        }
                    }
                    val unknownDevices = devices.filter {
                        !this.devices.identifiersForCurrentScanFilter.contains(it.identifier)
                    }
                    return if (unknownDevices.isEmpty()) {
                        remain()
                    } else {
                        suspend {
                            val newDiscovered = unknownDevices.fold(this.devices) { acc, discoveredDevice ->
                                acc.copyAndAddScanned(discoveredDevice.identifier, discoveredDevice.deviceCreator)
                            }
                            Scanning(newDiscovered, scanner, connectionSettings)
                        }
                    }
                }

                override fun stopScanning(cleanMode: BluetoothService.CleanMode): suspend () -> ScanningState.Enabled.Idle = {
                    Idle(devices.updateScanFilter(devices.currentScanFilter.filter, cleanMode), scanner)
                }

                override suspend fun afterOldStateIsRemoved(oldState: ScanningState) {
                    if (oldState !is Scanning) {
                        scanner.scanForDevices(devices.currentScanFilter.filter, connectionSettings)
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

        override val devices: ScanningState.Devices = nothingFound
        abstract val lastDevices: ScanningState.Devices
        override val deinitialize: suspend () -> Deinitialized = { Deinitialized(lastDevices, scanner) }

        class Disabled internal constructor(
            override val lastDevices: ScanningState.Devices,
            override val scanner: Scanner,
        ) : NoBluetooth(), ScanningState.NoBluetooth.Disabled {

            private val permittedHandler = PermittedHandler(lastDevices, scanner)

            override val enable: suspend () -> ScanningState.Enabled = {
                Enabled.Idle(lastDevices, scanner)
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
            override val lastDevices: ScanningState.Devices,
            override val scanner: Scanner,
        ) : NoBluetooth(), ScanningState.NoBluetooth.MissingPermissions {

            override fun permit(enabled: Boolean): suspend () -> ScanningState = {
                if (enabled) {
                    Enabled.Idle(lastDevices, scanner)
                } else {
                    Disabled(lastDevices, scanner)
                }
            }
        }
    }

    object NoHardware : ScanningStateImpl(), ScanningState.NoHardware
}
