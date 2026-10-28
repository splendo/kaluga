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

package com.splendo.kaluga.bytes.serialization

import com.splendo.kaluga.bytes.ByteOrder
import com.splendo.kaluga.bytes.buildByteArray
import com.splendo.kaluga.bytes.decodeULong
import com.splendo.kaluga.bytes.isBitSet
import com.splendo.kaluga.bytes.toHexString
import com.splendo.kaluga.bytes.layout.ByteArrayEndedBeforeSerializationCompleted
import com.splendo.kaluga.bytes.layout.InvalidChecksumException
import com.splendo.kaluga.bytes.layout.InvalidPostfix
import com.splendo.kaluga.bytes.layout.InvalidPrefix
import com.splendo.kaluga.bytes.layout.UnexpectedBytesRemaining

internal interface BinaryDescriptorDecoder {

    val flags: BooleanArray
    val currentOffset: Int
    val validateChecksum: Boolean

    fun subArrayFrom(offset: Int): ByteArray

    fun beginStructure(binaryDescriptor: BinaryDescriptor, flagBitSize: Int = binaryDescriptor.flagBitSize): BinaryDescriptorDecoder
    fun endStructure()

    fun isEmpty(): Boolean
    fun remainingPayloadBytes(): Int
    fun peekNextIs(value: ByteArray, consumeIfMatch: Boolean): Boolean
    fun isNextBitSet(): Boolean
    fun nextBytes(size: Int): ByteArray
}

internal class RootBinaryDescriptorDecoder(private val byteArray: ByteArray, private val byteOrder: ByteOrder, override val validateChecksum: Boolean) :
    BinaryDescriptorDecoder {

    override val flags: BooleanArray = BooleanArray(0)
    private var offset = 0
    private var bitOffset = 0
    override val currentOffset: Int get() = offset

    override fun isEmpty(): Boolean = !hasAtLeast(1)

    override fun remainingPayloadBytes(): Int = byteArray.size - offset

    override fun peekNextIs(value: ByteArray, consumeIfMatch: Boolean): Boolean = if (hasAtLeast(value.size)) {
        val start = if (bitOffset > 0) offset + 1 else offset
        when (byteOrder) {
            ByteOrder.MOST_SIGNIFICANT_FIRST -> byteArray.copyOfRange(byteArray.size - start - value.size, byteArray.size - start)
            ByteOrder.LEAST_SIGNIFICANT_FIRST -> byteArray.copyOfRange(start, start + value.size)
        }.contentEquals(value).also { match ->
            if (match && consumeIfMatch) {
                offset += value.size
            }
        }
    } else {
        false
    }

    override fun isNextBitSet(): Boolean {
        if (bitOffset >= Byte.SIZE_BITS) {
            consumeBit()
        }
        return when (byteOrder) {
            ByteOrder.MOST_SIGNIFICANT_FIRST -> byteArray[byteArray.size - offset - 1]
            ByteOrder.LEAST_SIGNIFICANT_FIRST -> byteArray[offset]
        }.isBitSet(bitOffset++)
    }

    override fun nextBytes(size: Int): ByteArray = ensureAvailable(size) {
        consumeBit()
        when (byteOrder) {
            ByteOrder.MOST_SIGNIFICANT_FIRST -> byteArray.copyOfRange(byteArray.size - offset - size, byteArray.size - offset)
            ByteOrder.LEAST_SIGNIFICANT_FIRST -> byteArray.copyOfRange(offset, offset + size)
        }.also {
            offset += size
        }
    }

    override fun subArrayFrom(offset: Int) = when (byteOrder) {
        ByteOrder.MOST_SIGNIFICANT_FIRST -> byteArray.copyOfRange(byteArray.size - this.offset, byteArray.size - offset)
        ByteOrder.LEAST_SIGNIFICANT_FIRST -> byteArray.copyOfRange(offset, this.offset)
    }

    private fun consumeBit() {
        if (bitOffset > 0) {
            bitOffset = 0
            nextBytes(1)
        }
    }

    private fun <T> ensureAvailable(size: Int, block: () -> T): T {
        // A negative size is a frame too short for the footer it was asked to leave unread.
        if (size < 0 || size + offset > byteArray.size) throw ByteArrayEndedBeforeSerializationCompleted("Not enough bytes remaining")
        return block()
    }

    override fun beginStructure(binaryDescriptor: BinaryDescriptor, flagBitSize: Int): BinaryDescriptorDecoder = beginStructure(binaryDescriptor, 0, flagBitSize)
    fun beginStructure(binaryDescriptor: BinaryDescriptor, parentFooterSize: Int, flagBitSize: Int): StructureBinaryDescriptorDecoder {
        // Whenever we start a new structure, we start a new flag byte
        consumeBit()

        consumePrefix(binaryDescriptor)

        // Current offset is the start of the body of the structure
        val startingOffset = offset

        // Prepare flags
        val flags = BooleanArray(flagBitSize) {
            isNextBitSet()
        }

        return StructureBinaryDescriptorDecoder(binaryDescriptor, this, flags, startingOffset, parentFooterSize)
    }

    override fun endStructure() {
        consumeBit()
        if (offset < byteArray.size) {
            throw UnexpectedBytesRemaining("There are ${byteArray.size - offset} bytes remaining after decoding")
        }
    }

    fun hasAtLeast(bytes: Int): Boolean {
        val start = if (bitOffset > 0) offset + 1 else offset
        return (start <= byteArray.size - bytes)
    }
}

