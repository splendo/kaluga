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

import com.splendo.kaluga.base.utils.UInt24
import com.splendo.kaluga.base.utils.toUInt24

infix fun UInt24.shr(bitCount: Int) = (value shr bitCount).toUInt24()
infix fun UInt24.shl(bitCount: Int) = (value shl bitCount).toUInt24()

infix fun UInt24.or(other: UInt24) = UInt24(value or other.value)
infix fun UInt24.and(other: UInt24) = UInt24(value and other.value)

fun ByteArray.decodeUInt24(octetIndex: Int, byteOrder: ByteOrder): UInt24 {
    for (offset in 0..<UInt24.SIZE_BYTES) {
        require(octetIndex + offset in indices) {
            "Cannot convert ByteArray to Int24. The byte with index ${octetIndex + offset} is not available in ByteArray."
        }
    }
    return drop(octetIndex).take(UInt24.SIZE_BYTES).foldIndexed(0U) { index, acc, byte ->
        acc or ((byte.toUInt() and 0xFF.toUInt()) shl byteOrder.shift(index, UInt24.SIZE_BITS))
    }.toUInt24()
}

fun UInt24.toByteArray(byteOrder: ByteOrder) = ByteArray(UInt24.SIZE_BYTES) {
    (value shr byteOrder.shift(it, UInt24.SIZE_BITS)).toByte()
}
fun UInt24.isBitSet(index: Number) = (this shr index.toInt()) and UInt24(1U) == UInt24(1U)
fun UInt24.setBit(index: Number) = (this or (1U.toUInt24() shl index.toInt()))
