plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.3.72"
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
    val androidx_lifecycle_version: String by ext
    val serialization_version: String by ext
    api("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serialization_version")
    api("androidx.lifecycle:lifecycle-runtime-ktx:$androidx_lifecycle_version")
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:$androidx_lifecycle_version")
    api("androidx.lifecycle:lifecycle-livedata-ktx:$androidx_lifecycle_version")
}

android {
    packagingOptions {
        exclude("META-INF/kotlinx-serialization-runtime.kotlin_module")
    }
}

kotlin {

    sourceSets {
        val ext = (gradle as ExtensionAware).extra
        val serialization_version: String by ext
        getByName("commonMain") {
            dependencies {
                implementation(project(":base", ""))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:$serialization_version")
            }
        }

        getByName("commonTest") {
            dependencies {
                implementation(project(":test-utils", ""))
            }
        }

        getByName("jvmMain") {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serialization_version")
            }
        }
        getByName("jsMain") {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:$serialization_version")
            }
        }

        getByName("${ext["ios_primary_arch"]}Main") {
            kotlin.srcDirs("src/iosMain")
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:$serialization_version")
            }
        }
        val singleSet = ext["ios_one_sourceset"] as Boolean
        if (!singleSet) {

            getByName("iosarm32Main") {
                kotlin.srcDirs("src/iosMain")
                dependencies {
                    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:$serialization_version")
                }
            }
            getByName("${ext["ios_secondary_arch"]}Main") {
                dependencies {
                    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:$serialization_version")
                }
            }
        }
    }

}