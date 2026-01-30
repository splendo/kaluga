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

import com.splendo.kaluga.base.utils.Int24
import com.splendo.kaluga.base.utils.MedFloat16
import com.splendo.kaluga.base.utils.MedFloat32
import com.splendo.kaluga.base.utils.UInt24
import kotlin.experimental.or
import kotlin.math.min

/**
 * Builds a [ByteArray] from primary types
 */
interface ByteArrayBuilder {

    /**
     * The [ByteOrder] in which the array is built.
     * If [ByteOrder.MOST_SIGNIFICANT_FIRST] any elements will be added to the front of the array.
     * This order acts as the default for encoding primary types added to this array.
     */
    val byteOrder: ByteOrder

    /**
     * Adds a single [Byte] to the array
     * @param byte the [Byte] to add
     */
    fun add(byte: Byte)

    /**
     * Adds a [Short] to the array, encoded using [Short.toByteArray]
     * @param short the [short] to add
     * @param order the [ByteOrder] in which the Short will be encoded. Defaults to [byteOrder]
     */
    fun add(short: Short, order: ByteOrder = byteOrder)

    /**
     * Adds an [Int24] to the array, encoded using [Int24.toByteArray]
     * @param int24 the [Int24] to add
     * @param order the [ByteOrder] in which the Int23 will be encoded. Defaults to [byteOrder]
     */
    fun add(int24: Int24, order: ByteOrder = byteOrder)

    /**
     * Adds an [Int] to the array, encoded using [Int.toByteArray]
     * @param int the [Int] to add
     * @param order the [ByteOrder] in which the Int will be encoded. Defaults to [byteOrder]
     */
    fun add(int: Int, order: ByteOrder = byteOrder)

    /**
     * Adds a [Long] to the array, encoded using [Long.toByteArray]
     * @param long the [Long] to add
     * @param order the [ByteOrder] in which the Long will be encoded. Defaults to [byteOrder]
     */
    fun add(long: Long, order: ByteOrder = byteOrder)

    /**
     * Adds a [Float] to the array, encoded using [Float.toByteArray]
     * @param float the [Float] to add
     * @param order the [ByteOrder] in which the Float will be encoded. Defaults to [byteOrder]
     */
    fun add(float: Float, order: ByteOrder = byteOrder)

    /**
     * Adds a [Double] to the array, encoded using [Double.toByteArray]
     * @param double the [Double] to add
     * @param order the [ByteOrder] in which the Double will be encoded. Defaults to [byteOrder]
     */
    fun add(double: Double, order: ByteOrder = byteOrder)

    /**
     * Adds a single [UByte] to the array
     * @param uByte the [UByte] to add
     */
    fun add(uByte: UByte)

    /**
     * Adds a [UShort] to the array, encoded using [UShort.toByteArray]
     * @param uShort the [UShort] to add
     * @param order the [ByteOrder] in which the UShort will be encoded. Defaults to [byteOrder]
     */
    fun add(uShort: UShort, order: ByteOrder = byteOrder)

    /**
     * Adds a [UInt] to the array, encoded using [UInt.toByteArray]
     * @param uInt the [UInt] to add
     * @param order the [ByteOrder] in which the UInt will be encoded. Defaults to [byteOrder]
     */
    fun add(uInt: UInt, order: ByteOrder = byteOrder)

    /**
     * Adds a [ULong] to the array, encoded using [ULong.toByteArray]
     * @param uLong the [ULong] to add
     * @param order the [ByteOrder] in which the ULong will be encoded. Defaults to [byteOrder]
     */
    fun add(uLong: ULong, order: ByteOrder = byteOrder)

    /**
     * Adds a [UInt24] to the array, encoded using [UInt24.toByteArray]
     * @param uInt24 the [UInt24] to add
     * @param order the [ByteOrder] in which the UInt24 will be encoded. Defaults to [byteOrder]
     */
    fun add(uInt24: UInt24, order: ByteOrder = byteOrder)

    /**
     * Adds a [MedFloat16] to the array.
     * @param medFloat16 The [MedFloat16] to add.
     */
    fun add(medFloat16: MedFloat16)

    /**
     * Adds a [MedFloat32] to the array.
     * @param medFloat32 The [MedFloat32] to add.
     */
    fun add(medFloat32: MedFloat32)

    /**
     * Adds a [Boolean] to the array.
     * Any subsequent Boolean will be added as the next bit of a byte until the byte is full.
     * If any additional element is added in between, the Boolean containing byte will be added as is.
     * @param flag the Boolean to add.
     */
    fun add(flag: Boolean)

    /**
     * Adds a [ByteArray] to the array.
     * The order of this array will be preserved but its position is determined by [byteOrder].
     * @param bytes The [ByteArray] to add.
     */
    fun add(bytes: ByteArray)

    /**
     * Adds a [Char] to the array. The char will be encoded using [Encoding.encodeChar]
     * @param char the Char to add
     * @param encoding the [Encoding] to encode the char with.
     * @param order the [ByteOrder] in which the Char will be encoded.
     */
    fun add(char: Char, encoding: Encoding = Encoding.UTF_8, order: ByteOrder = byteOrder)

