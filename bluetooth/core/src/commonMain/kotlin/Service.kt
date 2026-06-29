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

/**
 * A GATT Service is an [Attribute] that forms collection of data and associated behaviors to accomplish a particular function or feature.
 * In GATT, a service is defined by its service definition.
 * A service definition may contain included services, mandatory characteristics, and optional characteristics.
 */
interface Service : Attribute {

    /**
     * There are two types of services: primary service and secondary service.
     * A primary service is a service that exposes functionality of this device.
     * A primary service can be included by another service.
     * Primary services can be discovered using Primary Service Discovery procedures.
     * A secondary service is a service that should only be included from a primary service or another secondary service or other higher layer specification.
     * A secondary service is only relevant in the context of the entity that includes it.
     */
    enum class Type {
        PRIMARY,
        SECONDARY,
    }

    /**
     * The [Type] of the service
     */
    val type: Type

    /**
     * The list of [Service] that this service includes
     */
    val includedServices: List<Service>

    /**
     * The list of [Characteristic] this service supports
     */
    val characteristics: List<Characteristic>
}
