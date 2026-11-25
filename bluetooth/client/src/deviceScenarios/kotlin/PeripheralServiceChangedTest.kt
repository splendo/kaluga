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

import com.splendo.kaluga.bluetooth.TestPeripheral.Command
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

/**
 * The peripheral adds a service while connected and indicates Service Changed. Android does not subscribe to Service
 * Changed on a peripheral it is not bonded with, so the peripheral sends the indication regardless, as one that kept
 * the subscription of a bonded client would.
 */
class PeripheralServiceChangedTest : PeripheralTest() {

    // Android rediscovers the services itself and reports onServiceChanged, which the client does not handle: the
    // objects of the old table stay valid and the new service stays out of sight until the next connection.
    @Test
    fun serviceChangedWhileConnectedLeavesTheOldTable() = peripheralTest {
        val old = connectAndDiscover()
        old.send(Command.ChangeServices(after = 200.milliseconds))
        assertNull(withTimeoutOrNull(3.seconds) { old.service.invalidated.await() }, "the old table was invalidated")
        val services = device.discoveredServices().first()
        assertEquals(old.services, services)
        assertTrue(services.none { it.uuid == TestPeripheral.extraService })
        assertEquals<GattResponse>(GattResponse.ReadSuccess(TestPeripheral.initialValue), within(10.seconds, "read through the old table") { old.data.read() })
    }
}
