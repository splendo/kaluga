package com.splendo.kaluga.base.bytes

import com.splendo.kaluga.base.bytes.ByteOrder
import com.splendo.kaluga.base.bytes.shift

fun ByteArray.decodeLong(octetIndex: Int, byteOrder: ByteOrder): Long {
    for (offset in 0..<Long.SIZE_BYTES) {
        require(octetIndex + offset in indices) {
            "Cannot convert ByteArray to Long. The byte with index ${octetIndex + offset} is not available in ByteArray."
        }
    }
    return drop(octetIndex).take(Long.SIZE_BYTES).foldIndexed(0L) { index, acc, byte ->
        acc or (byte.toLong() shl byteOrder.shift(index, Long.SIZE_BITS))
    }
}

/** Converts this [Long] into [ByteArray] using given [byteOrder] */
fun Long.toByteArray(byteOrder: ByteOrder) = ByteArray(Long.SIZE_BYTES) {
    (this shr byteOrder.shift(it, Long.SIZE_BITS)).toByte()
}

fun Long.isBitSet(index: Number) = (this shr index.toInt()) and 1L == 1L
fun Long.setBit(index: Number) = (this or (1L shl index.toInt()))
