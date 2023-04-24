plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("org.jmailen.kotlinter")
}

publishableComponent("scientific")

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":base"))
                apiDependency(Dependencies.KotlinX.Serialization.Core)
            }
        }
        commonTest {
            dependencies {
                implementation(project(":test-utils-base"))
                apiDependency(Dependencies.KotlinX.Serialization.Json)
            }
        }
    }
}
