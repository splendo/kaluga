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

package com.splendo.kaluga.bluetooth.device

import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.RemoteAttribute
import com.splendo.kaluga.bluetooth.RemoteCharacteristic
import com.splendo.kaluga.bluetooth.RemoteDescriptor
import com.splendo.kaluga.bluetooth.RemoteService
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.characteristics
import com.splendo.kaluga.bluetooth.descriptors
import com.splendo.kaluga.bluetooth.device.RemoteAttributeBinding.UnitReadBuilder
import com.splendo.kaluga.bluetooth.device.RemoteAttributeBinding.WriteBuilder
import com.splendo.kaluga.bluetooth.device.RemoteCharacteristicBinding.ObserveBuilder
import com.splendo.kaluga.bluetooth.discoveredServices
import com.splendo.kaluga.bluetooth.filterDiscovering
import com.splendo.kaluga.bluetooth.get
import com.splendo.kaluga.bluetooth.getOrNull
import com.splendo.kaluga.bluetooth.includedServices
import com.splendo.kaluga.bluetooth.serialization.BluetoothFormat
import com.splendo.kaluga.bluetooth.startDiscovering
import com.splendo.kaluga.bluetooth.uuidFrom
import com.splendo.kaluga.bluetooth.value
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.serializer
import kotlin.jvm.JvmName

/**
 * Builder for setting up binding to an object [T] so that it may be notified when [RemoteService] become available / unavailable.
 * @property T the type of the object to bind to
 * @property Transformation the type of Transformation that should be applied to the object when availability changes.
 */
sealed interface RequiresServicesDiscoveredBinding<T, Transformation> {

    /**
     * A [RequiresServicesDiscoveredBinding] that does not mutate the object when availability changes.
     * @property T the type of the object to bind to
     */
    interface NonMutating<T> : RequiresServicesDiscoveredBinding<T, Unit> {
        fun mutate(builder: Mutating<T>.() -> Unit)
    }

    /**
     * A [RequiresServicesDiscoveredBinding] that mutates the object when availability changes.
     * @property T the type of the object to bind to and mutate when availability changes.
     */
    interface Mutating<T> : RequiresServicesDiscoveredBinding<T, T>

    /**
     * Sets up a [Transformation] action when [RemoteService] become available.
     * @param action the [Transformation] to apply when [RemoteService] become available.
     */
    fun onAvailable(action: T.() -> Transformation)

    /**
     * Sets up a [Transformation] action when [RemoteService] become unavailable.
     * @param action the [Transformation] to apply when [RemoteService] become unavailable.
     */
    fun onUnavailable(action: T.() -> Transformation)
}

/**
 * Builder for setting up binding to an object [T] so that it may be changed by a [RemoteAttribute].
 * @property T the type of the object to bind to
 */
@Suppress("INAPPLICABLE_JVM_NAME")
interface RemoteAttributeBinding<T> {

    /**
     * Builder for setting up binding to an object [T] so that it may be changed when reading from a [RemoteAttribute] after being triggered by a [Trigger].
     * @property T the type of the object to bind to
     * @property Trigger the type of Trigger will cause the [RemoteAttribute] to be read.
     * @property Response the type of Response that will be returned when the [RemoteAttribute] is read.
     * @property Transformation the type of Transformation that should be applied to the object when the [RemoteAttribute] is read.
     */
    sealed interface ReadBuilder<T, Trigger, Response, Transformation> {

        /**
         * A [ReadBuilder] that does not mutate the object when reading from a [RemoteAttribute] after being triggered by a [Trigger].
         * @property T the type of the object to bind to
         * @property Trigger the type of Trigger will cause the [RemoteAttribute] to be read.
         * @property Response the type of Response that will be returned when the [RemoteAttribute] is read.
         */
        interface NonMutating<T, Trigger, Response> : ReadBuilder<T, Trigger, Response, Unit> {

            /**
             * Creates a [Mutating] [ReadBuilder] that mutates the object when reading from a [RemoteAttribute] after being triggered by a [Trigger].
             * @param builder the builder to apply to the [Mutating] [ReadBuilder]
             */
            fun mutate(builder: Mutating<T, Trigger, Response>.() -> Unit)
        }

        /**
         * A [ReadBuilder] that mutates the object when reading from a [RemoteAttribute] after being triggered by a [Trigger].
         * @property T the type of the object to bind to and mutate when reading from the [RemoteAttribute].
         * @property Trigger the type of Trigger will cause the [RemoteAttribute] to be read.
         * @property Response the type of Response that will be returned when the [RemoteAttribute] is read.
         */
        interface Mutating<T, Trigger, Response> : ReadBuilder<T, Trigger, Response, T>

        /**
         * Sets up a [Transformation] when a [Response] is read from a [RemoteAttribute].
         * @param action the [Transformation] to apply when a [Response] is received. Contains the [Trigger] that caused the [RemoteAttribute] to be read and the [Response] that was read.
         */
        fun onRead(action: T.(Response, Trigger) -> Transformation)

        /**
         * Sets up a [Transformation] when failing to read from a [RemoteAttribute].
         * @param action the [Transformation] to apply when a [Response] is received. Contains the [Trigger] that caused the [RemoteAttribute] to be read and the [GattResponse.ReadError] that was received.
         */
        fun onFailedToRead(action: T.(Trigger, GattResponse.ReadError) -> Transformation)
    }

    /**
     * Builder for setting up binding to an object [T] so that it may be changed when reading from a [RemoteAttribute] after being triggered by a [Unit].
     * @property T the type of the object to bind to
     * @property Response the type of Response that will be returned when the [RemoteAttribute] is read.
     * @property Transformation the type of Transformation that should be applied to the object when the [RemoteAttribute] is read.
     */
    sealed class UnitReadBuilder<T, Response, Transformation> protected constructor(private val builder: ReadBuilder<T, Unit, Response, Transformation>) {

        /**
         * A [ReadBuilder] that does not mutate the object when reading from a [RemoteAttribute] after being triggered by a [Unit].
         * @property T the type of the object to bind to
         * @property Response the type of Response that will be returned when the [RemoteAttribute] is read.
         */
        class NonMutating<T, Response> internal constructor(private val builder: ReadBuilder.NonMutating<T, Unit, Response>) : UnitReadBuilder<T, Response, Unit>(builder) {

            /**
             * Creates a [Mutating] [UnitReadBuilder] that mutates the object when reading from a [RemoteAttribute] after being triggered by a [Unit].
             * @param builder the builder to apply to the [Mutating] [UnitReadBuilder]
             */
            fun mutate(builder: Mutating<T, Response>.() -> Unit) = this.builder.mutate {
                Mutating(this).apply(builder)
            }
        }

        /**
         * A [ReadBuilder] that mutates the object when reading from a [RemoteAttribute] after being triggered by a [Unit].
         * @property T the type of the object to bind to and mutate when reading from the [RemoteAttribute].
         * @property Response the type of Response that will be returned when the [RemoteAttribute] is read.
         */
        class Mutating<T, Response> internal constructor(private val builder: ReadBuilder.Mutating<T, Unit, Response>) : UnitReadBuilder<T, Response, T>(builder)

        /**
         * Sets up a [Transformation] when a [Response] is read from a [RemoteAttribute].
         * @param action the [Transformation] to apply when a [Response] is received. Contains the [Response] that was read.
         */
        fun onRead(action: T.(Response) -> Transformation) = builder.onRead { response, _ -> action(response) }

        /**
         * Sets up a [Transformation] when failing to read from a [RemoteAttribute].
         * @param action the [Transformation] to apply when a [Response] is received. Contains the [GattResponse.ReadError] that was received.
         */
        fun onFailedToRead(action: T.(GattResponse.ReadError) -> Transformation) = builder.onFailedToRead { _, error -> action(error) }
    }

    /**
     * Builder for setting up binding to an object [T] so that it may be changed when writing [Data] to a [RemoteAttribute].
     * @property T the type of the object to bind to
     * @property Data the type of data to write to the [RemoteAttribute].
     * @property Transformation the type of Transformation that should be applied to the object when the [RemoteAttribute] is written to.
     */
    sealed interface WriteBuilder<T, Data, Transformation> {

        /**
         * A [WriteBuilder] that does not mutate the object when writing [Data] to a [RemoteAttribute].
         * @property T the type of the object to bind to
         * @property Data the type of data to write to the [RemoteAttribute].
         */
        interface NonMutating<T, Data> : WriteBuilder<T, Data, Unit> {

            /**
             * Creates a [Mutating] [WriteBuilder] that mutates the object when writing [Data] to a [RemoteAttribute].
             * @param builder the builder to apply to the [Mutating] [WriteBuilder]
             */
            fun mutate(builder: Mutating<T, Data>.() -> Unit)
        }

        /**
         * A [WriteBuilder] that mutates the object when writing [Data] to a [RemoteAttribute].
         * @property T the type of the object to bind to
         * @property Data the type of data to write to the [RemoteAttribute].
         */
        interface Mutating<T, Data> : WriteBuilder<T, Data, T>

        /**
         * Sets up a [Transformation] when a [Data] is written to a [RemoteAttribute].
         * @param action the [Transformation] to apply when a [Data] has been written. Contains the [Data] that was written.
         */
        fun onWrite(action: T.(Data) -> Transformation)

        /**
         * Sets up a [Transformation] when failing to write [Data] to a [RemoteAttribute].
         * @param action the [Transformation] to apply when a [Data] has been written. Contains the [Data] that was attempted to be written and the [GattResponse.WriteError] that was received.
         */
        fun onFailedToWrite(action: T.(Data, GattResponse.WriteError) -> Transformation)
    }

    /**
     * Consumes a [Trigger] from a [Channel] to cause the [RemoteAttribute] to be read.
     * @param Trigger the type of Trigger that will cause the [RemoteAttribute] to be read.
     * @param Response the type of Response that will be returned when the [RemoteAttribute] is read.
     * @param asValue maps the [ByteArray] received from the [GattResponse.ReadSuccess] to the [Response] that will be returned.
     * @param builder sets up the response to a triggered read using a [ReadBuilder.NonMutating]
     */
    fun <Trigger, Response> Channel<Trigger>.consumeToTriggerRead(asValue: ByteArray.() -> Response, builder: ReadBuilder.NonMutating<T, Trigger, Response>.() -> Unit)

    /**
     * Consumes a [Unit] from a [Channel] to cause the [RemoteAttribute] to be read.
     * @param Response the type of Response that will be returned when the [RemoteAttribute] is read.
     * @param asValue maps the [ByteArray] received from the [GattResponse.ReadSuccess] to the [Response] that will be returned.
     * @param unitBuilder sets up the response to a triggered read using a [UnitReadBuilder.NonMutating]
     */
    @JvmName("consumeUnitToTriggerRead")
    fun <Response> Channel<Unit>.consumeToTriggerRead(asValue: ByteArray.() -> Response, unitBuilder: UnitReadBuilder.NonMutating<T, Response>.() -> Unit) =
        consumeToTriggerRead(asValue, builder = {
            UnitReadBuilder.NonMutating(this).apply(unitBuilder)
        })

