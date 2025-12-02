/*
 Copyright (c) 2020. Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceConnectionManager
import com.splendo.kaluga.bluetooth.serialization.BluetoothFormat
import com.splendo.kaluga.logging.ContextualLogger
import com.splendo.kaluga.logging.info
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy

/**
 * The Attribute Protocol allows a device referred to as the server to expose a set of attributes and their associated values to a peer device referred to as the client.
 * These attributes exposed by the server can be discovered, read, and written by a client, and can be indicated and notified by the server.
 *
 * Each attribute has an attribute type that identifies, by means of a [UUID], what the attribute represents so that a client can understand the attributes exposed by a server.
 */
interface Attribute {
    /**
     * The [UUID] of the attribute
     */
    val uuid: UUID
}

/**
 * Gets the [Attribute] of [T] with the given [UUID] from a [List] of [T]
 * @throws NoSuchElementException if the [Attribute] is not found
 * @return the [Attribute] of [T] with the given [UUID]
 */
operator fun <T : Attribute> List<T>.get(uuid: UUID) = first { it.uuid.uuidString == uuid.uuidString }

/**
 * Gets the [Attribute] of [T] with the given [UUID] from a [List] of [T] or `null` if it cannot be found
 * @return the [Attribute] of [T] with the given [UUID] or `null` if it cannot be found
 */
fun <T : Attribute> List<T>.getOrNull(uuid: UUID) = find { it.uuid.uuidString == uuid.uuidString }

/**
 * Gets the [Flow] of [Attribute] of [T] with the given [UUID] from a [Flow] of [List] of [T]
 * @return the [Flow] of [Attribute] of [T] with the given [UUID]. Flow throws [NoSuchElementException] if the attribute cannot be found.
 */
operator fun <T : Attribute> Flow<List<T>>.get(uuid: UUID): Flow<T> = this.map { attributes ->
    attributes[uuid]
}.distinctUntilChanged()

/**
 * Gets the [Flow] of [Attribute] of [T] with the given [UUID] from a [Flow] of [List] of [T], or `null` if it cannot be found
 * @return the [Flow] of [Attribute] of [T] with the given [UUID] or `null` if it cannot be found.
 */
fun <T : Attribute> Flow<List<T>>.getOrNull(uuid: UUID): Flow<T?> = this.map { attributes ->
    attributes.getOrNull(uuid)
}.distinctUntilChanged()

/**
 * An Exception thrown when [RemoteAttribute.read] could not read into a data object
 * @property reason the [GattResponse.ReadError] that caused the read to fail
 */
class FailedToReadException(val reason: GattResponse.ReadError) : Exception()

/**
 * An [Attribute] for the Client side of a Bluetooth Low Energy connection.
 * @param ReadAction the [DeviceAction.Read] associated with the attribute
 * @param WriteAction the [DeviceAction.Write] associated with the attribute
 * @param emitNewAction method to call when a new [DeviceConnectionManager.Event.AddAction] event should take place
 * @param logger the [ContextualLogger] to use for logging.
 */
