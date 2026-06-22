plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.compose.get().pluginId)
}

kaluga {
    moduleName = "example.feature.datetime"
    supportJVM = true
    supportMacOS = true
    supportWasmJS = true
    dependencies {
        common {
            main {
                api(project(":core-arch"))
                api("com.splendo.kaluga.date-time:date-time:${project.rootProject.version}")
                api("com.splendo.kaluga.date-time:timer:${project.rootProject.version}")
            }
        }
    }
}
