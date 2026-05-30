plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.compose.get().pluginId)
}

kaluga {
    moduleName = "example.feature.media"
    supportMacOS = true
    dependencies {
        common {
            main {
                api(project(":core-arch"))
                api("com.splendo.kaluga:media:${project.rootProject.version}")
                api("com.splendo.kaluga:media-compose:${project.rootProject.version}")
                api("com.splendo.kaluga:lifecycle-compose:${project.rootProject.version}")
            }
        }
    }
}