    /**
     * Consumes a [Unit] from a [Channel] to cause the [RemoteAttribute] to be read.
     * @param Response the type of Response that will be returned when the [RemoteAttribute] is read.
     * @param deserializationStrategy the [DeserializationStrategy] to map the [ByteArray] received from the [GattResponse.ReadSuccess] to the [Response] that will be returned.
     * @param bluetoothFormat the [BluetoothFormat] to use for deserialization.
     * @param unitBuilder sets up the response to a triggered read using a [UnitReadBuilder.NonMutating]
     */
    fun <Response> Channel<Unit>.consumeToTriggerRead(
        deserializationStrategy: DeserializationStrategy<Response>,
        bluetoothFormat: BluetoothFormat = BluetoothFormat,
        unitBuilder: UnitReadBuilder.NonMutating<T, Response>.() -> Unit,
    ) = consumeToTriggerRead({
        bluetoothFormat.decodeFromByteArray(deserializationStrategy, this)
    }, unitBuilder)

    /**
     * Consumes a [Trigger] from a [Channel] to cause the [RemoteAttribute] to be read.
     * @param Trigger the type of Trigger that will cause the [RemoteAttribute] to be read.
     * @param builder sets up the response to a triggered read using a [ReadBuilder.NonMutating]
     */
    fun <Trigger> Channel<Trigger>.consumeToTriggerRead(builder: ReadBuilder.NonMutating<T, Trigger, ByteArray>.() -> Unit) =
        consumeToTriggerRead(asValue = { this }, builder = builder)

    /**
     * Consumes a [Unit] from a [Channel] to cause the [RemoteAttribute] to be read.
     * @param Response the type of Response that will be returned when the [RemoteAttribute] is read.
     * @param unitBuilder sets up the response to a triggered read using a [UnitReadBuilder.NonMutating]
     */
    @JvmName("consumeUnitToTriggerRead")
    fun <Response> Channel<Unit>.consumeToTriggerRead(unitBuilder: UnitReadBuilder.NonMutating<T, ByteArray>.() -> Unit) = consumeToTriggerRead(builder = {
        UnitReadBuilder.NonMutating(this).apply(unitBuilder)
    })

    /**
     * Collects a [Trigger] from a [Flow] to cause the [RemoteAttribute] to be read.
     * @param Trigger the type of Trigger that will cause the [RemoteAttribute] to be read.
     * @param Response the type of Response that will be returned when the [RemoteAttribute] is read.
     * @param asValue maps the [ByteArray] received from the [GattResponse.ReadSuccess] to the [Response] that will be returned.
     * @param builder sets up the response to a triggered read using a [ReadBuilder.NonMutating]
     */
    fun <Trigger, Response> Flow<Trigger>.collectToTriggerRead(asValue: ByteArray.() -> Response, builder: ReadBuilder.NonMutating<T, Trigger, Response>.() -> Unit)

    /**
     * Collects a [Unit] from a [Flow] to cause the [RemoteAttribute] to be read.
     * @param Response the type of Response that will be returned when the [RemoteAttribute] is read.
     * @param asValue maps the [ByteArray] received from the [GattResponse.ReadSuccess] to the [Response] that will be returned.
     * @param unitBuilder sets up the response to a triggered read using a [UnitReadBuilder.NonMutating]
     */
    @JvmName("collectUnitToTriggerRead")
    fun <Response> Flow<Unit>.collectToTriggerRead(asValue: ByteArray.() -> Response, unitBuilder: UnitReadBuilder.NonMutating<T, Response>.() -> Unit) =
        collectToTriggerRead(asValue, builder = {
            UnitReadBuilder.NonMutating(this).apply(unitBuilder)
        })

    /**
     * Collects a [Unit] from a [Flow] to cause the [RemoteAttribute] to be read.
     * @param Response the type of Response that will be returned when the [RemoteAttribute] is read.
     * @param deserializationStrategy the [DeserializationStrategy] to map the [ByteArray] received from the [GattResponse.ReadSuccess] to the [Response] that will be returned.
     * @param bluetoothFormat the [BluetoothFormat] to use for deserialization.
     * @param unitBuilder sets up the response to a triggered read using a [UnitReadBuilder.NonMutating]
     */
    fun <Response> Flow<Unit>.collectToTriggerRead(
        deserializationStrategy: DeserializationStrategy<Response>,
        bluetoothFormat: BluetoothFormat = BluetoothFormat,
        unitBuilder: UnitReadBuilder.NonMutating<T, Response>.() -> Unit,
    ) = collectToTriggerRead({
        bluetoothFormat.decodeFromByteArray(deserializationStrategy, this)
    }, unitBuilder)

    /**
     * Collects a [Trigger] from a [Flow] to cause the [RemoteAttribute] to be read.
     * @param Trigger the type of Trigger that will cause the [RemoteAttribute] to be read.
     * @param builder sets up the response to a triggered read using a [ReadBuilder.NonMutating]
     */
    fun <Trigger> Flow<Trigger>.collectToTriggerRead(builder: ReadBuilder.NonMutating<T, Trigger, ByteArray>.() -> Unit) =
        collectToTriggerRead(asValue = { this }, builder = builder)

    /**
     * Collects a [Unit] from a [Flow] to cause the [RemoteAttribute] to be read.
     * @param unitBuilder sets up the response to a triggered read using a [UnitReadBuilder.NonMutating]
     */
    @JvmName("collectUnitToTriggerRead")
    fun <Response> Flow<Unit>.collectToTriggerRead(unitBuilder: UnitReadBuilder.NonMutating<T, ByteArray>.() -> Unit) = collectToTriggerRead(builder = {
        UnitReadBuilder.NonMutating(this).apply(unitBuilder)
    })

    /**
     * Consumes [Data] from a [Channel] to cause the [RemoteAttribute] to be written to.
     * @param Data the type of data to write to the [RemoteAttribute].
     * @param asByte maps the [Data] to a [ByteArray] to write.
     * @param builder sets up the response to a triggered write using a [WriteBuilder.NonMutating]
     */
    fun <Data> Channel<Data>.consumeToTriggerWrite(asByte: Data.() -> ByteArray, builder: WriteBuilder.NonMutating<T, Data>.() -> Unit)

    /**
     * Consumes [Trigger] from a [Channel] to have [Data] written to the [RemoteAttribute].
     * @param Trigger the type of Trigger that will cause the [RemoteAttribute] to be written to.
     * @param Data the type of data to write to the [RemoteAttribute].
     * @param serializationStrategy the [SerializationStrategy] to map the [Data] to a [ByteArray] to write.
     * @param bluetoothFormat the [BluetoothFormat] to use for serialization.
     * @param mapper maps the [Trigger] to the [Data] to write.
     * @param builder sets up the response to a triggered write using a [WriteBuilder.NonMutating]
     */
    fun <Trigger, Data> Channel<Trigger>.consumeToTriggerWrite(
        serializationStrategy: SerializationStrategy<Data>,
        bluetoothFormat: BluetoothFormat = BluetoothFormat,
        mapper: Trigger.() -> Data,
        builder: WriteBuilder.NonMutating<T, Trigger>.() -> Unit,
    ) = consumeToTriggerWrite({ bluetoothFormat.encodeToByteArray(serializationStrategy, mapper()) }, builder)

    /**
     * Consumes [Data] from a [Channel] to write it to the [RemoteAttribute].
     * @param Data the type of data to write to the [RemoteAttribute].
     * @param serializationStrategy the [SerializationStrategy] to map the [Data] to a [ByteArray] to write.
     * @param bluetoothFormat the [BluetoothFormat] to use for serialization.
     * @param builder sets up the response to a triggered write using a [WriteBuilder.NonMutating]
     */
    fun <Data> Channel<Data>.consumeToTriggerWrite(
        serializationStrategy: SerializationStrategy<Data>,
        bluetoothFormat: BluetoothFormat = BluetoothFormat,
        builder: WriteBuilder.NonMutating<T, Data>.() -> Unit,
    ) = consumeToTriggerWrite(serializationStrategy, bluetoothFormat, { this }, builder)

    /**
     * Consumes [ByteArray] from a [Channel] to write it to the [RemoteAttribute].
     * @param builder sets up the response to a triggered write using a [WriteBuilder.NonMutating]
     */
    fun Channel<ByteArray>.consumeToTriggerWrite(builder: WriteBuilder.NonMutating<T, ByteArray>.() -> Unit) = consumeToTriggerWrite({ this }, builder)

    /**
     * Collects [Data] from a [Flow] to cause the [RemoteAttribute] to be written to.
     * @param Data the type of data to write to the [RemoteAttribute].
     * @param asByte maps the [Data] to a [ByteArray] to write.
     * @param builder sets up the response to a triggered write using a [WriteBuilder.NonMutating]
     */
    fun <Data> Flow<Data>.collectToTriggerWrite(asByte: Data.() -> ByteArray, builder: WriteBuilder.NonMutating<T, Data>.() -> Unit)

    /**
     * Collects [Trigger] from a [Flow] to have [Data] written to the [RemoteAttribute].
     * @param Trigger the type of Trigger that will cause the [RemoteAttribute] to be written to.
     * @param Data the type of data to write to the [RemoteAttribute].
     * @param serializationStrategy the [SerializationStrategy] to map the [Data] to a [ByteArray] to write.
     * @param bluetoothFormat the [BluetoothFormat] to use for serialization.
     * @param mapper maps the [Trigger] to the [Data] to write.
     * @param builder sets up the response to a triggered write using a [WriteBuilder.NonMutating]
     */
    fun <Trigger, Data> Flow<Trigger>.collectToTriggerWrite(
        serializationStrategy: SerializationStrategy<Data>,
        bluetoothFormat: BluetoothFormat = BluetoothFormat,
        mapper: Trigger.() -> Data,
        builder: WriteBuilder.NonMutating<T, Trigger>.() -> Unit,
    ) = collectToTriggerWrite({ bluetoothFormat.encodeToByteArray(serializationStrategy, mapper()) }, builder)

    /**
     * Collects [Data] from a [Flow] to write it to the [RemoteAttribute].
     * @param Data the type of data to write to the [RemoteAttribute].
     * @param serializationStrategy the [SerializationStrategy] to map the [Data] to a [ByteArray] to write.
     * @param bluetoothFormat the [BluetoothFormat] to use for serialization.
     * @param builder sets up the response to a triggered write using a [WriteBuilder.NonMutating]
     */
    fun <Data> Flow<Data>.collectToTriggerWrite(
        serializationStrategy: SerializationStrategy<Data>,
        bluetoothFormat: BluetoothFormat = BluetoothFormat,
        builder: WriteBuilder.NonMutating<T, Data>.() -> Unit,
    ) = collectToTriggerWrite(serializationStrategy, bluetoothFormat, { this }, builder)

    /**
     * Collects [ByteArray] from a [Flow] to write it to the [RemoteAttribute].
     * @param builder sets up the response to a triggered write using a [WriteBuilder.NonMutating]
     */
    fun Flow<ByteArray>.collectToTriggerWrite(builder: WriteBuilder.NonMutating<T, ByteArray>.() -> Unit) = collectToTriggerWrite({ this }, builder)
}

/**
 * Consumes a [Unit] from a [Channel] to cause the [RemoteAttribute] to be read.
 * @param Response the type of Response that will be returned when the [RemoteAttribute] is read.
 * @param T  the type of the object to bind to
 * @param remoteAttributeBinding the [RemoteAttributeBinding] to bind this [Channel] to
 * @param bluetoothFormat the [BluetoothFormat] to use for deserialization.
 * @param unitBuilder sets up the response to a triggered read using a [UnitReadBuilder.NonMutating]
 */
