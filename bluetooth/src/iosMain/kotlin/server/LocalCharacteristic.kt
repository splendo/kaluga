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

import com.splendo.kaluga.base.utils.typedList
import com.splendo.kaluga.bluetooth.Characteristic
import com.splendo.kaluga.bluetooth.CharacteristicProperty
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.warn
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import platform.CoreBluetooth.CBAttributePermissionsReadEncryptionRequired
import platform.CoreBluetooth.CBAttributePermissionsReadable
import platform.CoreBluetooth.CBAttributePermissionsWriteEncryptionRequired
import platform.CoreBluetooth.CBAttributePermissionsWriteable
import platform.CoreBluetooth.CBCharacteristicPropertyAuthenticatedSignedWrites
import platform.CoreBluetooth.CBCharacteristicPropertyIndicate
import platform.CoreBluetooth.CBCharacteristicPropertyIndicateEncryptionRequired
import platform.CoreBluetooth.CBCharacteristicPropertyNotify
import platform.CoreBluetooth.CBCharacteristicPropertyNotifyEncryptionRequired
import platform.CoreBluetooth.CBCharacteristicPropertyRead
import platform.CoreBluetooth.CBCharacteristicPropertyWrite
import platform.CoreBluetooth.CBCharacteristicPropertyWriteWithoutResponse
import platform.CoreBluetooth.CBDescriptor
import platform.CoreBluetooth.CBMutableCharacteristic

