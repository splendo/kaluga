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

// The layouts keep the shared suite's bit-width property names (`8bit`, ...), which the tests pass as named arguments.
@file:Suppress("ktlint:standard:property-naming")

package com.splendo.kaluga.bytes.example.format

import com.splendo.kaluga.bytes.ByteOrder
import com.splendo.kaluga.bytes.strings.Encoding
import com.splendo.kaluga.bytes.annotations.BytesLayout
import com.splendo.kaluga.bytes.annotations.Encoded
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
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.unit.Joule
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

// Spike: a value class that IS a Kaluga ScientificValue and also carries BytesFormat annotations.
@Serializable
@JvmInline
value class EnergyExpended(
    @Size(Length.`16_BIT`)
    @Unsigned
    override val value: Int,
) : ScientificValue<PhysicalQuantity.Energy, Joule> {
    override val unit: Joule get() = Joule
}

object EncodeScientificValueClassFieldFixtures {
    @BytesLayout
    interface Reading {
        val energy: EnergyExpended
    }
}

@Serializable
enum class SensorContact {
    NotSupported,
    NotDetected,
    Detected,
}

object EncodeBooleanFixtures {
    @BytesLayout
    interface Container {
        val fieldValue: Boolean

        @FlagIndex(0)
        val flagValue: Boolean
        val nullableValue: Boolean?
        val inlineValue: ValueContainer<Boolean>
        val inlineNullableValue: ValueContainer<Boolean>?
    }
}

object EncodeEnumInFlagsFixtures {
    @BytesLayout
    interface OnlyContact {
        @FlagIndex(0)
        val contact: SensorContact
    }

    // Flags byte (the enum) followed by a body field.
    @BytesLayout
    interface Reading {
        @FlagIndex(0)
        val contact: SensorContact

        @Size(Length.`8_BIT`)
        val rate: Byte
    }

    // A nullable flag-enum: the presence bit sits at the index, the ordinal in the bits above it.
    @BytesLayout
    interface OptionalContact {
        @FlagIndex(0)
        val contact: SensorContact?
    }
}

object EncodeNumericInFlagsFixtures {
    // A sub-byte numeric subfield of a bit field: @FlagIndex + @FlagWidth and no @Size packs the value straight into
    // the flag region, least-significant bit first (like an enum ordinal), rather than as body bytes.
    @BytesLayout
    interface Packed {
        @FlagIndex(0)
        @FlagWidth(bits = 4)
        val nibble: Int

        @FlagIndex(4)
        @FlagWidth(bits = 12)
        val wide: Int
    }

    // A single unsigned 4-bit value occupies one flags byte (the high nibble stays zero).
    @BytesLayout
    interface Nibble {
        @Unsigned
        @FlagIndex(0)
        @FlagWidth(bits = 4)
        val value: Int
    }

    // A signed sub-byte value round-trips through two's-complement sign extension: -3 in 4 bits = 0b1101.
    @BytesLayout
    interface Signed {
        @FlagIndex(0)
        @FlagWidth(bits = 4)
        val value: Int
    }
}

object RejectsNumericTooLargeForFlagWidthFixtures {
    @BytesLayout
    interface TooBig {
        @Unsigned
        @FlagIndex(0)
        @FlagWidth(bits = 4)
        val value: Int
    }
}

object EncodeByteFixtures {
    @BytesLayout
    interface Container {
        val defaultLength: Byte

        @Size(Length.`8_BIT`)
        val `8bit`: Byte

        @Size(Length.`16_BIT`)
        val `16bit`: UByte

        @Unsigned val unsigned: UByte
        val nullable: Byte?
        val nullableUnsigned: UByte?
        val inlineValue: ValueContainer<Byte>
        val nullableInlineValue: ValueContainer<Byte>?
        val inlineUnsignedValue: ValueContainer<UByte>
        val nullableInlineUnsignedValue: ValueContainer<UByte>?
    }
}

object EncodeShortFixtures {
    @BytesLayout
    interface Container {
        val defaultLength: Short

