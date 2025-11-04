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
    fun add(short: Short)
    fun add(int: Int)
    fun add(long: Long)
    fun add(float: Float)
    fun add(double: Double)
    fun add(uByte: UByte)
    fun add(uShort: UShort)
    fun add(uInt: UInt)
    fun add(uLong: ULong)
    fun add(int24: Int24)
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
        currentByte = currentByte or (if (flag) 1 else 0 shl currentBit).toByte()
        currentBit++
        if (currentBit == Byte.SIZE_BITS) {
            addCurrentByte()
        }
    }

    override fun add(byte: Byte) {
        add(byteArrayOf(byte))
    }

    override fun add(short: Short) {
        add(short.toByteArray(order))
    }

    override fun add(int: Int) {
        add(int.toByteArray(order))
    }

    override fun add(long: Long) {
        add(long.toByteArray(order))
    }

    override fun add(float: Float) {
        add(float.toByteArray(order))
    }

    override fun add(double: Double) {
        add(double.toByteArray(order))
    }

    override fun add(uByte: UByte) {
        add(uByte.toByteArray())
    }

    override fun add(uShort: UShort) {
        add(uShort.toByteArray(order))
    }

    override fun add(uInt: UInt) {
        add(uInt.toByteArray(order))
    }

    override fun add(uLong: ULong) {
        add(uLong.toByteArray(order))
    }

    override fun add(int24: Int24) {
        add(int24.toByteArray(order))
    }

    override fun add(string: String) {
        add(string.encodeToByteArray())
    }

    override fun add(char: Char) {
        add(char.toString())
    }

    override fun add(bytes: ByteArray) {
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
