/*
 Copyright 2025 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bluetooth.serialization

import com.splendo.kaluga.base.bytes.ByteOrder
import com.splendo.kaluga.base.bytes.Encoding
import com.splendo.kaluga.base.bytes.StringEncodingSettings
import com.splendo.kaluga.base.bytes.buildByteArray
import com.splendo.kaluga.base.bytes.toByteArray
import com.splendo.kaluga.base.crc.CRC16
import com.splendo.kaluga.base.bytes.MedFloat16
import com.splendo.kaluga.base.bytes.MedFloat32
import com.splendo.kaluga.base.bytes.UInt24
import com.splendo.kaluga.base.bytes.toHexString
import com.splendo.kaluga.base.bytes.toInt24
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlinx.serialization.serializer
import kotlin.jvm.JvmInline
import kotlin.math.pow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

class BluetoothFormatTest {

    @Serializable
    @Prefix([0x42, 0x23])
    @Postfix([0x22])
    data class Nested<T>(val nested: T)

    @Serializable
    enum class SomeEnum {
        @SerializedByteValue(value = 0x01)
        A,

        @SerializedByteValue(value = 0x02)
        B,
    }

    @Serializable
    @Prefix([0x22, 0x44])
    @Postfix([0x33])
    data object Object

    @Serializable
    @Prefix([0x11, 0x55])
    @Postfix([0x66, 0xAA.toByte()])
    sealed class SomeSealedClass {
        @Serializable
        @SerializedByteValue(value = 0x01)
        data class A(val a: Int) : SomeSealedClass()

        @Serializable
        @SerializedByteValue(value = 0x02)
        data class B(
            @Scalar(decimalExponent = -2)
            val b: Double,
        ) : SomeSealedClass()
    }

    // A sealed hierarchy WITHOUT @SerializedByteValue, with subclasses that have a
    // DIFFERENT number of properties. The fallback identifies each option by its
    // subclass serialName encoded as a raw UTF-8 string (no length prefix).
    @Serializable
    sealed class UnmarkedSealed {
        @Serializable
        data class One(val a: Int) : UnmarkedSealed()

        @Serializable
        data class Three(val x: Byte, val y: Byte, val z: Byte) : UnmarkedSealed()
    }

    @Serializable
    data class UnmarkedSealedContainer(val value: UnmarkedSealed)

    @Serializable
    @JvmInline
    value class ValueContainer<T>(val value: T)

    @Serializable
    @JvmInline
    value class NumberValueContainer<T>(@Size(Length.`8_BIT`) @Size(Length.`16_BIT`) val value: T)

    @Serializable
    @JvmInline
    value class RRInterval(
        @Size(Length.`16_BIT`)
        @Scalar(binaryExponent = 10)
        val seconds: Double,
    ) {
        constructor(duration: Duration) : this(duration.toDouble(DurationUnit.SECONDS))
    }

    @Test
    fun encodeBoolean() {
        validateEncoding(true, byteArrayOf(0x01))
        validateEncoding(false, byteArrayOf(0x00))

        @Serializable
        data class Container(
            val fieldValue: Boolean,
            @FlagIndex(0)
            val flagValue: Boolean,
            val nullableValue: Boolean?,
            val inlineValue: ValueContainer<Boolean>,
            val inlineNullableValue: ValueContainer<Boolean>?,
        )

        validateEncoding(Container(false, flagValue = true, nullableValue = null, ValueContainer(true), null), byteArrayOf(0b10001))
        validateEncoding(Container(true, flagValue = false, nullableValue = false, ValueContainer(false), ValueContainer(true)), byteArrayOf(0b1001110))
        validateEncoding(Container(true, flagValue = true, nullableValue = true, ValueContainer(true), ValueContainer(true)), byteArrayOf(0b1111111))
    }

    @Test
    fun encodeByte() {
        validateEncoding(42.toByte(), byteArrayOf(42.toByte()))

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

        validateEncoding(
            Container(
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
            Container(
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
        validateEncoding(42.toShort(), 42.toShort().toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST))

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
            @com.splendo.kaluga.bluetooth.serialization.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST) val mostSignificant: Short,
            val nullable: Short?,
            @Size(Length.`8_BIT`) @Size(Length.`16_BIT`) val nullableVariableSizing: Short?,
            val inlineValue: NumberValueContainer<Short>,
            val nullableInlineValue: NumberValueContainer<Short>?,
            val inlineUnsignedValue: NumberValueContainer<UShort>,
            val nullableInlineUnsignedValue: NumberValueContainer<UShort>?,
        )

        validateEncoding(
            Container(
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
            Container(
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
        validateEncoding(42, 42.toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST))

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
            @com.splendo.kaluga.bluetooth.serialization.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST) val mostSignificant: Int,
            val nullable: Int?,
            @Size(Length.`8_BIT`) @Size(Length.`16_BIT`) @Size(Length.`24_BIT`) @Size(Length.`32_BIT`) val nullableVariableSizing: Int?,
            @Size(Length.`24_BIT`) @Size(Length.`32_BIT`) val inlineValue: NumberValueContainer<Int>,
            @Size(Length.`24_BIT`) val nullableInlineValue: NumberValueContainer<Int>?,
            val inlineUnsignedValue: NumberValueContainer<UInt>,
            @Size(Length.`24_BIT`) val nullableInlineUnsignedValue: NumberValueContainer<UInt>?,
        )

        validateEncoding(
            Container(
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
            Container(
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
        validateEncoding(42L, 42L.toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST))

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
            @com.splendo.kaluga.bluetooth.serialization.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST) val mostSignificant: Long,
            val nullable: Long?,
            @Size(Length.`16_BIT`) @Size(Length.`24_BIT`) @Size(Length.`32_BIT`) @Size(Length.`64_BIT`) val nullableVariableSizing: Long?,
            @Size(Length.`32_BIT`) @Size(Length.`64_BIT`) val inlineValue: ValueContainer<Long>,
            @Size(Length.`32_BIT`) @Size(Length.`64_BIT`) val nullableInlineValue: ValueContainer<Long>?,
            val unsignedInlineValue: ValueContainer<ULong>,
            val nullableUnsignedInlineValue: ValueContainer<ULong>?,
        )

        validateEncoding(
            Container(
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
            Container(
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
    fun encodeFloat() {
        validateEncoding(42.0f, 42.0f.toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST))

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

        validateEncoding(
            Container(
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
            Container(
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
        validateEncoding(42.0, 42.0.toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST))

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

        validateEncoding(
            Container(
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
            Container(
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
        validateEncoding('a', buildByteArray { add(char = 'a') })

        @Serializable
        data class Container(
            val default: Char,
            @Encoded(Encoding.UTF_8) val utf8: Char,
            @Encoded(Encoding.UTF_16) val utf16: Char,
            @Encoded(Encoding.ASCII) val ascii: Char,
            val nullable: Char?,
            @com.splendo.kaluga.bluetooth.serialization.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST) @Encoded(Encoding.UTF_16) val utf16MostSignificant: Char,
            @Encoded(Encoding.UTF_16) val nullableUTF16: Char?,
        )

        validateEncoding(
            Container(
                'a',
                'b',
                'c',
                'd',
                'e',
                'f',
                'g',
            ),
            buildByteArray {
                add(true) // nullable flag
                add(true) // nullableUTF16
                add('a')
                add('b')
                add('c', Encoding.UTF_16)
                add('d', Encoding.ASCII)
                add('e')
                add('f', Encoding.UTF_16, ByteOrder.MOST_SIGNIFICANT_FIRST)
                add('g', Encoding.UTF_16)
            },
        )
    }

    @Test
    fun encodeString() {
        // validateEncoding("Hello world", buildByteArray { add("Hello world") })

        @Serializable
        data class Container(
            val default: String,
            @Encoded(Encoding.UTF_8) val utf8: String,
            @Encoded(Encoding.UTF_16) val utf16: String,
            @Encoded(Encoding.ASCII) val ascii: String,
            val nullable: String?,
            @Encoded(Encoding.UTF_16) val nullableUTF16: String?,
            @NullTerminated val nullTerminated: String,
            @LengthPrefix(lengthAsShort = false, canOverflow = true) val lengthPrefix: String,
            @Unsized val unsized: String,
        )

        validateEncoding(
            Container(
                "Sentence A",
                "Sentence B",
                "Sentence C",
                "Sentence D",
                "Sentence E",
                "Sentence F",
                "Sentence G",
                "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec.",
                "Sentence I",
            ),
            buildByteArray {
                add(true)
                add(true)
                add("Sentence A")
                add("Sentence B")
                add("Sentence C", StringEncodingSettings(encoding = Encoding.UTF_16))
                add("Sentence D", StringEncodingSettings(encoding = Encoding.ASCII))
                add("Sentence E")
                add("Sentence F", StringEncodingSettings(encoding = Encoding.UTF_16))
                add("Sentence G", StringEncodingSettings(endMarking = StringEncodingSettings.NullTerminated))
                add(
                    "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec.",
                    StringEncodingSettings(endMarking = StringEncodingSettings.LengthPrefix.WithOverflow()),
                )
                add("Sentence I", StringEncodingSettings(endMarking = StringEncodingSettings.NoMarking))
            },
        )

        @Serializable
        data class InvalidByteOrderChange(@com.splendo.kaluga.bluetooth.serialization.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST) val content: String)

        assertFailsWith<InvalidByteOrderException> {
            BluetoothFormat.encodeToByteArray(InvalidByteOrderChange.serializer(), InvalidByteOrderChange("Invalid"))
        }

        @Serializable
        data class ContentContainsNull(@NullTerminated val content: String)

        assertFailsWith<IllegalArgumentException> {
            BluetoothFormat.encodeToByteArray(ContentContainsNull.serializer(), ContentContainsNull("Content\u0000NullTerminus"))
        }

        @Serializable
        data class ContentAfterUnsized(@Unsized val string: String, val otherContent: Byte)

        assertFailsWith<DataAfterUnconstrainedData> {
            BluetoothFormat.encodeToByteArray(ContentAfterUnsized.serializer(), ContentAfterUnsized("StringContent", 0x01))
        }

        // A plain Boolean (no @FlagIndex/@FlagWidth) has no flag slot, so it is encoded into the body via
        // addBit rather than addAction. Placing one after an @Unsized String must still be rejected: there is
        // no way to know where the unsized data ends, so any trailing body data is undecodable.
        @Serializable
        data class BooleanAfterUnsized(@Unsized val string: String, val flag: Boolean)

        assertFailsWith<DataAfterUnconstrainedData> {
            BluetoothFormat.encodeToByteArray(BooleanAfterUnsized.serializer(), BooleanAfterUnsized("StringContent", true))
        }
    }

    @Test
    fun encodeList() {
        @Serializable
        data class Item(val valueA: Byte, val valueB: Byte, val valueC: Byte)

        validateEncoding(
            listOf(
                Item(0xA0.toByte(), 0xA1.toByte(), 0xA2.toByte()),
                Item(0xB0.toByte(), 0xB1.toByte(), 0xB2.toByte()),
                Item(0xC0.toByte(), 0xC1.toByte(), 0xC2.toByte()),
            ),
            ListSerializer(Item.serializer()),
            buildByteArray {
                add(uByte = 3u)
                add(0xA0.toByte())
                add(0xA1.toByte())
                add(0xA2.toByte())
                add(0xB0.toByte())
                add(0xB1.toByte())
                add(0xB2.toByte())
                add(0xC0.toByte())
                add(0xC1.toByte())
                add(0xC2.toByte())
            },
        )

        @Serializable
        data class ListContainer(
            val default: List<Item>,
            @Size(Length.`16_BIT`) val shortSized: List<Item>,
            @Size(Length.`8_BIT`) @Size(Length.`16_BIT`) val flexibleSized: List<Item>,
            @LengthPrefix(canOverflow = true) val lengthPrefix: List<Item>,
            @NullIfEmpty val nullIfEmpty: List<Item>,
            @NullTerminated val nullTerminated: List<Item>,
            @Unsized val unsized: List<Item>,
        )

        validateEncoding(
            ListContainer(
                MutableList(10) { Item(it.toByte(), (it + 10).toByte(), (it + 20).toByte()) },
                MutableList(500) { Item(it.toByte(), (it + 30).toByte(), (it + 40).toByte()) },
                MutableList(40) { Item(it.toByte(), (it - 30).toByte(), (it - 40).toByte()) },
                MutableList(33) { Item(it.toByte(), (it - 10).toByte(), (it - 20).toByte()) },
                MutableList(5) { Item(it.toByte(), (it - 50).toByte(), (it + 50).toByte()) },
                listOf(Item(0x01, 0x00, 0x03), Item(0x04, 0x05, 0x00)),
                listOf(Item(0x06, 0x07, 0x08)),
            ),
            buildByteArray {
                add(false) // flexibleSized
                add(true) // nullIfEmpty
                add(uByte = 10u)
                repeat(10) {
                    add(byte = it.toByte())
                    add(byte = (it + 10).toByte())
                    add(byte = (it + 20).toByte())
                }
                add(uShort = 500u)
                repeat(500) {
                    add(byte = it.toByte())
                    add(byte = (it + 30).toByte())
                    add(byte = (it + 40).toByte())
                }
                add(uByte = 40u)
                repeat(40) {
                    add(byte = it.toByte())
                    add(byte = (it - 30).toByte())
                    add(byte = (it - 40).toByte())
                }
                add(uByte = 33u)
                repeat(33) {
                    add(byte = it.toByte())
                    add(byte = (it - 10).toByte())
                    add(byte = (it - 20).toByte())
                }
                add(uByte = 5u)
                repeat(5) {
                    add(byte = it.toByte())
                    add(byte = (it - 50).toByte())
                    add(byte = (it + 50).toByte())
                }
                add(byte = 0x01)
                add(byte = 0x00)
                add(byte = 0x03)
                add(byte = 0x04)
                add(byte = 0x05)
                add(byte = 0x00)
                add(byte = 0x00)
                add(byte = 0x06)
                add(byte = 0x07)
                add(byte = 0x08)
            },
        )

        validateEncoding(
            ListContainer(
                MutableList(8) { Item(it.toByte(), (it + 10).toByte(), (it + 20).toByte()) },
                MutableList(2) { Item(it.toByte(), (it + 30).toByte(), (it + 40).toByte()) },
                MutableList(1000) { Item(it.toByte(), (it - 30).toByte(), (it - 40).toByte()) },
                MutableList(800) { Item(it.toByte(), (it - 10).toByte(), (it - 20).toByte()) },
                emptyList(),
                listOf(Item(0x01, 0x02, 0x03)),
                emptyList(),
            ),
            buildByteArray {
                add(true) // flexibleSized
                add(false) // nullIfEmpty
                add(uByte = 8u)
                repeat(8) {
                    add(byte = it.toByte())
                    add(byte = (it + 10).toByte())
                    add(byte = (it + 20).toByte())
                }
                add(uShort = 2u)
                repeat(2) {
                    add(byte = it.toByte())
                    add(byte = (it + 30).toByte())
                    add(byte = (it + 40).toByte())
                }
                add(uShort = 1000u)
                repeat(1000) {
                    add(byte = it.toByte())
                    add(byte = (it - 30).toByte())
                    add(byte = (it - 40).toByte())
                }
                add(0xFF.toByte())
                add(uShort = 800u)
                repeat(800) {
                    add(byte = it.toByte())
                    add(byte = (it - 10).toByte())
                    add(byte = (it - 20).toByte())
                }
                add(byte = 0x01)
                add(byte = 0x02)
                add(byte = 0x03)
                add(byte = 0x00)
            },
        )

        @Serializable
        data class NullTerminatedList(@NullTerminated val list: List<Byte>)

        assertFailsWith<UnexpectedNullTermination> {
            BluetoothFormat.encodeToByteArray(NullTerminatedList.serializer(), NullTerminatedList(listOf(0x00.toByte(), 0x01.toByte())))
        }

        @Serializable
        data class ContentAfterUnsized(@Unsized val list: List<Byte>, val otherContent: Byte)

        assertFailsWith<DataAfterUnconstrainedData> {
            BluetoothFormat.encodeToByteArray(ContentAfterUnsized.serializer(), ContentAfterUnsized(listOf(0x01, 0x02, 0x03), 0x01))
        }
    }

    @Test
    fun encodeNumericList() {
        validateEncoding(
            listOf(1, 2, 3),
            ListSerializer(Int.serializer()),
            buildByteArray {
                add(byte = 3)
                add(1)
                add(2)
                add(3)
            },
        )
        @Serializable
        data class NumericListContainer(
            @ItemSize(Length.`8_BIT`)
            val intList: List<Int>,
            @ItemSize(Length.`8_BIT`)
            @ItemSize(Length.`16_BIT`)
            val variableSizedIntList: List<Int>,
            val nullableList: List<Int?>,
            @NullIfEmpty val nullIfEmptyList: List<Int>,
            @ItemSize(Length.`8_BIT`) @ItemScalar(decimalExponent = 4) val scalarList: List<Double>,
            @ItemSize(Length.`16_BIT`) @ItemMedFloat val medFloatList: List<Double>,
            @ItemByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST) val mostSignificant: List<Short>,
            val unsignedList: List<UByte>,
            val inlineList: List<NumberValueContainer<Int>>,
        )

        validateEncoding(
            NumericListContainer(
                listOf(1, 2, 3),
                listOf(4, 300, -12, 8000),
                listOf(5, null, 6),
                emptyList(),
                listOf(0.0001, 0.0002, 0.0004),
                listOf(1.5, 2.5, 3.5, 4.5),
                listOf(100, 200, 300),
                listOf(5u, 7u, 9u, 11u),
                listOf(12, 13, 14, 600).map { NumberValueContainer(it) },

            ),
            buildByteArray {
                add(false) // nullIfEmptyList
                // intList
                add(uByte = 3u)
                add(byte = 1)
                add(byte = 2)
                add(byte = 3)
                // variableSizedIntList
                add(uByte = 4u)
                add(false)
                add(byte = 4)
                add(true)
                add(short = 300)
                add(false)
                add(byte = -12)
                add(true)
                add(short = 8000)
                // nullableList
                add(uByte = 3u)
                add(true)
                add(5)
                add(false)
                add(byte = 0x01)
                add(6)
                // scalar list
                add(uByte = 3u)
                add(byte = 1)
                add(byte = 2)
                add(byte = 4)
                // medFloatList
                add(uByte = 4u)
                add(MedFloat16(1.5))
                add(MedFloat16(2.5))
                add(MedFloat16(3.5))
                add(MedFloat16(4.5))
                // mostSignificant
                add(uByte = 3u)
                add(short = 100, ByteOrder.MOST_SIGNIFICANT_FIRST)
                add(short = 200, ByteOrder.MOST_SIGNIFICANT_FIRST)
                add(short = 300, ByteOrder.MOST_SIGNIFICANT_FIRST)
                // unsignedList
                add(uByte = 4u)
                add(uByte = 5u)
                add(uByte = 7u)
                add(uByte = 9u)
                add(uByte = 11u)
                // inlineList
                add(uByte = 4u)
                add(false)
                add(byte = 12)
                add(false)
                add(byte = 13)
                add(false)
                add(byte = 14)
                add(true)
                add(short = 600)
            },
        )
    }

    @Test
    fun encodeStringList() {
        validateEncoding(
            listOf("A", "B", "C"),
            ListSerializer(String.serializer()),
            buildByteArray {
                add(uByte = 3u)
                add("A")
                add("B")
                add("C")
            },
        )
        @Serializable
        data class StringListContainer(
            @ItemEncoded(Encoding.UTF_8)
            val utf8List: List<String>,
            @ItemEncoded(Encoding.UTF_16)
            val utf16List: List<String>,
            @ItemEncoded(Encoding.ASCII)
            val asciiList: List<String>,
            @ItemLengthPrefix(canOverflow = true)
            val lengthPrefixList: List<String>,
            @ItemNullTerminated
            val nullTerminatedList: List<String>,
            val nullableList: List<String?>,
        )

        validateEncoding(
            StringListContainer(
                listOf("A", "B", "C", "D"),
                listOf("E", "F", "G", "H"),
                listOf("I", "J", "K", "L"),
                listOf("M", "N", MutableList(500) { "O" }.joinToString(), "P"),
                listOf("Q", "R", "S", "T"),
                listOf("U", "V", null, "X"),
            ),
            buildByteArray {
                add(uByte = 4u)
                add("A")
                add("B")
                add("C")
                add("D")
                add(uByte = 4u)
                add("E", settings = StringEncodingSettings(encoding = Encoding.UTF_16))
                add("F", settings = StringEncodingSettings(encoding = Encoding.UTF_16))
                add("G", settings = StringEncodingSettings(encoding = Encoding.UTF_16))
                add("H", settings = StringEncodingSettings(encoding = Encoding.UTF_16))
                add(uByte = 4u)
                add("I", settings = StringEncodingSettings(encoding = Encoding.ASCII))
                add("J", settings = StringEncodingSettings(encoding = Encoding.ASCII))
                add("K", settings = StringEncodingSettings(encoding = Encoding.ASCII))
                add("L", settings = StringEncodingSettings(encoding = Encoding.ASCII))
                add(uByte = 4u)
                add("M", settings = StringEncodingSettings(endMarking = StringEncodingSettings.LengthPrefix.WithOverflow()))
                add("N", settings = StringEncodingSettings(endMarking = StringEncodingSettings.LengthPrefix.WithOverflow()))
                add(MutableList(500) { "O" }.joinToString(), settings = StringEncodingSettings(endMarking = StringEncodingSettings.LengthPrefix.WithOverflow()))
                add("P", settings = StringEncodingSettings(endMarking = StringEncodingSettings.LengthPrefix.WithOverflow()))
                add(uByte = 4u)
                add("Q", settings = StringEncodingSettings(endMarking = StringEncodingSettings.NullTerminated))
                add("R", settings = StringEncodingSettings(endMarking = StringEncodingSettings.NullTerminated))
                add("S", settings = StringEncodingSettings(endMarking = StringEncodingSettings.NullTerminated))
                add("T", settings = StringEncodingSettings(endMarking = StringEncodingSettings.NullTerminated))
                add(uByte = 4u)
                add(true)
                add("U")
                add(true)
                add("V")
                add(false)
                add(byte = 0x01)
                add("X")
            },
        )
    }

    @Test
    fun encodeMap() {
        @Serializable
        data class Key(val keyValue: UShort)

        @Serializable
        data class Value(val value: Byte)

        validateEncoding(
            mapOf(
                Key(0xA0.toUShort()) to Value(0xA1.toByte()),
                Key(0xB0.toUShort()) to Value(0xB1.toByte()),
                Key(0xC0.toUShort()) to Value(0xC1.toByte()),
            ),
            MapSerializer(Key.serializer(), Value.serializer()),
            buildByteArray {
                add(uByte = 3u)
                add(0xA0.toUShort())
                add(0xA1.toByte())
                add(0xB0.toUShort())
                add(0xB1.toByte())
                add(0xC0.toUShort())
                add(0xC1.toByte())
            },
        )

        @Serializable
        data class MapContainer(
            val default: Map<Key, Value>,
            @Size(Length.`16_BIT`) val shortSized: Map<Key, Value>,
            @Size(Length.`8_BIT`) @Size(Length.`16_BIT`) val flexibleSized: Map<Key, Value>,
            @LengthPrefix(canOverflow = true) val lengthPrefix: Map<Key, Value>,
            @NullIfEmpty val nullIfEmpty: Map<Key, Value>,
            @NullTerminated val nullTerminated: Map<Key, Value>,
            @Unsized val unsized: Map<Key, Value>,
        )

        validateEncoding(
            MapContainer(
                MutableList(10) { Key(it.toUShort()) to Value((it + 10).toByte()) }.toMap(),
                MutableList(500) { Key(it.toUShort()) to Value((it + 30).toByte()) }.toMap(),
                MutableList(40) { Key(it.toUShort()) to Value((it - 30).toByte()) }.toMap(),
                MutableList(33) { Key(it.toUShort()) to Value((it - 10).toByte()) }.toMap(),
                MutableList(5) { Key(it.toUShort()) to Value((it - 50).toByte()) }.toMap(),
                mapOf(Key(0x01u) to Value(0x00), Key(0x02u) to Value(0x03)),
                mapOf(Key(0x06u) to Value(0x07), Key(0x08u) to Value(0x09)),
            ),
            buildByteArray {
                add(false) // flexibleSized
                add(true) // nullIfEmpty
                add(uByte = 10u)
                repeat(10) {
                    add(uShort = it.toUShort())
                    add(byte = (it + 10).toByte())
                }
                add(uShort = 500u)
                repeat(500) {
                    add(uShort = it.toUShort())
                    add(byte = (it + 30).toByte())
                }
                add(uByte = 40u)
                repeat(40) {
                    add(uShort = it.toUShort())
                    add(byte = (it - 30).toByte())
                }
                add(uByte = 33u)
                repeat(33) {
                    add(uShort = it.toUShort())
                    add(byte = (it - 10).toByte())
                }
                add(uByte = 5u)
                repeat(5) {
                    add(uShort = it.toUShort())
                    add(byte = (it - 50).toByte())
                }
                add(uShort = 0x01u)
                add(byte = 0x00)
                add(uShort = 0x02u)
                add(byte = 0x03)
                add(byte = 0x00)
                add(uShort = 0x06u)
                add(byte = 0x07)
                add(uShort = 0x08u)
                add(byte = 0x09)
            },
        )

        validateEncoding(
            MapContainer(
                MutableList(8) { Key(it.toUShort()) to Value((it + 10).toByte()) }.toMap(),
                MutableList(2) { Key(it.toUShort()) to Value((it + 30).toByte()) }.toMap(),
                MutableList(1000) { Key(it.toUShort()) to Value((it - 30).toByte()) }.toMap(),
                MutableList(800) { Key(it.toUShort()) to Value((it - 10).toByte()) }.toMap(),
                emptyMap(),
                mapOf(Key(0x01u) to Value(0x02), Key(0x03u) to Value(0x04)),
                emptyMap(),
            ),
            buildByteArray {
                add(true) // flexibleSized
                add(false) // nullIfEmpty
                add(uByte = 8u)
                repeat(8) {
                    add(uShort = it.toUShort())
                    add(byte = (it + 10).toByte())
                }
                add(uShort = 2u)
                repeat(2) {
                    add(uShort = it.toUShort())
                    add(byte = (it + 30).toByte())
                }
                add(uShort = 1000u)
                repeat(1000) {
                    add(uShort = it.toUShort())
                    add(byte = (it - 30).toByte())
                }
                add(0xFF.toByte())
                add(uShort = 800u)
                repeat(800) {
                    add(uShort = it.toUShort())
                    add(byte = (it - 10).toByte())
                }
                add(uShort = 0x01u)
                add(byte = 0x02)
                add(uShort = 0x03u)
                add(byte = 0x04)
                add(byte = 0x00)
            },
        )

        @Serializable
        data class NullTerminatedMap(@NullTerminated val map: Map<Byte, Byte>)

        assertFailsWith<UnexpectedNullTermination> {
            BluetoothFormat.encodeToByteArray(NullTerminatedMap.serializer(), NullTerminatedMap(mapOf(0x00.toByte() to 0x01.toByte())))
        }

        @Serializable
        data class ContentAfterUnsized(@Unsized val map: Map<Byte, Byte>, val otherContent: Byte)

        assertFailsWith<DataAfterUnconstrainedData> {
            BluetoothFormat.encodeToByteArray(ContentAfterUnsized.serializer(), ContentAfterUnsized(mapOf(0x01.toByte() to 0x02.toByte(), 0x03.toByte() to 0x04.toByte()), 0x01))
        }
    }

    @Test
    fun encodeNumericMap() {
        validateEncoding(
            mapOf(1.toShort() to 2, 3.toShort() to 4),
            MapSerializer(Short.serializer(), Int.serializer()),
            buildByteArray {
                add(uByte = 2u)
                add(short = 1)
                add(2)
                add(short = 3)
                add(4)
            },
        )
        @Serializable
        data class NumericMapContainer(
            @KeySize(Length.`8_BIT`)
            @ValueSize(Length.`24_BIT`)
            val fixedMap: Map<Short, Int>,
            @KeySize(Length.`8_BIT`)
            @KeySize(Length.`16_BIT`)
            @ValueSize(Length.`24_BIT`)
            @ValueSize(Length.`32_BIT`)
            val variableSizedMap: Map<Short, Int>,
            val nullableMap: Map<Short?, Int?>,
            @KeySize(Length.`8_BIT`) @KeyScalar(decimalExponent = 2)
            @ValueSize(Length.`24_BIT`)
            @ValueScalar(decimalExponent = 3)
            val scalarMap: Map<Double, Double>,
            @KeySize(Length.`16_BIT`) @KeyMedFloat
            @ValueSize(Length.`32_BIT`)
            @ValueMedFloat
            val medFloatList: Map<Double, Double>,
            @KeyByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
            @ValueByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
            val mostSignificant: Map<Short, Int>,
            val unsignedMap: Map<UShort, UInt>,
            val inlineMap: Map<NumberValueContainer<Short>, ValueContainer<Int>>,
        )

        validateEncoding(
            NumericMapContainer(
                mapOf(1.toShort() to 2, 3.toShort() to 4),
                mapOf(5.toShort() to 5000000, 300.toShort() to 10000000),
                mapOf(6.toShort() to 11, null to 12, 7.toShort() to null),
                mapOf(0.25 to 0.125, 0.75 to 5.0),
                mapOf(0.25 to 0.5, 5.0 to 7.0, 45.0 to 0.98),
                mapOf((-10).toShort() to 999, 1000.toShort() to -54),
                mapOf(20u.toUShort() to 777777u, 25u.toUShort() to 89898989u),
                mapOf(NumberValueContainer(30.toShort()) to ValueContainer(12345), NumberValueContainer(400.toShort()) to ValueContainer(3456)),

            ),
            buildByteArray {
                // fixedMap
                add(uByte = 2u)
                add(byte = 1)
                add(2.toInt24())
                add(byte = 3)
                add(4.toInt24())
                // variableSizedMap
                add(uByte = 2u)
                add(false)
                add(byte = 5)
                add(false)
                add(5000000.toInt24())
                add(true)
                add(short = 300)
                add(true)
                add(10000000)
                // nullableMap
                add(uByte = 3u)
                add(true)
                add(short = 6)
                add(true)
                add(11)
                add(byte = 0x00)
                add(true)
                add(12)
                add(true)
                add(short = 7)
                add(false)
                // scalarMap
                add(uByte = 2u)
                add(byte = 25)
                add(125.toInt24())
                add(byte = 75)
                add(5000.toInt24())
                add(uByte = 3u)
                add(MedFloat16(0.25))
                add(MedFloat32(0.5))
                add(MedFloat16(5.0))
                add(MedFloat32(7.0))
                add(MedFloat16(45.0))
                add(MedFloat32(0.98))
                add(uByte = 2u)
                add(short = -10, order = ByteOrder.MOST_SIGNIFICANT_FIRST)
                add(999, order = ByteOrder.MOST_SIGNIFICANT_FIRST)
                add(short = 1000, order = ByteOrder.MOST_SIGNIFICANT_FIRST)
                add(-54, order = ByteOrder.MOST_SIGNIFICANT_FIRST)
                add(uByte = 2u)
                add(uShort = 20u)
                add(777777u)
                add(uShort = 25u)
                add(89898989u)
                add(uByte = 2u)
                add(false)
                add(byte = 30)
                add(12345)
                add(true)
                add(short = 400)
                add(3456)
            },
        )
    }

    @Test
    fun encodeStringMap() {
        validateEncoding(
            mapOf('A' to "Alfa", 'B' to "Bravo", 'C' to "Charlie"),
            MapSerializer(Char.serializer(), String.serializer()),
            buildByteArray {
                add(uByte = 3u)
                add('A')
                add("Alfa")
                add('B')
                add("Bravo")
                add('C')
                add("Charlie")
            },
        )
        @Serializable
        data class StringMapContainer(
            @KeyEncoded(Encoding.UTF_8)
            @ValueEncoded(Encoding.UTF_8)
            val utf8Map: Map<Char, String>,
            @KeyEncoded(Encoding.UTF_16)
            @ValueEncoded(Encoding.UTF_16)
            val utf16Map: Map<Char, String>,
            @KeyEncoded(Encoding.ASCII)
            @ValueEncoded(Encoding.ASCII)
            val asciiMap: Map<Char, String>,
            @KeyLengthPrefix(lengthAsShort = true)
            @ValueLengthPrefix(canOverflow = true)
            val lengthPrefixMap: Map<String, String>,
            @KeyNullTerminated
            @ValueNullTerminated
            val nullTerminatedMap: Map<String, String>,
            val nullableMap: Map<String?, String?>,
        )

        validateEncoding(
            StringMapContainer(
                mapOf('A' to "Alfa", 'B' to "Bravo", 'C' to "Charlie"),
                mapOf('D' to "Delta", 'E' to "Echo", 'F' to "Foxtrot"),
                mapOf('G' to "Golf", 'H' to "Hotel", 'I' to "India"),
                mapOf("Key" to "Value", "Long" to MutableList(1000) { "A" }.joinToString()),
                mapOf("" to "Empty", "Empty" to ""),
                mapOf("Key" to "Value", null to "Empty", "NoKey" to null),
            ),
            buildByteArray {
                add(uByte = 3u)
                add('A', Encoding.UTF_8)
                add("Alfa", StringEncodingSettings(encoding = Encoding.UTF_8))
                add('B', Encoding.UTF_8)
                add("Bravo", StringEncodingSettings(encoding = Encoding.UTF_8))
                add('C', Encoding.UTF_8)
                add("Charlie", StringEncodingSettings(encoding = Encoding.UTF_8))
                add(uByte = 3u)
                add('D', Encoding.UTF_16)
                add("Delta", StringEncodingSettings(encoding = Encoding.UTF_16))
                add('E', Encoding.UTF_16)
                add("Echo", StringEncodingSettings(encoding = Encoding.UTF_16))
                add('F', Encoding.UTF_16)
                add("Foxtrot", StringEncodingSettings(encoding = Encoding.UTF_16))
                add(uByte = 3u)
                add('G', Encoding.ASCII)
                add("Golf", StringEncodingSettings(encoding = Encoding.ASCII))
                add('H', Encoding.ASCII)
                add("Hotel", StringEncodingSettings(encoding = Encoding.ASCII))
                add('I', Encoding.ASCII)
                add("India", StringEncodingSettings(encoding = Encoding.ASCII))
                add(uByte = 2u)
                add("Key", StringEncodingSettings(endMarking = StringEncodingSettings.LengthPrefix.ShortLength))
                add("Value", StringEncodingSettings(endMarking = StringEncodingSettings.LengthPrefix.WithOverflow()))
                add("Long", StringEncodingSettings(endMarking = StringEncodingSettings.LengthPrefix.ShortLength))
                add(MutableList(1000) { "A" }.joinToString(), StringEncodingSettings(endMarking = StringEncodingSettings.LengthPrefix.WithOverflow()))
                add(uByte = 2u)
                add("", StringEncodingSettings(endMarking = StringEncodingSettings.NullTerminated))
                add("Empty", StringEncodingSettings(endMarking = StringEncodingSettings.NullTerminated))
                add("Empty", StringEncodingSettings(endMarking = StringEncodingSettings.NullTerminated))
                add("", StringEncodingSettings(endMarking = StringEncodingSettings.NullTerminated))
                add(uByte = 3u)
                add(true)
                add("Key")
                add(true)
                add("Value")
                add(byte = 0x00)
                add(true)
                add("Empty")
                add(true)
                add("NoKey")
                add(false)
            },
        )
    }

    @Test
    fun encodeValueOnlyNullTerminatedMap() {
        // Only the value is null-terminated; the key uses the default ByteLength prefix.
        // This exercises valueAnnotations() mapping @ValueNullTerminated -> NullTerminated independently
        // of @KeyNullTerminated (which the combined test in encodeStringMap masked).
        @Serializable
        data class ValueNullTerminatedMapContainer(
            @ValueNullTerminated
            val map: Map<String, String>,
        )

        validateEncoding(
            ValueNullTerminatedMapContainer(
                mapOf("Key" to "Value", "Other" to "Thing"),
            ),
            ValueNullTerminatedMapContainer.serializer(),
            buildByteArray {
                add(uByte = 2u)
                add("Key") // default ByteLength prefix
                add("Value", StringEncodingSettings(endMarking = StringEncodingSettings.NullTerminated))
                add("Other") // default ByteLength prefix
                add("Thing", StringEncodingSettings(endMarking = StringEncodingSettings.NullTerminated))
            },
        )

        // Conversely, @KeyNullTerminated must NOT leak onto the value: key is null-terminated, value keeps default.
        @Serializable
        data class KeyNullTerminatedMapContainer(
            @KeyNullTerminated
            val map: Map<String, String>,
        )

        validateEncoding(
            KeyNullTerminatedMapContainer(
                mapOf("Key" to "Value", "Other" to "Thing"),
            ),
            KeyNullTerminatedMapContainer.serializer(),
            buildByteArray {
                add(uByte = 2u)
                add("Key", StringEncodingSettings(endMarking = StringEncodingSettings.NullTerminated))
                add("Value") // default ByteLength prefix
                add("Other", StringEncodingSettings(endMarking = StringEncodingSettings.NullTerminated))
                add("Thing") // default ByteLength prefix
            },
        )
    }

    @Test
    fun encodeEnum() {
        validateEncoding(SomeEnum.A, SomeEnum.serializer(), byteArrayOf(0x01))
    }

    @Test
    fun encodeObject() {
        validateEncoding(Object, byteArrayOf(0x22, 0x44, 0x33))
    }

    @Test
    fun encodeSealed() {
        validateEncoding(SomeSealedClass.A(4), SomeSealedClass.serializer(), byteArrayOf(0x11, 0x55, 0x01, 0x04, 0x00, 0x00, 0x00, 0x66, 0xAA.toByte()))
        validateEncoding(SomeSealedClass.B(600.0), SomeSealedClass.serializer(), byteArrayOf(0x11, 0x55, 0x02, 0x06, 0x00, 0x00, 0x00, 0x66, 0xAA.toByte()))
    }

    @Test
    fun encodeUnmarkedSealed() {
        // No @SerializedByteValue: each option is identified by its subclass serialName as a
        // raw UTF-8 string (no length prefix), followed by the subclass body.
        val oneName = UnmarkedSealed.One.serializer().descriptor.serialName
            .encodeToByteArray()
        val threeName = UnmarkedSealed.Three.serializer().descriptor.serialName
            .encodeToByteArray()

        validateEncoding(
            UnmarkedSealedContainer(UnmarkedSealed.One(4)),
            UnmarkedSealedContainer.serializer(),
            buildByteArray {
                add(oneName)
                add(4)
            },
        )

        validateEncoding(
            UnmarkedSealedContainer(UnmarkedSealed.Three(1, 2, 3)),
            UnmarkedSealedContainer.serializer(),
            buildByteArray {
                add(threeName)
                add(byte = 1)
                add(byte = 2)
                add(byte = 3)
            },
        )
    }

    @Test
    fun encodePolymorphic() {
        @Serializable
        abstract class Base

        @Serializable
        @SerializedByteValue(value = 0x01)
        data class A(val a: Int) : Base()

        @Serializable
        @SerializedByteValue(value = 0x02)
        data class B(@MedFloat val b: Double) : Base()

        val module = SerializersModule {
            polymorphic(Base::class) {
                subclass(A::class)
                subclass(B::class)
            }
        }

        @Serializable
        data class Container(val base: Base)

        validateEncoding(Container(A(4)), Container.serializer(), byteArrayOf(0x01, 0x04, 0x00, 0x00, 0x00), BluetoothFormat { serializersModule = module })
        validateEncoding(Container(B(600.0)), Container.serializer(), byteArrayOf(0x02) + MedFloat32(600.0).toByteArray(), BluetoothFormat { serializersModule = module })
    }

    @Test
    fun encodeHeartRate() {
        @Serializable
        data class HeartRate(
            @Size(Length.`8_BIT`)
            @Size(Length.`16_BIT`)
            @Unsigned
            val heartRate: Int,
            @FlagIndex(1)
            val contactSupported: Boolean,
            @FlagIndex(2)
            val contactDetected: Boolean = !contactSupported,
            @Unsigned
            @Size(Length.`16_BIT`)
            val energyExpended: Int? = null,
            @NullIfEmpty
            @Unsized
            val rrIntervals: List<RRInterval> = emptyList(),
        )

        validateEncoding(
            HeartRate(
                heartRate = 85,
                contactSupported = true,
                contactDetected = false,
                rrIntervals = emptyList(),
            ),
            byteArrayOf(0x02, 0x55),
        )
        validateEncoding(
            HeartRate(
                heartRate = 300,
                contactSupported = true,
                contactDetected = false,
                energyExpended = 500,
                rrIntervals = listOf(RRInterval(1.seconds), RRInterval(0.5.seconds)),
            ),
            byteArrayOf(0x1B, 0x2C, 0x01, 0xF4.toByte(), 0x01, 0x00, 0x04, 0x00, 0x02),
        )

        validateEncoding(
            listOf(
                HeartRate(50, contactSupported = true, contactDetected = true),
                HeartRate(500, contactSupported = true, contactDetected = false, rrIntervals = listOf(RRInterval(2.seconds), RRInterval(0.25.seconds))),
            ),
            ListSerializer(HeartRate.serializer()),
            byteArrayOf(0x02, 0x06, 0x32, 0x13, 0xF4.toByte(), 0x01, 0x00, 0x08, 0x00, 0x01),
        )
    }

    @Test
    fun encodeWithChecksum() {
        @Serializable
        @Prefix([0x19])
        @Postfix([0x45])
        @Checksum(16, 0x8005u, 0x0000u, reflectIn = true, reflectOut = true)
        data class WithChecksum(val index: Int, val content: String)

        validateEncoding(
            WithChecksum(1234, "123456789"),
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

    // Kotlin/JS has no true 32-bit Float and does not canonicalize Float literals, so e.g. `1234.56f`
    // keeps full double precision and would not equal the value decoded back from its 32-bit encoding.
    // Round-tripping through the raw bits yields the genuine 32-bit value on every platform (a no-op on
    // jvm/native/wasm), so the encode→decode round-trip assertions hold on js too.
    private fun Float.as32Bit(): Float = Float.fromBits(toRawBits())

    @Test
    fun encodeWithChecksumMostSignificantFirst() {
        @Serializable
        @Prefix([0x19])
        @Postfix([0x45])
        @Checksum(16, 0x8005u, 0x0000u, reflectIn = true, reflectOut = true)
        @com.splendo.kaluga.bluetooth.serialization.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
        data class WithChecksum(val index: Int, val content: String)

        val value = WithChecksum(1234, "123456789")

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

        // Full round-trip with checksum validation enabled (default BluetoothFormat): encode then decode.
        validateRoundTrip(value, WithChecksum.serializer(), expectedBytes)
    }

    @Test
    fun encodeNullTerminatedAndOverflowMostSignificantFirst() {
        @Serializable
        @com.splendo.kaluga.bluetooth.serialization.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
        data class Container(@NullTerminated val nullTerminated: Map<String, String>, @LengthPrefix(lengthAsShort = false, canOverflow = true) val overflow: String)

        val longString = MutableList(500) { "X" }.joinToString("")
        val value = Container(
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

        validateRoundTrip(value, Container.serializer(), expected)
    }

    // Like validateEncoding but without the LSB Nested<T> wrapper, since a MOST_SIGNIFICANT_FIRST structure
    // cannot legally be nested inside a LEAST_SIGNIFICANT_FIRST one (InvalidByteOrderException).
    private fun <T> validateRoundTrip(value: T, serializer: KSerializer<T>, expectedValue: ByteArray, format: BluetoothFormat = BluetoothFormat) {
        val bytes = format.encodeToByteArray(serializer, value)
        assertTrue(bytes.contentEquals(expectedValue), "Expected ${expectedValue.toHexString(separator = " ")} but got ${bytes.toHexString(separator = " ")}")
        assertEquals(value, format.decodeFromByteArray(serializer, bytes))
    }

    @OptIn(InternalSerializationApi::class)
    private inline fun <reified T : Any> validateEncoding(value: T, expectedValue: ByteArray) = validateEncoding(value, T::class.serializer(), expectedValue)

    private fun <T> validateEncoding(value: T, serializer: KSerializer<T>, expectedValue: ByteArray, format: BluetoothFormat = BluetoothFormat) {
        val bytes = format.encodeToByteArray(serializer, value)
        assertTrue(bytes.contentEquals(expectedValue), "Expected ${expectedValue.toHexString(separator = " ")} but got ${bytes.toHexString(separator = " ")}")

        assertEquals(value, format.decodeFromByteArray(serializer, bytes))

        val nestedSerializer = Nested.serializer(serializer)
        val nested = Nested(value)
        val nestedBytes = format.encodeToByteArray(nestedSerializer, nested)
        assertTrue(nestedBytes.contentEquals(byteArrayOf(0x42, 0x23) + expectedValue + 0x22))

        assertEquals(nested, format.decodeFromByteArray(nestedSerializer, nestedBytes))
    }
}
