plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "bytes.annotations"

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
                api(project(":bytes:strings", ""))
                api(project(":bytes:stuffing", ""))
                api(libs.kotlinx.serialization.core)
            }
        }
    }
}
