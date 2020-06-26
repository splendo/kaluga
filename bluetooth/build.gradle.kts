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

dependencies {
    implementation("no.nordicsemi.android.support.v18:scanner:1.4.2")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":permissions", ""))
            }
        }
        commonTest {
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
                implementation(project(":permissions", "${ext["ios_primary_arch"]}Default"))
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
                    implementation(project(":permissions", "${ext["ios_secondary_arch"]}Default"))
                }
            }
        }
    }
}
