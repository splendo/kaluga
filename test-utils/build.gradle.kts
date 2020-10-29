plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("maven-publish")
    id("com.android.library")
    id("org.jlleitschuh.gradle.ktlint")
}

val ext = (gradle as ExtensionAware).extra

apply(from = "../gradle/publishable_component.gradle")

group = "com.splendo.kaluga"
version = ext["library_version"]!!

val kotlinx_coroutines_version = ext["kotlinx_coroutines_version"]!!
val androidx_arch_core_testing_version = ext["androidx_arch_core_testing_version"]!!

dependencies {
    implementation("androidx.arch.core:core-testing:$androidx_arch_core_testing_version")
}

kotlin {
    sourceSets {
        getByName("commonMain") {
            dependencies {
                // these are not coming from component.gradle because they need to be in the main scope
                api(kotlin("test"))
                api(kotlin("test-junit"))
                api("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinx_coroutines_version")
                implementation(project(":alerts", ""))
                implementation(project(":architecture", ""))
                implementation(project(":base", ""))
                implementation(project(":hud", ""))
                implementation(project(":keyboard", ""))
                implementation(project(":logging", ""))
                implementation(project(":permissions", ""))
            }
        }
        getByName("jsMain") {
            dependencies {
                api(kotlin("test-js"))
            }
        }
    }
}
