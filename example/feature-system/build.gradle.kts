plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.compose.get().pluginId)
}

kaluga {
    moduleName = "example.feature.system"
    supportMacOS = true
    supportWasmJS = true
    appleFramework {
        export("com.splendo.kaluga:system:${project.rootProject.version}")
    }
    dependencies {
        common {
            main {
                api(project(":core-arch"))
                api("com.splendo.kaluga:system:${project.rootProject.version}")
            }
        }
    }
}
