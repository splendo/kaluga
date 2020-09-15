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

    val statelyVersion: String by project

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":base", ""))
                implementation(project(":architecture", ""))
                implementation(project(":logging", ""))

                implementation("co.touchlab:stately-common:$statelyVersion")
                implementation("co.touchlab:stately-concurrency:$statelyVersion")
            }
        }
    }
}
