buildscript {

    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

plugins {
    kotlin("multiplatform")
    kotlin("xcode-compat") version "0.2.5"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven(url="https://kotlin.bintray.com/kotlinx")
}

kotlin {

    xcode {
        setupFramework("KotlinNativeFramework") {
            export(project(":shared"))
            transitiveExport = true
            freeCompilerArgs += "-Xobjc-generics"
        }
    }

    sourceSets {
        getByName("KotlinNativeFrameworkMain") {

            val ext = (gradle as ExtensionAware).extra
            val primaryIosArch = ext["ios_primary_arch"]

            dependencies {
                api(project(":shared", "${primaryIosArch}Default"))
            }
        }
    }
}
