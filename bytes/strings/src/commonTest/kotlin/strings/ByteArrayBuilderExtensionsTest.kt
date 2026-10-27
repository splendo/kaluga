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
import com.splendo.kaluga.bytes.stuffing.ByteStuffingScheme
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

    @Test
    fun buildMultiByteChars() {
        val chars = "a\u00E9\u20AC\uD800z"
        for (order in ByteOrder.entries) {
            val expected = chars.toList().let { if (order == ByteOrder.MOST_SIGNIFICANT_FIRST) it.reversed() else it }
                .fold(byteArrayOf()) { acc, char -> acc + Encoding.UTF_8.encodeChar(char, order) }
            for (expectedSize in listOf(1, 2, 3, 64)) {
                val built = buildByteArray(order, expectedSize) { chars.forEach { add(it) } }
                assertContentEquals(expected, built, "$order, expected size $expectedSize")
            }
        }
    }

    @Test
    fun buildStringsAcrossChunks() {
        val nullTerminated = StringEncodingSettings(StringEncodingSettings.NullTerminated)
        val stuffed = StringEncodingSettings(StringEncodingSettings.ByteStuffed.Delimited(ByteStuffingScheme.Cobs))
        for (order in ByteOrder.entries) {
            val parts = listOf(
                "h\u00E9llo".toByteArray(nullTerminated, order),
                "a\u0000b".toByteArray(stuffed, order),
                "x".toByteArray(StringEncodingSettings(), order),
            )
            val expected = (if (order == ByteOrder.MOST_SIGNIFICANT_FIRST) parts.reversed() else parts).reduce { acc, bytes -> acc + bytes }
            for (expectedSize in listOf(1, 3, 64)) {
                val built = buildByteArray(order, expectedSize) {
                    add("h\u00E9llo", nullTerminated)
                    add("a\u0000b", stuffed)
                    add("x")
                }
                assertContentEquals(expected, built, "$order, expected size $expectedSize")
            }
        }
    }
}
