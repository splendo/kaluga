plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
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
                implementation(project(":logging", ""))
                expose(Dependencies.KotlinX.Serialization.Core)
                expose(Dependencies.KotlinX.Serialization.Json)
            }
        }

        getByName("commonTest") {
            dependencies {
                implementation(project(":test-utils-base", ""))
            }
        }
    }
}
