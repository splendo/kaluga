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
 * Thrown when stuffed data is malformed, e.g. an escape byte with no value following it or an invalid COBS code.
 */
class ByteStuffingException(message: String) : Exception(message)

/**
 * A byte-stuffing scheme. Stuffing transforms content so that a chosen set of bytes never appear literally in the
 * output, letting a delimiter drawn from that set unambiguously mark the end of a region (a null terminator, a frame
 * flag, etc.).
 *
 * A scheme is either a [DelimiterByteStuffingScheme] (it has a fixed canonical delimiter) or a
 * [NonDelimiterByteStuffingScheme] (a terminator must be chosen explicitly).
 */
sealed interface ByteStuffingScheme {

    /** Whether [byte] never appears literally in [stuff]ed output, so it can serve as a delimiter. */
    fun escapes(byte: Byte): Boolean

    /** Transforms [data] so no escaped byte appears literally. */
    fun stuff(data: ByteArray): ByteArray

    /** Reverses [stuff] over a whole array. */
    fun unstuff(data: ByteArray): ByteArray

    /** The number of bytes [stuff] produces for [data]. */
    fun stuffedSize(data: ByteArray): Int

    /**
     * A scheme that escapes each protected byte by replacing it with [escapeByte] followed by a transformed "stored"
     * byte. Covers CSafe, XOR/PPP and SLIP style stuffing.
     */
    sealed class EscapeBased : ByteStuffingScheme {

        sealed class WithDelimiter :
            EscapeBased(),
            DelimiterByteStuffingScheme {
            override fun unstuffUntil(iterator: Iterator<Byte>): ByteArray = unstuffUntil(iterator) { it == delimiter }
        }

        sealed class WithoutDelimiter :
            EscapeBased(),
            NonDelimiterByteStuffingScheme

        /** The byte prepended to an escaped value. It is always itself escaped. */
        abstract val escapeByte: Byte

        /** The byte emitted after [escapeByte] to represent the escaped [original]. */
        protected abstract fun encodeStored(original: Byte): Byte

        /** Reconstructs the original byte from the [stored] byte that followed [escapeByte]. */
        protected abstract fun decodeStored(stored: Byte): Byte

        override fun stuffedSize(data: ByteArray): Int {
            var size = 0
            for (byte in data) size += if (escapes(byte)) 2 else 1
            return size
        }

