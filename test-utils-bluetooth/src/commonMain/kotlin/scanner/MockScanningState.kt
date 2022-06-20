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

import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.BaseAdvertisementData
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.scanner.Filter
import com.splendo.kaluga.bluetooth.scanner.ScanningState

sealed class MockScanningState {

    companion object {
        val nothingDiscovered = Discovered(emptySet())
    }

    data class Discovered(
        override val devices: List<Device>,
        override val filter: Filter,
    ) : ScanningState.Discovered {
        constructor(filter: Filter) : this(emptyList(), filter)

        override fun copyAndAdd(device: Device): Discovered =
            Discovered(listOf(*devices.toTypedArray(), device), filter)

        override fun discoveredForFilter(filter: Filter) =
            if (this.filter == filter)
                this
            else
                Discovered(filter)
    }

    sealed class Inactive : MockScanningState()
    data class NotInitialized(val isHardwareSupported: Boolean) : Inactive(), ScanningState.NotInitialized {

        fun startInitializing() = if (!isHardwareSupported) {
            { NoHardware }
        } else {
            { Initializing(nothingDiscovered) }
        }
    }

    data class Deinitialized(override val previouslyDiscovered: ScanningState.Discovered) :
        Inactive(), ScanningState.Deinitialized {
        override val reinitialize = suspend { Initializing(previouslyDiscovered) }
    }

    sealed class Active : MockScanningState() {
        abstract val discovered: ScanningState.Discovered
        val deinitialize: suspend () -> Deinitialized = { Deinitialized(discovered) }
    }

    class PermittedHandler : ScanningState.Permitted {
        override val revokePermission = suspend { NoBluetooth.MissingPermissions() }
    }

    data class Initializing(
        override val discovered: ScanningState.Discovered
    ) : Active(), ScanningState.Initializing {

        override fun initialized(hasPermission: Boolean, enabled: Boolean): suspend () -> ScanningState.Initialized =
            suspend {
                when {
                    !hasPermission -> NoBluetooth.MissingPermissions()
                    !enabled -> NoBluetooth.Disabled()
                    else -> Enabled.Idle(discovered)
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
            override val discovered: ScanningState.Discovered
        ) : Enabled(), ScanningState.Enabled.Idle {

            override val permittedHandler: PermittedHandler = PermittedHandler()

            override fun startScanning(filter: Set<UUID>): suspend () -> Scanning = {
                Scanning(
                    discovered.discoveredForFilter(filter)
                )
            }

            override fun refresh(filter: Set<UUID>): suspend () -> Idle = {
                Idle(
                    discovered.discoveredForFilter(filter)
                )
            }
        }

        class Scanning(
            override val discovered: ScanningState.Discovered
        ) : Enabled(),
            ScanningState.Enabled.Scanning {

            override val permittedHandler: PermittedHandler = PermittedHandler()

            override suspend fun discoverDevice(
                identifier: Identifier,
                rssi: Int,
                advertisementData: BaseAdvertisementData,
                deviceCreator: () -> Device
            ): suspend () -> ScanningState.Enabled.Scanning {

                return discovered.devices.find { it.identifier == identifier }
                    ?.let { knownDevice ->
                        knownDevice.advertisementDataDidUpdate(advertisementData)
                        knownDevice.rssiDidUpdate(rssi)
                        remain()
                    } ?: suspend { Scanning(discovered.copyAndAdd(deviceCreator())) }
            }

            override val stopScanning = suspend { Idle(discovered) }
        }
    }

    sealed class NoBluetooth : Active() {

        override val discovered: Discovered = nothingDiscovered

        class Disabled : NoBluetooth(), ScanningState.NoBluetooth.Disabled {

            private val permittedHandler = PermittedHandler()

            override val enable: suspend () -> ScanningState.Enabled = {
                Enabled.Idle(nothingDiscovered)
            }

            override val revokePermission: suspend () -> MissingPermissions = permittedHandler.revokePermission
        }

        class MissingPermissions : NoBluetooth(), ScanningState.NoBluetooth.MissingPermissions {

            override fun permit(enabled: Boolean): suspend () -> ScanningState = {
                if (enabled) Enabled.Idle(nothingDiscovered)
                else Disabled()
            }
        }
    }

    object NoHardware : MockScanningState(), ScanningState.NoHardware
}
