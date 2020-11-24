plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("maven-publish")
    id("com.android.library")
    id("org.jlleitschuh.gradle.ktlint")
}

val ext = (gradle as ExtensionAware).extra

apply(from = "../gradle/publishable_component.gradle")

group = "com.splendo.kaluga"
version = ext["library_version"]!!

val androidx_arch_core_testing_version = ext["androidx_arch_core_testing_version"]!!

repositories {
    maven("https://dl.bintray.com/ekito/koin")
}

dependencies {
    implementation("androidx.arch.core:core-testing:$androidx_arch_core_testing_version")
}

kotlin {
    sourceSets {
        getByName("commonMain") {
            dependencies {
                val ext = (gradle as ExtensionAware).extra

                // these are not coming from component.gradle because they need to be in the main scope
                api(kotlin("test"))
                api(kotlin("test-junit"))

                // we don't want these to be automatically be imported,
                // test sourcesets already depend on a main sourceset which should have the right dependencies
                // Making this `implementation` or `api` will increase linking times for every test build.
                // unfortunately Kotlin/Native does not support compileOnly (yet?), this generates a warning

                compileOnly(project(":alerts", ""))
                compileOnly(project(":architecture", ""))
                compileOnly(project(":base", ""))
                compileOnly(project(":hud", ""))
                compileOnly(project(":keyboard", ""))
                compileOnly(project(":logging", ""))
                compileOnly(project(":permissions", ""))
                compileOnly("org.koin:koin-core:" + ext["koin_version"])
            }
        }

        getByName("commonTest") {
            dependencies {
                val ext = (gradle as ExtensionAware).extra

                // we need this to test since in commonMain these are compileOnly
                implementation("org.koin:koin-core:" + ext["koin_version"])
                implementation(project(":architecture", ""))
                implementation(project(":alerts", ""))
                implementation(project(":base", ""))
            }
        }
        getByName("jsMain") {
            dependencies {
                api(kotlin("test-js"))
            }
        }

        getByName("jvmMain") {
            val ext = (gradle as ExtensionAware).extra

            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-test:${ext["kotlinx_coroutines_version"]}")
            }
        }
    }
}

android {
    dependencies {
        val ext = (gradle as ExtensionAware).extra
        api("org.jetbrains.kotlinx:kotlinx-coroutines-test:${ext["kotlinx_coroutines_version"]}")
    }
}
