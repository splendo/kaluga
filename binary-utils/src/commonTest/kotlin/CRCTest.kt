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

class CRCTest {

    private companion object {
        val SAMPLE_DATA = "123456789".toByteArray()
    }

    @Test
    fun testCRC8() {
        // CRC-8
        assertEquals(
            expected = 0xf4u,
            actual = CRC8_CCITT.calculate(SAMPLE_DATA, initialValue = 0u)
        )
        // CRC-8/CCITT-FALSE
        assertEquals(
            expected = 0xfbu,
            actual = CRC8_CCITT.calculate(SAMPLE_DATA, initialValue = 0xffu)
        )
    }

    @Test
    fun testCRC16() {
        // CRC-16/XMODEM
        assertEquals(
            expected = 0x31c3u,
            actual = CRC16_CCITT.calculate(SAMPLE_DATA, initialValue = 0u)
        )
        // CRC-16/CCITT-FALSE
        assertEquals(
            expected = 0x29b1u,
            actual = CRC16_CCITT.calculate(SAMPLE_DATA, initialValue = 0xffffu)
        )
    }
}
