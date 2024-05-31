package extensions

import helpers.jvmTarget
import kotlinx.validation.ApiValidationExtension
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLoggingContainer
import org.gradle.kotlin.dsl.configure
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

    /**
     * Compiler args to be added to the kotlinOptions
     */
    val additionalFreeCompilerArgs = mutableListOf<String>()

    /**
     * Sets up a [Project] with the configuration of this extension after it has been evaluated.
     */
    @OptIn(ExperimentalStdlibApi::class)
    @JvmName("handleProjectEvaluated")
    fun afterProjectEvaluated(project: Project) {
        project.group = baseGroup
        project.version = version

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

        project.tasks.withType(KotlinCompile::class.java) {
            compilerOptions {
                jvmTarget.set(versionCatalog.jvmTarget)
                freeCompilerArgs.addAll(*additionalFreeCompilerArgs.toTypedArray(), "-Xjvm-default=all")
            }
        }
        project.extensions.configure(ApiValidationExtension::class) {
            apiExtensions()
        }
        project.afterProjectEvaluated()
    }

    /**
     * Abstract setup of a [Project] with the configuration of this extension after it has been evaluated.
     */
    protected abstract fun Project.afterProjectEvaluated()

    protected abstract fun ApiValidationExtension.apiExtensions()
}
