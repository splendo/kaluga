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

import com.splendo.kaluga.base.utils.toHexString
import com.splendo.kaluga.base.utils.toNSData
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.info
import com.splendo.kaluga.logging.warn
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import platform.CoreBluetooth.CBAdvertisementDataLocalNameKey
import platform.CoreBluetooth.CBAdvertisementDataServiceUUIDsKey
import platform.CoreBluetooth.CBPeripheralManager
import platform.darwin.dispatch_queue_create
import kotlin.coroutines.CoroutineContext

actual class BluetoothServer internal constructor(private val logger: Logger, coroutineContext: CoroutineContext) :
    CoroutineScope by CoroutineScope(coroutineContext + CoroutineName("BluetoothServer")),
    AutoCloseable {

    companion object {
        const val TAG = "BluetoothServer"
    }

    internal class DSL(private val logger: Logger, private val coroutineContext: CoroutineContext) : BluetoothServerDSL {

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

        suspend fun build(): BluetoothServer = BluetoothServer(logger, coroutineContext).apply {
            logger
            advertisementBuilder?.let {
                advertise(it)
            }
            for ((uuid, builder) in serviceBuilders) {
                add(uuid, builder)
            }
        }
    }

    private inner class NotifyingAction(
        val characteristic: LocalCharacteristic,
        val devices: List<ConnectedDevice>?,
        val value: ByteArray,
        val completed: CompletableDeferred<Boolean> = CompletableDeferred(),
    ) {
        fun execute() {
            if (manager.updateValue(value.toNSData(), characteristic.characteristic, devices)) {
                completed.complete(true)
            }
        }
    }

    private class AdvertisementSettings(val settings: Map<Any?, *>) {

        val hasStarted = CompletableDeferred<Boolean>()

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

    internal val delegate = KalugaCBPeripheralManagerDelegate(
        logger = logger,
        handlingContext = coroutineContext,
        onServiceAdded = { service, success ->
            servicesBeingAdded[service.UUID]?.complete(success)
        },
        didStartAdvertising = { success ->
            currentAdvertisementSettings?.hasStarted?.complete(success)
        },
        onAvailable = {
            currentNotifyingAction?.execute()
        },
    )

    private val serverQueue = dispatch_queue_create("BluetoothServer", null)
    private val manager = CBPeripheralManager(delegate, serverQueue)

    private val _isAdvertising = MutableStateFlow(false)
    actual val isAdvertising: StateFlow<Boolean> = _isAdvertising.asStateFlow()

    private val _services = mutableListOf<LocalService>()
    actual val services: List<LocalService> get() = _services.toList()

    private val advertiseChannel = Channel<AdvertisementSettings>(capacity = Channel.UNLIMITED)
    private var currentAdvertisementSettings: AdvertisementSettings? = null

    private val addServiceChannel = Channel<Pair<() -> LocalService, CompletableDeferred<LocalService?>>>(capacity = Channel.UNLIMITED)
    private val servicesBeingAdded = mutableMapOf<UUID, CompletableDeferred<Boolean>>()

    private val notificationChannel = Channel<NotifyingAction>(capacity = Channel.UNLIMITED)
    private var currentNotifyingAction: NotifyingAction? = null

    init {
        monitorAdvertising()
        monitorAddServices()
        monitorNotifyingActions()
    }

    actual suspend fun advertise(data: AdvertisementDataBuilder.() -> Unit): Boolean {
        val advertisingSettings = AdvertisementSettings.Builder().apply(data).build()
        advertiseChannel.send(advertisingSettings)
        return advertisingSettings.hasStarted.await()
    }
    actual fun stopAdvertising() {
        stopAdvertising(true)
    }

    private fun stopAdvertising(log: Boolean) {
        if (_isAdvertising.value) {
            if (log) {
                logger.info(TAG) { "Stop Advertising" }
            }
            manager.stopAdvertising()
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
        _services.remove(service)
        manager.removeService(service.service)
    }

    actual fun removeAllServices() {
        delegate.removeAllServices()
        _services.clear()
        manager.removeAllServices()
    }

    suspend fun notify(characteristic: LocalCharacteristic, value: ByteArray, devices: List<ConnectedDevice>?): Boolean {
        val action = NotifyingAction(characteristic, devices, value)
        notificationChannel.send(action)
        return action.completed.await()
    }

    override fun close() {
        advertiseChannel.close()
        addServiceChannel.close()
        notificationChannel.close()
        delegate.removeAllServices()
        manager.setDelegate(null)
        stopAdvertising()
    }

    private fun monitorAddServices() {
        launch {
            for ((serviceAddingAction, result) in addServiceChannel) {
                val service = serviceAddingAction()
                val addingCompleted = CompletableDeferred<Boolean>()
                servicesBeingAdded[service.uuid] = addingCompleted
                try {
                    logger.info(TAG) { "Adding service ${service.uuid}" }
                    manager.addService(service.service)
                    if (addingCompleted.await()) {
                        logger.warn(TAG) { "Added service ${service.uuid}" }
                        _services.add(service)
                        result.complete(service)
                    } else {
                        logger.warn(TAG) { "Failed to add service ${service.uuid}" }
                        result.complete(null)
                    }
                } finally {
                    servicesBeingAdded.remove(service.uuid)
                }
            }
        }
    }

    private fun monitorAdvertising() {
        launch {
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
    }

    private fun monitorNotifyingActions() {
        launch {
            for (notifyingAction in notificationChannel) {
                currentNotifyingAction = notifyingAction
                try {
                    val devices = notifyingAction.devices ?: notifyingAction.characteristic.subscribedDevices.value
                    logger.info(TAG) {
                        "Notify ${devices.joinToString(", ") { device ->
                            device.identifier.UUIDString
                        } } that Characteristic ${notifyingAction.characteristic.uuid} updated to ${notifyingAction.value.toHexString(":")}"
                    }
                    notifyingAction.execute()
                    notifyingAction.completed.await().also { didNotify ->
                        if (didNotify) {
                            logger.info(TAG) { "Notification sent" }
                        } else {
                            logger.warn(TAG) { "Notification failed" }
                        }
                    }
                } finally {
                    currentNotifyingAction = null
                }
            }
        }
    }
}
