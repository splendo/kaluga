plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("org.jmailen.kotlinter")
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
