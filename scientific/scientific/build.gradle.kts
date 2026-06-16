plugins {
    id("com.splendo.kaluga.plugin")
    alias(libs.plugins.kotlin.serialization)
}

kaluga {
    moduleName = "scientific"

    supportJVM = true
    supportJS = true
    supportWasmJS = true
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true

    dependencies {
        common {
            main {
                implementation(project(":base:core"))
                api(project(":base:i18n"))
                api(project(":base:formatting"))
                api(libs.kotlinx.serialization.core)
            }
            test {
                implementation(project(":base:test"))
                implementation(libs.kotlinx.serialization.json)
            }
        }
    }
}
