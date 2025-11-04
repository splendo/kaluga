package com.splendo.kaluga.base.utils

fun ByteArray.decodeULong(octetIndex: Int, byteOrder: ByteOrder): ULong = decodeLong(octetIndex, byteOrder).toULong()
fun ULong.toByteArray(byteOrder: ByteOrder) = this.toLong().toByteArray(byteOrder)

fun ULong.isBitSet(index: Number) = toLong().isBitSet(index)
fun ULong.setBit(index: Number) = toLong().setBit(index).toULong()
