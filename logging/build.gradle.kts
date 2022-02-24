plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("com.android.library")
    id("convention.publication")
    id("org.jlleitschuh.gradle.ktlint")
}

val ext = (gradle as ExtensionAware).extra

apply(from = "../gradle/publishable_component.gradle")

group = "com.splendo.kaluga"
version = ext["library_version"]!!

kotlin {
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    js {
        nodejs()
    }

    sourceSets {
        val commonMain by getting {
            val ext = (gradle as ExtensionAware).extra
            dependencies {
                implementation("ru.pocketbyte.kydra:kydra-log:${ext["kydra_log_version"]}")
                implementation("co.touchlab:stately-concurrency:${ext["stately_version"]}")
            }
        }

        val commonTest by getting {
            dependencies {
                val ext = (gradle as ExtensionAware).extra
                // Stately Isolite is in flux and not part of the current statelyVersion. Upgrade this when tracked properly
                implementation("co.touchlab:stately-isolate:${ext["stately_isolate_version"]}")
                implementation("co.touchlab:stately-iso-collections:${ext["stately_isolate_version"]}")
                api(project(":test-utils", ""))
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
