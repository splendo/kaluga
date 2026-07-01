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

package com.splendo.kaluga.bluetooth.sharedserver

import com.splendo.kaluga.base.test.BaseTest
import com.splendo.kaluga.base.test.testRunBlocking
import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.serialization.BluetoothFormat
import com.splendo.kaluga.bluetooth.sharedcontract.LocalSharedCharacteristic
import com.splendo.kaluga.bluetooth.sharedcontract.LocalSharedService
import com.splendo.kaluga.bluetooth.sharedcontract.SharedCharacteristicReadResponse
import com.splendo.kaluga.bluetooth.sharedcontract.SharedDeviceServer
import com.splendo.kaluga.bluetooth.test.server.MockBluetoothServerBuilder
import com.splendo.kaluga.bluetooth.test.server.MockConnectedDevice
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.serializer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

// Real runtime round-trip against a behavioral mock Bluetooth server for the external-api flavour: the generated
// SharedDeviceServer.bluetooth() factory (in :sharedserver) builds a behavioral MockBluetoothServer whose delegate tree wires
// the contract interfaces (in :sharedcontract), and a simulated central drives reads/writes/subscriptions end-to-end.
class GeneratedExternalApiServerStructureTest : BaseTest() {

    private class CharacteristicDelegate : LocalSharedCharacteristic.Delegate {
        var written: Int? = null
        var subscribed = false

        override suspend fun LocalSharedCharacteristic.onReadLevel(identifier: Identifier): SharedCharacteristicReadResponse = SharedCharacteristicReadResponse.Success(42)

        override suspend fun LocalSharedCharacteristic.onWriteTarget(target: Int, identifier: Identifier): GattResponse.WriteResponse {
            written = target
            return GattResponse.WriteSuccess.Acknowledged
        }

        override suspend fun LocalSharedCharacteristic.onFailedToWriteTarget(exception: Exception, identifier: Identifier): GattResponse.WriteResponse =
            GattResponse.ApplicationError(0x80)

        override fun LocalSharedCharacteristic.onSubscribeToState(identifier: Identifier) {
            subscribed = true
        }

        override fun LocalSharedCharacteristic.onUnsubscribeToState(identifier: Identifier) {
            subscribed = false
        }
    }

    private class ServiceDelegate(override val sharedCharacteristicDelegate: CharacteristicDelegate) : LocalSharedService.Delegate

    private class ServerDelegate(override val sharedServiceDelegate: ServiceDelegate) : SharedDeviceServer.Delegate

    @Test
    fun serverRoundTrip() = testRunBlocking {
        withTimeout(10.seconds) {
            val characteristicDelegate = CharacteristicDelegate()
            val builder = MockBluetoothServerBuilder()
            val server = SharedDeviceServer.bluetooth(
                builder,
                ServerDelegate(ServiceDelegate(characteristicDelegate)),
            )
            val mock = builder.createdServers.last()
            try {
                assertNotNull(server.sharedService.sharedCharacteristic)

                val readResponse = mock.triggerRead(LocalSharedCharacteristic.UUID)
                val readSuccess = assertIs<GattResponse.ReadSuccess>(readResponse)
                assertEquals(42, BluetoothFormat.decodeFromByteArray(serializer<Int>(), readSuccess.value))

                val writeResponse = mock.triggerWrite(
                    LocalSharedCharacteristic.UUID,
                    BluetoothFormat.encodeToByteArray(serializer<Int>(), 7),
                )
                assertIs<GattResponse.WriteSuccess>(writeResponse)
                assertEquals(7, characteristicDelegate.written)

                val device = MockConnectedDevice()
                mock.subscribe(LocalSharedCharacteristic.UUID, device)
                assertTrue(characteristicDelegate.subscribed)
                assertTrue(server.sharedService.sharedCharacteristic.notifyAllStateChanged(5))
            } finally {
                server.close()
            }
        }
    }
}
