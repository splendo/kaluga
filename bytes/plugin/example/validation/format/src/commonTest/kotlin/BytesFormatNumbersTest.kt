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
import com.splendo.kaluga.bytes.medfloat.MedFloat16
import com.splendo.kaluga.bytes.medfloat.MedFloat32
import com.splendo.kaluga.bytes.integer.UInt24
import com.splendo.kaluga.bytes.buildByteArray
import com.splendo.kaluga.bytes.layout.ByteArrayEndedBeforeSerializationCompleted
import com.splendo.kaluga.bytes.serialization.BytesFormat
import com.splendo.kaluga.bytes.toByteArray
import com.splendo.kaluga.bytes.toHexString
import com.splendo.kaluga.bytes.integer.toInt24
import com.splendo.kaluga.bytes.integer.toInt40
import com.splendo.kaluga.bytes.integer.toInt48
import com.splendo.kaluga.bytes.integer.toUInt40
import com.splendo.kaluga.bytes.integer.toUInt48
import com.splendo.kaluga.bytes.integer.add
import com.splendo.kaluga.bytes.medfloat.add
import com.splendo.kaluga.bytes.strings.add
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Joule
import kotlin.math.pow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

/**
 * The number tests every backend runs; [BytesFormatNumbersTest] adds the ones the views do not support yet.
 */
abstract class NumbersSuite(backendFactory: ByteLayoutBackendFactory) : BytesFormatTestBase(backendFactory) {

    @Test
    fun encodeScientificValueClassField() {
        // A root value class is no layout; as the only property of one it encodes to the same bytes.
        validateEncoding(EncodeScientificValueClassFieldFixturesReadingData(EnergyExpended(500)), byteArrayOf(0xF4.toByte(), 0x01))
    }

    @Test
    fun encodeNumericInFlags() {
        // nibble = 5 -> bits 0..3 = 0b0101; wide = 0x123 -> bits 4..15. Two flag bytes: byte0 = 0x35, byte1 = 0x12.
        validateEncoding(EncodeNumericInFlagsFixturesPackedData(nibble = 5, wide = 0x123), byteArrayOf(0x35, 0x12))

        validateEncoding(EncodeNumericInFlagsFixturesNibbleData(0xA), byteArrayOf(0x0A))

        validateEncoding(EncodeNumericInFlagsFixturesSignedData(-3), byteArrayOf(0x0D))
        validateEncoding(EncodeNumericInFlagsFixturesSignedData(7), byteArrayOf(0x07))
    }

    @Test
    fun rejectsNumericTooLargeForFlagWidth() {
        // 16 needs 5 bits; an unsigned 4-bit flag field holds 0..15, so encoding must fail rather than truncate.
        assertFailsWith<IllegalArgumentException> {
            createBackend().encode(RejectsNumericTooLargeForFlagWidthFixturesTooBigData.serializer(), RejectsNumericTooLargeForFlagWidthFixturesTooBigData(16))
        }
    }

    @Test
    fun encodeRange() {
        validateEncoding(EncodeRangeFixturesEightBitData(0.0), buildByteArray { add(0x00.toByte()) })
        validateEncoding(EncodeRangeFixturesEightBitData(100.0), buildByteArray { add(0xFF.toByte()) })

        // Intermediate: round((75 / 100) x 255) = round(191.25) = 191
        val encoded8 = createBackend().encode(EncodeRangeFixturesEightBitData.serializer(), EncodeRangeFixturesEightBitData(75.0))
        assertEquals(191.toByte(), encoded8[0])
        // Round-trip: 191 / 255 x 100 ~= 74.9%
        val decoded8 = createBackend().decode(EncodeRangeFixturesEightBitData.serializer(), encoded8)
        assertTrue(decoded8.percent in 74.9..75.1, "expected ~75 %, got ${decoded8.percent}")

        validateEncoding(EncodeRangeFixturesSixteenBitData(0.0), buildByteArray { add(uShort = 0u) })
        validateEncoding(EncodeRangeFixturesSixteenBitData(100.0), buildByteArray { add(uShort = 65535u) })

        validateEncoding(
            EncodeRangeFixturesWithHeaderData(0x42, 1.0),
            buildByteArray {
                add(0x42.toByte())
                add(0xFF.toByte())
            },
        )

        validateEncoding(EncodeRangeFixturesTemperatureData(-40.0), buildByteArray { add(0x00.toByte()) })
        validateEncoding(EncodeRangeFixturesTemperatureData(85.0), buildByteArray { add(0xFF.toByte()) })
    }

