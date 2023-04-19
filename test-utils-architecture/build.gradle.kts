plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("org.jmailen.kotlinter")
    id("kotlinx-atomicfu")
}

publishableComponent("test.architecture")

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":test-utils-base"))
                api(project(":architecture"))
            }
        }
        commonTest {
            dependencies { }
        }
    }
}
