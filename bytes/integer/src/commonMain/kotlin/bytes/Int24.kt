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

import kotlin.jvm.JvmInline

/**
 * A 24-bit representation of a (signed) Integer
 * @property value the [Int] value this Integer represents. Must be between [Int24.MIN_VALUE] and [Int24.MAX_VALUE]
 */
@JvmInline
value class Int24(val value: Int) : Comparable<Int24> {
    companion object {
        private const val MIN_VALUE_INT = -0x800000
        private const val MAX_VALUE_INT = 0x7FFFFF

        /**
         * A constant holding the minimum value an instance of Int24 can have.
         */
        val MIN_VALUE = Int24(MIN_VALUE_INT)

        /**
         * A constant holding the maximum value an instance of Int24 can have.
         */
        val MAX_VALUE = Int24(MAX_VALUE_INT)

        /**
         * The number of bytes used to represent an instance of Int24 in a binary form.
         */
        const val SIZE_BYTES = 3

        /**
         * The number of bits used to represent an instance of Int24 in a binary form.
         */
        const val SIZE_BITS = 24
    }
    init {
        require(value in MIN_VALUE_INT..MAX_VALUE_INT) {
            "Value $value out of signed 24-bit range"
        }
    }

    override fun compareTo(other: Int24): Int = value.compareTo(other.value)
    operator fun plus(other: Int24) = (value + other.value).toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST).decodeInt24(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)
    operator fun minus(other: Int24) = (value - other.value).toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST).decodeInt24(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)
    operator fun times(other: Int24) = (value * other.value).toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST).decodeInt24(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)
    operator fun div(other: Int24) = (value / other.value).toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST).decodeInt24(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)
}

/**
 * Converts this [Number] value to [Int24].
 * @throws IllegalArgumentException if this value is not between [Int24.MIN_VALUE] and [Int24.MAX_VALUE]
 * @return The [Int24]
 */
fun Number.toInt24(): Int24 = Int24(toInt())

/**
 * Converts this [Int24] value to [UInt24].
 * If this value is positive, the resulting UInt24 value represents the same numerical value as this Int24.
 * The resulting UInt24 value has the same binary representation as this Int24 value.
 */
fun Int24.toUInt24() = toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST).decodeUInt24(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)
