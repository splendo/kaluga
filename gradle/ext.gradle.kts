// import java.util.Properties
// import java.io.*
//
// println("tasks: $gradle.startParameter.taskNames")
//
// // if the include is made from the example project shared module we need to go up a few directories
// // and depending on when we are included, we can be in the `gradle` folder or not.
// val path_prefix = when {
//     file("componentskt.gradle.kts").exists() -> ""
//     file("gradle/componentskt.gradle.kts").exists() -> "gradle/"
//     else -> "../../../gradle/"
// }
// val f2 = file("${path_prefix}gitBranch.gradle.kts")
//
// apply(from = f2.path)
// val systemProperties = System.getProperties()
//
// val props = Properties()
// val propFile = file("../local.properties")
// if (propFile.exists()) {
//     val inputStream = FileInputStream(propFile.absolutePath)
//     props.load(inputStream)
// }
//
// val libraryVersionLocalProperties: String? = props["kaluga.libraryVersion"] as? String
// val exampleEmbeddingMethodLocalProperties: String? = props["kaluga.exampleEmbeddingMethod"] as? String
// val exampleMavenRepoLocalProperties: String? = props["kaluga.exampleMavenRepo"] as? String
// val kotlinVersion: String = extra["kaluga.kotlinVersion"] as String
//
// // set some global variables
// val ext = (gradle as ExtensionAware).extra
// ext.apply {
//     set("kotlin_version", kotlinVersion)
//     set("kotlinx_coroutines_version", "1.6.3-native-mt")
//     set("stately_version", "1.2.3")
//     set("stately_isolate_version", "1.2.3")
//     set("koin_version", "3.2.2")
//     set("serialization_version", "1.4.0")
//     set("napier_version", "2.4.0")
//     set("android_ble_scanner_version", "1.6.0")
//     val library_version_base = "1.0.0"
//     set("library_version_base", library_version_base)
//     set("library_version", libraryVersionLocalProperties ?: "$library_version_base${systemProperties["kaluga_branch_postfix"]}")
//     set("android_min_sdk_version", 21)
//     set("android_compile_sdk_version", 33)
//     set("android_target_sdk_version", 33)
//     set("android_build_tools_version", "33.0.0")
//
//     set("play_services_version", "20.0.0")
//     set("play_core_version", "1.10.3")
//     set("play_core_ktx_version", "1.8.1")
//
//     set("androidx_appcompat_version", "1.5.1")
//     set("androidx_fragment_version", "1.5.3")
//     set("androidx_core_version", "1.9.0")
//     set("androidx_lifecycle_version", "2.5.1")
//     set("androidx_lifecycle_viewmodel_compose_version", "2.5.1")
//     set("androidx_arch_core_testing_version", "2.1.0")
//     set("androidx_browser_version", "1.4.0")
//     set("androidx_constraint_layout_version", "2.1.4")
//
//     set("androidx_compose_compiler_version", "1.3.2")
//     set("androidx_compose_version", "1.2.1")
//     set("androidx_activity_compose_version", "1.6.0")
//     set("androidx_navigation_compose_version", "2.5.2")
//
//     set("material_version", "1.6.1")
//     set("material_components_adapter_version", "1.1.17")
//
//     // sub packages of test have different versions, but alpha/beta/rc releases are harmonized
//     set("junit_version", "4.13.2")
//
//     val androidx_test_version_postfix = ""
//     set("androidx_test_version_postfix", androidx_test_version_postfix)
//     set("androidx_test_version", "1.4.0$androidx_test_version_postfix")
//     set("androidx_test_espresso_version", "3.4.0$androidx_test_version_postfix")
//     set("androidx_test_junit_version", "1.1.3$androidx_test_version_postfix")
//     set("androidx_test_uiautomator_version", "2.2.0")
//
//     // mockito and bytebuddy need to be upgraded in lockstep
//     set("mockito_version", "3.11.2")
//     set("bytebuddy_version", "1.11.3")
//
//     // Javascript
//     set("js_bigdecimal_version", "1.0.26")
//
//     // used/modified at runtime.
//     val component_type_default = "default"
//     set("component_type_default", component_type_default)
//     set("component_type_compose", "compose")
//     set("component_type_app", "app")
//     set("component_type_composeApp", "composeApp")
//     set("component_type", component_type_default)
// }
//
// if (System.getenv().containsKey("EXAMPLE_EMBEDDING_METHOD")) {
//     ext.set("example_embedding_method", System.getenv()["EXAMPLE_EMBEDDING_METHOD"])
//     logger.lifecycle("System env EXAMPLE_EMBEDDING_METHOD set to ${System.getenv()["EXAMPLE_EMBEDDING_METHOD"]}, using ${ext["example_embedding_method"]}")
// } else {
//     ext.set("example_embedding_method", exampleEmbeddingMethodLocalProperties ?: "composite")
//     logger.lifecycle("local.properties read (kaluga.exampleEmbeddingMethod=$exampleEmbeddingMethodLocalProperties, using ${ext["example_embedding_method"]})")
// }
//
// if (System.getenv().containsKey("EXAMPLE_MAVEN_REPO")) {
//     ext.set("example_maven_repo", System.getenv()["EXAMPLE_MAVEN_REPO"])
//     logger.lifecycle("System env EXAMPLE_MAVEN_REPO set to ${System.getenv()["EXAMPLE_MAVEN_REPO"]}, using ${ext["example_maven_repo"]}")
// } else {
//     // load some more from local.properties or set defaults.
//     if (exampleMavenRepoLocalProperties != null) {
//         ext.set("example_maven_repo", exampleMavenRepoLocalProperties)
//         logger.lifecycle("local.properties read (kaluga.exampleMavenRepo=$exampleMavenRepoLocalProperties, using ${ext["example_maven_repo"]})")
//     } else {
//         ext.set("example_maven_repo", "local")
//         logger.lifecycle("local.properties not found, using default value (${ext["example_maven_repo"]})")
//     }
// }
//
// val CONNECTED_CHECK_EXPANSION = System.getenv().containsKey("CONNECTED_CHECK_EXPANSION")
// ext.set("connect_check_expansion", CONNECTED_CHECK_EXPANSION or System.getenv().containsKey("CI"))
//
// val connect_check_expansion: Boolean by ext
// if (connect_check_expansion)
//     logger.lifecycle("Adding extra dependend task to connected checks of similarly named modules (CONNECTED_CHECK_EXPANSION env present: $CONNECTED_CHECK_EXPANSION)")
//
// // based on https://github.com/Kotlin/xcode-compat/blob/d677a43edc46c50888bca0a7890a81f976a42809/xcode-compat/src/main/kotlin/org/jetbrains/kotlin/xcodecompat/XcodeCompatPlugin.kt#L16
// val sdkName = System.getenv("SDK_NAME") ?: "unknown"
// ext.set("is_real_ios_device", sdkName.startsWith("iphoneos"))
// logger.lifecycle("Run on real ios device: ${ext["is_real_ios_device"]} from sdk: $sdkName")
//
// // Run on IntelliJ
// val ideaActive = System.getProperty("idea.active") == "true"
// ext.set("idea_active", ideaActive)
// logger.lifecycle("Run on IntelliJ: ${ext["idea_active"]}")
//
// // Run on apple silicon
// val isAppleSilicon = System.getProperty("os.arch") == "aarch64"
// ext.set("is_apple_silicon", isAppleSilicon)
// logger.lifecycle("Run on apple silicon: ${ext["is_apple_silicon"]}")
//
// val iosX64 = "iosX64"
// val iosArm64 = "iosArm64"
// val iosSimulatorArm64 = "iosSimulatorArm64"
// val allIosTargets = listOf(iosX64, iosArm64, iosSimulatorArm64)
// val idea_active: Boolean by ext
// val is_real_ios_device: Boolean by ext
// val is_apple_silicon: Boolean by ext
//
// ext.set(
//     "ios_targets",
//     when {
//         !idea_active -> allIosTargets
//         is_real_ios_device -> listOf(iosArm64)
//         is_apple_silicon -> listOf(iosSimulatorArm64)
//         else -> listOf(iosX64)
//     }
// )
// logger.lifecycle("Run on ios targets: ${ext["ios_targets"]}")
