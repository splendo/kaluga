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

import org.gradle.api.Action
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithSimulatorTests

fun org.gradle.api.Project.commonComponent() {
    val action = object : Action<KotlinMultiplatformExtension> {
        override fun execute(t: KotlinMultiplatformExtension) {
            t.commonMultiplatformComponent()
        }
    }
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("kotlin", action)
}

fun KotlinMultiplatformExtension.commonMultiplatformComponent() {
    targets {
        configureEach {
            compilations.configureEach {
                (kotlinOptions as? KotlinJvmOptions)?.jvmTarget = "1.8"
            }
        }
    }

    android("androidLib").publishAllLibraryVariants()
    val target: KotlinNativeTarget.() -> Unit =
        {
            binaries {
                getTest("DEBUG").apply {
                    freeCompilerArgs = freeCompilerArgs + listOf("-e", "com.splendo.kaluga.test.base.mainBackground")
                }
            }
        }
    Library.IOS.targets.forEach { iosTarget ->
        when (iosTarget) {
            IOSTarget.X64 -> iosX64(target).applyTestDevice()
            IOSTarget.Arm64 -> iosArm64(target)
            IOSTarget.SimulatorArm64 -> iosSimulatorArm64(target).applyTestDevice()
        }
    }

    jvm()
    js(KotlinJsCompilerType.IR) {
        nodejs()
        compilations.configureEach {
            kotlinOptions {
                metaInfo = true
                sourceMap = true
                moduleKind = "umd"
            }
        }
    }

    val commonMain = sourceSets.maybeCreate("commonMain").apply {
        dependencies {
            implement(Dependencies.KotlinX.Coroutines.Core)
        }
    }

    val commonTest = sourceSets.maybeCreate("commonTest").apply {
        dependencies {
            implementation(kotlin("test"))
            implementation(kotlin("test-common"))
            implementation(kotlin("test-annotations-common"))
        }
    }

    val jvmMain = sourceSets.maybeCreate("jvmMain").apply {
        dependencies {
            implementation(kotlin("stdlib"))
            implement(Dependencies.KotlinX.Coroutines.Swing)
        }
    }

    val jvmTest = sourceSets.maybeCreate("jvmTest").apply {
        dependsOn(commonTest)
        dependencies {
            implementation(kotlin("test"))
            implementation(kotlin("test-junit"))
        }
    }

    val jsMain = sourceSets.maybeCreate("jsMain").apply {
        dependencies {
            implementation(kotlin("stdlib-js"))
            implement(Dependencies.KotlinX.Coroutines.Js)
        }
    }

    val jsTest = sourceSets.maybeCreate("jsTest").apply {
        dependencies {
            implementation(kotlin("test-js"))
        }
    }

    val iosMain = sourceSets.maybeCreate("iosMain").apply {
        dependsOn(commonMain)
    }

    val iosTest = sourceSets.maybeCreate("iosTest").apply {
        dependsOn(commonTest)
    }

    sourceSets.all {
        languageSettings {
            optIn("kotlinx.coroutines.DelicateCoroutinesApi")
            optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            optIn("kotlinx.coroutines.ObsoleteCoroutinesApi")
            optIn("kotlinx.coroutines.InternalCoroutinesApi")
            optIn("kotlinx.coroutines.FlowPreview")
            optIn("kotlin.ExperimentalUnsignedTypes")
            optIn("kotlin.ExperimentalStdlibApi")
            optIn("kotlin.time.ExperimentalTime")
            optIn("kotlin.ExperimentalStdlibApi")
            enableLanguageFeature("InlineClasses")
        }
    }
}

fun KotlinNativeTargetWithSimulatorTests.applyTestDevice() {
    testRuns.all {
        deviceId = Library.IOS.TestRunnerDeviceId
    }
}