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

import com.splendo.kaluga.base.bytes.ByteArrayBuilder
import com.splendo.kaluga.base.bytes.ByteOrder
import com.splendo.kaluga.base.bytes.buildByteArray
import com.splendo.kaluga.base.bytes.toByteArray
import kotlinx.serialization.SerializationException
import kotlin.math.ceil

/**
 * Builder for creating a [ByteArray] from a [BluetoothBinaryDescriptor]
 */
internal interface BinaryBuilder {

    val expectedSize: Int

    fun addFlag(index: Int, value: Boolean)
    fun addBit(value: Boolean)
    fun addAction(expectedSize: Int, action: ByteArrayBuilder.() -> Unit)
    fun makeUnconstrained()

    fun ByteArrayBuilder.build()
}

/**
 * Exception thrown if trying to add data after a collection of data marked with [Unsized].
 * Since [Unsized] elements do not indicate an end point, there is no way to know when to stop decoding them.
 */
class DataAfterUnconstrainedData(override val message: String?) : SerializationException()

/**
 * A [BinaryBuilder] to build data for a single [BluetoothBinaryDescriptor] structure
 */
internal abstract class StructureBinaryBuilder(val binaryDescriptor: BluetoothBinaryDescriptor, flagBitsSize: Int, private val onUnconstrained: () -> Unit) : BinaryBuilder {

    val flagBits: MutableList<Boolean> = MutableList(flagBitsSize.coerceAtLeast(0)) { false }

    override val expectedSize: Int
        get() = totalBodySize +
            (binaryDescriptor.structureSettings.checksumAlgorithm?.byteWidth ?: 0) +
            (binaryDescriptor.structureSettings.prefix?.array?.size ?: 0) +
            (binaryDescriptor.structureSettings.postfix?.array?.size ?: 0)
    private var currentBits: Int = flagBitsSize % Byte.SIZE_BITS
    private var expectedBodySize: Int = flagBitsSize / Byte.SIZE_BITS

    private val totalBodySize: Int get() = expectedBodySize +
        if (currentBits > 0) 1 else 0
    private val actions = mutableListOf<ByteArrayBuilder.() -> Unit>()
    private var isOfUnconstrainedSize: Boolean = false

    override fun addFlag(index: Int, value: Boolean) {
        flagBits[index] = value
    }

    override fun addBit(value: Boolean) {
        if (isOfUnconstrainedSize) {
            throw DataAfterUnconstrainedData("Attempted to add data after data of an unconstrained size")
        }
        currentBits++
        if (currentBits == Byte.SIZE_BITS) {
            expectedBodySize++
            currentBits = 0
        }
        actions += {
            add(value)
        }
    }

    override fun addAction(expectedSize: Int, action: ByteArrayBuilder.() -> Unit) {
        if (isOfUnconstrainedSize) {
            throw DataAfterUnconstrainedData("Attempted to add data after data of an unconstrained size")
        }
        if (currentBits > 0) {
            expectedBodySize++
            currentBits = 0
        }
        expectedBodySize += expectedSize
        actions += action
    }

    override fun makeUnconstrained() {
        onUnconstrained()
        isOfUnconstrainedSize = true
    }

    override fun ByteArrayBuilder.build() {
        if (byteOrder == binaryDescriptor.byteOrder) {
            binaryDescriptor.structureSettings.prefix?.let {
                add(it.array)
            }
            val crc = binaryDescriptor.structureSettings.checksumAlgorithm
            if (crc != null) {
                // The body is the flag bits + remaining body. This is also the part used for checksum verification
                val body = buildByteArray(binaryDescriptor.byteOrder, totalBodySize) {
                    buildBody()
                }
                add(body)
                // Store the checksum as a [crc.byteWidth]-wide numeric in the structure's byte order, just like
                // every other multi-byte value. The full ULong is encoded in [byteOrder] and the [crc.byteWidth]
                // least-significant bytes are kept (the high zero bytes sit at the most-significant end).
                val crcBytes = crc.compute(body).toByteArray(binaryDescriptor.byteOrder)
                add(
                    when (binaryDescriptor.byteOrder) {
                        ByteOrder.MOST_SIGNIFICANT_FIRST -> crcBytes.copyOfRange(crcBytes.size - crc.byteWidth, crcBytes.size)
                        ByteOrder.LEAST_SIGNIFICANT_FIRST -> crcBytes.copyOfRange(0, crc.byteWidth)
                    },
                )
            } else {
                buildBody()
            }

            binaryDescriptor.structureSettings.postfix?.let {
                add(it.array)
            }
        } else {
            add(
                buildByteArray(binaryDescriptor.byteOrder, expectedSize) {
                    build()
                },
            )
        }
    }

