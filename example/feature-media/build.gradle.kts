plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.compose.get().pluginId)
}

kaluga {
    moduleName = "example.feature.media"
    supportMacOS = true
    supportWasmJS = true
    dependencies {
        common {
            main {
                api(project(":core-arch"))
                api("com.splendo.kaluga.media:media:${project.rootProject.version}")
                api("com.splendo.kaluga.media:compose:${project.rootProject.version}")
            }
        }
    }
}