    @Test
    fun encodeBoolean() {
        validateEncoding(EncodeBooleanFixturesContainerData(false, flagValue = true, nullableValue = null, ValueContainer(true), null), byteArrayOf(0b10001))
        validateEncoding(EncodeBooleanFixturesContainerData(true, flagValue = false, nullableValue = false, ValueContainer(false), ValueContainer(true)), byteArrayOf(0b1001110))
        validateEncoding(EncodeBooleanFixturesContainerData(true, flagValue = true, nullableValue = true, ValueContainer(true), ValueContainer(true)), byteArrayOf(0b1111111))
    }

    @Test
    fun encodeEnumInFlags() {
        // 3 cases -> ceil(log2(3)) = 2 flag bits, ordinal stored LSB-first at index 0.
        validateEncoding(EncodeEnumInFlagsFixturesOnlyContactData(SensorContact.NotSupported), byteArrayOf(0b00))
        validateEncoding(EncodeEnumInFlagsFixturesOnlyContactData(SensorContact.NotDetected), byteArrayOf(0b01))
        validateEncoding(EncodeEnumInFlagsFixturesOnlyContactData(SensorContact.Detected), byteArrayOf(0b10))

        validateEncoding(EncodeEnumInFlagsFixturesReadingData(SensorContact.Detected, 42), byteArrayOf(0b10, 42))

        validateEncoding(EncodeEnumInFlagsFixturesOptionalContactData(null), byteArrayOf(0b000))
        validateEncoding(EncodeEnumInFlagsFixturesOptionalContactData(SensorContact.NotSupported), byteArrayOf(0b001))
        validateEncoding(EncodeEnumInFlagsFixturesOptionalContactData(SensorContact.Detected), byteArrayOf(0b101))
    }

    @Test
    fun encodeByte() {
        validateEncoding(
            EncodeByteFixturesContainerData(
                defaultLength = 42,
                `8bit` = 23,
                `16bit` = 250.toUByte(),
                unsigned = 123u,
                nullable = null,
                nullableUnsigned = null,
                inlineValue = ValueContainer(4),
                nullableInlineValue = ValueContainer(10),
                inlineUnsignedValue = ValueContainer(23u),
                nullableInlineUnsignedValue = null,
            ),
            buildByteArray {
                add(false)
                add(false)
                add(true) // nullableInlineValue
                add(false) // nullableInlineUnsignedValue
                add(byte = 42)
                add(byte = 23)
                add(short = 250)
                add(byte = 123)
                add(byte = 4)
                add(byte = 10)
                add(uByte = 23u)
            },
        )

        validateEncoding(
            EncodeByteFixturesContainerData(
                defaultLength = -80,
                `8bit` = -45,
                `16bit` = 230.toUByte(),
                unsigned = 24u,
                nullable = -20,
                nullableUnsigned = 10u,
                inlineValue = ValueContainer(-8),
                nullableInlineValue = null,
                inlineUnsignedValue = ValueContainer(99u),
                nullableInlineUnsignedValue = ValueContainer(77u),
            ),
            buildByteArray {
                add(true)
                add(true)
                add(false)
                add(true)
                add(byte = -80)
                add(byte = -45)
                add(uShort = 230u)
                add(uByte = 24u)
                add(byte = -20)
                add(uByte = 10u)
                add(byte = -8)
                add(uByte = 99u)
                add(uByte = 77u)
            },
        )
    }

