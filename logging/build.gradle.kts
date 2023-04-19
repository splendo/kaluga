plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("com.android.library")
    id("convention.publication")
    id("org.jetbrains.dokka")
    id("org.jmailen.kotlinter")
    id("kotlinx-atomicfu")
}

publishableComponent("logging")

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
