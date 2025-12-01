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
 * Encodes a [String] into a [ByteArray] using the given [Encoding] and [ByteOrder].
 * @param string the [String] to encode.
 * @param byteOrder the [ByteOrder] to use.
 */
fun Encoding.encodeString(string: String, byteOrder: ByteOrder) = when (this) {
    UTF_8 -> string.encodeToByteArray().let {
        when (byteOrder) {
            ByteOrder.MOST_SIGNIFICANT_FIRST -> it.reversedArray()
            ByteOrder.LEAST_SIGNIFICANT_FIRST -> it
        }
    }
    UTF_16 -> string.toUTF16(byteOrder)
    ASCII -> string.toAscii(byteOrder)
}

/**
 * Settings to determine how a String should be encoded into a ByteArray.
 * @property endMarking The [EndMarking] to use for encoding when the string ends.
 * @property encoding The [Encoding] to use for encoding the string.
 */
data class StringEncodingSettings(val endMarking: EndMarking = LengthPrefix.ByteLength, val encoding: Encoding = Encoding.UTF_8) {

    /**
     * Method used for marking the end of a String when encoding it to a [ByteArray]
     */
    sealed class EndMarking

    /**
     * An [EndMarking] where the length of the String is encoded as a prefix.
     */
    sealed class LengthPrefix : EndMarking() {
        abstract fun encodeSize(size: UInt, order: ByteOrder): ByteArray

        /**
         * A [LengthPrefix] where the size is always encoded as a single [UByte]
         */
        data object ByteLength : LengthPrefix() {
            override fun encodeSize(size: UInt, order: ByteOrder): ByteArray {
                require(size <= UByte.MAX_VALUE) { "Size $size is too large to encode as byte" }
                return byteArrayOf(size.toUByte().toByte())
            }
        }

        /**
         * A [LengthPrefix] where the size is always encoded as an [UShort]
         */
        data object ShortLength : LengthPrefix() {
            override fun encodeSize(size: UInt, order: ByteOrder): ByteArray {
                require(size <= UShort.MAX_VALUE) { "Size $size is too large to encode as short" }
                return when (order) {
                    ByteOrder.MOST_SIGNIFICANT_FIRST -> size.toUShort().toByteArray(order)
                    ByteOrder.LEAST_SIGNIFICANT_FIRST -> size.toUShort().toByteArray(order)
                }
            }
        }

        /**
         * A [LengthPrefix] where the size is encoded as an [UByte] if it fits, or using a [sentinel] prefix followed by a [UShort] length
         * @property sentinel the sentinel to indicate length was encoded as a short
         */
        data class WithOverflow(val sentinel: Byte = 0xFF.toByte()) : LengthPrefix() {
            override fun encodeSize(size: UInt, order: ByteOrder): ByteArray = when {
                size <= UByte.MAX_VALUE.toUInt() -> ByteLength.encodeSize(size, order)
                else -> buildByteArray(order) {
                    add(sentinel)
                    add(ShortLength.encodeSize(size, order))
                }
            }
        }
    }

    /**
     * An [EndMarking] where the end is marked by a final empty character `\u0000`. If the string contains this character encoding will fail.
     */
    data object NullTerminated : EndMarking()

    /**
     * No [EndMarking] will be used, the entire array should be decoded
     */
    data object NoMarking : EndMarking()

    /**
     * An [EndMarking] where the String is always encoded as a fixed length [length].
     * If the string is longer, its final characters will be dropped.
     * If it is smaller, it will be padded with null characters.
     */

    data class FixedLength(val length: Int) : EndMarking()
}

/**
 * Encodes a [String] to a [ByteArray] using the given [StringEncodingSettings] and [ByteOrder].
 * @param settings the [StringEncodingSettings] to apply to the encoding.
 * @param order the [ByteOrder] to use. When passing [ByteOrder.MOST_SIGNIFICANT_FIRST] the first character will be at the end of the array.
 * @throws IllegalArgumentException if the string contains a null character and [StringEncodingSettings.NullTerminated] is used,
 * or if the length of the string cannot be encoded by [StringEncodingSettings.LengthPrefix]
 * @return The encoded [ByteArray]
 */
fun String.toByteArray(settings: StringEncodingSettings, order: ByteOrder): ByteArray = when (val endMarking = settings.endMarking) {
    is StringEncodingSettings.LengthPrefix -> {
        val encodedString = settings.encoding.encodeString(this, order)
        val encodedSize = endMarking.encodeSize((encodedString.size / settings.encoding.byteSize).toUInt(), order)
        when (order) {
            ByteOrder.MOST_SIGNIFICANT_FIRST -> encodedString + encodedSize
            ByteOrder.LEAST_SIGNIFICANT_FIRST -> encodedSize + encodedString
        }
    }
    is StringEncodingSettings.NullTerminated -> {
        require(!contains('\u0000')) { "Null terminated string cannot contain null character" }
        val encodedString = settings.encoding.encodeString(this, order)
        when (order) {
            ByteOrder.MOST_SIGNIFICANT_FIRST -> byteArrayOf(0x00.toByte()) + encodedString
            ByteOrder.LEAST_SIGNIFICANT_FIRST -> encodedString + 0x00.toByte()
        }
    }
    is StringEncodingSettings.NoMarking -> settings.encoding.encodeString(this, order)
    is StringEncodingSettings.FixedLength -> {
        val encodedString = settings.encoding.encodeString(this, order)
        if (encodedString.size < settings.encoding.byteSize * endMarking.length) {
            val extraBytes = ByteArray(settings.encoding.byteSize * endMarking.length - encodedString.size)
            when (order) {
                ByteOrder.MOST_SIGNIFICANT_FIRST -> extraBytes + encodedString
                ByteOrder.LEAST_SIGNIFICANT_FIRST -> encodedString + extraBytes
            }
        } else {
            when (order) {
                ByteOrder.MOST_SIGNIFICANT_FIRST -> encodedString.takeLast(settings.encoding.byteSize * endMarking.length).toByteArray()
                ByteOrder.LEAST_SIGNIFICANT_FIRST -> encodedString.take(settings.encoding.byteSize * endMarking.length).toByteArray()
            }
        }
    }
}

