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
import com.splendo.kaluga.bytes.strings.StringEncodingSettings
import com.splendo.kaluga.bytes.strings.byteArraySize
import com.splendo.kaluga.bytes.strings.copyIntoArray
import com.splendo.kaluga.bytes.strings.decodeCharacters
import com.splendo.kaluga.bytes.strings.decodeString
import com.splendo.kaluga.bytes.layout.ByteArrayEndedBeforeSerializationCompleted

/**
 * The strings of generated views, encoded and decoded exactly as `BytesFormat` does.
 *
 * A string is read from the bytes at `pos` up to `lim`, one byte at a time through the same decoder `BytesFormat`
 * uses, so its end markings, and the errors it raises for a truncated or unterminated string, are the same. A
 * byte-stuffed string ends at its first unescaped terminator, found with [ViewStuffing.end].
 */
@InternalBytesApi
object ViewStrings {

    /**
     * Decodes the string at [pos]. Unlike every other view read, this allocates: the [String] it returns, and for a
     * byte-stuffed string an unstuffed copy of its bytes.
     * @throws ByteArrayEndedBeforeSerializationCompleted when the string does not end before [lim].
     */
    fun read(b: ByteArray, pos: Int, lim: Int, settings: StringEncodingSettings): String {
        ViewBytes.checkRange(pos, 0, lim)
        return when (val endMarking = settings.endMarking) {
            is StringEncodingSettings.ByteStuffed -> {
                val content = ViewStuffing.unstuff(b, pos, lim, endMarking.scheme, endMarking.terminator)
                decoding { content.decodeCharacters(0, content.size, settings.encoding) }
            }

            else -> {
                val size = size(b, pos, lim, settings)
                val from = if (endMarking is StringEncodingSettings.LengthPrefix) pos + lengthPrefixSize(b, pos, endMarking) else pos
                val to = if (endMarking is StringEncodingSettings.WithTerminal) pos + size - 1 else pos + size
                decoding { b.decodeCharacters(from, to, settings.encoding, trimTrailingNulls = endMarking is StringEncodingSettings.FixedLength) }
            }
        }
    }

    /**
     * Checks the string at [pos] as [read] decodes it, without building the [String]: its end marking, and that no
     * character is cut off.
     * @throws ByteArrayEndedBeforeSerializationCompleted when [read] would.
     */
    fun check(b: ByteArray, pos: Int, lim: Int, settings: StringEncodingSettings) {
        val endMarking = settings.endMarking
        if (endMarking is StringEncodingSettings.ByteStuffed) {
            read(b, pos, lim, settings)
            return
        }
        val size = size(b, pos, lim, settings)
        val from = if (endMarking is StringEncodingSettings.LengthPrefix) pos + lengthPrefixSize(b, pos, endMarking) else pos
        val to = if (endMarking is StringEncodingSettings.WithTerminal) pos + size - 1 else pos + size
        when (settings.encoding) {
            Encoding.ASCII -> Unit

            Encoding.UTF_16 -> if ((to - from) % 2 != 0) throw ByteArrayEndedBeforeSerializationCompleted("No byte at $to")

            Encoding.UTF_8 -> {
                var index = from
                while (index < to) {
                    val lead = b[index].toInt() and 0xFF
                    index += when {
                        lead < 0x80 -> 1
                        lead < 0xE0 -> 2
                        lead < 0xF0 -> 3
                        else -> 4
                    }
                    if (index > to) throw ByteArrayEndedBeforeSerializationCompleted("Truncated UTF-8 sequence: missing continuation byte")
                }
            }
        }
    }

    // The bytes of a length prefix the decoder has read at [pos].
    private fun lengthPrefixSize(b: ByteArray, pos: Int, endMarking: StringEncodingSettings.LengthPrefix): Int = when (endMarking) {
        is StringEncodingSettings.LengthPrefix.ByteLength -> 1
        is StringEncodingSettings.LengthPrefix.ShortLength -> 2
        is StringEncodingSettings.LengthPrefix.WithOverflow -> if (b[pos] == endMarking.sentinel) 3 else 1
    }

    /**
     * The number of bytes the string at [pos] takes, as `BytesFormat` consumes them while decoding it, without
     * decoding its characters.
     * @throws ByteArrayEndedBeforeSerializationCompleted when the string does not end before [lim].
     */
    fun size(b: ByteArray, pos: Int, lim: Int, settings: StringEncodingSettings): Int {
        ViewBytes.checkRange(pos, 0, lim)
        (settings.endMarking as? StringEncodingSettings.ByteStuffed)?.let { return ViewStuffing.end(b, pos, lim, it.scheme, it.terminator) - pos }
        return size(lim - pos, settings) { b[pos + it] }
    }

