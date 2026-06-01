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
 * A Characteristic is an [Attribute] that contains the value of a single piece of data and any required descriptors that describe the value.
 * It is a value used in a service along with properties and configuration information about how the value is accessed and information about how the value is displayed or represented.
 * A Characteristic may contain one or more [Descriptor].
 */
interface Characteristic : Attribute {

    /**
     * The [Service] this characteristic belongs to
     */
    val service: Service

    /**
     * The set of [CharacteristicProperty] of this characteristic.
     */
    val properties: Set<CharacteristicProperty>

    /**
     * The list of [Descriptor] available for this characteristic
     */
    val descriptors: List<Descriptor>
}

/**
 * A CharacteristicProperty determines how a [Characteristic] Value can be used, or how the [Descriptor] can be accessed.
 * @param rawValue the raw value associated with the property
 * @property encryptedValue the value associated with the property when encryption is used
 */
sealed class CharacteristicProperty(val rawValue: Int, val encryptedValue: Int) {

    constructor(rawValue: Int) : this(rawValue, rawValue)

    companion object {

        private val allProperties = setOf(
            Broadcast,
            Read,
            Write,
            WriteWithoutResponse,
            SignedWrite,
            Notify,
            Indicate,
            ExtendedProperties,
        )

        /**
         * Gets a [Set] of [CharacteristicProperty] from an [Int]
         */
        fun fromInt(properties: Int): Set<CharacteristicProperty> = allProperties.filter {
            (properties and it.rawValue) != 0 || (properties and it.encryptedValue) != 0
        }.toSet()
    }

    /**
     * [Characteristic] is broadcastable
     */
    data object Broadcast : CharacteristicProperty(0x01)

    /**
     * [Characteristic] is readable
     */
    data object Read : CharacteristicProperty(0x02)

    sealed class Writable(rawValue: Int) : CharacteristicProperty(rawValue)

    /**
     * [Characteristic] can be written without response
     */
    data object WriteWithoutResponse : Writable(0x04)

    /**
     * [Characteristic] supports write with signature
     */
    data object SignedWrite : Writable(0x40)

    /**
     * [Characteristic] can be written
     */
    data object Write : Writable(0x08)

    sealed class Notifiable(rawValue: Int, encryptedValue: Int) : CharacteristicProperty(rawValue, encryptedValue)

    /**
     * [Characteristic] supports notification
     */
    data object Notify : Notifiable(0x10, 256)

    /**
     * [Characteristic] supports indication
     */
    data object Indicate : Notifiable(0x20, 512)

    /**
     * [Characteristic] has extended properties
     */
    data object ExtendedProperties : CharacteristicProperty(0x80)
}

/**
 * Gets the Raw Int from a set of [CharacteristicProperty]
 * @param encrypted if `true` the [CharacteristicProperty.encryptedValue] value will be used, otherwise [CharacteristicProperty.rawValue]
 */
fun Set<CharacteristicProperty>.rawValue(encrypted: Boolean): Int = fold(0) { acc, characteristicProperty ->
    if (encrypted) {
        acc or characteristicProperty.encryptedValue
    } else {
        acc or characteristicProperty.rawValue
    }
}
