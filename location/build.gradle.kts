plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("org.jmailen.kotlinter")
    id("kotlinx-atomicfu")
}

publishableComponent("location")

dependencies {
    implementationDependency(Dependencies.Android.PlayServices.Location)
    implementationDependency(Dependencies.KotlinX.Coroutines.PlayServices)
    implementationDependency(Dependencies.KotlinX.AtomicFu)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":service"))
                api(project(":location-permissions", ""))
            }
        }
        commonTest {
            dependencies {
                implementation(project(":test-utils-location", ""))
            }
        }
    }
}
