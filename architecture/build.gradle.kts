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
    val ext = (gradle as ExtensionAware).extra
    val kotlin_version: String by ext
    val androidx_lifecycle_version: String by ext
    val androidx_browser_version: String by ext

    api("org.jetbrains.kotlin:kotlin-reflect:${Library.kotlinVersion}")
    expose(Dependencies.AndroidX.Lifecycle.Runtime)
    expose(Dependencies.AndroidX.Lifecycle.ViewModel)
    expose(Dependencies.AndroidX.Lifecycle.LiveData)
    implement(Dependencies.AndroidX.Browser)
}

kotlin {
    sourceSets {
        val ext = (gradle as ExtensionAware).extra
        val serialization_version: String by ext

        getByName("commonMain") {
            dependencies {
                implementation(project(":base", ""))
                expose(Dependencies.KotlinX.Serialization.Core)
                expose(Dependencies.KotlinX.Serialization.Json)
            }
        }

        getByName("commonTest") {
            dependencies {
                api(project(":test-utils-base", ""))
            }
        }
    }
}