    /**
     * [read] in a frame accumulated MOST_SIGNIFICANT_FIRST, which the decoder reads from the end: the string's bytes
     * are those before [pos], last first, down to [lim].
     */
    fun readDown(b: ByteArray, pos: Int, lim: Int, settings: StringEncodingSettings): String {
        ViewBytes.checkRange(lim, pos - lim, b.size)
        var index = pos
        val next = { if (index > lim) b[--index] else null }
        return decoding { generateSequence(next) { next() }.decodeString(settings) }
    }

    /** [size] in a frame accumulated MOST_SIGNIFICANT_FIRST; see [readDown]. */
    fun sizeDown(b: ByteArray, pos: Int, lim: Int, settings: StringEncodingSettings): Int {
        ViewBytes.checkRange(lim, pos - lim, b.size)
        return size(pos - lim, settings) { b[pos - 1 - it] }
    }

    // [byte] is the string's byte at an index in the order the decoder reads them.
    private inline fun size(available: Int, settings: StringEncodingSettings, byte: (Int) -> Byte): Int = decoding {
        when (val endMarking = settings.endMarking) {
            is StringEncodingSettings.LengthPrefix -> {
                require(available >= 1) { "Did not include a length" }
                // Like the decoder, a one byte length is unsigned and a short one is read least significant first.
                val overflows = endMarking is StringEncodingSettings.LengthPrefix.WithOverflow && byte(0) == endMarking.sentinel
                val prefix = when {
                    endMarking is StringEncodingSettings.LengthPrefix.ShortLength -> 2
                    overflows -> 3
                    else -> 1
                }
                val count = when {
                    endMarking is StringEncodingSettings.LengthPrefix.ShortLength -> shortLength(available, 0, byte)
                    overflows -> shortLength(available, 1, byte)
                    else -> byte(0).toInt() and 0xFF
                }
                val length = count * settings.encoding.byteSize
                require(length >= 0) { "Requested element count $length is less than zero." }
                require(prefix + length <= available) { "String size does not match encoded size $length" }
                prefix + length
            }

            is StringEncodingSettings.FixedLength -> {
                val length = settings.encoding.byteSize * endMarking.length
                require(length <= available) { "String does not match fixed size $length" }
                length
            }

            is StringEncodingSettings.ByteStuffed -> throw UnsupportedOperationException("BytesFormat only stuffs strings encoded LEAST_SIGNIFICANT_FIRST")

            is StringEncodingSettings.WithTerminal -> {
                val allowUtf16NullByte = endMarking is StringEncodingSettings.NullTerminated && settings.encoding == Encoding.UTF_16
                var index = 0
                var found = -1
                while (index < available && found < 0) {
                    if (!((allowUtf16NullByte && index % 2 != 0) || byte(index) != endMarking.terminator)) found = index + 1
                    index++
                }
                require(found >= 0) { "Does not end with the terminator byte" }
                found
            }

            is StringEncodingSettings.NoMarking -> available
        }
    }

    /** The number of bytes [value] encodes to. */
    fun sizeOf(value: String, settings: StringEncodingSettings): Int = value.byteArraySize(settings)

    /** [write] into a frame accumulated MOST_SIGNIFICANT_FIRST: [value] ends at [pos], encoded in [order]. */
    fun writeDown(b: ByteArray, pos: Int, value: String, settings: StringEncodingSettings, order: ByteOrder) {
        val size = value.byteArraySize(settings)
        write(b, pos - size, pos, value, settings, order)
    }

    /**
     * Encodes [value] at [pos] as `BytesFormat` does.
     * @throws IllegalArgumentException, as `BytesFormat` does, when [value] cannot be encoded with [settings].
     */
    fun write(b: ByteArray, pos: Int, lim: Int, value: String, settings: StringEncodingSettings, order: ByteOrder) {
        ViewBytes.checkRange(pos, value.byteArraySize(settings), lim)
        value.copyIntoArray(b, settings, pos, order)
    }

    private inline fun shortLength(available: Int, at: Int, byte: (Int) -> Byte): Int {
        require(available >= at + 2) { "Did not include a Short as length" }
        return (byte(at).toInt() and 0xFF) or ((byte(at + 1).toInt() and 0xFF) shl Byte.SIZE_BITS)
    }

    // The decoder reports a string it cannot read as the input ending early.
    private inline fun <T> decoding(block: () -> T): T = try {
        block()
    } catch (e: IllegalArgumentException) {
        throw ByteArrayEndedBeforeSerializationCompleted(e.message.orEmpty())
    } catch (e: NoSuchElementException) {
        throw ByteArrayEndedBeforeSerializationCompleted(e.message.orEmpty())
    }
}
