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
            implementation(project(":core-arch"))
            implementation(project(":core-koin"))
            implementation(project(":feature-localization"))
            implementation(project(":feature-datetime"))
            implementation(project(":feature-location"))
            implementation(project(":feature-permissions"))
            implementation(project(":feature-bluetooth-client"))
            implementation(project(":feature-scientific"))
            implementation(libs.compose.ui)
        }
    }
}
