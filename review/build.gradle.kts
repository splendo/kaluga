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

repositories {
    google()
    jcenter()
    mavenCentral()
}

dependencies {
    val ext = (gradle as ExtensionAware).extra
    val play_core_version: String by ext
    val play_core_ktx_version: String by ext

    implementation("com.google.android.play:core:$play_core_version")
    implementation("com.google.android.play:core-ktx:$play_core_ktx_version")
}

kotlin {
    sourceSets {
        getByName("commonMain") {
            dependencies {
                implementation(project(":architecture", ""))
                implementation(project(":base", ""))
                implementation(project(":logging", ""))
            }
        }
    }
}
