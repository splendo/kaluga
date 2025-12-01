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
fun Long.toByteArray(byteOrder: ByteOrder) = ByteArray(Long.SIZE_BYTES) {
    (this shr byteOrder.shift(it, Long.SIZE_BITS)).toByte()
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
