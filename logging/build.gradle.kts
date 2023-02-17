plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("com.android.library")
    id("convention.publication")
    id("org.jetbrains.dokka")
    id("org.jlleitschuh.gradle.ktlint")
    id("kotlinx-atomicfu")
}

publishableComponent()

dependencies {
    implementationDependency(Dependencies.KotlinX.AtomicFu)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementationDependency(Dependencies.Napier)
            }
        }
        commonTest {
            dependencies {
                api(project(":test-utils-base", ""))
            }
        }
    }
}
