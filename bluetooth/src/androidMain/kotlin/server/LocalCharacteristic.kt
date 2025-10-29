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

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import com.splendo.kaluga.base.utils.containsAny
import com.splendo.kaluga.bluetooth.Characteristic
import com.splendo.kaluga.bluetooth.CharacteristicProperty
import com.splendo.kaluga.bluetooth.Descriptor
import com.splendo.kaluga.bluetooth.UUID
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

actual class LocalCharacteristic internal constructor(
    val characteristic: BluetoothGattCharacteristic,
    actual override val service: LocalService,
    internal val server: BluetoothServer,
    buildDescriptors: LocalCharacteristic.() -> List<LocalDescriptor>,
) : Characteristic,
    FlowCollector<ByteArray> {

    actual enum class Permission(val androidPermission: Int) {
        READABLE(BluetoothGattCharacteristic.PERMISSION_READ),
        WRITABLE(BluetoothGattCharacteristic.PERMISSION_WRITE),
        READ_ENCRYPTION_REQUIRED(BluetoothGattCharacteristic.PERMISSION_READ_ENCRYPTED),
        WRITE_ENCRYPTION_REQUIRED(BluetoothGattCharacteristic.PERMISSION_WRITE_ENCRYPTED),
    }

    internal class DSL(val uuid: UUID, private val server: BluetoothServer) : LocalCharacteristicDSL {

        var properties = 0
        var permissions = 0

        private var readAction: (suspend LocalCharacteristic.(ConnectedDevice, Int) -> GattResponse.ReadResponse)? = null
        private var writeAction: (suspend LocalCharacteristic.(ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse)? = null
        private val descriptorBuilders = mutableListOf<LocalDescriptor.DSL>()

        override fun readable(encrypted: Boolean, onRead: suspend LocalCharacteristic.(ConnectedDevice, Int) -> GattResponse.ReadResponse) {
            require(readAction == null) { "Read already set" }
            properties = properties or BluetoothGattCharacteristic.PROPERTY_READ
            permissions = permissions or if (encrypted) BluetoothGattCharacteristic.PERMISSION_READ_ENCRYPTED else BluetoothGattCharacteristic.PERMISSION_READ
            readAction = onRead
        }

        override fun writable(
            properties: Set<CharacteristicProperty.Writable>,
            encrypted: Boolean,
            onWrite: suspend LocalCharacteristic.(ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse,
        ) {
            require(writeAction == null) { "Write already set" }
            this.properties = this.properties or properties.fold(0) { acc, property ->
                acc or when (property) {
                    CharacteristicProperty.Write -> BluetoothGattCharacteristic.PROPERTY_WRITE
                    CharacteristicProperty.WriteWithoutResponse -> BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE
                    CharacteristicProperty.SignedWrite -> BluetoothGattCharacteristic.PROPERTY_SIGNED_WRITE
                }
            }
            permissions = permissions or if (encrypted) BluetoothGattCharacteristic.PERMISSION_WRITE_ENCRYPTED else BluetoothGattCharacteristic.PERMISSION_WRITE
            writeAction = onWrite
        }

        override fun notifiable(
            properties: Set<CharacteristicProperty.Notifiable>,
            encrypted: Boolean,
            onSubscribe: suspend LocalCharacteristic.(ConnectedDevice) -> Unit,
            onUnsubscribe: suspend LocalCharacteristic.(ConnectedDevice) -> Unit,
        ) {
            require(
                descriptorBuilders.none {
                    it.uuid == Descriptor.CLIENT_CHARACTERISTIC_CONFIGURATION_DESCRIPTOR
                },
            ) { "Client characteristic configuration descriptor already declared" }
            this.properties = this.properties or properties.fold(0) { acc, property ->
                acc or when (property) {
                    CharacteristicProperty.Notify -> BluetoothGattCharacteristic.PROPERTY_NOTIFY
                    CharacteristicProperty.Indicate -> BluetoothGattCharacteristic.PROPERTY_INDICATE
                }
            }
            descriptor(Descriptor.CLIENT_CHARACTERISTIC_CONFIGURATION_DESCRIPTOR) {
                writable(encrypted) { device, value, offset ->
                    when {
                        offset != 0 -> GattResponse.InvalidOffset
                        value.contentEquals(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE) ||
                            value.contentEquals(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE) -> {
                            characteristic.subscribe(device)
                            characteristic.onSubscribe(device)
                            GattResponse.WriteSuccess
                        }
                        value.contentEquals(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE) -> {
                            characteristic.unsubscribe(device)
                            characteristic.onUnsubscribe(device)
                            GattResponse.WriteSuccess
                        }
                        else -> GattResponse.InvalidHandle
                    }
                }
            }
        }

        override fun descriptor(uuid: UUID, descriptor: LocalDescriptorDSL.() -> Unit) {
            require(descriptorBuilders.none { it.uuid == uuid }) { "Descriptor $uuid already declared" }
            descriptorBuilders.add(LocalDescriptor.DSL(uuid).apply(descriptor))
        }

        fun build(forService: LocalService): LocalCharacteristic = LocalCharacteristic(
            BluetoothGattCharacteristic(uuid, properties, permissions),
            forService,
            server,
        ) {
            descriptorBuilders.map {
                it.build(this)
            }
        }.apply {
            readAction?.let { onRead ->
                server.callback.registerReadAction(this, onRead)
            }
            writeAction?.let { onRead ->
                server.callback.registerWriteAction(this, onRead)
            }
        }
    }

    override val uuid: UUID = characteristic.uuid
    override val properties: Set<CharacteristicProperty> = CharacteristicProperty.fromInt(characteristic.properties)

    private val _subscribedDevices = MutableStateFlow(emptyList<ConnectedDevice>())
    actual val subscribedDevices = _subscribedDevices.asStateFlow()

    actual override val descriptors: List<LocalDescriptor> = buildDescriptors().also { descriptors ->
        descriptors.forEach { characteristic.addDescriptor(it.descriptor) }
    }

    actual val permissions: Set<Permission> = Permission.entries.filter {
        it.androidPermission and characteristic.permissions != 0
    }.toSet()

    actual suspend fun notify(device: ConnectedDevice, value: ByteArray): Boolean =
        if (properties.containsAny(setOf(CharacteristicProperty.Notify, CharacteristicProperty.Indicate))) {
            server.notify(this, device, value)
        } else {
            false
        }

    actual suspend fun notifyAll(value: ByteArray): Boolean {
        var result = true
        for (device in subscribedDevices.value) {
            result = result or notify(device, value)
        }
        return result
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
