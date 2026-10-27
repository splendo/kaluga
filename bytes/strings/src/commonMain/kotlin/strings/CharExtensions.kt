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

package com.splendo.kaluga.bytes.strings

import com.splendo.kaluga.bytes.ByteOrder
import com.splendo.kaluga.bytes.copyIntoByteArray
import com.splendo.kaluga.bytes.decodeUShort
import com.splendo.kaluga.bytes.strings.Encoding.ASCII
import com.splendo.kaluga.bytes.strings.Encoding.UTF_16
import com.splendo.kaluga.bytes.strings.Encoding.UTF_8
import com.splendo.kaluga.bytes.toByteArray

/**
 * Character encoding
 * @property byteSize the number of [Byte] required to encode a [Char] with this encoding
 */
enum class Encoding(val byteSize: Int) {

    /**
     * UTF-8 character encoding
     */
    UTF_8(1),

    /**
     * UTF-16 character encoding
     */
    UTF_16(2),

    /**
     * ASCII encoding
     */
    ASCII(1),
}

/**
 * Returns the number of [Byte]s required to encode a [Char] using the given [Encoding].
 * For [Encoding.UTF_8] this is 1 to 3, depending on the character; a lone surrogate encodes as the 3-byte replacement character.
 * @param char the [Char] to encode.
 * @return the number of [Byte]s required to encode [char].
 */
fun Encoding.byteSizeOf(char: Char): Int = when (this) {
    UTF_8 -> when {
        char.code < 0x80 -> 1
        char.code < 0x800 -> 2
        else -> 3
    }

    UTF_16 -> 2

    ASCII -> 1
}

/**
 * Encodes a [Char] into a [ByteArray] using the given [Encoding] and [ByteOrder].
 * @param char the [Char] to encode.
 * @param byteOrder the [ByteOrder] to use. For [Encoding] where [Encoding.byteSize] is 1, this only matters for characters that encode to more than one byte.
 */
fun Encoding.encodeChar(char: Char, byteOrder: ByteOrder): ByteArray = copyCharIntoByteArray(char, ByteArray(byteSizeOf(char)), byteOrder = byteOrder)

/**
 * Encodes a [Char] using the given [Encoding] and [ByteOrder] and copies it into a [ByteArray] at a given offset.
 * @param char the [Char] to encode.
 * @param array the [ByteArray] to copy the encoded data into.
 * @param offset the offset at which to copy the encoded data.
 * @param byteOrder the [ByteOrder] in which the [Char] is encoded. For [Encoding] where [Encoding.byteSize] is 1, this only matters for characters that encode to more than one byte.
 * @throws IllegalArgumentException if [array] is not large enough to hold [byteSizeOf] [char] bytes at the [offset].
 * @return the [array] the [Char] was copied into.
 */
fun Encoding.copyCharIntoByteArray(char: Char, array: ByteArray, offset: Int = 0, byteOrder: ByteOrder): ByteArray {
    val size = byteSizeOf(char)
    require(array.size >= offset + size) { "Cannot copy into ByteArray. Must be at least ${offset + size} long" }
    when (this) {
        UTF_8 -> char.copyUTF8IntoArray(array, offset, size, byteOrder)
        UTF_16 -> char.copyUTF16IntoByteArray(array, offset, byteOrder)
        ASCII -> array[offset] = char.toAscii()
    }
    return array
}

private fun Char.copyUTF8IntoArray(array: ByteArray, offset: Int, size: Int, byteOrder: ByteOrder) {
    fun put(index: Int, value: Int) {
        val position = when (byteOrder) {
            ByteOrder.MOST_SIGNIFICANT_FIRST -> offset + size - 1 - index
            ByteOrder.LEAST_SIGNIFICANT_FIRST -> offset + index
        }
        array[position] = value.toByte()
    }
    when {
        size == 1 -> put(0, code)

        size == 2 -> {
            put(0, (code shr 6) or 0xC0)
            put(1, (code and 0x3F) or 0x80)
        }

        isSurrogate() -> REPLACEMENT_BYTE_SEQUENCE.forEachIndexed { index, byte -> put(index, byte.toInt()) }

        else -> {
            put(0, 0xE0 or (code shr 12))
            put(1, 0x80 or ((code shr 6) and 0x3F))
            put(2, 0x80 or (code and 0x3F))
        }
    }
}

/**
 * Encodes a [Char] to a [ByteArray] in UTF-16 using the given [ByteOrder].
 * @param byteOrder the [ByteOrder] to use.
 * @return the [ByteArray] representing the [Char] in UTF-16.
 */
fun Char.toUTF16(byteOrder: ByteOrder): ByteArray = code.toUShort().toByteArray(byteOrder)

/**
 * Encodes a [Char] and copies it into a [ByteArray] at a given offset in UTF-16 using the given [ByteOrder].
 * @param array the [ByteArray] to copy the encoded data into.
 * @param offset the offset at which to copy the encoded data.
 * @param byteOrder the [ByteOrder] to use.
 * @throws IllegalArgumentException if [array] is not  is not large enough to hold 2 bytes at the [offset].
 * @return the [ByteArray] representing the [Char] in UTF-16.
 */
