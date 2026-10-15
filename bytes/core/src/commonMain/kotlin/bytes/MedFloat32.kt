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
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * A 32-bit representation of a floating point number consisting of a 8 bit exponent and 24 bit mantissa so that 10^exponent * mantissa (IEEE-11073)
 */
@JvmInline
value class MedFloat32(val value: Double) : Comparable<MedFloat32> {
    companion object {
        internal val NAN_BYTE_VALUE = byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0x7F.toByte(), 0x00)
        internal val POSITIVE_INFINITY_BYTE_VALUE = byteArrayOf(0xFE.toByte(), 0xFF.toByte(), 0x7F.toByte(), 0x00)
        internal val NEGATIVE_INFINITY_BYTE_VALUE = byteArrayOf(0x02, 0x00, 0x80.toByte(), 0x00)
        internal val NOT_AT_THIS_RESOLUTION_BYTE_VALUE = byteArrayOf(0x00, 0x00, 0x80.toByte(), 0x00)
        internal val RESERVED_FOR_FUTURE_USE_BYTE_VALUE = byteArrayOf(0x01, 0x00, 0x80.toByte(), 0x00)

        fun canRepresent(value: Double): Boolean {
            if (value.isNaN() || value.isInfinite()) return true

            return (Byte.MIN_VALUE.toInt()..Byte.MAX_VALUE.toInt()).find { exp ->
                val scaled = value / 10.0.pow(exp)

                val mantissa = scaled.roundToInt()
                mantissa.toDouble() == scaled && mantissa in Int24.MIN_VALUE.value..Int24.MAX_VALUE.value
            } != null
        }
    }

    override fun compareTo(other: MedFloat32): Int = value.compareTo(other.value)
    fun isNaN(): Boolean = value.isNaN() || toByteArray().contentEquals(NAN_BYTE_VALUE)
    fun isPositiveInfinity(): Boolean = (value.isInfinite() && value > 0) || toByteArray().contentEquals(POSITIVE_INFINITY_BYTE_VALUE)

    fun isNegativeInfinity(): Boolean = (value.isInfinite() && value < 0) || toByteArray().contentEquals(NEGATIVE_INFINITY_BYTE_VALUE)
    fun isInfinite(): Boolean = isPositiveInfinity() || isNegativeInfinity()
    fun isFinite(): Boolean = !isInfinite() && !isNaN()
    fun isNotAvailableAtThisResolution(): Boolean = !canRepresent(value)
}
