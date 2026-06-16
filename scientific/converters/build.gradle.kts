plugins {
    id("com.splendo.kaluga.plugin")
    alias(libs.plugins.kotlin.serialization)
}

kaluga {
    moduleName = "scientific.converter"
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true

    supportJVM = true
    supportJS = true
    supportWasmJS = true

    dependencies {
        common {
            main {
                implementation(project(":base:base"))
                implementation(project(":scientific:scientific"))
            }
            test {
                implementation(project(":base:test"))
            }
        }
    }
}
