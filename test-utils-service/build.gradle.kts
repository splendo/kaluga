plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jlleitschuh.gradle.ktlint")
}

/* Multiplatform component */
publishableComponent()

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
