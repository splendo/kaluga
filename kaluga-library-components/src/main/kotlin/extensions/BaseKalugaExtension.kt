package extensions

import helpers.GitBranch
import helpers.jvmTarget
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
import org.gradle.kotlin.dsl.extra
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
        const val baseGroup = "com.splendo.kaluga"
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
        project.group = baseGroup
        project.version = "$version${project.GitBranch.kalugaBranchPostfix}"

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
                    project.gradle.taskGraph.hasTask("publish") && project.extra.get("signingSecretKeyRingFile") != null
                }
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
        val signingKeyId: String? by project
        val signingPassword: String? by project
        val signingSecretKeyRingFile: String? by project
        val ossrhUsername: String? by project
        val ossrhPassword: String? by project

        // Stub secrets to let the project sync and build without the publication values set up
        project.extra["signing.keyId"] = signingKeyId ?: System.getenv("SIGNING_KEY_ID")
        project.extra["signing.password"] = signingPassword ?: System.getenv("SIGNING_PASSWORD")
        project.extra["signing.secretKeyRingFile"] = signingSecretKeyRingFile ?: System.getenv("SIGNING_SECRET_KEY_RING_FILE")
        project.extra["ossrhUsername"] = ossrhUsername ?: System.getenv("OSSRH_USERNAME")
        project.extra["ossrhPassword"] = ossrhPassword ?: System.getenv("OSSRH_PASSWORD")

        repositories {
            maven {
                name = "sonatype"
                setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = project.extra["ossrhUsername"].toString()
                    password = project.extra["ossrhPassword"].toString()
                }
            }

            maven {
                name = "snapshots"
                setUrl("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                credentials {
                    username = project.extra["ossrhUsername"].toString()
                    password = project.extra["ossrhPassword"].toString()
                }
            }
        }
    }
}
