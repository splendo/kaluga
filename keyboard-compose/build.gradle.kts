plugins {
    id("com.android.library")
    kotlin("android")
    id("jacoco")
    id("convention.publication")
    id("org.jetbrains.dokka")
    id("org.jmailen.kotlinter")
}

composeAndroidComponent("keyboard.compose")

dependencies {
    implementation(project(":base"))
    api(project(":keyboard"))
    api(project(":architecture-compose"))
}
