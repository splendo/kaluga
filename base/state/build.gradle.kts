plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.kotlinx.atomicfu.get().pluginId)
}

kaluga {
    moduleName = "base.state"

    supportJVM = true
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
        common {
            main {
                implementation(project(":base:core", ""))
            }
            test {
                implementation(project(":base:test", ""))
            }
        }
        js {
            main {
                api(libs.kotlinx.atomicfu)
            }
        }
    }
}
