plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "bluetooth.test.server"
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
