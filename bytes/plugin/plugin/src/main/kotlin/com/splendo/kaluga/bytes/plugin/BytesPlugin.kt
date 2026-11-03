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
import com.splendo.kaluga.ksp.plugin.PluginProperties
import com.splendo.kaluga.ksp.plugin.addKspProcessor
import com.splendo.kaluga.ksp.plugin.registerKspCommonSources
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import org.jetbrains.kotlinx.serialization.gradle.SerializationGradleSubplugin

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

        registerKspCommonSources()

        afterEvaluate {
            extensions.configure<KotlinMultiplatformExtension> {
                // What is generated is only known once KSP has read every interface, since each can override the
                // module-wide default, so both runtimes are always there.
                sourceSets.commonMain {
                    dependencies {
                        implementation("com.splendo.kaluga.bytes:annotations:$kalugaVersion")
                        implementation("com.splendo.kaluga.bytes:serialization:$kalugaVersion")
                        implementation("com.splendo.kaluga.bytes:view:$kalugaVersion")
                    }
                }
            }
            // Every pass generates its registries into the same packages, so each names them for itself.
            tasks.withType<KspAATask>().configureEach {
                kspConfig.processorOptions.put("registryPrefix", registryPrefix(project.path, name))
            }
            addKspProcessor("com.splendo.kaluga.bytes:ksp:$kalugaVersion")
            bytesExtension.afterEvaluate()
        }
    }
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

object BytesPluginVersion {
    private val properties = PluginProperties(BytesPluginVersion::class.java, "bytes.properties")

    val kalugaVersion: String by lazy { properties["kalugaVersion"] }
}
