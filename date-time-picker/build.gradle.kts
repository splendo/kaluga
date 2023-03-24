plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("org.jlleitschuh.gradle.ktlint")
    id("com.github.psxpaul.execfork") version "0.2.2"
}

publishableComponent()

dependencies {
    androidTestImplementationDependency(Dependencies.AndroidX.Activity.Ktx)
}

task<com.github.psxpaul.task.ExecFork>("recordVideo") {
    executable = "./record_emulator.sh"
    args.add("video.mp4")
    workingDir = project.rootProject.rootDir
    waitForOutput = "FFmpeg developers"
}

kotlin {
    sourceSets {
        getByName("commonMain") {
            dependencies {
                implementation(project(":architecture", ""))
                implementation(project(":base", ""))
            }
        }
        getByName("commonTest") {
            dependencies {
                implementation(project(":test-utils-date-time-picker", ""))
            }
        }
    }
}
