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

import com.splendo.kaluga.bluetooth.NetsimPeripheral.Command
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

/**
 * A peripheral that never answers a read. The client has no timeout of its own: the stack gives up after the 30 second
 * ATT timeout, and since a bearer that timed out may not be used again, it drops the link.
 */
class NetsimUnansweredReadTest : NetsimTest() {

    @Test
    fun readThatIsNeverAnswered() = netsimTest(timeout = 150.seconds) {
        val attributes = connectAndDiscover()
        attributes.send(Command.HoldRead())
        val unanswered = attributes.data.startRead()
        awaitInFlight(unanswered)
        val queued = attributes.data.startRead()
        val start = TimeSource.Monotonic.markNow()
        val response = unanswered.response.answer("unanswered read", 60.seconds)
        log("unanswered read failed after ${start.elapsedNow()}")
        // Android fails the read with its GATT_ERROR just before it reports the link as closed.
        assertEquals<GattResponse>(GattResponse.ApplicationError(0x85), response)
        assertEquals<GattResponse>(GattResponse.DeviceUnavailable, queued.response.answer("queued read"))
        awaitDisconnected()
        assertTrue(attributes.data.invalidated.isCompleted)

        val reconnected = connectAndDiscover()
        assertEquals<GattResponse>(GattResponse.ReadSuccess(NetsimPeripheral.initialValue), within(10.seconds, "read after reconnecting") { reconnected.data.read() })
    }
}
