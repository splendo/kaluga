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

import com.splendo.kaluga.bluetooth.PeripheralScenarioPlatform.Stack
import com.splendo.kaluga.bluetooth.AttMtu.Companion.MAX_ATTRIBUTE_LENGTH
import com.splendo.kaluga.bluetooth.TestPeripheral.Command
import com.splendo.kaluga.bluetooth.TestPeripheral.counting
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.seconds

/**
 * Values that do not fit in one ATT packet at the default MTU of 23, where a value of more than 22 bytes takes a long
 * read or a long write, each at the default MTU and at the largest one.
 */
class PeripheralLongValueTest : PeripheralTest() {

    @Test
    fun longReadAtTheDefaultMtu() = longRead(AttMtu.Default)

    @Test
    fun longReadAtALargeMtu() = longRead(AttMtu.Large)

    private fun longRead(mtu: AttMtu) = peripheralTestAt(mtu) { attributes ->
        attributes.send(Command.SetLongValue(300))
        assertEquals<GattResponse>(GattResponse.ReadSuccess(counting(300)), within(10.seconds, "long read") { attributes.long.read() })
    }

    @Test
    fun readOfTheMaximumAttributeLengthAtTheDefaultMtu() = readOfTheMaximumAttributeLength(AttMtu.Default)

    @Test
    fun readOfTheMaximumAttributeLengthAtALargeMtu() = readOfTheMaximumAttributeLength(AttMtu.Large)

    private fun readOfTheMaximumAttributeLength(mtu: AttMtu) = peripheralTestAt(mtu) { attributes ->
        attributes.send(Command.SetLongValue(MAX_ATTRIBUTE_LENGTH))
        assertEquals<GattResponse>(GattResponse.ReadSuccess(counting(MAX_ATTRIBUTE_LENGTH)), within(10.seconds, "long read") { attributes.long.read() })
    }

    @Test
    fun readLongerThanTheMaximumAttributeLengthAtTheDefaultMtu() = readLongerThanTheMaximumAttributeLength(AttMtu.Default)

    @Test
    fun readLongerThanTheMaximumAttributeLengthAtALargeMtu() = readLongerThanTheMaximumAttributeLength(AttMtu.Large)

    // The stack stops reading at the maximum length of an attribute and reports what it has as the whole value. Android
    // cuts the value at that length; CoreBluetooth keeps every response it read, of MTU - 1 bytes each, so the last one
    // reaches past it.
    private fun readLongerThanTheMaximumAttributeLength(mtu: AttMtu) = peripheralTestAt(mtu) { attributes ->
        attributes.send(Command.SetLongValue(600))
        val response = mtu.size - 1
        val length = when (platform.stack) {
            Stack.Android -> MAX_ATTRIBUTE_LENGTH
            Stack.CoreBluetooth -> (MAX_ATTRIBUTE_LENGTH + response - 1) / response * response
        }
        assertEquals<GattResponse>(GattResponse.ReadSuccess(counting(length)), within(10.seconds, "long read") { attributes.long.read() })
    }

    @Test
    fun longWriteAtTheDefaultMtu() = longWrite(AttMtu.Default)

    @Test
    fun longWriteAtALargeMtu() = longWrite(AttMtu.Large)

    private fun longWrite(mtu: AttMtu) = peripheralTestAt(mtu) { attributes ->
        assertEquals<GattResponse>(GattResponse.WriteSuccess.Acknowledged, within(10.seconds, "long write") { attributes.long.write(counting(300)) })
        assertEquals<GattResponse>(GattResponse.ReadSuccess(counting(300)), within(10.seconds, "read back") { attributes.long.read() })
    }

    @Test
    fun writeOfTheMaximumAttributeLengthAtTheDefaultMtu() = writeOfTheMaximumAttributeLength(AttMtu.Default)

    @Test
    fun writeOfTheMaximumAttributeLengthAtALargeMtu() = writeOfTheMaximumAttributeLength(AttMtu.Large)

    private fun writeOfTheMaximumAttributeLength(mtu: AttMtu) = peripheralTestAt(mtu) { attributes ->
        assertEquals<GattResponse>(GattResponse.WriteSuccess.Acknowledged, within(10.seconds, "long write") { attributes.long.write(counting(MAX_ATTRIBUTE_LENGTH)) })
        assertEquals<GattResponse>(GattResponse.ReadSuccess(counting(MAX_ATTRIBUTE_LENGTH)), within(10.seconds, "read back") { attributes.long.read() })
    }

