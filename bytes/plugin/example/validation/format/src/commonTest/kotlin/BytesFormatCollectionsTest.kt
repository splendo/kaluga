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

package com.splendo.kaluga.bytes.example.format

import com.splendo.kaluga.bytes.ByteOrder
import com.splendo.kaluga.bytes.strings.Encoding
import com.splendo.kaluga.bytes.medfloat.MedFloat16
import com.splendo.kaluga.bytes.medfloat.MedFloat32
import com.splendo.kaluga.bytes.strings.StringEncodingSettings
import com.splendo.kaluga.bytes.annotations.LengthPrefix
import com.splendo.kaluga.bytes.annotations.NullTerminated
import com.splendo.kaluga.bytes.buildByteArray
import com.splendo.kaluga.bytes.serialization.DataAfterUnconstrainedData
import com.splendo.kaluga.bytes.serialization.UnexpectedTerminator
import com.splendo.kaluga.bytes.integer.toInt24
import com.splendo.kaluga.bytes.integer.add
import com.splendo.kaluga.bytes.medfloat.add
import com.splendo.kaluga.bytes.strings.add
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.serializer

/**
 * The collection tests every backend runs; [BytesFormatCollectionsTest] adds the ones only `BytesFormat` can run.
 */
abstract class CollectionsSuite(backendFactory: ByteLayoutBackendFactory) : BytesFormatTestBase(backendFactory) {

    @Test
    fun encodeList() {
        validateEncoding(
            EncodeListFixturesListContainerData(
                MutableList(10) { EncodeListFixturesItemData(it.toByte(), (it + 10).toByte(), (it + 20).toByte()) },
                MutableList(500) { EncodeListFixturesItemData(it.toByte(), (it + 30).toByte(), (it + 40).toByte()) },
                MutableList(40) { EncodeListFixturesItemData(it.toByte(), (it - 30).toByte(), (it - 40).toByte()) },
                MutableList(33) { EncodeListFixturesItemData(it.toByte(), (it - 10).toByte(), (it - 20).toByte()) },
                MutableList(5) { EncodeListFixturesItemData(it.toByte(), (it - 50).toByte(), (it + 50).toByte()) },
                listOf(EncodeListFixturesItemData(0x01, 0x00, 0x03), EncodeListFixturesItemData(0x04, 0x05, 0x00)),
                listOf(EncodeListFixturesItemData(0x06, 0x07, 0x08)),
            ),
            buildByteArray {
                add(false) // flexibleSized
                add(true) // nullIfEmpty
                add(uByte = 10u)
                repeat(10) {
                    add(byte = it.toByte())
                    add(byte = (it + 10).toByte())
                    add(byte = (it + 20).toByte())
                }
                add(uShort = 500u)
                repeat(500) {
                    add(byte = it.toByte())
                    add(byte = (it + 30).toByte())
                    add(byte = (it + 40).toByte())
                }
                add(uByte = 40u)
                repeat(40) {
                    add(byte = it.toByte())
                    add(byte = (it - 30).toByte())
                    add(byte = (it - 40).toByte())
                }
                add(uByte = 33u)
                repeat(33) {
                    add(byte = it.toByte())
                    add(byte = (it - 10).toByte())
                    add(byte = (it - 20).toByte())
                }
                add(uByte = 5u)
                repeat(5) {
                    add(byte = it.toByte())
                    add(byte = (it - 50).toByte())
                    add(byte = (it + 50).toByte())
                }
                add(byte = 0x01)
                add(byte = 0x00)
                add(byte = 0x03)
                add(byte = 0x04)
                add(byte = 0x05)
                add(byte = 0x00)
                add(byte = 0x00)
                add(byte = 0x06)
                add(byte = 0x07)
                add(byte = 0x08)
            },
        )

        validateEncoding(
            EncodeListFixturesListContainerData(
                MutableList(8) { EncodeListFixturesItemData(it.toByte(), (it + 10).toByte(), (it + 20).toByte()) },
                MutableList(2) { EncodeListFixturesItemData(it.toByte(), (it + 30).toByte(), (it + 40).toByte()) },
                MutableList(1000) { EncodeListFixturesItemData(it.toByte(), (it - 30).toByte(), (it - 40).toByte()) },
                MutableList(800) { EncodeListFixturesItemData(it.toByte(), (it - 10).toByte(), (it - 20).toByte()) },
                emptyList(),
                listOf(EncodeListFixturesItemData(0x01, 0x02, 0x03)),
                emptyList(),
            ),
            buildByteArray {
                add(true) // flexibleSized
                add(false) // nullIfEmpty
                add(uByte = 8u)
                repeat(8) {
                    add(byte = it.toByte())
                    add(byte = (it + 10).toByte())
                    add(byte = (it + 20).toByte())
                }
                add(uShort = 2u)
                repeat(2) {
                    add(byte = it.toByte())
                    add(byte = (it + 30).toByte())
                    add(byte = (it + 40).toByte())
                }
                add(uShort = 1000u)
                repeat(1000) {
                    add(byte = it.toByte())
                    add(byte = (it - 30).toByte())
                    add(byte = (it - 40).toByte())
                }
                add(0xFF.toByte())
                add(uShort = 800u)
                repeat(800) {
                    add(byte = it.toByte())
                    add(byte = (it - 10).toByte())
                    add(byte = (it - 20).toByte())
                }
                add(byte = 0x01)
                add(byte = 0x02)
                add(byte = 0x03)
                add(byte = 0x00)
            },
        )

        assertFailsWith<UnexpectedTerminator> {
            createBackend().encode(EncodeListFixturesNullTerminatedListData.serializer(), EncodeListFixturesNullTerminatedListData(listOf(0x00.toByte(), 0x01.toByte())))
        }
    }

