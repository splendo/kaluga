plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("jacoco")
    id("convention.publication")
    id("org.jetbrains.dokka")
    id("org.jlleitschuh.gradle.ktlint")
}

databindingAndroidComponent("%PACKAGE%")

dependencies {
    implementation(project(":base"))
    implementation(project("%BASEMODULE%"))
}
