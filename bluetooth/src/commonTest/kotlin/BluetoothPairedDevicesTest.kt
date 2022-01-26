/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.device.randomIdentifier
import kotlinx.coroutines.flow.Flow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BluetoothPairedDevicesTest : BluetoothFlowTest<List<Identifier>>() {

    private companion object {
        val IDENTIFIER = randomIdentifier()
        val FILTER = setOf(randomUUID())
    }

    override val flow: suspend () -> Flow<List<Identifier>> = {
        setup(Setup.BLUETOOTH)
        bluetooth.pairedDevices(FILTER)
    }

    @Test
    fun testEmptyPairedDevices() = testWithFlow {
        test {
            assertTrue(it.isEmpty())
        }
    }

    @Test
    fun testNonEmptyPairedDevices() = testWithFlow {

        test { /* this will init scanner */ }

        // Mock paired devices list
        mockBaseScanner().pairedDevices.emit(listOf(IDENTIFIER))

        test {
            assertTrue(it.isNotEmpty())
            assertEquals(IDENTIFIER, it.first())
        }
    }
}
