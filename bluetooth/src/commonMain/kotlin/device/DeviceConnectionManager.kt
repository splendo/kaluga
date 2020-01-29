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
import kotlinx.coroutines.CompletableDeferred

internal abstract class BaseDeviceConnectionManager(internal val connectionSettings: ConnectionSettings = ConnectionSettings(),
                                                    internal val deviceInfoHolder: DeviceInfoHolder,
                                                    internal val stateRepo: StateRepo<DeviceState>) {

    interface Builder {
        fun create(connectionSettings: ConnectionSettings, deviceInfo: DeviceInfoHolder, stateRepo: StateRepo<DeviceState>): BaseDeviceConnectionManager
    }

    abstract suspend fun connect()
    abstract suspend fun discoverServices()
    abstract suspend fun disconnect()
    abstract suspend fun readRssi()
    abstract suspend fun performAction(action: DeviceAction): Boolean

    internal suspend fun handleConnect(onNotConnecting: () -> Unit) {
        stateRepo.takeAndChangeState { state ->
            when (state) {
                is DeviceState.Connecting -> state.didConnect
                is DeviceState.Reconnecting -> state.didConnect
                is DeviceState.Connected -> state.remain
                else -> {
                    onNotConnecting()
                    state.remain
                }
            }
        }
    }

    internal suspend fun handleDisconnect(onDisconnect: () -> Unit) {
        stateRepo.takeAndChangeState { state ->
            when (state) {
                is DeviceState.Reconnecting -> {
                    val retry = state.retry()
                    if (!retry.first)
                        onDisconnect()
                    retry.second
                }
                is DeviceState.Connected -> {
                    when (connectionSettings.reconnectionSettings) {
                        is ConnectionSettings.ReconnectionSettings.Always,
                        is ConnectionSettings.ReconnectionSettings.Limited -> {
                            state.reconnect
                        }
                        else -> {
                            onDisconnect()
                            state.didDisconnect
                        }
                    }
                }
                is DeviceState.Disconnected -> state.remain
                else -> {
                    onDisconnect()
                    state.didDisconnect
                }
            }
        }
    }

}

internal expect class DeviceConnectionManager : BaseDeviceConnectionManager