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

package com.splendo.kaluga.bytes.integer

import com.splendo.kaluga.bytes.ByteOrder
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class IntEncodingTest {

    @Test
    fun encodeDecodeInt24AsByte() {
        assertEncodeDecode(
            42.toInt24(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeInt24(0, order) },
            byteArrayOf(0x2A, 0x00, 0x00),
        )
        assertEncodeDecode(
            500.toInt24(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeInt24(0, order) },
            byteArrayOf(0xF4.toByte(), 0x01, 0x00),
        )
        assertEncodeDecode(
            80000.toInt24(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeInt24(0, order) },
            byteArrayOf(0x80.toByte(), 0x38, 0x01),
        )
        assertEncodeDecode(
            (-42).toInt24(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeInt24(0, order) },
            byteArrayOf(0xD6.toByte(), 0xFF.toByte(), 0xFF.toByte()),
        )
        assertEncodeDecode(
            (-500).toInt24(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeInt24(0, order) },
            byteArrayOf(0x0C.toByte(), 0xFE.toByte(), 0xFF.toByte()),
        )
        assertEncodeDecode(
            (-80000).toInt24(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeInt24(0, order) },
            byteArrayOf(0x80.toByte(), 0xC7.toByte(), 0xFE.toByte()),
        )
    }

    @Test
    fun encodeDecodeUInt24AsByte() {
        assertEncodeDecode(
            42U.toUInt24(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeUInt24(0, order) },
            byteArrayOf(0x2A, 0x00, 0x00),
        )
        assertEncodeDecode(
            500U.toUInt24(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeUInt24(0, order) },
            byteArrayOf(0xF4.toByte(), 0x01, 0x00),
        )
        assertEncodeDecode(
            80000U.toUInt24(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeUInt24(0, order) },
            byteArrayOf(0x80.toByte(), 0x38, 0x01),
        )
        assertEncodeDecode(
            (-42).toUInt().toUInt24(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeUInt24(0, order) },
            byteArrayOf(0xD6.toByte(), 0xFF.toByte(), 0xFF.toByte()),
        )
        assertEncodeDecode(
            (-500).toUInt().toUInt24(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeUInt24(0, order) },
            byteArrayOf(0x0C.toByte(), 0xFE.toByte(), 0xFF.toByte()),
        )
        assertEncodeDecode(
            (-80000).toUInt().toUInt24(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeUInt24(0, order) },
            byteArrayOf(0x80.toByte(), 0xC7.toByte(), 0xFE.toByte()),
        )
    }

    @Test
    fun encodeDecodeInt40AsByte() {
        assertEncodeDecode(
            42.toInt40(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeInt40(0, order) },
            byteArrayOf(0x2A, 0x00, 0x00, 0x00, 0x00),
        )
        assertEncodeDecode(
            0x123456789AL.toInt40(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeInt40(0, order) },
            byteArrayOf(0x9A.toByte(), 0x78, 0x56, 0x34, 0x12),
        )
        assertEncodeDecode(
            0x7FFFFFFFFFL.toInt40(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeInt40(0, order) },
            byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x7F),
        )
        assertEncodeDecode(
            (-42).toInt40(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeInt40(0, order) },
            byteArrayOf(0xD6.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte()),
        )
        assertEncodeDecode(
            (-0x8000000000L).toInt40(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeInt40(0, order) },
            byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x80.toByte()),
        )
    }

    @Test
    fun encodeDecodeUInt40AsByte() {
        assertEncodeDecode(
            42UL.toUInt40(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeUInt40(0, order) },
            byteArrayOf(0x2A, 0x00, 0x00, 0x00, 0x00),
        )
        assertEncodeDecode(
            0xAABBCCDDEEUL.toUInt40(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeUInt40(0, order) },
            byteArrayOf(0xEE.toByte(), 0xDD.toByte(), 0xCC.toByte(), 0xBB.toByte(), 0xAA.toByte()),
        )
        assertEncodeDecode(
            0xFFFFFFFFFFUL.toUInt40(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeUInt40(0, order) },
            byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte()),
        )
    }

    @Test
    fun encodeDecodeInt48AsByte() {
        assertEncodeDecode(
            42.toInt48(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeInt48(0, order) },
            byteArrayOf(0x2A, 0x00, 0x00, 0x00, 0x00, 0x00),
        )
        assertEncodeDecode(
            0x123456789ABCL.toInt48(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeInt48(0, order) },
            byteArrayOf(0xBC.toByte(), 0x9A.toByte(), 0x78, 0x56, 0x34, 0x12),
        )
        assertEncodeDecode(
            0x7FFFFFFFFFFFL.toInt48(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeInt48(0, order) },
            byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x7F),
        )
        assertEncodeDecode(
            (-42).toInt48(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeInt48(0, order) },
            byteArrayOf(0xD6.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte()),
        )
        assertEncodeDecode(
            (-0x800000000000L).toInt48(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeInt48(0, order) },
            byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, 0x80.toByte()),
        )
    }

    @Test
    fun encodeDecodeUInt48AsByte() {
        assertEncodeDecode(
            42UL.toUInt48(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeUInt48(0, order) },
            byteArrayOf(0x2A, 0x00, 0x00, 0x00, 0x00, 0x00),
        )
        assertEncodeDecode(
            0xAABBCCDDEEFFUL.toUInt48(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeUInt48(0, order) },
            byteArrayOf(0xFF.toByte(), 0xEE.toByte(), 0xDD.toByte(), 0xCC.toByte(), 0xBB.toByte(), 0xAA.toByte()),
        )
        assertEncodeDecode(
            0xFFFFFFFFFFFFUL.toUInt48(),
            { value, order -> value.toByteArray(order) },
            { value, order -> value.decodeUInt48(0, order) },
            byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte()),
        )
    }

    private fun <T> assertEncodeDecode(value: T, encode: (T, ByteOrder) -> ByteArray, decode: (ByteArray, ByteOrder) -> T, expected: ByteArray) {
        assertContentEquals(expected, encode(value, ByteOrder.LEAST_SIGNIFICANT_FIRST))
        assertEquals(value, decode(expected, ByteOrder.LEAST_SIGNIFICANT_FIRST))
        assertContentEquals(expected.reversed().toByteArray(), encode(value, ByteOrder.MOST_SIGNIFICANT_FIRST))
        assertEquals(value, decode(expected.reversed().toByteArray(), ByteOrder.MOST_SIGNIFICANT_FIRST))
    }
}
