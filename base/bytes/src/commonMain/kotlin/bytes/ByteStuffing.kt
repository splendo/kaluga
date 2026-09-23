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

/**
 * Thrown when stuffed data is malformed, e.g. an [ByteStuffingScheme.escapeByte] with no value following it.
 */
class ByteStuffingException(message: String) : Exception(message)

/**
 * An escape-based byte-stuffing scheme. Stuffing guarantees that a chosen set of "escaped" byte values never
 * appear literally in the transformed output, so a delimiter drawn from that set can unambiguously mark the end
 * of a region (a null terminator, a frame stop flag, etc.).
 *
 * Each escaped byte is replaced by [escapeByte] followed by a transformed "stored" byte; decoding reverses this.
 */
sealed class ByteStuffingScheme {

    /** The byte prepended to an escaped value. It is always itself escaped. */
    abstract val escapeByte: Byte

    /** Whether [byte] is escaped by this scheme, i.e. never appears literally in [stuff]ed output. */
    abstract fun escapes(byte: Byte): Boolean

    /** The byte emitted after [escapeByte] to represent the escaped [original]. */
    protected abstract fun encodeStored(original: Byte): Byte

    /** Reconstructs the original byte from the [stored] byte that followed [escapeByte]. */
    protected abstract fun decodeStored(stored: Byte): Byte

    /** The number of bytes [stuff] produces for [data]: each escaped byte expands to two. */
    fun stuffedSize(data: ByteArray): Int {
        var size = 0
        for (byte in data) size += if (escapes(byte)) 2 else 1
        return size
    }

    /** Replaces every escaped byte in [data] with [escapeByte] followed by its stored form. */
    fun stuff(data: ByteArray): ByteArray {
        val out = ByteArray(stuffedSize(data))
        var index = 0
        for (byte in data) {
            if (escapes(byte)) {
                out[index++] = escapeByte
                out[index++] = encodeStored(byte)
            } else {
                out[index++] = byte
            }
        }
        return out
    }

    /** Reverses [stuff] over a whole array. */
    fun unstuff(data: ByteArray): ByteArray {
        val out = ByteArray(data.size)
        var outIndex = 0
        var index = 0
        while (index < data.size) {
            val byte = data[index++]
            if (byte == escapeByte) {
                if (index >= data.size) throw ByteStuffingException("Escape byte at end of stuffed data with no value to follow")
                out[outIndex++] = decodeStored(data[index++])
            } else {
                out[outIndex++] = byte
            }
        }
        return out.copyOf(outIndex)
    }

    /**
     * Consumes stuffed bytes from [iterator], un-stuffing them until an unescaped byte matching [isDelimiter]
     * (which is consumed and discarded), and returns the reconstructed content.
     * @throws ByteStuffingException if the iterator ends before an unescaped delimiter is found.
     */
    fun unstuffUntil(iterator: Iterator<Byte>, isDelimiter: (Byte) -> Boolean): ByteArray {
        val out = mutableListOf<Byte>()
        while (iterator.hasNext()) {
            val byte = iterator.next()
            when {
                byte == escapeByte -> {
                    if (!iterator.hasNext()) throw ByteStuffingException("Escape byte at end of stuffed data with no value to follow")
                    out += decodeStored(iterator.next())
                }
                isDelimiter(byte) -> return out.toByteArray()
                else -> out += byte
            }
        }
        throw ByteStuffingException("Stuffed data ended before an unescaped delimiter was found")
    }

    /**
     * A CSafe-style scheme: the escaped values are the contiguous, [mask]-aligned block sharing the high bits of
     * [escapeByte] (`base = escapeByte and mask.inv()`), stored as their low [mask] bits. Defaults escape `0xF0..0xF3`.
     * @property escapeByte the escape marker, itself within the escaped block
     * @property mask the low-bit mask distinguishing escaped values sharing the common high-bit base
     */
    data class CSafe(override val escapeByte: Byte = 0xF3.toByte(), val mask: Byte = 0x03) : ByteStuffingScheme() {
        private val maskInt: Int = mask.toInt() and 0xFF
        private val base: Int = (escapeByte.toInt() and 0xFF) and maskInt.inv()
        override fun escapes(byte: Byte): Boolean = (byte.toInt() and 0xFF) in base..(base or maskInt)
        override fun encodeStored(original: Byte): Byte = (original.toInt() and maskInt).toByte()
        override fun decodeStored(stored: Byte): Byte = (base or (stored.toInt() and maskInt)).toByte()
    }

    /**
     * A PPP/HDLC-style scheme: an explicit set of escaped bytes, each stored XOR-ed with [xorKey]. Because the set is
     * arbitrary it can protect non-contiguous delimiters such as `0x00`. [escapeByte] is always escaped.
     * @property escapeByte the escape marker
     * @property escapedBytes the additional byte values to escape (the delimiter(s) and any others)
     * @property xorKey the value XOR-ed with an escaped byte to produce its stored form (must map escaped bytes out of the set)
     */
    data class Xor(override val escapeByte: Byte, val escapedBytes: Set<Byte>, val xorKey: Byte = 0x20) : ByteStuffingScheme() {
        private val allEscaped: Set<Byte> = escapedBytes + escapeByte
        private val xorInt: Int = xorKey.toInt() and 0xFF
        override fun escapes(byte: Byte): Boolean = byte in allEscaped
        override fun encodeStored(original: Byte): Byte = ((original.toInt() and 0xFF) xor xorInt).toByte()
        override fun decodeStored(stored: Byte): Byte = ((stored.toInt() and 0xFF) xor xorInt).toByte()
    }
}
