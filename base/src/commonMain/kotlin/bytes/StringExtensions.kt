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
data class StringEncodingSettings(val endMarking: EndMarking = LengthPrefix(), val encoding: Encoding = Encoding.UTF_8) {

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

fun ByteArray.decodeString(settings: StringEncodingSettings, order: ByteOrder) = when (order) {
    ByteOrder.MOST_SIGNIFICANT_FIRST -> reversedArray()
    ByteOrder.LEAST_SIGNIFICANT_FIRST -> this
}.asSequence().constrainOnce().decodeString(settings)

private sealed class LengthPrefixDecodingState {

    data class Ready(val current: Byte, val knownLength: KnownLength) : LengthPrefixDecodingState()
    sealed class NotReady : LengthPrefixDecodingState()
    data class KnownLength(val length: Int, val offset: Int) : NotReady()
    class UnknownLength(val prefix: ByteArray) : NotReady()
}
fun Sequence<Byte>.decodeString(settings: StringEncodingSettings): String {
    val terminatingSequence = when (val endMarking = settings.endMarking) {
        is StringEncodingSettings.LengthPrefix -> {
            runningFold<Byte, LengthPrefixDecodingState>(LengthPrefixDecodingState.UnknownLength(byteArrayOf())) { state, byte ->
                when (state) {
                    is LengthPrefixDecodingState.Ready -> state.copy(current = byte)
                    is LengthPrefixDecodingState.KnownLength -> LengthPrefixDecodingState.Ready(byte, state)
                    is LengthPrefixDecodingState.UnknownLength -> {
                        when {
                            endMarking.lengthAsShort && !state.prefix.isEmpty() -> {
                                LengthPrefixDecodingState.KnownLength(
                                    (state.prefix + byte).decodeUShort(0, ByteOrder.LEAST_SIGNIFICANT_FIRST).toInt(),
                                    2,
                                )
                            }
                            endMarking.lengthAsShort -> LengthPrefixDecodingState.UnknownLength(state.prefix + byte)
                            endMarking.canOverflow && state.prefix.size == 2 ->
                                LengthPrefixDecodingState.KnownLength(
                                    byteArrayOf(state.prefix[0], byte).decodeUShort(0, ByteOrder.LEAST_SIGNIFICANT_FIRST).toInt(),
                                    3,
                                )
                            endMarking.canOverflow && state.prefix.size == 1 -> LengthPrefixDecodingState.UnknownLength(state.prefix + byte)
                            endMarking.canOverflow && byte == endMarking.sentinel -> LengthPrefixDecodingState.UnknownLength(byteArrayOf(byte))
                            else -> LengthPrefixDecodingState.KnownLength(
                                byte.toUByte().toInt(),
                                1,
                            )
                        }
                    }
                }
            }.withIndex().takeWhile { (index, state) ->
                when (state) {
                    is LengthPrefixDecodingState.Ready -> index <= (settings.encoding.byteSize * state.knownLength.length) + state.knownLength.offset
                    is LengthPrefixDecodingState.NotReady -> true
                }
            }.mapNotNull { (_, state) ->
                when (state) {
                    is LengthPrefixDecodingState.Ready -> state.current
                    is LengthPrefixDecodingState.NotReady -> null
                }
            }
        }
        is StringEncodingSettings.FixedLength -> {
            take(settings.encoding.byteSize * endMarking.length)
        }
        is StringEncodingSettings.NullTerminated -> {
            withIndex().takeWhile { (index, byte) -> index % 2 != 0 || byte != 0x00.toByte() }.map { (_, byte) -> byte }
        }
        is StringEncodingSettings.NoMarking -> {
            this
        }
    }
    var result = ""
    val iterator = terminatingSequence.iterator()
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
