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

package com.splendo.kaluga.bluetooth.serialization

import com.splendo.kaluga.base.bytes.ByteOrder
import com.splendo.kaluga.base.bytes.buildByteArray
import com.splendo.kaluga.base.bytes.decodeULong
import com.splendo.kaluga.base.bytes.isBitSet
import com.splendo.kaluga.base.utils.toHexString
import kotlinx.serialization.SerializationException

/**
 * Exception thrown if the [ByteArray] provided to [BluetoothFormat] is smaller than expected
 */
class ByteArrayEndedBeforeSerializationCompleted(override val message: String) : SerializationException()

/**
 * Exception thrown if the [ByteArray] provided to [BluetoothFormat] is bigger then expected
 */
class UnexpectedBytesRemaining(override val message: String) : SerializationException()

/**
 * Exception thrown if the Prefix of a structure does not match [Prefix] value
 */
class InvalidPrefix(override val message: String) : SerializationException()

/**
 * Exception thrown if the Postfix of a structure does not match [Postfix] value
 */
class InvalidPostfix(override val message: String) : SerializationException()

internal interface BluetoothBinaryDescriptorDecoder {

    val flags: BooleanArray

    fun beginStructure(binaryDescriptor: BluetoothBinaryDescriptor, flagBitSize: Int = binaryDescriptor.flagBitSize): BluetoothBinaryDescriptorDecoder
    fun endStructure()

    fun isEmpty(): Boolean
    fun peekNextIs(value: ByteArray, consumeIfMatch: Boolean): Boolean
    fun isNextBitSet(): Boolean
    fun nextBytes(size: Int): ByteArray
}

internal class RootBluetoothBinaryDescriptorDecoder(private val byteArray: ByteArray, private val byteOrder: ByteOrder, private val validateChecksum: Boolean) :
    BluetoothBinaryDescriptorDecoder {

    override val flags: BooleanArray = BooleanArray(0)
    private var offset = 0
    private var bitOffset = 0

    override fun isEmpty(): Boolean = !hasAtLeast(1)

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

    fun subArrayFrom(offset: Int) = when (byteOrder) {
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
        if (size + offset > byteArray.size) throw ByteArrayEndedBeforeSerializationCompleted("Not enough bytes remaining")
        return block()
    }

    override fun beginStructure(binaryDescriptor: BluetoothBinaryDescriptor, flagBitSize: Int): BluetoothBinaryDescriptorDecoder = beginStructure(binaryDescriptor, 0, flagBitSize)
    fun beginStructure(binaryDescriptor: BluetoothBinaryDescriptor, parentFooterSize: Int, flagBitSize: Int): StructureBluetoothBinaryDescriptorDecoder {
        // Whenever we start a new structure, we start a new flag byte
        consumeBit()

        // Consume prefix
        binaryDescriptor.structureSettings.prefix?.let { prefixBytes ->
            val actualPrefix = nextBytes(prefixBytes.array.size)
            if (!actualPrefix.contentEquals(prefixBytes.array)) {
                throw InvalidPrefix("Expected Prefix ${prefixBytes.array.toHexString()} but got ${actualPrefix.toHexString()}")
            }
        }

        // Current offset is the start of the body of the structure
        val startingOffset = offset

        // Prepare flags
        val flags = BooleanArray(flagBitSize) {
            isNextBitSet()
        }

        return StructureBluetoothBinaryDescriptorDecoder(binaryDescriptor, this, flags, startingOffset, validateChecksum, parentFooterSize)
    }

    override fun endStructure() {
        if (offset < byteArray.size) {
            throw UnexpectedBytesRemaining("There are ${byteArray.size - offset} bytes remaining after decoding")
        }
    }

    fun hasAtLeast(bytes: Int): Boolean {
        val start = if (bitOffset > 0) offset + 1 else offset
        return (start <= byteArray.size - bytes)
    }
}

/**
 * Exception thrown if the Checksum does not match given [Checksum] algorithm.
 * This check can be disabled using [BluetoothFormat.validateChecksum]
 */
class InvalidChecksumException(val expected: ULong, val actual: ULong) : SerializationException() {
    override val message: String = "Checksum expected $expected but got $actual"
}
internal class StructureBluetoothBinaryDescriptorDecoder(
    val descriptor: BluetoothBinaryDescriptor,
    val rootDecoder: RootBluetoothBinaryDescriptorDecoder,
    override val flags: BooleanArray,
    private val startingOffset: Int,
    private val validateChecksum: Boolean,
    parentFooterSize: Int,
) : BluetoothBinaryDescriptorDecoder {
    private val footerSize = parentFooterSize + (descriptor.structureSettings.postfix?.array?.size ?: 0)

    override fun isEmpty(): Boolean = !rootDecoder.hasAtLeast(footerSize + 1)

    override fun peekNextIs(value: ByteArray, consumeIfMatch: Boolean): Boolean = rootDecoder.peekNextIs(value, consumeIfMatch)
    override fun isNextBitSet(): Boolean = rootDecoder.isNextBitSet()
    override fun nextBytes(size: Int): ByteArray = rootDecoder.nextBytes(size)

    override fun beginStructure(binaryDescriptor: BluetoothBinaryDescriptor, flagBitSize: Int): BluetoothBinaryDescriptorDecoder =
        rootDecoder.beginStructure(binaryDescriptor, footerSize, flagBitSize)
    override fun endStructure() {
        // Consume and validate checksum if available
        descriptor.structureSettings.checksumAlgorithm?.let { crc ->

            // Validation can be disabled
            if (validateChecksum) {
                // Get the subset from startingOffset to currentOffset so we know the part decoded by this structure
                val body = rootDecoder.subArrayFrom(startingOffset)
                // Use the body to compute the checksum. Must be
                val checksum = buildByteArray(descriptor.byteOrder, maxOf(crc.byteWidth, 8)) {
                    add(nextBytes(crc.byteWidth))
                    // Zero pad so we can decode as ULong
                    if (crc.byteWidth < 8) {
                        add(ByteArray(8 - crc.byteWidth))
                    }
                }.decodeULong(0, descriptor.byteOrder)
                val actual = crc.compute(body)
                if (checksum != actual) {
                    throw InvalidChecksumException(checksum, actual)
                }
            } else {
                // Even if not validating, we should still consume the CRC bytes
                nextBytes(crc.byteWidth)
            }
        }

        // Consume postfix
        descriptor.structureSettings.postfix?.let { postfixBytes ->
            val actualPostfix = nextBytes(postfixBytes.array.size)
            if (!actualPostfix.contentEquals(postfixBytes.array)) {
                throw InvalidPostfix("Expected Postfix ${postfixBytes.array.toHexString()} but got ${actualPostfix.toHexString()}")
            }
        }
    }
}
