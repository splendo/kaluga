/*
 Copyright (c) 2020. Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bluetooth.device

import com.splendo.kaluga.state.StateRepo
import kotlinx.coroutines.CoroutineScope

internal actual class DeviceConnectionManager(connectionSettings: ConnectionSettings, deviceHolder: DeviceHolder, stateRepo: StateRepo<DeviceState>, coroutineScope: CoroutineScope) : BaseDeviceConnectionManager(connectionSettings, deviceHolder, stateRepo, coroutineScope) {

    class Builder() : BaseDeviceConnectionManager.Builder {

        override fun create(connectionSettings: ConnectionSettings, deviceHolder: DeviceHolder, stateRepo: StateRepo<DeviceState>, coroutineScope: CoroutineScope): DeviceConnectionManager {
            return DeviceConnectionManager(connectionSettings, deviceHolder, stateRepo, coroutineScope)
        }
    }

    override suspend fun connect() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun discoverServices() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun disconnect() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun readRssi() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun performAction(action: DeviceAction) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}