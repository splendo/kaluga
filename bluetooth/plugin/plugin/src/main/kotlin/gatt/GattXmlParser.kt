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

import org.w3c.dom.Element
import java.io.File
import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory

/** A parsed GATT definition: a [GattCharacteristic] value structure, a [GattService], or a standalone descriptor
 *  definition (a [GattDescriptorDefinition] parsed from a descriptor's own type XML). */
sealed interface GattDefinition {
    data class Characteristic(val value: GattCharacteristic) : GattDefinition
    data class Service(val value: GattService) : GattDefinition
    data class Descriptor(val value: GattDescriptorDefinition) : GattDefinition
}

/** Parses Bluetooth SIG GATT characteristic, service and descriptor XML. Prototype: plain fields and flat services only. */
object GattXmlParser {

    /** Parses [file], dispatching on its root element to a characteristic, service or descriptor definition. */
    fun parse(file: File): GattDefinition = file.inputStream().use { input ->
        val root = document(input)
        when (root.tagName) {
            "Characteristic" -> GattDefinition.Characteristic(parseCharacteristic(root))
            "Service" -> GattDefinition.Service(parseService(root))
            "Descriptor" -> GattDefinition.Descriptor(parseDescriptor(root))
            else -> error("Unsupported root <${root.tagName}> in ${file.name}")
        }
    }

    fun parseCharacteristic(input: InputStream): GattCharacteristic = parseCharacteristic(document(input))

    fun parseService(input: InputStream): GattService = parseService(document(input))

    fun parseDescriptor(input: InputStream): GattDescriptorDefinition = parseDescriptor(document(input))

    private fun parseCharacteristic(root: Element): GattCharacteristic {
        require(root.tagName == "Characteristic") { "Expected a <Characteristic> root, but was <${root.tagName}>" }
        val name = root.getAttribute("name").ifBlank { root.getAttribute("type") }
        val value = parseValue(name, root.directChildren("Value").firstOrNull())
        return GattCharacteristic(
            name = name,
            uuid = root.getAttribute("uuid"),
            fields = value.fields,
            flagFields = value.flagFields,
            type = root.getAttribute("type").ifBlank { null },
        )
    }

    // A descriptor's own type XML (root <Descriptor>): the source of its UUID and value structure. The access it grants
    // is declared not here but on each service's <Descriptor> reference, so it is supplied during generation.
    private fun parseDescriptor(root: Element): GattDescriptorDefinition {
        require(root.tagName == "Descriptor") { "Expected a <Descriptor> root, but was <${root.tagName}>" }
        val name = root.getAttribute("name").ifBlank { root.getAttribute("type") }
        val value = parseValue(name, root.directChildren("Value").firstOrNull())
        return GattDescriptorDefinition(
            type = root.getAttribute("type"),
            uuid = root.getAttribute("uuid"),
            name = name,
            fields = value.fields,
            flagFields = value.flagFields,
        )
    }

    private class ParsedValue(val fields: List<GattField>, val flagFields: List<GattFlagField>)

    // Parses a <Value> into its field structure: a flat field list, or — when a leading <Field> carries a <BitField> —
    // the flags-resolved fields plus the enums packed in the flags region. Shared by characteristics and descriptors,
    // whose value structures are identical.
    private fun parseValue(name: String, value: Element?): ParsedValue {
        if (value == null) return ParsedValue(emptyList(), emptyList())
        val fieldElements = value.directChildren("Field")
        val flagElements = fieldElements.filter { it.directChildren("BitField").isNotEmpty() }
        return if (flagElements.isEmpty()) {
            val fields = fieldElements.map { it.toField() }
            requireTrailingRepeated(name, fields)
            ParsedValue(fields, emptyList())
        } else {
            resolveConditional(name, flagElements, fieldElements - flagElements)
        }
    }

    // The <Descriptor> references declared by a service's <Characteristic>: each names a descriptor by type and grants
    // access. Its UUID and value structure come from the descriptor's own type XML, resolved during generation.
    private fun descriptorReferencesOf(reference: Element): List<GattDescriptorReference> =
        reference.directChildren("Descriptors").firstOrNull()?.directChildren("Descriptor").orEmpty().map { descriptor ->
            GattDescriptorReference(
                type = descriptor.getAttribute("type"),
                name = descriptor.getAttribute("name").ifBlank { descriptor.getAttribute("type") },
                properties = grantedProperties(descriptor.directChildren("Properties").firstOrNull()),
            )
        }

    /** The access [GattProperty] set granted by a `<Properties>` element (anything not `Excluded`). */
    private fun grantedProperties(properties: Element?): Set<GattProperty> =
        GattProperty.entries.filter { property -> properties?.childText(property.elementName)?.let { it != "Excluded" } == true }.toSet()

