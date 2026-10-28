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
import com.splendo.kaluga.bytes.annotations.ByOrdinal
import com.splendo.kaluga.bytes.annotations.BytesLayout
import com.splendo.kaluga.bytes.annotations.Checksum
import com.splendo.kaluga.bytes.annotations.FlagIndex
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.annotations.LengthPrefix
import com.splendo.kaluga.bytes.annotations.NullTerminated
import com.splendo.kaluga.bytes.annotations.Postfix
import com.splendo.kaluga.bytes.annotations.Prefix
import com.splendo.kaluga.bytes.annotations.Reserved
import com.splendo.kaluga.bytes.annotations.SerializedByteValue
import com.splendo.kaluga.bytes.annotations.Size
import com.splendo.kaluga.bytes.annotations.SizePolymorphic
import com.splendo.kaluga.bytes.annotations.Unsigned
import com.splendo.kaluga.bytes.annotations.Unsized
import com.splendo.kaluga.bytes.crc.CRC16
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

// CRC uses childByteOrder (MSB), so it is written high-byte-first.
// Body bytes are laid out sequentially (excludeStructure keeps the builder LSB/sequential).
// @Checksum uses the same algorithm as CRC16.compute (poly 0x8005, init 0x0000, reflectIn/Out=true).
@BytesLayout
@Prefix([0xAA.toByte(), 0x55.toByte()])
@Postfix([0x55.toByte()])
@Checksum(16, 0x8005u, 0x0000u, reflectIn = true, reflectOut = true)
@com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
sealed interface BigEndianFrame

@BytesLayout
@SerializedByteValue(0x12, 0x00, 0x00)
interface EmptyVariant : BigEndianFrame

// An unannotated nested structure must accumulate in the parent's builder direction (LSB = sequential)
// even when the parent's childByteOrder = MSB.  Without the preferredBuilderByteOrder fix, the nested
// struct would inherit childByteOrder (MSB) as its byteOrder and reverse its own content.
@BytesLayout
@com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
sealed interface Frame

@BytesLayout
@SerializedByteValue(0x01, 0x00, 0x04)
interface Payload : Frame {
    // field 1 - must appear first in wire
    @Size(Length.`16_BIT`)
    @Unsigned
    val a: Int

    // field 2 - must appear second
    @Size(Length.`16_BIT`)
    @Unsigned
    val b: Int
}

// childByteOrder (MSB) must propagate to enum ordinal encoding inside the structure.
@Serializable
@ByOrdinal(size = Length.`16_BIT`)
enum class Status { OFF, ON }

@BytesLayout
@com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
interface StatusFrame {
    val status: Status
}

// @Reserved bytes must be included in the staticByteSize used by @SizePolymorphic dispatch.
// Short: 1 value byte + 2 reserved after = 3 bytes total.
// Long:  1 value byte + 4 reserved after = 5 bytes total.
@BytesLayout
@SizePolymorphic
sealed interface ReservedPayload

@BytesLayout
interface Short3 : ReservedPayload {
    @Reserved(after = 2)
    @Size(Length.`8_BIT`)
    @Unsigned
    val value: Int
}

@BytesLayout
interface Long5 : ReservedPayload {
    @Reserved(after = 4)
    @Size(Length.`8_BIT`)
    @Unsigned
    val value: Int
}

// A value class carrying its own prefix, checksum and postfix, as the only property of a layout so both backends can
// encode it.
@Serializable
@JvmInline
@Prefix([0x55])
@Postfix([0xAA.toByte()])
@Checksum(16, 0x8005u, 0x0000u, reflectIn = true, reflectOut = true)
value class FramedValue(@Size(Length.`8_BIT`) val int: Int)

object ValueClassWithHeaderAndFooterFixtures {
    @BytesLayout
    interface Holder {
        val value: FramedValue
    }
}

object EncodeWithChecksumFixtures {
    @BytesLayout
    @Prefix([0x19])
    @Postfix([0x45])
    @Checksum(16, 0x8005u, 0x0000u, reflectIn = true, reflectOut = true)
    interface WithChecksum {
        val index: Int
        val content: String
    }
}

