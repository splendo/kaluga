package com.splendo.kaluga.base.utils

infix fun UShort.shl(bitCount: Int) = (toUInt() shl bitCount).toUShort()
infix fun UShort.shr(bitCount: Int) = (toUInt() shr bitCount).toUShort()

fun ByteArray.decodeUShort(octetIndex: Int, byteOrder: ByteOrder): UShort = decodeShort(octetIndex, byteOrder).toUShort()

/**
 * Converts [UShort] to [ByteArray]
 * @param byteOrder can take [ByteOrder.MOST_SIGNIFICANT_FIRST] or [ByteOrder.LEAST_SIGNIFICANT_FIRST]
 */
fun UShort.toByteArray(byteOrder: ByteOrder) = this.toShort().toByteArray(byteOrder)

fun UShort.isBitSet(mask: Number) = (this.toInt() and mask.toInt()) == mask.toInt()
fun UShort.setBit(index: Number) = (this or (1.toUShort() shl index.toInt()))
