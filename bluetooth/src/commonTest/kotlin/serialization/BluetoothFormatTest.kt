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
import com.splendo.kaluga.base.bytes.buildByteArray
import com.splendo.kaluga.base.bytes.toByteArray
import com.splendo.kaluga.base.utils.MedFloat16
import com.splendo.kaluga.base.utils.MedFloat32
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
import kotlin.math.pow
import kotlin.test.Test
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
        )

        validateEncoding(Container(false, flagValue = true, nullableValue = null), byteArrayOf(0b0001))
        validateEncoding(Container(true, flagValue = false, nullableValue = true), byteArrayOf(0b1110))
        validateEncoding(Container(true, flagValue = true, nullableValue = false), byteArrayOf(0b0111))
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
        )

        validateEncoding(
            Container(
                defaultLength = 42,
                `8bit` = 23,
                `16bit` = 250.toUByte(),
                unsigned = 123u,
                nullable = null,
            ),
            buildByteArray {
                add(false)
                add(byte = 42)
                add(byte = 23)
                add(short = 250)
                add(byte = 123)
            },
        )

        validateEncoding(
            Container(
                defaultLength = 80,
                `8bit` = 45,
                `16bit` = 230.toUByte(),
                unsigned = 24u,
                nullable = 20,
            ),
            buildByteArray {
                add(true)
                add(byte = 80)
                add(byte = 45)
                add(short = 230)
                add(byte = 24)
                add(byte = 20)
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
            ),
            buildByteArray {
                add(true) // variableSizeRange flag
                add(false) // nullable flag
                add(false) // nullableVariableSizing flag
                add(short = 42)
                add(byte = 112)
                add(short = 512)
                add(int24 = 16777.toInt24())
                add(byte = 5)
                add(MedFloat16(123.0))
                add(short = 567)
                add(short = 1234)
                add(short = 567, order = ByteOrder.MOST_SIGNIFICANT_FIRST)
            },
        )

        validateEncoding(
            Container(
                defaultLength = 80,
                `8bit` = 37,
                `16bit` = 8,
                `24bit` = 16.toUShort(),
                scalar = 700,
                medFloat16 = 23,
                variableSizeRange = 30,
                unsigned = 2345.toUShort(),
                mostSignificant = 123,
                nullable = 34,
                nullableVariableSizing = 200,
            ),
            buildByteArray {
                add(false) // variableSizeRange flag
                add(true) // nullable flag
                add(true) // nullableVariableSizing flag
                add(true)
                add(short = 80)
                add(byte = 37)
                add(short = 8)
                add(int24 = 16.toInt24())
                add(byte = 7)
                add(MedFloat16(23.0))
                add(byte = 30)
                add(short = 2345)
                add(short = 123, order = ByteOrder.MOST_SIGNIFICANT_FIRST)
                add(short = 34)
                add(short = 200)
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
            ),
            buildByteArray {
                add(false) // variableSizeLowRange flags
                add(true) // variableSizeHighRange flags
                add(false)
                add(false) // Nullable
                add(false) // nullableVariableSizing
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
            },
        )
        validateEncoding(
            Container(
                defaultLength = 80,
                `8bit` = 20,
                `16bit` = 5,
                `24bit` = 33,
                `32bit` = 2000,
                `64bit` = 1234567u,
                scalar = 700,
                medFloat16 = 45,
                medFloat32 = 78,
                variableSizeLowRange = 2000000,
                variableSizeHighRange = 9000000,
                unsigned = 56u,
                mostSignificant = 67,
                nullable = 78,
                nullableVariableSizing = 10000000,
            ),
            buildByteArray {
                add(true) // variableSizeLowRange flags
                add(false) // variableSizeHighRange flags
                add(true)
                add(true) // Nullable
                add(true) // nullableVariableSizing
                add(true)
                add(true)
                add(int = 80)
                add(byte = 20)
                add(short = 5)
                add(33.toInt24())
                add(2000)
                add(1234567.toULong())
                add(byte = 7)
                add(MedFloat16(45.0))
                add(MedFloat32(78.0))
                add(2000000)
                add(9000000)
                add(56u)
                add(67, ByteOrder.MOST_SIGNIFICANT_FIRST)
                add(78)
                add(10000000)
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
            @Unsigned val unsigned: ULong,
            @com.splendo.kaluga.bluetooth.serialization.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST) val mostSignificant: Long,
            val nullable: Long?,
            @Sizing(Length.`16_BIT`) @Sizing(Length.`24_BIT`) @Sizing(Length.`32_BIT`) @Sizing(Length.`64_BIT`) val nullableVariableSizing: Long?,
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
                unsigned = 0x12345678u,
                mostSignificant = 0x12345678,
                nullable = null,
                nullableVariableSizing = null,
            ),
            buildByteArray {
                add(false) // variableSizeLowRange flag
                add(true) // variableSizeHighRange flags
                add(false)
                add(false) // variableFullRange flags
                add(false)
                add(true)
                add(false) // nullable
                add(false) // nullableVariableSizing
                add(false)
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
                add(uLong = 0x12345678u)
                add(long = 0x12345678, ByteOrder.MOST_SIGNIFICANT_FIRST)
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
                unsigned = 77u,
                mostSignificant = -4,
                nullable = -22,
                nullableVariableSizing = -7000000,
            ),
            buildByteArray {
                add(true) // variableSizeLowRange flag
                add(false) // variableSizeHighRange flags
                add(true)
                add(false) // variableFullRange flags
                add(false)
                add(false)
                add(true) // nullable
                add(true) // nullableVariableSizing
                add(true)
                add(false)
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
                add(uLong = 77u)
                add(long = -4, ByteOrder.MOST_SIGNIFICANT_FIRST)
                add(long = -22)
                add((-7000000).toInt24())
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
        validateEncoding(Container(B(600.0)), Container.serializer(), byteArrayOf(0x02, 0x00, 0x58, 0x02, 0x00), BluetoothFormat { serializersModule = module })
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
