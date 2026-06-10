/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

import android.bluetooth.BluetoothDevice
import com.splendo.kaluga.bluetooth.MTU
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.Identifier

actual interface ConnectedDevice : Device {
    actual val mtu: MTU?

    /**
     * The platform [BluetoothDevice] of the connected device
     */
    val device: BluetoothDevice
}

/**
 * The default [ConnectedDevice], wrapping the platform [BluetoothDevice].
 * @property device the platform [BluetoothDevice] of the connected device
 */
class DefaultConnectedDevice internal constructor(override val device: BluetoothDevice, private val mtuProvider: () -> MTU?) : ConnectedDevice {

    /**
     * @param device the platform [BluetoothDevice] of the connected device
     * @param mtu the [MTU] negotiated with the device, or `null` if it is not known
     */
    constructor(device: BluetoothDevice, mtu: MTU? = null) : this(device, { mtu })

    override val identifier: Identifier = device.address

    // Read live so a single cached instance (held e.g. via Notifiable.subscribedDevices) keeps reporting the current MTU.
    override val mtu: MTU? get() = mtuProvider()

    // Equality is keyed on the identifier only, so a device used as a map key (e.g. write reassembly caches) keeps matching.
    override fun equals(other: Any?): Boolean = other is DefaultConnectedDevice && other.identifier == identifier
    override fun hashCode(): Int = identifier.hashCode()
}
