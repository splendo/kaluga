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

/** A parsed GATT definition: either a [GattCharacteristic] (value structure) or a [GattService] (structure + access). */
sealed interface GattDefinition {
    data class Characteristic(val value: GattCharacteristic) : GattDefinition
    data class Service(val value: GattService) : GattDefinition
}

/** Parses Bluetooth SIG GATT characteristic and service XML. Prototype: plain fields and flat services only. */
object GattXmlParser {

    /** Parses [file], dispatching on its root element to a characteristic or service definition. */
    fun parse(file: File): GattDefinition = file.inputStream().use { input ->
        val root = document(input)
        when (root.tagName) {
            "Characteristic" -> GattDefinition.Characteristic(parseCharacteristic(root))
            "Service" -> GattDefinition.Service(parseService(root))
            else -> error("Unsupported root <${root.tagName}> in ${file.name}")
        }
    }

    fun parseCharacteristic(input: InputStream): GattCharacteristic = parseCharacteristic(document(input))

    fun parseService(input: InputStream): GattService = parseService(document(input))

    private fun parseCharacteristic(root: Element): GattCharacteristic {
        require(root.tagName == "Characteristic") { "Expected a <Characteristic> root, but was <${root.tagName}>" }
        val value = root.children("Value").firstOrNull()
        val variantsElement = value?.directChildren("Variants")?.firstOrNull()
        val variants = variantsElement?.directChildren("Variant")?.map { variant ->
            GattVariant(
                name = variant.getAttribute("name"),
                discriminator = variant.getAttribute("value").toInt(),
                fields = fieldsOf(variant),
            )
        }.orEmpty()
        return GattCharacteristic(
            name = root.getAttribute("name").ifBlank { root.getAttribute("type") },
            uuid = root.getAttribute("uuid"),
            fields = if (variantsElement != null || value == null) emptyList() else fieldsOf(value),
            variants = variants,
        )
    }

    private fun fieldsOf(parent: Element): List<GattField> = parent.directChildren("Field").map { field ->
        GattField(
            name = field.getAttribute("name"),
            format = field.childText("Format").orEmpty(),
            multiplier = field.childText("Multiplier")?.toIntOrNull() ?: 1,
            decimalExponent = field.childText("DecimalExponent")?.toIntOrNull() ?: 0,
            binaryExponent = field.childText("BinaryExponent")?.toIntOrNull() ?: 0,
        )
    }

    private fun parseService(root: Element): GattService {
        require(root.tagName == "Service") { "Expected a <Service> root, but was <${root.tagName}>" }
        val characteristics = root.children("Characteristics").firstOrNull()
            ?.children("Characteristic")
            .orEmpty()
            .map { characteristic ->
                val properties = characteristic.children("Properties").firstOrNull()
                val granted = GattProperty.entries.filter { property ->
                    properties?.childText(property.elementName)?.let { it != "Excluded" } == true
                }.toSet()
                GattServiceCharacteristic(uuid = characteristic.getAttribute("uuid"), properties = granted)
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
