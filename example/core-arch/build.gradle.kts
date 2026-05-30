plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.compose.get().pluginId)
}

kaluga {
    moduleName = "example.core.arch"
    supportMacOS = true
    dependencies {
        android {
            main {
                api(libs.koin.compose.viewmodel)
            }
        }
        common {
            main {
                api("com.splendo.kaluga:base:${project.rootProject.version}")
                api(libs.koin.core)
                api(libs.koin.compose)
                api(libs.koin.compose.viewmodel)
                api(libs.compose.foundation)
                api(libs.compose.material3)
                api(libs.compose.ui)
                api(libs.compose.navigation)
                api(libs.compose.lifecycle.viewmodel)
            }
        }
    }
}
