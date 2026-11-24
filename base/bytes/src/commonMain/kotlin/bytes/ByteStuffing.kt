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

    /**
     * Whether [byte] is escaped (transformed) when it occurs in the input. This includes the escape byte itself, which
     * — unlike other escaped values — still appears literally in the output as an escape prefix; use [canTerminateWith]
     * to test whether a byte is safe as a terminator.
     */
    fun escapes(byte: Byte): Boolean

    /**
     * Whether [byte] can unambiguously terminate [stuff]ed output appended with it: it is escaped within the content and
     * never emitted literally. Defaults to [escapes]; escape-based schemes additionally exclude their escape byte, which
     * does appear literally.
     */
    fun canTerminateWith(byte: Byte): Boolean = escapes(byte)

    /** Transforms [data] so no escaped byte appears literally. */
    fun stuff(data: ByteArray): ByteArray

    /** Reverses [stuff] over a whole array. */
    fun unstuff(data: ByteArray): ByteArray

    /** The number of bytes [stuff] produces for [data]. */
    fun stuffedSize(data: ByteArray): Int

    /**
     * Lazily stuffs [source] into a folded byte stream — the streaming dual of [stuff]. Each source byte is transformed
     * on demand, so a stuffing layer can be composed over a byte [Sequence] without materialising the whole array.
     */
    fun stuff(source: Sequence<Byte>): Sequence<Byte>

    /**
     * A scheme that escapes each protected byte by replacing it with [escapeByte] followed by a transformed "stored"
     * byte. Covers CSafe, XOR/PPP and SLIP style stuffing.
     */
    sealed class EscapeBased : ByteStuffingScheme {

        sealed class WithDelimiter :
            EscapeBased(),
            DelimiterByteStuffingScheme {
            override fun unstuff(iterator: Iterator<Byte>): ByteArray = collectUnstuffed(iterator) { it == delimiter }
            override fun unstuff(source: Sequence<Byte>): Sequence<Byte> = unstuffed(source.iterator()) { it == delimiter }
        }

        sealed class WithoutDelimiter :
            EscapeBased(),
            NonDelimiterByteStuffingScheme {
            override fun unstuff(iterator: Iterator<Byte>, isDelimiter: (Byte) -> Boolean): ByteArray = collectUnstuffed(iterator, isDelimiter)
            override fun unstuff(source: Sequence<Byte>, isDelimiter: (Byte) -> Boolean): Sequence<Byte> = unstuffed(source.iterator(), isDelimiter)
        }

        /** The byte prepended to an escaped value. It is always itself escaped. */
        abstract val escapeByte: Byte

        /** The byte emitted after [escapeByte] to represent the escaped [original]. */
        protected abstract fun encodeStored(original: Byte): Byte

        /** Reconstructs the original byte from the [stored] byte that followed [escapeByte]. */
        protected abstract fun decodeStored(stored: Byte): Byte

        // The escape byte is escaped but still emitted literally as a prefix, so it cannot serve as a terminator.
        override fun canTerminateWith(byte: Byte): Boolean = escapes(byte) && byte != escapeByte

        override fun stuffedSize(data: ByteArray): Int {
            var size = 0
            for (byte in data) size += if (escapes(byte)) 2 else 1
            return size
        }

        // Emits the stuffed form of one byte — an escape prefix and stored value, or the byte itself. Shared by the
        // eager and folded stuff paths so they cannot diverge.
        private inline fun stuffByte(byte: Byte, emit: (Byte) -> Unit) {
            if (escapes(byte)) {
                emit(escapeByte)
                emit(encodeStored(byte))
            } else {
                emit(byte)
            }
        }

        override fun stuff(data: ByteArray): ByteArray {
            val out = ByteArray(stuffedSize(data))
            var index = 0
            for (byte in data) stuffByte(byte) { out[index++] = it }
            return out
        }

        override fun stuff(source: Sequence<Byte>): Sequence<Byte> = sequence {
            for (byte in source) stuffByte(byte) { yield(it) }
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

        // Lazily un-stuffs [iterator] up to — and consuming — the first unescaped [isDelimiter] byte. Both the folded
        // Sequence un-stuff and the eager array un-stuff run off this single decode loop via [collectUnstuffed].
        protected fun unstuffed(iterator: Iterator<Byte>, isDelimiter: (Byte) -> Boolean): Sequence<Byte> = sequence {
            while (iterator.hasNext()) {
                val byte = iterator.next()
                when {
                    byte == escapeByte -> {
                        if (!iterator.hasNext()) throw ByteStuffingException("Escape byte at end of stuffed data with no value to follow")
                        yield(decodeStored(iterator.next()))
                    }

                    isDelimiter(byte) -> return@sequence

                    else -> yield(byte)
                }
            }
            throw ByteStuffingException("Stuffed data ended before an unescaped delimiter was found")
        }

        protected fun collectUnstuffed(iterator: Iterator<Byte>, isDelimiter: (Byte) -> Boolean): ByteArray {
            val out = GrowableByteArray()
            for (byte in unstuffed(iterator, isDelimiter)) out.append(byte)
            return out.toByteArray()
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

        override fun unstuff(iterator: Iterator<Byte>): ByteArray {
            val out = GrowableByteArray()
            for (byte in unstuff(iterator.asSequence())) out.append(byte)
            return out.toByteArray()
        }

        // Streaming COBS buffers each block of up to 254 non-zero bytes until it knows the code byte (a 0x00 in the
        // input or a full block), so its output is emitted one block at a time.
        override fun stuff(source: Sequence<Byte>): Sequence<Byte> = sequence {
            val block = ArrayList<Byte>(0xFE)
            for (byte in source) {
                if (byte == 0x00.toByte()) {
                    yield((block.size + 1).toByte())
                    yieldAll(block)
                    block.clear()
                } else {
                    block.add(byte)
                    if (block.size == 0xFE) {
                        yield(0xFF.toByte())
                        yieldAll(block)
                        block.clear()
                    }
                }
            }
            yield((block.size + 1).toByte())
            yieldAll(block)
        }

        // Lazily un-stuffs [source] up to — and consuming — the delimiter (a raw 0x00, which COBS output never contains),
        // the folded dual of the eager [unstuff]. Each block's trailing zero is re-inserted unless the delimiter follows it.
        override fun unstuff(source: Sequence<Byte>): Sequence<Byte> = sequence {
            val iterator = source.iterator()
            if (!iterator.hasNext()) throw ByteStuffingException("COBS data ended before its delimiter was found")
            var code = iterator.next().toInt() and 0xFF
            while (code != 0) {
                for (i in 1 until code) {
                    if (!iterator.hasNext()) throw ByteStuffingException("Truncated COBS data")
                    yield(iterator.next())
                }
                if (!iterator.hasNext()) throw ByteStuffingException("COBS data ended before its delimiter was found")
                val next = iterator.next().toInt() and 0xFF
                if (code < 0xFF && next != 0) yield(0)
                code = next
            }
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
    fun unstuff(iterator: Iterator<Byte>): ByteArray

    /**
     * Lazily un-stuffs [source] until an unescaped byte matching [delimiter] (which is consumed), the folded dual of
     * [unstuff]. The returned [Sequence] ends at the delimiter and throws [ByteStuffingException], when consumed,
     * if [source] ends before one is found.
     */
    fun unstuff(source: Sequence<Byte>): Sequence<Byte>
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
    fun unstuff(iterator: Iterator<Byte>, isDelimiter: (Byte) -> Boolean): ByteArray

    /**
     * Lazily un-stuffs [source] until an unescaped byte matching [isDelimiter] (which is consumed), the folded dual of
     * [unstuff]. The returned [Sequence] ends at the delimiter and throws [ByteStuffingException], when consumed,
     * if [source] ends before one is found.
     */
    fun unstuff(source: Sequence<Byte>, isDelimiter: (Byte) -> Boolean): Sequence<Byte>
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
