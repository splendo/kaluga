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

import com.android.build.gradle.LibraryExtension
import com.splendo.kaluga.plugin.container.AppleInteropContainer
import com.splendo.kaluga.plugin.container.MultiplatformDependencyContainer
import com.splendo.kaluga.plugin.helpers.jvmTarget
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.model.ObjectFactory
import org.gradle.api.publish.PublicationContainer
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.newInstance
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JsModuleKind
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jmailen.gradle.kotlinter.tasks.LintTask
import javax.inject.Inject

open class KalugaMultiplatformSubprojectExtension @Inject constructor(
    versionCatalog: VersionCatalog,
    libraryExtension: LibraryExtension,
    objects: ObjectFactory,
) : BaseKalugaSubprojectExtension(versionCatalog, libraryExtension, null, objects) {

    companion object {
        private val testDependentProjectsEnvName = "TEST_DEPENDENT_PROJECTS"
        private val onCiEnvName = "CI"
    }
    private enum class IOSTarget(val sourceSetName: String) {
        X64("iosX64"),
        Arm64("iosArm64"),
        SimulatorArm64("iosSimulatorArm64"),
    }

    var supportJVM: Boolean = false
    var supportJS: Boolean = false

    private val multiplatformDependencies = objects.newInstance(MultiplatformDependencyContainer::class)
    private val appleInterop = objects.newInstance(AppleInteropContainer::class)
    private var frameworkConfig: (Framework.() -> Unit)? = null

    fun dependencies(action: Action<MultiplatformDependencyContainer>) {
        action.execute(multiplatformDependencies)
    }

    fun appleInterop(action: Action<AppleInteropContainer>) {
        action.execute(appleInterop)
    }

    fun appleFramework(action: (Framework.() -> Unit)) {
        frameworkConfig = action
    }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    override fun Project.setupSubproject() {
        // Android Target must be setup before project is evaluated as publishing will break otherwise
        extensions.configure(KotlinMultiplatformExtension::class) {
            androidTarget("androidLib") {
                instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
                unitTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
                publishAllLibraryVariants()
            }
        }
    }

    override fun Project.configureSubproject() {
        extensions.configure(KotlinMultiplatformExtension::class) {
            configureMultiplatform(this@configureSubproject)
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
            iosTargets.forEach { target ->
                val targetName = target.sourceSetName
                if (tasks.names.contains("linkDebugTest${targetName.replaceFirstChar { it.titlecase() } }")) {
                    // creating copy task for the target
                    val copyTask = tasks.create("copy${targetName.replaceFirstChar { it.titlecase() } }TestResources", Copy::class) {
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

        if (listOf(testDependentProjectsEnvName, onCiEnvName).any { System.getenv().containsKey(it) }) {
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

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    private fun KotlinMultiplatformExtension.configureMultiplatform(project: Project) {
        compilerOptions {
            freeCompilerArgs.addAll("-Xexpect-actual-classes", "-Xconsistent-data-class-copy-visibility")
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

        val iosTargets = project.iosTargets.map { iosTarget ->
            when (iosTarget) {
                IOSTarget.Arm64 -> iosArm64()
                IOSTarget.X64 -> iosX64()
                IOSTarget.SimulatorArm64 -> iosSimulatorArm64()
            }
        }

        if (supportJVM) {
            jvm()
        }
        if (supportJS) {
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
        }

        applyDefaultHierarchyTemplate()

        project.afterEvaluate {
            with(sourceSets) {
                commonMain.configure {
                    dependencies {
                        implementation("kotlinx-coroutines-core".asDependency())
                        multiplatformDependencies.common.mainDependencies.forEach { it.execute(this) }
                    }
                }

                commonTest.configure {
                    dependencies {
                        implementation(kotlin("test"))
                        implementation(kotlin("test-common"))
                        implementation(kotlin("test-annotations-common"))
                        multiplatformDependencies.common.testDependencies.forEach { it.execute(this) }
                    }
                }

                getByName("androidLibMain").apply {
                    dependencies {
                        androidMainDependencies.forEach { implementation(it) }
                        multiplatformDependencies.android.mainDependencies.forEach { it.execute(this) }
                    }
                }

                getByName("androidLibUnitTest").apply {
                    dependencies {
                        androidTestDependencies.forEach { implementation(it) }
                        multiplatformDependencies.android.testDependencies.forEach { it.execute(this) }
                    }
                }

                getByName("androidLibInstrumentedTest").apply {
                    dependencies {
                        androidInstrumentedTestDependencies.forEach { implementation(it) }
                        multiplatformDependencies.android.instrumentedTestDependencies.forEach { it.execute(this) }
                    }
                }

                if (multiplatformDependencies.apple.mainDependencies.isNotEmpty()) {
                    appleMain.configure {
                        dependencies {
                            multiplatformDependencies.apple.mainDependencies.forEach { it.execute(this) }
                        }
                    }
                }

                if (multiplatformDependencies.apple.testDependencies.isNotEmpty()) {
                    appleTest.configure {
                        dependencies {
                            multiplatformDependencies.apple.testDependencies.forEach { it.execute(this) }
                        }
                    }
                }

                iosMain.configure {
                    dependencies {
                        multiplatformDependencies.ios.mainDependencies.forEach { it.execute(this) }
                    }
                }

                iosTest.configure {
                    dependencies {
                        multiplatformDependencies.ios.testDependencies.forEach { it.execute(this) }
                    }
                }

                if (supportJVM) {
                    jvmMain.configure {
                        dependencies {
                            implementation(kotlin("stdlib"))
                            implementation("kotlinx-coroutines-swing".asDependency())
                            multiplatformDependencies.jvm.mainDependencies.forEach { it.execute(this) }
                        }
                    }

                    jvmTest.configure {
                        dependencies {
                            implementation(kotlin("test"))
                            implementation(kotlin("test-junit"))
                            multiplatformDependencies.jvm.testDependencies.forEach { it.execute(this) }
                        }
                    }
                }

                if (supportJS) {
                    jsMain.configure {
                        dependencies {
                            implementation(kotlin("stdlib-js"))
                            implementation("kotlinx-coroutines-js".asDependency())
                            multiplatformDependencies.js.mainDependencies.forEach { it.execute(this) }
                        }
                    }

                    jsTest.configure {
                        dependencies {
                            implementation(kotlin("test-js"))
                            multiplatformDependencies.js.testDependencies.forEach { it.execute(this) }
                        }
                    }
                }
            }

            project.configure(iosTargets) {
                compilations.getByName("main").cinterops.let { mainInterops ->
                    appleInterop.main.forEach { it.execute(mainInterops) }
                }
                compilations.getByName("test").cinterops.let { mainInterops ->
                    appleInterop.test.forEach { it.execute(mainInterops) }
                }
                binaries {
                    frameworkConfig?.let { iosExport ->
                        framework { iosExport() }
                    }
                    getTest("DEBUG").apply {
                        freeCompilerArgs = freeCompilerArgs + listOf("-e", "com.splendo.kaluga.test.base.mainBackground")
                    }
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
                    if (pluginManager.hasPlugin(versionCatalog.findPlugin("kotlin-serialization").get().get().pluginId)) {
                        optIn("kotlinx.serialization.ExperimentalSerializationApi")
                    }
                    enableLanguageFeature("InlineClasses")
                }
            }
        }
    }

    override fun LibraryExtension.configure() {
        @Suppress("UnstableApiUsage")
        testOptions {
            unitTests.isReturnDefaultValues = true
            targetSdk = versionCatalog.findVersion("androidCompileSdk").get().displayName.toInt()
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
                groupId = BASE_GROUP
                version = project.kalugaVersion
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