    private fun ByteArrayBuilder.buildBody() {
        flagBits.forEach {
            add(it)
        }
        actions.forEach { apply(it) }
        if (currentBits > 0) {
            repeat(Byte.SIZE_BITS - currentBits) {
                add(false)
            }
        }
    }
}

/**
 * A [StructureBinaryBuilder] to build data for a single class
 */
internal class ClassBinaryBuilder(binaryDescriptor: BluetoothBinaryDescriptor, onUnconstrained: () -> Unit) :
    StructureBinaryBuilder(
        binaryDescriptor,
        binaryDescriptor.children.fold(0) { max, binaryDescriptor ->
            maxOf(binaryDescriptor.bitIndex + binaryDescriptor.bitWidth, max)
        },
        onUnconstrained,
    )

/**
 * A [StructureBinaryBuilder] to build data for an item in a collection.
 * Unlike [ClassBinaryBuilder], this assumes its flag width from the parent rather than the children, as each item will have its individual flag bytes.
 */
internal class ItemBinaryBuilder(binaryDescriptor: BluetoothBinaryDescriptor, onUnconstrained: () -> Unit) :
    StructureBinaryBuilder(
        binaryDescriptor,
        (binaryDescriptor.bitIndex + binaryDescriptor.bitWidth),
        onUnconstrained,
    ) {
    fun checkIfStartsWithNull(value: ByteArray, order: ByteOrder): Boolean = when (order) {
        ByteOrder.LEAST_SIGNIFICANT_FIRST -> value.firstOrNull() == 0x00.toByte()
        ByteOrder.MOST_SIGNIFICANT_FIRST -> value.lastOrNull() == 0x00.toByte()
    }
}

/**
 * Exception thrown when a collection is marked with [NullTerminated] but one of its items starts with a 0x00 byte.
 */
class UnexpectedNullTermination(override val message: String) : SerializationException()

/**
 * A [BinaryBuilder] to build data for a collection (List/Map) structure
 */
internal abstract class CollectionBinaryBuilder(private val byteOrder: ByteOrder, private val classBuilders: List<ItemBinaryBuilder>, private val isNullTerminated: Boolean) :
    BinaryBuilder {
    private var currentIndex = 0
    val currentClassBuilder: ItemBinaryBuilder get() = classBuilders[currentIndex]

    override val expectedSize: Int get() = classBuilders.sumOf { it.expectedSize }

    fun setIndex(index: Int): BluetoothBinaryDescriptor {
        currentIndex = index
        return currentClassBuilder.binaryDescriptor
    }

    override fun addBit(value: Boolean) {
        currentClassBuilder.addBit(value)
    }

    override fun addAction(expectedSize: Int, action: ByteArrayBuilder.() -> Unit) {
        currentClassBuilder.addAction(expectedSize, action)
    }

    override fun addFlag(index: Int, value: Boolean) {
        currentClassBuilder.addFlag(index, value)
    }

    override fun makeUnconstrained() {
        currentClassBuilder.makeUnconstrained()
    }

    override fun ByteArrayBuilder.build() {
        if (byteOrder == this@CollectionBinaryBuilder.byteOrder) {
            classBuilders.forEachIndexed { index, classBuilder ->
                if (isNullTerminated && classBuilder.binaryDescriptor.fieldIndex == 0) {
                    val value = buildByteArray(expectedSize = classBuilder.expectedSize) {
                        with(classBuilder) {
                            build()
                        }
                    }
                    if (classBuilder.checkIfStartsWithNull(value, byteOrder)) {
                        throw UnexpectedNullTermination("The element at $index starts with Null Byte in a Null Terminated List")
                    }
                    add(value)
                } else {
                    with(classBuilder) {
                        build()
                    }
                }
            }
        } else {
            add(
                with(this@CollectionBinaryBuilder) {
                    buildByteArray(byteOrder, expectedSize) {
                        build()
                    }
                },
            )
        }
    }
}

/**
 * A [CollectionBinaryBuilder] for a List
 */
internal class ListBinaryBuilder(binaryDescriptor: BluetoothBinaryDescriptor, size: Int, isNullTerminated: Boolean, onUnconstrained: () -> Unit) :
    CollectionBinaryBuilder(
        binaryDescriptor.byteOrder,
        MutableList(size) {
            ItemBinaryBuilder(binaryDescriptor.children.first(), onUnconstrained)
        },
        isNullTerminated,
    )

/**
 * A [CollectionBinaryBuilder] for a Map
 */
internal class MapBinaryBuilder(binaryDescriptor: BluetoothBinaryDescriptor, size: Int, isNullTerminated: Boolean, onUnconstrained: () -> Unit) :
    CollectionBinaryBuilder(
        binaryDescriptor.byteOrder,
        MutableList(size * 2) {
            val index = it % 2
            ItemBinaryBuilder(binaryDescriptor.children[index], onUnconstrained)
        },
        isNullTerminated,
    )
