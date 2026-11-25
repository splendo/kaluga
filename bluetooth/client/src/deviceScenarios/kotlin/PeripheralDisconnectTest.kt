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
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotSame
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

/**
 * Disconnects while requests are pending, requested by the client or caused by the peripheral.
 */
class PeripheralDisconnectTest : PeripheralTest() {

    @Test
    fun readsWritesAndNotifies() = peripheralTest {
        val attributes = connectAndDiscover()
        assertEquals<GattResponse>(GattResponse.ReadSuccess(TestPeripheral.initialValue), within(10.seconds, "read") { attributes.data.read() })
        assertEquals<GattResponse>(GattResponse.WriteSuccess.Acknowledged, within(10.seconds, "write") { attributes.data.write(byteArrayOf(4, 5)) })
        assertEquals<GattResponse>(GattResponse.ReadSuccess(byteArrayOf(4, 5)), within(10.seconds, "read back") { attributes.data.read() })

        val notifications = Channel<ByteArray>(Channel.UNLIMITED)
        assertIs<RemoteCharacteristic.SubscriptionResult.DidSubscribe>(within(10.seconds, "subscribe") { attributes.data.subscribe { notifications.trySend(it) } })
        assertTrue(attributes.data.isNotifying.value)
        attributes.send(Command.Notify(byteArrayOf(7, 7)))
        assertContentEquals(byteArrayOf(7, 7), within(10.seconds, "notification") { notifications.receive() })
    }

    @Test
    fun linkDropWithAReadInFlightAnswersEveryPendingAction() = peripheralTest {
        val attributes = connectAndDiscover()
        attributes.send(Command.HoldRead())
        attributes.send(Command.DropWhenHeld(1.seconds))
        val inFlight = attributes.data.startRead()
        awaitInFlight(inFlight)
        val queuedWrite = attributes.data.startWrite(byteArrayOf(9))
        val queuedRead = attributes.data.startRead()
        awaitInFlight(inFlight, queued = 2)

        val responses = listOf(
            inFlight.response.answer("in-flight read"),
            queuedWrite.response.answer("queued write"),
            queuedRead.response.answer("queued read"),
        )
        awaitDisconnected()
        assertTrue(attributes.data.invalidated.isCompleted)
        // Android may fail the action in flight itself, just before it reports the link as closed.
        assertIs<GattResponse.ReadError>(responses.first())
        assertEquals(List<GattResponse>(2) { GattResponse.DeviceUnavailable }, responses.drop(1))
    }

    @Test
    fun requestedDisconnectWithAHeldReadAnswersEveryPendingAction() = requestedDisconnectWithAHeldRead(Command.HoldRead())

    @Test
    fun requestedDisconnectWithAReadAnsweredBeforeTheLinkCloses() = requestedDisconnectWithAHeldRead(Command.HoldRead(releaseAfter = 700.milliseconds))

    private fun requestedDisconnectWithAHeldRead(hold: Command) = peripheralTest {
        val attributes = connectAndDiscover()
        attributes.send(hold)
        val inFlight = attributes.data.startRead()
        awaitInFlight(inFlight)
        val queuedWrite = attributes.data.startWrite(byteArrayOf(9))
        val queuedMtu = device.requestMtu(100)
        awaitInFlight(inFlight, queued = 2)
        // Gives the read time to reach the peripheral, so it is held there rather than still queued in the stack.
        delay(500)

        val disconnect = scope.async { device.disconnect() }
        val queuedWriteResponse = queuedWrite.response.answer("queued write")
        val queuedMtuResponse = queuedMtu.response.answer("queued MTU")
        val inFlightResponse = inFlight.response.answer("in-flight read")
        disconnect.answer("disconnect")
        awaitDisconnected()
        assertEquals<GattResponse>(GattResponse.DeviceUnavailable, queuedWriteResponse)
        assertEquals<GattResponse>(GattResponse.MTUDeviceUnavailable(100), queuedMtuResponse)
        assertTrue(
            inFlightResponse == GattResponse.DeviceUnavailable || inFlightResponse == GattResponse.ReadSuccess(TestPeripheral.initialValue),
            "in-flight read answered $inFlightResponse",
        )
    }

    @Test
    fun reconnectAfterADropGivesNewObjects() = peripheralTest {
        val old = connectAndDiscover()
        old.send(Command.Drop(200.milliseconds))
        awaitDisconnected()
        within(1.seconds, "old service invalidated") { old.service.invalidated.await() }
        assertTrue(old.data.invalidated.isCompleted)
        assertFalse(old.data.isNotifying.value)

        val staleStart = TimeSource.Monotonic.markNow()
        assertEquals<GattResponse>(GattResponse.DeviceUnavailable, within(1.seconds, "stale read") { old.data.read() })
        assertEquals<GattResponse>(GattResponse.DeviceUnavailable, within(1.seconds, "stale write") { old.data.write(byteArrayOf(1)) })
        assertEquals<RemoteCharacteristic.SubscriptionResult>(
            RemoteCharacteristic.SubscriptionResult.FailedToSubscribe(GattResponse.DeviceUnavailable),
            within(1.seconds, "stale subscribe") { old.data.subscribe { } },
        )
        log("stale calls answered in ${staleStart.elapsedNow()}")

        val new = connectAndDiscover()
        assertNotSame(old.service, new.service)
        assertNotSame(old.data, new.data)
        assertFalse(new.data.invalidated.isCompleted)
        val notifications = Channel<ByteArray>(Channel.UNLIMITED)
        assertIs<RemoteCharacteristic.SubscriptionResult.DidSubscribe>(within(10.seconds, "subscribe after reconnect") { new.data.subscribe { notifications.trySend(it) } })
        new.send(Command.Notify(byteArrayOf(0x42)))
        assertContentEquals(byteArrayOf(0x42), within(10.seconds, "notification after reconnect") { notifications.receive() })
    }

    @Test
    fun linkDropWithAnEnableInFlightFailsTheSubscription() = peripheralTest {
        val attributes = connectAndDiscover()
        attributes.send(Command.HoldCccdWrite())
        attributes.send(Command.DropWhenHeld(1.seconds))
        val subscribe = scope.async { attributes.data.subscribe { } }
        val result = subscribe.answer("subscribe", 20.seconds)
        val notifyingAtAnswer = attributes.data.isNotifying.value
        awaitDisconnected()
        assertFalse(notifyingAtAnswer)
        assertFalse(attributes.data.isNotifying.value)
        assertIs<RemoteCharacteristic.SubscriptionResult.FailedToSubscribe>(result)
    }

    @Test
    fun twoRequestsForTheSameMtuBothComplete() = peripheralTest {
        connectAndDiscover()
        val first = device.requestMtu(185)
        val second = device.requestMtu(185)
        assertIs<GattResponse.MTUSuccess>(first.response.answer("first MTU request"))
        assertIs<GattResponse.MTUSuccess>(second.response.answer("second MTU request"))
    }
}
