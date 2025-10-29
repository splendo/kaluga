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

import com.splendo.kaluga.base.collections.concurrentMutableMapOf
import com.splendo.kaluga.bluetooth.MTU
import com.splendo.kaluga.bluetooth.RSSI
import com.splendo.kaluga.bluetooth.RemoteCharacteristic
import com.splendo.kaluga.bluetooth.RemoteDescriptor
import com.splendo.kaluga.bluetooth.RemoteService
import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.ServiceWrapper
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.uuidString
import com.splendo.kaluga.logging.ContextualLogger
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.logging.error
import com.splendo.kaluga.logging.info
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlin.jvm.JvmName

/**
 * A manager for connecting a [Device]
 */
interface DeviceConnectionManager {

    /**
     * Builder for creating a [BaseDeviceConnectionManager]
     */
    interface Builder {

        /**
         * Creates a [DeviceConnectionManager]
         * @param deviceWrapper the [DeviceWrapper] wrapping the [ConnectableDevice]
         * @param settings the [ConnectionSettings] to apply for connecting
         * @param coroutineScope the [CoroutineScope] on which the device should be managed
         * @return the created [DeviceConnectionManager]
         */
        fun create(deviceWrapper: DeviceWrapper, settings: ConnectionSettings, coroutineScope: CoroutineScope): DeviceConnectionManager
    }

    /**
     * The state of a [DeviceConnectionManager]
     */
    enum class State {
        /**
         * Device is disconnected
         */
        DISCONNECTED,

        /**
         * Device is disconnecting
         */
        DISCONNECTING,

        /**
         * Device is connected
         */
        CONNECTED,

        /**
         * Device is connecting
         */
        CONNECTING,
    }

    /**
     * Events detected by a [DeviceConnectionManager]
     */
    sealed class Event {

        /**
         * [Event] indicating the device started connecting
         * @param reconnectionSettings the [ConnectionSettings.ReconnectionSettings] to use when reconnecting if the device disconnects unexpectedly
         */
        data class Connecting(val reconnectionSettings: ConnectionSettings.ReconnectionSettings) : Event()

        /**
         * [Event] indicating the device cancelled connecting
         */
        data object CancelledConnecting : Event()

        /**
         * [Event] indicating the device did connect
         */
        data object Connected : Event()

        /**
         * [Event] indicating the device started disconnecting
         */
        data object Disconnecting : Event()

        /**
         * [Event] indicating the device did disconnect
         * @property onDisconnect the action to execute once the event has been handled
         */
        data class Disconnected(val onDisconnect: suspend () -> Unit) : Event()

        /**
         * [Event] indicating the device started discovering services
         */
        data object Discovering : Event()

        /**
         * [Event] indicating the device has discovered a list of [Service]
         * @property services the list of [Service] discovered
         */
        data class DiscoveredServices(val services: List<Service>) : Event()

        /**
         * [Event] indicating a [DeviceAction] should be scheduled
         * @property action the [DeviceAction] to schedule
         */
        data class AddAction(val action: DeviceAction) : Event()

        /**
         * [Event] indicating the device completed executing a [DeviceAction]
         * @property action the [DeviceAction] that was executed
         * @property succeeded if `true`, [action] completed successfully
         */
        data class CompletedAction(val action: DeviceAction?, val succeeded: Boolean) : Event()
    }

    /**
     * A [Flow] of all the [Event] detected by the connection manager
     */
    val events: Flow<Event>

    /**
     * A [Flow] of the RSSI value of the device
     */
    val rssi: Flow<RSSI>

    /**
     * Gets the current [State] of the device
     */
    fun getCurrentState(): State

    /**
     * Starts connecting to the device
     */
    fun connect()

    /**
     * Starts discovering [Service] for the device
     */
    suspend fun discoverServices()

    /**
     * Starts disconnecting from the device
     */
    fun disconnect()

    /**
     * Starts reading the latest RSSI value of the device
     */
    suspend fun readRssi()

    /**
     * Starts performing a [DeviceAction]
     * @param action the [DeviceAction] to perform
     */
    suspend fun performAction(action: DeviceAction)

