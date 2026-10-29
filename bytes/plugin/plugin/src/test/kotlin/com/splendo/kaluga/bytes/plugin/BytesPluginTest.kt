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

import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Project
import org.gradle.api.ProjectConfigurationException
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlinx.serialization.gradle.SerializationGradleSubplugin
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class BytesPluginTest {

    private fun project(bytes: BytesExtension.() -> Unit = {}, targets: KotlinMultiplatformExtension.() -> Unit): Project {
        val project = ProjectBuilder.builder().build()
        project.plugins.apply(BytesPlugin::class.java)
        project.extensions.configure<KotlinMultiplatformExtension>(targets)
        project.extensions.getByType<BytesExtension>().bytes()
        (project as ProjectInternal).evaluate()
        return project
    }

    private fun Project.dependencyNames(configuration: String): List<String> = configurations.getByName(configuration).dependencies.map { "${it.group}:${it.name}" }

    private val Project.kspArguments: Map<String, String> get() = extensions.getByType<KspExtension>().arguments

    @Test
    fun `adds the processor to every pass and both runtimes whatever is generated`() {
        val project = project(bytes = { generate.set(setOf(Generate.VIEW)) }) {
            jvm()
            js { nodejs() }
        }

        assertEquals(
            listOf("$BYTES:annotations", "$BYTES:serialization", "$BYTES:view"),
            project.dependencyNames("commonMainImplementation").filter { it.startsWith("$BYTES:") },
        )
        assertTrue("$BYTES:ksp" in project.dependencyNames("kspCommonMainMetadata"))
        assertTrue("$BYTES:ksp" in project.dependencyNames("kspJvm") && "$BYTES:ksp" in project.dependencyNames("kspJs"))
        assertTrue(project.plugins.hasPlugin(SerializationGradleSubplugin::class.java))
        assertEquals("VIEW", project.kspArguments["generate"])
        assertEquals("false", project.kspArguments["isSingleTarget"])
        assertEquals("false", project.kspArguments["skipUnsupportedViews"])
    }

    @Test
    fun `rejects generating nothing`() {
        val failure = assertFailsWith<ProjectConfigurationException> { project(bytes = { generate.set(emptySet()) }) { jvm() } }
        assertTrue(generateSequence<Throwable>(failure) { it.cause }.any { "must include Generate.DATA and/or Generate.VIEW" in it.message.orEmpty() }, failure.toString())
    }

    @Test
    fun `names the registries of each pass by its project and source set`() {
        assertEquals("ValidationFormatCommonMain", registryPrefix(":validation:format", "kspCommonMainKotlinMetadata"))
        assertEquals("ValidationFormatJvmMain", registryPrefix(":validation:format", "kspJvmMain"))
        assertEquals("AndroidMain", registryPrefix(":", "kspAndroidMain"))
        assertEquals("BytesKit2CommonMain", registryPrefix(":bytes-kit:2", "kspCommonMainKotlinMetadata"))
    }

    @Test
    fun `mirrors the Generate of the annotations`() {
        val annotations = File("../../annotations/src/commonMain/kotlin/annotations/BytesLayout.kt").readText()
        val entries = Regex("enum class Generate \\{([^}]*)}").find(annotations)!!.groupValues[1]
            .lines().map { it.trim().removeSuffix(",") }.filter { it.isNotEmpty() && !it.startsWith("/") && !it.startsWith("*") }
        assertEquals(entries, Generate.entries.map { it.name })
    }
}

private const val BYTES = "com.splendo.kaluga.bytes"
