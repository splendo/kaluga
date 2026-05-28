/*
 Copyright (c) 2020. Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.base.utils

import com.splendo.kaluga.test.base.BaseTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DataUtilsTest : BaseTest() {

    @Test
    fun testByteArrayConverter() {
        val byteArray = byteArrayOf(0x2F, 0x4A, 0x0, 0x01)
        val byteData = byteArray.toNSData()
        assertEquals(byteArray.size.toULong(), byteData.length)
        assertEquals(true, byteData.toByteArray().contentEquals(byteArray))
    }
}
