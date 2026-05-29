plugins {
    id("com.splendo.kaluga.plugin")
    alias(libs.plugins.kotlin.serialization)
    id(libs.plugins.compose.get().pluginId)
}

val modules = listOf(
    "alerts" to true,
    "architecture" to true,
    "base" to false,
    "bluetooth" to false,
    "beacons" to false,
    "date-time" to false,
    "date-time-picker" to true,
    "hud" to true,
    "keyboard" to true,
    "links" to true,
    "location" to false,
    "logging" to false,
    "media" to true,
    "resources" to true,
    "review" to true,
    "scientific" to false,
    "scientific-converters" to false,
    "system" to true,
    "permissions" to true
)

kaluga {
    moduleName = "example.shared"
    appleFramework {
        baseName = "KalugaExampleShared"
        isStatic = false
        transitiveExport = true
        modules.forEach { (module, isExportable) ->
            if (isExportable) {
                export("com.splendo.kaluga:$module:${project.rootProject.version}")
            }
        }
    }
    dependencies {
        android {
            main {
                api(libs.koin.compose.viewmodel)
            }
        }
        common {
            main {
                modules.forEach { (module, _) ->
                    api("com.splendo.kaluga:$module:${project.rootProject.version}")
                }
                api(libs.koin.core)
                api(libs.compose.foundation)
                api(libs.compose.material3)
                api(libs.compose.ui)
                api(libs.compose.navigation)
                api(libs.compose.lifecycle.viewmodel)
                api(libs.koin.compose)
                api(libs.koin.compose.viewmodel)
            }
        }
    }
}
