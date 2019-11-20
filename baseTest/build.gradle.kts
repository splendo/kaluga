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

repositories {
    maven("https://dl.bintray.com/pocketbyte/hydra/")
}

kotlin {
    sourceSets {
        getByName("commonMain") {
            dependencies {
                api(kotlin("test"))
                api(kotlin("test-junit"))
                api("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.0")
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