object EncodeWithChecksumMostSignificantFirstFixtures {
    @BytesLayout
    @Prefix([0x19])
    @Postfix([0x45])
    @Checksum(16, 0x8005u, 0x0000u, reflectIn = true, reflectOut = true)
    @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
    interface WithChecksum {
        val index: Int
        val content: String
    }
}

object EncodeNullTerminatedAndOverflowMostSignificantFirstFixtures {
    @BytesLayout
    @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
    interface Container {
        @NullTerminated val nullTerminated: Map<String, String>

        @LengthPrefix(lengthAsShort = false, canOverflow = true)
        val overflow: String
    }
}

object ByteOrderExcludeStructureEncodesSequentiallyFixtures {
    // Plain @ByteOrder(MSB) reverses the entire array; excludeStructure = true must NOT do that.
    // Fields appear in declaration order; each multi-byte value defaults to big-endian (childByteOrder),
    // but individual properties can still override back to LSB - childByteOrder is a default, not a lock.
    @BytesLayout
    @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
    interface Frame {
        // inherits childByteOrder -> big-endian
        @Size(Length.`16_BIT`)
        @Unsigned
        val a: Int

        // explicit per-property LSB override
        @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.LEAST_SIGNIFICANT_FIRST)
        @Size(Length.`16_BIT`)
        @Unsigned
        val b: Int

        // inherits childByteOrder -> big-endian
        @Size(Length.`16_BIT`)
        @Unsigned
        val c: Int
    }
}

object ByteOrderExcludeStructureNestedInsideLsbParentAllowedFixtures {
    @BytesLayout
    @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
    interface BigEndianFields {
        @Size(Length.`16_BIT`)
        @Unsigned
        val value: Int
    }

    @BytesLayout
    interface Outer {
        val inner: BigEndianFields
    }
}

object ByteOrderExcludeStructureDoesNotChangeStringCheckFixtures {
    // A @BytesLayout with this configuration is rejected at compile time; kept as a kotlinx class for the runtime check.
    @Serializable
    data class InvalidStringByteOrder(@com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST) val text: String)
}

object ReservedBytesAfterFixtures {
    // Two reserved bytes appended after the last field (trailing padding).
    @BytesLayout
    interface Packet {
        @Size(Length.`8_BIT`)
        @Unsigned
        val a: Int

        @Reserved(after = 2)
        @Size(Length.`8_BIT`)
        @Unsigned
        val b: Int
    }
}

object ReservedBytesBeforeFixtures {
    // Two reserved bytes before the second field (mid-packet padding).
    @BytesLayout
    interface Packet {
        @Size(Length.`8_BIT`)
        @Unsigned
        val a: Int

        @Reserved(before = 2)
        @Size(Length.`8_BIT`)
        @Unsigned
        val b: Int
    }
}

object ReservedBytesBeforeAndAfterFixtures {
    // Reserved padding both before and after a single field:
    //   [a (2B)] [reserved (2B)] [b (2B)] [reserved (2B)]
    @BytesLayout
    interface Packet {
        @Size(Length.`16_BIT`)
        @Unsigned
        val a: Int

        @Reserved(before = 2, after = 2)
        @Size(Length.`16_BIT`)
        @Unsigned
        val b: Int
    }
}

object ReservedBytesDecodeIgnoresContentFixtures {
    @BytesLayout
    interface Packet {
        @Size(Length.`8_BIT`)
        @Unsigned
        val a: Int

        @Reserved(before = 1, after = 1)
        @Size(Length.`8_BIT`)
        @Unsigned
        val b: Int
    }
}

object ReservedBytesMultipleFieldsFixtures {
    // Reserved padding around each of several fields.
    @BytesLayout
    interface Packet {
        @Reserved(after = 1)
        @Size(Length.`8_BIT`)
        @Unsigned
        val x: Int

        @Reserved(before = 1, after = 2)
        @Size(Length.`8_BIT`)
        @Unsigned
        val y: Int

        @Size(Length.`8_BIT`)
        @Unsigned
        val z: Int
    }
}

