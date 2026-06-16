/*
 Copyright 2025 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bluetooth.server

import com.splendo.kaluga.bluetooth.MTU
import com.splendo.kaluga.bluetooth.mtu
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.Identifier
import platform.CoreBluetooth.CBCentral

actual interface ConnectedDevice : Device {
    actual val mtu: MTU?

    /**
     * The platform [CBCentral] of the connected device
     */
    val cbCentral: CBCentral
}

/**
 * The default [ConnectedDevice], wrapping the platform [CBCentral].
 * @property cbCentral the platform [CBCentral] of the connected device
 */
class DefaultConnectedDevice(override val cbCentral: CBCentral) : ConnectedDevice {
    override val identifier: Identifier = cbCentral.identifier

    override val mtu: MTU get() = cbCentral.mtu

    // Equality is keyed on the identifier only, so a device used as a map key (e.g. write reassembly caches)
    // keeps matching across callbacks even though [mtu] is read live from the central.
    override fun equals(other: Any?): Boolean = other is DefaultConnectedDevice && other.identifier == identifier
    override fun hashCode(): Int = identifier.hashCode()
}
