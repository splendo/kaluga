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
import com.splendo.kaluga.logging.Logger

/**
 * A Bluetooth Service
 * @param service the [ServiceWrapper] to access the platform service
 * @param emitNewAction method to call when a new [DeviceConnectionManager.Event.AddAction] event should take place
 * @param parentLogTag the log tag used to modify the log tag of this service
 * @param logger the [Logger] to use for logging.
 */
class Service(service: ServiceWrapper, emitNewAction: (DeviceConnectionManager.Event.AddAction) -> Unit, parentLogTag: String, logger: Logger) {

    /**
     * The [UUID] of the service
     */
    val uuid = service.uuid

    /**
     * The list of [Characteristic] associated with the service
     */
    val characteristics = service.characteristics.map {
        Characteristic(
            it,
            emitNewAction = emitNewAction,
            parentLogTag = "$parentLogTag Service ${uuid.uuidString}",
            logger = logger,
        )
    }
}

/**
 * Accessor to the platform level Bluetooth service
 */
expect interface ServiceWrapper {

    /**
     * The list of [CharacteristicWrapper] associated with the service
     */
    val characteristics: List<CharacteristicWrapper>

    /**
     * The [UUID] of the service
     */
    val uuid: UUID
}
