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
                implementationDependency(Dependencies.Napier)
                implementationDependency(Dependencies.Stately.Concurrency)
            }
        }
        commonTest {
            dependencies {
                implementationDependency(Dependencies.Stately.Isolate)
                implementationDependency(Dependencies.Stately.IsoCollections)
                api(project(":test-utils-base", ""))
            }
        }
    }
}
