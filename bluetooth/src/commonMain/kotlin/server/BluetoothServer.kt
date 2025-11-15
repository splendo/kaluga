/*
 Copyright 2025 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bluetooth.server

import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.base.utils.getCompletedOrNull
import com.splendo.kaluga.base.utils.toHexString
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.uuidFrom
import com.splendo.kaluga.bluetooth.uuidString
import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.RestrictedLogLevel
import com.splendo.kaluga.logging.RestrictedLogger
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.logging.info
import com.splendo.kaluga.logging.warn
import com.splendo.kaluga.permissions.base.Permissions
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.cancellation.CancellationException

interface BluetoothServerDSL {
    fun advertise(data: AdvertiseData.Builder.() -> Unit)
    fun service(uuid: UUID, service: LocalService.DSL.Primary.() -> Unit)
    fun service(uuidString: String, service: LocalService.DSL.Primary.() -> Unit) {
        service(uuidFrom(uuidString), service)
    }
}

enum class ServerStatus {
    NOT_SUPPORTED,
    AWAITING_PERMISSIONS,
    AWAITING_BLUETOOTH_ENABLED,
    AVAILABLE,
    CLOSED,
}

internal typealias LocalCharacteristicRegisterReadAction = (
    characteristic: LocalCharacteristic,
    onRead: suspend LocalCharacteristic.(ConnectedDevice, Int) -> GattResponse.ReadResponse,
) -> Unit
internal typealias LocalCharacteristicRegisterWriteAction = (
    characteristic: LocalCharacteristic,
    onWrite: suspend LocalCharacteristic.(ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse,
) -> Unit
internal typealias LocalDescriptorRegisterReadAction = (descriptor: LocalDescriptor, onRead: suspend LocalDescriptor.(ConnectedDevice, Int) -> GattResponse.ReadResponse) -> Unit
internal typealias LocalDescriptorRegisterWriteAction = (
    descriptor: LocalDescriptor,
    onWrite: suspend LocalDescriptor.(ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse,
) -> Unit
internal typealias NotifiableRegisterSubscription = LocalCharacteristic.Notifiable.(encrypted: Boolean) -> Unit

internal sealed interface ServerState {
    sealed interface Initial : ServerState
    sealed interface Unavailable : ServerState
    sealed interface Active : ServerState {
        fun close(): Closed
    }
    object NotSupported : Initial, Unavailable {
        override val status: ServerStatus get() = ServerStatus.NOT_SUPPORTED
    }

    interface AwaitingPermissions :
        Initial,
        Active {
        suspend fun awaitPermitted(autoRequest: Boolean): HasPermissions

        override val status: ServerStatus get() = ServerStatus.AWAITING_PERMISSIONS
    }

    sealed interface HasPermissions : Active {
        suspend fun awaitRevoked(): AwaitingPermissions
    }

    interface AwaitingBluetoothEnabled : HasPermissions {
        suspend fun awaitEnabled(autoEnable: Boolean): Available
        override val status: ServerStatus get() = ServerStatus.AWAITING_BLUETOOTH_ENABLED
    }

    interface Available : HasPermissions {

        suspend fun addService(service: LocalService): Boolean
        fun removeService(service: LocalService)
        fun removeAllServices()

        suspend fun startAdvertising(data: AdvertiseData): Boolean
        fun stopAdvertising()

        suspend fun awaitDisabled(): AwaitingBluetoothEnabled
        override suspend fun awaitRevoked(): AwaitingPermissions

        suspend fun execute(characteristic: LocalCharacteristic.Notifiable, device: ConnectedDevice, value: ByteArray): Boolean

        override val status: ServerStatus get() = ServerStatus.AVAILABLE

        fun serviceBuilder(uuid: UUID, notify: Notify): LocalServiceDSL.Primary
    }

    object Closed : Unavailable {
        override val status: ServerStatus get() = ServerStatus.CLOSED
    }

    val status: ServerStatus
}

data class ServerSettings(
    val permissions: Permissions,
    val autoRequestPermission: Boolean = true,
    val autoEnableBluetooth: Boolean = true,
    val logger: Logger = RestrictedLogger(RestrictedLogLevel.Verbose),
)

class BluetoothServer internal constructor(private val settings: ServerSettings, initialState: ServerState.Initial, coroutineContext: CoroutineContext) :
    CoroutineScope by CoroutineScope(coroutineContext + CoroutineName("BluetoothServer")),
    AutoCloseable {

    companion object {
        const val TAG = "BluetoothServer"
    }

    internal class DSL(private val settings: ServerSettings, private val initialState: ServerState.Initial, private val coroutineContext: CoroutineContext) : BluetoothServerDSL {

        private var advertisementBuilder: (AdvertiseData.Builder.() -> Unit)? = null
        private val serviceBuilders = mutableMapOf<UUID, LocalService.DSL.Primary.() -> Unit>()

        override fun advertise(data: AdvertiseData.Builder.() -> Unit) {
            require(advertisementBuilder == null) { "Can only set advertisement data once" }
            advertisementBuilder = data
        }

        override fun service(uuid: UUID, service: LocalService.DSL.Primary.() -> Unit) {
            require(!serviceBuilders.containsKey(uuid)) { "Service $uuid already added" }
            serviceBuilders[uuid] = service
        }

        suspend fun build(): BluetoothServer = BluetoothServer(settings, initialState, coroutineContext).apply {
            try {
                advertisementBuilder?.let { advertise(it) }
                for ((uuid, builder) in serviceBuilders) {
                    add(uuid, builder)
                }
            } catch (e: kotlinx.coroutines.CancellationException) {
                close()
                throw e
            }
        }
    }

    private sealed class ServiceAction {
        class Add(val service: (ServerState.Available) -> LocalService, val isAdded: CompletableDeferred<LocalService?>) : ServiceAction()

        class Remove(val service: LocalService, val isRemoved: CompletableDeferred<Unit>) : ServiceAction()
        class RemoveAll(val isRemoved: CompletableDeferred<Unit>) : ServiceAction()
    }

    private class NotifyingAction(
        val characteristic: LocalCharacteristic.Notifiable,
        val device: ConnectedDevice,
        val value: ByteArray,
        val completed: CompletableDeferred<Boolean> = CompletableDeferred(),
    ) {
        fun notifySuccess() {
            completed.complete(true)
        }

        fun notifyFailed() {
            completed.complete(false)
        }
    }

    private data class AdvertisingSettings(val data: AdvertiseData, val hasStarted: CompletableDeferred<Boolean>, val onStop: () -> Unit) {

        class Builder : AdvertiseData.Builder {

            override var localName: String? = null
            private val serviceUUIDs = mutableSetOf<UUID>()

            override fun serviceUUIDs(vararg uuid: UUID) {
                serviceUUIDs.addAll(uuid)
            }

            fun build(hasStarted: CompletableDeferred<Boolean>, onStop: () -> Unit): AdvertisingSettings = AdvertisingSettings(
                AdvertiseData(
                    localName,
                    serviceUUIDs.toList(),
                ),
                hasStarted,
                onStop,
            )
        }

        fun onStartSuccess() {
            hasStarted.complete(true)
        }

        fun stop() {
            hasStarted.complete(false)
            onStop()
        }
    }

    private val logger = settings.logger
    private val _status = MutableStateFlow(initialState.status)
    val status: StateFlow<ServerStatus> = _status.asStateFlow()
    private val _isAdvertising = MutableStateFlow(false)
    val isAdvertising: StateFlow<Boolean> = _isAdvertising.asStateFlow()

    private val _services = MutableStateFlow<List<LocalService>>(emptyList())
    val services: StateFlow<List<LocalService>> = _services.asStateFlow()

    private val advertiseChannel = Channel<(ServerState.Available?) -> AdvertisingSettings>(capacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST) { undelivered ->
        undelivered(null).hasStarted.complete(false)
    }
    private var currentAdvertiseSettings: AdvertisingSettings? = null

    private val serviceActionChannel = Channel<ServiceAction>(capacity = Channel.UNLIMITED)
    private val notificationChannel = Channel<NotifyingAction>(capacity = Channel.UNLIMITED)
    private val stateJob = when (initialState) {
        is ServerState.AwaitingPermissions -> manageState(initialState)
        is ServerState.Unavailable -> null
    }

    suspend fun advertise(data: AdvertiseData.Builder.() -> Unit): Boolean = performOrFailOnClose(false) {

        debug("TEST", "Advertise")
        val hasStarted = CompletableDeferred<Boolean>()
        val advertisingSettingsBuilder = AdvertisingSettings.Builder().apply(data)
        try {
            advertiseChannel.send { active -> advertisingSettingsBuilder.build(hasStarted) { active?.stopAdvertising() } }
            hasStarted.await()
        } catch (_: ClosedSendChannelException) {
            false
        }
    }

    fun stopAdvertising() {
        stopAdvertising(true)
    }

    private fun stopAdvertising(log: Boolean) {
        currentAdvertiseSettings?.let {
            if (log) {
                logger.info(TAG) { "Stop Advertising" }
            }
            // If not started, inform starting failed
            it.stop()
        }
        currentAdvertiseSettings = null
        _isAdvertising.value = false
    }

    suspend fun add(uuid: UUID, service: LocalService.DSL.Primary.() -> Unit): LocalService? = performOrFailOnClose(null) {
        val response = CompletableDeferred<LocalService?>()
        val serviceBuilder: (ServerState.Available) -> LocalService = { available ->
            available.serviceBuilder(uuid, this::notify).apply(service).build()
        }
        try {
            serviceActionChannel.send(ServiceAction.Add(serviceBuilder, response))
            response.await()
        } catch (_: ClosedSendChannelException) {
            null
        }
    }

    suspend fun remove(service: LocalService) = performOrFailOnClose(false) {
        val completed = EmptyCompletableDeferred()
        try {
            serviceActionChannel.send(ServiceAction.Remove(service, completed))
            completed.await()
            true
        } catch (_: ClosedSendChannelException) {
            false
        }
    }

    suspend fun removeAllServices() = performOrFailOnClose(false) {
        val completed = EmptyCompletableDeferred()
        try {
            serviceActionChannel.send(ServiceAction.RemoveAll(completed))
            completed.await()
            true
        } catch (_: ClosedSendChannelException) {
            false
        }
    }

    internal suspend fun notify(characteristic: LocalCharacteristic.Notifiable, device: ConnectedDevice, value: ByteArray): Boolean {
        val action = NotifyingAction(characteristic, device, value)
        return try {
            notificationChannel.send(action)
            action.completed.await()
        } catch (_: ClosedSendChannelException) {
            false
        } catch (e: CancellationException) {
            // Ensure the event is not actually sent to the device
            action.completed.complete(false)
            throw e
        }
    }

    override fun close() {
        stateJob?.cancel()
        advertiseChannel.close()
        serviceActionChannel.close()
        notificationChannel.close()
    }

    private fun manageState(initialState: ServerState.AwaitingPermissions) = launch {
        var state: ServerState.Active = initialState
        _status.value = state.status
        try {
            while (true) {
                debug("TEST", "Manage state $state")
                val newState = when (val currentState = state) {
                    is ServerState.AwaitingPermissions -> {
                        logger.info(TAG) { "Missing Permissions" }
                        currentState.awaitPermitted(settings.autoRequestPermission).also {
                            logger.info(TAG) { "Has Permissions" }
                        }
                    }

                    is ServerState.AwaitingBluetoothEnabled -> {
                        logger.info(TAG) { "Bluetooth Disabled" }
                        val isEnabled = async { currentState.awaitEnabled(settings.autoEnableBluetooth) }
                        val isRevoked = async { currentState.awaitRevoked() }
                        try {
                            select {
                                isEnabled.onAwait { available ->
                                    logger.info(TAG) { "Bluetooth Enabled" }
                                    available
                                }
                                isRevoked.onAwait { awaitingPermissions ->
                                    awaitingPermissions
                                }
                            }
                        } finally {
                            isEnabled.cancel()
                            isRevoked.cancel()
                        }
                    }

                    is ServerState.Available -> {
                        val availableJob = launch {
                            onAvailable(currentState)
                        }
                        val isDisabled = async { currentState.awaitDisabled() }
                        val isRevoked = async { currentState.awaitRevoked() }
                        try {
                            select<ServerState.Active> {
                                isDisabled.onAwait { disabled ->
                                    disabled
                                }
                                isRevoked.onAwait { awaitingPermissions ->
                                    awaitingPermissions
                                }
                            }
                        } finally {
                            availableJob.cancel()
                            isDisabled.cancel()
                            isRevoked.cancel()
                        }
                    }
                }
                state = newState
                _status.value = state.status
            }
        } finally {

            debug("TEST", "Finish $initialState")
            val newState = state.close()
            _status.value = newState.status
        }
    }

    private suspend fun onAvailable(available: ServerState.Available) {
        val cancelledService = CompletableDeferred<ServiceAction.Add?>()
        val jobs = listOf(
            monitorAdvertising(available),
            monitorAddServices(available, cancelledService),
            monitorNotifyingActions(available),
        )
        try {
            // Keep active so cleanup occurs correctly
            jobs.joinAll()
        } finally {
            logger.info(TAG) { "Closing Server" }
            jobs.forEach { it.cancel() }
            disconnectAllConnectedDevices()
            removeServicesAndSaveForRestoration(cancelledService.getCompletedOrNull())
            stopAdvertisementForRestoration()
        }
    }

    private fun removeServicesAndSaveForRestoration(currentlyBeingAdded: ServiceAction.Add?) {
        val activeActions = mutableListOf<ServiceAction>()
        currentlyBeingAdded?.let {
            activeActions.add(it)
        }
        while (!serviceActionChannel.isEmpty) {
            serviceActionChannel.tryReceive().getOrNull()?.let {
                activeActions.add(it)
            }
        }
        _services.value.forEach { service ->
            serviceActionChannel.trySend(ServiceAction.Add({ service }, CompletableDeferred()))
        }
        activeActions.forEach { serviceActionChannel.trySend(it) }
        _services.value = emptyList()
    }

    private fun disconnectAllConnectedDevices() {
        _services.value.forEach(::disconnectAllConnectedDevices)
    }

    private fun disconnectAllConnectedDevices(service: LocalService) {
        service.characteristics.forEach { characteristic ->
            when (characteristic) {
                is LocalCharacteristic.Notifiable -> characteristic.unsubscribeAll()
                is LocalCharacteristic.Static -> {}
            }
        }
    }

    private fun stopAdvertisementForRestoration() {
        currentAdvertiseSettings?.takeIf { advertiseChannel.isEmpty }?.let { advertisementSettings ->
            advertiseChannel.trySend {
                if (advertisementSettings.hasStarted.isCompleted) {
                    advertisementSettings.copy(hasStarted = CompletableDeferred(), onStop = {})
                } else {
                    advertisementSettings.copy(onStop = {})
                }
            }
        }
        stopAdvertising(false)
    }

    private fun monitorAdvertising(available: ServerState.Available) = launch {
        for (advertiseSettingsBuilder in advertiseChannel) {
            stopAdvertising(log = false)

            logger.info(TAG) { "Stop Previous" }
            val advertiseSettings = advertiseSettingsBuilder(available)
            currentAdvertiseSettings = advertiseSettings
            if (available.startAdvertising(advertiseSettings.data)) {
                logger.info(TAG) { "Start Advertising" }
                _isAdvertising.value = true
                advertiseSettings.onStartSuccess()
            } else {
                logger.warn(TAG) { "Advertising could not be started" }
                currentAdvertiseSettings = null
                advertiseSettings.stop()
            }
        }
    }

    private fun monitorAddServices(available: ServerState.Available, cancelledService: CompletableDeferred<ServiceAction.Add?>) = launch {
        for (serviceAction in serviceActionChannel) {
            when (serviceAction) {
                is ServiceAction.Add -> {
                    val service = serviceAction.service(available)
                    logger.info(TAG) { "Adding service ${service.uuid}" }
                    val didAdd = try {
                        available.addService(service)
                    } catch (e: CancellationException) {
                        cancelledService.complete(ServiceAction.Add({ service }, serviceAction.isAdded))
                        throw e
                    }

                    withContext(NonCancellable) {
                        if (didAdd) {
                            logger.warn(TAG) { "Added service ${service.uuid}" }
                            _services.update { it + service }
                            serviceAction.isAdded.complete(service)
                        } else {
                            logger.warn(TAG) { "Failed to add service ${service.uuid}" }
                            serviceAction.isAdded.complete(null)
                        }
                    }
                }

                is ServiceAction.Remove -> {
                    withContext(NonCancellable) {
                        available.removeService(serviceAction.service)
                        _services.update { it - serviceAction.service }
                        disconnectAllConnectedDevices(serviceAction.service)
                        serviceAction.isRemoved.complete()
                    }
                }

                is ServiceAction.RemoveAll -> {
                    withContext(NonCancellable) {
                        available.removeAllServices()
                        _services.value = emptyList()
                        disconnectAllConnectedDevices()
                        serviceAction.isRemoved.complete()
                    }
                }
            }
        }
    }

    private fun monitorNotifyingActions(available: ServerState.Available) = launch {
        for (notifyingAction in notificationChannel) {
            try {
                // Notification actions may have been cancelled, or the service may have been removed
                if (!notifyingAction.completed.isCompleted && _services.value.any { service -> service.characteristics.contains(notifyingAction.characteristic) }) {
                    logger.info(TAG) {
                        "Notify ${notifyingAction.device.identifier} that Characteristic ${notifyingAction.characteristic.uuid} updated to ${
                            notifyingAction.value.toHexString(
                                ":",
                            )
                        }"
                    }
                    try {
                        if (available.execute(notifyingAction.characteristic, notifyingAction.device, notifyingAction.value)) {
                            logger.info(TAG) { "Notification sent" }
                            notifyingAction.notifySuccess()
                        } else {
                            logger.warn(TAG) { "Failed to notify" }
                            notifyingAction.notifyFailed()
                        }
                    } catch (e: CancellationException) {
                        notifyingAction.notifyFailed()
                        throw e
                    }
                }
            } catch (e: CancellationException) {
                notifyingAction.completed.complete(false)
                throw e
            }
        }
    }

    private suspend fun awaitClose() = _status.first { it == ServerStatus.CLOSED }

    private suspend fun <T> performOrFailOnClose(resultOnClose: T, block: suspend () -> T): T {
        val action = async { block() }
        val didClose = async { awaitClose() }
        return try {
            select {
                action.onAwait { it }
                didClose.onAwait { resultOnClose }
            }
        } finally {
            action.cancel()
            didClose.cancel()
        }
    }
}

data class AdvertiseData(val localName: String?, val serviceUUIDs: List<UUID>) {
    interface Builder {
        var localName: String?
        fun serviceUUIDs(vararg uuid: UUID)

        fun serviceUUIDs(vararg uuidString: String) {
            serviceUUIDs(
                *uuidString.map { uuidFrom(it) }.toTypedArray(),
            )
        }
    }
}

expect class ConnectedDevice : Device {
    override val identifier: Identifier
}
