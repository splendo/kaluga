/*
 Copyright 2025 Splendo Consulting B.V. The Netherlands

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
import org.jetbrains.kotlin.gradle.dsl.abi.AbiValidationVariantSpec
import org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation

sealed class BaseKalugaSubprojectExtension(versionCatalog: VersionCatalog, protected val namespacePostfix: String?, objects: ObjectFactory) :
    BaseKalugaExtension(versionCatalog, objects) {

    abstract var namespace: String?
    var moduleName: String
        get() = namespace.orEmpty()
            .removePrefix("$BASE_GROUP.")
            .removeSuffix(namespacePostfix?.let { ".$it" } ?: "")
        set(value) {
            namespace = listOfNotNull(BASE_GROUP, value, namespacePostfix).joinToString(".")
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

    protected val androidDeviceTestDependencies = listOf(
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

    protected abstract fun Project.setupSubproject()

    @OptIn(ExperimentalAbiValidation::class)
    override fun Project.afterProjectEvaluated() {
        if (moduleName.isEmpty()) {
            throw RuntimeException("moduleName must be configured")
        }
    }

    protected abstract fun Project.configureSubproject()

    protected fun String.asDependency() = versionCatalog.findLibrary(this).get()

    @OptIn(ExperimentalAbiValidation::class)
    protected fun AbiValidationVariantSpec.abiExtension() {
        filters.exclude {
            byNames.set(
                setOf(
                    "com.splendo.kaluga.$moduleName.BuildConfig",
                )
            )
        }
    }
}
