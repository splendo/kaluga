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

    val floatValue = when (mantissa) {
        MedFloat16.NAN -> Float.NaN
        MedFloat16.POSITIVE_INFINITY -> Float.POSITIVE_INFINITY
        MedFloat16.NEGATIVE_INFINITY -> Float.NEGATIVE_INFINITY
        else -> mantissa * 10.0.pow(exponent).toFloat()
    }
    return MedFloat16(floatValue)
}

fun MedFloat16.toByteArray(): ByteArray {
    if (value.isNaN()) return MedFloat16.NAN.toShort().toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST)
    if (value == Float.POSITIVE_INFINITY) return MedFloat16.POSITIVE_INFINITY.toShort().toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST)
    if (value == Float.NEGATIVE_INFINITY) return MedFloat16.NEGATIVE_INFINITY.toShort().toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST)
    var mantissa = value
    var exponent = 0

    while (mantissa !in -2048f..2047f && exponent < 7) {
        mantissa /= 10f
        exponent++
    }

    while (mantissa in -204.8f..204.7f &&
        mantissa != mantissa.toInt().toFloat() &&
        exponent > -8
    ) {
        mantissa *= 10f
        exponent--

        if (mantissa !in -2048f..2047f) {
            mantissa /= 10f
            exponent++
            break
        }
    }

    val mant = mantissa.toInt().coerceIn(-2048, 2047)
    val exp = exponent and 0x0F

    val raw = (exp shl 12) or (mant and 0x0FFF)

    return byteArrayOf(
        (raw and 0xFF).toByte(),
        ((raw shr 8) and 0xFF).toByte(),
    )
}
