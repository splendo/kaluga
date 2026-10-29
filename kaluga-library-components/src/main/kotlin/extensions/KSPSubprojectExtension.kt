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

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmExtension
import javax.inject.Inject

open class KSPSubprojectExtension @Inject constructor(versionCatalog: VersionCatalog, androidxVersionCatalog: VersionCatalog, objects: ObjectFactory) :
    KalugaJvmSubprojectExtension(versionCatalog, androidxVersionCatalog, "ksp", objects) {

    override fun Project.configureSubproject() {
        configureJvm(this)
        extensions.configure(KotlinJvmExtension::class) {
            sourceSets.all {
                languageSettings {
                    optIn("com.google.devtools.ksp.KspExperimental")
                }
            }
        }
        dependencies {
            add("implementation", "google-devtools-ksp-symbolProcessingAPI".asDependency())
            add("implementation", "kotlinpoet-ksp".asDependency())
        }
    }
}
