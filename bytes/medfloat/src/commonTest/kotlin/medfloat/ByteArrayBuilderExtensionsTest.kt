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

package com.splendo.kaluga.bytes.medfloat

import com.splendo.kaluga.bytes.ByteOrder
import com.splendo.kaluga.bytes.buildByteArray
import kotlin.test.Test
import kotlin.test.assertContentEquals

class ByteArrayBuilderExtensionsTest {

    @Test
    fun buildByteArray() {
        val byteArray = buildByteArray {
            add(byte = 0x01)
            add(MedFloat16(0.1234))
            add(MedFloat32(0.567890))
        }

        assertContentEquals(
            byteArrayOf(0x01) + MedFloat16(0.1234).toByteArray() + MedFloat32(0.567890).toByteArray(),
            byteArray,
        )
    }

    @Test
    fun buildByteArrayMostSignificantFirst() {
        val byteArray = buildByteArray(ByteOrder.MOST_SIGNIFICANT_FIRST) {
            add(byte = 0x01)
            add(MedFloat16(0.1234))
            add(MedFloat32(0.567890))
        }

        assertContentEquals(
            MedFloat32(0.567890).toByteArray() + MedFloat16(0.1234).toByteArray() + byteArrayOf(0x01),
            byteArray,
        )
    }
}
