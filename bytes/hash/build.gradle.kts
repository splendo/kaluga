plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "bytes.hash"

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
