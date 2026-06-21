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
import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.device.randomIdentifier
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

// Beyond proving the generated types exist, this actually stands up the generated simulated SERVER and CLIENT and runs a
// read/write round-trip through them: the client talks to the server's delegate over the in-memory simulated transport.
class GeneratedSimulatorStructureTest : BaseTest() {

    private class CharacteristicDelegate(val level: Int) : LocalSharedCharacteristic.Delegate {
        var lastWrittenTarget: Int? = null
        override suspend fun LocalSharedCharacteristic.onReadLevel(identifier: Identifier): SharedCharacteristicReadResponse =
            SharedCharacteristicReadResponse.Success(level)
        override suspend fun LocalSharedCharacteristic.onWriteTarget(target: Int, identifier: Identifier): GattResponse.WriteResponse {
            lastWrittenTarget = target
            return GattResponse.WriteSuccess.Acknowledged
        }
        override suspend fun LocalSharedCharacteristic.onFailedToWriteTarget(exception: Exception, identifier: Identifier): GattResponse.WriteResponse =
            GattResponse.WriteSuccess.Acknowledged
        override fun LocalSharedCharacteristic.onSubscribeToState(identifier: Identifier) {}
        override fun LocalSharedCharacteristic.onUnsubscribeToState(identifier: Identifier) {}
    }

    private class Delegate(characteristicDelegate: CharacteristicDelegate) : SharedDeviceServer.Delegate {
        override val sharedServiceDelegate: LocalSharedService.Delegate = object : LocalSharedService.Delegate {
            override val sharedCharacteristicDelegate = characteristicDelegate
        }
    }

    @Test
    fun simulatedClientRoundTripsThroughServer() = testRunBlocking {
        val characteristicDelegate = CharacteristicDelegate(level = 42)
        val server = SharedDeviceServer.simulated(Delegate(characteristicDelegate))
        try {
            val client = SharedDeviceClient.simulated(randomIdentifier(), server)

            val read = client.sharedService.sharedCharacteristic.readLevel()
            assertIs<SharedCharacteristicReadResponse.Success>(read)
            assertEquals(42, read.response)

            val write = client.sharedService.sharedCharacteristic.writeTarget(7)
            assertIs<GattResponse.WriteSuccess>(write)
            assertEquals(7, characteristicDelegate.lastWrittenTarget)
        } finally {
            server.close()
        }
    }
}
