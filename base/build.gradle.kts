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

kotlin {
    sourceSets {

        val ext = (gradle as ExtensionAware).extra

        getByName("commonMain") {
            dependencies {
                implementation(project(":logging", ""))
                api("co.touchlab:stately-common:${ext["stately_version"]}")
                api("co.touchlab:stately-isolate:${ext["stately_isolate_version"]}")
                api("co.touchlab:stately-iso-collections:${ext["stately_isolate_version"]}")
                api("co.touchlab:stately-concurrency:${ext["stately_version"]}")
            }
        }
        getByName("jsMain") {
            dependencies {
                implementation(kotlin("stdlib-common", "${ext["kotlin_version"]}"))
                // JavaScript BigDecimal lib based on native BigInt
                implementation(npm("@splendo/bigdecimal", "${ext["js_bigdecimal_version"]}"))
            }
        }
        getByName("jsTest") {
            dependencies {
                api(kotlin("test-js"))
            }
        }
        getByName("commonTest") {
            dependencies {
                implementation(project(":test-utils-base", ""))
            }
        }
    }
}
