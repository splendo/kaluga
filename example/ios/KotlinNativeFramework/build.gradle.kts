plugins {
    kotlin("multiplatform")
    kotlin("xcode-compat") version "0.2.3"
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    maven(url="https://kotlin.bintray.com/kotlinx")
}

kotlin {
    xcode {
        setupFramework("KotlinNativeFramework")
    }

    sourceSets {
        getByName("KotlinNativeFrameworkMain") {

            val ext = (gradle as ExtensionAware).extra
            var primaryIosArch = ext["ios_primary_arch"]

            dependencies {
                implementation(project(":shared", "${primaryIosArch}Default"))
            }
        }
    }
}