    @Test
    fun encodeShort() {
        validateEncoding(
            EncodeShortFixturesContainerData(
                defaultLength = 42,
                `8bit` = 112,
                `16bit` = 512,
                `24bit` = 16777.toUShort(),
                scalar = 500,
                medFloat16 = 123,
                variableSizeRange = 567,
                unsigned = 1234.toUShort(),
                mostSignificant = 567,
                nullable = null,
                nullableVariableSizing = null,
                inlineValue = NumberValueContainer(60),
                nullableInlineValue = NumberValueContainer(70),
                inlineUnsignedValue = NumberValueContainer(600u),
                nullableInlineUnsignedValue = null,
            ),
            buildByteArray {
                add(true) // variableSizeRange flag
                add(false) // nullable flag
                add(false) // nullableVariableSizing flag
                add(false)
                add(false) // inlineValue sizing
                add(true) // nullableInlineValue
                add(false)
                add(true) // inlineUnsignedValue
                add(false) // nullableInlineUnsignedValue
                add(false)
                add(short = 42)
                add(byte = 112)
                add(short = 512)
                add(int24 = 16777.toInt24())
                add(byte = 5)
                add(MedFloat16(123.0))
                add(short = 567)
                add(short = 1234)
                add(short = 567, order = ByteOrder.MOST_SIGNIFICANT_FIRST)
                add(byte = 60)
                add(byte = 70)
                add(uShort = 600u)
            },
        )

        validateEncoding(
            EncodeShortFixturesContainerData(
                defaultLength = -80,
                `8bit` = -37,
                `16bit` = -8,
                `24bit` = 16.toUShort(),
                scalar = -700,
                medFloat16 = -23,
                variableSizeRange = -30,
                unsigned = 2345.toUShort(),
                mostSignificant = -123,
                nullable = -34,
                nullableVariableSizing = -200,
                inlineValue = NumberValueContainer(-300),
                nullableInlineValue = null,
                inlineUnsignedValue = NumberValueContainer(5u),
                nullableInlineUnsignedValue = NumberValueContainer(6u),
            ),
            buildByteArray {
                add(false) // variableSizeRange flag
                add(true) // nullable flag
                add(true) // nullableVariableSizing flag
                add(true)
                add(true) // inlineValue sizing
                add(false) // nullableInlineValue
                add(false)
                add(false) // inlineUnsignedValue
                add(true) // nullableInlineUnsignedValue
                add(false)
                add(short = -80)
                add(byte = -37)
                add(short = -8)
                add(int24 = 16.toInt24())
                add(byte = -7)
                add(MedFloat16(-23.0))
                add(byte = -30)
                add(uShort = 2345u)
                add(short = -123, order = ByteOrder.MOST_SIGNIFICANT_FIRST)
                add(short = -34)
                add(short = -200)
                add(short = -300)
                add(uByte = 5u)
                add(uByte = 6u)
            },
        )
    }

