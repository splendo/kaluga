import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id(libs.plugins.kotlin.multiplatform.get().pluginId)
    id(libs.plugins.compose.get().pluginId)
    alias(libs.plugins.jetbrains.compose)
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
            // `:shared` aggregates every feature whose Kaluga library supports wasmJs (via
            // `sharedFeaturesModule`) and provides the web `initKoin`; it api-exposes core-arch/core-koin.
            implementation(project(":shared"))
            implementation(libs.compose.ui)
        }
    }
}
