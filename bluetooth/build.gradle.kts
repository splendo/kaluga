plugins {
    id("com.splendo.kaluga.plugin")
    alias(libs.plugins.kotlin.serialization)
}

kaluga {
    moduleName = "bluetooth"
    supportMacOS = true
    dependencies {
        common {
            main {
                api(project(":bluetooth-base"))
                api(project(":bluetooth-client"))
                api(project(":bluetooth-server"))
                implementation(project(":service"))
            }
        }
    }
}
