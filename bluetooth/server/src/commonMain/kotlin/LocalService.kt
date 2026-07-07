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

import com.splendo.kaluga.bluetooth.CharacteristicProperty
import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.uuidFrom

/**
 * A [Service] available from a [BluetoothServer]
 * @property wrapper the [com.splendo.kaluga.bluetooth.server.LocalServiceWrapper] to access the platform service.
 */
class LocalService internal constructor(
    val wrapper: LocalServiceWrapper,
    override val type: Service.Type,
    buildIncludedServices: LocalService.() -> List<LocalService>,
    buildCharacteristics: LocalService.() -> List<LocalCharacteristic>,
) : Service {

    /**
     * DSL for setting up a [LocalService].
     */
    interface DSL {

        /**
         * Includes a [LocalService] in the service being built. The included service is configured with the same [DSL]
         * and may include further services, to any depth.
         * @param uuid the [UUID] of the [LocalService] to include
         * @param service the [DSL] to use to set up the included [LocalService]
         */
        fun includedService(uuid: UUID, service: DSL.() -> Unit)

        /**
         * Includes a [LocalService] in the service being built
         * @param uuidString string of the [UUID] of the [LocalService] to include
         * @param service the [DSL] to use to set up the included [LocalService]
         * @throws com.splendo.kaluga.bluetooth.UUIDException if [uuidString] is not a valid [UUID]
         */
        fun includedService(uuidString: String, service: DSL.() -> Unit) {
            includedService(uuidFrom(uuidString), service)
        }

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
         * @throws com.splendo.kaluga.bluetooth.UUIDException if [uuidString] is not a valid [UUID]
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

internal class LocalServiceDSL(
    val uuid: UUID,
    val type: Service.Type,
    private val notify: Notify,
    private val registerCharacteristicReadAction: LocalCharacteristicRegisterReadAction,
    private val registerCharacteristicWriteAction: LocalCharacteristicRegisterWriteAction,
    private val registerSubscriptionActions: NotifiableRegisterSubscription,
    private val buildDescriptor: BuildDescriptor,
    private val wrapperBuilder: LocalServiceWrapperBuilder,
) : LocalService.DSL {

    internal val characteristicsBuilders = mutableListOf<LocalCharacteristicDSL>()
    internal val includedServicesBuilders = mutableListOf<LocalServiceDSL>()

    override fun includedService(uuid: UUID, service: LocalService.DSL.() -> Unit) {
        includedServicesBuilders.add(
            LocalServiceDSL(
                uuid,
                Service.Type.SECONDARY,
                notify,
                registerCharacteristicReadAction,
                registerCharacteristicWriteAction,
                registerSubscriptionActions,
                buildDescriptor,
                wrapperBuilder,
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

    fun build(): LocalService = LocalService(
        wrapperBuilder.createService(uuid, type),
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
                it.build(this, wrapperBuilder)
            }
        },
    )
}

/**
 * Accessor to the platform level Local Bluetooth service.
 *
 * Implemented per platform by `DefaultLocalServiceWrapper` (wrapping the framework service) and
 * mockable in tests, allowing a [LocalService] graph to be built without a live Bluetooth stack.
 */
expect interface LocalServiceWrapper {

    /**
     * The [UUID] of the service
     */
    val uuid: UUID

    /**
     * Adds an included [LocalServiceWrapper] to the service
     */
    fun addIncludedService(service: LocalServiceWrapper)

    /**
     * Adds a [LocalCharacteristicWrapper] to the service
     */
    fun addCharacteristic(characteristic: LocalCharacteristicWrapper)
}

/**
 * Creates the platform [LocalServiceWrapper]s, [LocalCharacteristicWrapper]s and
 * [LocalDescriptorWrapper]s for a [LocalService] graph. Injected into the DSL so tests can supply
 * wrappers that don't require a live Bluetooth stack.
 */
interface LocalServiceWrapperBuilder {

    /**
     * Creates a [LocalServiceWrapper] for the given [uuid] and [type]
     */
    fun createService(uuid: UUID, type: Service.Type): LocalServiceWrapper

    /**
     * Creates a [LocalCharacteristicWrapper] for the given [uuid], [properties] and [permissions]
     */
    fun createCharacteristic(
        uuid: UUID,
        properties: Set<CharacteristicProperty>,
        encryptedNotification: Boolean,
        permissions: Set<LocalCharacteristic.Permission>,
    ): LocalCharacteristicWrapper

    /**
     * Creates a [LocalDescriptorWrapper] for the given [uuid] and [permissions]
     */
    fun createDescriptor(uuid: UUID, permissions: Set<LocalDescriptor.Permissions>): LocalDescriptorWrapper
}