inline fun <reified Response, T> Channel<Unit>.consumeToTriggerRead(
    remoteAttributeBinding: RemoteAttributeBinding<T>,
    bluetoothFormat: BluetoothFormat = BluetoothFormat,
    noinline unitBuilder: UnitReadBuilder.NonMutating<T, Response>.() -> Unit,
) = with(remoteAttributeBinding) {
    consumeToTriggerRead(bluetoothFormat.serializersModule.serializer<Response>(), bluetoothFormat, unitBuilder)
}

/**
 * Collects a [Unit] from a [Flow] to cause the [RemoteAttribute] to be read.
 * @param Response the type of Response that will be returned when the [RemoteAttribute] is read.
 * @param T  the type of the object to bind to
 * @param remoteAttributeBinding the [RemoteAttributeBinding] to bind this [Channel] to
 * @param bluetoothFormat the [BluetoothFormat] to use for deserialization.
 * @param unitBuilder sets up the response to a triggered read using a [UnitReadBuilder.NonMutating]
 */
inline fun <reified Response, T> Flow<Unit>.collectToTriggerRead(
    remoteAttributeBinding: RemoteAttributeBinding<T>,
    bluetoothFormat: BluetoothFormat = BluetoothFormat,
    noinline unitBuilder: UnitReadBuilder.NonMutating<T, Response>.() -> Unit,
) = with(remoteAttributeBinding) {
    collectToTriggerRead(bluetoothFormat.serializersModule.serializer<Response>(), bluetoothFormat, unitBuilder)
}

/**
 * Consumes [Trigger] from a [Channel] to have [Trigger] written to the [RemoteAttribute].
 * @param Trigger the type of Trigger that will cause the [RemoteAttribute] to be written to.
 * @param Data the type of data to write to the [RemoteAttribute].
 * @param T  the type of the object to bind to
 * @param remoteAttributeBinding the [RemoteAttributeBinding] to bind this [Channel] to
 * @param bluetoothFormat the [BluetoothFormat] to use for serialization.
 * @param mapper maps the [Trigger] to the [Data] to write.
 * @param builder sets up the response to a triggered write using a [WriteBuilder.NonMutating]
 */
inline fun <Trigger, reified Data, T> Channel<Trigger>.consumeToTriggerWrite(
    remoteAttributeBinding: RemoteAttributeBinding<T>,
    bluetoothFormat: BluetoothFormat = BluetoothFormat,
    noinline mapper: Trigger.() -> Data,
    noinline builder: WriteBuilder.NonMutating<T, Trigger>.() -> Unit,
) = with(remoteAttributeBinding) {
    consumeToTriggerWrite(bluetoothFormat.serializersModule.serializer<Data>(), bluetoothFormat, mapper, builder)
}

/**
 * Consumes [Data] from a [Channel] to write it to the [RemoteAttribute].
 * @param Data the type of data to write to the [RemoteAttribute].
 * @param T  the type of the object to bind to
 * @param remoteAttributeBinding the [RemoteAttributeBinding] to bind this [Channel] to
 * @param bluetoothFormat the [BluetoothFormat] to use for serialization.
 * @param builder sets up the response to a triggered write using a [WriteBuilder.NonMutating]
 */
inline fun <reified Data, T> Channel<Data>.consumeToTriggerWrite(
    remoteAttributeBinding: RemoteAttributeBinding<T>,
    bluetoothFormat: BluetoothFormat = BluetoothFormat,
    noinline builder: WriteBuilder.NonMutating<T, Data>.() -> Unit,
) = with(remoteAttributeBinding) {
    consumeToTriggerWrite(bluetoothFormat.serializersModule.serializer<Data>(), bluetoothFormat, builder)
}

/**
 * Collects [Trigger] from a [Flow] to have [Trigger] written to the [RemoteAttribute].
 * @param Trigger the type of Trigger that will cause the [RemoteAttribute] to be written to.
 * @param Data the type of data to write to the [RemoteAttribute].
 * @param T  the type of the object to bind to
 * @param remoteAttributeBinding the [RemoteAttributeBinding] to bind this [Channel] to
 * @param bluetoothFormat the [BluetoothFormat] to use for serialization.
 * @param mapper maps the [Trigger] to the [Data] to write.
 * @param builder sets up the response to a triggered write using a [WriteBuilder.NonMutating]
 */
inline fun <Trigger, reified Data, T> Flow<Trigger>.collectToTriggerWrite(
    remoteAttributeBinding: RemoteAttributeBinding<T>,
    bluetoothFormat: BluetoothFormat = BluetoothFormat,
    noinline mapper: Trigger.() -> Data,
    noinline builder: WriteBuilder.NonMutating<T, Trigger>.() -> Unit,
) = with(remoteAttributeBinding) {
    collectToTriggerWrite(bluetoothFormat.serializersModule.serializer<Data>(), bluetoothFormat, mapper, builder)
}

/**
 * Collects [Data] from a [Flow] to write it to the [RemoteAttribute].
 * @param Data the type of data to write to the [RemoteAttribute].
 * @param T  the type of the object to bind to
 * @param remoteAttributeBinding the [RemoteAttributeBinding] to bind this [Channel] to
 * @param bluetoothFormat the [BluetoothFormat] to use for serialization.
 * @param builder sets up the response to a triggered write using a [WriteBuilder.NonMutating]
 */
inline fun <reified Data, T> Flow<Data>.collectToTriggerWrite(
    remoteAttributeBinding: RemoteAttributeBinding<T>,
    bluetoothFormat: BluetoothFormat = BluetoothFormat,
    noinline builder: WriteBuilder.NonMutating<T, Data>.() -> Unit,
) = with(remoteAttributeBinding) {
    collectToTriggerWrite(bluetoothFormat.serializersModule.serializer<Data>(), bluetoothFormat, builder)
}

/**
 * Builder for setting up binding to an object [T] so that it may be changed by a [ConnectableDevice].
 * @property T the type of the object to bind to
 */
sealed interface ConnectedDeviceBinding<T> {

    /**
     * A [ConnectedDeviceBinding] that assumes the [ConnectableDevice] being bound to is actually connected and has discovered its [RemoteService]
     * @property T the type of the object to bind to
     */
    interface EnsuresAvailable<T> : ConnectedDeviceBinding<T> {

        /**
         * Binds the object to the [RemoteService] at a given [UUID].
         * @param uuid the [UUID] of the [RemoteService] to bind to
         * @param binding the [RemoteServiceBinding.EnsuresAvailable] set up binding to the [RemoteService]
         * @throws NoSuchElementException if the [RemoteService] is not found
         */
        fun service(uuid: UUID, binding: RemoteServiceBinding.EnsuresAvailable<T>.() -> Unit)

        /**
         * Binds the object to the [RemoteService] at a given [UUID].
         * @param uuidString the string of the [UUID] of the [RemoteService] to bind to
         * @param binding the [RemoteServiceBinding.EnsuresAvailable] set up binding to the [RemoteService]
         * @throws NoSuchElementException if the [RemoteService] is not found
         * @throws com.splendo.kaluga.bluetooth.UUIDException if the [UUID] is not valid
         */
        fun service(uuidString: String, binding: RemoteServiceBinding.EnsuresAvailable<T>.() -> Unit) {
            service(uuidFrom(uuidString), binding)
        }

        /**
         * Binds an object of type [R] to the [RemoteService] at a given [UUID] and updates the object whenever it changes.
         * @param R the type of the object to bind to
         * @param uuid the [UUID] of the [RemoteService] to bind to
         * @param update the function that updates the object whenever the bound object changes
         * @param binding the [RemoteServiceBinding.EnsuresAvailable] set up binding to the [RemoteService]
         * @throws NoSuchElementException if the [RemoteService] is not found
         */
        fun <R> R.bindService(uuid: UUID, update: T.(R) -> T = { this }, binding: RemoteServiceBinding.EnsuresAvailable<R>.() -> Unit)

        /**
         * Binds an object of type [R] to the [RemoteService] at a given [UUID] and updates the object whenever it changes.
         * @param R the type of the object to bind to
         * @param uuidString the string of the [UUID] of the [RemoteService] to bind to
         * @param update the function that updates the object whenever the bound object changes
         * @param binding the [RemoteServiceBinding.EnsuresAvailable] set up binding to the [RemoteService]
         * @throws NoSuchElementException if the [RemoteService] is not found
         * @throws com.splendo.kaluga.bluetooth.UUIDException if the [UUID] is not valid
         */
        fun <R> R.bindService(uuidString: String, update: T.(R) -> T = { this }, binding: RemoteServiceBinding.EnsuresAvailable<R>.() -> Unit) {
            bindService(uuidFrom(uuidString), update, binding)
        }
    }

    /**
     * A [ConnectedDeviceBinding] and [RequiresServicesDiscoveredBinding.NonMutating] that will only bind whenever a service is discovered.
     * @property T the type of the object to bind to
     */
    interface RequiresServicesDiscovered<T> :
        ConnectedDeviceBinding<T>,
        RequiresServicesDiscoveredBinding.NonMutating<T> {

        /**
         * Binds the object to the [RemoteService] at a given [UUID] whenever it is available.
         * @param uuid the [UUID] of the [RemoteService] to bind to
         * @param binding the [RemoteServiceBinding.RequiresServicesDiscovered] set up binding to the [RemoteService]
         */
        fun service(uuid: UUID, binding: RemoteServiceBinding.RequiresServicesDiscovered<T>.() -> Unit)

        /**
         * Binds the object to the [RemoteService] at a given [UUID] whenever it is available.
         * @param uuidString the string of the [UUID] of the [RemoteService] to bind to
         * @param binding the [RemoteServiceBinding.RequiresServicesDiscovered] set up binding to the [RemoteService]
         * @throws com.splendo.kaluga.bluetooth.UUIDException if the [UUID] is not valid
         */
        fun service(uuidString: String, binding: RemoteServiceBinding.RequiresServicesDiscovered<T>.() -> Unit) {
            service(uuidFrom(uuidString), binding)
        }

        /**
         * Binds an object of type [R] to the [RemoteService] at a given [UUID] whenever it is available and updates the object whenever it changes.
         * @param R the type of the object to bind to
         * @param uuid the [UUID] of the [RemoteService] to bind to
         * @param update the function that updates the object whenever the bound object changes
         * @param binding the [RemoteServiceBinding.RequiresServicesDiscovered] set up binding to the [RemoteService]
         * @throws NoSuchElementException if the [RemoteService] is not found
         */
        fun <R> R.bindService(uuid: UUID, update: T.(R) -> T = { this }, binding: RemoteServiceBinding.RequiresServicesDiscovered<R>.() -> Unit)

        /**
         * Binds an object of type [R] to the [RemoteService] at a given [UUID] whenever it is available and updates the object whenever it changes.
         * @param R the type of the object to bind to
         * @param uuidString the string of the [UUID] of the [RemoteService] to bind to
         * @param update the function that updates the object whenever the bound object changes
         * @param binding the [RemoteServiceBinding.RequiresServicesDiscovered] set up binding to the [RemoteService]
         * @throws NoSuchElementException if the [RemoteService] is not found
         * @throws com.splendo.kaluga.bluetooth.UUIDException if the [UUID] is not valid
         */
        fun <R> R.bindService(uuidString: String, update: T.(R) -> T = { this }, binding: RemoteServiceBinding.RequiresServicesDiscovered<R>.() -> Unit) {
            bindService(uuidFrom(uuidString), update, binding)
        }
    }
}

