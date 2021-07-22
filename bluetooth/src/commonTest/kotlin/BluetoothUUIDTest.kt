/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.test.BaseTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class BluetoothUUIDTest: BaseTest() {

    @Test
    fun randomUUID__it_generates_an_random_full_UUID() {
        val uuid = randomUUID()

        assertEquals(36, uuid.uuidString.length, "it should generate a long UUID")

        val itMatches = Regex("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}", RegexOption.IGNORE_CASE).matches(uuid.uuidString)
        assertTrue(itMatches, "UUID should contain of 5 sections separated by \'-\'. Sections should contain 8, 4,4,4 and 12 characters. Actual UUID: $uuid")
    }

    @Test
    fun uuidFrom_invalid_string__it_throws_an_invalid_format_exception() {
        assertFailsWith<UUIDException.InvalidFormat> { uuidFrom("") }
        assertFailsWith<UUIDException.InvalidFormat> { uuidFrom("00000000-1234-1234-1234") }
        assertFailsWith<UUIDException.InvalidFormat> { uuidFrom("00000000") }
        assertFailsWith<UUIDException.InvalidFormat> { uuidFrom("00000000-1234-1234-1234-000000000000-<invalid>") }
        assertFailsWith<UUIDException.InvalidFormat> { uuidFrom("00000000-1234-1234-1234-000000i00000") }
        assertFailsWith<UUIDException.InvalidFormat> { uuidFrom("00000000_1234_1234_1234_000000000000") }
        assertFailsWith<UUIDException.InvalidFormat> { uuidFrom("0") }
    }
}