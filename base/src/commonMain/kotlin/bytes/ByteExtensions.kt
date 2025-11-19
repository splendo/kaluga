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
