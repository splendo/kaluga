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
    UTF_8 -> string.toUTF8(byteOrder)
    UTF_16 -> string.toUTF16(byteOrder)
    ASCII -> string.toAscii(byteOrder)
}

fun Encoding.copyEncodedStringIntoArray(string: String, array: ByteArray, offset: Int = 0, byteOrder: ByteOrder) = when (this) {
    UTF_8 -> string.copyUTF8IntoArray(array, offset, byteOrder)
    UTF_16 -> string.copyUTF16IntoArray(array, offset, byteOrder)
    ASCII -> string.copyAsciiIntoArray(array, offset, byteOrder)
}

fun Encoding.byteSizeOf(string: String): Int = when (this) {
    UTF_8 -> string.utf8Size
    UTF_16 -> string.utf16Size
    ASCII -> string.asciiSize
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

        abstract fun expectedByteSize(size: UInt): Int
        fun encodeSize(size: UInt, order: ByteOrder): ByteArray = copyEncodedSizeInto(ByteArray(expectedByteSize(size)), size, order, 0)
        abstract fun copyEncodedSizeInto(array: ByteArray, size: UInt, order: ByteOrder, offset: Int): ByteArray

        /**
         * A [LengthPrefix] where the size is always encoded as a single [UByte]
         */
        data object ByteLength : LengthPrefix() {

            override fun expectedByteSize(size: UInt): Int = Byte.SIZE_BYTES

            override fun copyEncodedSizeInto(array: ByteArray, size: UInt, order: ByteOrder, offset: Int): ByteArray {
                require(size <= UByte.MAX_VALUE) { "Size $size is too large to encode as byte" }
                require(array.size >= offset + Byte.SIZE_BYTES) { "Cannot copy into ByteArray. Must be at least ${offset + Byte.SIZE_BYTES} long" }
                array[offset] = size.toUByte().toByte()
                return array
            }
        }

        /**
         * A [LengthPrefix] where the size is always encoded as an [UShort]
         */
        data object ShortLength : LengthPrefix() {
            override fun expectedByteSize(size: UInt): Int = UShort.SIZE_BYTES

            override fun copyEncodedSizeInto(array: ByteArray, size: UInt, order: ByteOrder, offset: Int): ByteArray {
                require(size <= UShort.MAX_VALUE) { "Size $size is too large to encode as short" }
                return size.toUShort().copyIntoByteArray(array, offset, order)
            }
        }

        /**
         * A [LengthPrefix] where the size is encoded as an [UByte] if it fits, or using a [sentinel] prefix followed by a [UShort] length
         * @property sentinel the sentinel to indicate length was encoded as a short
         */
        data class WithOverflow(val sentinel: Byte = 0xFF.toByte()) : LengthPrefix() {

            override fun expectedByteSize(size: UInt): Int = when {
                size <= UByte.MAX_VALUE.toUInt() -> Byte.SIZE_BYTES
                else -> Byte.SIZE_BYTES + UShort.SIZE_BYTES
            }

            override fun copyEncodedSizeInto(array: ByteArray, size: UInt, order: ByteOrder, offset: Int): ByteArray = when {
                size <= UByte.MAX_VALUE.toUInt() -> ByteLength.copyEncodedSizeInto(array, size, order, offset)

                else -> {
                    require(array.size >= offset + Byte.SIZE_BYTES + 1) { "Cannot copy into ByteArray. Must be at least ${offset + Byte.SIZE_BYTES + 1} long" }
                    when (order) {
                        ByteOrder.MOST_SIGNIFICANT_FIRST -> {
                            ShortLength.copyEncodedSizeInto(array, size, order, offset)
                            array[offset + 2] = sentinel
                        }

                        ByteOrder.LEAST_SIGNIFICANT_FIRST -> {
                            array[offset] = sentinel
                            ShortLength.copyEncodedSizeInto(array, size, order, offset + 1)
                        }
                    }
                    array
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

fun String.byteArraySize(settings: StringEncodingSettings): Int {
    val stringSize = settings.encoding.byteSizeOf(this)
    return when (val endMarking = settings.endMarking) {
        is StringEncodingSettings.LengthPrefix -> {
            val lengthSize = endMarking.expectedByteSize((stringSize / settings.encoding.byteSize).toUInt())
            stringSize + lengthSize
        }

        is StringEncodingSettings.NullTerminated -> stringSize + 1

        is StringEncodingSettings.NoMarking -> stringSize

        is StringEncodingSettings.FixedLength -> settings.encoding.byteSize * endMarking.length
    }
}

/**
 * Encodes a [String] to a [ByteArray] using the given [StringEncodingSettings] and [ByteOrder].
 * @param settings the [StringEncodingSettings] to apply to the encoding.
 * @param order the [ByteOrder] to use. When passing [ByteOrder.MOST_SIGNIFICANT_FIRST] the first character will be at the end of the array.
 * @throws IllegalArgumentException if the string contains a null character and [StringEncodingSettings.NullTerminated] is used,
 * or if the length of the string cannot be encoded by [StringEncodingSettings.LengthPrefix]
 * @return The encoded [ByteArray]
 */
fun String.toByteArray(settings: StringEncodingSettings, order: ByteOrder): ByteArray = copyIntoArray(ByteArray(byteArraySize(settings)), settings, order = order)
fun String.copyIntoArray(array: ByteArray, settings: StringEncodingSettings, offset: Int = 0, order: ByteOrder): ByteArray {
    val totalSize = byteArraySize(settings)
    val stringSize = settings.encoding.byteSizeOf(this)
    require(array.size >= offset + totalSize) { "Cannot copy into ByteArray. Must be at least ${offset + totalSize} long" }
    return when (val endMarking = settings.endMarking) {
        is StringEncodingSettings.LengthPrefix -> {
            val size = (stringSize / settings.encoding.byteSize).toUInt()
            when (order) {
                ByteOrder.MOST_SIGNIFICANT_FIRST -> {
                    settings.encoding.copyEncodedStringIntoArray(this, array, offset, order)
                    endMarking.copyEncodedSizeInto(array, size, order, offset + stringSize)
                }

                ByteOrder.LEAST_SIGNIFICANT_FIRST -> {
                    endMarking.copyEncodedSizeInto(array, size, order, offset)
                    settings.encoding.copyEncodedStringIntoArray(this, array, offset + endMarking.expectedByteSize(size), order)
                }
            }
        }

        is StringEncodingSettings.NullTerminated -> {
            require(!contains('\u0000')) { "Null terminated string cannot contain null character" }
            when (order) {
                ByteOrder.MOST_SIGNIFICANT_FIRST -> {
                    array[offset] = 0x00.toByte()
                    settings.encoding.copyEncodedStringIntoArray(this, array, offset + 1, order)
                }

                ByteOrder.LEAST_SIGNIFICANT_FIRST -> {
                    settings.encoding.copyEncodedStringIntoArray(this, array, offset, order)
                    array[offset + stringSize] = 0x00.toByte()
                    array
                }
            }
        }

        is StringEncodingSettings.NoMarking -> settings.encoding.copyEncodedStringIntoArray(this, array, offset, order)

        is StringEncodingSettings.FixedLength -> {
            if (stringSize < totalSize) {
                when (order) {
                    ByteOrder.MOST_SIGNIFICANT_FIRST -> settings.encoding.copyEncodedStringIntoArray(this, array, offset + (totalSize - stringSize), order)
                    ByteOrder.LEAST_SIGNIFICANT_FIRST -> settings.encoding.copyEncodedStringIntoArray(this, array, offset, order)
                }
            } else {
                settings.encoding.copyEncodedStringIntoArray(take(endMarking.length), array, offset, order)
            }
            array
        }
    }
}

val String.utf8Size: Int get() {
    var size = 0
    var i = 0
    while (i < length) {
        val c = this[i]
        when {
            c.code < 0x80 -> size += 1

            c.code < 0x800 -> size += 2

            c.isHighSurrogate() -> {
                size += 4
                i++ // skip low surrogate
            }

            else -> size += 3
        }
        i++
    }
    return size
}

fun String.toUTF8(byteOrder: ByteOrder): ByteArray = copyUTF8IntoArray(ByteArray(utf8Size), byteOrder = byteOrder)

fun String.copyUTF8IntoArray(array: ByteArray, offset: Int = 0, byteOrder: ByteOrder): ByteArray {
    val size = utf8Size
    require(array.size >= offset + size) { "Cannot copy into ByteArray. Must be at least ${offset + size} long" }
    var pos = 0
    val writeByte = when (byteOrder) {
        ByteOrder.MOST_SIGNIFICANT_FIRST -> {
            { byte: Byte ->
                array[offset + size - pos++ - 1] = byte
            }
        }

        ByteOrder.LEAST_SIGNIFICANT_FIRST -> {
            { byte: Byte -> array[offset + pos++] = byte }
        }
    }
    var i = 0

    while (i < length) {
        val c = this[i].code
        when {
            c < 0x80 -> {
                writeByte(c.toByte())
            }

            c < 0x800 -> {
                writeByte((0xC0 or (c shr 6)).toByte())
                writeByte((0x80 or (c and 0x3F)).toByte())
            }

            c in 0xD800..0xDBFF -> { // high surrogate
                val low = this[++i].code
                val codePoint =
                    ((c - 0xD800) shl 10) + (low - 0xDC00) + 0x10000
                writeByte((0xF0 or (codePoint shr 18)).toByte())
                writeByte((0x80 or ((codePoint shr 12) and 0x3F)).toByte())
                writeByte((0x80 or ((codePoint shr 6) and 0x3F)).toByte())
                writeByte((0x80 or (codePoint and 0x3F)).toByte())
            }

            else -> {
                writeByte((0xE0 or (c shr 12)).toByte())
                writeByte((0x80 or ((c shr 6) and 0x3F)).toByte())
                writeByte((0x80 or (c and 0x3F)).toByte())
            }
        }
        i++
    }
    return array
}

val String.utf16Size: Int get() = length * 2
fun String.toUTF16(byteOrder: ByteOrder): ByteArray = copyUTF16IntoArray(ByteArray(utf16Size), byteOrder = byteOrder)

fun String.copyUTF16IntoArray(array: ByteArray, offset: Int = 0, byteOrder: ByteOrder): ByteArray {
    val byteLength = utf16Size
    require(array.size >= offset + byteLength) { "Cannot copy into ByteArray. Must be at least ${offset + byteLength} long" }

    forEachIndexed { index, character ->
        val utf16Char = character.toUTF16(byteOrder)
        when (byteOrder) {
            ByteOrder.MOST_SIGNIFICANT_FIRST -> {
                array[offset + byteLength - index * 2 - 1] = utf16Char[1]
                array[offset + byteLength - index * 2 - 2] = utf16Char[0]
            }

            ByteOrder.LEAST_SIGNIFICANT_FIRST -> {
                array[offset + index * 2] = utf16Char[0]
                array[offset + index * 2 + 1] = utf16Char[1]
            }
        }
    }
    return array
}

val String.asciiSize: Int get() = length
fun String.toAscii(byteOrder: ByteOrder): ByteArray = copyAsciiIntoArray(ByteArray(asciiSize), byteOrder = byteOrder)

fun String.copyAsciiIntoArray(array: ByteArray, offset: Int = 0, byteOrder: ByteOrder): ByteArray {
    val byteLength = asciiSize
    require(array.size >= offset + byteLength) { "Cannot copy into ByteArray. Must be at least ${offset + byteLength} long" }
    forEachIndexed { index, character ->
        val index = when (byteOrder) {
            ByteOrder.MOST_SIGNIFICANT_FIRST -> length - index - 1
            ByteOrder.LEAST_SIGNIFICANT_FIRST -> index
        }

        array[offset + index] = character.toAscii()
    }
    return array
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
