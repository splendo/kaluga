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

import com.splendo.kaluga.plugin.helpers.jvmTarget
import com.splendo.kaluga.plugin.helpers.kalugaVersion
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
import org.jetbrains.kotlin.gradle.dsl.JvmDefaultMode
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
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
        // XXX: re-enable if there are problems with Dokka
        // this is currently sidestepped by excluding scientific units
        // project.extensions.configure(type = DokkaExtension::class) {
        //
        //     // use a separate dokka process
        //     dokkaGeneratorIsolation.set(ProcessIsolation {
        //         maxHeapSize.set("4g")
        //     })
        // }

        project.tasks.withType(KotlinCompile::class.java) {
            compilerOptions {
                jvmTarget.set(versionCatalog.jvmTarget)
                jvmDefault.set(JvmDefaultMode.ENABLE)
            }
        }
        project.beforeEvaluated()
        project.setupPublishingDuringEvaluation()
    }

    private fun Project.setupPublishingDuringEvaluation() {
        project.extensions.configure(MavenPublishBaseExtension::class) {
            coordinates(version = project.kalugaVersion)

            // this specific android config must go early
            if (project.plugins.hasPlugin(com.android.build.gradle.LibraryPlugin::class.java)) {
                configure(
                    platform = AndroidMultiVariantLibrary(
                        sourcesJar = true,
                        publishJavadocJar = false,
                    ),
                )
            }

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

    protected fun Project.setupPublishingAfterEvaluation() {
        project.extensions.configure(MavenPublishBaseExtension::class) {
            // scientific docs take an enormous amount of RAM so we skip them
            when {
                project.plugins.hasPlugin(KotlinMultiplatformPluginWrapper::class.java) -> configure(
                    KotlinMultiplatform(
                        // scientific converters docs take an enormous amount of RAM so we skip them
                        javadocJar = if (project.name.startsWith("converters")) {
                            JavadocJar.Empty()
                        } else {
                            JavadocJar.Dokka("dokkaGeneratePublicationHtml")
                        },
                        sourcesJar = true,
                    ),
                )

                project.plugins.hasPlugin(VersionCatalogPlugin::class.java) -> configure(platform = com.vanniktech.maven.publish.VersionCatalog())

                project.plugins.hasPlugin(com.android.build.gradle.LibraryPlugin::class.java) -> {
                    // noop, android went in before evaluate
                }

                else -> {
                    project.logger.info(
                        "No plugin type detected that can be published for ${project.name}, skipping configuration. Plugins: ${project.plugins.joinToString {
                            it.javaClass.name
                        }}",
                    )
                }
            }
            publishToMavenCentral()
            signAllPublications()
        }
    }

    protected abstract fun Project.beforeEvaluated()

    /**
     * Sets up a [Project] with the configuration of this extension after it has been evaluated.
     */
    @OptIn(ExperimentalStdlibApi::class)
    @JvmName("handleProjectEvaluated")
    fun afterProjectEvaluated(project: Project) {
        project.group = project.mavenGroup()

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

        project.afterProjectEvaluated()
    }

    /**
     * The Maven groupId for this project, following the Gradle project nesting: [BASE_GROUP] plus
     * every path segment *above* the project itself (the artifactId is the project's own name).
     *
     * - `:base` → `com.splendo.kaluga` (flat module)
     * - `:architecture:compose` → `com.splendo.kaluga.architecture` (artifactId `compose`)
     * - `:bluetooth:test-client` → `com.splendo.kaluga.bluetooth` (artifactId `test-client`)
     *
     * Group container projects (e.g. `:architecture`) have no build script and are never published.
     */
    private fun Project.mavenGroup(): String {
        val parentSegments = path.split(":").filter { it.isNotEmpty() }.dropLast(1)
        return (listOf(BASE_GROUP) + parentSegments).joinToString(".")
    }

    /**
     * Abstract setup of a [Project] with the configuration of this extension after it has been evaluated.
     */
    protected abstract fun Project.afterProjectEvaluated()
}