actual sealed class LocalCharacteristic(val characteristic: CBMutableCharacteristic, actual override val service: LocalService) : Characteristic {

    actual class Static internal constructor(characteristic: CBMutableCharacteristic, service: LocalService) : LocalCharacteristic(characteristic, service) {

        actual override val descriptors: List<LocalDescriptor> = characteristic.descriptors.orEmpty().typedList<CBDescriptor>().map { LocalDescriptor(it, this) }
    }

    actual class Notifiable internal constructor(
        characteristic: CBMutableCharacteristic,
        service: LocalService,
        private val server: BluetoothServer,
        private val onSubscribe: Notifiable.(ConnectedDevice) -> Unit,
        private val onUnsubscribe: Notifiable.(ConnectedDevice) -> Unit,
    ) : LocalCharacteristic(characteristic, service),
        FlowCollector<ByteArray> {
        private val _subscribedDevices = MutableStateFlow(emptyList<ConnectedDevice>())
        actual val subscribedDevices = _subscribedDevices.asStateFlow()

        actual override val descriptors: List<LocalDescriptor> = characteristic.descriptors.orEmpty().typedList<CBDescriptor>().map { LocalDescriptor(it, this) }

        actual suspend fun notify(device: ConnectedDevice, value: ByteArray): Boolean = server.notify(
            this,
            value,
            listOf(device),
        )

        actual suspend fun notifyAll(value: ByteArray): Boolean = server.notify(
            this,
            value,
            null,
        )

        actual override suspend fun emit(value: ByteArray) {
            notifyAll(value)
        }

        internal fun subscribe(device: ConnectedDevice) {
            _subscribedDevices.update { it + device }
            onSubscribe(device)
        }
        internal fun unsubscribe(device: ConnectedDevice) {
            _subscribedDevices.update { it - device }
            onUnsubscribe(device)
        }
    }

    actual override val uuid: UUID = characteristic.UUID

    internal class DSL(val uuid: UUID, private val server: BluetoothServer, val logger: Logger) : LocalCharacteristicDSL {

        var properties = 0UL
        var permissions = 0UL

        private var readAction: (suspend LocalCharacteristic.(ConnectedDevice, Int) -> GattResponse.ReadResponse)? = null
        private var writeAction: (suspend LocalCharacteristic.(ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse)? = null

        private var subscriptionActions: Pair<Notifiable.(ConnectedDevice) -> Unit, Notifiable.(ConnectedDevice) -> Unit>? = null

        override fun readable(encrypted: Boolean, onRead: suspend LocalCharacteristic.(ConnectedDevice, Int) -> GattResponse.ReadResponse) {
            require(readAction == null) { "Read already set" }
            properties = properties or CBCharacteristicPropertyRead
            permissions = permissions or if (encrypted) CBAttributePermissionsReadEncryptionRequired else CBAttributePermissionsReadable
            readAction = onRead
        }

        override fun writable(
            properties: Set<CharacteristicProperty.Writable>,
            encrypted: Boolean,
            onWrite: suspend LocalCharacteristic.(ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse,
        ) {
            require(writeAction == null) { "Write already set" }
            this.properties = this.properties or properties.fold(0UL) { acc, property ->
                acc or when (property) {
                    CharacteristicProperty.Write -> CBCharacteristicPropertyWrite
                    CharacteristicProperty.WriteWithoutResponse -> CBCharacteristicPropertyWriteWithoutResponse
                    CharacteristicProperty.SignedWrite -> CBCharacteristicPropertyAuthenticatedSignedWrites
                }
            }
            permissions = permissions or if (encrypted) CBAttributePermissionsWriteEncryptionRequired else CBAttributePermissionsWriteable
            writeAction = onWrite
        }

        override fun notifiable(
            properties: Set<CharacteristicProperty.Notifiable>,
            encrypted: Boolean,
            onSubscribe: Notifiable.(ConnectedDevice) -> Unit,
            onUnsubscribe: Notifiable.(ConnectedDevice) -> Unit,
        ) {
            require(subscriptionActions == null) { "Notifying already set" }
            this.properties = this.properties or properties.fold(0UL) { acc, property ->
                acc or when (property) {
                    CharacteristicProperty.Notify -> if (encrypted) CBCharacteristicPropertyNotifyEncryptionRequired else CBCharacteristicPropertyNotify
                    CharacteristicProperty.Indicate -> if (encrypted) CBCharacteristicPropertyIndicateEncryptionRequired else CBCharacteristicPropertyIndicate
                }
            }
            subscriptionActions = onSubscribe to onUnsubscribe
        }

        override fun descriptor(uuid: UUID, descriptor: LocalDescriptorDSL.() -> Unit) {
            logger.warn("LocalCharacteristicDSL") { "iOS does not support custom descriptors. Ignoring: $uuid" }
        }

        fun build(forService: LocalService): LocalCharacteristic {
            val cbCharacteristic = CBMutableCharacteristic(uuid, properties, null, permissions)
            val characteristic = subscriptionActions?.let { (onSubscribe, onUnsubscribe) ->
                Notifiable(
                    cbCharacteristic,
                    forService,
                    server,
                    onSubscribe,
                    onUnsubscribe,
                ).apply {
                    server.delegate.registerSubscriptionActions(this, {
                        subscribe(it)
                    }, {
                        unsubscribe(it)
                    })
                }
            } ?: Static(cbCharacteristic, forService)

            readAction?.let { onRead ->
                server.delegate.registerReadAction(characteristic, onRead)
            }
            writeAction?.let { onRead ->
                server.delegate.registerWriteAction(characteristic, onRead)
            }

            return characteristic
        }
    }

    actual enum class Permission(val cbAttributePermission: ULong) {
        READABLE(CBAttributePermissionsReadable),
        WRITABLE(CBAttributePermissionsWriteable),
        READ_ENCRYPTION_REQUIRED(CBAttributePermissionsReadEncryptionRequired),
        WRITE_ENCRYPTION_REQUIRED(CBAttributePermissionsWriteEncryptionRequired),
    }

    actual override val properties: Set<CharacteristicProperty> = CharacteristicProperty.fromInt(characteristic.properties.toInt())
    actual val permissions: Set<Permission> = Permission.entries.filter {
        it.cbAttributePermission and characteristic.permissions != 0UL
    }.toSet()

    actual abstract override val descriptors: List<LocalDescriptor>
}
