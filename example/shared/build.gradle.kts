plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("jacoco")
    id("com.android.library")
    id("org.jlleitschuh.gradle.ktlint")
}

repositories {
    mavenLocal()
    mavenCentral()
    google()
}

apply(from = "../../gradle/component.gradle")

kotlin {
    sourceSets {
        commonMain {
            val ext = (gradle as ExtensionAware).extra

            dependencies {
                if (!(ext["exampleAsRoot"] as Boolean)) {
                    implementation(project(":alerts", ""))
                    implementation(project(":architecture", ""))
                    implementation(project(":base", ""))
                    implementation(project(":date-time-picker", ""))
                    implementation(project(":hud", ""))
                    implementation(project(":keyboard", ""))
                    implementation(project(":links", ""))
                    implementation(project(":location", ""))
                    implementation(project(":logging", ""))
                    implementation(project(":base-permissions"))
                    implementation(project(":location-permissions"))
                    implementation(project(":bluetooth-permissions"))
                    implementation(project(":camera-permissions"))
                    implementation(project(":contacts-permissions"))
                    implementation(project(":microphone-permissions"))
                    implementation(project(":storage-permissions"))
                    implementation(project(":notifications-permissions"))
                    implementation(project(":calendar-permissions"))
                    implementation(project(":resources", ""))
                    implementation(project(":review", ""))
                    implementation(project(":system", ""))
                } else {
                    val libraryVersion = ext["library_version"]
                    implementation("com.splendo.kaluga:alerts:$libraryVersion")
                    implementation("com.splendo.kaluga:architecture:$libraryVersion")
                    implementation("com.splendo.kaluga:base:$libraryVersion")
                    implementation("com.splendo.kaluga:date-time-picker:$libraryVersion")
                    implementation("com.splendo.kaluga:hud:$libraryVersion")
                    implementation("com.splendo.kaluga:keyboard:$libraryVersion")
                    implementation("com.splendo.kaluga:links:$libraryVersion")
                    implementation("com.splendo.kaluga:location:$libraryVersion")
                    implementation("com.splendo.kaluga:logging:$libraryVersion")
                    implementation("com.splendo.kaluga:base-permissions:$libraryVersion")
                    implementation("com.splendo.kaluga:location-permissions:$libraryVersion")
                    implementation("com.splendo.kaluga:bluetooth-permissions:$libraryVersion")
                    implementation("com.splendo.kaluga:camera-permissions:$libraryVersion")
                    implementation("com.splendo.kaluga:contacts-permissions:$libraryVersion")
                    implementation("com.splendo.kaluga:microphone-permissions:$libraryVersion")
                    implementation("com.splendo.kaluga:storage-permissions:$libraryVersion")
                    implementation("com.splendo.kaluga:notifications-permissions:$libraryVersion")
                    implementation("com.splendo.kaluga:calendar-permissions:$libraryVersion")
                    implementation("com.splendo.kaluga:resources:$libraryVersion")
                    implementation("com.splendo.kaluga:review:$libraryVersion")
                    implementation("com.splendo.kaluga:system:$libraryVersion")
                }
            }
        }
    }
}
