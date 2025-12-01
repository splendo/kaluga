package com.splendo.kaluga.base.bytes

/**
 * Converts [ByteArray] to [UInt]
 * @param octetIndex index of byte to start. Must not be higher than the fourth to last octet.
 * @param byteOrder [ByteOrder] to use for decoding.
 * @throws IllegalArgumentException if [octetIndex] is bigger than the size of the [ByteArray] minus 4.
 * @return the decoded [UInt]
 */
fun ByteArray.decodeUInt(octetIndex: Int, byteOrder: ByteOrder): UInt = decodeInt(octetIndex, byteOrder).toUInt()

/**
 * Encodes this [UInt] into a [ByteArray].
 * @param byteOrder the [ByteOrder] in which the [UInt] is encoded
 * @return the encoded [ByteArray].
 */
fun UInt.toByteArray(byteOrder: ByteOrder) = toInt().toByteArray(byteOrder)

/**
 * Checks whether the bit at [index] is set in this [UInt]
 * @param index the index of the bit to check.
 * @return `true` if the bit is set, `false` otherwise.
 */
fun UInt.isBitSet(index: Number) = (this shr index.toInt()) and 1U == 1U

/**
 * Creates a [UInt] that is equal to this [UInt] except the bit at [index] is set
 * @param index the index of the bit to set.
 * @return the new [UInt]
 */
fun UInt.setBit(index: Number) = (this or (1 shl index.toInt()).toUInt())
