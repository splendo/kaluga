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

package com.splendo.kaluga.bluetooth.sharedcontract

import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.device.Identifier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

// Compiling the generated contract only proves it parses; it does not prove the plugin emitted the client/server API
// interfaces with the members we expect. Implementing every generated interface here does: a missing, renamed or
// wrongly-typed member (or delegate) fails to compile.
class GeneratedContractStructureTest {

    // --- client API ---
    private object RemoteCharacteristic : RemoteSharedCharacteristic {
        override val state: Flow<Short> = flowOf(0.toShort())
        override suspend fun readLevel(): SharedCharacteristicReadResponse = SharedCharacteristicReadResponse.Success(0)
        override suspend fun writeTarget(target: Int): GattResponse.WriteResponse = GattResponse.WriteSuccess.Acknowledged
    }

    private object RemoteService : RemoteSharedService {
        override val sharedCharacteristic = RemoteCharacteristic
    }

    private object Client : SharedDeviceClient {
        override val sharedService = RemoteService
    }

    // --- server API ---
    private object LocalCharacteristic : LocalSharedCharacteristic {
        override val stateSubscribers: Flow<List<Identifier>> = flowOf(emptyList())
        override suspend fun notifyAllStateChanged(state: Short): Boolean = true
        override suspend fun notifyStateChanged(identifier: Identifier, state: Short): Boolean = true
    }

    private object LocalCharacteristicDelegate : LocalSharedCharacteristic.Delegate {
        override suspend fun LocalSharedCharacteristic.onReadLevel(identifier: Identifier): SharedCharacteristicReadResponse =
            SharedCharacteristicReadResponse.Success(0)
        override suspend fun LocalSharedCharacteristic.onWriteTarget(target: Int, identifier: Identifier): GattResponse.WriteResponse =
            GattResponse.WriteSuccess.Acknowledged
        override suspend fun LocalSharedCharacteristic.onFailedToWriteTarget(exception: Exception, identifier: Identifier): GattResponse.WriteResponse =
            GattResponse.WriteSuccess.Acknowledged
        override fun LocalSharedCharacteristic.onSubscribeToState(identifier: Identifier) {}
        override fun LocalSharedCharacteristic.onUnsubscribeToState(identifier: Identifier) {}
    }

    private object LocalService : LocalSharedService {
        override val sharedCharacteristic = LocalCharacteristic
    }

    private object LocalServiceDelegate : LocalSharedService.Delegate {
        override val sharedCharacteristicDelegate = LocalCharacteristicDelegate
    }

    private object Server : SharedDeviceServer {
        override val sharedService = LocalService
        override fun close() {}
    }

    private object ServerDelegate : SharedDeviceServer.Delegate {
        override val sharedServiceDelegate = LocalServiceDelegate
    }

    @Test
    fun contractApiExists() {
        // client device -> service -> characteristic
        assertNotNull(Client.sharedService.sharedCharacteristic)
        assertNotNull(RemoteSharedService.UUID)
        assertNotNull(RemoteSharedCharacteristic.UUID)

        // server device -> service -> characteristic, and the delegate tree
        assertNotNull(Server.sharedService.sharedCharacteristic)
        assertNotNull(ServerDelegate.sharedServiceDelegate.sharedCharacteristicDelegate)
        assertNotNull(LocalSharedService.UUID)
        assertNotNull(LocalSharedCharacteristic.UUID)

        // the read response is a sealed type with the level's type as its success payload
        val response: SharedCharacteristicReadResponse = SharedCharacteristicReadResponse.Success(5)
        assertEquals(5, (response as SharedCharacteristicReadResponse.Success).response)
    }
}
