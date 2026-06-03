plugins {
    id("com.splendo.kaluga.plugin")
    alias(libs.plugins.kotlin.serialization)
    id(libs.plugins.compose.get().pluginId)
}

kaluga {
    moduleName = "example.feature.links"
    supportMacOS = true
    appleFramework {
        export("com.splendo.kaluga:links:${project.rootProject.version}")
    }
    dependencies {
        common {
            main {
                api(project(":core-arch"))
                api("com.splendo.kaluga:links:${project.rootProject.version}")
            }
        }
    }
}
