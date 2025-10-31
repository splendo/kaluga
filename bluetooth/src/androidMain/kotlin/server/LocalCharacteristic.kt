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
import com.splendo.kaluga.bluetooth.Characteristic
import com.splendo.kaluga.bluetooth.CharacteristicProperty
import com.splendo.kaluga.bluetooth.Descriptor
import com.splendo.kaluga.bluetooth.UUID
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

actual sealed class LocalCharacteristic(val characteristic: BluetoothGattCharacteristic, actual override val service: LocalService) : Characteristic {

    actual enum class Permission(val androidPermission: Int) {
        READABLE(BluetoothGattCharacteristic.PERMISSION_READ),
        WRITABLE(BluetoothGattCharacteristic.PERMISSION_WRITE),
        READ_ENCRYPTION_REQUIRED(BluetoothGattCharacteristic.PERMISSION_READ_ENCRYPTED),
        WRITE_ENCRYPTION_REQUIRED(BluetoothGattCharacteristic.PERMISSION_WRITE_ENCRYPTED),
    }

    actual class Static internal constructor(characteristic: BluetoothGattCharacteristic, service: LocalService, buildDescriptors: Static.() -> List<LocalDescriptor>) :
        LocalCharacteristic(characteristic, service) {
        actual override val descriptors: List<LocalDescriptor> = buildDescriptors().also { descriptors ->
            descriptors.forEach { characteristic.addDescriptor(it.descriptor) }
        }
    }

    actual class Notifiable internal constructor(
        characteristic: BluetoothGattCharacteristic,
        service: LocalService,
        private val server: BluetoothServer,
        private val onSubscribe: Notifiable.(ConnectedDevice) -> Unit,
        private val onUnsubscribe: Notifiable.(ConnectedDevice) -> Unit,
        buildDescriptors: Notifiable.() -> List<LocalDescriptor>,
    ) : LocalCharacteristic(characteristic, service),
        FlowCollector<ByteArray> {

        private val _subscribedDevices = MutableStateFlow(emptyList<ConnectedDevice>())
        actual val subscribedDevices = _subscribedDevices.asStateFlow()
        actual override val descriptors: List<LocalDescriptor> = buildDescriptors().also { descriptors ->
            descriptors.forEach { characteristic.addDescriptor(it.descriptor) }
        }
        actual suspend fun notify(device: ConnectedDevice, value: ByteArray): Boolean = server.notify(this, device, value)

        actual suspend fun notifyAll(value: ByteArray): Boolean {
            var result = true
            for (device in subscribedDevices.value) {
                result = result or notify(device, value)
            }
            return result
        }

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

    internal class DSL(val uuid: UUID, private val server: BluetoothServer) : LocalCharacteristicDSL {

        var properties = 0
        var permissions = 0

        private var readAction: (suspend LocalCharacteristic.(ConnectedDevice, Int) -> GattResponse.ReadResponse)? = null
        private var writeAction: (suspend LocalCharacteristic.(ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse)? = null
        private var subscriptionActions: Pair<Notifiable.(ConnectedDevice) -> Unit, Notifiable.(ConnectedDevice) -> Unit>? = null
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
            onSubscribe: Notifiable.(ConnectedDevice) -> Unit,
            onUnsubscribe: Notifiable.(ConnectedDevice) -> Unit,
        ) {
            require(
                descriptorBuilders.none {
                    it.uuid == Descriptor.CLIENT_CHARACTERISTIC_CONFIGURATION_DESCRIPTOR
                } &&
                    subscriptionActions == null,
            ) { "Client characteristic configuration descriptor already declared" }
            this.properties = this.properties or properties.fold(0) { acc, property ->
                acc or when (property) {
                    CharacteristicProperty.Notify -> BluetoothGattCharacteristic.PROPERTY_NOTIFY
                    CharacteristicProperty.Indicate -> BluetoothGattCharacteristic.PROPERTY_INDICATE
                }
            }
            subscriptionActions = onSubscribe to onUnsubscribe
            descriptor(Descriptor.CLIENT_CHARACTERISTIC_CONFIGURATION_DESCRIPTOR) {
                writable(encrypted) { device, value, offset ->
                    when {
                        offset != 0 -> GattResponse.InvalidOffset
                        value.contentEquals(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE) ||
                            value.contentEquals(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE) -> {
                            (characteristic as Notifiable).subscribe(device)
                            GattResponse.WriteSuccess
                        }
                        value.contentEquals(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE) -> {
                            (characteristic as Notifiable).unsubscribe(device)
                            GattResponse.WriteSuccess
                        }
                        else -> GattResponse.InvalidHandle
                    }
                }
            }
        }

        override fun descriptor(uuid: UUID, descriptor: LocalDescriptorDSL.() -> Unit) {
            require(descriptorBuilders.none { it.uuid == uuid }) { "Descriptor $uuid already declared" }
            descriptorBuilders.add(LocalDescriptor.DSL(uuid, server).apply(descriptor))
        }

        fun build(forService: LocalService): LocalCharacteristic {
            val characteristic = subscriptionActions?.let { (onSubscribe, onUnsubscribe) ->

                Notifiable(
                    BluetoothGattCharacteristic(uuid, properties, permissions),
                    forService,
                    server,
                    onSubscribe,
                    onUnsubscribe,
                ) {
                    descriptorBuilders.mapNotNull { descriptorBuilder ->
                        descriptorBuilder.build(this).takeIf { it.uuid != Descriptor.CLIENT_CHARACTERISTIC_CONFIGURATION_DESCRIPTOR }
                    }
                }
            } ?: Static(
                BluetoothGattCharacteristic(uuid, properties, permissions),
                forService,
            ) {
                descriptorBuilders.map { it.build(this) }
            }

            readAction?.let { onRead ->
                server.callback.registerReadAction(characteristic, onRead)
            }
            writeAction?.let { onRead ->
                server.callback.registerWriteAction(characteristic, onRead)
            }

            return characteristic
        }
    }

    actual override val uuid: UUID = characteristic.uuid
    actual override val properties: Set<CharacteristicProperty> = CharacteristicProperty.fromInt(characteristic.properties)

    actual abstract override val descriptors: List<LocalDescriptor>

    actual val permissions: Set<Permission> = Permission.entries.filter {
        it.androidPermission and characteristic.permissions != 0
    }.toSet()
}
