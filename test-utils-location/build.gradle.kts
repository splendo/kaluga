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
        commonMain {
            dependencies {
                api(project(":location"))
                api(project(":test-utils-permissions"))
            }
        }
        commonTest {
            dependencies {
            }
        }
    }
}