/**
 * Builder for setting up binding to an object [T] so that it may be changed by a [RemoteService].
 * @property T the type of the object to bind to
 */
interface RemoteServiceBinding<T> {

    /**
     * A [RemoteServiceBinding] that assumes the [RemoteService] being bound to has been discovered.
     * @property T the type of the object to bind to
     */
    interface EnsuresAvailable<T> : RemoteServiceBinding<T> {

        /**
         * Binds the object to the included [RemoteService] at a given [UUID].
         * @param uuid the [UUID] of the [RemoteService] to bind to
         * @param binding the [RemoteServiceBinding.EnsuresAvailable] set up binding to the [RemoteService]
         * @throws NoSuchElementException if the [RemoteService] is not found
         */
        fun service(uuid: UUID, binding: EnsuresAvailable<T>.() -> Unit)

        /**
         * Binds the object to the included [RemoteService] at a given [UUID].
         * @param uuidString the string of the [UUID] of the [RemoteService] to bind to
         * @param binding the [RemoteServiceBinding.EnsuresAvailable] set up binding to the [RemoteService]
         * @throws NoSuchElementException if the [RemoteService] is not found
         * @throws com.splendo.kaluga.bluetooth.UUIDException if the [UUID] is not valid
         */
        fun service(uuidString: String, binding: EnsuresAvailable<T>.() -> Unit) {
            service(uuidFrom(uuidString), binding)
        }

        /**
         * Binds an object of type [R] to the included [RemoteService] at a given [UUID] and updates the object whenever it changes.
         * @param R the type of the object to bind to
         * @param uuid the [UUID] of the [RemoteService] to bind to
         * @param update the function that updates the object whenever the bound object changes
         * @param binding the [RemoteServiceBinding.EnsuresAvailable] set up binding to the [RemoteService]
         * @throws NoSuchElementException if the [RemoteService] is not found
         */
        fun <R> R.bindService(uuid: UUID, update: T.(R) -> T = { this }, binding: EnsuresAvailable<R>.() -> Unit)

        /**
         * Binds an object of type [R] to the included [RemoteService] at a given [UUID] and updates the object whenever it changes.
         * @param R the type of the object to bind to
         * @param uuidString the string of the [UUID] of the [RemoteService] to bind to
         * @param update the function that updates the object whenever the bound object changes
         * @param binding the [RemoteServiceBinding.EnsuresAvailable] set up binding to the [RemoteService]
         * @throws NoSuchElementException if the [RemoteService] is not found
         * @throws com.splendo.kaluga.bluetooth.UUIDException if the [UUID] is not valid
         */
        fun <R> R.bindService(uuidString: String, update: T.(R) -> T = { this }, binding: EnsuresAvailable<R>.() -> Unit) {
            bindService(uuidFrom(uuidString), update, binding)
        }

        /**
         * Binds the object to the [RemoteCharacteristic] at a given [UUID].
         * @param uuid the [UUID] of the [RemoteCharacteristic] to bind to
         * @param binding the [RemoteCharacteristicBinding.EnsuresServicesAvailable] set up binding to the [RemoteCharacteristic]
         * @throws NoSuchElementException if the [RemoteCharacteristic] is not found
         */
        fun characteristic(uuid: UUID, binding: RemoteCharacteristicBinding.EnsuresServicesAvailable<T>.() -> Unit)

        /**
         * Binds the object to the [RemoteCharacteristic] at a given [UUID].
         * @param uuidString the string of the [UUID] of the [RemoteCharacteristic] to bind to
         * @param binding the [RemoteCharacteristicBinding.EnsuresServicesAvailable] set up binding to the [RemoteCharacteristic]
         * @throws NoSuchElementException if the [RemoteCharacteristic] is not found
         * @throws com.splendo.kaluga.bluetooth.UUIDException if the [UUID] is not valid
         */
        fun characteristic(uuidString: String, binding: RemoteCharacteristicBinding.EnsuresServicesAvailable<T>.() -> Unit) {
            characteristic(uuidFrom(uuidString), binding)
        }

        /**
         * Binds an object of type [R] to the [RemoteCharacteristic] at a given [UUID] and updates the object whenever it changes.
         * @param R the type of the object to bind to
         * @param uuid the [UUID] of the [RemoteCharacteristic] to bind to
         * @param update the function that updates the object whenever the bound object changes
         * @param binding the [RemoteCharacteristicBinding.EnsuresServicesAvailable] set up binding to the [RemoteCharacteristic]
         * @throws NoSuchElementException if the [RemoteCharacteristic] is not found
         */
        fun <R> R.bindCharacteristic(uuid: UUID, update: T.(R) -> T = { this }, binding: RemoteCharacteristicBinding.EnsuresServicesAvailable<R>.() -> Unit)

        /**
         * Binds an object of type [R] to the [RemoteCharacteristic] at a given [UUID] and updates the object whenever it changes.
         * @param R the type of the object to bind to
         * @param uuidString the string of the [UUID] of the [RemoteCharacteristic] to bind to
         * @param update the function that updates the object whenever the bound object changes
         * @param binding the [RemoteCharacteristicBinding.EnsuresServicesAvailable] set up binding to the [RemoteCharacteristic]
         * @throws NoSuchElementException if the [RemoteCharacteristic] is not found
         * @throws com.splendo.kaluga.bluetooth.UUIDException if the [UUID] is not valid
         */
        fun <R> R.bindCharacteristic(uuidString: String, update: T.(R) -> T = { this }, binding: RemoteCharacteristicBinding.EnsuresServicesAvailable<R>.() -> Unit) {
            bindCharacteristic(uuidFrom(uuidString), update, binding)
        }
    }

    /**
     * A [RemoteServiceBinding] and [RequiresServicesDiscoveredBinding.NonMutating] that will only bind whenever the [RemoteService] has been discovered.
     * @property T the type of the object to bind to
     */
    interface RequiresServicesDiscovered<T> :
        RemoteServiceBinding<T>,
        RequiresServicesDiscoveredBinding.NonMutating<T> {

        /**
         * Binds the object to the included [RemoteService] at a given [UUID] whenever it is available.
         * @param uuid the [UUID] of the [RemoteService] to bind to
         * @param binding the [RemoteServiceBinding.RequiresServicesDiscovered] set up binding to the [RemoteService]
         */
        fun service(uuid: UUID, binding: RequiresServicesDiscovered<T>.() -> Unit)

        /**
         * Binds the object to the included [RemoteService] at a given [UUID] whenever it is available.
         * @param uuidString the string of the [UUID] of the [RemoteService] to bind to
         * @param binding the [RemoteServiceBinding.RequiresServicesDiscovered] set up binding to the [RemoteService]
         * @throws com.splendo.kaluga.bluetooth.UUIDException if the [UUID] is not valid
         */
        fun service(uuidString: String, binding: RequiresServicesDiscovered<T>.() -> Unit) {
            service(uuidFrom(uuidString), binding)
        }

        /**
         * Binds an object of type [R] to the included [RemoteService] at a given [UUID] whenever it is available and updates the object whenever it changes.
         * @param R the type of the object to bind to
         * @param uuid the [UUID] of the [RemoteService] to bind to
         * @param update the function that updates the object whenever the bound object changes
         * @param binding the [RemoteServiceBinding.RequiresServicesDiscovered] set up binding to the [RemoteService]
         */
        fun <R> R.bindService(uuid: UUID, update: T.(R) -> T = { this }, binding: RequiresServicesDiscovered<R>.() -> Unit)

        /**
         * Binds an object of type [R] to the included [RemoteService] at a given [UUID] whenever it is available and updates the object whenever it changes.
         * @param R the type of the object to bind to
         * @param uuidString the string of the [UUID] of the [RemoteService] to bind to
         * @param update the function that updates the object whenever the bound object changes
         * @param binding the [RemoteServiceBinding.RequiresServicesDiscovered] set up binding to the [RemoteService]
         * @throws com.splendo.kaluga.bluetooth.UUIDException if the [UUID] is not valid
         */
        fun <R> R.bindService(uuidString: String, update: T.(R) -> T = { this }, binding: RequiresServicesDiscovered<R>.() -> Unit) {
            bindService(uuidFrom(uuidString), update, binding)
        }

        /**
         * Binds the object to the [RemoteCharacteristic] at a given [UUID] whenever it is available.
         * @param uuid the [UUID] of the [RemoteCharacteristic] to bind to
         * @param binding the [RemoteCharacteristicBinding.RequiresServicesDiscovered] set up binding to the [RemoteCharacteristic]
         */
        fun characteristic(uuid: UUID, binding: RemoteCharacteristicBinding.RequiresServicesDiscovered<T>.() -> Unit)

        /**
         * Binds the object to the [RemoteCharacteristic] at a given [UUID] whenever it is available.
         * @param uuidString the string of the [UUID] of the [RemoteCharacteristic] to bind to
         * @param binding the [RemoteCharacteristicBinding.RequiresServicesDiscovered] set up binding to the [RemoteCharacteristic]
         * @throws com.splendo.kaluga.bluetooth.UUIDException if the [UUID] is not valid
         */
        fun characteristic(uuidString: String, binding: RemoteCharacteristicBinding.RequiresServicesDiscovered<T>.() -> Unit) {
            characteristic(uuidFrom(uuidString), binding)
        }

        /**
         * Binds an object of type [R] to the [RemoteCharacteristic] at a given [UUID] whenever it is available and updates the object whenever it changes.
         * @param R the type of the object to bind to
         * @param uuid the [UUID] of the [RemoteCharacteristic] to bind to
         * @param update the function that updates the object whenever the bound object changes
         * @param binding the [RemoteCharacteristicBinding.RequiresServicesDiscovered] set up binding to the [RemoteCharacteristic]
         */
        fun <R> R.bindCharacteristic(uuid: UUID, update: T.(R) -> T = { this }, binding: RemoteCharacteristicBinding.RequiresServicesDiscovered<R>.() -> Unit)

        /**
         * Binds an object of type [R] to the [RemoteCharacteristic] at a given [UUID] whenever it is available and updates the object whenever it changes.
         * @param R the type of the object to bind to
         * @param uuidString the string of the [UUID] of the [RemoteCharacteristic] to bind to
         * @param update the function that updates the object whenever the bound object changes
         * @param binding the [RemoteCharacteristicBinding.RequiresServicesDiscovered] set up binding to the [RemoteCharacteristic]
         * @throws com.splendo.kaluga.bluetooth.UUIDException if the [UUID] is not valid
         */
        fun <R> R.bindCharacteristic(uuidString: String, update: T.(R) -> T = { this }, binding: RemoteCharacteristicBinding.RequiresServicesDiscovered<R>.() -> Unit) {
            bindCharacteristic(uuidFrom(uuidString), update, binding)
        }
    }
}

/**
 * Builder for setting up binding to an object [T] so that it may be changed by a [RemoteCharacteristic].
 * @property T the type of the object to bind to
 */
sealed interface RemoteCharacteristicBinding<T> : RemoteAttributeBinding<T> {

    /**
     * A [RemoteCharacteristicBinding] that assumes the [RemoteCharacteristic] being bound to has been discovered.
     * @property T the type of the object to bind to
     */
    interface EnsuresServicesAvailable<T> : RemoteCharacteristicBinding<T> {

        /**
         * Binds the object to the [RemoteDescriptor] at a given [UUID].
         * @param uuid the [UUID] of the [RemoteDescriptor] to bind to
         * @param binding the [RemoteDescriptorBinding] set up binding to the [RemoteDescriptor]
         * @throws NoSuchElementException if the [RemoteDescriptor] is not found
         */
        fun descriptor(uuid: UUID, binding: RemoteDescriptorBinding<T>.() -> Unit)

