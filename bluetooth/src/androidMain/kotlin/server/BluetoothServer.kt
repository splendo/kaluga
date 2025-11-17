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
import android.bluetooth.BluetoothGattDescriptor
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
import com.splendo.kaluga.base.collections.concurrentMutableMapOf
import com.splendo.kaluga.base.flow.filterOnlyImportant
import com.splendo.kaluga.base.utils.toHexString
import com.splendo.kaluga.bluetooth.BluetoothMonitor
import com.splendo.kaluga.bluetooth.CharacteristicProperty
import com.splendo.kaluga.bluetooth.DefaultBluetoothMonitor
import com.splendo.kaluga.bluetooth.Descriptor
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.server.BluetoothServer.Companion.TAG
import com.splendo.kaluga.bluetooth.uuidString
import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.info
import com.splendo.kaluga.permissions.base.PermissionState
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermission
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermissionStateRepo
import com.splendo.kaluga.service.EnableServiceActivity
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.transformLatest
import kotlin.experimental.or

@SuppressLint("MissingPermission")
internal sealed class AndroidServerState {
    class AwaitingPermissions(
        private val manager: BluetoothManager,
        private val permissionStateRepo: BluetoothPermissionStateRepo,
        private val callback: KalugaBluetoothGattServerCallback,
        private val context: Context,
        private val logger: Logger,
    ) : AndroidServerState(),
        ServerState.AwaitingPermissions {
        override suspend fun awaitPermitted(autoRequest: Boolean): ServerState.HasPermissions =
            permissionStateRepo.filterOnlyImportant().map { listOf(it) }.transformLatest { permissions ->
                if (permissions.all { it is PermissionState.Allowed }) {
                    val enabledManager = DefaultBluetoothMonitor(context.applicationContext, manager.adapter)
                    enabledManager.startMonitoring()
                    emit(
                        AwaitingBluetoothEnabled(manager, enabledManager, permissionStateRepo, callback, context, logger),
                    )
                } else {
                    if (autoRequest) {
                        permissions.filterIsInstance<PermissionState.Denied.Requestable<BluetoothPermission>>().forEach { state ->
                            logger.info(TAG) { "Request Permission" }
                            state.request()
                        }
                    }
                }
            }.first()

        override fun close(): ServerState.Closed = ServerState.Closed
    }

    class AwaitingBluetoothEnabled(
        private val manager: BluetoothManager,
        private val bluetoothMonitor: BluetoothMonitor,
        private val permissionStateRepo: BluetoothPermissionStateRepo,
        private val callback: KalugaBluetoothGattServerCallback,
        private val context: Context,
        private val logger: Logger,
    ) : AndroidServerState(),
        ServerState.AwaitingBluetoothEnabled {

        override suspend fun awaitEnabled(autoEnable: Boolean): ServerState.Available = bluetoothMonitor.isEnabled.transformLatest { enabled ->
            if (enabled) {
                emit(
                    Available(manager, bluetoothMonitor, permissionStateRepo, callback, context, logger),
                )
            } else {
                if (autoEnable) {
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
            }
        }.first()

        override suspend fun awaitRevoked(): ServerState.AwaitingPermissions {
            permissionStateRepo.filterOnlyImportant().first { state -> listOf(state).any { it !is PermissionState.Allowed } }
            bluetoothMonitor.stopMonitoring()
            return AwaitingPermissions(manager, permissionStateRepo, callback, context, logger)
        }

        override fun close(): ServerState.Closed {
            bluetoothMonitor.stopMonitoring()
            return ServerState.Closed
        }
    }

    class Available(
        private val manager: BluetoothManager,
        private val bluetoothMonitor: BluetoothMonitor,
        private val permissionStateRepo: BluetoothPermissionStateRepo,
        private val callback: KalugaBluetoothGattServerCallback,
        private val context: Context,
        private val logger: Logger,
    ) : AndroidServerState(),
        ServerState.Available {

        private class AdvertisementCallback : AdvertiseCallback() {

            private var hasStarted = CompletableDeferred<Boolean>()

            fun reset(): Deferred<Boolean> {
                hasStarted.complete(false) // Complete previous if not completed
                hasStarted = CompletableDeferred()
                return hasStarted
            }

            override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
                hasStarted.complete(true)
            }

            override fun onStartFailure(errorCode: Int) {
                info(TAG) { "Start Advertising Android Failure with $errorCode" }
                hasStarted.complete(false)
            }
        }

        private val defaultLocalName = manager.adapter.name
        private val server = manager.openGattServer(context, callback)
        private val advertiser = manager.adapter.bluetoothLeAdvertiser
        private val advertiserCallback = AdvertisementCallback()

        init {
            callback.registerSendResponse { device, requestId, status, offset, data ->
                server.sendResponse(device, requestId, status, offset, data)
            }
        }

        override suspend fun addService(service: LocalService): Boolean = coroutineScope {
            val response = async { callback.serviceAdded.mapNotNull { (serviceAdded, success) -> success.takeIf { serviceAdded == service.wrapper.service } }.first() }
            if (server.addService(service.wrapper.service)) {
                response.await()
            } else {
                false
            }
        }

        override fun removeService(service: LocalService) {
            callback.removeService(service.wrapper.service)
            server.removeService(service.wrapper.service)
        }

        override fun removeAllServices() {
            callback.removeAllServices()
            server.clearServices()
        }

        override suspend fun startAdvertising(data: com.splendo.kaluga.bluetooth.server.AdvertiseData): Boolean = coroutineScope {
            val settings = AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
                .setConnectable(true)
                .build()

            val advertiseData = AdvertiseData.Builder().setIncludeDeviceName(false)
                .apply {
                    data.serviceUUIDs.forEach {
                        addServiceUuid(ParcelUuid(it))
                    }
                }
                .build()
            val scanResponse = AdvertiseData.Builder().setIncludeDeviceName(data.localName != null).build()

            val didComplete = advertiserCallback.reset()
            data.localName?.let {
                manager.adapter.name = it
            }
            advertiser.startAdvertising(settings, advertiseData, scanResponse, advertiserCallback)
            didComplete.await().also { success ->

                if (!success) {
                    manager.adapter.name = defaultLocalName
                }
            }
        }

        override fun stopAdvertising() {
            advertiser.stopAdvertising(advertiserCallback)
            manager.adapter.name = defaultLocalName
        }

        override suspend fun execute(characteristic: LocalCharacteristic.Notifiable, device: ConnectedDevice, value: ByteArray): Boolean = coroutineScope {
            val didNotify = async { callback.notificationSent.mapNotNull { (deviceNotified, success) -> success.takeIf { deviceNotified == device.device } }.first() }
            logger.info(TAG) { "Notify characteristic ${characteristic.uuid.uuidString} updated to ${value.toHexString(" ")}" }
            val didStart = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                server.notifyCharacteristicChanged(
                    device.device,
                    characteristic.wrapper.characteristic,
                    characteristic.properties.contains(CharacteristicProperty.Indicate),
                    value,
                ) == BluetoothStatusCodes.SUCCESS
            } else {
                characteristic.wrapper.characteristic.setValue(value)
                server.notifyCharacteristicChanged(
                    device.device,
                    characteristic.wrapper.characteristic,
                    characteristic.properties.contains(CharacteristicProperty.Indicate),
                )
            }
            if (didStart) {
                didNotify.await()
            } else {
                false
            }
        }

