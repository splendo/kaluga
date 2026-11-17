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

import com.splendo.kaluga.bluetooth.CharacteristicProperty
import com.splendo.kaluga.bluetooth.RemoteCharacteristic
import com.splendo.kaluga.bluetooth.test.characteristic
import com.splendo.kaluga.bluetooth.test.connectedMockClient
import com.splendo.kaluga.bluetooth.value
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * What collecting the notifications of a characteristic read through a view allocates per notification; run by the
 * `androidHostAllocationTest` task, see [Allocations].
 *
 * A view is a value class, so a `Flow` of views boxes every one it emits. The generated `collectHeartRate` wraps the
 * undecoded bytes inside its inlined action instead, and allocates nothing per notification. Measured on the generated
 * Bluetooth implementation over a mocked device, beyond what the stack allocates to notify its own bytes.
 */
class NotificationAllocationTest {

    private val bytes = HeartRateView(bpm = 72, energy = 300).bytes

    private var sum = 0L
    private var received = 0

    private fun receive(value: Int) {
        received++
        sum += value
    }

    private fun perNotification(collect: suspend () -> Unit): Double = Allocations.of { runBlocking { collect() } }.toDouble() / NOTIFICATIONS

    @Test
    fun theGeneratedBluetoothImplementationAllocatesNoViewOverTheNotifiedBytes() = runBlocking {
        val mock = connectedMockClient {
            uuid = RemoteHeartRateService.UUID
            characteristics {
                characteristic {
                    uuid = RemoteMeasurementCharacteristic.UUID
                    properties += CharacteristicProperty.Notify
                }
            }
        }
        try {
            val remote = mock.services.single().characteristics.single()
            val characteristic = BluetoothRemoteMeasurementCharacteristic(remote)

            // Kept subscribed, so notifications stay enabled and every collection below subscribes without an action to pump.
            val subscribed = async { remote.subscribe { } }
            mock.pump()
            assertIs<RemoteCharacteristic.SubscriptionResult.DidSubscribe>(subscribed.await())

            // Notifies the stack as a device would while [collect] runs; a new subscriber is handed the last value first.
            suspend fun notified(collect: suspend () -> Unit) = coroutineScope {
                received = 0
                val collector = launch(start = CoroutineStart.UNDISPATCHED) { collect() }
                repeat(NOTIFICATIONS) { mock.connectionManager.notify(RemoteMeasurementCharacteristic.UUID, bytes) }
                while (received < NOTIFICATIONS) yield()
                collector.cancel()
            }

            // What the stack itself allocates per notification, collected straight off the characteristic.
            val stack = perNotification { notified { remote.value().collect { receive(it.size) } } }
            val undecoded = perNotification { notified { characteristic.heartRateBytes.collect { receive(it.size) } } }
            val inline = perNotification { notified { characteristic.collectHeartRate { receive(it.bpm) } } }
            val views = perNotification { notified { characteristic.heartRate.collect { receive(it.bpm) } } }
            println(
                "allocation per notification by BluetoothRemoteMeasurementCharacteristic: stack $stack bytes, heartRateBytes $undecoded, collectHeartRate $inline, heartRate $views",
            )

            assertTrue(undecoded - stack < 1.0, "heartRateBytes allocates ${undecoded - stack} bytes per notification")
            assertTrue(inline - stack < 1.0, "collectHeartRate allocates ${inline - stack} bytes per notification")
            // An object header and a reference: the box of one view.
            assertTrue(views - stack >= 12.0, "Flow<HeartRateView> allocates only ${views - stack} bytes per notification")
        } finally {
            mock.close()
        }
    }

    private companion object {
        const val NOTIFICATIONS = 100_000
    }
}
