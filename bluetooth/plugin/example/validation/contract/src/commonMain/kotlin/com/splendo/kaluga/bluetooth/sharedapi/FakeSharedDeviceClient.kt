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

package com.splendo.kaluga.bluetooth.sharedapi

import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.sharedcontract.RemoteSharedCharacteristic
import com.splendo.kaluga.bluetooth.sharedcontract.RemoteSharedService
import com.splendo.kaluga.bluetooth.sharedcontract.SharedCharacteristicReadResponse
import com.splendo.kaluga.bluetooth.sharedcontract.SharedDeviceClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * A non-Bluetooth fake of the generated [SharedDeviceClient] API, suitable for previews and tests.
 * Possible only because the API interfaces are generated without any implementation or platform dependency.
 */
class FakeSharedDeviceClient(private val level: Int = 0, private val states: List<Short> = listOf(0)) : SharedDeviceClient {
    override val sharedService: RemoteSharedService = object : RemoteSharedService {
        override val sharedCharacteristic: RemoteSharedCharacteristic = object : RemoteSharedCharacteristic {
            override val state: Flow<Short> = flowOf(*states.toTypedArray())
            override suspend fun readLevel(): SharedCharacteristicReadResponse = SharedCharacteristicReadResponse.Success(level)
            override suspend fun writeTarget(target: Int): GattResponse.WriteResponse = GattResponse.WriteSuccess.Acknowledged
        }
    }
}
