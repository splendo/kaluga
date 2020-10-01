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

kotlin {
    sourceSets {

        val ext = (gradle as ExtensionAware).extra

        getByName("commonMain") {
            dependencies {
                implementation(project(":logging", ""))
                api("co.touchlab:stately-common:${ext["stately_version"]}")
                api("co.touchlab:stately-concurrency:${ext["stately_version"]}")
            }
        }
        getByName("commonTest") {
            dependencies {
                implementation(project(":test-utils", ""))
            }
        }
    }
}
