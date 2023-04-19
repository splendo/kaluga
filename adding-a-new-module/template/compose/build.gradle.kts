plugins {
    id("com.android.library")
    kotlin("android")
    id("jacoco")
    id("convention.publication")
    id("org.jetbrains.dokka")
    id("org.jmailen.kotlinter")
}

composeAndroidComponent("%PACKAGE%")

dependencies {
    implementation(project(":base"))
    implementation(project("%BASEMODULE%"))
}
