plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.compose.get().pluginId)
}

kaluga {
    moduleName = "example.feature.localization"
    supportMacOS = true
    supportWasmJS = true
    dependencies {
        common {
            main {
                api(project(":core-arch"))
                api("com.splendo.kaluga.base:base:${project.rootProject.version}")
            }
        }
    }
}
