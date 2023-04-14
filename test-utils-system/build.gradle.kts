plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("org.jlleitschuh.gradle.ktlint")
}

publishableComponent("test.system")

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":system"))
                api(project(":test-utils-base"))
            }
        }
        commonTest {
            dependencies {}
        }
    }
}