    @Test
    fun encodeInteger() {
        validateEncoding(
            EncodeIntegerFixturesContainerData(
                defaultLength = 42,
                `8bit` = 112,
                `16bit` = 512,
                `24bit` = 16777,
                `32bit` = 0x7ABCDEFF,
                `64bit` = UInt.MAX_VALUE - 12u,
                scalar = 500,
                medFloat16 = 123,
                medFloat32 = 1234567,
                variableSizeLowRange = 123,
                variableSizeHighRange = 1234567,
                unsigned = 0x12345678u,
                mostSignificant = 0x12345678,
                nullable = null,
                nullableVariableSizing = null,
                inlineValue = NumberValueContainer(800),
                nullableInlineValue = NumberValueContainer(900),
                inlineUnsignedValue = NumberValueContainer(2345u),
                nullableInlineUnsignedValue = null,
            ),
            buildByteArray {
                add(false) // variableSizeLowRange flags
                add(true) // variableSizeHighRange flags
                add(false)
                add(false) // Nullable
                add(false) // nullableVariableSizing
                add(false)
                add(false)
                add(true) // inlineValue sizing
                add(false)
                add(true) // nullableInlineValue
                add(true)
                add(false)
                add(true) // inlineUnsignedValue
                add(false)
                add(false)
                add(false)
                add(int = 42)
                add(byte = 112)
                add(short = 512)
                add(16777.toInt24())
                add(0x7ABCDEFF)
                add((UInt.MAX_VALUE - 12u).toULong())
                add(byte = 5)
                add(MedFloat16(123.0))
                add(MedFloat32(1234567.0))
                add(short = 123)
                add(1234567.toInt24())
                add(0x12345678u)
                add(0x12345678u, ByteOrder.MOST_SIGNIFICANT_FIRST)
                add(short = 800)
                add(short = 900)
                add(uShort = 2345u)
            },
        )
        validateEncoding(
            EncodeIntegerFixturesContainerData(
                defaultLength = -80,
                `8bit` = -20,
                `16bit` = -5,
                `24bit` = -33,
                `32bit` = -2000,
                `64bit` = 1234567u,
                scalar = -700,
                medFloat16 = -45,
                medFloat32 = -78,
                variableSizeLowRange = -2000000,
                variableSizeHighRange = -9000000,
                unsigned = 56u,
                mostSignificant = -67,
                nullable = -78,
                nullableVariableSizing = -10000000,
                inlineValue = NumberValueContainer(-15000000),
                nullableInlineValue = null,
                inlineUnsignedValue = NumberValueContainer(250u),
                nullableInlineUnsignedValue = NumberValueContainer(8000000u),
            ),
            buildByteArray {
                add(true) // variableSizeLowRange flags
                add(false) // variableSizeHighRange flags
                add(true)
                add(true) // Nullable
                add(true) // nullableVariableSizing
                add(true)
                add(true)
                add(true) // inlineValue sizing
                add(true)
                add(false) // nullableInlineValue
                add(false)
                add(false)
                add(false) // inlineUnsignedValue sizing
                add(true) // nullableInlineUnsignedValue
                add(false)
                add(true)
                add(int = -80)
                add(byte = -20)
                add(short = -5)
                add((-33).toInt24())
                add(-2000)
                add(1234567.toULong())
                add(byte = -7)
                add(MedFloat16(-45.0))
                add(MedFloat32(-78.0))
                add(-2000000)
                add(-9000000)
                add(56u)
                add(-67, ByteOrder.MOST_SIGNIFICANT_FIRST)
                add(-78)
                add(-10000000)
                add(-15000000)
                add(uByte = 250u)
                add(uInt24 = UInt24(8000000u))
            },
        )
    }

    @Test
    fun encodeLong() {
        validateEncoding(
            EncodeLongFixturesContainerData(
                defaultLength = 42,
                `8bit` = 112,
                `16bit` = 512,
                `24bit` = 16777,
                `32bit` = 0x7ABCDEFF,
                `64bit` = (UInt.MAX_VALUE - 12u).toLong(),
                scalar = 500,
                medFloat16 = 123,
                medFloat32 = 1234567,
                variableSizeLowRange = 123,
                variableSizeHighRange = 1234567,
                variableFullRange = 1234567890123456789,
                variableMedFloat = 30000000000,
                unsigned = 0x12345678u,
                mostSignificant = 0x12345678,
                nullable = null,
                nullableVariableSizing = null,
                inlineValue = ValueContainer(800),
                nullableInlineValue = ValueContainer(3000000000),
                unsignedInlineValue = ValueContainer(2345u),
                nullableUnsignedInlineValue = null,
            ),
            buildByteArray {
                add(false) // variableSizeLowRange flag
                add(true) // variableSizeHighRange flags
                add(false)
                add(false) // variableFullRange flags
                add(false)
                add(true)
                add(true) // variableMedFloat
                add(false) // nullable
                add(false) // nullableVariableSizing
                add(false)
                add(false)
                add(false) // inlineValue
                add(true) // nullableInlineValue
                add(true)
                add(false)
                add(long = 42)
                add(byte = 112)
                add(short = 512)
                add(16777.toInt24())
                add(0x7ABCDEFF)
                add((UInt.MAX_VALUE - 12u).toLong())
                add(byte = 5)
                add(MedFloat16(123.0))
                add(MedFloat32(1234567.0))
                add(short = 123)
                add(1234567.toInt24())
                add(1234567890123456789)
                add(MedFloat32(30000000000.0))
                add(uLong = 0x12345678u)
                add(long = 0x12345678, ByteOrder.MOST_SIGNIFICANT_FIRST)
                add(800)
                add(3000000000L)
                add(uLong = 2345u)
            },
        )

        validateEncoding(
            EncodeLongFixturesContainerData(
                defaultLength = -80,
                `8bit` = -20,
                `16bit` = -3,
                `24bit` = -19,
                `32bit` = -21,
                `64bit` = -23,
                scalar = -700,
                medFloat16 = -45,
                medFloat32 = -56,
                variableSizeLowRange = -2000000,
                variableSizeHighRange = -9000000,
                variableFullRange = -2,
                variableMedFloat = -7000,
                unsigned = 77u,
                mostSignificant = -4,
                nullable = -22,
                nullableVariableSizing = -7000000,
                inlineValue = ValueContainer(-8000000000),
                nullableInlineValue = null,
                unsignedInlineValue = ValueContainer(1234567890123u),
                nullableUnsignedInlineValue = ValueContainer(1234567890123456789u),
            ),
            buildByteArray {
                add(true) // variableSizeLowRange flag
                add(false) // variableSizeHighRange flags
                add(true)
                add(false) // variableFullRange flags
                add(false)
                add(false)
                add(false) // variableMedFloat
                add(true) // nullable
                add(true) // nullableVariableSizing
                add(true)
                add(false)
                add(true) // inlineValue
                add(false) // nullableInlineValue
                add(false)
                add(true) // nullableUnsignedInlineValue
                add(long = -80)
                add(byte = -20)
                add(short = -3)
                add((-19).toInt24())
                add(-21)
                add(-23L)
                add(byte = -7)
                add(MedFloat16(-45.0))
                add(MedFloat32(-56.0))
                add(int = -2000000)
                add(-9000000)
                add(byte = -2)
                add(MedFloat16(-7000.0))
                add(uLong = 77u)
                add(long = -4, ByteOrder.MOST_SIGNIFICANT_FIRST)
                add(long = -22)
                add((-7000000).toInt24())
                add(-8000000000L)
                add(1234567890123u)
                add(1234567890123456789u)
            },
        )
    }

