import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    // Applied without a version: the Kotlin Multiplatform plugin is already on the build classpath
    // via the root `com.splendo.kaluga.plugin`, so re-declaring a version would clash.
    id("org.jetbrains.kotlin.multiplatform")
    id(libs.plugins.compose.get().pluginId)
    // The JetBrains Compose Gradle plugin is only needed by this executable module: it wires the
    // Skiko Wasm runtime (`skiko.mjs`/`skiko.wasm`) into the webpack bundle. The library modules
    // get by with just the compose-compiler plugin since they never bundle for the browser.
    id("org.jetbrains.compose") version "1.11.1"
}

group = "com.splendo.kaluga"
version = libs.versions.kaluga.get()

@OptIn(ExperimentalWasmDsl::class)
kotlin {
    wasmJs {
        outputModuleName.set("kalugaExampleWeb")
        browser {
            commonWebpackConfig {
                outputFileName = "kalugaExampleWeb.js"
            }
        }
        binaries.executable()
    }

    sourceSets {
        wasmJsMain.dependencies {
            implementation(project(":core-arch"))
            implementation(project(":core-koin"))
            implementation(project(":feature-localization"))
            implementation(project(":feature-datetime"))
            implementation(project(":feature-scientific"))
            implementation(libs.compose.ui)
        }
    }
}