fun Char.copyUTF16IntoByteArray(array: ByteArray, offset: Int = 0, byteOrder: ByteOrder) = code.toUShort().copyIntoByteArray(array, offset, byteOrder)

/**
 * Encodes a [Char] to a [Byte] in ASCII.
 * @throws IllegalArgumentException if the character cannot be represented in ASCII. Use [Char.toAsciiOrNull] to get a non-throwing variant
 * @return the [ByteArray] representing the [Char] in ASCII.
 */
fun Char.toAscii(): Byte = if (code > 0x7F) {
    throw IllegalArgumentException("Non-ASCII character: '$this' (0x${code.toString(16)})")
} else {
    code.toByte()
}

/**
 * Encodes a [Char] to a [Byte] in ASCII or `null` if the character cannot be represented in ASCII.
 * @return the [ByteArray] representing the [Char] in ASCII if it can be represented in ASCII, `null` otherwise.
 */
fun Char.toAsciiOrNull(): Byte? = try {
    toAscii()
} catch (_: IllegalArgumentException) {
    null
}

/**
 * Decodes a [Char] from a [Byte] assuming UTF-8 encoding.
 * @throws IllegalArgumentException if the byte is not a complete UTF-8 sequence on its own, i.e. not in `0x00..0x7F`.
 */
fun Byte.decodeUTF8Char(): Char {
    require(this >= 0) { "0x${(toInt() and 0xFF).toString(16)} is not a single-byte UTF-8 character" }
    return toInt().toChar()
}

/**
 * Decodes a [Char] from the UTF-8 sequence of one to three bytes starting at [octetIndex] in a [ByteArray].
 * The sequence may be stored with its lead byte first or, when it was encoded [ByteOrder.MOST_SIGNIFICANT_FIRST], last.
 * @param octetIndex the index of the first octet of the sequence.
 * @throws IllegalArgumentException if the bytes are not a UTF-8 sequence for a single [Char].
 * @return the Char decoded from the [ByteArray] at [octetIndex].
 */
fun ByteArray.decodeUTF8Char(octetIndex: Int): Char {
    var index = octetIndex
    return decodeUTF8Char { get(index++) }
}

/**
 * Decodes a [Char] from a UTF-8 sequence of one to three bytes, read one at a time from [nextByte].
 * The sequence may arrive with its lead byte first or, when it was encoded [ByteOrder.MOST_SIGNIFICANT_FIRST], last.
 * @param nextByte returns the next byte of the sequence. It is called once per byte of the sequence.
 * @throws IllegalArgumentException if the bytes are not a UTF-8 sequence for a single [Char].
 * @return the decoded Char.
 */
inline fun decodeUTF8Char(nextByte: () -> Byte): Char {
    val first = nextByte().toInt() and 0xFF
    return when {
        first < 0x80 -> first.toChar()

        first.isUTF8Continuation() -> {
            val second = nextByte().toInt() and 0xFF
            if (second.isUTF8Continuation()) {
                utf8Char(nextByte().toInt() and 0xFF, second, first)
            } else {
                utf8Char(second, first)
            }
        }

        first and 0xE0 == 0xC0 -> utf8Char(first, nextByte().toInt() and 0xFF)

        else -> utf8Char(first, nextByte().toInt() and 0xFF, nextByte().toInt() and 0xFF)
    }
}

@PublishedApi
internal fun Int.isUTF8Continuation() = this and 0xC0 == 0x80

@PublishedApi
internal fun utf8Char(lead: Int, continuation: Int): Char {
    require(lead and 0xE0 == 0xC0) { "0x${lead.toString(16)} does not start a 2-byte UTF-8 sequence" }
    return ((lead and 0x1F) shl 6 or continuationBits(continuation)).toChar()
}

@PublishedApi
internal fun utf8Char(lead: Int, firstContinuation: Int, secondContinuation: Int): Char {
    require(lead and 0xF0 == 0xE0) { "0x${lead.toString(16)} does not start a UTF-8 sequence for a single Char" }
    return ((lead and 0x0F) shl 12 or (continuationBits(firstContinuation) shl 6) or continuationBits(secondContinuation)).toChar()
}

private fun continuationBits(byte: Int): Int {
    require(byte.isUTF8Continuation()) { "0x${byte.toString(16)} is not a UTF-8 continuation byte" }
    return byte and 0x3F
}

/**
 * Decodes a [Char] from a [Byte] assuming ASCII encoding.
 */
fun Byte.decodeAsciiChar() = Char(toInt())

/**
 * Decodes a [Char] at [octetIndex] from a [ByteArray] assuming ASCII encoding.
 * @param octetIndex the index of the octet to decode.
 * @return the Char decoded from the [ByteArray] at [octetIndex].
 */
fun ByteArray.decodeAsciiChar(octetIndex: Int) = get(octetIndex).decodeAsciiChar()

/**
 * Decodes a [Char] at [octetIndex] from a [ByteArray] assuming UTF-16 encoding.
 * @param octetIndex the index of the octet to decode.
 * @return the Char decoded from the [ByteArray] at [octetIndex].
 */
fun ByteArray.decodeUTF16Char(octetIndex: Int, order: ByteOrder) = Char(decodeUShort(octetIndex, order).toInt())
