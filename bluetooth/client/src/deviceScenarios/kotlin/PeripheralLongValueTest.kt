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
import com.splendo.kaluga.bluetooth.TestPeripheral.counting
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

/**
 * Values that do not fit in one ATT packet. Without an MTU request the link keeps the default MTU of 23, so a value
 * of more than 22 bytes takes a long read or a long write.
 */
class PeripheralLongValueTest : PeripheralTest() {

    private companion object {
        const val DEFAULT_MTU = 23
        const val MAX_ATTRIBUTE_LENGTH = 512
    }

    @Test
    fun longRead() = peripheralTest {
        val attributes = connectAndDiscover()
        attributes.send(Command.SetLongValue(300))
        assertEquals<GattResponse>(GattResponse.ReadSuccess(counting(300)), within(10.seconds, "long read") { attributes.long.read() })
    }

    @Test
    fun readOfTheMaximumAttributeLength() = peripheralTest {
        val attributes = connectAndDiscover()
        attributes.send(Command.SetLongValue(MAX_ATTRIBUTE_LENGTH))
        assertEquals<GattResponse>(GattResponse.ReadSuccess(counting(MAX_ATTRIBUTE_LENGTH)), within(10.seconds, "long read") { attributes.long.read() })
    }

    // The stack stops reading at the maximum length of an attribute and reports what it has as the whole value.
    @Test
    fun readLongerThanTheMaximumAttributeLength() = peripheralTest {
        val attributes = connectAndDiscover()
        attributes.send(Command.SetLongValue(600))
        assertEquals<GattResponse>(GattResponse.ReadSuccess(counting(MAX_ATTRIBUTE_LENGTH)), within(10.seconds, "long read") { attributes.long.read() })
    }

    @Test
    fun longWrite() = peripheralTest {
        val attributes = connectAndDiscover()
        assertEquals<GattResponse>(GattResponse.WriteSuccess.Acknowledged, within(10.seconds, "long write") { attributes.long.write(counting(300)) })
        assertEquals<GattResponse>(GattResponse.ReadSuccess(counting(300)), within(10.seconds, "read back") { attributes.long.read() })
    }

    @Test
    fun writeOfTheMaximumAttributeLength() = peripheralTest {
        val attributes = connectAndDiscover()
        assertEquals<GattResponse>(GattResponse.WriteSuccess.Acknowledged, within(10.seconds, "long write") { attributes.long.write(counting(MAX_ATTRIBUTE_LENGTH)) })
        assertEquals<GattResponse>(GattResponse.ReadSuccess(counting(MAX_ATTRIBUTE_LENGTH)), within(10.seconds, "read back") { attributes.long.read() })
    }

    @Test
    fun writeLongerThanTheMaximumAttributeLength() = peripheralTest {
        val attributes = connectAndDiscover()
        assertEquals<GattResponse>(GattResponse.InvalidAttributeValueLength, within(10.seconds, "long write") { attributes.long.write(counting(600)) })
        assertEquals<GattResponse>(GattResponse.ReadSuccess(TestPeripheral.initialValue), within(10.seconds, "read after the write") { attributes.data.read() })
    }

    @Test
    fun notificationOfTheFullPayloadAtTheDefaultMtu() = peripheralTest {
        val attributes = connectAndDiscover()
        val notifications = Channel<ByteArray>(Channel.UNLIMITED)
        assertIs<RemoteCharacteristic.SubscriptionResult.DidSubscribe>(within(10.seconds, "subscribe") { attributes.data.subscribe { notifications.trySend(it) } })
        attributes.send(Command.NotifyLength(DEFAULT_MTU - 3))
        assertContentEquals(counting(DEFAULT_MTU - 3), within(10.seconds, "notification") { notifications.receive() })
    }

    // A negotiated MTU leaves room for a notification longer than an attribute can hold. The stack drops such a
    // notification without telling the app, so the longest one that arrives holds the maximum attribute length.
    @Test
    fun notificationLongerThanTheMaximumAttributeLength() = peripheralTest {
        val attributes = connectAndDiscover()
        val mtu = assertIs<GattResponse.MTUSuccess>(device.requestMtu(517).response.answer("MTU request")).mtu
        assertTrue(mtu - 3 > MAX_ATTRIBUTE_LENGTH, "negotiated MTU $mtu")
        val notifications = Channel<ByteArray>(Channel.UNLIMITED)
        assertIs<RemoteCharacteristic.SubscriptionResult.DidSubscribe>(within(10.seconds, "subscribe") { attributes.data.subscribe { notifications.trySend(it) } })
        attributes.send(Command.NotifyLength(mtu - 3))
        assertNull(withTimeoutOrNull(2.seconds) { notifications.receive() }, "a notification of ${mtu - 3} bytes arrived")
        attributes.send(Command.NotifyLength(MAX_ATTRIBUTE_LENGTH))
        assertContentEquals(counting(MAX_ATTRIBUTE_LENGTH), within(10.seconds, "notification") { notifications.receive() })
    }

    // Android confirms a write without response once the stack has taken it, so it answers as acknowledged.
    @Test
    fun writesWithoutResponseInQuickSuccession() = peripheralTest {
        val attributes = connectAndDiscover()
        val count = 100
        val writes = List(count) { attributes.sink.startWrite(byteArrayOf(it.toByte()), WriteType.WithoutResponse) }
        val responses = writes.mapIndexed { index, write -> write.response.answer("write without response $index") }
        assertEquals(List<GattResponse>(count) { GattResponse.WriteSuccess.Acknowledged }, responses)
        assertEquals<GattResponse>(GattResponse.ReadSuccess(counting(count)), within(10.seconds, "read what arrived") { attributes.sink.read() })
    }
}
