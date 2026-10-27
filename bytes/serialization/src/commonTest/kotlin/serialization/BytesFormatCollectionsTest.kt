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

package com.splendo.kaluga.bytes.serialization

import com.splendo.kaluga.bytes.strings.add
import com.splendo.kaluga.bytes.medfloat.add
import com.splendo.kaluga.bytes.integer.add
import com.splendo.kaluga.bytes.ByteOrder
import com.splendo.kaluga.bytes.strings.Encoding
import com.splendo.kaluga.bytes.medfloat.MedFloat16
import com.splendo.kaluga.bytes.medfloat.MedFloat32
import com.splendo.kaluga.bytes.strings.StringEncodingSettings
import com.splendo.kaluga.bytes.buildByteArray
import com.splendo.kaluga.bytes.integer.toInt24
import com.splendo.kaluga.bytes.annotations.Checksum
import com.splendo.kaluga.bytes.annotations.FlagIndex
import com.splendo.kaluga.bytes.annotations.ItemByteOrder
import com.splendo.kaluga.bytes.annotations.ItemEncoded
import com.splendo.kaluga.bytes.annotations.ItemLengthPrefix
import com.splendo.kaluga.bytes.annotations.ItemMedFloat
import com.splendo.kaluga.bytes.annotations.ItemNullTerminated
import com.splendo.kaluga.bytes.annotations.ItemScalar
import com.splendo.kaluga.bytes.annotations.ItemSize
import com.splendo.kaluga.bytes.annotations.KeyByteOrder
import com.splendo.kaluga.bytes.annotations.KeyEncoded
import com.splendo.kaluga.bytes.annotations.KeyLengthPrefix
import com.splendo.kaluga.bytes.annotations.KeyMedFloat
import com.splendo.kaluga.bytes.annotations.KeyNullTerminated
import com.splendo.kaluga.bytes.annotations.KeyScalar
import com.splendo.kaluga.bytes.annotations.KeySize
import com.splendo.kaluga.bytes.annotations.KeyUnsigned
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.annotations.LengthPrefix
import com.splendo.kaluga.bytes.annotations.NullIfEmpty
import com.splendo.kaluga.bytes.annotations.NullTerminated
import com.splendo.kaluga.bytes.annotations.Postfix
import com.splendo.kaluga.bytes.annotations.Prefix
import com.splendo.kaluga.bytes.annotations.Size
import com.splendo.kaluga.bytes.annotations.Unsized
import com.splendo.kaluga.bytes.annotations.ValueByteOrder
import com.splendo.kaluga.bytes.annotations.ValueEncoded
import com.splendo.kaluga.bytes.annotations.ValueLengthPrefix
import com.splendo.kaluga.bytes.annotations.ValueMedFloat
import com.splendo.kaluga.bytes.annotations.ValueNullTerminated
import com.splendo.kaluga.bytes.annotations.ValueScalar
import com.splendo.kaluga.bytes.annotations.ValueSize
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlin.test.Test
import kotlin.test.assertFailsWith

class BytesFormatCollectionsTest : BytesFormatTestBase(BytesFormatBackend.Factory) {

    private object EncodeListFixtures {
        @Serializable
        data class Item(val valueA: Byte, val valueB: Byte, val valueC: Byte)

        @Serializable
        data class ListContainer(
            val default: List<Item>,
            @Size(Length.`16_BIT`) val shortSized: List<Item>,
            @Size(Length.`8_BIT`) @Size(Length.`16_BIT`) val flexibleSized: List<Item>,
            @LengthPrefix(canOverflow = true) val lengthPrefix: List<Item>,
            @NullIfEmpty val nullIfEmpty: List<Item>,
            @NullTerminated val nullTerminated: List<Item>,
            @Unsized val unsized: List<Item>,
        )

        @Serializable
        data class NullTerminatedList(@NullTerminated val list: List<Byte>)

        @Serializable
        data class ContentAfterUnsized(@Unsized val list: List<Byte>, val otherContent: Byte)
    }

