/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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
import com.splendo.kaluga.base.bytes.toHexString
import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.MTU
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.uuidFrom
import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.RestrictedLogLevel
import com.splendo.kaluga.logging.RestrictedLogger
import com.splendo.kaluga.logging.info
import com.splendo.kaluga.logging.warn
import com.splendo.kaluga.permissions.base.Permissions
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
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
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.cancellation.CancellationException

/**
 * Sets up a [BluetoothServer]
 */
interface BluetoothServerDSL {

    /**
     * Starts advertising with the given [data]
     * @param data the [AdvertiseData.Builder] to use to set up the advertising
     */
    fun advertise(data: AdvertiseData.Builder.() -> Unit)

    /**
     * Adds a [LocalService] using [LocalService.DSL]
     * @param uuid the [UUID] of the [LocalService] to add
     * @param service the [LocalService.DSL] to use to set up the [LocalService]
     */
    fun service(uuid: UUID, service: LocalService.DSL.() -> Unit)

    /**
     * Adds a [LocalService] using [LocalService.DSL]
     * @param uuidString the string of the [UUID] of the [LocalService] to add
     * @param service the [LocalService.DSL] to use to set up the [LocalService]
     * @throws com.splendo.kaluga.bluetooth.UUIDException if [uuidString] is not a valid [UUID]
     */
    fun service(uuidString: String, service: LocalService.DSL.() -> Unit) {
        service(uuidFrom(uuidString), service)
    }
}

/**
 * The Status of a [BluetoothServer]
 */
enum class ServerStatus {
    /**
     * The hardware does not support acting as a Bluetooth Server
     */
    NOT_SUPPORTED,

    /**
     * The user must provide permissions to [com.splendo.kaluga.permissions.bluetooth.BluetoothPermission.Type.Server] first
     */
    AWAITING_PERMISSIONS,

    /**
     * Bluetooth is disabled by the hardware,
     */
    AWAITING_BLUETOOTH_ENABLED,

    /**
     * The server is available to connect to
     */
    AVAILABLE,

    /**
     * The server was closed using [BluetoothServer.close] and is no longer available.
     */
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

        fun serviceBuilder(uuid: UUID, notify: Notify): LocalServiceDSL
    }

    object Closed : Unavailable {
        override val status: ServerStatus get() = ServerStatus.CLOSED
    }

    val status: ServerStatus
}

/**
 * Settings for configuring a [BluetoothServer]
 * @property permissions the [Permissions] to manage the bluetooth related permissions
 * @property autoRequestPermission if `true` the server should automatically request permissions if not granted
 * @property autoEnableBluetooth if `true` the server should automatically enable the Bluetooth service if disabled
 * @property logger the [Logger] to log to
 */
data class ServerSettings(
    val permissions: Permissions,
    val autoRequestPermission: Boolean = true,
    val autoEnableBluetooth: Boolean = true,
    val logger: Logger = RestrictedLogger(RestrictedLogLevel.None),
)

/**
 * A Bluetooth Server that makes the Hardware advertise [AdvertiseData] and exposes [LocalService] to connect to.
 */
interface BluetoothServer : AutoCloseable {

    /**
     * A [StateFlow] of the [ServerStatus] of this Server
     */
    val status: StateFlow<ServerStatus>

    /**
     * A [StateFlow] indicating whether this server is currently advertising
     */
    val isAdvertising: StateFlow<Boolean>

    /**
     * A [StateFlow] of the [LocalService]s that are currently discoverable
     */
    val services: StateFlow<List<LocalService>>

    /**
     * Starts advertising with the given [data]
     * @param data the [AdvertiseData.Builder] to use to set up the advertising
     * @return `true` if advertising was started, `false` if not
     */
    suspend fun advertise(data: AdvertiseData.Builder.() -> Unit): Boolean

    /**
     * Stops advertising any [AdvertiseData]
     */
    fun stopAdvertising()

    /**
     * Attempts to add a [LocalService] at a given [UUID] and suspends until it has been added.
     * @param uuid the [UUID] of the [LocalService] to add
     * @param service the [LocalService.DSL] to use to set up the [LocalService]
     * @return the [LocalService] added or `null` if it could not be added
     */
    suspend fun add(uuid: UUID, service: LocalService.DSL.() -> Unit): LocalService?

