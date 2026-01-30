package com.splendo.kaluga.base.bytes

import kotlin.math.abs
import kotlin.math.log10

/**
 * Determines the number of digits in an Integer
 */
fun Int.length() = when (this) {
    0 -> 1
    else -> log10(abs(toDouble())).toInt() + 1
}

/**
 * Converts [ByteArray] to [Int]
 * @param octetIndex index of byte to start. Must not be higher than the fourth to last octet.
 * @param byteOrder [ByteOrder] to use for decoding.
 * @throws IllegalArgumentException if [octetIndex] is bigger than the size of the [ByteArray] minus 4.
 * @return the decoded [Int]
 */
fun ByteArray.decodeInt(octetIndex: Int, byteOrder: ByteOrder): Int {
    for (offset in 0..<Int.SIZE_BYTES) {
        require(octetIndex + offset in indices) {
            "Cannot convert ByteArray to Int. The byte with index ${octetIndex + offset} is not available in ByteArray."
        }
    }
    return drop(octetIndex).take(Int.SIZE_BYTES).foldIndexed(0) { index, acc, byte ->
        acc or ((byte.toInt() and 0xFF) shl byteOrder.shift(index, Int.SIZE_BITS))
    }
}

/**
 * Encodes this [Int] into a [ByteArray].
 * @param byteOrder the [ByteOrder] in which the [Int] is encoded
 * @return the encoded [ByteArray].
 */
fun Int.toByteArray(byteOrder: ByteOrder) = copyIntoByteArray(ByteArray(Int.SIZE_BYTES), byteOrder = byteOrder)

/**
 * Encodes this [Int] and copies it into a [ByteArray] at a given offset.
 * @param array the [ByteArray] to copy the encoded data into.
 * @param offset the offset at which to copy the encoded data.
 * @param byteOrder the [ByteOrder] in which the [Int] is encoded
 * @throws IllegalArgumentException if [array] is not  is not large enough to hold 4 bytes at the [offset].
 * @return the encoded [ByteArray].
 */
fun Int.copyIntoByteArray(array: ByteArray, offset: Int = 0, byteOrder: ByteOrder): ByteArray {
    require(array.size >= offset + Int.SIZE_BYTES) { "Cannot copy into ByteArray. Must be at least ${offset + Int.SIZE_BYTES} long" }
    for (index in 0..<Int.SIZE_BYTES) {
        array[offset + index] = (this shr byteOrder.shift(index, Int.SIZE_BITS)).toByte()
    }
    return array
}

/**
 * Checks whether the bit at [index] is set in this [Int]
 * @param index the index of the bit to check.
 * @return `true` if the bit is set, `false` otherwise.
 */
fun Int.isBitSet(index: Number) = (this shr index.toInt()) and 1 == 1

/**
 * Creates a [Int] that is equal to this [Int] except the bit at [index] is set
 * @param index the index of the bit to set.
 * @return the new [Int]
 */
fun Int.setBit(index: Number) = (this or (1 shl index.toInt()))