    @Test
    fun writeLongerThanTheMaximumAttributeLengthAtTheDefaultMtu() = writeLongerThanTheMaximumAttributeLength(AttMtu.Default)

    @Test
    fun writeLongerThanTheMaximumAttributeLengthAtALargeMtu() = writeLongerThanTheMaximumAttributeLength(AttMtu.Large)

    private fun writeLongerThanTheMaximumAttributeLength(mtu: AttMtu) = peripheralTestAt(mtu) { attributes ->
        assertEquals<GattResponse>(GattResponse.InvalidAttributeValueLength, within(10.seconds, "long write") { attributes.long.write(counting(600)) })
        assertEquals<GattResponse>(GattResponse.ReadSuccess(TestPeripheral.initialValue), within(10.seconds, "read after the write") { attributes.data.read() })
    }

    @Test
    fun notificationOfTheFullPayloadAtTheDefaultMtu() = peripheralTestAt(AttMtu.Default) { attributes ->
        val notifications = Channel<ByteArray>(Channel.UNLIMITED)
        assertIs<RemoteCharacteristic.SubscriptionResult.DidSubscribe>(within(10.seconds, "subscribe") { attributes.data.subscribe { notifications.trySend(it) } })
        attributes.send(Command.NotifyLength(AttMtu.Default.size - 3))
        assertContentEquals(counting(AttMtu.Default.size - 3), within(10.seconds, "notification") { notifications.receive() })
    }

    // A large MTU leaves room for a notification longer than an attribute can hold. Android drops such a notification
    // without telling the app; CoreBluetooth hands it over whole.
    @Test
    fun notificationOfTheFullPayloadAtALargeMtu() = peripheralTestAt(AttMtu.Large) { attributes ->
        val notifications = Channel<ByteArray>(Channel.UNLIMITED)
        assertIs<RemoteCharacteristic.SubscriptionResult.DidSubscribe>(within(10.seconds, "subscribe") { attributes.data.subscribe { notifications.trySend(it) } })
        attributes.send(Command.NotifyLength(AttMtu.Large.size - 3))
        val longest = withTimeoutOrNull(2.seconds) { notifications.receive() }
        when (platform.stack) {
            Stack.Android -> assertNull(longest, "a notification of ${AttMtu.Large.size - 3} bytes arrived")
            Stack.CoreBluetooth -> assertContentEquals(counting(AttMtu.Large.size - 3), longest)
        }
        attributes.send(Command.NotifyLength(MAX_ATTRIBUTE_LENGTH))
        assertContentEquals(counting(MAX_ATTRIBUTE_LENGTH), within(10.seconds, "notification") { notifications.receive() })
    }

    @Test
    fun writesWithoutResponseInQuickSuccessionAtTheDefaultMtu() = writesWithoutResponseInQuickSuccession(AttMtu.Default)

    @Test
    fun writesWithoutResponseInQuickSuccessionAtALargeMtu() = writesWithoutResponseInQuickSuccession(AttMtu.Large)

    // Android confirms a write without response once the stack has taken it, so it answers as acknowledged. CoreBluetooth
    // confirms nothing, so the answer tells that the peripheral was ready to take the write.
    private fun writesWithoutResponseInQuickSuccession(mtu: AttMtu) = peripheralTestAt(mtu) { attributes ->
        val count = 100
        val writes = List(count) { attributes.sink.startWrite(byteArrayOf(it.toByte()), WriteType.WithoutResponse) }
        val responses = writes.mapIndexed { index, write -> write.response.answer("write without response $index") }
        val expected = when (platform.stack) {
            Stack.Android -> GattResponse.WriteSuccess.Acknowledged
            Stack.CoreBluetooth -> GattResponse.WriteSuccess.Ready
        }
        assertEquals(List<GattResponse>(count) { expected }, responses)
        assertEquals<GattResponse>(GattResponse.ReadSuccess(counting(count)), within(10.seconds, "read what arrived") { attributes.sink.read() })
    }
}
