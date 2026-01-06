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

package com.splendo.kaluga.plugin.extensions

import com.splendo.kaluga.plugin.helpers.javaVersion
import com.splendo.kaluga.plugin.helpers.jvmTarget
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.model.ObjectFactory
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmExtension

open class KSPSubprojectExtension(versionCatalog: VersionCatalog, objects: ObjectFactory) :
    BaseKalugaSubprojectExtension(versionCatalog, "ksp", objects) {

    override var namespace: String? = null

    override fun Project.setupSubproject() {}

    override fun Project.configureSubproject() {
        extensions.configure(KotlinJvmExtension::class) {
            compilerOptions {
                jvmTarget.set(versionCatalog.jvmTarget)
            }
            sourceSets.all {
                languageSettings {
                    optIn("com.google.devtools.ksp.KspExperimental")
                }
            }
        }
        extensions.configure(JavaPluginExtension::class) {
            sourceCompatibility = versionCatalog.javaVersion
            targetCompatibility = versionCatalog.javaVersion
        }
        dependencies {
            add("implementation", "google-devtools-ksp-symbolProcessingAPI".asDependency())
            add("implementation", "kotlinpoet-ksp".asDependency())
        }
    }

    override fun Project.beforeEvaluated() {
        configureSubproject()
    }
    override fun Project.afterProjectEvaluated() {
        setupPublishingAfterEvaluation()
    }
}