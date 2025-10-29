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
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothStatusCodes
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.content.Context
import android.os.Build
import android.os.ParcelUuid
import com.splendo.kaluga.base.utils.toHexString
import com.splendo.kaluga.bluetooth.CharacteristicProperty
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
import kotlin.coroutines.CoroutineContext

@SuppressLint("MissingPermission")
actual class BluetoothServer internal constructor(
    private val manager: BluetoothManager,
    context: Context,
    private val logger: Logger,
    coroutineContext: CoroutineContext
) : CoroutineScope by CoroutineScope(coroutineContext + CoroutineName("BluetoothServer")), AutoCloseable {

    companion object {
        const val TAG = "BluetoothServer"
    }

    internal class DSL(
        private val manager: BluetoothManager,
        private val context: Context,
        private val logger: Logger,
        private val coroutineContext: CoroutineContext
    ) : BluetoothServerDSL {

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

        suspend fun build(): BluetoothServer = BluetoothServer(manager, context, logger, coroutineContext).apply { logger
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
        val device: ConnectedDevice,
        val value: ByteArray,
        val completed: CompletableDeferred<Boolean> = CompletableDeferred(),
    ) {
        fun execute(): Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            server.notifyCharacteristicChanged(
                device.device,
                characteristic.characteristic,
                characteristic.properties.contains(CharacteristicProperty.Indicate),
                value
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
        val data: AdvertiseData
    ) : AdvertiseCallback() {

        class Builder : AdvertisementDataBuilder {

            override var localName: String? = null
            private val serviceUUIDs = mutableSetOf<UUID>()

            override fun serviceUUIDs(vararg uuid: UUID) {
                serviceUUIDs.addAll(uuid)
            }

            fun build(): AdvertisingSettings = AdvertisingSettings(
                localName,
                AdvertiseData.Builder().
                setIncludeDeviceName(localName != null)
                    .apply {
                        serviceUUIDs.forEach {
                            addServiceUuid(ParcelUuid(it))
                        }
                    }
                    .build()
            )
        }

        val hasStarted = CompletableDeferred<Boolean>()


        override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
            hasStarted.complete(true)
        }

        override fun onStartFailure(errorCode: Int) {
            hasStarted.complete(false)
        }
    }

    internal val callback = AndroidBluetoothServerCallback(
        logger,
        coroutineContext,
        onNotificationSent = { device, success ->
            currentNotifyingAction?.takeIf { it.device.device.address == device.address }?.completed?.complete(success)
        },
        onServiceAdded = { service, success ->
            servicesBeingAdded[service]?.complete(success)
        },
        sendResponse = ::sendResponse
    )

    private val server = manager.openGattServer(context, callback)
    private val defaultLocalName = manager.adapter.name

    private val _isAdvertising = MutableStateFlow(false)
    actual val isAdvertising: StateFlow<Boolean> = _isAdvertising.asStateFlow()

    private val _services = mutableListOf<LocalService>()
    actual val services: List<LocalService> get() = _services.toList()

    private val advertiseChannel = Channel<AdvertisingSettings>(capacity = Channel.UNLIMITED)
    private var currentAdvertiseCallback: AdvertisingSettings? = null

    private val addServiceChannel = Channel<Pair<() -> LocalService, CompletableDeferred<LocalService?>>>(capacity = Channel.UNLIMITED)
    private val servicesBeingAdded = mutableMapOf<BluetoothGattService, CompletableDeferred<Boolean>>()

    private val notificationChannel = Channel<NotifyingAction>(capacity = Channel.UNLIMITED)
    private var currentNotifyingAction: NotifyingAction? = null

    init {
        monitorAdvertising()
        monitorAddServices()
        monitorNotifyingActions()
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
            manager.adapter.bluetoothLeAdvertiser.stopAdvertising(it)
        }
        currentAdvertiseCallback = null
        manager.adapter.name = defaultLocalName
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
        _services.remove(service)
    }

    actual fun removeAllServices() {
        callback.removeAllServices()
        _services.clear()
    }

    private fun sendResponse(device: BluetoothDevice, requestId: Int, status: Int, offset: Int, data: ByteArray?): Boolean = server.sendResponse(device, requestId, status, offset, data)

    internal suspend fun notify(characteristic: LocalCharacteristic, device: ConnectedDevice, value: ByteArray): Boolean {
        val action = NotifyingAction(characteristic, device, value)
        notificationChannel.send(action)
        return action.completed.await()
    }

    override fun close() {
        advertiseChannel.close()
        addServiceChannel.close()
        notificationChannel.close()
        callback.removeAllServices()
        server.close()
        stopAdvertising()
    }

    private fun monitorAdvertising() {
        launch {
            val adapter = manager.adapter
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
    }

    private fun monitorAddServices() {
        launch {
            for ((serviceAddingAction, result) in addServiceChannel) {
                val service = serviceAddingAction()
                val addingCompleted = CompletableDeferred<Boolean>()
                servicesBeingAdded[service.service] = addingCompleted
                try {
                    logger.info(TAG) { "Adding service ${service.uuid}" }
                    if (server.addService(service.service)) {
                        if (addingCompleted.await()) {
                            logger.warn(TAG) { "Added service ${service.uuid}" }
                            _services.add(service)
                            result.complete(service)
                        } else {
                            logger.warn(TAG) { "Failed to add service ${service.uuid}" }
                            result.complete(null)
                        }
                    } else {
                        logger.warn(TAG) { "Failed to add service ${service.uuid}" }
                        result.complete(null)
                    }
                } finally {
                    servicesBeingAdded.remove(service.service)
                }
            }
        }
    }

    private fun monitorNotifyingActions() {
        launch {
            for (notifyingAction in notificationChannel) {
                currentNotifyingAction = notifyingAction
                try {
                    logger.info(TAG) { "Notify ${notifyingAction.device.identifier} that Characteristic ${notifyingAction.characteristic.uuid} updated to ${notifyingAction.value.toHexString(":")}" }
                    if (notifyingAction.execute()) {
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
                } finally {
                    currentNotifyingAction = null
                }
            }
        }
    }
}