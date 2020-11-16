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

    val ext = (gradle as ExtensionAware).extra

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":base", ""))
                implementation(project(":logging", ""))

                implementation("co.touchlab:stately-concurrency:${ext["stately_version"]}")
            }
        }

        commonTest {
            dependencies {
                implementation(project(":test-utils", ""))
            }
        }
    }
}
