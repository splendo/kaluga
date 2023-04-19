plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("org.jmailen.kotlinter")
    id("kotlinx-atomicfu")
}

publishableComponent("permissions.base")

dependencies {
    implementationDependency(Dependencies.KotlinX.AtomicFu)
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
