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
import java.io.InputStream

/**
 * Resolves standard Bluetooth SIG service and characteristic UUIDs by name (or `org.bluetooth.*` id), so a GATT YAML
 * can reference standard attributes by name instead of repeating their UUIDs. Backed by a bundled curated subset of
 * common assignments; the same loader reads the Bluetooth SIG `assigned_numbers` UUID tables verbatim.
 */
object GattUuids {

    private val services by lazy { loadResource("/gatt/assigned_numbers/service_uuids.yaml") }
    private val characteristics by lazy { loadResource("/gatt/assigned_numbers/characteristic_uuids.yaml") }

    /** The short UUID for the standard service [name] (or its `org.bluetooth.service.*` id), or null if unknown. */
    fun service(name: String): String? = services[normalize(name)]

    /** The short UUID for the standard characteristic [name] (or its `org.bluetooth.characteristic.*` id), or null if unknown. */
    fun characteristic(name: String): String? = characteristics[normalize(name)]

    /** Normalises a UUID literal — a YAML hex int such as `0x2A6E` or a string — to the short hex form `2A6E`. */
    fun shortUuid(value: Any?): String = when (value) {
        is Number -> value.toInt().toString(16).uppercase().padStart(4, '0')
        else -> value.toString().removePrefix("0x").removePrefix("0X").uppercase()
    }

    private fun loadResource(path: String): Map<String, String> = checkNotNull(javaClass.getResourceAsStream(path)) { "missing assigned-numbers resource $path" }.use(::load)

    private fun load(input: InputStream): Map<String, String> {
        val root = Yaml().load<Map<String, Any?>>(input) ?: return emptyMap()
        val byKey = HashMap<String, String>()
        (root["uuids"] as? List<*>).orEmpty().filterIsInstance<Map<*, *>>().forEach { entry ->
            val uuid = shortUuid(entry["uuid"])
            entry["name"]?.let { byKey[normalize(it.toString())] = uuid }
            entry["id"]?.let { byKey[normalize(it.toString())] = uuid }
        }
        return byKey
    }

    private fun normalize(value: String): String = value.lowercase().filter { it.isLetterOrDigit() }
}
