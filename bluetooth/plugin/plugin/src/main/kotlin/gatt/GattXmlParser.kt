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
import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory

/** Parses a Bluetooth SIG GATT characteristic XML into a [GattCharacteristic]. Prototype: plain fields only. */
object GattXmlParser {

    fun parse(input: InputStream): GattCharacteristic {
        val root = DocumentBuilderFactory.newInstance()
            .apply { isNamespaceAware = false }
            .newDocumentBuilder()
            .parse(input)
            .documentElement
        require(root.tagName == "Characteristic") { "Expected a <Characteristic> root, but was <${root.tagName}>" }

        val fields = root.children("Value").firstOrNull()
            ?.children("Field")
            .orEmpty()
            .map { field ->
                GattField(
                    name = field.getAttribute("name"),
                    format = field.childText("Format").orEmpty(),
                    multiplier = field.childText("Multiplier")?.toIntOrNull() ?: 1,
                    decimalExponent = field.childText("DecimalExponent")?.toIntOrNull() ?: 0,
                    binaryExponent = field.childText("BinaryExponent")?.toIntOrNull() ?: 0,
                )
            }
        return GattCharacteristic(
            name = root.getAttribute("name").ifBlank { root.getAttribute("type") },
            uuid = root.getAttribute("uuid"),
            fields = fields,
        )
    }

    private fun Element.children(tag: String): List<Element> {
        val nodes = getElementsByTagName(tag)
        return (0 until nodes.length).map { nodes.item(it) as Element }
    }

    private fun Element.childText(tag: String): String? = children(tag).firstOrNull { it.parentNode === this }?.textContent?.trim()?.takeIf { it.isNotEmpty() }
}
