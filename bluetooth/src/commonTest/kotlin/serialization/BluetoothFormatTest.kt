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
import kotlinx.serialization.serializer
import kotlin.test.Test
import kotlin.test.assertTrue

class BluetoothFormatTest {

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

        @Serializable
        data class Nested(
            val heartRate: HeartRate,
            @LengthPrefix()
            val text: String
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
            Nested(
                HeartRate(50, contactSupported = true, contactDetected = true),
                "Hello World"
            ),
            byteArrayOf(0x06, 0x32, 0x0B, 0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x20, 0x57, 0x6F, 0x72, 0x6C, 0x64)
        )
    }

    @OptIn(InternalSerializationApi::class)
    private inline fun <reified T : Any> validateEncoding(value: T, expectedValue: ByteArray) = validateEncoding(value, T::class.serializer(), expectedValue)

    private fun <T> validateEncoding(value: T, serializer: KSerializer<T>, expectedValue: ByteArray) {
        val bytes = BluetoothFormat.encodeToByteArray(serializer, value)
        assertTrue(bytes.contentEquals(expectedValue), "Expected ${expectedValue.toHexString(separator = " ")} but got ${bytes.toHexString(separator = " ")}")
    }
}