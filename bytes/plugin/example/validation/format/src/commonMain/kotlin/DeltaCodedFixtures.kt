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
import com.splendo.kaluga.bytes.annotations.BytesLayout
import com.splendo.kaluga.bytes.annotations.DeltaCoded
import com.splendo.kaluga.bytes.annotations.Discriminator
import com.splendo.kaluga.bytes.annotations.Fallback
import com.splendo.kaluga.bytes.annotations.ItemByteOrder
import com.splendo.kaluga.bytes.annotations.ItemSize
import com.splendo.kaluga.bytes.annotations.ItemUnsigned
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.annotations.LengthPrefix
import com.splendo.kaluga.bytes.annotations.SerializedByteValue
import com.splendo.kaluga.bytes.annotations.Unsized

object DeltaCodedFixtures {
    /** Signed 16-bit samples, each after the first stored as a signed 8-bit step from the one before it. */
    @BytesLayout
    interface Waveform {
        @DeltaCoded(Length.`8_BIT`)
        val samples: List<Short>
    }

    /** Samples with a marker after them that encodes only while absent, as nothing marks where the samples end. */
    @BytesLayout
    interface Marked {
        @DeltaCoded(Length.`8_BIT`)
        val samples: List<Short>
        val marker: Byte?
    }

    /** A delta-coded array after a header, its items and steps big-endian. */
    @BytesLayout
    interface Channel {
        val channel: UByte

        @DeltaCoded(Length.`8_BIT`)
        @ItemByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
        val samples: ShortArray
    }

    /** Unsigned 16-bit counts read into Ints, wrapping around from 65535 to 0. */
    @BytesLayout
    interface Counter {
        @DeltaCoded(Length.`8_BIT`)
        @ItemSize(Length.`16_BIT`)
        @ItemUnsigned
        val counts: List<Int>
    }

    /** Signed 32-bit items with 16-bit steps. */
    @BytesLayout
    interface Wide {
        @DeltaCoded(Length.`16_BIT`)
        val values: IntArray
    }

    /** A record of a sensor stream: samples raw or delta-coded by type, so the reader knows which by the type alone. */
    @BytesLayout
    @LengthPrefix
    sealed interface Packet {
        @BytesLayout
        @SerializedByteValue(0x01)
        interface Raw : Packet {
            @Unsized
            val samples: List<Short>
        }

        @BytesLayout
        @SerializedByteValue(0x02)
        interface Delta : Packet {
            @DeltaCoded(Length.`8_BIT`)
            val samples: List<Short>
        }

        @BytesLayout
        @SerializedByteValue(0x03)
        interface Battery : Packet {
            val level: UByte
        }

        @BytesLayout
        @Fallback
        interface Other : Packet {
            @Discriminator
            val type: UByte

            @Unsized
            val value: ByteArray
        }
    }

    /** A message of packets back to back after a sequence number, to its end. */
    @BytesLayout
    interface Stream {
        val sequence: UShort

        @Unsized
        val packets: List<Packet>
    }
}
