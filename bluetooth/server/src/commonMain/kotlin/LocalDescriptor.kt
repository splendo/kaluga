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

import com.splendo.kaluga.bluetooth.Descriptor
import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.serialization.BluetoothFormat
import com.splendo.kaluga.bluetooth.serialization.ByteArrayEndedBeforeSerializationCompleted
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy

/**
 * A [Descriptor] available from a [BluetoothServer]
 * @property wrapper the [com.splendo.kaluga.bluetooth.server.LocalDescriptorWrapper] to access the platform descriptor
 * @property characteristic the [LocalCharacteristic] this descriptor belongs to
 */
class LocalDescriptor(val wrapper: LocalDescriptorWrapper, override val characteristic: LocalCharacteristic) : Descriptor {

    /**
     * DSL for setting up a [com.splendo.kaluga.bluetooth.server.LocalDescriptor]
     */
    interface DSL {

        /**
         * Makes this [LocalDescriptor] readable by a [com.splendo.kaluga.bluetooth.server.ConnectedDevice]
         * Cannot be called if [readable], or [readableAlwaysSuccess] has been called before
         * @param encrypted `true` if reading from the descriptor should be encrypted. This will result in [LocalCharacteristic.Permission.READ_ENCRYPTION_REQUIRED].
         * Otherwise will add [LocalCharacteristic.Permission.READABLE]
         * @param onRead the function to call when reading from the descriptor.
         * This contains the [com.splendo.kaluga.bluetooth.server.ConnectedDevice] and the offset of the data to read and should return a [GattResponse.ReadResponse]
         */
        fun readable(encrypted: Boolean = false, onRead: suspend LocalDescriptor.(ConnectedDevice, Int) -> GattResponse.ReadResponse)

        /**
         * Makes this [LocalDescriptor] readable by a [com.splendo.kaluga.bluetooth.server.ConnectedDevice] to always return [GattResponse.ReadSuccess]
         * Cannot be called if [readable], or [readableAlwaysSuccess] has been called before
         * @param encrypted `true` if reading from the descriptor should be encrypted. This will result in [LocalCharacteristic.Permission.READ_ENCRYPTION_REQUIRED].
         * Otherwise will add [LocalCharacteristic.Permission.READABLE]
         * @param onRead the function to call when reading from the descriptor.
         * This contains the [com.splendo.kaluga.bluetooth.server.ConnectedDevice] and the offset of the data to read and should return the [ByteArray] being read.
         */
        fun readableAlwaysSuccess(encrypted: Boolean = false, onRead: suspend LocalDescriptor.(ConnectedDevice, Int) -> ByteArray) {
            readable(encrypted) { device, offset ->
                GattResponse.ReadSuccess(onRead(this, device, offset))
            }
        }

        /**
         * Makes this [LocalDescriptor] readable by a [com.splendo.kaluga.bluetooth.server.ConnectedDevice] to always return [GattResponse.ReadSuccess]
         * Cannot be called if [readable], or [readableAlwaysSuccess] has been called before
         * @param T the type of the data being read
         * @param encrypted `true` if reading from the descriptor should be encrypted. This will result in [LocalCharacteristic.Permission.READ_ENCRYPTION_REQUIRED].
         * Otherwise will add [LocalCharacteristic.Permission.READABLE]
         * @param serializationStrategy the [SerializationStrategy] to use to encode the [T] to a [ByteArray]
         * @param bluetoothFormat the [BluetoothFormat] to use to encode the [T] to a [ByteArray]
         * @param onRead the function to call when reading from the descriptor.
         * This contains the [com.splendo.kaluga.bluetooth.server.ConnectedDevice] and the offset of the data to read and should return the [ByteArray] being read.
         */
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

