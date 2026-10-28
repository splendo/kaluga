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
import com.splendo.kaluga.bytes.medfloat.add
import com.splendo.kaluga.bytes.integer.add
import com.splendo.kaluga.bytes.ByteOrder
import com.splendo.kaluga.bytes.medfloat.MedFloat16
import com.splendo.kaluga.bytes.medfloat.MedFloat32
import com.splendo.kaluga.bytes.integer.UInt24
import com.splendo.kaluga.bytes.buildByteArray
import com.splendo.kaluga.bytes.toByteArray
import com.splendo.kaluga.bytes.toHexString
import com.splendo.kaluga.bytes.integer.toInt24
import com.splendo.kaluga.bytes.integer.toInt40
import com.splendo.kaluga.bytes.integer.toInt48
import com.splendo.kaluga.bytes.integer.toUInt40
import com.splendo.kaluga.bytes.integer.toUInt48
import com.splendo.kaluga.bytes.annotations.FlagIndex
import com.splendo.kaluga.bytes.annotations.FlagWidth
import com.splendo.kaluga.bytes.annotations.ItemSize
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.annotations.MedFloat
import com.splendo.kaluga.bytes.annotations.PresentWhenAllSet
import com.splendo.kaluga.bytes.annotations.Range
import com.splendo.kaluga.bytes.annotations.Scalar
import com.splendo.kaluga.bytes.annotations.Size
import com.splendo.kaluga.bytes.annotations.Unsigned
import com.splendo.kaluga.bytes.annotations.Encoded
import com.splendo.kaluga.bytes.annotations.KeyEncoded
import com.splendo.kaluga.bytes.strings.Encoding
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Joule
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.math.pow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import com.splendo.kaluga.bytes.layout.ByteArrayEndedBeforeSerializationCompleted
import com.splendo.kaluga.bytes.layout.UnsupportedCharEncoding

// Spike: a value class that IS a Kaluga ScientificValue and also carries BytesFormat annotations.
@Serializable
@JvmInline
private value class EnergyExpended(
    @Size(Length.`16_BIT`)
    @Unsigned
    override val value: Int,
) : ScientificValue<PhysicalQuantity.Energy, Joule> {
    override val unit: Joule get() = Joule
}

@Serializable
private enum class SensorContact {
    NotSupported,
    NotDetected,
    Detected,
}

class BytesFormatNumbersTest : BytesFormatTestBase(BytesFormatBackend.Factory) {

    @Test
    fun encodeScientificValueClass() {
        // round-trips through BytesFormat as a 16-bit unsigned little-endian value...
        validateRoundTrip(EnergyExpended(500), EnergyExpended.serializer(), byteArrayOf(0xF4.toByte(), 0x01))
        // ...while still being a usable ScientificValue
        assertEquals(500(Joule).compareTo(EnergyExpended(500)), 0, EnergyExpended(500).toString())
    }

    private object EncodeBooleanFixtures {
        @Serializable
        data class Container(
            val fieldValue: Boolean,
            @FlagIndex(0)
            val flagValue: Boolean,
            val nullableValue: Boolean?,
            val inlineValue: ValueContainer<Boolean>,
            val inlineNullableValue: ValueContainer<Boolean>?,
        )
    }

    @Test
    fun encodeBoolean() {
        validateEncoding(true, byteArrayOf(0x01))
        validateEncoding(false, byteArrayOf(0x00))

        validateEncoding(EncodeBooleanFixtures.Container(false, flagValue = true, nullableValue = null, ValueContainer(true), null), byteArrayOf(0b10001))
        validateEncoding(EncodeBooleanFixtures.Container(true, flagValue = false, nullableValue = false, ValueContainer(false), ValueContainer(true)), byteArrayOf(0b1001110))
        validateEncoding(EncodeBooleanFixtures.Container(true, flagValue = true, nullableValue = true, ValueContainer(true), ValueContainer(true)), byteArrayOf(0b1111111))
    }

    private object EncodeEnumInFlagsFixtures {
        @Serializable
        data class OnlyContact(@FlagIndex(0) val contact: SensorContact)

        // Flags byte (the enum) followed by a body field.
        @Serializable
        data class Reading(@FlagIndex(0) val contact: SensorContact, @Size(Length.`8_BIT`) val rate: Byte)