    @Test
    fun encodeNumericList() {
        validateEncoding(
            EncodeNumericListFixturesNumericListContainerData(
                listOf(1, 2, 3),
                listOf(4, 300, -12, 8000),
                listOf(5, null, 6),
                emptyList(),
                listOf(0.0001, 0.0002, 0.0004),
                listOf(1.5, 2.5, 3.5, 4.5),
                listOf(100, 200, 300),
                listOf(5u, 7u, 9u, 11u),
                listOf(12, 13, 14, 600).map { NumberValueContainer(it) },

            ),
            buildByteArray {
                add(false) // nullIfEmptyList
                // intList
                add(uByte = 3u)
                add(byte = 1)
                add(byte = 2)
                add(byte = 3)
                // variableSizedIntList
                add(uByte = 4u)
                add(false)
                add(byte = 4)
                add(true)
                add(short = 300)
                add(false)
                add(byte = -12)
                add(true)
                add(short = 8000)
                // nullableList
                add(uByte = 3u)
                add(true)
                add(5)
                add(false)
                add(byte = 0x01)
                add(6)
                // scalar list
                add(uByte = 3u)
                add(byte = 1)
                add(byte = 2)
                add(byte = 4)
                // medFloatList
                add(uByte = 4u)
                add(MedFloat16(1.5))
                add(MedFloat16(2.5))
                add(MedFloat16(3.5))
                add(MedFloat16(4.5))
                // mostSignificant
                add(uByte = 3u)
                add(short = 100, ByteOrder.MOST_SIGNIFICANT_FIRST)
                add(short = 200, ByteOrder.MOST_SIGNIFICANT_FIRST)
                add(short = 300, ByteOrder.MOST_SIGNIFICANT_FIRST)
                // unsignedList
                add(uByte = 4u)
                add(uByte = 5u)
                add(uByte = 7u)
                add(uByte = 9u)
                add(uByte = 11u)
                // inlineList
                add(uByte = 4u)
                add(false)
                add(byte = 12)
                add(false)
                add(byte = 13)
                add(false)
                add(byte = 14)
                add(true)
                add(short = 600)
            },
        )
    }