        override suspend fun awaitDisabled(): ServerState.AwaitingBluetoothEnabled {
            bluetoothMonitor.isEnabled.first { !it }
            clean(false)
            return AwaitingBluetoothEnabled(manager, bluetoothMonitor, permissionStateRepo, callback, context, logger)
        }

        override suspend fun awaitRevoked(): ServerState.AwaitingPermissions {
            permissionStateRepo.filterOnlyImportant().first { state -> listOf(state).any { it !is PermissionState.Allowed } }
            clean(true)
            return AwaitingPermissions(manager, permissionStateRepo, callback, context, logger)
        }

        override fun serviceBuilder(uuid: UUID, notify: Notify): LocalServiceDSL.Primary = LocalServiceDSL.Primary(
            uuid,
            notify,
            callback::registerReadAction,
            callback::registerWriteAction,
            { encrypted ->
                LocalDescriptorDSL(
                    Descriptor.CLIENT_CHARACTERISTIC_CONFIGURATION_DESCRIPTOR,
                    callback::registerReadAction,
                    callback::registerWriteAction,
                ).apply {
                    val deviceStatus = concurrentMutableMapOf<ConnectedDevice, ByteArray>()
                    readable(encrypted) { device, offset ->
                        when {
                            offset != 0 -> GattResponse.InvalidOffset
                            else -> GattResponse.ReadSuccess(deviceStatus.synchronized { getOrPut(device) { BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE } })
                        }
                    }
                    writable(encrypted) { device, value, offset ->
                        when {
                            offset != 0 -> GattResponse.InvalidOffset
                            (value.contentEquals(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE) && properties.contains(CharacteristicProperty.Indicate)) ||
                                (value.contentEquals(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE) && properties.contains(CharacteristicProperty.Notify)) -> {
                                deviceStatus.synchronized {
                                    val current = getOrDefault(device, BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE)
                                    val lsb = current[0] or value[0]
                                    val msb = current[1] or value[1]
                                    put(device, byteArrayOf(lsb, msb))
                                }
                                subscribe(device)
                                GattResponse.WriteSuccess
                            }
                            value.contentEquals(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE) -> {
                                deviceStatus[device] = value
                                unsubscribe(device)
                                GattResponse.WriteSuccess
                            }
                            else -> GattResponse.InvalidHandle
                        }
                    }
                }.build(this)
            },
            { uuid ->
                LocalDescriptorDSL(uuid, callback::registerReadAction, callback::registerWriteAction)
            },
        )

        override fun close(): ServerState.Closed {
            clean(true)
            return ServerState.Closed
        }

        private fun clean(stopMonitoringBluetooth: Boolean) {
            callback.reset()
            server.close()
            if (stopMonitoringBluetooth) {
                bluetoothMonitor.stopMonitoring()
            }
            manager.adapter.name = defaultLocalName
        }
    }
}
