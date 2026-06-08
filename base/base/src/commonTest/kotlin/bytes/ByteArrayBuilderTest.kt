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

package com.splendo.kaluga.base.bytes

import com.splendo.kaluga.base.utils.Int24
import com.splendo.kaluga.base.utils.MedFloat16
import com.splendo.kaluga.base.utils.MedFloat32
import com.splendo.kaluga.base.utils.UInt24
import com.splendo.kaluga.base.utils.toHexString
import kotlin.test.Test
import kotlin.test.assertContentEquals

class ByteArrayBuilderTest {

    @Test
    fun buildByteArray() {
        val smallByteArray = buildByteArray {
            add(byte = 0x01)
            add(byte = 0x02)
        }
        assertContentEquals(byteArrayOf(0x01, 0x02), smallByteArray)
        val byteArray = buildByteArray {
            add(true)
            add(false)
            add(true)
            add(true)
            add(byte = 0x01)
            add(uByte = 0x81u)
            add(short = 0x0203)
            add(uShort = 0x8203u)
            add(Int24(0x040506))
            add(UInt24(0x840506u))
            add(0x0708090A)
            add(0x8708090Au)
            add(0x0102030405060708)
            add(0x8102030405060708u)
            add(0.3f)
            add(0.025)
            add(MedFloat16(0.1234))
            add(MedFloat32(0.567890))
            add('a')
            add('b', Encoding.UTF_16)
            add("Hello World")
            add("Foo Bar", StringEncodingSettings(StringEncodingSettings.NullTerminated, Encoding.UTF_16))
        }

        println(byteArray.toHexString(separator = " "))

        assertContentEquals(
            byteArrayOf(
                0b1101,
                0x01,
                0x81.toByte(),
                0x03, 0x02,
                0x03, 0x82.toByte(),
                0x06, 0x05, 0x04,
                0x06, 0x05, 0x84.toByte(),
                0x0A, 0x09, 0x08, 0x07,
                0x0A, 0x09, 0x08, 0x87.toByte(),
                0x08, 0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01,
                0x08, 0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x81.toByte(),
            ) +
                0.3f.toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST) +
                0.025.toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST) +
                MedFloat16(0.1234).toByteArray() +
                MedFloat32(0.567890).toByteArray() +
                byteArrayOf(0x61, 0x62, 0x00) +
                "Hello World".toByteArray(StringEncodingSettings(), ByteOrder.LEAST_SIGNIFICANT_FIRST) +
                "Foo Bar".toByteArray(StringEncodingSettings(StringEncodingSettings.NullTerminated, Encoding.UTF_16), ByteOrder.LEAST_SIGNIFICANT_FIRST),
            byteArray,
        )
    }

    @Test
    fun buildByteArrayMostSignificantFirst() {
        val smallByteArray = buildByteArray(ByteOrder.MOST_SIGNIFICANT_FIRST) {
            add(byte = 0x01)
            add(byte = 0x02)
        }
        assertContentEquals(byteArrayOf(0x02, 0x01), smallByteArray)
        val byteArray = buildByteArray(ByteOrder.MOST_SIGNIFICANT_FIRST) {
            add(true)
            add(false)
            add(true)
            add(true)
            add(byte = 0x01)
            add(uByte = 0x81u)
            add(short = 0x0203)
            add(uShort = 0x8203u)
            add(Int24(0x040506))
            add(UInt24(0x840506u))
            add(0x0708090A)
            add(0x8708090Au)
            add(0x0102030405060708)
            add(0x8102030405060708u)
            add(0.3f)
            add(0.025)
            add(MedFloat16(0.1234))
            add(MedFloat32(0.567890))
            add('a')
            add('b', Encoding.UTF_16)
            add("Hello World")
            add("Foo Bar", StringEncodingSettings(StringEncodingSettings.NullTerminated, Encoding.UTF_16))
        }

        println(byteArray.toHexString(separator = " "))

        assertContentEquals(
            "Foo Bar".toByteArray(StringEncodingSettings(StringEncodingSettings.NullTerminated, Encoding.UTF_16), ByteOrder.MOST_SIGNIFICANT_FIRST) +
                "Hello World".toByteArray(StringEncodingSettings(), ByteOrder.MOST_SIGNIFICANT_FIRST) +
                byteArrayOf(0x00, 0x62, 0x61) +
                MedFloat32(0.567890).toByteArray() +
                MedFloat16(0.1234).toByteArray() +
                0.025.toByteArray(ByteOrder.MOST_SIGNIFICANT_FIRST) +
                0.3f.toByteArray(ByteOrder.MOST_SIGNIFICANT_FIRST) +
                byteArrayOf(
                    0x81.toByte(), 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
                    0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
                    0x87.toByte(), 0x08, 0x09, 0x0A,
                    0x07, 0x08, 0x09, 0x0A,
                    0x84.toByte(), 0x05, 0x06,
                    0x04, 0x05, 0x06,
                    0x82.toByte(), 0x03,
                    0x02, 0x03,
                    0x81.toByte(),
                    0x01,
                    0b1101,
                ),
            byteArray,
        )
    }
}
