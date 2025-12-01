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
 * @param block the building block using [ByteArrayBuilder] to build the array.
 * @return the built [ByteArray]
 */
fun buildByteArray(order: ByteOrder = ByteOrder.LEAST_SIGNIFICANT_FIRST, block: ByteArrayBuilder.() -> Unit) = ByteArrayBuilderImpl(
    order,
).apply(block).build()

private class ByteArrayBuilderImpl(override val byteOrder: ByteOrder) : ByteArrayBuilder {
    var bytes = byteArrayOf()
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
        add(byteArrayOf(byte))
    }

    override fun add(short: Short, order: ByteOrder) {
        add(short.toByteArray(order))
    }

    override fun add(int24: Int24, order: ByteOrder) {
        add(int24.toByteArray(order))
    }

    override fun add(int: Int, order: ByteOrder) {
        add(int.toByteArray(order))
    }

    override fun add(long: Long, order: ByteOrder) {
        add(long.toByteArray(order))
    }

    override fun add(float: Float, order: ByteOrder) {
        add(float.toByteArray(order))
    }

    override fun add(double: Double, order: ByteOrder) {
        add(double.toByteArray(order))
    }

    override fun add(medFloat16: MedFloat16) {
        add(medFloat16.toByteArray())
    }

    override fun add(medFloat32: MedFloat32) {
        add(medFloat32.toByteArray())
    }

    override fun add(uByte: UByte) {
        add(uByte.toByteArray())
    }

    override fun add(uShort: UShort, order: ByteOrder) {
        add(uShort.toByteArray(order))
    }

    override fun add(uInt: UInt, order: ByteOrder) {
        add(uInt.toByteArray(order))
    }

    override fun add(uLong: ULong, order: ByteOrder) {
        add(uLong.toByteArray(order))
    }

    override fun add(uInt24: UInt24, order: ByteOrder) {
        add(uInt24.toByteArray(order))
    }

    override fun add(string: String, settings: StringEncodingSettings, order: ByteOrder) {
        add(string.toByteArray(settings, order))
    }

    override fun add(char: Char, encoding: Encoding, order: ByteOrder) {
        add(char.toString(), StringEncodingSettings(StringEncodingSettings.NoMarking, encoding), order)
    }

    override fun add(bytes: ByteArray) {
        if (currentBit > 0) {
            addCurrentByte()
        }
        when (byteOrder) {
            ByteOrder.MOST_SIGNIFICANT_FIRST -> this.bytes = bytes + this.bytes
            ByteOrder.LEAST_SIGNIFICANT_FIRST -> this.bytes += bytes
        }
    }

    private fun addCurrentByte() {
        when (byteOrder) {
            ByteOrder.MOST_SIGNIFICANT_FIRST -> this.bytes = byteArrayOf(currentByte) + this.bytes
            ByteOrder.LEAST_SIGNIFICANT_FIRST -> this.bytes += currentByte
        }
        currentByte = 0
        currentBit = 0
    }

    fun build(): ByteArray {
        if (currentBit > 0) {
            addCurrentByte()
        }
        return bytes
    }
}
