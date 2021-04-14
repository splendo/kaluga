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
    implementation(project(":location", ""))
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                val ext = (gradle as ExtensionAware).extra
                implementation(project(":permissions", ""))
                implementation(project(":logging", ""))
                implementation(project(":base", ""))
                implementation("co.touchlab:stately-concurrency:${ext["stately_version"]}")
            }
        }
        commonTest {
            dependencies {
                implementation(project(":test-utils", ""))
            }
        }
    }
}
