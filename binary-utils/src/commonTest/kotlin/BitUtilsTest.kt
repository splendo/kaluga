/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

import kotlin.test.Test
import kotlin.test.assertEquals

class BitUtilsTest {

    @Test
    fun testByteToBigEndianUShort() {
        assertEquals(
            expected = 0.toUShort(),
            actual = 0.toUByte().toBigEndianUShort()
        )
        assertEquals(
            expected = 0x0100.toUShort(),
            actual = 0x01.toUByte().toBigEndianUShort()
        )
        assertEquals(
            expected = 0xff00.toUShort(),
            actual = 0xff.toUByte().toBigEndianUShort()
        )
    }

    @Test
    fun testByteToBigEndianUInt() {
        assertEquals(
            expected = 0u,
            actual = 0.toUByte().toBigEndianUInt()
        )
        assertEquals(
            expected = 0x0100_0000u,
            actual = 0x01.toUByte().toBigEndianUInt()
        )
        assertEquals(
            expected = 0xff00_0000u,
            actual = 0xff.toUByte().toBigEndianUInt()
        )
    }
}
