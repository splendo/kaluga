plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("maven-publish")
    id("com.android.library")
    id("org.jlleitschuh.gradle.ktlint")
}

val ext = (gradle as ExtensionAware).extra

apply(from = "../gradle/publishable_component.gradle")

group = "com.splendo.kaluga"
version = ext["library_version"]!!
val kotlinx_coroutines_version = ext["kotlinx_coroutines_version"]!!

dependencies {
    val ext = (gradle as ExtensionAware).extra
    val play_services_version: String by ext
    val kotlin_version: String by ext
    implementation("com.google.android.gms:play-services-location:$play_services_version")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")
}

kotlin {
    sourceSets {
        getByName("androidLibMain") {
            dependencies {
                implementation(kotlin("reflect"))
            }
        }
        getByName("commonMain") {
            dependencies {
                implementation(project(":logging", ""))
                api(project(":base", ""))
            }
        }
        getByName("commonTest") {
            dependencies {
                api(project(":test-utils", ""))
            }
        }
    }
}
