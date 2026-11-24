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

import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.test.AndroidMockCharacteristicWrapper
import com.splendo.kaluga.bluetooth.test.MockCharacteristic
import com.splendo.kaluga.bluetooth.test.MockRemoteServiceWrapper
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * What [RemoteCharacteristic.notify] allocates to hand a notification to its subscribers; run by the
 * `androidHostAllocationTest` task, see [Allocations].
 */
class NotifyAllocationTest {

    private var received = 0

    private val characteristic = MockCharacteristic(
        AndroidMockCharacteristicWrapper(service = MockRemoteServiceWrapper(), properties = setOf(CharacteristicProperty.Notify)),
    ) { (it.action as DeviceAction.Notification).complete(GattResponse.WriteSuccess.Acknowledged) }

    @Test
    fun notifyingSubscribersAllocatesNothing() = runBlocking {
        repeat(SUBSCRIBERS) {
            assertIs<RemoteCharacteristic.SubscriptionResult.DidSubscribe>(characteristic.subscribe { received++ })
        }
        val value = byteArrayOf(72)

        val allocated = Allocations.of { repeat(NOTIFICATIONS) { characteristic.notify(value) } }

        assertEquals(0L, allocated, "notify allocates ${allocated.toDouble() / NOTIFICATIONS} bytes per notification")
        assertEquals(4 * SUBSCRIBERS * NOTIFICATIONS, received)
    }

    @Test
    fun aSubscriberMayUnsubscribeFromItsOwnUpdate() = runBlocking {
        lateinit var first: RemoteCharacteristic.Subscription
        first = assertIs<RemoteCharacteristic.SubscriptionResult.DidSubscribe>(
            characteristic.subscribe {
                received++
                first.startUnsubscribe()
            },
        ).subscription
        characteristic.subscribe { received++ }

        characteristic.notify(byteArrayOf(1))
        characteristic.notify(byteArrayOf(2))

        assertEquals(3, received)
    }

    private companion object {
        const val SUBSCRIBERS = 3
        const val NOTIFICATIONS = 100_000
    }
}
