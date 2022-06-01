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
import com.splendo.kaluga.bluetooth.ServiceWrapper
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.uuidString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.jvm.JvmName

abstract class BaseDeviceConnectionManager(
    val deviceWrapper: DeviceWrapper,
    bufferCapacity: Int = BUFFER_CAPACITY,
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

    sealed class Event {
        object Connecting : Event()
        object CancelledConnecting : Event()
        object Connected : Event()
        object Disconnecting : Event()
        data class Disconnected(val onDisconnect: suspend () -> Unit) : Event()
        object Discovering : Event()
        data class DiscoveredServices(val services: List<Service>) : Event()
        data class AddAction(val action: DeviceAction) : Event()
        data class CompletedAction(val action: DeviceAction?, val succeeded: Boolean) : Event()
        data class MtuUpdated(val newMtu: Int) : Event()
    }

    private val _currentAction = AtomicReference<DeviceAction?>(null)
    protected var currentAction: DeviceAction?
        get() = _currentAction.get()
        set(value) { _currentAction.set(value) }
    protected val notifyingCharacteristics = sharedMutableMapOf<String, Characteristic>()

    private val sharedEvents = MutableSharedFlow<Event>(0, bufferCapacity, BufferOverflow.DROP_OLDEST)
    val events = sharedEvents.asSharedFlow()

    private val sharedRssi = MutableSharedFlow<Int>(0, 1, BufferOverflow.DROP_OLDEST)
    val rssi = sharedRssi.asSharedFlow()

    abstract suspend fun connect()
    abstract suspend fun discoverServices()
    abstract suspend fun disconnect()
    abstract suspend fun readRssi()
    abstract suspend fun requestMtu(mtu: Int): Boolean
    abstract suspend fun performAction(action: DeviceAction)

    fun handleNewRssi(rssi: Int) {
        sharedRssi.tryEmit(rssi)
    }

    fun handleNewMtu(mtu: Int) {
        sharedEvents.tryEmit(Event.MtuUpdated(mtu))
    }

    fun startConnecting() {
        sharedEvents.tryEmit(Event.Connecting)
    }

    fun cancelConnecting() {
        sharedEvents.tryEmit(Event.CancelledConnecting)
    }

    fun handleConnect() {
        sharedEvents.tryEmit(Event.Connected)
    }

    fun startDisconnecting() {
        sharedEvents.tryEmit(Event.Disconnecting)
    }

    fun createService(wrapper: ServiceWrapper): Service = Service(wrapper, sharedEvents)

    suspend fun handleDisconnect(onDisconnect: (suspend () -> Unit)? = null) {
        val currentAction = _currentAction
        val notifyingCharacteristics = this.notifyingCharacteristics
        val clean = suspend {
            currentAction.value = null
            notifyingCharacteristics.clear()
            onDisconnect?.invoke()
            Unit
        }
        sharedEvents.tryEmit(Event.Disconnected(clean))
    }

    fun startDiscovering() {
        sharedEvents.tryEmit(Event.Discovering)
    }

    @JvmName("handleDiscoverWrappersCompleted")
    fun handleDiscoverCompleted(serviceWrappers: List<ServiceWrapper>) = handleDiscoverCompleted(serviceWrappers.map { createService(it) })

    fun handleDiscoverCompleted(services: List<Service>) {
        sharedEvents.tryEmit(Event.DiscoveredServices(services))
    }

    open fun handleCurrentActionCompleted(succeeded: Boolean) {
        val currentAction = this.currentAction
        _currentAction.value = null
        sharedEvents.tryEmit(Event.CompletedAction(currentAction, succeeded))
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
}

internal expect class DeviceConnectionManager : BaseDeviceConnectionManager
