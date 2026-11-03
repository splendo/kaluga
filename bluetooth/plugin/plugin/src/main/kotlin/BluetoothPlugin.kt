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

import com.google.devtools.ksp.gradle.KspAATask
import com.google.devtools.ksp.gradle.KspExtension
import com.google.devtools.ksp.gradle.KspGradleSubplugin
import com.splendo.kaluga.bluetooth.plugin.gatt.GattGeneration
import com.splendo.kaluga.ksp.plugin.PluginProperties
import com.splendo.kaluga.ksp.plugin.addKspProcessor
import com.splendo.kaluga.ksp.plugin.registerKspCommonSources
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper

/**
 * Gradle plugin (`com.splendo.kaluga.bluetooth.plugin`) that generates typed Bluetooth clients and servers from
 * `@Bluetooth` annotated definitions. It applies the Kotlin Multiplatform and KSP plugins, wires in the Kaluga
 * Bluetooth KSP processor and the runtime dependencies it needs, and exposes the [BluetoothExtension] (`bluetooth { }`)
 * for configuration.
 */
class BluetoothPlugin : Plugin<Project> {

    override fun apply(target: Project) = target.run {
        plugins.apply(KotlinMultiplatformPluginWrapper::class)
        plugins.apply(KspGradleSubplugin::class)

        val kalugaVersion = BluetoothPluginVersion.kalugaVersion
        val coroutinesVersion = BluetoothPluginVersion.coroutinesVersion

        val bluetoothExtension = extensions.create("bluetooth", BluetoothExtension::class.java, extensions.getByType<KspExtension>())

        val defaultGeneratedBluetoothDir = layout.buildDirectory.dir(GENERATED_DIR)

        registerKspCommonSources()
        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.commonMain {
                kotlin.srcDir(defaultGeneratedBluetoothDir)
            }
        }

        afterEvaluate {
            val xmlGeneration = bluetoothExtension.xmlGeneration
            val generatedSourceDir = xmlGeneration?.let {
                it.outputDirectory?.let(::file) ?: defaultGeneratedBluetoothDir.get().asFile
            }
            extensions.configure<KotlinMultiplatformExtension> {
                val bluetoothTargets = bluetoothExtension.target.get()
                val implementations = bluetoothExtension.implementFor.get()
                val concreteImplementation = ImplementFor.BLUETOOTH in implementations
                val generatesMock = ImplementFor.MOCK in implementations
                sourceSets.commonMain {
                    generatedSourceDir?.let { kotlin.srcDir(it) }
                    dependencies {
                        implementation("com.splendo.kaluga.bluetooth:annotations:$kalugaVersion")
                        // core is api: generated interfaces expose Flow, Identifier, GattResponse etc.
                        // from bluetooth:core in their public signatures, making them visible to consumers.
                        api("com.splendo.kaluga.bluetooth:core:$kalugaVersion")
                        // Generated Bluetooth class bodies directly call kotlinx.coroutines.flow operators
                        // (Flow<T>, map, flatMapLatest, firstOrNull, flowOf). bluetooth:core exposes these
                        // via its own api dep, but that only takes effect once bluetooth:core is rebuilt.
                        // Declaring it here too makes it available immediately regardless of build cache.
                        // Gradle's conflict resolution picks the highest declared version, so user-declared
                        // upgrades (e.g. a bugfix release) are honoured automatically.
                        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                        if (concreteImplementation && BluetoothTarget.CLIENT in bluetoothTargets) {
                            implementation("com.splendo.kaluga.bluetooth:client:$kalugaVersion")
                        }
                        if (concreteImplementation && BluetoothTarget.SERVER in bluetoothTargets) {
                            implementation("com.splendo.kaluga.bluetooth:server:$kalugaVersion")
                        }
                        if (generatesMock) {
                            implementation("com.splendo.kaluga.base:test:$kalugaVersion")
                        }
                        if (bluetoothExtension.useScientificUnits) {
                            implementation("com.splendo.kaluga.scientific:scientific:$kalugaVersion")
                        }
                    }
                }
            }
            val annotationSourceFiles = bluetoothExtension.annotationSourceDirectories.get().map { file(it) }
            tasks.withType<KspAATask>().configureEach {
                kspConfig.sourceRoots.from(annotationSourceFiles)
            }
            // The annotationSource directories hold shared @Bluetooth definitions whose generation is owned by the
            // metadata (or single leaf) pass. Include them in commonSource so the per-target passes recognise them as
            // common and skip regenerating them (which would emit duplicate js/wasmJs/etc. output).
            addKspProcessor("com.splendo.kaluga.bluetooth:ksp:$kalugaVersion", annotationSourceFiles)
            if (xmlGeneration != null) {
                val outputDir = checkNotNull(generatedSourceDir)
                val packageName = xmlGeneration.packageName ?: bluetoothExtension.generatedPackage ?: DEFAULT_GENERATED_PACKAGE
                val sources = xmlGeneration.sourceDirectories.map { file(it) }
                val generate = tasks.register("generateBluetoothDefinitions") {
                    inputs.files(sources)
                    outputs.dir(outputDir)
                    doLast {
                        GattGeneration.generateTo(outputDir, sources, xmlGeneration.deviceName, packageName, bluetoothExtension.useScientificUnits)
                    }
                }
                // The generated definitions must exist before KSP processes them and before anything is compiled.
                tasks.matching { it.name.startsWith("ksp") || it.name.startsWith("compile") }.configureEach {
                    dependsOn(generate)
                }
            }
            bluetoothExtension.afterEvaluate(::file)
        }
    }

    private companion object {
        const val GENERATED_DIR = "generated/bluetooth/commonMain/kotlin"
        const val DEFAULT_GENERATED_PACKAGE = "com.splendo.kaluga.bluetooth.generated"
    }
}

object BluetoothPluginVersion {
    private val properties = PluginProperties(BluetoothPluginVersion::class.java, "bluetooth.properties")

    val kalugaVersion: String by lazy { properties["kalugaVersion"] }

    val coroutinesVersion: String by lazy { properties["coroutinesVersion"] }
}
