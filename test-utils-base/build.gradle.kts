plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jlleitschuh.gradle.ktlint")
}

val ext = (gradle as ExtensionAware).extra

apply(from = "../gradle/publishable_component.gradle")

group = "com.splendo.kaluga"
version = ext["library_version"]!!

val androidx_arch_core_testing_version = ext["androidx_arch_core_testing_version"]!!

dependencies {
    api("androidx.arch.core:core-testing:$androidx_arch_core_testing_version")
}

kotlin {
    js {
        nodejs()
    }

    sourceSets {
        commonMain {
            dependencies {
                // these are not coming from component.gradle because they need to be in the main scope
                api(kotlin("test"))
                api(kotlin("test-junit"))

                // these dependencies make test linking slow, but Kotlin/Native cannot handle `compileOnly`
                // https://github.com/splendo/kaluga/issues/208
                api(project(":base", ""))
                api(project(":logging", ""))
            }
        }

        getByName("androidLibMain") {
        }

        getByName("jsMain") {
            dependencies {
                api(kotlin("test-js"))
            }
        }

        getByName("jvmMain") {
            val ext = (gradle as ExtensionAware).extra

            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-test:" + ext["kotlinx_coroutines_version"])
                api("org.jetbrains.kotlinx:kotlinx-coroutines-debug:" + ext["kotlinx_coroutines_version"])
            }
        }
    }
}

android {
    dependencies {
        val ext = (gradle as ExtensionAware).extra
        api("org.jetbrains.kotlinx:kotlinx-coroutines-test:" + ext["kotlinx_coroutines_version"])
        api("org.jetbrains.kotlinx:kotlinx-coroutines-debug:" + ext["kotlinx_coroutines_version"])
    }
}
