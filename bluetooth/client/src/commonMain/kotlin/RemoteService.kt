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

import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.DeviceConnectionManager
import com.splendo.kaluga.logging.ContextualLogger

/**
 * A [Service] that is accessed remotely by a bluetooth client using [BluetoothClient]
 */
class RemoteService internal constructor(
    service: RemoteServiceWrapper,
    override val includedServices: List<RemoteService>,
    emitNewAction: (DeviceConnectionManager.Event.AddAction) -> Unit,
    logger: ConnectionSettings.ConnectionLogger.ServiceLogger,
) : Service {

    /**
     * Constructor
     * @param service the [RemoteServiceWrapper] to access the platform service.
     * @param includedServices the list of [com.splendo.kaluga.bluetooth.RemoteService] this service includes.
     * @param emitNewAction method to call when a new [DeviceConnectionManager.Event.AddAction] event should take place
     * @param logger the [ContextualLogger] to use for logging.
     */
    constructor(
        service: RemoteServiceWrapper,
        includedServices: List<RemoteService>,
        emitNewAction: (DeviceConnectionManager.Event.AddAction) -> Unit,
        logger: ContextualLogger,
    ) : this(
        service,
        includedServices,
        emitNewAction,
        ConnectionSettings.ConnectionLogger.ServiceLogger(logger),
    )

    override val uuid = service.uuid
    override val type = service.type

    /**
     * The list of [RemoteCharacteristic] this service supports
     */
    override val characteristics: List<RemoteCharacteristic> = service.characteristics.map {
        RemoteCharacteristic(
            it,
            service = this,
            emitNewAction = emitNewAction,
            logger = logger[it.uuid],
        )
    }
}

/**
 * Accessor to the platform level Bluetooth service
 */
expect interface RemoteServiceWrapper {

    /**
     * The [Service.Type] of the service
     */
    val type: Service.Type

    /**
     * The list of [RemoteServiceWrapper] this service includes
     */
    val includedServices: List<RemoteServiceWrapper>

    /**
     * The list of [RemoteCharacteristicWrapper] associated with the service
     */
    val characteristics: List<RemoteCharacteristicWrapper>

    /**
     * The [UUID] of the service
     */
    val uuid: UUID
}
