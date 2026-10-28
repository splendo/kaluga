plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.kotlinx.atomicfu.get().pluginId)
    alias(libs.plugins.kotlin.serialization)
}

kaluga {
    moduleName = "bluetooth.core"
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
                api(project(":base:core"))
                api(project(":bytes:core"))
                api(project(":bytes:strings"))
                implementation(project(":bytes:integer"))
                implementation(project(":bytes:medfloat"))
                implementation(project(":bytes:crc"))
                implementation(project(":bytes:stuffing"))
                implementation(project(":bytes:hash"))
                implementation(project(":base:formatting"))
                api(project(":service:service"))
                api(libs.kotlinx.serialization.core)
                api(libs.kotlinx.coroutines.core)
            }
            test {
                implementation(project(":bluetooth:test-core", ""))
                implementation(project(":scientific:scientific"))
            }
        }
    }
}
