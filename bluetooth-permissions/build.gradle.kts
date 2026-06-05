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
                api(project(":location-permissions", ""))
            }
        }
        common {
            main {
                api(project(":base-permissions", ""))
            }
            test {
                implementation(project(":test-utils-base", ""))
            }
        }
    }
}