        /**
         * Binds the object to the [RemoteDescriptor] at a given [UUID].
         * @param uuidString the string of the [UUID] of the [RemoteDescriptor] to bind to
         * @param binding the [RemoteDescriptorBinding] set up binding to the [RemoteDescriptor]
         * @throws NoSuchElementException if the [RemoteDescriptor] is not found
         * @throws com.splendo.kaluga.bluetooth.UUIDException if the [UUID] is not valid
         */
        fun descriptor(uuidString: String, binding: RemoteDescriptorBinding<T>.() -> Unit) {
            descriptor(uuidFrom(uuidString), binding)
        }

        /**
         * Binds an object of type [R] to the [RemoteDescriptor] at a given [UUID] and updates the object whenever it changes.
         * @param R the type of the object to bind to
         * @param uuid the [UUID] of the [RemoteDescriptor] to bind to
         * @param update the function that updates the object whenever the bound object changes
         * @param binding the [RemoteDescriptorBinding] set up binding to the [RemoteDescriptor]
         * @throws NoSuchElementException if the [RemoteDescriptor] is not found
         */
        fun <R> R.bindDescriptor(uuid: UUID, update: T.(R) -> T = { this }, binding: RemoteDescriptorBinding<R>.() -> Unit)

        /**
         * Binds an object of type [R] to the [RemoteDescriptor] at a given [UUID] and updates the object whenever it changes.
         * @param R the type of the object to bind to
         * @param uuidString the string of the [UUID] of the [RemoteDescriptor] to bind to
         * @param update the function that updates the object whenever the bound object changes
         * @param binding the [RemoteDescriptorBinding] set up binding to the [RemoteDescriptor]
         * @throws NoSuchElementException if the [RemoteDescriptor] is not found
         * @throws com.splendo.kaluga.bluetooth.UUIDException if the [UUID] is not valid
         */
        fun <R> R.bindDescriptor(uuidString: String, update: T.(R) -> T = { this }, binding: RemoteDescriptorBinding<R>.() -> Unit) {
            bindDescriptor(uuidFrom(uuidString), update, binding)
        }
    }

    /**
     * A [RemoteCharacteristicBinding] and [RequiresServicesDiscoveredBinding.NonMutating] that will only bind whenever the [RemoteCharacteristic] has been discovered.
     * @property T the type of the object to bind to
     */
    interface RequiresServicesDiscovered<T> :
        RemoteCharacteristicBinding<T>,
        RequiresServicesDiscoveredBinding.NonMutating<T> {

        /**
         * Binds the object to the [RemoteDescriptor] at a given [UUID] whenever it is available.
         * @param uuid the [UUID] of the [RemoteDescriptor] to bind to
         * @param binding the [RemoteDescriptorBinding.RequiresServicesDiscovered] set up binding to the [RemoteDescriptor]
         */
        fun descriptor(uuid: UUID, binding: RemoteDescriptorBinding.RequiresServicesDiscovered<T>.() -> Unit)

        /**
         * Binds the object to the [RemoteDescriptor] at a given [UUID] whenever it is available.
         * @param uuidString the string of the [UUID] of the [RemoteDescriptor] to bind to
         * @param binding the [RemoteDescriptorBinding.RequiresServicesDiscovered] set up binding to the [RemoteDescriptor]
         * @throws com.splendo.kaluga.bluetooth.UUIDException if the [UUID] is not valid
         */
        fun descriptor(uuidString: String, binding: RemoteDescriptorBinding.RequiresServicesDiscovered<T>.() -> Unit) {
            descriptor(uuidFrom(uuidString), binding)
        }

        /**
         * Binds an object of type [R] to the [RemoteDescriptor] at a given [UUID] whenever it is available and updates the object whenever it changes.
         * @param R the type of the object to bind to
         * @param uuid the [UUID] of the [RemoteDescriptor] to bind to
         * @param update the function that updates the object whenever the bound object changes
         * @param binding the [RemoteDescriptorBinding.RequiresServicesDiscovered] set up binding to the [RemoteDescriptor]
         */
        fun <R> R.bindDescriptor(uuid: UUID, update: T.(R) -> T = { this }, binding: RemoteDescriptorBinding.RequiresServicesDiscovered<R>.() -> Unit)

        /**
         * Binds an object of type [R] to the [RemoteDescriptor] at a given [UUID] whenever it is available and updates the object whenever it changes.
         * @param R the type of the object to bind to
         * @param uuidString the string of the [UUID] of the [RemoteDescriptor] to bind to
         * @param update the function that updates the object whenever the bound object changes
         * @param binding the [RemoteDescriptorBinding.RequiresServicesDiscovered] set up binding to the [RemoteDescriptor]
         * @throws com.splendo.kaluga.bluetooth.UUIDException if the [UUID] is not valid
         */
        fun <R> R.bindDescriptor(uuidString: String, update: T.(R) -> T = { this }, binding: RemoteDescriptorBinding.RequiresServicesDiscovered<R>.() -> Unit) {
            bindDescriptor(uuidFrom(uuidString), update, binding)
        }
    }

    /**
     * Builder for setting up binding to an object [T] so that it may be changed when the [Notification] from a [RemoteCharacteristic] changed.
     * @property T the type of the object to bind to
     * @property Notification the type of Notification that the [RemoteCharacteristic] sends.
     * @property Transformation the type of Transformation that should be applied to the object when the [RemoteAttribute] is read.
     */
    sealed interface ObserveBuilder<T, Notification, Transformation> {

        /**
         * A [ObserveBuilder] that does not mutate the object when a [RemoteCharacteristic] receives a [Notification].
         * @property T the type of the object to bind to
         * @property Notification the type of Notification that the [RemoteCharacteristic] sends.
         * @property Transformation the type of Transformation that should be applied to the object when the [RemoteAttribute] is read.
         */
        interface NonMutating<T, Notification> : ObserveBuilder<T, Notification, Unit> {

            /**
             * Creates a [Mutating] [ObserveBuilder] that mutates the object when when a [RemoteCharacteristic] receives a [Notification].
             * @param builder the builder to apply to the [Mutating] [ObserveBuilder]
             */
            fun mutate(builder: Mutating<T, Notification>.() -> Unit)
        }

        /**
         * A [ObserveBuilder] that mutates the object when a [RemoteCharacteristic] receives a [Notification].
         * @property T the type of the object to bind to
         * @property Notification the type of Notification that the [RemoteCharacteristic] sends.
         * @property Transformation the type of Transformation that should be applied to the object when the [RemoteAttribute] is read.
         */
        interface Mutating<T, Notification> : ObserveBuilder<T, Notification, T>

        /**
         * Sets up a [Transformation] when a [Notification] is received from a [RemoteCharacteristic].
         * @param action the [Transformation] to apply when a [Notification] is received. Contains the received [Notification].
         */
        fun onNotification(action: suspend T.(Notification) -> Transformation)
    }

    /**
     * Sets up observation of a [Notification] from the [RemoteCharacteristic]
     * @param Notification the type of Notification to receive from the [RemoteCharacteristic]
     * @param asValue the function that converts the [ByteArray] to the [Notification]
     * @param builder sets up the observation response to a new [Notification]
     */
    fun <Notification> observe(asValue: ByteArray.() -> Notification, builder: ObserveBuilder.NonMutating<T, Notification>.() -> Unit)

    /**
     * Sets up observation of a [Notification] from the [RemoteCharacteristic]
     * @param Notification the type of Notification to receive from the [RemoteCharacteristic]
     * @param deserializationStrategy the [DeserializationStrategy] to use to convert the [ByteArray] to the [Notification]
     * @param bluetoothFormat the [BluetoothFormat] to use to convert the [ByteArray] to the [Notification]
     * @param builder sets up the observation response to a new [Notification]
     */
    fun <Notification> observe(deserializationStrategy: DeserializationStrategy<Notification>, bluetoothFormat: BluetoothFormat = BluetoothFormat, builder: ObserveBuilder.NonMutating<T, Notification>.() -> Unit) =
        observe({ bluetoothFormat.decodeFromByteArray(deserializationStrategy, this) }, builder)

    /**
     * Sets up observation of a [ByteArray] from the [RemoteCharacteristic]
     * @param builder sets up the observation response to a new [ByteArray]
     */
    fun observe(builder: ObserveBuilder.NonMutating<T, ByteArray>.() -> Unit) = observe(asValue = { this }, builder = builder)
}

/**
 * Sets up observation of a [Notification] from the [RemoteCharacteristic]
 * @param Notification the type of Notification to receive from the [RemoteCharacteristic]
 * @param T  the type of the object to bind to
 * @param bluetoothFormat the [BluetoothFormat] to use to convert the [ByteArray] to the [Notification]
 * @param builder sets up the observation response to a new [Notification]
 */
inline fun <reified Notification, T> RemoteCharacteristicBinding<T>.observe(
    bluetoothFormat: BluetoothFormat = BluetoothFormat,
    noinline builder: ObserveBuilder.NonMutating<T, Notification>.() -> Unit,
) = observe(bluetoothFormat.serializersModule.serializer<Notification>(), bluetoothFormat, builder)


/**
 * Builder for setting up binding to an object [T] so that it may be changed by a [RemoteDescriptor].
 * @property T the type of the object to bind to
 */
interface RemoteDescriptorBinding<T> : RemoteAttributeBinding<T> {

    /**
     * A [RemoteDescriptorBinding] and [RequiresServicesDiscoveredBinding.NonMutating] that will only bind whenever the [RemoteDescriptor] has been discovered.
     * @property T the type of the object to bind to
     */
    interface RequiresServicesDiscovered<T> :
        RemoteDescriptorBinding<T>,
        RequiresServicesDiscoveredBinding.NonMutating<T>
}

/**
 * Binds an object of type [T] to the [Flow] of a [ConnectableDevice], so that it may be updated according to [binding]
 * @param T the type of the object to bind to
 * @param device the [Flow] of the [ConnectableDevice] that should trigger updates.
 * @param scope the [CoroutineScope] in which to bind
 * @param binding the [ConnectedDeviceBinding.RequiresServicesDiscovered] that sets up binding to the [ConnectableDevice]
 * @return a [StateFlow] of [T] that updates whenever mutating occurs due to actions set up in [binding]
 */
@JvmName("bindDevice")
fun <T> T.bind(device: Flow<ConnectableDevice?>, scope: CoroutineScope, binding: ConnectedDeviceBinding.RequiresServicesDiscovered<T>.() -> Unit): StateFlow<T> {
    val onServicesDiscoveredActions = mutableListOf<() -> Unit>()
    val onServicesUndiscoveredActions = mutableListOf<() -> Unit>()
    val builder = ConnectedDeviceBindingImpl.RequiresServicesDiscovered(
        MutableStateFlow(this),
        device,
        onServicesDiscoveredActions,
        onServicesUndiscoveredActions,
        scope,
    ).apply(binding)
    val discoveredActions = onServicesDiscoveredActions.toList()
    val undiscoveredActions = onServicesUndiscoveredActions.toList()
    scope.launch {
        device.startDiscovering().collect { discoveringState ->
            if (discoveringState != null) {
                discoveredActions.forEach { it() }
            } else {
                undiscoveredActions.forEach { it() }
            }
        }
    }
    return builder.build()
}

