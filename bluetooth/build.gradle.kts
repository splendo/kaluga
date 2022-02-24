plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jlleitschuh.gradle.ktlint")
}

val ext = (gradle as ExtensionAware).extra

apply(from = "../gradle/publishable_component.gradle")

group = "com.splendo.kaluga"
version = ext["library_version"]!!

dependencies {
    val ext = (gradle as ExtensionAware).extra
    implementation("no.nordicsemi.android.support.v18:scanner:${ext["android_ble_scanner_version"]}")
    implementation(project(":location", ""))
}

kotlin {
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    js {
        nodejs()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                val ext = (gradle as ExtensionAware).extra
                api(project(":bluetooth-permissions", ""))
                implementation(project(":logging", ""))
                implementation(project(":base", ""))
                implementation("co.touchlab:stately-concurrency:${ext["stately_version"]}")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(project(":test-utils", ""))
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }

        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}
