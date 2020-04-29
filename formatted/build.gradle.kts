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
    // implementation("androidx.fragment:fragment:1.1.0")
    // implementation("androidx.appcompat:appcompat:1.1.0")
    testImplementation("org.mockito:mockito-core:2.28.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")
    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.2.0")
    // androidTestImplementation("androidx.fragment:fragment-ktx:1.1.0")
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
