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

import com.splendo.kaluga.base.bytes.Encoding
import com.splendo.kaluga.base.bytes.StringEncodingSettings
import com.splendo.kaluga.base.bytes.decodeAsciiChar
import com.splendo.kaluga.base.bytes.decodeDouble
import com.splendo.kaluga.base.bytes.decodeFloat
import com.splendo.kaluga.base.bytes.decodeInt
import com.splendo.kaluga.base.bytes.decodeInt24
import com.splendo.kaluga.base.bytes.decodeLong
import com.splendo.kaluga.base.bytes.decodeMedFloat16
import com.splendo.kaluga.base.bytes.decodeMedFloat32
import com.splendo.kaluga.base.bytes.decodeShort
import com.splendo.kaluga.base.bytes.decodeString
import com.splendo.kaluga.base.bytes.decodeUInt
import com.splendo.kaluga.base.bytes.decodeUInt24
import com.splendo.kaluga.base.bytes.decodeULong
import com.splendo.kaluga.base.bytes.decodeUShort
import com.splendo.kaluga.base.bytes.decodeUTF16Char
import com.splendo.kaluga.base.bytes.decodeUTF8Char
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.modules.SerializersModule
import kotlin.math.pow

/**
 * [Decoder] of a single [BluetoothBinaryDescriptor]. Its [decoder] should match the structure of the descriptor
 */
internal class BluetoothBinaryDecoder(
    private val binaryDescriptor: BluetoothBinaryDescriptor,
    private val decoder: BluetoothBinaryDescriptorDecoder,
    override val serializersModule: SerializersModule,
) : Decoder {

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder = when (descriptor.kind) {
        is StructureKind.LIST -> BluetoothBinaryCompositeDecoder.List(binaryDescriptor, decoder, serializersModule)
        is StructureKind.MAP -> BluetoothBinaryCompositeDecoder.Map(binaryDescriptor, decoder, serializersModule)
        else -> BluetoothBinaryCompositeDecoder.Class(binaryDescriptor, decoder.beginStructure(binaryDescriptor), serializersModule)
    }

    override fun decodeBoolean(): Boolean = binaryDescriptor.decodeBoolean(decoder)

    override fun decodeByte(): Byte = binaryDescriptor.decodeByteElement(decoder)

    override fun decodeChar(): Char = binaryDescriptor.decodeCharElement(decoder)

    override fun decodeDouble(): Double = binaryDescriptor.decodeDoubleElement(decoder)

    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int {
        val offset = if (binaryDescriptor.isNullable) 1 else 0
        return if (binaryDescriptor.bitIndex >= 0 && binaryDescriptor.bitWidth > offset) {
            // Packed into the flags as the ordinal, least-significant bit first.
            (0 until binaryDescriptor.bitWidth - offset).fold(0) { acc, bit ->
                if (decoder.flags[binaryDescriptor.bitIndex + offset + bit]) acc or (1 shl bit) else acc
            }
        } else {
            // Otherwise the enum is an (unsized) identifier in the body; check for the first match.
            binaryDescriptor.enumMap.firstNotNullOf { (key, value) -> key.takeIf { decoder.peekNextIs(value.array, true) } }
        }
    }

    override fun decodeFloat(): Float = binaryDescriptor.decodeFloatElement(decoder)

    override fun decodeInline(descriptor: SerialDescriptor): Decoder = this

    override fun decodeInt(): Int = binaryDescriptor.decodeIntElement(decoder)

    override fun decodeLong(): Long = binaryDescriptor.decodeLongElement(decoder)

    @ExperimentalSerializationApi
    override fun decodeNotNullMark(): Boolean = !binaryDescriptor.isNullable || decoder.flags[binaryDescriptor.bitIndex]

    @ExperimentalSerializationApi
    override fun decodeNull(): Nothing? = null

    override fun decodeShort(): Short = binaryDescriptor.decodeShortElement(decoder)

    override fun decodeString(): String = binaryDescriptor.decodeStringElement(decoder)
}

