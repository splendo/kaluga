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

package com.splendo.kaluga.base.bytes

import kotlin.jvm.JvmInline

/**
 * A 40-bit representation of an (unsigned) Integer
 * @property value the [ULong] value this Integer represents. Must be between [UInt40.MIN_VALUE] and [UInt40.MAX_VALUE]
 */
@JvmInline
value class UInt40(val value: ULong) : Comparable<UInt40> {

    companion object {
        private const val MIN_ULONG_VALUE = 0UL
        private const val MAX_ULONG_VALUE = 0xFFFFFFFFFFUL

        /**
         * A constant holding the minimum value an instance of UInt40 can have.
         */
        val MIN_VALUE = UInt40(MIN_ULONG_VALUE)

        /**
         * A constant holding the maximum value an instance of UInt40 can have.
         */
        val MAX_VALUE = UInt40(MAX_ULONG_VALUE)

        /**
         * The number of bytes used to represent an instance of UInt40 in a binary form.
         */
        const val SIZE_BYTES = 5

        /**
         * The number of bits used to represent an instance of UInt40 in a binary form.
         */
        const val SIZE_BITS = 40
    }

    init {
        require(value in MIN_ULONG_VALUE..MAX_ULONG_VALUE) {
            "Value $value out of unsigned 40-bit range"
        }
    }

    override fun compareTo(other: UInt40): Int = value.compareTo(other.value)
    operator fun plus(other: UInt40) = (value + other.value).toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST).decodeUInt40(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)
    operator fun minus(other: UInt40) = (value - other.value).toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST).decodeUInt40(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)
    operator fun times(other: UInt40) = (value * other.value).toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST).decodeUInt40(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)
    operator fun div(other: UInt40) = (value / other.value).toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST).decodeUInt40(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)
}

/**
 * Converts this [ULong] value to [UInt40].
 * The resulting value retains only the lowest 40 bits.
 * @return The [UInt40]
 */
fun ULong.toUInt40() = UInt40(this and 0xFFFFFFFFFFUL)

/**
 * Converts this [UInt40] value to [Int40].
 * If this value is positive, the resulting Int40 value represents the same numerical value as this UInt40.
 * The resulting Int40 value has the same binary representation as this UInt40 value.
 */
fun UInt40.toInt40() = toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST).decodeInt40(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)
