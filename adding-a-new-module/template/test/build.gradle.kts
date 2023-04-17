plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("org.jlleitschuh.gradle.ktlint")
}

publishableComponent("%PACKAGE%")

dependencies { }

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":test-utils-base"))
                implementation(project("%BASEMODULE%"))
            }
        }
        commonTest {
            dependencies {}
        }
    }
}
