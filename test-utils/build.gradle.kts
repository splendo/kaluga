plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("org.jlleitschuh.gradle.ktlint")
}

publishableComponent()

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":test-utils-alerts"))
                api(project(":test-utils-architecture"))
                api(project(":test-utils-base"))
                api(project(":test-utils-bluetooth"))
                api(project(":test-utils-hud"))
                api(project(":test-utils-keyboard"))
                api(project(":test-utils-koin"))
                api(project(":test-utils-location"))
                api(project(":test-utils-media"))
                api(project(":test-utils-permissions"))
                api(project(":test-utils-resources"))
                api(project(":test-utils-system"))
            }
        }
        commonTest {
            dependencies { }
        }
    }
}