    /**
     * Adds a [String] to the array. The String will be encoded using [Encoding.encodeString]
     * @param string the String to add
     * @param settings the [StringEncodingSettings] to encode the String with.
     * @param order the [ByteOrder] in which the String will be encoded.
     */
    fun add(string: String, settings: StringEncodingSettings = StringEncodingSettings(), order: ByteOrder = byteOrder)
}

/**
 * Builds a [ByteArray] using a [ByteArrayBuilder]
 * @param order the [ByteOrder] in which to add to the [ByteArray]. This is the default order in which elements will be encoded.
 * @param expectedSize the initial size the ByteArray will use to approximate its final size.
 * @param block the building block using [ByteArrayBuilder] to build the array.
 * @return the built [ByteArray]
 */
fun buildByteArray(order: ByteOrder = ByteOrder.LEAST_SIGNIFICANT_FIRST, expectedSize: Int = Long.SIZE_BYTES, block: ByteArrayBuilder.() -> Unit) = ByteArrayBuilderImpl(
    expectedSize,
    order,
).apply(block).build()

private class ByteArrayBuilderImpl(expectedSize: Int, override val byteOrder: ByteOrder) : ByteArrayBuilder {

    init {
        require(expectedSize > 0) { "buildByteArray must have an expected size larger than 0" }
    }
    private val completedChunks = mutableListOf<ByteArray>()
    var currentChunk = ByteArray(expectedSize)
    var currentByteOffset = 0
    var currentByte: Byte = 0
    var currentBit = 0

    override fun add(flag: Boolean) {
        if (flag) {
            currentByte = currentByte or (1 shl currentBit).toByte()
        }
        currentBit++
        if (currentBit == Byte.SIZE_BITS) {
            addCurrentByte()
        }
    }

    override fun add(byte: Byte) {
        add(
            Byte.SIZE_BYTES,
            generateIntoMethod = { currentChunk[it] = byte },
            generateMethod = { byteArrayOf(byte) },
        )
    }

    override fun add(short: Short, order: ByteOrder) {
        add(
            Short.SIZE_BYTES,
            generateIntoMethod = { short.copyIntoByteArray(currentChunk, it, order) },
            generateMethod = { short.toByteArray(order) },
        )
    }

    override fun add(int24: Int24, order: ByteOrder) {
        add(
            Int24.SIZE_BYTES,
            generateIntoMethod = { int24.copyIntoByteArray(currentChunk, it, order) },
            generateMethod = { int24.toByteArray(order) },
        )
    }

    override fun add(int: Int, order: ByteOrder) {
        add(
            Int.SIZE_BYTES,
            generateIntoMethod = { int.copyIntoByteArray(currentChunk, it, order) },
            generateMethod = { int.toByteArray(order) },
        )
    }

    override fun add(long: Long, order: ByteOrder) {
        add(
            Long.SIZE_BYTES,
            generateIntoMethod = { long.copyIntoByteArray(currentChunk, it, order) },
            generateMethod = { long.toByteArray(order) },
        )
    }

    override fun add(float: Float, order: ByteOrder) {
        add(
            Float.SIZE_BYTES,
            generateIntoMethod = { float.copyIntoByteArray(currentChunk, it, order) },
            generateMethod = { float.toByteArray(order) },
        )
    }

    override fun add(double: Double, order: ByteOrder) {
        add(
            Double.SIZE_BYTES,
            generateIntoMethod = { double.copyIntoByteArray(currentChunk, it, order) },
            generateMethod = { double.toByteArray(order) },
        )
    }

    override fun add(medFloat16: MedFloat16) {
        add(
            2,
            generateIntoMethod = { medFloat16.copyIntoByteArray(currentChunk, it) },
            generateMethod = { medFloat16.toByteArray() },
        )
    }

    override fun add(medFloat32: MedFloat32) {
        add(
            4,
            generateIntoMethod = { medFloat32.copyIntoByteArray(currentChunk, it) },
            generateMethod = { medFloat32.toByteArray() },
        )
    }

    override fun add(uByte: UByte) {
        add(
            UByte.SIZE_BYTES,
            generateIntoMethod = { currentChunk[it] = uByte.toByte() },
            generateMethod = { uByte.toByteArray() },
        )
    }

    override fun add(uShort: UShort, order: ByteOrder) {
        add(
            UShort.SIZE_BYTES,
            generateIntoMethod = { uShort.copyIntoByteArray(currentChunk, it, order) },
            generateMethod = { uShort.toByteArray(order) },
        )
    }

    override fun add(uInt: UInt, order: ByteOrder) {
        add(
            UInt.SIZE_BYTES,
            generateIntoMethod = { uInt.copyIntoByteArray(currentChunk, it, order) },
            generateMethod = { uInt.toByteArray(order) },
        )
    }

    override fun add(uLong: ULong, order: ByteOrder) {
        add(
            ULong.SIZE_BYTES,
            generateIntoMethod = { uLong.copyIntoByteArray(currentChunk, it, order) },
            generateMethod = { uLong.toByteArray(order) },
        )
    }

