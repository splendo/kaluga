plugins {
    id("com.splendo.kaluga.plugin")
    alias(libs.plugins.kotlin.serialization)
}

kaluga {
    moduleName = "bytes.serialization"

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
                api(libs.kotlinx.serialization.core)
            }
            test {
                implementation(project(":base:test", ""))
                implementation(project(":scientific:scientific"))
            }
        }
    }
}
