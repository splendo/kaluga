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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

// Implementing every generated client interface proves the plugin emitted the CLIENT API (device -> service ->
// characteristic, with its read/write/notify members) with the names and types we expect; a missing or renamed member
// fails to compile, which compiling the generated sources alone would not catch.
class GeneratedClientStructureTest {

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

    @Test
    fun clientApiExists() {
        assertNotNull(Client.sharedService.sharedCharacteristic)
        assertNotNull(RemoteSharedService.UUID)
        assertNotNull(RemoteSharedCharacteristic.UUID)

        val response: SharedCharacteristicReadResponse = SharedCharacteristicReadResponse.Success(5)
        assertEquals(5, (response as SharedCharacteristicReadResponse.Success).response)
    }
}
