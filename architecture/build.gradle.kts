plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.3.70"
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
    testImplementation("org.mockito:mockito-core:2.28.2")
    androidTestImplementation("androidx.appcompat:appcompat:1.1.0")
    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")


}

android {
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
    packagingOptions {
        exclude("META-INF/kotlinx-serialization-runtime.kotlin_module")
    }
}

kotlin {
    targets {
        tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    sourceSets {
        val ext = (gradle as ExtensionAware).extra
        getByName("commonMain") {
            dependencies {
                implementation(project(":base", ""))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:0.20.0")
            }
        }

        getByName("commonTest") {
            dependencies {
                implementation(project(":test-utils", ""))
            }
        }

        getByName("jvmMain") {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0")
            }
        }
        getByName("jsMain") {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:0.20.0")
            }
        }

        getByName("${ext["ios_primary_arch"]}Main") {
            kotlin.srcDirs("src/iosMain")
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:0.20.0")
            }
        }
        val singleSet = ext["ios_one_sourceset"] as Boolean
        if (!singleSet) {

            getByName("iosarm32Main") {
                kotlin.srcDirs("src/iosMain")
                dependencies {
                    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:0.20.0")
                }
            }
            getByName("${ext["ios_secondary_arch"]}Main") {
                dependencies {
                    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:0.20.0")
                }
            }
        }
    }

}