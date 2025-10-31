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

import com.splendo.kaluga.base.flow.filterOnlyImportant
import com.splendo.kaluga.base.utils.toHexString
import com.splendo.kaluga.base.utils.toNSData
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.logging.info
import com.splendo.kaluga.logging.warn
import com.splendo.kaluga.permissions.base.PermissionState
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermission
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import platform.CoreBluetooth.CBAdvertisementDataLocalNameKey
import platform.CoreBluetooth.CBAdvertisementDataServiceUUIDsKey
import platform.CoreBluetooth.CBMutableService
import platform.CoreBluetooth.CBPeripheralManager
import platform.CoreBluetooth.CBPeripheralManagerOptionShowPowerAlertKey
import platform.CoreBluetooth.CBService
import platform.darwin.dispatch_queue_create
import kotlin.coroutines.CoroutineContext

actual class BluetoothServer internal constructor(private val settings: ServerSettings, coroutineContext: CoroutineContext) :
    CoroutineScope by CoroutineScope(coroutineContext + CoroutineName("BluetoothServer")),
    AutoCloseable {

    companion object {
        const val TAG = "BluetoothServer"
    }

    internal class DSL(private val settings: ServerSettings, private val coroutineContext: CoroutineContext) : BluetoothServerDSL {

        private var advertisementBuilder: (AdvertisementDataBuilder.() -> Unit)? = null
        private val serviceBuilders = mutableMapOf<UUID, LocalServiceDSL.Primary.() -> Unit>()

        override fun advertise(data: AdvertisementDataBuilder.() -> Unit) {
            require(advertisementBuilder == null) { "Can only set advertisement data once" }
            advertisementBuilder = data
        }

        override fun service(uuid: UUID, service: LocalServiceDSL.Primary.() -> Unit) {
            require(!serviceBuilders.containsKey(uuid)) { "Service $uuid already added" }
            serviceBuilders[uuid] = service
        }

        suspend fun build(): BluetoothServer = BluetoothServer(settings, coroutineContext).apply {
            try {
                advertisementBuilder?.let {
                    advertise(it)
                }
                for ((uuid, builder) in serviceBuilders) {
                    add(uuid, builder)
                }
            } catch (e: CancellationException) {
                close()
                throw e
            }
        }
    }

    private inner class AddingServiceAction(val service: LocalService, val hasCompleted: CompletableDeferred<LocalService?>) {

        fun complete(success: Boolean) {
            hasCompleted.complete(service.takeIf { success })
        }
    }

    private class NotifyingAction(
        val characteristic: LocalCharacteristic,
        val devices: List<ConnectedDevice>?,
        val value: ByteArray,
        val completed: CompletableDeferred<Boolean> = CompletableDeferred(),
    ) {
        fun execute(manager: CBPeripheralManager) {
            if (manager.updateValue(value.toNSData(), characteristic.characteristic, devices)) {
                completed.complete(true)
            }
        }
    }

    private class AdvertisementSettings(val settings: Map<Any?, *>, val hasStarted: CompletableDeferred<Boolean> = CompletableDeferred()) {

        class Builder : AdvertisementDataBuilder {
            override var localName: String? = null
            private val serviceUUIDs = mutableSetOf<UUID>()

            override fun serviceUUIDs(vararg uuid: UUID) {
                serviceUUIDs.addAll(uuid)
            }

            fun build() = AdvertisementSettings(
                buildMap {
                    localName?.let {
                        put(CBAdvertisementDataLocalNameKey, it)
                    }
                    if (serviceUUIDs.isNotEmpty()) {
                        put(CBAdvertisementDataServiceUUIDsKey, serviceUUIDs.toList())
                    }
                },
            )
        }
    }

    private val isAvailable = MutableStateFlow(false)

    internal val delegate = KalugaCBPeripheralManagerDelegate(
        logger = settings.logger,
        handlingContext = coroutineContext,
        onEnabledChanged = {
            isAvailable.value = it
        },
        onServiceAdded = { service, success ->
            servicesBeingAdded?.let { (toAdd, hasCompleted) ->
                if (service == toAdd) {
                    hasCompleted.complete(success)
                }
            }
        },
        didStartAdvertising = { success ->
            currentAdvertisementSettings?.hasStarted?.complete(success)
        },
        onAvailable = {
            currentNotifyingAction?.execute(it)
        },
    )

    private val serverQueue = dispatch_queue_create("BluetoothServer", null)
    private val manager = MutableStateFlow<CBPeripheralManager?>(null)
    private val logger = settings.logger

    private val _state = MutableStateFlow<ServerState>(ServerState.AWAITING_BLUETOOTH_ENABLED)
    actual val state: StateFlow<ServerState> = _state.asStateFlow()

    private val _isAdvertising = MutableStateFlow(false)
    actual val isAdvertising: StateFlow<Boolean> = _isAdvertising.asStateFlow()

    private val _services = MutableStateFlow(emptyList<LocalService>())
    actual val services: StateFlow<List<LocalService>> = _services.asStateFlow()

    private val advertiseChannel = Channel<AdvertisementSettings>(capacity = Channel.UNLIMITED)
    private var currentAdvertisementSettings: AdvertisementSettings? = null

    private val addServiceChannel = Channel<Pair<() -> LocalService, CompletableDeferred<LocalService?>>>(capacity = Channel.UNLIMITED)
    private var servicesBeingAdded: Pair<CBService, CompletableDeferred<Boolean>>? = null
    private var currentAddingServiceAction: AddingServiceAction? = null

    private val notificationChannel = Channel<NotifyingAction>(capacity = Channel.UNLIMITED)
    private var currentNotifyingAction: NotifyingAction? = null

    private val stateJob = launch { manageState() }

    actual suspend fun advertise(data: AdvertisementDataBuilder.() -> Unit): Boolean {
        val advertisingSettings = AdvertisementSettings.Builder().apply(data).build()
        advertiseChannel.send(advertisingSettings)
        return advertisingSettings.hasStarted.await()
    }
    actual fun stopAdvertising() {
        stopAdvertising(true)
    }

    private fun stopAdvertising(log: Boolean) {
        currentAdvertisementSettings?.let { advertisementSettings ->
            if (log) {
                logger.info(TAG) { "Stop Advertising" }
            }
            advertisementSettings.hasStarted.complete(false)
            manager.value?.stopAdvertising()
        }
        currentAdvertisementSettings = null
        _isAdvertising.value = false
    }

    actual suspend fun add(uuid: UUID, service: LocalServiceDSL.Primary.() -> Unit): LocalService? {
        val response = CompletableDeferred<LocalService?>()
        val serviceBuilder = { LocalService.DSL.Primary(uuid, this, logger).apply(service).build() }
        return try {
            addServiceChannel.send(serviceBuilder to response)
            response.await()
        } catch (e: ClosedSendChannelException) {
            null
        }
    }

    actual fun remove(service: LocalService) {
        delegate.removeService(service.service)
        _services.value = _services.value - service
        manager.value?.let { manager ->
            service.includedServices.forEach { manager.removeService(it.service) }
            manager.removeService(service.service)
        }
    }

    actual fun removeAllServices() {
        delegate.removeAllServices()
        _services.value = emptyList()
        manager.value?.removeAllServices()
    }

    suspend fun notify(characteristic: LocalCharacteristic, value: ByteArray, devices: List<ConnectedDevice>?): Boolean {
        val action = NotifyingAction(characteristic, devices, value)
        notificationChannel.send(action)
        return action.completed.await()
    }

    actual override fun close() {
        stateJob.cancel()
        advertiseChannel.close()
        addServiceChannel.close()
        notificationChannel.close()
        _state.value = ServerState.CLOSED
    }

    private fun manageState() = launch {
        val servicesToRestore = mutableListOf<Pair<LocalService, CompletableDeferred<LocalService?>>>()
        val advertisementToRestore = MutableStateFlow<AdvertisementSettings?>(null)
        settings.permissions[BluetoothPermission.Server].filterOnlyImportant().map { listOf(it) }.collectLatest { permissions ->
            if (permissions.all { it is PermissionState.Allowed }) {
                logger.info(TAG) { "Has Permissions" }
                val manager =
                    CBPeripheralManager(delegate, serverQueue, mapOf<Any?, Any>(CBPeripheralManagerOptionShowPowerAlertKey to true).takeIf { settings.autoEnableBluetooth })
                try {
                    this@BluetoothServer.manager.value = manager
                    isAvailable.collectLatest { bluetoothEnabled ->
                        if (bluetoothEnabled) {
                            logger.info(TAG) { "Bluetooth Enabled" }
                            onAvailable(manager, advertisementToRestore, servicesToRestore)
                        } else {
                            logger.info(TAG) { "Bluetooth Disabled" }
                            _state.value = ServerState.AWAITING_BLUETOOTH_ENABLED
                        }
                    }
                } finally {
                    this@BluetoothServer.manager.value = manager
                }
            } else {
                logger.info(TAG) { "Missing Permissions" }
                _state.value = ServerState.AWAITING_PERMISSIONS
                if (settings.autoRequestPermission) {
                    permissions.filterIsInstance<PermissionState.Denied.Requestable<BluetoothPermission.Server>>().forEach { state ->
                        logger.info(TAG) { "Request Permission" }
                        state.request()
                    }
                }
            }
        }
    }

    private suspend fun onAvailable(
        manager: CBPeripheralManager,
        advertisementToRestore: MutableStateFlow<AdvertisementSettings?>,
        servicesToRestore: MutableList<Pair<LocalService, CompletableDeferred<LocalService?>>>,
    ) {
        _state.value = ServerState.AVAILABLE
        val jobs = listOf(
            monitorAdvertising(manager),
            monitorAddServices(manager),
            monitorNotifyingActions(manager),
        )
        var isRestoringService = false
        try {
            // Restore Advertisement
            advertisementToRestore.value?.takeIf { advertiseChannel.isEmpty }?.let { advertisementSettings ->
                logger.info(TAG) { "Restoring Advertisement" }
                advertiseChannel.send(advertisementSettings)
                advertisementSettings.hasStarted.await()
            }
            advertisementToRestore.value = null
            // Restore removed Services
            while (servicesToRestore.isNotEmpty()) {
                val (toAdd, response) = servicesToRestore.first()
                logger.info(TAG) { "Restoring Service ${toAdd.uuid}" }
                addServiceChannel.send({ toAdd } to response)
                isRestoringService = true
                response.await()
                servicesToRestore.removeAt(0)
                isRestoringService = false
            }
            // Keep active so cleanup occurs correctly
            jobs.joinAll()
        } finally {
            logger.info(TAG) { "Closing Server" }
            jobs.forEach { it.cancel() }
            disconnectAllConnectedDevices()
            // Prevent duplicate restoration
            if (isRestoringService) {
                servicesToRestore.removeAt(0)
            }
            servicesToRestore.addAll(removeServicesAndSaveForRestoration())
            advertisementToRestore.value = stopAdvertisementForRestoration()
        }
    }

    private fun removeServicesAndSaveForRestoration(): List<Pair<LocalService, CompletableDeferred<LocalService?>>> = buildList {
        addAll(_services.value.map { it to CompletableDeferred() })
        currentAddingServiceAction?.let {
            add(it.service to it.hasCompleted)
        }
        _services.value = emptyList()
        currentAddingServiceAction = null
    }

    private fun disconnectAllConnectedDevices() {
        currentNotifyingAction?.completed?.complete(false)
        currentNotifyingAction = null
        _services.value.forEach { service ->
            service.characteristics.forEach { characteristic ->
                characteristic.subscribedDevices.value.forEach { device ->
                    characteristic.unsubscribe(device)
                }
            }
        }
    }

    private fun stopAdvertisementForRestoration() = currentAdvertisementSettings?.let { advertisementSettings ->
        if (advertisementSettings.hasStarted.isCompleted) {
            AdvertisementSettings(advertisementSettings.settings)
        } else {
            advertisementSettings
        }
    }.also {
        stopAdvertising(false)
    }

    private fun monitorAddServices(manager: CBPeripheralManager) = launch {
        for ((serviceAddingAction, result) in addServiceChannel) {
            val primaryService = serviceAddingAction()
            val addingServiceAction = AddingServiceAction(primaryService, result)
            currentAddingServiceAction = addingServiceAction
            logger.info(TAG) { "Adding service ${primaryService.uuid}" }

            val (didAddSubServices, servicesAdded) = primaryService.includedServices.fold(true to listOf<CBMutableService>()) { (success, servicesAdded), includedService ->
                if (success) {
                    logger.info(TAG) { "Adding included service ${includedService.uuid} to ${primaryService.uuid}" }
                    val didAdd = CompletableDeferred<Boolean>()
                    servicesBeingAdded = includedService.service to didAdd
                    manager.addService(includedService.service)
                    val success = didAdd.await()
                    servicesBeingAdded = null
                    if (success) {
                        logger.info(TAG) { "Did add included service ${includedService.uuid} to ${primaryService.uuid}" }
                    } else {
                        logger.warn(TAG) { "Failed to add service ${includedService.uuid} to ${primaryService.uuid}" }
                    }
                    success to servicesAdded + includedService.service
                } else {
                    false to servicesAdded
                }
            }

            if (didAddSubServices) {
                val didAdd = CompletableDeferred<Boolean>()
                servicesBeingAdded = primaryService.service to didAdd
                manager.addService(primaryService.service)
                if (didAdd.await()) {
                    logger.warn(TAG) { "Added service ${primaryService.uuid}" }
                    _services.update { it + primaryService }
                    result.complete(primaryService)
                } else {
                    logger.warn(TAG) { "Failed to add service ${primaryService.uuid}" }
                    servicesAdded.forEach { manager.removeService(it) }
                    result.complete(null)
                }
            } else {
                logger.warn(TAG) { "Failed to add included services. Not adding primary service ${primaryService.uuid}" }
                servicesAdded.forEach { manager.removeService(it) }
                result.complete(null)
            }

            currentAddingServiceAction = null
        }
    }

    private fun monitorAdvertising(manager: CBPeripheralManager) = launch {
        for (advertiseSettings in advertiseChannel) {
            stopAdvertising(log = false)
            currentAdvertisementSettings = advertiseSettings
            manager.startAdvertising(advertiseSettings.settings)
            if (advertiseSettings.hasStarted.await()) {
                logger.info(TAG) { "Start Advertising" }
                _isAdvertising.value = true
            } else {
                logger.warn(TAG) { "Advertising could not be started" }
                currentAdvertisementSettings = null
                _isAdvertising.value = false
            }
        }
    }

    private fun monitorNotifyingActions(manager: CBPeripheralManager) = launch {
        for (notifyingAction in notificationChannel) {
            currentNotifyingAction = notifyingAction
            val devices = notifyingAction.devices ?: notifyingAction.characteristic.subscribedDevices.value
            logger.info(TAG) {
                "Notify ${devices.joinToString(", ") { device ->
                    device.identifier.UUIDString
                } } that Characteristic ${notifyingAction.characteristic.uuid} updated to ${notifyingAction.value.toHexString(":")}"
            }
            notifyingAction.execute(manager)
            notifyingAction.completed.await().also { didNotify ->
                if (didNotify) {
                    logger.info(TAG) { "Notification sent" }
                } else {
                    logger.warn(TAG) { "Notification failed" }
                }
            }
            currentNotifyingAction = null
        }
    }
}
