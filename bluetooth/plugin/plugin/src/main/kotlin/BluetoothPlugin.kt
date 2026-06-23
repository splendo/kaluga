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
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.SourceTask
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.gradle.kotlin.dsl.withType
import org.gradle.process.CommandLineArgumentProvider
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import java.io.File
import java.util.Properties

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

        val bluetoothExtension = extensions.create("bluetooth", BluetoothExtension::class.java, extensions.getByType<KspExtension>())

        val generatedKspDir = layout.buildDirectory.dir("generated/ksp/metadata/commonMain/kotlin")
        val defaultGeneratedBluetoothDir = layout.buildDirectory.dir(GENERATED_DIR)

        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.commonMain {
                kotlin.srcDir(generatedKspDir)
                kotlin.srcDir(defaultGeneratedBluetoothDir)
            }
        }

        // Generated sources under build/generated are registered as source directories for compilation, but they are
        // produced output (already formatted by KotlinPoet), not hand-written code, so kotlinter should neither lint nor
        // reformat them. Excluding them also drops the generated dir from the lint/format tasks' inputs, which removes the
        // implicit task dependency (on KSP / the XML generator) that Gradle would otherwise reject.
        val generatedRoot = layout.buildDirectory.dir("generated").get().asFile.absolutePath
        tasks.matching { it.name.startsWith("formatKotlin") || it.name.startsWith("lintKotlin") }.configureEach {
            (this as? SourceTask)?.exclude { it.file.absolutePath.startsWith(generatedRoot) }
        }

        afterEvaluate {
            val xmlGeneration = bluetoothExtension.xmlGeneration
            val generatedSourceDir = xmlGeneration?.let {
                it.outputDirectory?.let(::file) ?: defaultGeneratedBluetoothDir.get().asFile
            }
            extensions.configure<KotlinMultiplatformExtension> {
                project.dependencies.add(
                    "kspCommonMainMetadata",
                    "com.splendo.kaluga.bluetooth:ksp:$kalugaVersion",
                )
                targets.configureEach {
                    if (name !in listOf("metadata")) {
                        project.dependencies.add(
                            "ksp${name.uppercaseFirstChar()}",
                            "com.splendo.kaluga.bluetooth:ksp:$kalugaVersion",
                        )
                    }
                }
                val isSinglePlatform = targets.count { it.name != "metadata" } == 1
                val bluetoothTargets = bluetoothExtension.target.get()
                val implementations = bluetoothExtension.implementFor.get()
                val generatesImplementation = implementations.isNotEmpty()
                val generatesMock = ImplementFor.MOCK in implementations
                sourceSets.commonMain {
                    generatedSourceDir?.let { kotlin.srcDir(it) }
                    bluetoothExtension.annotationSourceDirectories.get().forEach { kotlin.srcDir(it) }
                    dependencies {
                        implementation("com.splendo.kaluga.bluetooth:annotations:$kalugaVersion")
                        implementation("com.splendo.kaluga.bluetooth:core:$kalugaVersion")
                        if (generatesImplementation && BluetoothTarget.CLIENT in bluetoothTargets) {
                            implementation("com.splendo.kaluga.bluetooth:client:$kalugaVersion")
                        }
                        if (generatesImplementation && BluetoothTarget.SERVER in bluetoothTargets) {
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

                tasks.withType<KspAATask>().configureEach {
                    if (!isSinglePlatform && name != "kspCommonMainKotlinMetadata") {
                        dependsOn("kspCommonMainKotlinMetadata")
                    }
                }
                this@run.extensions.configure<KspExtension> {
                    arg(CommonSourceArgumentProvider(sourceSets.commonMain.get().kotlin.sourceDirectories))
                    arg("isSingleTarget", "$isSinglePlatform")
                }
            }
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
            bluetoothExtension.afterEvaluate()
        }
    }

    private companion object {
        const val GENERATED_DIR = "generated/bluetooth/commonMain/kotlin"
        const val DEFAULT_GENERATED_PACKAGE = "com.splendo.kaluga.bluetooth.generated"
    }
}

/**
 * Passes the common source directories to the processor as the `commonSource` option. The directories are declared as
 * a relative-path-sensitive input so the KSP task stays cacheable and relocatable across machines/checkout locations,
 * while the option value handed to the processor remains an absolute path (it is matched against absolute file paths).
 */
internal class CommonSourceArgumentProvider(
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    val commonSources: FileCollection,
) : CommandLineArgumentProvider {
    override fun asArguments(): Iterable<String> = listOf("commonSource=${commonSources.files.joinToString(separator = ":") { it.absolutePath }}")
}

object BluetoothPluginVersion {
    val kalugaVersion: String by lazy {
        BluetoothPluginVersion::class.java
            .classLoader
            .getResourceAsStream("bluetooth.properties")
            ?.use {
                Properties().apply { load(it) }
            }
            ?.getProperty("kalugaVersion")
            ?: error("Bluetooth plugin version not found")
    }
}
