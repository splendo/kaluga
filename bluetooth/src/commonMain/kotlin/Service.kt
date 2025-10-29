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

import com.splendo.kaluga.bluetooth.device.DeviceConnectionManager
import com.splendo.kaluga.logging.ContextualLogger

interface Service : Attribute {
    enum class Type {
        PRIMARY,
        SECONDARY;
    }

    val type: Type
    val includedServices: List<Service>
    val characteristics: List<Characteristic>
}

/**
 * A Bluetooth Service
 * @param service the [ServiceWrapper] to access the platform service
 * @param emitNewAction method to call when a new [DeviceConnectionManager.Event.AddAction] event should take place
 * @param logger the [ContextualLogger] to use for logging.
 */
class RemoteService(
    service: ServiceWrapper,
    override val includedServices: List<RemoteService>,
    emitNewAction: (DeviceConnectionManager.Event.AddAction) -> Unit, logger: ContextualLogger) : Service {

    /**
     * The [UUID] of the service
     */
    override val uuid = service.uuid
    override val type = service.type

    /**
     * The list of [Characteristic] associated with the service
     */
    override val characteristics: List<RemoteCharacteristic> = service.characteristics.map {
        RemoteCharacteristic(
            it,
            service = this,
            emitNewAction = emitNewAction,
            logger = logger.withAppendedContext("Service" to uuid.uuidString, "Characteristic" to it.uuid.uuidString),
        )
    }
}

/**
 * Accessor to the platform level Bluetooth service
 */
expect interface ServiceWrapper {
    val type: Service.Type

    val includedServices: List<ServiceWrapper>

    /**
     * The list of [CharacteristicWrapper] associated with the service
     */
    val characteristics: List<CharacteristicWrapper>

    /**
     * The [UUID] of the service
     */
    val uuid: UUID
}
