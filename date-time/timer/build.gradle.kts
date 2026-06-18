plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "datetime.timer"
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true

    supportJVM = true
    supportJS = true
    supportWasmJS = true

    dependencies {
        common {
            main {
                implementation(project(":base:core", ""))
                implementation(project(":base:state", ""))
            }
            test {
                implementation(project(":base:test", ""))
            }
        }
    }
}
