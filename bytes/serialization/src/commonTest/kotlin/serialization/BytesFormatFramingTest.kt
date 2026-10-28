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
import com.splendo.kaluga.bytes.ByteOrder
import com.splendo.kaluga.bytes.strings.StringEncodingSettings
import com.splendo.kaluga.bytes.buildByteArray
import com.splendo.kaluga.bytes.toByteArray
import com.splendo.kaluga.bytes.annotations.ByOrdinal
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
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import com.splendo.kaluga.bytes.layout.InvalidByteOrderException
import com.splendo.kaluga.bytes.layout.InvalidChecksumException
import com.splendo.kaluga.bytes.layout.InvalidPostfix
import com.splendo.kaluga.bytes.layout.InvalidPrefix

@Serializable
@JvmInline
@Prefix([0x55])
@Postfix([0xAA.toByte()])
@Checksum(16, 0x8005u, 0x0000u, reflectIn = true, reflectOut = true)
private value class ValueContainerWithHeaderAndFooter(@Size(Length.`8_BIT`) val int: Int)

// CRC uses childByteOrder (MSB), so it is written high-byte-first.
// Body bytes are laid out sequentially (excludeStructure keeps the builder LSB/sequential).
// @Checksum uses the same algorithm as CRC16.compute (poly 0x8005, init 0x0000, reflectIn/Out=true).
@Serializable
@Prefix([0xAA.toByte(), 0x55.toByte()])
@Postfix([0x55.toByte()])
@Checksum(16, 0x8005u, 0x0000u, reflectIn = true, reflectOut = true)
@com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
private sealed interface BigEndianFrame

@Serializable
@SerializedByteValue(0x12, 0x00, 0x00)
private data object EmptyVariant : BigEndianFrame

// An unannotated nested structure must accumulate in the parent's builder direction (LSB = sequential)
// even when the parent's childByteOrder = MSB.  Without the preferredBuilderByteOrder fix, the nested
// struct would inherit childByteOrder (MSB) as its byteOrder and reverse its own content.
@Serializable
@com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
private sealed interface Frame

@Serializable
@SerializedByteValue(0x01, 0x00, 0x04)
private data class Payload(
    @Size(Length.`16_BIT`) @Unsigned val a: Int, // field 1 - must appear first in wire
    @Size(Length.`16_BIT`) @Unsigned val b: Int, // field 2 - must appear second
) : Frame

// childByteOrder (MSB) must propagate to enum ordinal encoding inside the structure.
@Serializable
@ByOrdinal(size = Length.`16_BIT`)
private enum class Status { OFF, ON }

@Serializable
@com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
private data class StatusFrame(val status: Status)

// @Reserved bytes must be included in the staticByteSize used by @SizePolymorphic dispatch.
// Short: 1 value byte + 2 reserved after = 3 bytes total.
// Long:  1 value byte + 4 reserved after = 5 bytes total.
@Serializable
@SizePolymorphic
private sealed interface ReservedPayload

@Serializable
private data class Short3(@Reserved(after = 2) @Size(Length.`8_BIT`) @Unsigned val value: Int) : ReservedPayload

@Serializable
private data class Long5(@Reserved(after = 4) @Size(Length.`8_BIT`) @Unsigned val value: Int) : ReservedPayload

class BytesFormatFramingTest : BytesFormatTestBase(BytesFormatBackend.Factory) {

    private object EncodeWithChecksumFixtures {
        @Serializable
        @Prefix([0x19])
        @Postfix([0x45])
        @Checksum(16, 0x8005u, 0x0000u, reflectIn = true, reflectOut = true)
        data class WithChecksum(val index: Int, val content: String)
    }

    @Test
    fun encodeWithChecksum() {
        validateEncoding(
            EncodeWithChecksumFixtures.WithChecksum(1234, "123456789"),
            buildByteArray {
                add(0x19.toByte())
                val body = buildByteArray {
                    add(1234)
                    add("123456789")
                }
                add(body)
                add(CRC16.compute(body).toUShort())
                add(0x45.toByte())
            },
        )
    }

