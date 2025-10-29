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

import android.bluetooth.BluetoothGattDescriptor
import com.splendo.kaluga.bluetooth.Descriptor
import com.splendo.kaluga.bluetooth.UUID

actual class LocalDescriptor internal constructor(
    val descriptor: BluetoothGattDescriptor,
    actual override val characteristic: LocalCharacteristic,
) : Descriptor {

    internal class DSL(val uuid: UUID) : LocalDescriptorDSL {

        private var permissions = 0
        private var readAction: (suspend LocalDescriptor.(ConnectedDevice, Int) -> GattResponse.ReadResponse)? = null
        private var writeAction: (suspend LocalDescriptor.(ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse)? = null

        override fun readable(
            encrypted: Boolean,
            onRead: suspend LocalDescriptor.(ConnectedDevice, Int) -> GattResponse.ReadResponse
        ) {
            require(readAction == null) { "Read already set" }
            permissions = permissions or if (encrypted) BluetoothGattDescriptor.PERMISSION_READ_ENCRYPTED else BluetoothGattDescriptor.PERMISSION_READ
            readAction = onRead
        }

        override fun writable(
            encrypted: Boolean,
            onWrite: suspend LocalDescriptor.(ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse
        ) {
            require(writeAction == null) { "Write already set" }
            permissions = permissions or if (encrypted) BluetoothGattDescriptor.PERMISSION_WRITE_ENCRYPTED else BluetoothGattDescriptor.PERMISSION_WRITE
            writeAction = onWrite
        }

        fun build(forCharacteristic: LocalCharacteristic): LocalDescriptor = LocalDescriptor(
            BluetoothGattDescriptor(uuid, permissions),
            forCharacteristic
        ).apply {
            readAction?.let { onRead ->
                forCharacteristic.server.callback.registerReadAction(this, onRead)
            }
            writeAction?.let { onRead ->
                forCharacteristic.server.callback.registerWriteAction(this, onRead)
            }
        }
    }

    override val uuid: UUID = descriptor.uuid
}
