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

kotlin {
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    js {
        nodejs()
    }

    sourceSets {
        val ext = (gradle as ExtensionAware).extra

        val commonMain by getting {
            dependencies {
                implementation(project(":logging", ""))
                api("co.touchlab:stately-common:${ext["stately_version"]}")
                api("co.touchlab:stately-isolate:${ext["stately_isolate_version"]}")
                api("co.touchlab:stately-iso-collections:${ext["stately_isolate_version"]}")
                api("co.touchlab:stately-concurrency:${ext["stately_version"]}")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(project(":test-utils", ""))
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by getting {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }

        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by getting {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
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
    }
}
