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

package com.splendo.kaluga.ksp.plugin

import com.google.devtools.ksp.gradle.KspAATask
import com.google.devtools.ksp.gradle.KspExtension
import com.google.devtools.ksp.gradle.KspGradleSubplugin
import org.gradle.api.Project
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KalugaKspTest {

    private fun project(targets: KotlinMultiplatformExtension.() -> Unit): Project {
        val project = ProjectBuilder.builder().build()
        project.plugins.apply(KotlinMultiplatformPluginWrapper::class.java)
        project.plugins.apply(KspGradleSubplugin::class.java)
        project.registerKspCommonSources()
        project.tasks.register("lintKotlinCommonMain")
        project.extensions.getByType(KotlinMultiplatformExtension::class.java).targets()
        project.afterEvaluate { it.addKspProcessor(PROCESSOR, listOf(it.file("annotations"))) }
        (project as ProjectInternal).evaluate()
        return project
    }

    private fun Project.dependencyNames(configuration: String): List<String> = configurations.getByName(configuration).dependencies.map { "${it.group}:${it.name}" }

    private val Project.kspArguments: Map<String, String> get() = extensions.getByType(KspExtension::class.java).arguments

    private fun Project.taskDependencies(name: String): Set<String> = tasks.getByName(name).let { task ->
        task.taskDependencies.getDependencies(task).map { it.name }.toSet()
    }

    @Test
    fun `adds the processor to the metadata pass and every target`() {
        val project = project {
            jvm()
            js { nodejs() }
        }
        val processor = PROCESSOR.substringBeforeLast(':')
        assertTrue(processor in project.dependencyNames("kspCommonMainMetadata"))
        assertTrue(processor in project.dependencyNames("kspJvm") && processor in project.dependencyNames("kspJs"))
    }

    @Test
    fun `processes the common sources in the pass of a single target`() {
        val single = project { jvm() }
        assertEquals("true", single.kspArguments["isSingleTarget"])
        assertTrue("kspCommonMainKotlinMetadata" !in single.tasks.getByName("kspKotlinJvm").dependsOn.map { it.toString() })

        // With several targets, the metadata pass generates the common code before any target's pass.
        val several = project {
            jvm()
            js { nodejs() }
        }
        assertEquals("false", several.kspArguments["isSingleTarget"])
        assertTrue("kspCommonMainKotlinMetadata" in several.tasks.getByName("kspKotlinJvm").dependsOn.map { it.toString() })
    }

    @Test
    fun `lints after only the pass generating the common sources`() {
        assertEquals(setOf("kspKotlinJvm"), project { jvm() }.taskDependencies("lintKotlinCommonMain"))
        val several = project {
            jvm()
            js { nodejs() }
        }
        assertEquals(setOf("kspCommonMainKotlinMetadata"), several.taskDependencies("lintKotlinCommonMain"))
    }

    @Test
    fun `passes the common and additional sources as the common sources`() {
        val project = project { jvm() }
        val provider = project.tasks.withType(KspAATask::class.java).getByName("kspKotlinJvm").commandLineArgumentProviders.get()
            .filterIsInstance<CommonSourceArgumentProvider>().single()
        val commonSources = provider.asArguments().single().removePrefix("commonSource=").split(":")
        assertTrue(project.file("src/commonMain/kotlin").absolutePath in commonSources, commonSources.toString())
        assertTrue(project.file("annotations").absolutePath in commonSources, commonSources.toString())
    }
}

private const val PROCESSOR = "com.example:processor:1.0"
