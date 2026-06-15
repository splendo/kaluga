plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.compose.get().pluginId)
}

kaluga {
    moduleName = "example.feature.beacons"
    supportMacOS = true
    dependencies {
        common {
            main {
                api(project(":core-arch"))
                api(project(":feature-bluetooth-client"))
                api("com.splendo.kaluga.bluetooth:beacons:${project.rootProject.version}")
            }
        }
    }
}