/**
 * Binds an object of type [T] to a [ConnectableDevice], so that it may be updated according to [binding]
 * @param T the type of the object to bind to
 * @param device the [ConnectableDevice] that should trigger updates.
 * @param scope the [CoroutineScope] in which to bind
 * @param binding the [ConnectedDeviceBinding.RequiresServicesDiscovered] that sets up binding to the [ConnectableDevice]
 * @return a [StateFlow] of [T] that updates whenever mutating occurs due to actions set up in [binding]
 */
fun <T> T.bind(device: ConnectableDevice, scope: CoroutineScope, binding: ConnectedDeviceBinding.RequiresServicesDiscovered<T>.() -> Unit): StateFlow<T> {
    val onServicesDiscoveredActions = mutableListOf<() -> Unit>()
    val onServicesUndiscoveredActions = mutableListOf<() -> Unit>()
    val builder = ConnectedDeviceBindingImpl.RequiresServicesDiscovered(
        MutableStateFlow(this),
        flowOf(device),
        onServicesDiscoveredActions,
        onServicesUndiscoveredActions,
        scope,
    ).apply(binding)
    val discoveredActions = onServicesDiscoveredActions.toList()
    val undiscoveredActions = onServicesUndiscoveredActions.toList()
    scope.launch {
        device.filterDiscovering().collect { discoveringState ->
            if (discoveringState != null) {
                discoveredActions.forEach { it() }
            } else {
                undiscoveredActions.forEach { it() }
            }
        }
    }
    return builder.build()
}

/**
 * Binds an object of type [T] to a [ConnectableDeviceState.Connected.DiscoveredServices] state, so that it may be updated according to [binding].
 * This assumes services has been discovered and as such any [binding] will fail if a [RemoteAttribute] is not available.
 * @param T the type of the object to bind to
 * @param deviceState the  [ConnectableDeviceState.Connected.DiscoveredServices] state
 * @param scope the [CoroutineScope] in which to bind
 * @param binding the [ConnectedDeviceBinding.EnsuresAvailable] that sets up binding to the [ConnectableDevice]
 * @return a [StateFlow] of [T] that updates whenever mutating occurs due to actions set up in [binding]
 */
fun <T> T.bind(
    deviceState: ConnectableDeviceState.Connected.DiscoveredServices,
    scope: CoroutineScope,
    binding: ConnectedDeviceBinding.EnsuresAvailable<T>.() -> Unit,
): StateFlow<T> = ConnectedDeviceBindingImpl.EnsuresServicesAvailable(
    MutableStateFlow(this),
    deviceState,
    scope,
).apply(binding).build()

/**
 * Binds an object of type [T] to the [Flow] of a [RemoteService], so that it may be updated according to [binding]
 * @param T the type of the object to bind to
 * @param service the [Flow] of the [RemoteService] that should trigger updates.
 * @param scope the [CoroutineScope] in which to bind
 * @param binding the [RemoteServiceBinding.RequiresServicesDiscovered] that sets up binding to the [RemoteService]
 * @return a [StateFlow] of [T] that updates whenever mutating occurs due to actions set up in [binding]
 */
@JvmName("bindService")
fun <T> T.bind(service: Flow<RemoteService?>, scope: CoroutineScope, binding: RemoteServiceBinding.RequiresServicesDiscovered<T>.() -> Unit): StateFlow<T> {
    val onServicesDiscoveredActions = mutableListOf<() -> Unit>()
    val onServicesUndiscoveredActions = mutableListOf<() -> Unit>()
    val builder = RemoteServiceBindingImpl.RequiresServicesDiscovered(
        MutableStateFlow(this),
        service,
        onServicesDiscoveredActions,
        onServicesUndiscoveredActions,
        scope,
    ).apply(binding)
    val discoveredActions = onServicesDiscoveredActions.toList()
    val undiscoveredActions = onServicesUndiscoveredActions.toList()
    scope.launch {
        service.collect { service ->
            if (service != null) {
                discoveredActions.forEach { it() }
            } else {
                undiscoveredActions.forEach { it() }
            }
        }
    }
    return builder.build()
}

/**
 * Binds an object of type [T] to a [RemoteService], so that it may be updated according to [binding].
 * @param T the type of the object to bind to
 * @param service the [RemoteService] that should trigger updates.
 * @param scope the [CoroutineScope] in which to bind
 * @param binding the [RemoteServiceBinding.EnsuresAvailable] that sets up binding to the [RemoteService]
 * @return a [StateFlow] of [T] that updates whenever mutating occurs due to actions set up in [binding]
 */
fun <T> T.bind(service: RemoteService, scope: CoroutineScope, binding: RemoteServiceBinding.EnsuresAvailable<T>.() -> Unit): StateFlow<T> =
    RemoteServiceBindingImpl.EnsuresServicesAvailable(
        MutableStateFlow(this),
        service,
        scope,
    ).apply(binding).build()

/**
 * Binds an object of type [T] to the [Flow] of a [RemoteCharacteristic], so that it may be updated according to [binding]
 * @param T the type of the object to bind to
 * @param characteristic the [Flow] of the [RemoteCharacteristic] that should trigger updates.
 * @param scope the [CoroutineScope] in which to bind
 * @param binding the [RemoteCharacteristicBinding.RequiresServicesDiscovered] that sets up binding to the [RemoteCharacteristic]
 * @return a [StateFlow] of [T] that updates whenever mutating occurs due to actions set up in [binding]
 */
@JvmName("bindCharacteristic")
fun <T> T.bind(characteristic: Flow<RemoteCharacteristic?>, scope: CoroutineScope, binding: RemoteCharacteristicBinding.RequiresServicesDiscovered<T>.() -> Unit): StateFlow<T> {
    val onServicesDiscoveredActions = mutableListOf<() -> Unit>()
    val onServicesUndiscoveredActions = mutableListOf<() -> Unit>()
    val builder = RemoteCharacteristicBindingImpl.RequiresServicesDiscovered(
        MutableStateFlow(this),
        characteristic,
        onServicesDiscoveredActions,
        onServicesUndiscoveredActions,
        scope,
    ).apply(binding)
    val discoveredActions = onServicesDiscoveredActions.toList()
    val undiscoveredActions = onServicesUndiscoveredActions.toList()
    scope.launch {
        characteristic.collect { service ->
            if (service != null) {
                discoveredActions.forEach { it() }
            } else {
                undiscoveredActions.forEach { it() }
            }
        }
    }
    return builder.build()
}

/**
 * Binds an object of type [T] to a [RemoteCharacteristic], so that it may be updated according to [binding].
 * @param T the type of the object to bind to
 * @param characteristic the [RemoteCharacteristic] that should trigger updates.
 * @param scope the [CoroutineScope] in which to bind
 * @param binding the [RemoteCharacteristicBinding.EnsuresServicesAvailable] that sets up binding to the [RemoteCharacteristic]
 * @return a [StateFlow] of [T] that updates whenever mutating occurs due to actions set up in [binding]
 */
fun <T> T.bind(characteristic: RemoteCharacteristic, scope: CoroutineScope, binding: RemoteCharacteristicBinding.EnsuresServicesAvailable<T>.() -> Unit) =
    RemoteCharacteristicBindingImpl.EnsuresServicesAvailable(
        MutableStateFlow(this),
        characteristic,
        scope,
    ).apply(binding).build()


/**
 * Binds an object of type [T] to the [Flow] of a [RemoteDescriptor], so that it may be updated according to [binding]
 * @param T the type of the object to bind to
 * @param descriptor the [Flow] of the [RemoteDescriptor] that should trigger updates.
 * @param scope the [CoroutineScope] in which to bind
 * @param binding the [RemoteDescriptorBinding.RequiresServicesDiscovered] that sets up binding to the [RemoteDescriptor]
 * @return a [StateFlow] of [T] that updates whenever mutating occurs due to actions set up in [binding]
 */
@JvmName("bindDescriptor")
fun <T> T.bind(descriptor: Flow<RemoteDescriptor?>, scope: CoroutineScope, binding: RemoteDescriptorBinding.RequiresServicesDiscovered<T>.() -> Unit): StateFlow<T> {
    val onServicesDiscoveredActions = mutableListOf<() -> Unit>()
    val onServicesUndiscoveredActions = mutableListOf<() -> Unit>()
    val builder = RemoteDescriptorBindingImpl.RequiresServicesDiscovered(
        MutableStateFlow(this),
        descriptor,
        onServicesDiscoveredActions,
        onServicesUndiscoveredActions,
        scope,
    ).apply(binding)
    val discoveredActions = onServicesDiscoveredActions.toList()
    val undiscoveredActions = onServicesUndiscoveredActions.toList()
    scope.launch {
        descriptor.collect { service ->
            if (service != null) {
                discoveredActions.forEach { it() }
            } else {
                undiscoveredActions.forEach { it() }
            }
        }
    }
    return builder.build()
}

/**
 * Binds an object of type [T] to a [RemoteDescriptor], so that it may be updated according to [binding].
 * @param T the type of the object to bind to
 * @param descriptor the [RemoteDescriptor] that should trigger updates.
 * @param scope the [CoroutineScope] in which to bind
 * @param binding the [RemoteDescriptorBinding] that sets up binding to the [RemoteDescriptor]
 * @return a [StateFlow] of [T] that updates whenever mutating occurs due to actions set up in [binding]
 */
fun <T> T.bind(descriptor: RemoteDescriptor, scope: CoroutineScope, binding: RemoteDescriptorBinding<T>.() -> Unit): StateFlow<T> =
    RemoteDescriptorBindingImpl.EnsuresAvailable(MutableStateFlow(this), descriptor, scope).apply(binding).build()

private sealed class ConnectedDeviceBindingImpl<T>(protected val callingScope: MutableStateFlow<T>, protected val scope: CoroutineScope) : ConnectedDeviceBinding<T> {

    protected val bindingSubActions = mutableListOf<() -> Unit>()
    protected val observations = mutableListOf<suspend CoroutineScope.() -> Unit>()

    class EnsuresServicesAvailable<T>(callingScope: MutableStateFlow<T>, private val deviceState: ConnectableDeviceState.Connected.DiscoveredServices, scope: CoroutineScope) :
        ConnectedDeviceBindingImpl<T>(callingScope, scope),
        ConnectedDeviceBinding.EnsuresAvailable<T> {
        override fun service(uuid: UUID, binding: RemoteServiceBinding.EnsuresAvailable<T>.() -> Unit) {
            val serviceBinding = RemoteServiceBindingImpl.EnsuresServicesAvailable(callingScope, deviceState.services[uuid], scope).apply(binding)
            bindingSubActions += {
                serviceBinding.build()
            }
        }

        override fun <R> R.bindService(uuid: UUID, update: T.(R) -> T, binding: RemoteServiceBinding.EnsuresAvailable<R>.() -> Unit) {
            val serviceBinding = RemoteServiceBindingImpl.EnsuresServicesAvailable(MutableStateFlow(this), deviceState.services[uuid], scope).apply(binding)
            observations += {
                serviceBinding.build().collect { update -> callingScope.update { scope -> scope.update(update) } }
            }
        }
    }

