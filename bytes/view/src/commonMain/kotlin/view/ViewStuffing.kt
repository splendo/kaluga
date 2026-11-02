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
import com.splendo.kaluga.bytes.stuffing.ByteStuffingException
import com.splendo.kaluga.bytes.stuffing.ByteStuffingScheme
import com.splendo.kaluga.bytes.layout.ByteArrayEndedBeforeSerializationCompleted

/**
 * The byte stuffing of generated views, which stuffs and unstuffs exactly as `BytesFormat` does.
 *
 * Stuffed content can't be read in place. A stuffed string or collection ends at the first unescaped terminator, which
 * [end] finds without allocating, and reading it unstuffs a copy. A byte-stuffed root frame is held unstuffed and only
 * stuffed on the wire.
 */
@InternalBytesApi
object ViewStuffing {

    /**
     * The position after the terminator of the content stuffed with [scheme] at [pos], consuming the bytes the decoder
     * does before it unstuffs them.
     * @throws ByteStuffingException when no unescaped [terminator] comes before [lim].
     */
    fun end(b: ByteArray, pos: Int, lim: Int, scheme: ByteStuffingScheme, terminator: Byte): Int {
        ViewBytes.checkRange(pos, 0, lim)
        var index = pos
        when (scheme) {
            is ByteStuffingScheme.EscapeBased -> {
                while (index < lim) {
                    val byte = b[index++]
                    when {
                        byte == scheme.escapeByte -> {
                            if (index >= lim) exhausted("Escape byte at end of stuffed data with no value to follow")
                            index++
                        }

                        byte == terminator -> return index
                    }
                }
                exhausted("Stuffed data ended before an unescaped delimiter was found")
            }

            is ByteStuffingScheme.Cobs -> {
                while (index < lim) {
                    if (b[index++] == terminator) return index
                }
                exhausted("COBS data ended before its delimiter was found")
            }
        }
    }

    /**
     * A copy of the content stuffed with [scheme] at [pos], unstuffed, without its terminator.
     * @throws ByteStuffingException when the content is not validly stuffed or has no terminator before [lim].
     */
    fun unstuff(b: ByteArray, pos: Int, lim: Int, scheme: ByteStuffingScheme, terminator: Byte): ByteArray =
        scheme.unstuff(b.copyOfRange(pos, end(b, pos, lim, scheme, terminator) - 1))

    /** The number of bytes [write] takes for the [content] of a collection. */
    fun sizeOf(content: ByteArray, scheme: ByteStuffingScheme): Int = if (content.isEmpty()) 1 else scheme.stuffedSize(content) + 1

    /**
     * Writes the [content] of a collection at [pos] stuffed with [scheme], then [terminator], returning the number of
     * bytes written. Like `BytesFormat`, which builds nothing for an empty collection, it writes empty content as the
     * terminator alone, even with a scheme that stuffs it into more.
     */
    fun write(b: ByteArray, pos: Int, lim: Int, content: ByteArray, scheme: ByteStuffingScheme, terminator: Byte): Int {
        val stuffed = if (content.isEmpty()) content else scheme.stuff(content)
        ViewBytes.checkRange(pos, stuffed.size + 1, lim)
        stuffed.copyInto(b, pos)
        b[pos + stuffed.size] = terminator
        return stuffed.size + 1
    }

    /**
     * The frame [wire] holds, with the bytes between its [prefixSize] and [postfixSize] bytes unstuffed.
     * @throws ByteArrayEndedBeforeSerializationCompleted when [wire] is too short for its prefix and postfix.
     * @throws ByteStuffingException when its content is not validly stuffed.
     */
    fun fromWire(wire: ByteArray, prefixSize: Int, postfixSize: Int, scheme: ByteStuffingScheme): ByteArray {
        checkFrame(wire, prefixSize, postfixSize)
        return reframe(wire, prefixSize, postfixSize, scheme.unstuff(wire.copyOfRange(prefixSize, wire.size - postfixSize)))
    }

    /** [frame] as sent on the wire: the bytes between its [prefixSize] and [postfixSize] bytes stuffed with [scheme]. */
    fun toWire(frame: ByteArray, prefixSize: Int, postfixSize: Int, scheme: ByteStuffingScheme): ByteArray {
        checkFrame(frame, prefixSize, postfixSize)
        return reframe(frame, prefixSize, postfixSize, scheme.stuff(frame.copyOfRange(prefixSize, frame.size - postfixSize)))
    }

    // The decoder unstuffs up to the end of the body, before any checksum and postfix, and fails to unstuff there.
    private fun exhausted(message: String): Nothing = throw ByteStuffingException(message)

    private fun checkFrame(bytes: ByteArray, prefixSize: Int, postfixSize: Int) {
        if (bytes.size < prefixSize + postfixSize) {
            throw ByteArrayEndedBeforeSerializationCompleted("Frame of ${bytes.size} bytes is too small for its ${prefixSize + postfixSize}-byte prefix and postfix")
        }
    }

    private fun reframe(bytes: ByteArray, prefixSize: Int, postfixSize: Int, body: ByteArray): ByteArray {
        val framed = ByteArray(prefixSize + body.size + postfixSize)
        bytes.copyInto(framed, 0, 0, prefixSize)
        body.copyInto(framed, prefixSize)
        bytes.copyInto(framed, prefixSize + body.size, bytes.size - postfixSize, bytes.size)
        return framed
    }
}

/**
 * The contents of the stuffed collections of a frame, built while sizing it and taken, in the same order, while
 * writing it, so each is built once per encode.
 */
@InternalBytesApi
class ViewContents {
    private var contents = arrayOfNulls<ByteArray>(4)
    private var added = 0
    private var taken = 0

    /** Keeps [content] for the write that follows. */
    fun add(content: ByteArray) {
        if (added == contents.size) contents = contents.copyOf(contents.size * 2)
        contents[added++] = content
    }

    /** The next content kept by [add], or `null` when there is none. */
    fun take(): ByteArray? = if (taken < added) contents[taken].also { contents[taken++] = null } else null
}
