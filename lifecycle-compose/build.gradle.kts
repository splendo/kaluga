plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.compose.get().pluginId)
}

kaluga {
    moduleName = "lifecycle.compose"
    supportMacOS = true
    supportWasmJS = true
    dependencies {
        common {
            main {
                api(project(":lifecycle"))
                api(libs.compose.ui)
                api(libs.compose.lifecycle.viewmodel)
            }
        }
    }
}
