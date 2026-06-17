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
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
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

        afterEvaluate {
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
                val generatesImplementation = bluetoothExtension.implementFor.get().isNotEmpty()
                sourceSets.commonMain {
                    kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
                    if (bluetoothExtension.xmlGeneration != null) kotlin.srcDir("build/$XML_GENERATED_DIR")
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
                    }
                }

                tasks.withType<KspAATask>().configureEach {
                    if (!isSinglePlatform && name != "kspCommonMainKotlinMetadata") {
                        dependsOn("kspCommonMainKotlinMetadata")
                    }
                }
                this@run.extensions.configure<KspExtension> {
                    arg("commonSource", sourceSets.commonMain.get().kotlin.sourceDirectories.files.joinToString(separator = ":") { it.absolutePath })
                    arg("isSingleTarget", "$isSinglePlatform")
                }
            }
            bluetoothExtension.xmlGeneration?.let { xml ->
                val outputDir = layout.buildDirectory.dir(XML_GENERATED_DIR)
                val sourceDirectories = xml.sourceDirectories.map { file(it) }
                val packageName = xml.packageName ?: bluetoothExtension.generatedPackage ?: DEFAULT_XML_PACKAGE
                val generate = tasks.register("generateBluetoothXmlDefinitions") {
                    inputs.files(sourceDirectories)
                    outputs.dir(outputDir)
                    doLast {
                        GattGeneration.generateTo(outputDir.get().asFile, sourceDirectories, xml.deviceName, packageName)
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
        const val XML_GENERATED_DIR = "generated/bluetooth-xml/commonMain/kotlin"
        const val DEFAULT_XML_PACKAGE = "com.splendo.kaluga.bluetooth.generated"
    }
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
