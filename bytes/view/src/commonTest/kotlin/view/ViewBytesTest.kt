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

import com.splendo.kaluga.bytes.ByteOrder
import com.splendo.kaluga.bytes.strings.Encoding
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.layout.ByteArrayEndedBeforeSerializationCompleted
import com.splendo.kaluga.bytes.integer.toByteArray
import com.splendo.kaluga.bytes.toByteArray
import com.splendo.kaluga.bytes.integer.toInt24
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.serialization.SerializationException

class ViewBytesTest {

    private val lsb = ByteOrder.LEAST_SIGNIFICANT_FIRST
    private val msb = ByteOrder.MOST_SIGNIFICANT_FIRST

    @Test
    fun rejectsAccessOutsideTheLimit() {
        val bytes = byteArrayOf(1, 2, 3, 4)
        assertEquals(4, ViewBytes.u8(bytes, 3, 4))
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { ViewBytes.u8(bytes, 4, 4) }
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { ViewBytes.u8(bytes, -1, 4) }
        // The limit, not the array size, bounds the view, so a footer is out of reach.
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { ViewBytes.readNatural(bytes, 2, Length.`16_BIT`, false, lsb, 3) }
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { ViewBytes.writeNatural(bytes, 3, 1L, Length.`16_BIT`, false, lsb, 4) }
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { ViewBytes.bit(bytes, 3, 8, 4) }
        assertFalse(ViewBytes.matches(bytes, 3, byteArrayOf(4, 0), 4))
        assertContentEquals(byteArrayOf(1, 2, 3, 4), bytes)
    }

