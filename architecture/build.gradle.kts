plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
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
    jcenter()
    mavenCentral()
}

dependencies {
    val ext = (gradle as ExtensionAware).extra
    val kotlin_version: String by ext
    val androidx_lifecycle_version: String by ext
    val androidx_browser_version: String by ext

    api("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")
    api("androidx.lifecycle:lifecycle-runtime-ktx:$androidx_lifecycle_version")
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:$androidx_lifecycle_version")
    api("androidx.lifecycle:lifecycle-livedata-ktx:$androidx_lifecycle_version")
    implementation("androidx.browser:browser:$androidx_browser_version")

}

kotlin {

    sourceSets {
        val ext = (gradle as ExtensionAware).extra
        val serialization_version: String by ext

        // For IDE
        val ios_primary_arch:String by ext
        getByName("${ios_primary_arch}Main") {
             dependencies {
                 api(project(":base", ""))
             }
        }

        getByName("commonMain") {
            dependencies {
                implementation(project(":base", ""))
                api("org.jetbrains.kotlinx:kotlinx-serialization-core:$serialization_version")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:$serialization_version")
            }
        }

        getByName("commonTest") {
            dependencies {
                api(project(":test-utils", ""))
            }
        }
    }
}
