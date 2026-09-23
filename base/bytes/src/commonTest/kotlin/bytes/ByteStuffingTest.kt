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
        val scheme = ByteStuffingScheme.Xor(escapeByte = 0x7D, escapedBytes = setOf(0x00))
        scheme.assertRoundTrip(
            byteArrayOf(0x41, 0x00, 0x7D, 0x42),
            byteArrayOf(0x41, 0x7D, 0x20, 0x7D, 0x5D, 0x42),
        )
        assertTrue(scheme.escapes(0x00))
        assertTrue(scheme.escapes(0x7D))
    }

    @Test
    fun unstuffUntilDelimiter() {
        val scheme = ByteStuffingScheme.Xor(escapeByte = 0x7D, escapedBytes = setOf(0x00))
        // Content [0x41, 0x00] stuffed, then an unescaped 0x00 terminator, then trailing bytes left unread.
        val stream = byteArrayOf(0x41, 0x7D, 0x20, 0x00, 0x99.toByte())
        val iterator = stream.iterator()
        val content = scheme.unstuffUntil(iterator) { it == 0x00.toByte() }
        assertTrue(content.contentEquals(byteArrayOf(0x41, 0x00)))
        assertEquals(0x99.toByte(), iterator.next())
    }

    @Test
    fun malformedStuffedData() {
        val scheme = ByteStuffingScheme.CSafe()
        assertFailsWith<ByteStuffingException> { scheme.unstuff(byteArrayOf(0x0A, 0xF3.toByte())) }
        assertFailsWith<ByteStuffingException> { scheme.unstuffUntil(byteArrayOf(0x0A, 0x0B).iterator()) { it == 0x00.toByte() } }
    }
}
