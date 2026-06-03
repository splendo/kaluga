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

import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.UUID
import platform.CoreBluetooth.CBMutableService
import platform.CoreBluetooth.CBService
import kotlin.jvm.JvmInline

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
     * Identity used to correlate this service with incoming peripheral-manager callbacks.
     */
    val identity: AttributeIdentity

    /**
     * Adds the service to a parent [CBMutableService] as an included service
     */
    fun addToParent(parent: CBMutableService)

    /**
     * Adds the service to a [KalugaBluetoothServerWrapper]
     */
    fun addTo(serverWrapper: KalugaBluetoothServerWrapper)

    /**
     * Removes the service from a [KalugaBluetoothServerWrapper]
     */
    fun removeFrom(serverWrapper: KalugaBluetoothServerWrapper)
}

class DefaultLocalServiceWrapper(internal val service: CBMutableService) : LocalServiceWrapper {
    constructor(uuid: UUID, type: Service.Type) : this(
        CBMutableService(uuid, type == Service.Type.PRIMARY),
    )

    override val uuid: UUID = service.UUID

    override val identity: AttributeIdentity get() = CBServiceIdentity(service)

    override fun addIncludedService(service: LocalServiceWrapper) {
        service.addToParent(this.service)
    }

    override fun addCharacteristic(characteristic: LocalCharacteristicWrapper) {
        characteristic.addToService(service)
    }

    override fun addToParent(parent: CBMutableService) {
        parent.setIncludedServices(parent.includedServices.orEmpty() + service)
    }

    override fun addTo(serverWrapper: KalugaBluetoothServerWrapper) {
        serverWrapper.add(service)
    }

    override fun removeFrom(serverWrapper: KalugaBluetoothServerWrapper) {
        serverWrapper.remove(service)
    }
}

@JvmInline
value class CBServiceIdentity(val service: CBService) : AttributeIdentity