    @Test
    fun encode40And48Bit() {
        // variableSizing 0x123456789A overflows 32 bits, so the flexible sizing picks 40-bit (index 1 of {32,40,48}).
        validateEncoding(
            Encode40And48BitFixturesContainerData(
                `40bit` = 0x123456789A,
                `48bit` = -42,
                unsigned40 = 0xFFFFFFFFFFuL,
                unsigned48 = 0xAABBCCDDEEFFuL,
                variableSizing = 0x123456789A,
            ),
            buildByteArray {
                add(true) // variableSizing length bits (0b01 -> 40-bit)
                add(false)
                add(0x123456789A.toInt40())
                add((-42L).toInt48())
                add(0xFFFFFFFFFFuL.toUInt40())
                add(0xAABBCCDDEEFFuL.toUInt48())
                add(0x123456789A.toInt40())
            },
        )

        // negative values exercise sign extension; variableSizing -2 now fits 32-bit (index 0 -> 0b00).
        validateEncoding(
            Encode40And48BitFixturesContainerData(
                `40bit` = -549755813888, // Int40.MIN_VALUE
                `48bit` = 140737488355327, // Int48.MAX_VALUE
                unsigned40 = 42uL,
                unsigned48 = 0uL,
                variableSizing = -2,
            ),
            buildByteArray {
                add(false) // variableSizing length bits (0b00 -> 32-bit)
                add(false)
                add((-549755813888L).toInt40())
                add(140737488355327L.toInt48())
                add(42uL.toUInt40())
                add(0uL.toUInt48())
                add(int = -2)
            },
        )
    }

    @Test
    fun encodePresentWhenAllSet() {
        // a, b present -> bits 0 and 1 set (0b11), so c is present too
        validateEncoding(EncodePresentWhenAllSetFixturesContainerData(a = 10, b = 20, c = 30), byteArrayOf(0b11, 10, 20, 30))
        // a absent -> bit 0 clear, so c is necessarily absent (only b present)
        validateEncoding(EncodePresentWhenAllSetFixturesContainerData(a = null, b = 20, c = null), byteArrayOf(0b10, 20))
        // b absent -> bit 1 clear, so c is necessarily absent (only a present)
        validateEncoding(EncodePresentWhenAllSetFixturesContainerData(a = 10, b = null, c = null), byteArrayOf(0b01, 10))
        // nothing present
        validateEncoding(EncodePresentWhenAllSetFixturesContainerData(a = null, b = null, c = null), byteArrayOf(0b00))
    }

