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

import com.splendo.kaluga.bluetooth.Characteristic
import com.splendo.kaluga.bluetooth.CharacteristicProperty
import com.splendo.kaluga.bluetooth.UUID
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow

interface LocalCharacteristicDSL {
    fun readable(encrypted: Boolean = false, onRead: suspend LocalCharacteristic.(ConnectedDevice, Int) -> GattResponse.ReadResponse)
    fun writable(
        properties: Set<CharacteristicProperty.Writable> = setOf(CharacteristicProperty.Write),
        encrypted: Boolean = false,
        onWrite: suspend LocalCharacteristic.(ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse,
    )
    fun notifiable(
        properties: Set<CharacteristicProperty.Notifiable> = setOf(CharacteristicProperty.Notify),
        encrypted: Boolean = false,
        onSubscribe: suspend LocalCharacteristic.(ConnectedDevice) -> Unit,
        onUnsubscribe: suspend LocalCharacteristic.(ConnectedDevice) -> Unit,
    )
    fun descriptor(uuid: UUID, descriptor: LocalDescriptorDSL.() -> Unit)
}
expect class LocalCharacteristic :
    Characteristic,
    FlowCollector<ByteArray> {

    enum class Permission {
        READABLE,
        WRITABLE,
        READ_ENCRYPTION_REQUIRED,
        WRITE_ENCRYPTION_REQUIRED,
    }

    expect override val service: LocalService
    expect val permissions: Set<Permission>
    expect val subscribedDevices: StateFlow<List<ConnectedDevice>>
    expect override val descriptors: List<LocalDescriptor>

    expect suspend fun notify(device: ConnectedDevice, value: ByteArray): Boolean
    expect suspend fun notifyAll(value: ByteArray): Boolean
}
