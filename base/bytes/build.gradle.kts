plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "base.bytes"

    supportJVM = true
    supportJS = true
    supportWasmJS = true
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true

    dependencies {
        common {
            test {
                implementation(project(":base:test", ""))
            }
        }
    }
}
