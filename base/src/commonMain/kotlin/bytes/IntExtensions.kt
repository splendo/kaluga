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

fun Int.toByteArray(byteOrder: ByteOrder) = ByteArray(Int.SIZE_BYTES) {
    (this shr byteOrder.shift(it, Int.SIZE_BITS)).toByte()
}

fun Int.isBitSet(index: Number) = (this shr index.toInt()) and 1 == 1
fun Int.setBit(index: Number) = (this or (1 shl index.toInt()))
