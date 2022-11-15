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
    implement(Dependencies.Android.PlayServices.Location)
    implement(Dependencies.KotlinX.Coroutines.PlayServices)
    implement(Dependencies.KotlinX.AtomicFu)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
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