        @Size(Length.`8_BIT`)
        val `8bit`: Short

        @Size(Length.`16_BIT`)
        val `16bit`: Short

        @Size(Length.`24_BIT`)
        val `24bit`: UShort

        @Size(Length.`8_BIT`)
        @Scalar(decimalExponent = -2)
        val scalar: Short

        @Size(Length.`16_BIT`)
        @MedFloat
        val medFloat16: Short

        @Size(Length.`8_BIT`)
        @Size(Length.`16_BIT`)
        val variableSizeRange: Short

        @Unsigned val unsigned: UShort

        @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
        val mostSignificant: Short
        val nullable: Short?

        @Size(Length.`8_BIT`)
        @Size(Length.`16_BIT`)
        val nullableVariableSizing: Short?
        val inlineValue: NumberValueContainer<Short>
        val nullableInlineValue: NumberValueContainer<Short>?
        val inlineUnsignedValue: NumberValueContainer<UShort>
        val nullableInlineUnsignedValue: NumberValueContainer<UShort>?
    }
}

object EncodeIntegerFixtures {
    @BytesLayout
    interface Container {
        val defaultLength: Int

        @Size(Length.`8_BIT`)
        val `8bit`: Int

        @Size(Length.`16_BIT`)
        val `16bit`: Int

        @Size(Length.`24_BIT`)
        val `24bit`: Int

        @Size(Length.`32_BIT`)
        val `32bit`: Int

        @Size(Length.`64_BIT`)
        val `64bit`: UInt

        @Size(Length.`8_BIT`)
        @Scalar(decimalExponent = -2)
        val scalar: Int

        @Size(Length.`16_BIT`)
        @MedFloat
        val medFloat16: Int

        @Size(Length.`32_BIT`)
        @MedFloat
        val medFloat32: Int

        @Size(Length.`16_BIT`)
        @Size(Length.`32_BIT`)
        val variableSizeLowRange: Int

        @Size(Length.`16_BIT`)
        @Size(Length.`24_BIT`)
        @Size(Length.`32_BIT`)
        val variableSizeHighRange: Int

        @Unsigned val unsigned: UInt

        @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
        val mostSignificant: Int
        val nullable: Int?

        @Size(Length.`8_BIT`)
        @Size(Length.`16_BIT`)
        @Size(Length.`24_BIT`)
        @Size(Length.`32_BIT`)
        val nullableVariableSizing: Int?

        @Size(Length.`24_BIT`)
        @Size(Length.`32_BIT`)
        val inlineValue: NumberValueContainer<Int>

        @Size(Length.`24_BIT`)
        val nullableInlineValue: NumberValueContainer<Int>?
        val inlineUnsignedValue: NumberValueContainer<UInt>

        @Size(Length.`24_BIT`)
        val nullableInlineUnsignedValue: NumberValueContainer<UInt>?
    }
}

object EncodeLongFixtures {
    @BytesLayout
    interface Container {
        val defaultLength: Long

        @Size(Length.`8_BIT`)
        val `8bit`: Long

        @Size(Length.`16_BIT`)
        val `16bit`: Long

        @Size(Length.`24_BIT`)
        val `24bit`: Long

        @Size(Length.`32_BIT`)
        val `32bit`: Long

        @Size(Length.`64_BIT`)
        val `64bit`: Long

        @Size(Length.`8_BIT`)
        @Scalar(decimalExponent = -2)
        val scalar: Long

        @Size(Length.`16_BIT`)
        @MedFloat
        val medFloat16: Long

        @Size(Length.`32_BIT`)
        @MedFloat
        val medFloat32: Long

        @Size(Length.`16_BIT`)
        @Size(Length.`32_BIT`)
        val variableSizeLowRange: Long

        @Size(Length.`16_BIT`)
        @Size(Length.`24_BIT`)
        @Size(Length.`32_BIT`)
        val variableSizeHighRange: Long

        @Size(Length.`8_BIT`)
        @Size(Length.`16_BIT`)
        @Size(Length.`24_BIT`)
        @Size(Length.`32_BIT`)
        @Size(Length.`64_BIT`)
        val variableFullRange: Long

