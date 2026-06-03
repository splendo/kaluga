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
@file:JvmName("AndroidLocalDescriptor")

package com.splendo.kaluga.bluetooth.server

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.server.LocalDescriptor.Permissions

actual interface LocalDescriptorWrapper {
    actual val uuid: UUID

    /**
     * Identity used to correlate this descriptor with incoming GATT callbacks.
     */
    val identity: AttributeIdentity

    /**
     * Adds the descriptor to a [BluetoothGattCharacteristic]
     */
    fun addToCharacteristic(characteristic: BluetoothGattCharacteristic)
}

class DefaultLocalDescriptorWrapper(internal val descriptor: BluetoothGattDescriptor) : LocalDescriptorWrapper {

    constructor(uuid: UUID, permissions: Set<Permissions>) : this(
        BluetoothGattDescriptor(
            uuid,
            permissions.fold(0) { acc, permission ->
                acc or
                    permission.rawValue
            },
        ),
    )

    override val uuid: UUID = descriptor.uuid

    override val identity: AttributeIdentity get() = GattDescriptorIdentity(descriptor)

    override fun addToCharacteristic(characteristic: BluetoothGattCharacteristic) {
        characteristic.addDescriptor(descriptor)
    }
}

@JvmInline
value class GattDescriptorIdentity(val descriptor: BluetoothGattDescriptor) : AttributeIdentity

private val Permissions.rawValue: Int get() = when (this) {
    Permissions.READ -> BluetoothGattDescriptor.PERMISSION_READ
    Permissions.READ_ENCRYPTED -> BluetoothGattDescriptor.PERMISSION_READ_ENCRYPTED
    Permissions.WRITE -> BluetoothGattDescriptor.PERMISSION_WRITE
    Permissions.WRITE_ENCRYPTED -> BluetoothGattDescriptor.PERMISSION_WRITE_ENCRYPTED
}