    @Test
    fun encodeStringList() {
        validateEncoding(
            EncodeStringListFixturesStringListContainerData(
                listOf("A", "B", "C", "D"),
                listOf("E", "F", "G", "H"),
                listOf("I", "J", "K", "L"),
                listOf("M", "N", MutableList(500) { "O" }.joinToString(), "P"),
                listOf("Q", "R", "S", "T"),
                listOf("U", "V", null, "X"),
            ),
            buildByteArray {
                add(uByte = 4u)
                add("A")
                add("B")
                add("C")
                add("D")
                add(uByte = 4u)
                add("E", settings = StringEncodingSettings(encoding = Encoding.UTF_16))
                add("F", settings = StringEncodingSettings(encoding = Encoding.UTF_16))
                add("G", settings = StringEncodingSettings(encoding = Encoding.UTF_16))
                add("H", settings = StringEncodingSettings(encoding = Encoding.UTF_16))
                add(uByte = 4u)
                add("I", settings = StringEncodingSettings(encoding = Encoding.ASCII))
                add("J", settings = StringEncodingSettings(encoding = Encoding.ASCII))
                add("K", settings = StringEncodingSettings(encoding = Encoding.ASCII))
                add("L", settings = StringEncodingSettings(encoding = Encoding.ASCII))
                add(uByte = 4u)
                add("M", settings = StringEncodingSettings(endMarking = StringEncodingSettings.LengthPrefix.WithOverflow()))
                add("N", settings = StringEncodingSettings(endMarking = StringEncodingSettings.LengthPrefix.WithOverflow()))
                add(MutableList(500) { "O" }.joinToString(), settings = StringEncodingSettings(endMarking = StringEncodingSettings.LengthPrefix.WithOverflow()))
                add("P", settings = StringEncodingSettings(endMarking = StringEncodingSettings.LengthPrefix.WithOverflow()))
                add(uByte = 4u)
                add("Q", settings = StringEncodingSettings(endMarking = StringEncodingSettings.NullTerminated))
                add("R", settings = StringEncodingSettings(endMarking = StringEncodingSettings.NullTerminated))
                add("S", settings = StringEncodingSettings(endMarking = StringEncodingSettings.NullTerminated))
                add("T", settings = StringEncodingSettings(endMarking = StringEncodingSettings.NullTerminated))
                add(uByte = 4u)
                add(true)
                add("U")
                add(true)
                add("V")
                add(false)
                add(byte = 0x01)
                add("X")
            },
        )
    }

    @Test
    fun encodeFlagIndexedLengthList() {
        validateEncoding(
            EncodeFlagIndexedLengthListFixturesContainerShortData(
                0x42,
                listOf(
                    EncodeFlagIndexedLengthListFixturesItemData(0x0A, 0x0B),
                    EncodeFlagIndexedLengthListFixturesItemData(0x0C, 0x0D),
                    EncodeFlagIndexedLengthListFixturesItemData(0x0E, 0x0F),
                ),
            ),
            buildByteArray {
                // flag header: 16 bits encoding count=3, LSB-first -> 0x03 0x00
                add(uShort = 3u)
                // body: headerByte
                add(0x42.toByte())
                // items
                add(0x0A.toByte())
                add(0x0B.toByte())
                add(0x0C.toByte())
                add(0x0D.toByte())
                add(0x0E.toByte())
                add(0x0F.toByte())
            },
        )

        // Empty list: count=0 in flag header, body field still present, no item bytes
        validateEncoding(
            EncodeFlagIndexedLengthListFixturesContainerShortData(0x99.toByte(), emptyList()),
            buildByteArray {
                add(uShort = 0u) // count = 0
                add(0x99.toByte()) // headerByte
            },
        )

        // Large count (>255) to exercise the full 16-bit range
        val largeList = MutableList(300) { EncodeFlagIndexedLengthListFixturesItemData(it.toByte(), (it + 1).toByte()) }
        val largeEncoded = buildByteArray {
            add(uShort = 300u) // 0x2C 0x01
            add(0x01.toByte()) // headerByte
            largeList.forEach {
                add(it.a)
                add(it.b)
            }
        }
        validateEncoding(EncodeFlagIndexedLengthListFixturesContainerShortData(0x01, largeList), largeEncoded)

        validateEncoding(
            EncodeFlagIndexedLengthListFixturesContainerByteData(
                0x55,
                listOf(EncodeFlagIndexedLengthListFixturesItemData(0x01, 0x02), EncodeFlagIndexedLengthListFixturesItemData(0x03, 0x04)),
            ),
            buildByteArray {
                // flag header: 8 bits encoding count=2
                add(uByte = 2u)
                // body: extra
                add(0x55.toByte())
                // items
                add(0x01.toByte())
                add(0x02.toByte())
                add(0x03.toByte())
                add(0x04.toByte())
            },
        )

        validateEncoding(
            EncodeFlagIndexedLengthListFixturesContainerWithSiblingFlagData(true, listOf(EncodeFlagIndexedLengthListFixturesItemData(0xAB.toByte(), 0xCD.toByte()))),
            buildByteArray {
                // flag header: 3 bytes (bits 0-23)
                // bits 0-15: count=1 -> 0x01, 0x00
                // bit 16: nullable present=true -> set; bit 17: value=true -> set
                // byte0=0x01, byte1=0x00, byte2=0b00000011=0x03
                add(uByte = 1u)
                add(uByte = 0u)
                add(0b00000011.toByte())
                // items
                add(0xAB.toByte())
                add(0xCD.toByte())
            },
        )
    }

