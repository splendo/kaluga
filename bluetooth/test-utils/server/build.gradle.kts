plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.bluetooth.server"
    supportMacOS = true
    dependencies {
        common {
            main {
                api(project(":bluetooth:test-core"))
                api(project(":bluetooth:server"))
            }
        }
    }
}
