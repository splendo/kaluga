plugins {
    id("com.splendo.kaluga.plugin")
    alias(libs.plugins.kotlin.serialization)
    id(libs.plugins.compose.get().pluginId)
}

kaluga {
    moduleName = "example.feature.permissions"
    supportMacOS = true
    appleFramework {
        export("com.splendo.kaluga:permissions:${project.rootProject.version}")
    }
    dependencies {
        android {
            main {
                api(libs.koin.compose.viewmodel)
                api("com.splendo.kaluga:architecture:${project.rootProject.version}")
            }
        }
        common {
            main {
                api(project(":core-arch"))
                api(project(":core-koin"))
                api("com.splendo.kaluga:permissions:${project.rootProject.version}")
            }
        }
    }
}
