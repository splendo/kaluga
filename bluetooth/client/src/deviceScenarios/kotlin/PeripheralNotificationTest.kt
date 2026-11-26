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
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

/**
 * Notifications and indications from the peripheral, and the client's notification state machine.
 */
class PeripheralNotificationTest : PeripheralTest() {

    @Test
    fun indicateOnlyCharacteristicDeliversIndications() = peripheralTest {
        val attributes = connectAndDiscover()
        assertEquals(setOf(CharacteristicProperty.Indicate), attributes.indicate.properties)
        val indications = Channel<ByteArray>(Channel.UNLIMITED)
        assertIs<RemoteCharacteristic.SubscriptionResult.DidSubscribe>(within(10.seconds, "subscribe") { attributes.indicate.subscribe { indications.trySend(it) } })
        assertTrue(attributes.indicate.isNotifying.value)
        attributes.send(Command.Indicate(byteArrayOf(0x11)))
        attributes.send(Command.Indicate(byteArrayOf(0x22)))
        assertContentEquals(byteArrayOf(0x11), within(10.seconds, "first indication") { indications.receive() })
        assertContentEquals(byteArrayOf(0x22), within(10.seconds, "second indication") { indications.receive() })
    }

    @Test
    fun burstOfNotificationsArrivesInOrderAtTheDefaultMtu() = burstOfNotificationsArrivesInOrder(AttMtu.Default)

    @Test
    fun burstOfNotificationsArrivesInOrderAtALargeMtu() = burstOfNotificationsArrivesInOrder(AttMtu.Large)

    private fun burstOfNotificationsArrivesInOrder(mtu: AttMtu) = peripheralTestAt(mtu) { attributes ->
        val notifications = Channel<ByteArray>(Channel.UNLIMITED)
        assertIs<RemoteCharacteristic.SubscriptionResult.DidSubscribe>(within(10.seconds, "subscribe") { attributes.data.subscribe { notifications.trySend(it) } })
        val count = 200
        attributes.send(Command.NotifyBurst(count))
        val received = within(20.seconds, "$count notifications") {
            List(count) { notifications.receive().let { (it[0].toInt() and 0xFF) or (it[1].toInt() and 0xFF shl 8) } }
        }
        assertEquals((0 until count).toList(), received)
    }

    @Test
    fun notificationWhileAReadIsInFlight() = peripheralTest {
        val attributes = connectAndDiscover()
        val notifications = Channel<ByteArray>(Channel.UNLIMITED)
        assertIs<RemoteCharacteristic.SubscriptionResult.DidSubscribe>(within(10.seconds, "subscribe") { attributes.data.subscribe { notifications.trySend(it) } })
        // The peripheral answers the read with its value at release time, so after the notification.
        attributes.send(Command.HoldRead(releaseAfter = 800.milliseconds))
        attributes.send(Command.Notify(byteArrayOf(0x33), after = 300.milliseconds))
        val read = attributes.data.startRead()
        assertContentEquals(byteArrayOf(0x33), within(10.seconds, "notification during the read") { notifications.receive() })
        assertEquals<GattResponse>(GattResponse.ReadSuccess(byteArrayOf(0x33)), read.response.answer("read"))
        assertNull(withTimeoutOrNull(1.seconds) { notifications.receive() }, "the read answer reached the subscriber as a notification")
    }

    /**
     * Two notifications while a read is held, with the read answered by the second value, so each update can be told
     * apart from the read response. Android reports a read response and a notification through different callbacks.
     * CoreBluetooth reports both through `didUpdateValueFor` with nothing to tell them apart, so the first update after
     * the read request answers the read: the first notification, while the read response reaches the subscriber.
     */
    @Test
    fun twoNotificationsWhileAReadIsInFlight() = peripheralTest {
        val attributes = connectAndDiscover()
        val notifications = Channel<ByteArray>(Channel.UNLIMITED)
        assertIs<RemoteCharacteristic.SubscriptionResult.DidSubscribe>(within(10.seconds, "subscribe") { attributes.data.subscribe { notifications.trySend(it) } })
        attributes.send(Command.HoldRead(releaseAfter = 1000.milliseconds))
        attributes.send(Command.Notify(byteArrayOf(0x33), after = 300.milliseconds))
        attributes.send(Command.Notify(byteArrayOf(0x44), after = 600.milliseconds))
        val read = attributes.data.startRead()
        val answer = read.response.answer("read")
        val received = within(10.seconds, "two updates") { List(2) { notifications.receive().toList() } }
        assertNull(withTimeoutOrNull(1.seconds) { notifications.receive() }, "a third update reached the subscriber")
        when (platform.stack) {
            PeripheralScenarioPlatform.Stack.Android -> {
                assertEquals<GattResponse>(GattResponse.ReadSuccess(byteArrayOf(0x44)), answer)
                assertEquals(listOf(listOf<Byte>(0x33), listOf<Byte>(0x44)), received)
            }

            PeripheralScenarioPlatform.Stack.CoreBluetooth -> {
                assertEquals<GattResponse>(GattResponse.ReadSuccess(byteArrayOf(0x33)), answer)
                assertEquals(listOf(listOf<Byte>(0x44), listOf<Byte>(0x44)), received)
            }
        }
    }

    @Test
    fun enableDirectlyFollowedByDisable() = peripheralTest {
        val attributes = connectAndDiscover()
        val enable = assertNotNull(attributes.data.startEnableNotification())
        val disable = assertNotNull(attributes.data.startDisableNotification())
        assertEquals<GattResponse>(GattResponse.WriteSuccess.Acknowledged, enable.response.answer("enable"))
        assertEquals<GattResponse>(GattResponse.WriteSuccess.Acknowledged, disable.response.answer("disable"))
        assertFalse(attributes.data.isNotifying.value)

        val notifications = Channel<ByteArray>(Channel.UNLIMITED)
        assertIs<RemoteCharacteristic.SubscriptionResult.DidSubscribe>(within(10.seconds, "subscribe") { attributes.data.subscribe { notifications.trySend(it) } })
        attributes.send(Command.Notify(byteArrayOf(0x44)))
        assertContentEquals(byteArrayOf(0x44), within(10.seconds, "notification") { notifications.receive() })
    }

    @Test
    fun disableBetweenTwoEnablesIsSuperseded() = peripheralTest {
        val attributes = connectAndDiscover()
        val enable = assertNotNull(attributes.data.startEnableNotification())
        val disable = assertNotNull(attributes.data.startDisableNotification())
        val enableAgain = attributes.data.startEnableNotification()
        assertEquals<GattResponse>(GattResponse.Superseded, disable.response.answer("disable"))
        assertEquals<GattResponse>(GattResponse.WriteSuccess.Acknowledged, enable.response.answer("enable"))
        assertEquals<GattResponse>(GattResponse.WriteSuccess.Acknowledged, enableAgain?.response?.answer("enable again") ?: GattResponse.WriteSuccess.Acknowledged)
        assertTrue(attributes.data.isNotifying.value)

        val notifications = Channel<ByteArray>(Channel.UNLIMITED)
        assertIs<RemoteCharacteristic.SubscriptionResult.DidSubscribe>(within(10.seconds, "subscribe") { attributes.data.subscribe { notifications.trySend(it) } })
        attributes.send(Command.Notify(byteArrayOf(0x55)))
        assertContentEquals(byteArrayOf(0x55), within(10.seconds, "notification") { notifications.receive() })
    }
}
