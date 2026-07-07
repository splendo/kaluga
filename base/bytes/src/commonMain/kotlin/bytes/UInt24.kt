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
 * A 24-bit representation of an (unsigned) Integer
 * @property value the [UInt] value this Integer represents. Must be between [UInt24.MIN_VALUE] and [UInt24.MAX_VALUE]
 */
@JvmInline
value class UInt24(val value: UInt) : Comparable<UInt24> {

    companion object {
        private const val MIN_UINT_VALUE = 0U
        private const val MAX_UINT_VALUE = 0xFFFFFFU

        /**
         * A constant holding the minimum value an instance of UInt24 can have.
         */
        val MIN_VALUE = UInt24(MIN_UINT_VALUE)

        /**
         * A constant holding the maximum value an instance of UInt24 can have.
         */
        val MAX_VALUE = UInt24(MAX_UINT_VALUE)

        /**
         * The number of bytes used to represent an instance of UInt24 in a binary form.
         */
        const val SIZE_BYTES = 3

        /**
         * The number of bits used to represent an instance of UInt24 in a binary form.
         */
        const val SIZE_BITS = 24
    }

    init {
        require(value in MIN_UINT_VALUE..MAX_UINT_VALUE) {
            "Value $value out of unsigned 24-bit range"
        }
    }

    override fun compareTo(other: UInt24): Int = value.compareTo(other.value)
    operator fun plus(other: UInt24) = (value + other.value).toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST).decodeUInt24(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)
    operator fun minus(other: UInt24) = (value - other.value).toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST).decodeUInt24(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)
    operator fun times(other: UInt24) = (value * other.value).toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST).decodeUInt24(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)
    operator fun div(other: UInt24) = (value / other.value).toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST).decodeUInt24(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)
}

/**
 * Converts this [UInt] value to [UInt24].
 * @throws IllegalArgumentException if this value is not between [UInt24.MIN_VALUE] and [UInt24.MAX_VALUE]
 * @return The [UInt24]
 */
fun UInt.toUInt24() = UInt24(this and 0xFFFFFF.toUInt())

/**
 * Converts this [UInt24] value to [Int24].
 * If this value is positive, the resulting Int24 value represents the same numerical value as this UInt24.
 * The resulting Int24 value has the same binary representation as this UInt24 value.
 */
fun UInt24.toInt24() = toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST).decodeInt24(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)
