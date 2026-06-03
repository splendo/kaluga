/*
 Copyright (c) 2020. Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bluetooth

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService

/**
 * Accessor to a [BluetoothGattService]
 */
actual interface RemoteServiceWrapper {

    /**
     * The [UUID] of the service
     */
    actual val uuid: java.util.UUID

    /**
     * The [Service.Type] of this service (primary/secondary)
     */
    actual val type: Service.Type

    /**
     * Returns the instance ID for this service.
     * If a remote device offers multiple services with the same UUID (ex. multiple battery services for different batteries), the instance ID is used to distinguish services.
     */
    val instanceId: Int

    /**
     * The list of [RemoteCharacteristicWrapper] associated with the service
     */
    actual val characteristics: List<RemoteCharacteristicWrapper>

    /**
     * The list of [RemoteServiceWrapper] included in this service
     */
    actual val includedServices: List<RemoteServiceWrapper>

    /**
     * Gets the [RemoteCharacteristicWrapper] for the characteristic with a given [java.util.UUID] if it belongs to the service
     * @param uuid the [java.util.UUID] of the characteristic to get
     * @return the [RemoteCharacteristicWrapper] belonging to [uuid] if it exists, or `null` otherwise
     */
    fun getCharacteristic(uuid: java.util.UUID): RemoteCharacteristicWrapper?

    /**
     * Adds a [BluetoothGattCharacteristic] to the service
     * @param characteristic the [BluetoothGattCharacteristic] to add
     * @return `true` if the characteristic was added to [characteristics]
     */
    fun addCharacteristic(characteristic: BluetoothGattCharacteristic): Boolean

    /**
     * Adds an included [BluetoothGattService] to the service
     * @param service the [BluetoothGattService] to add
     * @return `true` if the service was added to [includedServices]
     */
    fun addService(service: BluetoothGattService): Boolean
}

/**
 * Default implementation of [RemoteServiceWrapper]
 * @param gattService the [BluetoothGattService] to wrap
 */
class DefaultGattServiceWrapper(private val gattService: BluetoothGattService) : RemoteServiceWrapper {

    override val uuid: java.util.UUID
        get() = gattService.uuid
    override val type: Service.Type
        get() = when (gattService.type) {
            BluetoothGattService.SERVICE_TYPE_PRIMARY -> Service.Type.PRIMARY
            else -> Service.Type.SECONDARY
        }
    override val instanceId: Int
        get() = gattService.instanceId
    override val characteristics: List<RemoteCharacteristicWrapper>
        get() = gattService.characteristics.map { DefaultRemoteCharacteristicWrapper(it) }
    override val includedServices: List<RemoteServiceWrapper>
        get() = gattService.includedServices.map { DefaultGattServiceWrapper(it) }

    override fun getCharacteristic(uuid: java.util.UUID): RemoteCharacteristicWrapper? = gattService.getCharacteristic(uuid)?.let { DefaultRemoteCharacteristicWrapper(it) }

    override fun addCharacteristic(characteristic: BluetoothGattCharacteristic): Boolean = gattService.addCharacteristic(characteristic)

    override fun addService(service: BluetoothGattService): Boolean = gattService.addService(service)
}
