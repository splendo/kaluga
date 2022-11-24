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
dependencies {
    implementation(project(mapOf("path" to ":base")))
}

kotlin {

    val ext = (gradle as ExtensionAware).extra

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":base", ""))
                implementation(project(":logging", ""))
            }
        }

        commonTest {
            dependencies {
                implementation(project(":test-utils-base", ""))
            }
        }
    }
}
