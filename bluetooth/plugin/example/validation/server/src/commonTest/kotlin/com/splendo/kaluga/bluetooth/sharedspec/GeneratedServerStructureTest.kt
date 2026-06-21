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

import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.device.Identifier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.test.Test
import kotlin.test.assertNotNull

// Implementing every generated server interface (and its delegates) proves the plugin emitted the SERVER API with the
// notify members and delegate callbacks we expect; a missing or renamed member fails to compile.
//
// Note: the generated Bluetooth-backed server (`SharedDeviceServer.bluetooth(...)`) is exercised at runtime in the
// `simulator` module, whose simulated transport runs in-memory. Standing up the real Bluetooth server here would require
// the core library's peripheral/service-graph harness, which validates the BLE runtime rather than the generated code.
class GeneratedServerStructureTest {

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
    fun serverApiExists() {
        assertNotNull(Server.sharedService.sharedCharacteristic)
        assertNotNull(ServerDelegate.sharedServiceDelegate.sharedCharacteristicDelegate)
        assertNotNull(LocalSharedService.UUID)
        assertNotNull(LocalSharedCharacteristic.UUID)
    }
}