    @Test
    fun encodePresentWhenAllSetIsLossyWhenInconsistentWithItsFlags() {
        // `c` owns no flag bit: its presence is *derived* from bits 0 and 1, so a value whose `c` disagrees with those
        // bits cannot round-trip. These are still valid instances; we just pin the (lossy) wire behaviour.

        // c set while its gating bits are clear: c's byte is written, but on decode bits 0/1 are clear so c reads back
        // as null - the value is silently lost (and its stray body byte is ignored).
        val cWithoutGate = EncodePresentWhenAllSetIsLossyWhenInconsistentWithItsFlagsFixturesContainerData(a = null, b = null, c = 30)
        val encoded = createBackend().encode(EncodePresentWhenAllSetIsLossyWhenInconsistentWithItsFlagsFixturesContainerData.serializer(), cWithoutGate)
        assertTrue(encoded.contentEquals(byteArrayOf(0b00, 30)), encoded.toHexString(separator = " "))
        assertEquals(
            EncodePresentWhenAllSetIsLossyWhenInconsistentWithItsFlagsFixturesContainerData(a = null, b = null, c = null),
            createBackend().decode(EncodePresentWhenAllSetIsLossyWhenInconsistentWithItsFlagsFixturesContainerData.serializer(), encoded),
        )

        // c null while its gating bits are set: decode expects c's byte but none was written, so decoding fails outright.
        val gateWithoutC = EncodePresentWhenAllSetIsLossyWhenInconsistentWithItsFlagsFixturesContainerData(a = 10, b = 20, c = null)
        val truncated = createBackend().encode(EncodePresentWhenAllSetIsLossyWhenInconsistentWithItsFlagsFixturesContainerData.serializer(), gateWithoutC)
        assertTrue(truncated.contentEquals(byteArrayOf(0b11, 10, 20)), truncated.toHexString(separator = " "))
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> {
            createBackend().decode(EncodePresentWhenAllSetIsLossyWhenInconsistentWithItsFlagsFixturesContainerData.serializer(), truncated)
        }
    }

    @Test
    fun encodeFloat() {
        validateEncoding(
            EncodeFloatFixturesContainerData(
                1234.56f.as32Bit(),
                567.89f.as32Bit(),
                0.125f,
                ((3.0 - 10.0) / (3 * 10.0.pow(-5) * 2.0.pow(2))).toFloat(),
                0.025f,
                123.0f,
                1234567.0f,
                null,
            ),
            buildByteArray {
                add(true) // flexibleScalar flag
                add(false) // nullable flag
                add(1234.56f)
                add(567.89f)
                add(0.125f.toDouble())
                add(byte = 3)
                add(short = 250)
                add(MedFloat16(123.0))
                add(MedFloat32(1234567.0))
            },
        )

        validateEncoding(
            EncodeFloatFixturesContainerData(
                (-12.34f).as32Bit(),
                (-234.56f).as32Bit(),
                8.0f,
                ((6.0 - 10.0) / (3 * 10.0.pow(-5) * 2.0.pow(2))).toFloat(),
                0.0024f,
                10.0f,
                0.5f,
                0.01f.as32Bit(),
            ),
            buildByteArray {
                add(false) // flexibleScalar flag
                add(true) // nullable flag
                add(-12.34f)
                add(-234.56f)
                add(8.0)
                add(byte = 6)
                add(byte = 24)
                add(MedFloat16(10.0))
                add(MedFloat32(0.5))
                add(0.01f)
            },
        )
    }

