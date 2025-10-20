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

import com.splendo.kaluga.bluetooth.CharacteristicWrapper
import com.splendo.kaluga.bluetooth.ServiceWrapper
import com.splendo.kaluga.bluetooth.UUID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

expect class BluetoothServer : AutoCloseable {

    interface DSL {
        fun advertise(data: AdvertisementDataBuilder.() -> Unit)
        fun service(uuid: UUID, service: Service.DSL.Primary.() -> Unit)
    }

    expect class Builder {
        expect fun build(dsl: DSL.() -> Unit): BluetoothServer
    }

    val isAdvertising: StateFlow<Boolean>
    val services: List<Service>

    suspend fun advertise(data: AdvertisementDataBuilder.() -> Unit)
    fun stopAdvertising()

    suspend fun add(service: Service.DSL.Primary.() -> Unit): Service
    fun remove(service: Service)
    fun removeAllServices()
}

sealed interface GattResponse {
    sealed interface ReadResponse : GattResponse
    data class ReadSuccess(val value: ByteArray) : ReadResponse {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) {
                return false
            }

            other as ReadSuccess

            return value.contentEquals(other.value)
        }

        override fun hashCode(): Int {
            return value.contentHashCode()
        }
    }

    sealed interface WriteResponse : GattResponse
    data object WriteSuccess : WriteResponse
    sealed interface Error : ReadResponse, WriteResponse
    data object InvalidHandle : Error
    data object InvalidAttributeLength : Error
    data object InvalidOffset : Error
    data object AttributeNotFound : Error
    data object AttributeNotLong : Error
    data object InvalidAttributeValueLength : Error
    data object UnlikelyError : Error
    data object InsufficientResources : Error
}

interface AdvertisementDataBuilder {
    var localName: String?
    fun serviceUUIDs(vararg uuid: UUID)
}

expect class Service {

    enum class Type {
        PRIMARY,
        SECONDARY;
    }

    sealed interface DSL {

        interface Primary : DSL {
            fun includedService(uuid: UUID, service: Secondary.() -> Unit)
        }

        interface Secondary : DSL
        fun characteristic(uuid: UUID, characteristic: Characteristic.DSL.() -> Unit)
    }

    expect val uuid: UUID
    expect val type: Type
    expect val characteristics: List<Characteristic>
    expect val includedServices: List<Service>
}

expect class Characteristic {
    interface DSL {
        fun readable(
            encrypted: Boolean = false,
            onRead: suspend Characteristic.(Device, Int) -> GattResponse.ReadResponse
        )
        fun writable(
            properties: Set<Property.Writable> = setOf(Property.Write),
            encrypted: Boolean = false,
            onWrite: suspend Characteristic.(Device, ByteArray, Int) -> GattResponse.WriteResponse
        )
        fun notifiable(
            properties: Set<Property.Notifiable> = setOf(Property.Notify),
            encrypted: Boolean = false,
            onSubscribe: suspend Characteristic.(Device) -> Unit,
            onUnsubscribe: suspend Characteristic.(Device) -> Unit,
        )
        fun descriptor(uuid: UUID, descriptor: Descriptor.DSL.() -> Unit)
    }

    sealed class Property {
        data object Read : Property
        sealed class Writable : Property
        data object Write : Writable
        data object WriteWithoutResponse : Writable
        data object SignedWrite : Writable
        sealed class Notifiable : Property
        data object Notify : Notifiable
        data object Indicate : Notifiable
    }

    enum class Permission {
        READABLE,
        WRITABLE,
        READ_ENCRYPTION_REQUIRED,
        WRITE_ENCRYPTION_REQUIRED;
    }

    expect val uuid: UUID
    expect val service: Service
    expect val properties: Set<Property>
    expect val permissions: Set<Permission>
    expect val subscribedDevices: List<Device>
    expect val descriptors: List<Descriptor>

    expect suspend fun notify(device: Device, value: ByteArray)
}

suspend fun Characteristic.notifyAll(value: ByteArray) = subscribedDevices.forEach { notify(it, value) }

expect class Descriptor {

    interface DSL {
        fun readable(
            encrypted: Boolean = false,
            onRead: suspend Descriptor.(Device, Int) -> GattResponse.ReadResponse
        )

        fun writable(
            encrypted: Boolean = false,
            onWrite: suspend Descriptor.(Device, ByteArray, Int) -> GattResponse.WriteResponse
        )
    }

    expect val uuid: UUID
    expect val characteristic: Characteristic
}

expect class Device {

}
