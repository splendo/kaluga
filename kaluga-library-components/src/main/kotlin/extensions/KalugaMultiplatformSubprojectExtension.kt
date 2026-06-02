/*
 Copyright 2025 Splendo Consulting B.V. The Netherlands

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

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.splendo.kaluga.plugin.container.AppleInteropContainer
import com.splendo.kaluga.plugin.container.BuildSwiftLibTask
import com.splendo.kaluga.plugin.container.MultiplatformDependencyContainer
import com.splendo.kaluga.plugin.container.sdkName
import com.splendo.kaluga.plugin.helpers.jvmTarget
import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.newInstance
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JsModuleKind
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.abi.AbiValidationMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation
import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.targets.js.testing.KotlinJsTest
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jmailen.gradle.kotlinter.tasks.LintTask
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.Locale.getDefault
import javax.inject.Inject

open class KalugaMultiplatformSubprojectExtension @Inject constructor(
    val multiplatformExtension: KotlinMultiplatformExtension,
    versionCatalog: VersionCatalog,
    objects: ObjectFactory,
) : BaseKalugaSubprojectExtension(versionCatalog, null, objects) {

    val isMacOs = Os.isFamily(Os.FAMILY_MAC)
    val ideaActive = System.getProperty("idea.active") == "true"
    val isAppleSilicon = System.getProperty("os.arch") == "aarch64"

    private enum class IOSTarget(val sourceSetName: String) {
        Arm64("iosArm64"),
        SimulatorArm64("iosSimulatorArm64"),
    }

    private enum class MacOSTarget(val sourceSetName: String) {
        Arm64("macosArm64"),
    }

    private enum class TVOSTarget(val sourceSetName: String) {
        Arm64("tvosArm64"),
        SimulatorArm64("tvosSimulatorArm64"),
    }

    private enum class WatchOSTarget(val sourceSetName: String) {
        Arm64("watchosArm64"),
        SimulatorArm64("watchosSimulatorArm64"),
    }

    var supportJVM: Boolean = false
        set(value) {
            field = value
            if (value) {
                multiplatformExtension.jvm()
            }
        }
    var supportJS: Boolean = false
        set(value) {
            field = value
            if (value) {
                multiplatformExtension.js(KotlinJsCompilerType.IR) {
                    val testTaskSetup: KotlinJsTest.() -> Unit = {
                        useMocha {
                            timeout = "5m"
                        }
                    }
                    nodejs {
                        testTask(testTaskSetup)
                    }
                    browser {
                        testTask(testTaskSetup)
                    }
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
        }
    var iosDeploymentTarget: String = "15.0"
    var macosDeploymentTarget: String = "11.0"
    var tvosDeploymentTarget: String = "15.0"
    var watchosDeploymentTarget: String = "8.0"

    /**
     * When `true`, this module is also compiled for `macosArm64`.
     *
     * iOS-only code stays in `iosMain`; code that works on every Apple target via Foundation /
     * CoreBluetooth / CoreLocation / AVFoundation should live in `appleMain` so it is shared with
     * the macOS targets.
     */
    var supportMacOS: Boolean = false
        set(value) {
            field = value
            if (value) {
                macosTargetsToRegister().forEach { macosTarget ->
                    val target = when (macosTarget) {
                        MacOSTarget.Arm64 -> multiplatformExtension.macosArm64()
                    }
                    registeredMacosTargets += target
                }
            }
        }

    /**
     * When `true`, this module is also compiled for `tvosArm64` and `tvosSimulatorArm64`.
     *
     * iOS-only code stays in `iosMain`; code that works on every Apple target via Foundation /
     * CoreBluetooth / CoreLocation / AVFoundation should live in `appleMain` so it is shared with
     * the tvOS targets.
     */
    var supportTvOS: Boolean = false
        set(value) {
            field = value
            if (value) {
                tvosTargetsToRegister().forEach { tvosTarget ->
                    val target = when (tvosTarget) {
                        TVOSTarget.Arm64 -> multiplatformExtension.tvosArm64()
                        TVOSTarget.SimulatorArm64 -> multiplatformExtension.tvosSimulatorArm64()
                    }
                    registeredTvosTargets += target
                }
            }
        }

    /**
     * When `true`, this module is also compiled for `watchosArm64` and `watchosSimulatorArm64`.
     *
     * iOS-only code stays in `iosMain`; code that works on every Apple target via Foundation /
     * CoreBluetooth / CoreLocation / AVFoundation should live in `appleMain` so it is shared with
     * the watchOS targets.
     */
    var supportWatchOS: Boolean = false
        set(value) {
            field = value
            if (value) {
                watchosTargetsToRegister().forEach { watchosTarget ->
                    val target = when (watchosTarget) {
                        WatchOSTarget.Arm64 -> multiplatformExtension.watchosArm64()
                        WatchOSTarget.SimulatorArm64 -> multiplatformExtension.watchosSimulatorArm64()
                    }
                    registeredWatchosTargets += target
                }
            }
        }

    private val registeredMacosTargets = mutableListOf<KotlinNativeTarget>()
    private val registeredTvosTargets = mutableListOf<KotlinNativeTarget>()
    private val registeredWatchosTargets = mutableListOf<KotlinNativeTarget>()

    private fun macosTargetsToRegister(): Set<MacOSTarget> = when {
        !isMacOs || !isAppleSilicon -> emptySet()
        !ideaActive -> MacOSTarget.values().toSet()
        else -> setOf(MacOSTarget.Arm64)
    }

    private fun tvosTargetsToRegister(): Set<TVOSTarget> {
        if (!isMacOs) return emptySet()
        val sdkName = System.getenv("SDK_NAME") ?: "unknown"
        val isRealTvOSDevice = sdkName.startsWith("appletvos")
        return when {
            !ideaActive -> TVOSTarget.values().toSet()
            isRealTvOSDevice -> setOf(TVOSTarget.Arm64)
            !isAppleSilicon -> emptySet()
            else -> setOf(TVOSTarget.SimulatorArm64)
        }
    }

    private fun watchosTargetsToRegister(): Set<WatchOSTarget> {
        if (!isMacOs) return emptySet()
        val sdkName = System.getenv("SDK_NAME") ?: "unknown"
        val isRealWatchOSDevice = sdkName.startsWith("watchos")
        return when {
            !ideaActive -> WatchOSTarget.values().toSet()
            isRealWatchOSDevice -> setOf(WatchOSTarget.Arm64)
            !isAppleSilicon -> emptySet()
            else -> setOf(WatchOSTarget.SimulatorArm64)
        }
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

    fun appleFramework(action: (Framework.() -> Unit)) {
        frameworkConfig = action
    }

    private val androidLibraryExtension = multiplatformExtension.extensions.findByType(KotlinMultiplatformAndroidLibraryExtension::class.java)!!
    override var namespace: String?
        get() = androidLibraryExtension.namespace
        set(value) {
            androidLibraryExtension.namespace = value
        }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    override fun Project.setupSubproject() {
    }

    override fun Project.configureSubproject() {
        extensions.configure(KotlinMultiplatformExtension::class) {
            configureMultiplatform(this@configureSubproject)
        }
        tasks.register("printConfigurations") {
            doLast {
                configurations.all { println(this) }
            }
        }

        // output all reports to a single location
        tasks.withType<LintTask>().configureEach {
            reports.set(mapOf("plain" to rootProject.layout.buildDirectory.get().asFile.resolve("reports/ktlint/${project.path}-${this.name}.txt")))
        }

        iosTargets.forEach { target ->
            val targetName = target.sourceSetName
            if (tasks.names.contains("linkDebugTest${targetName.replaceFirstChar { it.titlecase() }}")) {
                // creating copy task for the target
                val copyTask = tasks.register("copy${targetName.replaceFirstChar { it.titlecase() }}TestResources", Copy::class) {
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

    @OptIn(ExperimentalKotlinGradlePluginApi::class, ExperimentalAbiValidation::class)
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
                IOSTarget.SimulatorArm64 -> iosSimulatorArm64()
            }
        }

        extensions.configure(AbiValidationMultiplatformExtension::class) {
            enabled.set(true)
            abiExtension()
        }

        project.afterEvaluate {
            applyDefaultHierarchyTemplate()

            // When watchOS is supported, split off an intermediate source set covering only the
            // 64-bit Apple targets. Foundation typealiases like NSUInteger resolve to ULong on
            // iOS / macOS / tvOS / watchosSimulatorArm64 (arm64) but to UInt on watchosArm64
            // (arm64_32), and shared `appleMain` code that references them fails the
            // `compileAppleMainKotlinMetadata` task. Modules can place such code under
            // `src/apple64BitMain` and ship a parallel `src/watchosArm64Main` copy.
            if (registeredWatchosTargets.isNotEmpty()) {
                val apple64BitMain = sourceSets.maybeCreate("apple64BitMain")
                sourceSets.matching { it.name == "appleMain" }.configureEach {
                    apple64BitMain.dependsOn(this)
                }
                sourceSets.matching {
                    it.name in setOf("iosMain", "macosMain", "tvosMain", "watchosSimulatorArm64Main")
                }.configureEach {
                    dependsOn(apple64BitMain)
                }
            }

            dependencies {
                implementation("kotlinx-coroutines-core".asDependency())

                testImplementation(kotlin("test"))
                testImplementation(kotlin("test-common"))
                testImplementation(kotlin("test-annotations-common"))
            }

            with(sourceSets) {
                commonMain.configure {
                    dependencies {
                        multiplatformDependencies.common.mainDependencies.forEach {
                            it.execute(this)
                        }
                    }
                }

                commonTest.configure {
                    dependencies {
                        multiplatformDependencies.common.testDependencies.forEach {
                            it.execute(this)
                        }
                    }
                }

                androidMain.configure {
                    dependencies {
                        androidMainDependencies.forEach {
                            implementation(it)
                        }

                        multiplatformDependencies.android.mainDependencies.forEach {
                            it.execute(this)
                        }
                    }
                }

                getByName("androidHostTest") {
                    dependencies {
                        androidTestDependencies.forEach {
                            implementation(it)
                        }
                        multiplatformDependencies.android.testDependencies.forEach { it.execute(this) }
                    }
                }

                getByName("androidDeviceTest") {
                    // dependsOn(commonTest) triggers a warning in AGP 9.0 ("different Source Set Trees")
                    // but is required for androidDeviceTest to access expect/actual and commonTest utilities
                    dependsOn(getByName("commonTest"))
                    dependencies {
                        androidDeviceTestDependencies.forEach {
                            implementation(it)
                        }
                        multiplatformDependencies.android.deviceTestDependencies.forEach { it.execute(this) }
                    }
                }

                if (multiplatformDependencies.apple.mainDependencies.isNotEmpty()) {
                    appleMain.configure {
                        dependencies {
                            multiplatformDependencies.apple.mainDependencies.forEach {
                                it.execute(this)
                            }
                        }
                    }
                }

                if (multiplatformDependencies.apple.testDependencies.isNotEmpty()) {
                    appleTest.configure {
                        dependencies {
                            multiplatformDependencies.apple.testDependencies.forEach {
                                it.execute(this)
                            }
                        }
                    }
                }

                iosMain.configure {
                    dependencies {
                        multiplatformDependencies.ios.mainDependencies.forEach {
                            it.execute(this)
                        }
                    }
                }

                iosTest.configure {
                    dependencies {
                        multiplatformDependencies.ios.testDependencies.forEach {
                            it.execute(this)
                        }
                    }
                }

                if (registeredMacosTargets.isNotEmpty()) {
                    if (multiplatformDependencies.macos.mainDependencies.isNotEmpty()) {
                        macosMain.configure {
                            dependencies {
                                multiplatformDependencies.macos.mainDependencies.forEach { it.execute(this) }
                            }
                        }
                    }
                    if (multiplatformDependencies.macos.testDependencies.isNotEmpty()) {
                        macosTest.configure {
                            dependencies {
                                multiplatformDependencies.macos.testDependencies.forEach { it.execute(this) }
                            }
                        }
                    }
                }

                if (registeredTvosTargets.isNotEmpty()) {
                    if (multiplatformDependencies.tvos.mainDependencies.isNotEmpty()) {
                        tvosMain.configure {
                            dependencies {
                                multiplatformDependencies.tvos.mainDependencies.forEach { it.execute(this) }
                            }
                        }
                    }
                    if (multiplatformDependencies.tvos.testDependencies.isNotEmpty()) {
                        tvosTest.configure {
                            dependencies {
                                multiplatformDependencies.tvos.testDependencies.forEach { it.execute(this) }
                            }
                        }
                    }
                }

                if (registeredWatchosTargets.isNotEmpty()) {
                    if (multiplatformDependencies.watchos.mainDependencies.isNotEmpty()) {
                        watchosMain.configure {
                            dependencies {
                                multiplatformDependencies.watchos.mainDependencies.forEach { it.execute(this) }
                            }
                        }
                    }
                    if (multiplatformDependencies.watchos.testDependencies.isNotEmpty()) {
                        watchosTest.configure {
                            dependencies {
                                multiplatformDependencies.watchos.testDependencies.forEach { it.execute(this) }
                            }
                        }
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

            val developerDirProvider = providers.exec {
                if (iosTargets.isNotEmpty()) {
                    commandLine("xcode-select", "-p")
                }
            }.standardOutput.asText.map { it.trim() }

            // Swift source-set resolution: `src/iosMain/swift` is iOS-only (current convention);
            // `src/appleMain/swift` is shared across iOS + macOS. Modules that want their Swift
            // wrappers to cover macOS too should put them under `appleMain/swift`.
            val iosSwiftDir = project.file("src/iosMain/swift")
            val appleSwiftDir = project.file("src/appleMain/swift")
            fun swiftSourceDirFor(forIos: Boolean): File? = when {
                forIos && iosSwiftDir.exists() && iosSwiftDir.listFiles().isNotEmpty() -> iosSwiftDir
                appleSwiftDir.exists() && appleSwiftDir.listFiles().isNotEmpty() -> appleSwiftDir
                else -> null
            }

            fun KotlinNativeTarget.configureSwiftAndInterop(swiftSourceDir: File?, deploymentTargetValue: String) {
                val cinteropSwiftInteropTaskName = "cinteropSwiftInterop${name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(getDefault()) else it.toString() }}"
                logger.info("cinteropSwiftInteropTask: ${project.name}:$cinteropSwiftInteropTaskName with target: $name ($konanTarget)")
                val buildSwiftLibTask = swiftSourceDir?.let { srcDir ->
                    tasks.register<BuildSwiftLibTask>("buildSwiftLib_$name") {
                        logger.info("buildSwiftLib_$name registered for $konanTarget with deployment $deploymentTargetValue")
                        this.srcDir.set(srcDir)
                        libraryOutputDir.set(layout.buildDirectory.dir("swift/$name"))
                        headerOutputDir.set(layout.buildDirectory.dir("objc/$name"))
                        target.set(konanTarget)
                        deploymentTarget.set(deploymentTargetValue)
                    }
                }
                compilations.getByName("main").let { main ->
                    main.cinterops.let { mainInterops ->
                        buildSwiftLibTask?.let { buildSwiftLibTask ->
                            mainInterops.create("swiftInterop") {
                                val defFile = File(layout.buildDirectory.asFile.get(), "cinterop/$name/$targetName/swiftinterop.def")

                                if (defFile.exists()) {
                                    defFile.delete()
                                }
                                defFile.parentFile.mkdirs()
                                defFile.createNewFile()
                                val writer = BufferedWriter(FileWriter(defFile))
                                val staticLibPath = buildSwiftLibTask.get().libraryOutputDir.get().asFile.absolutePath
                                val devDir = project.providers.environmentVariable("DEVELOPER_DIR").getOrElse(developerDirProvider.get())
                                val swiftLibPath = "$devDir/Toolchains/XcodeDefault.xctoolchain/usr/lib/swift/${buildSwiftLibTask.get().target.get().sdkName}"
                                val linkerOpts = "-L$swiftLibPath -rpath /usr/lib/swift"
                                val headerSourceDir = buildSwiftLibTask.get().headerOutputDir.get().asFile

                                logger.info("Creating .def file: $defFile with libraryPaths: $staticLibPath linkerOpts: $linkerOpts, compiling with headers from $headerSourceDir")

                                try {
                                    writer.write(
                                        """
                                            |language = Objective-C
                                            |headers = SwiftInterop.h
                                            |package = com.splendo.kaluga.$moduleName
                                            |headerFilter = SwiftInterop.h
                                            |staticLibraries = libswiftinterop.a
                                            |libraryPaths = $staticLibPath/
                                            |linkerOpts = $linkerOpts
                                        """.trimMargin(),
                                    )
                                } finally {
                                    writer.close()
                                }
                                definitionFile.set(defFile)
                                compilerOpts("-I/${headerSourceDir.absolutePath}")
                                includeDirs.allHeaders(headerSourceDir)
                                tasks.named(cinteropSwiftInteropTaskName).configure {
                                    dependsOn(buildSwiftLibTask)
                                }
                            }
                        }

                        appleInterop.main.forEach { settings ->
                            settings.execute(mainInterops)
                        }
                    }
                }
                compilations.getByName("test").let { test ->
                    test.cinterops.let { mainInterops ->
                        appleInterop.test.forEach { it.execute(mainInterops) }
                    }
                }
                binaries {
                    getTest("DEBUG").apply {
                        freeCompilerArgs = freeCompilerArgs + listOf("-e", "com.splendo.kaluga.test.base.mainBackground")
                    }
                }
            }

            project.configure(iosTargets) {
                configureSwiftAndInterop(swiftSourceDirFor(forIos = true), iosDeploymentTarget)
                binaries {
                    frameworkConfig?.let { iosExport ->
                        framework { iosExport() }
                    }
                }
            }

            // The `registeredMacos/Tvos/WatchosTargets` lists are populated by their respective
            // `supportXxx` setters, which run after `configureMultiplatform()` — so these lists
            // are only meaningful inside `afterEvaluate { }`. These targets only consume Swift
            // sources under `appleMain/swift`.
            fun configureNonIosTargets(targets: List<KotlinNativeTarget>, deploymentTarget: String) {
                project.configure(targets) {
                    configureSwiftAndInterop(swiftSourceDirFor(forIos = false), deploymentTarget)
                    binaries {
                        frameworkConfig?.let { export ->
                            framework { export() }
                        }
                    }
                }
            }
            configureNonIosTargets(registeredMacosTargets, macosDeploymentTarget)
            configureNonIosTargets(registeredTvosTargets, tvosDeploymentTarget)
            configureNonIosTargets(registeredWatchosTargets, watchosDeploymentTarget)

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
                    val sourceSetName = this@all.name.lowercase()
                    if (sourceSetName.contains("ios") || sourceSetName.contains("macos") || sourceSetName.contains("tvos") || sourceSetName.contains("watchos") || sourceSetName.contains("apple") || sourceSetName.contains("native")) {
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
            project.setupPublishingAfterEvaluation()
        }
    }

    fun KotlinMultiplatformAndroidLibraryExtension.configure() {
        compileSdk = versionCatalog.findVersion("androidCompileSdk").get().displayName.toInt()
        buildToolsVersion = versionCatalog.findVersion("androidBuildTools").get().displayName
        minSdk = versionCatalog.findVersion("androidMinSdk").get().displayName.toInt()

        withHostTest {
            isReturnDefaultValues = true
            targetSdk {
                release(versionCatalog.findVersion("androidCompileSdk").get().displayName.toInt())
            }
        }
        withDeviceTest {
            animationsDisabled = true
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        // TODO: base on a flag in kaluga configuration
        androidResources {
            enable = true
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

    private val Project.iosTargets: Set<IOSTarget> get() {
        if (!isMacOs) return emptySet()
        val sdkName = System.getenv("SDK_NAME") ?: "unknown"
        val isRealIOSDevice = sdkName.startsWith("iphoneos").also {
            logger.info("Run on real ios device: $it from sdk: $sdkName")
        }

        return when {
            !ideaActive -> IOSTarget.values().toSet()
            isRealIOSDevice -> setOf(IOSTarget.Arm64)
            !isAppleSilicon -> emptySet()
            else -> setOf(IOSTarget.SimulatorArm64)
        }
    }

    override fun Project.beforeEvaluated() {
        setupSubproject()

        multiplatformExtension.extensions.getByType(KotlinMultiplatformAndroidLibraryTarget::class.java).apply {
            configure()
        }

        configureSubproject()
    }
}
