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

package com.splendo.kaluga.bluetooth.sharedspec

import com.splendo.kaluga.base.test.BaseTest
import com.splendo.kaluga.base.test.testRunBlocking
import com.splendo.kaluga.bluetooth.CharacteristicProperty
import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.test.ConnectedMockClient
import com.splendo.kaluga.bluetooth.test.characteristic
import com.splendo.kaluga.bluetooth.test.connectedMockClient
import com.splendo.kaluga.bytes.serialization.BytesFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withTimeout
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

/**
 * A generated client is a passthrough to the characteristic it wraps, so a disconnect reaches it the same way: the
 * notification flow completes, `isNotifying` drops to false and the pending and later reads and writes answer with
 * [GattResponse.DeviceUnavailable].
 */
class GeneratedClientDisconnectTest : BaseTest() {

    private suspend fun connect(scope: CoroutineScope): ConnectedMockClient = scope.connectedMockClient {
        uuid = RemoteSharedService.UUID
        characteristics {
            characteristic {
                uuid = RemoteSharedCharacteristic.UUID
                properties += setOf(CharacteristicProperty.Read, CharacteristicProperty.Write, CharacteristicProperty.Notify)
            }
        }
    }

    @Test
    fun disconnectEndsNotificationsAndAnswersLaterCalls() = testRunBlocking {
        withTimeout(5.seconds) {
            val mock = connect(this)
            try {
                val characteristic = SharedDeviceClient.bluetooth(mock.client, mock.identifier).filterNotNull().first().sharedService.sharedCharacteristic

                val states = async { characteristic.state.toList() }
                mock.pump()
                characteristic.isNotifying.first { it }
                mock.connectionManager.notify(RemoteSharedCharacteristic.UUID, BytesFormat.encodeToByteArray(BytesFormat.serializer<Short>(), 3))

                val read = async { characteristic.readLevel() }
                assertIs<DeviceAction.Read.Characteristic>(mock.connectionManager.disconnectWhilePerformingAction())

                assertEquals(listOf<Short>(3), states.await())
                assertFalse(characteristic.isNotifying.first())
                assertEquals(GattResponse.DeviceUnavailable, assertIs<SharedCharacteristicReadResponse.Failure>(read.await()).error)
                assertEquals(GattResponse.DeviceUnavailable, characteristic.writeTarget(7))
                assertTrue(characteristic.state.toList().isEmpty())
            } finally {
                mock.close()
            }
        }
    }
}
