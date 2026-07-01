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

infix fun UInt40.shr(bitCount: Int) = (value shr bitCount).toUInt40()
infix fun UInt40.shl(bitCount: Int) = (value shl bitCount).toUInt40()

infix fun UInt40.or(other: UInt40) = UInt40(value or other.value)
infix fun UInt40.and(other: UInt40) = UInt40(value and other.value)

/**
 * Decodes a [ByteArray] into an [UInt40] using the 5 bytes starting from [octetIndex]
 * @param octetIndex the the index of the octet start the decoding from. Must be be at most the fifth to last octet
 * @param byteOrder the [ByteOrder] in which the [UInt40] is encoded
 * @throws IllegalArgumentException if [octetIndex] or its next four octets are not available in the [ByteArray]
 * @return the decoded [UInt40]
 */
fun ByteArray.decodeUInt40(octetIndex: Int, byteOrder: ByteOrder): UInt40 {
    for (offset in 0..<UInt40.SIZE_BYTES) {
        require(octetIndex + offset in indices) {
            "Cannot convert ByteArray to UInt40. The byte with index ${octetIndex + offset} is not available in ByteArray."
        }
    }
    return drop(octetIndex).take(UInt40.SIZE_BYTES).foldIndexed(0UL) { index, acc, byte ->
        acc or ((byte.toULong() and 0xFFUL) shl byteOrder.shift(index, UInt40.SIZE_BITS))
    }.toUInt40()
}

/**
 * Encodes this [UInt40] into a [ByteArray].
 * @param byteOrder the [ByteOrder] in which the [UInt40] is encoded
 * @return the encoded [ByteArray].
 */
fun UInt40.toByteArray(byteOrder: ByteOrder) = copyIntoByteArray(ByteArray(UInt40.SIZE_BYTES), byteOrder = byteOrder)

/**
 * Encodes this [UInt40] and copies it into a [ByteArray] at a given offset.
 * @param array the [ByteArray] to copy the encoded data into.
 * @param offset the offset at which to copy the encoded data.
 * @param byteOrder the [ByteOrder] in which the [UInt40] is encoded
 * @throws IllegalArgumentException if [array] is not large enough to hold 5 bytes at the [offset].
 * @return the encoded [ByteArray].
 */
fun UInt40.copyIntoByteArray(array: ByteArray, offset: Int = 0, byteOrder: ByteOrder): ByteArray {
    require(array.size >= offset + UInt40.SIZE_BYTES) { "Cannot copy into ByteArray. Must be at least ${offset + UInt40.SIZE_BYTES} long" }
    for (index in 0..<UInt40.SIZE_BYTES) {
        array[offset + index] = (value shr byteOrder.shift(index, UInt40.SIZE_BITS)).toByte()
    }
    return array
}

/**
 * Checks whether the bit at [index] is set in this [UInt40]
 * @param index the index of the bit to check.
 * @return `true` if the bit is set, `false` otherwise.
 */
fun UInt40.isBitSet(index: Number) = (this shr index.toInt()) and UInt40(1U) == UInt40(1U)

/**
 * Creates a [UInt40] that is equal to this [UInt40] except the bit at [index] is set
 * @param index the index of the bit to set.
 * @return the new [UInt40]
 */
fun UInt40.setBit(index: Number) = (this or (1U.toULong().toUInt40() shl index.toInt()))
