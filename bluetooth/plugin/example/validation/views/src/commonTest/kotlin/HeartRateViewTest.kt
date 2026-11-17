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
import com.splendo.kaluga.bluetooth.test.MockDescriptorWrapper
import com.splendo.kaluga.bluetooth.test.characteristic
import com.splendo.kaluga.bluetooth.test.connectedMockClient
import com.splendo.kaluga.bluetooth.test.descriptor
import com.splendo.kaluga.bluetooth.test.randomIdentifier
import com.splendo.kaluga.bluetooth.test.server.MockBluetoothServerBuilder
import com.splendo.kaluga.bluetooth.test.server.MockConnectedDevice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeout
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

/**
 * A characteristic and a descriptor typed by a `@BytesLayout` that generates a view: the client and the server hand out
 * views over the very arrays the Bluetooth stack holds, and take views whose arrays they pass on as they are.
 */
class HeartRateViewTest : BaseTest() {

    private suspend fun connect(scope: CoroutineScope): ConnectedMockClient = scope.connectedMockClient {
        uuid = RemoteHeartRateService.UUID
        characteristics {
            characteristic {
                uuid = RemoteMeasurementCharacteristic.UUID
                properties += setOf(CharacteristicProperty.Read, CharacteristicProperty.Write, CharacteristicProperty.Notify)
                descriptors { descriptor(RemoteLimitDescriptor.UUID) }
            }
        }
    }

    private fun ConnectedMockClient.limitWrapper() = services.single().characteristics.single { it.uuid == RemoteMeasurementCharacteristic.UUID }
        .descriptors.single { it.uuid == RemoteLimitDescriptor.UUID }.wrapper as MockDescriptorWrapper

    @Test
    fun clientReadsTheBytesTheStackHoldsWithoutCopying() = testRunBlocking {
        withTimeout(5.seconds) {
            val mock = connect(this)
            try {
                val bytes = HeartRateView(bpm = 72, energy = 300).bytes
                mock.characteristicWrapper(RemoteHeartRateService.UUID, RemoteMeasurementCharacteristic.UUID).updateValue(bytes)
                val measurement = HeartRateDeviceClient.bluetooth(mock.client, mock.identifier).filterNotNull().first().heartRateService.measurement

                val read = async { measurement.readHeartRate() }
                mock.pump()
                val view = assertIs<MeasurementCharacteristicReadResponse.Success>(read.await()).response
                assertSame(bytes, view.bytes)
                assertEquals(72, view.bpm)
                assertEquals(300, view.energy)
            } finally {
                mock.close()
            }
        }
    }

    @Test
    fun clientWritesTheViewsOwnBytes() = testRunBlocking {
        withTimeout(5.seconds) {
            val mock = connect(this)
            try {
                val measurement = HeartRateDeviceClient.bluetooth(mock.client, mock.identifier).filterNotNull().first().heartRateService.measurement
                val view = HeartRateView(bpm = 64, energy = 1)

                val write = async { measurement.writeHeartRate(view) }
                mock.pump()
                assertIs<GattResponse.WriteSuccess>(write.await())
                assertSame(view.bytes, mock.characteristicWrapper(RemoteHeartRateService.UUID, RemoteMeasurementCharacteristic.UUID).value)
            } finally {
                mock.close()
            }
        }
    }

    @Test
    fun clientNotifiesViewsOverTheNotifiedBytes() = testRunBlocking {
        withTimeout(5.seconds) {
            val mock = connect(this)
            try {
                val measurement = HeartRateDeviceClient.bluetooth(mock.client, mock.identifier).filterNotNull().first().heartRateService.measurement
                val bytes = HeartRateView(bpm = 90, energy = 2).bytes

                val notified = async { measurement.heartRate.first() }
                mock.pump()
                mock.connectionManager.notify(RemoteMeasurementCharacteristic.UUID, bytes)
                val view = notified.await()
                assertSame(bytes, view.bytes)
                assertEquals(90, view.bpm)
            } finally {
                mock.close()
            }
        }
    }

