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
import com.splendo.kaluga.bytes.strings.Encoding
import com.splendo.kaluga.bytes.strings.StringEncodingSettings
import com.splendo.kaluga.bytes.buildByteArray
import com.splendo.kaluga.bytes.annotations.Encoded
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.annotations.LengthPrefix
import com.splendo.kaluga.bytes.annotations.NullTerminated
import com.splendo.kaluga.bytes.annotations.Size
import com.splendo.kaluga.bytes.annotations.Unsized
import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertFailsWith
import com.splendo.kaluga.bytes.layout.InvalidByteOrderException

class BytesFormatStringsTest : BytesFormatTestBase(BytesFormatBackend.Factory) {

    private object EncodeStringFixtures {
        @Serializable
        data class Container(
            val default: String,
            @Encoded(Encoding.UTF_8) val utf8: String,
            @Encoded(Encoding.UTF_16) val utf16: String,
            @Encoded(Encoding.ASCII) val ascii: String,
            val nullable: String?,
            @Encoded(Encoding.UTF_16) val nullableUTF16: String?,
            @NullTerminated val nullTerminated: String,
            @LengthPrefix(lengthAsShort = false, canOverflow = true) val lengthPrefix: String,
            @Unsized val unsized: String,
        )

        @Serializable
        data class InvalidByteOrderChange(@com.splendo.kaluga.bytes.annotations.ByteOrder(com.splendo.kaluga.bytes.ByteOrder.MOST_SIGNIFICANT_FIRST) val content: String)

        @Serializable
        data class ContentContainsNull(@NullTerminated val content: String)

        @Serializable
        data class ContentAfterUnsized(@Unsized val string: String, val otherContent: Byte)

        // A plain Boolean (no @FlagIndex/@FlagWidth) has no flag slot, so it is encoded into the body via
        // addBit rather than addAction. Placing one after an @Unsized String must still be rejected: there is
        // no way to know where the unsized data ends, so any trailing body data is undecodable.
        @Serializable
        data class BooleanAfterUnsized(@Unsized val string: String, val flag: Boolean)
    }

    @Test
    fun encodeString() {
        validateEncoding(
            EncodeStringFixtures.Container(
                "Sentence A",
                "Sentence B",
                "Sentence C",
                "Sentence D",
                "Sentence E",
                "Sentence F",
                "Sentence G",
                "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec.",
                "Sentence I",
            ),
            buildByteArray {
                add(true)
                add(true)
                add("Sentence A")
                add("Sentence B")
                add("Sentence C", StringEncodingSettings(encoding = Encoding.UTF_16))
                add("Sentence D", StringEncodingSettings(encoding = Encoding.ASCII))
                add("Sentence E")
                add("Sentence F", StringEncodingSettings(encoding = Encoding.UTF_16))
                add("Sentence G", StringEncodingSettings(endMarking = StringEncodingSettings.NullTerminated))
                add(
                    "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec.",
                    StringEncodingSettings(endMarking = StringEncodingSettings.LengthPrefix.WithOverflow()),
                )
                add("Sentence I", StringEncodingSettings(endMarking = StringEncodingSettings.NoMarking))
            },
        )

        assertFailsWith<InvalidByteOrderException> {
            BytesFormat.encodeToByteArray(EncodeStringFixtures.InvalidByteOrderChange.serializer(), EncodeStringFixtures.InvalidByteOrderChange("Invalid"))
        }

        assertFailsWith<IllegalArgumentException> {
            BytesFormat.encodeToByteArray(EncodeStringFixtures.ContentContainsNull.serializer(), EncodeStringFixtures.ContentContainsNull("Content\u0000NullTerminus"))
        }

        assertFailsWith<DataAfterUnconstrainedData> {
            BytesFormat.encodeToByteArray(EncodeStringFixtures.ContentAfterUnsized.serializer(), EncodeStringFixtures.ContentAfterUnsized("StringContent", 0x01))
        }

        assertFailsWith<DataAfterUnconstrainedData> {
            BytesFormat.encodeToByteArray(EncodeStringFixtures.BooleanAfterUnsized.serializer(), EncodeStringFixtures.BooleanAfterUnsized("StringContent", true))
        }
    }

    private object MsbExcludeStructureLengthPrefixAndNullTerminatedStringRoundTripsFixtures {
        @Serializable
        @com.splendo.kaluga.bytes.annotations.ByteOrder(com.splendo.kaluga.bytes.ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
        data class MsbStringFrame(@LengthPrefix val prefixed: String, @NullTerminated val terminated: String)
    }

    @Test
    fun msbExcludeStructureLengthPrefixAndNullTerminatedStringRoundTrips() {
        // The frame accumulates sequentially, so each string's length prefix and terminator sit where an LSB frame puts them.
        validateEncoding(
            MsbExcludeStructureLengthPrefixAndNullTerminatedStringRoundTripsFixtures.MsbStringFrame(prefixed = "AB", terminated = "CD"),
            MsbExcludeStructureLengthPrefixAndNullTerminatedStringRoundTripsFixtures.MsbStringFrame.serializer(),
            byteArrayOf(0x02, 0x41, 0x42, 0x43, 0x44, 0x00),
        )
    }

    private object LongByteLengthPrefixedStringRoundTripsFixtures {
        @Serializable
        data class LongByteLengthString(@LengthPrefix val s: String, @Size(Length.`8_BIT`) val trailer: Byte)
    }

    @Test
    fun longByteLengthPrefixedStringRoundTrips() {
        // A one-byte length of 200 is above Byte.MAX_VALUE and must read back unsigned.
        validateRoundTrip(
            LongByteLengthPrefixedStringRoundTripsFixtures.LongByteLengthString(s = "x".repeat(200), trailer = 0x2A),
            LongByteLengthPrefixedStringRoundTripsFixtures.LongByteLengthString.serializer(),
        )
    }
}