    /**
     * Attempts to remove a [LocalService] and suspends until it has been removed.
     * @param service the [LocalService] to remove
     * @return `true` if the [LocalService] was removed, `false` if not
     */
    suspend fun remove(service: LocalService): Boolean

    /**
     * Attempts to remove all [LocalService] and suspends until they have been removed.
     * @return `true` if all [LocalService] were removed, `false` if not
     */
    suspend fun removeAllServices(): Boolean
}

/**
 * The default implementation of [BluetoothServer]. It makes the Hardware advertise [AdvertiseData] and exposes [LocalService] to connect to.
 */
class DefaultBluetoothServer internal constructor(private val settings: ServerSettings, initialState: ServerState.Initial, coroutineContext: CoroutineContext) :
    BluetoothServer,
    CoroutineScope by CoroutineScope(coroutineContext + CoroutineName("BluetoothServer")) {

    companion object {
        const val TAG = "BluetoothServer"
    }

    internal class DSL(private val settings: ServerSettings, private val initialState: ServerState.Initial, private val coroutineContext: CoroutineContext) : BluetoothServerDSL {

        private var advertisementBuilder: (AdvertiseData.Builder.() -> Unit)? = null
        private val serviceBuilders = mutableMapOf<UUID, LocalService.DSL.() -> Unit>()

        override fun advertise(data: AdvertiseData.Builder.() -> Unit) {
            require(advertisementBuilder == null) { "Can only set advertisement data once" }
            advertisementBuilder = data
        }

        override fun service(uuid: UUID, service: LocalService.DSL.() -> Unit) {
            require(!serviceBuilders.containsKey(uuid)) { "Service $uuid already added" }
            serviceBuilders[uuid] = service
        }

        suspend fun build(): BluetoothServer = DefaultBluetoothServer(settings, initialState, coroutineContext).apply {
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

        class Remove(val service: LocalService, val isRemoved: EmptyCompletableDeferred) : ServiceAction()
        class RemoveAll(val isRemoved: EmptyCompletableDeferred) : ServiceAction()
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
    override val status: StateFlow<ServerStatus> = _status.asStateFlow()
    private val _isAdvertising = MutableStateFlow(false)
    override val isAdvertising: StateFlow<Boolean> = _isAdvertising.asStateFlow()

    private val _services = MutableStateFlow<List<LocalService>>(emptyList())
    override val services: StateFlow<List<LocalService>> = _services.asStateFlow()

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

    override suspend fun advertise(data: AdvertiseData.Builder.() -> Unit): Boolean = performOrFailOnClose(false) {
        val hasStarted = CompletableDeferred<Boolean>()
        val advertisingSettingsBuilder = AdvertisingSettings.Builder().apply(data)
        try {
            advertiseChannel.send { active -> advertisingSettingsBuilder.build(hasStarted) { active?.stopAdvertising() } }
            hasStarted.await()
        } catch (_: ClosedSendChannelException) {
            false
        }
    }

    override fun stopAdvertising() {
        stopAdvertising(true)
    }

    private fun stopAdvertising(log: Boolean) {
        currentAdvertiseSettings?.let {
            if (log) {
                logger.info(TAG) { "Stop Advertising ${it.data}" }
            }
            // If not started, inform starting failed
            it.stop()
        }
        currentAdvertiseSettings = null
        _isAdvertising.value = false
    }

    override suspend fun add(uuid: UUID, service: LocalService.DSL.() -> Unit): LocalService? = performOrFailOnClose(null) {
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

    override suspend fun remove(service: LocalService) = performOrFailOnClose(false) {
        val completed = EmptyCompletableDeferred()
        try {
            serviceActionChannel.send(ServiceAction.Remove(service, completed))
            completed.await()
            true
        } catch (_: ClosedSendChannelException) {
            false
        }
    }

    override suspend fun removeAllServices() = performOrFailOnClose(false) {
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

    /**
     * Closes this [BluetoothServer]
     * After this method has been called the Server will no longer be available. Any subsequent calls to [advertise], [add], [remove], or [removeAllServices] will fail.
     */
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
                // Keep tracking state until this job gets cancelled by close
                val newState = when (val currentState = state) {
                    is ServerState.AwaitingPermissions -> {
                        // Next state change occurs whenever we get permissions
                        logger.info(TAG) { "Missing Permissions" }
                        currentState.awaitPermitted(settings.autoRequestPermission).also {
                            logger.info(TAG) { "Has Permissions" }
                        }
                    }

                    is ServerState.AwaitingBluetoothEnabled -> {
                        logger.info(TAG) { "Bluetooth Disabled" }
                        // Next state change occurs wen either bluetooth gets enabled or permissions get revoked
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
                        // Run availability lifecycle
                        val availableJob = launch {
                            onAvailable(currentState)
                        }
                        // Next state change occurs wen either bluetooth gets disabled or permissions get revoked
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
            val newState = state.close()
            _status.value = newState.status
        }
    }

    private suspend fun onAvailable(available: ServerState.Available) {
        val cancelledService = CompletableDeferred<ServiceAction.Add?>()
        // Start monitoring all actions
        val jobs = listOf(
            monitorAdvertising(available),
            monitorAddServices(available, cancelledService),
            monitorNotifyingActions(available),
        )
        try {
            // Keep active so cleanup occurs correctly
            jobs.joinAll()
        } finally {
            // Prepare for restoration
            jobs.forEach { it.cancel() }
            disconnectAllConnectedDevices()
            removeServicesAndSaveForRestoration(cancelledService.getCompletedOrNull())

            stopAdvertisementForRestoration()
        }
    }

    private fun removeServicesAndSaveForRestoration(currentlyBeingAdded: ServiceAction.Add?) {
        // First grab all open ServiceActions so they may be executed as soon as Server becomes available again
        val activeActions = mutableListOf<ServiceAction>()
        currentlyBeingAdded?.let {
            activeActions.add(it)
        }
        while (!serviceActionChannel.isEmpty && !serviceActionChannel.isClosedForReceive) {
            serviceActionChannel.tryReceive().getOrNull()?.let {
                activeActions.add(it)
            }
        }
        // Add actions for adding current services back. These should be at the front of the queue
        _services.value.forEach { service ->
            serviceActionChannel.trySend(ServiceAction.Add({ service }, CompletableDeferred()))
        }
        // Add pending actions back
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

                    if (didAdd) {
                        logger.warn(TAG) { "Added service ${service.uuid}" }
                        _services.update { it + service }
                        serviceAction.isAdded.complete(service)
                    } else {
                        logger.warn(TAG) { "Failed to add service ${service.uuid}" }
                        serviceAction.isAdded.complete(null)
                    }
                }

                is ServiceAction.Remove -> {
                    available.removeService(serviceAction.service)
                    _services.update { it - serviceAction.service }
                    disconnectAllConnectedDevices(serviceAction.service)
                    serviceAction.isRemoved.complete()
                }

                is ServiceAction.RemoveAll -> {
                    available.removeAllServices()
                    _services.value = emptyList()
                    disconnectAllConnectedDevices()
                    serviceAction.isRemoved.complete()
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

/**
 * Data advertised by a [BluetoothServer]
 * @property localName the name of the device to advertise, or `null` to not advertise a name.
 *
 * Platform caveats for [localName]:
 * - **Android** has no API to advertise a custom name, so providing one temporarily renames the device's
 *   **global** Bluetooth adapter (visible to other apps and the system UI) for the duration of advertising.
 *   The original name is restored when advertising stops or the server is closed, but if the process is killed
 *   while advertising the renamed adapter may persist. The rename is asynchronous, so the first advertisement
 *   may still carry the previous name. Leave this `null` to advertise without touching the adapter name.
 * - **iOS** drops the local name from the advertisement entirely while the app is in the background, and moves
 *   service UUIDs into an overflow area only other iOS devices can discover.
 * @property serviceUUIDs the [UUID]s of the [LocalService] to advertise
 */
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

/**
 * A [Device] that connected to a [BluetoothServer]
 */
expect interface ConnectedDevice : Device {
    /**
     * The [MTU] negotiated with this device, or `null` if it is not known. Notification payloads should be sized to at most `mtu - 3` bytes.
     */
    val mtu: MTU?
}
