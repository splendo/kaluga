/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import extensions.KalugaMultiplatformSubprojectExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper

class MultiplatformLibraryComponentsPlugin: BaseLibraryComponentsPlugin<KalugaMultiplatformSubprojectExtension>() {

    override fun Project.createBaseKalugaSubprojectExtension(versionCatalog: VersionCatalog): KalugaMultiplatformSubprojectExtension {
        pluginManager.apply(KotlinMultiplatformPluginWrapper::class)
        pluginManager.apply(LibraryPlugin::class)
        return extensions.create(EXTENSION_NAME, KalugaMultiplatformSubprojectExtension::class.java, versionCatalog, project.extensions.findByType(LibraryExtension::class))
    }
}
