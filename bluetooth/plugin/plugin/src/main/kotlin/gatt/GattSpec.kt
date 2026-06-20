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
 * A characteristic is a single value structure ([fields]), optionally with enumerated values carried in a leading
 * flags byte via [flagFields].
 */
data class GattCharacteristic(
    val name: String,
    val uuid: String,
    val fields: List<GattField>,
    val flagFields: List<GattFlagField> = emptyList(),
    val descriptors: List<GattDescriptor> = emptyList(),
    // The SIG type identifier (e.g. `org.bluetooth.characteristic.heart_rate_measurement`); services reference
    // characteristics by this when they carry no UUID.
    val type: String? = null,
)

/**
 * A GATT descriptor of a [GattCharacteristic], generated as a nested `@BluetoothDescriptor` interface. Its [uuid] and
 * value structure ([fields], optionally [flagFields]) come from the descriptor's own type XML ([GattDescriptorDefinition]);
 * [properties] are the access granted by the service's [GattDescriptorReference].
 */
data class GattDescriptor(
    val name: String,
    val uuid: String,
    val properties: Set<GattProperty>,
    val fields: List<GattField> = emptyList(),
    val flagFields: List<GattFlagField> = emptyList(),
)

/**
 * A standalone descriptor definition parsed from a descriptor's own type XML (root `<Descriptor>`): the source of a
 * descriptor's [uuid] and value structure ([fields]/[flagFields]), resolved against a service's [GattDescriptorReference]s
 * by [type]. The access a descriptor grants is not declared here but on each referencing service.
 */
data class GattDescriptorDefinition(
    val type: String,
    val uuid: String,
    val name: String,
    val fields: List<GattField> = emptyList(),
    val flagFields: List<GattFlagField> = emptyList(),
)

/**
 * A descriptor as referenced by a [GattServiceCharacteristic]: named by [type], granting access [properties]. Resolved
 * against the parsed [GattDescriptorDefinition]s to obtain its UUID and value structure.
 */
data class GattDescriptorReference(val type: String, val name: String, val properties: Set<GattProperty>)

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
    val unit: String? = null,
    val description: String? = null,
    // The UUID of another characteristic whose value structure this field embeds (the SIG `<Reference>`); when set,
    // [format] is empty and the field is typed as the referenced characteristic's generated value class.
    val reference: String? = null,
    // The flag bits whose conjunction gates this (optional) field's presence, for a compound `<Requirement>` that
    // resolves to more than one bit. When set, the field is nullable and generated with `@PresentWhenAllSet` instead of
    // a single `@FlagIndex`.
    val presenceFlagIndices: List<Int> = emptyList(),
    // A field whose `<Format>` carries top-level `<Enumerations>` (not a `<BitField>`): an enumerated byte value,
    // generated as a nested enum with a `@SerializedByteValue` per case.
    val enumCases: List<GattFlagCase> = emptyList(),
)

/**
 * A parsed GATT service definition: the characteristics it contains and how each may be accessed.
 */
data class GattService(
    val name: String,
    val uuid: String,
    val characteristics: List<GattServiceCharacteristic>,
)

/**
 * A characteristic as referenced by a [GattService], together with the access [properties] the service grants it and any
 * [descriptorReferences] it declares. The reference is by [uuid] when present, otherwise by SIG [type] (the SIG service
 * XML references characteristics by type).
 */
data class GattServiceCharacteristic(
    val uuid: String,
    val properties: Set<GattProperty>,
    val type: String? = null,
    val descriptorReferences: List<GattDescriptorReference> = emptyList(),
) {
    /** The characteristic this reference resolves to in [byKey] (keyed by both UUID and type), or null if unknown. */
    fun resolve(byKey: Map<String, GattCharacteristic>): GattCharacteristic? = byKey[uuid] ?: type?.let { byKey[it] }
}

/** A GATT characteristic access property, mapping onto the Kaluga access annotations. */
enum class GattProperty { READ, WRITE, WRITE_WITHOUT_RESPONSE, SIGNED_WRITE, NOTIFY, INDICATE }

internal fun String.toPascalCase(): String = split(Regex("[^A-Za-z0-9]+"))
    .filter { it.isNotEmpty() }
    .joinToString("") { part -> part.replaceFirstChar { it.uppercaseChar() } }

internal fun String.toCamelCase(): String = toPascalCase().replaceFirstChar { it.lowercaseChar() }
