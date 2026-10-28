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
                implementation(project(":base:core", ""))
                api(project(":bytes:annotations", ""))
                api(project(":bytes:core", ""))
                api(project(":bytes:layout", ""))
                api(project(":bytes:medfloat", ""))
                api(project(":bytes:strings", ""))
                api(project(":bytes:stuffing", ""))
                api(libs.kotlinx.serialization.core)
            }
            test {
                implementation(project(":base:test", ""))
                implementation(project(":bytes:crc", ""))
                implementation(project(":bytes:integer", ""))
                implementation(project(":scientific:scientific"))
            }
        }
    }
}

kotlin {
    sourceSets.all {
        languageSettings.optIn("com.splendo.kaluga.bytes.layout.InternalBytesApi")
    }
}
