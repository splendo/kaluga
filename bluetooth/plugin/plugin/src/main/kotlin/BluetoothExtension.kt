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

/**
 * The roles to generate from the annotated `@Bluetooth` definitions.
 */
enum class BluetoothTarget {
    /** Generate a client to connect to and interact with the device as a central. */
    CLIENT,

    /** Generate a server to host the device as a peripheral. */
    SERVER,
}

/**
 * The implementations to generate for each [BluetoothTarget].
 */
enum class ImplementFor {
    /** Generate the implementation backed by the platform Bluetooth stack (`bluetooth-client` / `bluetooth-server`). */
    BLUETOOTH,

    /** Generate an in-process simulated implementation, where a simulated client talks directly to a simulated server. */
    SIMULATOR,
}

/**
 * Configures the `com.splendo.kaluga.bluetooth.plugin` code generation through the `bluetooth { }` DSL.
 */
open class BluetoothExtension(private val kspExtension: KspExtension, objects: ObjectFactory) {

    /**
     * The roles to generate. Defaults to [BluetoothTarget.CLIENT] only.
     */
    val target = objects.setProperty<BluetoothTarget>().apply {
        add(BluetoothTarget.CLIENT)
    }

    /**
     * The implementations to generate for each [target]. Defaults to [ImplementFor.BLUETOOTH] only;
     * set to empty (or call [apiOnly]) to generate only the API interfaces.
     */
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

    /** Configuration for generating `@Bluetooth` definitions from Bluetooth SIG GATT XML; see [generateFromXml]. */
    class XmlGeneration(val deviceName: String, val sourceDirectories: Set<String>, val packageName: String?, val outputDirectory: String?)

    /** Configuration for generating `@Bluetooth` definitions from a Kaluga GATT device YAML; see [generateFromYaml]. */
    class YamlGeneration(val file: String, val packageName: String?, val outputDirectory: String?)

    internal var xmlGeneration: XmlGeneration? = null
        private set

    internal var yamlGeneration: YamlGeneration? = null
        private set

    /**
     * Generates the `@Bluetooth` device, services and characteristics (with `@Serializable` value classes) for
     * [deviceName] from the Bluetooth SIG GATT characteristic and service XML found under [sourceDirectories].
     * Conditional characteristics (whose value varies by a leading discriminator byte) map to sealed classes. The
     * generated definitions then feed the normal code generation. [packageName] defaults to [generatedPackage].
     *
     * By default the definitions are (re)generated into the build directory on every build. Set [outputDirectory] to a
     * dedicated, project-relative source directory to emit them there instead: one module can then generate (and commit)
     * the definitions and other modules consume them via [annotationSource] without re-running the XML generation. The
     * output directory is managed by the generator and cleared on each run, so it must not hold hand-written sources.
     *
     * The generated value classes are `@Serializable`, so the module must apply the Kotlin serialization plugin
     * (`org.jetbrains.kotlin.plugin.serialization`).
     */
    fun generateFromXml(deviceName: String, vararg sourceDirectories: String, packageName: String? = null, outputDirectory: String? = null) {
        check(yamlGeneration == null) { "Configure either generateFromXml or generateFromYaml, not both." }
        xmlGeneration = XmlGeneration(deviceName, sourceDirectories.toSet(), packageName, outputDirectory)
    }

    /**
     * Generates the `@Bluetooth` device, services and characteristics (with `@Serializable` value classes) from the
     * Kaluga GATT device YAML at [file]. A single file describes the whole device — its services (with per-characteristic
     * access) and characteristics (fields, scaling, and conditional variants, the last mapping to sealed classes). The
     * device name is taken from the YAML. [packageName] defaults to [generatedPackage]; [outputDirectory] behaves as in
     * [generateFromXml].
     *
     * The generated value classes are `@Serializable`, so the module must apply the Kotlin serialization plugin
     * (`org.jetbrains.kotlin.plugin.serialization`).
     */
    fun generateFromYaml(file: String, packageName: String? = null, outputDirectory: String? = null) {
        check(xmlGeneration == null) { "Configure either generateFromXml or generateFromYaml, not both." }
        yamlGeneration = YamlGeneration(file, packageName, outputDirectory)
    }

    /** Forwards this configuration to KSP as processor options. Invoked by [BluetoothPlugin]; not intended to be called directly. */
    fun afterEvaluate() {
        kspExtension.arg("target", target.get().joinToString(separator = ",") { it.name })
        kspExtension.arg("implementFor", implementFor.get().joinToString(separator = ",") { it.name })
        kspExtension.arg("generateApi", "$generateApi")
        generatedPackage?.let { kspExtension.arg("generatedPackage", it) }
        apiPackage?.let { kspExtension.arg("apiPackage", it) }
    }
}
