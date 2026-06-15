/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

import android.bluetooth.BluetoothGattServer
import android.bluetooth.BluetoothGattService
import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.UUID

actual interface LocalServiceWrapper {

    /**
     * The [UUID] of the service
     */
    actual val uuid: UUID

    /**
     * Adds an included [LocalServiceWrapper] to the service
     */
    actual fun addIncludedService(service: LocalServiceWrapper)

    /**
     * Adds a [LocalCharacteristicWrapper] to the service
     */
    actual fun addCharacteristic(characteristic: LocalCharacteristicWrapper)

    /**
     * Identity used to correlate this service with incoming GATT callbacks.
     */
    val identity: AttributeIdentity

    /**
     * Adds the service to a parent [BluetoothGattService] as an included service
     */
    fun addToParent(parent: BluetoothGattService)

    /**
     * Adds the service to a [BluetoothGattServer]
     */
    fun addTo(gattServer: BluetoothGattServer): Boolean

    /**
     * Removes the service from a [BluetoothGattServer]
     */
    fun removeFrom(gattServer: BluetoothGattServer)
}

class DefaultLocalServiceWrapper(internal val service: BluetoothGattService) : LocalServiceWrapper {
    constructor(
        uuid: UUID,
        type: Service.Type,
    ) : this(
        BluetoothGattService(
            uuid,
            when (type) {
                Service.Type.PRIMARY -> BluetoothGattService.SERVICE_TYPE_PRIMARY
                Service.Type.SECONDARY -> BluetoothGattService.SERVICE_TYPE_SECONDARY
            },
        ),
    )

    override val uuid: UUID = service.uuid

    override val identity: AttributeIdentity get() = GattServiceIdentity(service)

    override fun addIncludedService(service: LocalServiceWrapper) {
        service.addToParent(this.service)
    }

    override fun addCharacteristic(characteristic: LocalCharacteristicWrapper) {
        characteristic.addToService(service)
    }

    override fun addToParent(parent: BluetoothGattService) {
        parent.addService(service)
    }

    override fun addTo(gattServer: BluetoothGattServer): Boolean = gattServer.addService(service)

    override fun removeFrom(gattServer: BluetoothGattServer) {
        gattServer.removeService(service)
    }
}

@JvmInline
value class GattServiceIdentity(val service: BluetoothGattService) : AttributeIdentity
