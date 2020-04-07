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

dependencies {
    testImplementation("org.mockito:mockito-core:2.28.2")
    androidTestImplementation("androidx.appcompat:appcompat:1.1.0")
    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.2.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")


}

android {
    testOptions {
        unitTests.isReturnDefaultValues = true
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
        commonMain {
            dependencies {
                implementation(project(":base", ""))
            }
        }
    }
}