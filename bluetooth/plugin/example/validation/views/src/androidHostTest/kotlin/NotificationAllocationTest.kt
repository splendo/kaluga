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

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * What collecting the notifications of a characteristic read through a view allocates per notification; run by the
 * `androidHostAllocationTest` task, see [Allocations].
 *
 * A view is a value class, so a `Flow` of views boxes every one it emits. The generated `collectHeartRate` wraps the
 * undecoded bytes inside its inlined action instead, and allocates nothing per notification.
 */
class NotificationAllocationTest {

    private val bytes = HeartRateView(bpm = 72, energy = 300).bytes
    private val notifications: Flow<ByteArray> = flow { repeat(NOTIFICATIONS) { emit(bytes) } }

    // The flows of the Bluetooth implementation, over notifications that need no Bluetooth stack.
    private val characteristic = object : RemoteMeasurementCharacteristic by MockRemoteMeasurementCharacteristic() {
        override val heartRateBytes: Flow<ByteArray> = notifications
        override val heartRate: Flow<HeartRateView> = notifications.map { HeartRateView(it) }
    }

    private var sum = 0L

    private fun perNotification(collect: suspend () -> Unit): Double = Allocations.of { runBlocking { collect() } }.toDouble() / NOTIFICATIONS

    @Test
    fun collectingTheBytesAndWrappingThemInlineAllocatesNoView() {
        val undecoded = perNotification { characteristic.heartRateBytes.collect { sum += it.size } }
        val inline = perNotification { characteristic.collectHeartRate { sum += it.bpm } }
        val views = perNotification { characteristic.heartRate.collect { sum += it.bpm } }
        val boxes = perNotification { flow { repeat(NOTIFICATIONS) { emit(HeartRateView(bytes)) } }.collect { sum += it.bpm } }
        println("allocation per notification: undecoded $undecoded bytes, collectHeartRate $inline, Flow<HeartRateView> $views, a flow emitting views $boxes")

        assertTrue(inline - undecoded < 1.0, "collectHeartRate allocates ${inline - undecoded} bytes per notification")
        // An object header and a reference: the box of one view.
        assertTrue(boxes - undecoded >= 12.0, "A flow of views allocates only ${boxes - undecoded} bytes per notification")
        assertTrue(views - undecoded >= boxes - undecoded, "Flow<HeartRateView> allocates only ${views - undecoded} bytes per notification")
    }

    private companion object {
        const val NOTIFICATIONS = 100_000
    }
}
