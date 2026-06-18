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

package com.splendo.kaluga.bluetooth.plugin.gatt

/**
 * A parsed Bluetooth SIG GATT characteristic definition, limited to what the prototype generator consumes.
 * A characteristic is either a single structure ([fields]) or, when its value varies by a leading discriminator,
 * a set of [variants] (generated as a sealed class).
 */
data class GattCharacteristic(val name: String, val uuid: String, val fields: List<GattField>, val variants: List<GattVariant> = emptyList()) {
    val isVariant: Boolean get() = variants.isNotEmpty()
}

/** One variant of a conditional [GattCharacteristic], selected on the wire by its [discriminator] byte. */
data class GattVariant(val name: String, val discriminator: Int, val fields: List<GattField>)

/**
 * A single value field of a [GattCharacteristic]. [format] is the raw GATT format token (e.g. `uint16`, `sint8`,
 * `SFLOAT`, `utf8s`); [multiplier]/[decimalExponent]/[binaryExponent] capture the field's scaling.
 */
data class GattField(val name: String, val format: String, val multiplier: Int = 1, val decimalExponent: Int = 0, val binaryExponent: Int = 0)

/** A whole device: its [name] and the [services] and [characteristics] it exposes, as parsed from a single source. */
data class GattDevice(val name: String, val services: List<GattService>, val characteristics: List<GattCharacteristic>)

/** A parsed GATT service definition: the characteristics it contains and how each may be accessed. */
data class GattService(val name: String, val uuid: String, val characteristics: List<GattServiceCharacteristic>)

/** A characteristic as referenced by a [GattService], by [uuid], together with the access [properties] the service grants it. */
data class GattServiceCharacteristic(val uuid: String, val properties: Set<GattProperty>)

/** A GATT characteristic access property, mapping onto the Kaluga access annotations. */
enum class GattProperty { READ, WRITE, WRITE_WITHOUT_RESPONSE, NOTIFY, INDICATE }

internal fun String.toPascalCase(): String = split(Regex("[^A-Za-z0-9]+"))
    .filter { it.isNotEmpty() }
    .joinToString("") { part -> part.replaceFirstChar { it.uppercaseChar() } }

internal fun String.toCamelCase(): String = toPascalCase().replaceFirstChar { it.lowercaseChar() }
