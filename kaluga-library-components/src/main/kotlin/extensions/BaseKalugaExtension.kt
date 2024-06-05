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

import com.splendo.kaluga.plugin.helpers.gitBranch
import com.splendo.kaluga.plugin.helpers.jvmTarget
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLoggingContainer
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.SigningExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * An extension to a [Project] that allows for easily reusing logic in Splendo Health
 */
sealed class BaseKalugaExtension(protected val versionCatalog: VersionCatalog, objects: ObjectFactory) {

    companion object {
        const val BASE_GROUP = "com.splendo.kaluga"
        private const val OSSRH_USERNAME = "OSSRH_USERNAME"
        private const val OSSRH_PASSWORD = "OSSRH_PASSWORD"
        private const val SIGNING_KEY_ID = "SIGNING_KEY_ID"
        private const val SIGNING_PASSWORD = "SIGNING_PASSWORD"
        private const val SIGNING_SECRET_KEY_RING_FILE = "SIGNING_SECRET_KEY_RING_FILE"
    }

    val version = versionCatalog.findVersion("kaluga").get().displayName
    internal data class TestLoggingContainerAction(val action: Action<in TestLoggingContainer>)
    private val testLogging: Property<TestLoggingContainerAction> = objects.property(TestLoggingContainerAction::class.java)

    /**
     * Configure a [TestLoggingContainer]
     */
    fun testLogging(action: Action<in TestLoggingContainer>) {
        testLogging.set(TestLoggingContainerAction(action))
    }

    fun beforeProjectEvaluated(project: Project) {
        project.tasks.withType(KotlinCompile::class.java) {
            compilerOptions {
                jvmTarget.set(versionCatalog.jvmTarget)
                freeCompilerArgs.addAll("-Xjvm-default=all")
            }
        }
        project.beforeEvaluated()
    }

    protected abstract fun Project.beforeEvaluated()

    /**
     * Sets up a [Project] with the configuration of this extension after it has been evaluated.
     */
    @OptIn(ExperimentalStdlibApi::class)
    @JvmName("handleProjectEvaluated")
    fun afterProjectEvaluated(project: Project) {
        project.group = BASE_GROUP
        project.version = "$version${project.gitBranch.kalugaBranchPostfix}"

        project.tasks.withType(Test::class.java) {
            testLogging {
                events = org.gradle.api.tasks.testing.logging.TestLogEvent.entries.toSet()
                exceptionFormat = TestExceptionFormat.FULL
                showExceptions = true
                showCauses = true
                showStackTraces = true
                this@BaseKalugaExtension.testLogging.orNull?.action?.execute(this)
            }
        }

        project.extensions.configure(SigningExtension::class) {
            setRequired(
                {
                    project.gradle.taskGraph.hasTask("publish") && project.signingSecretKeyRingFile != null
                },
            )
        }

        val jarTask by project.tasks.registering(Jar::class) {
            archiveClassifier.set("javadoc")
        }

        project.extensions.configure(PublishingExtension::class) {
            configureRepositories(project)
            // Configure all publications
            publications.withType(MavenPublication::class) {
                // Stub javadoc.jar artifact
                artifact(jarTask.get())

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
                            name.set("Splendo BV")
                        }
                    }
                    scm {
                        url.set("https://github.com/splendo/kaluga")
                    }
                }
            }

            project.extensions.configure(SigningExtension::class) {
                val signingKeyId = project.getStringPropertyOrSystemEnvironment("signingKeyId", SIGNING_KEY_ID)
                val signingPassword = project.getStringPropertyOrSystemEnvironment("signingPassword", SIGNING_PASSWORD)

                useInMemoryPgpKeys(signingKeyId, signingPassword, project.signingSecretKeyRingFile)
                sign(publications)
            }
        }

        project.afterProjectEvaluated()
    }

    /**
     * Abstract setup of a [Project] with the configuration of this extension after it has been evaluated.
     */
    protected abstract fun Project.afterProjectEvaluated()

    private fun PublishingExtension.configureRepositories(project: Project) {
        val ossrhUsername = project.getStringPropertyOrSystemEnvironment("ossrhUsername", OSSRH_USERNAME)
        val ossrhPassword = project.getStringPropertyOrSystemEnvironment("ossrhPassword", OSSRH_PASSWORD)

        repositories {
            maven {
                name = "sonatype"
                setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = ossrhUsername
                    password = ossrhPassword
                }
            }

            maven {
                name = "snapshots"
                setUrl("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                credentials {
                    username = ossrhUsername
                    password = ossrhPassword
                }
            }
        }
    }

    private val Project.signingSecretKeyRingFile: String? get() = getStringPropertyOrSystemEnvironment("signingSecretKeyRingFile", SIGNING_SECRET_KEY_RING_FILE)
    private fun Project.getStringPropertyOrSystemEnvironment(propertyName: String, environmentPropertyName: String): String? =
        properties[propertyName] as? String ?: System.getenv(environmentPropertyName)
}
