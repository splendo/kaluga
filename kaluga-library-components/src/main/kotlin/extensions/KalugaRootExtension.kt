/*
 Copyright 2024 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.plugin.extensions

import com.splendo.kaluga.plugin.helpers.kalugaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.closureOf
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.owasp.dependencycheck.gradle.extension.AnalyzerExtension
import org.owasp.dependencycheck.gradle.extension.DependencyCheckExtension
import javax.inject.Inject

/**
 * A [BaseKalugaExtension] used for a [Project] that is the root of other projects
 */
open class KalugaRootExtension @Inject constructor(healthVersionCatalog: VersionCatalog, objects: ObjectFactory) : BaseKalugaExtension(healthVersionCatalog, objects) {

    /**
     * When `true` all projects will include Maven Local as a repository
     */
    var includeMavenLocal = false

    val additionalMavenRepos = mutableListOf<String>()

    override fun Project.beforeEvaluated() {}
    override fun Project.afterProjectEvaluated() {
        allprojects {
            repositories {
                if (includeMavenLocal) {
                    mavenLocal()
                }
                additionalMavenRepos.forEach {
                    maven(it)
                }
                google()
                mavenCentral()
            }
        }

        generateNonDependentProjectsFileTask()

        project.koverModules()

        project.logger.lifecycle("Kaluga version for publishing: ${project.kalugaVersion}")
        listOf("mavenCentralUsername", "mavenCentralPassword", "signingInMemoryKey", "signingInMemoryKeyId", "signingInMemoryKeyPassword").forEach { property ->
            val value = project.providers.gradleProperty(property).getOrElse("missing")
            if (value == "missing") {
                project.logger.debug("publishing: $property is not set. Publishing to Maven Central will fail.")
            } else {
                project.logger.info("publishing: $property is present: chars: ${value.length}, lines: ${value.lines().size}")
            }
        }

        afterEvaluate {
            // owasp dependency checker workaround

            // FIXME: If checks for the root project are enabled, a lot of false positives are picked up.
            // In addition it seems like the only working way to skip checks is to create
            // a dummy configuration with no dependencies
            val configuration = configurations.create("dummy")

            DependencyCheckExtension(this).apply {
                analyzers {
                    closureOf<AnalyzerExtension> {
                        assemblyEnabled = false
                    }
                }
                scanConfigurations = listOf(configuration.name)
            }
        }
        project.setupPublishingAfterEvaluation()
    }

    private fun Project.generateNonDependentProjectsFileTask() {
        tasks.register("generateNonDependentProjectsFile") {
            outputs.upToDateWhen { false }

            val outputFile = rootProject.file("non_dependent_projects.properties")
            val blacklist = properties["generateNonDependentProjectsFile.blacklist"]?.toString()
                ?.split(',')?.map { it.trim() }?.filter { it.isNotEmpty() } ?: listOf()

            doLast {
                // One matrix entry per top-level feature group (the first Gradle path segment, e.g.
                // `bluetooth`, `permissions`, or a flat module like `date-time`). CI runs each entry
                // with `gradle -p <group> <testTask>`, which cascades to every submodule of the group
                // (core, client, compose, test-utils, ...) that has the task. A group's platform
                // capabilities are the union of its multiplatform submodules' configured targets.
                val groupTargets = linkedMapOf<String, MutableSet<String>>()
                subprojects.forEach { subproject ->
                    val group = subproject.path.removePrefix(":").substringBefore(":")
                    if (group in blacklist) {
                        return@forEach
                    }
                    val targets = groupTargets.getOrPut(group) { mutableSetOf() }
                    subproject.extensions.findByType(KotlinMultiplatformExtension::class.java)
                        ?.let { kotlin -> targets.addAll(kotlin.targets.map { it.name }) }
                }

                // A group is testable on the native/JS matrices only if it has a multiplatform module.
                val entries = groupTargets
                    .filterValues { targets -> targets.any { it.startsWith("ios") } }
                    .map { (group, targets) ->
                        fun targetsStartingWith(prefix: String) = targets.any { it.startsWith(prefix) }
                        """{"dir":"$group","name":"$group",""" +
                            """"ios":true,""" +
                            """"supportMacOS":${targetsStartingWith("macos")},""" +
                            """"supportTvOS":${targetsStartingWith("tvos")},""" +
                            """"supportWatchOS":${targetsStartingWith("watchos")},""" +
                            """"supportJS":${targets.contains("js")},""" +
                            """"supportWasmJS":${targets.contains("wasmJs")}}"""
                    }

                outputFile.writeText("projects=[${entries.joinToString(",")}]")
                logger.lifecycle("Generated ${outputFile.name} with ${entries.size} module groups")
            }
        }
    }

    private fun Project.koverModules() {
        dependencies {
            subprojects.forEach {
                add("kover", it)
            }
        }
    }
}
