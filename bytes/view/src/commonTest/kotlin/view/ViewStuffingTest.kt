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

package com.splendo.kaluga.bytes.view

import com.splendo.kaluga.bytes.stuffing.ByteStuffingException
import com.splendo.kaluga.bytes.stuffing.ByteStuffingScheme
import com.splendo.kaluga.bytes.stuffing.DelimiterByteStuffingScheme
import com.splendo.kaluga.bytes.stuffing.NonDelimiterByteStuffingScheme
import com.splendo.kaluga.bytes.layout.ByteArrayEndedBeforeSerializationCompleted
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ViewStuffingTest {

    private val schemes: List<Pair<ByteStuffingScheme, Byte>> = listOf(
        ByteStuffingScheme.CSafe() to 0xF2.toByte(),
        ByteStuffingScheme.Xor(0x7D, 0x00) to 0x00,
        ByteStuffingScheme.Slip() to 0xC0.toByte(),
        ByteStuffingScheme.Cobs to 0x00,
    )

    private val contents = listOf(
        byteArrayOf(),
        byteArrayOf(0x01, 0x02),
        byteArrayOf(0x00, 0x7D, 0x7C, 0xC0.toByte(), 0xDB.toByte(), 0xF0.toByte(), 0xF1.toByte(), 0xF2.toByte(), 0xF3.toByte()),
        ByteArray(300) { (it % 7).toByte() },
    )

    @Test
    fun endsWhereTheDecoderStopsUnstuffing() {
        for ((scheme, terminator) in schemes) {
            for (content in contents) {
                val bytes = byteArrayOf(0x55) + scheme.stuff(content) + terminator + 0x09
                val consumed = consumedByDecoder(scheme, terminator, bytes, 1)
                assertEquals(1 + consumed, ViewStuffing.end(bytes, 1, bytes.size, scheme, terminator), "$scheme")
                assertContentEquals(content, ViewStuffing.unstuff(bytes, 1, bytes.size, scheme, terminator), "$scheme")
            }
        }
    }

    @Test
    fun writesStuffedCollectionContentThenItsTerminator() {
        for ((scheme, terminator) in schemes) {
            for (content in contents) {
                val size = ViewStuffing.sizeOf(content, scheme)
                val bytes = ByteArray(size + 2)
                assertEquals(size, ViewStuffing.write(bytes, 1, bytes.size, content, scheme, terminator))
                val stuffed = if (content.isEmpty()) byteArrayOf() else scheme.stuff(content)
                assertContentEquals(byteArrayOf(0) + stuffed + terminator + 0, bytes, "$scheme")
            }
        }
    }

    @Test
    fun failsLikeTheDecoder() {
        val xor = ByteStuffingScheme.Xor(0x7D, 0x00)
        assertEquals(
            "Stuffed data ended before an unescaped delimiter was found",
            assertFailsWith<ByteStuffingException> { ViewStuffing.end(byteArrayOf(0x01, 0x7D, 0x20), 0, 3, xor, 0x00) }.message,
        )
        assertEquals(
            "Escape byte at end of stuffed data with no value to follow",
            assertFailsWith<ByteStuffingException> { ViewStuffing.end(byteArrayOf(0x01, 0x7D), 0, 2, xor, 0x00) }.message,
        )
        assertFailsWith<ByteStuffingException> { ViewStuffing.end(byteArrayOf(0x01, 0x02), 0, 2, ByteStuffingScheme.Cobs, 0x00) }
        // The decoder stops at the end of the body, so a terminator after it, in a checksum or postfix, is not found.
        assertFailsWith<ByteStuffingException> { ViewStuffing.end(byteArrayOf(0x01, 0x7D, 0x00), 0, 2, xor, 0x00) }
        assertFailsWith<ByteStuffingException> { ViewStuffing.end(byteArrayOf(0x01, 0x02, 0x00), 0, 2, ByteStuffingScheme.Cobs, 0x00) }
        assertFailsWith<ByteStuffingException> { ViewStuffing.unstuff(byteArrayOf(0x05, 0x01, 0x00), 0, 3, ByteStuffingScheme.Cobs, 0x00) }
    }

    @Test
    fun stuffsOnlyTheBodyOfAWireFrame() {
        val scheme = ByteStuffingScheme.CSafe()
        val frame = byteArrayOf(0xF1.toByte(), 0xF1.toByte(), 0x05, 0xF2.toByte())
        val wire = ViewStuffing.toWire(frame, 1, 1, scheme)
        assertContentEquals(byteArrayOf(0xF1.toByte(), 0xF3.toByte(), 0x01, 0x05, 0xF2.toByte()), wire)
        assertContentEquals(frame, ViewStuffing.fromWire(wire, 1, 1, scheme))
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { ViewStuffing.fromWire(byteArrayOf(0xF1.toByte()), 1, 1, scheme) }
        assertFailsWith<ByteStuffingException> { ViewStuffing.fromWire(byteArrayOf(0xF1.toByte(), 0xF3.toByte(), 0xF2.toByte()), 1, 1, scheme) }
    }

    private fun consumedByDecoder(scheme: ByteStuffingScheme, terminator: Byte, bytes: ByteArray, from: Int): Int {
        var consumed = 0
        val iterator = object : Iterator<Byte> {
            override fun hasNext() = from + consumed < bytes.size
            override fun next() = bytes[from + consumed++]
        }
        when (scheme) {
            is DelimiterByteStuffingScheme -> scheme.unstuff(iterator)
            is NonDelimiterByteStuffingScheme -> scheme.unstuff(iterator) { it == terminator }
        }
        return consumed
    }
}
