package com.splendo.kaluga.base.bytes

import com.splendo.kaluga.base.bytes.ByteOrder

fun ByteArray.decodeUInt(octetIndex: Int, byteOrder: ByteOrder): UInt = decodeInt(octetIndex, byteOrder).toUInt()
fun UInt.toByteArray(byteOrder: ByteOrder) = toInt().toByteArray(byteOrder)

fun UInt.isBitSet(index: Number) = (this shr index.toInt()) and 1U == 1U
fun UInt.setBit(index: Number) = (this or (1 shl index.toInt()).toUInt())
