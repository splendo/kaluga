buildscript {

    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

plugins {
    kotlin("multiplatform")
    id("org.jlleitschuh.gradle.ktlint")
}

kotlin {

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "KotlinNativeFramework"
            export(project(":shared"))
            transitiveExport = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":shared"))
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
    }
}

tasks.create<Delete>("cleanKotlinNativeFrameworkTest") {
    delete = setOf(
        "build"
    )
}
