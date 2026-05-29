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
        // Deliberately NOT exporting `:shared` here: doing so dragged
        // `kaluga.scientific.unit.Pascal` into mobileshared's Objective-C header, and
        // `+ (instancetype)pascal` collides with clang's reserved `pascal` calling-convention
        // keyword. iOS Swift code links KalugaExampleShared.framework separately for the
        // macOS-capable types, so re-exporting them from mobileshared would be redundant anyway.
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
                api(project(":shared"))
                modules.forEach { (module, _) ->
                    api("com.splendo.kaluga:$module:${project.rootProject.version}")
                }
            }
        }
    }
}
