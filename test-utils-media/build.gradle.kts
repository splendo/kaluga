plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("org.jmailen.kotlinter")
}

publishableComponent("test.media")

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":media"))
                api(project(":test-utils-architecture"))
            }
        }
        commonTest {
            dependencies {
            }
        }
    }
}