    @Test
    fun encodeMap() {
        validateEncoding(
            EncodeMapFixturesMapContainerData(
                MutableList(10) { EncodeMapFixturesKeyData(it.toUShort()) to EncodeMapFixturesValueData((it + 10).toByte()) }.toMap(),
                MutableList(500) { EncodeMapFixturesKeyData(it.toUShort()) to EncodeMapFixturesValueData((it + 30).toByte()) }.toMap(),
                MutableList(40) { EncodeMapFixturesKeyData(it.toUShort()) to EncodeMapFixturesValueData((it - 30).toByte()) }.toMap(),
                MutableList(33) { EncodeMapFixturesKeyData(it.toUShort()) to EncodeMapFixturesValueData((it - 10).toByte()) }.toMap(),
                MutableList(5) { EncodeMapFixturesKeyData(it.toUShort()) to EncodeMapFixturesValueData((it - 50).toByte()) }.toMap(),
                mapOf(EncodeMapFixturesKeyData(0x01u) to EncodeMapFixturesValueData(0x00), EncodeMapFixturesKeyData(0x02u) to EncodeMapFixturesValueData(0x03)),
                mapOf(EncodeMapFixturesKeyData(0x06u) to EncodeMapFixturesValueData(0x07), EncodeMapFixturesKeyData(0x08u) to EncodeMapFixturesValueData(0x09)),
            ),
            buildByteArray {
                add(false) // flexibleSized
                add(true) // nullIfEmpty
                add(uByte = 10u)
                repeat(10) {
                    add(uShort = it.toUShort())
                    add(byte = (it + 10).toByte())
                }
                add(uShort = 500u)
                repeat(500) {
                    add(uShort = it.toUShort())
                    add(byte = (it + 30).toByte())
                }
                add(uByte = 40u)
                repeat(40) {
                    add(uShort = it.toUShort())
                    add(byte = (it - 30).toByte())
                }
                add(uByte = 33u)
                repeat(33) {
                    add(uShort = it.toUShort())
                    add(byte = (it - 10).toByte())
                }
                add(uByte = 5u)
                repeat(5) {
                    add(uShort = it.toUShort())
                    add(byte = (it - 50).toByte())
                }
                add(uShort = 0x01u)
                add(byte = 0x00)
                add(uShort = 0x02u)
                add(byte = 0x03)
                add(byte = 0x00)
                add(uShort = 0x06u)
                add(byte = 0x07)
                add(uShort = 0x08u)
                add(byte = 0x09)
            },
        )

        validateEncoding(
            EncodeMapFixturesMapContainerData(
                MutableList(8) { EncodeMapFixturesKeyData(it.toUShort()) to EncodeMapFixturesValueData((it + 10).toByte()) }.toMap(),
                MutableList(2) { EncodeMapFixturesKeyData(it.toUShort()) to EncodeMapFixturesValueData((it + 30).toByte()) }.toMap(),
                MutableList(1000) { EncodeMapFixturesKeyData(it.toUShort()) to EncodeMapFixturesValueData((it - 30).toByte()) }.toMap(),
                MutableList(800) { EncodeMapFixturesKeyData(it.toUShort()) to EncodeMapFixturesValueData((it - 10).toByte()) }.toMap(),
                emptyMap(),
                mapOf(EncodeMapFixturesKeyData(0x01u) to EncodeMapFixturesValueData(0x02), EncodeMapFixturesKeyData(0x03u) to EncodeMapFixturesValueData(0x04)),
                emptyMap(),
            ),
            buildByteArray {
                add(true) // flexibleSized
                add(false) // nullIfEmpty
                add(uByte = 8u)
                repeat(8) {
                    add(uShort = it.toUShort())
                    add(byte = (it + 10).toByte())
                }
                add(uShort = 2u)
                repeat(2) {
                    add(uShort = it.toUShort())
                    add(byte = (it + 30).toByte())
                }
                add(uShort = 1000u)
                repeat(1000) {
                    add(uShort = it.toUShort())
                    add(byte = (it - 30).toByte())
                }
                add(0xFF.toByte())
                add(uShort = 800u)
                repeat(800) {
                    add(uShort = it.toUShort())
                    add(byte = (it - 10).toByte())
                }
                add(uShort = 0x01u)
                add(byte = 0x02)
                add(uShort = 0x03u)
                add(byte = 0x04)
                add(byte = 0x00)
            },
        )

        assertFailsWith<UnexpectedTerminator> {
            createBackend().encode(EncodeMapFixturesNullTerminatedMapData.serializer(), EncodeMapFixturesNullTerminatedMapData(mapOf(0x00.toByte() to 0x01.toByte())))
        }
    }

