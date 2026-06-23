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
import org.gradle.api.NamedDomainObjectContainer
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
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
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

        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.commonMain {
                kotlin.srcDir(generatedKspDir)
            }
        }

        // The KSP-generated sources are registered as a commonMain source directory, so kotlinter's tasks read from the
        // KSP output directory. That directory is a task output, so Gradle requires an explicit dependency (it rejects the
        // otherwise-implicit one). We also exclude those files from the kotlinter source: they are produced output (already
        // formatted by KotlinPoet), not hand-written code, so they should be neither linted nor reformatted.
        val generatedRoot = layout.buildDirectory.dir("generated").get().asFile.absolutePath
        val kspTasks = tasks.withType<KspAATask>()
        tasks.matching { it.name.startsWith("formatKotlin") || it.name.startsWith("lintKotlin") }.configureEach {
            dependsOn(kspTasks)
            (this as? SourceTask)?.exclude { it.file.absolutePath.startsWith(generatedRoot) }
        }

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
                    arg(CommonSourceArgumentProvider(sourceSets.commonMain.get().kotlin.sourceDirectories))
                    arg("isSingleTarget", "$isSinglePlatform")
                }

                if (isSinglePlatform) {
                    this@run.forceSingleTargetCommonProcessing(sourceSets, targets.first { it.name != "metadata" })
                }
            }
            bluetoothExtension.afterEvaluate()
        }
    }
}

/**
 * Workaround for KSP's lack of common-source processing in a single-target KMP project (see
 * https://github.com/google/ksp/issues/1525). With one leaf target the `commonMain` metadata compile is named
 * `compileKotlinMetadata`, which KSP unconditionally skips, so no KSP task ever processes `commonMain` and the
 * generated API is never produced. KSP also fails to wire the leaf target's own KSP task for the new
 * Android KMP library target (empty source roots / processor classpath / a disabling `onlyIf`).
 *
 * To make generation work anyway, drive the single leaf target's KSP task directly over the common sources and
 * relocate its output into `commonMain` so common code (and hand-written consumers) can reference the generated API.
 * This is deliberately fragile — it reaches into KSP's task internals — and is only used when the project has a
 * single leaf target; the multi-target path uses the normal `kspCommonMainKotlinMetadata` pass. Currently shaped for
 * the Android leaf (the only single-target case Kaluga produces); a no-op if the leaf's KSP task is absent.
 */
private fun Project.forceSingleTargetCommonProcessing(sourceSets: NamedDomainObjectContainer<KotlinSourceSet>, leaf: KotlinTarget) {
    val leafMain = "${leaf.name}Main"
    val kspTaskName = "ksp${leafMain.uppercaseFirstChar()}"
    if (kspTaskName !in tasks.names) return

    // KSP leaves the leaf's own resolvable processor classpath empty for this target, so build a resolvable view
    // here. Reuse the processor dependency the plugin already declared on the leaf target's KSP configuration
    // (`ksp<Target>`) rather than re-stating the coordinate, so the version follows the rest of the plugin.
    val processorClasspath = configurations.detachedConfiguration(
        *configurations.getByName("ksp${leaf.name.uppercaseFirstChar()}").allDependencies.toTypedArray(),
    )
    val leafGenDir = layout.buildDirectory.dir("generated/ksp/${leaf.name}/$leafMain/kotlin")
    val commonRoots = sourceSets.getByName("commonMain").kotlin.srcDirs.filterNot { it.path.contains("generated/ksp") }

    val kspTask = tasks.named(kspTaskName, KspAATask::class.java).get()
    kspTask.kspConfig.sourceRoots.from(commonRoots)
    kspTask.kspConfig.commonSourceRoots.from(commonRoots)
    kspTask.kspConfig.processorClasspath.from(processorClasspath)
    kspTask.kspClasspath.from(processorClasspath)
    kspTask.setOnlyIf { true }

    val leafKotlin = sourceSets.getByName(leafMain).kotlin
    leafKotlin.setSrcDirs(leafKotlin.srcDirs.filterNot { it.path.contains("generated/ksp/${leaf.name}/$leafMain") })
    sourceSets.getByName("commonMain").kotlin.srcDir(leafGenDir)
    tasks.named("compile${leafMain.uppercaseFirstChar()}").configure { dependsOn(kspTask) }
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