private sealed class BluetoothBinaryCompositeDecoder(protected val binaryDescriptor: BluetoothBinaryDescriptor, override val serializersModule: SerializersModule) :
    CompositeDecoder {

    class Class(binaryDescriptor: BluetoothBinaryDescriptor, private val decoder: BluetoothBinaryDescriptorDecoder, serializersModule: SerializersModule) :
        BluetoothBinaryCompositeDecoder(binaryDescriptor, serializersModule) {

        var currentIndex = 0

        override fun decodeElementIndex(descriptor: SerialDescriptor): Int = if (currentIndex < binaryDescriptor.children.size) {
            currentIndex++
        } else {
            DECODE_DONE
        }
        override fun binaryDescriptorAtIndex(index: Int): BluetoothBinaryDescriptor = binaryDescriptor.children[index]

        override fun endStructure(descriptor: SerialDescriptor) {
            decoder.endStructure()
        }

        override fun decoderAtIndex(index: Int): BluetoothBinaryDescriptorDecoder = decoder
    }

    sealed class Collection(binaryDescriptor: BluetoothBinaryDescriptor, private val decoder: BluetoothBinaryDescriptorDecoder, serializersModule: SerializersModule) :
        BluetoothBinaryCompositeDecoder(binaryDescriptor, serializersModule) {

        private val decoders = mutableMapOf<Int, BluetoothBinaryDescriptorDecoder>()
        private val collectionSettings = binaryDescriptor.collectionSettings ?: BluetoothBinaryDescriptor.CollectionSettings(
            BluetoothBinaryDescriptor.CollectionSettings.NumericLength(setOf(Length.`8_BIT`)),
            false,
        )
        private val expectedSize = if (!binaryDescriptor.isNullable || !collectionSettings.nullIfEmpty || decoder.flags[binaryDescriptor.bitIndex]) {
            // When not null, we can check whether a size is known
            when (val lengthMarking = collectionSettings.lengthMarking) {
                is BluetoothBinaryDescriptor.CollectionSettings.LengthPrefix -> {
                    // Length Prefix prefixes the length of the array
                    when (val endMarking = lengthMarking.endMarking) {
                        is StringEncodingSettings.LengthPrefix.ByteLength -> decoder.nextBytes(1)[0].toInt()

                        is StringEncodingSettings.LengthPrefix.ShortLength -> decoder.nextBytes(2).decodeUShort(0, binaryDescriptor.byteOrder).toInt()

                        is StringEncodingSettings.LengthPrefix.WithOverflow -> {
                            if (decoder.peekNextIs(byteArrayOf(endMarking.sentinel), true)) {
                                decoder.nextBytes(2).decodeUShort(0, binaryDescriptor.byteOrder).toInt()
                            } else {
                                decoder.nextBytes(1)[0].toInt()
                            }
                        }
                    }
                }

                is BluetoothBinaryDescriptor.CollectionSettings.NumericLength -> {
                    // Numeric Length encoded the length
                    binaryDescriptor.decodeNaturalNumericElement(decoder, BluetoothBinaryDescriptor.NumericSettings.Natural(lengthMarking.supportedLengths, false)).toInt()
                }

                // For NullTerminated or Unmarked, the length is unknown when decoding starts
                is BluetoothBinaryDescriptor.CollectionSettings.Unmarked -> -1

                is BluetoothBinaryDescriptor.CollectionSettings.NullMarked -> -1
            }
        } else {
            0
        }

        protected fun hasElementAtIndex(index: Int): Boolean = when {
            expectedSize >= 0 -> index < expectedSize
            collectionSettings.lengthMarking is BluetoothBinaryDescriptor.CollectionSettings.NullMarked -> !decoder.peekNextIs(byteArrayOf(0x00), true)
            else -> !decoder.isEmpty()
        }

        override fun decoderAtIndex(index: Int): BluetoothBinaryDescriptorDecoder = decoders.getOrPut(index) {
            val elementDescriptor = binaryDescriptorAtIndex(index)
            decoder.beginStructure(elementDescriptor, elementDescriptor.bitWidth)
        }

        override fun endStructure(descriptor: SerialDescriptor) {}
    }

    class List(binaryDescriptor: BluetoothBinaryDescriptor, decoder: BluetoothBinaryDescriptorDecoder, serializersModule: SerializersModule) :
        Collection(binaryDescriptor, decoder, serializersModule) {

        var currentIndex = 0

        override fun decodeElementIndex(descriptor: SerialDescriptor): Int = if (hasElementAtIndex(currentIndex)) {
            currentIndex++
        } else {
            DECODE_DONE
        }

        override fun binaryDescriptorAtIndex(index: Int): BluetoothBinaryDescriptor = binaryDescriptor.children.first()
    }

    class Map(binaryDescriptor: BluetoothBinaryDescriptor, decoder: BluetoothBinaryDescriptorDecoder, serializersModule: SerializersModule) :
        Collection(binaryDescriptor, decoder, serializersModule) {

        var currentIndex = 0

        override fun decodeElementIndex(descriptor: SerialDescriptor): Int = when {
            currentIndex % 2 == 0 && hasElementAtIndex(currentIndex / 2) -> currentIndex++
            currentIndex % 2 == 1 -> currentIndex++
            else -> DECODE_DONE
        }

        override fun binaryDescriptorAtIndex(index: Int): BluetoothBinaryDescriptor = binaryDescriptor.children[index % 2]
    }

    abstract fun binaryDescriptorAtIndex(index: Int): BluetoothBinaryDescriptor
    abstract fun decoderAtIndex(index: Int): BluetoothBinaryDescriptorDecoder

    override fun decodeBooleanElement(descriptor: SerialDescriptor, index: Int): Boolean = binaryDescriptorAtIndex(index).decodeBoolean(decoderAtIndex(index))

    override fun decodeByteElement(descriptor: SerialDescriptor, index: Int): Byte = binaryDescriptorAtIndex(index).decodeByteElement(decoderAtIndex(index))

    override fun decodeCharElement(descriptor: SerialDescriptor, index: Int): Char = binaryDescriptorAtIndex(index).decodeCharElement(decoderAtIndex(index))

    override fun decodeDoubleElement(descriptor: SerialDescriptor, index: Int): Double = binaryDescriptorAtIndex(index).decodeDoubleElement(decoderAtIndex(index))

    override fun decodeFloatElement(descriptor: SerialDescriptor, index: Int): Float = binaryDescriptorAtIndex(index).decodeFloatElement(decoderAtIndex(index))

    override fun decodeInlineElement(descriptor: SerialDescriptor, index: Int): Decoder =
        BluetoothBinaryDecoder(binaryDescriptorAtIndex(index), decoderAtIndex(index), serializersModule)

    override fun decodeIntElement(descriptor: SerialDescriptor, index: Int): Int = binaryDescriptorAtIndex(index).decodeIntElement(decoderAtIndex(index))

    override fun decodeLongElement(descriptor: SerialDescriptor, index: Int): Long = binaryDescriptorAtIndex(index).decodeLongElement(decoderAtIndex(index))

    @ExperimentalSerializationApi
    override fun <T : Any> decodeNullableSerializableElement(descriptor: SerialDescriptor, index: Int, deserializer: DeserializationStrategy<T?>, previousValue: T?): T? {
        val binaryDescriptor = binaryDescriptorAtIndex(index)
        return if (decoderAtIndex(index).flags[binaryDescriptor.bitIndex]) {
            decodeSerializableElement(descriptor, index, deserializer, previousValue)
        } else {
            null
        }
    }

    override fun <T> decodeSerializableElement(descriptor: SerialDescriptor, index: Int, deserializer: DeserializationStrategy<T>, previousValue: T?): T = when (descriptor.kind) {
        is PolymorphicKind.SEALED -> {
            val binaryDescriptor = binaryDescriptorAtIndex(index).children.first { binaryDescriptor ->
                binaryDescriptor.fieldName == deserializer.descriptor.serialName
            }
            BluetoothBinaryDecoder(binaryDescriptor, decoderAtIndex(index), serializersModule).decodeSerializableValue(deserializer)
        }

        is PolymorphicKind.OPEN -> {
            val binaryDescriptor = binaryDescriptor.children.first { binaryDescriptor -> binaryDescriptor.fieldName == deserializer.descriptor.serialName }
            BluetoothBinaryDecoder(binaryDescriptor, decoderAtIndex(index), serializersModule).decodeSerializableValue(deserializer)
        }

        else -> {
            BluetoothBinaryDecoder(binaryDescriptorAtIndex(index), decoderAtIndex(index), serializersModule).decodeSerializableValue(deserializer)
        }
    }

    override fun decodeShortElement(descriptor: SerialDescriptor, index: Int): Short = binaryDescriptorAtIndex(index).decodeShortElement(decoderAtIndex(index))

    override fun decodeStringElement(descriptor: SerialDescriptor, index: Int): String = if (descriptor.kind is PolymorphicKind && index == 0) {
        // For polymorphic classes the first element is its type string. Find its match in the polymorphicMap
        binaryDescriptor.polymorphicMap.firstNotNullOf { (key, value) ->
            val decoder = decoderAtIndex(index)
            key.takeIf { decoder.peekNextIs(value.array, true) }
        }
    } else {
        binaryDescriptorAtIndex(index).decodeStringElement(decoderAtIndex(index))
    }
}

