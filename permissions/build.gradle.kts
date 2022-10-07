plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jlleitschuh.gradle.ktlint")
}

val ext = (gradle as ExtensionAware).extra

apply(from = "../gradle/publishable_component.gradle.kts")

group = "com.splendo.kaluga"
version = ext["library_version"]!!

dependencies { }

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":bluetooth-permissions"))
                api(project(":calendar-permissions"))
                api(project(":location-permissions"))
                api(project(":storage-permissions"))
                api(project(":notifications-permissions"))
                api(project(":contacts-permissions"))
                api(project(":microphone-permissions"))
                api(project(":camera-permissions"))
            }
        }
        commonTest {
            dependencies {
                implementation(project(":test-utils-base"))
            }
        }
    }
}
