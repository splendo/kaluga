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
import com.splendo.kaluga.bytes.annotations.Checksum
import com.splendo.kaluga.bytes.annotations.Discriminator
import com.splendo.kaluga.bytes.annotations.Fallback
import com.splendo.kaluga.bytes.annotations.FlagIndex
import com.splendo.kaluga.bytes.annotations.FlagWidth
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.annotations.LengthPrefix
import com.splendo.kaluga.bytes.annotations.Postfix
import com.splendo.kaluga.bytes.annotations.Prefix
import com.splendo.kaluga.bytes.annotations.SerializedByteValue
import com.splendo.kaluga.bytes.annotations.Size
import com.splendo.kaluga.bytes.annotations.Unsized

object RecordFixtures {
    /** Every record of the stream starts with a time step, in as few bytes as hold it, its size in the flag header. */
    interface Timed {
        @FlagIndex(0)
        @Size(Length.`8_BIT`)
        @Size(Length.`16_BIT`)
        @Size(Length.`32_BIT`)
        val step: Int
    }

    /**
     * A record of a sensor stream: a type byte, a length byte, then the bytes the length counts, which start with the
     * flag header and the time step of every type, known or not.
     */
    @BytesLayout
    @LengthPrefix
    sealed interface Reading {
        @BytesLayout
        @SerializedByteValue(0x20)
        interface Temperature :
            Reading,
            Timed {
            val celsius: Short
        }

        @BytesLayout
        @SerializedByteValue(0x21)
        interface Run :
            Reading,
            Timed {
            @Unsized
            val samples: List<UShort>
        }

        @BytesLayout
        @SerializedByteValue(0x30)
        interface Note :
            Reading,
            Timed {
            @Unsized
            val text: String
        }

        @BytesLayout
        @SerializedByteValue(0x40)
        interface Mark : Reading

        /** A record of a type this reader does not know: its time step read like any other's, the rest kept as it is. */
        @BytesLayout
        @Fallback
        interface Other :
            Reading,
            Timed {
            @Discriminator
            val type: UByte

            @Unsized
            val value: ByteArray
        }
    }

    /** A message of records back to back after a header, to its end. */
    @BytesLayout
    interface Chunk {
        @FlagIndex(0)
        @FlagWidth(4)
        val rewinds: UByte

        @FlagIndex(4)
        val fill: Boolean
        val first: UInt

        @Unsized
        val readings: List<Reading>
    }

    /** A record bounds what it holds, so fields can follow one. */
    @BytesLayout
    interface Followed {
        val reading: Reading
        val maybe: Reading?
        val after: Byte
    }

    /** Records whose subtypes have a frame of their own, which the length counts. */
    @BytesLayout
    @LengthPrefix
    sealed interface Framed {
        @BytesLayout
        @SerializedByteValue(0x10)
        @Postfix([0x55])
        interface Ended : Framed {
            @Unsized
            val data: List<Byte>
        }

        @BytesLayout
        @SerializedByteValue(0x11)
        @Checksum(8, 0x07u, 0x00u)
        interface Summed : Framed {
            val value: Byte
        }
    }

    @BytesLayout
    interface FramedStream {
        @Unsized
        val records: List<Framed>
    }

    @BytesLayout
    interface FramedFollowed {
        val record: Framed
        val after: Byte
    }

    /** Two-byte types and a 16-bit big-endian length, framed with a prefix and a checksum. */
    @BytesLayout
    @LengthPrefix(lengthAsShort = true)
    @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
    @Prefix([0x7E])
    @Checksum(8, 0x07u, 0x00u)
    sealed interface Message {
        @BytesLayout
        @SerializedByteValue(0x01, 0x00)
        interface Value : Message {
            val value: Short
        }

        @BytesLayout
        @Fallback
        interface Other : Message {
            @Discriminator
            val type: UShort

            @Unsized
            val rest: List<Byte>
        }
    }

    /** A fallback without a length, reading the arguments of any other command to the end. */
    @BytesLayout
    sealed interface Command {
        @BytesLayout
        @SerializedByteValue(0x01)
        interface Start : Command {
            val profile: Byte
        }

        @BytesLayout
        @Fallback
        interface Unsupported : Command {
            @Discriminator
            val code: UByte

            @Unsized
            val arguments: List<Byte>
        }
    }
}
