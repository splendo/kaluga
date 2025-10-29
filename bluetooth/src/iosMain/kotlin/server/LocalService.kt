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

import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.logging.Logger
import platform.CoreBluetooth.CBMutableService

actual class LocalService(
    val service: CBMutableService,
    override val type: Service.Type,
    private val server: BluetoothServer,
    buildIncludedServices: LocalService.() -> List<LocalService>,
    buildCharacteristics: LocalService.() -> List<LocalCharacteristic>,
) : Service {
    internal sealed class DSL(val uuid: UUID, val server: BluetoothServer, val logger: Logger) {

        abstract val type: Service.Type
        internal val characteristicsBuilders = mutableListOf<LocalCharacteristic.DSL>()
        abstract val includedServicesBuilders: List<DSL>

        class Primary(uuid: UUID, server: BluetoothServer, logger: Logger) :
            DSL(uuid, server, logger),
            LocalServiceDSL.Primary {
            override val type: Service.Type = Service.Type.PRIMARY
            override val includedServicesBuilders = mutableListOf<Secondary>()

            override fun includedService(uuid: UUID, service: LocalServiceDSL.Secondary.() -> Unit) {
                includedServicesBuilders.add(Secondary(uuid, server, logger).apply(service))
            }

            override fun characteristic(uuid: UUID, characteristic: LocalCharacteristicDSL.() -> Unit) {
                characteristicsBuilders.add(LocalCharacteristic.DSL(uuid, server, logger).apply(characteristic))
            }
        }

        class Secondary(uuid: UUID, server: BluetoothServer, logger: Logger) :
            DSL(uuid, server, logger),
            LocalServiceDSL.Secondary {
            override val type: Service.Type = Service.Type.SECONDARY
            override val includedServicesBuilders: List<DSL> = emptyList()

            override fun characteristic(uuid: UUID, characteristic: LocalCharacteristicDSL.() -> Unit) {
                characteristicsBuilders.add(LocalCharacteristic.DSL(uuid, server, logger).apply(characteristic))
            }
        }

        fun build(): LocalService = LocalService(
            CBMutableService(
                uuid,
                type == Service.Type.PRIMARY,
            ),
            type,
            server,
            buildIncludedServices = {
                includedServicesBuilders.map {
                    it.build()
                }
            },
            buildCharacteristics = {
                characteristicsBuilders.map {
                    it.build(this)
                }
            },
        )
    }

    override val uuid: UUID = service.UUID
    actual override val characteristics: List<LocalCharacteristic> = buildCharacteristics().also { characteristics ->
        service.setCharacteristics(characteristics.map { it.characteristic })
    }

    actual override val includedServices: List<LocalService> = buildIncludedServices().also { includedServices ->
        service.setIncludedServices(includedServices.map { it.service })
    }
}
