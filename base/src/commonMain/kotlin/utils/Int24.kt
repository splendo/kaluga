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

package com.splendo.kaluga.base.utils

import kotlin.jvm.JvmInline

@JvmInline
value class Int24(val value: UInt)
infix fun Int24.shr(bitCount: Int) = Int24(value shr bitCount)
infix fun Int24.shl(bitCount: Int) = Int24(value shl bitCount)

infix fun Int24.or(other: Int24) = Int24(value or other.value)
infix fun Int24.and(other: Int24) = Int24(value and other.value)
fun UInt.toInt24() = Int24(this and 0xFFFFFFU)

fun ByteArray.decodeInt24(octetIndex: Int, byteOrder: ByteOrder): Int24 {
    for (offset in 0..<Int.SIZE_BYTES - 1) {
        require(octetIndex + offset in indices) {
            "Cannot convert ByteArray to Int. The byte with index ${octetIndex + offset} is not available in ByteArray."
        }
    }
    return drop(octetIndex).take(Int.SIZE_BYTES - 1).foldIndexed(0U) { index, acc, byte ->
        acc or (byte.toUInt() shl byteOrder.shift(index, Int.SIZE_BITS))
    }.toInt24()
}

fun Int24.toByteArray(byteOrder: ByteOrder) = ByteArray(Int.SIZE_BYTES - 1) {
    (value shr byteOrder.shift(it, Int.SIZE_BITS)).toByte()
}
fun Int24.isBitSet(index: Number) = (this shr index.toInt()) and Int24(1U) == Int24(1U)
fun Int24.setBit(index: Number) = (this or (1U.toInt24() shl index.toInt()))
