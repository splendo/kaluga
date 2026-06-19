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
 * A characteristic is either a single structure ([fields], optionally with values carried in a leading flags byte
 * via [flagFields]) or, when its value varies by a leading discriminator, a set of [variants] (a sealed class).
 */
data class GattCharacteristic(
    val name: String,
    val uuid: String,
    val fields: List<GattField>,
    val variants: List<GattVariant> = emptyList(),
    val flagFields: List<GattFlagField> = emptyList(),
) {
    val isVariant: Boolean get() = variants.isNotEmpty()
}

/** One variant of a conditional [GattCharacteristic], selected on the wire by its [discriminator] byte. */
data class GattVariant(val name: String, val discriminator: Int, val fields: List<GattField>)

/**
 * An enumerated value carried inside the leading flags byte, occupying [size] bits at bit [index] (e.g. a status
 * field packed into the flags). Generated as an enum stored in the flags region. [cases] are ordered by [GattFlagCase.key].
 */
data class GattFlagField(val name: String, val index: Int, val size: Int, val cases: List<GattFlagCase>, val description: String? = null)

/** One case of a [GattFlagField], stored on the wire as [key]. [description] is the spec text, surfaced as KDoc. */
data class GattFlagCase(val key: Int, val description: String?)

/**
 * A single value field of a [GattCharacteristic]. [format] is the raw GATT format token (e.g. `uint16`, `sint8`,
 * `SFLOAT`, `utf8s`); [multiplier]/[decimalExponent]/[binaryExponent] capture the field's scaling.
 *
 * [alternateFormats] holds additional widths when a flags bit selects the format (e.g. uint8/uint16), generated as
 * multiple `@Size`. [optional] marks a field whose presence is gated by a flags bit (generated as nullable). [repeated]
 * is the SIG `<Repeated>` flag: the field has no length and fills the rest of the packet, so it generates as an unsized
 * list and must be the last field. [flagIndex] is the bit driving that selection/presence. [description] is the spec's
 * free text, surfaced as KDoc.
 */
data class GattField(
    val name: String,
    val format: String,
    val multiplier: Int = 1,
    val decimalExponent: Int = 0,
    val binaryExponent: Int = 0,
    val alternateFormats: List<String> = emptyList(),
    val optional: Boolean = false,
    val repeated: Boolean = false,
    val flagIndex: Int? = null,
    val description: String? = null,
)

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
