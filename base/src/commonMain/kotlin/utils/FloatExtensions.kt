package com.splendo.kaluga.base.utils

/**
 * Converts [ByteArray] to [Float]
 * @param octetIndex index of byte to start
 * @param byteOrder can take [ByteOrder.MOST_SIGNIFICANT_FIRST] and [ByteOrder.LEAST_SIGNIFICANT_FIRST]
 * @throws IllegalArgumentException
 */
fun ByteArray.decodeFloat(octetIndex: Int, byteOrder: ByteOrder): Float = Float.fromBits(
    decodeInt(octetIndex, byteOrder),
)

fun Float.toByteArray(byteOrder: ByteOrder) = toRawBits().toByteArray(byteOrder)
