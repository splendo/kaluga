plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("maven-publish")
    id("com.android.library")
}

val ext =  (gradle as ExtensionAware).extra

apply(from = "../gradle/publishable_component.gradle")

group = "com.splendo.mpp"
version = ext["library_version"]!!


repositories {
    maven("https://dl.bintray.com/pocketbyte/hydra/")
}

dependencies {
    implementation("com.google.android.gms:play-services-location:17.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.3.1")
}

kotlin {
    sourceSets {
        getByName("commonMain") {
            dependencies {
                implementation(project(":logging", ""))
            }
        }
    }
}

val singleSet =ext["ios_one_sourceset"] as Boolean

if (singleSet) {

    kotlin {
        sourceSets {
            getByName("iosMain") {
                dependencies {
                    implementation(project(":logging", "iosDefault"))
                }
            }
        }
    }
} else {

    kotlin {
        sourceSets {
            getByName("iosX64Main") {
                dependencies {
                    implementation(project(":logging", "iosX64Default"))
                }
            }
        }
        sourceSets {
            getByName("iosArm64Main") {
                dependencies {
                    implementation(project(":logging", "iosArm64Default"))
                }
            }
        }
        sourceSets {
            getByName("iosArm32Main") {
                dependencies {
                    implementation(project(":logging", "iosArm32Default"))
                }
            }
        }
    }
}