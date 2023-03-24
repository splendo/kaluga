plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jlleitschuh.gradle.ktlint")
}

val ext = (gradle as ExtensionAware).extra

apply(from = "../gradle/publishable_component.gradle")

group = "com.splendo.kaluga"
version = ext["library_version"]!!

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":base"))
            }
        }
        commonTest {
            dependencies {
                implementation(project(":test-utils"))
            }
        }
    }
}
