plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("org.jlleitschuh.gradle.ktlint")
}

publishableComponent()

dependencies {
    implementationDependency(Dependencies.Android.Play.Core)
    implementationDependency(Dependencies.Android.Play.CoreKtx)
}

kotlin {
    sourceSets {
        getByName("commonMain") {
            dependencies {
                implementation(project(":architecture", ""))
                implementation(project(":base", ""))
                implementation(project(":logging", ""))
            }
        }
    }
}
