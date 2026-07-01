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

/**
 * Shifts this value left by the bitCount number of bits.
 * @param bitCount the number of bits to shift by.
 * @return the shifted value.
 */
infix fun Int48.shl(bitCount: Int) = (value shl bitCount).toInt48()

/**
 * Shifts this value right by the bitCount number of bits.
 * @param bitCount the number of bits to shift by.
 * @return the shifted value.
 */
infix fun Int48.shr(bitCount: Int) = (value shr bitCount).toInt48()

/**
 * Performs a bitwise OR operation between the two values.
 */
infix fun Int48.or(other: Int48) = Int48(value or other.value)

/**
 * Performs a bitwise AND operation between the two values.
 */
infix fun Int48.and(other: Int48) = Int48(value and other.value)

/**
 * Decodes a [ByteArray] into an [Int48] using the 6 bytes starting from [octetIndex]
 * @param octetIndex the the index of the octet start the decoding from. Must be be at most the sixth to last octet
 * @param byteOrder the [ByteOrder] in which the [Int48] is encoded
 * @throws IllegalArgumentException if [octetIndex] or its next five octets are not available in the [ByteArray]
 * @return the decoded [Int48]
 */
fun ByteArray.decodeInt48(octetIndex: Int, byteOrder: ByteOrder): Int48 {
    for (offset in 0..<Int48.SIZE_BYTES) {
        require(octetIndex + offset in indices) {
            "Cannot convert ByteArray to Int48. The byte with index ${octetIndex + offset} is not available in ByteArray."
        }
    }
    val value = drop(octetIndex).take(Int48.SIZE_BYTES).foldIndexed(0L) { index, acc, byte ->
        acc or ((byte.toLong() and 0xFF) shl byteOrder.shift(index, Int48.SIZE_BITS))
    }
    return if (value and (1L shl (Int48.SIZE_BITS - 1)) != 0L) {
        Int48(value or (-1L shl Int48.SIZE_BITS))
    } else {
        Int48(value)
    }
}

/**
 * Encodes this [Int48] into a [ByteArray].
 * @param byteOrder the [ByteOrder] in which the [Int48] is encoded
 * @return the encoded [ByteArray].
 */
fun Int48.toByteArray(byteOrder: ByteOrder) = copyIntoByteArray(ByteArray(Int48.SIZE_BYTES), byteOrder = byteOrder)

/**
 * Encodes this [Int48] and copies it into a [ByteArray] at a given offset.
 * @param array the [ByteArray] to copy the encoded data into.
 * @param offset the offset at which to copy the encoded data.
 * @param byteOrder the [ByteOrder] in which the [Int48] is encoded
 * @throws IllegalArgumentException if [array] is not large enough to hold 6 bytes at the [offset].
 * @return the encoded [ByteArray].
 */
fun Int48.copyIntoByteArray(array: ByteArray, offset: Int = 0, byteOrder: ByteOrder): ByteArray {
    require(array.size >= offset + Int48.SIZE_BYTES) { "Cannot copy into ByteArray. Must be at least ${offset + Int48.SIZE_BYTES} long" }
    for (index in 0..<Int48.SIZE_BYTES) {
        array[offset + index] = (value shr byteOrder.shift(index, Int48.SIZE_BITS)).toByte()
    }
    return array
}

/**
 * Checks whether the bit at [index] is set in this [Int48]
 * @param index the index of the bit to check.
 * @return `true` if the bit is set, `false` otherwise.
 */
fun Int48.isBitSet(index: Number) = (this shr index.toInt()) and Int48(1) == Int48(1)

/**
 * Creates a [Int48] that is equal to this [Int48] except the bit at [index] is set
 * @param index the index of the bit to set.
 * @return the new [Int48]
 */
fun Int48.setBit(index: Number) = (this or (1.toInt48() shl index.toInt()))
