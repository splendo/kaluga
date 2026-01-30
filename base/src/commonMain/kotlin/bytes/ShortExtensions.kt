package com.splendo.kaluga.base.bytes

import kotlin.experimental.and
import kotlin.experimental.or

/**
 * Shifts this value left by the bitCount number of bits.
 * @param bitCount the number of bits to shift by.
 * @return the shifted value.
 */
infix fun Short.shl(bitCount: Int) = (toUInt() shl bitCount).toShort()

/**
 * Shifts this value right by the bitCount number of bits.
 * @param bitCount the number of bits to shift by.
 * @return the shifted value.
 */
infix fun Short.shr(bitCount: Int) = (toUInt() shr bitCount).toShort()

/**
 * Converts [ByteArray] to [Short]
 * @param octetIndex index of byte to start. Must not be higher than the second to last octet.
 * @param byteOrder [ByteOrder] to use for decoding.
 * @throws IllegalArgumentException if [octetIndex] is bigger than the size of the [ByteArray] minus 2.
 * @return the decoded [Short]
 */
fun ByteArray.decodeShort(octetIndex: Int, byteOrder: ByteOrder): Short {
    for (offset in 0..<Short.SIZE_BYTES) {
        require(octetIndex + offset in indices) {
            "Cannot convert ByteArray to Short. The byte with index ${octetIndex + offset} is not available in ByteArray."
        }
    }
    return drop(octetIndex).take(Short.SIZE_BYTES).foldIndexed(0.toShort()) { index, acc, byte ->
        acc or ((byte.toShort() and 0xFF) shl byteOrder.shift(index, Short.SIZE_BITS))
    }
}

/**
 * Encodes this [Short] into a [ByteArray].
 * @param byteOrder the [ByteOrder] in which the [Short] is encoded
 * @return the encoded [ByteArray].
 */
fun Short.toByteArray(byteOrder: ByteOrder) = copyIntoByteArray(ByteArray(Short.SIZE_BYTES), byteOrder = byteOrder)

/**
 * Encodes this [Short] and copies it into a [ByteArray] at a given offset.
 * @param array the [ByteArray] to copy the encoded data into.
 * @param offset the offset at which to copy the encoded data.
 * @param byteOrder the [ByteOrder] in which the [Short] is encoded
 * @throws IllegalArgumentException if [array] is not  is not large enough to hold 2 bytes at the [offset].
 * @return the encoded [ByteArray].
 */
fun Short.copyIntoByteArray(array: ByteArray, offset: Int = 0, byteOrder: ByteOrder): ByteArray {
    require(array.size >= offset + Short.SIZE_BYTES) { "Cannot copy into ByteArray. Must be at least ${offset + Short.SIZE_BYTES} long" }
    for (index in 0..<Short.SIZE_BYTES) {
        array[offset + index] = (this shr byteOrder.shift(index, Short.SIZE_BITS)).toByte()
    }
    return array
}

/**
 * Checks whether the bit at [index] is set in this [Short]
 * @param index the index of the bit to check.
 * @return `true` if the bit is set, `false` otherwise.
 */
fun Short.isBitSet(index: Number) = (this shr index.toInt()) and 1.toShort() == 1.toShort()

/**
 * Creates a [Short] that is equal to this [Short] except the bit at [index] is set
 * @param index the index of the bit to set.
 * @return the new [Short]
 */
fun Short.setBit(index: Number) = (this or (1.toShort() shl index.toInt()))
