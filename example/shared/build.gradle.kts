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
        getByName("iosMain") {
            val ext =  (gradle as ExtensionAware).extra

            println("properties ${ext.properties}")

            val singleSet = ext["ios_one_sourceset"] as Boolean
            var iosArch = ext["ios_arch"]

            if (singleSet)
                iosArch = "ios"


            dependencies {

                // architecture specific dependency seems only needed when importing another project.
                // when importing through Maven in the commonMain module it is not needed.

                if (!(ext["exampleAsRoot"] as Boolean)) {
                    implementation(project(":Components", "${iosArch}Default"))

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


