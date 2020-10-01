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

dependencies {
    val play_services_version = (gradle as ExtensionAware).extra["play_services_version"]
    implementation("com.google.android.gms:play-services-location:$play_services_version")
}

kotlin {

    sourceSets {
        getByName("commonMain") {
            val ext = (gradle as ExtensionAware).extra

            dependencies {
                implementation(project(":logging", ""))
                implementation("co.touchlab:stately-iso-collections:${ext["stately_isolate_version"]}")
                api(project(":base", ""))
            }
        }
        getByName("commonTest") {
            dependencies {
                api(project(":test-utils", ""))


            }
        }
    }
}
