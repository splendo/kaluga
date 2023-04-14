plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("org.jlleitschuh.gradle.ktlint")
}

publishableComponent("test.service")

dependencies { }

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":service"))
                api(project(":test-utils-base"))
            }
        }
        commonTest {
            dependencies {
            }
        }
    }
}
