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
import com.splendo.kaluga.base.bytes.decodeUTF16Char
import com.splendo.kaluga.base.bytes.decodeUTF8Char
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.modules.SerializersModule
import kotlin.math.pow

internal fun BluetoothBinaryDescriptor.decodeBoolean(decoder: BluetoothBinaryDescriptorDecoder): Boolean {
    val offset = if (isNullable) 1 else 0
    return if (bitIndex >= 0 && bitWidth > offset) {
        decoder.flags[bitIndex + offset]
    } else {
        decoder.isNextBitSet()
    }
}

internal fun BluetoothBinaryDescriptor.decodeNumericElement(decoder: BluetoothBinaryDescriptorDecoder, settings: BluetoothBinaryDescriptor.NumericSettings): Number =
    when (settings) {
        is BluetoothBinaryDescriptor.NumericSettings.Natural -> {
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
            when (expectedLength) {
                Length.`8_BIT` -> bytes[0]
                Length.`16_BIT` -> bytes.decodeShort(0, byteOrder)
                Length.`24_BIT` -> bytes.decodeInt24(0, byteOrder).value
                Length.`32_BIT` -> bytes.decodeInt(0, byteOrder)
                Length.`64_BIT` -> bytes.decodeLong(0, byteOrder)
            }
        }
        is BluetoothBinaryDescriptor.NumericSettings.Scalar -> {
            val decoded = decodeNumericElement(decoder, BluetoothBinaryDescriptor.NumericSettings.Natural(settings.supportedLengths, settings.signed))
            val double = if (settings.signed) {
                decoded.toDouble()
            } else {
                decoded.toLong().toULong().toDouble()
            }
            (double - settings.offset) / (settings.multiplier * 10.0.pow(settings.decimalExponent) * 2.0.pow(settings.binaryExponent))
        }
        is BluetoothBinaryDescriptor.NumericSettings.Decimal -> {
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
            when (lengthToDecode) {
                Length.`32_BIT` -> bytes.decodeFloat(0, byteOrder)
                Length.`64_BIT` -> bytes.decodeDouble(0, byteOrder)
                else -> throw IllegalArgumentException("Decimal only supports 16 and 32 bit decoding")
            }
        }
        is BluetoothBinaryDescriptor.NumericSettings.MedFloat -> {
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
            when (lengthToDecode) {
                Length.`16_BIT` -> bytes.decodeMedFloat16(0).value
                Length.`32_BIT` -> bytes.decodeMedFloat32(0).value
                else -> throw IllegalArgumentException("MedFloat only supports 16 and 32 bit decoding")
            }
        }
    }

internal fun BluetoothBinaryDescriptor.decodeByteElement(decoder: BluetoothBinaryDescriptorDecoder) =
    decodeNumericElement(decoder, numericSettings ?: BluetoothBinaryDescriptor.NumericSettings.Natural(setOf(Length.`8_BIT`), false)).toByte()

internal fun BluetoothBinaryDescriptor.decodeShortElement(decoder: BluetoothBinaryDescriptorDecoder) = decodeNumericElement(
    decoder,
    numericSettings ?: BluetoothBinaryDescriptor.NumericSettings.Natural(setOf(Length.`16_BIT`), false),
).toShort()

internal fun BluetoothBinaryDescriptor.decodeIntElement(decoder: BluetoothBinaryDescriptorDecoder) = decodeNumericElement(
    decoder,
    numericSettings ?: BluetoothBinaryDescriptor.NumericSettings.Natural(setOf(Length.`32_BIT`), false),
).toInt()

internal fun BluetoothBinaryDescriptor.decodeLongElement(decoder: BluetoothBinaryDescriptorDecoder) = decodeNumericElement(
    decoder,
    numericSettings ?: BluetoothBinaryDescriptor.NumericSettings.Natural(setOf(Length.`64_BIT`), false),
).toLong()

internal fun BluetoothBinaryDescriptor.decodeFloatElement(decoder: BluetoothBinaryDescriptorDecoder) = decodeNumericElement(
    decoder,
    numericSettings ?: BluetoothBinaryDescriptor.NumericSettings.Decimal(setOf(Length.`32_BIT`)),
).toFloat()

internal fun BluetoothBinaryDescriptor.decodeDoubleElement(decoder: BluetoothBinaryDescriptorDecoder) = decodeNumericElement(
    decoder,
    numericSettings ?: BluetoothBinaryDescriptor.NumericSettings.Decimal(setOf(Length.`64_BIT`)),
).toDouble()

internal fun BluetoothBinaryDescriptor.decodeCharElement(decoder: BluetoothBinaryDescriptorDecoder): Char =
    when (stringSettings?.encoding ?: Encoding.UTF_8) {
        Encoding.UTF_8 -> decoder.nextBytes(1).first().decodeUTF8Char()
        Encoding.UTF_16 -> decoder.nextBytes(2).decodeUTF16Char(0, byteOrder)
        Encoding.ASCII -> decoder.nextBytes(1).first().decodeAsciiChar()
    }

internal fun BluetoothBinaryDescriptor.decodeStringElement(decoder: BluetoothBinaryDescriptorDecoder): String {
    val settings = stringSettings?.let {
        StringEncodingSettings(it.endMarking, it.encoding)
    } ?: StringEncodingSettings(StringEncodingSettings.LengthPrefix(), Encoding.UTF_8)
    val stringSequence = generateSequence {
        if (!decoder.isEmpty()) {
            decoder.nextBytes(1)[0]
        } else {
            null
        }
    }
    return stringSequence.decodeString(settings)
}

internal class BluetoothBinaryDecoder(
    private val entry: BluetoothBinaryDescriptor,
    private val decoder: BluetoothBinaryDescriptorDecoder,
    override val serializersModule: SerializersModule,
) : Decoder {

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        TODO("Not yet implemented")
    }

    override fun decodeBoolean(): Boolean = entry.decodeBoolean(decoder)

    override fun decodeByte(): Byte = entry.decodeByteElement(decoder)

    override fun decodeChar(): Char = entry.decodeCharElement(decoder)

    override fun decodeDouble(): Double = entry.decodeDoubleElement(decoder)

    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int {
        TODO("Not yet implemented")
    }

    override fun decodeFloat(): Float = entry.decodeFloatElement(decoder)

    override fun decodeInline(descriptor: SerialDescriptor): Decoder = this

    override fun decodeInt(): Int = entry.decodeIntElement(decoder)

    override fun decodeLong(): Long = entry.decodeLongElement(decoder)

    @ExperimentalSerializationApi
    override fun decodeNotNullMark(): Boolean = !entry.isNullable || decoder.flags[entry.bitIndex]

    @ExperimentalSerializationApi
    override fun decodeNull(): Nothing? = null

    override fun decodeShort(): Short = entry.decodeShortElement(decoder)

    override fun decodeString(): String = entry.decodeStringElement(decoder)
}
