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

import java.io.File
import java.io.FileInputStream
import java.util.*

object Library {

    private val props = Properties()
    private const val baseVersion = "1.0.0"
    val version: String by lazy {
        println("------Properties" + props.entries.joinToString(" ") { (key, value) -> "$key to $value" })
        val libraryVersionLocalProperties: String? = props["kaluga.libraryVersion"] as? String
        (libraryVersionLocalProperties ?: "$baseVersion${GitBranch.kalugaBranchPostfix}").also {
            println("Library version $it")
        }
    }
    const val group = "com.splendo.kaluga"

    init {
        val propFile = File("../local.properties")
        if (propFile.exists()) {
            val inputStream = FileInputStream(propFile.absolutePath)
            props.load(inputStream)
        }
    }

    object Android {
        const val minSdk = 21
        const val compileSdk = 33
        const val targetSdk = 33
        const val buildTools = "33.0.0"
        const val composeCompiler = "1.3.2"
    }

    object IOS {
        // based on https://github.com/Kotlin/xcode-compat/blob/d677a43edc46c50888bca0a7890a81f976a42809/xcode-compat/src/main/kotlin/org/jetbrains/kotlin/xcodecompat/XcodeCompatPlugin.kt#L16
        val sdkName = System.getenv("SDK_NAME") ?: "unknown"
        val isRealIOSDevice = sdkName.startsWith("iphoneos").also {
            println("Run on real ios device: $it from sdk: $sdkName")
        }

        // Run on IntelliJ
        val ideaActive = (System.getProperty("idea.active") == "true").also {
            println("Run on IntelliJ: $it")
        }

        // Run on apple silicon
        val isAppleSilicon = (System.getProperty("os.arch") == "aarch64").also {
            println("Run on apple silicon: $it")
        }

        val targets = when {
            !ideaActive -> IOSTarget.values().toSet()
            isRealIOSDevice -> setOf(IOSTarget.Arm64)
            isAppleSilicon -> setOf(IOSTarget.SimulatorArm64)
            else -> setOf(IOSTarget.X64)
        }.also { targets ->
            println("Run on ios targets: ${targets.joinToString(" ") { it.name }}")
        }

        val TestRunnerDeviceId by lazy {
            if (System.getenv().containsKey("IOS_TEST_RUNNER_DEVICE_ID")) {
                System.getenv()["IOS_TEST_RUNNER_DEVICE_ID"].also {
                    println("System env IOS_TEST_RUNNER_DEVICE_ID set to ${System.getenv()["IOS_TEST_RUNNER_DEVICE_ID"]}, using $it")
                }!!
            } else {
                // load some more from local.properties or set defaults.
                val iosTestRunnerDeviceIdLocalProperty: String? =
                    props["kaluga.iosTestRunnerDeviceIdLocalProperty"] as? String
                iosTestRunnerDeviceIdLocalProperty?.also {
                    println("local.properties read (kaluga.iosTestRunnerDeviceIdLocalProperty=$iosTestRunnerDeviceIdLocalProperty, using $it)")
                }
                    ?: "iPhone 14".also {
                        println("local.properties not found, using default value ($it)")
                    }
            }
        }
    }

    val exampleEmbeddingMethod by lazy {
        if (System.getenv().containsKey("EXAMPLE_EMBEDDING_METHOD")) {
            System.getenv()["EXAMPLE_EMBEDDING_METHOD"].also {
                println("System env EXAMPLE_EMBEDDING_METHOD set to ${System.getenv()["EXAMPLE_EMBEDDING_METHOD"]}, using $it")
            }!!
        } else {
            val exampleEmbeddingMethodLocalProperties = props["kaluga.exampleEmbeddingMethod"] as? String
            (exampleEmbeddingMethodLocalProperties ?: "composite").also {
                println("local.properties read (kaluga.exampleEmbeddingMethod=$exampleEmbeddingMethodLocalProperties, using $it)")
            }
        }
    }

    val exampleMavenRepo by lazy {
        if (System.getenv().containsKey("EXAMPLE_MAVEN_REPO")) {
            System.getenv()["EXAMPLE_MAVEN_REPO"].also {
                println("System env EXAMPLE_MAVEN_REPO set to ${System.getenv()["EXAMPLE_MAVEN_REPO"]}, using $it")
            }!!
        } else {
            // load some more from local.properties or set defaults.
            val exampleMavenRepoLocalProperties: String? =
                props["kaluga.exampleMavenRepo"] as? String
            exampleMavenRepoLocalProperties?.also {
                println("local.properties read (kaluga.exampleMavenRepo=$exampleMavenRepoLocalProperties, using $it)")
            }
                ?: "local".also {
                    println("local.properties not found, using default value ($it)")
                }
        }
    }

    val connectCheckExpansion = (System.getenv().containsKey("CONNECTED_CHECK_EXPANSION") or System.getenv().containsKey("CI")).also {
        if (it) {
            println("Adding extra dependend task to connected checks of similarly named modules (CONNECTED_CHECK_EXPANSION env present: ${ System.getenv().containsKey("CONNECTED_CHECK_EXPANSION") })")
        }
    }
}

enum class IOSTarget {
    X64,
    Arm64,
    SimulatorArm64
}
