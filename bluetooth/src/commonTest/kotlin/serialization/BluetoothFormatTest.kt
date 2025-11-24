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
import com.splendo.kaluga.base.bytes.StringEncodingSettings
import com.splendo.kaluga.base.bytes.buildByteArray
import com.splendo.kaluga.base.bytes.toByteArray
import com.splendo.kaluga.base.utils.MedFloat16
import com.splendo.kaluga.base.utils.MedFloat32
import com.splendo.kaluga.base.utils.UInt24
import com.splendo.kaluga.base.utils.toHexString
import com.splendo.kaluga.base.utils.toInt24
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlinx.serialization.serializer
import kotlin.jvm.JvmInline
import kotlin.math.pow
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class BluetoothFormatTest {

    @Serializable
    @Prefix([0x42, 0x23])
    @Postfix([0x22])
    class Nested<T>(val nested: T)

    @Serializable
    enum class SomeEnum {
        @SerializedByteValue(value = 0x01)
        A,

        @SerializedByteValue(value = 0x02)
        B,
    }

    @Serializable
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

    @Serializable
    @JvmInline
    value class ValueContainer<T>(val value: T)

    @Serializable
    @JvmInline
    value class NumberValueContainer<T>(@Sizing(Length.`8_BIT`) @Sizing(Length.`16_BIT`) val value: T)

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
            @Sizing(Length.`8_BIT`) val `8bit`: Byte,
            @Sizing(Length.`16_BIT`) val `16bit`: UByte,
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
            @Sizing(Length.`8_BIT`) val `8bit`: Short,
            @Sizing(Length.`16_BIT`) val `16bit`: Short,
            @Sizing(Length.`24_BIT`) val `24bit`: UShort,
            @Sizing(Length.`8_BIT`) @Scalar(decimalExponent = -2) val scalar: Short,
            @Sizing(Length.`16_BIT`) @MedFloat val medFloat16: Short,
            @Sizing(Length.`8_BIT`) @Sizing(Length.`16_BIT`) val variableSizeRange: Short,
            @Unsigned val unsigned: UShort,
            @com.splendo.kaluga.bluetooth.serialization.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST) val mostSignificant: Short,
            val nullable: Short?,
            @Sizing(Length.`8_BIT`) @Sizing(Length.`16_BIT`) val nullableVariableSizing: Short?,
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
            @Sizing(Length.`8_BIT`) val `8bit`: Int,
            @Sizing(Length.`16_BIT`) val `16bit`: Int,
            @Sizing(Length.`24_BIT`) val `24bit`: Int,
            @Sizing(Length.`32_BIT`) val `32bit`: Int,
            @Sizing(Length.`64_BIT`) val `64bit`: UInt,
            @Sizing(Length.`8_BIT`) @Scalar(decimalExponent = -2) val scalar: Int,
            @Sizing(Length.`16_BIT`) @MedFloat val medFloat16: Int,
            @Sizing(Length.`32_BIT`) @MedFloat val medFloat32: Int,
            @Sizing(Length.`16_BIT`) @Sizing(Length.`32_BIT`) val variableSizeLowRange: Int,
            @Sizing(Length.`16_BIT`) @Sizing(Length.`24_BIT`) @Sizing(Length.`32_BIT`) val variableSizeHighRange: Int,
            @Unsigned val unsigned: UInt,
            @com.splendo.kaluga.bluetooth.serialization.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST) val mostSignificant: Int,
            val nullable: Int?,
            @Sizing(Length.`8_BIT`) @Sizing(Length.`16_BIT`) @Sizing(Length.`24_BIT`) @Sizing(Length.`32_BIT`) val nullableVariableSizing: Int?,
            @Sizing(Length.`24_BIT`) @Sizing(Length.`32_BIT`) val inlineValue: NumberValueContainer<Int>,
            @Sizing(Length.`24_BIT`) val nullableInlineValue: NumberValueContainer<Int>?,
            val inlineUnsignedValue: NumberValueContainer<UInt>,
            @Sizing(Length.`24_BIT`) val nullableInlineUnsignedValue: NumberValueContainer<UInt>?,
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
            @Sizing(Length.`8_BIT`) val `8bit`: Long,
            @Sizing(Length.`16_BIT`) val `16bit`: Long,
            @Sizing(Length.`24_BIT`) val `24bit`: Long,
            @Sizing(Length.`32_BIT`) val `32bit`: Long,
            @Sizing(Length.`64_BIT`) val `64bit`: Long,
            @Sizing(Length.`8_BIT`) @Scalar(decimalExponent = -2) val scalar: Long,
            @Sizing(Length.`16_BIT`) @MedFloat val medFloat16: Long,
            @Sizing(Length.`32_BIT`) @MedFloat val medFloat32: Long,
            @Sizing(Length.`16_BIT`) @Sizing(Length.`32_BIT`) val variableSizeLowRange: Long,
            @Sizing(Length.`16_BIT`) @Sizing(Length.`24_BIT`) @Sizing(Length.`32_BIT`) val variableSizeHighRange: Long,
            @Sizing(Length.`8_BIT`) @Sizing(Length.`16_BIT`) @Sizing(Length.`24_BIT`) @Sizing(Length.`32_BIT`) @Sizing(Length.`64_BIT`) val variableFullRange: Long,
            @Sizing(Length.`16_BIT`) @Sizing(Length.`32_BIT`) @MedFloat val variableMedFloat: Long,
            @Unsigned val unsigned: ULong,
            @com.splendo.kaluga.bluetooth.serialization.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST) val mostSignificant: Long,
            val nullable: Long?,
            @Sizing(Length.`16_BIT`) @Sizing(Length.`24_BIT`) @Sizing(Length.`32_BIT`) @Sizing(Length.`64_BIT`) val nullableVariableSizing: Long?,
            @Sizing(Length.`32_BIT`) @Sizing(Length.`64_BIT`) val inlineValue: ValueContainer<Long>,
            @Sizing(Length.`32_BIT`) @Sizing(Length.`64_BIT`) val nullableInlineValue: ValueContainer<Long>?,
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
        class Container(
            val defaultLength: Float,
            @Sizing(Length.`32_BIT`) val `32bit`: Float,
            @Sizing(Length.`64_BIT`) val `64bit`: Float,
            @Sizing(Length.`8_BIT`) @Scalar(multiplier = 3, decimalExponent = -5, binaryExponent = 2, offset = 10) val scalar: Float,
            @Sizing(Length.`8_BIT`) @Sizing(Length.`16_BIT`) @Scalar(decimalExponent = 4) val flexibleScalar: Float,
            @Sizing(Length.`16_BIT`) @MedFloat val medFloat16: Float,
            @Sizing(Length.`32_BIT`) @MedFloat val medFloat32: Float,
            val nullable: Float?,
        )

        validateEncoding(
            Container(
                1234.56f,
                567.89f,
                0.124f,
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
                add(0.124f.toDouble())
                add(byte = 3)
                add(short = 250)
                add(MedFloat16(123.0))
                add(MedFloat32(1234567.0))
            },
        )

        validateEncoding(
            Container(
                -12.34f,
                -234.56f,
                8.0f,
                ((6.0 - 10.0) / (3 * 10.0.pow(-5) * 2.0.pow(2))).toFloat(),
                0.0024f,
                10.0f,
                0.5f,
                0.01f,
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
        class Container(
            val defaultLength: Double,
            @Sizing(Length.`32_BIT`) val `32bit`: Double,
            @Sizing(Length.`64_BIT`) val `64bit`: Double,
            @Sizing(Length.`32_BIT`) @Sizing(Length.`64_BIT`) val variableSizing: Double,
            @Sizing(Length.`8_BIT`) @Scalar(multiplier = 5, decimalExponent = 2, binaryExponent = 3, offset = 50) val scalar: Double,
            @Sizing(Length.`8_BIT`) @Sizing(Length.`16_BIT`) @Scalar(decimalExponent = 4) val flexibleScalar: Double,
            @Sizing(Length.`16_BIT`) @MedFloat val medFloat16: Double,
            @Sizing(Length.`32_BIT`) @MedFloat val medFloat32: Double,
            @Sizing(Length.`16_BIT`) @Sizing(Length.`32_BIT`) @MedFloat val flexibleMedFloat: Double,
            val nullable: Double?,
        )

        validateEncoding(
            Container(
                1234.56,
                567.89,
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
                add(567.89f)
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
                -234.56,
                8.0,
                7e-100,
                ((6.0 - 50.0) / (5 * 10.0.pow(2) * 2.0.pow(3))),
                0.0025,
                10.0,
                0.5,
                -6e-9,
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
                add(MedFloat32(-6e-9))
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
            @Encoded(StringEncodingSettings.Encoding.UTF_8) val utf8: Char,
            @Encoded(StringEncodingSettings.Encoding.UTF_16) val utf16: Char,
            @Encoded(StringEncodingSettings.Encoding.ASCII) val ascii: Char,
            val nullable: Char?,
            @com.splendo.kaluga.bluetooth.serialization.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST) @Encoded(StringEncodingSettings.Encoding.UTF_16) val utf16MostSignificant: Char,
            @Encoded(StringEncodingSettings.Encoding.UTF_16) val nullableUTF16: Char?,
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
                add('c', StringEncodingSettings.Encoding.UTF_16)
                add('d', StringEncodingSettings.Encoding.ASCII)
                add('e')
                add('f', StringEncodingSettings.Encoding.UTF_16, ByteOrder.MOST_SIGNIFICANT_FIRST)
                add('g', StringEncodingSettings.Encoding.UTF_16)
            },
        )
    }

    @Test
    fun encodeString() {
        validateEncoding("Hello world", buildByteArray { add("Hello world") })

        @Serializable
        data class Container(
            val default: String,
            @Encoded(StringEncodingSettings.Encoding.UTF_8) val utf8: String,
            @Encoded(StringEncodingSettings.Encoding.UTF_16) val utf16: String,
            @Encoded(StringEncodingSettings.Encoding.ASCII) val ascii: String,
            val nullable: String?,
            @Encoded(StringEncodingSettings.Encoding.UTF_16) val nullableUTF16: String?,
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
                add("Sentence C", StringEncodingSettings(encoding = StringEncodingSettings.Encoding.UTF_16))
                add("Sentence D", StringEncodingSettings(encoding = StringEncodingSettings.Encoding.ASCII))
                add("Sentence E")
                add("Sentence F", StringEncodingSettings(encoding = StringEncodingSettings.Encoding.UTF_16))
                add("Sentence G", StringEncodingSettings(endMarking = StringEncodingSettings.NullTerminated))
                add(
                    "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec.",
                    StringEncodingSettings(endMarking = StringEncodingSettings.LengthPrefix(lengthAsShort = false, canOverflow = true)),
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

        assertFailsWith<DataAfterUnconstainedData> {
            BluetoothFormat.encodeToByteArray(ContentAfterUnsized.serializer(), ContentAfterUnsized("StringContent", 0x01))
        }
    }

    @Test
    fun encodeList() {
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
        data class LengthContainer(
            @ItemSize(Length.`8_BIT`)
            val list: List<Int>,
        )

        validateEncoding(LengthContainer(listOf(1, 2, 3)), byteArrayOf(0x03, 0x01, 0x02, 0x03))

        @Serializable
        data class FlexibleLengthContainer(
            @ItemSize(Length.`8_BIT`)
            @ItemSize(Length.`16_BIT`)
            val list: List<Int>,
        )

        validateEncoding(FlexibleLengthContainer(listOf(1, 512, 3)), byteArrayOf(0x03, 0x00, 0x01, 0x01, 0x00, 0x02, 0x00, 0x03))
    }

    @Test
    fun encodeEnum() {
        validateEncoding(SomeEnum.A, SomeEnum.serializer(), byteArrayOf(0x01))
    }

    @Test
    fun encodeSealed() {
        validateEncoding(SomeSealedClass.A(4), SomeSealedClass.serializer(), byteArrayOf(0x01, 0x04, 0x00, 0x00, 0x00))
        validateEncoding(SomeSealedClass.B(600.0), SomeSealedClass.serializer(), byteArrayOf(0x02, 0x06, 0x00, 0x00, 0x00))
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
            @Sizing(Length.`8_BIT`)
            @Sizing(Length.`16_BIT`)
            @Unsigned
            val heartRate: Int,
            @FlagIndex(1)
            val contactSupported: Boolean,
            @FlagIndex(2)
            val contactDetected: Boolean = !contactSupported,
            @Unsigned
            @Sizing(Length.`16_BIT`)
            val energyExpended: Int? = null,
        )

        validateEncoding(
            HeartRate(
                heartRate = 85,
                contactSupported = true,
                contactDetected = false,
            ),
            byteArrayOf(0x02, 0x55),
        )
        validateEncoding(
            HeartRate(
                heartRate = 300,
                contactSupported = true,
                contactDetected = false,
                energyExpended = 500,
            ),
            byteArrayOf(0x0B, 0x2C, 0x01, 0xF4.toByte(), 0x01),
        )

        validateEncoding(
            listOf(
                HeartRate(50, contactSupported = true, contactDetected = true),
                HeartRate(500, contactSupported = true, contactDetected = false),
            ),
            ListSerializer(HeartRate.serializer()),
            byteArrayOf(0x02, 0x06, 0x32, 0x03, 0xF4.toByte(), 0x01),
        )
    }

    @OptIn(InternalSerializationApi::class)
    private inline fun <reified T : Any> validateEncoding(value: T, expectedValue: ByteArray) = validateEncoding(value, T::class.serializer(), expectedValue)

    private fun <T> validateEncoding(value: T, serializer: KSerializer<T>, expectedValue: ByteArray, format: BluetoothFormat = BluetoothFormat) {
        val bytes = format.encodeToByteArray(serializer, value)
        assertTrue(bytes.contentEquals(expectedValue), "Expected ${expectedValue.toHexString(separator = " ")} but got ${bytes.toHexString(separator = " ")}")

        val nestedBytes = format.encodeToByteArray(Nested.serializer(serializer), Nested(value))
        assertTrue(nestedBytes.contentEquals(byteArrayOf(0x42, 0x23) + expectedValue + 0x22))
    }
}
