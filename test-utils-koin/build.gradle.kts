plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jlleitschuh.gradle.ktlint")
}

val ext = (gradle as ExtensionAware).extra

apply(from = "../gradle/publishable_component.gradle.kts")

group = "com.splendo.kaluga"
version = ext["library_version"]!!

dependencies { }

kotlin {
    sourceSets {
        commonMain {
            val ext = (gradle as ExtensionAware).extra
            dependencies {
                api(project(":test-utils-base"))
                api(project(":test-utils-architecture"))
                api("io.insert-koin:koin-core:" + ext["koin_version"])
            }
        }
        commonTest {
            dependencies {
            }
        }
    }
}
