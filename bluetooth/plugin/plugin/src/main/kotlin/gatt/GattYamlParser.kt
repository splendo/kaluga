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

import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.InputStream

/**
 * Parses a Kaluga GATT device YAML into the same model the XML parser produces. A single file describes the whole
 * device: its services (with per-characteristic access) and its characteristics (fields, scaling, and conditional
 * variants). Standard UUIDs/names can be taken from the Bluetooth SIG `assigned_numbers` YAML; the field layout that
 * the SIG no longer publishes machine-readably lives here.
 */
object GattYamlParser {

    /** Parses the device described by [file]. */
    fun parse(file: File): GattDevice = file.inputStream().use(::parse)

    /** Parses the device described by [input]. */
    fun parse(input: InputStream): GattDevice {
        val root = Yaml().load<Map<String, Any?>>(input) ?: error("Empty GATT YAML")
        return GattDevice(
            name = root.string("device") ?: error("GATT YAML is missing the 'device' name"),
            services = root.list("services").map { it.asMap().toService() },
            characteristics = root.list("characteristics").map { it.asMap().toCharacteristic() },
        )
    }

    private fun Map<String, Any?>.toService() = GattService(
        name = string("name").orEmpty(),
        uuid = string("uuid").orEmpty(),
        characteristics = list("characteristics").map { it.asMap().toServiceCharacteristic() },
    )

    private fun Map<String, Any?>.toServiceCharacteristic() = GattServiceCharacteristic(
        uuid = string("uuid").orEmpty(),
        properties = list("access").map { gattProperty(it.toString()) }.toSet(),
    )

    private fun Map<String, Any?>.toCharacteristic(): GattCharacteristic {
        val variants = list("variants").map { it.asMap().toVariant() }
        return GattCharacteristic(
            name = string("name").orEmpty(),
            uuid = string("uuid").orEmpty(),
            fields = if (variants.isEmpty()) list("fields").map { it.asMap().toField() } else emptyList(),
            variants = variants,
        )
    }

    private fun Map<String, Any?>.toVariant() = GattVariant(
        name = string("name").orEmpty(),
        discriminator = int("value", 0),
        fields = list("fields").map { it.asMap().toField() },
    )

    private fun Map<String, Any?>.toField() = GattField(
        name = string("name").orEmpty(),
        format = string("format").orEmpty(),
        multiplier = int("multiplier", 1),
        decimalExponent = int("decimalExponent", 0),
        binaryExponent = int("binaryExponent", 0),
    )

    private fun gattProperty(token: String): GattProperty = when (token.lowercase().filter { it.isLetterOrDigit() }) {
        "read" -> GattProperty.READ
        "write" -> GattProperty.WRITE
        "writewithoutresponse" -> GattProperty.WRITE_WITHOUT_RESPONSE
        "notify" -> GattProperty.NOTIFY
        "indicate" -> GattProperty.INDICATE
        else -> error("Unknown access property '$token'")
    }

    private fun Map<String, Any?>.string(key: String): String? = this[key]?.toString()

    private fun Map<String, Any?>.int(key: String, default: Int): Int = (this[key] as? Number)?.toInt() ?: default

    private fun Map<String, Any?>.list(key: String): List<Any?> = (this[key] as? List<*>).orEmpty()

    private fun Any?.asMap(): Map<String, Any?> = (this as? Map<*, *>)?.entries?.associate { (key, value) -> key.toString() to value }
        ?: error("Expected a YAML mapping, but was $this")
}
