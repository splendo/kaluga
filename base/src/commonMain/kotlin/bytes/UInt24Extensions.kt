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
import com.splendo.kaluga.base.utils.UInt24
import com.splendo.kaluga.base.utils.toUInt24

infix fun UInt24.shr(bitCount: Int) = (value shr bitCount).toUInt24()
infix fun UInt24.shl(bitCount: Int) = (value shl bitCount).toUInt24()

infix fun UInt24.or(other: UInt24) = UInt24(value or other.value)
infix fun UInt24.and(other: UInt24) = UInt24(value and other.value)

/**
 * Decodes a [ByteArray] into an [UInt24] using the 3 bytes starting from [octetIndex]
 * @param octetIndex the the index of the octet start the decoding from. Must be be at most the third to last octet
 * @param byteOrder the [ByteOrder] in which the [UInt24] is encoded
 * @throws IllegalArgumentException if [octetIndex] or its next two octets are not available in the [ByteArray]
 * @return the decoded [UInt24]
 */
fun ByteArray.decodeUInt24(octetIndex: Int, byteOrder: ByteOrder): UInt24 {
    for (offset in 0..<UInt24.SIZE_BYTES) {
        require(octetIndex + offset in indices) {
            "Cannot convert ByteArray to Int24. The byte with index ${octetIndex + offset} is not available in ByteArray."
        }
    }
    return drop(octetIndex).take(UInt24.SIZE_BYTES).foldIndexed(0U) { index, acc, byte ->
        acc or ((byte.toUInt() and 0xFF.toUInt()) shl byteOrder.shift(index, UInt24.SIZE_BITS))
    }.toUInt24()
}

/**
 * Encodes this [UInt24] into a [ByteArray].
 * @param byteOrder the [ByteOrder] in which the [UInt24] is encoded
 * @return the encoded [ByteArray].
 */
fun UInt24.toByteArray(byteOrder: ByteOrder) = copyIntoByteArray(ByteArray(UInt24.SIZE_BYTES), byteOrder = byteOrder)

/**
 * Encodes this [UInt24] and copies it into a [ByteArray] at a given offset.
 * @param array the [ByteArray] to copy the encoded data into.
 * @param offset the offset at which to copy the encoded data.
 * @param byteOrder the [ByteOrder] in which the [UInt24] is encoded
 * @throws IllegalArgumentException if [array] is not  is not large enough to hold 3 bytes at the [offset].
 * @return the encoded [ByteArray].
 */
fun UInt24.copyIntoByteArray(array: ByteArray, offset: Int = 0, byteOrder: ByteOrder): ByteArray {
    require(array.size >= offset + UInt24.SIZE_BYTES) { "Cannot copy into ByteArray. Must be at least ${offset + UInt24.SIZE_BYTES} long" }
    for (index in 0..<UInt24.SIZE_BYTES) {
        array[offset + index] = (value shr byteOrder.shift(index, UInt24.SIZE_BITS)).toByte()
    }
    return array
}

/**
 * Checks whether the bit at [index] is set in this [UInt24]
 * @param index the index of the bit to check.
 * @return `true` if the bit is set, `false` otherwise.
 */
fun UInt24.isBitSet(index: Number) = (this shr index.toInt()) and UInt24(1U) == UInt24(1U)

/**
 * Creates a [UInt24] that is equal to this [UInt24] except the bit at [index] is set
 * @param index the index of the bit to set.
 * @return the new [UInt24]
 */
fun UInt24.setBit(index: Number) = (this or (1U.toUInt24() shl index.toInt()))
