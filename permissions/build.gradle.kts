plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("maven-publish")
    id("com.android.library")
}

val ext =  (gradle as ExtensionAware).extra


apply(from = "../gradle/publishable_component.gradle")

group = "com.splendo.kaluga"
version = ext["library_version"]!!
val kotlinx_coroutines_version = ext["kotlinx_coroutines_version"]!!

repositories {
    maven("https://dl.bintray.com/pocketbyte/hydra/")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinx_coroutines_version")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-debug:$kotlinx_coroutines_version")
    testImplementation("org.mockito:mockito-core:3.1.0")

    implementation("androidx.appcompat:appcompat:1.1.0")

    commonTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-debug:$kotlinx_coroutines_version")

}

android {
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

kotlin {

    sourceSets {
        commonMain {
            dependencies {
                api(project(":base", ""))
            }
        }
    }
}

val singleSet =ext["ios_one_sourceset"] as Boolean

kotlin {
    sourceSets {
        val ext =  (gradle as ExtensionAware).extra
        getByName("${ext["ios_primary_arch"]}Main") {
            dependencies {
                api(project(":base", "${ext["ios_primary_arch"]}Default"))
            }
        }
    }
}

if (!singleSet)  {

    kotlin {

        sourceSets {
            val ext =  (gradle as ExtensionAware).extra
            getByName("${ext["ios_secondary_arch"]}Main") {
                dependencies {
                    api(project(":base", "${ext["ios_secondary_arch"]}Default"))
                }
            }
        }
        sourceSets {
            val ext =  (gradle as ExtensionAware).extra
            getByName("${ext["ios_secondary_arch"]}Main") {
                dependencies {
                    api(project(":base", "${ext["ios_secondary_arch"]}Default"))
                }
            }
        }
    }
}