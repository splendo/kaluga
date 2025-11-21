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
            @Sizing(Length.`8_BIT`) @Sizing(Length.`16_BIT`) @Sizing(Length.`24_BIT`) @Sizing(Length.`32_BIT`) @Sizing(Length.`64_BIT`) val fullRange: UInt,
            @Unsigned val unsigned: UInt,
            @com.splendo.kaluga.bluetooth.serialization.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST) val mostSignificant: Int,
            val nullable: Int?,
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
                fullRange = 0xFFFFFFAAu,
                unsigned = 0x12345678u,
                mostSignificant = 0x12345678,
                nullable = null,
            ),
            byteArrayOf(0x02) +
                42.toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST) +
                112.toByte() +
                512.toShort().toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST) +
                16777.toInt24().toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST) +
                0x7ABCDEFF.toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST) +
                (UInt.MAX_VALUE - 12u).toULong().toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST) +
                5.toByte() +
                MedFloat16(123.0).toByteArray() +
                MedFloat32(1234567.0).toByteArray() +
                123.toShort().toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST) +
                1234567.toInt24().toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST) +
                0xFFFFFFAAu.toULong().toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST) +
                0x12345678u.toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST) +
                0x12345678.toByteArray(ByteOrder.MOST_SIGNIFICANT_FIRST),

        )
    }

    @Test
    fun encodeIntList() {
        validateEncoding(
            listOf(1, 2, 3),
            ListSerializer(Int.serializer()),
            byteArrayOf(0x03, 0x01, 0x00, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00),
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
