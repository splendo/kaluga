/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bytes.strings

import com.splendo.kaluga.bytes.ByteOrder
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CharExtensionsTest {

    @Test
    fun utf8CharMatchesStringEncoding() {
        for (code in 0..0xFFFF) {
            val char = code.toChar()
            for (order in ByteOrder.entries) {
                val expected = char.toString().toUTF8(order)
                assertEquals(expected.size, Encoding.UTF_8.byteSizeOf(char), "size of U+${code.toString(16)}")
                assertContentEquals(expected, Encoding.UTF_8.encodeChar(char, order), "U+${code.toString(16)} $order")
            }
        }
    }

    @Test
    fun utf8CharRoundTrip() {
        for (code in 0..0xFFFF) {
            val char = code.toChar()
            val decoded = if (char.isSurrogate()) '�' else char
            for (order in ByteOrder.entries) {
                assertEquals(decoded, Encoding.UTF_8.encodeChar(char, order).decodeUTF8Char(0), "U+${code.toString(16)} $order")
            }
        }
    }

    @Test
    fun decodeUTF8CharReadsOneSequence() {
        val bytes = byteArrayOf(0x61, 0xC3.toByte(), 0xA9.toByte(), 0xAC.toByte(), 0x82.toByte(), 0xE2.toByte())
        var index = 0
        val read = List(3) { decodeUTF8Char { bytes[index++] } }
        assertEquals(listOf('a', 'é', '€'), read)
        assertEquals(bytes.size, index)
    }

    @Test
    fun decodeUTF8CharRejectsMalformed() {
        // A 4-byte sequence encodes a code point outside the range of a single Char.
        assertFailsWith<IllegalArgumentException> { byteArrayOf(0xF0.toByte(), 0x9F.toByte(), 0x98.toByte(), 0x80.toByte()).decodeUTF8Char(0) }
        assertFailsWith<IllegalArgumentException> { byteArrayOf(0xC3.toByte(), 0x41).decodeUTF8Char(0) }
        assertFailsWith<IllegalArgumentException> { byteArrayOf(0xA9.toByte(), 0x41).decodeUTF8Char(0) }
        assertFailsWith<IllegalArgumentException> { 0xC3.toByte().decodeUTF8Char() }
        assertEquals('a', 0x61.toByte().decodeUTF8Char())
    }

    @Test
    fun copyCharIntoExactFit() {
        assertContentEquals(byteArrayOf(0x62, 0x00), Encoding.UTF_16.copyCharIntoByteArray('b', ByteArray(2), byteOrder = ByteOrder.LEAST_SIGNIFICANT_FIRST))
        assertContentEquals(byteArrayOf(0x00, 0x62), Encoding.ASCII.copyCharIntoByteArray('b', byteArrayOf(0x00, 0x00), 1, ByteOrder.LEAST_SIGNIFICANT_FIRST))
        assertContentEquals(
            byteArrayOf(0xC3.toByte(), 0xA9.toByte()),
            Encoding.UTF_8.copyCharIntoByteArray('é', ByteArray(2), byteOrder = ByteOrder.LEAST_SIGNIFICANT_FIRST),
        )
        assertFailsWith<IllegalArgumentException> { Encoding.UTF_8.copyCharIntoByteArray('é', ByteArray(1), byteOrder = ByteOrder.LEAST_SIGNIFICANT_FIRST) }
        assertFailsWith<IllegalArgumentException> { Encoding.ASCII.copyCharIntoByteArray('é', ByteArray(1), byteOrder = ByteOrder.LEAST_SIGNIFICANT_FIRST) }
    }
}
