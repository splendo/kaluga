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

package com.splendo.kaluga.bytes.layout

import com.splendo.kaluga.bytes.ByteOrder
import com.splendo.kaluga.bytes.integer.Int24
import com.splendo.kaluga.bytes.integer.Int40
import com.splendo.kaluga.bytes.integer.Int48
import com.splendo.kaluga.bytes.medfloat.MedFloat16
import com.splendo.kaluga.bytes.integer.UInt24
import com.splendo.kaluga.bytes.integer.UInt40
import com.splendo.kaluga.bytes.integer.UInt48
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.copyIntoByteArray
import com.splendo.kaluga.bytes.integer.copyIntoByteArray
import com.splendo.kaluga.bytes.decodeInt
import com.splendo.kaluga.bytes.integer.decodeInt24
import com.splendo.kaluga.bytes.integer.decodeInt40
import com.splendo.kaluga.bytes.integer.decodeInt48
import com.splendo.kaluga.bytes.decodeLong
import com.splendo.kaluga.bytes.decodeShort
import com.splendo.kaluga.bytes.decodeUInt
import com.splendo.kaluga.bytes.integer.decodeUInt24
import com.splendo.kaluga.bytes.integer.decodeUInt40
import com.splendo.kaluga.bytes.integer.decodeUInt48
import com.splendo.kaluga.bytes.decodeULong
import com.splendo.kaluga.bytes.decodeUShort
import com.splendo.kaluga.bytes.integer.toInt24
import com.splendo.kaluga.bytes.integer.toInt40
import com.splendo.kaluga.bytes.integer.toInt48
import com.splendo.kaluga.bytes.integer.toUInt24
import com.splendo.kaluga.bytes.integer.toUInt40
import com.splendo.kaluga.bytes.integer.toUInt48
import kotlin.math.pow
import kotlin.math.round

/**
 * Whether [number] can be encoded in this [Length] with the given signedness.
 */
@InternalBytesApi
fun Length.fits(number: Number, signed: Boolean): Boolean = when (this) {
    // Widen through Long, never Int: a Long outside Int range would otherwise truncate to a value that
    // wrongly looks like it fits (e.g. 1L shl 32 truncates to 0).
    Length.`8_BIT` -> if (signed) {
        number.toLong() in Byte.MIN_VALUE.toLong()..Byte.MAX_VALUE.toLong()
    } else {
        number.toLong().toULong() in UByte.MIN_VALUE.toULong()..UByte.MAX_VALUE.toULong()
    }

    Length.`16_BIT` -> if (signed) {
        number.toLong() in Short.MIN_VALUE.toLong()..Short.MAX_VALUE.toLong()
    } else {
        number.toLong().toULong() in UShort.MIN_VALUE.toULong()..UShort.MAX_VALUE.toULong()
    }

    Length.`24_BIT` -> if (signed) {
        number.toLong() in Int24.MIN_VALUE.value.toLong()..Int24.MAX_VALUE.value.toLong()
    } else {
        number.toLong().toULong() in UInt24.MIN_VALUE.value.toULong()..UInt24.MAX_VALUE.value.toULong()
    }

    Length.`32_BIT` -> if (signed) {
        number.toLong() in Int.MIN_VALUE.toLong()..Int.MAX_VALUE.toLong()
    } else {
        number.toLong().toULong() in UInt.MIN_VALUE.toULong()..UInt.MAX_VALUE.toULong()
    }

    Length.`40_BIT` -> if (signed) {
        number.toLong() in Int40.MIN_VALUE.value..Int40.MAX_VALUE.value
    } else {
        number.toLong().toULong() in UInt40.MIN_VALUE.value..UInt40.MAX_VALUE.value
    }

    Length.`48_BIT` -> if (signed) {
        number.toLong() in Int48.MIN_VALUE.value..Int48.MAX_VALUE.value
    } else {
        number.toLong().toULong() in UInt48.MIN_VALUE.value..UInt48.MAX_VALUE.value
    }

    Length.`64_BIT` -> true
}

/** [fits] for an integral [number], without boxing it. */
@InternalBytesApi
fun Length.fits(number: Long, signed: Boolean): Boolean = when (this) {
    Length.`8_BIT` -> if (signed) number in Byte.MIN_VALUE.toLong()..Byte.MAX_VALUE.toLong() else number.toULong() <= UByte.MAX_VALUE.toULong()
    Length.`16_BIT` -> if (signed) number in Short.MIN_VALUE.toLong()..Short.MAX_VALUE.toLong() else number.toULong() <= UShort.MAX_VALUE.toULong()
    Length.`24_BIT` -> if (signed) number in Int24.MIN_VALUE.value.toLong()..Int24.MAX_VALUE.value.toLong() else number.toULong() <= UInt24.MAX_VALUE.value.toULong()
    Length.`32_BIT` -> if (signed) number in Int.MIN_VALUE.toLong()..Int.MAX_VALUE.toLong() else number.toULong() <= UInt.MAX_VALUE.toULong()
    Length.`40_BIT` -> if (signed) number in Int40.MIN_VALUE.value..Int40.MAX_VALUE.value else number.toULong() <= UInt40.MAX_VALUE.value
    Length.`48_BIT` -> if (signed) number in Int48.MIN_VALUE.value..Int48.MAX_VALUE.value else number.toULong() <= UInt48.MAX_VALUE.value
    Length.`64_BIT` -> true
}

