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

    /** The package in which the API interfaces live, falling back to [generatedPackage]. */
    fun apiPackage(defaultPackage: String): String = apiPackage ?: generatedPackage(defaultPackage)
}