    @Test
    fun encodeDouble() {
        validateEncoding(
            EncodeDoubleFixturesContainerData(
                1234.56,
                567.9f.as32Bit().toDouble(),
                0.124,
                variableSizing = 800.0,
                ((3.0 - 50.0) / (5 * 10.0.pow(2) * 2.0.pow(3))),
                0.025,
                123.0,
                1234567.0,
                0.123,
                null,
            ),
            buildByteArray {
                add(false) // variableSizing
                add(true) // flexibleScalar flags
                add(false) // flexibleMedFloat
                add(false) // nullable flag
                add(1234.56)
                add(567.9f)
                add(0.124)
                add(800.0f)
                add(byte = 3)
                add(short = 250)
                add(MedFloat16(123.0))
                add(MedFloat32(1234567.0))
                add(MedFloat16(0.123))
            },
        )

        validateEncoding(
            EncodeDoubleFixturesContainerData(
                -12.34,
                (-234.56f).as32Bit().toDouble(),
                8.0,
                7e-100,
                ((6.0 - 50.0) / (5 * 10.0.pow(2) * 2.0.pow(3))),
                0.0025,
                10.0,
                0.5,
                -8e-10,
                0.01,
            ),
            buildByteArray {
                add(true) // variableSizing)
                add(false) // flexibleScalar flag
                add(true) // flexibleMedFloat
                add(true) // nullable flag
                add(-12.34)
                add(-234.56f)
                add(8.0)
                add(7e-100)
                add(byte = 6)
                add(byte = 25)
                add(MedFloat16(10.0))
                add(MedFloat32(0.5))
                add(MedFloat32(-8e-10))
                add(0.01)
            },
        )
    }

    @Test
    fun encodeChar() {
        validateEncoding(
            EncodeCharFixturesContainerData(
                'a',
                'c',
                'd',
                'e',
                'f',
                'g',
            ),
            buildByteArray {
                add(true) // nullable flag
                add(true) // nullableUTF16
                add('a', Encoding.ASCII)
                add('c', Encoding.UTF_16)
                add('d', Encoding.ASCII)
                add('e', Encoding.ASCII)
                add('f', Encoding.UTF_16, ByteOrder.MOST_SIGNIFICANT_FIRST)
                add('g', Encoding.UTF_16)
            },
        )
    }

    @Test
    fun longEightAndSixtyFourBitSelectionRoundTrips() {
        validateRoundTrip(LongSelectionFixturesLongWideSelection8BitData(v = 1L shl 32), LongSelectionFixturesLongWideSelection8BitData.serializer())
    }

    @Test
    fun longSixteenAndSixtyFourBitSelectionRoundTrips() {
        validateRoundTrip(LongSelectionFixturesLongWideSelection16BitData(v = 1L shl 32), LongSelectionFixturesLongWideSelection16BitData.serializer())
    }

    @Test
    fun longNegativeOutOfIntRangeSelectionRoundTrips() {
        validateRoundTrip(LongSelectionFixturesLongWideSelection8BitData(v = -(1L shl 40)), LongSelectionFixturesLongWideSelection8BitData.serializer())
    }

    // A nullable number differs from its non-null declaration only by the presence flag, so the value bytes
    // that follow must be identical; in particular the widened value must not be sign-extended.
    private fun <T, U> assertNullableMatchesNonNull(nonNull: T, nonNullSerializer: KSerializer<T>, nullable: U, nullableSerializer: KSerializer<U>) {
        val backend = createBackend()
        val nonNullEncoded = backend.encode(nonNullSerializer, nonNull)
        val nullableEncoded = backend.encode(nullableSerializer, nullable)
        val tail = nullableEncoded.copyOfRange(nullableEncoded.size - nonNullEncoded.size, nullableEncoded.size)
        assertTrue(
            tail.contentEquals(nonNullEncoded),
            "non-null=${nonNullEncoded.toHexString(separator = " ")} nullable=${nullableEncoded.toHexString(separator = " ")}",
        )
        assertEquals(nullable, backend.decode(nullableSerializer, nullableEncoded))
    }

    @Test
    fun nullableSizedUByteMatchesNonNullWireBytes() = assertNullableMatchesNonNull(
        NullableUnsignedFixturesNullableSizedUByteNonNullData(b = 200u, trailer = 0x2A),
        NullableUnsignedFixturesNullableSizedUByteNonNullData.serializer(),
        NullableUnsignedFixturesNullableSizedUByteData(b = 200u, trailer = 0x2A),
        NullableUnsignedFixturesNullableSizedUByteData.serializer(),
    )