// A checksummed layout nested in another, whose setters must keep both checksums up to date.
object NestedChecksumFixtures {
    @BytesLayout
    @Prefix([0x01])
    @Checksum(16, 0x8005u, 0x0000u, reflectIn = true, reflectOut = true)
    interface Inner {
        val count: Byte
    }

    @BytesLayout
    @Postfix([0x7F])
    @Checksum(16, 0x1021u, 0xFFFFu)
    interface Outer {
        val inner: Inner
        val last: Int
    }
}

object UnsizedPayloadWithChecksumRoundTripsFixtures {
    @BytesLayout
    @Prefix([0x11])
    @Postfix([0x22])
    @Checksum(16, 0x8005u, 0x0000u, reflectIn = true, reflectOut = true)
    interface UnsizedWithChecksum {
        @Unsized val payload: List<Byte>
    }
}

object ReservedAfterFixtures {
    @BytesLayout
    interface ReservedAfterValueClass {
        @Reserved(after = 2)
        val wrapped: ValueContainer<Byte>

        @Size(Length.`8_BIT`)
        val trailer: Byte
    }

    @BytesLayout
    interface ReservedAfterUnsigned {
        @Reserved(after = 2)
        val v: UByte

        @Size(Length.`8_BIT`)
        val trailer: Byte
    }

    @BytesLayout
    interface ReservedAfterNullable {
        @Reserved(after = 2)
        val v: Byte?

        @Size(Length.`8_BIT`)
        val trailer: Byte
    }
}

object BooleansBeforeNestedStructureFixtures {
    @BytesLayout
    interface EmptyMarker

    @BytesLayout
    interface EmptyNestedAfterBooleans {
        val flagA: Boolean
        val flagB: Boolean
        val marker: EmptyMarker

        @Size(Length.`8_BIT`)
        val trailer: Byte
    }

    @BytesLayout
    interface NestedFlagHeader {
        @FlagIndex(0)
        val flag: Boolean
    }

    @BytesLayout
    interface NestedLeadingBoolean {
        val flag: Boolean
    }

    @BytesLayout
    interface ParentBooleansThenNestedFlagHeader {
        val boolA: Boolean
        val boolB: Boolean
        val nested: NestedFlagHeader

        @Size(Length.`8_BIT`)
        val trailer: Byte
    }

    @BytesLayout
    interface ParentBooleansThenNestedLeadingBoolean {
        val boolA: Boolean
        val boolB: Boolean
        val nested: NestedLeadingBoolean

        @Size(Length.`8_BIT`)
        val trailer: Byte
    }
}

object FramedValueClassFixtures {
    @Serializable
    enum class Mode3 { OFF, LOW, HIGH }

    @Serializable
    @JvmInline
    @Prefix([0x77])
    @Postfix([0x88.toByte()])
    value class FramedFlagValue(@FlagIndex(0) val mode: Mode3)

    @BytesLayout
    interface FramedFlagHolder {
        val wrapped: FramedFlagValue

        @Size(Length.`8_BIT`)
        val trailer: Byte
    }

    @Serializable
    @JvmInline
    @Prefix([0x66])
    @Postfix([0x99.toByte()])
    value class FramedWideSizeValue(@Size(Length.`8_BIT`) @Size(Length.`64_BIT`) val v: Long)

    @BytesLayout
    interface FramedWideSizeHolder {
        val wrapped: FramedWideSizeValue

        @Size(Length.`8_BIT`)
        val trailer: Byte
    }

    @Serializable
    @JvmInline
    value class NullableWrapped(val v: Byte?)

    @BytesLayout
    interface NullableWrappedHolder {
        val wrapped: NullableWrapped

        @Size(Length.`8_BIT`)
        val trailer: Byte
    }

    @Serializable
    @JvmInline
    @Prefix([0x55])
    @Postfix([0xAA.toByte()])
    value class NullableWrappedFramed(val v: Byte?)

    @BytesLayout
    interface NullableWrappedFramedHolder {
        val wrapped: NullableWrappedFramed

        @Size(Length.`8_BIT`)
        val trailer: Byte
    }
}
