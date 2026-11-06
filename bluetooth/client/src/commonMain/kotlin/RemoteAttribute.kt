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
import com.splendo.kaluga.bytes.serialization.BytesFormat
import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.info
import kotlinx.coroutines.Deferred
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy

/**
 * An [Attribute] for the Client side of a Bluetooth Low Energy connection.
 *
 * Reads and writes answer with [GattResponse.DeviceUnavailable] when the device disconnects while they are pending, and at
 * once when the attribute is [invalidated]; they never fail exceptionally.
 * @param ReadAction the [DeviceAction.Read] associated with the attribute
 * @param WriteAction the [DeviceAction.Write] associated with the attribute
 * @param emitNewAction method to call when a new [DeviceConnectionManager.Event.AddAction] event should take place
 * @param logger the [Logger] to use for logging.
 */
abstract class RemoteAttribute<ReadAction : DeviceAction.Read, WriteAction : DeviceAction.Write>(
    private val emitNewAction: (DeviceConnectionManager.Event.AddAction) -> Unit,
    private val logger: Logger,
) : Attribute {

    /**
     * Completes once this attribute can no longer be used, see [RemoteService.invalidated].
     */
    abstract val invalidated: Deferred<Unit>

    /**
     * Requests a read of the value of the attribute from the device and suspends until the value is received.
     * @return the [GattResponse.ReadResponse] received from the device, [GattResponse.DeviceUnavailable] if the device
     * disconnected before it answered or the attribute is [invalidated]
     */
    suspend fun read(): GattResponse.ReadResponse = startRead().response.await()

    /**
     * Requests a read of a value of [T] of the attribute from the device and suspends until the value is received.
     * @param T the type of the data object to read
     * @param deserializationStrategy the [DeserializationStrategy] to use to deserialize the value
     * @param format the [BytesFormat] to use to deserialize the value
     * @throws FailedToReadException if the read fails
     * @return the value of type [T] received from the device
     */
    suspend fun <T> read(deserializationStrategy: DeserializationStrategy<T>, format: BytesFormat = BytesFormat): T = when (val response = read()) {
        is GattResponse.ReadSuccess -> format.decodeFromByteArray(deserializationStrategy, response.value)
        is GattResponse.ReadError -> throw FailedToReadException(response)
    }

    /**
     * Requests a read of a value of [T] of the attribute from the device and suspends until the value is received.
     * @param T the type of the data object to read
     * @param deserializationStrategy the [DeserializationStrategy] to use to deserialize the value
     * @param format the [BytesFormat] to use to deserialize the value
     * @return the value of type [T] received from the device, or `null` if the request failed
     */
    suspend fun <T> readOrNull(deserializationStrategy: DeserializationStrategy<T>, format: BytesFormat = BytesFormat) =
        runCatching { read(deserializationStrategy, format) }.getOrNull()

    /**
     * Requests a read of a value of [T] of the attribute from the device and suspends until the value is received.
     * @param T the type of the data object to read
     * @param format the [BytesFormat] to use to deserialize the value
     * @throws FailedToReadException if the read fails
     * @return the value of type [T] received from the device
     */
    suspend inline fun <reified T> read(format: BytesFormat = BytesFormat): T = read(format.serializer<T>(), format)

    /**
     * Requests a read of a value of [T] of the attribute from the device and suspends until the value is received.
     * @param T the type of the data object to read
     * @param format the [BytesFormat] to use to deserialize the value
     * @return the value of type [T] received from the device, or `null` if the request failed
     */
    suspend inline fun <reified T> readOrNull(format: BytesFormat = BytesFormat): T? = runCatching<RemoteAttribute<ReadAction, WriteAction>, T> { read(format) }.getOrNull()

    /**
     * Creates and emits a [ReadAction]
     * @return the [ReadAction] created. Its response is already set when the attribute does not permit reads or is
     * [invalidated]
     */
    fun startRead(): ReadAction = createReadAction().also(::addActionUnlessAnswered)

    internal abstract fun createReadAction(): ReadAction

    /**
     * Attempts to write a [ByteArray] to the attribute on the device and suspends until the write completes.
     * @param newValue the [ByteArray] to write.
     * @return the [GattResponse.WriteResponse] received from the remote device, [GattResponse.DeviceUnavailable] if the
     * device disconnected before it answered or the attribute is [invalidated]
     */
    suspend fun write(newValue: ByteArray): GattResponse.WriteResponse = startWrite(newValue).response.await()

    /**
     * Attempts to write a value of [T] to the attribute on the device and suspends until the write completes.
     * @param T the type of the data object to write.
     * @param serializationStrategy the [SerializationStrategy] to use to serialize the value.
     * @param format the [BytesFormat] to use to serialize the value.
     * @param newValue the [T] to write.
     * @return the [GattResponse.WriteResponse] received from the remote device.
     */
    suspend fun <T> write(serializationStrategy: SerializationStrategy<T>, format: BytesFormat = BytesFormat, newValue: T): GattResponse.WriteResponse =
        write(format.encodeToByteArray(serializationStrategy, newValue))

    /**
     * Attempts to write a value of [T] to the attribute on the device and suspends until the write completes.
     * @param T the type of the data object to write.
     * @param format the [BytesFormat] to use to serialize the value.
     * @param newValue the [T] to write.
     * @return the [GattResponse.WriteResponse] received from the remote device.
     */
    suspend inline fun <reified T> write(format: BytesFormat = BytesFormat, newValue: T): GattResponse.WriteResponse =
        write(format.encodeToByteArray(format.serializer<T>(), newValue))

    /**
     * Creates and emits a [WriteAction] to write a given [ByteArray]
     * @param newValue the [ByteArray] to write to the attribute
     * @return the [WriteAction] created. Its response is already set when the attribute does not permit writes or is
     * [invalidated]
     */
    fun startWrite(newValue: ByteArray): WriteAction = createWriteAction(newValue).also(::addActionUnlessAnswered)

    /**
     * Creates and emits a [WriteAction] to write a given [T]
     * @param T the type of the data object to write.
     * @param serializationStrategy the [SerializationStrategy] to use to serialize the value.
     * @param format the [BytesFormat] to use to serialize the value.
     * @param newValue the [T] to write to the attribute
     * @return the [WriteAction] created
     */
    fun <T> startWrite(serializationStrategy: SerializationStrategy<T>, format: BytesFormat = BytesFormat, newValue: T) =
        startWrite(format.encodeToByteArray(serializationStrategy, newValue))

    /**
     * Creates and emits a [WriteAction] to write a given [T]
     * @param T the type of the data object to write.
     * @param format the [BytesFormat] to use to serialize the value.
     * @param newValue the [T] to write to the attribute
     * @return the [WriteAction] created
     */
    inline fun <reified T> startWrite(format: BytesFormat = BytesFormat, newValue: T) = startWrite(format.encodeToByteArray(format.serializer(), newValue))

    internal abstract fun createWriteAction(newValue: ByteArray): WriteAction

    /**
     * Emits [action] unless its response is already set by the attribute's properties, or by [invalidated]
     */
    protected fun addActionUnlessAnswered(action: DeviceAction<*>) {
        if (invalidated.isCompleted) {
            action.completeUnavailable()
        }
        if (!action.response.isCompleted) {
            addAction(action)
        }
    }

    protected fun addAction(action: DeviceAction<*>) {
        logger.info { "Add action $action" }
        emitNewAction(DeviceConnectionManager.Event.AddAction(action))
    }
}
