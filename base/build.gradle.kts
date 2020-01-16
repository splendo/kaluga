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
                api("co.touchlab:stately:0.9.5")
                implementation(project(":logging", ""))
            }
        }
        getByName("commonTest") {
            dependencies {
                implementation(project(":test-utils", ""))
            }
        }
    }

}