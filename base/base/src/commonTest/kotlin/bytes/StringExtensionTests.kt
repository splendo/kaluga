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

package com.splendo.kaluga.base.bytes

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFails

class StringExtensionTests {

    companion object {
        const val SHORT_STRING = "This is a short string"
        const val LONG_STRING = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. " +
            "Aenean commodo ligula eget dolor. " +
            "Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. " +
            "Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. " +
            "Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. " +
            "In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. " +
            "Nullam dictum felis eu pede mollis pretium. Integer tincidunt. " +
            "Cras dapibu"
    }

    @Test
    fun encodeShortStringWithPrefix() {
        assertContentEquals(
            SHORT_STRING.toUTF8(ByteOrder.MOST_SIGNIFICANT_FIRST),
            SHORT_STRING.encodeToByteArray().reversedArray(),
        )

        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.LengthPrefix.ByteLength, Encoding.UTF_8),
            ByteOrder.LEAST_SIGNIFICANT_FIRST,
            byteArrayOf(SHORT_STRING.length.toByte()) + SHORT_STRING.map { it.code.toByte() }.toByteArray(),
        )

        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.LengthPrefix.ByteLength, Encoding.UTF_8),
            ByteOrder.MOST_SIGNIFICANT_FIRST,
            SHORT_STRING.encodeToByteArray().reversedArray() + SHORT_STRING.length.toByte(),
        )

        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.LengthPrefix.ByteLength, Encoding.UTF_16),
            ByteOrder.LEAST_SIGNIFICANT_FIRST,
            byteArrayOf(SHORT_STRING.length.toByte()) + SHORT_STRING.fold(byteArrayOf()) { acc, char -> acc + char.toUTF16(ByteOrder.LEAST_SIGNIFICANT_FIRST) },
        )

        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.LengthPrefix.ByteLength, Encoding.UTF_16),
            ByteOrder.MOST_SIGNIFICANT_FIRST,
            SHORT_STRING.fold(byteArrayOf()) { acc, char -> char.toUTF16(ByteOrder.MOST_SIGNIFICANT_FIRST) + acc } + SHORT_STRING.length.toByte(),
        )

        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.LengthPrefix.ByteLength, Encoding.ASCII),
            ByteOrder.LEAST_SIGNIFICANT_FIRST,
            byteArrayOf(SHORT_STRING.length.toByte()) + SHORT_STRING.map { it.toAscii() }.toByteArray(),
        )

        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.LengthPrefix.ByteLength, Encoding.ASCII),
            ByteOrder.MOST_SIGNIFICANT_FIRST,
            SHORT_STRING.reversed().map { it.toAscii() }.toByteArray() + SHORT_STRING.length.toByte(),
        )

        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.LengthPrefix.ShortLength, Encoding.UTF_8),
            ByteOrder.LEAST_SIGNIFICANT_FIRST,
            SHORT_STRING.length.toUShort().toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST) + SHORT_STRING.encodeToByteArray(),
        )

        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.LengthPrefix.ShortLength, Encoding.UTF_8),
            ByteOrder.MOST_SIGNIFICANT_FIRST,
            SHORT_STRING.encodeToByteArray().reversedArray() + SHORT_STRING.length.toUShort().toByteArray(ByteOrder.MOST_SIGNIFICANT_FIRST),
        )

        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.LengthPrefix.ShortLength, Encoding.UTF_16),
            ByteOrder.LEAST_SIGNIFICANT_FIRST,
            SHORT_STRING.length.toUShort().toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST) +
                SHORT_STRING.fold(byteArrayOf()) { acc, char -> acc + char.toUTF16(ByteOrder.LEAST_SIGNIFICANT_FIRST) },
        )

        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.LengthPrefix.ShortLength, Encoding.UTF_16),
            ByteOrder.MOST_SIGNIFICANT_FIRST,
            SHORT_STRING.fold(byteArrayOf()) { acc, char -> char.toUTF16(ByteOrder.MOST_SIGNIFICANT_FIRST) + acc } +
                SHORT_STRING.length.toUShort().toByteArray(ByteOrder.MOST_SIGNIFICANT_FIRST),
        )

        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.LengthPrefix.ShortLength, Encoding.ASCII),
            ByteOrder.LEAST_SIGNIFICANT_FIRST,
            SHORT_STRING.length.toUShort().toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST) + SHORT_STRING.map { it.toAscii() }.toByteArray(),
        )

        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.LengthPrefix.ShortLength, Encoding.ASCII),
            ByteOrder.MOST_SIGNIFICANT_FIRST,
            SHORT_STRING.reversed().map { it.toAscii() }.toByteArray() + SHORT_STRING.length.toUShort().toByteArray(ByteOrder.MOST_SIGNIFICANT_FIRST),
        )

        assertFails {
            val settings = StringEncodingSettings(StringEncodingSettings.LengthPrefix.ByteLength, Encoding.UTF_8)
            val encoded = SHORT_STRING.toByteArray(settings, ByteOrder.LEAST_SIGNIFICANT_FIRST).dropLast(5).toByteArray()
            encoded.decodeString(settings, ByteOrder.LEAST_SIGNIFICANT_FIRST)
        }
    }

    @Test
    fun encodeLongStringWithPrefix() {
        LONG_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.LengthPrefix.ShortLength, Encoding.UTF_8),
            ByteOrder.LEAST_SIGNIFICANT_FIRST,
            LONG_STRING.length.toUShort().toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST) + LONG_STRING.encodeToByteArray(),
        )

        LONG_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.LengthPrefix.ShortLength, Encoding.UTF_8),
            ByteOrder.MOST_SIGNIFICANT_FIRST,
            LONG_STRING.encodeToByteArray().reversedArray() + LONG_STRING.length.toUShort().toByteArray(ByteOrder.MOST_SIGNIFICANT_FIRST),
        )

        LONG_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.LengthPrefix.ShortLength, Encoding.UTF_16),
            ByteOrder.LEAST_SIGNIFICANT_FIRST,
            LONG_STRING.length.toUShort().toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST) +
                LONG_STRING.fold(byteArrayOf()) { acc, char -> acc + char.toUTF16(ByteOrder.LEAST_SIGNIFICANT_FIRST) },
        )

        LONG_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.LengthPrefix.ShortLength, Encoding.UTF_16),
            ByteOrder.MOST_SIGNIFICANT_FIRST,
            LONG_STRING.fold(byteArrayOf()) { acc, char -> char.toUTF16(ByteOrder.MOST_SIGNIFICANT_FIRST) + acc } +
                LONG_STRING.length.toUShort().toByteArray(ByteOrder.MOST_SIGNIFICANT_FIRST),
        )

        LONG_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.LengthPrefix.ShortLength, Encoding.ASCII),
            ByteOrder.LEAST_SIGNIFICANT_FIRST,
            LONG_STRING.length.toUShort().toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST) + LONG_STRING.map { it.toAscii() }.toByteArray(),
        )

        LONG_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.LengthPrefix.ShortLength, Encoding.ASCII),
            ByteOrder.MOST_SIGNIFICANT_FIRST,
            LONG_STRING.reversed().map { it.toAscii() }.toByteArray() + LONG_STRING.length.toUShort().toByteArray(ByteOrder.MOST_SIGNIFICANT_FIRST),
        )

        assertFails { LONG_STRING.toByteArray(StringEncodingSettings(StringEncodingSettings.LengthPrefix.ByteLength, Encoding.UTF_8), ByteOrder.LEAST_SIGNIFICANT_FIRST) }

        LONG_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.LengthPrefix.WithOverflow(), Encoding.UTF_8),
            ByteOrder.LEAST_SIGNIFICANT_FIRST,
            byteArrayOf(0xFF.toByte()) + LONG_STRING.length.toUShort().toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST) + LONG_STRING.encodeToByteArray(),
        )

        LONG_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.LengthPrefix.WithOverflow(), Encoding.UTF_8),
            ByteOrder.MOST_SIGNIFICANT_FIRST,
            LONG_STRING.encodeToByteArray().reversedArray() + LONG_STRING.length.toUShort().toByteArray(ByteOrder.MOST_SIGNIFICANT_FIRST) + 0xFF.toByte(),
        )

        LONG_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.LengthPrefix.WithOverflow(), Encoding.UTF_16),
            ByteOrder.LEAST_SIGNIFICANT_FIRST,
            byteArrayOf(0xFF.toByte()) + LONG_STRING.length.toUShort().toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST) +
                LONG_STRING.fold(byteArrayOf()) { acc, char -> acc + char.toUTF16(ByteOrder.LEAST_SIGNIFICANT_FIRST) },
        )

        LONG_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.LengthPrefix.WithOverflow(), Encoding.UTF_16),
            ByteOrder.MOST_SIGNIFICANT_FIRST,
            LONG_STRING.fold(byteArrayOf()) { acc, char -> char.toUTF16(ByteOrder.MOST_SIGNIFICANT_FIRST) + acc } +
                LONG_STRING.length.toUShort().toByteArray(ByteOrder.MOST_SIGNIFICANT_FIRST) +
                0xFF.toByte(),
        )

        LONG_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.LengthPrefix.WithOverflow(), Encoding.ASCII),
            ByteOrder.LEAST_SIGNIFICANT_FIRST,
            byteArrayOf(0xFF.toByte()) + LONG_STRING.length.toUShort().toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST) + LONG_STRING.map { it.toAscii() }.toByteArray(),
        )

        LONG_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.LengthPrefix.WithOverflow(), Encoding.ASCII),
            ByteOrder.MOST_SIGNIFICANT_FIRST,
            LONG_STRING.reversed().map { it.toAscii() }.toByteArray() + LONG_STRING.length.toUShort().toByteArray(ByteOrder.MOST_SIGNIFICANT_FIRST) + 0xFF.toByte(),
        )
    }

    @Test
    fun encodeFixedLength() {
        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.FixedLength(100), Encoding.UTF_8),
            ByteOrder.LEAST_SIGNIFICANT_FIRST,
            SHORT_STRING.encodeToByteArray() + ByteArray(100 - SHORT_STRING.length),
        )

        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.FixedLength(100), Encoding.UTF_8),
            ByteOrder.MOST_SIGNIFICANT_FIRST,
            ByteArray(100 - SHORT_STRING.length) + SHORT_STRING.encodeToByteArray().reversedArray(),
        )

        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.FixedLength(10), Encoding.UTF_8),
            ByteOrder.LEAST_SIGNIFICANT_FIRST,
            SHORT_STRING.take(10).encodeToByteArray(),
            SHORT_STRING.take(10),
        )

        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.FixedLength(10), Encoding.UTF_8),
            ByteOrder.MOST_SIGNIFICANT_FIRST,
            SHORT_STRING.take(10).encodeToByteArray().reversedArray(),
            SHORT_STRING.take(10),
        )

        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.FixedLength(100), Encoding.UTF_16),
            ByteOrder.LEAST_SIGNIFICANT_FIRST,
            SHORT_STRING.fold(byteArrayOf()) { acc, char -> acc + char.toUTF16(ByteOrder.LEAST_SIGNIFICANT_FIRST) } + ByteArray(2 * (100 - SHORT_STRING.length)),
        )

        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.FixedLength(100), Encoding.UTF_16),
            ByteOrder.MOST_SIGNIFICANT_FIRST,
            ByteArray(2 * (100 - SHORT_STRING.length)) + SHORT_STRING.fold(byteArrayOf()) { acc, char -> char.toUTF16(ByteOrder.MOST_SIGNIFICANT_FIRST) + acc },
        )

        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.FixedLength(10), Encoding.UTF_16),
            ByteOrder.LEAST_SIGNIFICANT_FIRST,
            SHORT_STRING.take(10).fold(byteArrayOf()) { acc, char -> acc + char.toUTF16(ByteOrder.LEAST_SIGNIFICANT_FIRST) },
            SHORT_STRING.take(10),
        )

        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.FixedLength(10), Encoding.UTF_16),
            ByteOrder.MOST_SIGNIFICANT_FIRST,
            SHORT_STRING.take(10).fold(byteArrayOf()) { acc, char -> char.toUTF16(ByteOrder.MOST_SIGNIFICANT_FIRST) + acc },
            SHORT_STRING.take(10),
        )

        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.FixedLength(100), Encoding.ASCII),
            ByteOrder.LEAST_SIGNIFICANT_FIRST,
            SHORT_STRING.map { it.toAscii() }.toByteArray() + ByteArray(100 - SHORT_STRING.length),
        )

        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.FixedLength(100), Encoding.ASCII),
            ByteOrder.MOST_SIGNIFICANT_FIRST,
            ByteArray(100 - SHORT_STRING.length) + SHORT_STRING.reversed().map { it.toAscii() }.toByteArray(),
        )

        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.FixedLength(10), Encoding.ASCII),
            ByteOrder.LEAST_SIGNIFICANT_FIRST,
            SHORT_STRING.take(10).map { it.toAscii() }.toByteArray(),
            SHORT_STRING.take(10),
        )

        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.FixedLength(10), Encoding.ASCII),
            ByteOrder.MOST_SIGNIFICANT_FIRST,
            SHORT_STRING.take(10).reversed().map { it.toAscii() }.toByteArray(),
            SHORT_STRING.take(10),
        )
    }

    @Test
    fun encodeNullTerminated() {
        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.NullTerminated, Encoding.UTF_8),
            ByteOrder.LEAST_SIGNIFICANT_FIRST,
            SHORT_STRING.encodeToByteArray() + 0x00.toByte(),
        )

        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.NullTerminated, Encoding.UTF_8),
            ByteOrder.MOST_SIGNIFICANT_FIRST,
            byteArrayOf(0x00.toByte()) + SHORT_STRING.encodeToByteArray().reversedArray(),
        )

        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.NullTerminated, Encoding.UTF_16),
            ByteOrder.LEAST_SIGNIFICANT_FIRST,
            SHORT_STRING.fold(byteArrayOf()) { acc, character -> acc + character.toUTF16(ByteOrder.LEAST_SIGNIFICANT_FIRST) } + 0x00.toByte(),
        )

        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.NullTerminated, Encoding.UTF_16),
            ByteOrder.MOST_SIGNIFICANT_FIRST,
            byteArrayOf(0x00.toByte()) + SHORT_STRING.fold(byteArrayOf()) { acc, character -> character.toUTF16(ByteOrder.MOST_SIGNIFICANT_FIRST) + acc },
        )

        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.NullTerminated, Encoding.ASCII),
            ByteOrder.LEAST_SIGNIFICANT_FIRST,
            SHORT_STRING.map { it.toAscii() }.toByteArray() + 0x00.toByte(),
        )

        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.NullTerminated, Encoding.ASCII),
            ByteOrder.MOST_SIGNIFICANT_FIRST,
            byteArrayOf(0x00.toByte()) + SHORT_STRING.reversed().map { it.toAscii() }.toByteArray(),
        )

        assertFails {
            "Empty\u0000Test".toByteArray(StringEncodingSettings(StringEncodingSettings.NullTerminated, Encoding.UTF_8), ByteOrder.LEAST_SIGNIFICANT_FIRST)
        }
        assertFails {
            "Empty\u0000Test".toByteArray(StringEncodingSettings(StringEncodingSettings.NullTerminated, Encoding.UTF_16), ByteOrder.LEAST_SIGNIFICANT_FIRST)
        }
        assertFails {
            "Empty\u0000Test".toByteArray(StringEncodingSettings(StringEncodingSettings.NullTerminated, Encoding.ASCII), ByteOrder.LEAST_SIGNIFICANT_FIRST)
        }
    }

    @Test
    fun encodeNoEndMarking() {
        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.NoMarking, Encoding.UTF_8),
            ByteOrder.LEAST_SIGNIFICANT_FIRST,
            SHORT_STRING.encodeToByteArray(),
        )

        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.NoMarking, Encoding.UTF_8),
            ByteOrder.MOST_SIGNIFICANT_FIRST,
            SHORT_STRING.encodeToByteArray().reversedArray(),
        )

        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.NoMarking, Encoding.UTF_16),
            ByteOrder.LEAST_SIGNIFICANT_FIRST,
            SHORT_STRING.fold(byteArrayOf()) { acc, character -> acc + character.toUTF16(ByteOrder.LEAST_SIGNIFICANT_FIRST) },
        )

        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.NoMarking, Encoding.UTF_16),
            ByteOrder.MOST_SIGNIFICANT_FIRST,
            SHORT_STRING.fold(byteArrayOf()) { acc, character -> character.toUTF16(ByteOrder.MOST_SIGNIFICANT_FIRST) + acc },
        )

        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.NoMarking, Encoding.ASCII),
            ByteOrder.LEAST_SIGNIFICANT_FIRST,
            SHORT_STRING.map { it.toAscii() }.toByteArray(),
        )

        SHORT_STRING.encodeDecode(
            StringEncodingSettings(StringEncodingSettings.NoMarking, Encoding.ASCII),
            ByteOrder.MOST_SIGNIFICANT_FIRST,
            SHORT_STRING.reversed().map { it.toAscii() }.toByteArray(),
        )
    }

    private fun String.encodeDecode(settings: StringEncodingSettings, order: ByteOrder, expectedBytes: ByteArray, expectedString: String = this) {
        val actual = toByteArray(settings, order)
        assertContentEquals(expectedBytes, actual)
        assertEquals(expectedString, actual.decodeString(settings, order))
    }
}
