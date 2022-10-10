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
        commonMain {
            dependencies {
                implementation(project(":base"))
                expose(Dependencies.KotlinX.Serialization.Core)
                expose(Dependencies.KotlinX.Serialization.Json)
            }
        }
        commonTest {
            dependencies {
                implementation(project(":test-utils-base"))
            }
        }
    }
}
