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
    mavenCentral()
}

dependencies {

    val ext = (gradle as ExtensionAware).extra

    val androidx_appcompat_version: String by ext

    implementation("androidx.appcompat:appcompat:$androidx_appcompat_version")
    implementation("androidx.fragment:fragment:${ext["androidx_fragment_version"]}")
    androidTestImplementation("androidx.fragment:fragment-ktx:${ext["androidx_fragment_version"]}")
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
