import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetPreset
import org.jetbrains.kotlin.gradle.tasks.CInteropProcess

val ext =  (gradle as ExtensionAware).extra

group = "com.splendo.kaluga"
version = ext["library_version"]!!

repositories {
    google()
    jcenter()
}

plugins {
    id("jacoco")
    id("maven-publish")
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
}

android {

    compileSdkVersion(28)

    defaultConfig {
        minSdkVersion(16)
        targetSdkVersion(28)
    }

    sourceSets {
        val main by getting {
            manifest.srcFile("src/androidLibMain/AndroidManifest.xml")
            res.srcDir("src/androidLibMain/resources")
        }
        val androidTest by getting {
            manifest.srcFile("src/androidLibAndroidTest/AndroidManifest.xml")
            java.srcDir("src/androidLibAndroidTest/kotlin")
            res.srcDir("src/androidLibAndroidTest/res")
        }
    }
}

kotlin {

    data class IosTarget(val name: String, val preset: String, val id: String)

    val iosTargets = listOf(
        IosTarget("ios", "iosArm64", "ios-arm64"),
        IosTarget("iosSim", "iosX64", "ios-x64")
    )

    android {
        publishAllLibraryVariants()
    }

    for ((targetName, presetName, id) in iosTargets) {
        targetFromPreset(presets.getByName<KotlinNativeTargetPreset>(presetName), targetName) {
            compilations {
                val main by getting {
                    val carthageBuildDir = "$projectDir/src/iosMain/native/Carthage/Build/iOS"
                    cinterops(Action {
                        val eddystone by creating {
                            defFile("src/iosMain/native/eddystone.def")
                            includeDirs("$carthageBuildDir/Eddystone.framework/Headers")
                            extraOpts("-verbose")
                        }
                    })
                }
            }
            mavenPublication {
                artifactId = "${project.name}-$id"
            }
        }
    }

    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation(kotlin("reflect"))

                implementation("com.bugsnag:bugsnag-android:4.11.0")
            }
        }

        val iosMain by getting {

        }

        val iosSimMain by getting {
            dependsOn(iosMain)
        }

    }

}

// Create Carthage tasks
listOf("bootstrap", "update").forEach { type ->
    task<Exec>("carthage${type.capitalize()}") {
        group = "carthage"
        executable = "carthage"
        args(
            type,
            "--project-directory", "src/iosMain/native",
            "--platform", "iOS",
            "--no-use-binaries" // Provided binaries are sometimes problematic, remove this to speedup process
            //"--cache-builds"
        )
    }
}

// Make CInterop tasks depend on Carthage
tasks.withType(CInteropProcess::class) {
    dependsOn("carthageBootstrap")
}

// Delete build directory on clean
tasks.named<Delete>("clean") {
    delete(buildDir)
}

// Temporary workaround for https://youtrack.jetbrains.com/issue/KT-27170
configurations.create("compileClasspath")