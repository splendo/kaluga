/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.base.bytes.ByteOrder
import com.splendo.kaluga.base.bytes.decodeInt40
import com.splendo.kaluga.base.bytes.decodeUInt40
import com.splendo.kaluga.base.bytes.toByteArray
import kotlin.jvm.JvmInline

/**
 * A 40-bit representation of a (signed) Integer
 * @property value the [Long] value this Integer represents. Must be between [Int40.MIN_VALUE] and [Int40.MAX_VALUE]
 */
@JvmInline
value class Int40(val value: Long) : Comparable<Int40> {
    companion object {
        private const val MIN_VALUE_LONG = -0x8000000000L
        private const val MAX_VALUE_LONG = 0x7FFFFFFFFFL

        /**
         * A constant holding the minimum value an instance of Int40 can have.
         */
        val MIN_VALUE = Int40(MIN_VALUE_LONG)

        /**
         * A constant holding the maximum value an instance of Int40 can have.
         */
        val MAX_VALUE = Int40(MAX_VALUE_LONG)

        /**
         * The number of bytes used to represent an instance of Int40 in a binary form.
         */
        const val SIZE_BYTES = 5

        /**
         * The number of bits used to represent an instance of Int40 in a binary form.
         */
        const val SIZE_BITS = 40
    }
    init {
        require(value in MIN_VALUE_LONG..MAX_VALUE_LONG) {
            "Value $value out of signed 40-bit range"
        }
    }

    override fun compareTo(other: Int40): Int = value.compareTo(other.value)
    operator fun plus(other: Int40) = (value + other.value).toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST).decodeInt40(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)
    operator fun minus(other: Int40) = (value - other.value).toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST).decodeInt40(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)
    operator fun times(other: Int40) = (value * other.value).toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST).decodeInt40(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)
    operator fun div(other: Int40) = (value / other.value).toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST).decodeInt40(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)
}

/**
 * Converts this [Number] value to [Int40].
 * @throws IllegalArgumentException if this value is not between [Int40.MIN_VALUE] and [Int40.MAX_VALUE]
 * @return The [Int40]
 */
fun Number.toInt40(): Int40 = Int40(toLong())

/**
 * Converts this [Int40] value to [UInt40].
 * If this value is positive, the resulting UInt40 value represents the same numerical value as this Int40.
 * The resulting UInt40 value has the same binary representation as this Int40 value.
 */
fun Int40.toUInt40() = toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST).decodeUInt40(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)
