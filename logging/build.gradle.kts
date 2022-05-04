plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("com.android.library")
    id("convention.publication")
    id("org.jlleitschuh.gradle.ktlint")
}

val ext = (gradle as ExtensionAware).extra

apply(from = "../gradle/publishable_component.gradle")

group = "com.splendo.kaluga"
version = ext["library_version"]!!

kotlin {
    js {
        nodejs()
    }

    sourceSets {
        commonMain {
            val ext = (gradle as ExtensionAware).extra
            dependencies {
                implementation("io.github.aakira:napier:${ext["napier_version"]}")
                implementation("co.touchlab:stately-concurrency:${ext["stately_version"]}")
            }
        }
        commonTest {
            dependencies {
                val ext = (gradle as ExtensionAware).extra
                // Stately Isolite is in flux and not part of the current statelyVersion. Upgrade this when tracked properly
                implementation("co.touchlab:stately-isolate:${ext["stately_isolate_version"]}")
                implementation("co.touchlab:stately-iso-collections:${ext["stately_isolate_version"]}")
                api(project(":test-utils", ""))
            }
        }
    }
}
