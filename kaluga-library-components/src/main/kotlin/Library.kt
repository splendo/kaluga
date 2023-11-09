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

import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.kotlin.dsl.extra
import org.jetbrains.kotlin.konan.file.File
import org.jetbrains.kotlin.konan.properties.Properties
import org.jetbrains.kotlin.konan.properties.loadProperties
import java.io.IOException

private val libraries: MutableMap<Project, LibraryImpl> = mutableMapOf()

/**
 * Gets a [LibraryImpl] for the [Project]. Only creates a new instance of the Library if none exist yet, to speed up the build process
 */
val Project.Library get() = libraries.getOrPut(this) { LibraryImpl(this) }

class LibraryImpl(project: Project) {

    private val props: Properties = File("${project.rootProject.buildDir.absolutePath}/../local.properties").let { file ->
        if (file.exists) {
            file.loadProperties()
        } else {
            Properties()
        }
    }
    private val logger = project.logger
    private val baseVersion = "1.3.0"
    val group = "com.splendo.kaluga"
    val version: String by lazy {
        val libraryVersionLocalProperties: String? = props["kaluga.libraryVersion"] as? String
        (libraryVersionLocalProperties ?: "$baseVersion${project.GitBranch.kalugaBranchPostfix}").also {
            logger.info("Library version $it")
        }
    }
    val kotlinVersion = project.extra["kaluga.kotlinVersion"] as? String ?: kotlin.run {
        logger.lifecycle("Missing kotlin version")
        throw IOException("Provide kaluga.kotlinVersion in your gradle.properties")
    }

    object Android {
        const val minSdk = 23
        const val compileSdk = 34
        const val targetSdk = 34
        const val buildTools = "34.0.0"
        const val composeCompiler = "1.5.4"
    }

    class IOSLibrary(props: Properties, logger: Logger) {
        // based on https://github.com/Kotlin/xcode-compat/blob/d677a43edc46c50888bca0a7890a81f976a42809/xcode-compat/src/main/kotlin/org/jetbrains/kotlin/xcodecompat/XcodeCompatPlugin.kt#L16
        val sdkName = System.getenv("SDK_NAME") ?: "unknown"
        val isRealIOSDevice = sdkName.startsWith("iphoneos").also {
            logger.info("Run on real ios device: $it from sdk: $sdkName")
        }

        // Run on IntelliJ
        val ideaActive = (System.getProperty("idea.active") == "true").also {
            logger.info("Run on IntelliJ: $it")
        }

        // Run on apple silicon
        val isAppleSilicon = (System.getProperty("os.arch") == "aarch64").also {
            logger.info("Run on apple silicon: $it")
        }

        val targets = when {
            !ideaActive -> IOSTarget.values().toSet()
            isRealIOSDevice -> setOf(IOSTarget.Arm64)
            isAppleSilicon -> setOf(IOSTarget.SimulatorArm64)
            else -> setOf(IOSTarget.X64)
        }.also { targets ->
            logger.info("Run on ios targets: ${targets.joinToString(" ") { it.name }}")
        }

        val TestRunnerDeviceId by lazy {
            if (System.getenv().containsKey("IOS_TEST_RUNNER_DEVICE_ID")) {
                System.getenv()["IOS_TEST_RUNNER_DEVICE_ID"].also {
                    logger.lifecycle("System env IOS_TEST_RUNNER_DEVICE_ID set to ${System.getenv()["IOS_TEST_RUNNER_DEVICE_ID"]}, using $it")
                }!!
            } else {
                // load some more from local.properties or set defaults.
                val iosTestRunnerDeviceIdLocalProperty: String? =
                    props["kaluga.iosTestRunnerDeviceIdLocalProperty"] as? String
                iosTestRunnerDeviceIdLocalProperty?.also {
                    logger.lifecycle("local.properties read (kaluga.iosTestRunnerDeviceIdLocalProperty=$iosTestRunnerDeviceIdLocalProperty, using $it)")
                }
                    ?: "iPhone 14".also {
                        logger.info("local.properties not found, using default value ($it)")
                    }
            }
        }
    }
    val IOS = IOSLibrary(props, logger)

    private val testDependentProjectsEnvName = "TEST_DEPENDENT_PROJECTS"
    private val testDependentProjects = System.getenv().containsKey(testDependentProjectsEnvName)

    private val onCiEnvName = "CI"
    private val onCI = System.getenv().containsKey(onCiEnvName)

    val enableDependentProjects = (testDependentProjects or onCI).also {
        if (it) {
            logger.info("Adding extra dependent tasks to test tasks of similarly named modules ($testDependentProjectsEnvName env present: $testDependentProjects $onCiEnvName env present: $onCI)")
        }
    }
}

enum class IOSTarget {
    X64,
    Arm64,
    SimulatorArm64,
}
