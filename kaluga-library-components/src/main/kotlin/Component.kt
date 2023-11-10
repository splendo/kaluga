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
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType
import org.jetbrains.kotlin.gradle.plugin.mpp.DefaultCInteropSettings
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithSimulatorTests
import org.jmailen.gradle.kotlinter.tasks.LintTask

sealed class ComponentType {
    object Default : ComponentType()
    object Compose : ComponentType()
    object DataBinding : ComponentType()
}

fun Project.commonComponent(
    packageName: String,
    iosMainInterop: (NamedDomainObjectContainer<DefaultCInteropSettings>.() -> Unit)? = null,
    iosTestInterop: (NamedDomainObjectContainer<DefaultCInteropSettings>.() -> Unit)? = null,
    iosExport: (Framework.() -> Unit)? = null
) {
    group = Library.group
    version = Library.version
    kotlinMultiplatform {
        commonMultiplatformComponent(this@commonComponent, iosMainInterop, iosTestInterop, iosExport)
    }

    commonAndroidComponent(packageName = packageName)
    androidLibrary {
        commonMultiplatformComponentAndroid()
    }

    task("printConfigurations") {
        doLast {
            configurations.all { println(this) }
        }
    }

    // output all reports to a single location
    tasks.withType<LintTask>().configureEach {
        reports.set(mapOf("plain" to rootProject.layout.buildDirectory.get().asFile.resolve("reports/ktlint/${project.path}-${this.name}.txt")))
    }

    afterEvaluate {
        Library.IOS.targets.forEach { target ->
            val targetName = target.sourceSetName
            if (tasks.names.contains("linkDebugTest${targetName.replaceFirstChar { it.titlecase() } }")) {
                // creating copy task for the target
                val copyTask = tasks.create("copy${targetName.replaceFirstChar { it.titlecase() } }TestResources", Copy::class.java) {
                    from("src/iosTest/resources/.")
                    into("${layout.buildDirectory.get().asFile}/bin/$targetName/debugTest")
                }

                // apply copy task to the target
                tasks.named("linkDebugTest${targetName.replaceFirstChar { it.titlecase() }}") {
                    dependsOn(copyTask)
                }
            }
        }
    }

    if (Library.enableDependentProjects) {
        parent?.subprojects?.filter {
            it.name.startsWith("${project.name}-") || it.name.endsWith("-${project.name}")
        }?.forEach { module ->
            afterEvaluate {
                logger.info("[connect_check_expansion] :${project.name}:connectedDebugAndroidTest dependsOn:${module.name}:connectedDebugAndroidTest")
                tasks.getByPath("connectedDebugAndroidTest")
                    .dependsOn(":${module.name}:connectedDebugAndroidTest")
            }
        }
    }
}

fun KotlinMultiplatformExtension.commonMultiplatformComponent(
    currentProject: Project,
    iosMainInterop: (NamedDomainObjectContainer<DefaultCInteropSettings>.() -> Unit)? = null,
    iosTestInterop: (NamedDomainObjectContainer<DefaultCInteropSettings>.() -> Unit)? = null,
    iosExport: (Framework.() -> Unit)? = null
) {
    targets.configureEach {
        compilations.configureEach {
            (kotlinOptions as? KotlinJvmOptions)?.jvmTarget = "11"
            kotlinOptions.freeCompilerArgs += "-Xexpect-actual-classes"
        }
    }

    androidTarget("androidLib").publishAllLibraryVariants()
    val target: KotlinNativeTarget.() -> Unit =
        {
            iosMainInterop?.let { mainInterop ->
                compilations.getByName("main").cinterops.mainInterop()
            }
            iosTestInterop?.let { testInterop ->
                compilations.getByName("test").cinterops.testInterop()
            }
            binaries {
                iosExport?.let { iosExport ->
                    framework {
                        iosExport()
                    }
                }
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
        browser()
        nodejs()
        compilations.configureEach {
            kotlinOptions {
                metaInfo = true
                sourceMap = true
                moduleKind = "umd"
            }
        }
        binaries.executable()
    }

    applyDefaultHierarchyTemplate()

    sourceSets.getByName("commonMain").apply {
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

    sourceSets.maybeCreate("androidLibInstrumentedTest").apply {
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
            if (this@all.name.lowercase().contains("ios")) {
                optIn("kotlinx.cinterop.ExperimentalForeignApi")
                optIn("kotlinx.cinterop.BetaInteropApi")
                optIn("kotlin.experimental.ExperimentalNativeApi")
            }
            enableLanguageFeature("InlineClasses")
        }
    }
}

fun LibraryExtension.commonMultiplatformComponentAndroid() {
    testOptions {
        unitTests.isReturnDefaultValues = true
    }

    packaging {
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
                "META-INF/licenses/ASM",
            ),
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