    override fun add(uInt24: UInt24, order: ByteOrder) {
        add(
            UInt24.SIZE_BYTES,
            generateIntoMethod = { uInt24.copyIntoByteArray(currentChunk, it, order) },
            generateMethod = { uInt24.toByteArray(order) },
        )
    }

    override fun add(string: String, settings: StringEncodingSettings, order: ByteOrder) {
        add(string.toByteArray(settings, order))
    }

    override fun add(char: Char, encoding: Encoding, order: ByteOrder) {
        add(
            encoding.byteSize,
            generateIntoMethod = { encoding.copyCharIntoByteArray(char, currentChunk, it, order) },
            generateMethod = { encoding.encodeChar(char, order) },
        )
    }

    override fun add(bytes: ByteArray) {
        add(
            bytes.size,
            generateIntoMethod = { bytes.copyInto(currentChunk, it) },
            generateMethod = { bytes },
        )
    }

    private inline fun add(expectedSize: Int, generateIntoMethod: (Int) -> Unit, generateMethod: () -> ByteArray) {
        if (currentBit > 0) {
            addCurrentByte()
        }

        val remaining = currentChunk.size - currentByteOffset
        when {
            expectedSize in 1..<remaining -> {
                generateIntoMethod(
                    when (byteOrder) {
                        ByteOrder.MOST_SIGNIFICANT_FIRST -> remaining - expectedSize
                        ByteOrder.LEAST_SIGNIFICANT_FIRST -> currentByteOffset
                    },
                )
                currentByteOffset += expectedSize
            }

            else -> {
                val fullSegment = generateMethod()
                val splitIndex = min(fullSegment.size, remaining)
                when (byteOrder) {
                    ByteOrder.LEAST_SIGNIFICANT_FIRST -> {
                        fullSegment.copyInto(currentChunk, currentByteOffset, 0, splitIndex)
                        currentByteOffset += splitIndex
                        if (splitIndex < fullSegment.size) {
                            completedChunks.add(currentChunk)
                            completedChunks.add(fullSegment.sliceArray(splitIndex..<fullSegment.size))
                            currentChunk = ByteArray(currentChunk.size * 2)
                            currentByteOffset = 0
                        }
                    }

                    ByteOrder.MOST_SIGNIFICANT_FIRST -> {
                        fullSegment.copyInto(currentChunk, remaining - splitIndex, fullSegment.size - splitIndex)
                        currentByteOffset += splitIndex
                        if (splitIndex < fullSegment.size) {
                            completedChunks.add(currentChunk)
                            completedChunks.add(fullSegment.sliceArray(0..<fullSegment.size - splitIndex))
                            currentChunk = ByteArray(currentChunk.size * 2)
                            currentByteOffset = 0
                        }
                    }
                }
            }
        }
        checkChunkCompleted()
    }

    private fun addCurrentByte() {
        when (byteOrder) {
            ByteOrder.MOST_SIGNIFICANT_FIRST -> currentChunk[currentChunk.size - currentByteOffset - 1] = currentByte
            ByteOrder.LEAST_SIGNIFICANT_FIRST -> currentChunk[currentByteOffset] = currentByte
        }
        currentByteOffset++
        currentByte = 0
        currentBit = 0
        checkChunkCompleted()
    }

    private fun checkChunkCompleted() {
        if (currentByteOffset == currentChunk.size) {
            completedChunks.add(currentChunk)
            currentChunk = ByteArray(currentChunk.size * 2)
            currentByteOffset = 0
        }
    }

    fun build(): ByteArray {
        if (currentBit > 0) {
            addCurrentByte()
        }

        return if (completedChunks.isEmpty()) {
            if (currentByteOffset == 0) {
                byteArrayOf()
            } else {
                when (byteOrder) {
                    ByteOrder.MOST_SIGNIFICANT_FIRST -> currentChunk.sliceArray(currentChunk.size - currentByteOffset..<currentChunk.size)
                    ByteOrder.LEAST_SIGNIFICANT_FIRST -> currentChunk.sliceArray(0..<currentByteOffset)
                }
            }
        } else {
            val totalSize = completedChunks.sumOf { it.size } + currentByteOffset
            val bytes = ByteArray(totalSize)
            val lastChunkOffset = completedChunks.fold(0) { sumOfOffset, chunk ->
                when (byteOrder) {
                    ByteOrder.MOST_SIGNIFICANT_FIRST -> {
                        chunk.copyInto(bytes, totalSize - sumOfOffset - chunk.size)
                    }

                    ByteOrder.LEAST_SIGNIFICANT_FIRST -> {
                        chunk.copyInto(bytes, sumOfOffset)
                    }
                }
                sumOfOffset + chunk.size
            }

            if (currentByteOffset > 0) {
                when (byteOrder) {
                    ByteOrder.MOST_SIGNIFICANT_FIRST -> {
                        currentChunk.copyInto(bytes, startIndex = currentChunk.size - currentByteOffset)
                    }

                    ByteOrder.LEAST_SIGNIFICANT_FIRST -> {
                        currentChunk.copyInto(bytes, lastChunkOffset, endIndex = currentByteOffset)
                    }
                }
            }

            bytes
        }
    }
}
