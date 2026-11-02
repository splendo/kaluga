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
import com.splendo.kaluga.bytes.layout.ChecksumSpec
import com.splendo.kaluga.bytes.layout.InvalidChecksumException
import com.splendo.kaluga.bytes.layout.InvalidPostfix
import com.splendo.kaluga.bytes.layout.InvalidPrefix
import com.splendo.kaluga.bytes.toHexString

/**
 * The checksums and frame markers of generated views, as `BytesFormat` writes and checks them: a checksum covers a
 * frame's body, everything between its prefix and the checksum itself, and is stored as a number of
 * [ChecksumSpec.crc]'s byte width in the frame's value byte order.
 */
@InternalBytesApi
object ViewChecksum {

    /** The checksum of `b[from, to)`. */
    fun compute(b: ByteArray, from: Int, to: Int, spec: ChecksumSpec): ULong {
        ViewBytes.checkRange(from, to - from, b.size)
        return spec.crc.compute(b, from, to)
    }

    /** The checksum stored at [pos]. */
    fun read(b: ByteArray, pos: Int, spec: ChecksumSpec, order: ByteOrder, lim: Int): ULong = ViewBytes.readUnsigned(b, pos, spec.crc.byteWidth, order, lim).toULong()

    /**
     * Computes the checksum of `b[from, to)` and stores it at [at]: right after the body, or right before it in a
     * frame accumulated MOST_SIGNIFICANT_FIRST.
     */
    fun update(b: ByteArray, from: Int, to: Int, spec: ChecksumSpec, order: ByteOrder, lim: Int, at: Int = to) {
        val width = spec.crc.byteWidth
        ViewBytes.checkRange(at, width, lim)
        val value = compute(b, from, to, spec)
        for (index in 0 until width) {
            val byte = (value shr (index * Byte.SIZE_BITS)).toByte()
            when (order) {
                ByteOrder.LEAST_SIGNIFICANT_FIRST -> b[at + index] = byte
                ByteOrder.MOST_SIGNIFICANT_FIRST -> b[at + width - 1 - index] = byte
            }
        }
    }

    /**
     * Checks the checksum stored at [at] against the one of `b[from, to)`.
     * @throws InvalidChecksumException, as `BytesFormat` does, when they differ.
     */
    fun check(b: ByteArray, from: Int, to: Int, spec: ChecksumSpec, order: ByteOrder, lim: Int, at: Int = to) {
        val stored = read(b, at, spec, order, lim)
        val actual = compute(b, from, to, spec)
        if (stored != actual) throw InvalidChecksumException(stored, actual)
    }

    /** @throws InvalidPrefix, as `BytesFormat` does, unless the bytes at [pos] are [prefix]. */
    fun checkPrefix(b: ByteArray, pos: Int, prefix: ByteArray, lim: Int) {
        ViewBytes.checkRange(pos, prefix.size, lim)
        if (!ViewBytes.matches(b, pos, prefix, lim)) {
            throw InvalidPrefix("Expected Prefix ${prefix.toHexString()} but got ${b.copyOfRange(pos, pos + prefix.size).toHexString()}")
        }
    }

    /** @throws InvalidPostfix, as `BytesFormat` does, unless the bytes at [pos] are [postfix]. */
    fun checkPostfix(b: ByteArray, pos: Int, postfix: ByteArray, lim: Int) {
        ViewBytes.checkRange(pos, postfix.size, lim)
        if (!ViewBytes.matches(b, pos, postfix, lim)) {
            throw InvalidPostfix("Expected Postfix ${postfix.toHexString()} but got ${b.copyOfRange(pos, pos + postfix.size).toHexString()}")
        }
    }
}

/**
 * Notified after a view nested in another was written to, so the enclosing views can update their checksums.
 */
@InternalBytesApi
fun interface ViewParent {
    fun changed()
}

/** The parent of the views an `edit` block writes through: they leave updating checksums to the end of the block. */
@InternalBytesApi
object ViewBatch : ViewParent {
    override fun changed() = Unit
}
