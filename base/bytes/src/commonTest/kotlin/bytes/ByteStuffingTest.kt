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

package bytes

import com.splendo.kaluga.base.bytes.ByteStuffingException
import com.splendo.kaluga.base.bytes.ByteStuffingScheme
import com.splendo.kaluga.base.bytes.DelimiterByteStuffingScheme
import com.splendo.kaluga.base.bytes.NonDelimiterByteStuffingScheme
import com.splendo.kaluga.base.bytes.StringEncodingSettings
import com.splendo.kaluga.base.bytes.toHexString
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ByteStuffingTest {

    private fun ByteStuffingScheme.assertRoundTrip(data: ByteArray, expectedStuffed: ByteArray) {
        val stuffed = stuff(data)
        assertTrue(stuffed.contentEquals(expectedStuffed), "Expected ${expectedStuffed.toHexString(separator = " ")} but got ${stuffed.toHexString(separator = " ")}")
        assertEquals(stuffed.size, stuffedSize(data))
        assertTrue(unstuff(stuffed).contentEquals(data), "unstuff did not round-trip")
    }

    @Test
    fun cSafeStuffing() {
        val scheme = ByteStuffingScheme.CSafe()
        // Each of 0xF0..0xF3 escapes to 0xF3 followed by its low two bits; other bytes pass through.
        scheme.assertRoundTrip(
            byteArrayOf(0xF0.toByte(), 0x0A, 0xF3.toByte(), 0x2B),
            byteArrayOf(0xF3.toByte(), 0x00, 0x0A, 0xF3.toByte(), 0x03, 0x2B),
        )
        assertTrue(scheme.escapes(0xF2.toByte()))
        assertTrue(!scheme.escapes(0x0A))
    }

    @Test
    fun xorStuffing() {
        // A scheme that protects the 0x00 delimiter: 0x00 and the escape byte are stored XOR 0x20.
        val scheme = ByteStuffingScheme.Xor(escapeByte = 0x7D, delimiter = 0x00)
        scheme.assertRoundTrip(
            byteArrayOf(0x41, 0x00, 0x7D, 0x42),
            byteArrayOf(0x41, 0x7D, 0x20, 0x7D, 0x5D, 0x42),
        )
        assertTrue(scheme.escapes(0x00))
        assertTrue(scheme.escapes(0x7D))
    }

    @Test
    fun unstuffUntilDelimiter() {
        val scheme = ByteStuffingScheme.Xor(escapeByte = 0x7D, delimiter = 0x00)
        // Content [0x41, 0x00] stuffed, then an unescaped 0x00 terminator, then trailing bytes left unread.
        val stream = byteArrayOf(0x41, 0x7D, 0x20, 0x00, 0x99.toByte())
        val iterator = stream.iterator()
        val content = scheme.unstuff(iterator)
        assertTrue(content.contentEquals(byteArrayOf(0x41, 0x00)))
        assertEquals(0x99.toByte(), iterator.next())
    }

    @Test
    fun slipStuffing() {
        // RFC 1055: END (0xC0) -> ESC ESC_END (0xDB 0xDC); ESC (0xDB) -> ESC ESC_ESC (0xDB 0xDD); other bytes pass through.
        val scheme = ByteStuffingScheme.Slip()
        scheme.assertRoundTrip(
            byteArrayOf(0xC0.toByte(), 0x41, 0xDB.toByte(), 0x42),
            byteArrayOf(0xDB.toByte(), 0xDC.toByte(), 0x41, 0xDB.toByte(), 0xDD.toByte(), 0x42),
        )
        assertTrue(scheme.escapes(0xC0.toByte()))
        assertTrue(scheme.escapes(0xDB.toByte()))
        assertTrue(!scheme.escapes(0x00))
    }

    @Test
    fun cobsStuffing() {
        val scheme = ByteStuffingScheme.Cobs
        // A single 0x00 splits the data into two blocks; the code bytes are the distances to the next zero/end.
        scheme.assertRoundTrip(
            byteArrayOf(0x11, 0x22, 0x00, 0x33),
            byteArrayOf(0x03, 0x11, 0x22, 0x02, 0x33),
        )
        // Zero-free data becomes one block prefixed by its length + 1.
        scheme.assertRoundTrip(byteArrayOf(0x11, 0x22, 0x33), byteArrayOf(0x04, 0x11, 0x22, 0x33))
        // Consecutive zeros become 0x01 code bytes; the output never contains 0x00.
        scheme.assertRoundTrip(byteArrayOf(0x00, 0x00), byteArrayOf(0x01, 0x01, 0x01))
        assertTrue(scheme.escapes(0x00))
        assertTrue(!scheme.escapes(0xF2.toByte()))
    }

    @Test
    fun cobsLongRun() {
        // A run of 254 non-zero bytes forces a 0xFF code byte (max block) and a trailing 0x01 block; still no 0x00.
        val scheme = ByteStuffingScheme.Cobs
        val data = ByteArray(300) { 0x01 }
        val stuffed = scheme.stuff(data)
        assertTrue(stuffed.none { it == 0x00.toByte() }, "COBS output must not contain 0x00")
        assertEquals(0xFF.toByte(), stuffed.first())
        assertTrue(scheme.unstuff(stuffed).contentEquals(data))
    }

    @Test
    fun cobsSizeConsistency() {
        // stuffedSize (used for the encode buffer) must match the actual encoding, and decode must round-trip,
        // across the 254-byte block boundary and mixed data.
        val scheme = ByteStuffingScheme.Cobs
        for (n in listOf(0, 1, 253, 254, 255, 508, 509)) {
            val data = ByteArray(n) { 0x01 }
            assertEquals(scheme.stuff(data).size, scheme.stuffedSize(data), "stuffedSize mismatch for n=$n")
            assertTrue(scheme.unstuff(scheme.stuff(data)).contentEquals(data), "round-trip failed for n=$n")
        }
        val mixed = byteArrayOf(0x00, 0x11, 0x00, 0x00, 0x22, 0x33)
        assertEquals(scheme.stuff(mixed).size, scheme.stuffedSize(mixed))
        assertTrue(scheme.unstuff(scheme.stuff(mixed)).contentEquals(mixed))
    }

    @Test
    fun cobsUnstuffUntilDelimiter() {
        val scheme = ByteStuffingScheme.Cobs
        // COBS-encoded [0x11, 0x00, 0x22] followed by a raw 0x00 delimiter, then a trailing byte.
        val stream = scheme.stuff(byteArrayOf(0x11, 0x00, 0x22)) + byteArrayOf(0x00, 0x99.toByte())
        val iterator = stream.iterator()
        val content = scheme.unstuff(iterator)
        assertTrue(content.contentEquals(byteArrayOf(0x11, 0x00, 0x22)))
        assertEquals(0x99.toByte(), iterator.next())
    }

    @Test
    fun schemeDelimiter() {
        // Delimiter schemes expose the byte they canonically frame with (always one they escape). CSafe carries no
        // delimiter — that it needs an explicit terminator is enforced at the type level (NonDelimiterByteStuffingScheme).
        assertEquals(0x00.toByte(), ByteStuffingScheme.Cobs.delimiter)
        assertEquals(0xC0.toByte(), ByteStuffingScheme.Slip().delimiter)
        assertEquals(0x7C.toByte(), ByteStuffingScheme.Xor(escapeByte = 0x7D, delimiter = 0x7C).delimiter)
    }

    @Test
    fun byteStuffedTerminator() {
        // A delimiter scheme supplies its own terminator via ByteStuffed.Delimited — no override is possible.
        assertEquals(0x00.toByte(), StringEncodingSettings.ByteStuffed.Delimited(ByteStuffingScheme.Cobs).terminator)
        assertEquals(0xC0.toByte(), StringEncodingSettings.ByteStuffed.Delimited(ByteStuffingScheme.Slip()).terminator)
        // A non-delimiter scheme requires an explicit terminator it escapes (CSafe escapes 0xF0..0xF3).
        assertEquals(0xF2.toByte(), StringEncodingSettings.ByteStuffed.Explicit(ByteStuffingScheme.CSafe(), 0xF2.toByte()).terminator)
        // A byte the scheme does not escape (0x00) cannot terminate; nor can the escape byte itself (0xF3), which is
        // escaped yet still emitted literally as a prefix.
        assertFailsWith<IllegalArgumentException> { StringEncodingSettings.ByteStuffed.Explicit(ByteStuffingScheme.CSafe(), 0x00) }
        assertFailsWith<IllegalArgumentException> { StringEncodingSettings.ByteStuffed.Explicit(ByteStuffingScheme.CSafe(), 0xF3.toByte()) }
    }

    @Test
    fun canTerminateWith() {
        val cSafe = ByteStuffingScheme.CSafe() // escape byte 0xF3, escapes 0xF0..0xF3
        assertTrue(cSafe.canTerminateWith(0xF2.toByte()))
        assertTrue(!cSafe.canTerminateWith(0xF3.toByte())) // the escape byte is emitted literally
        assertTrue(!cSafe.canTerminateWith(0x00)) // not escaped at all
        // A delimiter scheme's escape byte is likewise unsafe, but its delimiter is.
        val slip = ByteStuffingScheme.Slip()
        assertTrue(slip.canTerminateWith(slip.delimiter))
        assertTrue(!slip.canTerminateWith(0xDB.toByte())) // the escape byte
        assertTrue(ByteStuffingScheme.Cobs.canTerminateWith(0x00))
    }

    @Test
    fun malformedStuffedData() {
        val scheme = ByteStuffingScheme.CSafe()
        assertFailsWith<ByteStuffingException> { scheme.unstuff(byteArrayOf(0x0A, 0xF3.toByte())) }
        assertFailsWith<ByteStuffingException> { scheme.unstuff(byteArrayOf(0x0A, 0x0B).iterator()) { it == 0x00.toByte() } }
        // A 0x00 code byte is never valid COBS output.
        assertFailsWith<ByteStuffingException> { ByteStuffingScheme.Cobs.unstuff(byteArrayOf(0x00, 0x11)) }
    }

    // The folded unstuff ends at a delimiter, so append one the scheme escapes before decoding.
    private fun ByteStuffingScheme.unstuffFolded(stuffed: Sequence<Byte>): ByteArray = when (this) {
        is DelimiterByteStuffingScheme -> unstuff(stuffed + delimiter).toList().toByteArray()
        is NonDelimiterByteStuffingScheme -> {
            val terminator = (0..0xFF).map { it.toByte() }.first { canTerminateWith(it) }
            unstuff(stuffed + terminator) { it == terminator }.toList().toByteArray()
        }
        else -> error("unreachable")
    }

    // The folded Sequence<Byte> transforms must produce the same bytes as the eager array versions and round-trip.
    private fun ByteStuffingScheme.assertFoldedMatchesEager(data: ByteArray) {
        val folded = stuff(data.asSequence()).toList().toByteArray()
        val eager = stuff(data)
        assertTrue(folded.contentEquals(eager), "folded stuff ${folded.toHexString(separator = " ")} != eager ${eager.toHexString(separator = " ")}")
        assertTrue(unstuffFolded(eager.asSequence()).contentEquals(data), "folded unstuff did not round-trip")
        // Folding stuff into unstuff composes lazily and round-trips.
        assertTrue(unstuffFolded(stuff(data.asSequence())).contentEquals(data), "folded composition did not round-trip")
    }

    @Test
    fun foldedTransforms() {
        ByteStuffingScheme.CSafe().assertFoldedMatchesEager(byteArrayOf(0xF0.toByte(), 0x0A, 0xF3.toByte(), 0x2B))
        ByteStuffingScheme.CSafe().assertFoldedMatchesEager(byteArrayOf())
        ByteStuffingScheme.Xor(escapeByte = 0x7D, delimiter = 0x00).assertFoldedMatchesEager(byteArrayOf(0x41, 0x00, 0x7D, 0x42))
        ByteStuffingScheme.Slip().assertFoldedMatchesEager(byteArrayOf(0xC0.toByte(), 0x41, 0xDB.toByte(), 0x42))
        ByteStuffingScheme.Cobs.assertFoldedMatchesEager(byteArrayOf(0x11, 0x22, 0x00, 0x33))
        ByteStuffingScheme.Cobs.assertFoldedMatchesEager(byteArrayOf(0x00, 0x00))
        ByteStuffingScheme.Cobs.assertFoldedMatchesEager(byteArrayOf())
        // COBS across the 254-byte block boundary.
        ByteStuffingScheme.Cobs.assertFoldedMatchesEager(ByteArray(300) { 0x01 })
    }

    @Test
    fun foldedUnstuffIsLazy() {
        // The folded transform must pull no more of the source than the consumer takes.
        val scheme = ByteStuffingScheme.Xor(escapeByte = 0x7D, delimiter = 0x00)
        var consumed = 0
        val source = sequenceOf<Byte>(0x41, 0x42, 0x43, 0x44).onEach { consumed++ }
        assertEquals(0x41.toByte(), scheme.unstuff(source).first())
        assertEquals(1, consumed)
    }

    @Test
    fun foldedMalformed() {
        // Malformed input throws only once the folded stream is consumed up to the fault.
        // A trailing escape byte with no stored value to follow.
        assertFailsWith<ByteStuffingException> { ByteStuffingScheme.CSafe().unstuff(byteArrayOf(0x0A, 0xF3.toByte()).asSequence()) { it == 0x00.toByte() }.toList() }
        // A COBS block claiming more bytes than remain, with no delimiter.
        assertFailsWith<ByteStuffingException> { ByteStuffingScheme.Cobs.unstuff(byteArrayOf(0x05, 0x11).asSequence()).toList() }
    }
}
