plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jlleitschuh.gradle.ktlint")
}

val ext = (gradle as ExtensionAware).extra

apply(from = "../gradle/publishable_component.gradle")

group = "com.splendo.kaluga"
version = ext["library_version"]!!

dependencies {
    val play_services_version = (gradle as ExtensionAware).extra["play_services_version"]
    implementation("com.google.android.gms:play-services-location:$play_services_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.3.9")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":location-permissions", ""))
                implementation(project(":logging", ""))
                implementation(project(":base", ""))
            }
        }
        commonTest {
            dependencies {
                implementation(project(":test-utils-location", ""))
            }
        }
    }
}
