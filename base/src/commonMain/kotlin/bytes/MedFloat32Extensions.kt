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

/**
 * Converts [ByteArray] to [MedFloat32]
 * @param octetIndex index of byte to start. Must not be higher than the fourth to last octet.
 * @throws IllegalArgumentException if [octetIndex] is bigger than the size of the [ByteArray] minus 4.
 * @return the decoded [MedFloat32]
 */
fun ByteArray.decodeMedFloat32(octetIndex: Int): MedFloat32 {
    for (offset in 0..<Int.SIZE_BYTES) {
        require(octetIndex + offset in indices) {
            "Cannot convert ByteArray to MedFloat32. The byte with index ${octetIndex + offset} is not available in ByteArray."
        }
    }
    val content = drop(octetIndex).take(4).toByteArray()
    return when {
        content.contentEquals(MedFloat32.NAN_BYTE_VALUE) -> Double.NaN

        content.contentEquals(MedFloat32.POSITIVE_INFINITY_BYTE_VALUE) -> Double.POSITIVE_INFINITY

        content.contentEquals(MedFloat32.NEGATIVE_INFINITY_BYTE_VALUE) -> Double.NEGATIVE_INFINITY

        content.contentEquals(MedFloat32.NOT_AT_THIS_RESOLUTION_BYTE_VALUE) -> Double.NaN

        content.contentEquals(MedFloat32.RESERVED_FOR_FUTURE_USE_BYTE_VALUE) -> Double.NaN

        else -> {
            val mantissa = content.decodeInt24(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)
            val exponent = get(octetIndex + 3).toInt()
            mantissa.value * 10.0.pow(exponent)
        }
    }.let { MedFloat32(it) }
}

/**
 * Encodes this [MedFloat32] into a [ByteArray].
 * @return the encoded [ByteArray].
 */
fun MedFloat32.toByteArray() = copyIntoByteArray(ByteArray(4))

/**
 * Encodes this [MedFloat32] and copies it into a [ByteArray] at a given offset.
 * @param array the [ByteArray] to copy the encoded data into.
 * @param offset the offset at which to copy the encoded data.
 * @throws IllegalArgumentException if [array] is not  is not large enough to hold 4 bytes at the [offset].
 * @return the encoded [ByteArray].
 */
fun MedFloat32.copyIntoByteArray(array: ByteArray, offset: Int = 0): ByteArray {
    require(array.size >= offset + 4) { "Cannot copy into ByteArray. Must be at least ${offset + 4} long" }
    if (value.isNaN()) return MedFloat32.NAN_BYTE_VALUE.copyInto(array, offset)

    if (value == Double.POSITIVE_INFINITY) return MedFloat32.POSITIVE_INFINITY_BYTE_VALUE.copyInto(array, offset)

    if (value == Double.NEGATIVE_INFINITY) return MedFloat32.NEGATIVE_INFINITY_BYTE_VALUE.copyInto(array, offset)
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
        return MedFloat32.NOT_AT_THIS_RESOLUTION_BYTE_VALUE.copyInto(array, offset)
    }

    val mant = mantissa.toInt()

    array[offset] = (mant and 0xFF).toByte()
    array[offset + 1] = ((mant shr 8) and 0xFF).toByte()
    array[offset + 2] = ((mant shr 16) and 0xFF).toByte()
    array[offset + 3] = exponent.toByte()
    return array
}
