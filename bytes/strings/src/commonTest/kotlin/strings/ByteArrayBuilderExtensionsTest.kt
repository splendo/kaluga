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

package com.splendo.kaluga.bytes.strings

import com.splendo.kaluga.bytes.ByteOrder
import com.splendo.kaluga.bytes.buildByteArray
import kotlin.test.Test
import kotlin.test.assertContentEquals

class ByteArrayBuilderExtensionsTest {

    @Test
    fun buildByteArray() {
        val byteArray = buildByteArray {
            add(byte = 0x01)
            add('a')
            add('b', Encoding.UTF_16)
            add("Hello World")
            add("Foo Bar", StringEncodingSettings(StringEncodingSettings.NullTerminated, Encoding.UTF_16))
        }

        assertContentEquals(
            byteArrayOf(0x01, 0x61, 0x62, 0x00) +
                "Hello World".toByteArray(StringEncodingSettings(), ByteOrder.LEAST_SIGNIFICANT_FIRST) +
                "Foo Bar".toByteArray(StringEncodingSettings(StringEncodingSettings.NullTerminated, Encoding.UTF_16), ByteOrder.LEAST_SIGNIFICANT_FIRST),
            byteArray,
        )
    }

    @Test
    fun buildByteArrayMostSignificantFirst() {
        val byteArray = buildByteArray(ByteOrder.MOST_SIGNIFICANT_FIRST) {
            add(byte = 0x01)
            add('a')
            add('b', Encoding.UTF_16)
            add("Hello World")
            add("Foo Bar", StringEncodingSettings(StringEncodingSettings.NullTerminated, Encoding.UTF_16))
        }

        assertContentEquals(
            "Foo Bar".toByteArray(StringEncodingSettings(StringEncodingSettings.NullTerminated, Encoding.UTF_16), ByteOrder.MOST_SIGNIFICANT_FIRST) +
                "Hello World".toByteArray(StringEncodingSettings(), ByteOrder.MOST_SIGNIFICANT_FIRST) +
                byteArrayOf(0x00, 0x62, 0x61, 0x01),
            byteArray,
        )
    }
}
