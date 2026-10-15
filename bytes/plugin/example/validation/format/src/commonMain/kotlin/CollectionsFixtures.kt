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
import com.splendo.kaluga.bytes.annotations.BytesLayout
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

object EncodeListFixtures {
    @BytesLayout
    interface Item {
        val valueA: Byte
        val valueB: Byte
        val valueC: Byte
    }

    @BytesLayout
    interface ListContainer {
        val default: List<Item>

        @Size(Length.`16_BIT`)
        val shortSized: List<Item>

        @Size(Length.`8_BIT`)
        @Size(Length.`16_BIT`)
        val flexibleSized: List<Item>

        @LengthPrefix(canOverflow = true)
        val lengthPrefix: List<Item>

        @NullIfEmpty val nullIfEmpty: List<Item>

        @NullTerminated val nullTerminated: List<Item>

        @Unsized val unsized: List<Item>
    }

    @BytesLayout
    interface NullTerminatedList {
        @NullTerminated val list: List<Byte>
    }

    // A @BytesLayout with this configuration is rejected at compile time; kept as a kotlinx class for the runtime check.
    @Serializable
    data class ContentAfterUnsized(@Unsized val list: List<Byte>, val otherContent: Byte)
}

object EncodeNumericListFixtures {
    @BytesLayout
    interface NumericListContainer {
        @ItemSize(Length.`8_BIT`)
        val intList: List<Int>

        @ItemSize(Length.`8_BIT`)
        @ItemSize(Length.`16_BIT`)
        val variableSizedIntList: List<Int>
        val nullableList: List<Int?>

        @NullIfEmpty val nullIfEmptyList: List<Int>

        @ItemSize(Length.`8_BIT`)
        @ItemScalar(decimalExponent = 4)
        val scalarList: List<Double>

        @ItemSize(Length.`16_BIT`)
        @ItemMedFloat
        val medFloatList: List<Double>

        @ItemByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
        val mostSignificant: List<Short>
        val unsignedList: List<UByte>
        val inlineList: List<NumberValueContainer<Int>>
    }
}

object EncodeStringListFixtures {
    @BytesLayout
    interface StringListContainer {
        @ItemEncoded(Encoding.UTF_8)
        val utf8List: List<String>

        @ItemEncoded(Encoding.UTF_16)
        val utf16List: List<String>

        @ItemEncoded(Encoding.ASCII)
        val asciiList: List<String>

        @ItemLengthPrefix(canOverflow = true)
        val lengthPrefixList: List<String>

        @ItemNullTerminated
        val nullTerminatedList: List<String>
        val nullableList: List<String?>
    }
}

object EncodeFlagIndexedLengthListFixtures {
    @BytesLayout
    interface Item {
        val a: Byte
        val b: Byte
    }

    // 16-bit count in flag header (@LengthPrefix(lengthAsShort=true) + @FlagIndex(0)):
    //   wire layout: [0-1]=count(UShort LE in flag header) [2]=headerByte [3..]=items x2B
    // The flag header holds the list count; the body begins with headerByte, then item data.
    @BytesLayout
    interface ContainerShort {
        val headerByte: Byte

        @LengthPrefix(lengthAsShort = true)
        @FlagIndex(0)
        val items: List<Item>
    }

    // 8-bit count in flag header (@LengthPrefix default + @FlagIndex(0)):
    //   wire layout: [0]=count(UByte in flag header) [1]=extra [2..]=items x2B
    @BytesLayout
    interface ContainerByte {
        val extra: Byte

        @LengthPrefix
        @FlagIndex(0)
        val items: List<Item>
    }

    // Multiple flag-consuming fields alongside the flag-indexed list:
    //   contactPresent is a nullable Bool at bit 16; items count occupies bits 0-15
    @BytesLayout
    interface ContainerWithSiblingFlag {
        @FlagIndex(16)
        val contactPresent: Boolean?

        @LengthPrefix(lengthAsShort = true)
        @FlagIndex(0)
        val items: List<Item>
    }
}

object EncodeMapFixtures {
    @BytesLayout
    interface Key {
        val keyValue: UShort
    }

    @BytesLayout
    interface Value {
        val value: Byte
    }

    @BytesLayout
    interface MapContainer {
        val default: Map<Key, Value>

        @Size(Length.`16_BIT`)
        val shortSized: Map<Key, Value>

        @Size(Length.`8_BIT`)
        @Size(Length.`16_BIT`)
        val flexibleSized: Map<Key, Value>

        @LengthPrefix(canOverflow = true)
        val lengthPrefix: Map<Key, Value>

        @NullIfEmpty val nullIfEmpty: Map<Key, Value>

        @NullTerminated val nullTerminated: Map<Key, Value>

        @Unsized val unsized: Map<Key, Value>
    }

    @BytesLayout
    interface NullTerminatedMap {
        @NullTerminated val map: Map<Byte, Byte>
    }

    // A @BytesLayout with this configuration is rejected at compile time; kept as a kotlinx class for the runtime check.
    @Serializable
    data class ContentAfterUnsized(@Unsized val map: Map<Byte, Byte>, val otherContent: Byte)
}

