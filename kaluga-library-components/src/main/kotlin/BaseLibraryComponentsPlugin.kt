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

package com.splendo.kaluga.plugin

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.splendo.kaluga.plugin.extensions.BaseKalugaSubprojectExtension
import com.splendo.kaluga.plugin.extensions.KalugaRootExtension
import kotlinx.kover.gradle.plugin.KoverGradlePlugin
import kotlinx.validation.BinaryCompatibilityValidatorPlugin
import org.gradle.api.InvalidUserDataException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.UnknownDomainObjectException
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.PluginManager
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import org.gradle.plugins.signing.SigningPlugin
import org.jetbrains.dokka.gradle.DokkaPlugin
import org.jmailen.gradle.kotlinter.KotlinterPlugin
import org.owasp.dependencycheck.gradle.DependencyCheckPlugin
import kotlin.reflect.KClass

abstract class BaseLibraryComponentsPlugin<SubExtension : BaseKalugaSubprojectExtension> : Plugin<Project> {

    companion object {
        const val EXTENSION_NAME = "kaluga"
    }

    protected abstract val subExtensionClass: KClass<SubExtension>

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
        pluginManager.apply(MavenPublishPlugin::class)
        pluginManager.apply(SigningPlugin::class)
        pluginManager.apply(KotlinterPlugin::class)
        pluginManager.apply(DependencyCheckPlugin::class)
        pluginManager.apply(DokkaPlugin::class)

        val kalugaExtension = when {
            rootProject == this -> {
                pluginManager.apply(KoverGradlePlugin::class)
                pluginManager.apply(BinaryCompatibilityValidatorPlugin::class)
                extensions.create(EXTENSION_NAME, KalugaRootExtension::class, versionCatalog)
            }
            else -> {
                pluginManager.apply(LibraryPlugin::class)
                pluginManager.addSubprojectExtensionPlugins()
                extensions.create(EXTENSION_NAME, subExtensionClass, versionCatalog, project.extensions.findByType(LibraryExtension::class)!!)
            }
        }

        kalugaExtension.beforeProjectEvaluated(this)
        afterEvaluate {
            kalugaExtension.afterProjectEvaluated(this)
        }
    }

    protected abstract fun PluginManager.addSubprojectExtensionPlugins()
}
