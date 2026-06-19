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
import com.splendo.kaluga.base.bytes.decodeInt48
import com.splendo.kaluga.base.bytes.decodeUInt48
import com.splendo.kaluga.base.bytes.toByteArray
import kotlin.jvm.JvmInline

/**
 * A 48-bit representation of a (signed) Integer
 * @property value the [Long] value this Integer represents. Must be between [Int48.MIN_VALUE] and [Int48.MAX_VALUE]
 */
@JvmInline
value class Int48(val value: Long) : Comparable<Int48> {
    companion object {
        private const val MIN_VALUE_LONG = -0x800000000000L
        private const val MAX_VALUE_LONG = 0x7FFFFFFFFFFFL

        /**
         * A constant holding the minimum value an instance of Int48 can have.
         */
        val MIN_VALUE = Int48(MIN_VALUE_LONG)

        /**
         * A constant holding the maximum value an instance of Int48 can have.
         */
        val MAX_VALUE = Int48(MAX_VALUE_LONG)

        /**
         * The number of bytes used to represent an instance of Int48 in a binary form.
         */
        const val SIZE_BYTES = 6

        /**
         * The number of bits used to represent an instance of Int48 in a binary form.
         */
        const val SIZE_BITS = 48
    }
    init {
        require(value in MIN_VALUE_LONG..MAX_VALUE_LONG) {
            "Value $value out of signed 48-bit range"
        }
    }

    override fun compareTo(other: Int48): Int = value.compareTo(other.value)
    operator fun plus(other: Int48) = (value + other.value).toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST).decodeInt48(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)
    operator fun minus(other: Int48) = (value - other.value).toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST).decodeInt48(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)
    operator fun times(other: Int48) = (value * other.value).toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST).decodeInt48(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)
    operator fun div(other: Int48) = (value / other.value).toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST).decodeInt48(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)
}

/**
 * Converts this [Number] value to [Int48].
 * @throws IllegalArgumentException if this value is not between [Int48.MIN_VALUE] and [Int48.MAX_VALUE]
 * @return The [Int48]
 */
fun Number.toInt48(): Int48 = Int48(toLong())

/**
 * Converts this [Int48] value to [UInt48].
 * If this value is positive, the resulting UInt48 value represents the same numerical value as this Int48.
 * The resulting UInt48 value has the same binary representation as this Int48 value.
 */
fun Int48.toUInt48() = toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST).decodeUInt48(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)
