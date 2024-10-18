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

import kotlinx.validation.ApiValidationExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.closureOf
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories
import org.owasp.dependencycheck.gradle.extension.AnalyzerExtension
import org.owasp.dependencycheck.gradle.extension.DependencyCheckExtension
import javax.inject.Inject

/**
 * A [BaseSplendoHealthExtension] used for a [Project] that is the root of other projects
 */
open class KalugaRootExtension @Inject constructor(
    healthVersionCatalog: VersionCatalog,
    objects: ObjectFactory,
) : BaseKalugaExtension(healthVersionCatalog, objects) {

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
        project.extensions.configure(ApiValidationExtension::class) {
            apiExtensions(project)
        }

        project.koverModules()

        afterEvaluate {
            // owasp dependency checker workaround

            // FIXME: If checks for the root project are enabled, a lot of false positives are picked up.
            // In addition it seems like the only working way to skip checks is to create
            // a dummy configuration with no dependencies
            val configuration = configurations.create("dummy")

            DependencyCheckExtension(this).apply {
                analyzers(
                    closureOf<AnalyzerExtension> {
                        assemblyEnabled = false
                    },
                )
                scanConfigurations = listOf(configuration.name)
            }
        }
    }

    private fun Project.generateNonDependentProjectsFileTask() {
        task("generateNonDependentProjectsFile") {
            outputs.upToDateWhen { false }

            val file = project.file("non_dependent_projects.properties")
            file.delete()
            file.appendText("projects=[")
            var firstProject = true

            val blacklist = properties["generateNonDependentProjectsFile.blacklist"]?.toString()?.split(',')?.map { it.trim() } ?: listOf()

            subprojects.forEach { thisProject ->

                val dependsOnOtherProject = subprojects.any { otherProject ->
                    thisProject != otherProject && (
                        thisProject.name.startsWith(
                            otherProject.name,
                        ) || thisProject.name.endsWith(otherProject.name)
                        )
                }
                val otherProjectsDependOn = subprojects.any { otherProject ->
                    thisProject != otherProject && (
                        otherProject.name.startsWith(
                            thisProject.name,
                        ) || otherProject.name.endsWith(thisProject.name)
                        )
                }

                if (!blacklist.contains(thisProject.name) && (!dependsOnOtherProject || otherProjectsDependOn)) {
                    logger.debug("main module: ${thisProject.name} dependsOnOtherProject:$dependsOnOtherProject otherProjectsDependOn:$otherProjectsDependOn")

                    if (firstProject) {
                        firstProject = false
                    } else {
                        file.appendText(",")
                    }
                    file.appendText('"' + thisProject.name + '"')
                } else {
                    logger.debug("not a main module: ${thisProject.name} dependsOnOtherProject:$dependsOnOtherProject otherProjectsDependOn:$otherProjectsDependOn")
                }
            }

            file.appendText("]")
        }
    }

    private fun ApiValidationExtension.apiExtensions(project: Project) {
        project.subprojects.forEach { subproject ->
            val moduleName = subproject.name
            ignoredClasses.add("com.splendo.kaluga.$moduleName.BuildConfig")
            ignoredClasses.add("com.splendo.kaluga.${moduleName.replace("-", ".")}.BuildConfig")
            ignoredClasses.add("com.splendo.kaluga.${moduleName.replace("-", "")}.BuildConfig")
            ignoredClasses.add("com.splendo.kaluga.permissions.${moduleName.replace("-permissions", "")}.BuildConfig")
        }
        ignoredClasses.add("$BASE_GROUP.permissions.BuildConfig")
        ignoredClasses.add("$BASE_GROUP.test.BuildConfig")
        ignoredClasses.add("$BASE_GROUP.datetime.timer.BuildConfig")
    }

    private fun Project.koverModules() {
        dependencies {
            subprojects.forEach {
                add("kover", it)
            }
        }
    }
}