    @Test
    fun encodeNumericMap() {
        validateEncoding(
            EncodeNumericMapFixturesNumericMapContainerData(
                mapOf(1.toShort() to 2, 3.toShort() to 4),
                mapOf(5.toShort() to 5000000, 300.toShort() to 10000000),
                mapOf(6.toShort() to 11, null to 12, 7.toShort() to null),
                mapOf(0.25 to 0.125, 0.75 to 5.0),
                mapOf(0.25 to 0.5, 5.0 to 7.0, 45.0 to 0.98),
                mapOf((-10).toShort() to 999, 1000.toShort() to -54),
                mapOf(20u.toUShort() to 777777u, 25u.toUShort() to 89898989u),
                mapOf(NumberValueContainer(30.toShort()) to ValueContainer(12345), NumberValueContainer(400.toShort()) to ValueContainer(3456)),

            ),
            buildByteArray {
                // fixedMap
                add(uByte = 2u)
                add(byte = 1)
                add(2.toInt24())
                add(byte = 3)
                add(4.toInt24())
                // variableSizedMap
                add(uByte = 2u)
                add(false)
                add(byte = 5)
                add(false)
                add(5000000.toInt24())
                add(true)
                add(short = 300)
                add(true)
                add(10000000)
                // nullableMap
                add(uByte = 3u)
                add(true)
                add(short = 6)
                add(true)
                add(11)
                add(byte = 0x00)
                add(true)
                add(12)
                add(true)
                add(short = 7)
                add(false)
                // scalarMap
                add(uByte = 2u)
                add(byte = 25)
                add(125.toInt24())
                add(byte = 75)
                add(5000.toInt24())
                add(uByte = 3u)
                add(MedFloat16(0.25))
                add(MedFloat32(0.5))
                add(MedFloat16(5.0))
                add(MedFloat32(7.0))
                add(MedFloat16(45.0))
                add(MedFloat32(0.98))
                add(uByte = 2u)
                add(short = -10, order = ByteOrder.MOST_SIGNIFICANT_FIRST)
                add(999, order = ByteOrder.MOST_SIGNIFICANT_FIRST)
                add(short = 1000, order = ByteOrder.MOST_SIGNIFICANT_FIRST)
                add(-54, order = ByteOrder.MOST_SIGNIFICANT_FIRST)
                add(uByte = 2u)
                add(uShort = 20u)
                add(777777u)
                add(uShort = 25u)
                add(89898989u)
                add(uByte = 2u)
                add(false)
                add(byte = 30)
                add(12345)
                add(true)
                add(short = 400)
                add(3456)
            },
        )
    }

