plugins {
    id("com.android.library")
    kotlin("android")
    id("jacoco")
    id("convention.publication")
    id("org.jetbrains.dokka")
    id("org.jlleitschuh.gradle.ktlint")
}

composeAndroidComponent()

dependencies {
    implementation(project(":base"))
    api(project(":keyboard"))
    api(project(":architecture-compose"))
}