        override fun stuff(data: ByteArray): ByteArray {
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

        override fun unstuff(data: ByteArray): ByteArray {
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

        fun unstuffUntil(iterator: Iterator<Byte>, isDelimiter: (Byte) -> Boolean): ByteArray {
            val out = GrowableByteArray()
            while (iterator.hasNext()) {
                val byte = iterator.next()
                when {
                    byte == escapeByte -> {
                        if (!iterator.hasNext()) throw ByteStuffingException("Escape byte at end of stuffed data with no value to follow")
                        out.append(decodeStored(iterator.next()))
                    }

                    isDelimiter(byte) -> return out.toByteArray()

                    else -> out.append(byte)
                }
            }
            throw ByteStuffingException("Stuffed data ended before an unescaped delimiter was found")
        }
    }

    /**
     * A CSafe-style scheme: the escaped values are the contiguous, [mask]-aligned block sharing the high bits of
     * [escapeByte] (`base = escapeByte and mask.inv()`), stored as their low [mask] bits. Defaults escape `0xF0..0xF3`.
     * It has no single canonical delimiter (it frames with a prefix/postfix), so a terminated region needs an explicit terminator.
     * @property escapeByte the escape marker, itself within the escaped block
     * @property mask the low-bit mask distinguishing escaped values sharing the common high-bit base
     */
    data class CSafe(override val escapeByte: Byte = 0xF3.toByte(), val mask: Byte = 0x03) : EscapeBased.WithoutDelimiter() {
        private val maskInt: Int = mask.toInt() and 0xFF
        private val base: Int = (escapeByte.toInt() and 0xFF) and maskInt.inv()
        override fun escapes(byte: Byte): Boolean = (byte.toInt() and 0xFF) in base..(base or maskInt)
        override fun encodeStored(original: Byte): Byte = (original.toInt() and maskInt).toByte()
        override fun decodeStored(stored: Byte): Byte = (base or (stored.toInt() and maskInt)).toByte()
    }

    /**
     * A PPP/HDLC-style scheme: an explicit set of escaped bytes, each stored XOR-ed with [xorKey]. Because the set is
     * arbitrary it can protect non-contiguous delimiters such as `0x00`. [escapeByte] is always escaped. It has no
     * single canonical delimiter, so a terminated region needs an explicit terminator (one of [additionalEscapedBytes]).
     * @property escapeByte the escape marker
     * @property delimiter the delimiter byte to escape
     * @property additionalEscapedBytes the additional byte values to escape (the delimiter(s) and any others)
     * @property xorKey the value XOR-ed with an escaped byte to produce its stored form (must map escaped bytes out of the set)
     */
    data class Xor(override val escapeByte: Byte, override val delimiter: Byte, val additionalEscapedBytes: Set<Byte> = emptySet(), val xorKey: Byte = 0x20) :
        EscapeBased.WithDelimiter() {
        private val allEscaped: Set<Byte> = additionalEscapedBytes + escapeByte + delimiter
        private val xorInt: Int = xorKey.toInt() and 0xFF
        override fun escapes(byte: Byte): Boolean = byte in allEscaped
        override fun encodeStored(original: Byte): Byte = ((original.toInt() and 0xFF) xor xorInt).toByte()
        override fun decodeStored(stored: Byte): Byte = ((stored.toInt() and 0xFF) xor xorInt).toByte()
    }

    /**
     * A SLIP-style scheme (RFC 1055): the frame delimiter [end] and the [escapeByte] are escaped by substitution —
     * [end] becomes `[escapeByte][escapedEnd]` and [escapeByte] becomes `[escapeByte][escapedEsc]`. Defaults are the
     * standard SLIP values (`END=0xC0`, `ESC=0xDB`, `ESC_END=0xDC`, `ESC_ESC=0xDD`). An unrecognised escape decodes
     * to its stored byte unchanged (lenient). Its canonical [delimiter] is [end] (`0xC0`).
     * @property end the frame delimiter byte (its [delimiter]), escaped within the content
     * @property escapeByte the escape marker
     * @property escapedEnd the stored byte representing an escaped [end]
     * @property escapedEsc the stored byte representing an escaped [escapeByte]
     */
    data class Slip(val end: Byte = 0xC0.toByte(), override val escapeByte: Byte = 0xDB.toByte(), val escapedEnd: Byte = 0xDC.toByte(), val escapedEsc: Byte = 0xDD.toByte()) :
        EscapeBased.WithDelimiter() {
        override val delimiter: Byte get() = end
        override fun escapes(byte: Byte): Boolean = byte == end || byte == escapeByte
        override fun encodeStored(original: Byte): Byte = if (original == end) escapedEnd else escapedEsc
        override fun decodeStored(stored: Byte): Byte = when (stored) {
            escapedEnd -> end
            escapedEsc -> escapeByte
            else -> stored
        }
    }

    /**
     * Consistent Overhead Byte Stuffing (Cheshire & Baker). Unlike the escape-based schemes, COBS uses no escape byte:
     * it rewrites the data into blocks prefixed by a code byte so that the output never contains `0x00`, adding only
     * ~1 byte per 254 bytes of overhead. A `0x00` delimiter can therefore follow the output unambiguously — it protects
     * the `0x00` byte and no other, so its [delimiter] is `0x00`.
     */
    data object Cobs : DelimiterByteStuffingScheme {

        override val delimiter: Byte = 0x00
        override fun escapes(byte: Byte): Boolean = byte == 0x00.toByte()

        override fun stuffedSize(data: ByteArray): Int {
            var splits = 0
            var run = 0 // consecutive non-zero bytes
            for (byte in data) {
                if (byte == 0x00.toByte()) {
                    run = 0
                } else if (++run == 0xFE) {
                    splits++ // a full 254-byte run forces an extra code byte
                    run = 0
                }
            }
            return data.size + 1 + splits
        }

        override fun stuff(data: ByteArray): ByteArray {
            val out = ByteArray(stuffedSize(data))
            var outIndex = 1 // out[0] is reserved for the first block's code byte
            var codeIndex = 0
            var code = 1
            for (byte in data) {
                if (byte == 0x00.toByte()) {
                    out[codeIndex] = code.toByte()
                    codeIndex = outIndex++
                    code = 1
                } else {
                    out[outIndex++] = byte
                    code++
                    if (code == 0xFF) {
                        out[codeIndex] = code.toByte()
                        codeIndex = outIndex++
                        code = 1
                    }
                }
            }
            out[codeIndex] = code.toByte()
            return out
        }

        override fun unstuff(data: ByteArray): ByteArray {
            val out = ByteArray(decodedSize(data))
            var outIndex = 0
            var index = 0
            while (index < data.size) {
                val code = data[index++].toInt() and 0xFF // validated by decodedSize
                for (i in 1 until code) out[outIndex++] = data[index++]
                // A block shorter than 0xFF was terminated by a zero in the original; re-insert it unless at the end.
                if (code < 0xFF && index < data.size) out[outIndex++] = 0
            }
            return out
        }

        override fun unstuffUntil(iterator: Iterator<Byte>): ByteArray {
            val encoded = GrowableByteArray()
            while (iterator.hasNext()) {
                val byte = iterator.next()
                if (byte == delimiter) return unstuff(encoded.toByteArray())
                encoded.append(byte)
            }
            throw ByteStuffingException("COBS data ended before its delimiter was found")
        }

        // The exact decoded length, walking only the code bytes (jumping over data), so [unstuff] allocates once.
        private fun decodedSize(data: ByteArray): Int {
            var size = 0
            var index = 0
            while (index < data.size) {
                val code = data[index].toInt() and 0xFF
                if (code == 0) throw ByteStuffingException("Invalid COBS code byte 0x00")
                index += code
                if (index > data.size) throw ByteStuffingException("Truncated COBS data")
                size += code - 1
                if (code < 0xFF && index < data.size) size++
            }
            return size
        }
    }
}

/**
 * A [ByteStuffingScheme] with a fixed canonical [delimiter] byte it always escapes, so a terminated region framed with
 * it needs no explicit terminator. Implemented by [ByteStuffingScheme.Cobs] (`0x00`) and [ByteStuffingScheme.Slip] (its `END`).
 */
sealed interface DelimiterByteStuffingScheme : ByteStuffingScheme {
    val delimiter: Byte

