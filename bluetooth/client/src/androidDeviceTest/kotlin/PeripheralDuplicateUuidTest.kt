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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.seconds

/**
 * A service holding two characteristics with the same UUID, each with its own value.
 */
class PeripheralDuplicateUuidTest : PeripheralTest() {

    @Test
    fun bothAreDiscovered() = peripheralTest {
        val twins = connectAndDiscover().twins
        assertEquals(listOf(TestPeripheral.twin, TestPeripheral.twin), twins.map { it.uuid })
    }

    @Test
    fun readsReachEachTwin() = peripheralTest {
        val twins = connectAndDiscover().twins
        assertEquals<List<GattResponse>>(
            TestPeripheral.twinValues.map { GattResponse.ReadSuccess(it) },
            twins.mapIndexed { index, twin -> within(10.seconds, "read twin $index") { twin.read() } },
        )
    }

    @Test
    fun writesReachEachTwin() = peripheralTest {
        val twins = connectAndDiscover().twins
        assertEquals<GattResponse>(GattResponse.WriteSuccess.Acknowledged, within(10.seconds, "write twin 1") { twins[1].write(byteArrayOf(0x62)) })
        assertEquals<List<GattResponse>>(
            listOf(GattResponse.ReadSuccess(TestPeripheral.twinValues[0]), GattResponse.ReadSuccess(byteArrayOf(0x62))),
            twins.mapIndexed { index, twin -> within(10.seconds, "read twin $index") { twin.read() } },
        )
    }

    // The client routes a notification by the UUID of its characteristic, to the twin whose notifications were enabled
    // last, whichever twin sent it.
    @Test
    fun notificationsOfBothTwinsReachTheTwinEnabledLast() = peripheralTest {
        val attributes = connectAndDiscover()
        val notifications = List(2) { Channel<ByteArray>(Channel.UNLIMITED) }
        attributes.twins.forEachIndexed { index, twin ->
            assertIs<RemoteCharacteristic.SubscriptionResult.DidSubscribe>(within(10.seconds, "subscribe twin $index") { twin.subscribe { notifications[index].trySend(it) } })
        }
        attributes.send(Command.NotifyTwin(1, byteArrayOf(0x71)))
        attributes.send(Command.NotifyTwin(0, byteArrayOf(0x70)))
        assertContentEquals(byteArrayOf(0x71), within(10.seconds, "notification of twin 1") { notifications[1].receive() })
        assertContentEquals(byteArrayOf(0x70), within(10.seconds, "notification of twin 0 at twin 1") { notifications[1].receive() })
        assertNull(withTimeoutOrNull(1.seconds) { notifications[0].receive() })
    }
}
