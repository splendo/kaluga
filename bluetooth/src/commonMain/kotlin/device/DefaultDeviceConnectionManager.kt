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
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.logging.error
import com.splendo.kaluga.logging.info
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
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
    suspend fun unpair()
    suspend fun pair()
}

abstract class BaseDeviceConnectionManager(
    val deviceWrapper: DeviceWrapper,
    settings: ConnectionSettings,
    private val coroutineScope: CoroutineScope
) : DeviceConnectionManager, CoroutineScope by coroutineScope {

    interface Builder {
        fun create(
            deviceWrapper: DeviceWrapper,
            settings: ConnectionSettings,
            coroutineScope: CoroutineScope
        ): BaseDeviceConnectionManager
    }

    private val logTag = "Bluetooth Device ${deviceWrapper.identifier.stringValue}"
    private val logger = settings.logger

    private val _currentAction = AtomicReference<DeviceAction?>(null)
    protected var currentAction: DeviceAction?
        get() = _currentAction.get()
        set(value) { _currentAction.set(value) }
    protected val notifyingCharacteristics = sharedMutableMapOf<String, Characteristic>()

    private val sharedEvents = Channel<DeviceConnectionManager.Event>(UNLIMITED)
    override val events: Flow<DeviceConnectionManager.Event> = sharedEvents.receiveAsFlow()

    private val sharedRssi = MutableSharedFlow<Int>(0, 1, BufferOverflow.DROP_OLDEST)
    override val rssi = sharedRssi.asSharedFlow()

    override suspend fun readRssi() {
        logger.debug(logTag) { "Request Read RSSI" }
        // TODO call into abstract function?
    }

    fun handleNewRssi(rssi: Int) {
        logger.debug(logTag) { "Updated Rssi $rssi" }
        sharedRssi.tryEmit(rssi)
    }

    fun handleNewMtu(mtu: Int) {
        logger.debug(logTag) { "Updated Mtu $mtu" }
        emitSharedEvent(DeviceConnectionManager.Event.MtuUpdated(mtu))
    }

    override fun startConnecting() {
        logger.info(logTag) { "Start Connecting" }
        emitSharedEvent(DeviceConnectionManager.Event.Connecting)
    }

    override fun cancelConnecting() {
        logger.info(logTag) { "Cancel Connecting" }
        emitSharedEvent(DeviceConnectionManager.Event.CancelledConnecting)
    }

    override fun handleConnect() {
        logger.info(logTag) { "Did Connect" }
        emitSharedEvent(DeviceConnectionManager.Event.Connected)
    }

    override fun startDisconnecting() {
        logger.info(logTag) { "Start Disconnecting" }
        emitSharedEvent(DeviceConnectionManager.Event.Disconnecting)
    }

    override suspend fun performAction(action: DeviceAction) {
        logger.info(logTag) { "Perform action $action" }
        // TODO call into abstract function?
    }

    // TODO add logging for pairing

    fun createService(wrapper: ServiceWrapper): Service = Service(wrapper, ::emitSharedEvent, logTag, logger)

    override fun handleDisconnect(onDisconnect: (suspend () -> Unit)?) {
        val currentAction = _currentAction
        val notifyingCharacteristics = this.notifyingCharacteristics
        val clean = suspend {
            currentAction.value = null
            notifyingCharacteristics.clear()
            onDisconnect?.invoke()
            Unit
        }
        logger.info(logTag) { "Did Disconnect" }
        emitSharedEvent(DeviceConnectionManager.Event.Disconnected(clean))
    }

    override fun startDiscovering() {
        logger.info(logTag) { "Start Discovering Services" }
        emitSharedEvent(DeviceConnectionManager.Event.Discovering)
    }

    @JvmName("handleDiscoverWrappersCompleted")
    fun handleDiscoverCompleted(serviceWrappers: List<ServiceWrapper>) = handleDiscoverCompleted(serviceWrappers.map { createService(it) })

    fun handleDiscoverCompleted(services: List<Service>) {
        logger.info(logTag) { "Discovered services: ${services.map { it.uuid.uuidString }}" }
        emitSharedEvent(DeviceConnectionManager.Event.DiscoveredServices(services))
    }

    open fun handleCurrentActionCompleted(succeeded: Boolean) {
        val currentAction = this.currentAction
        _currentAction.value = null
        if (currentAction != null) {
            if (succeeded)
                logger.info(logTag) { "Completed $currentAction successfully" }
            else
                logger.error(logTag) { "Failed to complete $currentAction" }
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
        // Channel has unlimited buffer so this will never fail due to capacity
        sharedEvents.trySend(event)
    }
}

internal expect class DefaultDeviceConnectionManager : BaseDeviceConnectionManager
