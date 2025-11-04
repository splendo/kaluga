package com.splendo.kaluga.base.utils

enum class ByteOrder {
    MOST_SIGNIFICANT_FIRST,
    LEAST_SIGNIFICANT_FIRST,
}

/**
 * Returns octet index for given [index] based on this [ByteOrder] inside group of [bitsCount] Bits
 *
 * @see ByteOrder
 */
internal fun ByteOrder.octetIndex(index: Int, bitsCount: Int) = when (this) {
    ByteOrder.LEAST_SIGNIFICANT_FIRST -> index
    ByteOrder.MOST_SIGNIFICANT_FIRST -> bitsCount / Byte.SIZE_BITS - index - 1
}

/**
 * Returns numbers of bits for [Byte]
 * to be shifted (left or right) at given [index]
 * based on this [ByteOrder] inside group of [bitsCount] bits
 *
 * @see ByteOrder
 * */
internal fun ByteOrder.shift(index: Int, bitsCount: Int) = octetIndex(index, bitsCount) * Byte.SIZE_BITS