    /**
     * Fires an [Event.Connecting]
     * @param reconnectionSettings the [ConnectionSettings.ReconnectionSettings] to use when reconnecting if the device disconnects unexpectedly. If `null` the default will be used.
     */
    fun startConnecting(reconnectionSettings: ConnectionSettings.ReconnectionSettings? = null)

    /**
     * Fires an [Event.CancelledConnecting]
     */
    fun cancelConnecting()

    /**
     * Fires an [Event.Connected]
     */
    fun handleConnect()

    /**
     * Fires an [Event.Discovering]
     */
    fun startDiscovering()

    /**
     * Fires an [Event.Disconnecting]
     */
    fun startDisconnecting()

    /**
     * Fires an [Event.Disconnected]
     */
    fun handleDisconnect(onDisconnect: (suspend () -> Unit)? = null)

    /**
     * Fires an [Event.AddAction] with [DeviceAction.RequestMtu]
     */
    fun requestMtu(mtu: MTU): DeviceAction.RequestMtu

    /**
     * Resets all actions and disconnects the device
     */
    suspend fun reset()

    /**
     * Pairs the device
     */
    suspend fun pair()

    /**
     * Unpairs from the device
     */
    suspend fun unpair()
}

/**
 * A [DeviceConnectionManager] using a [DeviceWrapper]
 * @param deviceWrapper the [DeviceWrapper] wrapping the [Device]
 * @param settings the [ConnectionSettings] to apply for connecting
 * @param coroutineScope the [CoroutineScope] on which the device should be managed
 */
