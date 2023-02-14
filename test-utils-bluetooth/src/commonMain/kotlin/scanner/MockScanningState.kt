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

import com.splendo.kaluga.bluetooth.RSSI
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.BaseAdvertisementData
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.scanner.Filter
import com.splendo.kaluga.bluetooth.scanner.ScanningState

sealed class MockScanningState {

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

    sealed class Inactive : MockScanningState()
    data class NotInitialized(val isHardwareSupported: Boolean) : Inactive(), ScanningState.NotInitialized {

        fun startInitializing() = if (!isHardwareSupported) {
            { NoHardware }
        } else {
            { Initializing(nothingFound, nothingFound) }
        }
    }

    data class Deinitialized(
        override val previouslyDiscovered: ScanningState.Devices,
        override val previouslyPaired: ScanningState.Devices
    ) :
        Inactive(), ScanningState.Deinitialized {
        override val reinitialize = suspend { Initializing(previouslyDiscovered, previouslyPaired) }
    }

    sealed class Active : MockScanningState() {
        abstract val discovered: ScanningState.Devices
        abstract val paired: ScanningState.Devices
        val deinitialize: suspend () -> Deinitialized = { Deinitialized(discovered, paired) }
    }

    class PermittedHandler {
        val revokePermission = suspend { NoBluetooth.MissingPermissions() }
    }

    data class Initializing(
        override val discovered: ScanningState.Devices,
        override val paired: ScanningState.Devices
    ) : Active(), ScanningState.Initializing {

        override fun initialized(hasPermission: Boolean, enabled: Boolean): suspend () -> ScanningState.Initialized =
            suspend {
                when {
                    !hasPermission -> NoBluetooth.MissingPermissions()
                    !enabled -> NoBluetooth.Disabled()
                    else -> Enabled.Idle(discovered, paired)
                }
            }
    }

    sealed class Initialized : MockScanningState()

    sealed class Enabled : Active() {

        protected abstract val permittedHandler: PermittedHandler

        val disable = suspend {
            NoBluetooth.Disabled()
        }

        val revokePermission: suspend () -> NoBluetooth.MissingPermissions get() = permittedHandler.revokePermission

        class Idle(
            override val discovered: ScanningState.Devices,
            override val paired: ScanningState.Devices
        ) : Enabled(), ScanningState.Enabled.Idle {

            override val permittedHandler: PermittedHandler = PermittedHandler()

            override suspend fun retrievePairedDevices(filter: Filter) = Unit

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
                        Devices(deviceCreators.map { it.invoke() }, filter)
                    )
                }
            }

            override fun startScanning(filter: Set<UUID>): suspend () -> Scanning = {
                Scanning(
                    discovered.foundForFilter(filter),
                    paired
                )
            }

            override fun refresh(filter: Set<UUID>): suspend () -> Idle = {
                Idle(
                    discovered.foundForFilter(filter),
                    paired
                )
            }
        }

        class Scanning(
            override val discovered: ScanningState.Devices,
            override val paired: ScanningState.Devices
        ) : Enabled(),
            ScanningState.Enabled.Scanning {

            override val permittedHandler: PermittedHandler = PermittedHandler()

            override suspend fun retrievePairedDevices(filter: Filter) = Unit

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
                        Devices(deviceCreators.map { it.invoke() }, filter)
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
                    } ?: suspend { Scanning(discovered.copyAndAdd(deviceCreator()), paired) }
            }

            override val stopScanning = suspend { Idle(discovered, paired) }
        }
    }

    sealed class NoBluetooth : Active() {

        override val discovered: Devices = nothingFound
        override val paired: Devices = nothingFound

        class Disabled : NoBluetooth(), ScanningState.NoBluetooth.Disabled {

            private val permittedHandler = PermittedHandler()

            override val enable: suspend () -> ScanningState.Enabled = {
                Enabled.Idle(nothingFound, nothingFound)
            }

            override val revokePermission: suspend () -> MissingPermissions = permittedHandler.revokePermission
        }

        class MissingPermissions : NoBluetooth(), ScanningState.NoBluetooth.MissingPermissions {

            override fun permit(enabled: Boolean): suspend () -> ScanningState = {
                if (enabled) Enabled.Idle(nothingFound, nothingFound)
                else Disabled()
            }
        }
    }

    object NoHardware : MockScanningState(), ScanningState.NoHardware
}
