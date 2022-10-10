plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("com.android.library")
    id("convention.publication")
    id("org.jlleitschuh.gradle.ktlint")
}

publishableComponent()

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implement(Dependencies.Napier)
                implement(Dependencies.Stately.Concurrency)
            }
        }
        commonTest {
            dependencies {
                implement(Dependencies.Stately.Isolate)
                implement(Dependencies.Stately.IsoCollections)
                api(project(":test-utils-base", ""))
            }
        }
    }
}
