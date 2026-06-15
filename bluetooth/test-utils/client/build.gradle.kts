plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "bluetooth.test.client"
    supportJS = true
    supportWasmJS = true
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true
    dependencies {
        common {
            main {
                api(project(":bluetooth:test-core"))
                api(project(":bluetooth:client"))
            }
        }
    }
}