internal fun BluetoothBinaryDescriptor.decodeBoolean(decoder: BluetoothBinaryDescriptorDecoder): Boolean {
    val offset = if (isNullable) 1 else 0
    // For boolean elements, if there is a flag available, we should encode to it, otherwise encode in the body
    return if (bitIndex >= 0 && bitWidth > offset) {
        decoder.flags[bitIndex + offset]
    } else {
        decoder.isNextBitSet()
    }
}

internal fun BluetoothBinaryDescriptor.decodeNaturalNumericElement(decoder: BluetoothBinaryDescriptorDecoder, settings: BluetoothBinaryDescriptor.NumericSettings.Natural): Long {
    val supportedLengths = settings.supportedLengths.toList()
    val expectedLength = when (supportedLengths.size) {
        0 -> throw IllegalArgumentException("Size should be set")

        1 -> supportedLengths.first()

        else -> {
            // For multiple sizes, check the flags for the preferred size
            val offset = if (isNullable) 1 else 0
            val lengthIndex = (0..<bitWidth - offset).fold(0) { acc, index ->
                if (decoder.flags[bitIndex + index + offset]) 2.0.pow(index).toInt() + acc else acc
            }
            supportedLengths[lengthIndex]
        }
    }
    val bytes = decoder.nextBytes(expectedLength.bytes)
    // Because we deal with flexible sizing and unsigned elements, always return a Long so no data will be lost
    return when (expectedLength) {
        Length.`8_BIT` -> if (settings.signed) bytes[0].toLong() else bytes[0].toUByte().toUShort().toShort().toLong()
        Length.`16_BIT` -> if (settings.signed) bytes.decodeShort(0, byteOrder).toLong() else bytes.decodeUShort(0, byteOrder).toUInt().toLong()
        Length.`24_BIT` -> if (settings.signed) bytes.decodeInt24(0, byteOrder).value.toLong() else bytes.decodeUInt24(0, byteOrder).value.toULong().toLong()
        Length.`32_BIT` -> if (settings.signed) bytes.decodeInt(0, byteOrder).toLong() else bytes.decodeUInt(0, byteOrder).toULong().toLong()
        Length.`64_BIT` -> if (settings.signed) bytes.decodeLong(0, byteOrder) else bytes.decodeULong(0, byteOrder).toLong()
    }
}

