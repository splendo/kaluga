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

import com.splendo.kaluga.base.utils.MedFloat16
import kotlin.math.pow

fun ByteArray.decodeMedFloat16(octetIndex: Int): MedFloat16 {
    for (offset in 0..<Short.SIZE_BYTES) {
        require(octetIndex + offset in indices) {
            "Cannot convert ByteArray to MedFloat16. The byte with index ${octetIndex + offset} is not available in ByteArray."
        }
    }
    val bytes = drop(octetIndex)

    val raw = (bytes[1].toInt() shl 8) or (bytes[0].toInt() and 0xFF)

    // Extract fields
    val mantissa = (raw and 0x0FFF).let { mantissa ->
        if (mantissa and 0x800 != 0) {
            mantissa or 0xFFFF0000.toInt()
        } else {
            mantissa
        }
    }
    val exponent = ((raw shr 12) and 0x0F).let { exponent ->
        if (exponent and 0x08 != 0) {
            exponent or 0xFFFFFFF0.toInt()
        } else {
            exponent
        }
    }

    val doubleValue = when (mantissa) {
        MedFloat16.NAN -> Double.NaN
        MedFloat16.POSITIVE_INFINITY -> Double.POSITIVE_INFINITY
        MedFloat16.NEGATIVE_INFINITY -> Double.NEGATIVE_INFINITY
        MedFloat16.NOT_AT_THIS_RESOLUTION -> Double.NaN
        MedFloat16.RESERVED_FOR_FUTURE_USE -> Double.NaN
        else -> mantissa * 10.0.pow(exponent)
    }
    return MedFloat16(doubleValue)
}

fun MedFloat16.toByteArray(): ByteArray {
    if (value.isNaN()) return MedFloat16.NAN.toShort().toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST)
    if (value == Double.POSITIVE_INFINITY) return MedFloat16.POSITIVE_INFINITY.toShort().toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST)
    if (value == Double.NEGATIVE_INFINITY) return MedFloat16.NEGATIVE_INFINITY.toShort().toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST)
    var mantissa = value
    var exponent = 0

    while (mantissa !in MedFloat16.MIN_MANTISSA.toDouble()..MedFloat16.MAX_MANTISSA.toDouble() && exponent < MedFloat16.MAX_EXPONENT) {
        mantissa /= 10.0
        exponent++
    }

    while (mantissa in (MedFloat16.MIN_MANTISSA.toDouble() / 10.0)..(MedFloat16.MAX_MANTISSA.toDouble() / 10.0) &&
        mantissa != mantissa.toInt().toDouble() &&
        exponent > MedFloat16.MIN_EXPONENT
    ) {
        mantissa *= 10.0
        exponent--

        if (mantissa !in MedFloat16.MIN_MANTISSA.toDouble()..MedFloat16.MAX_MANTISSA.toDouble()) {
            mantissa /= 10.0
            exponent++
            break
        }
    }

    if (mantissa.toInt() !in MedFloat16.MIN_MANTISSA..MedFloat16.MAX_MANTISSA) {
        return MedFloat16.NOT_AT_THIS_RESOLUTION.toShort().toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST)
    }

    val mant = mantissa.toInt()
    val exp = exponent and 0x0F

    val raw = (exp shl 12) or (mant and 0x0FFF)

    return byteArrayOf(
        (raw and 0xFF).toByte(),
        ((raw shr 8) and 0xFF).toByte(),
    )
}
