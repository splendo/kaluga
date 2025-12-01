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

package com.splendo.kaluga.base.bytes

import com.splendo.kaluga.base.bytes.Encoding.ASCII
import com.splendo.kaluga.base.bytes.Encoding.UTF_16
import com.splendo.kaluga.base.bytes.Encoding.UTF_8

/**
 * Character encoding
 * @property byteSize the number of [Byte] required to encode with this encoding
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
 * Encodes a [Char] into a [ByteArray] using the given [Encoding] and [ByteOrder].
 * @param char the [Char] to encode.
 * @param byteOrder the [ByteOrder] to use. For [Encoding] where [Encoding.byteSize] is 1, this can be ignored.
 */
fun Encoding.encodeChar(char: Char, byteOrder: ByteOrder) = when (this) {
    UTF_8 -> char.toString().encodeToByteArray()
    UTF_16 -> char.toUTF16(byteOrder)
    ASCII -> char.toAscii()
}

/**
 * Encodes a [Char] to a [ByteArray] in UTF-16 using the given [ByteOrder].
 * @param byteOrder the [ByteOrder] to use.
 * @return the [ByteArray] representing the [Char] in UTF-16.
 */
fun Char.toUTF16(byteOrder: ByteOrder): ByteArray = code.toUShort().toByteArray(byteOrder)

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
 */
fun Byte.decodeUTF8Char() = toInt().toChar()

/**
 * Decodes a [Char] at [octetIndex] from a [ByteArray] assuming UTF-8 encoding.
 * @param octetIndex the index of the octet to decode.
 * @return the Char decoded from the [ByteArray] at [octetIndex].
 */
fun ByteArray.decodeUTF8Char(octetIndex: Int) = get(octetIndex).decodeUTF8Char()

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
