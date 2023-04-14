plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("org.jlleitschuh.gradle.ktlint")
}

publishableComponent("permissions.notifications")

kotlin {
    sourceSets {
        getByName("commonMain") {
            dependencies {
                api(project(":base-permissions", ""))
            }
        }
        getByName("commonTest") {
            dependencies {
                api(project(":test-utils-base", ""))
            }
        }
    }
}