        // A nullable flag-enum: the presence bit sits at the index, the ordinal in the bits above it.
        @Serializable
        data class OptionalContact(@FlagIndex(0) val contact: SensorContact?)
    }

    @Test
    fun encodeEnumInFlags() {
        // 3 cases -> ceil(log2(3)) = 2 flag bits, ordinal stored LSB-first at index 0.
        validateEncoding(EncodeEnumInFlagsFixtures.OnlyContact(SensorContact.NotSupported), byteArrayOf(0b00))
        validateEncoding(EncodeEnumInFlagsFixtures.OnlyContact(SensorContact.NotDetected), byteArrayOf(0b01))
        validateEncoding(EncodeEnumInFlagsFixtures.OnlyContact(SensorContact.Detected), byteArrayOf(0b10))

        validateEncoding(EncodeEnumInFlagsFixtures.Reading(SensorContact.Detected, 42), byteArrayOf(0b10, 42))

        validateEncoding(EncodeEnumInFlagsFixtures.OptionalContact(null), byteArrayOf(0b000))
        validateEncoding(EncodeEnumInFlagsFixtures.OptionalContact(SensorContact.NotSupported), byteArrayOf(0b001))
        validateEncoding(EncodeEnumInFlagsFixtures.OptionalContact(SensorContact.Detected), byteArrayOf(0b101))
    }

    private object EncodeNumericInFlagsFixtures {
        // A sub-byte numeric subfield of a bit field: @FlagIndex + @FlagWidth and no @Size packs the value straight into
        // the flag region, least-significant bit first (like an enum ordinal), rather than as body bytes.
        @Serializable
        data class Packed(@FlagIndex(0) @FlagWidth(bits = 4) val nibble: Int, @FlagIndex(4) @FlagWidth(bits = 12) val wide: Int)

        // A single unsigned 4-bit value occupies one flags byte (the high nibble stays zero).
        @Serializable
        data class Nibble(@Unsigned @FlagIndex(0) @FlagWidth(bits = 4) val value: Int)

        // A signed sub-byte value round-trips through two's-complement sign extension: -3 in 4 bits = 0b1101.
        @Serializable
        data class Signed(@FlagIndex(0) @FlagWidth(bits = 4) val value: Int)
    }

    @Test
    fun encodeNumericInFlags() {
        // nibble = 5 -> bits 0..3 = 0b0101; wide = 0x123 -> bits 4..15. Two flag bytes: byte0 = 0x35, byte1 = 0x12.
        validateEncoding(EncodeNumericInFlagsFixtures.Packed(nibble = 5, wide = 0x123), byteArrayOf(0x35, 0x12))

        validateEncoding(EncodeNumericInFlagsFixtures.Nibble(0xA), byteArrayOf(0x0A))

        validateEncoding(EncodeNumericInFlagsFixtures.Signed(-3), byteArrayOf(0x0D))
        validateEncoding(EncodeNumericInFlagsFixtures.Signed(7), byteArrayOf(0x07))
    }

    private object RejectsNumericTooLargeForFlagWidthFixtures {
        @Serializable
        data class TooBig(@Unsigned @FlagIndex(0) @FlagWidth(bits = 4) val value: Int)
    }

    @Test
    fun rejectsNumericTooLargeForFlagWidth() {
        // 16 needs 5 bits; an unsigned 4-bit flag field holds 0..15, so encoding must fail rather than truncate.
        assertFailsWith<IllegalArgumentException> {
            BytesFormat.encodeToByteArray(RejectsNumericTooLargeForFlagWidthFixtures.TooBig.serializer(), RejectsNumericTooLargeForFlagWidthFixtures.TooBig(16))
        }
    }

    private object EncodeByteFixtures {
        @Serializable
        data class Container(
            val defaultLength: Byte,
            @Size(Length.`8_BIT`) val `8bit`: Byte,
            @Size(Length.`16_BIT`) val `16bit`: UByte,
            @Unsigned val unsigned: UByte,
            val nullable: Byte?,
            val nullableUnsigned: UByte?,
            val inlineValue: ValueContainer<Byte>,
            val nullableInlineValue: ValueContainer<Byte>?,
            val inlineUnsignedValue: ValueContainer<UByte>,
            val nullableInlineUnsignedValue: ValueContainer<UByte>?,
        )
    }

