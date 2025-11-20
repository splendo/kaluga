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

import com.splendo.kaluga.base.utils.toHexString
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.serializer
import kotlin.test.Test
import kotlin.test.assertTrue

class BluetoothFormatTest {

    @Serializable
    @Prefix([0x42, 0x23])
    @Postfix([0x22])
    class Nested<T>(
        val nested: T,
    )

    @Serializable
    enum class SomeEnum {
        @SerializedByteValue(value = 0x01)
        A,
        @SerializedByteValue(value = 0x02)
        B
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
            val b: Double
        ) : SomeSealedClass()
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
            byteArrayOf(0x02, 0x55)
        )
        validateEncoding(
            HeartRate(
                heartRate = 300,
                contactSupported = true,
                contactDetected = false,
                energyExpended = 500,
            ),
            byteArrayOf(0x0B, 0x2C, 0x01, 0xF4.toByte(), 0x01)
        )

        validateEncoding(
            listOf(HeartRate(50, contactSupported = true, contactDetected = true),
                HeartRate(500, contactSupported = true, contactDetected = false),
            ), ListSerializer(HeartRate.serializer()), byteArrayOf(0x02, 0x06, 0x32, 0x03, 0xF4.toByte(), 0x01))
    }

    @OptIn(InternalSerializationApi::class)
    private inline fun <reified T : Any> validateEncoding(value: T, expectedValue: ByteArray) = validateEncoding(value, T::class.serializer(), expectedValue)

    private fun <T> validateEncoding(value: T, serializer: KSerializer<T>, expectedValue: ByteArray) {
        val bytes = BluetoothFormat.encodeToByteArray(serializer, value)
        assertTrue(bytes.contentEquals(expectedValue), "Expected ${expectedValue.toHexString(separator = " ")} but got ${bytes.toHexString(separator = " ")}")

        val nestedBytes = BluetoothFormat.encodeToByteArray(Nested.serializer(serializer), Nested(value))
        assertTrue(nestedBytes.contentEquals(byteArrayOf(0x42, 0x23) + expectedValue + 0x22))
    }
}