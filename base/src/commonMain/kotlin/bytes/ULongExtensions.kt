package com.splendo.kaluga.base.bytes

/**
 * Converts [ByteArray] to [ULong]
 * @param octetIndex index of byte to start. Must not be higher than the eight to last octet.
 * @param byteOrder [ByteOrder] to use for decoding.
 * @throws IllegalArgumentException if [octetIndex] is bigger than the size of the [ByteArray] minus 8.
 * @return the decoded [ULong]
 */
fun ByteArray.decodeULong(octetIndex: Int, byteOrder: ByteOrder): ULong = decodeLong(octetIndex, byteOrder).toULong()

/**
 * Encodes this [ULong] into a [ByteArray].
 * @param byteOrder the [ByteOrder] in which the [ULong] is encoded
 * @return the encoded [ByteArray].
 */
fun ULong.toByteArray(byteOrder: ByteOrder) = this.toLong().toByteArray(byteOrder)

/**
 * Checks whether the bit at [index] is set in this [ULong]
 * @param index the index of the bit to check.
 * @return `true` if the bit is set, `false` otherwise.
 */
fun ULong.isBitSet(index: Number) = toLong().isBitSet(index)

/**
 * Creates a [ULong] that is equal to this [ULong] except the bit at [index] is set
 * @param index the index of the bit to set.
 * @return the new [ULong]
 */
fun ULong.setBit(index: Number) = toLong().setBit(index).toULong()
