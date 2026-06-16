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

package bytes

import com.splendo.kaluga.base.bytes.ByteOrder
import com.splendo.kaluga.base.bytes.MedFloat16
import com.splendo.kaluga.base.bytes.MedFloat32
import com.splendo.kaluga.base.bytes.decodeMedFloat16
import com.splendo.kaluga.base.bytes.decodeMedFloat32
import com.splendo.kaluga.base.bytes.toByteArray
import com.splendo.kaluga.base.bytes.toInt24
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class MedFloatTest {

    @Test
    fun medFloat16() {
        assertMedFloat16(123400000.0, byteArrayOf(0xD2.toByte(), 0x54))
        assertMedFloat16(-123400000.0, byteArrayOf(0x2E.toByte(), 0x5B))
        assertMedFloat16(0.25, byteArrayOf(0x19, 0xE0.toByte()))
        assertMedFloat16(-0.25, byteArrayOf(0xE7.toByte(), 0xEF.toByte()))
    }

    @Test
    fun medFloat32() {
        assertMedFloat32(123400000.0, 1234000, 2)
        assertMedFloat32(-123400000.0, -1234000, 2)
        assertMedFloat32(0.25, 25, -2)
        assertMedFloat32(-0.25, -25, -2)
    }

    private fun assertMedFloat16(value: Double, expected: ByteArray) {
        val byteValue = MedFloat16(value).toByteArray()

        assertContentEquals(expected, byteValue)
        assertEquals(MedFloat16(value), byteValue.decodeMedFloat16(0))
    }

    private fun assertMedFloat32(value: Double, expectedMantissa: Int, expectedExponent: Byte) {
        val byteValue = MedFloat32(value).toByteArray()

        val expected = expectedMantissa.toInt24().toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST) + expectedExponent
        assertContentEquals(expected, byteValue)
        assertEquals(MedFloat32(value), byteValue.decodeMedFloat32(0))
    }
}
