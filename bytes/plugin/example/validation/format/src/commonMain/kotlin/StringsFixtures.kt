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
import com.splendo.kaluga.bytes.annotations.Encoded
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.annotations.LengthPrefix
import com.splendo.kaluga.bytes.annotations.NullTerminated
import com.splendo.kaluga.bytes.annotations.Size
import com.splendo.kaluga.bytes.annotations.Unsized
import kotlinx.serialization.Serializable

object EncodeStringFixtures {
    @BytesLayout
    interface Container {
        val default: String

        @Encoded(Encoding.UTF_8)
        val utf8: String

        @Encoded(Encoding.UTF_16)
        val utf16: String

        @Encoded(Encoding.ASCII)
        val ascii: String
        val nullable: String?

        @Encoded(Encoding.UTF_16)
        val nullableUTF16: String?

        @NullTerminated val nullTerminated: String

        @LengthPrefix(lengthAsShort = false, canOverflow = true)
        val lengthPrefix: String

        @Unsized val unsized: String
    }

    // A @BytesLayout with this configuration is rejected at compile time; kept as a kotlinx class for the runtime check.
    @Serializable
    data class InvalidByteOrderChange(@com.splendo.kaluga.bytes.annotations.ByteOrder(com.splendo.kaluga.bytes.ByteOrder.MOST_SIGNIFICANT_FIRST) val content: String)

    @BytesLayout
    interface ContentContainsNull {
        @NullTerminated val content: String
    }

    // A @BytesLayout with this configuration is rejected at compile time; kept as a kotlinx class for the runtime check.
    @Serializable
    data class ContentAfterUnsized(@Unsized val string: String, val otherContent: Byte)

    // A @BytesLayout with this configuration is rejected at compile time; kept as a kotlinx class for the runtime check.
    // A plain Boolean (no @FlagIndex/@FlagWidth) has no flag slot, so it is encoded into the body via
    // addBit rather than addAction. Placing one after an @Unsized String must still be rejected: there is
    // no way to know where the unsized data ends, so any trailing body data is undecodable.
    @Serializable
    data class BooleanAfterUnsized(@Unsized val string: String, val flag: Boolean)
}

object MsbExcludeStructureLengthPrefixAndNullTerminatedStringRoundTripsFixtures {
    @BytesLayout
    @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
    interface MsbStringFrame {
        @LengthPrefix val prefixed: String

        @NullTerminated val terminated: String
    }
}

object LongByteLengthPrefixedStringRoundTripsFixtures {
    @BytesLayout
    interface LongByteLengthString {
        @LengthPrefix val s: String

        @Size(Length.`8_BIT`)
        val trailer: Byte
    }
}
