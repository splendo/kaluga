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
import com.splendo.kaluga.base.bytes.isBitSet
import com.splendo.kaluga.base.utils.toHexString
import kotlinx.serialization.SerializationException

class ByteArrayEndedBeforeSerializationCompleted(override val message: String) : SerializationException()
class UnexpectedBytesRemaining(override val message: String) : SerializationException()

class InvalidPrefix(override val message: String) : SerializationException()
class InvalidPostfix(override val message: String) : SerializationException()

internal interface BluetoothBinaryDescriptorDecoder {

    val flags: List<Boolean>

    fun beginStructure(binaryDescriptor: BluetoothBinaryDescriptor, flagBitSize: Int = binaryDescriptor.flagBitSize): BluetoothBinaryDescriptorDecoder
    fun endStructure()

    fun isEmpty(): Boolean
    fun peekNextIs(value: ByteArray): Boolean
    fun isNextBitSet(): Boolean
    fun nextBytes(size: Int): ByteArray
}

internal class RootBluetoothBinaryDescriptorDecoder(private val byteArray: ByteArray, private val byteOrder: ByteOrder) : BluetoothBinaryDescriptorDecoder {

    override val flags: List<Boolean> = emptyList()
    private var offset = 0
    private var bitOffset = 0

    override fun isEmpty(): Boolean = !hasAtLeast(1)

    override fun peekNextIs(value: ByteArray): Boolean = if (hasAtLeast(value.size)) {
        val start = if (bitOffset > 0) offset + 1 else offset
        when (byteOrder) {
            ByteOrder.MOST_SIGNIFICANT_FIRST -> byteArray.copyOfRange(byteArray.size - start - value.size - 1, byteArray.size - start)
            ByteOrder.LEAST_SIGNIFICANT_FIRST -> byteArray.copyOfRange(start, start + value.size)
        }.contentEquals(value)
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
        ByteOrder.MOST_SIGNIFICANT_FIRST -> byteArray.copyOfRange(byteArray.size - this.offset - 1, byteArray.size - offset)
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
        binaryDescriptor.blockSettings.prefix?.let { prefixBytes ->
            val actualPrefix = nextBytes(prefixBytes.array.size)
            if (!actualPrefix.contentEquals(prefixBytes.array)) {
                throw InvalidPrefix("Expected Prefix ${prefixBytes.array.toHexString()} but got ${actualPrefix.toHexString()}")
            }
        }
        consumeBit()
        val startingOffset = offset
        val flags = MutableList(flagBitSize) {
            isNextBitSet()
        }

        return StructureBluetoothBinaryDescriptorDecoder(binaryDescriptor, this, flags, startingOffset, parentFooterSize)
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

internal class StructureBluetoothBinaryDescriptorDecoder(
    val descriptor: BluetoothBinaryDescriptor,
    val rootDecoder: RootBluetoothBinaryDescriptorDecoder,
    override val flags: List<Boolean>,
    private val startingOffset: Int,
    parentFooterSize: Int,
) : BluetoothBinaryDescriptorDecoder {
    private val footerSize = parentFooterSize + (descriptor.blockSettings.postfix?.array?.size ?: 0)

    override fun isEmpty(): Boolean = !rootDecoder.hasAtLeast(footerSize + 1)

    override fun peekNextIs(value: ByteArray): Boolean = rootDecoder.peekNextIs(value)
    override fun isNextBitSet(): Boolean = rootDecoder.isNextBitSet()
    override fun nextBytes(size: Int): ByteArray = rootDecoder.nextBytes(size)

    override fun beginStructure(binaryDescriptor: BluetoothBinaryDescriptor, flagBitSize: Int): BluetoothBinaryDescriptorDecoder =
        rootDecoder.beginStructure(binaryDescriptor, footerSize, flagBitSize)
    override fun endStructure() {
        when (descriptor.blockSettings.checksumAlgorithm) {
            ChecksumAlgorithm.NONE -> {}
            ChecksumAlgorithm.CRC16 -> {
                val bytesToValidate = rootDecoder.subArrayFrom(startingOffset)
                // TODO: Validate
            }
            ChecksumAlgorithm.CRC32 -> {
                val bytesToValidate = rootDecoder.subArrayFrom(startingOffset)
                // TODO: Validate
            }
        }
        descriptor.blockSettings.postfix?.let { postfixBytes ->
            val actualPostfix = nextBytes(postfixBytes.array.size)
            if (!actualPostfix.contentEquals(postfixBytes.array)) {
                throw InvalidPostfix("Expected Postfix ${postfixBytes.array.toHexString()} but got ${actualPostfix.toHexString()}")
            }
        }
    }
}
