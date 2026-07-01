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

package com.splendo.kaluga.bluetooth.sharedclient

import com.splendo.kaluga.base.test.BaseTest
import com.splendo.kaluga.base.test.testRunBlocking
import com.splendo.kaluga.bluetooth.CharacteristicProperty
import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.serialization.BluetoothFormat
import com.splendo.kaluga.bluetooth.sharedcontract.RemoteSharedCharacteristic
import com.splendo.kaluga.bluetooth.sharedcontract.RemoteSharedService
import com.splendo.kaluga.bluetooth.sharedcontract.SharedCharacteristicReadResponse
import com.splendo.kaluga.bluetooth.sharedcontract.SharedDeviceClient
import com.splendo.kaluga.bluetooth.test.ConnectedMockClient
import com.splendo.kaluga.bluetooth.test.characteristic
import com.splendo.kaluga.bluetooth.test.connectedMockClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withTimeout
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.time.Duration.Companion.seconds

// Real round-trip against the kaluga mock Bluetooth stack for the external-api variant: the generated Bluetooth*
// implementations of the shared contract interfaces (interfaces live in :validation:contract) are exercised end-to-end.
// A device exposing the generated service (b100) / characteristic (b101) is connected and discovered via the reusable
// connectedMockClient helper, the typed client is obtained through the SharedDeviceClient.bluetooth() factory, and
// read/write actions are driven through a MockDeviceConnectionManager. This proves the generated CLIENT API actually
// talks to the bluetooth stack, not merely that it compiles.
class GeneratedExternalApiClientStructureTest : BaseTest() {

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
    fun readLevelRoundTrip() = testRunBlocking {
        withTimeout(5.seconds) {
            val mock = connect(this)
            try {
                // The read value the mock device returns for b101: BluetoothFormat-encoded Int 42.
                mock.characteristicWrapper(RemoteSharedService.UUID, RemoteSharedCharacteristic.UUID)
                    .updateValue(BluetoothFormat.encodeToByteArray(BluetoothFormat.serializer<Int>(), 42))

                val client = SharedDeviceClient.bluetooth(mock.client, mock.identifier)

                val read = async { client.sharedService.sharedCharacteristic.readLevel() }
                mock.pump()
                val success = assertIs<SharedCharacteristicReadResponse.Success>(read.await())
                assertEquals(42, success.response)
            } finally {
                mock.close()
            }
        }
    }

    @Test
    fun writeTargetRoundTrip() = testRunBlocking {
        withTimeout(5.seconds) {
            val mock = connect(this)
            try {
                val client = SharedDeviceClient.bluetooth(mock.client, mock.identifier)

                val write = async { client.sharedService.sharedCharacteristic.writeTarget(7) }
                mock.pump()
                assertIs<GattResponse.WriteSuccess>(write.await())
                // The mock recorded the written bytes on the characteristic wrapper.
                assertEquals(
                    7,
                    BluetoothFormat.decodeFromByteArray(
                        BluetoothFormat.serializer<Int>(),
                        mock.characteristicWrapper(RemoteSharedService.UUID, RemoteSharedCharacteristic.UUID).value!!,
                    ),
                )
            } finally {
                mock.close()
            }
        }
    }
}
