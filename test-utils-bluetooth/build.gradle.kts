plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.bluetooth"
    supportMacOS = true
    dependencies {
        common {
            main {
                api(project(":test-utils-permissions"))
                api(project(":test-utils-service"))
                api(project(":bluetooth"))
            }
        }
    }
}
