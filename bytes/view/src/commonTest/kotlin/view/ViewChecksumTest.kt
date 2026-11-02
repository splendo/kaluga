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
import com.splendo.kaluga.bytes.layout.ChecksumSpec
import com.splendo.kaluga.bytes.layout.InvalidChecksumException
import com.splendo.kaluga.bytes.layout.InvalidPostfix
import com.splendo.kaluga.bytes.layout.InvalidPrefix
import com.splendo.kaluga.bytes.crc.CRC16
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ViewChecksumTest {

    private val crc16 = ChecksumSpec(16, 0x8005u, 0x0000u, 0x0000u, reflectIn = true, reflectOut = true)

    @Test
    fun storesAndChecksChecksumsInEitherOrder() {
        val body = byteArrayOf(0x12, 0x34, 0x56)
        val expected = CRC16.compute(body)
        for (order in ByteOrder.entries) {
            val bytes = byteArrayOf(0x7E) + body + ByteArray(2)
            ViewChecksum.update(bytes, 1, 4, crc16, order, bytes.size)
            assertEquals(expected.toULong(), ViewChecksum.read(bytes, 4, crc16, order, bytes.size))
            ViewChecksum.check(bytes, 1, 4, crc16, order, bytes.size)
            bytes[2] = 0
            assertFailsWith<InvalidChecksumException> { ViewChecksum.check(bytes, 1, 4, crc16, order, bytes.size) }
        }
        val lsb = byteArrayOf(0x7E) + body + ByteArray(2)
        ViewChecksum.update(lsb, 1, 4, crc16, ByteOrder.LEAST_SIGNIFICANT_FIRST, lsb.size)
        assertEquals((expected and 0xFFu).toByte(), lsb[4])
    }

    @Test
    fun checksFramesLikeTheDecoder() {
        val bytes = byteArrayOf(0x01, 0x02, 0x03)
        ViewChecksum.checkPrefix(bytes, 0, byteArrayOf(0x01, 0x02), 3)
        assertFailsWith<InvalidPrefix> { ViewChecksum.checkPrefix(bytes, 0, byteArrayOf(0x02), 3) }
        ViewChecksum.checkPostfix(bytes, 2, byteArrayOf(0x03), 3)
        assertFailsWith<InvalidPostfix> { ViewChecksum.checkPostfix(bytes, 1, byteArrayOf(0x03), 3) }
    }
}
