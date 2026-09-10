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

import com.android.build.api.dsl.LibraryExtension
import com.splendo.kaluga.plugin.helpers.jvmTarget
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation

abstract class BaseKalugaAndroidSubprojectExtension(versionCatalog: VersionCatalog, androidxVersionCatalog: VersionCatalog, val libraryExtension: LibraryExtension, namespacePostfix: String, objects: ObjectFactory) :
    BaseKalugaSubprojectExtension(versionCatalog, androidxVersionCatalog, namespacePostfix, objects) {

    override var namespace: String?
        get() = libraryExtension.namespace
        set(value) {
            libraryExtension.namespace = value
        }

    override fun Project.setupSubproject() {}

    override fun Project.configureSubproject() {
        extensions.configure(KotlinAndroidProjectExtension::class) {
            compilerOptions {
                jvmTarget.set(versionCatalog.jvmTarget)
                // languageSettings() is not applied by AGP 9.0 built-in Kotlin; use compilerOptions.optIn instead
                optIn.addAll(optInAnnotations())
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
            androidDeviceTestDependencies.forEach {
                add("androidTestImplementation", it)
            }
        }
    }

    protected open fun optInAnnotations(): List<String> = emptyList()
    protected abstract fun DependencyHandlerScope.commonDependencies()

    override fun Project.afterProjectEvaluated() {
        setupPublishingAfterEvaluation()
    }

    protected abstract fun LibraryExtension.configure()

    @OptIn(ExperimentalAbiValidation::class)
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
        extensions.findByType<KotlinAndroidProjectExtension>()?.abiValidation {
            configureKalugaAbi(layout.projectDirectory.dir("api"))
        }
        configureSubproject()
    }
}
