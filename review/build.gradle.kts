plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jlleitschuh.gradle.ktlint")
}

publishableComponent()

dependencies {
    implement(Dependencies.Android.Play.Core)
    implement(Dependencies.Android.Play.CoreKtx)
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
