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

/**
 * Builder for creating a [ByteArray] from a [BluetoothBinaryDescriptor]
 */
internal interface BinaryBuilder {
    fun addFlag(index: Int, value: Boolean)
    fun addAction(action: ByteArrayBuilder.() -> Unit)
    fun makeUnconstrained()

    fun build(): ByteArray
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
    private val actions = mutableListOf<ByteArrayBuilder.() -> Unit>()
    private var isOfUnconstrainedSize: Boolean = false

    override fun addFlag(index: Int, value: Boolean) {
        flagBits[index] = value
    }

    override fun addAction(action: ByteArrayBuilder.() -> Unit) {
        if (isOfUnconstrainedSize) {
            throw DataAfterUnconstrainedData("Attempted to add data after data of an unconstrained size")
        }
        actions += action
    }

    override fun makeUnconstrained() {
        onUnconstrained()
        isOfUnconstrainedSize = true
    }

    override fun build(): ByteArray {
        // The body is the flag bits + remaining body. This is also the part used for checksum verification
        val body = buildByteArray(binaryDescriptor.byteOrder, binaryDescriptor.expectedSize) {
            flagBits.forEach {
                add(it)
            }
            actions.forEach { apply(it) }
        }
        // Calculate checksum if necessary
        val checksum = binaryDescriptor.structureSettings.checksumAlgorithm?.let { crc ->
            crc.compute(body).toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST).take(crc.byteWidth).let {
                when (binaryDescriptor.byteOrder) {
                    ByteOrder.MOST_SIGNIFICANT_FIRST -> it.reversed()
                    ByteOrder.LEAST_SIGNIFICANT_FIRST -> it
                }.toByteArray()
            }
        } ?: byteArrayOf()

        // Full data consists of prefix + body + checksum + postfix
        return buildByteArray(binaryDescriptor.byteOrder, binaryDescriptor.expectedSize) {
            binaryDescriptor.structureSettings.prefix?.let {
                add(it.array)
            }
            add(body)
            add(checksum)
            binaryDescriptor.structureSettings.postfix?.let {
                add(it.array)
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

    fun setIndex(index: Int): BluetoothBinaryDescriptor {
        currentIndex = index
        return currentClassBuilder.binaryDescriptor
    }

    override fun addAction(action: ByteArrayBuilder.() -> Unit) {
        currentClassBuilder.addAction(action)
    }

    override fun addFlag(index: Int, value: Boolean) {
        currentClassBuilder.addFlag(index, value)
    }

    override fun makeUnconstrained() {
        currentClassBuilder.makeUnconstrained()
    }

    override fun build(): ByteArray = buildByteArray(byteOrder) {
        classBuilders.forEachIndexed { index, classBuilder ->
            val value = classBuilder.build()

            // Ensure no unexpected null termination occurs
            if (isNullTerminated && classBuilder.binaryDescriptor.fieldIndex == 0 && classBuilder.checkIfStartsWithNull(value, byteOrder)) {
                throw UnexpectedNullTermination("The element at $index starts with Null Byte in a Null Terminated List")
            }
            add(value)
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
