plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jlleitschuh.gradle.ktlint")
}

publishableComponent()

kotlin {
    sourceSets {
        getByName("commonMain") {
            dependencies {
                api(project(":base-permissions", ""))
            }
        }
        getByName("androidLibMain") {
            dependencies {
                api(project(":location-permissions", ""))
            }
        }
        getByName("commonTest") {
            dependencies {
                api(project(":test-utils-base", ""))
            }
        }
    }
}