    @Test
    fun encodeList() {
        validateEncoding(
            listOf(
                EncodeListFixtures.Item(0xA0.toByte(), 0xA1.toByte(), 0xA2.toByte()),
                EncodeListFixtures.Item(0xB0.toByte(), 0xB1.toByte(), 0xB2.toByte()),
                EncodeListFixtures.Item(0xC0.toByte(), 0xC1.toByte(), 0xC2.toByte()),
            ),
            ListSerializer(EncodeListFixtures.Item.serializer()),
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

        validateEncoding(
            EncodeListFixtures.ListContainer(
                MutableList(10) { EncodeListFixtures.Item(it.toByte(), (it + 10).toByte(), (it + 20).toByte()) },
                MutableList(500) { EncodeListFixtures.Item(it.toByte(), (it + 30).toByte(), (it + 40).toByte()) },
                MutableList(40) { EncodeListFixtures.Item(it.toByte(), (it - 30).toByte(), (it - 40).toByte()) },
                MutableList(33) { EncodeListFixtures.Item(it.toByte(), (it - 10).toByte(), (it - 20).toByte()) },
                MutableList(5) { EncodeListFixtures.Item(it.toByte(), (it - 50).toByte(), (it + 50).toByte()) },
                listOf(EncodeListFixtures.Item(0x01, 0x00, 0x03), EncodeListFixtures.Item(0x04, 0x05, 0x00)),
                listOf(EncodeListFixtures.Item(0x06, 0x07, 0x08)),
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
            EncodeListFixtures.ListContainer(
                MutableList(8) { EncodeListFixtures.Item(it.toByte(), (it + 10).toByte(), (it + 20).toByte()) },
                MutableList(2) { EncodeListFixtures.Item(it.toByte(), (it + 30).toByte(), (it + 40).toByte()) },
                MutableList(1000) { EncodeListFixtures.Item(it.toByte(), (it - 30).toByte(), (it - 40).toByte()) },
                MutableList(800) { EncodeListFixtures.Item(it.toByte(), (it - 10).toByte(), (it - 20).toByte()) },
                emptyList(),
                listOf(EncodeListFixtures.Item(0x01, 0x02, 0x03)),
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
            BytesFormat.encodeToByteArray(EncodeListFixtures.NullTerminatedList.serializer(), EncodeListFixtures.NullTerminatedList(listOf(0x00.toByte(), 0x01.toByte())))
        }

        assertFailsWith<DataAfterUnconstrainedData> {
            BytesFormat.encodeToByteArray(EncodeListFixtures.ContentAfterUnsized.serializer(), EncodeListFixtures.ContentAfterUnsized(listOf(0x01, 0x02, 0x03), 0x01))
        }
    }

    private object EncodeNumericListFixtures {
        @Serializable
        data class NumericListContainer(
            @ItemSize(Length.`8_BIT`)
            val intList: List<Int>,
            @ItemSize(Length.`8_BIT`)
            @ItemSize(Length.`16_BIT`)
            val variableSizedIntList: List<Int>,
            val nullableList: List<Int?>,
            @NullIfEmpty val nullIfEmptyList: List<Int>,
            @ItemSize(Length.`8_BIT`) @ItemScalar(decimalExponent = 4) val scalarList: List<Double>,
            @ItemSize(Length.`16_BIT`) @ItemMedFloat val medFloatList: List<Double>,
            @ItemByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST) val mostSignificant: List<Short>,
            val unsignedList: List<UByte>,
            val inlineList: List<NumberValueContainer<Int>>,
        )
    }

    @Test
    fun encodeNumericList() {
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
        validateEncoding(
            EncodeNumericListFixtures.NumericListContainer(
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

    private object EncodeStringListFixtures {
        @Serializable
        data class StringListContainer(
            @ItemEncoded(Encoding.UTF_8)
            val utf8List: List<String>,
            @ItemEncoded(Encoding.UTF_16)
            val utf16List: List<String>,
            @ItemEncoded(Encoding.ASCII)
            val asciiList: List<String>,
            @ItemLengthPrefix(canOverflow = true)
            val lengthPrefixList: List<String>,
            @ItemNullTerminated
            val nullTerminatedList: List<String>,
            val nullableList: List<String?>,
        )
    }

    @Test
    fun encodeStringList() {
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
        validateEncoding(
            EncodeStringListFixtures.StringListContainer(
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

    private object EncodeFlagIndexedLengthListFixtures {
        @Serializable
        data class Item(val a: Byte, val b: Byte)

        // 16-bit count in flag header (@LengthPrefix(lengthAsShort=true) + @FlagIndex(0)):
        //   wire layout: [0-1]=count(UShort LE in flag header) [2]=headerByte [3..]=items x2B
        // The flag header holds the list count; the body begins with headerByte, then item data.
        @Serializable
        data class ContainerShort(val headerByte: Byte, @LengthPrefix(lengthAsShort = true) @FlagIndex(0) val items: List<Item>)

        // 8-bit count in flag header (@LengthPrefix default + @FlagIndex(0)):
        //   wire layout: [0]=count(UByte in flag header) [1]=extra [2..]=items x2B
        @Serializable
        data class ContainerByte(val extra: Byte, @LengthPrefix @FlagIndex(0) val items: List<Item>)

        // Multiple flag-consuming fields alongside the flag-indexed list:
        //   contactPresent is a nullable Bool at bit 16; items count occupies bits 0-15
        @Serializable
        data class ContainerWithSiblingFlag(@FlagIndex(16) val contactPresent: Boolean?, @LengthPrefix(lengthAsShort = true) @FlagIndex(0) val items: List<Item>)
    }

    @Test
    fun encodeFlagIndexedLengthList() {
        validateEncoding(
            EncodeFlagIndexedLengthListFixtures.ContainerShort(
                0x42,
                listOf(
                    EncodeFlagIndexedLengthListFixtures.Item(0x0A, 0x0B),
                    EncodeFlagIndexedLengthListFixtures.Item(0x0C, 0x0D),
                    EncodeFlagIndexedLengthListFixtures.Item(0x0E, 0x0F),
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
            EncodeFlagIndexedLengthListFixtures.ContainerShort(0x99.toByte(), emptyList()),
            buildByteArray {
                add(uShort = 0u) // count = 0
                add(0x99.toByte()) // headerByte
            },
        )

        // Large count (>255) to exercise the full 16-bit range
        val largeList = MutableList(300) { EncodeFlagIndexedLengthListFixtures.Item(it.toByte(), (it + 1).toByte()) }
        val largeEncoded = buildByteArray {
            add(uShort = 300u) // 0x2C 0x01
            add(0x01.toByte()) // headerByte
            largeList.forEach {
                add(it.a)
                add(it.b)
            }
        }
        validateEncoding(EncodeFlagIndexedLengthListFixtures.ContainerShort(0x01, largeList), largeEncoded)

        validateEncoding(
            EncodeFlagIndexedLengthListFixtures.ContainerByte(
                0x55,
                listOf(EncodeFlagIndexedLengthListFixtures.Item(0x01, 0x02), EncodeFlagIndexedLengthListFixtures.Item(0x03, 0x04)),
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
            EncodeFlagIndexedLengthListFixtures.ContainerWithSiblingFlag(true, listOf(EncodeFlagIndexedLengthListFixtures.Item(0xAB.toByte(), 0xCD.toByte()))),
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

    private object EncodeMapFixtures {
        @Serializable
        data class Key(val keyValue: UShort)

        @Serializable
        data class Value(val value: Byte)

        @Serializable
        data class MapContainer(
            val default: Map<Key, Value>,
            @Size(Length.`16_BIT`) val shortSized: Map<Key, Value>,
            @Size(Length.`8_BIT`) @Size(Length.`16_BIT`) val flexibleSized: Map<Key, Value>,
            @LengthPrefix(canOverflow = true) val lengthPrefix: Map<Key, Value>,
            @NullIfEmpty val nullIfEmpty: Map<Key, Value>,
            @NullTerminated val nullTerminated: Map<Key, Value>,
            @Unsized val unsized: Map<Key, Value>,
        )

        @Serializable
        data class NullTerminatedMap(@NullTerminated val map: Map<Byte, Byte>)

        @Serializable
        data class ContentAfterUnsized(@Unsized val map: Map<Byte, Byte>, val otherContent: Byte)
    }

    @Test
    fun encodeMap() {
        validateEncoding(
            mapOf(
                EncodeMapFixtures.Key(0xA0.toUShort()) to EncodeMapFixtures.Value(0xA1.toByte()),
                EncodeMapFixtures.Key(0xB0.toUShort()) to EncodeMapFixtures.Value(0xB1.toByte()),
                EncodeMapFixtures.Key(0xC0.toUShort()) to EncodeMapFixtures.Value(0xC1.toByte()),
            ),
            MapSerializer(EncodeMapFixtures.Key.serializer(), EncodeMapFixtures.Value.serializer()),
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

        validateEncoding(
            EncodeMapFixtures.MapContainer(
                MutableList(10) { EncodeMapFixtures.Key(it.toUShort()) to EncodeMapFixtures.Value((it + 10).toByte()) }.toMap(),
                MutableList(500) { EncodeMapFixtures.Key(it.toUShort()) to EncodeMapFixtures.Value((it + 30).toByte()) }.toMap(),
                MutableList(40) { EncodeMapFixtures.Key(it.toUShort()) to EncodeMapFixtures.Value((it - 30).toByte()) }.toMap(),
                MutableList(33) { EncodeMapFixtures.Key(it.toUShort()) to EncodeMapFixtures.Value((it - 10).toByte()) }.toMap(),
                MutableList(5) { EncodeMapFixtures.Key(it.toUShort()) to EncodeMapFixtures.Value((it - 50).toByte()) }.toMap(),
                mapOf(EncodeMapFixtures.Key(0x01u) to EncodeMapFixtures.Value(0x00), EncodeMapFixtures.Key(0x02u) to EncodeMapFixtures.Value(0x03)),
                mapOf(EncodeMapFixtures.Key(0x06u) to EncodeMapFixtures.Value(0x07), EncodeMapFixtures.Key(0x08u) to EncodeMapFixtures.Value(0x09)),
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
            EncodeMapFixtures.MapContainer(
                MutableList(8) { EncodeMapFixtures.Key(it.toUShort()) to EncodeMapFixtures.Value((it + 10).toByte()) }.toMap(),
                MutableList(2) { EncodeMapFixtures.Key(it.toUShort()) to EncodeMapFixtures.Value((it + 30).toByte()) }.toMap(),
                MutableList(1000) { EncodeMapFixtures.Key(it.toUShort()) to EncodeMapFixtures.Value((it - 30).toByte()) }.toMap(),
                MutableList(800) { EncodeMapFixtures.Key(it.toUShort()) to EncodeMapFixtures.Value((it - 10).toByte()) }.toMap(),
                emptyMap(),
                mapOf(EncodeMapFixtures.Key(0x01u) to EncodeMapFixtures.Value(0x02), EncodeMapFixtures.Key(0x03u) to EncodeMapFixtures.Value(0x04)),
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
            BytesFormat.encodeToByteArray(EncodeMapFixtures.NullTerminatedMap.serializer(), EncodeMapFixtures.NullTerminatedMap(mapOf(0x00.toByte() to 0x01.toByte())))
        }

        assertFailsWith<DataAfterUnconstrainedData> {
            BytesFormat.encodeToByteArray(
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

    private object EncodeNumericMapFixtures {
        @Serializable
        data class NumericMapContainer(
            @KeySize(Length.`8_BIT`)
            @ValueSize(Length.`24_BIT`)
            val fixedMap: Map<Short, Int>,
            @KeySize(Length.`8_BIT`)
            @KeySize(Length.`16_BIT`)
            @ValueSize(Length.`24_BIT`)
            @ValueSize(Length.`32_BIT`)
            val variableSizedMap: Map<Short, Int>,
            val nullableMap: Map<Short?, Int?>,
            @KeySize(Length.`8_BIT`) @KeyScalar(decimalExponent = 2)
            @ValueSize(Length.`24_BIT`)
            @ValueScalar(decimalExponent = 3)
            val scalarMap: Map<Double, Double>,
            @KeySize(Length.`16_BIT`) @KeyMedFloat
            @ValueSize(Length.`32_BIT`)
            @ValueMedFloat
            val medFloatList: Map<Double, Double>,
            @KeyByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
            @ValueByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
            val mostSignificant: Map<Short, Int>,
            val unsignedMap: Map<UShort, UInt>,
            val inlineMap: Map<NumberValueContainer<Short>, ValueContainer<Int>>,
        )
    }

    @Test
    fun encodeNumericMap() {
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
        validateEncoding(
            EncodeNumericMapFixtures.NumericMapContainer(
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

    private object EncodeStringMapFixtures {
        @Serializable
        data class StringMapContainer(
            @ValueEncoded(Encoding.UTF_8)
            val utf8Map: Map<Char, String>,
            @KeyEncoded(Encoding.UTF_16)
            @ValueEncoded(Encoding.UTF_16)
            val utf16Map: Map<Char, String>,
            @KeyEncoded(Encoding.ASCII)
            @ValueEncoded(Encoding.ASCII)
            val asciiMap: Map<Char, String>,
            @KeyLengthPrefix(lengthAsShort = true)
            @ValueLengthPrefix(canOverflow = true)
            val lengthPrefixMap: Map<String, String>,
            @KeyNullTerminated
            @ValueNullTerminated
            val nullTerminatedMap: Map<String, String>,
            val nullableMap: Map<String?, String?>,
        )
    }

    @Test
    fun encodeStringMap() {
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
        validateEncoding(
            EncodeStringMapFixtures.StringMapContainer(
                mapOf('A' to "Alfa", 'B' to "Bravo", 'C' to "Charlie"),
                mapOf('D' to "Delta", 'E' to "Echo", 'F' to "Foxtrot"),
                mapOf('G' to "Golf", 'H' to "Hotel", 'I' to "India"),
                mapOf("Key" to "Value", "Long" to MutableList(1000) { "A" }.joinToString()),
                mapOf("" to "Empty", "Empty" to ""),
                mapOf("Key" to "Value", null to "Empty", "NoKey" to null),
            ),
            buildByteArray {
                add(uByte = 3u)
                add('A', Encoding.ASCII)
                add("Alfa", StringEncodingSettings(encoding = Encoding.UTF_8))
                add('B', Encoding.ASCII)
                add("Bravo", StringEncodingSettings(encoding = Encoding.UTF_8))
                add('C', Encoding.ASCII)
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

    private object EncodeValueOnlyNullTerminatedMapFixtures {
        // Only the value is null-terminated; the key uses the default ByteLength prefix.
        // This exercises valueAnnotations() mapping @ValueNullTerminated -> NullTerminated independently
        // of @KeyNullTerminated (which the combined test in encodeStringMap masked).
        @Serializable
        data class ValueNullTerminatedMapContainer(
            @ValueNullTerminated
            val map: Map<String, String>,
        )

        // Conversely, @KeyNullTerminated must NOT leak onto the value: key is null-terminated, value keeps default.
        @Serializable
        data class KeyNullTerminatedMapContainer(
            @KeyNullTerminated
            val map: Map<String, String>,
        )
    }

    @Test
    fun encodeValueOnlyNullTerminatedMap() {
        validateEncoding(
            EncodeValueOnlyNullTerminatedMapFixtures.ValueNullTerminatedMapContainer(
                mapOf("Key" to "Value", "Other" to "Thing"),
            ),
            EncodeValueOnlyNullTerminatedMapFixtures.ValueNullTerminatedMapContainer.serializer(),
            buildByteArray {
                add(uByte = 2u)
                add("Key") // default ByteLength prefix
                add("Value", StringEncodingSettings(endMarking = StringEncodingSettings.NullTerminated))
                add("Other") // default ByteLength prefix
                add("Thing", StringEncodingSettings(endMarking = StringEncodingSettings.NullTerminated))
            },
        )

        validateEncoding(
            EncodeValueOnlyNullTerminatedMapFixtures.KeyNullTerminatedMapContainer(
                mapOf("Key" to "Value", "Other" to "Thing"),
            ),
            EncodeValueOnlyNullTerminatedMapFixtures.KeyNullTerminatedMapContainer.serializer(),
            buildByteArray {
                add(uByte = 2u)
                add("Key", StringEncodingSettings(endMarking = StringEncodingSettings.NullTerminated))
                add("Value") // default ByteLength prefix
                add("Other", StringEncodingSettings(endMarking = StringEncodingSettings.NullTerminated))
                add("Thing") // default ByteLength prefix
            },
        )
    }

    private object MsbExcludeStructureListAndMapPreserveItemOrderFixtures {
        @Serializable
        @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
        data class MsbCollectionFrame(val items: List<Byte>, val pairs: Map<Byte, Byte>)
    }

    @Test
    fun msbExcludeStructureListAndMapPreserveItemOrder() {
        // Items accumulate in the frame's sequential direction; only their values follow the big-endian value order.
        validateEncoding(
            MsbExcludeStructureListAndMapPreserveItemOrderFixtures.MsbCollectionFrame(
                items = listOf(1, 2, 3),
                pairs = linkedMapOf(1.toByte() to 10.toByte(), 2.toByte() to 20.toByte(), 3.toByte() to 30.toByte()),
            ),
            MsbExcludeStructureListAndMapPreserveItemOrderFixtures.MsbCollectionFrame.serializer(),
            byteArrayOf(0x03, 0x01, 0x02, 0x03, 0x03, 0x01, 0x0A, 0x02, 0x14, 0x03, 0x1E),
        )
    }

    private object LongByteLengthPrefixedListRoundTripsFixtures {
        @Serializable
        data class LongByteLengthListItem(val v: Byte)

        @Serializable
        data class LongByteLengthList(@LengthPrefix val items: List<LongByteLengthListItem>, @Size(Length.`8_BIT`) val trailer: Byte)
    }

    @Test
    fun longByteLengthPrefixedListRoundTrips() {
        // A one-byte count of 200 is above Byte.MAX_VALUE and must read back unsigned.
        validateRoundTrip(
            LongByteLengthPrefixedListRoundTripsFixtures.LongByteLengthList(
                items = List(200) { LongByteLengthPrefixedListRoundTripsFixtures.LongByteLengthListItem(it.toByte()) },
                trailer = 0x2A,
            ),
            LongByteLengthPrefixedListRoundTripsFixtures.LongByteLengthList.serializer(),
        )
    }

    private object NullableListWithoutNullIfEmptyRoundTripsFixtures {
        @Serializable
        data class NullableListItem(val v: Byte)

        @Serializable
        data class NullableListNoNullIfEmpty(val items: List<NullableListItem>?, @Size(Length.`8_BIT`) val trailer: Byte)
    }

    @Test
    fun nullableListWithoutNullIfEmptyRoundTrips() {
        // Without @NullIfEmpty an empty list is present (its zero count is written), distinct from an absent one.
        validateRoundTrip(
            NullableListWithoutNullIfEmptyRoundTripsFixtures.NullableListNoNullIfEmpty(items = null, trailer = 0x2A),
            NullableListWithoutNullIfEmptyRoundTripsFixtures.NullableListNoNullIfEmpty.serializer(),
        )
        validateRoundTrip(
            NullableListWithoutNullIfEmptyRoundTripsFixtures.NullableListNoNullIfEmpty(items = emptyList(), trailer = 0x2A),
            NullableListWithoutNullIfEmptyRoundTripsFixtures.NullableListNoNullIfEmpty.serializer(),
        )
    }

    private object ListOfNullableSerializableClassRoundTripsFixtures {
        @Serializable
        data class NullableItemPoint(val x: Byte, val y: Byte)

        @Serializable
        data class NullableItemListHolder(val items: List<NullableItemPoint?>)
    }

    @Test
    fun listOfNullableSerializableClassRoundTrips() {
        validateRoundTrip(
            ListOfNullableSerializableClassRoundTripsFixtures.NullableItemListHolder(
                items = listOf(ListOfNullableSerializableClassRoundTripsFixtures.NullableItemPoint(0x01, 0x02), null),
            ),
            ListOfNullableSerializableClassRoundTripsFixtures.NullableItemListHolder.serializer(),
        )
    }

    private object ItemLevelPrefixPostfixChecksumOnListItemRoundTripsFixtures {
        @Serializable
        @Prefix([0x50])
        @Postfix([0x60])
        @Checksum(8, 0x07u, 0x00u)
        data class FramedChecksumItem(val v: Byte)

        @Serializable
        data class FramedChecksumItemListHolder(val items: List<FramedChecksumItem>, @Size(Length.`8_BIT`) val trailer: Byte)
    }

    @Test
    fun itemLevelPrefixPostfixChecksumOnListItemRoundTrips() {
        // Each item's frame is written once, by the item's own encoder.
        validateRoundTrip(
            ItemLevelPrefixPostfixChecksumOnListItemRoundTripsFixtures.FramedChecksumItemListHolder(
                items = listOf(
                    ItemLevelPrefixPostfixChecksumOnListItemRoundTripsFixtures.FramedChecksumItem(0x11),
                    ItemLevelPrefixPostfixChecksumOnListItemRoundTripsFixtures.FramedChecksumItem(0x22),
                ),
                trailer = 0x2A,
            ),
            ItemLevelPrefixPostfixChecksumOnListItemRoundTripsFixtures.FramedChecksumItemListHolder.serializer(),
        )
    }

    private object MsbListCountFixtures {
        @Serializable
        @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
        data class MsbOverflowListIncluded(@LengthPrefix(canOverflow = true) val items: List<Byte>)

        @Serializable
        @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
        data class MsbOverflowListExcluded(@LengthPrefix(canOverflow = true) val items: List<Byte>)

        @Serializable
        @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
        data class MsbSizedListIncluded(@Size(Length.`16_BIT`) val items: List<Byte>)

        @Serializable
        @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
        data class MsbSizedListExcluded(@Size(Length.`16_BIT`) val items: List<Byte>)
    }

    @Test
    fun msbOverflowLengthPrefixedListStructureIncludedRoundTrips() {
        validateRoundTrip(MsbListCountFixtures.MsbOverflowListIncluded(items = List(300) { it.toByte() }), MsbListCountFixtures.MsbOverflowListIncluded.serializer())
    }

    @Test
    fun msbOverflowLengthPrefixedListStructureExcludedRoundTrips() {
        validateRoundTrip(MsbListCountFixtures.MsbOverflowListExcluded(items = List(300) { it.toByte() }), MsbListCountFixtures.MsbOverflowListExcluded.serializer())
    }

    @Test
    fun msbSixteenBitListCountStructureIncludedRoundTrips() {
        validateRoundTrip(MsbListCountFixtures.MsbSizedListIncluded(items = listOf<Byte>(1, 2, 3, 4)), MsbListCountFixtures.MsbSizedListIncluded.serializer())
    }

    @Test
    fun msbSixteenBitListCountStructureExcludedRoundTrips() {
        validateRoundTrip(MsbListCountFixtures.MsbSizedListExcluded(items = listOf<Byte>(1, 2, 3, 4)), MsbListCountFixtures.MsbSizedListExcluded.serializer())
    }
}
