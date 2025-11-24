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

import kotlin.jvm.JvmInline
import kotlin.math.pow
import kotlin.math.roundToInt

@JvmInline
value class MedFloat32(val value: Double) : Comparable<MedFloat32> {
    companion object {
        const val NAN = 0x007FFFFF
        const val POSITIVE_INFINITY = 0x007FFFFE
        const val NEGATIVE_INFINITY = 0x00800002
        const val NOT_AT_THIS_RESOLUTION = 0x00800000
        const val RESERVED_FOR_FUTURE_USE = 0x00800001

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
}
