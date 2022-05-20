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
import co.touchlab.stately.concurrency.value
import com.splendo.kaluga.bluetooth.Characteristic
import com.splendo.kaluga.bluetooth.Descriptor
import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.uuidString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

abstract class BaseDeviceConnectionManager(
    val deviceWrapper: DeviceWrapper,
    private val bufferCapacity: Int = BUFFER_CAPACITY,
    coroutineScope: CoroutineScope
) : CoroutineScope by coroutineScope {

    internal companion object {
        const val BUFFER_CAPACITY = 256
    }

    interface Builder {
        fun create(
            deviceWrapper: DeviceWrapper,
            bufferCapacity: Int = BUFFER_CAPACITY,
            coroutineScope: CoroutineScope
        ): BaseDeviceConnectionManager
    }

    private val _currentAction = AtomicReference<DeviceAction?>(null)
    protected var currentAction: DeviceAction?
        get() = _currentAction.get()
        set(value) { _currentAction.set(value) }
    protected val notifyingCharacteristics = sharedMutableMapOf<String, Characteristic>()

    private val _rssiUpdate = createSharedFlow<Int>()
    val rssiUpdate: Flow<Int> = _rssiUpdate
    private val _startedConnecting = createSharedFlow<Unit>()
    val startedConnecting: Flow<Unit> = _startedConnecting
    private val _cancelledConnecting = createSharedFlow<Unit>()
    val cancelledConnecting: Flow<Unit> = _cancelledConnecting
    private val _didConnect = createSharedFlow<Unit>()
    val didConnect: Flow<Unit> = _didConnect
    private val _startedDisconnecting = createSharedFlow<Unit>()
    val startedDisconnecting: Flow<Unit> = _startedDisconnecting
    private val _didDisconnect = createSharedFlow<suspend () -> Unit>()
    val didDisconnect: Flow<suspend () -> Unit> = _didDisconnect
    private val _startedDiscovering = createSharedFlow<Unit>()
    val startedDiscovering: Flow<Unit> = _startedDiscovering
    private val _discoverCompleted = createSharedFlow<List<Service>>()
    val discoverCompleted: Flow<List<Service>> = _discoverCompleted
    val newAction = createSharedFlow<DeviceAction>()
    private val _actionCompleted = createSharedFlow<Pair<DeviceAction?, Boolean>>()
    val actionCompleted: Flow<Pair<DeviceAction?, Boolean>> = _actionCompleted

    private val _mtu = MutableStateFlow(-1)
    val mtuFlow: Flow<Int> = _mtu
    val mtu: Int get() = _mtu.value

    abstract suspend fun connect()
    abstract suspend fun discoverServices()
    abstract suspend fun disconnect()
    abstract suspend fun readRssi()
    abstract suspend fun requestMtu(mtu: Int): Boolean
    abstract suspend fun performAction(action: DeviceAction)

    suspend fun handleNewRssi(rssi: Int) {
        _rssiUpdate.emit(rssi)
    }

    fun handleNewMtu(mtu: Int) {
        _mtu.value = mtu
    }

    fun startConnecting() {
        launch {
            _startedConnecting.emit(Unit)
        }
    }

    fun cancelConnecting() {
        launch {
            _cancelledConnecting.emit(Unit)
        }
    }

    suspend fun handleConnect() {
        _didConnect.emit(Unit)
    }

    fun startDisconnecting() {
        launch {
            _startedDisconnecting.emit(Unit)
        }
    }

    suspend fun handleDisconnect(onDisconnect: (suspend () -> Unit)? = null) {
        val currentAction = _currentAction
        val notifyingCharacteristics = this.notifyingCharacteristics
        val clean = suspend {
            currentAction.value = null
            notifyingCharacteristics.clear()
            onDisconnect?.invoke()
            Unit
        }
        _didDisconnect.emit(clean)
    }

    fun startDiscovering() {
        launch { _startedDiscovering.emit(Unit) }
    }

    suspend fun handleDiscoverCompleted(services: List<Service>) {
        _discoverCompleted.emit(services)
    }

    open suspend fun handleCurrentActionCompleted(succeeded: Boolean) {
        val currentAction = this.currentAction
        _currentAction.value = null
        _actionCompleted.emit(currentAction to succeeded)
    }

    suspend fun handleUpdatedCharacteristic(uuid: UUID, succeeded: Boolean, onUpdate: ((Characteristic) -> Unit)? = null) {
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
            handleCurrentActionCompleted(succeeded)
        }
    }

    suspend fun handleUpdatedDescriptor(uuid: UUID, succeeded: Boolean, onUpdate: ((Descriptor) -> Unit)? = null) {
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
            handleCurrentActionCompleted(succeeded)
        }
    }

    suspend fun reset() {
        currentAction = null
        notifyingCharacteristics.clear()
        disconnect()
    }

    private fun <T> createSharedFlow(): MutableSharedFlow<T> = MutableSharedFlow(0, bufferCapacity, BufferOverflow.DROP_OLDEST)
}

internal expect class DeviceConnectionManager : BaseDeviceConnectionManager
