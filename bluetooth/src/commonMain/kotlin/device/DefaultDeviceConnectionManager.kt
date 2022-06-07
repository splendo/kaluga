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
import com.splendo.kaluga.base.flow.SequentialMutableSharedFlow
import com.splendo.kaluga.bluetooth.Characteristic
import com.splendo.kaluga.bluetooth.Descriptor
import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.ServiceWrapper
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.uuidString
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.logging.info
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlin.jvm.JvmName

interface DeviceConnectionManager {
    enum class State {
        DISCONNECTED,
        DISCONNECTING,
        CONNECTED,
        CONNECTING
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

    val events: Flow<Event>
    val rssi: Flow<Int>

    fun getCurrentState(): State
    suspend fun connect()
    suspend fun discoverServices()
    suspend fun disconnect()
    suspend fun readRssi()
    suspend fun requestMtu(mtu: Int): Boolean
    suspend fun performAction(action: DeviceAction)
    fun startConnecting()
    fun cancelConnecting()
    fun handleConnect()
    fun startDiscovering()
    fun startDisconnecting()
    fun handleDisconnect(onDisconnect: (suspend () -> Unit)? = null)
    suspend fun reset()
}

abstract class BaseDeviceConnectionManager(
    val deviceWrapper: DeviceWrapper,
    private val settings: ConnectionSettings,
    private val coroutineScope: CoroutineScope
) : DeviceConnectionManager, CoroutineScope by coroutineScope {

    internal companion object {
        const val BUFFER_CAPACITY = 256
    }

    interface Builder {
        fun create(
            deviceWrapper: DeviceWrapper,
            settings: ConnectionSettings,
            coroutineScope: CoroutineScope
        ): BaseDeviceConnectionManager
    }

    private val logTag = "Bluetooth Device ${deviceWrapper.identifier.stringValue}"

    private val _currentAction = AtomicReference<DeviceAction?>(null)
    protected var currentAction: DeviceAction?
        get() = _currentAction.get()
        set(value) { _currentAction.set(value) }
    protected val notifyingCharacteristics = sharedMutableMapOf<String, Characteristic>()

    private val sharedEvents = SequentialMutableSharedFlow<DeviceConnectionManager.Event>(0, settings.eventBufferSize, coroutineScope)
    override val events = sharedEvents.asSharedFlow()

    private val sharedRssi = MutableSharedFlow<Int>(0, 1, BufferOverflow.DROP_OLDEST)
    override val rssi = sharedRssi.asSharedFlow()

    override suspend fun readRssi() {
        logDebug { "Request Read RSSI" }
    }

    fun handleNewRssi(rssi: Int) {
        logDebug { "Updated Rssi $rssi" }
        sharedRssi.tryEmit(rssi)
    }

    fun handleNewMtu(mtu: Int) {
        logDebug { "Updated Mtu $mtu" }
        emitSharedEvent(DeviceConnectionManager.Event.MtuUpdated(mtu))
    }

    override fun startConnecting() {
        logInfo { "Start Connecting" }
        emitSharedEvent(DeviceConnectionManager.Event.Connecting)
    }

    override fun cancelConnecting() {
        logInfo { "Cancel Connecting" }
        emitSharedEvent(DeviceConnectionManager.Event.CancelledConnecting)
    }

    override fun handleConnect() {
        logInfo { "Did Connect" }
        emitSharedEvent(DeviceConnectionManager.Event.Connected)
    }

    override fun startDisconnecting() {
        logInfo { "Start Disconnecting" }
        emitSharedEvent(DeviceConnectionManager.Event.Disconnecting)
    }

    override suspend fun performAction(action: DeviceAction) {
        logInfo { "Perform action $action" }
    }

    fun createService(wrapper: ServiceWrapper): Service = Service(wrapper, ::emitSharedEvent, logTag, settings.logLevel)

    override fun handleDisconnect(onDisconnect: (suspend () -> Unit)?) {
        val currentAction = _currentAction
        val notifyingCharacteristics = this.notifyingCharacteristics
        val clean = suspend {
            currentAction.value = null
            notifyingCharacteristics.clear()
            onDisconnect?.invoke()
            Unit
        }
        logInfo { "Did Disconnect" }
        emitSharedEvent(DeviceConnectionManager.Event.Disconnected(clean))
    }

    override fun startDiscovering() {
        logInfo { "Start Discovering Services" }
        emitSharedEvent(DeviceConnectionManager.Event.Discovering)
    }

    @JvmName("handleDiscoverWrappersCompleted")
    fun handleDiscoverCompleted(serviceWrappers: List<ServiceWrapper>) = handleDiscoverCompleted(serviceWrappers.map { createService(it) })

    fun handleDiscoverCompleted(services: List<Service>) {
        logInfo { "Discovered services: ${services.map { it.uuid.uuidString }}" }
        emitSharedEvent(DeviceConnectionManager.Event.DiscoveredServices(services))
    }

    open fun handleCurrentActionCompleted(succeeded: Boolean) {
        val currentAction = this.currentAction
        _currentAction.value = null
        if (currentAction != null) {
            if (succeeded)
                logInfo { "Completed $currentAction successfully" }
            else
                logError { "Failed to complete $currentAction" }
        }
        emitSharedEvent(DeviceConnectionManager.Event.CompletedAction(currentAction, succeeded))
    }

    fun handleUpdatedCharacteristic(uuid: UUID, succeeded: Boolean, onUpdate: ((Characteristic) -> Unit)? = null) {
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

    fun handleUpdatedDescriptor(uuid: UUID, succeeded: Boolean, onUpdate: ((Descriptor) -> Unit)? = null) {
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

    override suspend fun reset() {
        currentAction = null
        notifyingCharacteristics.clear()
        disconnect()
    }

    private fun emitSharedEvent(event: DeviceConnectionManager.Event) {
        if (!sharedEvents.tryEmitOrLaunchAndEmit(event)) {
            logError { "Failed to Emit $event instantly. This may indicate that your event buffer is full. Increase the buffer size or reduce the number of events on this thread" }
        }
    }

    protected fun logInfo(message: () -> String) {
        if (settings.logLevel != ConnectionSettings.LogLevel.NONE) {
            info(logTag, message)
        }
    }

    protected fun logDebug(message: () -> String) {
        if (settings.logLevel == ConnectionSettings.LogLevel.VERBOSE) {
            debug(logTag, message)
        }
    }

    protected fun logError(message: () -> String) {
        if (settings.logLevel == ConnectionSettings.LogLevel.VERBOSE) {
            com.splendo.kaluga.logging.error(logTag, message)
        }
    }
}

internal expect class DefaultDeviceConnectionManager : BaseDeviceConnectionManager
