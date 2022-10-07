plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jlleitschuh.gradle.ktlint")
}

val ext = (gradle as ExtensionAware).extra

apply(from = "../gradle/publishable_component.gradle.kts")

group = "com.splendo.kaluga"
version = ext["library_version"]!!

dependencies {
    val ext = (gradle as ExtensionAware).extra
    implementation("no.nordicsemi.android.support.v18:scanner:${ext["android_ble_scanner_version"]}")
    implementation(project(":location", ""))
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                val ext = (gradle as ExtensionAware).extra
                api(project(":bluetooth-permissions", ""))
                implementation("co.touchlab:stately-concurrency:${ext["stately_version"]}")
            }
        }
        commonTest {
            dependencies {
                implementation(project(":test-utils-bluetooth", ""))
            }
        }
    }
}
