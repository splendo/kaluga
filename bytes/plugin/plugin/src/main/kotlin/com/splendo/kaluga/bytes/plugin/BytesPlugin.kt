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

package com.splendo.kaluga.bytes.plugin

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
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.gradle.kotlin.dsl.withType
import org.gradle.process.CommandLineArgumentProvider
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlinx.serialization.gradle.SerializationGradleSubplugin
import java.util.Properties

/**
 * Gradle plugin (`com.splendo.kaluga.bytes.plugin`) that generates `BytesFormat` data classes and/or zero-copy views
 * from `@BytesLayout` annotated interfaces. It applies the Kotlin Multiplatform, KSP and kotlinx serialization plugins,
 * wires in the Kaluga Bytes KSP processor and the runtime dependencies it needs, and exposes the [BytesExtension]
 * (`bytes { }`) for configuration.
 */
class BytesPlugin : Plugin<Project> {

    override fun apply(target: Project) = target.run {
        plugins.apply(KotlinMultiplatformPluginWrapper::class)
        plugins.apply(KspGradleSubplugin::class)
        // A single interface can generate DATA whatever the module-wide default, so DATA classes may appear in any module.
        if (!plugins.hasPlugin(SerializationGradleSubplugin::class.java)) plugins.apply(SerializationGradleSubplugin::class)

        val kalugaVersion = BytesPluginVersion.kalugaVersion

        val bytesExtension = extensions.create("bytes", BytesExtension::class.java, extensions.getByType<KspExtension>())

        val generatedKspDir = layout.buildDirectory.dir("generated/ksp/metadata/commonMain/kotlin")

        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.commonMain {
                kotlin.srcDir(generatedKspDir)
            }
        }

        // The KSP-generated sources are registered as a commonMain source directory, so kotlinter's tasks read from
        // the KSP output directory. Those files are produced output (already formatted by KotlinPoet), not
        // hand-written code, so exclude them from the kotlinter source: they should be neither linted nor
        // reformatted. The producer dependency kotlinter needs on that generated dir is wired in afterEvaluate, once
        // the generating KSP task is known, so only that task runs on format/lint rather than every per-target KSP
        // task.
        val generatedRoot = layout.buildDirectory.dir("generated").get().asFile.absolutePath
        tasks.matching { it.name.startsWith("formatKotlin") || it.name.startsWith("lintKotlin") }.configureEach {
            (this as? SourceTask)?.exclude { it.file.absolutePath.startsWith(generatedRoot) }
        }

        afterEvaluate {
            extensions.configure<KotlinMultiplatformExtension> {
                project.dependencies.add(
                    "kspCommonMainMetadata",
                    "com.splendo.kaluga.bytes:ksp:$kalugaVersion",
                )
                targets.configureEach {
                    if (name != "metadata") {
                        project.dependencies.add(
                            "ksp${name.uppercaseFirstChar()}",
                            "com.splendo.kaluga.bytes:ksp:$kalugaVersion",
                        )
                    }
                }
                val isSinglePlatform = targets.count { it.name != "metadata" } == 1

                // kotlinter reads the KSP output registered as a commonMain source dir; that dir is a task output, so
                // Gradle needs an explicit producer dependency. Only the metadata pass (multi-target) or the single
                // leaf pass writes a registered source dir, so depend on just that one. Depending on every KspAATask
                // would force js/wasmJs/macos KSP to run on every formatKotlin/lintKotlin.
                // A leaf's pass is named for its source set on the Android KMP library target, for its compilation otherwise.
                val generatingKspTaskNames = if (isSinglePlatform) {
                    val leaf = targets.first { it.name != "metadata" }.name.uppercaseFirstChar()
                    setOf("ksp${leaf}Main", "kspKotlin$leaf")
                } else {
                    setOf("kspCommonMainKotlinMetadata")
                }
                tasks.matching { it.name.startsWith("formatKotlin") || it.name.startsWith("lintKotlin") }.configureEach {
                    dependsOn(tasks.matching { it.name in generatingKspTaskNames })
                }

                // What is generated is only known once KSP has read every interface, since each can override the
                // module-wide default, so both runtimes are always there.
                sourceSets.commonMain {
                    dependencies {
                        implementation("com.splendo.kaluga.bytes:annotations:$kalugaVersion")
                        implementation("com.splendo.kaluga.bytes:serialization:$kalugaVersion")
                        implementation("com.splendo.kaluga.bytes:view:$kalugaVersion")
                    }
                }

                // Multi-target only: order the per-target passes after the common metadata pass so a leaf never
                // races the metadata pass for the same annotated interfaces.
                tasks.withType<KspAATask>().configureEach {
                    if (!isSinglePlatform && name != "kspCommonMainKotlinMetadata") {
                        dependsOn("kspCommonMainKotlinMetadata")
                    }
                    // Every pass generates its registries into the same packages, so each names them for itself.
                    kspConfig.processorOptions.put("registryPrefix", registryPrefix(project.path, name))
                }
                // The per-target passes also see the common sources; these args let the processor skip them there,
                // as the metadata (or single leaf) pass already generated their code into commonMain.
                this@run.extensions.configure<KspExtension> {
                    arg(CommonSourceArgumentProvider(this@run.files(sourceSets.commonMain.get().kotlin.sourceDirectories)))
                    arg("isSingleTarget", "$isSinglePlatform")
                }

                if (isSinglePlatform) {
                    this@run.forceSingleTargetCommonProcessing(sourceSets, targets.first { it.name != "metadata" })
                }
            }
            bytesExtension.afterEvaluate()
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
 * The prefix of the registry objects the KSP task [kspTaskName] of the project at [projectPath] generates: the project
 * path and the source set the task processes, as one identifier, so no two passes of any modules generate the same
 * name. `:validation:format` and `kspCommonMainKotlinMetadata` give `ValidationFormatCommonMain`.
 */
internal fun registryPrefix(projectPath: String, kspTaskName: String): String {
    val sourceSet = kspTaskName.removePrefix("ksp").removeSuffix("KotlinMetadata")
    return (projectPath.split(':') + sourceSet).flatMap { it.split(Regex("[^A-Za-z0-9]+")) }
        .filter { it.isNotEmpty() }
        .joinToString("") { it.uppercaseFirstChar() }
        .let { if (it.first().isDigit()) "_$it" else it }
}

/**
 * Passes the common source directories to the processor as the `commonSource` option. The directories are declared as
 * a relative-path-sensitive input so the KSP task stays cacheable and relocatable, while the option value handed to the
 * processor remains an absolute path (it is matched against absolute file paths).
 */
internal class CommonSourceArgumentProvider(
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    val commonSources: FileCollection,
) : CommandLineArgumentProvider {
    override fun asArguments(): Iterable<String> = listOf("commonSource=${commonSources.files.joinToString(separator = ":") { it.absolutePath }}")
}

object BytesPluginVersion {
    private val properties: Properties by lazy {
        BytesPluginVersion::class.java
            .classLoader
            .getResourceAsStream("bytes.properties")
            ?.use { Properties().apply { load(it) } }
            ?: error("Bytes plugin properties not found")
    }

    val kalugaVersion: String by lazy {
        properties.getProperty("kalugaVersion") ?: error("kalugaVersion not found in bytes.properties")
    }
}
