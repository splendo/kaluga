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

import com.android.build.gradle.LibraryPlugin
import extensions.KalugaAndroidSubprojectExtension
import extensions.KalugaMultiplatformSubprojectExtension
import extensions.KalugaRootExtension
import kotlinx.validation.BinaryCompatibilityValidatorPlugin
import org.gradle.api.InvalidUserDataException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.UnknownDomainObjectException
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.hasPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformAndroidPlugin
import org.jmailen.gradle.kotlinter.KotlinterPlugin
import org.owasp.dependencycheck.gradle.DependencyCheckPlugin

class LibraryComponentsPlugin: Plugin<Project> {

    companion object {
        const val EXTENSION_NAME = "kaluga"
    }
    override fun apply(target: Project) = target.run {
        val versionCatalog: VersionCatalog = try {
            extensions.getByType(VersionCatalogsExtension::class.java).named("libs")
        } catch (e: UnknownDomainObjectException) {
            project.logger.error("Missing Version Catalog Extension. Make sure the version catalog provided by this plugin is linked to your project as \"libs\"")
            throw e
        } catch (e: InvalidUserDataException) {
            project.logger.error(
                "Missing Version Catalog named \"libs\". " +
                    "Make sure the version catalog provided by this plugin is linked to your project as \"libs\"",
            )
            throw e
        }
        pluginManager.apply(KotlinterPlugin::class)
        pluginManager.apply(DependencyCheckPlugin::class)
        pluginManager.apply(BinaryCompatibilityValidatorPlugin::class)
        if (this != rootProject) {
            pluginManager.apply(LibraryPlugin::class)
        }

        val kalugaExtension = when {
            rootProject == this -> {
                extensions.create(EXTENSION_NAME, KalugaRootExtension::class, versionCatalog)
            }
            plugins.hasPlugin(KotlinPlatformAndroidPlugin::class) -> {
                extensions.create(EXTENSION_NAME, KalugaAndroidSubprojectExtension::class.java, versionCatalog)
            }
            else -> {
                pluginManager.apply(KotlinMultiplatformPluginWrapper::class)
                extensions.create(EXTENSION_NAME, KalugaMultiplatformSubprojectExtension::class.java, versionCatalog)
            }
        }

        afterEvaluate {
            kalugaExtension.afterProjectEvaluated(this)
        }
    }
}
