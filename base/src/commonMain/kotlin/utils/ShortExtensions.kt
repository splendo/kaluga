package com.splendo.kaluga.base.utils

import kotlin.experimental.and
import kotlin.experimental.or

infix fun Short.shl(bitCount: Int) = (toUInt() shl bitCount).toShort()
infix fun Short.shr(bitCount: Int) = (toUInt() shr bitCount).toShort()

fun ByteArray.decodeShort(octetIndex: Int, byteOrder: ByteOrder): Short {
    for (offset in 0..<Short.SIZE_BYTES) {
        require(octetIndex + offset in indices) {
            "Cannot convert ByteArray to Short. The byte with index ${octetIndex + offset} is not available in ByteArray."
        }
    }
    return drop(octetIndex).take(Short.SIZE_BYTES).foldIndexed(0.toShort()) { index, acc, byte ->
        acc or (byte.toShort() shl byteOrder.shift(index, Short.SIZE_BITS))
    }
}

/** Converts this [Short] into [ByteArray] using given [byteOrder] */
fun Short.toByteArray(byteOrder: ByteOrder) = ByteArray(Short.SIZE_BYTES) {
    (this shr byteOrder.shift(it, Long.SIZE_BITS)).toByte()
}
fun Short.isBitSet(index: Number) = (this shr index.toInt()) and 1.toShort() == 1.toShort()
fun Short.setBit(index: Number) = (this or (1.toShort() shl index.toInt()))
