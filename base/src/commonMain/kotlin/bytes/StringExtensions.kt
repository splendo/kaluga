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

data class StringEncodingSettings(val endMarking: EndMarking = LengthPrefix(), val encoding: Encoding = Encoding.UTF_8) {

    enum class Encoding {
        UTF_8,
        UTF_16,
        ASCII,
        ;

        fun encodeString(string: String, byteOrder: ByteOrder) = when (this) {
            UTF_8 -> string.encodeToByteArray().let {
                when (byteOrder) {
                    ByteOrder.MOST_SIGNIFICANT_FIRST -> it.reversedArray()
                    ByteOrder.LEAST_SIGNIFICANT_FIRST -> it
                }
            }
            UTF_16 -> string.toUTF16(byteOrder)
            ASCII -> string.toAscii(byteOrder)
        }
    }

    sealed class EndMarking

    data class LengthPrefix(val lengthAsShort: Boolean = false, val canOverflow: Boolean = false, val sentinel: Byte = 0xFF.toByte()) : EndMarking() {
        fun encodeSize(size: UInt, order: ByteOrder): ByteArray = when {
            lengthAsShort && size < UShort.MAX_VALUE.toUInt() -> size.toUShort().toByteArray(order)
            lengthAsShort -> throw IllegalArgumentException("Size $size is too large to encode as short")
            size <= UByte.MAX_VALUE.toUInt() -> byteArrayOf(size.toUByte().toByte())
            canOverflow && size <= UShort.MAX_VALUE.toUInt() -> when (order) {
                ByteOrder.MOST_SIGNIFICANT_FIRST -> size.toUShort().toByteArray(order) + sentinel
                ByteOrder.LEAST_SIGNIFICANT_FIRST -> byteArrayOf(sentinel) + size.toUShort().toByteArray(order)
            }
            canOverflow -> throw IllegalArgumentException("Size $size is too large to encode as short")
            else -> throw IllegalArgumentException("Size $size is too large to encode as byte")
        }
    }

    data object NullTerminated : EndMarking()
    data object NoMarking : EndMarking()

    data class FixedLength(val length: Int) : EndMarking()
}

fun String.toByteArray(settings: StringEncodingSettings, order: ByteOrder): ByteArray = when (val endMarking = settings.endMarking) {
    is StringEncodingSettings.LengthPrefix -> {
        val encodedString = settings.encoding.encodeString(this, order)
        val encodedSize = endMarking.encodeSize(encodedString.size.toUInt(), order)
        when (order) {
            ByteOrder.MOST_SIGNIFICANT_FIRST -> encodedString + encodedSize
            ByteOrder.LEAST_SIGNIFICANT_FIRST -> encodedSize + encodedString
        }
    }
    is StringEncodingSettings.NullTerminated -> {
        require(!contains('\u0000')) { "Null terminated string cannot contain null character" }
        settings.encoding.encodeString(this + '\u0000', order)
    }
    is StringEncodingSettings.NoMarking -> settings.encoding.encodeString(this, order)
    is StringEncodingSettings.FixedLength -> {
        val encodedString = settings.encoding.encodeString(this, order)
        if (encodedString.size < endMarking.length) {
            when (order) {
                ByteOrder.MOST_SIGNIFICANT_FIRST -> ByteArray(endMarking.length - encodedString.size) + encodedString
                ByteOrder.LEAST_SIGNIFICANT_FIRST -> encodedString + ByteArray(endMarking.length - encodedString.size)
            }
        } else {
            when (order) {
                ByteOrder.MOST_SIGNIFICANT_FIRST -> encodedString.takeLast(endMarking.length).toByteArray()
                ByteOrder.LEAST_SIGNIFICANT_FIRST -> encodedString.take(endMarking.length).toByteArray()
            }
        }
    }
}

fun String.toUTF16(byteOrder: ByteOrder): ByteArray {
    val byteLength = this.length * 2
    val result = ByteArray(byteLength)

    forEachIndexed { index, character ->
        val code = character.code
        when (byteOrder) {
            ByteOrder.MOST_SIGNIFICANT_FIRST -> {
                result[byteLength - index * 2 - 1] = ((code shr 8) and 0xFF).toByte()
                result[byteLength - index * 2 - 2] = (code and 0xFF).toByte()
            }
            ByteOrder.LEAST_SIGNIFICANT_FIRST -> {
                result[index] = (code and 0xFF).toByte()
                result[index + 1] = ((code shr 8) and 0xFF).toByte()
            }
        }
    }
    return result
}

fun String.toAscii(byteOrder: ByteOrder): ByteArray {
    val result = ByteArray(this.length)
    forEachIndexed { index, character ->
        val code = character.code
        val index = when (byteOrder) {
            ByteOrder.MOST_SIGNIFICANT_FIRST -> length - index - 1
            ByteOrder.LEAST_SIGNIFICANT_FIRST -> index
        }

        if (code > 0x7F) {
            throw IllegalArgumentException("Non-ASCII character: '$character' (0x${code.toString(16)})")
        } else {
            result[index] = code.toByte()
        }
    }
    return result
}

fun String.toAsciiOrNull(byteOrder: ByteOrder): ByteArray? = try {
    toAscii(byteOrder)
} catch (_: IllegalArgumentException) {
    null
}
