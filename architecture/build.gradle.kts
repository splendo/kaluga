plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("jacoco")
    id("com.android.library")
    id("convention.publication")
    id("org.jetbrains.dokka")
    id("org.jmailen.kotlinter")
    id("kotlinx-atomicfu")
}

publishableComponent("architecture")

dependencies {
    apiDependency(Dependencies.AndroidX.Lifecycle.Runtime)
    apiDependency(Dependencies.AndroidX.Lifecycle.ViewModel)
    apiDependency(Dependencies.AndroidX.Lifecycle.LiveData)
    apiDependency(Dependencies.Android.Material)
    implementationDependency(Dependencies.AndroidX.Browser)
    implementationDependency(Dependencies.KotlinX.AtomicFu)
}

kotlin {
    sourceSets {
        getByName("commonMain") {
            dependencies {
                implementation(project(":base", ""))
                apiDependency(Dependencies.KotlinX.Serialization.Core)
                apiDependency(Dependencies.KotlinX.Serialization.Json)
            }
        }

        getByName("commonTest") {
            dependencies {
                api(project(":test-utils-architecture", ""))
            }
        }
    }
}
