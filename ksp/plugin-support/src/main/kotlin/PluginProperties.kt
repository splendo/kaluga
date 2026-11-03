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

package com.splendo.kaluga.ksp.plugin

import java.util.Properties

/**
 * The properties a plugin's build bundles into its jar as [resource], such as the Kaluga version whose artifacts the
 * plugin adds. [owner] is a class of that plugin, whose class loader holds the resource.
 */
class PluginProperties(private val owner: Class<*>, private val resource: String) {

    private val properties: Properties by lazy {
        owner.classLoader
            .getResourceAsStream(resource)
            ?.use { Properties().apply { load(it) } }
            ?: error("Plugin properties $resource not found")
    }

    /** The value of [key]; fails when the resource does not define it. */
    operator fun get(key: String): String = properties.getProperty(key) ?: error("$key not found in $resource")
}
