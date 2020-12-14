
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

    api("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")
    api("androidx.lifecycle:lifecycle-runtime-ktx:$androidx_lifecycle_version")
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:$androidx_lifecycle_version")
    api("androidx.lifecycle:lifecycle-livedata-ktx:$androidx_lifecycle_version")
}

android {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

kotlin {

    sourceSets {
        val ext = (gradle as ExtensionAware).extra
        val serialization_version: String by ext

        getByName("androidLibMain") {
            dependencies {
                val browserVersion: String by ext

                implementation("androidx.browser:browser:$browserVersion")
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
