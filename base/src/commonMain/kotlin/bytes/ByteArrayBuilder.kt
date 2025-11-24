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

interface ByteArrayBuilder {

    val byteOrder: ByteOrder

    fun add(byte: Byte)
    fun add(short: Short, order: ByteOrder = byteOrder)
    fun add(int24: Int24, order: ByteOrder = byteOrder)
    fun add(int: Int, order: ByteOrder = byteOrder)
    fun add(long: Long, order: ByteOrder = byteOrder)
    fun add(float: Float, order: ByteOrder = byteOrder)
    fun add(double: Double, order: ByteOrder = byteOrder)
    fun add(uByte: UByte)
    fun add(uShort: UShort, order: ByteOrder = byteOrder)
    fun add(uInt: UInt, order: ByteOrder = byteOrder)
    fun add(uLong: ULong, order: ByteOrder = byteOrder)
    fun add(uInt24: UInt24, order: ByteOrder = byteOrder)
    fun add(medFloat16: MedFloat16)
    fun add(medFloat32: MedFloat32)
    fun add(flag: Boolean)
    fun add(bytes: ByteArray)
    fun add(char: Char, encoding: StringEncodingSettings.Encoding = StringEncodingSettings.Encoding.UTF_8, order: ByteOrder = byteOrder)
    fun add(string: String, settings: StringEncodingSettings = StringEncodingSettings(), order: ByteOrder = byteOrder)
}

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

    override fun add(char: Char, encoding: StringEncodingSettings.Encoding, order: ByteOrder) {
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