        @Size(Length.`16_BIT`)
        @Size(Length.`32_BIT`)
        @MedFloat
        val variableMedFloat: Long

        @Unsigned val unsigned: ULong

        @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
        val mostSignificant: Long
        val nullable: Long?

        @Size(Length.`16_BIT`)
        @Size(Length.`24_BIT`)
        @Size(Length.`32_BIT`)
        @Size(Length.`64_BIT`)
        val nullableVariableSizing: Long?

        @Size(Length.`32_BIT`)
        @Size(Length.`64_BIT`)
        val inlineValue: ValueContainer<Long>

        @Size(Length.`32_BIT`)
        @Size(Length.`64_BIT`)
        val nullableInlineValue: ValueContainer<Long>?
        val unsignedInlineValue: ValueContainer<ULong>
        val nullableUnsignedInlineValue: ValueContainer<ULong>?
    }
}

object Encode40And48BitFixtures {
    @BytesLayout
    interface Container {
        @Size(Length.`40_BIT`)
        val `40bit`: Long

        @Size(Length.`48_BIT`)
        val `48bit`: Long

        @Size(Length.`40_BIT`)
        @Unsigned
        val unsigned40: ULong

        @Size(Length.`48_BIT`)
        @Unsigned
        val unsigned48: ULong

        @Size(Length.`32_BIT`)
        @Size(Length.`40_BIT`)
        @Size(Length.`48_BIT`)
        val variableSizing: Long
    }
}

object EncodePresentWhenAllSetFixtures {
    @BytesLayout
    interface Container {
        @Size(Length.`8_BIT`)
        @FlagIndex(0)
        val a: Byte?

        @Size(Length.`8_BIT`)
        @FlagIndex(1)
        val b: Byte?

        // Present on the wire exactly when both bit 0 and bit 1 (a's and b's presence bits) are set; owns no bit.
        @Size(Length.`8_BIT`)
        @PresentWhenAllSet(0, 1)
        val c: Byte?
    }
}

object EncodePresentWhenAllSetIsLossyWhenInconsistentWithItsFlagsFixtures {
    @BytesLayout
    interface Container {
        @Size(Length.`8_BIT`)
        @FlagIndex(0)
        val a: Byte?

        @Size(Length.`8_BIT`)
        @FlagIndex(1)
        val b: Byte?

        @Size(Length.`8_BIT`)
        @PresentWhenAllSet(0, 1)
        val c: Byte?
    }
}

object EncodeFloatFixtures {
    @BytesLayout
    interface Container {
        val defaultLength: Float

        @Size(Length.`32_BIT`)
        val `32bit`: Float

        @Size(Length.`64_BIT`)
        val `64bit`: Float

        @Size(Length.`8_BIT`)
        @Scalar(multiplier = 3, decimalExponent = -5, binaryExponent = 2, offset = 10)
        val scalar: Float

        @Size(Length.`8_BIT`)
        @Size(Length.`16_BIT`)
        @Scalar(decimalExponent = 4)
        val flexibleScalar: Float

        @Size(Length.`16_BIT`)
        @MedFloat
        val medFloat16: Float

        @Size(Length.`32_BIT`)
        @MedFloat
        val medFloat32: Float
        val nullable: Float?
    }
}

object EncodeDoubleFixtures {
    @BytesLayout
    interface Container {
        val defaultLength: Double

        @Size(Length.`32_BIT`)
        val `32bit`: Double

        @Size(Length.`64_BIT`)
        val `64bit`: Double

        @Size(Length.`32_BIT`)
        @Size(Length.`64_BIT`)
        val variableSizing: Double

        @Size(Length.`8_BIT`)
        @Scalar(multiplier = 5, decimalExponent = 2, binaryExponent = 3, offset = 50)
        val scalar: Double

        @Size(Length.`8_BIT`)
        @Size(Length.`16_BIT`)
        @Scalar(decimalExponent = 4)
        val flexibleScalar: Double

        @Size(Length.`16_BIT`)
        @MedFloat
        val medFloat16: Double

