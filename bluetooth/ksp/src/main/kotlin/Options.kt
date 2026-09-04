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

package com.splendo.kaluga.bluetooth.ksp

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment

data class Options(
    val generateClient: Boolean,
    val generateServer: Boolean,
    val generateApi: Boolean,
    val generateBluetoothImplementation: Boolean,
    val generateSimulatorImplementation: Boolean,
    val generateMockImplementation: Boolean,
    val generatedPackage: String?,
    val apiPackage: String?,
) {
    constructor(environment: SymbolProcessorEnvironment) : this(
        environment.options["target"].orEmpty().split(",").contains("CLIENT"),
        environment.options["target"].orEmpty().split(",").contains("SERVER"),
        environment.options["generateApi"] != "false",
        environment.options["implementFor"].orEmpty().split(",").contains("BLUETOOTH"),
        environment.options["implementFor"].orEmpty().split(",").contains("SIMULATOR"),
        environment.options["implementFor"].orEmpty().split(",").contains("MOCK"),
        environment.options["generatedPackage"]?.takeIf { it.isNotBlank() },
        environment.options["apiPackage"]?.takeIf { it.isNotBlank() },
    )

    /** The package in which generated code is placed, falling back to [defaultPackage] when not configured. */
    fun generatedPackage(defaultPackage: String): String = generatedPackage ?: defaultPackage

    /**
     * The package in which the API interfaces live, falling back to [generatedPackage].
     *
     * The configured override only applies to declarations that belong to this module (i.e. whose
     * package is related to the configured package). External service declarations — e.g. a battery
     * service pulled in from another module — live in their own package and must not be remapped,
     * otherwise KotlinPoet treats them as same-package and omits the import, causing an unresolved
     * reference at compile time.
     */
    fun apiPackage(defaultPackage: String): String {
        val configured = apiPackage ?: return generatedPackage(defaultPackage)
        // If the configured package and the declaration's package are related (one is a prefix of the
        // other), this declaration belongs to the same module → apply the override.
        // Otherwise it is an external declaration → keep its own package.
        return if (defaultPackage.startsWith(configured) || configured.startsWith(defaultPackage)) {
            configured
        } else {
            defaultPackage
        }
    }

    /**
     * Prefix for implementation file names when [generateApi] is false.
     * Prevents dex duplicate conflicts when multiple impl modules (e.g. bluetooth + simulator)
     * are in the same Android dependency graph — each gets its own JVM class name.
     */
    val implementationFilePrefix: String get() = when {
        generateBluetoothImplementation -> "Bluetooth"
        generateSimulatorImplementation -> "Simulated"
        generateMockImplementation -> "Mock"
        else -> ""
    }
}
