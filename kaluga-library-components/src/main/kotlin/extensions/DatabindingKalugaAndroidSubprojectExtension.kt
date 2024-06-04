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
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.jetbrains.kotlin.gradle.plugin.LanguageSettingsBuilder
import javax.inject.Inject

open class DatabindingKalugaAndroidSubprojectExtension @Inject constructor(
    versionCatalog: VersionCatalog,
    libraryExtension: LibraryExtension,
    objects: ObjectFactory,
) : BaseKalugaAndroidSubprojectExtension(versionCatalog, libraryExtension, "databinding", objects) {

    override fun LanguageSettingsBuilder.languageSettings() {}

    override fun DependencyHandlerScope.commonDependencies() {}
    override fun LibraryExtension.configure() {
        buildFeatures { dataBinding = true }
    }
}