    @Test
    fun nullableWideUIntMatchesNonNullWireBytes() = assertNullableMatchesNonNull(
        NullableUnsignedFixturesNullableWideUIntNonNullData(v = 3_000_000_000u, trailer = 0x2A),
        NullableUnsignedFixturesNullableWideUIntNonNullData.serializer(),
        NullableUnsignedFixturesNullableWideUIntData(v = 3_000_000_000u, trailer = 0x2A),
        NullableUnsignedFixturesNullableWideUIntData.serializer(),
    )

    @Test
    fun nullableWideUIntListItemMatchesNonNullWireBytes() = assertNullableMatchesNonNull(
        NullableUnsignedFixturesNullableWideUIntListNonNullData(items = listOf(3_000_000_000u), trailer = 0x2A),
        NullableUnsignedFixturesNullableWideUIntListNonNullData.serializer(),
        NullableUnsignedFixturesNullableWideUIntListData(items = listOf(3_000_000_000u), trailer = 0x2A),
        NullableUnsignedFixturesNullableWideUIntListData.serializer(),
    )

    @Test
    fun presentWhenAllSetReferencingUnownedBitRoundTrips() {
        // Bit 10 is owned by no field, so it is never set and `b` is always absent; the header still spans it.
        validateEncoding(
            PresentWhenAllSetReferencingUnownedBitRoundTripsFixturesUnownedGateContainerData(a = 5, b = null),
            PresentWhenAllSetReferencingUnownedBitRoundTripsFixturesUnownedGateContainerData.serializer(),
            byteArrayOf(0x01, 0x00, 0x05),
        )
    }

    @Test
    fun msbExcludeStructureFloatsUseBigEndianWireBytes() {
        val value = MsbExcludeStructureFloatsUseBigEndianWireBytesFixturesMsbFloatFrameData(a = 1.5f, b = 2.5)
        val expected = byteArrayOf(0x3F, 0xC0.toByte(), 0x00, 0x00, 0x40, 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)
        val backend = createBackend()
        val encoded = backend.encode(MsbExcludeStructureFloatsUseBigEndianWireBytesFixturesMsbFloatFrameData.serializer(), value)
        assertTrue(encoded.contentEquals(expected), "Expected ${expected.toHexString(separator = " ")} but got ${encoded.toHexString(separator = " ")}")
        val decoded = backend.decode(MsbExcludeStructureFloatsUseBigEndianWireBytesFixturesMsbFloatFrameData.serializer(), encoded)
        assertEquals(value.a.as32Bit(), decoded.a)
        assertEquals(value.b, decoded.b)
    }
}

class BytesFormatNumbersTest : NumbersSuite(BytesFormatBackend.Factory) {

    @Test
    fun encodeBooleanRoot() {
        // A root primitive is no layout, so only BytesFormat encodes one.
        validateEncoding(true, byteArrayOf(0x01))
        validateEncoding(false, byteArrayOf(0x00))
    }

    @Test
    fun encodeByteRoot() {
        validateEncoding(42.toByte(), byteArrayOf(42.toByte()))
    }

    @Test
    fun encodeShortRoot() {
        validateEncoding(42.toShort(), 42.toShort().toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST))
    }

    @Test
    fun encodeIntegerRoot() {
        validateEncoding(42, 42.toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST))
    }

    @Test
    fun encodeLongRoot() {
        validateEncoding(42L, 42L.toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST))
    }

    @Test
    fun encodeFloatRoot() {
        validateEncoding(42.0f, 42.0f.toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST))
    }

    @Test
    fun encodeDoubleRoot() {
        validateEncoding(42.0, 42.0.toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST))
    }

    @Test
    fun encodeCharRoot() {
        validateEncoding('a', buildByteArray { add(char = 'a') })
    }

    @Test
    fun encodeScientificValueClass() {
        // round-trips through BytesFormat as a 16-bit unsigned little-endian value...
        validateRoundTrip(EnergyExpended(500), EnergyExpended.serializer(), byteArrayOf(0xF4.toByte(), 0x01))
        // ...while still being a usable ScientificValue
        assertEquals(500(Joule).compareTo(EnergyExpended(500)), 0, EnergyExpended(500).toString())
    }
}

class ViewNumbersTest : NumbersSuite(ViewBackend.Factory)