        /**
         * Makes this [LocalDescriptor] writable by a [com.splendo.kaluga.bluetooth.server.ConnectedDevice]
         * Cannot be called if [writable] has been called before
         * @param encrypted `true` if reading from the descriptor should be encrypted. This will result in [LocalCharacteristic.Permission.WRITE_ENCRYPTION_REQUIRED].
         * Otherwise will add [LocalCharacteristic.Permission.WRITABLE]
         * @param onWrite the function to call when reading from the descriptor.
         * This contains the [com.splendo.kaluga.bluetooth.server.ConnectedDevice], the [ByteArray] to write and the offset of the data to write and should return a [GattResponse.WriteResponse]
         */
        fun writable(encrypted: Boolean = false, onWrite: suspend LocalDescriptor.(ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse)

        /**
         * Makes this [LocalDescriptor] writable by a [com.splendo.kaluga.bluetooth.server.ConnectedDevice]
         * Cannot be called if [writable] has been called before
         * @param T the type of the data being written
         * @param encrypted `true` if reading from the descriptor should be encrypted.  This will result in [LocalCharacteristic.Permission.WRITE_ENCRYPTION_REQUIRED].
         * Otherwise will add [LocalCharacteristic.Permission.WRITABLE]
         * @param deserializationStrategy the [DeserializationStrategy] to use to decode the [ByteArray] being written to an instance of [T]
         * @param onFailedToWrite the function to call when writing to the descriptor fails.
         * This contains the [com.splendo.kaluga.bluetooth.server.ConnectedDevice] and the exception that caused deserialization to fail and should return a [GattResponse.WriteResponse]
         * @param onWrite the function to call when reading from the descriptor.
         * This contains the [com.splendo.kaluga.bluetooth.server.ConnectedDevice], and the [T] to write and should return a [GattResponse.WriteResponse].
         * If the data being written is split over multiple offsets, this will only be called when the data can be fully deserialized
         */
        fun <T> writable(
            encrypted: Boolean = false,
            deserializationStrategy: DeserializationStrategy<T>,
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
                        onWrite(device, bluetoothFormat.decodeFromByteArray(deserializationStrategy, it))
                    } catch (_: ByteArrayEndedBeforeSerializationCompleted) {
                        cache[device] = valueToDeserialize
                        GattResponse.WriteSuccess
                    } catch (e: Exception) {
                        onFailedToWrite(device, e)
                    }
                } ?: GattResponse.InvalidOffset
            }
        }

        /**
         * Makes this [LocalDescriptor] writable by a [com.splendo.kaluga.bluetooth.server.ConnectedDevice] and always responds with [GattResponse.WriteSuccess]
         * Cannot be called if [writable] or [writableAlwaysSuccess] has been called before
         * @param encrypted `true` if reading from the descriptor should be encrypted. This will result in [LocalCharacteristic.Permission.WRITE_ENCRYPTION_REQUIRED].
         * Otherwise will add [LocalCharacteristic.Permission.WRITABLE]
         * @param onWrite the function to call when reading from the descriptor.
         * This contains the [com.splendo.kaluga.bluetooth.server.ConnectedDevice], the [ByteArray] to write and the offset of the data to write
         */
        fun writableAlwaysSuccess(encrypted: Boolean = false, onWrite: suspend LocalDescriptor.(ConnectedDevice, ByteArray, Int) -> Unit) {
            writable(encrypted) { device, value, offset ->
                onWrite(device, value, offset)
                GattResponse.WriteSuccess
            }
        }

        /**
         * Makes this [LocalDescriptor] writable by a [com.splendo.kaluga.bluetooth.server.ConnectedDevice] and always responds with [GattResponse.WriteSuccess]
         * Cannot be called if [writable] or [writableAlwaysSuccess] has been called before
         * @param T the type of the data being written
         * @param encrypted `true` if reading from the descriptor should be encrypted. This will result in [LocalCharacteristic.Permission.WRITE_ENCRYPTION_REQUIRED].
         * Otherwise will add [LocalCharacteristic.Permission.WRITABLE]
         * @param deserializationStrategy the [DeserializationStrategy] to use to decode the [ByteArray] being written to an instance of [T]
         * @param onWrite the function to call when reading from the descriptor.
         * This contains the [com.splendo.kaluga.bluetooth.server.ConnectedDevice], and the [T] to write.
         * If the data being written is split over multiple offsets, this will only be called when the data can be fully deserialized
         */
        fun <T> writableAlwaysSuccess(
            encrypted: Boolean = false,
            deserializationStrategy: DeserializationStrategy<T>,
            bluetoothFormat: BluetoothFormat = BluetoothFormat,
            onWrite: suspend LocalDescriptor.(ConnectedDevice, T) -> Unit,
        ) = writable(
            encrypted,
            deserializationStrategy,
            bluetoothFormat,
            { _, _ -> GattResponse.WriteSuccess },
        ) { device, value ->
            onWrite(device, value)
            GattResponse.WriteSuccess
        }
    }

    /**
     * The permissions this descriptor gives to [com.splendo.kaluga.bluetooth.server.ConnectedDevice]
     */
    enum class Permissions {
        /**
         * The descriptor can be read by a [com.splendo.kaluga.bluetooth.server.ConnectedDevice]
         */
        READ,

        /**
         * The descriptor can be read by a [com.splendo.kaluga.bluetooth.server.ConnectedDevice] if an encrypted connection has been established
         */
        READ_ENCRYPTED,

        /**
         * The descriptor can be written to by a [com.splendo.kaluga.bluetooth.server.ConnectedDevice]
         */
        WRITE,

        /**
         * The descriptor can be written to by a [com.splendo.kaluga.bluetooth.server.ConnectedDevice] if an encrypted connection has been established
         */
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

    fun build(forCharacteristic: LocalCharacteristic, wrapperBuilder: LocalServiceWrapperBuilder): LocalDescriptor = LocalDescriptor(
        wrapperBuilder.createDescriptor(uuid, permissions),
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

/**
 * Accessor to the platform level Local Bluetooth descriptor.
 *
 * Implemented per platform by `DefaultLocalDescriptorWrapper` (wrapping the framework descriptor)
 * and mockable in tests.
 */
expect interface LocalDescriptorWrapper {
    val uuid: UUID
}

/**
 * Makes this [LocalDescriptor] readable by a [com.splendo.kaluga.bluetooth.server.ConnectedDevice] to always return [GattResponse.ReadSuccess]
 * Cannot be called if [LocalDescriptor.DSL.readable], or [LocalDescriptor.DSL.readableAlwaysSuccess] has been called before
 * @param T the type of the data being read
 * @param encrypted `true` if reading from the descriptor should be encrypted. This will result in [LocalCharacteristic.Permission.READ_ENCRYPTION_REQUIRED].
 * Otherwise will add [LocalCharacteristic.Permission.READABLE]
 * @param bluetoothFormat the [BluetoothFormat] to use to encode the [T] to a [ByteArray]
 * @param onRead the function to call when reading from the descriptor.
 * This contains the [com.splendo.kaluga.bluetooth.server.ConnectedDevice] and the offset of the data to read and should return the [ByteArray] being read.
 */
inline fun <reified T : Any> LocalDescriptor.DSL.readableAlwaysSuccess(
    encrypted: Boolean = false,
    bluetoothFormat: BluetoothFormat = BluetoothFormat,
    noinline onRead: suspend LocalDescriptor.(ConnectedDevice) -> T,
) = readableAlwaysSuccess(encrypted, bluetoothFormat.serializer<T>(), bluetoothFormat, onRead)

/**
 * Makes this [LocalDescriptor] writable by a [com.splendo.kaluga.bluetooth.server.ConnectedDevice]
 * Cannot be called if [LocalDescriptor.DSL.writable] or [LocalDescriptor.DSL.writableAlwaysSuccess] has been called before
 * @param T the type of the data being written
 * @param encrypted `true` if reading from the descriptor should be encrypted.  This will result in [LocalCharacteristic.Permission.WRITE_ENCRYPTION_REQUIRED].
 * Otherwise will add [LocalCharacteristic.Permission.WRITABLE]
 * @param onFailedToWrite the function to call when writing to the descriptor fails.
 * This contains the [com.splendo.kaluga.bluetooth.server.ConnectedDevice] and the exception that caused deserialization to fail and should return a [GattResponse.WriteResponse]
 * @param onWrite the function to call when reading from the descriptor.
 * This contains the [com.splendo.kaluga.bluetooth.server.ConnectedDevice], and the [T] to write and should return a [GattResponse.WriteResponse].
 * If the data being written is split over multiple offsets, this will only be called when the data can be fully deserialized
 */
inline fun <reified T : Any> LocalDescriptor.DSL.writable(
    encrypted: Boolean = false,
    bluetoothFormat: BluetoothFormat = BluetoothFormat,
    noinline onFailedToWrite: suspend LocalDescriptor.(ConnectedDevice, Exception) -> GattResponse.WriteResponse = { _, _ -> GattResponse.ApplicationError(0x80) },
    noinline onWrite: suspend LocalDescriptor.(ConnectedDevice, T) -> GattResponse.WriteResponse,
) = writable(encrypted, bluetoothFormat.serializer<T>(), bluetoothFormat, onFailedToWrite, onWrite)

/**
 * Makes this [LocalDescriptor] writable by a [com.splendo.kaluga.bluetooth.server.ConnectedDevice] and always responds with [GattResponse.WriteSuccess]
 * Cannot be called if [LocalDescriptor.DSL.writable] or [LocalDescriptor.DSL.writableAlwaysSuccess] has been called before
 * @param T the type of the data being written
 * @param encrypted `true` if reading from the descriptor should be encrypted. This will result in [LocalCharacteristic.Permission.WRITE_ENCRYPTION_REQUIRED].
 * Otherwise will add [LocalCharacteristic.Permission.WRITABLE]
 * @param onWrite the function to call when reading from the descriptor.
 * This contains the [com.splendo.kaluga.bluetooth.server.ConnectedDevice], and the [T] to write.
 * If the data being written is split over multiple offsets, this will only be called when the data can be fully deserialized
 */
inline fun <reified T : Any> LocalDescriptor.DSL.writableAlwaysSuccess(
    encrypted: Boolean = false,
    bluetoothFormat: BluetoothFormat = BluetoothFormat,
    noinline onWrite: suspend LocalDescriptor.(ConnectedDevice, T) -> Unit,
) = writableAlwaysSuccess(encrypted, bluetoothFormat.serializer<T>(), bluetoothFormat, onWrite)
