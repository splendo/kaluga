package com.splendo.kaluga.base.bytes

/**
 * Converts [ByteArray] to [Long]
 * @param octetIndex index of byte to start. Must not be higher than the eight to last octet.
 * @param byteOrder [ByteOrder] to use for decoding.
 * @throws IllegalArgumentException if [octetIndex] is bigger than the size of the [ByteArray] minus 8.
 * @return the decoded [Long]
 */
fun ByteArray.decodeLong(octetIndex: Int, byteOrder: ByteOrder): Long {
    for (offset in 0..<Long.SIZE_BYTES) {
        require(octetIndex + offset in indices) {
            "Cannot convert ByteArray to Long. The byte with index ${octetIndex + offset} is not available in ByteArray."
        }
    }
    return drop(octetIndex).take(Long.SIZE_BYTES).foldIndexed(0L) { index, acc, byte ->
        acc or ((byte.toLong() and 0xFF) shl byteOrder.shift(index, Long.SIZE_BITS))
    }
}

/**
 * Encodes this [Long] into a [ByteArray].
 * @param byteOrder the [ByteOrder] in which the [Long] is encoded
 * @return the encoded [ByteArray].
 */
fun Long.toByteArray(byteOrder: ByteOrder) = copyIntoByteArray(ByteArray(Long.SIZE_BYTES), byteOrder = byteOrder)

/**
 * Encodes this [Long] and copies it into a [ByteArray] at a given offset.
 * @param array the [ByteArray] to copy the encoded data into.
 * @param offset the offset at which to copy the encoded data.
 * @param byteOrder the [ByteOrder] in which the [Long] is encoded
 * @throws IllegalArgumentException if [array] is not  is not large enough to hold 8 bytes at the [offset].
 * @return the encoded [ByteArray].
 */
fun Long.copyIntoByteArray(array: ByteArray, offset: Int = 0, byteOrder: ByteOrder): ByteArray {
    require(array.size >= offset + Long.SIZE_BYTES) { "Cannot copy into ByteArray. Must be at least ${offset + Long.SIZE_BYTES} long" }
    for (index in 0..<Long.SIZE_BYTES) {
        array[offset + index] = (this shr byteOrder.shift(index, Long.SIZE_BITS)).toByte()
    }
    return array
}

/**
 * Checks whether the bit at [index] is set in this [Long]
 * @param index the index of the bit to check.
 * @return `true` if the bit is set, `false` otherwise.
 */
fun Long.isBitSet(index: Number) = (this shr index.toInt()) and 1L == 1L

/**
 * Creates a [Long] that is equal to this [Long] except the bit at [index] is set
 * @param index the index of the bit to set.
 * @return the new [Long]
 */
fun Long.setBit(index: Number) = (this or (1L shl index.toInt()))
