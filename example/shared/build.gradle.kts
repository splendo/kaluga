plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("jacoco")
    id("com.android.library")
    id("org.jlleitschuh.gradle.ktlint")
}

apply(from = "../../gradle/component.gradle")

kotlin {
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            val ext = (gradle as ExtensionAware).extra

            dependencies {
                if (!(ext["example_as_root"] as Boolean)) {
                    api(project(":alerts", ""))
                    api(project(":architecture", ""))
                    api(project(":base", ""))
                    api(project(":bluetooth", ""))
                    api(project(":beacons", ""))
                    api(project(":date-time-picker", ""))
                    api(project(":hud", ""))
                    api(project(":keyboard", ""))
                    api(project(":links", ""))
                    api(project(":location", ""))
                    api(project(":logging", ""))
                    api(project(":base-permissions", ""))
                    api(project(":location-permissions", ""))
                    api(project(":bluetooth-permissions", ""))
                    api(project(":camera-permissions", ""))
                    api(project(":contacts-permissions", ""))
                    api(project(":microphone-permissions", ""))
                    api(project(":storage-permissions", ""))
                    api(project(":notifications-permissions", ""))
                    api(project(":calendar-permissions", ""))
                    api(project(":resources", ""))
                    api(project(":review", ""))
                    api(project(":system", ""))
                } else {
                    val libraryVersion = ext["library_version"]
                    api("com.splendo.kaluga:alerts:$libraryVersion")
                    api("com.splendo.kaluga:architecture:$libraryVersion")
                    api("com.splendo.kaluga:base:$libraryVersion")
                    api("com.splendo.kaluga:bluetooth:$libraryVersion")
                    api("com.splendo.kaluga:beacons:$libraryVersion")
                    api("com.splendo.kaluga:date-time-picker:$libraryVersion")
                    api("com.splendo.kaluga:hud:$libraryVersion")
                    api("com.splendo.kaluga:keyboard:$libraryVersion")
                    api("com.splendo.kaluga:links:$libraryVersion")
                    api("com.splendo.kaluga:location:$libraryVersion")
                    api("com.splendo.kaluga:logging:$libraryVersion")
                    api("com.splendo.kaluga:resources:$libraryVersion")
                    api("com.splendo.kaluga:review:$libraryVersion")
                    api("com.splendo.kaluga:system:$libraryVersion")
                    api("com.splendo.kaluga:base-permissions:$libraryVersion")
                    api("com.splendo.kaluga:location-permissions:$libraryVersion")
                    api("com.splendo.kaluga:bluetooth-permissions:$libraryVersion")
                    api("com.splendo.kaluga:camera-permissions:$libraryVersion")
                    api("com.splendo.kaluga:contacts-permissions:$libraryVersion")
                    api("com.splendo.kaluga:microphone-permissions:$libraryVersion")
                    api("com.splendo.kaluga:storage-permissions:$libraryVersion")
                    api("com.splendo.kaluga:notifications-permissions:$libraryVersion")
                    api("com.splendo.kaluga:calendar-permissions:$libraryVersion")
                }
            }
        }

        val commonTest by getting

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
