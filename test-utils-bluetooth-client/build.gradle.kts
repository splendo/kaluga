plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.bluetooth.client"
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true
    dependencies {
        common {
            main {
                api(project(":test-utils-bluetooth-base"))
                api(project(":bluetooth-client"))
            }
        }
    }
}