    private object EncodeWithChecksumMostSignificantFirstFixtures {
        @Serializable
        @Prefix([0x19])
        @Postfix([0x45])
        @Checksum(16, 0x8005u, 0x0000u, reflectIn = true, reflectOut = true)
        @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
        data class WithChecksum(val index: Int, val content: String)
    }

    @Test
    fun encodeWithChecksumMostSignificantFirst() {
        val value = EncodeWithChecksumMostSignificantFirstFixtures.WithChecksum(1234, "123456789")

        // The checksum is computed over the body only (everything between the prefix and the checksum bytes).
        // In MOST_SIGNIFICANT_FIRST order the body is laid out as built here.
        val body = buildByteArray(ByteOrder.MOST_SIGNIFICANT_FIRST) {
            add(1234)
            add("123456789")
        }
        val expectedChecksum = CRC16.compute(body)

        // The stored CRC is just another multi-byte numeric in the structure's byte order, so in
        // MOST_SIGNIFICANT_FIRST it is written most-significant-byte first (hi, lo).
        val crcBytes = expectedChecksum.toUShort().toByteArray(ByteOrder.MOST_SIGNIFICANT_FIRST)
        val expectedBytes = buildByteArray(ByteOrder.MOST_SIGNIFICANT_FIRST) {
            add(byteArrayOf(0x19))
            add(body)
            add(crcBytes)
            add(byteArrayOf(0x45))
        }

        // Full round-trip with checksum validation enabled (default BytesFormat): encode then decode.
        validateRoundTrip(value, EncodeWithChecksumMostSignificantFirstFixtures.WithChecksum.serializer(), expectedBytes)
    }

    @Test
    fun encodeValueClassWithChecksumAndHeaderFooter() {
        val byteValue: Byte = 0x12
        val value = ValueContainerWithHeaderAndFooter(byteValue.toInt())
        val expectedChecksum = CRC16.compute(byteArrayOf(byteValue))
        val expectedBytes = buildByteArray {
            add(0x55.toByte())
            add(byteValue)
            add(expectedChecksum.toUShort())
            add(0xAA.toByte())
        }

        validateRoundTrip(value, ValueContainerWithHeaderAndFooter.serializer(), expectedBytes)
    }

    @Test
    fun valueClassWithHeaderFooterRejectsWrongPrefix() {
        assertFailsWith<InvalidPrefix> {
            BytesFormat.decodeFromByteArray(
                ValueContainerWithHeaderAndFooter.serializer(),
                byteArrayOf(0x44, 0x12, 0x00, 0x00, 0xAA.toByte()), // 0x44 instead of expected 0x55
            )
        }
    }

    @Test
    fun valueClassWithHeaderFooterRejectsWrongPostfix() {
        val byteValue: Byte = 0x12
        val crc = CRC16.compute(byteArrayOf(byteValue))
        assertFailsWith<InvalidPostfix> {
            BytesFormat.decodeFromByteArray(
                ValueContainerWithHeaderAndFooter.serializer(),
                buildByteArray {
                    add(0x55.toByte())
                    add(byteValue)
                    add(crc.toUShort())
                    add(0xBB.toByte()) // 0xBB instead of expected 0xAA
                },
            )
        }
    }

    @Test
    fun valueClassWithHeaderFooterRejectsWrongChecksum() {
        assertFailsWith<InvalidChecksumException> {
            BytesFormat.decodeFromByteArray(
                ValueContainerWithHeaderAndFooter.serializer(),
                byteArrayOf(0x55, 0x12, 0x00, 0x00, 0xAA.toByte()), // 0x0000 is not CRC16(0x12)
            )
        }
    }

    private object EncodeNullTerminatedAndOverflowMostSignificantFirstFixtures {
        @Serializable
        @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
        data class Container(@NullTerminated val nullTerminated: Map<String, String>, @LengthPrefix(lengthAsShort = false, canOverflow = true) val overflow: String)
    }

