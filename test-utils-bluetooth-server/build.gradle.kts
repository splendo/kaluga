plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.bluetooth.server"
    supportMacOS = true
    dependencies {
        common {
            main {
                api(project(":test-utils-bluetooth-base"))
                api(project(":bluetooth-server"))
            }
        }
    }
}
