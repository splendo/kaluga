plugins {
    id("com.splendo.kaluga.plugin")
    alias(libs.plugins.kotlin.serialization)
}

val modules = listOf(
    "alerts" to true,
    "architecture" to true,
    "beacons" to false,
    "date-time-picker" to true,
    "hud" to true,
    "keyboard" to true,
    "media" to true,
    "resources" to true,
    "review" to true,
)

kaluga {
    moduleName = "example.mobileshared"
    appleFramework {
        baseName = "KalugaMobileShared"
        isStatic = false
        transitiveExport = true
        modules.forEach { (module, isExportable) ->
            if (isExportable) {
                export("com.splendo.kaluga:$module:${project.rootProject.version}")
            }
        }
        export(project(":shared"))
    }
    dependencies {
        android {
            main {
                api(libs.koin.compose.viewmodel)
            }
        }
        common {
            main {
                api(project(":shared"))
                modules.forEach { (module, _) ->
                    api("com.splendo.kaluga:$module:${project.rootProject.version}")
                }
            }
        }
    }
}
