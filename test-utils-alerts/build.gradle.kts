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
                api(project(":test-utils-base"))
                implementation(project(":architecture"))
                implementation(project(":alerts"))
            }
        }
        commonTest {
            dependencies {
                api(project(":test-utils-architecture"))
            }
        }
    }
}
