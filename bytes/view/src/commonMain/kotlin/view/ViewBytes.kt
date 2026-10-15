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

package com.splendo.kaluga.bytes.view

import com.splendo.kaluga.bytes.layout.InternalBytesApi
import com.splendo.kaluga.bytes.ByteOrder
import com.splendo.kaluga.bytes.strings.Encoding
import com.splendo.kaluga.bytes.medfloat.MedFloat16
import com.splendo.kaluga.bytes.medfloat.copyIntoByteArray
import com.splendo.kaluga.bytes.medfloat.MedFloat32
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.copyIntoByteArray
import com.splendo.kaluga.bytes.strings.copyUTF16IntoByteArray
import com.splendo.kaluga.bytes.strings.decodeAsciiChar
import com.splendo.kaluga.bytes.decodeDouble
import com.splendo.kaluga.bytes.decodeFloat
import com.splendo.kaluga.bytes.medfloat.decodeMedFloat16
import com.splendo.kaluga.bytes.medfloat.decodeMedFloat32
import com.splendo.kaluga.bytes.strings.decodeUTF16Char
import com.splendo.kaluga.bytes.strings.decodeUTF8Char
import com.splendo.kaluga.bytes.strings.encodeChar
import com.splendo.kaluga.bytes.layout.ByteArrayEndedBeforeSerializationCompleted
import com.splendo.kaluga.bytes.layout.NumericCodec
import com.splendo.kaluga.bytes.strings.toAscii
import com.splendo.kaluga.bytes.toHexString
import kotlinx.serialization.SerializationException

/**
 * The bounds-checked byte access generated views are built on.
 *
 * Every accessor takes the backing array, a position and `lim`, the exclusive end of the region the view may read or
 * write. Accessing past `lim` (or before the start of the array) throws [ByteArrayEndedBeforeSerializationCompleted],
 * the exception `BytesFormat` throws for a truncated input. The checks are explicit because Kotlin/JS typed arrays do
 * not throw on out-of-range access.
 *
 * Bit positions count from the least significant bit of the byte at `s`, continuing into the following bytes, which
 * is how `BytesFormat` lays out flag headers and body Booleans.
 */
@InternalBytesApi
object ViewBytes {

    /** Throws [ByteArrayEndedBeforeSerializationCompleted] unless the [width] bytes at [pos] lie within `[0, lim)`. */
    fun checkRange(pos: Int, width: Int, lim: Int) {
        if (pos < 0 || width < 0 || pos > lim - width) {
            throw ByteArrayEndedBeforeSerializationCompleted("Not enough bytes remaining: $width bytes at $pos exceed $lim")
        }
    }

    /**
     * Throws [IllegalArgumentException] unless `[from, to)` is a range of [bytes].
     * Used by generated `wrapRange` functions.
     */
    fun checkWrapRange(bytes: ByteArray, from: Int, to: Int) {
        require(from in 0..to && to <= bytes.size) { "Range [$from, $to) is not within an array of ${bytes.size} bytes" }
    }

    /** [bits] rounded up to a whole byte, where the encoder starts anything it writes as bytes. */
    fun alignBits(bits: Int): Int = (bits + Byte.SIZE_BITS - 1) / Byte.SIZE_BITS * Byte.SIZE_BITS

    /**
     * The entry of [lengths] the size-selection flag bits pick with [index].
     * @throws IndexOutOfBoundsException, as `BytesFormat` does, when there is no such entry.
     */
    fun selectedLength(lengths: Array<Length>, index: Int): Length {
        if (index !in lengths.indices) throw IndexOutOfBoundsException("Index $index is not in 0 until ${lengths.size}")
        return lengths[index]
    }

    /** The unsigned value of the byte at [pos]. */
    fun u8(b: ByteArray, pos: Int, lim: Int): Int {
        checkRange(pos, 1, lim)
        return b[pos].toInt() and 0xFF
    }

