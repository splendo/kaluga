plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.compose.get().pluginId)
}

kaluga {
    moduleName = "media.compose"
    supportMacOS = true
    // wasmJs only: Compose Multiplatform's web target does not include the Kotlin/JS (IR) target.
    supportWasmJS = true
    dependencies {
        common {
            main {
                api(project(":media:media"))
                api(libs.compose.ui)
                api(libs.compose.foundation)
            }
        }
    }
}
