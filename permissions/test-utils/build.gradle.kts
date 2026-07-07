plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "permissions.test"
    supportJS = true
    supportWasmJS = true
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true
    dependencies {
        common {
            main {
                api(project(":base:test"))
                api(project(":permissions:core"))
            }
        }
    }
}
