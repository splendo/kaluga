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

import co.touchlab.stately.collections.sharedMutableMapOf
import co.touchlab.stately.concurrency.AtomicReference
import com.splendo.kaluga.bluetooth.Characteristic
import com.splendo.kaluga.bluetooth.Descriptor
import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.uuidString
import kotlinx.coroutines.CoroutineScope

abstract class BaseDeviceConnectionManager(
    val connectionSettings: ConnectionSettings = ConnectionSettings(),
    val deviceWrapper: DeviceWrapper,
    val stateRepo: DeviceStateFlowRepo
) : CoroutineScope by stateRepo {

    interface Builder {
        fun create(
            connectionSettings: ConnectionSettings,
            deviceWrapper: DeviceWrapper,
            stateRepo: DeviceStateFlowRepo
        ): BaseDeviceConnectionManager
    }

    private val _currentAction = AtomicReference<DeviceAction?>(null)
    protected var currentAction: DeviceAction?
        get() = _currentAction.get()
        set(value) { _currentAction.set(value) }
    protected val notifyingCharacteristics = sharedMutableMapOf<String, Characteristic>()

    abstract suspend fun connect()
    abstract suspend fun discoverServices()
    abstract suspend fun disconnect()
    abstract suspend fun readRssi()
    abstract suspend fun performAction(action: DeviceAction)

    suspend fun handleNewRssi(rssi: Int) {
        stateRepo.takeAndChangeState {
            it.rssiDidUpdate(rssi)
        }
    }

    suspend fun handleConnect() {
        stateRepo.takeAndChangeState { state ->
            when (state) {
                is DeviceState.Connecting -> state.didConnect
                is DeviceState.Reconnecting -> state.didConnect
                is DeviceState.Connected -> state.remain()
                else -> {
                    currentAction = null
                    notifyingCharacteristics.clear()
                    disconnect()
                    state.remain()
                }
            }
        }
    }

    suspend fun handleDisconnect(onDisconnect: (suspend () -> Unit)? = null) {
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
                is DeviceState.Connected -> when (connectionSettings.reconnectionSettings) {
                    is ConnectionSettings.ReconnectionSettings.Always,
                    is ConnectionSettings.ReconnectionSettings.Limited -> state.reconnect
                    is ConnectionSettings.ReconnectionSettings.Never -> {
                        clean()
                        state.didDisconnect
                    }
                }
                is DeviceState.Disconnected -> state.remain()
                is DeviceState.Connecting,
                is DeviceState.Disconnecting -> {
                    clean()
                    state.didDisconnect
                }
            }
        }
    }

    suspend fun handleScanCompleted(services: List<Service>) {
        stateRepo.takeAndChangeState { state ->
            when (state) {
                is DeviceState.Connected.Discovering -> state.didDiscoverServices(services)
                else -> state.remain()
            }
        }
    }

    suspend open fun handleCurrentActionCompleted() = stateRepo.takeAndChangeState { state ->
        (
            if (state is DeviceState.Connected.HandlingAction && state.action == currentAction)
                state.actionCompleted
            else
                state.remain()
        ).also { currentAction = null}
    }


    suspend fun handleUpdatedCharacteristic(uuid: UUID, onUpdate: ((Characteristic) -> Unit)? = null) {
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

    suspend fun handleUpdatedDescriptor(uuid: UUID, onUpdate: ((Descriptor) -> Unit)? = null) {
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