    @Test
    fun readsSignedAndUnsignedValues() {
        val bytes = byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0x7F)
        assertEquals(255, ViewBytes.u8(bytes, 0, 3))
        assertEquals(-1, ViewBytes.s8(bytes, 0, 3))
        assertEquals(0x7FFFFFL, ViewBytes.readUnsigned(bytes, 0, 3, lsb, 3))
        assertEquals(0x7FFFFFL, ViewBytes.readSigned(bytes, 0, 3, lsb, 3))
        assertEquals(0xFFFF7FL, ViewBytes.readUnsigned(bytes, 0, 3, msb, 3))
        assertEquals(-129L, ViewBytes.readSigned(bytes, 0, 3, msb, 3))
    }

    @Test
    fun signExtendsOddWidths() {
        val negative24 = (-2).toInt24().toByteArray(lsb)
        assertEquals(-2L, ViewBytes.readSigned(negative24, 0, 3, lsb, 3))
        assertEquals(-2L, ViewBytes.readNatural(negative24, 0, Length.`24_BIT`, true, lsb, 3))
        assertEquals(0xFFFFFEL, ViewBytes.readNatural(negative24, 0, Length.`24_BIT`, false, lsb, 3))

        val negative40 = ByteArray(5) { 0xFF.toByte() }
        assertEquals(-1L, ViewBytes.readSigned(negative40, 0, 5, lsb, 5))
        assertEquals(-1L, ViewBytes.readNatural(negative40, 0, Length.`40_BIT`, true, msb, 5))
        assertEquals(0xFFFFFFFFFFL, ViewBytes.readNatural(negative40, 0, Length.`40_BIT`, false, msb, 5))

        val negative48 = ByteArray(6) { 0xFF.toByte() }.also { it[0] = 0xFE.toByte() }
        assertEquals(-2L, ViewBytes.readSigned(negative48, 0, 6, lsb, 6))
        assertEquals(-0x0100_0000_0001L, ViewBytes.readSigned(negative48, 0, 6, msb, 6))
    }

    @Test
    fun writesNaturalNumbersLikeTheEncoder() {
        val bytes = ByteArray(8)
        ViewBytes.writeNatural(bytes, 1, 0x0102L, Length.`16_BIT`, false, msb, 8)
        assertContentEquals(byteArrayOf(0, 1, 2, 0, 0, 0, 0, 0), bytes)
        ViewBytes.writeNatural(bytes, 0, -1L, Length.`64_BIT`, true, lsb, 8)
        assertContentEquals(ByteArray(8) { 0xFF.toByte() }, bytes)
        // Truncation, as Number.toByte() truncates.
        ViewBytes.writeNatural(bytes, 0, 0x1FFL, Length.`8_BIT`, false, lsb, 8)
        assertEquals(0xFF.toByte(), bytes[0])
        // A scalar wire value saturates like Double.toInt().
        ViewBytes.writeNatural(bytes, 0, 1e12, Length.`32_BIT`, true, lsb, 8)
        assertContentEquals(Int.MAX_VALUE.toByteArray(lsb), bytes.copyOfRange(0, 4))
        // Signed 24 bit values must fit.
        assertFailsWith<IllegalArgumentException> { ViewBytes.writeNatural(bytes, 0, 0x800000L, Length.`24_BIT`, true, lsb, 8) }
    }

    @Test
    fun readsAndWritesBitsAcrossBytes() {
        val bytes = ByteArray(3)
        ViewBytes.setBits(bytes, 1, 4, 8, 0xA5, 3)
        assertContentEquals(byteArrayOf(0, 0x50, 0x0A), bytes)
        assertEquals(0xA5L, ViewBytes.bits(bytes, 1, 4, 8, 3))
        assertTrue(ViewBytes.bit(bytes, 1, 4, 3))
        assertFalse(ViewBytes.bit(bytes, 1, 5, 3))

        ViewBytes.setBit(bytes, 0, 12, false, 3)
        ViewBytes.setBit(bytes, 0, 0, true, 3)
        assertContentEquals(byteArrayOf(1, 0x40, 0x0A), bytes)
    }

    @Test
    fun alignsBitsToBytes() {
        assertEquals(0, ViewBytes.alignBits(0))
        assertEquals(8, ViewBytes.alignBits(1))
        assertEquals(8, ViewBytes.alignBits(8))
        assertEquals(24, ViewBytes.alignBits(17))
    }

    @Test
    fun matchesTablesInOrder() {
        val table = arrayOf(byteArrayOf(0x01, 0x00), byteArrayOf(0x00, 0x01))
        assertEquals(1, ViewBytes.matchIndex(byteArrayOf(0x00, 0x01), 0, table, 2))
        assertFailsWith<NoSuchElementException> { ViewBytes.matchIndex(byteArrayOf(0x02, 0x00), 0, table, 2) }
        assertFailsWith<NoSuchElementException> { ViewBytes.matchIndex(byteArrayOf(0x01), 0, table, 1) }
    }

    @Test
    fun mirrorsARange() {
        val bytes = byteArrayOf(1, 2, 3, 4, 5)
        ViewBytes.mirror(bytes, 1, 5)
        assertContentEquals(byteArrayOf(1, 5, 4, 3, 2), bytes)
        ViewBytes.mirror(bytes, 0, 0)
        assertContentEquals(byteArrayOf(1, 5, 4, 3, 2), bytes)
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { ViewBytes.mirror(bytes, 3, 6) }
    }

    @Test
    fun comparesRanges() {
        val a = byteArrayOf(9, 1, 2, 3)
        val b = byteArrayOf(1, 2, 3)
        assertTrue(ViewBytes.rangeEquals(a, 1, 4, b, 0, 3))
        assertEquals(ViewBytes.rangeHashCode(a, 1, 4), ViewBytes.rangeHashCode(b, 0, 3))
        assertFalse(ViewBytes.rangeEquals(a, 0, 3, b, 0, 3))
        assertFalse(ViewBytes.rangeEquals(a, 1, 3, b, 0, 3))
        assertFalse(ViewBytes.rangeEquals(a, 2, 5, b, 0, 3))
    }

    @Test
    fun packsNaturalNumbersIntoFlagBits() {
        val bytes = ByteArray(1)
        ViewBytes.setPackedNatural(bytes, 0, 0, 4, -3, true, 1)
        assertEquals(0x0D.toByte(), bytes[0])
        assertEquals(-3L, ViewBytes.packedNatural(bytes, 0, 0, 4, true, 1))
        assertEquals(13L, ViewBytes.packedNatural(bytes, 0, 0, 4, false, 1))
        assertFailsWith<IllegalArgumentException> { ViewBytes.setPackedNatural(bytes, 0, 0, 4, 16, false, 1) }
        assertEquals("0d", ViewBytes.hex(bytes, 0, 4))
    }

    @Test
    fun readsAndWritesChars() {
        val bytes = ByteArray(2)
        ViewBytes.writeChar(bytes, 0, 'é', Encoding.UTF_16, msb, 2)
        assertContentEquals(byteArrayOf(0x00, 0xE9.toByte()), bytes)
        assertEquals('é', ViewBytes.readChar(bytes, 0, Encoding.UTF_16, msb, 2))
        ViewBytes.writeChar(bytes, 1, 'a', Encoding.ASCII, lsb, 2)
        assertEquals('a', ViewBytes.readChar(bytes, 1, Encoding.ASCII, lsb, 2))
        assertFailsWith<IllegalArgumentException> { ViewBytes.writeChar(bytes, 0, 'é', Encoding.ASCII, lsb, 2) }
    }

    @Test
    fun rejectsNonAsciiChars() {
        val bytes = ByteArray(1)
        ViewBytes.writeChar(bytes, 0, '\u007F', Encoding.ASCII, msb, 1)
        assertEquals('\u007F', ViewBytes.readChar(bytes, 0, Encoding.ASCII, msb, 1))
        assertFailsWith<IllegalArgumentException> { ViewBytes.writeChar(bytes, 0, 'é', Encoding.ASCII, lsb, 1) }
        bytes[0] = 0xC3.toByte()
        assertFailsWith<IllegalArgumentException> { ViewBytes.readChar(bytes, 0, Encoding.ASCII, lsb, 1) }
    }

    @Test
    fun rejectsEnumOrdinalsOutOfRange() {
        assertEquals(ByteOrder.LEAST_SIGNIFICANT_FIRST, ViewBytes.entry(ByteOrder.entries, 1, "ByteOrder"))
        assertFailsWith<SerializationException> { ViewBytes.entry(ByteOrder.entries, 2, "ByteOrder") }
    }

    @Test
    fun rejectsInvalidWrapRanges() {
        val bytes = ByteArray(4)
        ViewBytes.checkWrapRange(bytes, 1, 4)
        assertFailsWith<IllegalArgumentException> { ViewBytes.checkWrapRange(bytes, 2, 1) }
        assertFailsWith<IllegalArgumentException> { ViewBytes.checkWrapRange(bytes, 0, 5) }
    }
}
