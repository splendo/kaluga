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
import com.splendo.kaluga.base.bytes.toByteArray
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

internal class BluetoothBinaryDecoder(
    private val binaryDescriptor: BluetoothBinaryDescriptor,
    private val decoder: BluetoothBinaryDescriptorDecoder,
    override val serializersModule: SerializersModule,
) : Decoder {

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        val subDecoder = decoder.beginStructure(binaryDescriptor)
        return when (descriptor.kind) {
            is StructureKind.LIST -> TODO()
            is StructureKind.MAP -> TODO()
            else -> BluetoothBinaryCompositeDecoder.Class(binaryDescriptor, subDecoder, serializersModule)
        }
    }

    override fun decodeBoolean(): Boolean = binaryDescriptor.decodeBoolean(decoder)

    override fun decodeByte(): Byte = binaryDescriptor.decodeByteElement(decoder)

    override fun decodeChar(): Char = binaryDescriptor.decodeCharElement(decoder)

    override fun decodeDouble(): Double = binaryDescriptor.decodeDoubleElement(decoder)

    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int = if ((0 until enumDescriptor.elementsCount).all {
            enumDescriptor.getElementAnnotations(it).filterIsInstance<SerializedByteValue>().isNotEmpty()
        }
    ) {
        val byteValue = decoder.nextBytes(1).first()
        (0 until enumDescriptor.elementsCount).first { index ->
            enumDescriptor.getElementAnnotations(index).filterIsInstance<SerializedByteValue>().first().value == byteValue
        }
    } else {
        (0 until enumDescriptor.elementsCount).first { index ->
            val encodedName = enumDescriptor.getElementName(index).toByteArray(StringEncodingSettings(StringEncodingSettings.NoMarking, Encoding.UTF_8), binaryDescriptor.byteOrder)
            decoder.peekNextIs(encodedName)
        }.also {
            decoder.nextBytes(
                enumDescriptor.getElementName(it).toByteArray(StringEncodingSettings(StringEncodingSettings.NoMarking, Encoding.UTF_8), binaryDescriptor.byteOrder).size,
            )
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

private sealed class BluetoothBinaryCompositeDecoder(
    protected val binaryDescriptor: BluetoothBinaryDescriptor,
    protected val decoder: BluetoothBinaryDescriptorDecoder,
    override val serializersModule: SerializersModule,
) : CompositeDecoder {

    class Class(binaryDescriptor: BluetoothBinaryDescriptor, decoder: BluetoothBinaryDescriptorDecoder, serializersModule: SerializersModule) :
        BluetoothBinaryCompositeDecoder(binaryDescriptor, decoder, serializersModule) {

        var currentIndex = 0

        override fun decodeElementIndex(descriptor: SerialDescriptor): Int = if (currentIndex < binaryDescriptor.children.size) {
            currentIndex++
        } else {
            DECODE_DONE
        }
        override fun binaryDescriptorAtIndex(index: Int): BluetoothBinaryDescriptor = binaryDescriptor.children[index]
    }

    abstract fun binaryDescriptorAtIndex(index: Int): BluetoothBinaryDescriptor

    override fun decodeBooleanElement(descriptor: SerialDescriptor, index: Int): Boolean = binaryDescriptorAtIndex(index).decodeBoolean(decoder)

    override fun decodeByteElement(descriptor: SerialDescriptor, index: Int): Byte = binaryDescriptorAtIndex(index).decodeByteElement(decoder)

    override fun decodeCharElement(descriptor: SerialDescriptor, index: Int): Char = binaryDescriptorAtIndex(index).decodeCharElement(decoder)

    override fun decodeDoubleElement(descriptor: SerialDescriptor, index: Int): Double = binaryDescriptorAtIndex(index).decodeDoubleElement(decoder)

    override fun decodeFloatElement(descriptor: SerialDescriptor, index: Int): Float = binaryDescriptorAtIndex(index).decodeFloatElement(decoder)

    override fun decodeInlineElement(descriptor: SerialDescriptor, index: Int): Decoder = BluetoothBinaryDecoder(binaryDescriptorAtIndex(index), decoder, serializersModule)

    override fun decodeIntElement(descriptor: SerialDescriptor, index: Int): Int = binaryDescriptorAtIndex(index).decodeIntElement(decoder)

    override fun decodeLongElement(descriptor: SerialDescriptor, index: Int): Long = binaryDescriptorAtIndex(index).decodeLongElement(decoder)

    @ExperimentalSerializationApi
    override fun <T : Any> decodeNullableSerializableElement(descriptor: SerialDescriptor, index: Int, deserializer: DeserializationStrategy<T?>, previousValue: T?): T? {
        val binaryDescriptor = binaryDescriptorAtIndex(index)
        return if (decoder.flags[binaryDescriptor.bitIndex]) {
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
            BluetoothBinaryDecoder(binaryDescriptor, decoder, serializersModule).decodeSerializableValue(deserializer)
        }
        is PolymorphicKind.OPEN -> {
            val binaryDescriptor = binaryDescriptor.children.first { binaryDescriptor -> binaryDescriptor.fieldName == deserializer.descriptor.serialName }
            BluetoothBinaryDecoder(binaryDescriptor, decoder, serializersModule).decodeSerializableValue(deserializer)
        }
        else -> {
            BluetoothBinaryDecoder(binaryDescriptorAtIndex(index), decoder, serializersModule).decodeSerializableValue(deserializer)
        }
    }

    override fun decodeShortElement(descriptor: SerialDescriptor, index: Int): Short = binaryDescriptorAtIndex(index).decodeShortElement(decoder)

    override fun decodeStringElement(descriptor: SerialDescriptor, index: Int): String = if (descriptor.kind is PolymorphicKind && index == 0) {
        TODO()
    } else {
        binaryDescriptorAtIndex(index).decodeStringElement(decoder)
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        decoder.endStructure()
    }
}

internal fun BluetoothBinaryDescriptor.decodeBoolean(decoder: BluetoothBinaryDescriptorDecoder): Boolean {
    val offset = if (isNullable) 1 else 0
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
            val offset = if (isNullable) 1 else 0
            val lengthIndex = (0..<bitWidth - offset).fold(0) { acc, index ->
                if (decoder.flags[bitIndex + index + offset]) 2.0.pow(index).toInt() + acc else acc
            }
            supportedLengths[lengthIndex]
        }
    }
    val bytes = decoder.nextBytes(expectedLength.bytes)
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
        else -> throw IllegalArgumentException("Decimal only supports 16 and 32 bit decoding")
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
    } ?: StringEncodingSettings(StringEncodingSettings.LengthPrefix(), Encoding.UTF_8)
    val next = {
        if (!decoder.isEmpty()) {
            decoder.nextBytes(1)[0]
        } else {
            null
        }
    }
    val stringSequence = generateSequence(next) {
        next()
    }
    return stringSequence.decodeString(settings)
}