    /**
     * Consumes stuffed bytes from [iterator], un-stuffing them until an unescaped byte matching [delimiter]
     * (which is consumed and discarded), and returns the reconstructed content.
     * @throws ByteStuffingException if the iterator ends before an unescaped delimiter is found.
     */
    fun unstuffUntil(iterator: Iterator<Byte>): ByteArray
}

/**
 * A [ByteStuffingScheme] with no single canonical delimiter, so a terminated region must be given an explicit
 * terminator it escapes. Implemented by [ByteStuffingScheme.CSafe] and [ByteStuffingScheme.Xor].
 */
sealed interface NonDelimiterByteStuffingScheme : ByteStuffingScheme {
    /**
     * Consumes stuffed bytes from [iterator], un-stuffing them until an unescaped byte matching [isDelimiter]
     * (which is consumed and discarded), and returns the reconstructed content.
     * @throws ByteStuffingException if the iterator ends before an unescaped delimiter is found.
     */
    fun unstuffUntil(iterator: Iterator<Byte>, isDelimiter: (Byte) -> Boolean): ByteArray
}

/**
 * A minimal growable, unboxed byte buffer for the streaming un-stuff paths, avoiding the per-byte boxing of a
 * `MutableList<Byte>` on hot decode paths.
 */
private class GrowableByteArray(initialCapacity: Int = 16) {
    private var array = ByteArray(initialCapacity.coerceAtLeast(16))
    private var size = 0

    fun append(byte: Byte) {
        if (size == array.size) array = array.copyOf(array.size * 2)
        array[size++] = byte
    }

    fun toByteArray(): ByteArray = array.copyOf(size)
}