internal fun BluetoothBinaryDescriptor.decodeScalarNumericElement(decoder: BluetoothBinaryDescriptorDecoder, settings: BluetoothBinaryDescriptor.NumericSettings.Scalar): Double {
    val decoded = decodeNaturalNumericElement(decoder, BluetoothBinaryDescriptor.NumericSettings.Natural(settings.supportedLengths, settings.signed))
    val double = if (settings.signed) {
        decoded.toDouble()
    } else {
        decoded.toULong().toDouble()
    }
    return (double - settings.offset) / (settings.multiplier * 10.0.pow(settings.decimalExponent) * 2.0.pow(settings.binaryExponent))
}

internal fun BluetoothBinaryDescriptor.decodeDecimalNumericElement(decoder: BluetoothBinaryDescriptorDecoder, settings: BluetoothBinaryDescriptor.NumericSettings.Decimal): Double {
    val lengthToDecode = if (settings.supportedLengths.size > 1) {
        val flagIndex = bitIndex + if (isNullable) 1 else 0
        if (decoder.flags[flagIndex]) {
            Length.`64_BIT`
        } else {
            Length.`32_BIT`
        }
    } else {
        settings.supportedLengths.first()
    }
    val bytes = decoder.nextBytes(lengthToDecode.bytes)
    return when (lengthToDecode) {
        Length.`32_BIT` -> bytes.decodeFloat(0, byteOrder).toDouble()
        Length.`64_BIT` -> bytes.decodeDouble(0, byteOrder)
        else -> throw IllegalArgumentException("Decimal only supports 32 and 64 bit decoding")
    }
}

