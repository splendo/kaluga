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

            dependencies {

                if (!(ext["exampleAsRoot"] as Boolean)) {
                    implementation(project(":Components", ""))
                    implementation(project(":logging", ""))
                    implementation(project(":alerts", "${primaryIosArch}Default"))
                    implementation(project(":permissions", "${primaryIosArch}Default"))
                } else {
                    val libraryVersion = ext["library_version"]
                    implementation("com.splendo.kaluga:Components:$libraryVersion")
                    implementation("com.splendo.kaluga:logging:$libraryVersion")
                    implementation("com.splendo.kaluga:alerts-$primaryIosArch:$libraryVersion")
                    implementation("com.splendo.kaluga:permissions-$primaryIosArch:$libraryVersion")
                }
            }
        }
    }
}