package com.splendo.kaluga.base.bytes

/**
 * Shifts this value left by the bitCount number of bits.
 * @param bitCount the number of bits to shift by.
 * @return the shifted value.
 */
infix fun UByte.shl(bitCount: Int) = (toUInt() shl bitCount).toUByte()

/**
 * Shifts this value right by the bitCount number of bits.
 * @param bitCount the number of bits to shift by.
 * @return the shifted value.
 */
infix fun UByte.shr(bitCount: Int) = (toUInt() shr bitCount).toUByte()

/**
 * Decodes a [UByte] from the given [ByteArray] at the given [octetIndex].
 * @param octetIndex the index of the octet to decode.
 * @return the decoded [UByte].
 */
fun ByteArray.decodeUByte(octetIndex: Int): UByte {
    require(octetIndex in indices) {
        "<${ByteArray::class}> is too short ($size) to get <${UByte::class}> at given index $octetIndex"
    }
    return this[octetIndex].toUByte()
}

/**
 * Encodes this [UByte] into a [ByteArray].
 */
fun UByte.toByteArray() = byteArrayOf(toByte())

/**
 * Checks whether the bit at [index] is set in this [UByte]
 * @param index the index of the bit to check.
 * @return `true` if the bit is set, `false` otherwise.
 */
fun UByte.isBitSet(index: Number) = (this shr index.toInt()) and 1U.toUByte() == 1U.toUByte()

/**
 * Creates a [UByte] that is equal to this [UByte] except the bit at [index] is set
 * @param index the index of the bit to set.
 * @return the new [UByte]
 */
fun UByte.setBit(index: Number) = (this or (1.toUByte() shl index.toInt()))
