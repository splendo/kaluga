plugins {
    id("com.splendo.kaluga.plugin")
    alias(libs.plugins.kotlin.serialization)
    id(libs.plugins.compose.get().pluginId)
}

kaluga {
    moduleName = "example.feature.bluetooth.client"
    supportMacOS = true
    supportWasmJS = true
    dependencies {
        common {
            main {
                api(project(":core-arch"))
                api(project(":core-koin"))
                api(project(":feature-bluetooth-base"))
                api(project(":feature-permissions"))
                api("com.splendo.kaluga.bluetooth:client:${project.rootProject.version}")
            }
        }
    }
}
