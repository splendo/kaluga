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

import com.splendo.kaluga.base.bytes.toByteArray
import kotlin.jvm.JvmInline
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * A 16-bit representation of a floating point number consisting of a 4 bit exponent and 12 bit mantissa so that 10^exponent * mantissa (IEEE-11073)
 */
@JvmInline
value class MedFloat16(val value: Double) : Comparable<MedFloat16> {
    companion object {

        internal val NAN_BYTE_VALUE = byteArrayOf(0xFF.toByte(), 0x07)

        /**
         * MedFloat16 representing Not A Number value
         */
        val NAN = MedFloat16(Double.NaN)

        internal val POSITIVE_INFINITY_BYTE_VALUE = byteArrayOf(0xFE.toByte(), 0x07)

        /**
         * MedFloat16 representing Positive Infinity value
         */
        val POSITIVE_INFINITY = MedFloat16(Double.POSITIVE_INFINITY)
        internal val NEGATIVE_INFINITY_BYTE_VALUE = byteArrayOf(0x02, 0x08)

        /**
         * MedFloat16 representing Negative Infinity value
         */
        val NEGATIVE_INFINITY = MedFloat16(Double.NEGATIVE_INFINITY)

        internal val NOT_AT_THIS_RESOLUTION_BYTE_VALUE = byteArrayOf(0x00, 0x08)
        internal val RESERVED_FOR_FUTURE_USE_BYTE_VALUE = byteArrayOf(0x01, 0x08)

        const val MIN_MANTISSA = -2048
        const val MAX_MANTISSA = 2047
        const val MIN_EXPONENT = -8
        const val MAX_EXPONENT = 7

        // **

        fun canRepresent(value: Double): Boolean {
            if (value.isNaN() || value.isInfinite()) return true

            return (MIN_EXPONENT..MAX_EXPONENT).find { exp ->
                val scaled = value / 10.0.pow(exp)

                val mantissa = scaled.roundToInt()
                mantissa.toDouble() == scaled && mantissa in MIN_MANTISSA..MAX_MANTISSA
            } != null
        }
    }

    override fun compareTo(other: MedFloat16): Int = value.compareTo(other.value)
    fun isNaN(): Boolean = value.isNaN() || toByteArray().contentEquals(NAN_BYTE_VALUE)
    fun isPositiveInfinity(): Boolean = (value.isInfinite() && value > 0) || toByteArray().contentEquals(POSITIVE_INFINITY_BYTE_VALUE)

    fun isNegativeInfinity(): Boolean = (value.isInfinite() && value < 0) || toByteArray().contentEquals(NEGATIVE_INFINITY_BYTE_VALUE)
    fun isInfinite(): Boolean = isPositiveInfinity() || isNegativeInfinity()
    fun isFinite(): Boolean = !isInfinite() && !isNaN()
    fun isNotAvailableAtThisResolution(): Boolean = !canRepresent(value)
}
