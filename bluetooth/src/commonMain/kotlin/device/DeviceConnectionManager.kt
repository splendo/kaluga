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

import com.splendo.kaluga.state.StateRepoAccesor

internal abstract class BaseDeviceConnectionManager(var reconnectionAttempts: Int = 0,
                                                    internal val deviceInfoHolder: DeviceInfoHolder,
                                                    internal val repoAccessor: StateRepoAccesor<DeviceState>) {

    interface Builder {
        fun create(reconnectionAttempts: Int, deviceInfo: DeviceInfoHolder, repoAccessor: StateRepoAccesor<DeviceState>): DeviceConnectionManager
    }

    abstract suspend fun connect()
    abstract suspend fun discoverServices()
    abstract suspend fun disconnect()
    abstract suspend fun readRssi()
    abstract suspend fun performAction(action: DeviceAction): Boolean
}

internal expect class DeviceConnectionManager : BaseDeviceConnectionManager