    class RequiresServicesDiscovered<T>(
        callingScope: MutableStateFlow<T>,
        private val device: Flow<ConnectableDevice?>,
        private val onServicesDiscoveredActions: MutableList<() -> Unit>,
        private val onServicesUndiscoveredActions: MutableList<() -> Unit>,
        scope: CoroutineScope,
    ) : ConnectedDeviceBindingImpl<T>(callingScope, scope),
        ConnectedDeviceBinding.RequiresServicesDiscovered<T>,
        RequiresServicesDiscoveredBinding.NonMutating<T> by RequiresServicesDiscoveredImpl.NonMutating(callingScope, onServicesDiscoveredActions, onServicesUndiscoveredActions) {
        override fun service(uuid: UUID, binding: RemoteServiceBinding.RequiresServicesDiscovered<T>.() -> Unit) {
            val serviceBinding = RemoteServiceBindingImpl.RequiresServicesDiscovered(
                callingScope,
                device.discoveredServices().getOrNull(uuid),
                onServicesDiscoveredActions,
                onServicesUndiscoveredActions,
                scope,
            ).apply(binding)
            bindingSubActions += {
                serviceBinding.build()
            }
        }

        override fun <R> R.bindService(uuid: UUID, update: T.(R) -> T, binding: RemoteServiceBinding.RequiresServicesDiscovered<R>.() -> Unit) {
            val serviceBinding = RemoteServiceBindingImpl.RequiresServicesDiscovered(
                MutableStateFlow(this),
                device.discoveredServices().getOrNull(uuid),
                onServicesDiscoveredActions,
                onServicesUndiscoveredActions,
                scope,
            ).apply(binding)
            observations += {
                serviceBinding.build().collect { update -> callingScope.update { scope -> scope.update(update) } }
            }
        }
    }

    fun build(): StateFlow<T> {
        bindingSubActions.forEach { it() }
        observations.forEach {
            scope.launch(block = it)
        }

        return callingScope.asStateFlow()
    }
}

private sealed class RemoteServiceBindingImpl<T>(protected val callingScope: MutableStateFlow<T>, protected val scope: CoroutineScope) : RemoteServiceBinding<T> {

    protected val bindingSubActions = mutableListOf<() -> Unit>()
    protected val observations = mutableListOf<suspend CoroutineScope.() -> Unit>()

    class EnsuresServicesAvailable<T>(callingScope: MutableStateFlow<T>, private val service: RemoteService, scope: CoroutineScope) :
        RemoteServiceBindingImpl<T>(callingScope, scope),
        RemoteServiceBinding.EnsuresAvailable<T> {
        override fun characteristic(uuid: UUID, binding: RemoteCharacteristicBinding.EnsuresServicesAvailable<T>.() -> Unit) {
            val characteristicBinding = RemoteCharacteristicBindingImpl.EnsuresServicesAvailable(callingScope, service.characteristics[uuid], scope).apply(binding)
            bindingSubActions += {
                characteristicBinding.build()
            }
        }

        override fun <R> R.bindCharacteristic(uuid: UUID, update: T.(R) -> T, binding: RemoteCharacteristicBinding.EnsuresServicesAvailable<R>.() -> Unit) {
            val characteristicBinding = RemoteCharacteristicBindingImpl.EnsuresServicesAvailable(MutableStateFlow(this), service.characteristics[uuid], scope).apply(binding)
            observations += {
                characteristicBinding.build().collect { update -> callingScope.update { scope -> scope.update(update) } }
            }
        }

        override fun service(uuid: UUID, binding: RemoteServiceBinding.EnsuresAvailable<T>.() -> Unit) {
            val serviceBinding = EnsuresServicesAvailable(callingScope, service.includedServices[uuid], scope).apply(binding)
            bindingSubActions += {
                serviceBinding.build()
            }
        }

        override fun <R> R.bindService(uuid: UUID, update: T.(R) -> T, binding: RemoteServiceBinding.EnsuresAvailable<R>.() -> Unit) {
            val serviceBinding = EnsuresServicesAvailable(MutableStateFlow(this), service.includedServices[uuid], scope).apply(binding)
            observations += {
                serviceBinding.build().collect { update -> callingScope.update { scope -> scope.update(update) } }
            }
        }
    }

    class RequiresServicesDiscovered<T>(
        callingScope: MutableStateFlow<T>,
        private val service: Flow<RemoteService?>,
        private val onServicesDiscoveredActions: MutableList<() -> Unit>,
        private val onServicesUndiscoveredActions: MutableList<() -> Unit>,
        scope: CoroutineScope,
    ) : RemoteServiceBindingImpl<T>(callingScope, scope),
        RemoteServiceBinding.RequiresServicesDiscovered<T>,
        RequiresServicesDiscoveredBinding.NonMutating<T> by RequiresServicesDiscoveredImpl.NonMutating(callingScope, onServicesDiscoveredActions, onServicesUndiscoveredActions) {

        override fun service(uuid: UUID, binding: RemoteServiceBinding.RequiresServicesDiscovered<T>.() -> Unit) {
            val serviceBinding = RequiresServicesDiscovered(
                callingScope,
                service.includedServices().getOrNull(uuid),
                onServicesDiscoveredActions,
                onServicesUndiscoveredActions,
                scope,
            ).apply(binding)
            bindingSubActions += {
                serviceBinding.build()
            }
        }

        override fun <R> R.bindService(uuid: UUID, update: T.(R) -> T, binding: RemoteServiceBinding.RequiresServicesDiscovered<R>.() -> Unit) {
            val serviceBinding = RequiresServicesDiscovered(
                MutableStateFlow(this),
                service.includedServices().getOrNull(uuid),
                onServicesDiscoveredActions,
                onServicesUndiscoveredActions,
                scope,
            ).apply(binding)
            observations += {
                serviceBinding.build().collect { update -> callingScope.update { scope -> scope.update(update) } }
            }
        }

        override fun characteristic(uuid: UUID, binding: RemoteCharacteristicBinding.RequiresServicesDiscovered<T>.() -> Unit) {
            val characteristicBinding = RemoteCharacteristicBindingImpl.RequiresServicesDiscovered(
                callingScope,
                service.characteristics().getOrNull(uuid),
                onServicesDiscoveredActions,
                onServicesUndiscoveredActions,
                scope,
            ).apply(binding)
            bindingSubActions += {
                characteristicBinding.build()
            }
        }

        override fun <R> R.bindCharacteristic(uuid: UUID, update: T.(R) -> T, binding: RemoteCharacteristicBinding.RequiresServicesDiscovered<R>.() -> Unit) {
            val characteristicBinding = RemoteCharacteristicBindingImpl.RequiresServicesDiscovered(
                MutableStateFlow(this),
                service.characteristics().getOrNull(uuid),
                onServicesDiscoveredActions,
                onServicesUndiscoveredActions,
                scope,
            ).apply(binding)
            observations += {
                characteristicBinding.build().collect { update -> callingScope.update { scope -> scope.update(update) } }
            }
        }
    }

    fun build(): StateFlow<T> {
        bindingSubActions.forEach { it() }
        observations.forEach {
            scope.launch(block = it)
        }

        return callingScope.asStateFlow()
    }
}

private sealed class RemoteCharacteristicBindingImpl<T>(callingScope: MutableStateFlow<T>, getCharacteristic: suspend () -> RemoteCharacteristic?, scope: CoroutineScope) :
    RemoteAttributeBindingImpl<T, DeviceAction.Read.Characteristic, DeviceAction.Write.Characteristic>(
        callingScope,
        getCharacteristic,
        scope,
    ),
    RemoteCharacteristicBinding<T> {

    class EnsuresServicesAvailable<T>(private val callingScope: MutableStateFlow<T>, private val characteristic: RemoteCharacteristic, private val scope: CoroutineScope) :
        RemoteCharacteristicBindingImpl<T>(
            callingScope,
            { characteristic },
            scope,
        ),
        RemoteCharacteristicBinding.EnsuresServicesAvailable<T> {

        override fun descriptor(uuid: UUID, binding: RemoteDescriptorBinding<T>.() -> Unit) {
            val descriptorBinding = RemoteDescriptorBindingImpl.EnsuresAvailable(callingScope, characteristic.descriptors[uuid], scope).apply(binding)
            bindingSubActions += {
                descriptorBinding.build()
            }
        }

        override fun <R> R.bindDescriptor(uuid: UUID, update: T.(R) -> T, binding: RemoteDescriptorBinding<R>.() -> Unit) {
            val descriptorBinding = RemoteDescriptorBindingImpl.EnsuresAvailable(MutableStateFlow(this), characteristic.descriptors[uuid], scope).apply(binding)
            observations += {
                descriptorBinding.build().collect { update -> callingScope.update { scope -> scope.update(update) } }
            }
        }

        override fun <R> observe(asValue: ByteArray.() -> R, builder: ObserveBuilder.NonMutating<T, R>.() -> Unit) {
            val builder = ObserverBuilder.NonMutating<T, R>().apply(builder)
            val onNotificationActions = builder.notificationActions()
            observations += {
                characteristic.value().collect { value ->
                    onNotificationActions.forEach { action ->
                        callingScope.update { it.action(value.asValue()) }
                    }
                }
            }
        }
    }

    class RequiresServicesDiscovered<T>(
        private val callingScope: MutableStateFlow<T>,
        private val characteristic: Flow<RemoteCharacteristic?>,
        private val onServicesDiscoveredActions: MutableList<() -> Unit>,
        private val onServicesUndiscoveredActions: MutableList<() -> Unit>,
        private val scope: CoroutineScope,
    ) : RemoteCharacteristicBindingImpl<T>(
        callingScope,
        { characteristic.firstOrNull() },
        scope,
    ),
        RemoteCharacteristicBinding.RequiresServicesDiscovered<T>,
        RequiresServicesDiscoveredBinding.NonMutating<T> by RequiresServicesDiscoveredImpl.NonMutating(callingScope, onServicesDiscoveredActions, onServicesUndiscoveredActions) {

        override fun descriptor(uuid: UUID, binding: RemoteDescriptorBinding.RequiresServicesDiscovered<T>.() -> Unit) {
            val descriptorBinding = RemoteDescriptorBindingImpl.RequiresServicesDiscovered(
                callingScope,
                characteristic.descriptors().getOrNull(uuid),
                onServicesDiscoveredActions,
                onServicesUndiscoveredActions,
                scope,
            ).apply(binding)
            bindingSubActions += {
                descriptorBinding.build()
            }
        }

        override fun <R> R.bindDescriptor(uuid: UUID, update: T.(R) -> T, binding: RemoteDescriptorBinding.RequiresServicesDiscovered<R>.() -> Unit) {
            val descriptorBinding = RemoteDescriptorBindingImpl.RequiresServicesDiscovered(
                MutableStateFlow(this),
                characteristic.descriptors().getOrNull(uuid),
                onServicesDiscoveredActions,
                onServicesUndiscoveredActions,
                scope,
            ).apply(binding)
            observations += {
                descriptorBinding.build().collect { update -> callingScope.update { scope -> scope.update(update) } }
            }
        }

        override fun <R> observe(asValue: ByteArray.() -> R, builder: ObserveBuilder.NonMutating<T, R>.() -> Unit) {
            val builder = ObserverBuilder.NonMutating<T, R>().apply(builder)
            val onNotificationActions = builder.notificationActions()
            observations += {
                characteristic.value().collect { value ->
                    onNotificationActions.forEach { action ->
                        callingScope.update { it.action(value.asValue()) }
                    }
                }
            }
        }
    }

    private sealed class ObserverBuilder<T, Notification> {

        protected val onNotificationActions = mutableListOf<suspend T.(Notification) -> T>()

        class NonMutating<T, Notification> :
            ObserverBuilder<T, Notification>(),
            ObserveBuilder.NonMutating<T, Notification> {
            override fun mutate(builder: ObserveBuilder.Mutating<T, Notification>.() -> Unit) {
                val mutating = Mutating<T, Notification>().apply(builder)
                onNotificationActions += mutating.notificationActions()
            }

            override fun onNotification(action: suspend T.(Notification) -> Unit) {
                onNotificationActions += { notification ->
                    action(notification)
                    this
                }
            }
        }

        class Mutating<T, Notification> :
            ObserverBuilder<T, Notification>(),
            ObserveBuilder.Mutating<T, Notification> {

            override fun onNotification(action: suspend T.(Notification) -> T) {
                onNotificationActions += action
            }
        }

        fun notificationActions() = onNotificationActions.toList()
    }
}