    private fun Element.toField(): GattField = GattField(
        name = getAttribute("name"),
        format = childText("Format").orEmpty(),
        multiplier = childText("Multiplier")?.toIntOrNull() ?: 1,
        decimalExponent = childText("DecimalExponent")?.toIntOrNull() ?: 0,
        binaryExponent = childText("BinaryExponent")?.toIntOrNull() ?: 0,
        // The SIG `<Repeated>` element: a field with no length, so it repeats to the end of the packet.
        repeated = childText("Repeated").toBoolean(),
        unit = childText("Unit"),
        description = fieldDescription(),
        // A `<Reference>` to another characteristic by UUID: the field embeds that characteristic's value structure.
        reference = childText("Reference"),
        // Top-level `<Enumerations>` (not in a `<BitField>`): an enumerated byte value, e.g. Body Sensor Location.
        enumCases = directChildren("Enumerations").firstOrNull()?.directChildren("Enumeration").orEmpty().map { e ->
            GattFlagCase(e.getAttribute("key").toInt(), e.getAttribute("value").ifBlank { null })
        },
    )

    // A repeated field carries no length and consumes the remainder, so it can only be the last field.
    private fun requireTrailingRepeated(name: String, fields: List<GattField>) {
        val index = fields.indexOfFirst { it.repeated }
        require(index < 0 || index == fields.lastIndex) {
            "Characteristic '$name' marks a non-trailing field as <Repeated>; a repeated field has no length and must be last."
        }
    }

    /** The spec's free text for a field — informative text, description and unit — joined for use as KDoc. */
    private fun Element.fieldDescription(): String? = listOfNotNull(
        childText("InformativeText"),
        childText("Description"),
        childText("Unit")?.let { "Unit: $it" },
    ).joinToString("\n").takeIf { it.isNotEmpty() }

    private class Bit(val index: Int, val size: Int, val name: String, val enumerations: List<Enumeration>)
    private class Enumeration(val key: Int, val value: String, val requires: String?)

    // One or more leading Flags fields carry a <BitField>: each bit either selects a field's format (every enumeration
    // `requires` one), gates a field's presence (some enumerations `requires`), or carries an enumerated value (no
    // `requires`). Conditional fields reference a bit through their <Requirement>. When several Flags fields are present
    // (e.g. two bytes, or a 16-bit field), each contributes its bits offset by the declared width of those before it,
    // so the bit indices span the whole multi-byte flags region.
    private fun resolveConditional(name: String, flagFields: List<Element>, valueFields: List<Element>): ParsedValue {
        var bitOffset = 0
        val bits = flagFields.flatMap { flagField ->
            val fieldBits = flagField.directChildren("BitField").first().directChildren("Bit").map { bit ->
                Bit(
                    index = bitOffset + bit.getAttribute("index").toInt(),
                    size = bit.getAttribute("size").toIntOrNull() ?: 1,
                    name = bit.getAttribute("name"),
                    enumerations = bit.directChildren("Enumerations").firstOrNull()?.directChildren("Enumeration").orEmpty().map { e ->
                        Enumeration(e.getAttribute("key").toInt(), e.getAttribute("value"), e.getAttribute("requires").ifBlank { null })
                    },
                )
            }
            bitOffset += formatWidth(flagField.childText("Format").orEmpty()).takeIf { it > 0 } ?: Byte.SIZE_BITS
            fieldBits
        }
        val bitByCondition = bits.flatMap { bit -> bit.enumerations.mapNotNull { it.requires?.to(bit) } }.toMap()

        // A bit whose value is not a gate (no `requires`) carries a value packed in the flags region: an enum when it
        // lists enumerations, otherwise a raw unsigned integer of its width (a sub-byte numeric subfield).
        val packedFlagFields = bits.filter { bit -> bit.enumerations.all { it.requires == null } }.map { bit ->
            GattFlagField(
                name = bit.name.removeSuffix("bits").removeSuffix("bit").trim().ifBlank { "Flag${bit.index}" },
                index = bit.index,
                size = bit.size,
                cases = bit.enumerations.map { GattFlagCase(it.key, it.value.ifBlank { null }) },
                description = bit.name.ifBlank { null },
            )
        }

        val consumed = mutableSetOf<Element>()
        val fields = mutableListOf<GattField>()
        valueFields.forEach { field ->
            if (field in consumed) return@forEach
            val conditionBits = field.conditionBits(bitByCondition)
            when {
                conditionBits.isEmpty() -> fields += field.toField()

                // A compound condition over several bits: the field is present only when all of them are set.
                conditionBits.size > 1 -> {
                    consumed += field
                    val gated = field.toField()
                    require(!gated.repeated) { "Characteristic '$name' field '${gated.name}' cannot be both <Repeated> and gated by a compound condition." }
                    fields += gated.copy(optional = true, presenceFlagIndices = conditionBits.map { it.index })
                }

                // Selector bit: every value gates a field -> the gated fields are width alternatives of one field,
                // distinguished only by a `(<format>)` suffix on the SIG name (e.g. `Value (uint8)`/`Value (uint16)`).
                conditionBits.single().enumerations.all { it.requires != null } -> {
                    val bit = conditionBits.single()
                    val group = valueFields.filter { it.conditionBits(bitByCondition).map(Bit::index) == listOf(bit.index) }
                    consumed += group
                    val baseNames = group.map { it.getAttribute("name").replace(Regex("\\s*\\(.*\\)\\s*$"), "") }.distinct()
                    require(baseNames.size == 1) {
                        "Characteristic '$name' selector bit ${bit.index} chooses between structurally different fields $baseNames; " +
                            "only width alternatives of a single field are supported."
                    }
                    val widest = group.maxBy { formatWidth(it.childText("Format").orEmpty()) }
                    fields += widest.toField().copy(
                        name = baseNames.single(),
                        alternateFormats = group.map { it.childText("Format").orEmpty() } - widest.childText("Format").orEmpty(),
                        flagIndex = bit.index,
                    )
                }

                // Presence gate: the field is driven by this bit. A `repeated` field fills the rest of the packet
                // (an unsized list, present when the bit is set); otherwise it is a single optional value.
                else -> {
                    consumed += field
                    val gated = field.toField()
                    fields += gated.copy(optional = !gated.repeated, flagIndex = conditionBits.single().index)
                }
            }
        }
        requireTrailingRepeated(name, fields)
        return ParsedValue(fields, packedFlagFields)
    }

