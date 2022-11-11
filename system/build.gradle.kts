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
                implementation(project(":base", ""))
                implementation(project(":logging", ""))
            }
        }

        commonTest {
            dependencies {
                implementation(project(":test-utils-system", ""))
            }
        }
    }
}
