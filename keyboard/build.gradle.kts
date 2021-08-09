plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("com.android.library")
    id("convention.publication")
    id("org.jlleitschuh.gradle.ktlint")
}

val ext = (gradle as ExtensionAware).extra

apply(from = "../gradle/publishable_component.gradle")

group = "com.splendo.kaluga"
version = ext["library_version"]!!

kotlin {
    sourceSets {
        getByName("commonMain") {
            dependencies {
                implementation(project(":architecture", ""))
                implementation(project(":base", ""))
            }
        }

        getByName("commonTest") {
            dependencies {
                api(project(":test-utils", ""))
            }
        }
        getByName("androidLibMain") {
            dependencies {
                val ext = (gradle as ExtensionAware).extra
                implementation(
                    "androidx.compose.ui:ui:${ext["androidx_compose_version"]}"
                )
            }
        }
    }
}
