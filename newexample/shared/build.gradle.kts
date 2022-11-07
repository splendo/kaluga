plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("jacoco")
    id("org.jlleitschuh.gradle.ktlint")
}

commonComponent()

kotlin {
    sourceSets {
        getByName("commonMain") {
            dependencies {
                val libraryVersion = Library.version
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