plugins {
    id("com.splendo.kaluga.plugin")
    alias(libs.plugins.kotlin.serialization)
    id(libs.plugins.compose.get().pluginId)
}

kaluga {
    moduleName = "example.feature.info"
    supportMacOS = true
    dependencies {
        common {
            main {
                api(project(":core-arch"))
            }
        }
    }
}