    @Test
    fun encodeStringMap() {
        validateEncoding(
            EncodeStringMapFixturesStringMapContainerData(
                mapOf('A' to "Alfa", 'B' to "Bravo", 'C' to "Charlie"),
                mapOf('D' to "Delta", 'E' to "Echo", 'F' to "Foxtrot"),
                mapOf('G' to "Golf", 'H' to "Hotel", 'I' to "India"),
                mapOf("Key" to "Value", "Long" to MutableList(1000) { "A" }.joinToString()),
                mapOf("" to "Empty", "Empty" to ""),
                mapOf("Key" to "Value", null to "Empty", "NoKey" to null),
            ),
            buildByteArray {
                add(uByte = 3u)
                add('A', Encoding.UTF_8)
                add("Alfa", StringEncodingSettings(encoding = Encoding.UTF_8))
                add('B', Encoding.UTF_8)
                add("Bravo", StringEncodingSettings(encoding = Encoding.UTF_8))
                add('C', Encoding.UTF_8)
                add("Charlie", StringEncodingSettings(encoding = Encoding.UTF_8))
                add(uByte = 3u)
                add('D', Encoding.UTF_16)
                add("Delta", StringEncodingSettings(encoding = Encoding.UTF_16))
                add('E', Encoding.UTF_16)
                add("Echo", StringEncodingSettings(encoding = Encoding.UTF_16))
                add('F', Encoding.UTF_16)
                add("Foxtrot", StringEncodingSettings(encoding = Encoding.UTF_16))
                add(uByte = 3u)
                add('G', Encoding.ASCII)
                add("Golf", StringEncodingSettings(encoding = Encoding.ASCII))
                add('H', Encoding.ASCII)
                add("Hotel", StringEncodingSettings(encoding = Encoding.ASCII))
                add('I', Encoding.ASCII)
                add("India", StringEncodingSettings(encoding = Encoding.ASCII))
                add(uByte = 2u)
                add("Key", StringEncodingSettings(endMarking = StringEncodingSettings.LengthPrefix.ShortLength))
                add("Value", StringEncodingSettings(endMarking = StringEncodingSettings.LengthPrefix.WithOverflow()))
                add("Long", StringEncodingSettings(endMarking = StringEncodingSettings.LengthPrefix.ShortLength))
                add(MutableList(1000) { "A" }.joinToString(), StringEncodingSettings(endMarking = StringEncodingSettings.LengthPrefix.WithOverflow()))
                add(uByte = 2u)
                add("", StringEncodingSettings(endMarking = StringEncodingSettings.NullTerminated))
                add("Empty", StringEncodingSettings(endMarking = StringEncodingSettings.NullTerminated))
                add("Empty", StringEncodingSettings(endMarking = StringEncodingSettings.NullTerminated))
                add("", StringEncodingSettings(endMarking = StringEncodingSettings.NullTerminated))
                add(uByte = 3u)
                add(true)
                add("Key")
                add(true)
                add("Value")
                add(byte = 0x00)
                add(true)
                add("Empty")
                add(true)
                add("NoKey")
                add(false)
            },
        )
    }

    @Test
    fun encodeValueOnlyNullTerminatedMap() {
        validateEncoding(
            EncodeValueOnlyNullTerminatedMapFixturesValueNullTerminatedMapContainerData(
                mapOf("Key" to "Value", "Other" to "Thing"),
            ),
            EncodeValueOnlyNullTerminatedMapFixturesValueNullTerminatedMapContainerData.serializer(),
            buildByteArray {
                add(uByte = 2u)
                add("Key") // default ByteLength prefix
                add("Value", StringEncodingSettings(endMarking = StringEncodingSettings.NullTerminated))
                add("Other") // default ByteLength prefix
                add("Thing", StringEncodingSettings(endMarking = StringEncodingSettings.NullTerminated))
            },
        )

        validateEncoding(
            EncodeValueOnlyNullTerminatedMapFixturesKeyNullTerminatedMapContainerData(
                mapOf("Key" to "Value", "Other" to "Thing"),
            ),
            EncodeValueOnlyNullTerminatedMapFixturesKeyNullTerminatedMapContainerData.serializer(),
            buildByteArray {
                add(uByte = 2u)
                add("Key", StringEncodingSettings(endMarking = StringEncodingSettings.NullTerminated))
                add("Value") // default ByteLength prefix
                add("Other", StringEncodingSettings(endMarking = StringEncodingSettings.NullTerminated))
                add("Thing") // default ByteLength prefix
            },
        )
    }

