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
import com.splendo.kaluga.bytes.strings.StringEncodingSettings
import com.splendo.kaluga.bytes.annotations.Checksum
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.annotations.LengthPrefix
import com.splendo.kaluga.bytes.annotations.NullTerminated
import com.splendo.kaluga.bytes.annotations.Postfix
import com.splendo.kaluga.bytes.annotations.Prefix
import com.splendo.kaluga.bytes.annotations.Size
import com.splendo.kaluga.bytes.buildByteArray
import com.splendo.kaluga.bytes.layout.InvalidByteOrderException
import com.splendo.kaluga.bytes.layout.InvalidChecksumException
import com.splendo.kaluga.bytes.layout.InvalidPostfix
import com.splendo.kaluga.bytes.layout.InvalidPrefix
import com.splendo.kaluga.bytes.serialization.BytesFormat
import com.splendo.kaluga.bytes.toByteArray
import com.splendo.kaluga.bytes.crc.CRC16
import com.splendo.kaluga.bytes.strings.add
import kotlin.jvm.JvmInline
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

@Serializable
@JvmInline
@Prefix([0x55])
@Postfix([0xAA.toByte()])
@Checksum(16, 0x8005u, 0x0000u, reflectIn = true, reflectOut = true)
private value class ValueContainerWithHeaderAndFooter(@Size(Length.`8_BIT`) val int: Int)

/**
 * The framing tests every backend runs; [BytesFormatFramingTest] adds the ones the views do not support yet.
 */
abstract class FramingSuite(backendFactory: ByteLayoutBackendFactory) : BytesFormatTestBase(backendFactory) {

    @Test
    fun byteOrderExcludeStructureEncodesSequentially() {
        // a=0x0102 -> [01 02] (MSB), b=0x0304 -> [04 03] (LSB override), c=0x0506 -> [05 06] (MSB)
        // Plain @ByteOrder(MSB) without excludeStructure would reverse the whole frame.
        validateEncoding(
            ByteOrderExcludeStructureEncodesSequentiallyFixturesFrameData(a = 0x0102, b = 0x0304, c = 0x0506),
            byteArrayOf(0x01, 0x02, 0x04, 0x03, 0x05, 0x06),
        )
    }

    @Test
    fun byteOrderExcludeStructureNestedInsideLsbParentAllowed() {
        // With excludeStructure = true, byteOrder == preferredBuilderByteOrder (LSB from parent),
        // so nesting inside an LSB structure must NOT throw InvalidByteOrderException.
        // Unannotated nested structures also inherit the builder direction rather than childByteOrder.

        // value=1: big-endian -> [00 01] inside the outer LSB structure; frame is sequential
        validateEncoding(
            ByteOrderExcludeStructureNestedInsideLsbParentAllowedFixturesOuterData(ByteOrderExcludeStructureNestedInsideLsbParentAllowedFixturesBigEndianFieldsData(1)),
            byteArrayOf(0x00, 0x01),
        )
    }

    @Test
    fun byteOrderExcludeStructureChildByteOrderPropagatesEnum() {
        // ON = ordinal 1, big-endian 2 bytes -> [00 01]
        validateEncoding(StatusFrameData(Status.ON), byteArrayOf(0x00, 0x01))
    }

    @Test
    fun reservedBytesCountedInStaticSize() {
        validateEncoding(Short3Data(0x0A), byteArrayOf(0x0A, 0x00, 0x00))
        validateEncoding(Long5Data(0x0B), byteArrayOf(0x0B, 0x00, 0x00, 0x00, 0x00))
    }

    @Test
    fun reservedBytesAfter() {
        validateEncoding(
            ReservedBytesAfterFixturesPacketData(0x01, 0x02),
            byteArrayOf(
                0x01, // a
                0x02, // b
                0x00,
                0x00, // 2 reserved bytes after b
            ),
        )
    }

    @Test
    fun reservedBytesBefore() {
        validateEncoding(
            ReservedBytesBeforeFixturesPacketData(0x01, 0x02),
            byteArrayOf(
                0x01, // a
                0x00,
                0x00, // 2 reserved bytes before b
                0x02, // b
            ),
        )
    }

