plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.compose.get().pluginId)
}

kaluga {
    moduleName = "example.feature.scientific"
    supportMacOS = true
    appleFramework {
        export("com.splendo.kaluga:scientific:${project.rootProject.version}")
        export("com.splendo.kaluga:scientific-converters:${project.rootProject.version}")
    }
    dependencies {
        common {
            main {
                api(project(":core-arch"))
                api("com.splendo.kaluga:scientific:${project.rootProject.version}")
                api("com.splendo.kaluga:scientific-converters:${project.rootProject.version}")
            }
        }
    }
}
