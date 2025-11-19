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

import com.splendo.kaluga.base.utils.Int24
import com.splendo.kaluga.base.utils.toInt24


infix fun Int24.shr(bitCount: Int) = (value shr bitCount).toInt24()
infix fun Int24.shl(bitCount: Int) = (value shl bitCount).toInt24()

infix fun Int24.or(other: Int24) = Int24(value or other.value)
infix fun Int24.and(other: Int24) = Int24(value and other.value)


fun ByteArray.decodeInt24(octetIndex: Int, byteOrder: ByteOrder): Int24 {
    for (offset in 0..<Int24.SIZE_BYTES) {
        require(octetIndex + offset in indices) {
            "Cannot convert ByteArray to Int24. The byte with index ${octetIndex + offset} is not available in ByteArray."
        }
    }
    val value = drop(octetIndex).take(Int24.SIZE_BYTES).foldIndexed(0) { index, acc, byte ->
        acc or (byte.toInt() shl byteOrder.shift(index, Int24.SIZE_BITS))
    }
    return if (value and 0x800000 != 0) {
        Int24(value or 0xFF000000.toInt())
    } else {
        Int24(value)
    }
}

fun Int24.toByteArray(byteOrder: ByteOrder) = ByteArray(Int24.SIZE_BYTES) {
    (value shr byteOrder.shift(it, Int24.SIZE_BITS)).toByte()
}
fun Int24.isBitSet(index: Number) = (this shr index.toInt()) and Int24(1) == Int24(1)
fun Int24.setBit(index: Number) = (this or (1.toInt24() shl index.toInt()))
