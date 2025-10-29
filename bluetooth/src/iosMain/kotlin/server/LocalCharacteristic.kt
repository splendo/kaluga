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

import com.splendo.kaluga.base.utils.containsAny
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

actual class LocalCharacteristic internal constructor(
    val characteristic: CBMutableCharacteristic,
    actual override val service: LocalService,
    private val server: BluetoothServer,
) : Characteristic,
    FlowCollector<ByteArray> {

    override val uuid: UUID = characteristic.UUID

    internal class DSL(val uuid: UUID, private val server: BluetoothServer, val logger: Logger) : LocalCharacteristicDSL {

        var properties = 0UL
        var permissions = 0UL

        private var readAction: (suspend LocalCharacteristic.(ConnectedDevice, Int) -> GattResponse.ReadResponse)? = null
        private var writeAction: (suspend LocalCharacteristic.(ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse)? = null

        private var subscriptionActions: Pair<suspend LocalCharacteristic.(ConnectedDevice) -> Unit, suspend LocalCharacteristic.(ConnectedDevice) -> Unit>? = null

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
            onSubscribe: suspend LocalCharacteristic.(ConnectedDevice) -> Unit,
            onUnsubscribe: suspend LocalCharacteristic.(ConnectedDevice) -> Unit,
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

        fun build(forService: LocalService) = LocalCharacteristic(
            CBMutableCharacteristic(uuid, properties, null, permissions),
            forService,
            server,
        ).apply {
            readAction?.let { onRead ->
                server.delegate.registerReadAction(this, onRead)
            }
            writeAction?.let { onRead ->
                server.delegate.registerWriteAction(this, onRead)
            }
            subscriptionActions?.let { (onSubscribe, onUnsubscribe) ->
                server.delegate.registerSubscriptionActions(this, {
                    subscribe(it)
                    onSubscribe(it)
                }, {
                    unsubscribe(it)
                    onUnsubscribe(it)
                })
            }
        }
    }

    actual enum class Permission(val cbAttributePermission: ULong) {
        READABLE(CBAttributePermissionsReadable),
        WRITABLE(CBAttributePermissionsWriteable),
        READ_ENCRYPTION_REQUIRED(CBAttributePermissionsReadEncryptionRequired),
        WRITE_ENCRYPTION_REQUIRED(CBAttributePermissionsWriteEncryptionRequired),
    }

    override val properties: Set<CharacteristicProperty> = CharacteristicProperty.fromInt(characteristic.properties.toInt())
    actual val permissions: Set<Permission> = Permission.entries.filter {
        it.cbAttributePermission and characteristic.permissions != 0UL
    }.toSet()

    private val _subscribedDevices = MutableStateFlow(emptyList<ConnectedDevice>())
    actual val subscribedDevices = _subscribedDevices.asStateFlow()

    actual override val descriptors: List<LocalDescriptor> = characteristic.descriptors.orEmpty().typedList<CBDescriptor>().map { LocalDescriptor(it, this) }

    actual suspend fun notify(device: ConnectedDevice, value: ByteArray): Boolean = notifyIfAllowed(
        value,
        listOf(device),
    )

    actual suspend fun notifyAll(value: ByteArray): Boolean = notifyIfAllowed(
        value,
        null,
    )

    private suspend fun notifyIfAllowed(value: ByteArray, devices: List<ConnectedDevice>?): Boolean =
        if (properties.containsAny(setOf(CharacteristicProperty.Notify, CharacteristicProperty.Indicate))) {
            server.notify(this, value, devices)
        } else {
            false
        }

    override suspend fun emit(value: ByteArray) {
        notifyAll(value)
    }

    internal fun subscribe(device: ConnectedDevice) {
        _subscribedDevices.update { it + device }
    }
    internal fun unsubscribe(device: ConnectedDevice) {
        _subscribedDevices.update { it - device }
    }
}
