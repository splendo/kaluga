plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("com.android.library")
    id("convention.publication")
    id("org.jetbrains.dokka")
    id("org.jmailen.kotlinter")
}

publishableComponent("keyboard")

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
                api(project(":test-utils-keyboard", ""))
            }
        }
    }
}
