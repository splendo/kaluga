plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.bluetooth.base"
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true
    dependencies {
        common {
            main {
                api(project(":test-utils-base"))
                api(project(":test-utils-service"))
                api(project(":bluetooth-base"))
            }
        }
    }
}