internal class StructureBinaryDescriptorDecoder(
    val descriptor: BinaryDescriptor,
    val rootDecoder: RootBinaryDescriptorDecoder,
    override val flags: BooleanArray,
    private val startingOffset: Int,
    parentFooterSize: Int,
) : BinaryDescriptorDecoder {
    // Bytes trailing this structure's body that belong to the frame rather than its fields. The [Checksum] counts
    // towards it just like the [Postfix]: an @Unsized field must stop before it, and @SizePolymorphic dispatch
    // must not include it in the payload size it dispatches on.
    private val footerSize = parentFooterSize +
        (descriptor.structureSettings.checksumAlgorithm?.byteWidth ?: 0) +
        (descriptor.structureSettings.postfix?.array?.size ?: 0)

    override val currentOffset: Int get() = rootDecoder.currentOffset
    override val validateChecksum: Boolean get() = rootDecoder.validateChecksum
    override fun subArrayFrom(offset: Int): ByteArray = rootDecoder.subArrayFrom(offset)

    override fun isEmpty(): Boolean = !rootDecoder.hasAtLeast(footerSize + 1)

    override fun remainingPayloadBytes(): Int = rootDecoder.remainingPayloadBytes() - footerSize

    override fun peekNextIs(value: ByteArray, consumeIfMatch: Boolean): Boolean = rootDecoder.peekNextIs(value, consumeIfMatch)
    override fun isNextBitSet(): Boolean = rootDecoder.isNextBitSet()
    override fun nextBytes(size: Int): ByteArray = rootDecoder.nextBytes(size)

    override fun beginStructure(binaryDescriptor: BinaryDescriptor, flagBitSize: Int): BinaryDescriptorDecoder =
        rootDecoder.beginStructure(binaryDescriptor, footerSize, flagBitSize)
    override fun endStructure() {
        // Consume and validate checksum if available
        validateChecksum(descriptor, rootDecoder, startingOffset)
        consumePostfix(descriptor)
    }
}

/**
 * Reads bytes one at a time via [BinaryDescriptorDecoder.nextBytes] until the decoder [is empty][BinaryDescriptorDecoder.isEmpty].
 * Used to feed a byte-stuffed region to [com.splendo.kaluga.bytes.stuffing.DelimiterByteStuffingScheme.unstuff].
 */
internal fun BinaryDescriptorDecoder.byteIterator(): Iterator<Byte> = object : Iterator<Byte> {
    override fun hasNext(): Boolean = !isEmpty()
    override fun next(): Byte = nextBytes(1)[0]
}

internal fun BinaryDescriptorDecoder.consumePrefix(descriptor: BinaryDescriptor) {
    descriptor.structureSettings.prefix?.let { prefixBytes ->
        val actual = nextBytes(prefixBytes.array.size)
        if (!actual.contentEquals(prefixBytes.array)) {
            throw InvalidPrefix("Expected Prefix ${prefixBytes.array.toHexString()} but got ${actual.toHexString()}")
        }
    }
}

internal fun BinaryDescriptorDecoder.consumePostfix(descriptor: BinaryDescriptor) {
    descriptor.structureSettings.postfix?.let { postfixBytes ->
        val actual = nextBytes(postfixBytes.array.size)
        if (!actual.contentEquals(postfixBytes.array)) {
            throw InvalidPostfix("Expected Postfix ${postfixBytes.array.toHexString()} but got ${actual.toHexString()}")
        }
    }
}

internal fun BinaryDescriptorDecoder.validateChecksum(descriptor: BinaryDescriptor, rootDecoder: BinaryDescriptorDecoder, startingOffset: Int) {
    // Validation can be disabled
    descriptor.structureSettings.checksumAlgorithm?.let { crc ->
        if (validateChecksum) {
            // Get the subset from startingOffset to currentOffset so we know the part decoded by this structure
            val body = rootDecoder.subArrayFrom(startingOffset)
            // Use the body to compute the checksum. Must be
            val checksum = buildByteArray(descriptor.childByteOrder, maxOf(crc.byteWidth, 8)) {
                add(nextBytes(crc.byteWidth))
                // Zero pad so we can decode as ULong
                if (crc.byteWidth < 8) {
                    add(ByteArray(8 - crc.byteWidth))
                }
            }.decodeULong(0, descriptor.childByteOrder)
            val actual = crc.compute(body)
            if (checksum != actual) {
                throw InvalidChecksumException(checksum, actual)
            }
        } else {
            // Even if not validating, we should still consume the CRC bytes
            nextBytes(crc.byteWidth)
        }
    }
}