    @Test
    fun clientReadsAndWritesADescriptorThroughTheView() = testRunBlocking {
        withTimeout(5.seconds) {
            val mock = connect(this)
            try {
                val limit = HeartRateDeviceClient.bluetooth(mock.client, mock.identifier).filterNotNull().first().heartRateService.measurement.limit
                val bytes = HeartRateView(bpm = 180, energy = 0).bytes
                mock.limitWrapper().updateValue(bytes)

                val read = async { limit.readLimit() }
                mock.pump()
                assertSame(bytes, assertIs<LimitDescriptorReadResponse.Success>(read.await()).response.bytes)

                val written = HeartRateView(bpm = 170, energy = 0)
                val write = async { limit.writeLimit(written) }
                mock.pump()
                assertIs<GattResponse.WriteSuccess>(write.await())
                assertSame(written.bytes, mock.limitWrapper().value)
            } finally {
                mock.close()
            }
        }
    }

    private class MeasurementDelegate(val heartRate: HeartRateView) : LocalMeasurementCharacteristic.Delegate {
        val written = mutableListOf<HeartRateView>()
        val failures = mutableListOf<Exception>()
        val writtenLimits = mutableListOf<HeartRateView>()

        override val limitDelegate = object : LocalLimitDescriptor.Delegate {
            override suspend fun LocalLimitDescriptor.onReadLimit(identifier: Identifier) = LimitDescriptorReadResponse.Success(heartRate)

            override suspend fun LocalLimitDescriptor.onWriteLimit(limit: HeartRateView, identifier: Identifier): GattResponse.WriteResponse {
                writtenLimits += limit
                return GattResponse.WriteSuccess.Acknowledged
            }

            override suspend fun LocalLimitDescriptor.onFailedToWriteLimit(exception: Exception, identifier: Identifier) = GattResponse.ApplicationError(0x80)
        }

        override suspend fun LocalMeasurementCharacteristic.onReadHeartRate(identifier: Identifier) = MeasurementCharacteristicReadResponse.Success(heartRate)

        override suspend fun LocalMeasurementCharacteristic.onWriteHeartRate(heartRate: HeartRateView, identifier: Identifier): GattResponse.WriteResponse {
            written += heartRate
            return GattResponse.WriteSuccess.Acknowledged
        }

        override suspend fun LocalMeasurementCharacteristic.onFailedToWriteHeartRate(exception: Exception, identifier: Identifier): GattResponse.WriteResponse {
            failures += exception
            return GattResponse.ApplicationError(0x80)
        }

        override fun LocalMeasurementCharacteristic.onSubscribeToHeartRate(identifier: Identifier) = Unit

        override fun LocalMeasurementCharacteristic.onUnsubscribeToHeartRate(identifier: Identifier) = Unit
    }

    private class BatteryDelegate : LocalBatteryCharacteristic.Delegate {
        override suspend fun LocalBatteryCharacteristic.onReadLevel(identifier: Identifier) = BatteryCharacteristicReadResponse.Success(BatteryLevelData(percentage = 80))

        override fun LocalBatteryCharacteristic.onSubscribeToLevel(identifier: Identifier) = Unit

        override fun LocalBatteryCharacteristic.onUnsubscribeToLevel(identifier: Identifier) = Unit
    }

    private class ServerDelegate(measurement: MeasurementDelegate) : HeartRateDeviceServer.Delegate {
        override val heartRateServiceDelegate = object : LocalHeartRateService.Delegate {
            override val measurementDelegate = measurement
            override val batteryDelegate = BatteryDelegate()
        }
    }

