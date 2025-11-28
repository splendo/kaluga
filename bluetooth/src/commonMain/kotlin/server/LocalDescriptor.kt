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
import com.splendo.kaluga.bluetooth.serialization.ByteArrayEndedBeforeSerializationCompleted
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.serializer

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
            serializationStrategy: SerializationStrategy<T>,
            bluetoothFormat: BluetoothFormat = BluetoothFormat,
            onRead: suspend LocalDescriptor.(ConnectedDevice) -> T,
        ) {
            readable(encrypted) { device, offset ->
                GattResponse.ReadSuccess(onRead(device), offset, serializationStrategy, bluetoothFormat)
            }
        }
        fun writable(encrypted: Boolean = false, onWrite: suspend LocalDescriptor.(ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse)

        fun <T> writable(
            encrypted: Boolean = false,
            serializationStrategy: DeserializationStrategy<T>,
            bluetoothFormat: BluetoothFormat = BluetoothFormat,
            onFailedToWrite: suspend LocalDescriptor.(ConnectedDevice, Exception) -> GattResponse.WriteResponse = { _, _ -> GattResponse.ApplicationError(0x80) },
            onWrite: suspend LocalDescriptor.(ConnectedDevice, T) -> GattResponse.WriteResponse,
        ) {
            val cache = mutableMapOf<ConnectedDevice, ByteArray>()
            writable(encrypted) { device, value, offset ->
                val currentCache = cache.remove(device) ?: byteArrayOf()
                val valueToDeserialize = when (offset) {
                    0 -> {
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
                    } catch (_: ByteArrayEndedBeforeSerializationCompleted) {
                        cache[device] = valueToDeserialize
                        GattResponse.WriteSuccess
                    } catch (e: Exception) {
                        onFailedToWrite(device, e)
                    }
                } ?: GattResponse.InvalidOffset
            }
        }

        fun writableAlwaysSuccess(encrypted: Boolean = false, onWrite: suspend LocalDescriptor.(ConnectedDevice, ByteArray, Int) -> Unit) {
            writable(encrypted) { device, value, offset ->
                onWrite(device, value, offset)
                GattResponse.WriteSuccess
            }
        }

        fun <T> writableAlwaysSuccess(
            encrypted: Boolean = false,
            serializationStrategy: DeserializationStrategy<T>,
            bluetoothFormat: BluetoothFormat = BluetoothFormat,
            onWrite: suspend LocalDescriptor.(ConnectedDevice, T) -> Unit,
        ) = writable(
            encrypted,
            serializationStrategy,
            bluetoothFormat,
            { _, _ -> GattResponse.WriteSuccess },
        ) { device, value ->
            onWrite(device, value)
            GattResponse.WriteSuccess
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

inline fun <reified T : Any> LocalDescriptor.DSL.readableAlwaysSuccess(
    encrypted: Boolean = false,
    bluetoothFormat: BluetoothFormat = BluetoothFormat,
    noinline onRead: suspend LocalDescriptor.(ConnectedDevice) -> T,
) = readableAlwaysSuccess(encrypted, bluetoothFormat.serializer<T>(), bluetoothFormat, onRead)

inline fun <reified T : Any> LocalDescriptor.DSL.writable(
    encrypted: Boolean = false,
    bluetoothFormat: BluetoothFormat = BluetoothFormat,
    noinline onFailedToWrite: suspend LocalDescriptor.(ConnectedDevice, Exception) -> GattResponse.WriteResponse = { _, _ -> GattResponse.ApplicationError(0x80) },
    noinline onWrite: suspend LocalDescriptor.(ConnectedDevice, T) -> GattResponse.WriteResponse,
) = writable(encrypted, bluetoothFormat.serializer<T>(), bluetoothFormat, onFailedToWrite, onWrite)

inline fun <reified T : Any> LocalDescriptor.DSL.writableAlwaysSuccess(
    encrypted: Boolean = false,
    bluetoothFormat: BluetoothFormat = BluetoothFormat,
    noinline onWrite: suspend LocalDescriptor.(ConnectedDevice, T) -> Unit,
) = writableAlwaysSuccess(encrypted, bluetoothFormat.serializer<T>(), bluetoothFormat, onWrite)
