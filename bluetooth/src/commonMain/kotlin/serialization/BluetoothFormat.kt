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

package com.splendo.kaluga.bluetooth.serialization

import com.splendo.kaluga.base.bytes.ByteArrayBuilder
import com.splendo.kaluga.base.bytes.buildByteArray
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer

/**
 * A [BinaryFormat] used for encoding and decoding structures to a Binary Data usable for Bluetooth communication.
 *
 * By default this will serialize as follows:
 *
 * Booleans will be added to the next available byte. Up to 8 booleans in a row will fit in a single byte, their bit position determined by their order.
 * Numbers will be serialized as their byte length implies (e.g. an Int is 4 bytes, a Short 2)
 * Strings and Collections will be prefixed with a single byte containing their length. If longer encoding will fail.
 * Polymorphic and Enum classes will serialize their serialName as a string with no length prefix.
 * When an element is nullable, a nullable flag will be added to the flags header. The position in the flags header is determined by order of the element within the object.
 *
 * Multiple flags are available to change this behaviour:
 *
 * - Use [Prefix] to always add bytes to the start of the structure.
 * - Use [Postfix] to always add bytes to the end of the structure.
 * - Use [Checksum] to add checksum bytes (of width [Checksum.width]) between the body and the [Postfix]. Checksum will be calculated over body only.
 * When decoding, if [validateChecksum] is `true` the checksum will be automatically compared to the checksum in the data and throw an exception if they don't match.
 * - Use [FlagIndex] to change the position of the header flag(s) to be used for storing headers.
 * If applied to a Boolean, the boolean will be stored as a flag instead of within the body itself.
 * - Use [FlagWidth] to change the width of the flags to be used by this element. If the desired width is bigger than this width, flag will be ignored.
 * - Use [ByteOrder] to change the byte order in which this element is encoded. Nested structures must have the same byte order, though primary types can change.
 * - Use [LengthPrefix] to change the length prefix used in Strings or Collections.
 * - Use [Encoded] to change the encoding used in Strings.
 * - Use [NullTerminated] to change the end marking of a Collection of String to be determined by a null byte instead of a length prefix
 * - Use [Unsigned] to encode numbers as unsigned.
 * - Use [Scalar] to encode numbers as a scalar value. Length can be modified using [Size], defaults to [Length.`16_BIT`] for Floats and [Length.`32_BIT`] for Doubles
 * - Use [MedFloat] to encode numbers as a MedFloat value. Can have its sizing determined by [Size].
 * When [Length.`16_BIT`] encodes as [com.splendo.kaluga.base.utils.MedFloat16], when [Length.`32_BIT`] encodes as [com.splendo.kaluga.base.utils.MedFloat32].
 * Any other [Size] is not allowed.
 * - Use [Size] to change the length of the bytes used to encode a numeric value.
 * When multiple are added, the smallest [Length] that fits the entire number will be used and flags will be added to the header to indicate which size was picked.
 * For Float/Double values, this can only be [Length.`32_BIT`] or [Length.`64_BIT`], for [MedFloat] it is restricted to [Length.`16_BIT`] and [Length.`32_BIT`].
 * - Use [Unsized] to mark a String or Collection as Unsized, meaning all remaining bytes (with the exception of any [Checksum] or [Postfix]) belong to this object.
 * Attempting to encode data after will lead to an exception.
 * - Use [NullIfEmpty] to mark a Collection as nullable if it is empty. When null its size will not be encoded.
 * - Use [SerializedByteValue] to change the byte identifier of an Enum or Polymorphic class. This replaces serializing its serial name as an unsized string.
 *
 * Equivalent flags are available to encode items in a List (e.g. [ItemSize]) or key/values in a Map (e.g. [KeyEncoded], [ValueNullTerminated])
 * @property validateChecksum if `true` decoding any data marked with [Checksum] will automatically validate the checksum and throw an exception if they don't match.
 * @property serializersModule Contains all serializers registered by format user for Contextual and Polymorphic serialization.
 * The same module should be exposed in the format's Encoder and Decoder.
 */
sealed class BluetoothFormat(private val validateChecksum: Boolean, override val serializersModule: SerializersModule = EmptySerializersModule()) : BinaryFormat {

    /**
     * Builder for creating a [BluetoothFormat]
     * @property validateChecksum if `true` decoding any data marked with [Checksum] will automatically validate the checksum and throw an exception if they don't match.
     * @property serializersModule Contains all serializers registered by format user for Contextual and Polymorphic serialization.
     * The same module should be exposed in the format's Encoder and Decoder.
     */
    class Builder internal constructor(from: BluetoothFormat) {

        var validateChecksum: Boolean = true
        var serializersModule: SerializersModule = from.serializersModule
    }

    /**
     * Default [BluetoothFormat]
     */
    companion object Default : BluetoothFormat(true) {
        operator fun invoke(from: BluetoothFormat = this, builder: Builder.() -> Unit): BluetoothFormat {
            val b = Builder(
                from,
            ).apply(builder)
            return BluetoothFormatImpl(
                b,
            )
        }
    }

    override fun <T> encodeToByteArray(serializer: SerializationStrategy<T>, value: T): ByteArray {
        val flag = BluetoothBinaryDescriptorRegistry.bluetoothBinaryDescriptor(serializer.descriptor, serializersModule)
        val builder = object : BinaryBuilder {
            val flags = MutableList(maxOf(flag.bitIndex + flag.bitWidth, 0)) { false }
            private val actions = mutableListOf<ByteArrayBuilder.() -> Unit>()
            private var isOfUnconstrainedSize: Boolean = false

            override fun addFlag(index: Int, value: Boolean) {
                flags[index] = value
            }

            override fun addAction(action: ByteArrayBuilder.() -> Unit) {
                require(!isOfUnconstrainedSize) { "This object has data of an unconstrained size." }
                actions += action
            }

            override fun makeUnconstrained() {
                isOfUnconstrainedSize = true
            }

            override fun build(): ByteArray = buildByteArray(flag.byteOrder) {
                flags.forEach { add(it) }
                actions.forEach { apply(it) }
            }
        }
        val encoder = BluetoothBinaryEncoder(flag, builder, serializersModule)
        serializer.serialize(encoder, value)

        return builder.build()
    }

    override fun <T> decodeFromByteArray(deserializer: DeserializationStrategy<T>, bytes: ByteArray): T {
        val flag = BluetoothBinaryDescriptorRegistry.bluetoothBinaryDescriptor(deserializer.descriptor, serializersModule)
        val decoder = BluetoothBinaryDecoder(flag, RootBluetoothBinaryDescriptorDecoder(bytes, flag.byteOrder, validateChecksum), serializersModule)

        return deserializer.deserialize(decoder)
    }

    /**
     * Returns a [KSerializer] for the given type [T] used by this [BluetoothFormat].
     */
    inline fun <reified T> serializer(): KSerializer<T> = serializersModule.serializer()
}

private class BluetoothFormatImpl(builder: BluetoothFormat.Builder) : BluetoothFormat(builder.validateChecksum, builder.serializersModule)
