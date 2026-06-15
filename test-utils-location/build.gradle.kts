plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.location"
    supportJS = true
    supportWasmJS = true
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true
    dependencies {
        common {
            main {
                api(project(":location"))
                api(project(":test-utils-permissions"))
                api(project(":test-utils-service"))
            }
        }
    }
}
