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

import com.splendo.kaluga.bytes.annotations.BytesLayout
import com.splendo.kaluga.bytes.annotations.Checksum
import com.splendo.kaluga.bytes.annotations.Count
import com.splendo.kaluga.bytes.annotations.Discriminator
import com.splendo.kaluga.bytes.annotations.Fallback
import com.splendo.kaluga.bytes.annotations.FlagIndex
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.annotations.LengthPrefix
import com.splendo.kaluga.bytes.annotations.Postfix
import com.splendo.kaluga.bytes.annotations.SerializedByteValue
import com.splendo.kaluga.bytes.annotations.Size
import com.splendo.kaluga.bytes.annotations.Trailing
import com.splendo.kaluga.bytes.annotations.Unsized

object TrailingFixtures {
    /**
     * A status value sent with or without the fields at its end: a flag header, a number of several sizes and a nullable
     * property before the tail, and a number of several sizes in it, whose size bits the header keeps either way.
     */
    @BytesLayout
    interface Status {
        @FlagIndex(0)
        val charging: Boolean

        @FlagIndex(1)
        @Size(Length.`8_BIT`)
        @Size(Length.`16_BIT`)
        val level: Int

        val mode: Byte?

        val voltage: UShort

        @Trailing
        val serviceVersion: UShort?

        @Trailing
        @FlagIndex(4)
        @Size(Length.`16_BIT`)
        @Size(Length.`32_BIT`)
        val uptime: Int?
    }

    /** Records of information whose types grew fields at their end, each bounded by its length. */
    @BytesLayout
    @LengthPrefix
    sealed interface Info {
        @BytesLayout
        @SerializedByteValue(0x01)
        interface Device : Info {
            val model: UShort

            @Trailing
            val serial: ULong?

            @Trailing
            val word: UShort?
        }

        @BytesLayout
        @SerializedByteValue(0x02)
        interface Battery : Info {
            val percent: UByte
        }

        /** The discriminator takes no bytes, so it may follow the tail. */
        @BytesLayout
        @Fallback
        interface Other : Info {
            val first: Byte

            @Trailing
            val second: Byte?

            @Discriminator
            val type: UByte
        }
    }

    @BytesLayout
    interface Infos {
        val count: Byte

        @Unsized
        val infos: List<Info>
    }

    @BytesLayout
    interface Span {
        val low: Short
        val high: Short
    }

    /** A layout, a collection and a string in the tail. */
    @BytesLayout
    interface Limits {
        val id: Byte

        @Trailing
        val span: Span?

        @Trailing
        @Count(2)
        val points: List<Short>?

        @Trailing
        val label: String?
    }

    /** The tail ends before the checksum and the postfix, which end the value. */
    @BytesLayout
    @Checksum(8, 0x07u, 0x00u)
    @Postfix([0x7F])
    interface Framed {
        val first: Byte

        @Trailing
        val second: Short?
    }
}