    // The flag bits a field's <Requirement>s resolve to, supporting several <Requirement> elements and a single one
    // listing multiple conditions (e.g. "C1 and C2" or "C1,C2"). `Mandatory` and unknown tokens contribute nothing.
    private fun Element.conditionBits(bitByCondition: Map<String, Bit>): List<Bit> =
        directChildren("Requirement")
            .flatMap { it.textContent.trim().split(Regex("[,\\s]+")) }
            .mapNotNull { bitByCondition[it] }
            .distinctBy { it.index }

    // The fixed wire bit width of a `<Format>` token, looked up from the closed SIG format set ([GattFormat]); used to
    // lay out multi-`Field` flag regions and rank flag-selected width alternatives. Variable-length and unrecognised
    // formats have no fixed width here and yield 0 (so a digit inside e.g. `utf8s` is never mistaken for a width).
    private fun formatWidth(format: String): Int = GattFormat.of(format)?.bits ?: 0

    private fun parseService(root: Element): GattService {
        require(root.tagName == "Service") { "Expected a <Service> root, but was <${root.tagName}>" }
        val characteristics = root.directChildren("Characteristics").firstOrNull()
            ?.directChildren("Characteristic")
            .orEmpty()
            .map { characteristic ->
                GattServiceCharacteristic(
                    uuid = characteristic.getAttribute("uuid"),
                    type = characteristic.getAttribute("type").ifBlank { null },
                    properties = grantedProperties(characteristic.directChildren("Properties").firstOrNull()),
                    descriptorReferences = descriptorReferencesOf(characteristic),
                )
            }
        return GattService(
            name = root.getAttribute("name").ifBlank { root.getAttribute("type") },
            uuid = root.getAttribute("uuid"),
            characteristics = characteristics,
        )
    }

    private fun document(input: InputStream): Element = DocumentBuilderFactory.newInstance()
        .apply { isNamespaceAware = false }
        .newDocumentBuilder()
        .parse(input)
        .documentElement

    private val GattProperty.elementName: String
        get() = when (this) {
            GattProperty.READ -> "Read"
            GattProperty.WRITE -> "Write"
            GattProperty.WRITE_WITHOUT_RESPONSE -> "WriteWithoutResponse"
            GattProperty.SIGNED_WRITE -> "SignedWrite"
            GattProperty.NOTIFY -> "Notify"
            GattProperty.INDICATE -> "Indicate"
        }

    private fun Element.children(tag: String): List<Element> {
        val nodes = getElementsByTagName(tag)
        return (0 until nodes.length).map { nodes.item(it) as Element }
    }

    private fun Element.directChildren(tag: String): List<Element> = children(tag).filter { it.parentNode === this }

    private fun Element.childText(tag: String): String? = children(tag).firstOrNull { it.parentNode === this }?.textContent?.trim()?.takeIf { it.isNotEmpty() }
}
