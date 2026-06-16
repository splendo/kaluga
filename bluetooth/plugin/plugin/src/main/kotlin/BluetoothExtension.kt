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

package com.splendo.kaluga.bluetooth.plugin

import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.setProperty

enum class BluetoothTarget {
    CLIENT,
    SERVER,
}

enum class ImplementFor {
    BLUETOOTH,
    SIMULATOR,
}

open class BluetoothExtension(private val kspExtension: KspExtension, objects: ObjectFactory) {

    val target = objects.setProperty<BluetoothTarget>().apply {
        add(BluetoothTarget.CLIENT)
    }

    val implementFor = objects.setProperty<ImplementFor>().apply {
        add(ImplementFor.BLUETOOTH)
    }

    /**
     * Additional source directories holding the annotated Bluetooth definitions to generate from.
     * Use this to share one set of `@Bluetooth` definitions across modules (e.g. a client module and a
     * server-only module) without duplicating them: point each module at the same directory.
     */
    val annotationSourceDirectories = objects.setProperty<String>()

    /**
     * Adds a source directory holding annotated Bluetooth definitions to generate from.
     * @see annotationSourceDirectories
     */
    fun annotationSource(path: String) {
        annotationSourceDirectories.add(path)
    }

    /**
     * Generates only the API interfaces, without any Bluetooth or simulator implementation.
     * The resulting module depends only on `bluetooth-core`, so it can be used for previews, fakes
     * or mocks without pulling in the platform Bluetooth libraries.
     */
    fun apiOnly() {
        implementFor.set(emptySet())
    }

    private var generateApi = true

    /**
     * Generates the implementations only; the API interfaces are imported from another module that
     * generated them with [apiOnly]. That module must be added as a dependency.
     */
    fun useExternalApi() {
        generateApi = false
    }

    /**
     * The package the generated code is placed in. Defaults to the package of the annotated definitions.
     */
    var generatedPackage: String? = null

    /**
     * The package the generated API interfaces live in. Defaults to [generatedPackage]. Set this on an
     * implementation module (see [useExternalApi]) to point at the package of the module that generated the API.
     */
    var apiPackage: String? = null

    fun afterEvaluate() {
        kspExtension.arg("target", target.get().joinToString(separator = ",") { it.name })
        kspExtension.arg("implementFor", implementFor.get().joinToString(separator = ",") { it.name })
        kspExtension.arg("generateApi", "$generateApi")
        generatedPackage?.let { kspExtension.arg("generatedPackage", it) }
        apiPackage?.let { kspExtension.arg("apiPackage", it) }
    }
}
