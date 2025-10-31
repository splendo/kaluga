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

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattServer
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothStatusCodes
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.ParcelUuid
import android.provider.Settings.ACTION_BLUETOOTH_SETTINGS
import com.splendo.kaluga.base.flow.filterOnlyImportant
import com.splendo.kaluga.base.utils.getCompletedOrNull
import com.splendo.kaluga.base.utils.toHexString
import com.splendo.kaluga.bluetooth.CharacteristicProperty
import com.splendo.kaluga.bluetooth.DefaultBluetoothMonitor
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.logging.info
import com.splendo.kaluga.logging.warn
import com.splendo.kaluga.permissions.base.PermissionState
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermission
import com.splendo.kaluga.service.EnableServiceActivity
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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@SuppressLint("MissingPermission")
actual class BluetoothServer internal constructor(
    private val context: Context,
    private val settings: ServerSettings,
    coroutineContext: CoroutineContext
) :
    CoroutineScope by CoroutineScope(coroutineContext + CoroutineName("BluetoothServer")),
    AutoCloseable {

    companion object {
        const val TAG = "BluetoothServer"
    }

    internal class DSL(private val context: Context, private val settings: ServerSettings, private val coroutineContext: CoroutineContext) :
        BluetoothServerDSL {

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

        suspend fun build(): BluetoothServer = BluetoothServer( context, settings, coroutineContext).apply {
            try {
                advertisementBuilder?.let {
                    advertise(it)
                }
                for ((uuid, builder) in serviceBuilders) {
                    add(uuid, builder)
                }
            } catch (e : CancellationException) {
                close()
                throw e
            }
        }
    }

    private inner class AddingServiceAction(
        val service: LocalService,
        val hasCompleted: CompletableDeferred<LocalService?>,
    ) {

        fun complete(success: Boolean) {
            hasCompleted.complete(service.takeIf { success })
        }
    }
    private class NotifyingAction(
        val characteristic: LocalCharacteristic,
        val device: ConnectedDevice,
        val value: ByteArray,
        val completed: CompletableDeferred<Boolean> = CompletableDeferred(),
    ) {
        fun execute(server: BluetoothGattServer): Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            server.notifyCharacteristicChanged(
                device.device,
                characteristic.characteristic,
                characteristic.properties.contains(CharacteristicProperty.Indicate),
                value,
            ) == BluetoothStatusCodes.SUCCESS
        } else {
            characteristic.characteristic.setValue(value)
            server.notifyCharacteristicChanged(
                device.device,
                characteristic.characteristic,
                characteristic.properties.contains(CharacteristicProperty.Indicate),
            )
        }
    }

    private class AdvertisingSettings(
        val localName: String?,
        val data: AdvertiseData,
        val hasStarted: CompletableDeferred<Boolean> = CompletableDeferred(),
    ) : AdvertiseCallback() {

        class Builder : AdvertisementDataBuilder {

            override var localName: String? = null
            private val serviceUUIDs = mutableSetOf<UUID>()

            override fun serviceUUIDs(vararg uuid: UUID) {
                serviceUUIDs.addAll(uuid)
            }

            fun build(): AdvertisingSettings = AdvertisingSettings(
                localName,
                AdvertiseData.Builder()
                    .setIncludeDeviceName(localName != null)
                    .apply {
                        serviceUUIDs.forEach {
                            addServiceUuid(ParcelUuid(it))
                        }
                    }
                    .build(),
            )
        }

        override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
            hasStarted.complete(true)
        }

        override fun onStartFailure(errorCode: Int) {
            hasStarted.complete(false)
        }
    }

    internal val callback = KalugaBluetoothGattServerCallback(
        settings.logger,
        coroutineContext,
        onNotificationSent = { device, success ->
            currentNotifyingAction?.takeIf { it.device.device.address == device.address }?.completed?.complete(success)
        },
        onServiceAdded = { service, success ->
            currentAddingServiceAction?.let { action ->
                if (action.service.service.uuid == service.uuid) {
                    action.complete(success)
                }
            }
        },
        sendResponse = ::sendResponse,
    )

    private val manager = context.applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
    private val server = MutableStateFlow<BluetoothGattServer?>(null)
    private val defaultLocalName = CompletableDeferred<String>()
    private val logger = settings.logger

    private val _state = MutableStateFlow<ServerState>(if (manager != null) ServerState.AWAITING_BLUETOOTH_ENABLED else ServerState.AWAITING_PERMISSIONS)
    actual val state: StateFlow<ServerState> = _state.asStateFlow()
    private val _isAdvertising = MutableStateFlow(false)
    actual val isAdvertising: StateFlow<Boolean> = _isAdvertising.asStateFlow()

    private val _services = MutableStateFlow<List<LocalService>>(emptyList())
    actual val services: StateFlow<List<LocalService>> = _services.asStateFlow()

    private val advertiseChannel = Channel<AdvertisingSettings>(capacity = Channel.UNLIMITED)
    private var currentAdvertiseCallback: AdvertisingSettings? = null

    private val addServiceChannel = Channel<Pair<() -> LocalService, CompletableDeferred<LocalService?>>>(capacity = Channel.UNLIMITED)
    private var currentAddingServiceAction: AddingServiceAction? = null

    private val notificationChannel = Channel<NotifyingAction>(capacity = Channel.UNLIMITED)
    private var currentNotifyingAction: NotifyingAction? = null

    private val stateJob = manager?.let {
        manageState(it)
    }

    init {
        if (manager == null) {
            logger.warn(TAG) { "Bluetooth not supported" }
        }
    }

    actual suspend fun advertise(data: AdvertisementDataBuilder.() -> Unit): Boolean {
        val advertisingSettings = AdvertisingSettings.Builder().apply(data).build()
        advertiseChannel.send(advertisingSettings)
        return advertisingSettings.hasStarted.await()
    }
    actual fun stopAdvertising() {
        stopAdvertising(true)
    }

    private fun stopAdvertising(log: Boolean) {
        currentAdvertiseCallback?.let {
            if (log) {
                logger.info(TAG) { "Stop Advertising" }
            }
            // If not started, inform starting failed
            it.hasStarted.complete(false)
            manager?.adapter?.bluetoothLeAdvertiser?.stopAdvertising(it)
        }
        currentAdvertiseCallback = null
        if (_state.value == ServerState.AVAILABLE) {
            defaultLocalName.getCompletedOrNull()?.let {
                manager?.adapter?.name = it
            }
        }
        _isAdvertising.value = false
    }

    actual suspend fun add(uuid: UUID, service: LocalServiceDSL.Primary.() -> Unit): LocalService? {
        val response = CompletableDeferred<LocalService?>()
        val serviceBuilder = { LocalService.DSL.Primary(uuid, this).apply(service).build() }
        return try {
            addServiceChannel.send(serviceBuilder to response)
            response.await()
        } catch (e: ClosedSendChannelException) {
            null
        }
    }

    actual fun remove(service: LocalService) {
        callback.removeService(service.service)
        _services.value = _services.value - service
        server.value?.removeService(service.service)
    }

    actual fun removeAllServices() {
        callback.removeAllServices()
        _services.value = emptyList()
        server.value?.clearServices()
    }

    private suspend fun sendResponse(device: BluetoothDevice, requestId: Int, status: Int, offset: Int, data: ByteArray?): Boolean = server.transformLatest { server ->
        if (server != null) {
            emit(server.sendResponse(device, requestId, status, offset, data))
        } else {
            emit(false)
        }
    }.first()

    internal suspend fun notify(characteristic: LocalCharacteristic, device: ConnectedDevice, value: ByteArray): Boolean {
        val action = NotifyingAction(characteristic, device, value)
        notificationChannel.send(action)
        return action.completed.await()
    }

    actual override fun close() {
        stateJob?.cancel()
        advertiseChannel.close()
        addServiceChannel.close()
        notificationChannel.close()
        _state.value = ServerState.CLOSED
    }

    private fun manageState(manager: BluetoothManager) = launch {
        val servicesToRestore = mutableListOf<Pair<LocalService, CompletableDeferred<LocalService?>>>()
        val advertisementToRestore = MutableStateFlow<AdvertisingSettings?>(null)
        settings.permissions[BluetoothPermission.Server].filterOnlyImportant().map { listOf(it) }.collectLatest { permissions ->
            if (permissions.all { it is PermissionState.Allowed }) {
                logger.info(TAG) { "Has Permissions" }
                val initialLocalName = defaultLocalName.getCompletedOrNull() ?: manager.adapter.name
                defaultLocalName.complete(initialLocalName)
                val enabledManager = DefaultBluetoothMonitor(context.applicationContext, manager.adapter)
                try {
                    enabledManager.startMonitoring()
                    enabledManager.isEnabled.distinctUntilChanged().collectLatest { bluetoothEnabled ->
                        if (bluetoothEnabled) {
                            logger.info(TAG) { "Bluetooth Enabled" }
                            onAvailable(manager, initialLocalName, advertisementToRestore, servicesToRestore)
                        } else {
                            logger.info(TAG) { "Bluetooth Disabled" }
                            _state.value = ServerState.AWAITING_BLUETOOTH_ENABLED
                            if (settings.autoEnableBluetooth) {
                                enableHardware(manager)
                            }
                        }
                    }
                } finally {
                    enabledManager.stopMonitoring()
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
        manager: BluetoothManager,
        defaultLocalName: String,
        advertisementToRestore: MutableStateFlow<AdvertisingSettings?>,
        servicesToRestore: MutableList<Pair<LocalService, CompletableDeferred<LocalService?>>>,
    ) {
        _state.value = ServerState.AVAILABLE
        val server = manager.openGattServer(context, callback)
        this@BluetoothServer.server.value = server
        val jobs = listOf(
            monitorAdvertising(manager.adapter, defaultLocalName),
            monitorAddServices(server),
            monitorNotifyingActions(server),
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
                logger.info(TAG) { "Restoring Service ${toAdd.service.uuid}" }
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
            callback.reset()
            server.close()
            this@BluetoothServer.server.value = null
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

    private fun stopAdvertisementForRestoration() = currentAdvertiseCallback?.let { advertisementSettings ->
            if (advertisementSettings.hasStarted.isCompleted) {
                AdvertisingSettings(advertisementSettings.localName, advertisementSettings.data)
            } else {
                advertisementSettings
            }
        }.also {
        stopAdvertising(false)
    }

    suspend fun enableHardware(manager: BluetoothManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            EnableServiceActivity.showEnableServiceActivity(
                context.applicationContext,
                hashCode().toString(),
                Intent(ACTION_BLUETOOTH_SETTINGS),
            ).await()
        } else {
            @Suppress("DEPRECATION")
            manager.adapter.enable()
        }
    }

    private fun monitorAdvertising(adapter: BluetoothAdapter, defaultLocalName: String) = launch {
        val advertiser = adapter.bluetoothLeAdvertiser
        val settings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
            .setConnectable(true)
            .build()
        for (advertiseSettings in advertiseChannel) {
            stopAdvertising(log = false)
            currentAdvertiseCallback = advertiseSettings
            adapter.name = advertiseSettings.localName ?: defaultLocalName
            advertiser.startAdvertising(settings, advertiseSettings.data, advertiseSettings)
            if (advertiseSettings.hasStarted.await()) {
                logger.info(TAG) { "Start Advertising" }
                _isAdvertising.value = true
            } else {
                logger.warn(TAG) { "Advertising could not be started" }
                currentAdvertiseCallback = null
                _isAdvertising.value = false
                adapter.name = defaultLocalName
            }
        }
    }

    private fun monitorAddServices(server: BluetoothGattServer) = launch {
        for ((serviceAddingAction, result) in addServiceChannel) {
            val service = serviceAddingAction()
            val addingServiceAction = AddingServiceAction(service, result)
            currentAddingServiceAction = addingServiceAction
            logger.info(TAG) { "Adding service ${service.uuid}" }
            if (server.addService(service.service)) {
                if (result.await() != null) {
                    logger.warn(TAG) { "Added service ${service.uuid}" }
                    _services.value = _services.value + service
                } else {
                    logger.warn(TAG) { "Failed to add service ${service.uuid}" }
                }
            } else {
                logger.warn(TAG) { "Failed to add service ${service.uuid}" }
                result.complete(null)
            }
            currentAddingServiceAction = null
        }
    }

    private fun monitorNotifyingActions(server: BluetoothGattServer) = launch {
        for (notifyingAction in notificationChannel) {
            currentNotifyingAction = notifyingAction
            logger.info(TAG) {
                "Notify ${notifyingAction.device.identifier} that Characteristic ${notifyingAction.characteristic.uuid} updated to ${notifyingAction.value.toHexString(
                    ":",
                )}"
            }
            if (notifyingAction.execute(server)) {
                notifyingAction.completed.await().also { didNotify ->
                    if (didNotify) {
                        logger.info(TAG) { "Notification sent" }
                    } else {
                        logger.warn(TAG) { "Notification failed" }
                    }
                }
            } else {
                logger.warn(TAG) { "Failed to notify" }
                notifyingAction.completed.complete(false)
            }
            currentNotifyingAction = null
        }
    }
}
