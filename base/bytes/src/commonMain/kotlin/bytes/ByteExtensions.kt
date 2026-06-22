/*
 Copyright 2025 Splendo Consulting B.V. The Netherlands

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 */

package com.splendo.kaluga.base.bytes

/**
 * Shifts this value left by the bitCount number of bits.
 * @param bitCount the number of bits to shift by.
 * @return the shifted value.
 */
infix fun Byte.shl(bitCount: Int) = toUByte().shl(bitCount).toByte()

/**
 * Shifts this value right by the bitCount number of bits.
 * @param bitCount the number of bits to shift by.
 * @return the shifted value.
 */
infix fun Byte.shr(bitCount: Int) = toUByte().shr(bitCount).toByte()

/**
 * Checks whether the bit at [index] is set in this [Byte]
 * @param index the index of the bit to check.
 * @return `true` if the bit is set, `false` otherwise.
 */
fun Byte.isBitSet(index: Number) = toUByte().isBitSet(index)

/**
 * Creates a [Byte] that is equal to this [Byte] except the bit at [index] is set
 * @param index the index of the bit to set.
 * @return the new [Byte]
 */
fun Byte.setBit(index: Number) = toUByte().setBit(index).toByte()

/**
 * Checks whether the bit at [index] is set in this [ByteArray]
 * @param index the index of the bit to check.
 * @throws [IndexOutOfBoundsException] If the index is out of bounds of this array, except in Kotlin/JS where the behavior is unspecified.
 * @return `true` if the bit is set, `false` otherwise.
 */
fun ByteArray.isBitSet(index: Number) = index.toInt().let { intIndex ->
    this[intIndex / Byte.SIZE_BITS]
        .toUByte()
        .isBitSet(intIndex - (Byte.SIZE_BITS * (intIndex / Byte.SIZE_BITS)))
}

/**
 * Creates a [ByteArray] that is equal to this array except the bit at [index] is set
 * @param index the index of the bit to set.
 * @return the new [ByteArray]
 */
fun ByteArray.setBit(index: Number): ByteArray = index.toInt().let { intIndex ->
    foldIndexed(byteArrayOf()) { arrayIndex, acc, bytes ->
        acc + if (arrayIndex == intIndex / Byte.SIZE_BITS) {
            bytes.setBit(intIndex - (Byte.SIZE_BITS * arrayIndex))
        } else {
            bytes
        }
    }
}
