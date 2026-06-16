plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.compose.get().pluginId)
}

kaluga {
    moduleName = "example.feature.localization"
    supportJVM = true
    supportMacOS = true
    supportWasmJS = true
    dependencies {
        common {
            main {
                api(project(":core-arch"))
                api("com.splendo.kaluga.base:i18n:${project.rootProject.version}")
                api("com.splendo.kaluga.base:formatting:${project.rootProject.version}")
                api("com.splendo.kaluga.date-time:date-time:${project.rootProject.version}")
            }
        }
    }
}
