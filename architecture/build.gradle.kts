plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("jacoco")
    id("com.android.library")
    id("convention.publication")
    id("org.jlleitschuh.gradle.ktlint")
    id("kotlinx-atomicfu")
}

publishableComponent()

dependencies {
    api("org.jetbrains.kotlin:kotlin-reflect:${Library.kotlinVersion}")
    expose(Dependencies.AndroidX.Lifecycle.Runtime)
    expose(Dependencies.AndroidX.Lifecycle.ViewModel)
    expose(Dependencies.AndroidX.Lifecycle.LiveData)
    implement(Dependencies.AndroidX.Browser)
}

kotlin {
    sourceSets {

        getByName("commonMain") {
            dependencies {
                implementation(project(":base", ""))
                expose(Dependencies.KotlinX.Serialization.Core)
                expose(Dependencies.KotlinX.Serialization.Json)
                implement(Dependencies.KotlinX.AtomicFu)
            }
        }

        getByName("commonTest") {
            dependencies {
                api(project(":test-utils-base", ""))
            }
        }
    }
}
