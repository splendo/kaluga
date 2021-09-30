/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.base.test.utils

import com.splendo.kaluga.base.utils.bytesOf
import com.splendo.kaluga.base.utils.decodeHex
import com.splendo.kaluga.base.utils.toHexString
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ByteUtilsTest {

    @Test
    fun testBytesOf() {
        assertContentEquals(
            ByteArray(0),
            bytesOf()
        )
        assertContentEquals(
            listOf(0x0, 0x1, 0xfe.toByte(), 0xff.toByte()).toByteArray(),
            bytesOf(0x0, 0x1, 0xfe, 0xff)
        )
    }

    @Test
    fun testToHexString() {
        assertEquals(
            "",
            bytesOf().toHexString()
        )
        assertEquals(
            "00",
            bytesOf(0x0).toHexString()
        )
        assertEquals(
            "00,01,02,fe,ff",
            bytesOf(0x0, 0x1, 0x2, 0xfe, 0xff).toHexString(separator = ",")
        )
    }

    @Test
    fun testDecode() {
        assertNull(
            "010".decodeHex()
        )
        assertContentEquals(
            bytesOf(),
            "".decodeHex()
        )
        assertContentEquals(
            bytesOf(0x0),
            "00".decodeHex()
        )
        assertContentEquals(
            bytesOf(0x0, 0x1, 0x2, 0x3, 0xfe, 0xff),
            "00010203feFF".decodeHex()
        )
    }
}
