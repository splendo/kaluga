plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jlleitschuh.gradle.ktlint")
    id("kotlinx-atomicfu")
}

publishableComponent()

dependencies {
    implement(Dependencies.KotlinX.AtomicFu)
}

kotlin {
    sourceSets {
        getByName("commonMain") {

            dependencies {
                api(project(":logging", ""))
                api(project(":base", ""))
            }
        }
        getByName("commonTest") {
            dependencies {
                api(project(":test-utils-permissions", ""))
            }
        }
    }
}
