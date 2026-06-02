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
import com.splendo.kaluga.bluetooth.uuidFrom

/**
 * A [Service] available from a [BluetoothServer]
 * @property wrapper the [com.splendo.kaluga.bluetooth.LocalServiceWrapper] to access the platform service.
 */
class LocalService internal constructor(
    val wrapper: LocalServiceWrapper,
    override val type: Service.Type,
    buildIncludedServices: LocalService.() -> List<LocalService>,
    buildCharacteristics: LocalService.() -> List<LocalCharacteristic>,
) : Service {

    /**
     * DSL for setting up a [LocalService]
     */
    sealed interface DSL {

        /**
         * [DSL] for setting up a [LocalService] with a [Service.Type.PRIMARY] type
         */
        interface Primary : DSL {

            /**
             * Includes a [LocalService] to the service being built
             * @param uuid the [UUID] of the [LocalService] to include
             * @param service the [LocalService.DSL.Secondary] to use to set up the included [LocalService]
             */
            fun includedService(uuid: UUID, service: Secondary.() -> Unit)

            /**
             * Includes a [LocalService] to the service being built
             * @param uuidString string of the [UUID] of the [LocalService] to include
             * @param service the [LocalService.DSL.Secondary] to use to set up the included [LocalService]
             * @throws UUIDException if [uuidString] is not a valid [UUID]
             */
            fun includedService(uuidString: String, service: Secondary.() -> Unit) {
                includedService(uuidFrom(uuidString), service)
            }
        }

        /**
         * [DSL] for setting up a [LocalService] with a [Service.Type.SECONDARY] type
         */
        interface Secondary : DSL

        /**
         * Adds a [LocalCharacteristic] to the service being built
         * @param uuid the [UUID] of the [LocalCharacteristic] to add
         * @param characteristic the [LocalCharacteristic.DSL] to use to set up the [LocalCharacteristic]
         */
        fun characteristic(uuid: UUID, characteristic: LocalCharacteristic.DSL.() -> Unit)

        /**
         * Adds a [LocalCharacteristic] to the service being built
         * @param uuidString string of the [UUID] of the [LocalCharacteristic] to add
         * @param characteristic the [LocalCharacteristic.DSL] to use to set up the [LocalCharacteristic]
         * @throws UUIDException if [uuidString] is not a valid [UUID]
         */
        fun characteristic(uuidString: String, characteristic: LocalCharacteristic.DSL.() -> Unit) {
            characteristic(uuidFrom(uuidString), characteristic)
        }
    }

    override val uuid: UUID = wrapper.uuid

    /**
     * The list of [LocalCharacteristic] this service supports
     */
    override val characteristics: List<LocalCharacteristic> = buildCharacteristics()

    /**
     * The list of [LocalService] included in this service
     */
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

/**
 * Accessor to the platform level Local Bluetooth service
 */
expect class LocalServiceWrapper {

    internal constructor(uuid: UUID, type: Service.Type)

    /**
     * The [UUID] of the service
     */
    val uuid: UUID

    /**
     * Adds an included [com.splendo.kaluga.bluetooth.LocalServiceWrapper] to the service
     */
    fun addIncludedService(service: LocalServiceWrapper)

    /**
     * Adds a [com.splendo.kaluga.bluetooth.LocalCharacteristicWrapper] to the service
     */
    fun addCharacteristic(characteristic: LocalCharacteristicWrapper)
}
