plugins {
    id("com.splendo.kaluga.plugin")
    alias(libs.plugins.kotlin.serialization)
    id(libs.plugins.compose.get().pluginId)
}

kaluga {
    moduleName = "example.feature.bluetooth.server"
    supportMacOS = true
    dependencies {
        common {
            main {
                api(project(":core-arch"))
                api(project(":core-koin"))
                api(project(":feature-bluetooth-base"))
                api(project(":feature-permissions"))
                api("com.splendo.kaluga.bluetooth:server:${project.rootProject.version}")
                // `scientific` only used inside the server screen (BeatsPerMinute literals).
                // `implementation` keeps unit classes out of the framework's public header.
                implementation("com.splendo.kaluga.scientific:scientific:${project.rootProject.version}")
            }
        }
    }
}
