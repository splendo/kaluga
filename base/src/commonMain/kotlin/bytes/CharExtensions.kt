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

import com.splendo.kaluga.base.bytes.Encoding.ASCII
import com.splendo.kaluga.base.bytes.Encoding.UTF_16
import com.splendo.kaluga.base.bytes.Encoding.UTF_8

enum class Encoding(val byteSize: Int) {
    UTF_8(1),
    UTF_16(2),
    ASCII(1),
}

fun Encoding.encodeChar(char: Char, byteOrder: ByteOrder) = when (this) {
    UTF_8 -> char.toString().encodeToByteArray()
    UTF_16 -> char.toUTF16(byteOrder)
    ASCII -> char.toAscii()
}

fun Char.toUTF16(byteOrder: ByteOrder): ByteArray = code.toUShort().toByteArray(byteOrder)
fun Char.toAscii(): Byte = if (code > 0x7F) {
    throw IllegalArgumentException("Non-ASCII character: '$this' (0x${code.toString(16)})")
} else {
    code.toByte()
}

fun Char.toAsciiOrNull(): Byte? = try {
    toAscii()
} catch (_: IllegalArgumentException) {
    null
}

fun Byte.decodeUTF8Char() = toInt().toChar()
fun ByteArray.decodeUTF8Char(octetIndex: Int) = get(octetIndex).decodeUTF8Char()
fun Byte.decodeAsciiChar() = Char(toInt())
fun ByteArray.decodeAsciiChar(octetIndex: Int) = get(octetIndex).decodeAsciiChar()
fun ByteArray.decodeUTF16Char(octetIndex: Int, order: ByteOrder) = Char(decodeUShort(octetIndex, order).toInt())
