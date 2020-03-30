plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("maven-publish")
    id("com.android.library")
}

val ext =  (gradle as ExtensionAware).extra

apply(from = "../gradle/publishable_component.gradle")

group = "com.splendo.kaluga"
version = ext["library_version"]!!
val kotlinx_coroutines_version = ext["kotlinx_coroutines_version"]!!

kotlin {
    sourceSets {
        getByName("commonMain") {
            dependencies {
                api(kotlin("test"))
                api(kotlin("test-junit"))
                api("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinx_coroutines_version")
                implementation(project(":permissions", ""))
                implementation(project(":base", ""))
                implementation(project(":logging", ""))
            }
        }
        getByName("jsMain") {
            dependencies {
                api(kotlin("test-js"))
            }
        }
    }
}