abstract class RemoteAttribute<ReadAction : DeviceAction.Read, WriteAction : DeviceAction.Write>(
    private val emitNewAction: (DeviceConnectionManager.Event.AddAction) -> Unit,
    private val logger: ContextualLogger,
) : Attribute {

    /**
     * Requests a read of the value of the attribute from the device and suspends until the value is received.
     * @return the [GattResponse.ReadResponse] received from the device
     */
    suspend fun read(): GattResponse.ReadResponse = startRead().response.await()

    /**
     * Requests a read of the a value of [T] of the attribute from the device and suspends until the value is received.
     * @param T the type of the data object to read
     * @param deserializationStrategy the [DeserializationStrategy] to use to deserialize the value
     * @param bluetoothFormat the [BluetoothFormat] to use to deserialize the value
     * @throws FailedToReadException if the read fails
     * @return the value of type [T] received from the device
     */
    suspend fun <T> read(deserializationStrategy: DeserializationStrategy<T>, bluetoothFormat: BluetoothFormat = BluetoothFormat): T = when (val response = read()) {
        is GattResponse.ReadSuccess -> bluetoothFormat.decodeFromByteArray(deserializationStrategy, response.value)
        is GattResponse.ReadError -> throw FailedToReadException(response)
    }

    /**
     * Requests a read of the a value of [T] of the attribute from the device and suspends until the value is received.
     * @param T the type of the data object to read
     * @param deserializationStrategy the [DeserializationStrategy] to use to deserialize the value
     * @param bluetoothFormat the [BluetoothFormat] to use to deserialize the value
     * @return the value of type [T] received from the device, or `null` if the request failed
     */
    suspend fun <T> readOrNull(deserializationStrategy: DeserializationStrategy<T>, bluetoothFormat: BluetoothFormat = BluetoothFormat) =
        runCatching { read(deserializationStrategy, bluetoothFormat) }.getOrNull()

    /**
     * Requests a read of the a value of [T] of the attribute from the device and suspends until the value is received.
     * @param T the type of the data object to read
     * @param bluetoothFormat the [BluetoothFormat] to use to deserialize the value
     * @throws FailedToReadException if the read fails
     * @return the value of type [T] received from the device
     */
    suspend inline fun <reified T> read(bluetoothFormat: BluetoothFormat = BluetoothFormat): T = read(bluetoothFormat.serializer<T>(), bluetoothFormat)

    /**
     * Requests a read of the a value of [T] of the attribute from the device and suspends until the value is received.
     * @param T the type of the data object to read
     * @param bluetoothFormat the [BluetoothFormat] to use to deserialize the value
     * @return the value of type [T] received from the device, or `null` if the request failed
     */
    suspend inline fun <reified T> readOrNull(bluetoothFormat: BluetoothFormat = BluetoothFormat): T? =
        runCatching<RemoteAttribute<ReadAction, WriteAction>, T> { read(bluetoothFormat) }.getOrNull()

    /**
     * Creates and emits a [ReadAction]
     * @return the [ReadAction] created
     */
    fun startRead(): ReadAction {
        val action = createReadAction()
        if (!action.response.isCompleted) {
            addAction(action)
        }
        return action
    }

    internal abstract fun createReadAction(): ReadAction

    /**
     * Attempts to write a [ByteArray] to the attribute on the device and suspends until the write completes.
     * @param newValue the [ByteArray] to write.
     * @return the [GattResponse.WriteResponse] received from the remote device.
     */
    suspend fun write(newValue: ByteArray): GattResponse.WriteResponse = startWrite(newValue).response.await()

    /**
     * Attempts to write a value of [T] to the attribute on the device and suspends until the write completes.
     * @param T the type of the data object to write.
     * @param serializationStrategy the [SerializationStrategy] to use to serialize the value.
     * @param bluetoothFormat the [BluetoothFormat] to use to serialize the value.
     * @param newValue the [T] to write.
     * @return the [GattResponse.WriteResponse] received from the remote device.
     */
    suspend fun <T> write(serializationStrategy: SerializationStrategy<T>, bluetoothFormat: BluetoothFormat = BluetoothFormat, newValue: T): GattResponse.WriteResponse =
        write(bluetoothFormat.encodeToByteArray(serializationStrategy, newValue))

    /**
     * Attempts to write a value of [T] to the attribute on the device and suspends until the write completes.
     * @param T the type of the data object to write.
     * @param bluetoothFormat the [BluetoothFormat] to use to serialize the value.
     * @param newValue the [T] to write.
     * @return the [GattResponse.WriteResponse] received from the remote device.
     */
    suspend inline fun <reified T> write(bluetoothFormat: BluetoothFormat = BluetoothFormat, newValue: T): GattResponse.WriteResponse =
        write(bluetoothFormat.encodeToByteArray(bluetoothFormat.serializer<T>(), newValue))

    /**
     * Creates and emits a [WriteAction] to write a given [ByteArray]
     * @param newValue the [ByteArray] to write to the attribute
     * @return the [WriteAction] created
     */
    fun startWrite(newValue: ByteArray): WriteAction {
        val action = createWriteAction(newValue)
        if (!action.response.isCompleted) {
            addAction(action)
        }
        return action
    }

    /**
     * Creates and emits a [WriteAction] to write a given [T]
     * @param T the type of the data object to write.
     * @param serializationStrategy the [SerializationStrategy] to use to serialize the value.
     * @param bluetoothFormat the [BluetoothFormat] to use to serialize the value.
     * @param newValue the [T] to write to the attribute
     * @return the [WriteAction] created
     */
    fun <T> startWrite(serializationStrategy: SerializationStrategy<T>, bluetoothFormat: BluetoothFormat = BluetoothFormat, newValue: T) =
        startWrite(bluetoothFormat.encodeToByteArray(serializationStrategy, newValue))

    /**
     * Creates and emits a [WriteAction] to write a given [T]
     * @param T the type of the data object to write.
     * @param bluetoothFormat the [BluetoothFormat] to use to serialize the value.
     * @param newValue the [T] to write to the attribute
     * @return the [WriteAction] created
     */
    inline fun <reified T> startWrite(bluetoothFormat: BluetoothFormat = BluetoothFormat, newValue: T) =
        startWrite(bluetoothFormat.encodeToByteArray(bluetoothFormat.serializer(), newValue))

    internal abstract fun createWriteAction(newValue: ByteArray): WriteAction

    protected fun addAction(action: DeviceAction<*>) {
        logger.info { "Add action $action" }
        emitNewAction(DeviceConnectionManager.Event.AddAction(action))
    }
}
