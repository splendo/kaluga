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

android {
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation("com.google.firebase:firebase-firestore:19.0.2")
                implementation("com.google.firebase:firebase-auth:17.0.0")
            }
        }
    }
}
