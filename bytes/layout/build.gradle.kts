plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "bytes.layout"

    supportJVM = true
    supportJS = true
    supportWasmJS = true
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true

    dependencies {
        common {
            main {
                api(project(":bytes:annotations", ""))
                api(project(":bytes:core", ""))
                api(project(":bytes:integer", ""))
                api(project(":bytes:medfloat", ""))
                api(project(":bytes:strings", ""))
                api(project(":bytes:stuffing", ""))
                api(project(":bytes:crc", ""))
            }
            test {
                implementation(project(":base:test", ""))
            }
        }
    }
}

kotlin {
    sourceSets.all {
        languageSettings.optIn("com.splendo.kaluga.bytes.layout.InternalBytesApi")
    }
}
