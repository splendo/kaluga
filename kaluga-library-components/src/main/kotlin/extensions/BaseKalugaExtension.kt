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

import com.android.build.gradle.LibraryPlugin
import com.splendo.kaluga.plugin.helpers.kalugaVersion
import com.splendo.kaluga.plugin.helpers.jvmTarget
import com.vanniktech.maven.publish.AndroidMultiVariantLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.model.ObjectFactory
import org.gradle.api.plugins.catalog.VersionCatalogPlugin
import org.gradle.api.provider.Property
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.api.tasks.testing.logging.TestLoggingContainer
import org.gradle.kotlin.dsl.configure
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * An extension to a [Project] that allows for easily reusing logic in Splendo Health
 */
sealed class BaseKalugaExtension(protected val versionCatalog: VersionCatalog, objects: ObjectFactory) {

    companion object {
        const val BASE_GROUP = "com.splendo.kaluga"
    }

    internal data class TestLoggingContainerAction(val action: Action<in TestLoggingContainer>)
    private val testLogging: Property<TestLoggingContainerAction> = objects.property(TestLoggingContainerAction::class.java)

    /**
     * Configure a [TestLoggingContainer]
     */
    fun testLogging(action: Action<in TestLoggingContainer>) {
        testLogging.set(TestLoggingContainerAction(action))
    }

    fun beforeProjectEvaluated(project: Project) {

        project.extensions.configure(type = DokkaExtension::class) {

            // use a separate dokka process
            dokkaGeneratorIsolation.set(ProcessIsolation {
                maxHeapSize.set("4g")
            })
        }

        project.tasks.withType(KotlinCompile::class.java) {
            compilerOptions {
                jvmTarget.set(versionCatalog.jvmTarget)
                freeCompilerArgs.addAll("-Xjvm-default=all")
            }
        }

        project.beforeEvaluated()



        project.extensions.configure(MavenPublishBaseExtension::class) {

            coordinates(version = versionCatalog.findVersion("kaluga").get().displayName)

            when {
                project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") -> configure(
                    KotlinMultiplatform(
                        // scientific docs take an enormous amount of RAM so we skip them
                        javadocJar = if (project.name.startsWith("scientific"))
                            JavadocJar.Empty()
                        else
                            JavadocJar.Dokka("dokkaGeneratePublicationHtml"),
                        androidVariantsToPublish = listOf("debug", "release"),
                        sourcesJar = true,
                    )
                )

                project.plugins.hasPlugin(LibraryPlugin::class.java) -> {
                    configure(platform = AndroidMultiVariantLibrary(
                        sourcesJar = true,
                        publishJavadocJar = false,
                    )
                    )
                }

                project.plugins.hasPlugin(VersionCatalogPlugin::class.java) -> configure(platform = com.vanniktech.maven.publish.VersionCatalog()) // TODO: This is not correct, it should be a platform
                else -> {
                    project.logger.info("No plugin type detected that can be published for ${project.name}, skipping configuration. Plugins: ${project.plugins.joinToString { it.javaClass.name }}")
                }
            }

            publishToMavenCentral()
            signAllPublications()

            // Provide artifacts information requited by Maven Central
            pom {
                name.set(project.name)
                description.set("Collection of Kotlin Flow based libraries")
                url.set("https://github.com/splendo/kaluga")

                licenses {
                    license {
                        name.set("The Apache Software License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("splendo")
                        name.set("Splendo Consulting BV")
                    }
                }
                scm {
                    url.set("https://github.com/splendo/kaluga")
                }
            }
        }
    }

    protected abstract fun Project.beforeEvaluated()

    /**
     * Sets up a [Project] with the configuration of this extension after it has been evaluated.
     */
    @OptIn(ExperimentalStdlibApi::class)
    @JvmName("handleProjectEvaluated")
    fun afterProjectEvaluated(project: Project) {
        project.group = BASE_GROUP

        project.tasks.withType(Test::class.java) {
            testLogging {
                events = TestLogEvent.entries.toSet()
                exceptionFormat = TestExceptionFormat.FULL
                showExceptions = true
                showCauses = true
                showStackTraces = true
                this@BaseKalugaExtension.testLogging.orNull?.action?.execute(this)
            }
        }

        project.logger.lifecycle("Publishing Kaluga version: ${project.kalugaVersion}")

        listOf("mavenCentralUsername", "mavenCentralPassword", "signingInMemoryKey", "signingInMemoryKeyId", "signingInMemoryKeyPassword").forEach { property ->
            val value = project.providers.gradleProperty(property).getOrElse("missing")
            if (value == "missing") println("⚠️'$property' is not set for project ${project.name}. Publishing to Maven Central will fail.")
            else println("✅ '$property' is present: chars: ${value.length}, lines: ${value.lines().size}")

        }
        project.afterProjectEvaluated()
    }

    /**
     * Abstract setup of a [Project] with the configuration of this extension after it has been evaluated.
     */
    protected abstract fun Project.afterProjectEvaluated()

}