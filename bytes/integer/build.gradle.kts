plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "bytes.integer"

    supportJVM = true
    supportJS = true
    supportWasmJS = true
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true

    dependencies {
        common {
            main {
                api(project(":bytes:core", ""))
            }
            test {
                implementation(project(":base:test", ""))
            }
        }
    }
}