internal fun BluetoothBinaryDescriptor.decodeMedFloatNumericElement(
    decoder: BluetoothBinaryDescriptorDecoder,
    settings: BluetoothBinaryDescriptor.NumericSettings.MedFloat,
): Double {
    val lengthToDecode = if (settings.supportedLengths.size > 1) {
        val flagIndex = bitIndex + if (isNullable) 1 else 0
        if (decoder.flags[flagIndex]) {
            Length.`32_BIT`
        } else {
            Length.`16_BIT`
        }
    } else {
        settings.supportedLengths.first()
    }
    val bytes = decoder.nextBytes(lengthToDecode.bytes)
    return when (lengthToDecode) {
        Length.`16_BIT` -> bytes.decodeMedFloat16(0).value
        Length.`32_BIT` -> bytes.decodeMedFloat32(0).value
        else -> throw IllegalArgumentException("MedFloat only supports 16 and 32 bit decoding")
    }
}

internal fun BluetoothBinaryDescriptor.numericSettingsOrNaturalDefault(defaultLength: Length) =
    numericSettings ?: BluetoothBinaryDescriptor.NumericSettings.Natural(setOf(defaultLength), false)
internal fun BluetoothBinaryDescriptor.numericSettingsOrDecimalDefault(defaultLength: Length) =
    numericSettings ?: BluetoothBinaryDescriptor.NumericSettings.Decimal(setOf(defaultLength))

internal fun BluetoothBinaryDescriptor.decodeByteElement(decoder: BluetoothBinaryDescriptorDecoder) = numericSettingsOrNaturalDefault(Length.`8_BIT`).let { settings ->
    when (settings) {
        is BluetoothBinaryDescriptor.NumericSettings.Natural -> if (settings.signed) {
            decodeNaturalNumericElement(decoder, settings).toByte()
        } else {
            // Convert over ULong to keep higher values
            decodeNaturalNumericElement(decoder, settings).toULong().toUInt().toByte()
        }

        is BluetoothBinaryDescriptor.NumericSettings.Scalar -> decodeScalarNumericElement(decoder, settings).toInt().toByte()

        is BluetoothBinaryDescriptor.NumericSettings.Decimal -> decodeDecimalNumericElement(decoder, settings).toInt().toByte()

        is BluetoothBinaryDescriptor.NumericSettings.MedFloat -> decodeMedFloatNumericElement(decoder, settings).toInt().toByte()
    }
}

internal fun BluetoothBinaryDescriptor.decodeShortElement(decoder: BluetoothBinaryDescriptorDecoder) = numericSettingsOrNaturalDefault(Length.`16_BIT`).let { settings ->
    when (settings) {
        is BluetoothBinaryDescriptor.NumericSettings.Natural -> if (settings.signed) {
            decodeNaturalNumericElement(decoder, settings).toShort()
        } else {
            // Convert over ULong to keep higher values
            decodeNaturalNumericElement(decoder, settings).toULong().toUShort().toShort()
        }

        is BluetoothBinaryDescriptor.NumericSettings.Scalar -> decodeScalarNumericElement(decoder, settings).toInt().toShort()

        is BluetoothBinaryDescriptor.NumericSettings.Decimal -> decodeDecimalNumericElement(decoder, settings).toInt().toShort()

        is BluetoothBinaryDescriptor.NumericSettings.MedFloat -> decodeMedFloatNumericElement(decoder, settings).toInt().toShort()
    }
}

internal fun BluetoothBinaryDescriptor.decodeIntElement(decoder: BluetoothBinaryDescriptorDecoder) = numericSettingsOrNaturalDefault(Length.`32_BIT`).let { settings ->
    when (settings) {
        is BluetoothBinaryDescriptor.NumericSettings.Natural -> if (settings.signed) {
            decodeNaturalNumericElement(decoder, settings).toInt()
        } else {
            // Convert over ULong to keep higher values
            decodeNaturalNumericElement(decoder, settings).toULong().toUInt().toInt()
        }

        is BluetoothBinaryDescriptor.NumericSettings.Scalar -> decodeScalarNumericElement(decoder, settings).toInt()

        is BluetoothBinaryDescriptor.NumericSettings.Decimal -> decodeDecimalNumericElement(decoder, settings).toInt()

        is BluetoothBinaryDescriptor.NumericSettings.MedFloat -> decodeMedFloatNumericElement(decoder, settings).toInt()
    }
}

