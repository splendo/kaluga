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
import com.splendo.kaluga.bluetooth.uuidFrom

class LocalService internal constructor(
    val wrapper: LocalServiceWrapper,
    override val type: Service.Type,
    buildIncludedServices: LocalService.() -> List<LocalService>,
    buildCharacteristics: LocalService.() -> List<LocalCharacteristic>,
) : Service {

    sealed interface DSL {

        interface Primary : DSL {
            fun includedService(uuid: UUID, service: Secondary.() -> Unit)
            fun includedService(uuidString: String, service: Secondary.() -> Unit) {
                includedService(uuidFrom(uuidString), service)
            }
        }

        interface Secondary : DSL
        fun characteristic(uuid: UUID, characteristic: LocalCharacteristic.DSL.() -> Unit)
        fun characteristic(uuidString: String, characteristic: LocalCharacteristic.DSL.() -> Unit) {
            characteristic(uuidFrom(uuidString), characteristic)
        }
    }

    override val uuid: UUID = wrapper.uuid
    override val characteristics: List<LocalCharacteristic> = buildCharacteristics()

    override val includedServices: List<LocalService> = buildIncludedServices()
}

internal sealed class LocalServiceDSL(val uuid: UUID) {

    abstract val type: Service.Type
    internal val characteristicsBuilders = mutableListOf<LocalCharacteristicDSL>()
    abstract val includedServicesBuilders: List<Secondary>

    class Primary(
        uuid: UUID,
        private val notify: Notify,
        private val registerCharacteristicReadAction: LocalCharacteristicRegisterReadAction,
        private val registerCharacteristicWriteAction: LocalCharacteristicRegisterWriteAction,
        private val registerSubscriptionActions: NotifiableRegisterSubscription,
        private val buildDescriptor: BuildDescriptor,
    ) : LocalServiceDSL(uuid),
        LocalService.DSL.Primary {
        override val type: Service.Type = Service.Type.PRIMARY
        override val includedServicesBuilders = mutableListOf<Secondary>()

        override fun includedService(uuid: UUID, service: LocalService.DSL.Secondary.() -> Unit) {
            includedServicesBuilders.add(
                Secondary(
                    uuid,
                    notify,
                    registerCharacteristicReadAction,
                    registerCharacteristicWriteAction,
                    registerSubscriptionActions,
                    buildDescriptor,
                ).apply(service),
            )
        }

        override fun characteristic(uuid: UUID, characteristic: LocalCharacteristic.DSL.() -> Unit) {
            characteristicsBuilders.add(
                LocalCharacteristicDSL(
                    uuid,
                    notify,
                    registerCharacteristicReadAction,
                    registerCharacteristicWriteAction,
                    registerSubscriptionActions,
                    buildDescriptor,
                ).apply(characteristic),
            )
        }
    }
    class Secondary(
        uuid: UUID,
        private val notify: Notify,
        private val registerCharacteristicReadAction: LocalCharacteristicRegisterReadAction,
        private val registerCharacteristicWriteAction: LocalCharacteristicRegisterWriteAction,
        private val registerSubscriptionActions: NotifiableRegisterSubscription,
        private val buildDescriptor: BuildDescriptor,
    ) : LocalServiceDSL(uuid),
        LocalService.DSL.Secondary {
        override val type: Service.Type = Service.Type.SECONDARY
        override val includedServicesBuilders: List<Secondary> = emptyList()

        override fun characteristic(uuid: UUID, characteristic: LocalCharacteristic.DSL.() -> Unit) {
            characteristicsBuilders.add(
                LocalCharacteristicDSL(
                    uuid,
                    notify,
                    registerCharacteristicReadAction,
                    registerCharacteristicWriteAction,
                    registerSubscriptionActions,
                    buildDescriptor,
                ).apply(characteristic),
            )
        }
    }

    fun build(): LocalService = LocalService(
        LocalServiceWrapper(
            uuid,
            type,
        ),
        type,
        buildIncludedServices = {
            includedServicesBuilders.map { includedServiceBuilder ->
                includedServiceBuilder.build().also {
                    wrapper.addIncludedService(it.wrapper)
                }
            }
        },
        buildCharacteristics = {
            characteristicsBuilders.map {
                it.build(this)
            }
        },
    )
}

expect class LocalServiceWrapper {

    constructor(uuid: UUID, type: Service.Type)

    val uuid: UUID

    fun addIncludedService(service: LocalServiceWrapper)
    fun addCharacteristic(characteristic: LocalCharacteristicWrapper)
}