    @Test
    fun encodeByte() {
        validateEncoding(42.toByte(), byteArrayOf(42.toByte()))

        validateEncoding(
            EncodeByteFixtures.Container(
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
            EncodeByteFixtures.Container(
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

    private object EncodeShortFixtures {
        @Serializable
        data class Container(
            val defaultLength: Short,
            @Size(Length.`8_BIT`) val `8bit`: Short,
            @Size(Length.`16_BIT`) val `16bit`: Short,
            @Size(Length.`24_BIT`) val `24bit`: UShort,
            @Size(Length.`8_BIT`) @Scalar(decimalExponent = -2) val scalar: Short,
            @Size(Length.`16_BIT`) @MedFloat val medFloat16: Short,
            @Size(Length.`8_BIT`) @Size(Length.`16_BIT`) val variableSizeRange: Short,
            @Unsigned val unsigned: UShort,
            @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST) val mostSignificant: Short,
            val nullable: Short?,
            @Size(Length.`8_BIT`) @Size(Length.`16_BIT`) val nullableVariableSizing: Short?,
            val inlineValue: NumberValueContainer<Short>,
            val nullableInlineValue: NumberValueContainer<Short>?,
            val inlineUnsignedValue: NumberValueContainer<UShort>,
            val nullableInlineUnsignedValue: NumberValueContainer<UShort>?,
        )
    }

    @Test
    fun encodeShort() {
        validateEncoding(42.toShort(), 42.toShort().toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST))

        validateEncoding(
            EncodeShortFixtures.Container(
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
            EncodeShortFixtures.Container(
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

    private object EncodeIntegerFixtures {
        @Serializable
        data class Container(
            val defaultLength: Int,
            @Size(Length.`8_BIT`) val `8bit`: Int,
            @Size(Length.`16_BIT`) val `16bit`: Int,
            @Size(Length.`24_BIT`) val `24bit`: Int,
            @Size(Length.`32_BIT`) val `32bit`: Int,
            @Size(Length.`64_BIT`) val `64bit`: UInt,
            @Size(Length.`8_BIT`) @Scalar(decimalExponent = -2) val scalar: Int,
            @Size(Length.`16_BIT`) @MedFloat val medFloat16: Int,
            @Size(Length.`32_BIT`) @MedFloat val medFloat32: Int,
            @Size(Length.`16_BIT`) @Size(Length.`32_BIT`) val variableSizeLowRange: Int,
            @Size(Length.`16_BIT`) @Size(Length.`24_BIT`) @Size(Length.`32_BIT`) val variableSizeHighRange: Int,
            @Unsigned val unsigned: UInt,
            @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST) val mostSignificant: Int,
            val nullable: Int?,
            @Size(Length.`8_BIT`) @Size(Length.`16_BIT`) @Size(Length.`24_BIT`) @Size(Length.`32_BIT`) val nullableVariableSizing: Int?,
            @Size(Length.`24_BIT`) @Size(Length.`32_BIT`) val inlineValue: NumberValueContainer<Int>,
            @Size(Length.`24_BIT`) val nullableInlineValue: NumberValueContainer<Int>?,
            val inlineUnsignedValue: NumberValueContainer<UInt>,
            @Size(Length.`24_BIT`) val nullableInlineUnsignedValue: NumberValueContainer<UInt>?,
        )
    }

    @Test
    fun encodeInteger() {
        validateEncoding(42, 42.toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST))

        validateEncoding(
            EncodeIntegerFixtures.Container(
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
            EncodeIntegerFixtures.Container(
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

    private object EncodeLongFixtures {
        @Serializable
        data class Container(
            val defaultLength: Long,
            @Size(Length.`8_BIT`) val `8bit`: Long,
            @Size(Length.`16_BIT`) val `16bit`: Long,
            @Size(Length.`24_BIT`) val `24bit`: Long,
            @Size(Length.`32_BIT`) val `32bit`: Long,
            @Size(Length.`64_BIT`) val `64bit`: Long,
            @Size(Length.`8_BIT`) @Scalar(decimalExponent = -2) val scalar: Long,
            @Size(Length.`16_BIT`) @MedFloat val medFloat16: Long,
            @Size(Length.`32_BIT`) @MedFloat val medFloat32: Long,
            @Size(Length.`16_BIT`) @Size(Length.`32_BIT`) val variableSizeLowRange: Long,
            @Size(Length.`16_BIT`) @Size(Length.`24_BIT`) @Size(Length.`32_BIT`) val variableSizeHighRange: Long,
            @Size(Length.`8_BIT`) @Size(Length.`16_BIT`) @Size(Length.`24_BIT`) @Size(Length.`32_BIT`) @Size(Length.`64_BIT`) val variableFullRange: Long,
            @Size(Length.`16_BIT`) @Size(Length.`32_BIT`) @MedFloat val variableMedFloat: Long,
            @Unsigned val unsigned: ULong,
            @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST) val mostSignificant: Long,
            val nullable: Long?,
            @Size(Length.`16_BIT`) @Size(Length.`24_BIT`) @Size(Length.`32_BIT`) @Size(Length.`64_BIT`) val nullableVariableSizing: Long?,
            @Size(Length.`32_BIT`) @Size(Length.`64_BIT`) val inlineValue: ValueContainer<Long>,
            @Size(Length.`32_BIT`) @Size(Length.`64_BIT`) val nullableInlineValue: ValueContainer<Long>?,
            val unsignedInlineValue: ValueContainer<ULong>,
            val nullableUnsignedInlineValue: ValueContainer<ULong>?,
        )
    }

    @Test
    fun encodeLong() {
        validateEncoding(42L, 42L.toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST))

        validateEncoding(
            EncodeLongFixtures.Container(
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
            EncodeLongFixtures.Container(
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

    private object Encode40And48BitFixtures {
        @Serializable
        data class Container(
            @Size(Length.`40_BIT`) val `40bit`: Long,
            @Size(Length.`48_BIT`) val `48bit`: Long,
            @Size(Length.`40_BIT`) @Unsigned val unsigned40: ULong,
            @Size(Length.`48_BIT`) @Unsigned val unsigned48: ULong,
            @Size(Length.`32_BIT`) @Size(Length.`40_BIT`) @Size(Length.`48_BIT`) val variableSizing: Long,
        )
    }

    @Test
    fun encode40And48Bit() {
        // variableSizing 0x123456789A overflows 32 bits, so the flexible sizing picks 40-bit (index 1 of {32,40,48}).
        validateEncoding(
            Encode40And48BitFixtures.Container(
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
            Encode40And48BitFixtures.Container(
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

    private object EncodePresentWhenAllSetFixtures {
        @Serializable
        data class Container(
            @Size(Length.`8_BIT`) @FlagIndex(0) val a: Byte?,
            @Size(Length.`8_BIT`) @FlagIndex(1) val b: Byte?,
            // Present on the wire exactly when both bit 0 and bit 1 (a's and b's presence bits) are set; owns no bit.
            @Size(Length.`8_BIT`) @PresentWhenAllSet(0, 1) val c: Byte?,
        )
    }

    @Test
    fun encodePresentWhenAllSet() {
        // a, b present -> bits 0 and 1 set (0b11), so c is present too
        validateEncoding(EncodePresentWhenAllSetFixtures.Container(a = 10, b = 20, c = 30), byteArrayOf(0b11, 10, 20, 30))
        // a absent -> bit 0 clear, so c is necessarily absent (only b present)
        validateEncoding(EncodePresentWhenAllSetFixtures.Container(a = null, b = 20, c = null), byteArrayOf(0b10, 20))
        // b absent -> bit 1 clear, so c is necessarily absent (only a present)
        validateEncoding(EncodePresentWhenAllSetFixtures.Container(a = 10, b = null, c = null), byteArrayOf(0b01, 10))
        // nothing present
        validateEncoding(EncodePresentWhenAllSetFixtures.Container(a = null, b = null, c = null), byteArrayOf(0b00))
    }

    private object EncodePresentWhenAllSetIsLossyWhenInconsistentWithItsFlagsFixtures {
        @Serializable
        data class Container(
            @Size(Length.`8_BIT`) @FlagIndex(0) val a: Byte?,
            @Size(Length.`8_BIT`) @FlagIndex(1) val b: Byte?,
            @Size(Length.`8_BIT`) @PresentWhenAllSet(0, 1) val c: Byte?,
        )
    }

    @Test
    fun encodePresentWhenAllSetIsLossyWhenInconsistentWithItsFlags() {
        // `c` owns no flag bit: its presence is *derived* from bits 0 and 1, so a value whose `c` disagrees with those
        // bits cannot round-trip. These are still valid instances; we just pin the (lossy) wire behaviour.

        // c set while its gating bits are clear: c's byte is written, but on decode bits 0/1 are clear so c reads back
        // as null - the value is silently lost (and its stray body byte is ignored).
        val cWithoutGate = EncodePresentWhenAllSetIsLossyWhenInconsistentWithItsFlagsFixtures.Container(a = null, b = null, c = 30)
        val encoded = BytesFormat.encodeToByteArray(EncodePresentWhenAllSetIsLossyWhenInconsistentWithItsFlagsFixtures.Container.serializer(), cWithoutGate)
        assertTrue(encoded.contentEquals(byteArrayOf(0b00, 30)), encoded.toHexString(separator = " "))
        assertEquals(
            EncodePresentWhenAllSetIsLossyWhenInconsistentWithItsFlagsFixtures.Container(a = null, b = null, c = null),
            BytesFormat.decodeFromByteArray(EncodePresentWhenAllSetIsLossyWhenInconsistentWithItsFlagsFixtures.Container.serializer(), encoded),
        )

        // c null while its gating bits are set: decode expects c's byte but none was written, so decoding fails outright.
        val gateWithoutC = EncodePresentWhenAllSetIsLossyWhenInconsistentWithItsFlagsFixtures.Container(a = 10, b = 20, c = null)
        val truncated = BytesFormat.encodeToByteArray(EncodePresentWhenAllSetIsLossyWhenInconsistentWithItsFlagsFixtures.Container.serializer(), gateWithoutC)
        assertTrue(truncated.contentEquals(byteArrayOf(0b11, 10, 20)), truncated.toHexString(separator = " "))
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> {
            BytesFormat.decodeFromByteArray(EncodePresentWhenAllSetIsLossyWhenInconsistentWithItsFlagsFixtures.Container.serializer(), truncated)
        }
    }

    private object EncodeFloatFixtures {
        @Serializable
        data class Container(
            val defaultLength: Float,
            @Size(Length.`32_BIT`) val `32bit`: Float,
            @Size(Length.`64_BIT`) val `64bit`: Float,
            @Size(Length.`8_BIT`) @Scalar(multiplier = 3, decimalExponent = -5, binaryExponent = 2, offset = 10) val scalar: Float,
            @Size(Length.`8_BIT`) @Size(Length.`16_BIT`) @Scalar(decimalExponent = 4) val flexibleScalar: Float,
            @Size(Length.`16_BIT`) @MedFloat val medFloat16: Float,
            @Size(Length.`32_BIT`) @MedFloat val medFloat32: Float,
            val nullable: Float?,
        )
    }

    @Test
    fun encodeFloat() {
        validateEncoding(42.0f, 42.0f.toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST))

        validateEncoding(
            EncodeFloatFixtures.Container(
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
            EncodeFloatFixtures.Container(
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

    private object EncodeDoubleFixtures {
        @Serializable
        data class Container(
            val defaultLength: Double,
            @Size(Length.`32_BIT`) val `32bit`: Double,
            @Size(Length.`64_BIT`) val `64bit`: Double,
            @Size(Length.`32_BIT`) @Size(Length.`64_BIT`) val variableSizing: Double,
            @Size(Length.`8_BIT`) @Scalar(multiplier = 5, decimalExponent = 2, binaryExponent = 3, offset = 50) val scalar: Double,
            @Size(Length.`8_BIT`) @Size(Length.`16_BIT`) @Scalar(decimalExponent = 4) val flexibleScalar: Double,
            @Size(Length.`16_BIT`) @MedFloat val medFloat16: Double,
            @Size(Length.`32_BIT`) @MedFloat val medFloat32: Double,
            @Size(Length.`16_BIT`) @Size(Length.`32_BIT`) @MedFloat val flexibleMedFloat: Double,
            val nullable: Double?,
        )
    }

    @Test
    fun encodeDouble() {
        validateEncoding(42.0, 42.0.toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST))

        validateEncoding(
            EncodeDoubleFixtures.Container(
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
            EncodeDoubleFixtures.Container(
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

    private object EncodeRangeFixtures {
        // 8-bit: 0x00 = min, 0xFF = max
        @Serializable
        data class EightBit(@Range(0.0, 100.0) @Size(Length.`8_BIT`) val percent: Double)

        // 16-bit: 0x0000 = min, 0xFFFF = max
        @Serializable
        data class SixteenBit(@Range(0.0, 100.0) @Size(Length.`16_BIT`) val percent: Double)

        // Body field before the range field; verifies byte layout is [rangeByte][other]
        @Serializable
        data class WithHeader(val header: Byte, @Range(0.0, 1.0) @Size(Length.`8_BIT`) val fraction: Double)

        // Custom min/max range (temperature -40 degC ... +85 degC encoded as unsigned byte)
        @Serializable
        data class Temperature(@Range(-40.0, 85.0) @Size(Length.`8_BIT`) val celsius: Double)
    }

    @Test
    fun encodeRange() {
        validateEncoding(EncodeRangeFixtures.EightBit(0.0), buildByteArray { add(0x00.toByte()) })
        validateEncoding(EncodeRangeFixtures.EightBit(100.0), buildByteArray { add(0xFF.toByte()) })

        // Intermediate: round((75 / 100) x 255) = round(191.25) = 191
        val encoded8 = BytesFormat.encodeToByteArray(EncodeRangeFixtures.EightBit.serializer(), EncodeRangeFixtures.EightBit(75.0))
        assertEquals(191.toByte(), encoded8[0])
        // Round-trip: 191 / 255 x 100 ~= 74.9%
        val decoded8 = BytesFormat.decodeFromByteArray(EncodeRangeFixtures.EightBit.serializer(), encoded8)
        assertTrue(decoded8.percent in 74.9..75.1, "expected ~75 %, got ${decoded8.percent}")

        validateEncoding(EncodeRangeFixtures.SixteenBit(0.0), buildByteArray { add(uShort = 0u) })
        validateEncoding(EncodeRangeFixtures.SixteenBit(100.0), buildByteArray { add(uShort = 65535u) })

        validateEncoding(
            EncodeRangeFixtures.WithHeader(0x42, 1.0),
            buildByteArray {
                add(0x42.toByte())
                add(0xFF.toByte())
            },
        )

        validateEncoding(EncodeRangeFixtures.Temperature(-40.0), buildByteArray { add(0x00.toByte()) })
        validateEncoding(EncodeRangeFixtures.Temperature(85.0), buildByteArray { add(0xFF.toByte()) })
    }

    private object WideNaturalFixtures {
        @Serializable
        data class RangeThirtyTwoBit(@Range(0.0, 1.0) @Size(Length.`32_BIT`) val fraction: Double)

        @Serializable
        data class RangeSixtyFourBit(@Range(0.0, 1.0) @Size(Length.`64_BIT`) val fraction: Double)

        @Serializable
        data class UnsignedScalarThirtyTwoBit(@Scalar(multiplier = 2) @Unsigned @Size(Length.`32_BIT`) val value: Double)
    }

    @Test
    fun encodeRangeAtFullWidth() {
        validateEncoding(WideNaturalFixtures.RangeThirtyTwoBit(0.0), buildByteArray { add(uInt = 0u) })
        validateEncoding(WideNaturalFixtures.RangeThirtyTwoBit(1.0), buildByteArray { add(uInt = UInt.MAX_VALUE) })
        validateEncoding(WideNaturalFixtures.RangeSixtyFourBit(0.0), buildByteArray { add(uLong = 0u) })
        validateEncoding(WideNaturalFixtures.RangeSixtyFourBit(0.5), buildByteArray { add(uLong = 1uL shl 63) })
        validateEncoding(WideNaturalFixtures.RangeSixtyFourBit(1.0), buildByteArray { add(uLong = ULong.MAX_VALUE) })
    }

    @Test
    fun encodeUnsignedScalarAboveIntRange() {
        validateEncoding(WideNaturalFixtures.UnsignedScalarThirtyTwoBit(UInt.MAX_VALUE.toDouble() / 2), buildByteArray { add(uInt = UInt.MAX_VALUE) })
    }

    private object ExcludeStructureCharFixtures {
        @Serializable
        data class Container(
            @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
            @Encoded(Encoding.UTF_16)
            val char: Char,
        )
    }

    @Test
    fun encodeUtf16CharWithExcludedStructure() {
        validateEncoding(ExcludeStructureCharFixtures.Container('A'), byteArrayOf(0x00, 0x41))
    }

    private object EncodeCharFixtures {
        @Serializable
        data class Container(
            val default: Char,
            @Encoded(Encoding.UTF_16) val utf16: Char,
            @Encoded(Encoding.ASCII) val ascii: Char,
            val nullable: Char?,
            @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST) @Encoded(Encoding.UTF_16) val utf16MostSignificant: Char,
            @Encoded(Encoding.UTF_16) val nullableUTF16: Char?,
        )
    }

    @Test
    fun encodeChar() {
        validateEncoding('a', buildByteArray { add(char = 'a') })

        validateEncoding(
            EncodeCharFixtures.Container(
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

    private object Utf8CharFixtures {
        @Serializable
        data class Container(val char: Char)

        @Serializable
        data class Utf8Container(@Encoded(Encoding.UTF_8) val char: Char)

        @Serializable
        data class Utf8KeyContainer(@KeyEncoded(Encoding.UTF_8) val map: Map<Char, String>)
    }

    @Test
    fun rejectsUtf8Chars() {
        assertFailsWith<UnsupportedCharEncoding> {
            BytesFormat.encodeToByteArray(Utf8CharFixtures.Utf8Container.serializer(), Utf8CharFixtures.Utf8Container('a'))
        }
        assertFailsWith<UnsupportedCharEncoding> {
            BytesFormat.decodeFromByteArray(Utf8CharFixtures.Utf8Container.serializer(), byteArrayOf(0x61))
        }
        assertFailsWith<UnsupportedCharEncoding> {
            BytesFormat.encodeToByteArray(Utf8CharFixtures.Utf8KeyContainer.serializer(), Utf8CharFixtures.Utf8KeyContainer(emptyMap()))
        }
        assertFailsWith<IllegalArgumentException> {
            BytesFormat.encodeToByteArray(Utf8CharFixtures.Container.serializer(), Utf8CharFixtures.Container('é'))
        }
        assertFailsWith<IllegalArgumentException> {
            BytesFormat.decodeFromByteArray(Utf8CharFixtures.Container.serializer(), byteArrayOf(0xC3.toByte()))
        }
    }

    private object LongSelectionFixtures {
        @Serializable
        data class LongWideSelection8Bit(@Size(Length.`8_BIT`) @Size(Length.`64_BIT`) val v: Long)

        @Serializable
        data class LongWideSelection16Bit(@Size(Length.`16_BIT`) @Size(Length.`64_BIT`) val v: Long)
    }

    @Test
    fun longEightAndSixtyFourBitSelectionRoundTrips() {
        validateRoundTrip(LongSelectionFixtures.LongWideSelection8Bit(v = 1L shl 32), LongSelectionFixtures.LongWideSelection8Bit.serializer())
    }

    @Test
    fun longSixteenAndSixtyFourBitSelectionRoundTrips() {
        validateRoundTrip(LongSelectionFixtures.LongWideSelection16Bit(v = 1L shl 32), LongSelectionFixtures.LongWideSelection16Bit.serializer())
    }

    @Test
    fun longNegativeOutOfIntRangeSelectionRoundTrips() {
        validateRoundTrip(LongSelectionFixtures.LongWideSelection8Bit(v = -(1L shl 40)), LongSelectionFixtures.LongWideSelection8Bit.serializer())
    }

    private object MsbExcludeStructureFloatsUseBigEndianWireBytesFixtures {
        @Serializable
        @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
        data class MsbFloatFrame(val a: Float, val b: Double)
    }

    @Test
    fun msbExcludeStructureFloatsUseBigEndianWireBytes() {
        val value = MsbExcludeStructureFloatsUseBigEndianWireBytesFixtures.MsbFloatFrame(a = 1.5f, b = 2.5)
        val expected = byteArrayOf(0x3F, 0xC0.toByte(), 0x00, 0x00, 0x40, 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)
        val backend = createBackend()
        val encoded = backend.encode(MsbExcludeStructureFloatsUseBigEndianWireBytesFixtures.MsbFloatFrame.serializer(), value)
        assertTrue(encoded.contentEquals(expected), "Expected ${expected.toHexString(separator = " ")} but got ${encoded.toHexString(separator = " ")}")
        val decoded = backend.decode(MsbExcludeStructureFloatsUseBigEndianWireBytesFixtures.MsbFloatFrame.serializer(), encoded)
        assertEquals(value.a.as32Bit(), decoded.a)
        assertEquals(value.b, decoded.b)
    }

    private object PresentWhenAllSetReferencingUnownedBitRoundTripsFixtures {
        @Serializable
        data class UnownedGateContainer(@Size(Length.`8_BIT`) @FlagIndex(0) val a: Byte?, @Size(Length.`8_BIT`) @PresentWhenAllSet(10) val b: Byte?)
    }

    @Test
    fun presentWhenAllSetReferencingUnownedBitRoundTrips() {
        // Bit 10 is owned by no field, so it is never set and `b` is always absent; the header still spans it.
        validateEncoding(
            PresentWhenAllSetReferencingUnownedBitRoundTripsFixtures.UnownedGateContainer(a = 5, b = null),
            PresentWhenAllSetReferencingUnownedBitRoundTripsFixtures.UnownedGateContainer.serializer(),
            byteArrayOf(0x01, 0x00, 0x05),
        )
    }

    private object NullableUnsignedFixtures {
        @Serializable
        data class NullableSizedUByteNonNull(@Size(Length.`16_BIT`) val b: UByte, @Size(Length.`8_BIT`) val trailer: Byte)

        @Serializable
        data class NullableSizedUByte(@Size(Length.`16_BIT`) val b: UByte?, @Size(Length.`8_BIT`) val trailer: Byte)

        @Serializable
        data class NullableWideUIntNonNull(@Size(Length.`64_BIT`) val v: UInt, @Size(Length.`8_BIT`) val trailer: Byte)

        @Serializable
        data class NullableWideUInt(@Size(Length.`64_BIT`) val v: UInt?, @Size(Length.`8_BIT`) val trailer: Byte)

        @Serializable
        data class NullableWideUIntListNonNull(@ItemSize(Length.`64_BIT`) val items: List<UInt>, @Size(Length.`8_BIT`) val trailer: Byte)

        @Serializable
        data class NullableWideUIntList(@ItemSize(Length.`64_BIT`) val items: List<UInt?>, @Size(Length.`8_BIT`) val trailer: Byte)
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
        NullableUnsignedFixtures.NullableSizedUByteNonNull(b = 200u, trailer = 0x2A),
        NullableUnsignedFixtures.NullableSizedUByteNonNull.serializer(),
        NullableUnsignedFixtures.NullableSizedUByte(b = 200u, trailer = 0x2A),
        NullableUnsignedFixtures.NullableSizedUByte.serializer(),
    )

    @Test
    fun nullableWideUIntMatchesNonNullWireBytes() = assertNullableMatchesNonNull(
        NullableUnsignedFixtures.NullableWideUIntNonNull(v = 3_000_000_000u, trailer = 0x2A),
        NullableUnsignedFixtures.NullableWideUIntNonNull.serializer(),
        NullableUnsignedFixtures.NullableWideUInt(v = 3_000_000_000u, trailer = 0x2A),
        NullableUnsignedFixtures.NullableWideUInt.serializer(),
    )

    @Test
    fun nullableWideUIntListItemMatchesNonNullWireBytes() = assertNullableMatchesNonNull(
        NullableUnsignedFixtures.NullableWideUIntListNonNull(items = listOf(3_000_000_000u), trailer = 0x2A),
        NullableUnsignedFixtures.NullableWideUIntListNonNull.serializer(),
        NullableUnsignedFixtures.NullableWideUIntList(items = listOf(3_000_000_000u), trailer = 0x2A),
        NullableUnsignedFixtures.NullableWideUIntList.serializer(),
    )
}
