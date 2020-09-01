plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.3.72"
    id("jacoco")
    id("maven-publish")
    id("com.android.library")
    id("org.jlleitschuh.gradle.ktlint")
}

repositories {
    mavenLocal()
    mavenCentral()
    google()
    jcenter()
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
                    implementation(project(":hud", ""))
                    implementation(project(":keyboard", ""))
                    implementation(project(":location", ""))
                    implementation(project(":logging", ""))
                    implementation(project(":permissions", ""))
                    implementation(project(":resources", ""))
                    implementation(project(":salesforce", ""))
                } else {
                    val libraryVersion = ext["library_version"]
                    implementation("com.splendo.kaluga:alerts:$libraryVersion")
                    implementation("com.splendo.kaluga:architecture:$libraryVersion")
                    implementation("com.splendo.kaluga:base:$libraryVersion")
                    implementation("com.splendo.kaluga:hud:$libraryVersion")
                    implementation("com.splendo.kaluga:keyboard:$libraryVersion")
                    implementation("com.splendo.kaluga:location:$libraryVersion")
                    implementation("com.splendo.kaluga:logging:$libraryVersion")
                    implementation("com.splendo.kaluga:permissions:$libraryVersion")
                    implementation("com.splendo.kaluga:resources:$libraryVersion")
                    implementation("com.splendo.kaluga:salesforce:$libraryVersion")
                }
            }
        }
    }
}