abstract class BaseDeviceConnectionManager(protected val deviceWrapper: DeviceWrapper, settings: ConnectionSettings, private val coroutineScope: CoroutineScope) :
    DeviceConnectionManager,
    CoroutineScope by coroutineScope {

    private val tag = "Bluetooth ${deviceWrapper.identifier.stringValue}"
    protected val logger = ContextualLogger(settings.logger, tag)
    protected val dataLogger = ContextualLogger(settings.dataLogger, tag)

    private val defaultReconnectionSettings = settings.reconnectionSettings

    protected var currentAction: DeviceAction? = null
    protected val notifyingCharacteristics = concurrentMutableMapOf<String, RemoteCharacteristic>()

    private val eventChannel = Channel<DeviceConnectionManager.Event>(UNLIMITED)
    override val events: Flow<DeviceConnectionManager.Event> = eventChannel.receiveAsFlow()

    private val sharedRssi = MutableSharedFlow<RSSI>(0, 1, BufferOverflow.DROP_OLDEST)
    override val rssi = sharedRssi.asSharedFlow()

    override suspend fun readRssi() {
        logger.debug { "Request Read RSSI" }
        // TODO call into abstract function?
    }

    protected open fun handleNewRssi(rssi: RSSI) {
        logger.debug { "Updated Rssi $rssi" }
        sharedRssi.tryEmit(rssi)
    }

    protected fun handleNewMtu(mtu: MTU, succeeded: Boolean) {
        logger.debug { "Updated Mtu $mtu" }
        val action = currentAction
        if (action is DeviceAction.RequestMtu) {
            action.mtuResponse = mtu
            handleCurrentActionCompleted(succeeded)
        }
    }

    final override fun startConnecting(reconnectionSettings: ConnectionSettings.ReconnectionSettings?) {
        logger.info { "Start Connecting" }
        emitEvent(DeviceConnectionManager.Event.Connecting(reconnectionSettings ?: defaultReconnectionSettings))
    }

    final override fun cancelConnecting() {
        logger.info { "Cancel Connecting" }
        emitEvent(DeviceConnectionManager.Event.CancelledConnecting)
    }

    final override fun handleConnect() {
        logger.info { "Did Connect" }
        emitEvent(DeviceConnectionManager.Event.Connected)
    }

    final override fun startDisconnecting() {
        logger.info { "Start Disconnecting" }
        emitEvent(DeviceConnectionManager.Event.Disconnecting)
    }

    final override suspend fun performAction(action: DeviceAction) {
        logger.info { "Perform action $action" }
        didStartPerformingAction(action)
    }

    protected abstract suspend fun didStartPerformingAction(action: DeviceAction)

    final override suspend fun pair() {
        logger.info { "Pair" }
        requestStartPairing()
    }

    protected abstract suspend fun requestStartPairing()

    final override suspend fun unpair() {
        logger.info { "Unpair" }
        requestStartUnpairing()
    }

    protected abstract suspend fun requestStartUnpairing()

    protected open fun createService(wrapper: ServiceWrapper): RemoteService = RemoteService(
        wrapper,
        wrapper.includedServices.map { createService(it) },
        ::emitEvent,
        dataLogger
    )

    final override fun handleDisconnect(onDisconnect: (suspend () -> Unit)?) {
        val currentAction = this.currentAction
        currentAction?.completedSuccessfully?.cancel()
        val notifyingCharacteristics = this.notifyingCharacteristics
        val clean = suspend {
            this.currentAction = null
            notifyingCharacteristics.clear()
            onDisconnect?.invoke()
            Unit
        }
        logger.info { "Did Disconnect" }
        emitEvent(DeviceConnectionManager.Event.Disconnected(clean))
    }

    override fun requestMtu(mtu: MTU): DeviceAction.RequestMtu {
        val action = DeviceAction.RequestMtu(mtu)
        emitEvent(DeviceConnectionManager.Event.AddAction(action))
        return action
    }

    final override fun startDiscovering() {
        logger.info { "Start Discovering Services" }
        emitEvent(DeviceConnectionManager.Event.Discovering)
    }

    @JvmName("handleDiscoverWrappersCompleted")
    internal fun handleDiscoverCompleted(serviceWrappers: List<ServiceWrapper>) = handleDiscoverCompleted(serviceWrappers.map { createService(it) })

    protected open fun handleDiscoverCompleted(services: List<Service>) {
        logger.info { "Discovered services: ${services.map { it.uuid.uuidString }}" }
        emitEvent(DeviceConnectionManager.Event.DiscoveredServices(services))
    }

    protected open fun handleCurrentActionCompleted(succeeded: Boolean) {
        val currentAction = this.currentAction
        this.currentAction = null
        if (currentAction != null) {
            if (succeeded) {
                logger.info { "Completed $currentAction successfully" }
            } else {
                logger.error { "Failed to complete $currentAction" }
            }
        }
        emitEvent(DeviceConnectionManager.Event.CompletedAction(currentAction, succeeded))
    }

    protected open fun handleUpdatedCharacteristic(uuid: UUID, succeeded: Boolean, onUpdate: ((RemoteCharacteristic) -> Unit)? = null) {
        notifyingCharacteristics[uuid.uuidString]?.let {
            onUpdate?.invoke(it)
            it.updateValue()
        }
        val characteristicToUpdate = when (val action = currentAction) {
            is DeviceAction.Read.Characteristic -> {
                if (action.characteristic.uuid.uuidString == uuid.uuidString) {
                    action.characteristic
                } else {
                    null
                }
            }
            is DeviceAction.Write.Characteristic -> {
                if (action.characteristic.uuid.uuidString == uuid.uuidString) {
                    action.characteristic
                } else {
                    null
                }
            }
            else -> null
        }

        characteristicToUpdate?.let {
            onUpdate?.invoke(it)
            it.updateValue()
            handleCurrentActionCompleted(succeeded)
        }
    }

    protected open fun handleUpdatedDescriptor(uuid: UUID, succeeded: Boolean, onUpdate: ((RemoteDescriptor) -> Unit)? = null) {
        val descriptorToUpdate = when (val action = currentAction) {
            is DeviceAction.Read.Descriptor -> {
                if (action.descriptor.uuid.uuidString == uuid.uuidString) {
                    action.descriptor
                } else {
                    null
                }
            }
            is DeviceAction.Write.Descriptor -> {
                if (action.descriptor.uuid.uuidString == uuid.uuidString) {
                    action.descriptor
                } else {
                    null
                }
            }
            else -> {
                null
            }
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

    private fun emitEvent(event: DeviceConnectionManager.Event) {
        // Channel has unlimited buffer so this will never fail due to capacity
        eventChannel.trySend(event)
    }
}

internal expect class DefaultDeviceConnectionManager : BaseDeviceConnectionManager {
    override fun connect()
    override fun disconnect()
    override fun getCurrentState(): DeviceConnectionManager.State
    override suspend fun discoverServices()
    override suspend fun didStartPerformingAction(action: DeviceAction)
    override suspend fun requestStartPairing()
    override suspend fun requestStartUnpairing()
}
