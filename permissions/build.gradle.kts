plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("org.jmailen.kotlinter")
}

publishableComponent("permissions")

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
