plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "base.crc"

    supportJVM = true
    supportJS = true
    supportWasmJS = true
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true

    dependencies {
        common {
            test {
                implementation(project(":base:base", ""))
                implementation(project(":base:test", ""))
            }
        }
    }
}
