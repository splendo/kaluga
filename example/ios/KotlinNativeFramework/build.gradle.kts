plugins {
    kotlin("multiplatform")
    kotlin("xcode-compat") version "0.2.3"
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/pocketbyte/hydra/")
}

kotlin {
    xcode {
        setupFramework("KotlinNativeFramework")
    }

    sourceSets {
        getByName("KotlinNativeFrameworkMain") {

            val ext = (gradle as ExtensionAware).extra

            val singleSet = ext["ios_one_sourceset"] as Boolean
            var iosArch = ext["ios_arch"]
            if (singleSet)
                iosArch = "ios"

            dependencies {
                implementation(project(":shared", "${iosArch}Default"))
            }
        }
    }
}