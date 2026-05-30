plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.compose.get().pluginId)
}

kaluga {
    moduleName = "example.feature.scientific"
    supportMacOS = true
    // No `appleFramework {}` exports for scientific units — they're an implementation detail
    // consumed only by `ScientificScreen` (a Compose composable used inside Kotlin). Keeping them
    // off the framework's public Obj-C surface also avoids the `+pascal` selector collision with
    // clang's `pascal` calling-convention keyword.
    dependencies {
        common {
            main {
                api(project(":core-arch"))
                implementation("com.splendo.kaluga:scientific:${project.rootProject.version}")
                implementation("com.splendo.kaluga:scientific-converters:${project.rootProject.version}")
            }
        }
    }
}