    /** The signed value of the byte at [pos]. */
    fun s8(b: ByteArray, pos: Int, lim: Int): Int {
        checkRange(pos, 1, lim)
        return b[pos].toInt()
    }

    /** The unsigned value of the [width] (1 to 8) bytes at [pos] in [order]. */
    fun readUnsigned(b: ByteArray, pos: Int, width: Int, order: ByteOrder, lim: Int): Long {
        require(width in 1..Long.SIZE_BYTES) { "Width must be 1..${Long.SIZE_BYTES} bytes, was $width" }
        checkRange(pos, width, lim)
        var result = 0L
        for (index in 0 until width) {
            val byte = when (order) {
                ByteOrder.LEAST_SIGNIFICANT_FIRST -> b[pos + width - 1 - index]
                ByteOrder.MOST_SIGNIFICANT_FIRST -> b[pos + index]
            }
            result = (result shl Byte.SIZE_BITS) or (byte.toLong() and 0xFF)
        }
        return result
    }

    /** The two's-complement value of the [width] (1 to 8) bytes at [pos] in [order], sign-extended from its top bit. */
    fun readSigned(b: ByteArray, pos: Int, width: Int, order: ByteOrder, lim: Int): Long =
        NumericCodec.signExtend(readUnsigned(b, pos, width, order, lim), width * Byte.SIZE_BITS, signed = true)

    /** Reads a natural number exactly as `BytesFormat` decodes one; see [NumericCodec.readNatural]. */
    fun readNatural(b: ByteArray, pos: Int, length: Length, signed: Boolean, order: ByteOrder, lim: Int): Long {
        checkRange(pos, length.bytes, lim)
        return NumericCodec.readNatural(b, pos, length, signed, order)
    }

    /** Writes an integral natural number exactly as `BytesFormat` encodes one; see [NumericCodec.writeNatural]. */
    fun writeNatural(b: ByteArray, pos: Int, value: Long, length: Length, signed: Boolean, order: ByteOrder, lim: Int) {
        checkRange(pos, length.bytes, lim)
        NumericCodec.writeNatural(b, pos, value, length, signed, order)
    }

    /** Writes the floating point wire value of a scalar or range encoding exactly as `BytesFormat` does. */
    fun writeNatural(b: ByteArray, pos: Int, value: Double, length: Length, signed: Boolean, order: ByteOrder, lim: Int) {
        checkRange(pos, length.bytes, lim)
        NumericCodec.writeNatural(b, pos, value, length, signed, order)
    }

    fun readFloat(b: ByteArray, pos: Int, order: ByteOrder, lim: Int): Float {
        checkRange(pos, Float.SIZE_BYTES, lim)
        return b.decodeFloat(pos, order)
    }

    fun writeFloat(b: ByteArray, pos: Int, value: Float, order: ByteOrder, lim: Int) {
        checkRange(pos, Float.SIZE_BYTES, lim)
        value.copyIntoByteArray(b, pos, order)
    }

    fun readDouble(b: ByteArray, pos: Int, order: ByteOrder, lim: Int): Double {
        checkRange(pos, Double.SIZE_BYTES, lim)
        return b.decodeDouble(pos, order)
    }

    fun writeDouble(b: ByteArray, pos: Int, value: Double, order: ByteOrder, lim: Int) {
        checkRange(pos, Double.SIZE_BYTES, lim)
        value.copyIntoByteArray(b, pos, order)
    }

    fun readMedFloat16(b: ByteArray, pos: Int, lim: Int): Double {
        checkRange(pos, 2, lim)
        return b.decodeMedFloat16(pos).value
    }

    fun writeMedFloat16(b: ByteArray, pos: Int, value: Double, lim: Int) {
        checkRange(pos, 2, lim)
        MedFloat16(value).copyIntoByteArray(b, pos)
    }

    fun readMedFloat32(b: ByteArray, pos: Int, lim: Int): Double {
        checkRange(pos, 4, lim)
        return b.decodeMedFloat32(pos).value
    }

