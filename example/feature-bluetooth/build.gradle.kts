plugins {
    id("com.splendo.kaluga.plugin")
    alias(libs.plugins.kotlin.serialization)
    id(libs.plugins.compose.get().pluginId)
}

kaluga {
    moduleName = "example.feature.bluetooth"
    supportMacOS = true
    dependencies {
        common {
            main {
                api(project(":core-arch"))
                api(project(":core-koin"))
                api(project(":feature-permissions"))
                api("com.splendo.kaluga:bluetooth:${project.rootProject.version}")
                api("com.splendo.kaluga:scientific:${project.rootProject.version}")
            }
        }
    }
}
