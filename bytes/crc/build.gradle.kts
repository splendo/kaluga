plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "bytes.crc"

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
                implementation(project(":bytes:core", ""))
                implementation(project(":bytes:strings", ""))
            }
        }
    }
}
