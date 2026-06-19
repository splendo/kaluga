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

package com.splendo.kaluga.base.bytes

import com.splendo.kaluga.base.utils.UInt48
import com.splendo.kaluga.base.utils.toUInt48

infix fun UInt48.shr(bitCount: Int) = (value shr bitCount).toUInt48()
infix fun UInt48.shl(bitCount: Int) = (value shl bitCount).toUInt48()

infix fun UInt48.or(other: UInt48) = UInt48(value or other.value)
infix fun UInt48.and(other: UInt48) = UInt48(value and other.value)

/**
 * Decodes a [ByteArray] into an [UInt48] using the 6 bytes starting from [octetIndex]
 * @param octetIndex the the index of the octet start the decoding from. Must be be at most the sixth to last octet
 * @param byteOrder the [ByteOrder] in which the [UInt48] is encoded
 * @throws IllegalArgumentException if [octetIndex] or its next five octets are not available in the [ByteArray]
 * @return the decoded [UInt48]
 */
fun ByteArray.decodeUInt48(octetIndex: Int, byteOrder: ByteOrder): UInt48 {
    for (offset in 0..<UInt48.SIZE_BYTES) {
        require(octetIndex + offset in indices) {
            "Cannot convert ByteArray to UInt48. The byte with index ${octetIndex + offset} is not available in ByteArray."
        }
    }
    return drop(octetIndex).take(UInt48.SIZE_BYTES).foldIndexed(0UL) { index, acc, byte ->
        acc or ((byte.toULong() and 0xFFUL) shl byteOrder.shift(index, UInt48.SIZE_BITS))
    }.toUInt48()
}

/**
 * Encodes this [UInt48] into a [ByteArray].
 * @param byteOrder the [ByteOrder] in which the [UInt48] is encoded
 * @return the encoded [ByteArray].
 */
fun UInt48.toByteArray(byteOrder: ByteOrder) = copyIntoByteArray(ByteArray(UInt48.SIZE_BYTES), byteOrder = byteOrder)

/**
 * Encodes this [UInt48] and copies it into a [ByteArray] at a given offset.
 * @param array the [ByteArray] to copy the encoded data into.
 * @param offset the offset at which to copy the encoded data.
 * @param byteOrder the [ByteOrder] in which the [UInt48] is encoded
 * @throws IllegalArgumentException if [array] is not large enough to hold 6 bytes at the [offset].
 * @return the encoded [ByteArray].
 */
fun UInt48.copyIntoByteArray(array: ByteArray, offset: Int = 0, byteOrder: ByteOrder): ByteArray {
    require(array.size >= offset + UInt48.SIZE_BYTES) { "Cannot copy into ByteArray. Must be at least ${offset + UInt48.SIZE_BYTES} long" }
    for (index in 0..<UInt48.SIZE_BYTES) {
        array[offset + index] = (value shr byteOrder.shift(index, UInt48.SIZE_BITS)).toByte()
    }
    return array
}

/**
 * Checks whether the bit at [index] is set in this [UInt48]
 * @param index the index of the bit to check.
 * @return `true` if the bit is set, `false` otherwise.
 */
fun UInt48.isBitSet(index: Number) = (this shr index.toInt()) and UInt48(1U) == UInt48(1U)

/**
 * Creates a [UInt48] that is equal to this [UInt48] except the bit at [index] is set
 * @param index the index of the bit to set.
 * @return the new [UInt48]
 */
fun UInt48.setBit(index: Number) = (this or (1U.toULong().toUInt48() shl index.toInt()))