/**
 * The numeric math of the wire format. Every implementation of a layout must go through these functions to produce
 * the same bytes on every platform.
 */
@InternalBytesApi
object NumericCodec {

    /**
     * Picks the [Length] to encode [value] with: the first of [supportedLengths] (in order) that fits, or the last one.
     * The index is what gets written into the size-selection flag bits.
     */
    fun selectLength(supportedLengths: Set<Length>, value: Number, signed: Boolean): IndexedValue<Length> =
        supportedLengths.withIndex().firstOrNull { (_, length) -> length.fits(value, signed) } ?: supportedLengths.withIndex().last()

    /** The index into [supportedLengths] of the length [selectLength] picks for an integral [value]. */
    fun selectLengthIndex(supportedLengths: Array<Length>, value: Long, signed: Boolean): Int {
        for (index in supportedLengths.indices) {
            if (supportedLengths[index].fits(value, signed)) return index
        }
        return supportedLengths.lastIndex
    }

    /** The index into [supportedLengths] of the length [selectLength] picks for a floating point [value]. */
    fun selectLengthIndex(supportedLengths: Array<Length>, value: Double, signed: Boolean): Int {
        for (index in supportedLengths.indices) {
            if (supportedLengths[index].fits(value, signed)) return index
        }
        return supportedLengths.lastIndex
    }

    /** Whether [value] fits in [bits] bits with the given signedness, so a flag-packed numeric can't silently truncate. */
    fun fitsInBits(value: Long, bits: Int, signed: Boolean): Boolean = when {
        bits >= Long.SIZE_BITS -> true
        signed -> value in -(1L shl (bits - 1)) until (1L shl (bits - 1))
        else -> value in 0L until (1L shl bits)
    }

    /** Sign-extends a [bits]-wide flag-packed value when [signed] and its top bit is set. */
    fun signExtend(raw: Long, bits: Int, signed: Boolean): Long = if (signed && bits < Long.SIZE_BITS && (raw shr (bits - 1)) and 1L == 1L) {
        raw or (-1L shl bits)
    } else {
        raw
    }

    /**
     * Whether [value] is represented losslessly by a 32-bit float.
     *
     * Kotlin/JS has no true 32-bit Float, so `toFloat()` does not round to 32-bit precision there and a naive
     * `this == toFloat()` would always be true. Round-tripping through the raw bits obtains the genuine 32-bit value,
     * so the 32-bit-vs-64-bit sizing decision is identical on every platform.
     */
    fun fitsLosslesslyIn32BitFloat(value: Number): Boolean = value.toDouble() == Float.fromBits(value.toFloat().toRawBits()).toDouble()

    /** The [Length] a [NumericSettings.Decimal] with both 32 and 64 bit sizes picks for [value]. */
    fun decimalLength(value: Number): Length = if (fitsLosslesslyIn32BitFloat(value)) Length.`32_BIT` else Length.`64_BIT`

    /** The [Length] a [NumericSettings.MedFloat] with both 16 and 32 bit sizes picks for [value]. */
    fun medFloatLength(value: Double): Length = if (MedFloat16.canRepresent(value)) Length.`16_BIT` else Length.`32_BIT`

    /**
     * The integral wire value of a [NumericSettings.Scalar].
     *
     * Rounds rather than truncates, so sub-ULP floating-point noise (which differs per platform: Kotlin/JS has no
     * true 32-bit Float and `pow` can differ by a ULP) can't tip a boundary value to the wrong integer.
     */
    fun scalarToWire(settings: NumericSettings.Scalar, value: Number): Double =
        round(settings.multiplier * value.toDouble() * 10.0.pow(settings.decimalExponent) * 2.0.pow(settings.binaryExponent) + settings.offset)

    /** The value of a [NumericSettings.Scalar] from its decoded wire value [raw]. */
    fun scalarFromWire(settings: NumericSettings.Scalar, raw: Long): Double {
        val double = if (settings.signed) {
            raw.toDouble()
        } else {
            raw.toULong().toDouble()
        }
        return (double - settings.offset) / (settings.multiplier * 10.0.pow(settings.decimalExponent) * 2.0.pow(settings.binaryExponent))
    }

    /** The unsigned wire value of a [NumericSettings.RangeEncoded]. */
    fun rangeToWire(settings: NumericSettings.RangeEncoded, value: Number): Double {
        val ratio = (value.toDouble() - settings.min) / (settings.max - settings.min)
        return round(ratio * settings.maxWireValue.toDouble())
    }

