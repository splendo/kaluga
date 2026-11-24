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
import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.test.randomIdentifier
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withTimeout
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

/**
 * A measurement characteristic written by hand over the notifications it is given: it provides only the undecoded
 * notifications, as every implementation of the generated API does, and the views follow from them.
 */
class BytesOnlyMeasurement(override val heartRateBytes: Flow<ByteArray>) : RemoteMeasurementCharacteristic {
    override val isNotifying: Flow<Boolean> = flowOf(true)
    override val limit: RemoteLimitDescriptor = MockRemoteLimitDescriptor()
    override suspend fun readHeartRate(): MeasurementCharacteristicReadResponse = MeasurementCharacteristicReadResponse.Success(HeartRateView(heartRateBytes.first()))
    override suspend fun writeHeartRate(heartRate: HeartRateView): GattResponse.WriteResponse = GattResponse.WriteSuccess.Acknowledged
}

/**
 * The undecoded notifications of a characteristic read through a view are the one flow an implementation provides;
 * the flow of views the API derives from them, so the two cannot disagree, and `collectHeartRate` reads the bytes of
 * whatever implementation it is given.
 */
class NotifiedBytesTest : BaseTest() {

    private val first = HeartRateView(bpm = 72, energy = 300).bytes
    private val second = HeartRateView(bpm = 80, energy = 301).bytes

    @Test
    fun aHandWrittenImplementationProvidingTheBytesNotifiesViewsOverThem() = testRunBlocking {
        val measurement = BytesOnlyMeasurement(flowOf(first, second))

        val views = measurement.heartRate.toList()
        assertEquals(listOf(72, 80), views.map { it.bpm })
        assertSame(first, views[0].bytes)
        assertSame(second, views[1].bytes)

        val collected = mutableListOf<Int>()
        measurement.collectHeartRate { collected += it.energy }
        assertEquals(listOf(300, 301), collected)
    }

    @Test
    fun theMockIsDrivenWithBytes() = testRunBlocking {
        val mock = MockRemoteMeasurementCharacteristic(heartRateBytes = MutableSharedFlow(replay = 1))
        mock.heartRateBytes.emit(first)
        assertSame(first, mock.heartRate.first().bytes)
        assertEquals(72, mock.heartRate.first().bpm)
    }

    private class ServerDelegate : HeartRateDeviceServer.Delegate {
        override val heartRateServiceDelegate = object : LocalHeartRateService.Delegate {
            override val measurementDelegate = object : LocalMeasurementCharacteristic.Delegate {
                override val limitDelegate = object : LocalLimitDescriptor.Delegate {
                    override suspend fun LocalLimitDescriptor.onReadLimit(identifier: Identifier) = LimitDescriptorReadResponse.Failure(GattResponse.ReadNotPermitted)
                    override suspend fun LocalLimitDescriptor.onWriteLimit(limit: HeartRateView, identifier: Identifier) = GattResponse.WriteNotPermitted
                    override suspend fun LocalLimitDescriptor.onFailedToWriteLimit(exception: Exception, identifier: Identifier) = GattResponse.WriteNotPermitted
                }
                override suspend fun LocalMeasurementCharacteristic.onReadHeartRate(identifier: Identifier) =
                    MeasurementCharacteristicReadResponse.Failure(GattResponse.ReadNotPermitted)
                override suspend fun LocalMeasurementCharacteristic.onWriteHeartRate(heartRate: HeartRateView, identifier: Identifier) = GattResponse.WriteNotPermitted
                override suspend fun LocalMeasurementCharacteristic.onFailedToWriteHeartRate(exception: Exception, identifier: Identifier) = GattResponse.WriteNotPermitted
                override fun LocalMeasurementCharacteristic.onSubscribeToHeartRate(identifier: Identifier) = Unit
                override fun LocalMeasurementCharacteristic.onUnsubscribeToHeartRate(identifier: Identifier) = Unit
            }
            override val batteryDelegate = object : LocalBatteryCharacteristic.Delegate {
                override suspend fun LocalBatteryCharacteristic.onReadLevel(identifier: Identifier) = BatteryCharacteristicReadResponse.Failure(GattResponse.ReadNotPermitted)
                override fun LocalBatteryCharacteristic.onSubscribeToLevel(identifier: Identifier) = Unit
                override fun LocalBatteryCharacteristic.onUnsubscribeToLevel(identifier: Identifier) = Unit
            }
        }
    }

    @Test
    fun theSimulatorNotifiesTheViewsItIsGivenAndNotesTheirCollector() = testRunBlocking {
        withTimeout(5.seconds) {
            val server = HeartRateDeviceServer.simulated(ServerDelegate())
            try {
                val measurement = HeartRateDeviceClient.simulated(randomIdentifier(), server).heartRateService.measurement
                assertFalse(measurement.isNotifying.first())

                // Started undispatched, the collector subscribes before the server is asked to notify.
                val notified = async(start = CoroutineStart.UNDISPATCHED) { measurement.heartRate.first() }
                assertTrue(measurement.isNotifying.first())
                assertTrue(server.heartRateService.measurement.notifyAllHeartRateChanged(HeartRateView(first)))
                assertSame(first, notified.await().bytes)
                assertFalse(measurement.isNotifying.first { !it })
            } finally {
                server.close()
            }
        }
    }
}
