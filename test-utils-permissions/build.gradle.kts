plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.permissions"
    supportJS = true
    supportWasmJS = true
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true
    dependencies {
        common {
            main {
                api(project(":test-utils-base"))
                api(project(":base-permissions"))
            }
        }
    }
}
