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

import com.android.build.gradle.LibraryExtension
import com.splendo.kaluga.plugin.helpers.jvmTarget
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.model.ObjectFactory
import org.gradle.api.publish.PublicationContainer
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.plugin.LanguageSettingsBuilder

abstract class BaseKalugaAndroidSubprojectExtension(
    versionCatalog: VersionCatalog,
    libraryExtension: LibraryExtension,
    namespacePostfix: String,
    objects: ObjectFactory,
) : BaseKalugaSubprojectExtension(versionCatalog, libraryExtension, namespacePostfix, objects) {

    override fun Project.configureSubproject() {
        extensions.configure(KotlinAndroidProjectExtension::class) {
            compilerOptions {
                jvmTarget.set(versionCatalog.jvmTarget)
            }
            sourceSets.all {
                languageSettings {
                    languageSettings()
                }
            }
        }
        dependencies {
            androidMainDependencies.forEach {
                add("implementation", it)
            }

            commonDependencies()

            androidTestDependencies.forEach {
                add("testImplementation", it)
            }
            androidInstrumentedTestDependencies.forEach {
                add("androidTestImplementation", it)
            }
        }
    }

    protected abstract fun LanguageSettingsBuilder.languageSettings()
    protected abstract fun DependencyHandlerScope.commonDependencies()

    override fun PublicationContainer.configure(project: Project) {
        create("release", MavenPublication::class) {
            from(project.components.getByName("release"))

            artifactId = project.name
            groupId = BASE_GROUP
            version = this@BaseKalugaAndroidSubprojectExtension.version
        }
        create("debug", MavenPublication::class) {
            from(project.components.getByName("debug"))

            artifactId = project.name
            groupId = BASE_GROUP
            version = this@BaseKalugaAndroidSubprojectExtension.version
        }
    }
}
