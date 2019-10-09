plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("maven-publish")
    id("com.android.library")
}

repositories {
    mavenCentral()
    google()
    jcenter()
    maven("https://dl.bintray.com/pocketbyte/hydra/")
}

apply(from = "../../gradle/component.gradle")

kotlin {
    sourceSets {
        getByName("iosMain") {
            val ext =  (gradle as ExtensionAware).extra

            println("properties ${ext.properties}")

            val singleSet = ext["ios_one_sourceset"] as Boolean
            var iosArch = ext["ios_arch"]
            val orgArch = iosArch

            if (singleSet)
                iosArch = "ios"

            dependencies {

                if (!(ext["exampleAsRoot"] as Boolean)) {
                    implementation(project(":Components", "${iosArch}Default"))
                } else {
                    val libraryVersion = ext["library_version"]
                    implementation("com.splendo.kaluga:Components-$orgArch:$libraryVersion")
                }

            }
        }
        getByName("commonMain") {
            val ext =  (gradle as ExtensionAware).extra

            dependencies {

                if (!(ext["exampleAsRoot"] as Boolean)) {
                    implementation(project(":Components", ""))
                    implementation(project(":logging", ""))
                } else {
                    val libraryVersion = ext["library_version"]
                    implementation("com.splendo.kaluga:Components:$libraryVersion")
                }

            }
        }
    }
}


