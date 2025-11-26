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

import com.splendo.kaluga.base.bytes.ByteOrder
import com.splendo.kaluga.base.bytes.decodeInt
import com.splendo.kaluga.base.bytes.decodeInt24
import com.splendo.kaluga.base.bytes.decodeLong
import com.splendo.kaluga.base.bytes.decodeShort
import com.splendo.kaluga.base.bytes.decodeUInt
import com.splendo.kaluga.base.bytes.decodeUInt24
import com.splendo.kaluga.base.bytes.decodeULong
import com.splendo.kaluga.base.bytes.decodeUShort
import com.splendo.kaluga.base.bytes.isBitSet
import com.splendo.kaluga.base.bytes.setBit
import com.splendo.kaluga.base.bytes.toByteArray
import com.splendo.kaluga.base.utils.bytesOf
import com.splendo.kaluga.base.utils.decodeHex
import com.splendo.kaluga.base.utils.toHexString
import com.splendo.kaluga.base.utils.toInt24
import com.splendo.kaluga.base.utils.toUInt24
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ByteUtilsTest {

    @Test
    fun testBytesOf() {
        assertContentEquals(
            ByteArray(0),
            bytesOf(),
        )
        assertContentEquals(
            listOf(0x0, 0x1, 0xfe.toByte(), 0xff.toByte()).toByteArray(),
            bytesOf(0x0, 0x1, 0xfe, 0xff),
        )
    }

    @Test
    fun testToHexString() {
        assertEquals(
            "",
            bytesOf().toHexString(),
        )
        assertEquals(
            "00",
            bytesOf(0x0).toHexString(),
        )
        assertEquals(
            "00,01,02,fe,ff",
            bytesOf(0x0, 0x1, 0x2, 0xfe, 0xff).toHexString(separator = ","),
        )
    }

    @Test
    fun testDecode() {
        assertNull(
            "010".decodeHex(),
        )
        assertContentEquals(
            bytesOf(),
            "".decodeHex(),
        )
        assertContentEquals(
            bytesOf(0x0),
            "00".decodeHex(),
        )
        assertContentEquals(
            bytesOf(0x0, 0x1, 0x2, 0x3, 0xfe, 0xff),
            "00010203feFF".decodeHex(),
        )
    }

    @Test
    fun byteSetting() {
        var byte: Byte = 0x00
        byte = byte.setBit(1)
        assertEquals(0x02.toByte(), byte)
        assertTrue(byte.isBitSet(1))
    }

    @Test
    fun encodeDecodeLongAsByte() {
        assertEncodeDecode(
            42L,
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeLong(0, order) },
            byteArrayOf(0x2A, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)
        )
        assertEncodeDecode(
            500L,
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeLong(0, order) },
            byteArrayOf(0xF4.toByte(), 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)
        )
        assertEncodeDecode(
            80000L,
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeLong(0, order) },
            byteArrayOf(0x80.toByte(), 0x38, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00)
        )
        assertEncodeDecode(
            150000000L,
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeLong(0, order) },
            byteArrayOf(0x80.toByte(), 0xD1.toByte(), 0xF0.toByte(), 0x08, 0x00, 0x00, 0x00, 0x00)
        )
        assertEncodeDecode(
            876543210000000000L,
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeLong(0, order) },
            byteArrayOf(0x00.toByte(), 0xA4.toByte(), 0x54.toByte(), 0xC6.toByte(), 0x67, 0x1B, 0x2A, 0x0C)
        )
        assertEncodeDecode(
            -42L,
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeLong(0, order) },
            byteArrayOf(0xD6.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte())
        )
        assertEncodeDecode(
            -500L,
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeLong(0, order) },
            byteArrayOf(0x0C.toByte(), 0xFE.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte())
        )
        assertEncodeDecode(
            -80000L,
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeLong(0, order) },
            byteArrayOf(0x80.toByte(), 0xC7.toByte(), 0xFE.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte())
        )
        assertEncodeDecode(
            -150000000L,
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeLong(0, order) },
            byteArrayOf(0x80.toByte(), 0x2E.toByte(), 0x0F.toByte(), 0xF7.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte())
        )
        assertEncodeDecode(
            -876543210000000000L,
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeLong(0, order) },
            byteArrayOf(0x00, 0x5C.toByte(), 0xAB.toByte(), 0x39.toByte(), 0x98.toByte(), 0xE4.toByte(), 0xD5.toByte(), 0xF3.toByte())
        )
    }

    @Test
    fun encodeDecodeULongAsByte() {
        assertEncodeDecode(
            42UL,
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeULong(0, order) },
            byteArrayOf(0x2A, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)
        )
        assertEncodeDecode(
            500UL,
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeULong(0, order) },
            byteArrayOf(0xF4.toByte(), 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)
        )
        assertEncodeDecode(
            80000UL,
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeULong(0, order) },
            byteArrayOf(0x80.toByte(), 0x38, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00)
        )
        assertEncodeDecode(
            150000000UL,
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeULong(0, order) },
            byteArrayOf(0x80.toByte(), 0xD1.toByte(), 0xF0.toByte(), 0x08, 0x00, 0x00, 0x00, 0x00)
        )
        assertEncodeDecode(
            876543210000000000UL,
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeULong(0, order) },
            byteArrayOf(0x00.toByte(), 0xA4.toByte(), 0x54.toByte(), 0xC6.toByte(), 0x67, 0x1B, 0x2A, 0x0C)
        )
        assertEncodeDecode(
            (-42L).toULong(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeULong(0, order) },
            byteArrayOf(0xD6.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte())
        )
        assertEncodeDecode(
            (-500L).toULong(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeULong(0, order) },
            byteArrayOf(0x0C.toByte(), 0xFE.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte())
        )
        assertEncodeDecode(
            (-80000L).toULong(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeULong(0, order) },
            byteArrayOf(0x80.toByte(), 0xC7.toByte(), 0xFE.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte())
        )
        assertEncodeDecode(
            (-150000000L).toULong(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeULong(0, order) },
            byteArrayOf(0x80.toByte(), 0x2E.toByte(), 0x0F.toByte(), 0xF7.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte())
        )
        assertEncodeDecode(
            (-876543210000000000L).toULong(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeULong(0, order) },
            byteArrayOf(0x00, 0x5C.toByte(), 0xAB.toByte(), 0x39.toByte(), 0x98.toByte(), 0xE4.toByte(), 0xD5.toByte(), 0xF3.toByte())
        )
    }

    @Test
    fun encodeDecodeIntAsByte() {
        assertEncodeDecode(
            42,
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeInt(0, order) },
            byteArrayOf(0x2A, 0x00, 0x00, 0x00)
        )
        assertEncodeDecode(
            500,
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeInt(0, order) },
            byteArrayOf(0xF4.toByte(), 0x01, 0x00, 0x00)
        )
        assertEncodeDecode(
            80000,
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeInt(0, order) },
            byteArrayOf(0x80.toByte(), 0x38, 0x01, 0x00)
        )
        assertEncodeDecode(
            150000000,
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeInt(0, order) },
            byteArrayOf(0x80.toByte(), 0xD1.toByte(), 0xF0.toByte(), 0x08)
        )
        assertEncodeDecode(
            -42,
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeInt(0, order) },
            byteArrayOf(0xD6.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte())
        )
        assertEncodeDecode(
            -500,
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeInt(0, order) },
            byteArrayOf(0x0C.toByte(), 0xFE.toByte(), 0xFF.toByte(), 0xFF.toByte())
        )
        assertEncodeDecode(
            -80000,
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeInt(0, order) },
            byteArrayOf(0x80.toByte(), 0xC7.toByte(), 0xFE.toByte(), 0xFF.toByte())
        )
        assertEncodeDecode(
            -150000000,
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeInt(0, order) },
            byteArrayOf(0x80.toByte(), 0x2E.toByte(), 0x0F.toByte(), 0xF7.toByte())
        )
    }

    @Test
    fun encodeDecodeUIntAsByte() {
        assertEncodeDecode(
            42U,
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeUInt(0, order) },
            byteArrayOf(0x2A, 0x00, 0x00, 0x00)
        )
        assertEncodeDecode(
            500U,
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeUInt(0, order) },
            byteArrayOf(0xF4.toByte(), 0x01, 0x00, 0x00)
        )
        assertEncodeDecode(
            80000U,
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeUInt(0, order) },
            byteArrayOf(0x80.toByte(), 0x38, 0x01, 0x00)
        )
        assertEncodeDecode(
            150000000U,
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeUInt(0, order) },
            byteArrayOf(0x80.toByte(), 0xD1.toByte(), 0xF0.toByte(), 0x08)
        )
        assertEncodeDecode(
            (-42).toUInt(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeUInt(0, order) },
            byteArrayOf(0xD6.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte())
        )
        assertEncodeDecode(
            (-500).toUInt(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeUInt(0, order) },
            byteArrayOf(0x0C.toByte(), 0xFE.toByte(), 0xFF.toByte(), 0xFF.toByte())
        )
        assertEncodeDecode(
            (-80000).toUInt(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeUInt(0, order) },
            byteArrayOf(0x80.toByte(), 0xC7.toByte(), 0xFE.toByte(), 0xFF.toByte())
        )
        assertEncodeDecode(
            (-150000000).toUInt(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeUInt(0, order) },
            byteArrayOf(0x80.toByte(), 0x2E.toByte(), 0x0F.toByte(), 0xF7.toByte())
        )
    }

    @Test
    fun encodeDecodeInt24AsByte() {
        assertEncodeDecode(
            42.toInt24(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeInt24(0, order) },
            byteArrayOf(0x2A, 0x00, 0x00)
        )
        assertEncodeDecode(
            500.toInt24(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeInt24(0, order) },
            byteArrayOf(0xF4.toByte(), 0x01, 0x00)
        )
        assertEncodeDecode(
            80000.toInt24(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeInt24(0, order) },
            byteArrayOf(0x80.toByte(), 0x38, 0x01)
        )
        assertEncodeDecode(
            (-42).toInt24(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeInt24(0, order) },
            byteArrayOf(0xD6.toByte(), 0xFF.toByte(), 0xFF.toByte())
        )
        assertEncodeDecode(
            (-500).toInt24(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeInt24(0, order) },
            byteArrayOf(0x0C.toByte(), 0xFE.toByte(), 0xFF.toByte())
        )
        assertEncodeDecode(
            (-80000).toInt24(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeInt24(0, order) },
            byteArrayOf(0x80.toByte(), 0xC7.toByte(), 0xFE.toByte())
        )
    }

    @Test
    fun encodeDecodeUInt24AsByte() {
        assertEncodeDecode(
            42U.toUInt24(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeUInt24(0, order) },
            byteArrayOf(0x2A, 0x00, 0x00)
        )
        assertEncodeDecode(
            500U.toUInt24(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeUInt24(0, order) },
            byteArrayOf(0xF4.toByte(), 0x01, 0x00)
        )
        assertEncodeDecode(
            80000U.toUInt24(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeUInt24(0, order) },
            byteArrayOf(0x80.toByte(), 0x38, 0x01)
        )
        assertEncodeDecode(
            (-42).toUInt().toUInt24(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeUInt24(0, order) },
            byteArrayOf(0xD6.toByte(), 0xFF.toByte(), 0xFF.toByte())
        )
        assertEncodeDecode(
            (-500).toUInt().toUInt24(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeUInt24(0, order) },
            byteArrayOf(0x0C.toByte(), 0xFE.toByte(), 0xFF.toByte())
        )
        assertEncodeDecode(
            (-80000).toUInt().toUInt24(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeUInt24(0, order) },
            byteArrayOf(0x80.toByte(), 0xC7.toByte(), 0xFE.toByte())
        )
    }

    @Test
    fun encodeDecodeShortAsByte() {
        assertEncodeDecode(
            42.toShort(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeShort(0, order) },
            byteArrayOf(0x2A, 0x00)
        )
        assertEncodeDecode(
            500.toShort(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeShort(0, order) },
            byteArrayOf(0xF4.toByte(), 0x01)
        )
        assertEncodeDecode(
            (-42).toShort(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeShort(0, order) },
            byteArrayOf(0xD6.toByte(), 0xFF.toByte())
        )
        assertEncodeDecode(
            (-500).toShort(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeShort(0, order) },
            byteArrayOf(0x0C.toByte(), 0xFE.toByte())
        )
    }

    @Test
    fun encodeDecodeUShortAsByte() {
        assertEncodeDecode(
            42U,
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeUShort(0, order) },
            byteArrayOf(0x2A, 0x00)
        )
        assertEncodeDecode(
            500U,
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeUShort(0, order) },
            byteArrayOf(0xF4.toByte(), 0x01)
        )
        assertEncodeDecode(
            (-42).toShort().toUShort(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeUShort(0, order) },
            byteArrayOf(0xD6.toByte(), 0xFF.toByte())
        )
        assertEncodeDecode(
            (-500).toShort().toUShort(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeUShort(0, order) },
            byteArrayOf(0x0C.toByte(), 0xFE.toByte())
        )
    }

    private fun <T> assertEncodeDecode(
        value: T,
        encode: (T, ByteOrder) -> ByteArray,
        decode: (ByteArray, ByteOrder) -> T,
        expected: ByteArray
    ) {
        assertContentEquals(expected, encode(value, ByteOrder.LEAST_SIGNIFICANT_FIRST))
        assertEquals(value, decode(expected, ByteOrder.LEAST_SIGNIFICANT_FIRST))
        assertContentEquals(expected.reversed().toByteArray(), encode(value, ByteOrder.MOST_SIGNIFICANT_FIRST))
        assertEquals(value, decode(expected.reversed().toByteArray(), ByteOrder.MOST_SIGNIFICANT_FIRST))
    }
}
