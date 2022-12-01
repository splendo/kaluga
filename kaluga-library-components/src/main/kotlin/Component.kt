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

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithSimulatorTests
import org.jetbrains.kotlin.konan.file.File

sealed class ComponentType {
    abstract val isApp: Boolean

    data class Default(override val isApp: Boolean = false) : ComponentType()
    data class Compose(override val isApp: Boolean = false) : ComponentType()
}

fun Project.commonComponent() {
    group = Library.group
    version = Library.version
    kotlinMultiplatform {
        commonMultiplatformComponent(this@commonComponent)
    }

    commonAndroidComponent()
    android {
        commonMultiplatformComponentAndroid(this@commonComponent)
    }

    task("printConfigurations") {
        doLast {
            configurations.all { println(this) }
        }
    }

    afterEvaluate {
        Library.IOS.targets.forEach {
            val targetName = it.sourceSetName
            if (tasks.names.contains("linkDebugTest${targetName.capitalize() }")) {
                // creating copy task for the target
                val copyTask = tasks.create("copy${targetName.capitalize() }TestResources", Copy::class.java) {
                    from("src/iosTest/resources/.")
                    into("$buildDir/bin/$targetName/debugTest")
                }

                // apply copy task to the target
                tasks.named("linkDebugTest${targetName.capitalize()}") {
                    dependsOn(copyTask)
                }
            }
        }
    }

    ktlint { disabledRules.set(listOf("no-wildcard-imports", "filename", "import-ordering")) }

    if (Library.connectCheckExpansion) {
        parent?.subprojects?.filter {
            it.name.startsWith("${project.name}-") || it.name.endsWith("-${project.name}")
        }?.forEach {
            logger.info("[connect_check_expansion] :${project.name}:connectedDebugAndroidTest dependsOn:${name}:connectedDebugAndroidTest")
            try {
                tasks.getByPath("connectedDebugAndroidTest")
                    .dependsOn(":${name}:connectedDebugAndroidTest")
            } catch (e : org.gradle.api.UnknownTaskException) {
                logger.info("connectedDebugAndroidTest not supported")
            }
        }
    }
}

fun KotlinMultiplatformExtension.commonMultiplatformComponent(currentProject: Project) {
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
    val targets = currentProject.Library.IOS.targets
    targets.forEach { iosTarget ->
        when (iosTarget) {
            IOSTarget.X64 -> iosX64(target).applyTestDevice(currentProject)
            IOSTarget.Arm64 -> iosArm64(target)
            IOSTarget.SimulatorArm64 -> iosSimulatorArm64(target).applyTestDevice(currentProject)
        }
    }

    jvm()
    js(KotlinJsCompilerType.IR) {
        // Disable JS browser tests for now
        // See https://github.com/splendo/kaluga/issues/97
        // browser()
        nodejs()
        compilations.configureEach {
            kotlinOptions {
                metaInfo = true
                sourceMap = true
                moduleKind = "umd"
            }
        }
    }

    val commonMain = sourceSets.getByName("commonMain").apply {
        dependencies {
            implementationDependency(Dependencies.KotlinX.Coroutines.Core)
        }
    }

    val commonTest = sourceSets.getByName("commonTest").apply {
        dependencies {
            implementation(kotlin("test"))
            implementation(kotlin("test-common"))
            implementation(kotlin("test-annotations-common"))
        }
    }

    sourceSets.getByName("jvmMain").apply {
        dependencies {
            implementation(kotlin("stdlib"))
            implementationDependency(Dependencies.KotlinX.Coroutines.Swing)
        }
    }

    sourceSets.getByName("jvmTest").apply {
        dependsOn(commonTest)
        dependencies {
            implementation(kotlin("test"))
            implementation(kotlin("test-junit"))
        }
    }

    sourceSets.getByName("jsMain").apply {
        dependencies {
            implementation(kotlin("stdlib-js"))
            implementationDependency(Dependencies.KotlinX.Coroutines.Js)
        }
    }

    sourceSets.getByName("jsTest").apply {
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

    targets.forEach {
        val sourceSetName = it.sourceSetName

        sourceSets.getByName("${sourceSetName}Main").apply {
            dependsOn(iosMain)
        }
        sourceSets.getByName("${sourceSetName}Test").apply {
            dependsOn(iosTest)
        }
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

fun LibraryExtension.commonMultiplatformComponentAndroid(project: Project) {
    testOptions {
        unitTests.isReturnDefaultValues = true
    }

    packagingOptions {
        resources.excludes.addAll(
            listOf(
                "META-INF/kotlinx-coroutines-core.kotlin_module",
                "META-INF/shared_debug.kotlin_module",
                "META-INF/kotlinx-serialization-runtime.kotlin_module",
                "META-INF/AL2.0",
                "META-INF/LGPL2.1",
                // bytebuddy 🤡
                "win32-x86-64/attach_hotspot_windows.dll",
                "win32-x86/attach_hotspot_windows.dll",
                "META-INF/licenses/ASM"
            )
        )
    }
}

fun KotlinNativeTargetWithSimulatorTests.applyTestDevice(project: Project) {
    testRuns.all {
        deviceId = project.Library.IOS.TestRunnerDeviceId
    }
}

val IOSTarget.sourceSetName: String get() = when (this) {
    IOSTarget.X64 -> "iosX64"
    IOSTarget.Arm64 -> "iosArm64"
    IOSTarget.SimulatorArm64 -> "iosSimulatorArm64"
}
