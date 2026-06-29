plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "permissions.bluetooth"
    supportJS = true
    supportWasmJS = true
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true
    dependencies {
        android {
            main {
                api(project(":permissions:location", ""))
            }
        }
        common {
            main {
                api(project(":permissions:core", ""))
            }
            test {
                implementation(project(":base:test", ""))
            }
        }
    }
}
