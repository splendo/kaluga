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
import org.gradle.api.file.Directory
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.abi.AbiValidationExtension
import org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation

sealed class BaseKalugaSubprojectExtension(versionCatalog: VersionCatalog, androidxVersionCatalog: VersionCatalog, protected val namespacePostfix: String?, objects: ObjectFactory) :
    BaseKalugaExtension(versionCatalog, androidxVersionCatalog, objects) {

    abstract var namespace: String?
    var moduleName: String
        get() = namespace.orEmpty()
            .removePrefix("$BASE_GROUP.")
            .removeSuffix(namespacePostfix?.let { ".$it" } ?: "")
        set(value) {
            namespace = listOfNotNull(BASE_GROUP, value, namespacePostfix).joinToString(".")
        }

    protected val androidMainDependencies = listOf(
        "activity-activityKtx".asAndroidxDependency(),
        "appcompat-appcompat".asAndroidxDependency(),
        "kotlinx-coroutines-android".asDependency(),
    )

    protected val androidTestDependencies = listOf(
        "bytebuddy-agent",
        "junit",
        "kotlin-test",
        "kotlin-test-junit",
        "mockito-core",
    ).map { it.asDependency() }

    protected val androidDeviceTestDependencies = listOf(
        "test-core".asAndroidxDependency(),
        "test-coreKtx".asAndroidxDependency(),
        "testEspresso-espressoCore".asAndroidxDependency(),
        "testExt-junit".asAndroidxDependency(),
        "test-rules".asAndroidxDependency(),
        "test-runner".asAndroidxDependency(),
        "testUiautomator-uiautomator".asAndroidxDependency(),
    ) + listOf(
        "bytebuddy-agent",
        "bytebuddy-android",
        "kotlin-test",
        "kotlin-test-junit",
        "mockito-core",
        "mockito-android",
    ).map { it.asDependency() }

    protected abstract fun Project.setupSubproject()

    override fun Project.afterProjectEvaluated() {
        if (moduleName.isEmpty()) {
            throw RuntimeException("moduleName must be configured")
        }
        tasks.withType<Test>().configureEach {
            failOnNoDiscoveredTests.set(false)
        }
    }

    protected abstract fun Project.configureSubproject()

    protected fun String.asDependency() = versionCatalog.findLibrary(this).get()

    protected fun String.asAndroidxDependency() = androidxVersionCatalog.findLibrary(this).get()

    @OptIn(ExperimentalAbiValidation::class)
    protected fun AbiValidationExtension.configureKalugaAbi(apiDirectory: Directory) {
        referenceDumpDir.set(apiDirectory)
        filters.exclude {
            byNames.set(
                setOf(
                    "com.splendo.kaluga.$moduleName.BuildConfig",
                )
            )
        }
    }
}
