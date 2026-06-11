plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.compose.get().pluginId)
    alias(libs.plugins.jetbrains.compose)
}

kaluga {
    moduleName = "example.core.arch"
    supportJVM = true
    supportMacOS = true
    supportWasmJS = true
    dependencies {
        android {
            main {
                api(libs.koin.compose.viewmodel)
            }
        }
        common {
            main {
                api("com.splendo.kaluga:base:${project.rootProject.version}")
                api(libs.koin.core)
                api(libs.koin.compose)
                api(libs.koin.compose.viewmodel)
                api(libs.compose.foundation)
                api(libs.compose.material3)
                api(libs.compose.ui)
                api(libs.compose.navigation)
                api(libs.compose.lifecycle.viewmodel)
            }
        }
    }
}

// Bundles the Material Icons font (used to override `LocalIconSet` so the icon buttons render with the
// Material glyphs on every platform) as a Compose resource.
compose.resources {
    publicResClass = true
    packageOfResClass = "com.splendo.kaluga.example.arch.generated.resources"
}

dependencies {
    "commonMainImplementation"(compose.components.resources)
}

// ktlint (lintKotlin/formatKotlin) would otherwise scan the generated Compose resource accessors.
tasks.withType<org.jmailen.gradle.kotlinter.tasks.ConfigurableKtLintTask>().configureEach {
    exclude { it.file.path.contains("/generated/") }
}
