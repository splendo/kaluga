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

package com.splendo.kaluga.base.utils

import kotlin.experimental.or

interface ByteArrayBuilder {
    fun add(byte: Byte)
    fun add(short: Short, order: ByteOrder = ByteOrder.LEAST_SIGNIFICANT_FIRST)
    fun add(int: Int, order: ByteOrder = ByteOrder.LEAST_SIGNIFICANT_FIRST)
    fun add(long: Long, order: ByteOrder = ByteOrder.LEAST_SIGNIFICANT_FIRST)
    fun add(float: Float, order: ByteOrder = ByteOrder.LEAST_SIGNIFICANT_FIRST)
    fun add(double: Double, order: ByteOrder = ByteOrder.LEAST_SIGNIFICANT_FIRST)
    fun add(uByte: UByte)
    fun add(uShort: UShort, order: ByteOrder = ByteOrder.LEAST_SIGNIFICANT_FIRST)
    fun add(uInt: UInt, order: ByteOrder = ByteOrder.LEAST_SIGNIFICANT_FIRST)
    fun add(uLong: ULong, order: ByteOrder = ByteOrder.LEAST_SIGNIFICANT_FIRST)
    fun add(int24: Int24, order: ByteOrder = ByteOrder.LEAST_SIGNIFICANT_FIRST)
    fun add(flag: Boolean)
    fun add(bytes: ByteArray)
    fun add(char: Char)
    fun add(string: String)
}

fun buildByteArray(order: ByteOrder = ByteOrder.LEAST_SIGNIFICANT_FIRST, block: ByteArrayBuilder.() -> Unit) = ByteArrayBuilderImpl(order).apply(block).bytes

private class ByteArrayBuilderImpl(val order: ByteOrder) : ByteArrayBuilder {
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

    override fun add(int24: Int24, order: ByteOrder) {
        add(int24.toByteArray(order))
    }

    override fun add(string: String) {
        add(string.encodeToByteArray())
    }

    override fun add(char: Char) {
        add(char.toString())
    }

    override fun add(bytes: ByteArray) {
        if (currentBit > 0) {
            addCurrentByte()
        }
        when (order) {
            ByteOrder.MOST_SIGNIFICANT_FIRST -> this.bytes = bytes + this.bytes
            ByteOrder.LEAST_SIGNIFICANT_FIRST -> this.bytes += bytes
        }
    }

    private fun addCurrentByte() {
        when (order) {
            ByteOrder.MOST_SIGNIFICANT_FIRST -> this.bytes = byteArrayOf(currentByte) + this.bytes
            ByteOrder.LEAST_SIGNIFICANT_FIRST -> this.bytes += currentByte
        }
        currentByte = 0
        currentBit = 0
    }
}