    fun writeMedFloat32(b: ByteArray, pos: Int, value: Double, lim: Int) {
        checkRange(pos, 4, lim)
        MedFloat32(value).copyIntoByteArray(b, pos)
    }

    /** Reads a [Char] of [Encoding.byteSize] bytes as `BytesFormat` decodes one. */
    fun readChar(b: ByteArray, pos: Int, encoding: Encoding, order: ByteOrder, lim: Int): Char {
        checkRange(pos, encoding.byteSize, lim)
        return when (encoding) {
            Encoding.UTF_8 -> b[pos].decodeUTF8Char()
            Encoding.UTF_16 -> b.decodeUTF16Char(pos, order)
            Encoding.ASCII -> b[pos].decodeAsciiChar()
        }
    }

    /**
     * Writes a [Char] into [Encoding.byteSize] bytes as `BytesFormat` encodes one. Like `BytesFormat`, only the first
     * byte of a multi-byte UTF-8 character is kept.
     */
    fun writeChar(b: ByteArray, pos: Int, value: Char, encoding: Encoding, order: ByteOrder, lim: Int) {
        checkRange(pos, encoding.byteSize, lim)
        when (encoding) {
            Encoding.UTF_8 -> b[pos] = utf8FirstByte(value, order)
            Encoding.UTF_16 -> value.copyUTF16IntoByteArray(b, pos, order)
            Encoding.ASCII -> b[pos] = value.toAscii()
        }
    }

    // The first byte BytesFormat's UTF-8 encoder writes for [value], which in MOST_SIGNIFICANT_FIRST is its last.
    private fun utf8FirstByte(value: Char, order: ByteOrder): Byte {
        val code = value.code
        return when {
            code < 0x80 -> code.toByte()
            value.isSurrogate() -> Encoding.UTF_8.encodeChar(value, order).first()
            order == ByteOrder.MOST_SIGNIFICANT_FIRST -> (0x80 or (code and 0x3F)).toByte()
            code < 0x800 -> (0xC0 or (code shr 6)).toByte()
            else -> (0xE0 or (code shr 12)).toByte()
        }
    }

    /** Whether bit [bitPos] (counted from the least significant bit of the byte at [s]) is set. */
    fun bit(b: ByteArray, s: Int, bitPos: Int, lim: Int): Boolean {
        val pos = s + bitPos / Byte.SIZE_BITS
        checkRange(pos, 1, lim)
        return (b[pos].toInt() shr (bitPos % Byte.SIZE_BITS)) and 1 == 1
    }

    fun setBit(b: ByteArray, s: Int, bitPos: Int, value: Boolean, lim: Int) {
        val pos = s + bitPos / Byte.SIZE_BITS
        checkRange(pos, 1, lim)
        val mask = 1 shl (bitPos % Byte.SIZE_BITS)
        b[pos] = if (value) (b[pos].toInt() or mask).toByte() else (b[pos].toInt() and mask.inv()).toByte()
    }

    /** The [width] bits starting at [bitPos], least significant bit first, as an unsigned value. */
    fun bits(b: ByteArray, s: Int, bitPos: Int, width: Int, lim: Int): Long {
        var result = 0L
        for (index in 0 until width) {
            if (bit(b, s, bitPos + index, lim)) result = result or (1L shl index)
        }
        return result
    }

    /** Writes the low [width] bits of [value] starting at [bitPos], least significant bit first. */
    fun setBits(b: ByteArray, s: Int, bitPos: Int, width: Int, value: Long, lim: Int) {
        for (index in 0 until width) {
            setBit(b, s, bitPos + index, (value shr index) and 1L == 1L, lim)
        }
    }

    /**
     * The number packed into [bits] flag header bits at [bitPos], sign-extended when [signed], as `BytesFormat` decodes
     * a `@FlagIndex` + `@FlagWidth` number.
     */
    fun packedNatural(b: ByteArray, s: Int, bitPos: Int, bits: Int, signed: Boolean, lim: Int): Long = NumericCodec.signExtend(bits(b, s, bitPos, bits, lim), bits, signed)