    @Test
    fun msbExcludeStructureListAndMapPreserveItemOrder() {
        // Items accumulate in the frame's sequential direction; only their values follow the big-endian value order.
        validateEncoding(
            MsbExcludeStructureListAndMapPreserveItemOrderFixturesMsbCollectionFrameData(
                items = listOf(1, 2, 3),
                pairs = linkedMapOf(1.toByte() to 10.toByte(), 2.toByte() to 20.toByte(), 3.toByte() to 30.toByte()),
            ),
            MsbExcludeStructureListAndMapPreserveItemOrderFixturesMsbCollectionFrameData.serializer(),
            byteArrayOf(0x03, 0x01, 0x02, 0x03, 0x03, 0x01, 0x0A, 0x02, 0x14, 0x03, 0x1E),
        )
    }

    @Test
    fun msbOverflowLengthPrefixedListStructureIncludedRoundTrips() {
        validateRoundTrip(MsbListCountFixturesMsbOverflowListIncludedData(items = List(300) { it.toByte() }), MsbListCountFixturesMsbOverflowListIncludedData.serializer())
    }

    @Test
    fun msbSixteenBitListCountStructureIncludedRoundTrips() {
        validateRoundTrip(MsbListCountFixturesMsbSizedListIncludedData(items = listOf<Byte>(1, 2, 3, 4)), MsbListCountFixturesMsbSizedListIncludedData.serializer())
    }

    @Test
    fun itemLevelPrefixPostfixChecksumOnListItemRoundTrips() {
        // Each item's frame is written once, by the item's own encoder.
        validateRoundTrip(
            ItemLevelPrefixPostfixChecksumOnListItemRoundTripsFixturesFramedChecksumItemListHolderData(
                items = listOf(
                    ItemLevelPrefixPostfixChecksumOnListItemRoundTripsFixturesFramedChecksumItemData(0x11),
                    ItemLevelPrefixPostfixChecksumOnListItemRoundTripsFixturesFramedChecksumItemData(0x22),
                ),
                trailer = 0x2A,
            ),
            ItemLevelPrefixPostfixChecksumOnListItemRoundTripsFixturesFramedChecksumItemListHolderData.serializer(),
        )
    }

    @Test
    fun nullableListWithoutNullIfEmptyRoundTrips() {
        // Without @NullIfEmpty an empty list is present (its zero count is written), distinct from an absent one.
        validateRoundTrip(
            NullableListWithoutNullIfEmptyRoundTripsFixturesNullableListNoNullIfEmptyData(items = null, trailer = 0x2A),
            NullableListWithoutNullIfEmptyRoundTripsFixturesNullableListNoNullIfEmptyData.serializer(),
        )
        validateRoundTrip(
            NullableListWithoutNullIfEmptyRoundTripsFixturesNullableListNoNullIfEmptyData(items = emptyList(), trailer = 0x2A),
            NullableListWithoutNullIfEmptyRoundTripsFixturesNullableListNoNullIfEmptyData.serializer(),
        )
    }

    @Test
    fun msbOverflowLengthPrefixedListStructureExcludedRoundTrips() {
        validateRoundTrip(MsbListCountFixturesMsbOverflowListExcludedData(items = List(300) { it.toByte() }), MsbListCountFixturesMsbOverflowListExcludedData.serializer())
    }

    @Test
    fun msbSixteenBitListCountStructureExcludedRoundTrips() {
        validateRoundTrip(MsbListCountFixturesMsbSizedListExcludedData(items = listOf<Byte>(1, 2, 3, 4)), MsbListCountFixturesMsbSizedListExcludedData.serializer())
    }

    @Test
    fun longByteLengthPrefixedListRoundTrips() {
        // A one-byte count of 200 is above Byte.MAX_VALUE and must read back unsigned.
        validateRoundTrip(
            LongByteLengthPrefixedListRoundTripsFixturesLongByteLengthListData(
                items = List(200) { LongByteLengthPrefixedListRoundTripsFixturesLongByteLengthListItemData(it.toByte()) },
                trailer = 0x2A,
            ),
            LongByteLengthPrefixedListRoundTripsFixturesLongByteLengthListData.serializer(),
        )
    }

    @Test
    fun listOfNullableSerializableClassRoundTrips() {
        validateRoundTrip(
            ListOfNullableSerializableClassRoundTripsFixturesNullableItemListHolderData(
                items = listOf(ListOfNullableSerializableClassRoundTripsFixturesNullableItemPointData(0x01, 0x02), null),
            ),
            ListOfNullableSerializableClassRoundTripsFixturesNullableItemListHolderData.serializer(),
        )
    }
}

