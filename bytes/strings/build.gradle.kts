plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "bytes.strings"

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
                api(project(":bytes:stuffing", ""))
            }
            test {
                implementation(project(":base:test", ""))
            }
        }
    }
}
