plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("maven-publish")
    id("com.android.library")
}

repositories {
    mavenLocal()
    mavenCentral()
    google()
    jcenter()
    maven("https://dl.bintray.com/pocketbyte/hydra/")
}

apply(from = "../../gradle/component.gradle")

kotlin {
    sourceSets {
        getByName("commonMain") {
            val ext =  (gradle as ExtensionAware).extra
            var primaryIosArch = ext["ios_primary_arch"]

            dependencies {

                if (!(ext["exampleAsRoot"] as Boolean)) {
                    implementation(project(":location", ""))
                    implementation(project(":base", ""))
                    implementation(project(":logging", ""))
                    implementation(project(":alerts", ""))
                    implementation(project(":keyboard", ""))
                    implementation(project(":permissions", ""))
                    implementation(project(":loadingIndicator", ""))
                } else {
                    val libraryVersion = ext["library_version"]
                    implementation("com.splendo.kaluga:location:$libraryVersion")
                    implementation("com.splendo.kaluga:base:$libraryVersion")
                    implementation("com.splendo.kaluga:logging:$libraryVersion")
                    implementation("com.splendo.kaluga:alerts:$libraryVersion")
                    implementation("com.splendo.kaluga:keyboard:$libraryVersion")
                    implementation("com.splendo.kaluga:permissions:$libraryVersion")
                    implementation("com.splendo.kaluga:loadingIndicator:$libraryVersion")
                }
            }
        }
    }
}