    @Test
    fun serverAnswersReadsWithTheViewsOwnBytes() = testRunBlocking {
        withTimeout(10.seconds) {
            val delegate = MeasurementDelegate(HeartRateView(bpm = 72, energy = 300))
            val builder = MockBluetoothServerBuilder()
            val server = HeartRateDeviceServer.bluetooth(builder, ServerDelegate(delegate))
            val mock = builder.createdServers.last()
            try {
                assertSame(delegate.heartRate.bytes, assertIs<GattResponse.ReadSuccess>(mock.triggerRead(LocalMeasurementCharacteristic.UUID)).value)
                assertContentEquals(
                    delegate.heartRate.bytes.copyOfRange(1, delegate.heartRate.bytes.size),
                    assertIs<GattResponse.ReadSuccess>(mock.triggerRead(LocalMeasurementCharacteristic.UUID, offset = 1)).value,
                )
                assertSame(delegate.heartRate.bytes, assertIs<GattResponse.ReadSuccess>(mock.triggerDescriptorRead(LocalLimitDescriptor.UUID)).value)

                val battery = assertIs<GattResponse.ReadSuccess>(mock.triggerRead(LocalBatteryCharacteristic.UUID)).value
                assertContentEquals(byteArrayOf(80), battery)
            } finally {
                server.close()
            }
        }
    }

    @Test
    fun serverWrapsWrittenBytesOnceTheyAreComplete() = testRunBlocking {
        withTimeout(10.seconds) {
            val delegate = MeasurementDelegate(HeartRateView(bpm = 72, energy = 300))
            val builder = MockBluetoothServerBuilder()
            val server = HeartRateDeviceServer.bluetooth(builder, ServerDelegate(delegate))
            val mock = builder.createdServers.last()
            try {
                val bytes = HeartRateView(bpm = 100, energy = 5).bytes
                assertIs<GattResponse.WriteSuccess>(mock.triggerWrite(LocalMeasurementCharacteristic.UUID, bytes))
                assertSame(bytes, delegate.written.single().bytes)

                // A long write arrives in parts; the value is wrapped once all of it is there.
                val device = MockConnectedDevice()
                val split = HeartRateView(bpm = 110, energy = 6).bytes
                assertIs<GattResponse.WriteSuccess>(mock.triggerWrite(LocalMeasurementCharacteristic.UUID, split.copyOfRange(0, 1), device))
                assertEquals(1, delegate.written.size)
                assertIs<GattResponse.WriteSuccess>(mock.triggerWrite(LocalMeasurementCharacteristic.UUID, split.copyOfRange(1, split.size), device, offset = 1))
                assertEquals(110, delegate.written.last().bpm)
                assertEquals(6, delegate.written.last().energy)
                assertTrue(delegate.failures.isEmpty())

                val limit = HeartRateView(bpm = 150, energy = 0).bytes
                assertIs<GattResponse.WriteSuccess>(mock.triggerDescriptorWrite(LocalLimitDescriptor.UUID, limit))
                assertSame(limit, delegate.writtenLimits.single().bytes)

                val device2 = MockConnectedDevice()
                mock.subscribe(LocalMeasurementCharacteristic.UUID, device2)
                assertTrue(server.heartRateService.measurement.notifyAllHeartRateChanged(HeartRateView(bpm = 120, energy = 7)))
            } finally {
                server.close()
            }
        }
    }

    @Test
    fun simulatedClientAndServerPassTheSameView() = testRunBlocking {
        val delegate = MeasurementDelegate(HeartRateView(bpm = 72, energy = 300))
        val server = HeartRateDeviceServer.simulated(ServerDelegate(delegate))
        try {
            val measurement = HeartRateDeviceClient.simulated(randomIdentifier(), server).heartRateService.measurement

            assertSame(delegate.heartRate.bytes, assertIs<MeasurementCharacteristicReadResponse.Success>(measurement.readHeartRate()).response.bytes)

            val written = HeartRateView(bpm = 80, energy = 9)
            assertIs<GattResponse.WriteSuccess>(measurement.writeHeartRate(written))
            assertSame(written.bytes, delegate.written.single().bytes)
        } finally {
            server.close()
        }
    }
}
