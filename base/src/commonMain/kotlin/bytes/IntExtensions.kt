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
fun Int.toByteArray(byteOrder: ByteOrder) = ByteArray(Int.SIZE_BYTES) {
    (this shr byteOrder.shift(it, Int.SIZE_BITS)).toByte()
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
