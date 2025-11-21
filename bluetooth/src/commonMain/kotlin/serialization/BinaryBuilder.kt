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

internal interface BinaryBuilder {
    fun addFlag(index: Int, value: Boolean)
    fun addAction(action: ByteArrayBuilder.() -> Unit)
    fun makeUnconstrained()

    fun build(): ByteArray
}

internal abstract class StructureBinaryBuilder(val entry: FlagLayoutEntry, flagBitsSize: Int, private val onUnconstrained: () -> Unit) : BinaryBuilder {

    val flagBits: MutableList<Boolean> = MutableList(flagBitsSize.coerceAtLeast(0)) { false }
    private val actions = mutableListOf<ByteArrayBuilder.() -> Unit>()
    private var isOfUnconstrainedSize: Boolean = false

    override fun addFlag(index: Int, value: Boolean) {
        flagBits[index] = value
    }

    override fun addAction(action: ByteArrayBuilder.() -> Unit) {
        require(!isOfUnconstrainedSize) { "This object has data of an unconstrained size." }
        actions += action
    }

    override fun makeUnconstrained() {
        onUnconstrained()
        isOfUnconstrainedSize = true
    }

    override fun build(): ByteArray {
        val body = buildByteArray(entry.byteOrder) {
            flagBits.forEach {
                add(it)
            }
            actions.forEach { apply(it) }
        }
        val checksum = when (entry.blockSettings.checksumAlgorithm) {
            ChecksumAlgorithm.NONE -> byteArrayOf()
            ChecksumAlgorithm.CRC16 -> byteArrayOf()
            ChecksumAlgorithm.CRC32 -> byteArrayOf()
        }
        return buildByteArray(entry.byteOrder) {
            entry.blockSettings.prefix?.let {
                add(it.array)
            }
            add(body)
            add(checksum)
            entry.blockSettings.postfix?.let {
                add(it.array)
            }
        }
    }
}

internal class ClassBinaryBuilder(entry: FlagLayoutEntry, onUnconstrained: () -> Unit) :
    StructureBinaryBuilder(
        entry,
        entry.children.fold(0) { max, entry ->
            maxOf(entry.bitIndex + entry.bitWidth, max)
        },
        onUnconstrained,
    )

internal class ItemBinaryBuilder(entry: FlagLayoutEntry, onUnconstrained: () -> Unit) :
    StructureBinaryBuilder(
        entry,
        (entry.bitIndex + entry.bitWidth),
        onUnconstrained,
    )

internal abstract class CollectionBinaryBuilder(private val byteOrder: ByteOrder, private val classBuilders: List<ItemBinaryBuilder>) : BinaryBuilder {
    private var currentIndex = 0
    val currentClassBuilder: ItemBinaryBuilder get() = classBuilders[currentIndex]

    fun setIndex(index: Int): FlagLayoutEntry {
        currentIndex = index
        return currentClassBuilder.entry
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
        classBuilders.forEach {
            add(it.build())
        }
    }
}
internal class ListBinaryBuilder(entry: FlagLayoutEntry, size: Int, onUnconstrained: () -> Unit) :
    CollectionBinaryBuilder(
        entry.byteOrder,
        MutableList(size) {
            ItemBinaryBuilder(entry.children.first(), onUnconstrained)
        },
    )

internal class MapBinaryBuilder(entry: FlagLayoutEntry, size: Int, onUnconstrained: () -> Unit) :
    CollectionBinaryBuilder(
        entry.byteOrder,
        MutableList(size) {
            val index = it % 2
            ItemBinaryBuilder(entry.children[index], onUnconstrained)
        },
    )