    @Test
    fun encodeNullTerminatedAndOverflowMostSignificantFirst() {
        val longString = MutableList(500) { "X" }.joinToString("")
        val value = EncodeNullTerminatedAndOverflowMostSignificantFirstFixtures.Container(
            nullTerminated = mapOf("Key" to "Value", "Foo" to "Bar"),
            overflow = longString,
        )

        // Mirror the encoder's MSB build order so the expected bytes match the encoded output exactly.
        // @NullTerminated marks the *map* as null-terminated (single 0x00 sentinel after the last entry);
        // the string keys/values keep their default length-prefix encoding.
        val expected = buildByteArray(ByteOrder.MOST_SIGNIFICANT_FIRST) {
            add("Key")
            add("Value")
            add("Foo")
            add("Bar")
            add(byte = 0x00)
            // overflow string: length > 255 so the sentinel + 2-byte length is written
            add(longString, StringEncodingSettings(endMarking = StringEncodingSettings.LengthPrefix.WithOverflow()))
        }

        validateRoundTrip(value, EncodeNullTerminatedAndOverflowMostSignificantFirstFixtures.Container.serializer(), expected)
    }

    // ── @ByteOrder(excludeStructure = true) ──────────────────────────────────

    private object ByteOrderExcludeStructureEncodesSequentiallyFixtures {
        // Plain @ByteOrder(MSB) reverses the entire array; excludeStructure = true must NOT do that.
        // Fields appear in declaration order; each multi-byte value defaults to big-endian (childByteOrder),
        // but individual properties can still override back to LSB - childByteOrder is a default, not a lock.
        @Serializable
        @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
        data class Frame(
            @Size(Length.`16_BIT`) @Unsigned val a: Int, // inherits childByteOrder -> big-endian
            @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.LEAST_SIGNIFICANT_FIRST)
            @Size(Length.`16_BIT`)
            @Unsigned val b: Int, // explicit per-property LSB override
            @Size(Length.`16_BIT`) @Unsigned val c: Int, // inherits childByteOrder -> big-endian
        )
    }

    @Test
    fun byteOrderExcludeStructureEncodesSequentially() {
        // a=0x0102 -> [01 02] (MSB), b=0x0304 -> [04 03] (LSB override), c=0x0506 -> [05 06] (MSB)
        // Plain @ByteOrder(MSB) without excludeStructure would reverse the whole frame.
        validateEncoding(
            ByteOrderExcludeStructureEncodesSequentiallyFixtures.Frame(a = 0x0102, b = 0x0304, c = 0x0506),
            byteArrayOf(0x01, 0x02, 0x04, 0x03, 0x05, 0x06),
        )
    }

    @Test
    fun byteOrderExcludeStructureWithChecksumEmitsCrcBigEndian() {
        // Body (what CRC covers) = discriminator only = [0x12, 0x00, 0x00]
        val body = byteArrayOf(0x12, 0x00, 0x00)
        val crc = CRC16.compute(body)
        // CRC emitted big-endian (childByteOrder = MSB): high byte first
        val crcHi = ((crc shr 8) and 0xFFu).toByte()
        val crcLo = (crc and 0xFFu).toByte()

        val expected = byteArrayOf(0xAA.toByte(), 0x55.toByte()) + body + byteArrayOf(crcHi, crcLo, 0x55)
        // Must use BigEndianFrame.serializer() so the sealed interface's @Prefix/@Checksum/@Postfix apply.
        validateRoundTrip(EmptyVariant as BigEndianFrame, BigEndianFrame.serializer(), expected)
    }

    private object ByteOrderExcludeStructureNestedInsideLsbParentAllowedFixtures {
        @Serializable
        @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
        data class BigEndianFields(@Size(Length.`16_BIT`) @Unsigned val value: Int)

        @Serializable
        data class Outer(val inner: BigEndianFields)
    }

    @Test
    fun byteOrderExcludeStructureNestedInsideLsbParentAllowed() {
        // With excludeStructure = true, byteOrder == preferredBuilderByteOrder (LSB from parent),
        // so nesting inside an LSB structure must NOT throw InvalidByteOrderException.
        // Unannotated nested structures also inherit the builder direction rather than childByteOrder.

        // value=1: big-endian -> [00 01] inside the outer LSB structure; frame is sequential
        validateEncoding(
            ByteOrderExcludeStructureNestedInsideLsbParentAllowedFixtures.Outer(ByteOrderExcludeStructureNestedInsideLsbParentAllowedFixtures.BigEndianFields(1)),
            byteArrayOf(0x00, 0x01),
        )
    }

    @Test
    fun byteOrderExcludeStructureUnannotatedNestedStructureInheritsBuilderDirection() {
        // a=0x0102 -> [01 02] first, b=0x0304 -> [03 04] second.  Both big-endian, sequential order.
        // If the unannotated Payload inherited MSB as byteOrder it would reverse: [03 04 01 02].
        validateRoundTrip(
            Payload(0x0102, 0x0304),
            Frame.serializer(),
            byteArrayOf(0x01, 0x00, 0x04, 0x01, 0x02, 0x03, 0x04),
        )
    }

    @Test
    fun byteOrderExcludeStructureChildByteOrderPropagatesEnum() {
        // ON = ordinal 1, big-endian 2 bytes -> [00 01]
        validateEncoding(StatusFrame(Status.ON), byteArrayOf(0x00, 0x01))
    }

    private object ByteOrderExcludeStructureDoesNotChangeStringCheckFixtures {
        @Serializable
        data class InvalidStringByteOrder(@com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST) val text: String)
    }

    @Test
    fun byteOrderExcludeStructureDoesNotChangeStringCheck() {
        // For Strings, childByteOrder still governs char encoding - changing it is still disallowed
        // inside a structure whose parent has a different childByteOrder.
        assertFailsWith<InvalidByteOrderException> {
            BytesFormat.encodeToByteArray(
                ByteOrderExcludeStructureDoesNotChangeStringCheckFixtures.InvalidStringByteOrder.serializer(),
                ByteOrderExcludeStructureDoesNotChangeStringCheckFixtures.InvalidStringByteOrder("fail"),
            )
        }
    }

    // ── @Reserved ─────────────────────────────────────────────────────────────

    @Test
    fun reservedBytesCountedInStaticSize() {
        validateEncoding(Short3(0x0A), byteArrayOf(0x0A, 0x00, 0x00))
        validateEncoding(Long5(0x0B), byteArrayOf(0x0B, 0x00, 0x00, 0x00, 0x00))
    }

    private object ReservedBytesAfterFixtures {
        // Two reserved bytes appended after the last field (trailing padding).
        @Serializable
        data class Packet(@Size(Length.`8_BIT`) @Unsigned val a: Int, @Reserved(after = 2) @Size(Length.`8_BIT`) @Unsigned val b: Int)
    }

    @Test
    fun reservedBytesAfter() {
        validateEncoding(
            ReservedBytesAfterFixtures.Packet(0x01, 0x02),
            byteArrayOf(
                0x01, // a
                0x02, // b
                0x00,
                0x00, // 2 reserved bytes after b
            ),
        )
    }

    private object ReservedBytesBeforeFixtures {
        // Two reserved bytes before the second field (mid-packet padding).
        @Serializable
        data class Packet(@Size(Length.`8_BIT`) @Unsigned val a: Int, @Reserved(before = 2) @Size(Length.`8_BIT`) @Unsigned val b: Int)
    }

    @Test
    fun reservedBytesBefore() {
        validateEncoding(
            ReservedBytesBeforeFixtures.Packet(0x01, 0x02),
            byteArrayOf(
                0x01, // a
                0x00,
                0x00, // 2 reserved bytes before b
                0x02, // b
            ),
        )
    }

    private object ReservedBytesBeforeAndAfterFixtures {
        // Reserved padding both before and after a single field:
        //   [a (2B)] [reserved (2B)] [b (2B)] [reserved (2B)]
        @Serializable
        data class Packet(@Size(Length.`16_BIT`) @Unsigned val a: Int, @Reserved(before = 2, after = 2) @Size(Length.`16_BIT`) @Unsigned val b: Int)
    }

    @Test
    fun reservedBytesBeforeAndAfter() {
        validateEncoding(
            ReservedBytesBeforeAndAfterFixtures.Packet(a = 0x1600, b = 0x0258),
            byteArrayOf(
                0x00,
                0x16, // a (LE)
                0x00,
                0x00, // 2 reserved before b
                0x58,
                0x02, // b (LE)
                0x00,
                0x00, // 2 reserved after b
            ),
        )
    }

    private object ReservedBytesDecodeIgnoresContentFixtures {
        @Serializable
        data class Packet(@Size(Length.`8_BIT`) @Unsigned val a: Int, @Reserved(before = 1, after = 1) @Size(Length.`8_BIT`) @Unsigned val b: Int)
    }

    @Test
    fun reservedBytesDecodeIgnoresContent() {
        // On decode, non-zero reserved bytes are silently discarded - no exception.
        val serializer = ReservedBytesDecodeIgnoresContentFixtures.Packet.serializer()
        val bytes = byteArrayOf(
            0x0A, // a
            0xFF.toByte(), // reserved before b - garbage, should be ignored
            0x0B, // b
            0xDE.toByte(), // reserved after b - garbage, should be ignored
        )
        assertEquals(ReservedBytesDecodeIgnoresContentFixtures.Packet(0x0A, 0x0B), BytesFormat.decodeFromByteArray(serializer, bytes))
    }

    private object ReservedBytesMultipleFieldsFixtures {
        // Reserved padding around each of several fields.
        @Serializable
        data class Packet(
            @Reserved(after = 1) @Size(Length.`8_BIT`) @Unsigned val x: Int,
            @Reserved(before = 1, after = 2) @Size(Length.`8_BIT`) @Unsigned val y: Int,
            @Size(Length.`8_BIT`) @Unsigned val z: Int,
        )
    }

    @Test
    fun reservedBytesMultipleFields() {
        validateEncoding(
            ReservedBytesMultipleFieldsFixtures.Packet(x = 0x01, y = 0x02, z = 0x03),
            byteArrayOf(
                0x01, // x
                0x00, // 1 reserved after x
                0x00, // 1 reserved before y
                0x02, // y
                0x00,
                0x00, // 2 reserved after y
                0x03, // z
            ),
        )
    }

    private object UnsizedPayloadWithChecksumRoundTripsFixtures {
        @Serializable
        @Prefix([0x11])
        @Postfix([0x22])
        @Checksum(16, 0x8005u, 0x0000u, reflectIn = true, reflectOut = true)
        data class UnsizedWithChecksum(@Unsized val payload: List<Byte>)
    }

    @Test
    fun unsizedPayloadWithChecksumRoundTrips() {
        // The checksum is part of the footer, so the @Unsized payload stops before it.
        val body = byteArrayOf(0x01, 0x02, 0x03)
        val expectedBytes = buildByteArray {
            add(0x11.toByte())
            add(body)
            add(CRC16.compute(body).toUShort())
            add(0x22.toByte())
        }
        validateEncoding(
            UnsizedPayloadWithChecksumRoundTripsFixtures.UnsizedWithChecksum(payload = listOf(0x01, 0x02, 0x03)),
            UnsizedPayloadWithChecksumRoundTripsFixtures.UnsizedWithChecksum.serializer(),
            expectedBytes,
        )
    }

    private object ReservedAfterFixtures {
        @Serializable
        data class ReservedAfterValueClass(@Reserved(after = 2) val wrapped: ValueContainer<Byte>, @Size(Length.`8_BIT`) val trailer: Byte)

        @Serializable
        data class ReservedAfterUnsigned(@Reserved(after = 2) val v: UByte, @Size(Length.`8_BIT`) val trailer: Byte)

        @Serializable
        data class ReservedAfterNullable(@Reserved(after = 2) val v: Byte?, @Size(Length.`8_BIT`) val trailer: Byte)
    }

    @Test
    fun reservedAfterOnValueClassPropertyWritesPadding() {
        validateRoundTrip(
            ReservedAfterFixtures.ReservedAfterValueClass(wrapped = ValueContainer(0x11), trailer = 0x22),
            ReservedAfterFixtures.ReservedAfterValueClass.serializer(),
            byteArrayOf(0x11, 0x00, 0x00, 0x22),
        )
    }

    @Test
    fun reservedAfterOnUnsignedPropertyWritesPadding() {
        validateRoundTrip(
            ReservedAfterFixtures.ReservedAfterUnsigned(v = 0x11u, trailer = 0x22),
            ReservedAfterFixtures.ReservedAfterUnsigned.serializer(),
            byteArrayOf(0x11, 0x00, 0x00, 0x22),
        )
    }

    @Test
    fun reservedAfterOnNullablePropertyWritesPaddingWhenPresent() {
        validateRoundTrip(
            ReservedAfterFixtures.ReservedAfterNullable(v = 0x11, trailer = 0x22),
            ReservedAfterFixtures.ReservedAfterNullable.serializer(),
            byteArrayOf(0x01, 0x11, 0x00, 0x00, 0x22),
        )
    }

    @Test
    fun reservedAfterOnNullablePropertySkipsPaddingWhenNull() {
        // The padding belongs to the property's slot, so an absent property has none.
        validateRoundTrip(
            ReservedAfterFixtures.ReservedAfterNullable(v = null, trailer = 0x22),
            ReservedAfterFixtures.ReservedAfterNullable.serializer(),
            byteArrayOf(0x00, 0x22),
        )
    }

    private object BooleansBeforeNestedStructureFixtures {
        @Serializable
        data object EmptyMarker

        @Serializable
        data class EmptyNestedAfterBooleans(val flagA: Boolean, val flagB: Boolean, val marker: EmptyMarker, @Size(Length.`8_BIT`) val trailer: Byte)

        @Serializable
        data class NestedFlagHeader(@FlagIndex(0) val flag: Boolean)

        @Serializable
        data class NestedLeadingBoolean(val flag: Boolean)

        @Serializable
        data class ParentBooleansThenNestedFlagHeader(val boolA: Boolean, val boolB: Boolean, val nested: NestedFlagHeader, @Size(Length.`8_BIT`) val trailer: Byte)

        @Serializable
        data class ParentBooleansThenNestedLeadingBoolean(val boolA: Boolean, val boolB: Boolean, val nested: NestedLeadingBoolean, @Size(Length.`8_BIT`) val trailer: Byte)
    }

    @Test
    fun emptyNestedStructureAfterBodyBooleansRoundTrips() {
        validateRoundTrip(
            BooleansBeforeNestedStructureFixtures.EmptyNestedAfterBooleans(flagA = true, flagB = false, marker = BooleansBeforeNestedStructureFixtures.EmptyMarker, trailer = 0x2A),
            BooleansBeforeNestedStructureFixtures.EmptyNestedAfterBooleans.serializer(),
        )
    }

    @Test
    fun nestedFlagHeaderAfterParentBooleansRoundTrips() {
        // The parent's partial Boolean byte is padded before the nested structure starts its own flag header.
        validateRoundTrip(
            BooleansBeforeNestedStructureFixtures.ParentBooleansThenNestedFlagHeader(
                boolA = true,
                boolB = false,
                nested = BooleansBeforeNestedStructureFixtures.NestedFlagHeader(flag = true),
                trailer = 0x2A,
            ),
            BooleansBeforeNestedStructureFixtures.ParentBooleansThenNestedFlagHeader.serializer(),
            byteArrayOf(0x01, 0x01, 0x2A),
        )
    }

    @Test
    fun nestedLeadingBooleanAfterParentBooleansRoundTrips() {
        validateRoundTrip(
            BooleansBeforeNestedStructureFixtures.ParentBooleansThenNestedLeadingBoolean(
                boolA = true,
                boolB = false,
                nested = BooleansBeforeNestedStructureFixtures.NestedLeadingBoolean(flag = true),
                trailer = 0x2A,
            ),
            BooleansBeforeNestedStructureFixtures.ParentBooleansThenNestedLeadingBoolean.serializer(),
            byteArrayOf(0x01, 0x01, 0x2A),
        )
    }

    private object FramedValueClassFixtures {
        @Serializable
        enum class Mode3 { OFF, LOW, HIGH }

        @Serializable
        @JvmInline
        @Prefix([0x77])
        @Postfix([0x88.toByte()])
        value class FramedFlagValue(@FlagIndex(0) val mode: Mode3)

        @Serializable
        data class FramedFlagHolder(val wrapped: FramedFlagValue, @Size(Length.`8_BIT`) val trailer: Byte)

        @Serializable
        @JvmInline
        @Prefix([0x66])
        @Postfix([0x99.toByte()])
        value class FramedWideSizeValue(@Size(Length.`8_BIT`) @Size(Length.`64_BIT`) val v: Long)

        @Serializable
        data class FramedWideSizeHolder(val wrapped: FramedWideSizeValue, @Size(Length.`8_BIT`) val trailer: Byte)

        @Serializable
        @JvmInline
        value class NullableWrapped(val v: Byte?)

        @Serializable
        data class NullableWrappedHolder(val wrapped: NullableWrapped, @Size(Length.`8_BIT`) val trailer: Byte)

        @Serializable
        @JvmInline
        @Prefix([0x55])
        @Postfix([0xAA.toByte()])
        value class NullableWrappedFramed(val v: Byte?)

        @Serializable
        data class NullableWrappedFramedHolder(val wrapped: NullableWrappedFramed, @Size(Length.`8_BIT`) val trailer: Byte)
    }

    @Test
    fun flagPackedValueInFramedValueClassRoundTrips() {
        // A value class has no flag header of its own: the packed ordinal lives in the enclosing structure's header.
        validateRoundTrip(
            FramedValueClassFixtures.FramedFlagHolder(wrapped = FramedValueClassFixtures.FramedFlagValue(FramedValueClassFixtures.Mode3.HIGH), trailer = 0x2A),
            FramedValueClassFixtures.FramedFlagHolder.serializer(),
            byteArrayOf(0x02, 0x77, 0x88.toByte(), 0x2A),
        )
    }

    @Test
    fun wideSizeSelectionOnFramedValueClassPropertyRoundTrips() {
        validateRoundTrip(
            FramedValueClassFixtures.FramedWideSizeHolder(wrapped = FramedValueClassFixtures.FramedWideSizeValue(5L), trailer = 0x2A),
            FramedValueClassFixtures.FramedWideSizeHolder.serializer(),
        )
        validateRoundTrip(
            FramedValueClassFixtures.FramedWideSizeHolder(wrapped = FramedValueClassFixtures.FramedWideSizeValue(1L shl 40), trailer = 0x2A),
            FramedValueClassFixtures.FramedWideSizeHolder.serializer(),
        )
    }

    @Test
    fun valueClassWrappingNullableRoundTrips() {
        validateRoundTrip(
            FramedValueClassFixtures.NullableWrappedHolder(wrapped = FramedValueClassFixtures.NullableWrapped(0x11), trailer = 0x22),
            FramedValueClassFixtures.NullableWrappedHolder.serializer(),
        )
        validateRoundTrip(
            FramedValueClassFixtures.NullableWrappedHolder(wrapped = FramedValueClassFixtures.NullableWrapped(null), trailer = 0x22),
            FramedValueClassFixtures.NullableWrappedHolder.serializer(),
        )
    }

    @Test
    fun framedValueClassWrappingNullableRoundTrips() {
        // The frame stays on the wire when the wrapped value is absent; only the body bytes drop out.
        validateRoundTrip(
            FramedValueClassFixtures.NullableWrappedFramedHolder(wrapped = FramedValueClassFixtures.NullableWrappedFramed(0x11), trailer = 0x22),
            FramedValueClassFixtures.NullableWrappedFramedHolder.serializer(),
        )
        validateRoundTrip(
            FramedValueClassFixtures.NullableWrappedFramedHolder(wrapped = FramedValueClassFixtures.NullableWrappedFramed(null), trailer = 0x22),
            FramedValueClassFixtures.NullableWrappedFramedHolder.serializer(),
            byteArrayOf(0x00, 0x55, 0xAA.toByte(), 0x22),
        )
    }
}
