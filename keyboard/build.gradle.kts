plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("com.android.library")
    id("maven-publish")
    id("org.jlleitschuh.gradle.ktlint")
}

val ext = (gradle as ExtensionAware).extra

apply(from = "../gradle/publishable_component.gradle")

group = "com.splendo.kaluga"
version = ext["library_version"]!!

repositories {
    google()
    mavenCentral()
}

kotlin {
    sourceSets {
        getByName("commonMain") {
            dependencies {
                implementation(project(":base", ""))
            }
        }
    }
}