package com.splendo.kaluga.base.utils

infix fun Byte.shl(bitCount: Int) = toUByte().shl(bitCount).toByte()
infix fun Byte.shr(bitCount: Int) = toUByte().shr(bitCount).toByte()

fun Byte.isBitSet(index: Number) = toUByte().isBitSet(index)
fun Byte.setBit(index: Number) = toUByte().setBit(index).toByte()

fun ByteArray.isBitSet(index: Number) = index.toInt().let { intIndex ->
    this[intIndex / Byte.SIZE_BITS]
        .toUByte()
        .isBitSet(intIndex - (Byte.SIZE_BITS * (intIndex / Byte.SIZE_BITS)))
}

fun ByteArray.setBit(index: Number): ByteArray = index.toInt().let { intIndex ->
    foldIndexed(byteArrayOf()) { arrayIndex, acc, bytes ->
        acc + if (arrayIndex == intIndex / Byte.SIZE_BITS) {
            bytes.setBit(intIndex - (Byte.SIZE_BITS * arrayIndex))
        } else {
            bytes
        }
    }
}
