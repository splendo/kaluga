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
    google()
    mavenCentral()
}

kotlin {
    sourceSets {
        getByName("commonMain") {
            dependencies {
                val ext = (gradle as ExtensionAware).extra
                implementation(project(":base", ""))
                implementation("co.touchlab:stately-common:${ext["stately_version"]}")
                implementation("co.touchlab:stately-concurrency:${ext["stately_version"]}")
            }
        }
    }
}