fun String.toUTF16(byteOrder: ByteOrder): ByteArray {
    val byteLength = this.length * 2
    val result = ByteArray(byteLength)

    forEachIndexed { index, character ->
        val utf16Char = character.toUTF16(byteOrder)
        when (byteOrder) {
            ByteOrder.MOST_SIGNIFICANT_FIRST -> {
                result[byteLength - index * 2 - 1] = utf16Char[1]
                result[byteLength - index * 2 - 2] = utf16Char[0]
            }
            ByteOrder.LEAST_SIGNIFICANT_FIRST -> {
                result[index * 2] = utf16Char[0]
                result[index * 2 + 1] = utf16Char[1]
            }
        }
    }
    return result
}

fun String.toAscii(byteOrder: ByteOrder): ByteArray {
    val result = ByteArray(this.length)
    forEachIndexed { index, character ->
        val index = when (byteOrder) {
            ByteOrder.MOST_SIGNIFICANT_FIRST -> length - index - 1
            ByteOrder.LEAST_SIGNIFICANT_FIRST -> index
        }

        result[index] = character.toAscii()
    }
    return result
}

fun String.toAsciiOrNull(byteOrder: ByteOrder): ByteArray? = try {
    toAscii(byteOrder)
} catch (_: IllegalArgumentException) {
    null
}

fun ByteArray.decodeString(settings: StringEncodingSettings, order: ByteOrder): String {
    val array = when (order) {
        ByteOrder.MOST_SIGNIFICANT_FIRST -> reversedArray()
        ByteOrder.LEAST_SIGNIFICANT_FIRST -> this
    }
    var index = 0
    val next = { array.getOrNull(index++) }
    return generateSequence(next, { next() }).decodeString(settings)
}

/**
 * Decodes a Sequence of Byte ordered Least Significant first into a String using [StringEncodingSettings]
 * @throws [IllegalArgumentException] if the sequence terminates before allowed by [settings]
 * @param settings the [StringEncodingSettings] to be used for decoding the String
 * @return the decoded String
 */
fun Sequence<Byte>.decodeString(settings: StringEncodingSettings): String {
    val stringBytes = when (val endMarking = settings.endMarking) {
        is StringEncodingSettings.LengthPrefix -> {
            val length = when (endMarking) {
                is StringEncodingSettings.LengthPrefix.ShortLength -> {
                    val encodedShort = take(2).toList().toByteArray()
                    require(encodedShort.size == 2) { "Did not include a Short as length" }
                    encodedShort.decodeUShort(0, ByteOrder.LEAST_SIGNIFICANT_FIRST).toInt()
                }
                is StringEncodingSettings.LengthPrefix.WithOverflow -> {
                    val first = first()
                    if (first == endMarking.sentinel) {
                        val encodedShort = take(2).toList().toByteArray()
                        require(encodedShort.size == 2) { "Did not include a Short as length" }
                        encodedShort.decodeUShort(0, ByteOrder.LEAST_SIGNIFICANT_FIRST).toInt()
                    } else {
                        first
                    }
                }
                else -> first()
            }.toInt() * settings.encoding.byteSize
            val stringBytes = take(length).toList()
            require(stringBytes.size == length) { "String size ${stringBytes.size} does not match encoded size $length" }
            stringBytes
        }
        is StringEncodingSettings.FixedLength -> {
            val length = settings.encoding.byteSize * endMarking.length
            val stringBytes = take(length).toList()
            require(stringBytes.size == length) { "String size ${stringBytes.size} does not match fixed size $length" }
            stringBytes
        }
        is StringEncodingSettings.NullTerminated -> {
            var hasFoundNull = false
            val stringBytes = withIndex().takeWhile { (index, byte) ->
                val isCharacterByte = (settings.encoding == Encoding.UTF_16 && index % 2 != 0) || byte != 0x00.toByte()
                hasFoundNull = !isCharacterByte
                isCharacterByte
            }.map { (_, byte) -> byte }.toList()
            require(hasFoundNull) { "Does not end with a null marker" }
            stringBytes
        }
        is StringEncodingSettings.NoMarking -> {
            toList()
        }
    }
    var result = ""
    val iterator = stringBytes.iterator()
    while (iterator.hasNext()) {
        result += when (settings.encoding) {
            UTF_8 -> iterator.next().decodeUTF8Char()
            UTF_16 -> listOf(iterator.next(), iterator.next()).toByteArray().decodeUTF16Char(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)
            ASCII -> iterator.next().decodeAsciiChar()
        }
    }
    return if (settings.endMarking is StringEncodingSettings.FixedLength) {
        result.dropLastWhile { it == 0x00.toChar() }
    } else {
        result
    }
}