    /**
     * Packs [value] into [bits] flag header bits at [bitPos].
     * @throws IllegalArgumentException, as `BytesFormat` does, when [value] does not fit.
     */
    fun setPackedNatural(b: ByteArray, s: Int, bitPos: Int, bits: Int, value: Long, signed: Boolean, lim: Int) {
        require(NumericCodec.fitsInBits(value, bits, signed)) {
            "Value $value does not fit in a ${if (signed) "signed" else "unsigned"} $bits-bit flag field"
        }
        setBits(b, s, bitPos, bits, value, lim)
    }

    /** Whether the bytes at [pos] equal [pattern]; `false` when they would extend past [lim]. */
    fun matches(b: ByteArray, pos: Int, pattern: ByteArray, lim: Int): Boolean {
        if (pos < 0 || pos > lim - pattern.size) return false
        for (index in pattern.indices) {
            if (b[pos + index] != pattern[index]) return false
        }
        return true
    }

    /**
     * The index of the first entry of [table] the bytes at [pos] match, as `BytesFormat` identifies an enum entry.
     * @throws NoSuchElementException when none matches.
     */
    fun matchIndex(b: ByteArray, pos: Int, table: Array<ByteArray>, lim: Int): Int {
        for (index in table.indices) {
            if (matches(b, pos, table[index], lim)) return index
        }
        throw NoSuchElementException("No entry matches the bytes at $pos")
    }

    /** Copies [pattern] to [pos]. */
    fun write(b: ByteArray, pos: Int, pattern: ByteArray, lim: Int) {
        checkRange(pos, pattern.size, lim)
        pattern.copyInto(b, pos)
    }

    /** Zeroes `[from, to)`. */
    fun clear(b: ByteArray, from: Int, to: Int) {
        checkRange(from, to - from, b.size)
        b.fill(0, from, to)
    }

    /** Reverses `[from, to)` in place. */
    fun mirror(b: ByteArray, from: Int, to: Int) {
        checkRange(from, to - from, b.size)
        var low = from
        var high = to - 1
        while (low < high) {
            val byte = b[low]
            b[low] = b[high]
            b[high] = byte
            low++
            high--
        }
    }

    /** Whether `a[aFrom, aTo)` and `b[bFrom, bTo)` hold the same bytes; `false` when either is not a range of its array. */
    fun rangeEquals(a: ByteArray, aFrom: Int, aTo: Int, b: ByteArray, bFrom: Int, bTo: Int): Boolean {
        if (aTo - aFrom != bTo - bFrom) return false
        if (aFrom < 0 || aFrom > aTo || aTo > a.size || bFrom < 0 || bFrom > bTo || bTo > b.size) return false
        for (index in 0 until aTo - aFrom) {
            if (a[aFrom + index] != b[bFrom + index]) return false
        }
        return true
    }

    /** `b[from, to)` in hexadecimal, clipped to the array, for describing a view whose content cannot be read. */
    fun hex(b: ByteArray, from: Int, to: Int): String = b.copyOfRange(from.coerceIn(0, b.size), to.coerceIn(from.coerceIn(0, b.size), b.size)).toHexString(separator = " ")

    /** A hash of `b[from, to)` consistent with [rangeEquals]. */
    fun rangeHashCode(b: ByteArray, from: Int, to: Int): Int {
        checkRange(from, to - from, b.size)
        var result = 1
        for (index in from until to) {
            result = 31 * result + b[index]
        }
        return result
    }

    /**
     * The entry of [entries] at [index], the ordinal a flag-packed enum stores.
     * @throws SerializationException, as kotlinx.serialization does, when [index] is not an ordinal of the enum.
     */
    fun <E : Enum<E>> entry(entries: List<E>, index: Int, serialName: String): E =
        entries.getOrNull(index) ?: throw SerializationException("$index is not among valid $serialName enum values, values size is ${entries.size}")
}
