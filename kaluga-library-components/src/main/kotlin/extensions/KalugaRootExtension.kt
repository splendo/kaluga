package extensions

import kotlinx.validation.ApiValidationExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.closureOf
import org.gradle.kotlin.dsl.repositories
import org.owasp.dependencycheck.gradle.extension.AnalyzerExtension
import org.owasp.dependencycheck.gradle.extension.DependencyCheckExtension
import javax.inject.Inject

/**
 * A [BaseSplendoHealthExtension] used for a [Project] that is the root of other projects
 */
open class KalugaRootExtension @Inject constructor(
    healthVersionCatalog: VersionCatalog,
    objects: ObjectFactory,
) : BaseKalugaExtension(healthVersionCatalog, objects) {

    /**
     * When `true` all projects will include Maven Local as a repository
     */
    var includeMavenLocal = false

    override fun Project.afterProjectEvaluated() {
        allprojects {
            repositories {
                if (includeMavenLocal) {
                    mavenLocal()
                }
                google()
                mavenCentral()
            }
        }

        generateNonDependentProjectsFileTask()

        afterEvaluate {
            // owasp dependency checker workaround

            // FIXME: If checks for the root project are enabled, a lot of false positives are picked up.
            // In addition it seems like the only working way to skip checks is to create
            // a dummy configuration with no dependencies
            val configuration = configurations.create("dummy")

            DependencyCheckExtension(this).apply {
                analyzers(
                    closureOf<AnalyzerExtension> {
                        assemblyEnabled = false
                    },
                )
                scanConfigurations = listOf(configuration.name)
            }
        }
    }

    private fun Project.generateNonDependentProjectsFileTask() {
        task("generateNonDependentProjectsFile") {
            outputs.upToDateWhen { false }

            val file = project.file("non_dependent_projects.properties")
            file.delete()
            file.appendText("projects=[")
            var firstProject = true

            val blacklist = properties["generateNonDependentProjectsFile.blacklist"]?.toString()?.split(',')?.map { it.trim() } ?: listOf()

            subprojects.forEach { thisProject ->

                val dependsOnOtherProject = subprojects.any { otherProject -> thisProject != otherProject && (thisProject.name.startsWith(otherProject.name) || thisProject.name.endsWith(otherProject.name)) }
                val otherProjectsDependOn = subprojects.any { otherProject -> thisProject != otherProject && (otherProject.name.startsWith(thisProject.name) || otherProject.name.endsWith(thisProject.name)) }

                if (!blacklist.contains(thisProject.name) && (!dependsOnOtherProject || otherProjectsDependOn)) {
                    logger.debug("main module: ${thisProject.name} dependsOnOtherProject:$dependsOnOtherProject otherProjectsDependOn:$otherProjectsDependOn")

                    if (firstProject)
                        firstProject = false
                    else
                        file.appendText(",")
                    file.appendText('"' + thisProject.name + '"')
                } else {
                    logger.debug("not a main module: ${thisProject.name} dependsOnOtherProject:$dependsOnOtherProject otherProjectsDependOn:$otherProjectsDependOn")
                }
            }

            file.appendText("]")
        }
    }

    override fun ApiValidationExtension.apiExtensions() {
        ignoredClasses.add("$baseGroup.permissions.BuildConfig")
        ignoredClasses.add("$baseGroup.test.BuildConfig")
        ignoredClasses.add("$baseGroup.datetime.timer.BuildConfig")
    }
}
