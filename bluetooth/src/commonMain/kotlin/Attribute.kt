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

interface Attribute {
    /**
     * The [UUID] of the attribute
     */
    val uuid: UUID
}

operator fun <T : Attribute> List<T>.get(uuid: UUID) = first { it.uuid.uuidString == uuid.uuidString }
fun <T : Attribute> List<T>.getOrNull(uuid: UUID) = find { it.uuid.uuidString == uuid.uuidString }
operator fun <T : Attribute> Flow<List<T>>.get(uuid: UUID): Flow<T> = this.map { attributes ->
    attributes[uuid]
}.distinctUntilChanged()
fun <T : Attribute> Flow<List<T>>.getOrNull(uuid: UUID): Flow<T?> = this.map { attributes ->
    attributes.getOrNull(uuid)
}.distinctUntilChanged()

class FailedToReadException(val reason: GattResponse.ReadError) : Exception()

/**
 * A bluetooth attribute conforming to the Attribute Protocol in Bluetooth Low Energy
 * @param ReadAction the [DeviceAction.Read] associated with the attribute
 * @param WriteAction the [DeviceAction.Write] associated with the attribute
 * @param emitNewAction method to call when a new [DeviceConnectionManager.Event.AddAction] event should take place
 * @param logger the [ContextualLogger] to use for logging.
 */
abstract class RemoteAttribute<ReadAction : DeviceAction.Read, WriteAction : DeviceAction.Write>(
    private val emitNewAction: (DeviceConnectionManager.Event.AddAction) -> Unit,
    private val logger: ContextualLogger,
) : Attribute {

    suspend fun read(): GattResponse.ReadResponse = startRead().response.await()
    suspend fun <T> read(deserializationStrategy: DeserializationStrategy<T>, bluetoothFormat: BluetoothFormat = BluetoothFormat): T = when (val response = read()) {
        is GattResponse.ReadSuccess -> bluetoothFormat.decodeFromByteArray(deserializationStrategy, response.value)
        is GattResponse.ReadError -> throw FailedToReadException(response)
    }

    suspend fun <T> readOrNull(deserializationStrategy: DeserializationStrategy<T>, bluetoothFormat: BluetoothFormat = BluetoothFormat) =
        runCatching { read(deserializationStrategy, bluetoothFormat) }.getOrNull()

    suspend inline fun <reified T> read(bluetoothFormat: BluetoothFormat = BluetoothFormat): T = read(bluetoothFormat.serializer<T>(), bluetoothFormat)
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

    suspend fun write(newValue: ByteArray): GattResponse.WriteResponse = startWrite(newValue).response.await()
    suspend fun <T> write(serializationStrategy: SerializationStrategy<T>, bluetoothFormat: BluetoothFormat = BluetoothFormat, newValue: T): GattResponse.WriteResponse =
        write(bluetoothFormat.encodeToByteArray(serializationStrategy, newValue))
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

    internal abstract fun createWriteAction(newValue: ByteArray): WriteAction

    protected fun addAction(action: DeviceAction<*>) {
        logger.info { "Add action $action" }
        emitNewAction(DeviceConnectionManager.Event.AddAction(action))
    }
}
