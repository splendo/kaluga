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
                implementation(project(":base", ""))
            }
        }
        getByName("commonTest") {
            dependencies {
                implementation(project(":test-utils-base", ""))
            }
        }
    }
}