    /** The value of a [NumericSettings.RangeEncoded] from its decoded wire value [raw]. */
    fun rangeFromWire(settings: NumericSettings.RangeEncoded, raw: Long): Double =
        raw.toULong().toDouble() / settings.maxWireValue.toDouble() * (settings.max - settings.min) + settings.min

    /**
     * Reads a natural number of [length] from [bytes] at [offset]. Unsigned values are widened, so a Long is always
     * returned without losing data.
     */
    fun readNatural(bytes: ByteArray, offset: Int, length: Length, signed: Boolean, byteOrder: ByteOrder): Long = when (length) {
        Length.`8_BIT` -> if (signed) bytes[offset].toLong() else bytes[offset].toUByte().toUShort().toShort().toLong()
        Length.`16_BIT` -> if (signed) bytes.decodeShort(offset, byteOrder).toLong() else bytes.decodeUShort(offset, byteOrder).toUInt().toLong()
        Length.`24_BIT` -> if (signed) bytes.decodeInt24(offset, byteOrder).value.toLong() else bytes.decodeUInt24(offset, byteOrder).value.toULong().toLong()
        Length.`32_BIT` -> if (signed) bytes.decodeInt(offset, byteOrder).toLong() else bytes.decodeUInt(offset, byteOrder).toULong().toLong()
        Length.`40_BIT` -> if (signed) bytes.decodeInt40(offset, byteOrder).value else bytes.decodeUInt40(offset, byteOrder).value.toLong()
        Length.`48_BIT` -> if (signed) bytes.decodeInt48(offset, byteOrder).value else bytes.decodeUInt48(offset, byteOrder).value.toLong()
        Length.`64_BIT` -> if (signed) bytes.decodeLong(offset, byteOrder) else bytes.decodeULong(offset, byteOrder).toLong()
    }

    /**
     * Writes [value] as a natural number of [length] into [bytes] at [offset]. Out-of-range values are truncated to
     * [length] the way `Number.toByte()`/`toShort()`/`toInt()`/`toLong()` truncate them, except that 24, 40 and 48 bit
     * signed values must fit (their constructors reject anything else). A floating point [value] (the wire value of a
     * scalar or range encoding) is converted with the saturating `Double.toInt()`/`toLong()`, or `toUInt()`/`toULong()`
     * when unsigned.
     */
    fun writeNatural(bytes: ByteArray, offset: Int, value: Number, length: Length, signed: Boolean, byteOrder: ByteOrder) {
        if (value is Double || value is Float) {
            writeNatural(bytes, offset, value.toDouble(), length, signed, byteOrder)
        } else {
            writeNatural(bytes, offset, value.toLong(), length, signed, byteOrder)
        }
    }

    /** [writeNatural] for an integral [value]. */
    fun writeNatural(bytes: ByteArray, offset: Int, value: Long, length: Length, signed: Boolean, byteOrder: ByteOrder) {
        writeNatural(bytes, offset, value.toInt(), value, length, signed, byteOrder)
    }

    /** [writeNatural] for a floating point [value]. */
    fun writeNatural(bytes: ByteArray, offset: Int, value: Double, length: Length, signed: Boolean, byteOrder: ByteOrder) {
        if (signed) {
            writeNatural(bytes, offset, value.toInt(), value.toLong(), length, true, byteOrder)
        } else {
            writeNatural(bytes, offset, value.toUInt().toInt(), value.toULong().toLong(), length, false, byteOrder)
        }
    }

    private fun writeNatural(bytes: ByteArray, offset: Int, asInt: Int, asLong: Long, length: Length, signed: Boolean, byteOrder: ByteOrder) {
        when (length) {
            Length.`8_BIT` -> bytes[offset] = asInt.toByte()
            Length.`16_BIT` -> asInt.toShort().copyIntoByteArray(bytes, offset, byteOrder)
            Length.`24_BIT` -> if (signed) asInt.toInt24().copyIntoByteArray(bytes, offset, byteOrder) else asInt.toUInt().toUInt24().copyIntoByteArray(bytes, offset, byteOrder)
            Length.`32_BIT` -> asInt.copyIntoByteArray(bytes, offset, byteOrder)
            Length.`40_BIT` -> if (signed) asLong.toInt40().copyIntoByteArray(bytes, offset, byteOrder) else asLong.toULong().toUInt40().copyIntoByteArray(bytes, offset, byteOrder)
            Length.`48_BIT` -> if (signed) asLong.toInt48().copyIntoByteArray(bytes, offset, byteOrder) else asLong.toULong().toUInt48().copyIntoByteArray(bytes, offset, byteOrder)
            Length.`64_BIT` -> asLong.copyIntoByteArray(bytes, offset, byteOrder)
        }
    }
}
