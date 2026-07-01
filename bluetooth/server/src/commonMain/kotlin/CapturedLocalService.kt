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

import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.UUID

/**
 * The result of [buildCapturingLocalService], holding the built [LocalService] together with every read,
 * write and subscription action captured while building it.
 * @property service the built [LocalService]
 * @property characteristicReads the captured read actions per characteristic [UUID]
 * @property characteristicWrites the captured write actions per characteristic [UUID]
 * @property descriptorReads the captured read actions per descriptor [UUID]
 * @property descriptorWrites the captured write actions per descriptor [UUID]
 * @property subscribableCharacteristics the [UUID]s of the characteristics that registered a subscription action
 */
class CapturedLocalService(
    val service: LocalService,
    val characteristicReads: Map<UUID, suspend LocalCharacteristic.(ConnectedDevice, Int) -> GattResponse.ReadResponse>,
    val characteristicWrites: Map<UUID, suspend LocalCharacteristic.(ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse>,
    val descriptorReads: Map<UUID, suspend LocalDescriptor.(ConnectedDevice, Int) -> GattResponse.ReadResponse>,
    val descriptorWrites: Map<UUID, suspend LocalDescriptor.(ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse>,
    val subscribableCharacteristics: Set<UUID>,
)

/**
 * Test support builder that constructs a [LocalService] while capturing the per-attribute read and write
 * actions and the subscribable characteristics into a [CapturedLocalService].
 * @param uuid the [UUID] of the [LocalService]
 * @param wrapperBuilder the [LocalServiceWrapperBuilder] used to create the platform service
 * @param onNotify invoked when a characteristic notifies a device
 * @param service the [LocalService.DSL] block describing the service
 * @return the [CapturedLocalService] holding the built service and all captured actions
 */
fun buildCapturingLocalService(
    uuid: UUID,
    wrapperBuilder: LocalServiceWrapperBuilder,
    onNotify: suspend (LocalCharacteristic.Notifiable, ConnectedDevice, ByteArray) -> Boolean = { _, _, _ -> true },
    service: LocalService.DSL.() -> Unit,
): CapturedLocalService {
    val characteristicReads = mutableMapOf<UUID, suspend LocalCharacteristic.(ConnectedDevice, Int) -> GattResponse.ReadResponse>()
    val characteristicWrites = mutableMapOf<UUID, suspend LocalCharacteristic.(ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse>()
    val descriptorReads = mutableMapOf<UUID, suspend LocalDescriptor.(ConnectedDevice, Int) -> GattResponse.ReadResponse>()
    val descriptorWrites = mutableMapOf<UUID, suspend LocalDescriptor.(ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse>()
    val subscribableCharacteristics = mutableSetOf<UUID>()

    val builtService = LocalServiceDSL(
        uuid,
        Service.Type.PRIMARY,
        onNotify,
        registerCharacteristicReadAction = { characteristic, onRead -> characteristicReads[characteristic.uuid] = onRead },
        registerCharacteristicWriteAction = { characteristic, onWrite -> characteristicWrites[characteristic.uuid] = onWrite },
        registerSubscriptionActions = { subscribableCharacteristics += this.uuid },
        buildDescriptor = { descriptorUuid ->
            LocalDescriptorDSL(
                descriptorUuid,
                registerReadAction = { descriptor, onRead -> descriptorReads[descriptor.uuid] = onRead },
                registerWriteAction = { descriptor, onWrite -> descriptorWrites[descriptor.uuid] = onWrite },
            )
        },
        wrapperBuilder = wrapperBuilder,
    ).apply(service).build()

    return CapturedLocalService(
        builtService,
        characteristicReads,
        characteristicWrites,
        descriptorReads,
        descriptorWrites,
        subscribableCharacteristics,
    )
}