class BytesFormatCollectionsTest : CollectionsSuite(BytesFormatBackend.Factory) {
    // A root collection is no layout, so only BytesFormat encodes one; generated layouts reject the invalid ones at compile time.

    @Test
    fun encodeListRoot() {
        validateEncoding(
            listOf(
                EncodeListFixturesItemData(0xA0.toByte(), 0xA1.toByte(), 0xA2.toByte()),
                EncodeListFixturesItemData(0xB0.toByte(), 0xB1.toByte(), 0xB2.toByte()),
                EncodeListFixturesItemData(0xC0.toByte(), 0xC1.toByte(), 0xC2.toByte()),
            ),
            ListSerializer(EncodeListFixturesItemData.serializer()),
            buildByteArray {
                add(uByte = 3u)
                add(0xA0.toByte())
                add(0xA1.toByte())
                add(0xA2.toByte())
                add(0xB0.toByte())
                add(0xB1.toByte())
                add(0xB2.toByte())
                add(0xC0.toByte())
                add(0xC1.toByte())
                add(0xC2.toByte())
            },
        )
    }

    @Test
    fun encodeListRejectsInvalidLayouts() {
        assertFailsWith<DataAfterUnconstrainedData> {
            createBackend().encode(EncodeListFixtures.ContentAfterUnsized.serializer(), EncodeListFixtures.ContentAfterUnsized(listOf(0x01, 0x02, 0x03), 0x01))
        }
    }

    @Test
    fun encodeNumericListRoot() {
        validateEncoding(
            listOf(1, 2, 3),
            ListSerializer(Int.serializer()),
            buildByteArray {
                add(byte = 3)
                add(1)
                add(2)
                add(3)
            },
        )
    }

    @Test
    fun encodeStringListRoot() {
        validateEncoding(
            listOf("A", "B", "C"),
            ListSerializer(String.serializer()),
            buildByteArray {
                add(uByte = 3u)
                add("A")
                add("B")
                add("C")
            },
        )
    }

    @Test
    fun encodeMapRoot() {
        validateEncoding(
            mapOf(
                EncodeMapFixturesKeyData(0xA0.toUShort()) to EncodeMapFixturesValueData(0xA1.toByte()),
                EncodeMapFixturesKeyData(0xB0.toUShort()) to EncodeMapFixturesValueData(0xB1.toByte()),
                EncodeMapFixturesKeyData(0xC0.toUShort()) to EncodeMapFixturesValueData(0xC1.toByte()),
            ),
            MapSerializer(EncodeMapFixturesKeyData.serializer(), EncodeMapFixturesValueData.serializer()),
            buildByteArray {
                add(uByte = 3u)
                add(0xA0.toUShort())
                add(0xA1.toByte())
                add(0xB0.toUShort())
                add(0xB1.toByte())
                add(0xC0.toUShort())
                add(0xC1.toByte())
            },
        )
    }

    @Test
    fun encodeMapRejectsInvalidLayouts() {
        assertFailsWith<DataAfterUnconstrainedData> {
            createBackend().encode(
                EncodeMapFixtures.ContentAfterUnsized.serializer(),
                EncodeMapFixtures.ContentAfterUnsized(
                    mapOf(
                        0x01.toByte() to 0x02.toByte(),
                        0x03.toByte() to 0x04.toByte(),
                    ),
                    0x01,
                ),
            )
        }
    }

    @Test
    fun encodeNumericMapRoot() {
        validateEncoding(
            mapOf(1.toShort() to 2, 3.toShort() to 4),
            MapSerializer(Short.serializer(), Int.serializer()),
            buildByteArray {
                add(uByte = 2u)
                add(short = 1)
                add(2)
                add(short = 3)
                add(4)
            },
        )
    }

    @Test
    fun encodeStringMapRoot() {
        validateEncoding(
            mapOf('A' to "Alfa", 'B' to "Bravo", 'C' to "Charlie"),
            MapSerializer(Char.serializer(), String.serializer()),
            buildByteArray {
                add(uByte = 3u)
                add('A')
                add("Alfa")
                add('B')
                add("Bravo")
                add('C')
                add("Charlie")
            },
        )
    }
}

class ViewCollectionsTest : CollectionsSuite(ViewBackend.Factory)
