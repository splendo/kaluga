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
 * A 48-bit representation of an (unsigned) Integer
 * @property value the [ULong] value this Integer represents. Must be between [UInt48.MIN_VALUE] and [UInt48.MAX_VALUE]
 */
@JvmInline
value class UInt48(val value: ULong) : Comparable<UInt48> {

    companion object {
        private const val MIN_ULONG_VALUE = 0UL
        private const val MAX_ULONG_VALUE = 0xFFFFFFFFFFFFUL

        /**
         * A constant holding the minimum value an instance of UInt48 can have.
         */
        val MIN_VALUE = UInt48(MIN_ULONG_VALUE)

        /**
         * A constant holding the maximum value an instance of UInt48 can have.
         */
        val MAX_VALUE = UInt48(MAX_ULONG_VALUE)

        /**
         * The number of bytes used to represent an instance of UInt48 in a binary form.
         */
        const val SIZE_BYTES = 6

        /**
         * The number of bits used to represent an instance of UInt48 in a binary form.
         */
        const val SIZE_BITS = 48
    }

    init {
        require(value in MIN_ULONG_VALUE..MAX_ULONG_VALUE) {
            "Value $value out of unsigned 48-bit range"
        }
    }

    override fun compareTo(other: UInt48): Int = value.compareTo(other.value)
    operator fun plus(other: UInt48) = (value + other.value).toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST).decodeUInt48(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)
    operator fun minus(other: UInt48) = (value - other.value).toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST).decodeUInt48(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)
    operator fun times(other: UInt48) = (value * other.value).toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST).decodeUInt48(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)
    operator fun div(other: UInt48) = (value / other.value).toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST).decodeUInt48(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)
}

/**
 * Converts this [ULong] value to [UInt48].
 * The resulting value retains only the lowest 48 bits.
 * @return The [UInt48]
 */
fun ULong.toUInt48() = UInt48(this and 0xFFFFFFFFFFFFUL)

/**
 * Converts this [UInt48] value to [Int48].
 * If this value is positive, the resulting Int48 value represents the same numerical value as this UInt48.
 * The resulting Int48 value has the same binary representation as this UInt48 value.
 */
fun UInt48.toInt48() = toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST).decodeInt48(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)
