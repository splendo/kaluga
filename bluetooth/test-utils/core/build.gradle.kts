plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.bluetooth.base"
    supportJS = true
    supportWasmJS = true
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true
    dependencies {
        common {
            main {
                api(project(":base:test"))
                api(project(":service:test"))
                api(project(":bluetooth:core"))
            }
        }
    }
}
