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
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.model.ObjectFactory
import org.gradle.api.publish.PublicationContainer
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven
import org.gradle.kotlin.dsl.configure
import org.gradle.plugins.signing.Sign

sealed class BaseKalugaSubprojectExtension(
    versionCatalog: VersionCatalog,
    private val libraryExtension: LibraryExtension,
    private val namespacePostfix: String?,
    objects: ObjectFactory,
) : BaseKalugaExtension(versionCatalog, objects) {

    var isPublished: Boolean = true

    var moduleName: String
        get() = libraryExtension.namespace.orEmpty()
            .removePrefix("$BASE_GROUP.")
            .removeSuffix(namespacePostfix?.let { ".$it" } ?: "")
        set(value) {
            libraryExtension.namespace = listOfNotNull(BASE_GROUP, value, namespacePostfix).joinToString(".")
        }

    protected val androidMainDependencies = listOf(
        "androidx-activity-ktx",
        "androidx-appcompat",
        "kotlinx-coroutines-android",
    ).map { it.asDependency() }

    protected val androidTestDependencies = listOf(
        "bytebuddy-agent",
        "junit",
        "kotlin-test",
        "kotlin-test-junit",
        "mockito-core",
    ).map { it.asDependency() }

    protected val androidInstrumentedTestDependencies = listOf(
        "androidx-test-core",
        "androidx-test-core-ktx",
        "androidx-test-espresso",
        "androidx-test-junit",
        "androidx-test-rules",
        "androidx-test-runner",
        "androidx-test-uiautomator",
        "bytebuddy-agent",
        "bytebuddy-android",
        "kotlin-test",
        "kotlin-test-junit",
        "mockito-core",
        "mockito-android",
    ).map { it.asDependency() }

    override fun Project.beforeEvaluated() {
        setupSubproject()
        libraryExtension.apply {
            compileSdk = versionCatalog.findVersion("androidCompileSdk").get().displayName.toInt()
            buildToolsVersion = versionCatalog.findVersion("androidBuildTools").get().displayName

            defaultConfig {
                minSdk = versionCatalog.findVersion("androidMinSdk").get().displayName.toInt()

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            signingConfigs {
                create("stableDebug") {
                    storeFile = project.rootProject.file("keystore/stableDebug.keystore")
                    storePassword = "stableDebug"
                    keyAlias = "stableDebug"
                    keyPassword = "stableDebug"
                }
            }

            buildTypes {
                release {
                    isMinifyEnabled = false
                }
                debug {
                    signingConfig = signingConfigs.getByName("stableDebug")
                }
            }

            compileOptions {
                val javaVersion = JavaVersion.toVersion(versionCatalog.findVersion("java").get().displayName)
                sourceCompatibility = javaVersion
                targetCompatibility = javaVersion
            }

            configure()
        }
    }

    protected abstract fun Project.setupSubproject()

    override fun Project.afterProjectEvaluated() {
        if (moduleName.isEmpty()) {
            throw RuntimeException("moduleName must be configured")
        }

        configureSubproject()
        if (isPublished) {
            setupPublishing()
        }
    }

    protected abstract fun Project.configureSubproject()

    protected abstract fun LibraryExtension.configure()
    fun Project.setupPublishing() {
        extensions.configure(PublishingExtension::class) {
            publications {
                configure(project)
            }
        }

        tasks.withType(AbstractPublishToMaven::class.java).configureEach {
            dependsOn(tasks.withType(Sign::class.java))
        }
    }

    protected abstract fun PublicationContainer.configure(project: Project)

    protected fun String.asDependency() = versionCatalog.findLibrary(this).get()
}