    @Test
    fun reservedBytesBeforeAndAfter() {
        validateEncoding(
            ReservedBytesBeforeAndAfterFixturesPacketData(a = 0x1600, b = 0x0258),
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

    @Test
    fun reservedBytesDecodeIgnoresContent() {
        // On decode, non-zero reserved bytes are silently discarded - no exception.
        val serializer = ReservedBytesDecodeIgnoresContentFixturesPacketData.serializer()
        val bytes = byteArrayOf(
            0x0A, // a
            0xFF.toByte(), // reserved before b - garbage, should be ignored
            0x0B, // b
            0xDE.toByte(), // reserved after b - garbage, should be ignored
        )
        assertEquals(ReservedBytesDecodeIgnoresContentFixturesPacketData(0x0A, 0x0B), createBackend().decode(serializer, bytes))
    }

    @Test
    fun reservedBytesMultipleFields() {
        validateEncoding(
            ReservedBytesMultipleFieldsFixturesPacketData(x = 0x01, y = 0x02, z = 0x03),
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

    @Test
    fun byteOrderExcludeStructureUnannotatedNestedStructureInheritsBuilderDirection() {
        // a=0x0102 -> [01 02] first, b=0x0304 -> [03 04] second.  Both big-endian, sequential order.
        // If the unannotated PayloadData inherited MSB as byteOrder it would reverse: [03 04 01 02].
        validateRoundTrip(
            PayloadData(0x0102, 0x0304),
            FrameData.serializer(),
            byteArrayOf(0x01, 0x00, 0x04, 0x01, 0x02, 0x03, 0x04),
        )
    }

    @Test
    fun encodeWithChecksum() {
        validateEncoding(
            EncodeWithChecksumFixturesWithChecksumData(1234, "123456789"),
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

    @Test
    fun byteOrderExcludeStructureWithChecksumEmitsCrcBigEndian() {
        // Body (what CRC covers) = discriminator only = [0x12, 0x00, 0x00]
        val body = byteArrayOf(0x12, 0x00, 0x00)
        val crc = CRC16.compute(body)
        // CRC emitted big-endian (childByteOrder = MSB): high byte first
        val crcHi = ((crc shr 8) and 0xFFu).toByte()
        val crcLo = (crc and 0xFFu).toByte()

        val expected = byteArrayOf(0xAA.toByte(), 0x55.toByte()) + body + byteArrayOf(crcHi, crcLo, 0x55)
        // Must use BigEndianFrameData.serializer() so the sealed interface's @Prefix/@Checksum/@Postfix apply.
        validateRoundTrip(EmptyVariantData as BigEndianFrameData, BigEndianFrameData.serializer(), expected)
    }

    @Test
    fun encodeValueClassWithChecksumAndHeaderFooterField() {
        // A root value class is no layout; as the only property of one it encodes to the same bytes.
        val byteValue: Byte = 0x12
        val expectedChecksum = CRC16.compute(byteArrayOf(byteValue))
        val expectedBytes = buildByteArray {
            add(0x55.toByte())
            add(byteValue)
            add(expectedChecksum.toUShort())
            add(0xAA.toByte())
        }
        validateEncoding(ValueClassWithHeaderAndFooterFixturesHolderData(FramedValue(byteValue.toInt())), expectedBytes)
    }

    @Test
    fun valueClassWithHeaderFooterRejectsWrongPrefixField() {
        assertFailsWith<InvalidPrefix> {
            createBackend().decode(ValueClassWithHeaderAndFooterFixturesHolderData.serializer(), byteArrayOf(0x44, 0x12, 0x00, 0x00, 0xAA.toByte()))
        }
    }

    @Test
    fun valueClassWithHeaderFooterRejectsWrongPostfixField() {
        val byteValue: Byte = 0x12
        val crc = CRC16.compute(byteArrayOf(byteValue))
        assertFailsWith<InvalidPostfix> {
            createBackend().decode(
                ValueClassWithHeaderAndFooterFixturesHolderData.serializer(),
                buildByteArray {
                    add(0x55.toByte())
                    add(byteValue)
                    add(crc.toUShort())
                    add(0xBB.toByte())
                },
            )
        }
    }

    @Test
    fun valueClassWithHeaderFooterRejectsWrongChecksumField() {
        assertFailsWith<InvalidChecksumException> {
            createBackend().decode(ValueClassWithHeaderAndFooterFixturesHolderData.serializer(), byteArrayOf(0x55, 0x12, 0x00, 0x00, 0xAA.toByte()))
        }
    }

    @Test
    fun encodeWithChecksumMostSignificantFirst() {
        val value = EncodeWithChecksumMostSignificantFirstFixturesWithChecksumData(1234, "123456789")

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
        validateRoundTrip(value, EncodeWithChecksumMostSignificantFirstFixturesWithChecksumData.serializer(), expectedBytes)
    }

    @Test
    fun encodeNullTerminatedAndOverflowMostSignificantFirst() {
        val longString = MutableList(500) { "X" }.joinToString("")
        val value = EncodeNullTerminatedAndOverflowMostSignificantFirstFixturesContainerData(
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

        validateRoundTrip(value, EncodeNullTerminatedAndOverflowMostSignificantFirstFixturesContainerData.serializer(), expected)
    }

    @Test
    fun emptyNestedStructureAfterBodyBooleansRoundTrips() {
        validateRoundTrip(
            BooleansBeforeNestedStructureFixturesEmptyNestedAfterBooleansData(
                flagA = true,
                flagB = false,
                marker = BooleansBeforeNestedStructureFixturesEmptyMarkerData,
                trailer = 0x2A,
            ),
            BooleansBeforeNestedStructureFixturesEmptyNestedAfterBooleansData.serializer(),
        )
    }

    @Test
    fun nestedFlagHeaderAfterParentBooleansRoundTrips() {
        // The parent's partial Boolean byte is padded before the nested structure starts its own flag header.
        validateRoundTrip(
            BooleansBeforeNestedStructureFixturesParentBooleansThenNestedFlagHeaderData(
                boolA = true,
                boolB = false,
                nested = BooleansBeforeNestedStructureFixturesNestedFlagHeaderData(flag = true),
                trailer = 0x2A,
            ),
            BooleansBeforeNestedStructureFixturesParentBooleansThenNestedFlagHeaderData.serializer(),
            byteArrayOf(0x01, 0x01, 0x2A),
        )
    }

    @Test
    fun nestedLeadingBooleanAfterParentBooleansRoundTrips() {
        validateRoundTrip(
            BooleansBeforeNestedStructureFixturesParentBooleansThenNestedLeadingBooleanData(
                boolA = true,
                boolB = false,
                nested = BooleansBeforeNestedStructureFixturesNestedLeadingBooleanData(flag = true),
                trailer = 0x2A,
            ),
            BooleansBeforeNestedStructureFixturesParentBooleansThenNestedLeadingBooleanData.serializer(),
            byteArrayOf(0x01, 0x01, 0x2A),
        )
    }

    @Test
    fun flagPackedValueInFramedValueClassRoundTrips() {
        // A value class has no flag header of its own: the packed ordinal lives in the enclosing structure's header.
        validateRoundTrip(
            FramedValueClassFixturesFramedFlagHolderData(wrapped = FramedValueClassFixtures.FramedFlagValue(FramedValueClassFixtures.Mode3.HIGH), trailer = 0x2A),
            FramedValueClassFixturesFramedFlagHolderData.serializer(),
            byteArrayOf(0x02, 0x77, 0x88.toByte(), 0x2A),
        )
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
            UnsizedPayloadWithChecksumRoundTripsFixturesUnsizedWithChecksumData(payload = listOf(0x01, 0x02, 0x03)),
            UnsizedPayloadWithChecksumRoundTripsFixturesUnsizedWithChecksumData.serializer(),
            expectedBytes,
        )
    }

    @Test
    fun reservedAfterOnValueClassPropertyWritesPadding() {
        validateRoundTrip(
            ReservedAfterFixturesReservedAfterValueClassData(wrapped = ValueContainer(0x11), trailer = 0x22),
            ReservedAfterFixturesReservedAfterValueClassData.serializer(),
            byteArrayOf(0x11, 0x00, 0x00, 0x22),
        )
    }

    @Test
    fun reservedAfterOnUnsignedPropertyWritesPadding() {
        validateRoundTrip(
            ReservedAfterFixturesReservedAfterUnsignedData(v = 0x11u, trailer = 0x22),
            ReservedAfterFixturesReservedAfterUnsignedData.serializer(),
            byteArrayOf(0x11, 0x00, 0x00, 0x22),
        )
    }
}

class BytesFormatFramingTest : FramingSuite(BytesFormatBackend.Factory) {

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
            createBackend().decode(
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
            createBackend().decode(
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
            createBackend().decode(
                ValueContainerWithHeaderAndFooter.serializer(),
                byteArrayOf(0x55, 0x12, 0x00, 0x00, 0xAA.toByte()), // 0x0000 is not CRC16(0x12)
            )
        }
    }

    // ── @ByteOrder(excludeStructure = true) ──────────────────────────────────

    @Test
    fun byteOrderExcludeStructureDoesNotChangeStringCheck() {
        // For Strings, childByteOrder still governs char encoding - changing it is still disallowed
        // inside a structure whose parent has a different childByteOrder.
        assertFailsWith<InvalidByteOrderException> {
            createBackend().encode(
                ByteOrderExcludeStructureDoesNotChangeStringCheckFixtures.InvalidStringByteOrder.serializer(),
                ByteOrderExcludeStructureDoesNotChangeStringCheckFixtures.InvalidStringByteOrder("fail"),
            )
        }
    }

    @Test
    fun valueClassWrappingNullableRoundTrips() {
        validateRoundTrip(
            FramedValueClassFixturesNullableWrappedHolderData(wrapped = FramedValueClassFixtures.NullableWrapped(0x11), trailer = 0x22),
            FramedValueClassFixturesNullableWrappedHolderData.serializer(),
        )
        validateRoundTrip(
            FramedValueClassFixturesNullableWrappedHolderData(wrapped = FramedValueClassFixtures.NullableWrapped(null), trailer = 0x22),
            FramedValueClassFixturesNullableWrappedHolderData.serializer(),
        )
    }

    @Test
    fun reservedAfterOnNullablePropertySkipsPaddingWhenNull() {
        // The padding belongs to the property's slot, so an absent property has none.
        validateRoundTrip(
            ReservedAfterFixturesReservedAfterNullableData(v = null, trailer = 0x22),
            ReservedAfterFixturesReservedAfterNullableData.serializer(),
            byteArrayOf(0x00, 0x22),
        )
    }

    @Test
    fun wideSizeSelectionOnFramedValueClassPropertyRoundTrips() {
        validateRoundTrip(
            FramedValueClassFixturesFramedWideSizeHolderData(wrapped = FramedValueClassFixtures.FramedWideSizeValue(5L), trailer = 0x2A),
            FramedValueClassFixturesFramedWideSizeHolderData.serializer(),
        )
        validateRoundTrip(
            FramedValueClassFixturesFramedWideSizeHolderData(wrapped = FramedValueClassFixtures.FramedWideSizeValue(1L shl 40), trailer = 0x2A),
            FramedValueClassFixturesFramedWideSizeHolderData.serializer(),
        )
    }

    @Test
    fun reservedAfterOnNullablePropertyWritesPaddingWhenPresent() {
        validateRoundTrip(
            ReservedAfterFixturesReservedAfterNullableData(v = 0x11, trailer = 0x22),
            ReservedAfterFixturesReservedAfterNullableData.serializer(),
            byteArrayOf(0x01, 0x11, 0x00, 0x00, 0x22),
        )
    }

    @Test
    fun framedValueClassWrappingNullableRoundTrips() {
        // The frame stays on the wire when the wrapped value is absent; only the body bytes drop out.
        validateRoundTrip(
            FramedValueClassFixturesNullableWrappedFramedHolderData(wrapped = FramedValueClassFixtures.NullableWrappedFramed(0x11), trailer = 0x22),
            FramedValueClassFixturesNullableWrappedFramedHolderData.serializer(),
        )
        validateRoundTrip(
            FramedValueClassFixturesNullableWrappedFramedHolderData(wrapped = FramedValueClassFixtures.NullableWrappedFramed(null), trailer = 0x22),
            FramedValueClassFixturesNullableWrappedFramedHolderData.serializer(),
            byteArrayOf(0x00, 0x55, 0xAA.toByte(), 0x22),
        )
    }
}

class ViewFramingTest : FramingSuite(ViewBackend.Factory)
