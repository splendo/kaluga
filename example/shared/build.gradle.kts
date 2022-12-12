plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("jacoco")
    id("com.android.library")
    id("org.jlleitschuh.gradle.ktlint")
}

apply(from = "../../gradle/component.gradle.kts")

kotlin {
    sourceSets {
        commonMain {
            val ext = (gradle as ExtensionAware).extra

            dependencies {
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
                api("com.splendo.kaluga:permissions:$libraryVersion")
            }
        }
    }
}