internal fun BluetoothBinaryDescriptor.decodeLongElement(decoder: BluetoothBinaryDescriptorDecoder) = numericSettingsOrNaturalDefault(Length.`64_BIT`).let { settings ->
    when (settings) {
        is BluetoothBinaryDescriptor.NumericSettings.Natural -> decodeNaturalNumericElement(decoder, settings)
        is BluetoothBinaryDescriptor.NumericSettings.Scalar -> decodeScalarNumericElement(decoder, settings).toLong()
        is BluetoothBinaryDescriptor.NumericSettings.Decimal -> decodeDecimalNumericElement(decoder, settings).toLong()
        is BluetoothBinaryDescriptor.NumericSettings.MedFloat -> decodeMedFloatNumericElement(decoder, settings).toLong()
    }
}

internal fun BluetoothBinaryDescriptor.decodeFloatElement(decoder: BluetoothBinaryDescriptorDecoder) = numericSettingsOrDecimalDefault(Length.`32_BIT`).let { settings ->
    when (settings) {
        is BluetoothBinaryDescriptor.NumericSettings.Natural -> decodeNaturalNumericElement(decoder, settings).toDouble().toFloat()
        is BluetoothBinaryDescriptor.NumericSettings.Scalar -> decodeScalarNumericElement(decoder, settings).toFloat()
        is BluetoothBinaryDescriptor.NumericSettings.Decimal -> decodeDecimalNumericElement(decoder, settings).toFloat()
        is BluetoothBinaryDescriptor.NumericSettings.MedFloat -> decodeMedFloatNumericElement(decoder, settings).toFloat()
    }
}

internal fun BluetoothBinaryDescriptor.decodeDoubleElement(decoder: BluetoothBinaryDescriptorDecoder) = numericSettingsOrDecimalDefault(Length.`64_BIT`).let { settings ->
    when (settings) {
        is BluetoothBinaryDescriptor.NumericSettings.Natural -> decodeNaturalNumericElement(decoder, settings).toDouble()
        is BluetoothBinaryDescriptor.NumericSettings.Scalar -> decodeScalarNumericElement(decoder, settings)
        is BluetoothBinaryDescriptor.NumericSettings.Decimal -> decodeDecimalNumericElement(decoder, settings)
        is BluetoothBinaryDescriptor.NumericSettings.MedFloat -> decodeMedFloatNumericElement(decoder, settings)
    }
}

internal fun BluetoothBinaryDescriptor.decodeCharElement(decoder: BluetoothBinaryDescriptorDecoder): Char = when (stringSettings?.encoding ?: Encoding.UTF_8) {
    Encoding.UTF_8 -> decoder.nextBytes(1).first().decodeUTF8Char()
    Encoding.UTF_16 -> decoder.nextBytes(2).decodeUTF16Char(0, byteOrder)
    Encoding.ASCII -> decoder.nextBytes(1).first().decodeAsciiChar()
}

internal fun BluetoothBinaryDescriptor.decodeStringElement(decoder: BluetoothBinaryDescriptorDecoder): String {
    val settings = stringSettings?.let {
        StringEncodingSettings(it.endMarking, it.encoding)
    } ?: StringEncodingSettings(StringEncodingSettings.LengthPrefix.ByteLength, Encoding.UTF_8)
    val next = {
        // Decode until the decoder is empty.
        // Since the sequence will terminate automatically if its known length has been consumed, the empty check will only kick in for arrays too small or for unsized strings
        if (!decoder.isEmpty()) {
            decoder.nextBytes(1)[0]
        } else {
            null
        }
    }
    val stringSequence = generateSequence(next) {
        next()
    }
    // Decode String. Since the sequence would simply terminate if decoder is empty, any unexpected data will be thrown by ByteArray.decodeString
    // Treat these exceptions as a ByteArrayEndedBeforeSerializationCompleted
    return try {
        stringSequence.decodeString(settings)
    } catch (e: IllegalArgumentException) {
        throw ByteArrayEndedBeforeSerializationCompleted(e.message.orEmpty())
    } catch (e: NoSuchElementException) {
        throw ByteArrayEndedBeforeSerializationCompleted(e.message.orEmpty())
    }
}