object EncodeNumericMapFixtures {
    @BytesLayout
    interface NumericMapContainer {
        @KeySize(Length.`8_BIT`)
        @ValueSize(Length.`24_BIT`)
        val fixedMap: Map<Short, Int>

        @KeySize(Length.`8_BIT`)
        @KeySize(Length.`16_BIT`)
        @ValueSize(Length.`24_BIT`)
        @ValueSize(Length.`32_BIT`)
        val variableSizedMap: Map<Short, Int>
        val nullableMap: Map<Short?, Int?>

        @KeySize(Length.`8_BIT`)
        @KeyScalar(decimalExponent = 2)
        @ValueSize(Length.`24_BIT`)
        @ValueScalar(decimalExponent = 3)
        val scalarMap: Map<Double, Double>

        @KeySize(Length.`16_BIT`)
        @KeyMedFloat
        @ValueSize(Length.`32_BIT`)
        @ValueMedFloat
        val medFloatList: Map<Double, Double>

        @KeyByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
        @ValueByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
        val mostSignificant: Map<Short, Int>
        val unsignedMap: Map<UShort, UInt>
        val inlineMap: Map<NumberValueContainer<Short>, ValueContainer<Int>>
    }
}

object EncodeStringMapFixtures {
    @BytesLayout
    interface StringMapContainer {
        @KeyEncoded(Encoding.UTF_8)
        @ValueEncoded(Encoding.UTF_8)
        val utf8Map: Map<Char, String>

        @KeyEncoded(Encoding.UTF_16)
        @ValueEncoded(Encoding.UTF_16)
        val utf16Map: Map<Char, String>

        @KeyEncoded(Encoding.ASCII)
        @ValueEncoded(Encoding.ASCII)
        val asciiMap: Map<Char, String>

        @KeyLengthPrefix(lengthAsShort = true)
        @ValueLengthPrefix(canOverflow = true)
        val lengthPrefixMap: Map<String, String>

        @KeyNullTerminated
        @ValueNullTerminated
        val nullTerminatedMap: Map<String, String>
        val nullableMap: Map<String?, String?>
    }
}

object EncodeValueOnlyNullTerminatedMapFixtures {
    // Only the value is null-terminated; the key uses the default ByteLength prefix.
    // This exercises valueAnnotations() mapping @ValueNullTerminated -> NullTerminated independently
    // of @KeyNullTerminated (which the combined test in encodeStringMap masked).
    @BytesLayout
    interface ValueNullTerminatedMapContainer {
        @ValueNullTerminated
        val map: Map<String, String>
    }

    // Conversely, @KeyNullTerminated must NOT leak onto the value: key is null-terminated, value keeps default.
    @BytesLayout
    interface KeyNullTerminatedMapContainer {
        @KeyNullTerminated
        val map: Map<String, String>
    }
}

object MsbExcludeStructureListAndMapPreserveItemOrderFixtures {
    @BytesLayout
    @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
    interface MsbCollectionFrame {
        val items: List<Byte>
        val pairs: Map<Byte, Byte>
    }
}

object LongByteLengthPrefixedListRoundTripsFixtures {
    @BytesLayout
    interface LongByteLengthListItem {
        val v: Byte
    }

    @BytesLayout
    interface LongByteLengthList {
        @LengthPrefix val items: List<LongByteLengthListItem>

        @Size(Length.`8_BIT`)
        val trailer: Byte
    }
}

object NullableListWithoutNullIfEmptyRoundTripsFixtures {
    @BytesLayout
    interface NullableListItem {
        val v: Byte
    }

    @BytesLayout
    interface NullableListNoNullIfEmpty {
        val items: List<NullableListItem>?

        @Size(Length.`8_BIT`)
        val trailer: Byte
    }
}

object ListOfNullableSerializableClassRoundTripsFixtures {
    @BytesLayout
    interface NullableItemPoint {
        val x: Byte
        val y: Byte
    }

    @BytesLayout
    interface NullableItemListHolder {
        val items: List<NullableItemPoint?>
    }
}

object ItemLevelPrefixPostfixChecksumOnListItemRoundTripsFixtures {
    @BytesLayout
    @Prefix([0x50])
    @Postfix([0x60])
    @Checksum(8, 0x07u, 0x00u)
    interface FramedChecksumItem {
        val v: Byte
    }

    @BytesLayout
    interface FramedChecksumItemListHolder {
        val items: List<FramedChecksumItem>

        @Size(Length.`8_BIT`)
        val trailer: Byte
    }
}

object MsbListCountFixtures {
    @BytesLayout
    @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
    interface MsbOverflowListIncluded {
        @LengthPrefix(canOverflow = true)
        val items: List<Byte>
    }

    @BytesLayout
    @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
    interface MsbOverflowListExcluded {
        @LengthPrefix(canOverflow = true)
        val items: List<Byte>
    }

    @BytesLayout
    @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
    interface MsbSizedListIncluded {
        @Size(Length.`16_BIT`)
        val items: List<Byte>
    }

    @BytesLayout
    @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
    interface MsbSizedListExcluded {
        @Size(Length.`16_BIT`)
        val items: List<Byte>
    }
}
