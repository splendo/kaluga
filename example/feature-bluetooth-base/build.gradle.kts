plugins {
    id("com.splendo.kaluga.plugin")
    alias(libs.plugins.kotlin.serialization)
}

kaluga {
    moduleName = "example.feature.bluetooth.base"
    supportMacOS = true
    dependencies {
        common {
            main {
                api("com.splendo.kaluga:bluetooth-base:${project.rootProject.version}")
            }
        }
    }
}
