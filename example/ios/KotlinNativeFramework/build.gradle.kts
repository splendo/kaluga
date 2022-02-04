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
    id("org.jlleitschuh.gradle.ktlint")
}

kotlin {

    xcode {
        setupFramework("KotlinNativeFramework") {

            export(project(":shared"))

            transitiveExport = true
        }
    }

    sourceSets {
        getByName("KotlinNativeFrameworkMain") {
            dependencies {
                api(project(":shared"))
            }
        }
    }
}

tasks.create<Delete>("cleanKotlinNativeFrameworkTest") {
    delete = setOf(
        "build"
    )
}
