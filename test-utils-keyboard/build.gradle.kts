plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jlleitschuh.gradle.ktlint")
}

publishableComponent()

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":test-utils-base"))
                implementation(project(":architecture"))
                api(project(":keyboard"))
            }
        }
        commonTest {
            dependencies {
                implementation(project(":test-utils-architecture"))
            }
        }
    }
}
