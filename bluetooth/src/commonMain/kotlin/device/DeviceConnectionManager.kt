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

import com.splendo.kaluga.bluetooth.*
import com.splendo.kaluga.state.StateRepo
import kotlinx.coroutines.CoroutineScope

internal abstract class BaseDeviceConnectionManager(internal val connectionSettings: ConnectionSettings = ConnectionSettings(),
                                                    internal val deviceHolder: DeviceHolder,
                                                    internal val stateRepo: StateRepo<DeviceState>,
                                                    coroutineScope: CoroutineScope) : CoroutineScope by coroutineScope {

    interface Builder {
        fun create(connectionSettings: ConnectionSettings, deviceHolder: DeviceHolder, stateRepo: StateRepo<DeviceState>, coroutineScope: CoroutineScope): BaseDeviceConnectionManager
    }


    protected var currentAction: DeviceAction? = null
    protected val notifyingCharacteristics = mutableMapOf<String, Characteristic>()
    
    abstract suspend fun connect()
    abstract suspend fun discoverServices()
    abstract suspend fun disconnect()
    abstract suspend fun readRssi()
    abstract suspend fun performAction(action: DeviceAction)

    internal suspend fun handleNewRssi(rssi: Int) {
        stateRepo.takeAndChangeState {
            it.rssiDidUpdate(rssi)
        }
    }

    internal suspend fun handleConnect() {
        stateRepo.takeAndChangeState { state ->
            when (state) {
                is DeviceState.Connecting -> state.didConnect
                is DeviceState.Reconnecting -> state.didConnect
                is DeviceState.Connected -> state.remain
                else -> {
                    currentAction = null
                    notifyingCharacteristics.clear()
                    disconnect()
                    state.remain
                }
            }
        }
    }

    internal suspend fun handleDisconnect(onDisconnect: (suspend () -> Unit)? = null) {
        val clean = suspend {
            currentAction = null
            notifyingCharacteristics.clear()
            onDisconnect?.invoke()
        }

        stateRepo.takeAndChangeState { state ->

            when (state) {
                is DeviceState.Reconnecting -> {
                    state.retry().also {
                        if (it == state.didDisconnect) {
                            clean()
                        }
                    }
                }
                is DeviceState.Connected -> {
                    when (connectionSettings.reconnectionSettings) {
                        is ConnectionSettings.ReconnectionSettings.Always,
                        is ConnectionSettings.ReconnectionSettings.Limited -> {
                            state.reconnect
                        }
                        else -> {
                            clean()
                            state.didDisconnect
                        }
                    }
                }
                is DeviceState.Disconnected -> state.remain
                else -> {
                    clean()
                    state.didDisconnect
                }
            }
        }
    }

    internal suspend fun handleScanCompleted(services: List<Service>) {
        stateRepo.takeAndChangeState { state ->
            when (state) {
                is DeviceState.Connected.Discovering -> state.didDiscoverServices(services)
                else -> state.remain
            }
        }
    }

    internal suspend fun handleCurrentActionCompleted() {
        stateRepo.takeAndChangeState { state ->
            val newState = when (state) {
                is DeviceState.Connected.HandlingAction -> {
                    if (state.action == currentAction) {
                        state.actionCompleted
                    } else {
                        state.remain
                    }
                }
                else -> state.remain
            }
            currentAction = null
            newState
        }
    }

    internal suspend fun handleUpdatedCharacteristic(uuid: UUID, onUpdate: ((Characteristic) -> Unit)? = null) {
        notifyingCharacteristics[uuid.uuidString]?.updateValue()
        val characteristicToUpdate = when (val action = currentAction) {
            is DeviceAction.Read.Characteristic -> {
                if (action.characteristic.uuid.uuidString == uuid.uuidString) {
                    action.characteristic
                } else null
            }
            is DeviceAction.Write.Characteristic -> {
                if (action.characteristic.uuid.uuidString == uuid.uuidString) {
                    action.characteristic
                } else null
            }
            else -> null
        }

        characteristicToUpdate?.let {
            onUpdate?.invoke(it)
            it.updateValue()
            handleCurrentActionCompleted()
        }
    }

    internal suspend fun handleUpdatedDescriptor(uuid: UUID, onUpdate: ((Descriptor) -> Unit)? = null) {
        val descriptorToUpdate = when (val action = currentAction) {
            is DeviceAction.Read.Descriptor -> {
                if (action.descriptor.uuid.uuidString == uuid.uuidString) {
                    action.descriptor
                } else null
            }
            is DeviceAction.Write.Descriptor -> {
                if (action.descriptor.uuid.uuidString == uuid.uuidString) {
                    action.descriptor
                } else null
            }
            else -> null
        }

        descriptorToUpdate?.let {
            onUpdate?.invoke(it)
            it.updateValue()
            handleCurrentActionCompleted()
        }
    }

}

internal expect class DeviceConnectionManager : BaseDeviceConnectionManager