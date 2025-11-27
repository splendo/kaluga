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

import com.splendo.kaluga.bluetooth.Descriptor
import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.serialization.BluetoothFormat
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationException
import kotlinx.serialization.SerializationStrategy

class LocalDescriptor(val wrapper: LocalDescriptorWrapper, override val characteristic: LocalCharacteristic) : Descriptor {

    interface DSL {
        fun readable(encrypted: Boolean = false, onRead: suspend LocalDescriptor.(ConnectedDevice, Int) -> GattResponse.ReadResponse)

        fun readableAlwaysSuccess(encrypted: Boolean = false, onRead: suspend LocalDescriptor.(ConnectedDevice, Int) -> ByteArray) {
            readable(encrypted) { device, offset ->
                GattResponse.ReadSuccess(onRead(this, device, offset))
            }
        }

        fun <T> readableAlwaysSuccess(
            encrypted: Boolean = false,
            onRead: suspend LocalDescriptor.(ConnectedDevice) -> T,
            serializationStrategy: SerializationStrategy<T>,
            bluetoothFormat: BluetoothFormat = BluetoothFormat,
        ) {
            readableAlwaysSuccess(encrypted) { device, offset ->
                bluetoothFormat.encodeToByteArray(
                    serializationStrategy,
                    onRead(device),
                ).drop(offset).toByteArray()
            }
        }
        fun writable(encrypted: Boolean = false, onWrite: suspend LocalDescriptor.(ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse)

        fun writableAlwaysSuccess(encrypted: Boolean = false, onWrite: suspend LocalDescriptor.(ConnectedDevice, ByteArray, Int) -> Unit) {
            writable(encrypted) { device, value, offset ->
                onWrite(device, value, offset)
                GattResponse.WriteSuccess
            }
        }

        fun <T> writableAlwaysSuccess(
            encrypted: Boolean = false,
            onWrite: suspend LocalDescriptor.(ConnectedDevice, T) -> Unit,
            serializationStrategy: DeserializationStrategy<T>,
            bluetoothFormat: BluetoothFormat = BluetoothFormat,
        ) {
            val cache = mutableMapOf<ConnectedDevice, ByteArray>()
            writableAlwaysSuccess(encrypted) { device, value, offset ->
                val currentCache = cache[device] ?: byteArrayOf()
                val valueToDeserialize = when (offset) {
                    0 -> {
                        cache.remove(device)
                        value
                    }
                    currentCache.size -> {
                        currentCache + value
                    }
                    else -> null
                }
                valueToDeserialize?.let {
                    try {
                        onWrite(device, bluetoothFormat.decodeFromByteArray(serializationStrategy, it))
                    } catch (e: SerializationException) {
                        cache[device] = valueToDeserialize
                    }
                }
            }
        }
    }

    enum class Permissions {
        READ,
        READ_ENCRYPTED,
        WRITE,
        WRITE_ENCRYPTED,
    }

    override val uuid: UUID = wrapper.uuid
}

internal class LocalDescriptorDSL(val uuid: UUID, val registerReadAction: LocalDescriptorRegisterReadAction, val registerWriteAction: LocalDescriptorRegisterWriteAction) :
    LocalDescriptor.DSL {

    private val permissions = mutableSetOf<LocalDescriptor.Permissions>()
    private var readAction: (suspend LocalDescriptor.(ConnectedDevice, Int) -> GattResponse.ReadResponse)? = null
    private var writeAction: (suspend LocalDescriptor.(ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse)? = null

    override fun readable(encrypted: Boolean, onRead: suspend LocalDescriptor.(ConnectedDevice, Int) -> GattResponse.ReadResponse) {
        require(readAction == null) { "Read already set" }
        permissions.add(if (encrypted) LocalDescriptor.Permissions.READ_ENCRYPTED else LocalDescriptor.Permissions.READ)
        readAction = onRead
    }

    override fun writable(encrypted: Boolean, onWrite: suspend LocalDescriptor.(ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse) {
        require(writeAction == null) { "Write already set" }
        permissions.add(if (encrypted) LocalDescriptor.Permissions.WRITE_ENCRYPTED else LocalDescriptor.Permissions.WRITE)
        writeAction = onWrite
    }

    fun build(forCharacteristic: LocalCharacteristic): LocalDescriptor = LocalDescriptor(
        LocalDescriptorWrapper(uuid, permissions),
        forCharacteristic,
    ).apply {
        forCharacteristic.wrapper.addDescriptor(wrapper)
        readAction?.let { onRead ->
            registerReadAction(this, onRead)
        }
        writeAction?.let { onWrite ->
            registerWriteAction(this, onWrite)
        }
    }
}

expect class LocalDescriptorWrapper internal constructor(uuid: UUID, permissions: Set<LocalDescriptor.Permissions>) {
    val uuid: UUID
}
