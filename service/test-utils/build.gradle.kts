plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "service.test"
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true

    supportJVM = true
    supportJS = true
    supportWasmJS = true

    dependencies {
        common {
            main {
                api(project(":service:service"))
                api(project(":base:test"))
            }
        }
    }
}
