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

package com.splendo.kaluga.bluetooth.server

import com.splendo.kaluga.bluetooth.server.TestCentral.Instruction
import kotlinx.coroutines.flow.first
import org.junit.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

/**
 * Notifications from the server to a subscribed central.
 */
class CentralNotificationTest : CentralTest() {

    @Test
    fun notifiesASubscribedCentral() = centralTest {
        assertEquals(0, status(Instruction.Subscribe(TestCentral.NOTIFY)))
        assertEquals(listOf(central), within(10.seconds, "subscribed") { notify.subscribedDevices.first { it.isNotEmpty() } })
        assertTrue(notify.notifyAll(byteArrayOf(0x21, 0x22)))
        val (target, value) = within(10.seconds, "notification at the central") { forwarded.receive() }
        assertEquals(TestCentral.NOTIFY, target)
        assertContentEquals(byteArrayOf(0x21, 0x22), value)

        assertEquals(0, status(Instruction.Unsubscribe(TestCentral.NOTIFY)))
        assertEquals(emptyList(), within(10.seconds, "unsubscribed") { notify.subscribedDevices.first { it.isEmpty() } })
    }

    @Test
    fun disconnectEndsTheSubscription() = centralTest {
        assertEquals(0, status(Instruction.Subscribe(TestCentral.NOTIFY)))
        within(10.seconds, "subscribed") { notify.subscribedDevices.first { it.isNotEmpty() } }
        assertEquals(0, status(Instruction.Disconnect))
        awaitCentralDisconnected()
        assertEquals(emptyList(), within(10.seconds, "subscription ended") { notify.subscribedDevices.first { it.isEmpty() } })
        assertEquals(emptyList(), control.subscribedDevices.value)
    }
}
