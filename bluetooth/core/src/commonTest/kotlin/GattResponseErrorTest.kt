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

package com.splendo.kaluga.bluetooth

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GattResponseErrorTest {

    @Test
    fun knownCodes() {
        assertEquals<GattResponse.Error>(GattResponse.ReadNotPermitted, GattResponse.Error.from(0x02))
        assertEquals<GattResponse.Error>(GattResponse.WriteRequestRejected, GattResponse.Error.from(0xFC))
    }

    @Test
    fun applicationCodes() {
        assertEquals<GattResponse.Error>(GattResponse.ApplicationError(0x80), GattResponse.Error.from(0x80))
        assertEquals<GattResponse.Error>(GattResponse.ApplicationError(0x9F), GattResponse.Error.from(0x9F))
    }

    // A peer or the platform may report a code ATT reserves, such as CoreBluetooth answering a read its app left
    // unanswered with 0xF1, or a status of the platform's own, such as Android's GATT_FAILURE.
    @Test
    fun codesWithoutAnError() {
        listOf(0x20, 0xA0, 0xF1, 0x101).forEach { assertEquals(it, GattResponse.Error.from(it).statusCode) }
    }

    @Test
    fun successIsNoError() {
        assertFailsWith<IllegalArgumentException> { GattResponse.Error.from(0x00) }
    }
}
