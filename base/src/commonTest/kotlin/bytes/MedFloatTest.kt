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
import com.splendo.kaluga.base.bytes.decodeMedFloat16
import com.splendo.kaluga.base.bytes.toByteArray
import com.splendo.kaluga.base.utils.MedFloat16
import kotlin.math.pow
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class MedFloatTest {

    @Test
    fun medFloat16() {
        val byteValue = MedFloat16(123400000.0).toByteArray()
        assertContentEquals((5 * 2.0.pow(12).toInt() + 1234).toUShort().toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST), byteValue)
        assertEquals(MedFloat16(123400000.0), byteValue.decodeMedFloat16(0))
    }
}