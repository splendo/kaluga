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

import com.splendo.kaluga.bytes.strings.Encoding
import com.splendo.kaluga.bytes.strings.StringEncodingSettings
import com.splendo.kaluga.bytes.annotations.LengthPrefix
import com.splendo.kaluga.bytes.annotations.NullTerminated
import com.splendo.kaluga.bytes.buildByteArray
import com.splendo.kaluga.bytes.layout.InvalidByteOrderException
import com.splendo.kaluga.bytes.serialization.DataAfterUnconstrainedData
import com.splendo.kaluga.bytes.strings.add
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlinx.serialization.serializer

/**
 * The string tests every backend runs; [BytesFormatStringsTest] adds the ones only `BytesFormat` can run.
 */
abstract class StringsSuite(backendFactory: ByteLayoutBackendFactory) : BytesFormatTestBase(backendFactory) {

    @Test
    fun encodeString() {
        validateEncoding(
            EncodeStringFixturesContainerData(
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

        assertFailsWith<IllegalArgumentException> {
            createBackend().encode(EncodeStringFixturesContentContainsNullData.serializer(), EncodeStringFixturesContentContainsNullData("Content\u0000NullTerminus"))
        }
    }

    @Test
    fun msbExcludeStructureLengthPrefixAndNullTerminatedStringRoundTrips() {
        // The frame accumulates sequentially, so each string's length prefix and terminator sit where an LSB frame puts them.
        validateEncoding(
            MsbExcludeStructureLengthPrefixAndNullTerminatedStringRoundTripsFixturesMsbStringFrameData(prefixed = "AB", terminated = "CD"),
            MsbExcludeStructureLengthPrefixAndNullTerminatedStringRoundTripsFixturesMsbStringFrameData.serializer(),
            byteArrayOf(0x02, 0x41, 0x42, 0x43, 0x44, 0x00),
        )
    }

    @Test
    fun longByteLengthPrefixedStringRoundTrips() {
        // A one-byte length of 200 is above Byte.MAX_VALUE and must read back unsigned.
        validateRoundTrip(
            LongByteLengthPrefixedStringRoundTripsFixturesLongByteLengthStringData(s = "x".repeat(200), trailer = 0x2A),
            LongByteLengthPrefixedStringRoundTripsFixturesLongByteLengthStringData.serializer(),
        )
    }
}

class BytesFormatStringsTest : StringsSuite(BytesFormatBackend.Factory) {

    @Test
    fun encodeStringRejectsInvalidLayouts() {
        // Generated layouts reject these at compile time (see LayoutErrorTest in bytes-ksp).
        assertFailsWith<InvalidByteOrderException> {
            createBackend().encode(EncodeStringFixtures.InvalidByteOrderChange.serializer(), EncodeStringFixtures.InvalidByteOrderChange("Invalid"))
        }

        assertFailsWith<DataAfterUnconstrainedData> {
            createBackend().encode(EncodeStringFixtures.ContentAfterUnsized.serializer(), EncodeStringFixtures.ContentAfterUnsized("StringContent", 0x01))
        }

        assertFailsWith<DataAfterUnconstrainedData> {
            createBackend().encode(EncodeStringFixtures.BooleanAfterUnsized.serializer(), EncodeStringFixtures.BooleanAfterUnsized("StringContent", true))
        }
    }
}

class ViewStringsTest : StringsSuite(ViewBackend.Factory)
