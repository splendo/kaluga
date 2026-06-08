plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.kotlinx.atomicfu.get().pluginId)
    alias(libs.plugins.kotlin.serialization)
}

kaluga {
    moduleName = "bluetooth.base"
    supportJS = true
    supportWasmJS = true
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true
    dependencies {
        android {
            main {
                implementation(libs.kotlinx.atomicfu)
            }
        }
        ios {
            main {
                implementation(libs.kotlinx.atomicfu)
            }
        }
        common {
            main {
                api(project(":base:base"))
                api(project(":service:service"))
                api(libs.kotlinx.serialization.core)
            }
            test {
                implementation(project(":bluetooth:test:core", ""))
            }
        }
    }
}
