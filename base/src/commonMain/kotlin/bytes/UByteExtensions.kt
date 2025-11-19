package com.splendo.kaluga.base.bytes

infix fun UByte.shl(bitCount: Int) = (toUInt() shl bitCount).toUByte()
infix fun UByte.shr(bitCount: Int) = (toUInt() shr bitCount).toUByte()

fun ByteArray.decodeUByte(octetIndex: Int): UByte {
    require(octetIndex in indices) {
        "<${ByteArray::class}> is too short ($size) to get <${UByte::class}> at given index $octetIndex"
    }
    return this[octetIndex].toUByte()
}

fun UByte.toByteArray() = byteArrayOf(toByte())

fun UByte.isBitSet(index: Number) = (this shr index.toInt()) and 1U.toUByte() == 1U.toUByte()
fun UByte.setBit(index: Number) = (this or (1.toUByte() shl index.toInt()))
