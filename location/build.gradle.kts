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

dependencies {
    implementation("com.google.android.gms:play-services-location:17.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.3.1")
}

kotlin {
    sourceSets {
        getByName("commonMain") {
            dependencies {
                implementation(project(":base", ""))
                implementation(project(":logging", ""))
            }
        }
        getByName("commonTest") {
            dependencies {
                implementation(project(":test-utils", ""))
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
                implementation(project(":base", "${ext["ios_primary_arch"]}Default"))
                implementation(project(":logging", "${ext["ios_primary_arch"]}Default"))
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
                    implementation(project(":base", "${ext["ios_secondary_arch"]}Default"))
                    implementation(project(":logging", "${ext["ios_secondary_arch"]}Default"))
                }
            }
        }
        sourceSets {
            val ext =  (gradle as ExtensionAware).extra
            getByName("${ext["ios_secondary_arch"]}Main") {
                dependencies {
                    implementation(project(":base", "${ext["ios_secondary_arch"]}Default"))
                    implementation(project(":logging", "${ext["ios_secondary_arch"]}Default"))
                }
            }
        }
    }
}
