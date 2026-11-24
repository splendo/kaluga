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

import com.splendo.kaluga.base.test.BaseTest
import platform.Foundation.NSData
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Foundation.dataUsingEncoding
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DataUtilsTest : BaseTest() {

    @Test
    fun testByteArrayConverter() {
        val byteArray = byteArrayOf(0x2F, 0x4A, 0x0, 0x01)
        val byteData = byteArray.toNSData()
        assertEquals(byteArray.size.toLong(), byteData.length.toLong())
        assertEquals(true, byteData.toByteArray().contentEquals(byteArray))
    }

    @Test
    fun testEmptyRoundTrip() {
        val data = byteArrayOf().toNSData()
        assertEquals(0L, data.length.toLong())
        assertContentEquals(byteArrayOf(), data.toByteArray())
        assertContentEquals(byteArrayOf(), NSData().toByteArray())
    }

    @Test
    fun testEveryByteValueRoundTrips() {
        val byteArray = ByteArray(256) { it.toByte() }
        assertContentEquals(byteArray, byteArray.toNSData().toByteArray())
    }

    @Test
    fun testLargeRoundTrip() {
        val byteArray = ByteArray(4 * 1024 * 1024 + 3) { (it * 31 + it / 256).toByte() }
        val data = byteArray.toNSData()
        assertEquals(byteArray.size.toLong(), data.length.toLong())
        assertContentEquals(byteArray, data.toByteArray())
    }

    @Test
    fun testDataNotMadeFromAByteArray() {
        val data = assertNotNull(NSString.create(string = "Kaluga").dataUsingEncoding(NSUTF8StringEncoding))
        assertContentEquals("Kaluga".encodeToByteArray(), data.toByteArray())
    }
}
