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
import com.splendo.kaluga.base.utils.toNSData
import com.splendo.kaluga.bluetooth.KalugaBluetoothServerWrapper
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.info
import com.splendo.kaluga.logging.warn
import com.splendo.kaluga.permissions.base.PermissionState
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermission
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermissionStateRepo
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.transformLatest
import platform.CoreBluetooth.CBAdvertisementDataLocalNameKey
import platform.CoreBluetooth.CBAdvertisementDataServiceUUIDsKey
import platform.CoreBluetooth.CBMutableService
import platform.CoreBluetooth.CBPeripheralManager
import platform.darwin.dispatch_queue_create

internal sealed class IOSServerState {
    class AwaitingPermissions(private val permissionStateRepo: BluetoothPermissionStateRepo, private val delegate: KalugaCBPeripheralManagerDelegate, private val logger: Logger) :
        IOSServerState(),
        ServerState.AwaitingPermissions {
        override suspend fun awaitPermitted(autoRequest: Boolean): ServerState.HasPermissions =
            permissionStateRepo.filterOnlyImportant().map { listOf(it) }.transformLatest { permissions ->
                if (permissions.all { it is PermissionState.Allowed }) {
                    emit(
                        AwaitingBluetoothEnabled(permissionStateRepo, delegate, logger),
                    )
                } else {
                    if (autoRequest) {
                        permissions.filterIsInstance<PermissionState.Denied.Requestable<BluetoothPermission>>().forEach { state ->
                            logger.info(BluetoothServer.Companion.TAG) { "Request Permission" }
                            state.request()
                        }
                    }
                }
            }.first()

        override fun close(): ServerState.Closed = ServerState.Closed
    }

    class AwaitingBluetoothEnabled(
        private val permissionStateRepo: BluetoothPermissionStateRepo,
        private val delegate: KalugaCBPeripheralManagerDelegate,
        private val logger: Logger,
    ) : IOSServerState(),
        ServerState.AwaitingBluetoothEnabled {

        private val serverQueue = dispatch_queue_create("BluetoothServer", null)

        override suspend fun awaitEnabled(autoEnable: Boolean): ServerState.Available = coroutineScope {
            val isEnabled = async { delegate.isEnabled.first { it } }
            val wrapper = KalugaBluetoothServerWrapper.createByLinkingTo(delegate, serverQueue)
            try {
                isEnabled.await()
                Available(wrapper, permissionStateRepo, delegate, logger)
            } catch (e: CancellationException) {
                wrapper.unlink()
                throw e
            }
        }

        override suspend fun awaitRevoked(): ServerState.AwaitingPermissions {
            permissionStateRepo.filterOnlyImportant().first { state -> listOf(state).any { it !is PermissionState.Allowed } }
            return AwaitingPermissions(permissionStateRepo, delegate, logger)
        }

        override fun close(): ServerState.Closed = ServerState.Closed
    }

    class Available(
        private val bluetoothServerWrapper: KalugaBluetoothServerWrapper,
        private val permissionStateRepo: BluetoothPermissionStateRepo,
        private val delegate: KalugaCBPeripheralManagerDelegate,
        private val logger: Logger,
    ) : IOSServerState(),
        ServerState.Available {

        override suspend fun addService(service: LocalService): Boolean = coroutineScope {
            val servicesAdded = mutableListOf<CBMutableService>()
            try {
                // On iOS, the Included Services must be explicitly added
                val success = listOf(*service.includedServices.toTypedArray(), service).fold(true) { success, toAdd ->
                    if (!success) {
                        false
                    } else {
                        val response = async { delegate.serviceAdded.mapNotNull { (added, success) -> success.takeIf { added.UUID == toAdd.wrapper.service.UUID } }.first() }
                        bluetoothServerWrapper.add(toAdd.wrapper.service)
                        if (response.await()) {
                            servicesAdded.add(toAdd.wrapper.service)
                            true
                        } else {
                            false
                        }
                    }
                }
                if (!success) {
                    // When failing to add the parent service, clean up the included services as well
                    servicesAdded.forEach { bluetoothServerWrapper.remove(it) }
                }
                success
            } catch (e: CancellationException) {
                servicesAdded.forEach { bluetoothServerWrapper.remove(it) }
                throw e
            }
        }

        override fun removeService(service: LocalService) {
            delegate.removeService(service.wrapper.service)
            bluetoothServerWrapper.remove(service.wrapper.service)
        }

        override fun removeAllServices() {
            delegate.removeAllServices()
            bluetoothServerWrapper.removeAllServices()
        }

        override suspend fun startAdvertising(data: AdvertiseData): Boolean = coroutineScope {
            val success = delegate.resetAdvertising()
            bluetoothServerWrapper.startAdvertising(
                buildMap {
                    data.localName?.let {
                        put(CBAdvertisementDataLocalNameKey, it)
                    }
                    if (data.serviceUUIDs.isNotEmpty()) {
                        put(CBAdvertisementDataServiceUUIDsKey, data.serviceUUIDs.toList())
                    }
                },
            )
            success.await()
        }

        override fun stopAdvertising() {
            bluetoothServerWrapper.stopAdvertising()
        }

        override suspend fun execute(characteristic: LocalCharacteristic.Notifiable, device: ConnectedDevice, value: ByteArray): Boolean = coroutineScope {
            val isAvailable = delegate.resetAvailable()
            if (bluetoothServerWrapper.updateValue(value.toNSData(), characteristic.wrapper.characteristic, listOf(device.cbCentral))) {
                true
            } else {
                isAvailable.await()
                execute(characteristic, device, value)
            }
        }

        override suspend fun awaitDisabled(): ServerState.AwaitingBluetoothEnabled {
            delegate.isEnabled.first { !it }
            return AwaitingBluetoothEnabled(permissionStateRepo, delegate, logger)
        }

        override suspend fun awaitRevoked(): ServerState.AwaitingPermissions {
            permissionStateRepo.filterOnlyImportant().first { state -> listOf(state).any { it !is PermissionState.Allowed } }
            bluetoothServerWrapper.unlink()
            return AwaitingPermissions(permissionStateRepo, delegate, logger)
        }

        override fun serviceBuilder(uuid: UUID, notify: Notify): LocalServiceDSL.Primary = LocalServiceDSL.Primary(
            uuid,
            notify,
            delegate::registerReadAction,
            delegate::registerWriteAction,
            { encrypted -> delegate.registerSubscriptionActions(this) },
            { _ ->
                logger.warn("DescriptorDSL") { "iOS Does not support adding descriptors" }
                null
            },
        )

        override fun close(): ServerState.Closed {
            bluetoothServerWrapper.unlink()
            return ServerState.Closed
        }
    }
}
