package com.splendo.kaluga.base.bytes

/**
 * Shifts this value left by the bitCount number of bits.
 * @param bitCount the number of bits to shift by.
 * @return the shifted value.
 */
infix fun UShort.shl(bitCount: Int) = (toUInt() shl bitCount).toUShort()

/**
 * Shifts this value right by the bitCount number of bits.
 * @param bitCount the number of bits to shift by.
 * @return the shifted value.
 */
infix fun UShort.shr(bitCount: Int) = (toUInt() shr bitCount).toUShort()

/**
 * Converts [ByteArray] to [UShort]
 * @param octetIndex index of byte to start. Must not be higher than the second to last octet.
 * @param byteOrder [ByteOrder] to use for decoding.
 * @throws IllegalArgumentException if [octetIndex] is bigger than the size of the [ByteArray] minus 2.
 * @return the decoded [UShort]
 */
fun ByteArray.decodeUShort(octetIndex: Int, byteOrder: ByteOrder): UShort = decodeShort(octetIndex, byteOrder).toUShort()

/**
 * Encodes this [ULong] into a [ByteArray].
 * @param byteOrder the [ByteOrder] in which the [ULong] is encoded
 * @return the encoded [ByteArray].
 */
fun UShort.toByteArray(byteOrder: ByteOrder) = this.toShort().toByteArray(byteOrder)

/**
 * Checks whether the bit at [index] is set in this [UShort]
 * @param index the index of the bit to check.
 * @return `true` if the bit is set, `false` otherwise.
 */
fun UShort.isBitSet(index: Number) = (this.toInt() and index.toInt()) == index.toInt()

/**
 * Creates a [UShort] that is equal to this [UShort] except the bit at [index] is set
 * @param index the index of the bit to set.
 * @return the new [UShort]
 */
fun UShort.setBit(index: Number) = (this or (1.toUShort() shl index.toInt()))
