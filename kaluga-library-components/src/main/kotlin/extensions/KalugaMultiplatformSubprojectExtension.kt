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

package extensions

import com.android.build.gradle.LibraryExtension
import container.AppleInteropContainer
import container.MultiplatformDependencyContainer
import helpers.jvmTarget
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.model.ObjectFactory
import org.gradle.api.publish.PublicationContainer
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.newInstance
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JsModuleKind
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import javax.inject.Inject

open class KalugaMultiplatformSubprojectExtension @Inject constructor(
    versionCatalog: VersionCatalog,
    libraryExtension: LibraryExtension,
    objects: ObjectFactory,
) : BaseKalugaSubprojectExtension(versionCatalog, libraryExtension, null, objects) {

    private enum class IOSTarget {
        X64,
        Arm64,
        SimulatorArm64,
    }

    private val multiplatformDependencies = objects.newInstance(MultiplatformDependencyContainer::class)
    private val appleInterop = objects.newInstance(AppleInteropContainer::class)
    private var frameworkConfig: (Framework.() -> Unit)? = null

    fun dependencies(action: Action<MultiplatformDependencyContainer>) {
        action.execute(multiplatformDependencies)
    }

    fun appleInterop(action: Action<AppleInteropContainer>) {
        action.execute(appleInterop)
    }

    fun framework(action: (Framework.() -> Unit)) {
        frameworkConfig = action
    }
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    override fun Project.configureSubproject() {
        extensions.configure(KotlinMultiplatformExtension::class) {
            compilerOptions {
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
            targets.configureEach {
                compilations.configureEach {
                    compileTaskProvider.configure {
                        compilerOptions {
                            if (this is KotlinJvmCompilerOptions) {
                                jvmTarget.set(versionCatalog.jvmTarget)
                            }
                            freeCompilerArgs.add("-Xexpect-actual-classes")
                        }
                    }
                }
            }

            androidTarget("androidLib") {
                instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
                unitTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
                publishAllLibraryVariants()
            }
            val target: KotlinNativeTarget.() -> Unit =
                {
                    compilations.getByName("main").cinterops.let { mainInterops ->
                        appleInterop.main.forEach { it.execute(mainInterops) }
                    }
                    compilations.getByName("test").cinterops.let { mainInterops ->
                        appleInterop.test.forEach { it.execute(mainInterops) }
                    }
                    binaries {
                        frameworkConfig?.let { iosExport ->
                            framework {
                                iosExport()
                            }
                        }
                        getTest("DEBUG").apply {
                            freeCompilerArgs = freeCompilerArgs + listOf("-e", "com.splendo.kaluga.test.base.mainBackground")
                        }
                    }
                }

            val targets = project.iosTargets
            targets.forEach { iosTarget ->
                when (iosTarget) {
                    IOSTarget.Arm64 -> iosArm64(target)
                    IOSTarget.X64 -> iosX64(target)
                    IOSTarget.SimulatorArm64 -> iosSimulatorArm64(target)
                }
            }

            jvm()
            js(KotlinJsCompilerType.IR) {
                nodejs()
                browser()
                compilations.configureEach {
                    compileTaskProvider.configure {
                        compilerOptions {
                            sourceMap.set(true)
                            moduleKind.set(JsModuleKind.MODULE_UMD)
                        }
                    }
                }
            }

            applyDefaultHierarchyTemplate()

            afterEvaluate {
                sourceSets.getByName("commonMain").apply {
                    dependencies {
                        implementation("kotlinx-coroutines-core".asDependency())
                        multiplatformDependencies.common.mainDependencies.forEach { it.execute(this) }
                    }
                }

                sourceSets.getByName("commonTest").apply {
                    dependencies {
                        implementation(kotlin("test"))
                        implementation(kotlin("test-common"))
                        implementation(kotlin("test-annotations-common"))
                        multiplatformDependencies.common.testDependencies.forEach { it.execute(this) }
                    }
                }

                sourceSets.getByName("androidLibMain").apply {
                    dependencies {
                        androidMainDependencies.forEach { implementation(it) }
                        multiplatformDependencies.android.mainDependencies.forEach { it.execute(this) }
                    }
                }

                sourceSets.getByName("androidLibUnitTest").apply {
                    dependencies {
                        androidTestDependencies.forEach { implementation(it) }
                        multiplatformDependencies.android.testDependencies.forEach { it.execute(this) }
                    }
                }

                sourceSets.getByName("androidLibInstrumentedTest").apply {
                    dependencies {
                        androidInstrumentedTestDependencies.forEach { implementation(it) }
                        multiplatformDependencies.android.instrumentedTestDependencies.forEach { it.execute(this) }
                    }
                }

                sourceSets.getByName("appleMain").apply {
                    dependencies {
                        multiplatformDependencies.apple.mainDependencies.forEach { it.execute(this) }
                    }
                }

                sourceSets.getByName("appleTest").apply {
                    dependencies {
                        multiplatformDependencies.apple.testDependencies.forEach { it.execute(this) }
                    }
                }

                sourceSets.getByName("iosMain").apply {
                    dependencies {
                        multiplatformDependencies.ios.mainDependencies.forEach { it.execute(this) }
                    }
                }

                sourceSets.getByName("iosTest").apply {
                    dependencies {
                        multiplatformDependencies.ios.testDependencies.forEach { it.execute(this) }
                    }
                }

                sourceSets.getByName("jvmMain").apply {
                    dependencies {
                        implementation(kotlin("stdlib"))
                        implementation("kotlinx-coroutines-swing".asDependency())
                        multiplatformDependencies.jvm.mainDependencies.forEach { it.execute(this) }
                    }
                }

                sourceSets.getByName("jvmTest").apply {
                    dependencies {
                        implementation(kotlin("test"))
                        implementation(kotlin("test-junit"))
                        multiplatformDependencies.jvm.testDependencies.forEach { it.execute(this) }
                    }
                }

                sourceSets.getByName("jsMain").apply {
                    dependencies {
                        implementation(kotlin("stdlib-js"))
                        implementation("kotlinx-coroutines-js".asDependency())
                        multiplatformDependencies.js.mainDependencies.forEach { it.execute(this) }
                    }
                }

                sourceSets.getByName("jsTest").apply {
                    dependencies {
                        implementation(kotlin("test-js"))
                        multiplatformDependencies.js.testDependencies.forEach { it.execute(this) }
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
                        if (this@all.name.lowercase().contains("ios")) {
                            optIn("kotlinx.cinterop.ExperimentalForeignApi")
                            optIn("kotlinx.cinterop.BetaInteropApi")
                            optIn("kotlin.experimental.ExperimentalNativeApi")
                        }
                        enableLanguageFeature("InlineClasses")
                    }
                }
            }
        }
    }

    override fun LibraryExtension.configure() {
        @Suppress("UnstableApiUsage")
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
    override fun PublicationContainer.configure(project: Project) {
        getByName("kotlinMultiplatform") {
            (this as MavenPublication).let {
                artifactId = project.name
                groupId = baseGroup
                version = this@KalugaMultiplatformSubprojectExtension.version
            }
        }
    }

    private val Project.iosTargets: Set<IOSTarget> get() {
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

        return when {
            !ideaActive -> IOSTarget.values().toSet()
            isRealIOSDevice -> setOf(IOSTarget.Arm64)
            isAppleSilicon -> setOf(IOSTarget.SimulatorArm64)
            else -> setOf(IOSTarget.X64)
        }
    }
}