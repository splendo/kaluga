plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "lifecycle.test"
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true
    supportJVM = true
    supportJS = true
    supportWasmJS = true
    dependencies {
        common {
            main {
                api(project(":lifecycle:lifecycle"))
                api(project(":base:test"))
            }
        }
    }
}