        @Size(Length.`32_BIT`)
        @MedFloat
        val medFloat32: Double

        @Size(Length.`16_BIT`)
        @Size(Length.`32_BIT`)
        @MedFloat
        val flexibleMedFloat: Double
        val nullable: Double?
    }
}

object EncodeRangeFixtures {
    // 8-bit: 0x00 = min, 0xFF = max
    @BytesLayout
    interface EightBit {
        @Range(0.0, 100.0)
        @Size(Length.`8_BIT`)
        val percent: Double
    }

    // 16-bit: 0x0000 = min, 0xFFFF = max
    @BytesLayout
    interface SixteenBit {
        @Range(0.0, 100.0)
        @Size(Length.`16_BIT`)
        val percent: Double
    }

    // Body field before the range field; verifies byte layout is [rangeByte][other]
    @BytesLayout
    interface WithHeader {
        val header: Byte

        @Range(0.0, 1.0)
        @Size(Length.`8_BIT`)
        val fraction: Double
    }

    // Custom min/max range (temperature -40 degC ... +85 degC encoded as unsigned byte)
    @BytesLayout
    interface Temperature {
        @Range(-40.0, 85.0)
        @Size(Length.`8_BIT`)
        val celsius: Double
    }
}

object EncodeCharFixtures {
    @BytesLayout
    interface Container {
        val default: Char

        @Encoded(Encoding.UTF_8)
        val utf8: Char

        @Encoded(Encoding.UTF_16)
        val utf16: Char

        @Encoded(Encoding.ASCII)
        val ascii: Char
        val nullable: Char?

        @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
        @Encoded(Encoding.UTF_16)
        val utf16MostSignificant: Char

        @Encoded(Encoding.UTF_16)
        val nullableUTF16: Char?
    }
}

object LongSelectionFixtures {
    @BytesLayout
    interface LongWideSelection8Bit {
        @Size(Length.`8_BIT`)
        @Size(Length.`64_BIT`)
        val v: Long
    }

    @BytesLayout
    interface LongWideSelection16Bit {
        @Size(Length.`16_BIT`)
        @Size(Length.`64_BIT`)
        val v: Long
    }
}

object MsbExcludeStructureFloatsUseBigEndianWireBytesFixtures {
    @BytesLayout
    @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
    interface MsbFloatFrame {
        val a: Float
        val b: Double
    }
}

object PresentWhenAllSetReferencingUnownedBitRoundTripsFixtures {
    @BytesLayout
    interface UnownedGateContainer {
        @Size(Length.`8_BIT`)
        @FlagIndex(0)
        val a: Byte?

        @Size(Length.`8_BIT`)
        @PresentWhenAllSet(10)
        val b: Byte?
    }
}

object NullableUnsignedFixtures {
    @BytesLayout
    interface NullableSizedUByteNonNull {
        @Size(Length.`16_BIT`)
        val b: UByte

        @Size(Length.`8_BIT`)
        val trailer: Byte
    }

    @BytesLayout
    interface NullableSizedUByte {
        @Size(Length.`16_BIT`)
        val b: UByte?

        @Size(Length.`8_BIT`)
        val trailer: Byte
    }

    @BytesLayout
    interface NullableWideUIntNonNull {
        @Size(Length.`64_BIT`)
        val v: UInt

        @Size(Length.`8_BIT`)
        val trailer: Byte
    }

    @BytesLayout
    interface NullableWideUInt {
        @Size(Length.`64_BIT`)
        val v: UInt?

        @Size(Length.`8_BIT`)
        val trailer: Byte
    }

    @BytesLayout
    interface NullableWideUIntListNonNull {
        @ItemSize(Length.`64_BIT`)
        val items: List<UInt>

        @Size(Length.`8_BIT`)
        val trailer: Byte
    }

    @BytesLayout
    interface NullableWideUIntList {
        @ItemSize(Length.`64_BIT`)
        val items: List<UInt?>

        @Size(Length.`8_BIT`)
        val trailer: Byte
    }
}
