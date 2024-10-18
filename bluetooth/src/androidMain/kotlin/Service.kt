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
actual interface ServiceWrapper {

    /**
     * Service Type
     */
    enum class Type {

        /**
         * Primary service
         */
        PRIMARY,

        /**
         * Secondary service (included by primary services)
         */
        SECONDARY,
    }

    /**
     * The [UUID] of the service
     */
    actual val uuid: java.util.UUID

    /**
     * The [Type] of this service (primary/secondary)
     */
    val type: Type

    /**
     * Returns the instance ID for this service.
     * If a remote device offers multiple services with the same UUID (ex. multiple battery services for different batteries), the instance ID is used to distinguish services.
     */
    val instanceId: Int

    /**
     * The list of [CharacteristicWrapper] associated with the service
     */
    actual val characteristics: List<CharacteristicWrapper>

    /**
     * The list of [ServiceWrapper] included in this service
     */
    val includedServices: List<ServiceWrapper>

    /**
     * Gets the [CharacteristicWrapper] for the characteristic with a given [java.util.UUID] if it belongs to the service
     * @param uuid the [java.util.UUID] of the characteristic to get
     * @return the [CharacteristicWrapper] belonging to [uuid] if it exists, or `null` otherwise
     */
    fun getCharacteristic(uuid: java.util.UUID): CharacteristicWrapper?

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
 * Default implementation of [ServiceWrapper]
 * @param gattService the [BluetoothGattService] to wrap
 */
class DefaultGattServiceWrapper(private val gattService: BluetoothGattService) : ServiceWrapper {

    override val uuid: java.util.UUID
        get() = gattService.uuid
    override val type: ServiceWrapper.Type
        get() = when (gattService.type) {
            BluetoothGattService.SERVICE_TYPE_PRIMARY -> ServiceWrapper.Type.PRIMARY
            else -> ServiceWrapper.Type.SECONDARY
        }
    override val instanceId: Int
        get() = gattService.instanceId
    override val characteristics: List<CharacteristicWrapper>
        get() = gattService.characteristics.map { DefaultCharacteristicWrapper(it) }
    override val includedServices: List<ServiceWrapper>
        get() = gattService.includedServices.map { DefaultGattServiceWrapper(it) }

    override fun getCharacteristic(uuid: java.util.UUID): CharacteristicWrapper? = gattService.getCharacteristic(uuid)?.let { DefaultCharacteristicWrapper(it) }

    override fun addCharacteristic(characteristic: BluetoothGattCharacteristic): Boolean = gattService.addCharacteristic(characteristic)

    override fun addService(service: BluetoothGattService): Boolean = gattService.addService(service)
}
