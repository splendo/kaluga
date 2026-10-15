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
import com.splendo.kaluga.bytes.stuffing.ByteStuffingScheme
import com.splendo.kaluga.bytes.strings.Encoding
import com.splendo.kaluga.bytes.strings.StringEncodingSettings
import com.splendo.kaluga.bytes.layout.ByteArrayEndedBeforeSerializationCompleted
import com.splendo.kaluga.bytes.strings.toByteArray
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ViewStringsTest {

    private val lsb = ByteOrder.LEAST_SIGNIFICANT_FIRST

    private fun roundTrip(value: String, settings: StringEncodingSettings) {
        val encoded = value.toByteArray(settings, lsb)
        val framed = byteArrayOf(0x55) + encoded + 0x66
        assertEquals(encoded.size, ViewStrings.size(framed, 1, framed.size - 1, settings), "$settings")
        assertEquals(value, ViewStrings.read(framed, 1, framed.size - 1, settings))
        ViewStrings.check(framed, 1, framed.size - 1, settings)
        val written = ByteArray(encoded.size)
        ViewStrings.write(written, 0, written.size, value, settings, lsb)
        assertContentEquals(encoded, written)
    }

    @Test
    fun readsEveryEndMarking() {
        roundTrip("héllo", StringEncodingSettings())
        roundTrip("héllo", StringEncodingSettings(StringEncodingSettings.LengthPrefix.ShortLength, Encoding.UTF_16))
        roundTrip("x".repeat(300), StringEncodingSettings(StringEncodingSettings.LengthPrefix.WithOverflow()))
        roundTrip("short", StringEncodingSettings(StringEncodingSettings.LengthPrefix.WithOverflow(0xFE.toByte())))
        roundTrip("zé", StringEncodingSettings(StringEncodingSettings.NullTerminated, Encoding.UTF_16))
        roundTrip("abc", StringEncodingSettings(StringEncodingSettings.Terminated(0x7C), Encoding.ASCII))
        roundTrip("ab", StringEncodingSettings(StringEncodingSettings.FixedLength(4)))
        roundTrip("A|B", StringEncodingSettings(StringEncodingSettings.ByteStuffed.Delimited(ByteStuffingScheme.Xor(0x7D, 0x7C)), Encoding.ASCII))
        roundTrip("AB", StringEncodingSettings(StringEncodingSettings.ByteStuffed.Delimited(ByteStuffingScheme.Cobs), Encoding.UTF_16))
        roundTrip("\u00F2", StringEncodingSettings(StringEncodingSettings.ByteStuffed.Explicit(ByteStuffingScheme.CSafe(), 0xF2.toByte()), Encoding.UTF_16))
    }

    @Test
    fun checksStringsTheWayTheyAreRead() {
        val cutOff = StringEncodingSettings(StringEncodingSettings.FixedLength(1))
        val bytes = byteArrayOf(0xC3.toByte(), 0xA9.toByte())
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { ViewStrings.read(bytes, 0, 2, cutOff) }
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { ViewStrings.check(bytes, 0, 2, cutOff) }
        val odd = StringEncodingSettings(StringEncodingSettings.NoMarking, Encoding.UTF_16)
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { ViewStrings.read(bytes, 0, 1, odd) }
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { ViewStrings.check(bytes, 0, 1, odd) }
        ViewStrings.check(bytes, 0, 2, StringEncodingSettings(StringEncodingSettings.NoMarking))
    }

    @Test
    fun readsStringsOfMirroredFramesFromTheEnd() {
        for (settings in listOf(
            StringEncodingSettings(),
            StringEncodingSettings(StringEncodingSettings.LengthPrefix.WithOverflow()),
            StringEncodingSettings(StringEncodingSettings.NullTerminated),
        )) {
            val value = if (settings.endMarking is StringEncodingSettings.LengthPrefix.WithOverflow) "x".repeat(300) else "héllo"
            val encoded = value.toByteArray(settings, ByteOrder.MOST_SIGNIFICANT_FIRST)
            val framed = byteArrayOf(0x55) + encoded + 0x66
            val end = framed.size - 1
            assertEquals(encoded.size, ViewStrings.sizeDown(framed, end, 1, settings), "$settings")
            assertEquals(value, ViewStrings.readDown(framed, end, 1, settings))
            val written = ByteArray(encoded.size)
            ViewStrings.writeDown(written, written.size, value, settings, ByteOrder.MOST_SIGNIFICANT_FIRST)
            assertContentEquals(encoded, written)
        }
    }

    @Test
    fun readsAnUnmarkedStringUpToTheLimit() {
        val bytes = byteArrayOf(0x61, 0x62, 0x63, 0x7E)
        assertEquals(3, ViewStrings.size(bytes, 0, 3, StringEncodingSettings(StringEncodingSettings.NoMarking)))
        assertEquals("abc", ViewStrings.read(bytes, 0, 3, StringEncodingSettings(StringEncodingSettings.NoMarking)))
    }

    @Test
    fun reportsTruncatedStringsLikeTheDecoder() {
        val settings = StringEncodingSettings()
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { ViewStrings.size(byteArrayOf(5, 0x61), 0, 2, settings) }
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { ViewStrings.read(byteArrayOf(5, 0x61), 0, 2, settings) }
        // A one byte length is unsigned.
        assertEquals(129, ViewStrings.size(byteArrayOf(0x80.toByte()) + ByteArray(128), 0, 129, settings))
        val terminated = StringEncodingSettings(StringEncodingSettings.NullTerminated)
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { ViewStrings.size(byteArrayOf(0x61, 0x62), 0, 2, terminated) }
        assertFailsWith<IllegalArgumentException> { ViewStrings.write(ByteArray(4), 0, 4, "a\u0000", terminated, lsb) }
    }
}
