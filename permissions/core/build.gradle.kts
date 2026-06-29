plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.kotlinx.atomicfu.get().pluginId)
}

kaluga {
    moduleName = "permissions.base"
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
                api(project(":logging", ""))
                api(project(":base:core", ""))
                api(project(":base:state", ""))
            }
            test {
                implementation(project(":permissions:test", ""))
            }
        }
    }
}
