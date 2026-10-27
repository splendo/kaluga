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
import com.splendo.kaluga.bytes.buildByteArray
import kotlin.test.Test
import kotlin.test.assertContentEquals

class ByteArrayBuilderExtensionsTest {

    @Test
    fun buildByteArray() {
        val byteArray = buildByteArray {
            add(true)
            add(byte = 0x01)
            add(Int24(0x040506))
            add(UInt24(0x840506u))
            add(Int40(0x0102030405))
            add(UInt40(0x8102030405u))
            add(Int48(0x010203040506))
            add(UInt48(0x810203040506u))
            add(Int24(0x040506), ByteOrder.MOST_SIGNIFICANT_FIRST)
        }

        assertContentEquals(
            byteArrayOf(
                0x01,
                0x01,
                0x06, 0x05, 0x04,
                0x06, 0x05, 0x84.toByte(),
                0x05, 0x04, 0x03, 0x02, 0x01,
                0x05, 0x04, 0x03, 0x02, 0x81.toByte(),
                0x06, 0x05, 0x04, 0x03, 0x02, 0x01,
                0x06, 0x05, 0x04, 0x03, 0x02, 0x81.toByte(),
                0x04, 0x05, 0x06,
            ),
            byteArray,
        )
    }

    @Test
    fun buildByteArrayMostSignificantFirst() {
        val byteArray = buildByteArray(ByteOrder.MOST_SIGNIFICANT_FIRST) {
            add(true)
            add(byte = 0x01)
            add(Int24(0x040506))
            add(UInt24(0x840506u))
            add(Int40(0x0102030405))
            add(UInt40(0x8102030405u))
            add(Int48(0x010203040506))
            add(UInt48(0x810203040506u))
            add(Int24(0x040506), ByteOrder.LEAST_SIGNIFICANT_FIRST)
        }

        assertContentEquals(
            byteArrayOf(
                0x06, 0x05, 0x04,
                0x81.toByte(), 0x02, 0x03, 0x04, 0x05, 0x06,
                0x01, 0x02, 0x03, 0x04, 0x05, 0x06,
                0x81.toByte(), 0x02, 0x03, 0x04, 0x05,
                0x01, 0x02, 0x03, 0x04, 0x05,
                0x84.toByte(), 0x05, 0x06,
                0x04, 0x05, 0x06,
                0x01,
                0x01,
            ),
            byteArray,
        )
    }
}
