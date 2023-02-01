plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("org.jlleitschuh.gradle.ktlint")
}

/* Multiplatform component */
publishableComponent()
/* Compose component */
composeAndroidComponent()

dependencies { }

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":base"))
            }
        }
        commonTest {
            dependencies {
                implementation(project(":test-utils-base"))
            }
        }
    }
}
