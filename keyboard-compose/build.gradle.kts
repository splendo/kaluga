plugins {
    id("com.android.library")
    kotlin("android")
    id("jacoco")
    id("convention.publication")
    id("org.jlleitschuh.gradle.ktlint")
}

composeAndroidComponent()

dependencies {
    implementation(project(":base"))
    api(project(":keyboard"))
    implement(Dependencies.AndroidX.Compose.UI)
    implement(Dependencies.AndroidX.Compose.UITooling)
    implement(Dependencies.KotlinX.Coroutines.Core)
}
