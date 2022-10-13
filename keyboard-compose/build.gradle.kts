plugins {
    id("com.android.library")
    kotlin("android")
    id("jacoco")
    id("convention.publication")
    id("org.jlleitschuh.gradle.ktlint")
}

val ext = (gradle as ExtensionAware).extra
ext["component_type"] = ext["component_type_compose"]

// if the include is made from the example project shared module we need to go up one more directory
val path_prefix = if (file("../gradle/componentskt.gradle.kts").exists())
    ".." else "../.."

apply(from = "$path_prefix/gradle/android_compose.gradle")

group = "com.splendo.kaluga"
version = ext["library_version"]!!

ext["component_type"] = ext["component_type_default"]

dependencies {
    implementation(project(":base"))
    api(project(":keyboard"))
    val ext = (gradle as ExtensionAware).extra
    implementation("androidx.compose.ui:ui:" + ext["androidx_compose_version"])
    implementation("androidx.compose.ui:ui-tooling:" + ext["androidx_compose_version"])
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${ext["kotlinx_coroutines_version"]}!!")
}
