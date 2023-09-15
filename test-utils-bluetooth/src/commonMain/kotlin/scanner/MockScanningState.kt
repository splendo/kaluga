/*
 * Copyright 2022 Splendo Consulting B.V. The Netherlands
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.splendo.kaluga.test.bluetooth.scanner

import com.splendo.kaluga.bluetooth.BluetoothService
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.scanner.DefaultDevices
import com.splendo.kaluga.bluetooth.scanner.Filter
import com.splendo.kaluga.bluetooth.scanner.ScanningState
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.parameters.mock

sealed class MockScanningState {

    val nothingFound = DefaultDevices(filter = emptySet())

    sealed class Inactive : MockScanningState()
    data class NotInitialized(val isHardwareSupported: Boolean) : Inactive(), ScanningState.NotInitialized {

        fun startInitializing() = if (!isHardwareSupported) {
            { NoHardware }
        } else {
            { Initializing(nothingFound) }
        }
    }

    data class Deinitialized(
        override val previousDevices: ScanningState.Devices,
    ) :
        Inactive(), ScanningState.Deinitialized {
            override val reinitialize = suspend { Initializing(previousDevices) }
        }

    sealed class Active : MockScanningState() {
        abstract val devices: ScanningState.Devices
        open val deinitialize: suspend () -> Deinitialized = { Deinitialized(devices) }
    }

    class PermittedHandler(private val devices: ScanningState.Devices) {
        val revokePermission = suspend { NoBluetooth.MissingPermissions(devices) }
    }

    data class Initializing(
        override val devices: ScanningState.Devices,
    ) : Active(), ScanningState.Initializing {

        override fun initialized(hasPermission: Boolean, enabled: Boolean): suspend () -> ScanningState.Initialized =
            suspend {
                when {
                    !hasPermission -> NoBluetooth.MissingPermissions(devices)
                    !enabled -> NoBluetooth.Disabled(devices)
                    else -> Enabled.Idle(devices)
                }
            }
    }

    sealed class Initialized : MockScanningState()

    sealed class Enabled : Active() {

        protected abstract val permittedHandler: PermittedHandler

        val disable = suspend {
            NoBluetooth.Disabled(devices)
        }

        val revokePermission: suspend () -> NoBluetooth.MissingPermissions get() = permittedHandler.revokePermission

        protected fun devicesForPairedDevices(
            devices: Map<Identifier, () -> Device>,
            filter: Filter,
            removeForAllPairedFilters: Boolean,
        ) = this.devices.copyAndSetPaired(devices, filter, removeForAllPairedFilters)

        class Idle(
            override val devices: ScanningState.Devices,
        ) : Enabled(), ScanningState.Enabled.Idle {

            override val permittedHandler: PermittedHandler = PermittedHandler(devices)

            val retrievePairedDevicesMock = this::retrievePairedDevices.mock()

            override suspend fun retrievePairedDevices(
                filter: Filter,
                removeForAllPairedFilters: Boolean,
                connectionSettings: ConnectionSettings?,
            ): Unit = retrievePairedDevicesMock.call(filter, removeForAllPairedFilters, connectionSettings)

            override fun pairedDevices(
                devices: Map<Identifier, () -> Device>,
                filter: Filter,
                removeForAllPairedFilters: Boolean,
            ): suspend () -> ScanningState.Enabled = {
                Idle(
                    devicesForPairedDevices(devices, filter, removeForAllPairedFilters),
                )
            }

            override fun startScanning(
                filter: Filter,
                cleanMode: BluetoothService.CleanMode,
                connectionSettings: ConnectionSettings?,
            ): suspend () -> ScanningState.Enabled.Scanning = {
                Scanning(
                    devices.updateScanFilter(filter, cleanMode),
                )
            }

            override fun refresh(
                filter: Filter,
                cleanMode: BluetoothService.CleanMode,
            ): suspend () -> ScanningState.Enabled.Idle = {
                Idle(
                    devices.updateScanFilter(filter, cleanMode),
                )
            }
        }

        class Scanning(
            override val devices: ScanningState.Devices,
        ) : Enabled(),
            ScanningState.Enabled.Scanning {

                override val permittedHandler: PermittedHandler = PermittedHandler(devices)

                val retrievePairedDevicesMock = this::retrievePairedDevices.mock()

                override suspend fun retrievePairedDevices(
                    filter: Filter,
                    removeForAllPairedFilters: Boolean,
                    connectionSettings: ConnectionSettings?,
                ): Unit = retrievePairedDevicesMock.call(filter, removeForAllPairedFilters, connectionSettings)

                override fun pairedDevices(
                    devices: Map<Identifier, () -> Device>,
                    filter: Filter,
                    removeForAllPairedFilters: Boolean,
                ): suspend () -> ScanningState.Enabled = {
                    Scanning(
                        devicesForPairedDevices(devices, filter, removeForAllPairedFilters),
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
                            Scanning(
                                newDiscovered,
                            )
                        }
                    }
                }

                override fun stopScanning(cleanMode: BluetoothService.CleanMode): suspend () -> ScanningState.Enabled.Idle = {
                    Idle(devices.updateScanFilter(devices.currentScanFilter.filter, cleanMode))
                }
            }
    }

    sealed class NoBluetooth : Active() {

        override val devices: ScanningState.Devices = nothingFound
        protected abstract val previousDevices: ScanningState.Devices
        override val deinitialize: suspend () -> Deinitialized = { Deinitialized(previousDevices) }

        data class Disabled(override val previousDevices: ScanningState.Devices) : NoBluetooth(), ScanningState.NoBluetooth.Disabled {

            private val permittedHandler = PermittedHandler(previousDevices)

            override val enable: suspend () -> ScanningState.Enabled = {
                Enabled.Idle(previousDevices)
            }

            override val revokePermission: suspend () -> MissingPermissions = permittedHandler.revokePermission
        }

        data class MissingPermissions(override val previousDevices: ScanningState.Devices) : NoBluetooth(), ScanningState.NoBluetooth.MissingPermissions {

            override fun permit(enabled: Boolean): suspend () -> ScanningState = {
                if (enabled) Enabled.Idle(previousDevices) else Disabled(previousDevices)
            }
        }
    }

    object NoHardware : MockScanningState(), ScanningState.NoHardware
}
