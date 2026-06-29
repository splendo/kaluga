plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.compose.get().pluginId)
}

kaluga {
    moduleName = "example.feature.location"
    supportMacOS = true
    supportWasmJS = true
    dependencies {
        common {
            main {
                api(project(":core-arch"))
                api(project(":core-koin"))
                api(project(":feature-permissions"))
                api("com.splendo.kaluga.location:location:${project.rootProject.version}")
            }
        }
    }
}
