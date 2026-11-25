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
package com.splendo.kaluga.bluetooth.views

import com.splendo.kaluga.base.test.BaseTest
import com.splendo.kaluga.base.test.testRunBlocking
import com.splendo.kaluga.bluetooth.CharacteristicProperty
import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.test.ConnectedMockClient
import com.splendo.kaluga.bluetooth.test.characteristic
import com.splendo.kaluga.bluetooth.test.connectedMockClient
import com.splendo.kaluga.bluetooth.test.server.MockBluetoothServerBuilder
import com.splendo.kaluga.bluetooth.test.server.MockConnectedDevice
import com.splendo.kaluga.bytes.layout.ByteArrayEndedBeforeSerializationCompleted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeout
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.time.Duration.Companion.seconds

/**
 * A characteristic typed by a layout with a `@Trailing` tail: the generated client views the value as the stack holds
 * it, unchecked, so a value a peripheral sends without its tail reads the tail as absent rather than past the end of the array.
 */
class DeviceStatusViewTest : BaseTest() {

    private val full = DeviceStatusView(battery = 80, voltage = 3300u, serviceVersion = 0x0102u, serial = 0x1122334455667788u).bytes
    private val tailless = DeviceStatusView(battery = 80, voltage = 3300u, serviceVersion = null, serial = null).bytes
    private val middle = DeviceStatusView(battery = 80, voltage = 3300u, serviceVersion = 0x0102u, serial = null).bytes

    private suspend fun connect(scope: CoroutineScope): ConnectedMockClient = scope.connectedMockClient {
        uuid = RemoteStatusService.UUID
        characteristics {
            characteristic {
                uuid = RemoteStatusCharacteristic.UUID
                properties += setOf(CharacteristicProperty.Read, CharacteristicProperty.Write, CharacteristicProperty.Notify)
            }
        }
    }

    @Test
    fun sendsTheTailOnlyWhenItIsThere() {
        assertEquals(3, tailless.size)
        assertEquals(5, middle.size)
        assertEquals(13, full.size)
    }

    @Test
    fun clientReadsAShorterValueWithoutItsTail() = testRunBlocking {
        withTimeout(5.seconds) {
            val mock = connect(this)
            try {
                val status = StatusDeviceClient.bluetooth(mock.client, mock.identifier).filterNotNull().first().statusService.status
                for (bytes in listOf(tailless, middle, full)) {
                    mock.characteristicWrapper(RemoteStatusService.UUID, RemoteStatusCharacteristic.UUID).updateValue(bytes)
                    val read = async { status.readStatus() }
                    mock.pump()
                    val view = assertIs<StatusCharacteristicReadResponse.Success>(read.await()).response
                    assertSame(bytes, view.bytes)
                    assertEquals(80, view.battery)
                    assertEquals(3300u, view.voltage)
                    assertEquals(if (bytes === tailless) null else 0x0102u, view.serviceVersion)
                    assertEquals(if (bytes === full) 0x1122334455667788u else null, view.serial)
                }
            } finally {
                mock.close()
            }
        }
    }

    @Test
    fun clientNotifiesAShorterValueWithoutItsTail() = testRunBlocking {
        withTimeout(5.seconds) {
            val mock = connect(this)
            try {
                val status = StatusDeviceClient.bluetooth(mock.client, mock.identifier).filterNotNull().first().statusService.status
                val notified = async { status.status.first() }
                mock.pump()
                mock.connectionManager.notify(RemoteStatusCharacteristic.UUID, tailless)
                val view = notified.await()
                assertSame(tailless, view.bytes)
                assertNull(view.serviceVersion)
                assertNull(view.serial)
            } finally {
                mock.close()
            }
        }
    }

    @Test
    fun clientReadsAValueEndingInsideItsTailAsTooShort() {
        // An unchecked view of bytes that end inside a trailing field throws rather than reading past them.
        val cut = DeviceStatusView(full.copyOf(8))
        assertEquals(0x0102u, cut.serviceVersion)
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { cut.serial }
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { DeviceStatusView.wrap(full.copyOf(8)) }
    }

    private class StatusDelegate(val status: DeviceStatusView) : LocalStatusCharacteristic.Delegate {
        val written = mutableListOf<DeviceStatusView>()
        val failures = mutableListOf<Exception>()

        override suspend fun LocalStatusCharacteristic.onReadStatus(identifier: Identifier) = StatusCharacteristicReadResponse.Success(status)

        override suspend fun LocalStatusCharacteristic.onWriteStatus(status: DeviceStatusView, identifier: Identifier): GattResponse.WriteResponse {
            written += status
            return GattResponse.WriteSuccess.Acknowledged
        }

        override suspend fun LocalStatusCharacteristic.onFailedToWriteStatus(exception: Exception, identifier: Identifier): GattResponse.WriteResponse {
            failures += exception
            return GattResponse.ApplicationError(0x80)
        }

        override fun LocalStatusCharacteristic.onSubscribeToStatus(identifier: Identifier) = Unit

        override fun LocalStatusCharacteristic.onUnsubscribeToStatus(identifier: Identifier) = Unit
    }

    private class ServerDelegate(status: StatusDelegate) : StatusDeviceServer.Delegate {
        override val statusServiceDelegate = object : LocalStatusService.Delegate {
            override val statusDelegate = status
        }
    }

    @Test
    fun serverWrapsAShorterWrittenValueAndWaitsForTheRestOfOneEndingInsideItsTail() = testRunBlocking {
        withTimeout(10.seconds) {
            val delegate = StatusDelegate(DeviceStatusView(full))
            val builder = MockBluetoothServerBuilder()
            val server = StatusDeviceServer.bluetooth(builder, ServerDelegate(delegate))
            val mock = builder.createdServers.last()
            try {
                assertIs<GattResponse.WriteSuccess>(mock.triggerWrite(LocalStatusCharacteristic.UUID, tailless))
                val written = delegate.written.single()
                assertSame(tailless, written.bytes)
                assertNull(written.serviceVersion)
                assertNull(written.serial)

                // A value ending inside its tail is not complete yet: a long write delivers the rest.
                val device = MockConnectedDevice()
                assertIs<GattResponse.WriteSuccess>(mock.triggerWrite(LocalStatusCharacteristic.UUID, full.copyOf(8), device))
                assertEquals(1, delegate.written.size)
                assertIs<GattResponse.WriteSuccess>(mock.triggerWrite(LocalStatusCharacteristic.UUID, full.copyOfRange(8, full.size), device, offset = 8))
                assertEquals(0x1122334455667788u, delegate.written.last().serial)
                assertEquals(emptyList(), delegate.failures)
            } finally {
                server.close()
            }
        }
    }
}
