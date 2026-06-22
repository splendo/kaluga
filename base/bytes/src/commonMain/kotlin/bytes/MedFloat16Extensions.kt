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

import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.math.pow

/**
 * Converts [ByteArray] to [MedFloat16]
 * @param octetIndex index of byte to start. Must not be higher than the second to last octet.
 * @throws IllegalArgumentException if [octetIndex] is bigger than the size of the [ByteArray] minus 2.
 * @return the decoded [MedFloat16]
 */
fun ByteArray.decodeMedFloat16(octetIndex: Int): MedFloat16 {
    for (offset in 0..<Short.SIZE_BYTES) {
        require(octetIndex + offset in indices) {
            "Cannot convert ByteArray to MedFloat16. The byte with index ${octetIndex + offset} is not available in ByteArray."
        }
    }

    val content = drop(octetIndex).take(2).toByteArray()
    return when {
        content.contentEquals(MedFloat16.NAN_BYTE_VALUE) -> Double.NaN

        content.contentEquals(MedFloat16.POSITIVE_INFINITY_BYTE_VALUE) -> Double.POSITIVE_INFINITY

        content.contentEquals(MedFloat16.NEGATIVE_INFINITY_BYTE_VALUE) -> Double.NEGATIVE_INFINITY

        content.contentEquals(MedFloat16.NOT_AT_THIS_RESOLUTION_BYTE_VALUE) -> Double.NaN

        content.contentEquals(MedFloat16.RESERVED_FOR_FUTURE_USE_BYTE_VALUE) -> Double.NaN

        else -> {
            val raw = content.decodeShort(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)

            // Extract fields
            val mantissa = (raw and 0x0FFF).let { mantissa ->
                if (mantissa and 0x800 != 0.toShort()) {
                    mantissa or 0xF000.toShort()
                } else {
                    mantissa
                }
            }

            val exponent = ((raw shr 12) and 0x0F).let { exponent ->
                if (exponent and 0x08 != 0.toShort()) {
                    exponent or 0xFFF0.toShort()
                } else {
                    exponent
                }
            }

            mantissa * 10.0.pow(exponent.toInt())
        }
    }.let { MedFloat16(it) }
}

/**
 * Encodes this [MedFloat16] into a [ByteArray].
 * @return the encoded [ByteArray].
 */
fun MedFloat16.toByteArray(): ByteArray = copyIntoByteArray(ByteArray(2))

/**
 * Encodes this [MedFloat16] and copies it into a [ByteArray] at a given offset.
 * @param array the [ByteArray] to copy the encoded data into.
 * @param offset the offset at which to copy the encoded data.
 * @throws IllegalArgumentException if [array] is not  is not large enough to hold 2 bytes at the [offset].
 * @return the encoded [ByteArray].
 */
fun MedFloat16.copyIntoByteArray(array: ByteArray, offset: Int = 0): ByteArray {
    require(array.size >= offset + 2) { "Cannot copy into ByteArray. Must be at least ${offset + 2} long" }
    if (value.isNaN()) return MedFloat16.NAN_BYTE_VALUE.copyInto(array, offset)
    if (value == Double.POSITIVE_INFINITY) return MedFloat16.POSITIVE_INFINITY_BYTE_VALUE.copyInto(array, offset)
    if (value == Double.NEGATIVE_INFINITY) return MedFloat16.NEGATIVE_INFINITY_BYTE_VALUE.copyInto(array, offset)
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
        return MedFloat16.NOT_AT_THIS_RESOLUTION_BYTE_VALUE.copyInto(array, offset)
    }

    val mant = mantissa.toInt()
    val exp = exponent and 0x0F

    val raw = (exp shl 12) or (mant and 0x0FFF)

    array[offset] = (raw and 0xFF).toByte()
    array[offset + 1] = ((raw shr 8) and 0xFF).toByte()
    return array
}
