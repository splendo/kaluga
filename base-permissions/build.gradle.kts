plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jlleitschuh.gradle.ktlint")
    id("kotlinx-atomicfu")
}

publishableComponent()

kotlin {
    sourceSets {
        getByName("commonMain") {

            dependencies {
                api(project(":logging", ""))
                api(project(":base", ""))
                implement(Dependencies.KotlinX.AtomicFu)
            }
        }
        getByName("commonTest") {
            dependencies {
                api(project(":test-utils-permissions", ""))
            }
        }
    }
}