private sealed class RemoteDescriptorBindingImpl<T>(callingScope: MutableStateFlow<T>, getDescriptor: suspend () -> RemoteDescriptor?, scope: CoroutineScope) :
    RemoteAttributeBindingImpl<T, DeviceAction.Read.Descriptor, DeviceAction.Write.Descriptor>(
        callingScope,
        getDescriptor,
        scope,
    ) {

    class EnsuresAvailable<T>(callingScope: MutableStateFlow<T>, descriptor: RemoteDescriptor, scope: CoroutineScope) :
        RemoteDescriptorBindingImpl<T>(
            callingScope,
            { descriptor },
            scope,
        ),
        RemoteDescriptorBinding<T>

    class RequiresServicesDiscovered<T>(
        callingScope: MutableStateFlow<T>,
        descriptors: Flow<RemoteDescriptor?>,
        onServicesDiscoveredActions: MutableList<() -> Unit>,
        onServicesUndiscoveredActions: MutableList<() -> Unit>,
        scope: CoroutineScope,
    ) : RemoteDescriptorBindingImpl<T>(
        callingScope,
        { descriptors.firstOrNull() },
        scope,
    ),
        RemoteDescriptorBinding.RequiresServicesDiscovered<T>,
        RequiresServicesDiscoveredBinding.NonMutating<T> by RequiresServicesDiscoveredImpl.NonMutating(callingScope, onServicesDiscoveredActions, onServicesUndiscoveredActions)
}

private abstract class RemoteAttributeBindingImpl<T, ReadAction : DeviceAction.Read, WriteAction : DeviceAction.Write>(
    private val callingScope: MutableStateFlow<T>,
    private val getAttribute: suspend () -> RemoteAttribute<ReadAction, WriteAction>?,
    private val scope: CoroutineScope,
) : RemoteAttributeBinding<T> {

    private sealed class ReadBuilder<T, Trigger, Response> {

        protected val onReadActions = mutableListOf<T.(Response, Trigger) -> T>()
        protected val onFailedToReadActions = mutableListOf<T.(Trigger, GattResponse.ReadError) -> T>()

        class NonMutating<T, Trigger, Response> :
            ReadBuilder<T, Trigger, Response>(),
            RemoteAttributeBinding.ReadBuilder.NonMutating<T, Trigger, Response> {
            override fun mutate(builder: RemoteAttributeBinding.ReadBuilder.Mutating<T, Trigger, Response>.() -> Unit) {
                val mutating = Mutating<T, Trigger, Response>().apply(builder)
                onReadActions += mutating.readActions()
                onFailedToReadActions += mutating.failedToReadActions()
            }

            override fun onRead(action: T.(Response, Trigger) -> Unit) {
                onReadActions += { response, trigger ->
                    action(response, trigger)
                    this
                }
            }

            override fun onFailedToRead(action: T.(Trigger, GattResponse.ReadError) -> Unit) {
                onFailedToReadActions += { trigger, error ->
                    action(trigger, error)
                    this
                }
            }
        }

        class Mutating<T, Trigger, Response> :
            ReadBuilder<T, Trigger, Response>(),
            RemoteAttributeBinding.ReadBuilder.Mutating<T, Trigger, Response> {

            override fun onRead(action: T.(Response, Trigger) -> T) {
                onReadActions += action
            }
            override fun onFailedToRead(action: T.(Trigger, GattResponse.ReadError) -> T) {
                onFailedToReadActions += action
            }
        }

        fun readActions() = onReadActions.toList()
        fun failedToReadActions() = onFailedToReadActions.toList()
    }

    private sealed class WriteBuilder<T, Data, Transformation> {

        protected val onWriteActions = mutableListOf<T.(Data) -> T>()
        protected val onFailedToWriteActions = mutableListOf<T.(Data, GattResponse.WriteError) -> T>()
        class NonMutating<T, Data> :
            WriteBuilder<T, Data, Unit>(),
            RemoteAttributeBinding.WriteBuilder.NonMutating<T, Data> {
            override fun mutate(builder: RemoteAttributeBinding.WriteBuilder.Mutating<T, Data>.() -> Unit) {
                val mutating = Mutating<T, Data>().apply(builder)
                onWriteActions += mutating.writeActions()
                onFailedToWriteActions += mutating.failedToWriteActions()
            }

            override fun onWrite(action: T.(Data) -> Unit) {
                onWriteActions += { data ->
                    action(data)
                    this
                }
            }

            override fun onFailedToWrite(action: T.(Data, GattResponse.WriteError) -> Unit) {
                onFailedToWriteActions += { data, error ->
                    action(data, error)
                    this
                }
            }
        }

        class Mutating<T, Data> :
            WriteBuilder<T, Data, T>(),
            RemoteAttributeBinding.WriteBuilder.Mutating<T, Data> {

            override fun onWrite(action: T.(Data) -> T) {
                onWriteActions += action
            }
            override fun onFailedToWrite(action: T.(Data, GattResponse.WriteError) -> T) {
                onFailedToWriteActions += action
            }
        }

        fun writeActions() = onWriteActions.toList()
        fun failedToWriteActions() = onFailedToWriteActions.toList()
    }

    protected val bindingSubActions = mutableListOf<() -> Unit>()
    protected val observations = mutableListOf<suspend CoroutineScope.() -> Unit>()

    override fun <Trigger, Response> Channel<Trigger>.consumeToTriggerRead(
        asValue: ByteArray.() -> Response,
        builder: RemoteAttributeBinding.ReadBuilder.NonMutating<T, Trigger, Response>.() -> Unit,
    ) {
        val builder = ReadBuilder.NonMutating<T, Trigger, Response>().apply(builder)
        val onReadActions = builder.readActions()
        val onFailedToReadActions = builder.failedToReadActions()
        observations += {
            consumeEach { trigger ->
                read(trigger, asValue, onReadActions, onFailedToReadActions)
            }
        }
    }

    override fun <Trigger, Response> Flow<Trigger>.collectToTriggerRead(
        asValue: ByteArray.() -> Response,
        builder: RemoteAttributeBinding.ReadBuilder.NonMutating<T, Trigger, Response>.() -> Unit,
    ) {
        val builder = ReadBuilder.NonMutating<T, Trigger, Response>().apply(builder)
        val onReadActions = builder.readActions()
        val onFailedToReadActions = builder.failedToReadActions()
        observations += {
            collect { trigger ->
                read(trigger, asValue, onReadActions, onFailedToReadActions)
            }
        }
    }

    override fun <Data> Channel<Data>.consumeToTriggerWrite(asByte: Data.() -> ByteArray, builder: RemoteAttributeBinding.WriteBuilder.NonMutating<T, Data>.() -> Unit) {
        val builder = WriteBuilder.NonMutating<T, Data>().apply(builder)
        val onWriteActions = builder.writeActions()
        val onFailedToWriteActions = builder.failedToWriteActions()
        observations += {
            consumeEach { value ->
                write(value, asByte, onWriteActions, onFailedToWriteActions)
            }
        }
    }

    override fun <Data> Flow<Data>.collectToTriggerWrite(asByte: Data.() -> ByteArray, builder: RemoteAttributeBinding.WriteBuilder.NonMutating<T, Data>.() -> Unit) {
        val builder = WriteBuilder.NonMutating<T, Data>().apply(builder)
        val onWriteActions = builder.writeActions()
        val onFailedToWriteActions = builder.failedToWriteActions()
        observations += {
            collect { value ->
                write(value, asByte, onWriteActions, onFailedToWriteActions)
            }
        }
    }

    private suspend fun <Trigger, Response> read(
        trigger: Trigger,
        asResponse: ByteArray.() -> Response,
        onReadActions: List<T.(Response, Trigger) -> T>,
        onFailedToReadActions: List<T.(Trigger, GattResponse.ReadError) -> T>,
    ) = when (val result = getAttribute()?.read() ?: GattResponse.DeviceUnavailable) {
        is GattResponse.ReadSuccess -> onReadActions.forEach { action ->
            callingScope.update { it.action(result.value.asResponse(), trigger) }
        }
        is GattResponse.ReadError -> onFailedToReadActions.forEach { action ->
            callingScope.update { it.action(trigger, result) }
        }
    }

    private suspend fun <Data> write(
        value: Data,
        asByte: Data.() -> ByteArray,
        onWriteActions: List<T.(Data) -> T>,
        onFailedToWriteActions: List<T.(Data, GattResponse.WriteError) -> T>,
    ) = when (val result = getAttribute()?.write(value.asByte()) ?: GattResponse.DeviceUnavailable) {
        is GattResponse.WriteSuccess -> onWriteActions.forEach { action ->
            callingScope.update { it.action(value) }
        }
        is GattResponse.WriteError -> onFailedToWriteActions.forEach { action ->
            callingScope.update { it.action(value, result) }
        }
    }
    fun build(): StateFlow<T> {
        bindingSubActions.forEach { it() }
        observations.forEach {
            scope.launch(block = it)
        }

        return callingScope.asStateFlow()
    }
}

internal sealed class RequiresServicesDiscoveredImpl<T> {
    class NonMutating<T>(
        private val callingScope: MutableStateFlow<T>,
        private val onServicesDiscoveredActions: MutableList<() -> Unit>,
        private val onServicesUndiscoveredActions: MutableList<() -> Unit>,
    ) : RequiresServicesDiscoveredImpl<T>(),
        RequiresServicesDiscoveredBinding.NonMutating<T> {

        override fun mutate(builder: RequiresServicesDiscoveredBinding.Mutating<T>.() -> Unit) {
            val mutating = Mutating(callingScope, onServicesDiscoveredActions, onServicesUndiscoveredActions).apply(builder)
        }

        override fun onAvailable(action: T.() -> Unit) {
            onServicesDiscoveredActions += {
                callingScope.value.action()
            }
        }

        override fun onUnavailable(action: T.() -> Unit) {
            onServicesUndiscoveredActions += {
                callingScope.value.action()
            }
        }
    }

    class Mutating<T>(
        private val callingScope: MutableStateFlow<T>,
        private val onServicesDiscoveredActions: MutableList<() -> Unit>,
        private val onServicesUndiscoveredActions: MutableList<() -> Unit>,
    ) : RequiresServicesDiscoveredImpl<T>(),
        RequiresServicesDiscoveredBinding.Mutating<T> {
        override fun onAvailable(action: T.() -> T) {
            onServicesDiscoveredActions += {
                callingScope.update { it.action() }
            }
        }

        override fun onUnavailable(action: T.() -> T) {
            onServicesUndiscoveredActions += {
                callingScope.update { it.action() }
            }
        }
    }
}
