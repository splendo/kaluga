plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("org.jmailen.kotlinter")
}

publishableComponent("test.bluetooth")

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":test-utils-permissions"))
                api(project(":test-utils-service"))
                api(project(":bluetooth"))
            }
        }
        commonTest {
            dependencies { }
        }
    }
}
