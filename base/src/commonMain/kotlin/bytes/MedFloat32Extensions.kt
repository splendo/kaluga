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

import com.splendo.kaluga.base.utils.Int24
import com.splendo.kaluga.base.utils.MedFloat32
import kotlin.math.pow

fun ByteArray.decodeMedFloat32(octetIndex: Int): MedFloat32 {
    for (offset in 0..<Int.SIZE_BYTES) {
        require(octetIndex + offset in indices) {
            "Cannot convert ByteArray to MedFloat32. The byte with index ${octetIndex + offset} is not available in ByteArray."
        }
    }
    val mantissa = decodeInt24(octetIndex, ByteOrder.LEAST_SIGNIFICANT_FIRST)
    val double = when (mantissa.value) {
        MedFloat32.NAN -> Double.NaN
        MedFloat32.POSITIVE_INFINITY -> Double.POSITIVE_INFINITY
        MedFloat32.NEGATIVE_INFINITY -> Double.NEGATIVE_INFINITY
        MedFloat32.NOT_AT_THIS_RESOLUTION -> Double.NaN
        MedFloat32.RESERVED_FOR_FUTURE_USE -> Double.NaN
        else -> {
            val exponent = get(octetIndex + 3).toInt()
            mantissa.value * 10.0.pow(exponent)
        }
    }

    return MedFloat32(double)
}

fun MedFloat32.toByteArray(): ByteArray {
    if (value.isNaN()) return MedFloat32.NAN.toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST)

    if (value == Double.POSITIVE_INFINITY) return MedFloat32.POSITIVE_INFINITY.toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST)

    if (value == Double.NEGATIVE_INFINITY) return MedFloat32.NEGATIVE_INFINITY.toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST)
    var mantissa = value
    var exponent = 0

    while (mantissa !in Int24.MIN_VALUE.value.toDouble()..Int24.MAX_VALUE.value.toDouble() && exponent < Byte.MAX_VALUE) {
        mantissa /= 10.0
        exponent++
    }

    while (
        mantissa in (Int24.MIN_VALUE.value.toDouble()) / 10.0..(Int24.MAX_VALUE.value.toDouble() / 10.0) &&
        mantissa != mantissa.toInt().toDouble() &&
        exponent > Byte.MIN_VALUE
    ) {
        mantissa *= 10.0
        exponent--

        if (mantissa !in Int24.MIN_VALUE.value.toDouble()..Int24.MAX_VALUE.value.toDouble()) {
            mantissa /= 10.0
            exponent++
            break
        }
    }

    if (mantissa.toInt() !in Int24.MIN_VALUE.value..Int24.MAX_VALUE.value) {
        return MedFloat32.NOT_AT_THIS_RESOLUTION.toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST)
    }

    val mant = mantissa.toInt()

    return byteArrayOf(
        (mant and 0xFF).toByte(),
        ((mant shr 8) and 0xFF).toByte(),
        ((mant shr 16) and 0xFF).toByte(),
        exponent.toByte(),
    )
}
