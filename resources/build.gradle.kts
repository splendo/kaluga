plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jlleitschuh.gradle.ktlint")
}

val ext = (gradle as ExtensionAware).extra

apply(from = "../gradle/publishable_component.gradle")

group = "com.splendo.kaluga"
version = ext["library_version"]!!

dependencies {}

kotlin {
    sourceSets {
        val ext = (gradle as ExtensionAware).extra
        val serialization_version: String by ext

        getByName("commonMain") {
            dependencies {
                implementation(project(":base", ""))
                implementation(project(":logging", ""))
                api("org.jetbrains.kotlinx:kotlinx-serialization-core:$serialization_version")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:$serialization_version")
            }
        }

        getByName("commonTest") {
            dependencies {
                implementation(project(":test-utils", ""))
            }
        }
    }
}
